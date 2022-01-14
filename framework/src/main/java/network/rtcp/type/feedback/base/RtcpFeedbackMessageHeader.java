package network.rtcp.type.feedback.base;

public class RtcpFeedbackMessageHeader {

    /**
     *      ACK
     *    feedback
     *      V
     *      :<- - - -  NACK feedback - - - ->//
     *      :
     *      :   Immediate   ||
     *      : Feedback mode ||Early RTCP mode   Regular RTCP mode
     *      :<=============>||<=============>//<=================>
     *      :               ||
     *     -+---------------||---------------//------------------> group size
     *      2               ||
     *       Application-specific FB Threshold
     *          = f(data rate, packet loss, codec, ...)
     *
     */

}
