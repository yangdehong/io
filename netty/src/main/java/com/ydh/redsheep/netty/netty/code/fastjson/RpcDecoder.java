package com.ydh.redsheep.netty.netty.code.fastjson;

import com.ydh.redsheep.netty.netty.code.fastjson.serializer.Serializer;
import com.ydh.redsheep.netty.netty.model.MessageBO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> clazz;

    private Serializer serializer;

    public RpcDecoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 标记一下当前的readIndex的位置
        byteBuf.markReaderIndex();

        // 读取数据长度
        int dataLength = byteBuf.readInt();
        // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
        if (dataLength < 0) {
            System.out.println("ERROR!");
            ctx.close();
        }

        //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex.
        // 这个配合markReaderIndex使用的。
        // 把readIndex重置到mark的地方
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }

        // 将缓冲区的数据读到字节数组
        byte[] body = new byte[dataLength];
        byteBuf.readBytes(body);
        //将byte数据转化为我们需要的对象。
        MessageBO messageBO = serializer.deserialize(MessageBO.class, body);
        list.add(messageBO);
    }

}

