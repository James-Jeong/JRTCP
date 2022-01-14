package network.rtcp.type.feedback.transportlayer;

import network.rtcp.type.feedback.RtcpFeedback;
import network.rtcp.type.feedback.base.RtcpFeedbackMessageHeader;
import network.rtcp.type.report.base.RtcpHeader;

public class RtcpNegativeAck extends RtcpFeedback { // Negative Acknowledgement

    /**
     * The purpose is to speed up refreshment of the
     *    video in those situations where their use is feasible.
     *
     *      0                   1                   2                   3
     *      0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     *     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     *     |V=2|P|   MBZ   | PT=RTCP_NACK  |           length              |
     *     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     *     |                              SSRC                             |
     *     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     *     |              FSN              |              BLP              |
     *     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     *
     *   First Sequence Number (FSN): 16 bits
     *      Identifies the first sequence number lost.
     *
     *    Bitmask of following lost packets (BLP): 16 bits
     *      A bit is set to 1 if the corresponding packet has been lost,
     *      and set to 0 otherwise. BLP is set to 0 only if no packet
     *      other than that being NACKed (using the FSN field) has been
     *      lost. BLP is set to 0x00001 if the packet corresponding to
     *      the FSN and the following packet have been lost, etc.
     *
     *
     *
     */

    ////////////////////////////////////////////////////////////
    // VARIABLES
    public static final int MIN_LENGTH = RtcpFeedbackMessageHeader.MIN_LENGTH; // bytes


    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public RtcpNegativeAck(RtcpFeedbackMessageHeader rtcpFeedbackMessageHeader) {
        super(rtcpFeedbackMessageHeader);
    }

    public RtcpNegativeAck() {
    }

    public RtcpNegativeAck(byte[] data) {
        super(data);
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS


    ////////////////////////////////////////////////////////////

}
