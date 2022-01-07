package rtcp;

import network.rtcp.base.RtcpHeader;
import network.rtcp.base.RtcpPacketPaddingResult;
import network.rtcp.base.RtcpType;
import network.rtcp.packet.RtcpPacket;
import network.rtcp.type.RtcpBye;
import network.rtcp.type.RtcpSenderReport;
import network.rtcp.type.RtcpSourceDescription;
import network.rtcp.type.base.RtcpReportBlock;
import network.rtcp.type.base.sdes.SdesChunk;
import network.rtcp.type.base.sdes.SdesItem;
import network.rtcp.type.base.sdes.SdesType;
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
        RtcpPacket rtcpPacket = srCreationTest();
        getTest(rtcpPacket.getData());
    }

    public static RtcpPacket srCreationTest() {
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

        // HEADER
        RtcpPacketPaddingResult rtcpPacketPaddingResult = RtcpPacket.getPacketLengthByBytes(rtcpSenderReportData.length);
        logger.debug("[RtcpPacketTest][srCreationTest] rtcpPacketPaddingResult: {}", rtcpPacketPaddingResult);
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
        return rtcpPacket;
    }

    public static RtcpPacket sdesCreationTest() {
        // SDES CHUNK LIST
        List<SdesChunk> sdesChunkList = new ArrayList<>();

        // CHUNK 1
        List<SdesItem> chunk1SdesItemList = new ArrayList<>();
        SdesItem chunk1SdesItem1 = new SdesItem(SdesType.CNAME, 5, "CNAME");
        SdesItem chunk1SdesItem2 = new SdesItem(SdesType.PHONE, 5, "PHONE");
        SdesItem chunk1SdesItem3 = new SdesItem(SdesType.TOOL, 4, "TOOL");
        SdesItem chunk1SdesItem4 = new SdesItem(SdesType.END, 0, null);
        chunk1SdesItemList.add(chunk1SdesItem1);
        chunk1SdesItemList.add(chunk1SdesItem2);
        chunk1SdesItemList.add(chunk1SdesItem3);
        chunk1SdesItemList.add(chunk1SdesItem4);
        SdesChunk sdesChunk1 = new SdesChunk(
                1569920308,
                chunk1SdesItemList
        );
        sdesChunkList.add(sdesChunk1);

        // CHUNK 2
        List<SdesItem> chunk2SdesItemList = new ArrayList<>();
        SdesItem chunk2SdesItem1 = new SdesItem(SdesType.CNAME, 5, "CNAME");
        SdesItem chunk2SdesItem2 = new SdesItem(SdesType.LOC, 3, "LOC");
        SdesItem chunk2SdesItem3 = new SdesItem(SdesType.EMAIL, 5, "EMAIL");
        SdesItem chunk2SdesItem4 = new SdesItem(SdesType.END, 0, null);
        chunk2SdesItemList.add(chunk2SdesItem1);
        chunk2SdesItemList.add(chunk2SdesItem2);
        chunk2SdesItemList.add(chunk2SdesItem3);
        chunk2SdesItemList.add(chunk2SdesItem4);
        SdesChunk sdesChunk2 = new SdesChunk(
                26422708,
                chunk2SdesItemList
        );
        sdesChunkList.add(sdesChunk2);

        // CHUNK 3
        List<SdesItem> chunk3SdesItemList = new ArrayList<>();
        SdesItem chunk3SdesItem1 = new SdesItem(SdesType.CNAME, 5, "CNAME");
        SdesItem chunk3SdesItem2 = new SdesItem(SdesType.LOC, 3, "LOC");
        SdesItem chunk3SdesItem3 = new SdesItem(SdesType.END, 0, null);
        chunk3SdesItemList.add(chunk3SdesItem1);
        chunk3SdesItemList.add(chunk3SdesItem2);
        chunk3SdesItemList.add(chunk3SdesItem3);
        SdesChunk sdesChunk3 = new SdesChunk(
                328590819,
                chunk3SdesItemList
        );
        sdesChunkList.add(sdesChunk3);

        // RtcpSourceDescription
        RtcpSourceDescription rtcpSourceDescription = new RtcpSourceDescription(sdesChunkList);
        byte[] rtcpSourceDescriptionData = rtcpSourceDescription.getData();

        // HEADER
        RtcpPacketPaddingResult rtcpPacketPaddingResult = RtcpPacket.getPacketLengthByBytes(rtcpSourceDescriptionData.length);
        logger.debug("[RtcpPacketTest][sdesCreationTest] rtcpPacketPaddingResult: {}", rtcpPacketPaddingResult);
        RtcpHeader rtcpHeader = new RtcpHeader(
                2, rtcpPacketPaddingResult,
                rtcpSourceDescription.getSdesChunkList().size(), RtcpType.SOURCE_DESCRIPTION,
                26422708
        );
        RtcpPacket rtcpPacket = new RtcpPacket(rtcpHeader, rtcpSourceDescription);
        logger.debug("[RtcpPacketTest][sdesCreationTest] RtcpPacket: \n{}", rtcpPacket);

        byte[] rtcpPacketData = rtcpPacket.getData();
        Assert.assertNotNull(rtcpPacketData);
        logger.debug("[RtcpPacketTest][sdesCreationTest] RtcpPacketTest byte data: (size={})\n{}", rtcpPacketData.length, rtcpPacketData);
        logger.debug("[RtcpPacketTest][sdesCreationTest] RtcpPacketTest byte data: \n{}", ByteUtil.byteArrayToHex(rtcpPacketData));
        return rtcpPacket;
    }

    public static RtcpPacket byeCreationTest() {
        String reason = "GOODBYE";
        RtcpBye rtcpBye = new RtcpBye(
                (short) reason.length(),
                reason
        );
        byte[] rtcpByeData = rtcpBye.getData();

        // HEADER
        RtcpPacketPaddingResult rtcpPacketPaddingResult = RtcpPacket.getPacketLengthByBytes(rtcpByeData.length);
        logger.debug("[RtcpPacketTest][byeCreationTest] rtcpPacketPaddingResult: {}", rtcpPacketPaddingResult);
        RtcpHeader rtcpHeader = new RtcpHeader(
                2, rtcpPacketPaddingResult,
                1, RtcpType.GOOD_BYE,
                26422708
        );
        RtcpPacket rtcpPacket = new RtcpPacket(rtcpHeader, rtcpBye);
        logger.debug("[RtcpPacketTest][byeCreationTest] RtcpPacket: \n{}", rtcpPacket);

        byte[] rtcpPacketData = rtcpPacket.getData();
        Assert.assertNotNull(rtcpPacketData);
        logger.debug("[RtcpPacketTest][byeCreationTest] RtcpPacketTest byte data: (size={})\n{}", rtcpPacketData.length, rtcpPacketData);
        logger.debug("[RtcpPacketTest][byeCreationTest] RtcpPacketTest byte data: \n{}", ByteUtil.byteArrayToHex(rtcpPacketData));
        return rtcpPacket;
    }

    public void getTest(byte[] data) {
        RtcpPacket rtcpPacket = new RtcpPacket(data);
        logger.debug("[RtcpPacketTest][getTest] RtcpPacket: \n{}", rtcpPacket);
    }

}
