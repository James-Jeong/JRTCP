package network.rtcp.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.module.ByteUtil;

public class RtcpHeader {

    /**
     *  0               1               2               3
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |V=2|P|    RC   |   PT=SR=200   |             length L          |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                         SSRC of sender                        |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     */

    ////////////////////////////////////////////////////////////
    // VARIABLES
    public static final int LENGTH = 8;// 8 bytes (4 + 4(ssrc, unsigned int))
    // (unsigned integer 변수들 때문에 크게 잡음 (오버플로우 방지하기 위함))

    // VERSION
    // Identifies the version of RTP, which is the same in RTCP packets as in RTP data packets.
    // The version defined by this specification is two (2).
    private int v = 0; // (2 bits)

    // PADDING
    // If the padding bit is set, this RTCP packet contains some additional padding octets
    // at the end which are not part of the control information.
    // The last octet of the padding is a count of how many padding octets should be ignored.
    // Padding may be needed by some encryption algorithms with fixed block sizes.
    // In a compound RTCP packet, padding should only be required on
    // the last individual packet because the compound packet is encrypted as a whole.
    private int p = 0; // (1 bit)

    // 1) The number of reception report blocks contained in this packet.
    //      A value of zero is valid.
    // 2) The number of SSRC/CSRC chunks contained in this SDES packet.
    private int rc = 0; // (5 bits)

    // PACKET TYPE
    // Contains the constant 200 to identify this as an RTCP SR packet.
    /**
     * 200 = SR Sender Report packet
     * 201 = RR Receiver Report packet
     * 202 = SDES Source Description packet
     * 203 = BYE Goodbye packet
     * 204 = APP Application-defined packet
     */
    private short pt = 0; // (8 bits)

    // LENGTH
    // The length of this RTCP packet in 32-bit words minus one,
    // including the header and any padding.
    private int l = 0; // (16 bits)

    // SSRC
    // The synchronization source identifier for the originator of this SR packet.
    private long ssrc = 0; // (32 bits)
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpHeader(int v, int p, int rc, short pt, int l, long ssrc) {
        this.v = v;
        this.p = p;
        this.rc = rc;
        this.pt = pt;
        this.l = l;
        this.ssrc = (int) ssrc;
    }

    public RtcpHeader() {}

    public RtcpHeader(byte[] data) { // big endian
        if (data.length >= LENGTH) {
            int index = 0;

            // V, P, RC
            byte[] vprcData = new byte[1];
            System.arraycopy(data, index, vprcData, 0, 1);
            v = (vprcData[0] >>> 0x06) & 0x03;
            p = (vprcData[0] >>> 0x05) & 0x01;
            rc = vprcData[0] & 0x05;
            index += 1;

            // PT
            byte[] ptData = new byte[1];
            System.arraycopy(data, index, ptData, 0, 1);
            byte[] ptData2 = new byte[ByteUtil.NUM_BYTES_IN_SHORT];
            System.arraycopy(ptData, 0, ptData2, 1, 1);
            pt = ByteUtil.bytesToShort(ptData2, true);
            index += 1;

            // LENGTH
            byte[] lengthData = new byte[ByteUtil.NUM_BYTES_IN_SHORT];
            System.arraycopy(data, index, lengthData, 0, 2);
            byte[] lengthData2 = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(lengthData, 0, lengthData2, ByteUtil.NUM_BYTES_IN_SHORT, ByteUtil.NUM_BYTES_IN_SHORT);
            l = ByteUtil.bytesToInt(lengthData2, true);
            index += ByteUtil.NUM_BYTES_IN_SHORT;

            // SSRC
            byte[] ssrcData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, ssrcData, 0, ByteUtil.NUM_BYTES_IN_INT);
            byte[] ssrcData2 = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(ssrcData, 0, ssrcData2, ByteUtil.NUM_BYTES_IN_INT, ByteUtil.NUM_BYTES_IN_INT);
            ssrc = ByteUtil.bytesToLong(ssrcData2, true);
        }
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public byte[] getData() {
        byte[] data = new byte[LENGTH];
        int index = 0;

        // V, P, RC
        byte vprc = 0;
        vprc |= v;
        vprc <<= 0x01;
        vprc |= p;
        vprc <<= 0x05;
        vprc |= rc;
        byte[] vprcData = { vprc };
        System.arraycopy(vprcData, 0, data, index, 1);
        index += 1;

        // PT
        byte[] ptData = ByteUtil.shortToBytes(pt, true);
        byte[] ptData2 = { ptData[1] };
        System.arraycopy(ptData2, 0, data, index, ptData2.length);
        index += 1;

        // LENGTH
        byte[] lengthData = ByteUtil.shortToBytes((short) l, true);
        System.arraycopy(lengthData, 0, data, index, lengthData.length);
        index += 2;

        // SSRC
        byte[] ssrcData = ByteUtil.intToBytes((int) ssrc, true);
        System.arraycopy(ssrcData, 0, data, index, ssrcData.length);

        return data;
    }

    public void setData(int v, int p, int rc, short pt, int l, long ssrc) {
        this.v = v;
        this.p = p;
        this.rc = rc;
        this.pt = pt;
        this.l = l;
        this.ssrc = (int) ssrc;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public int getPt() {
        return pt;
    }

    public void setPt(short pt) {
        this.pt = pt;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public long getSsrc() {
        return ssrc;
    }

    public void setSsrc(long ssrc) {
        this.ssrc = ssrc;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    ////////////////////////////////////////////////////////////

}
