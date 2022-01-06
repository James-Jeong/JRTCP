package network.rtcp.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import network.rtcp.type.base.RtcpReportBlock;
import network.rtcp.base.RtcpHeader;
import util.module.ByteUtil;

import java.util.ArrayList;
import java.util.List;

public class RtcpSenderReport {

    /**
     *  0                   1                   2                   3
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |V=2|P|    RC   |   PT=SR=200   |             length            | header
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                         SSRC of sender                        |
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     * |              NTP timestamp, most significant word             | sender
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ info
     * |             NTP timestamp, least significant word             |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                         RTP timestamp                         |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                     sender's packet count                     |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                      sender's octet count                     |
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     * |                 SSRC_1 (SSRC of first source)                 | report
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ block
     * | fraction lost |       cumulative number of packets lost       |   1
     * -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |           extended highest sequence number received           |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                      interarrival jitter                      |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                         last SR (LSR)                         |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                   delay since last SR (DLSR)                  |
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     * |                 SSRC_2 (SSRC of second source)                | report
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ block
     * :                               ...                             :   2
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     * |                  profile-specific extensions                  |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     */

    ////////////////////////////////////////////////////////////
    // VARIABLES
    public static final int MIN_LENGTH = RtcpHeader.LENGTH + 20; // bytes

    // RTCP HEADER
    private RtcpHeader rtcpHeader = null;

    // The npt timestamp that indicates the point of time measured
    // in wall clock time when this report was sent.
    // In combination with timestamps returned in reception reports from the respective receivers,
    // it can be used to estimate the round-trip propagation time to and from the receivers.
    private long mswNts = 0; // Most Significant Word Ntp TimeStamp (32 bits)
    private long lswNts = 0; // Least Significant Word Ntp TimeStamp (32 bits)

    // The RTP timestamp resembles the same time as the NTP timestamp (above),
    // but is measured in the same units and with the same random offset
    // as the RTP timestamps in data packets.
    // This correspondence may be used for intra- and inter-media synchronisation
    // for sources whose NTP timestamps are synchronised,
    // and may be used by media-independent receivers to estimate the nominal RTP clock frequency.
    private long rts = 0; // Rtp TimeStamp (32 bits)

    // The sender's packet count totals up the number of RTP data packets
    // transmitted by the sender since joining the RTP session.
    // This field can be used to estimate the average data packet rate.
    // The total number of RTP data packets transmitted by the sender
    // since starting transmission up until the time this SR packet was generated.
    // The count is reset if the sender changes its SSRC identifier.
    private long spc = 0; // Sender Packet Count total (32 bits)

    // The total number of payload octets (i.e., not including the header or any padding)
    // transmitted in RTP data packets by the sender since starting up transmission.
    // This field can be used to estimate the average payload data rate.
    private long soc = 0; // Sender Octet Count total (32 bits)

    // Report Block List
    private List<RtcpReportBlock> rtcpReportBlockList = null;

    // Profile-specific extensions
    private byte[] profileSpecificExtensions = null;
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpSenderReport(RtcpHeader rtcpHeader,
                            long mswNts, long lswNts, long rts, long spc, long soc,
                            List<RtcpReportBlock> rtcpReportBlockList, byte[] profileSpecificExtensions) {
        this.rtcpHeader = rtcpHeader;
        this.mswNts = (int) mswNts;
        this.lswNts = (int) lswNts;
        this.rts = (int) rts;
        this.spc = (int) spc;
        this.soc = (int) soc;
        this.rtcpReportBlockList = rtcpReportBlockList;
        this.profileSpecificExtensions = profileSpecificExtensions;
    }

    public RtcpSenderReport() {}

