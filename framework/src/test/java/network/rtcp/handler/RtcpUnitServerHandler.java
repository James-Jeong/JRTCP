package network.rtcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import network.definition.DestinationRecord;
import network.definition.NetAddress;
import network.rtcp.RtcpTest;
import network.rtcp.base.RtcpHeader;
import network.rtcp.base.RtcpPacketPaddingResult;
import network.rtcp.base.RtcpType;
import network.rtcp.module.MockWallClock;
import network.rtcp.packet.RtcpCompoundPacket;
import network.rtcp.packet.RtcpPacket;
import network.rtcp.type.RtcpReceiverReport;
import network.rtcp.type.RtcpSenderReport;
import network.rtcp.type.base.RtcpReportBlock;
import network.rtcp.unit.RtcpUnit;
import network.socket.GroupSocket;
import network.socket.SocketManager;
import network.socket.netty.NettyChannel;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RtcpUnitServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger logger = LoggerFactory.getLogger(RtcpUnitServerHandler.class);

    private final String id;

    private final SocketManager socketManager;
    private final RtcpUnit rtcpUnit;
    private final NetAddress myAddress;
    private final boolean isSender;

    ////////////////////////////////////////////////////////////////////////////////

    public RtcpUnitServerHandler(SocketManager socketManager, String id, RtcpUnit rtcpUnit, boolean isSender, NetAddress myAddress) {
        this.socketManager = socketManager;
        this.id = id;
        this.rtcpUnit = rtcpUnit;
        this.myAddress = myAddress;
        this.isSender = isSender;
        logger.debug("RtcpServerHandler is created. (id={})", id);
    }

    ////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void channelRead0 (ChannelHandlerContext ctx, DatagramPacket msg) {
        ByteBuf buf = msg.content();
        if (buf == null) {
            return;
        }

        try {
            int readableBytes = buf.readableBytes();
            if (readableBytes > 0) {
                byte[] data = new byte[readableBytes];
                buf.readBytes(data);
                int index = 0;

                // 맨 처음 헤더 읽어서 길이 알아내서 해당 바이트 수 만큼 읽기
                int loopCount = 0;
                int totalRemainDataLength;
                RtcpPacket firstRtcpPacket = null;
                List<RtcpPacket> rtcpPacketList = null;
                Map<Short, RtcpPacket> rtcpTypePositionMap = new LinkedHashMap<>();

                while (true) {
                    totalRemainDataLength = readableBytes - index;
                    logger.debug("[RtcpServerHandler<{}>] remainDataLength: [{}], index: [{}]", id, totalRemainDataLength, index);
                    if (totalRemainDataLength <= 0) { break; }

                    byte[] headerData = new byte[RtcpHeader.LENGTH_SDES];
                    System.arraycopy(data, index, headerData, 0, RtcpHeader.LENGTH_SDES);
                    RtcpHeader rtcpHeader = new RtcpHeader(headerData);
                    if (rtcpHeader.getPacketType() != RtcpType.SOURCE_DESCRIPTION) {
                        headerData = new byte[RtcpHeader.LENGTH];
                        System.arraycopy(data, index, headerData, 0, RtcpHeader.LENGTH);
                        rtcpHeader = new RtcpHeader(headerData);
                    }
                    index += headerData.length;
                    logger.debug("[RtcpServerHandler<{}>] RtcpHeader[{}]: \n[{}]\nindex: [{}]", id, headerData.length, rtcpHeader, index);

                    int packetLength = rtcpHeader.getLength();
                    if (packetLength <= 0) { break; }

                    int curRemainDataLength = RtcpPacket.getRemainBytesByPacketLength(
                            packetLength,
                            rtcpHeader.getPacketType() == RtcpType.SOURCE_DESCRIPTION
                    );
                    logger.debug("[RtcpServerHandler<{}>] totalDataLength: [{}], index: [{}], PacketLength: [{}], curRemainDataLength: [{}]", id, readableBytes, index, packetLength, curRemainDataLength);

                    byte[] remainData = new byte[curRemainDataLength]; // body + padding
                    System.arraycopy(data, index, remainData, 0, curRemainDataLength);
                    index += curRemainDataLength;

                    RtcpPacket rtcpPacket = new RtcpPacket(
                            rtcpHeader,
                            RtcpPacket.getRtcpFormatByByteData(rtcpHeader.getPacketType(), remainData)
                    );

                    if (loopCount > 0) {
                        if (rtcpPacketList == null) {
                            rtcpPacketList = new ArrayList<>();
                            rtcpPacketList.add(firstRtcpPacket);
                        }
                        rtcpPacketList.add(rtcpPacket);
                    }

                    if (loopCount == 0) {
                        firstRtcpPacket = rtcpPacket;
                    }

                    rtcpTypePositionMap.put((short) rtcpPacket.getRtcpHeader().getPacketType(), rtcpPacket);
                    logger.debug("[RtcpServerHandler<{}>] RtcpPacket: (size={}+{})\n{}", id, headerData.length, curRemainDataLength, rtcpPacket);
                    loopCount++;
                }

                RtcpCompoundPacket rtcpCompoundPacket = null;
                if (rtcpPacketList != null && !rtcpPacketList.isEmpty()) {
                    rtcpCompoundPacket = new RtcpCompoundPacket(rtcpPacketList);
                    logger.debug("[RtcpServerHandler<{}>] RtcpCompoundPacket: (size={})\n{}", id, rtcpCompoundPacket.getTotalRtcpPacketSize(), rtcpCompoundPacket);
                }

                // - Sender 는 ReceiverReport 를 수신하여 RTT 및 지터를 확인하여 송신 버퍼를 제어한다. (Sender 는 RTP 전송 시 주기적으로 SenderReport 를 전송한다.)
                // - Receiver 는 SenderReport 를 수신하여 RTT 및 지터를 확인하여 ReceiverReport 를 전송하고 수신 버퍼를 제어한다.
                if (rtcpCompoundPacket != null) {
                    if (isSender) {
                        RtcpPacket rrPacket = rtcpTypePositionMap.get(RtcpType.RECEIVER_REPORT);
                        if (rrPacket != null) {
                            logger.debug("[RtcpServerHandler<{}>] ReceiverReport: \n{}", id, rrPacket.getRtcpFormat());
                        }
                    } else {
                        RtcpPacket srPacket = rtcpTypePositionMap.get(RtcpType.SENDER_REPORT);
                        if (srPacket != null) {
                            logger.debug("[RtcpServerHandler<{}>] SenderReport: \n{}", id, srPacket.getRtcpFormat());
                            rtcpUnit.onReceiveSR((RtcpSenderReport) srPacket.getRtcpFormat());
                            logger.debug("[RtcpServerHandler<{}>] RtcpUnit: \n{}", id, rtcpUnit);

                            processSrPacket();
                        }
                    }
                } else if (firstRtcpPacket != null) {
                    if (isSender) {
                        if (firstRtcpPacket.getRtcpHeader().getPacketType() == RtcpType.RECEIVER_REPORT) {
                            logger.debug("[RtcpServerHandler<{}>] ReceiverReport: \n{}", id, firstRtcpPacket.getRtcpFormat());
                        }
                    } else {
                        if (firstRtcpPacket.getRtcpHeader().getPacketType() == RtcpType.SENDER_REPORT) {
                            logger.debug("[RtcpServerHandler<{}>] SenderReport: \n{}", id, firstRtcpPacket.getRtcpFormat());
                            rtcpUnit.onReceiveSR((RtcpSenderReport) firstRtcpPacket.getRtcpFormat());
                            logger.debug("[RtcpServerHandler<{}>] RtcpUnit: \n{}", id, rtcpUnit);

                            processSrPacket();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("[RtcpServerHandler<{}>] Fail to handle UDP Packet.", id, e);
        }
    }

    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) {
        //logger.warn("ServerHandler.exceptionCaught", cause);
        //ctx.close();
    }

    private void processSrPacket() {
        ////////////////////////////////////////////////////////////
        // RECEIVER REPORT TEST (SEND)
        //
        List<RtcpReportBlock> rtcpReportBlockList = new ArrayList<>();
        RtcpReportBlock source1 = new RtcpReportBlock(
                rtcpUnit.getSsrc(),                                 // SSRC
                (byte) rtcpUnit.getFractionLost(),                  // Fraction Lost
                (int) rtcpUnit.getCumulativeNumberOfPacketsLost(),  // Cumulative Number of Packets Lost
                rtcpUnit.getExtHighSequence(),                      // Extended High Sequence number
                rtcpUnit.getJitter(),                               // Inter-Arrival Jitter
                rtcpUnit.getLastSrTimestamp(),                      // Last SR TimeStamp
                rtcpUnit.getDelaySinceLastSrTimestamp()             // Delay since Last SR TimeStamp
        );
        rtcpReportBlockList.add(source1);
        //

        //
        RtcpReceiverReport rtcpReceiverReport = new RtcpReceiverReport(
                rtcpReportBlockList,            // Report Block List
                null        // Profile Specific Extensions > not used yet
        );
        byte[] rtcpReceiverReportData = rtcpReceiverReport.getData();
        //

        //
        RtcpPacketPaddingResult rtcpPacketPaddingResult = RtcpPacket.getPacketLengthByBytes(
                rtcpReceiverReportData.length, false
        );
        RtcpHeader rtcpHeader = new RtcpHeader(
                2, rtcpPacketPaddingResult,
                rtcpReceiverReport.getReportBlockList().size(), RtcpType.RECEIVER_REPORT,
                rtcpUnit.getSsrc()
        );
        RtcpPacket rtcpPacket = new RtcpPacket(rtcpHeader, rtcpReceiverReport);
        logger.debug("[RtcpServerHandler<{}>] [RtcpPacket:\n{}]", id, rtcpPacket);
        byte[] rtcpPacketData = rtcpPacket.getData();
        //

        //
        GroupSocket groupSocket = socketManager.getSocket(myAddress);
        if (groupSocket == null) {
            logger.warn("[RtcpServerHandler<{}>] GroupSocket is not found. (address={})", id, myAddress);
            return;
        }

        Map<Long, DestinationRecord> destinationRecordMap = groupSocket.getCloneDestinationMap();
        for (Map.Entry<Long, DestinationRecord> entry : destinationRecordMap.entrySet()) {
            if (entry == null) { continue; }
            DestinationRecord destinationRecord = entry.getValue();
            if (destinationRecord == null) { continue; }

            NettyChannel nettyChannel = destinationRecord.getNettyChannel();
            if (nettyChannel == null) { continue; }
            nettyChannel.openConnectChannel(
                    destinationRecord.getGroupEndpointId().getGroupAddress().getAddressString(),
                    destinationRecord.getGroupEndpointId().getGroupAddress().getPort()
            );
            nettyChannel.sendData(rtcpPacketData, rtcpPacketData.length);
        }
        //
        ////////////////////////////////////////////////////////////
    }

}
