package com.ydh.redsheep.nio.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SocketChannel;

/**
 * @description:
 * @author: yangdehong
 * @version: 2017/11/9.
 */
public class SocketChannelClientTest {

    private ByteBuffer byteBuffer = ByteBuffer.allocate(8);
    // 创建一个int缓冲区的视图
    private IntBuffer intBuffer = byteBuffer.asIntBuffer();

    private SocketChannel socketChannel;

    public static void main(String[] args) throws Exception {
        System.out.println(new SocketChannelClientTest().getSum(31, 22));
    }

    private int getSum(int a, int b) throws Exception {
        // 创建连接
        socketChannel = connection();
        // 发送请求
        sendData(a, b);
        // 获取回调数据
        return recvData();
    }

    private int recvData() throws Exception {
        byteBuffer.clear();
        socketChannel.read(byteBuffer);
        return intBuffer.get(0);
    }

    private void sendData(int a, int b) throws Exception {
        byteBuffer.clear();
        intBuffer.put(a);
        intBuffer.put(b);
        socketChannel.write(byteBuffer);
        System.out.println("请求：" + a + "+" + b + "=");
    }

    private SocketChannel connection() throws Exception {
        return SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
    }

}
