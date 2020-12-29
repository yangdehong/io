package com.ydh.redsheep.netty.netty.reconnection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @description:
 * @author: yangdehong
 * @version: 2018/1/9.
 */
public class ReConnectionServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 在连接被建立并且准备进行通信时被调用
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }

    /**
     * 在收到消息时被调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            String body = (String) msg;
            System.out.println("Server :" + body );
            String response = "返回给客户端的响应：" + body + "$_";
            if ("ping".equals(body)) {
                response = "pong$_";
            }
            ctx.writeAndFlush(response);
            // future完成后触发监听器, 此处是写完即关闭(短连接). 因此需要关闭连接时, 要通过server端关闭.
            // 直接关闭用方法ctx[.channel()].close()
            //.addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // msg 是一个引用计数对象，这个对象必须显示地调用 release() 方法来释放。请记住处理器的职责是释放所有传递到处理器的引用计数对象
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 出现 Throwable 对象才会被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
