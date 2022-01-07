package network.rtcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import network.rtcp.base.RtcpHeader;
import network.rtcp.packet.RtcpCompoundPacket;
import network.rtcp.packet.RtcpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RtcpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger logger = LoggerFactory.getLogger(RtcpServerHandler.class);

    private final String id;

    ////////////////////////////////////////////////////////////////////////////////

    public RtcpServerHandler(String id) {
        this.id = id;
        logger.debug("RtcpServerHandler is created.");
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
                int remainDataLength;
                List<RtcpPacket> rtcpPacketList = null;
                while (true) {
                    remainDataLength = readableBytes - index;
                    logger.debug("[RtcpServerHandler<{}>] remainDataLength: [{}], index: [{}]", id, remainDataLength, index);
                    if (remainDataLength <= 0) { break; }

                    byte[] headerData = new byte[RtcpHeader.LENGTH];
                    System.arraycopy(data, index, headerData, 0, RtcpHeader.LENGTH);
                    index += headerData.length;

                    RtcpHeader rtcpHeader = new RtcpHeader(headerData);
                    int packetLength = rtcpHeader.getLength();
                    if (packetLength <= 0) { break; }
                    logger.debug("[RtcpServerHandler<{}>] PacketLength: [{}]", id, packetLength);

                    int curRemainDataLength = RtcpPacket.getRemainBytesByPacketLength(packetLength);
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
                        }
                        rtcpPacketList.add(rtcpPacket);
                    }

                    logger.debug("[RtcpServerHandler<{}>] RtcpPacket: (size={}+{})\n{}", id, headerData.length, curRemainDataLength, rtcpPacket);
                    loopCount++;
                }

                if (rtcpPacketList != null && !rtcpPacketList.isEmpty()) {
                    RtcpCompoundPacket rtcpCompoundPacket = new RtcpCompoundPacket(rtcpPacketList);
                    logger.debug("[RtcpServerHandler<{}>] RtcpCompoundPacket: (size={})\n{}", id, rtcpCompoundPacket.getTotalRtcpPacketSize(), rtcpCompoundPacket);
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

}
