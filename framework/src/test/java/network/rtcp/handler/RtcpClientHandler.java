package network.rtcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RtcpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger logger = LoggerFactory.getLogger(RtcpClientHandler.class);

    ////////////////////////////////////////////////////////////////////////////////

    public RtcpClientHandler() {
        logger.debug("RtcpClientHandler is created.");
    }

    ////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void channelRead0 (ChannelHandlerContext ctx, DatagramPacket msg) {
        // ignore
    }

    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) {
        // ignore
    }

}
