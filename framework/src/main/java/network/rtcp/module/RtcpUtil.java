package network.rtcp.module;

public class RtcpUtil {

    public static long differTimestamp(long left, long right) {
        return right - left;
    }

}
