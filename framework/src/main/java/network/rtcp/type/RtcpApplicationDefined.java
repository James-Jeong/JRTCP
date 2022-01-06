package network.rtcp.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import network.rtcp.base.RtcpHeader;
import util.module.ByteUtil;

import java.nio.charset.StandardCharsets;

public class RtcpApplicationDefined {

    /**
     *  0               1               2               3
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |V=2|P|   ST    |   PT=APP=204  |            length L           |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                              SSRC                             |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                         name (ASCII) N                        |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                  application-dependent data A                ...
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     */

    ////////////////////////////////////////////////////////////
    // VARIABLES
    public static final int MIN_LENGTH = RtcpHeader.LENGTH + ByteUtil.NUM_BYTES_IN_INT; // bytes

    // RTCP HEADER
    private RtcpHeader rtcpHeader = null;

    // NAME
    // - A name chosen by the person defining the set of APP packets to be unique
    // with respect to other APP packets this application might receive.
    // - The application creator might choose to use the application name,
    // and then coordinate the allocation of subtype values to others
    // who want to define new packet types for the application.
    // - Alternatively, it is recommended that others choose a name based on the entity they represent,
    // then coordinate the use of the name within that entity.
    // - The name is interpreted as a sequence of four ASCII characters,
    // with uppercase and lowercase characters treated as distinct.
    private String name = null; // ASCII characters, 4 octets (4 bytes)

    // APPLICATION-DEPENDENT DATA
    // optional (variable length, must be a multiple of 32 bits long)
    private byte[] applicationDependentData;
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpApplicationDefined(RtcpHeader rtcpHeader, String name, byte[] applicationDependentData) {
        this.rtcpHeader = rtcpHeader;
        this.name = name;
        this.applicationDependentData = applicationDependentData;
    }

    public RtcpApplicationDefined() {}

    public RtcpApplicationDefined(byte[] data) {
        if (data.length >= MIN_LENGTH) {
            int index = 0;

            // HEADER
            byte[] headerData = new byte[RtcpHeader.LENGTH];
            System.arraycopy(data, index, headerData, 0, RtcpHeader.LENGTH);
            rtcpHeader = new RtcpHeader(headerData);
            index += RtcpHeader.LENGTH;

            // NAME
            byte[] nameData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, nameData, 0, ByteUtil.NUM_BYTES_IN_INT);
            name = new String(nameData, StandardCharsets.UTF_8);
            index += nameData.length;

            // APPLICATION-DEPENDENT DATA
            int remainLength = data.length - index;
            if (remainLength > 0) {
                applicationDependentData = new byte[remainLength];
                System.arraycopy(data, index, applicationDependentData, 0, remainLength);
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

        // NAME
        byte[] nameData = name.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(nameData, 0, data, index, nameData.length);
        index += nameData.length;

        // APPLICATION_DEPENDENT DATA
        if (applicationDependentData != null && applicationDependentData.length > 0) {
            System.arraycopy(applicationDependentData, 0, data, index, applicationDependentData.length);
        }

        return data;
    }

    public void setData(RtcpHeader rtcpHeader, String name, byte[] applicationDependentData) {
        this.rtcpHeader = rtcpHeader;
        this.name = name;
        this.applicationDependentData = applicationDependentData;
    }

    public RtcpHeader getRtcpHeader() {
        return rtcpHeader;
    }

    public void setRtcpHeader(RtcpHeader rtcpHeader) {
        this.rtcpHeader = rtcpHeader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getApplicationDependentData() {
        return applicationDependentData;
    }

    public void setApplicationDependentData(byte[] applicationDependentData) {
        this.applicationDependentData = applicationDependentData;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    ////////////////////////////////////////////////////////////

}
