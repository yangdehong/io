package com.ydh.redsheep.netty.netty.reconnection;

import com.ydh.redsheep.netty.netty.util.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @description: 简单的传输数据
 * @author: yangdehong
 * @version: 2018/1/9.
 */
public class ReConnectionServer {

    public static void main(String[] args) {

        new ReConnectionServer().run();
    }

    public void run() {
        // 用来处理I/O操作的多线程事件循环器
        // 第一个经常被叫做‘boss’，用来接收进来的连接，第二个经常被叫做‘worker’，用来处理已经被接收的连接，
        // 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 一个启动 NIO 服务的辅助启动类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    // 指定channel
                    .channel(NioServerSocketChannel.class)
                    // 指定的 Channel 实现的配置参数，是提供给NioServerSocketChannel 用来接收进来的连接
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 是提供给由父管道 .channel 接收到的连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 设置日志
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    // 处理一个最近的已经接收的 Channel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) {
                            // 时限，读客户端超时没数据则断开
                            ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                            sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                            sc.pipeline().addLast("decoder", new StringDecoder());
                            sc.pipeline().addLast("encoder", new StringEncoder());
                            sc.pipeline().addLast(new ReConnectionServerHandler());
                        }
                    });

            // 绑定端口
            ChannelFuture cf = b.bind(Constants.PORT).sync();
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
