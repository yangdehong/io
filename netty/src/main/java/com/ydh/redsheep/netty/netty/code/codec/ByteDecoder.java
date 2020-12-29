package com.ydh.redsheep.netty.netty.code.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
* 字节解码器
* @author : yangdehong
* @date : 2019-10-18 09:38
*/
public class ByteDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() >= 4){
            list.add(byteBuf.readInt());
        }
    }

}
