package rtcp;

import network.rtcp.base.RtcpHeader;
import network.rtcp.base.RtcpType;
import network.rtcp.type.RtcpReceiverReport;
import network.rtcp.type.RtcpSourceDescription;
import network.rtcp.type.base.RtcpReportBlock;
import network.rtcp.type.base.sdes.SdesChunk;
import network.rtcp.type.base.sdes.SdesItem;
import network.rtcp.type.base.sdes.SdesType;
import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.module.ByteUtil;

import java.util.ArrayList;
import java.util.List;

public class RtcpSourceDescriptionTest {

    private static final Logger logger = LoggerFactory.getLogger(RtcpSourceDescriptionTest.class);

    @Test
    public void test() {
        byte[] data1 = creationTest();
        getTest(data1);

        byte[] data2 = multiChunkTest();
        getTest(data2);
    }

    private byte[] creationTest() {
        // HEADER
        RtcpHeader rtcpHeader = new RtcpHeader(2, 0, 1, RtcpType.SOURCE_DESCRIPTION, 7, 26422708);

        // SDES CHUNK LIST
        List<SdesChunk> sdesChunkList = new ArrayList<>();

        // CHUNK 1
        List<SdesItem> chunk1SdesItemList = new ArrayList<>();
        SdesItem chunk1SdesItem1 = new SdesItem(SdesType.CNAME, 5, "CNAME");
        SdesItem chunk1SdesItem2 = new SdesItem(SdesType.PHONE, 5, "PHONE");
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
        RtcpSourceDescription rtcpSourceDescription = new RtcpSourceDescription(
                rtcpHeader,
                sdesChunkList
        );
        logger.debug("[RtcpSourceDescriptionTest][creationTest] RtcpSourceDescription: \n{}", rtcpSourceDescription);

        byte[] rtcpSourceDescriptionData = rtcpSourceDescription.getData();
        logger.debug("[RtcpSourceDescriptionTest][creationTest] ReportBlock byte data: \n{}", rtcpSourceDescriptionData);
        logger.debug("[RtcpSourceDescriptionTest][creationTest] ReportBlock byte data: \n{}", ByteUtil.byteArrayToHex(rtcpSourceDescriptionData));
        return rtcpSourceDescriptionData;
    }

    private byte[] multiChunkTest() {
        // HEADER
        RtcpHeader rtcpHeader = new RtcpHeader(2, 0, 3, (short) 201, 7, 26422708);

        // SDES CHUNK LIST
        List<SdesChunk> sdesChunkList = new ArrayList<>();

        // CHUNK 1
        List<SdesItem> chunk1SdesItemList = new ArrayList<>();
        SdesItem chunk1SdesItem1 = new SdesItem(SdesType.CNAME, 5, "CNAME");
        SdesItem chunk1SdesItem2 = new SdesItem(SdesType.PHONE, 5, "PHONE");
        SdesItem chunk1SdesItem3 = new SdesItem(SdesType.TOOL, 4, "TOOL");
        chunk1SdesItemList.add(chunk1SdesItem1);
        chunk1SdesItemList.add(chunk1SdesItem2);
        chunk1SdesItemList.add(chunk1SdesItem3);
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
        chunk2SdesItemList.add(chunk2SdesItem1);
        chunk2SdesItemList.add(chunk2SdesItem2);
        chunk2SdesItemList.add(chunk2SdesItem3);
        SdesChunk sdesChunk2 = new SdesChunk(
                26422708,
                chunk2SdesItemList
        );
        sdesChunkList.add(sdesChunk2);

        // CHUNK 3
        List<SdesItem> chunk3SdesItemList = new ArrayList<>();
        SdesItem chunk3SdesItem1 = new SdesItem(SdesType.CNAME, 5, "CNAME");
        SdesItem chunk3SdesItem2 = new SdesItem(SdesType.LOC, 3, "LOC");
        chunk3SdesItemList.add(chunk3SdesItem1);
        chunk3SdesItemList.add(chunk3SdesItem2);
        SdesChunk sdesChunk3 = new SdesChunk(
                328590819,
                chunk3SdesItemList
        );
        sdesChunkList.add(sdesChunk3);

        // RtcpSourceDescription
        RtcpSourceDescription rtcpSourceDescription = new RtcpSourceDescription(
                rtcpHeader,
                sdesChunkList
        );
        logger.debug("[RtcpSourceDescriptionTest][creationTest] RtcpSourceDescription: \n{}", rtcpSourceDescription);

        byte[] rtcpSourceDescriptionData = rtcpSourceDescription.getData();
        logger.debug("[RtcpSourceDescriptionTest][creationTest] ReportBlock byte data: \n{}", rtcpSourceDescriptionData);
        logger.debug("[RtcpSourceDescriptionTest][creationTest] ReportBlock byte data: \n{}", ByteUtil.byteArrayToHex(rtcpSourceDescriptionData));
        return rtcpSourceDescriptionData;
    }

    private void getTest(byte[] data) {

    }

}
