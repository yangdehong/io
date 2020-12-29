package com.ydh.redsheep.nio.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @description:
 * @author: yangdehong
 * @version: 2017/11/16.
 */
public class SelectorServiceTest {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private int keys=0;

    public static void main(String[] args) throws Exception {
        new SelectorServiceTest().start();
    }

    private void start() throws Exception {
        // 初始化
        init();
        // 监听
        listen();
    }

    private void listen() throws Exception {
        System.out.println("服务器已经启动。。。。。。");
        while (true) {
            // 选择一个通道
            keys = selector.select();
            Iterator it = selector.selectedKeys().iterator();
            if (keys>0) {
                while (it.hasNext()){
                    SelectionKey selectionKey = (SelectionKey) it.next();
                    it.remove();
                    // 客户端请求连接
                    if (selectionKey.isAcceptable()) {
                        serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                        // 获取客户端通道
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        // 返回信息
                        socketChannel.write(ByteBuffer.wrap(" 你 好 啊 ！".getBytes()));
                        // 注册读取
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int len = socketChannel.read(byteBuffer);
                        System.out.println("收到信息："+new String(byteBuffer.array(), 0, len));
                    }
                }
            } else {
                System.out.println("好了！！！！");
            }
        }
    }

    private void init() throws Exception {
        selector = Selector.open();
        // 建立通道
        serverSocketChannel = ServerSocketChannel.open();
        // 绑定访问端口
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 8888));
        //设置成非阻塞模式。在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接,返回的将是null。
        serverSocketChannel.configureBlocking(false);
        // serverSocketChannel注册到管理器，当有客户端连接时触发
        SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }


}
