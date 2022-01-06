package network.rtcp.packet;

public class RtcpPacket {

    /**
     * https://www4.cs.fau.de/Projects/JRTP/pmt/node82.html
     *
     * Each RTCP packet carries in its header one of the following packet type codes:
     *
     * 200 = SR Sender Report packet
     * 201 = RR Receiver Report packet
     * 202 = SDES Source Description packet
     * 203 = BYE Goodbye packet
     * 204 = APP Application-defined packet
     */

    public RtcpPacket() {}

}
