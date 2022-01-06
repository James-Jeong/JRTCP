package network.rtcp.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import network.rtcp.base.RtcpHeader;
import network.rtcp.type.base.sdes.SdesChunk;

import java.util.ArrayList;
import java.util.List;

public class RtcpSourceDescription {

    /**
     *  0                   1                   2                   3
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |V=2|P|    SC   |  PT=SDES=202  |             length            | header
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     * |                          SSRC/CSRC_1                          | chunk
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+   1
     * |                           SDES items                          |
     * |                              ...                              |
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     * |                          SSRC/CSRC_2                          | chunk
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+   2
     * |                           SDES items                          |
     * |                              ...                              |
     * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
     *
     *  0               1               2               3
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |    CNAME=1    |     length    | user and domain name         ...
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     */

    ////////////////////////////////////////////////////////////
    // VARIABLES
    public static final int MIN_LENGTH = RtcpHeader.LENGTH; // bytes
    public static final int CHUNK_LIMIT = 31; // bytes (2^5 - 1, 5 is the bits of resource count of header)

    // RTCP HEADER
    private RtcpHeader rtcpHeader = null;

    // SDES CHUNK LIST
    private List<SdesChunk> sdesChunkList = null; // Limit 31 chunks
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpSourceDescription(RtcpHeader rtcpHeader, List<SdesChunk> sdesChunkList) {
        this.rtcpHeader = rtcpHeader;
        this.sdesChunkList = sdesChunkList;
    }

    public RtcpSourceDescription() {}

    public RtcpSourceDescription(byte[] data) {
        if (data.length >= MIN_LENGTH) {
            int index = 0;

            // HEADER
            byte[] headerData = new byte[RtcpHeader.LENGTH];
            System.arraycopy(data, index, headerData, 0, RtcpHeader.LENGTH);
            rtcpHeader = new RtcpHeader(headerData);
            index += RtcpHeader.LENGTH;

            int limitChunkSize = rtcpHeader.getResourceCount();
            if (limitChunkSize > CHUNK_LIMIT) {
                sdesChunkList = new ArrayList<>(CHUNK_LIMIT);
            } else {
                sdesChunkList = new ArrayList<>(limitChunkSize);
            }

            // SDES CHUNK LIST
            // Each chunk consists of an SSRC/CSRC identifier followed by a list of
            //   zero or more items, which carry information about the SSRC/CSRC. Each
            //   chunk starts on a 32-bit boundary. Each item consists of an 8-bit
            //   type field, an 8-bit octet count describing the length of the text
            //   (thus, not including this two-octet header), and the text itself.
            //   Note that the text can be no longer than 255 octets (bytes), but this is
            //   consistent with the need to limit RTCP bandwidth consumption.
            // 1) Chunk 개수는 헤더에서 결정
            // 2) Chunk 는 SSRC 로 구분
            // 3) Chunk 는 마지막 바이트가 0 인 것으로 구분
            int remainDataLength = data.length - index;
            if (limitChunkSize > 0 && remainDataLength > 0) {
                List<Integer> chunkPositionList = new ArrayList<>(limitChunkSize);
                for (int i = index; i < data.length; i++) {
                    if (data[i] == 0) {
                        chunkPositionList.add(i);
                    }
                }

                int curChunkDataLength;
                for (int i = 0; i < limitChunkSize; i++) {
                    if (i + 1 >= limitChunkSize) { // 마지막인 chunk 인 경우
                        curChunkDataLength = data.length - chunkPositionList.get(i);
                    } else {
                        curChunkDataLength = chunkPositionList.get(i + 1);
                    }

                    if (curChunkDataLength > 0) {
                        byte[] curChunkData = new byte[curChunkDataLength];
                        System.arraycopy(data, index, curChunkData, 0, curChunkDataLength);
                        SdesChunk sdesChunk = new SdesChunk(curChunkData);
                        sdesChunkList.add(sdesChunk);
                    }
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

        // SdesChunk List
        if (sdesChunkList != null && !sdesChunkList.isEmpty()) {
            int totalSdesChunkSize = 0;
            for (SdesChunk sdesChunk : sdesChunkList) {
                byte[] sdesItemData = sdesChunk.getData();
                totalSdesChunkSize += sdesItemData.length;
            }

            byte[] newData = new byte[data.length + totalSdesChunkSize];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;

            for (SdesChunk sdesChunk : sdesChunkList) {
                if (sdesChunk == null) { continue; }

                byte[] sdesChunkData = sdesChunk.getData();
                System.arraycopy(sdesChunkData, 0, data, index, sdesChunkData.length);
                index += sdesChunkData.length;
            }
        }

        return data;
    }

    public void setData(RtcpHeader rtcpHeader, List<SdesChunk> sdesChunkList) {
        this.rtcpHeader = rtcpHeader;
        this.sdesChunkList = sdesChunkList;
    }

    public RtcpHeader getRtcpHeader() {
        return rtcpHeader;
    }

    public void setRtcpHeader(RtcpHeader rtcpHeader) {
        this.rtcpHeader = rtcpHeader;
    }

    public List<SdesChunk> getSdesChunkList() {
        return sdesChunkList;
    }

    public SdesChunk getSdesChunkByIndex(int index) {
        if (sdesChunkList == null || index < 0 || index >= sdesChunkList.size()) { return null; }
        return sdesChunkList.get(index);
    }

    public void setSdesChunkList(List<SdesChunk> sdesChunkList) {
        this.sdesChunkList = sdesChunkList;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    ////////////////////////////////////////////////////////////

}
