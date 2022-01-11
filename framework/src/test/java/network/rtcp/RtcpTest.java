package network.rtcp;

import instance.BaseEnvironment;
import instance.DebugLevel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import network.ChannelHandlerMaker;
import network.definition.DestinationRecord;
import network.definition.NetAddress;
import network.rtcp.base.RtcpHeader;
import network.rtcp.base.RtcpPacketPaddingResult;
import network.rtcp.base.RtcpType;
import network.rtcp.handler.RtcpClientHandler;
import network.rtcp.handler.RtcpServerHandler;
import network.rtcp.module.CnameGenerator;
import network.rtcp.module.MockWallClock;
import network.rtcp.module.RtpClock;
import network.rtcp.module.SsrcGenerator;
import network.rtcp.packet.RtcpCompoundPacket;
import network.rtcp.packet.RtcpPacket;
import network.rtcp.type.RtcpReceiverReport;
import network.rtcp.type.base.RtcpReportBlock;
import network.rtcp.unit.RtcpUnit;
import network.rtp.RtpPacket;
import network.socket.GroupSocket;
import network.socket.SocketManager;
import network.socket.SocketProtocol;
import network.socket.netty.NettyChannel;
import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Assert;
import org.junit.Test;
import rtcp.RtcpPacketTest;
import service.ResourceManager;
import service.scheduler.schedule.ScheduleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RtcpTest {

    @Test
    public void test() {
        singleRtcpPacketTest();
        multiRtcpPacketTest();
        rtpStatisticsTest();
    }

    public void singleRtcpPacketTest() {
        ////////////////////////////////////////////////////////////
        // 인스턴스 생성
        BaseEnvironment baseEnvironment = new BaseEnvironment(
                new ScheduleManager(),
                new ResourceManager(5000, 7000),
                DebugLevel.DEBUG
        );
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // SocketManager 생성
        SocketManager socketManager = new SocketManager(
                baseEnvironment,
                false,
                10,
                500000,
                500000
        );
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // 소켓 생성 (GroupSocket1)
        NetAddress netAddress1 = new NetAddress("127.0.0.1", 5000,true, SocketProtocol.UDP);
        ChannelInitializer<NioDatagramChannel> serverChannelInitializer1 = ChannelHandlerMaker.get(
                new RtcpServerHandler("SENDER/" + netAddress1.getAddressString())
        );
        Assert.assertTrue(socketManager.addSocket(netAddress1, serverChannelInitializer1));
        GroupSocket groupSocket1 = socketManager.getSocket(netAddress1);
        Assert.assertNotNull(groupSocket1);
        // Listen channel open
        Assert.assertTrue(groupSocket1.getListenSocket().openListenChannel());
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // 소켓 생성 (GroupSocket2)
        NetAddress netAddress2 = new NetAddress("127.0.0.1", 6000,true, SocketProtocol.UDP);
        ChannelInitializer<NioDatagramChannel> serverChannelInitializer2 = ChannelHandlerMaker.get(
                new RtcpServerHandler("RECEIVER/" + netAddress1.getAddressString())
        );
        Assert.assertTrue(socketManager.addSocket(netAddress2, serverChannelInitializer2));
        GroupSocket groupSocket2 = socketManager.getSocket(netAddress2);
        Assert.assertNotNull(groupSocket2);
        // Listen channel open
        Assert.assertTrue(groupSocket2.getListenSocket().openListenChannel());
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // Destination 추가
        long sessionId = 1234;
        ChannelInitializer<NioDatagramChannel> clientChannelInitializer = ChannelHandlerMaker.get(new RtcpClientHandler());
        Assert.assertTrue(groupSocket1.addDestination(netAddress2, null, sessionId, clientChannelInitializer));
        baseEnvironment.printMsg("[RtcpTest][singleRtcpPacketTest] GROUP-SOCKET1: {%s}", groupSocket1.toString());
        baseEnvironment.printMsg("[RtcpTest][singleRtcpPacketTest] GROUP-SOCKET2: {%s}", groupSocket2.toString());
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // RTCP 송수신
        long ssrc = SsrcGenerator.generateSsrc();
        DestinationRecord destinationRecord = groupSocket1.getDestination(sessionId);
        Assert.assertNotNull(destinationRecord);
        NettyChannel nettyChannel = destinationRecord.getNettyChannel();
        nettyChannel.openConnectChannel(netAddress2.getAddressString(), netAddress2.getPort());

        RtcpPacket rtcpPacket = RtcpPacketTest.srCreationTest(ssrc);
        byte[] rtcpPacketData = rtcpPacket.getData();
        Assert.assertNotNull(rtcpPacketData);
        nettyChannel.sendData(rtcpPacketData, rtcpPacketData.length);
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // 소켓 삭제
        TimeUnit timeUnit = TimeUnit.SECONDS;
        try {
            timeUnit.sleep(1);
        } catch (InterruptedException e) {
            baseEnvironment.printMsg(DebugLevel.WARN, "[RtcpTest][singleRtcpPacketTest] SLEEP FAIL");
        }

        Assert.assertTrue(socketManager.removeSocket(netAddress1));
        Assert.assertTrue(socketManager.removeSocket(netAddress2));
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // 인스턴스 삭제
        baseEnvironment.stop();
        ////////////////////////////////////////////////////////////
    }

    public void multiRtcpPacketTest() {
        ////////////////////////////////////////////////////////////
        // 인스턴스 생성
        BaseEnvironment baseEnvironment = new BaseEnvironment(
                new ScheduleManager(),
                new ResourceManager(5000, 7000),
                DebugLevel.DEBUG
        );
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // SocketManager 생성
        SocketManager socketManager = new SocketManager(
                baseEnvironment,
                false,
                10,
                500000,
                500000
        );
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // 소켓 생성 (GroupSocket1)
        NetAddress netAddress1 = new NetAddress("127.0.0.1", 5000,true, SocketProtocol.UDP);
        ChannelInitializer<NioDatagramChannel> serverChannelInitializer1 = ChannelHandlerMaker.get(
                new RtcpServerHandler("SENDER/" + netAddress1.getAddressString())
        );
        Assert.assertTrue(socketManager.addSocket(netAddress1, serverChannelInitializer1));
        GroupSocket groupSocket1 = socketManager.getSocket(netAddress1);
        Assert.assertNotNull(groupSocket1);
        // Listen channel open
        Assert.assertTrue(groupSocket1.getListenSocket().openListenChannel());
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // 소켓 생성 (GroupSocket2)
        NetAddress netAddress2 = new NetAddress("127.0.0.1", 6000,true, SocketProtocol.UDP);
        ChannelInitializer<NioDatagramChannel> serverChannelInitializer2 = ChannelHandlerMaker.get(
                new RtcpServerHandler("RECEIVER/" + netAddress1.getAddressString())
        );
        Assert.assertTrue(socketManager.addSocket(netAddress2, serverChannelInitializer2));
        GroupSocket groupSocket2 = socketManager.getSocket(netAddress2);
        Assert.assertNotNull(groupSocket2);
        // Listen channel open
        Assert.assertTrue(groupSocket2.getListenSocket().openListenChannel());
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // Destination 추가
        long sessionId = 1234;
        ChannelInitializer<NioDatagramChannel> clientChannelInitializer = ChannelHandlerMaker.get(new RtcpClientHandler());
        Assert.assertTrue(groupSocket1.addDestination(netAddress2, null, sessionId, clientChannelInitializer));
        baseEnvironment.printMsg("[RtcpTest][multiRtcpPacketTest] GROUP-SOCKET1: {%s}", groupSocket1.toString());
        baseEnvironment.printMsg("[RtcpTest][multiRtcpPacketTest] GROUP-SOCKET2: {%s}", groupSocket2.toString());
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // RTCP 송수신
        long ssrc = SsrcGenerator.generateSsrc();
        DestinationRecord destinationRecord = groupSocket1.getDestination(sessionId);
        Assert.assertNotNull(destinationRecord);
        NettyChannel nettyChannel = destinationRecord.getNettyChannel();
        nettyChannel.openConnectChannel(netAddress2.getAddressString(), netAddress2.getPort());

        // 주의! RtcpPacket.getData() 함수로 바이트 데이터 가져와서 다시 패킷 객체 생성해서 보내면 패딩 바이트 모두 소멸됨
        RtcpPacket rtcpSrPacket = RtcpPacketTest.srCreationTest(ssrc);
        RtcpPacket rtcpSdesPacket = RtcpPacketTest.sdesCreationTest();
        RtcpPacket rtcpByePacket = RtcpPacketTest.byeCreationTest(ssrc);

        RtcpCompoundPacket rtcpCompoundPacket = new RtcpCompoundPacket();
        rtcpCompoundPacket.addRtcpPacketToListAt(0, rtcpSrPacket);
        rtcpCompoundPacket.addRtcpPacketToListAt(1, rtcpSdesPacket);
        rtcpCompoundPacket.addRtcpPacketToListAt(2, rtcpByePacket);
        baseEnvironment.printMsg("[RtcpTest][multiRtcpPacketTest] rtcpCompoundPacket: %s", rtcpCompoundPacket);

        byte[] rtcpCompoundPacketData = rtcpCompoundPacket.getData();
        Assert.assertNotNull(rtcpCompoundPacketData);
        nettyChannel.sendData(rtcpCompoundPacketData, rtcpCompoundPacketData.length);
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // 메시지 수신 대기 1초
        TimeUnit timeUnit = TimeUnit.SECONDS;
        try {
            timeUnit.sleep(1);
        } catch (InterruptedException e) {
            baseEnvironment.printMsg(DebugLevel.WARN, "[RtcpTest][multiRtcpPacketTest] SLEEP FAIL");
        }
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // 소켓 삭제
        Assert.assertTrue(socketManager.removeSocket(netAddress1));
        Assert.assertTrue(socketManager.removeSocket(netAddress2));
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // 인스턴스 삭제
        baseEnvironment.stop();
        ////////////////////////////////////////////////////////////
    }

    public void rtpStatisticsTest() {
        ////////////////////////////////////////////////////////////
        // 인스턴스 생성
        BaseEnvironment baseEnvironment = new BaseEnvironment(
                new ScheduleManager(),
                new ResourceManager(5000, 7000),
                DebugLevel.DEBUG
        );
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // CNAME 생성
        String cname = CnameGenerator.generateCname();
        baseEnvironment.printMsg("[RtcpTest][rtpStatisticsTest] CNAME: [%s]", cname);
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // RtcpUnit 생성
        long ssrc = SsrcGenerator.generateSsrc();
        int timeDelay = 20; // ms
        MockWallClock mockWallClock = new MockWallClock();
        RtpClock rtpClock = new RtpClock(mockWallClock);
        rtpClock.setClockRate(1000);

        RtcpUnit rtcpUnit = new RtcpUnit(
                rtpClock,
                ssrc,
                cname
        );
        baseEnvironment.printMsg("[RtcpTest][rtpStatisticsTest] RtcpUnit: \n%s", rtcpUnit);
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // RtpPacket 생성
        int packetCount = 10;
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        long curTime = TimeStamp.getCurrentTime().getSeconds();

        List<RtpPacket> rtpPacketList = new ArrayList<>();
        for (int i = 0 ; i < packetCount; i++) {
            RtpPacket rtpPacket = new RtpPacket();
            rtpPacket.setValue(
                    2, 0, 0, 0, 0, 8,
                    i + 1,
                    curTime,
                    ssrc,
                    data,
                    data.length
            );
            rtpPacketList.add(rtpPacket);
            //baseEnvironment.printMsg("[RtcpTest][rtpStatisticsTest] RtpPacket-%s: \n%s", i + 1, rtpPacket);
            curTime += timeDelay;
        }
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // RTP 통계 처리
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;

        // RTP Packet 수신 입장
        try {
            int i = 0;
            for (RtpPacket rtpPacket : rtpPacketList) {
                // RTP Packet 최초 수신 시 시간 동기화 필요 (최초 1회)
                if (i == 0) {
                    rtpClock.synchronize(rtpPacket.getTimeStamp());
                }
                rtcpUnit.onReceiveRtp(rtpPacket);

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
                        rtcpUnit.getDelaySincelastSrTimestamp()             // Delay since Last SR TimeStamp
                );
                rtcpReportBlockList.add(source1);
                //

                //
                RtcpReceiverReport rtcpReceiverReport = new RtcpReceiverReport(
                        rtcpReportBlockList,
                        null
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
                baseEnvironment.printMsg("[RtcpTest][rtpStatisticsTest] [RtcpPacket:\n%s]", rtcpPacket);
                //
                ////////////////////////////////////////////////////////////

                baseEnvironment.printMsg("[RtcpTest][rtpStatisticsTest] onReceiveRtp [RtpPacket:\n%s] \nRtcpUnit: \n%s", rtpPacket, rtcpUnit);
                timeUnit.sleep(timeDelay);
                mockWallClock.tick(timeDelay * 1000000); // 1 Milliseconds = 1000000 Nanoseconds
                i++;
            }
        } catch (Exception e) {
            // ignore
        }
        ////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        // 인스턴스 삭제
        baseEnvironment.stop();
        ////////////////////////////////////////////////////////////
    }

}
