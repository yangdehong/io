package com.ydh.redsheep.netty.netty.sjoin.length;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @description:
 * @author: yangdehong
 * @version: 2018/1/9.
 */
public class LengthClientHandler extends ChannelInboundHandlerAdapter {


    /**
     * Creates a client-side handler.
     */
    public LengthClientHandler() {
        //TODO
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.wrappedBuffer("aaa".getBytes()));
        ctx.writeAndFlush(Unpooled.wrappedBuffer("bbbbbbbbbbbbbbbb".getBytes()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            String response = (String) msg;
            System.out.println("Client: " + response);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
