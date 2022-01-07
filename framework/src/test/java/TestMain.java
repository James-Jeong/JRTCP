import org.junit.Test;
import rtcp.*;

public class TestMain {

    @Test
    public void test() {
        //NetworkTest networkTest = new NetworkTest();
        //networkTest.test();

        RtcpHeaderTest rtcpHeaderTest = new RtcpHeaderTest();
        rtcpHeaderTest.test();

        RtcpReportBlockTest rtcpReportBlockTest = new RtcpReportBlockTest();
        rtcpReportBlockTest.test();

        RtcpSenderReportTest rtcpSenderReportTest = new RtcpSenderReportTest();
        rtcpSenderReportTest.test();

        RtcpReceiverReportTest rtcpReceiverReportTest = new RtcpReceiverReportTest();
        rtcpReceiverReportTest.test();

        RtcpSourceDescriptionTest rtcpSourceDescriptionTest = new RtcpSourceDescriptionTest();
        rtcpSourceDescriptionTest.test();

        RtcpByeTest rtcpByeTest = new RtcpByeTest();
        rtcpByeTest.test();

        RtcpApplicationDefinedTest rtcpApplicationDefinedTest = new RtcpApplicationDefinedTest();
        rtcpApplicationDefinedTest.test();

        RtcpPacketTest rtcpPacketTest = new RtcpPacketTest();
        rtcpPacketTest.test();

        RtcpCompoundPacketTest rtcpCompoundPacketTest = new RtcpCompoundPacketTest();
        rtcpCompoundPacketTest.test();
    }

}
