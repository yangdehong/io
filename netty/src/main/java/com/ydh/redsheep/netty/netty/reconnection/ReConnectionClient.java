package com.ydh.redsheep.netty.netty.reconnection;

import com.ydh.redsheep.io.netty.util.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: yangdehong
 * @version: 2018/1/9.
 */
public class ReConnectionClient {

    public static void main(String[] args) {
        new ReConnectionClient().start();
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // 设置日志
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            // 时限，读客户端超时没数据则断开
                            ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                            ch.pipeline().addLast(new IdleStateHandler(10, 5, 60, TimeUnit.SECONDS));
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new ReConnectionClientHandler());
                        }
                    });

            // 启动客户端，在断线重连中，不能使用sync
            ChannelFuture f = b.connect(Constants.HOST, Constants.PORT);

            //断线重连
            f.addListener((ChannelFuture channelFuture) -> {
                if (!channelFuture.isSuccess()) {
                    EventLoop loop = channelFuture.channel().eventLoop();
                    loop.schedule(() -> {
                        System.out.println("服务端链接不上，开始重连操作...");
                        start();
                    }, 1L, TimeUnit.SECONDS);
                } else {
                    System.out.println("服务端链接成功...");
                }
            });

            f.channel();
            // 等待直到连接关闭
//            f.channel().closeFuture().sync(); // 在断线重连中，不能使用sync
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            group.shutdownGracefully(); // 在断线重连中，不能关闭
        }
    }

}



