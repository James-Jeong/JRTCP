package rtcp;

import network.rtcp.base.RtcpHeader;
import network.rtcp.base.RtcpType;
import network.rtcp.type.RtcpApplicationDefined;
import network.rtcp.type.RtcpBye;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.module.ByteUtil;

public class RtcpApplicationDefinedTest {

    private static final Logger logger = LoggerFactory.getLogger(RtcpApplicationDefinedTest.class);

    @Test
    public void test() {
        byte[] data = creationTest();
        getTest(data);
    }

    private byte[] creationTest() {
        // HEADER
        RtcpHeader rtcpHeader = new RtcpHeader(2, 0, 1, RtcpType.APPLICATION_DEFINED, 7, 26422708);
        String name = "TEST";

        RtcpApplicationDefined applicationDefined = new RtcpApplicationDefined(
                rtcpHeader,
                name,
                null
        );
        logger.debug("[RtcpApplicationDefinedTest][creationTest] RtcpApplicationDefined: \n{}", applicationDefined);

        byte[] applicationDefinedData = applicationDefined.getData();
        logger.debug("[RtcpApplicationDefinedTest][creationTest] RtcpApplicationDefined byte data: \n{}", applicationDefinedData);
        logger.debug("[RtcpApplicationDefinedTest][creationTest] RtcpApplicationDefined byte data: \n{}", ByteUtil.byteArrayToHex(applicationDefinedData));
        return applicationDefinedData;
    }

    private void getTest(byte[] data) {
        RtcpApplicationDefined applicationDefined = new RtcpApplicationDefined(data);
        logger.debug("[RtcpApplicationDefinedTest][getTest] RtcpApplicationDefined: \n{}", applicationDefined);
    }

}
