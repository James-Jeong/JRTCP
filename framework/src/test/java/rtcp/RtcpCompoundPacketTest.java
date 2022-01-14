package rtcp;

import network.rtcp.type.report.base.RtcpHeader;
import network.rtcp.packet.RtcpPacketPaddingResult;
import network.rtcp.base.RtcpType;
import network.rtcp.module.SsrcGenerator;
import network.rtcp.packet.RtcpCompoundPacket;
import network.rtcp.packet.RtcpPacket;
import network.rtcp.type.report.RtcpSenderReport;
import network.rtcp.type.report.RtcpSourceDescription;
import network.rtcp.type.report.base.report.RtcpReportBlock;
import network.rtcp.type.report.base.sdes.SdesChunk;
import network.rtcp.type.report.base.sdes.SdesItem;
import network.rtcp.type.report.base.sdes.SdesType;
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
        long ssrc = SsrcGenerator.generateSsrc();
        long curTime = TimeStamp.getCurrentTime().getTime();
        long rtpTimeStamp = 250880;

        // REPORT BLOCK LIST
        List<RtcpReportBlock> rtcpReportBlockList = new ArrayList<>();
        RtcpReportBlock source1 = new RtcpReportBlock(
                ssrc,
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
        RtcpPacketPaddingResult rtcpSenderReportPacketPaddingResult = RtcpPacket.getPacketLengthByBytes(
                rtcpSenderReportData.length, false
        );
        RtcpHeader rtcpSenderReportPacketHeader = new RtcpHeader(
                2, rtcpSenderReportPacketPaddingResult,
                rtcpSenderReport.getReportBlockList().size(), RtcpType.SENDER_REPORT,
                ssrc
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
                ssrc,
                chunk1SdesItemList
        );
        sdesChunkList.add(sdesChunk1);

        // RtcpSourceDescription
        RtcpSourceDescription rtcpSourceDescription = new RtcpSourceDescription(sdesChunkList);
        byte[] rtcpSourceDescriptionData = rtcpSourceDescription.getData();
        logger.debug("[RtcpCompoundPacketTest][creationTest] RtcpSourceDescription byte data: (size={})\n{}", rtcpSourceDescriptionData.length, rtcpSourceDescriptionData);

        // HEADER
        RtcpPacketPaddingResult rtcpSdesPacketPaddingResult = RtcpPacket.getPacketLengthByBytes(
                rtcpSourceDescriptionData.length, true
        );
        logger.debug("rtcpSdesPacketPaddingResult: {}", rtcpSdesPacketPaddingResult);
        RtcpHeader rtcpSdesPacketHeader = new RtcpHeader(
                2, rtcpSdesPacketPaddingResult,
                rtcpSourceDescription.getSdesChunkList().size(), RtcpType.SOURCE_DESCRIPTION, ssrc
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
