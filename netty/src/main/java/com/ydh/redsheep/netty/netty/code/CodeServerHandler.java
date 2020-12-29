package com.ydh.redsheep.netty.netty.code;

import com.ydh.redsheep.io.netty.model.MessageBO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @description:
 * @author: yangdehong
 * @version: 2018/1/9.
 */
public class CodeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
//            String resp = (String) msg;
//            System.out.println("Client : " + resp);
//            ctx.writeAndFlush("我是从server过来的");

            MessageBO resp = (MessageBO) msg;
            System.out.println("Client : " + resp.getId() + ", " + resp.getContent());
            ctx.writeAndFlush(new MessageBO(2, "我是从server过来的"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 一旦一个消息被编码或解码会自动调用 ReferenceCountUtil.release(message)。
            // 如果你稍后还需要用到这个引用，你可以调用 ReferenceCountUtil.retain(message)。
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
