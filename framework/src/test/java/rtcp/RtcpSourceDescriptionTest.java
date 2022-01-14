package rtcp;

import network.rtcp.module.SsrcGenerator;
import network.rtcp.type.report.RtcpSourceDescription;
import network.rtcp.type.report.base.sdes.SdesChunk;
import network.rtcp.type.report.base.sdes.SdesItem;
import network.rtcp.type.report.base.sdes.SdesType;
import org.junit.Assert;
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
        long ssrc = SsrcGenerator.generateSsrc();

        byte[] data1 = creationTest(ssrc);
        getTest(data1);

        byte[] data2 = multiChunkTest();
        getTest(data2);
    }

    private byte[] creationTest(long ssrc) {
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
                ssrc,
                chunk1SdesItemList
        );
        sdesChunkList.add(sdesChunk1);

        // RtcpSourceDescription
        RtcpSourceDescription rtcpSourceDescription = new RtcpSourceDescription(sdesChunkList);
        logger.debug("[RtcpSourceDescriptionTest][creationTest] RtcpSourceDescription: \n{}", rtcpSourceDescription);

        byte[] rtcpSourceDescriptionData = rtcpSourceDescription.getData();
        Assert.assertNotNull(rtcpSourceDescriptionData);
        logger.debug("[RtcpSourceDescriptionTest][creationTest] ReportBlock byte data: \n{}", rtcpSourceDescriptionData);
        logger.debug("[RtcpSourceDescriptionTest][creationTest] ReportBlock byte data: \n{}", ByteUtil.byteArrayToHex(rtcpSourceDescriptionData));
        return rtcpSourceDescriptionData;
    }

    private byte[] multiChunkTest() {
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

        long ssrc1 = SsrcGenerator.generateSsrc();
        SdesChunk sdesChunk1 = new SdesChunk(
                ssrc1,
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

        long ssrc2 = SsrcGenerator.generateSsrc();
        SdesChunk sdesChunk2 = new SdesChunk(
                ssrc2,
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

        long ssrc3 = SsrcGenerator.generateSsrc();
        SdesChunk sdesChunk3 = new SdesChunk(
                ssrc3,
                chunk3SdesItemList
        );
        sdesChunkList.add(sdesChunk3);

        // RtcpSourceDescription
        RtcpSourceDescription rtcpSourceDescription = new RtcpSourceDescription(sdesChunkList);
        logger.debug("[RtcpSourceDescriptionTest][creationTest] RtcpSourceDescription: \n{}", rtcpSourceDescription);

        byte[] rtcpSourceDescriptionData = rtcpSourceDescription.getData();
        logger.debug("[RtcpSourceDescriptionTest][creationTest] ReportBlock byte data: \n{}", rtcpSourceDescriptionData);
        logger.debug("[RtcpSourceDescriptionTest][creationTest] ReportBlock byte data: \n{}", ByteUtil.byteArrayToHex(rtcpSourceDescriptionData));
        return rtcpSourceDescriptionData;
    }

    private void getTest(byte[] data) {
        RtcpSourceDescription rtcpSourceDescription = new RtcpSourceDescription(data);
        logger.debug("[RtcpSourceDescriptionTest][getTest] RtcpSourceDescription: \n{}", rtcpSourceDescription);
    }

}
