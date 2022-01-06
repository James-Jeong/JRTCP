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
    public static final int LENGTH = 8;// 8 bytes (4 + 4(ssrc))

    // Version
    // Identifies the version of RTP, which is the same in RTCP packets as in RTP data packets.
    // The version defined by this specification is two (2).
    private int v; // (2 bits)

    // Padding
    // If the padding bit is set, this RTCP packet contains some additional padding octets
    // at the end which are not part of the control information.
    // The last octet of the padding is a count of how many padding octets should be ignored.
    // Padding may be needed by some encryption algorithms with fixed block sizes.
    // In a compound RTCP packet, padding should only be required on
    // the last individual packet because the compound packet is encrypted as a whole.
    private int p; // (1 bit)

    // The number of reception report blocks contained in this packet.
    // A value of zero is valid.
    private int rc; // (5 bits)

    // The packet type
    // Contains the constant 200 to identify this as an RTCP SR packet.
    /**
     * 200 = SR Sender Report packet
     * 201 = RR Receiver Report packet
     * 202 = SDES Source Description packet
     * 203 = BYE Goodbye packet
     * 204 = APP Application-defined packet
     */
    private int pt; // (8 bits)

    // The length of this RTCP packet in 32-bit words minus one,
    // including the header and any padding.
    private int l; // (16 bits)

    // The synchronization source identifier for the originator of this SR packet.
    private int ssrc; // (32 bits)
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpHeader(int v, int p, int rc, int pt, int l, int ssrc) {
        this.v = v;
        this.p = p;
        this.rc = rc;
        this.pt = pt;
        this.l = l;
        this.ssrc = ssrc;
    }

    public RtcpHeader() {}

    public RtcpHeader(byte[] data) { // big endian
        if (data.length >= LENGTH) {
            int index = 0;

            // V, P, RC
            byte[] vprcData = new byte[1];
            System.arraycopy(data, index, vprcData, 0, 1);
            v = vprcData[0] & 0x11000000;
            p = vprcData[0] & 0x00100000;
            rc = vprcData[0] & 0x00011111;
            index += 1;

            // PT
            byte[] ptData = new byte[1];
            System.arraycopy(data, index, ptData, 0, 1);
            pt = ptData[0];
            index += 1;

            // LENGTH
            byte[] lengthData = new byte[2];
            System.arraycopy(data, index, lengthData, 0, 2);
            l = ByteUtil.bytesToInt(lengthData, true);
            index += 2;

            // SSRC
            byte[] ssrcData = new byte[4];
            System.arraycopy(data, index, ssrcData, 0, 4);
            ssrc = ByteUtil.bytesToInt(ssrcData, true);
        }
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public byte[] getData() {
        byte[] data = new byte[LENGTH];
        int index = 0;

        // V, P, RC
        byte vprc = (byte) ((byte) v & 0x11000000);
        byte pByte = (byte) ((byte) p & 0x00100000);
        vprc |= pByte;
        byte rcByte = (byte) ((byte) rc & 0x00011111);
        vprc |= rcByte;
        byte[] vprcData = { vprc };
        System.arraycopy(vprcData, 0, data, index, 1);
        index += 1;

        // PT
        byte[] ptData = { (byte) pt };
        System.arraycopy(ptData, 0, data, index, 1);
        index += 1;

        // LENGTH
        byte[] lengthData = ByteUtil.shortToBytes((short) l, true);
        System.arraycopy(lengthData, 0, data, index, 2);
        index += 2;

        // SSRC
        byte[] ssrcData = ByteUtil.intToBytes((int) ssrc, true);
        System.arraycopy(ssrcData, 0, data, index, 2);

        return data;
    }

    public void setData(int v, int p, int rc, int pt, int l, int ssrc) {
        this.v = v;
        this.p = p;
        this.rc = rc;
        this.pt = pt;
        this.l = l;
        this.ssrc = ssrc;
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

    public void setPt(int pt) {
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

    public void setSsrc(int ssrc) {
        this.ssrc = ssrc;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    ////////////////////////////////////////////////////////////

}
