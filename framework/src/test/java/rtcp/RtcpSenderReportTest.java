package rtcp;

import network.rtcp.module.SsrcGenerator;
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

public class RtcpSenderReportTest {

    private static final Logger logger = LoggerFactory.getLogger(RtcpSenderReportTest.class);

    @Test
    public void test() {
        long ssrc = SsrcGenerator.generateSsrc();

        byte[] data = creationTest(ssrc);
        getTest(data);
    }

    private byte[] creationTest(long ssrc) {
        long curSeconds = TimeStamp.getCurrentTime().getSeconds();
        long curFraction = TimeStamp.getCurrentTime().getFraction();
        long rtpTimeStamp = 250880;

        // REPORT BLOCK LIST
        List<RtcpReportBlock> rtcpReportBlockList = new ArrayList<>();
        RtcpReportBlock source1 = new RtcpReportBlock(
                ssrc, (byte) 0, 1,
                50943, 76,
                curSeconds, 35390
        );
        rtcpReportBlockList.add(source1);

        // RtcpSenderReport
        RtcpSenderReport rtcpSenderReport = new RtcpSenderReport(
                curSeconds,
                curFraction, rtpTimeStamp,
                1568, 2508,
                rtcpReportBlockList,
                null
        );
        logger.debug("[RtcpSenderReportTest][creationTest] RtcpSenderReport: \n{}", rtcpSenderReport);

        byte[] rtcpSenderReportData = rtcpSenderReport.getData();
        Assert.assertNotNull(rtcpSenderReportData);
        logger.debug("[RtcpSenderReportTest][creationTest] RtcpSenderReport byte data: \n{}", rtcpSenderReportData);
        logger.debug("[RtcpSenderReportTest][creationTest] RtcpSenderReport byte data: \n{}", ByteUtil.byteArrayToHex(rtcpSenderReportData));
        return rtcpSenderReportData;
    }

    private void getTest(byte[] data) {
        RtcpSenderReport rtcpSenderReport = new RtcpSenderReport(data);
        logger.debug("[RtcpSenderReportTest][getTest] ReportBlock: \n{}", rtcpSenderReport);
    }

}
