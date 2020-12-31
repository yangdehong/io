package com.ydh.redsheep.netty.netty.sjoin.length;

import com.ydh.redsheep.netty.netty.util.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @description: 消息定长. 固定包的长度, 长度不够用空格补全. 接收方需要trim, 效率不高不推荐
 * @author: yangdehong
 * @version: 2018/1/9.
 */
public class LengthServer {

    public static void main(String[] args) throws Exception {
        // 1 创建线两个事件循环组
        // 一个是用于处理服务器端接收客户端连接的
        // 一个是进行网络通信的（网络读写的）
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // 指定NIO的模式.NioServerSocketChannel对应TCP, NioDatagramChannel对应UDP
                    .option(ChannelOption.SO_BACKLOG, 1024)// 设置TCP缓冲区
                    .option(ChannelOption.SO_SNDBUF, 32*1024) // 设置发送缓冲大小
                    .option(ChannelOption.SO_RCVBUF, 32*1024) // 这是接收缓冲大小
                    .option(ChannelOption.SO_KEEPALIVE, true) // 保持连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception { //SocketChannel建立连接后的管道
                            ChannelPipeline p = ch.pipeline();
                            //设置定长字符串接收, 定长为5, 积累到5个字节才会把数据发出去
                            p.addLast(new FixedLengthFrameDecoder(5));
                            //设置字符串形式的解码
                            p.addLast("decoder", new StringDecoder());
                            p.addLast("encoder", new StringEncoder());
                            p.addLast(new LengthServerHandler());
                        }
                    });

            // 启动服务端
            ChannelFuture f = b.bind(Constants.PORT).sync();

            // 等待直到关闭
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
