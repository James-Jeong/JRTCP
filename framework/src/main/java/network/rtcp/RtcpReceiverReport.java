package network.rtcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.module.ByteUtil;

public class RtcpReceiverReport {

    /**
     *  0               1               2               3
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |V=2|P|    RC   |   PT=RR=201   |            length L           |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                     SSRC of packet sender                     |
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     * |                 SSRC_1 (SSRC of first source)                 |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * | fraction lost |       cumulative number of packets lost       |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |           extended highest sequence number received           |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                      inter-arrival jitter                     |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                         last SR (LSR)                         |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                   delay since last SR (DLSR)                  |
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     * |                 SSRC_2 (SSRC of second source)                |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * :                               ...                             :
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     * |                  profile-specific extensions                  |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     */

    ////////////////////////////////////////////////////////////
    // VARIABLES
    public static final int LENGTH = RtcpHeader.LENGTH + 20;

    // RTCP HEADER
    private RtcpHeader rtcpHeader;

    // The npt timestamp that indicates the point of time measured
    // in wall clock time when this report was sent.
    // In combination with timestamps returned in reception reports from the respective receivers,
    // it can be used to estimate the round-trip propagation time to and from the receivers.
    private long nts;

    // The RTP timestamp resembles the same time as the NTP timestamp (above),
    // but is measured in the same units and with the same random offset
    // as the RTP timestamps in data packets.
    // This correspondence may be used for intra- and inter-media synchronisation
    // for sources whose NTP timestamps are synchronised,
    // and may be used by media-independent receivers to estimate the nominal RTP clock frequency.
    private int rts;

    // The sender's packet count totals up the number of RTP data packets
    // transmitted by the sender since joining the RTP session.
    // This field can be used to estimate the average data packet rate.
    private int spc;

    // The total number of payload octets (i.e., not including the header or any padding)
    // transmitted in RTP data packets by the sender since starting up transmission.
    // This field can be used to estimate the average payload data rate.
    private int soc;
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpReceiverReport(RtcpHeader rtcpHeader, long nts, int rts, int spc, int soc) {
        this.rtcpHeader = rtcpHeader;
        this.nts = nts;
        this.rts = rts;
        this.spc = spc;
        this.soc = soc;
    }

    public RtcpReceiverReport() {}

    public RtcpReceiverReport(byte[] data) {
        if (data.length >= LENGTH) {
            int index = 0;

            // HEADER
            byte[] headerData = new byte[RtcpHeader.LENGTH];
            System.arraycopy(data, index, headerData, 0, RtcpHeader.LENGTH);
            rtcpHeader = new RtcpHeader(headerData);
            index += RtcpHeader.LENGTH;

            // NTS
            byte[] ntsData = new byte[8];
            System.arraycopy(data, index, ntsData, 0, ByteUtil.NUM_BYTES_IN_LONG);
            nts = ByteUtil.bytesToLong(ntsData, true);
            index += ByteUtil.NUM_BYTES_IN_LONG;

            // RTS
            byte[] rtsData = new byte[4];
            System.arraycopy(data, index, rtsData, 0, ByteUtil.NUM_BYTES_IN_INT);
            rts = ByteUtil.bytesToInt(rtsData, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            // SPC
            byte[] spcData = new byte[4];
            System.arraycopy(data, index, spcData, 0, ByteUtil.NUM_BYTES_IN_INT);
            spc = ByteUtil.bytesToInt(spcData, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            // SOC
            byte[] socData = new byte[4];
            System.arraycopy(data, index, socData, 0, ByteUtil.NUM_BYTES_IN_INT);
            soc = ByteUtil.bytesToInt(socData, true);
        }
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public byte[] getData() {
        byte[] data = new byte[LENGTH];
        int index = 0;

        // HEADER
        byte[] headerData = rtcpHeader.getByteData();
        System.arraycopy(headerData, 0, data, index, headerData.length);
        index += headerData.length;

        // NTS
        byte[] ntsData = ByteUtil.longToBytes(nts, true);
        System.arraycopy(ntsData, 0, data, index, ntsData.length);
        index += ntsData.length;

        // RTS
        byte[] rtsData = ByteUtil.intToBytes(rts, true);
        System.arraycopy(rtsData, 0, data, index, rtsData.length);
        index += rtsData.length;

        // SPC
        byte[] spcData = ByteUtil.intToBytes(spc, true);
        System.arraycopy(spcData, 0, data, index, spcData.length);
        index += spcData.length;

        // SOC
        byte[] socData = ByteUtil.intToBytes(soc, true);
        System.arraycopy(socData, 0, data, index, socData.length);

        return data;
    }

    public void setData(RtcpHeader rtcpHeader, long nts, int rts, int spc, int soc) {
        this.rtcpHeader = rtcpHeader;
        this.nts = nts;
        this.rts = rts;
        this.spc = spc;
        this.soc = soc;
    }

    public RtcpHeader getRtcpHeader() {
        return rtcpHeader;
    }

    public void setRtcpHeader(RtcpHeader rtcpHeader) {
        this.rtcpHeader = rtcpHeader;
    }

    public long getNts() {
        return nts;
    }

    public void setNts(long nts) {
        this.nts = nts;
    }

    public int getRts() {
        return rts;
    }

    public void setRts(int rts) {
        this.rts = rts;
    }

    public int getSpc() {
        return spc;
    }

    public void setSpc(int spc) {
        this.spc = spc;
    }

    public int getSoc() {
        return soc;
    }

    public void setSoc(int soc) {
        this.soc = soc;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    ////////////////////////////////////////////////////////////

}