    public RtcpSenderReport(byte[] data) {
        int index = 0;
        int dataLength = data.length;
        if (dataLength >= MIN_LENGTH) {
            rtcpReportBlockList = null;

            // HEADER
            byte[] headerData = new byte[RtcpHeader.LENGTH];
            System.arraycopy(data, index, headerData, 0, RtcpHeader.LENGTH);
            rtcpHeader = new RtcpHeader(headerData);
            index += RtcpHeader.LENGTH;

            // NTS > TimeStamp.getCurrentTime().getTime()
            byte[] mswNtsData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, mswNtsData, 0, ByteUtil.NUM_BYTES_IN_INT);
            byte[] mswNtsData2 = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(mswNtsData, 0, mswNtsData2, ByteUtil.NUM_BYTES_IN_INT, ByteUtil.NUM_BYTES_IN_INT);
            mswNts = ByteUtil.bytesToLong(mswNtsData2, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            byte[] lswNtsData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, lswNtsData, 0, ByteUtil.NUM_BYTES_IN_INT);
            byte[] lswNtsData2 = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(lswNtsData, 0, lswNtsData2, ByteUtil.NUM_BYTES_IN_INT, ByteUtil.NUM_BYTES_IN_INT);
            lswNts = ByteUtil.bytesToLong(lswNtsData2, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            // RTS > RtpPacket.getTimeStamp()
            byte[] rtsData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, rtsData, 0, ByteUtil.NUM_BYTES_IN_INT);
            byte[] rtsData2 = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(rtsData, 0, rtsData2, ByteUtil.NUM_BYTES_IN_INT, ByteUtil.NUM_BYTES_IN_INT);
            rts = ByteUtil.bytesToLong(rtsData2, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            // SPC > Get by Network Statistics
            byte[] spcData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, spcData, 0, ByteUtil.NUM_BYTES_IN_INT);
            byte[] spcData2 = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(spcData, 0, spcData2, ByteUtil.NUM_BYTES_IN_INT, ByteUtil.NUM_BYTES_IN_INT);
            spc = ByteUtil.bytesToLong(spcData2, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            // SOC > Get by Network Statistics
            byte[] socData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, socData, 0, ByteUtil.NUM_BYTES_IN_INT);
            byte[] socData2 = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(socData, 0, socData2, ByteUtil.NUM_BYTES_IN_INT, ByteUtil.NUM_BYTES_IN_INT);
            soc = ByteUtil.bytesToLong(socData2, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            if (dataLength > MIN_LENGTH) {
                rtcpReportBlockList = new ArrayList<>();

                // ReportBlock
                int curBlockIndex = index;
                while (curBlockIndex < dataLength) {
                    if (dataLength - curBlockIndex < RtcpReportBlock.LENGTH) { break; }

                    byte[] curBlockData = new byte[RtcpReportBlock.LENGTH];
                    System.arraycopy(data, curBlockIndex, curBlockData, 0, RtcpReportBlock.LENGTH);
                    RtcpReportBlock rtcpReceiverRtcpReportBlock = new RtcpReportBlock(curBlockData);
                    rtcpReportBlockList.add(rtcpReceiverRtcpReportBlock);
                    curBlockIndex += RtcpReportBlock.LENGTH;
                }

                // Profile Specific Extensions
                index = curBlockIndex;
                int remainLength = dataLength - index;
                if (remainLength > 0) {
                    profileSpecificExtensions = new byte[remainLength];
                    System.arraycopy(data, index, profileSpecificExtensions, 0, remainLength);
                }
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

        // NTS
        byte[] ntsData = new byte[ByteUtil.NUM_BYTES_IN_LONG];
        byte[] mswNtsData = ByteUtil.intToBytes((int) mswNts, true);
        System.arraycopy(mswNtsData, 0, ntsData, 0, mswNtsData.length);
        byte[] lswNtsData = ByteUtil.intToBytes((int) lswNts, true);
        System.arraycopy(lswNtsData, 0, ntsData, ByteUtil.NUM_BYTES_IN_INT, ByteUtil.NUM_BYTES_IN_INT);
        System.arraycopy(ntsData, 0, data, index, ntsData.length);
        index += ntsData.length;

        // RTS
        byte[] rtsData = ByteUtil.intToBytes((int) rts, true);
        System.arraycopy(rtsData, 0, data, index, rtsData.length);
        index += rtsData.length;

        // SPC
        byte[] spcData = ByteUtil.intToBytes((int) spc, true);
        System.arraycopy(spcData, 0, data, index, spcData.length);
        index += spcData.length;

        // SOC
        byte[] socData = ByteUtil.intToBytes((int) soc, true);
        System.arraycopy(socData, 0, data, index, socData.length);
        index += socData.length;

        // Report Block
        if (rtcpReportBlockList != null && !rtcpReportBlockList.isEmpty()) {
            byte[] newData = new byte[data.length + (rtcpReportBlockList.size() * RtcpReportBlock.LENGTH)];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;

            for (RtcpReportBlock rtcpReceiverRtcpReportBlock : rtcpReportBlockList) {
                if (rtcpReceiverRtcpReportBlock == null) { continue; }

                byte[] curReportBlockData = rtcpReceiverRtcpReportBlock.getByteData();
                System.arraycopy(curReportBlockData, 0, data, index, curReportBlockData.length);
                index += curReportBlockData.length;
            }
        }

        // Profile Specific Extenstions
        if (profileSpecificExtensions != null && profileSpecificExtensions.length > 0) {
            byte[] newData = new byte[data.length + profileSpecificExtensions.length];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;

            System.arraycopy(profileSpecificExtensions, 0, data, index, profileSpecificExtensions.length);
        }

        return data;
    }

    public void setData(RtcpHeader rtcpHeader,
                        long mswNts, long lswNts, long rts, long spc, long soc,
                        List<RtcpReportBlock> rtcpReportBlockList, byte[] profileSpecificExtensions) {
        this.rtcpHeader = rtcpHeader;
        this.mswNts = mswNts;
        this.lswNts = lswNts;
        this.rts = rts;
        this.spc = spc;
        this.soc = soc;
        this.rtcpReportBlockList = rtcpReportBlockList;
        this.profileSpecificExtensions = profileSpecificExtensions;
    }

    public RtcpHeader getRtcpHeader() {
        return rtcpHeader;
    }

    public void setRtcpHeader(RtcpHeader rtcpHeader) {
        this.rtcpHeader = rtcpHeader;
    }

    public long getMswNts() {
        return mswNts;
    }

    public void setMswNts(long mswNts) {
        this.mswNts = mswNts;
    }

    public long getLswNts() {
        return lswNts;
    }

    public void setLswNts(long lswNts) {
        this.lswNts = lswNts;
    }

    public long getRts() {
        return rts;
    }

    public void setRts(long rts) {
        this.rts = rts;
    }

    public long getSpc() {
        return spc;
    }

    public void setSpc(long spc) {
        this.spc = spc;
    }

    public long getSoc() {
        return soc;
    }

    public void setSoc(long soc) {
        this.soc = soc;
    }

    public void setSoc(int soc) {
        this.soc = soc;
    }

    public List<RtcpReportBlock> getReportBlockList() {
        return rtcpReportBlockList;
    }

    public RtcpReportBlock getReportBlockByIndex(int index) {
        if (rtcpReportBlockList == null || index < 0 || index >= rtcpReportBlockList.size()) { return null; }
        return rtcpReportBlockList.get(index);
    }

    public void setReportBlockList(List<RtcpReportBlock> rtcpReportBlockList) {
        this.rtcpReportBlockList = rtcpReportBlockList;
    }

    public byte[] getProfileSpecificExtensions() {
        return profileSpecificExtensions;
    }

    public void setProfileSpecificExtensions(byte[] profileSpecificExtensions) {
        this.profileSpecificExtensions = profileSpecificExtensions;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    ////////////////////////////////////////////////////////////

}
