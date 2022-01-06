package network.rtcp.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import network.rtcp.type.base.ReportBlock;
import network.rtcp.base.RtcpHeader;

import java.util.ArrayList;
import java.util.List;

public class RtcpReceiverReport {

    /**
     *  0                   1                   2                   3
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |V=2|P|    RC   |   PT=RR=201   |             length            | header
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                     SSRC of packet sender                     |
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     * |                 SSRC_1 (SSRC of first source)                 | report
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ block
     * | fraction lost |       cumulative number of packets lost       |   1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
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
    public static final int MIN_LENGTH = RtcpHeader.LENGTH; // bytes

    // RTCP HEADER
    private RtcpHeader rtcpHeader;

    // Report Block List
    private List<ReportBlock> reportBlockList;

    // Profile-specific extensions
    private byte[] profileSpecificExtensions;

    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpReceiverReport(RtcpHeader rtcpHeader, List<ReportBlock> reportBlockList, byte[] profileSpecificExtensions) {
        this.rtcpHeader = rtcpHeader;
        this.reportBlockList = reportBlockList;
        this.profileSpecificExtensions = profileSpecificExtensions;
    }

    public RtcpReceiverReport() {}

    public RtcpReceiverReport(byte[] data) {
        int dataLength = data.length;
        if (dataLength <= MIN_LENGTH) {
            reportBlockList = null;
        } else {
            reportBlockList = new ArrayList<>();
            int index = 0;

            // HEADER
            byte[] headerData = new byte[RtcpHeader.LENGTH];
            System.arraycopy(data, index, headerData, 0, RtcpHeader.LENGTH);
            rtcpHeader = new RtcpHeader(headerData);
            index += RtcpHeader.LENGTH;

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
    public byte[] getByteData() {
        byte[] data = new byte[MIN_LENGTH + (reportBlockList.size() * ReportBlock.LENGTH) + profileSpecificExtensions.length];
        int index = 0;

        byte[] headerData = rtcpHeader.getData();
        System.arraycopy(headerData, 0, data, index, headerData.length);
        index += headerData.length;

        for (ReportBlock rtcpReceiverReportBlock : reportBlockList) {
            if (rtcpReceiverReportBlock == null) { continue; }

            byte[] curReportBlockData = rtcpReceiverReportBlock.getByteData();
            System.arraycopy(curReportBlockData, 0, data, index, curReportBlockData.length);
            index += curReportBlockData.length;
        }

        if (profileSpecificExtensions != null && profileSpecificExtensions.length > 0) {
            System.arraycopy(profileSpecificExtensions, 0, data, index, profileSpecificExtensions.length);
        }

        return  data;
    }

    public void setData(RtcpHeader rtcpHeader, List<ReportBlock> reportBlockList, byte[] profileSpecificExtensions) {
        this.rtcpHeader = rtcpHeader;
        this.reportBlockList = reportBlockList;
        this.profileSpecificExtensions = profileSpecificExtensions;
    }

    public RtcpHeader getRtcpHeader() {
        return rtcpHeader;
    }

    public void setRtcpHeader(RtcpHeader rtcpHeader) {
        this.rtcpHeader = rtcpHeader;
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
