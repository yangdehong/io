package com.ydh.redsheep.netty.netty.code.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
* string编解码器
* @author : yangdehong
* @date : 2019-10-18 09:58
*/
public class StringCodec extends ByteToMessageCodec<String> {

    @Override
    public void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        out.writeBytes(msg.getBytes(StandardCharsets.UTF_8));

    }
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.toString(StandardCharsets.UTF_8));
    }

}
