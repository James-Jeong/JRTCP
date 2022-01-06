package rtcp;

import network.rtcp.base.RtcpHeader;
import network.rtcp.base.RtcpType;
import network.rtcp.type.RtcpBye;
import network.rtcp.type.RtcpSenderReport;
import network.rtcp.type.base.RtcpReportBlock;
import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.module.ByteUtil;

import java.util.ArrayList;
import java.util.List;

public class RtcpByeTest {

    private static final Logger logger = LoggerFactory.getLogger(RtcpByeTest.class);

    @Test
    public void test() {
        byte[] data = creationTest();
        getTest(data);
    }

    private byte[] creationTest() {
        // HEADER
        RtcpHeader rtcpHeader = new RtcpHeader(2, 0, 1, RtcpType.GOOD_BYE, 7, 26422708);
        String reason = "TEST";

        RtcpBye rtcpBye = new RtcpBye(
                rtcpHeader,
                (short) reason.length(),
                reason
        );
        logger.debug("[RtcpByeTest][creationTest] RtcpBye: \n{}", rtcpBye);

        byte[] rtcpByeData = rtcpBye.getData();
        logger.debug("[RtcpByeTest][creationTest] RtcpBye byte data: \n{}", rtcpByeData);
        logger.debug("[RtcpByeTest][creationTest] RtcpBye byte data: \n{}", ByteUtil.byteArrayToHex(rtcpByeData));
        return rtcpByeData;
    }

    private void getTest(byte[] data) {
        RtcpBye rtcpBye = new RtcpBye(data);
        logger.debug("[RtcpByeTest][getTest] RtcpBye: \n{}", rtcpBye);
    }

}
