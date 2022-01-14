package rtcp;

import network.rtcp.type.report.RtcpBye;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.module.ByteUtil;

public class RtcpByeTest {

    private static final Logger logger = LoggerFactory.getLogger(RtcpByeTest.class);

    @Test
    public void test() {
        byte[] data = creationTest();
        getTest(data);
    }

    private byte[] creationTest() {
        String reason = "TEST";
        RtcpBye rtcpBye = new RtcpBye(
                (short) reason.length(),
                reason
        );
        logger.debug("[RtcpByeTest][creationTest] RtcpBye: \n{}", rtcpBye);

        byte[] rtcpByeData = rtcpBye.getData();
        Assert.assertNotNull(rtcpByeData);
        logger.debug("[RtcpByeTest][creationTest] RtcpBye byte data: \n{}", rtcpByeData);
        logger.debug("[RtcpByeTest][creationTest] RtcpBye byte data: \n{}", ByteUtil.byteArrayToHex(rtcpByeData));
        return rtcpByeData;
    }

    private void getTest(byte[] data) {
        RtcpBye rtcpBye = new RtcpBye(data);
        logger.debug("[RtcpByeTest][getTest] RtcpBye: \n{}", rtcpBye);
    }

}
