package com.ydh.redsheep.netty.netty.code;

import com.ydh.redsheep.io.netty.code.protostuff.ProtostuffDecoder;
import com.ydh.redsheep.io.netty.code.protostuff.ProtostuffEncoder;
import com.ydh.redsheep.io.netty.model.MessageBO;
import com.ydh.redsheep.io.netty.util.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @description: 编解码器
 * @author: yangdehong
 * @version: 2018/1/9.
 */
public class CodeServer {
    public static void main(String[] args){

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 是提供给由父管道 .channel 接收到的连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //设置日志
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            // 添加编解码. 发送自定义的类型, 而Handler的方法接收的msg参数的实际类型也是相应的自定义类了
                            // netty自带的编解码器有string,object，在handler中接收的就可以直接使用
                            // string编解码器直接编解码string类型的消息
//                            sc.pipeline().addLast("decoder", new StringDecoder());
//                            sc.pipeline().addLast("encoder", new StringEncoder());
                            // 则此解码器必须与适当的ByteToMessageDecoder、
//                            sc.pipeline().addLast("base64Decoder", new Base64Decoder());
//                            sc.pipeline().addLast("base64Encoder", new Base64Encoder());
                            // object编解码器只能编解码object的消息
//                            sc.pipeline().addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(
//                                    this.getClass().getClassLoader()
//                            )));
//                            sc.pipeline().addLast("encoder", new ObjectEncoder());
                            // 自定义编解码器
//                            sc.pipeline().addLast("decoder", new ByteDecoder());
//                            sc.pipeline().addLast("encoder", new ByteEncoder());
//                            sc.pipeline().addLast("decoder", new StringCodec());
//                            sc.pipeline().addLast("encoder", new StringCodec());
                            /**
                             * netty自带的编解码器效率不是太好，所以经常会使用下面第三方的编解码器
                             */
                            // jboss-Marshalling
//                            sc.pipeline().addLast("decoder", MarshallingCodeCFactory.buildMarshallingDecoder());
//                            sc.pipeline().addLast("encoder", MarshallingCodeCFactory.buildMarshallingEncoder());
                            // kryo序列号编译器
//                            sc.pipeline().addLast("decoder", new KryoDecoder());
//                            sc.pipeline().addLast("encoder", new KryoEncoder());
                            // protostuff
                            sc.pipeline().addLast("decoder", new ProtostuffDecoder(MessageBO.class));
                            sc.pipeline().addLast("encoder", new ProtostuffEncoder(MessageBO.class));
                            sc.pipeline().addLast(new CodeServerHandler());
                        }
                    });

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
