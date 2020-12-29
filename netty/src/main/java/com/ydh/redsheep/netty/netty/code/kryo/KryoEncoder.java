package com.ydh.redsheep.netty.netty.code.kryo;

import com.ydh.redsheep.netty.netty.model.MessageBO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
*
* @author : yangdehong
* @date : 2019-10-18 11:02
*/
public class KryoEncoder extends MessageToByteEncoder<MessageBO> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageBO msg, ByteBuf out) {
        // 1. 将对象转换为byte
        KryoSerializer.serialize(msg, out);
    }

}
