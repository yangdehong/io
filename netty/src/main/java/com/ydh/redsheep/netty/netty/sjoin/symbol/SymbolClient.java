package com.ydh.redsheep.netty.netty.sjoin.symbol;

import com.ydh.redsheep.netty.netty.util.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @description:
 * @author: yangdehong
 * @version: 2018/1/9.
 */
public class SymbolClient {

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                        p.addLast(new DelimiterBasedFrameDecoder(1024, buf));
                        p.addLast(new StringDecoder());
                        p.addLast(new SymbolClientHandler());
                    }
                });

            // 启动客户端
            ChannelFuture f = b.connect(Constants.HOST, Constants.PORT).sync();

            // 等待直到连接关闭
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }
}



