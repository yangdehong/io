package com.ydh.redsheep.nio.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @description:
 * @author: yangdehong
 * @version: 2017/11/16.
 */
public class SelectorClientTest {

    private Selector selector;
    private ByteBuffer outBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer inBuffer = ByteBuffer.allocate(1024);
    private int keys=0;
    private SocketChannel socketChannel;

    public static void main(String[] args) throws Exception {
        new SelectorClientTest().start();
    }

    private void start() throws Exception {
        // 初始化
        init();
        // 监听
        listen();
    }

    private void listen() throws Exception {
        while (true) {
            // 选择一个通道
            keys = selector.select();
            if (keys>0) {
                Iterator it = selector.selectedKeys().iterator();
                while (it.hasNext()){
                    SelectionKey selectionKey = (SelectionKey) it.next();
                    // 客户端请求连接
                    if (selectionKey.isConnectable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        // 如果正在连接就连接完成
                        if (socketChannel.isConnectionPending()) {
                            socketChannel.finishConnect();
                            System.out.println("完成");
                        }
                        // 注册写
                        socketChannel.register(selector, SelectionKey.OP_WRITE);
                    } else if (selectionKey.isWritable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        outBuffer.clear();
                        System.out.println("client正在写数据。。。。。。");
                        socketChannel.write(outBuffer.wrap("我是clientA".getBytes()));
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("client完成写数据。。。。。。");

                    } else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        inBuffer.clear();
                        System.out.println("client正在读数据。。。。。。");
                        socketChannel.read(inBuffer);
                        System.out.println("==>"+inBuffer.array());
                        System.out.println("client完成读数据。。。。。。");
                    }
                }
            }
        }

    }

    private void init() throws Exception {
        // 获得通道管理器
        selector = Selector.open();
        // 获取通道
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        // 绑定访问端口
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }








}
