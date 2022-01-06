import network.NetworkTest;
import org.junit.Test;
import rtcp.RtcpHeaderTest;
import rtcp.RtcpReceiverReportTest;
import rtcp.RtcpReportBlockTest;
import rtcp.RtcpSenderReportTest;

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
    }

}
