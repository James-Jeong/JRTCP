package rtcp;

import network.rtcp.base.RtcpHeader;
import network.rtcp.base.RtcpPacketPaddingResult;
import network.rtcp.base.RtcpType;
import network.rtcp.packet.RtcpPacket;
import network.rtcp.type.RtcpSenderReport;
import network.rtcp.type.base.RtcpReportBlock;
import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.module.ByteUtil;

import java.util.ArrayList;
import java.util.List;

public class RtcpPacketTest {

    private static final Logger logger = LoggerFactory.getLogger(RtcpPacketTest.class);

    @Test
    public void test() {
        byte[] data = srCreationTest();
        getTest(data);
    }

    public byte[] srCreationTest() {
        // SR
        long curTime = TimeStamp.getCurrentTime().getTime();

        // REPORT BLOCK LIST
        List<RtcpReportBlock> rtcpReportBlockList = new ArrayList<>();
        RtcpReportBlock source1 = new RtcpReportBlock(
                1569920308, (byte) 0, 1,
                50943, 76,
                curTime,
                35390
        );
        rtcpReportBlockList.add(source1);

        // RtcpSenderReport
        RtcpSenderReport rtcpSenderReport = new RtcpSenderReport(
                System.currentTimeMillis(),
                curTime, 250880,
                1568, 2508,
                rtcpReportBlockList,
                null
        );
        byte[] rtcpSenderReportData = rtcpSenderReport.getData();
        logger.debug("[RtcpPacketTest][srCreationTest] RtcpSenderReport byte data: (size={})\n{}", rtcpSenderReportData.length, rtcpSenderReportData);

        // HEADER
        RtcpPacketPaddingResult rtcpPacketPaddingResult = RtcpPacket.getPacketLengthByBytes(rtcpSenderReportData.length);
        /*RtcpHeader rtcpHeader = new RtcpHeader(2, rtcpPacketPaddingResult.isPadding()? 1 : 0,
                rtcpSenderReport.getReportBlockList().size(), RtcpType.SENDER_REPORT,
                rtcpPacketPaddingResult.getLength(), 26422708
        );*/
        RtcpHeader rtcpHeader = new RtcpHeader(
                2, rtcpPacketPaddingResult,
                rtcpSenderReport.getReportBlockList().size(), RtcpType.SENDER_REPORT,
                26422708
        );
        RtcpPacket rtcpPacket = new RtcpPacket(rtcpHeader, rtcpSenderReport);
        logger.debug("[RtcpPacketTest][srCreationTest] RtcpPacket: \n{}", rtcpPacket);

        byte[] rtcpPacketData = rtcpPacket.getData();
        Assert.assertNotNull(rtcpPacketData);
        logger.debug("[RtcpPacketTest][srCreationTest] RtcpPacketTest byte data: (size={})\n{}", rtcpPacketData.length, rtcpPacketData);
        logger.debug("[RtcpPacketTest][srCreationTest] RtcpPacketTest byte data: \n{}", ByteUtil.byteArrayToHex(rtcpPacketData));
        return rtcpPacketData;
    }

    public void getTest(byte[] data) {
        RtcpPacket rtcpPacket = new RtcpPacket(data);
        logger.debug("[RtcpPacketTest][getTest] RtcpPacket: \n{}", rtcpPacket);
    }

}
