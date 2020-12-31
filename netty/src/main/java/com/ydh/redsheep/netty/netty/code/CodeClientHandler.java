package com.ydh.redsheep.netty.netty.code;

import com.ydh.redsheep.netty.netty.model.MessageBO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @description:
 * @author: yangdehong
 * @version: 2018/1/9.
 */
public class CodeClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
//            String resp = (String) msg;
//            System.out.println("Client : " + resp);

            MessageBO resp = (MessageBO) msg;
            System.out.println("Client : " + resp.getId() + ", " + resp.getContent());
        }catch (Exception e){
            e.printStackTrace();
        } finally {
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
