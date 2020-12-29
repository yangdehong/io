package com.ydh.redsheep.netty.netty.code.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
* 字节编码器
* @author : yangdehong
* @date : 2019-10-17 21:29
*/
public class ByteEncoder extends MessageToByteEncoder<Integer> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(msg);
    }
}
