package network.rtcp.type;

import network.rtcp.base.RtcpFormat;
import network.rtcp.base.RtcpHeader;

public class RtcpFullIntraRequest extends RtcpFormat {

    /**
     * FIR is also known as an "instantaneous decoder refresh request",
     *    "fast video update request" or "video fast update request".
     *
     *    Long-standing experience of the conversational video
     *    conferencing industry suggests that there is a need for a few
     *    additional feedback messages, to support centralized multipoint
     *    conferencing efficiently.
     *
     *    Some of the messages have applications
     *    beyond centralized multipoint, and this is indicated in the
     *    description of the message.
     *
     *    A Full Intra Request (FIR) Command, when received by the designated
     *    media sender, requires that the media sender sends a Decoder Refresh
     *    Point (see section 2.2) at the earliest opportunity.
     *    The evaluation of such an opportunity includes the current encoder coding strategy
     *    and the current available network resources.
     */

    ////////////////////////////////////////////////////////////
    // VARIABLES
    public static final int MIN_LENGTH = RtcpHeader.LENGTH; // bytes


    ////////////////////////////////////////////////////////////


}
