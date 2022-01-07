package network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class ChannelHandlerMaker {

    public static ChannelInitializer<NioDatagramChannel> get(SimpleChannelInboundHandler<DatagramPacket> handler) {
        return new ChannelInitializer<NioDatagramChannel>() {
            @Override
            protected void initChannel(NioDatagramChannel nioDatagramChannel) {
                final ChannelPipeline channelPipeline = nioDatagramChannel.pipeline();
                channelPipeline.addLast(handler);
            }
        };
    }

}
