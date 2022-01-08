package network.rtcp;

import instance.BaseEnvironment;
import instance.DebugLevel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import network.ChannelHandlerMaker;
import network.definition.DestinationRecord;
import network.definition.NetAddress;
import network.rtcp.handler.RtcpClientHandler;
import network.rtcp.handler.RtcpServerHandler;
import network.rtcp.packet.rtcp.RtcpCompoundPacket;
import network.rtcp.packet.rtcp.RtcpPacket;
import network.socket.GroupSocket;
import network.socket.SocketManager;
import network.socket.SocketProtocol;
import network.socket.netty.NettyChannel;
import org.junit.Assert;
import org.junit.Test;
import rtcp.RtcpPacketTest;
import service.ResourceManager;
import service.scheduler.schedule.ScheduleManager;

import java.util.concurrent.TimeUnit;

public class RtcpTest {

    @Test
    public void test() {
        singleRtcpPacketTest();
        multiRtcpPacketTest();
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
        // TODO RTCP 송수신
        DestinationRecord destinationRecord = groupSocket1.getDestination(sessionId);
        Assert.assertNotNull(destinationRecord);
        NettyChannel nettyChannel = destinationRecord.getNettyChannel();
        nettyChannel.openConnectChannel(netAddress2.getAddressString(), netAddress2.getPort());

        RtcpPacket rtcpPacket = RtcpPacketTest.srCreationTest();
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
        DestinationRecord destinationRecord = groupSocket1.getDestination(sessionId);
        Assert.assertNotNull(destinationRecord);
        NettyChannel nettyChannel = destinationRecord.getNettyChannel();
        nettyChannel.openConnectChannel(netAddress2.getAddressString(), netAddress2.getPort());

        // 주의! RtcpPacket.getData() 함수로 바이트 데이터 가져와서 다시 패킷 객체 생성해서 보내면 패딩 바이트 모두 소멸됨
        RtcpPacket rtcpSrPacket = RtcpPacketTest.srCreationTest();
        RtcpPacket rtcpSdesPacket = RtcpPacketTest.sdesCreationTest();
        RtcpPacket rtcpByePacket = RtcpPacketTest.byeCreationTest();

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

}
