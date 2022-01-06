package network.rtcp.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import network.rtcp.base.RtcpHeader;
import util.module.ByteUtil;

import java.nio.charset.StandardCharsets;

public class RtcpBye {

    /**
     *  0               1               2               3
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |V=2|P|    SC   |   PT=BYE=203  |            length L           |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                           SSRC/CSRC                           |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * :                              ...                              :
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     * |     length    |               reason for leaving (opt)       ...
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     */

    ////////////////////////////////////////////////////////////
    // VARIABLES
    public static final int MIN_LENGTH = RtcpHeader.LENGTH + 1; // bytes
    public static final int LIMIT_REASON_LENGTH = 255; // bytes

    // RTCP HEADER
    private RtcpHeader rtcpHeader = null;

    // LENGTH
    private short length = 0; // (8 bits)

    // REASON
    private String reason = null; // (? bits, limit is 255 bytes)
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpBye(RtcpHeader rtcpHeader, short length, String reason) {
        this.rtcpHeader = rtcpHeader;
        this.length = (byte) length;
        this.reason = reason;
    }

    public RtcpBye() {}

    public RtcpBye(byte[] data) {
        if (data.length >= MIN_LENGTH) {
            int index = 0;

            // HEADER
            byte[] headerData = new byte[RtcpHeader.LENGTH];
            System.arraycopy(data, index, headerData, 0, RtcpHeader.LENGTH);
            rtcpHeader = new RtcpHeader(headerData);
            index += RtcpHeader.LENGTH;

            // LENGTH
            byte[] lengthData = new byte[ByteUtil.NUM_BYTES_IN_SHORT];
            System.arraycopy(data, index, lengthData, 1, 1);
            length = ByteUtil.bytesToShort(lengthData, true);
            index += 1;

            // REASON
            int remainDataLength = data.length - index;
            if (remainDataLength > 0) {
                if (remainDataLength > LIMIT_REASON_LENGTH) {
                    remainDataLength = LIMIT_REASON_LENGTH;
                }

                byte[] reasonData = new byte[remainDataLength];
                System.arraycopy(data, index, reasonData, 0, remainDataLength);
                reason = new String(reasonData, StandardCharsets.UTF_8);
            }
        }
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public byte[] getData() {
        if (rtcpHeader == null) {
            return null;
        }

        byte[] data = new byte[MIN_LENGTH];
        int index = 0;

        // HEADER
        byte[] headerData = rtcpHeader.getData();
        System.arraycopy(headerData, 0, data, index, headerData.length);
        index += headerData.length;

        // LENGTH
        byte[] lengthData = ByteUtil.shortToBytes(length, true);
        System.arraycopy(lengthData, 1, data, index, 1);
        index += 1;

        // REASON
        if (reason != null && !reason.isEmpty()) {
            byte[] reasonData = reason.getBytes(StandardCharsets.UTF_8);
            byte[] newData = new byte[data.length + reasonData.length];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;
            System.arraycopy(reasonData, 0, data, index, reasonData.length);
        }

        return data;
    }

    public RtcpHeader getRtcpHeader() {
        return rtcpHeader;
    }

    public void setRtcpHeader(RtcpHeader rtcpHeader) {
        this.rtcpHeader = rtcpHeader;
    }

    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    ////////////////////////////////////////////////////////////

}
