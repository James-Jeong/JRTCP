package rtcp;

import network.rtcp.base.RtcpHeader;
import network.rtcp.base.RtcpPacketPaddingResult;
import network.rtcp.base.RtcpType;
import network.rtcp.packet.RtcpCompoundPacket;
import network.rtcp.packet.RtcpPacket;
import network.rtcp.type.RtcpSenderReport;
import network.rtcp.type.RtcpSourceDescription;
import network.rtcp.type.base.RtcpReportBlock;
import network.rtcp.type.base.sdes.SdesChunk;
import network.rtcp.type.base.sdes.SdesItem;
import network.rtcp.type.base.sdes.SdesType;
import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RtcpCompoundPacketTest {

    private static final Logger logger = LoggerFactory.getLogger(RtcpCompoundPacketTest.class);

    @Test
    public void test() {
        creationTest();
    }

    public void creationTest() {
        ////////////////////////////////////////////////////////////////////////////
        // SR
        long curTime = TimeStamp.getCurrentTime().getTime();
        long rtpTimeStamp = 250880;

        // REPORT BLOCK LIST
        List<RtcpReportBlock> rtcpReportBlockList = new ArrayList<>();
        RtcpReportBlock source1 = new RtcpReportBlock(
                1569920308,
                (byte) 0,
                1,
                50943,
                76,
                curTime,
                35390
        );
        rtcpReportBlockList.add(source1);

        // RtcpSenderReport
        RtcpSenderReport rtcpSenderReport = new RtcpSenderReport(
                System.currentTimeMillis(),
                curTime,
                rtpTimeStamp,
                1568,
                2508,
                rtcpReportBlockList,
                null
        );
        byte[] rtcpSenderReportData = rtcpSenderReport.getData();
        logger.debug("[RtcpCompoundPacketTest][creationTest] RtcpSenderReport byte data: (size={})\n{}", rtcpSenderReportData.length, rtcpSenderReportData);

        // HEADER
        RtcpPacketPaddingResult rtcpSenderReportPacketPaddingResult = RtcpPacket.getPacketLengthByBytes(rtcpSenderReportData.length);
        //RtcpHeader rtcpSenderReportPacketHeader = new RtcpHeader(2, 0, 1, RtcpType.SENDER_REPORT, 7, 26422708);
        RtcpHeader rtcpSenderReportPacketHeader = new RtcpHeader(
                2, rtcpSenderReportPacketPaddingResult,
                rtcpSenderReport.getReportBlockList().size(), RtcpType.SENDER_REPORT, 26422708
        );
        RtcpPacket rtcpSenderReportPacket = new RtcpPacket(rtcpSenderReportPacketHeader, rtcpSenderReport);
        ////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////
        // SDES
        // SDES CHUNK LIST
        List<SdesChunk> sdesChunkList = new ArrayList<>();

        // CHUNK 1
        List<SdesItem> chunk1SdesItemList = new ArrayList<>();
        SdesItem chunk1SdesItem1 = new SdesItem(SdesType.CNAME, 5, "CNAME");
        SdesItem chunk1SdesItem2 = new SdesItem(SdesType.PHONE, 4, "AAAA");
        SdesItem chunk1SdesItem3 = new SdesItem(SdesType.TOOL, 4, "TOOL");
        chunk1SdesItemList.add(chunk1SdesItem1);
        chunk1SdesItemList.add(chunk1SdesItem2);
        chunk1SdesItemList.add(chunk1SdesItem3);
        SdesChunk sdesChunk1 = new SdesChunk(
                1569920308,
                chunk1SdesItemList
        );
        sdesChunkList.add(sdesChunk1);

        // RtcpSourceDescription
        RtcpSourceDescription rtcpSourceDescription = new RtcpSourceDescription(sdesChunkList);
        byte[] rtcpSourceDescriptionData = rtcpSourceDescription.getData();
        logger.debug("[RtcpCompoundPacketTest][creationTest] RtcpSourceDescription byte data: (size={})\n{}", rtcpSourceDescriptionData.length, rtcpSourceDescriptionData);

        // HEADER
        RtcpPacketPaddingResult rtcpSdesPacketPaddingResult = RtcpPacket.getPacketLengthByBytes(rtcpSourceDescriptionData.length);
        logger.debug("RtcpPacketPaddingResult: {}", rtcpSdesPacketPaddingResult);
        //RtcpHeader rtcpSdesPacketHeader = new RtcpHeader(2, 0, 1, RtcpType.SENDER_REPORT, 7, 26422708);
        RtcpHeader rtcpSdesPacketHeader = new RtcpHeader(
                2, rtcpSdesPacketPaddingResult,
                rtcpSourceDescription.getTotalSdesChunkSize(), RtcpType.SENDER_REPORT, 26422708
        );
        RtcpPacket rtcpSdesPacket = new RtcpPacket(rtcpSdesPacketHeader, rtcpSourceDescription);
        ////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////
        // COMPOUND
        RtcpCompoundPacket rtcpCompoundPacket = new RtcpCompoundPacket();
        rtcpCompoundPacket.addRtcpPacketToListAt(0, rtcpSenderReportPacket);
        rtcpCompoundPacket.addRtcpPacketToListAt(1, rtcpSdesPacket);
        logger.debug("[RtcpCompoundPacketTest][creationTest] RtcpCompoundPacket: (size={})\n{}", rtcpCompoundPacket.getTotalRtcpPacketSize(), rtcpCompoundPacket);
        ////////////////////////////////////////////////////////////////////////////
    }

}
