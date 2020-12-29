package com.ydh.redsheep.netty.netty.echo;

import com.ydh.redsheep.netty.netty.util.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @description:
 * @author: yangdehong
 * @version: 2018/1/9.
 */
public class EchoClient {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 设置日志
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            // 启动客户端
            ChannelFuture f = b.connect(Constants.HOST, Constants.PORT).sync();

            f.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));
            f.channel().writeAndFlush(Unpooled.copiedBuffer("666".getBytes()));
            Thread.sleep(2000);
            f.channel().writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));

            // 等待直到连接关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}



