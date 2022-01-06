package rtcp;

import network.rtcp.base.RtcpHeader;
import network.rtcp.base.RtcpType;
import network.rtcp.type.RtcpSenderReport;
import network.rtcp.type.base.RtcpReportBlock;
import org.apache.commons.net.ntp.TimeStamp;
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
        byte[] data = creationTest();
        getTest(data);
    }

    private byte[] creationTest() {
        long curTime = TimeStamp.getCurrentTime().getTime();
        long rtpTimeStamp = 250880;

        // HEADER
        RtcpHeader rtcpHeader = new RtcpHeader(2, 0, 1, RtcpType.SENDER_REPORT, 7, 26422708);

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
                rtcpHeader,
                System.currentTimeMillis(),
                curTime,
                rtpTimeStamp,
                1568,
                2508,
                rtcpReportBlockList,
                null
        );
        logger.debug("[RtcpSenderReportTest][creationTest] RtcpSenderReport: \n{}", rtcpSenderReport);

        byte[] rtcpSenderReportData = rtcpSenderReport.getData();
        logger.debug("[RtcpSenderReportTest][creationTest] RtcpSenderReport byte data: \n{}", rtcpSenderReportData);
        logger.debug("[RtcpSenderReportTest][creationTest] RtcpSenderReport byte data: \n{}", ByteUtil.byteArrayToHex(rtcpSenderReportData));
        return rtcpSenderReportData;
    }

    private void getTest(byte[] data) {
        RtcpSenderReport rtcpSenderReport = new RtcpSenderReport(data);
        logger.debug("[RtcpSenderReportTest][getTest] ReportBlock: \n{}", rtcpSenderReport);
    }

}
