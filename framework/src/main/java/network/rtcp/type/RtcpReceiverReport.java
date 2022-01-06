package network.rtcp.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import network.rtcp.type.base.RtcpReportBlock;
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
    private RtcpHeader rtcpHeader = null;

    // Report Block List
    private List<RtcpReportBlock> rtcpReportBlockList;

    // Profile-specific extensions
    private byte[] profileSpecificExtensions;

    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpReceiverReport(RtcpHeader rtcpHeader, List<RtcpReportBlock> rtcpReportBlockList, byte[] profileSpecificExtensions) {
        this.rtcpHeader = rtcpHeader;
        this.rtcpReportBlockList = rtcpReportBlockList;
        this.profileSpecificExtensions = profileSpecificExtensions;
    }

    public RtcpReceiverReport() {}

    public RtcpReceiverReport(byte[] data) {
        int dataLength = data.length;
        if (dataLength <= MIN_LENGTH) {
            rtcpReportBlockList = null;
        } else {
            rtcpReportBlockList = new ArrayList<>();
            int index = 0;

            // HEADER
            byte[] headerData = new byte[RtcpHeader.LENGTH];
            System.arraycopy(data, index, headerData, 0, RtcpHeader.LENGTH);
            rtcpHeader = new RtcpHeader(headerData);
            index += RtcpHeader.LENGTH;

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
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public byte[] getData() {
        if (rtcpHeader == null) {
            return null;
        }

        byte[] data = new byte[MIN_LENGTH];
        int index = 0;

        byte[] headerData = rtcpHeader.getData();
        System.arraycopy(headerData, 0, data, index, headerData.length);
        index += headerData.length;

        if (rtcpReportBlockList != null && !rtcpReportBlockList.isEmpty()) {
            byte[] newData = new byte[data.length + (rtcpReportBlockList.size() * RtcpReportBlock.LENGTH)];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;

            for (RtcpReportBlock rtcpReceiverRtcpReportBlock : rtcpReportBlockList) {
                if (rtcpReceiverRtcpReportBlock == null) {
                    continue;
                }

                byte[] curReportBlockData = rtcpReceiverRtcpReportBlock.getByteData();
                System.arraycopy(curReportBlockData, 0, data, index, curReportBlockData.length);
                index += curReportBlockData.length;
            }
        }

        if (profileSpecificExtensions != null && profileSpecificExtensions.length > 0) {
            byte[] newData = new byte[data.length + profileSpecificExtensions.length];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;

            System.arraycopy(profileSpecificExtensions, 0, data, index, profileSpecificExtensions.length);
        }

        return  data;
    }

    public void setData(RtcpHeader rtcpHeader, List<RtcpReportBlock> rtcpReportBlockList, byte[] profileSpecificExtensions) {
        this.rtcpHeader = rtcpHeader;
        this.rtcpReportBlockList = rtcpReportBlockList;
        this.profileSpecificExtensions = profileSpecificExtensions;
    }

    public RtcpHeader getRtcpHeader() {
        return rtcpHeader;
    }

    public void setRtcpHeader(RtcpHeader rtcpHeader) {
        this.rtcpHeader = rtcpHeader;
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
