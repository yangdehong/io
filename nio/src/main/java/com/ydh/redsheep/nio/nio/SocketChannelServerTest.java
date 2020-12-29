package com.ydh.redsheep.nio.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @description:
 * @author: yangdehong
 * @version: 2017/11/9.
 */
public class SocketChannelServerTest {

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    // 创建一个int缓冲区的视图
    private IntBuffer intBuffer = byteBuffer.asIntBuffer();

    private SocketChannel socketChannel;
    private ServerSocketChannel serverSocketChannel;

    public static void main(String[] args) throws Exception {
        new SocketChannelServerTest().start();

    }

    private void start() throws Exception {
        // 打开通道
        openChannel();
        // 监听客户端请求
        listenConnection();

    }

    private void listenConnection() throws Exception {
        while (true) {
            socketChannel = serverSocketChannel.accept();
            // 是否有客户端请求
            if (null != socketChannel) {
                System.out.println("新的连接加入。。。。。。");
                // 处理请求
                processConnection();
                socketChannel.close();
            }
        }
    }

    private void processConnection() throws Exception {
        System.out.println("开始处理请求");
        byteBuffer.clear();// 清理，设置position为0，cap为最大
        socketChannel.read(byteBuffer);
        int result = intBuffer.get(0) + intBuffer.get(1);
        byteBuffer.flip();
        byteBuffer.clear();
        // 修改视图 原来的缓冲区发生变化
        intBuffer.put(0, result);
        socketChannel.write(byteBuffer);
        System.out.println("处理完成");
    }

    private void openChannel() throws Exception {
        // 建立通道
        serverSocketChannel = ServerSocketChannel.open();
        // 绑定访问端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        // 设置成非阻塞模式。在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接,返回的将是null。
        serverSocketChannel.configureBlocking(false);
        System.out.println("服务器已经打开。。。。。。");
    }

}
