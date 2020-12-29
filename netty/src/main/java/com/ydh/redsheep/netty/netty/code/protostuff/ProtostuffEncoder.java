package com.ydh.redsheep.netty.netty.code.protostuff;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtostuffEncoder extends MessageToByteEncoder {

    private Class<?> clazz;

    public ProtostuffEncoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out)  {
        if (clazz.isInstance(in)) {
            byte[] data = ProtostuffSerializer.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }


}
