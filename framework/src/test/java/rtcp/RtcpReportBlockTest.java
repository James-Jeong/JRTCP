package rtcp;

import network.rtcp.type.base.ReportBlock;
import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.module.ByteUtil;

public class RtcpReportBlockTest {

    private static final Logger logger = LoggerFactory.getLogger(RtcpReportBlockTest.class);

    @Test
    public void test() {
        long curTime = TimeStamp.getCurrentTime().getTime();
        byte[] data = creationTest(curTime);
        getTest(data);
    }

    private byte[] creationTest(long curTime) {
        ReportBlock reportBlock = new ReportBlock(
                1569920308,
                (byte) 0,
                1,
                50943,
                76,
                curTime,
                35390
        );

        logger.debug("[RtcpReportBlockTest][creationTest] ReportBlock: \n{}", reportBlock);

        byte[] reportBlockData = reportBlock.getByteData();
        logger.debug("[RtcpReportBlockTest][creationTest] ReportBlock byte data: \n{}", reportBlockData);
        logger.debug("[RtcpReportBlockTest][creationTest] ReportBlock byte data: \n{}", ByteUtil.byteArrayToHex(reportBlockData));
        return reportBlockData;
    }

    private void getTest(byte[] data) {
        ReportBlock reportBlock = new ReportBlock(data);
        logger.debug("[RtcpReportBlockTest][getTest] ReportBlock: \n{}", reportBlock);
    }

}
