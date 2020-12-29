package com.ydh.redsheep.netty.netty.code.kryo;

import com.ydh.redsheep.netty.netty.model.MessageBO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
*
* @author : yangdehong
* @date : 2019-10-18 11:03
*/
public class KryoDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        Object msg = KryoSerializer.deserialize(in, MessageBO.class);
        out.add(msg);
    }

}
