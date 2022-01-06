package network.rtcp.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import network.rtcp.type.base.ReportBlock;
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
    private RtcpHeader rtcpHeader;

    // The npt timestamp that indicates the point of time measured
    // in wall clock time when this report was sent.
    // In combination with timestamps returned in reception reports from the respective receivers,
    // it can be used to estimate the round-trip propagation time to and from the receivers.
    private long nts; // Ntp TimeStamp (64 bits)

    // The RTP timestamp resembles the same time as the NTP timestamp (above),
    // but is measured in the same units and with the same random offset
    // as the RTP timestamps in data packets.
    // This correspondence may be used for intra- and inter-media synchronisation
    // for sources whose NTP timestamps are synchronised,
    // and may be used by media-independent receivers to estimate the nominal RTP clock frequency.
    private int rts; // Rtp TimeStamp (32 bits)

    // The sender's packet count totals up the number of RTP data packets
    // transmitted by the sender since joining the RTP session.
    // This field can be used to estimate the average data packet rate.
    // The total number of RTP data packets transmitted by the sender
    // since starting transmission up until the time this SR packet was generated.
    // The count is reset if the sender changes its SSRC identifier.
    private int spc; // Sender Packet Count total (32 bits)

    // The total number of payload octets (i.e., not including the header or any padding)
    // transmitted in RTP data packets by the sender since starting up transmission.
    // This field can be used to estimate the average payload data rate.
    private int soc; // Sender Octet Count total (32 bits)

    // Report Block List
    private List<ReportBlock> reportBlockList;

    // Profile-specific extensions
    private byte[] profileSpecificExtensions;
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpSenderReport(RtcpHeader rtcpHeader, long nts, int rts, int spc, int soc, List<ReportBlock> reportBlockList, byte[] profileSpecificExtensions) {
        this.rtcpHeader = rtcpHeader;
        this.nts = nts;
        this.rts = rts;
        this.spc = spc;
        this.soc = soc;
        this.reportBlockList = reportBlockList;
        this.profileSpecificExtensions = profileSpecificExtensions;
    }

    public RtcpSenderReport() {}

    public RtcpSenderReport(byte[] data) {
        int index = 0;
        int dataLength = data.length;
        if (dataLength <= MIN_LENGTH) {
            reportBlockList = null;

            // HEADER
            byte[] headerData = new byte[RtcpHeader.LENGTH];
            System.arraycopy(data, index, headerData, 0, RtcpHeader.LENGTH);
            rtcpHeader = new RtcpHeader(headerData);
            index += RtcpHeader.LENGTH;

            // NTS > TimeStamp.getCurrentTime().getTime()
            byte[] ntsData = new byte[8];
            System.arraycopy(data, index, ntsData, 0, ByteUtil.NUM_BYTES_IN_LONG);
            nts = ByteUtil.bytesToLong(ntsData, true);
            index += ByteUtil.NUM_BYTES_IN_LONG;

            // RTS > RtpPacket.getTimeStamp()
            byte[] rtsData = new byte[4];
            System.arraycopy(data, index, rtsData, 0, ByteUtil.NUM_BYTES_IN_INT);
            rts = ByteUtil.bytesToInt(rtsData, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            // SPC > Get by Network Statistics
            byte[] spcData = new byte[4];
            System.arraycopy(data, index, spcData, 0, ByteUtil.NUM_BYTES_IN_INT);
            spc = ByteUtil.bytesToInt(spcData, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            // SOC > Get by Network Statistics
            byte[] socData = new byte[4];
            System.arraycopy(data, index, socData, 0, ByteUtil.NUM_BYTES_IN_INT);
            soc = ByteUtil.bytesToInt(socData, true);
        }

        if (dataLength > MIN_LENGTH) {
            reportBlockList = new ArrayList<>();

            // ReportBlock
            int curBlockIndex = index;
            while (curBlockIndex < dataLength) {
                if (dataLength - curBlockIndex < ReportBlock.LENGTH) { break; }

                byte[] curBlockData = new byte[ReportBlock.LENGTH];
                System.arraycopy(data, curBlockIndex, curBlockData, 0, ReportBlock.LENGTH);
                ReportBlock rtcpReceiverReportBlock = new ReportBlock(curBlockData);
                reportBlockList.add(rtcpReceiverReportBlock);
                curBlockIndex += ReportBlock.LENGTH;
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
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public byte[] getData() {
        byte[] data = new byte[MIN_LENGTH];
        int index = 0;

        // HEADER
        byte[] headerData = rtcpHeader.getData();
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

        for (ReportBlock rtcpReceiverReportBlock : reportBlockList) {
            if (rtcpReceiverReportBlock == null) { continue; }

            byte[] curReportBlockData = rtcpReceiverReportBlock.getByteData();
            System.arraycopy(curReportBlockData, 0, data, index, curReportBlockData.length);
            index += curReportBlockData.length;
        }

        if (profileSpecificExtensions != null && profileSpecificExtensions.length > 0) {
            System.arraycopy(profileSpecificExtensions, 0, data, index, profileSpecificExtensions.length);
        }

        return data;
    }

    public void setData(RtcpHeader rtcpHeader, long nts, int rts, int spc, int soc, List<ReportBlock> reportBlockList, byte[] profileSpecificExtensions) {
        this.rtcpHeader = rtcpHeader;
        this.nts = nts;
        this.rts = rts;
        this.spc = spc;
        this.soc = soc;
        this.reportBlockList = reportBlockList;
        this.profileSpecificExtensions = profileSpecificExtensions;
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

    public List<ReportBlock> getReportBlockList() {
        return reportBlockList;
    }

    public ReportBlock getReportBlockByIndex(int index) {
        return reportBlockList.get(index);
    }

    public void setReportBlockList(List<ReportBlock> reportBlockList) {
        this.reportBlockList = reportBlockList;
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
