package com.ydh.redsheep.nio.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @description:
 * @author: yangdehong
 * @version: 2017/11/16.
 */
public class DatagramChannelServiceTest {

    // UDP协议服务端
    private int port = 9975;
    DatagramChannel channel;
    private Charset charset = Charset.forName("UTF-8");
    private Selector selector;

    public DatagramChannelServiceTest() throws IOException {
        try {
            selector = Selector.open();
            channel = DatagramChannel.open();
        } catch (Exception e) {
            selector = null;
            channel = null;
            System.out.println("超时");
        }
        System.out.println("服务器启动");
    }

    /* 编码过程 */
    public ByteBuffer encode(String str) {
        return charset.encode(str);
    }

    /* 解码过程 */
    public String decode(ByteBuffer bb) {
        return charset.decode(bb).toString();
    }

    /* 服务器服务方法 */
    public void service() throws IOException {
        if (channel == null || selector == null) {
            return;
        }
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(port));
        // channel.write(ByteBuffer.wrap(new String("aaaa").getBytes()));
        channel.register(selector, SelectionKey.OP_READ);
        /** 外循环，已经发生了SelectionKey数目 */
        while (selector.select() > 0) {
            System.out.println("有新channel加入");
            /* 得到已经被捕获了的SelectionKey的集合 */
            Iterator iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = null;
                try {
                    key = (SelectionKey) iterator.next();
                    iterator.remove();

                    if (key.isReadable()) {
                        reveice(key);
                    }
                    if (key.isWritable()) {
                        // send(key);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        if (key != null) {
                            key.cancel();
                            key.channel().close();
                        }
                    } catch (ClosedChannelException cex) {
                        e.printStackTrace();
                    }
                }
            }
            /* 内循环完 */
        }
        /* 外循环完 */
    }

    /*
     * 接收 用receive()读IO
     * 作为服务端一般不需要调用connect()，如果未调用<span style="font-family: Arial, Helvetica, sans-serif;">connect()时调</span><span style="font-family: Arial, Helvetica, sans-serif;">用read()\write()读写，会报java.nio.channels</span>
     * .NotYetConnectedException 只有调用connect()之后,才能使用read和write.
     */
    synchronized public void reveice(SelectionKey key) throws IOException {
        if (key == null) {
            return;
        }
        // ***用channel.receive()获取客户端消息***//
        // ：接收时需要考虑字节长度
        DatagramChannel sc = (DatagramChannel) key.channel();
        String content = "";
        // create buffer with capacity of 48 bytes
        ByteBuffer buf = ByteBuffer.allocate(1024);// java里一个(utf-8)中文3字节,gbk中文占2个字节
        buf.clear();
        SocketAddress address = sc.receive(buf); // read into buffer. 返回客户端的地址信息
        String clientAddress = address.toString().replace("/", "").split(":")[0];
        String clientPost = address.toString().replace("/", "").split(":")[1];

        buf.flip(); // make buffer ready for read
        while (buf.hasRemaining()) {
            buf.get(new byte[buf.limit()]);// read 1 byte at a time
            content += new String(buf.array());
        }
        buf.clear(); // make buffer ready for writing
        System.out.println("接收：" + content.trim());
        // 第一次发；udp采用数据报模式，发送多少次，接收多少次
        ByteBuffer buf2 = ByteBuffer.allocate(65507);
        buf2.clear();
        buf2.put(("消息推送内容 abc..UDP是一个非连接的协议，传输数据之前源端和终端不建立连接，" +
                "当它想传送时就简单地去抓取来自应用程序的数据，并尽可能快地把它扔到网络上。在发送端UDP是一个非连接的协议，" +
                "传输数据之前源端和终端不建立连接，当它想传送时就简单地去抓取来自应用程序的数据，并尽可能快地把它扔到网络上。" +
                "在发送端UDP是一个非连接的协议，传输数据之前源端和终端不建立连接，当它想传送时就简单地去抓取来自应用程序的数据，" +
                "并尽可能快地把它扔到网络上。在发送端@Q").getBytes());
        buf2.flip();
        channel.send(buf2, new InetSocketAddress(clientAddress, Integer.parseInt(clientPost))); // 将消息回送给客户端

        // 第二次发
        ByteBuffer buf3 = ByteBuffer.allocate(65507);
        buf3.clear();
        buf3.put("任务完成".getBytes());
        buf3.flip();
        channel.send(buf3, new InetSocketAddress(clientAddress, Integer.parseInt(clientPost))); // 将消息回送给客户端
    }

    int y = 0;

    public void send(SelectionKey key) {
        if (key == null) {
            return;
        }
        // ByteBuffer buff = (ByteBuffer) key.attachment();
        DatagramChannel sc = (DatagramChannel) key.channel();
        try {
            sc.write(ByteBuffer.wrap(new String("aaaa").getBytes()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("send2() " + (++y));
    }

    /* 发送文件 */
    public void sendFile(SelectionKey key) {
        if (key == null) {
            return;
        }
        ByteBuffer buff = (ByteBuffer) key.attachment();
        SocketChannel sc = (SocketChannel) key.channel();
        String data = decode(buff);
        if (data.indexOf("get") == -1) {
            return;
        }
        String subStr = data.substring(data.indexOf(" "), data.length());
        System.out.println("截取之后的字符串是 " + subStr);
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(subStr);
            FileChannel fileChannel = fileInput.getChannel();
            fileChannel.transferTo(0, fileChannel.size(), sc);
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInput.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new DatagramChannelServiceTest().service();
    }

}
