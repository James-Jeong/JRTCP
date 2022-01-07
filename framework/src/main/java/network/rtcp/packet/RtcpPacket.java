package network.rtcp.packet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import network.rtcp.base.RtcpFormat;
import network.rtcp.base.RtcpHeader;
import network.rtcp.base.RtcpPacketPaddingResult;
import network.rtcp.base.RtcpType;
import network.rtcp.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class RtcpPacket {

    private static final Logger logger = LoggerFactory.getLogger(RtcpPacket.class);

    /**
     * - Reference
     * https://datatracker.ietf.org/doc/html/rfc1889
     * https://www4.cs.fau.de/Projects/JRTP/pmt/node82.html
     * https://www.freesoft.org/CIE/RFC/1889/13.htm
     *
     * - Each RTCP packet carries in its header one of the following packet type codes:
     * 1) 200 = SR (Sender Report packet)
     * 2) 201 = RR (Receiver Report packet)
     * 3) 202 = SDES (Source Description packet)
     * 4) 203 = BYE (Goodbye packet)
     * 5) 204 = APP (Application-defined packet)
     */

    ////////////////////////////////////////////////////////////
    // VARIABLES
    public static final int PACKET_MULTIPLE = 4; // 32 bits word

    private RtcpHeader rtcpHeader = null;
    private RtcpFormat rtcpFormat = null;
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpPacket(RtcpHeader rtcpHeader, RtcpFormat rtcpFormat) {
        this.rtcpHeader = rtcpHeader;
        this.rtcpFormat = rtcpFormat;
    }

    public RtcpPacket() {}

    public RtcpPacket(byte[] data) {
        if (data.length >= RtcpHeader.LENGTH) {
            byte[] headerData = new byte[RtcpHeader.LENGTH];
            System.arraycopy(data, 0, headerData, 0, RtcpHeader.LENGTH);

            rtcpHeader = new RtcpHeader(headerData);
            rtcpFormat = getRtcpFormatByByteData(rtcpHeader.getPacketType(), data);
        }
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public static RtcpFormat getRtcpFormatByByteData(int packetType, byte[] data) {
        RtcpFormat rtcpFormat = null;

        switch (packetType) {
            case RtcpType.SENDER_REPORT:
                rtcpFormat = new RtcpSenderReport(data);
                break;
            case RtcpType.RECEIVER_REPORT:
                rtcpFormat = new RtcpReceiverReport(data);
                break;
            case RtcpType.SOURCE_DESCRIPTION:
                rtcpFormat = new RtcpSourceDescription(data);
                break;
            case RtcpType.GOOD_BYE:
                rtcpFormat = new RtcpBye(data);
                break;
            case RtcpType.APPLICATION_DEFINED:
                rtcpFormat = new RtcpApplicationDefined(data);
                break;
            default:
                logger.warn("UNKNOWN RTCP TYPE ({})", packetType);
                break;
        }

        return rtcpFormat;
    }

    public byte[] getData() {
        if (rtcpHeader == null || rtcpFormat == null) {
            return null;
        }

        byte[] data = new byte[RtcpHeader.LENGTH];
        int index = 0;

        // HEADER
        byte[] headerData = rtcpHeader.getData();
        System.arraycopy(headerData, 0, data, index, headerData.length);
        index += headerData.length;

        byte[] rtcpFormatData = rtcpFormat.getData();
        if (rtcpFormatData != null && rtcpFormatData.length > 0) {
            // Check the validation of the format data size > The size must be a 32 bits word multiple!
            int rtcpFormatDataLength = rtcpFormatData.length;
            int paddingBytes = rtcpHeader.getPaddingBytes();
            if (paddingBytes > 0) {
                byte[] newRtcpFormatData = new byte[rtcpFormatDataLength + paddingBytes];
                Arrays.fill(newRtcpFormatData, (byte) 0);
                System.arraycopy(rtcpFormatData, 0, newRtcpFormatData, 0, rtcpFormatDataLength);
                rtcpFormatData = newRtcpFormatData;
                rtcpFormatDataLength = rtcpFormatData.length;
            }

            byte[] newData = new byte[RtcpHeader.LENGTH + rtcpFormatDataLength];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;

            System.arraycopy(rtcpFormatData, 0, data, index, rtcpFormatDataLength);
        }

        return data;
    }

    public void setData(RtcpFormat rtcpFormat) {
        if (rtcpFormat == null) { return; }

        this.rtcpFormat = rtcpFormat;
    }

    public RtcpHeader getRtcpHeader() {
        return rtcpHeader;
    }

    public void setRtcpHeader(RtcpHeader rtcpHeader) {
        this.rtcpHeader = rtcpHeader;
    }

    public RtcpFormat getRtcpFormat() {
        return rtcpFormat;
    }

    public void setRtcpFormat(RtcpFormat rtcpFormat) {
        this.rtcpFormat = rtcpFormat;
    }

    public static RtcpPacketPaddingResult getPacketLengthByBytes(int bytes) {
        RtcpPacketPaddingResult rtcpPacketPaddingResult = new RtcpPacketPaddingResult();

        // 1) 데이터가 없으므로 0 을 반환
        if (bytes <= 0) { return rtcpPacketPaddingResult; }

        // 헤더가 8 바이트 고정이므로 기본적으로 패킷으로 구성되려면
        // 헤더를 포함한 전체 데이터 크기(Header + Body + Padding)는 8 바이트 초과되어야만 한다.
        bytes += RtcpHeader.LENGTH;
        int remainderBytesByMultiple = bytes % PACKET_MULTIPLE;
        if (remainderBytesByMultiple != 0) {
            int paddingLength = PACKET_MULTIPLE - remainderBytesByMultiple;
            bytes += paddingLength;
            rtcpPacketPaddingResult.setPaddingBytes(paddingLength);
            rtcpPacketPaddingResult.setPadding(true);
        }

        int dividedBytesByMultiple = bytes / PACKET_MULTIPLE;
        // 2) 헤더를 포함한 전체 데이터 크기가 8 바이트 초과인 경우, 1 감소한 값을 반환
        if (dividedBytesByMultiple > 1) {
            rtcpPacketPaddingResult.setLength(dividedBytesByMultiple - 1);
            return rtcpPacketPaddingResult;
        }
        // 3) 헤더만 포함되어있거나 의미없는 데이터가 포함된 경우, 0 을 반환
        else {
            return rtcpPacketPaddingResult;
        }
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    ////////////////////////////////////////////////////////////

}
