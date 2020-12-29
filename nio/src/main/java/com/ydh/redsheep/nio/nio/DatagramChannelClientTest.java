package com.ydh.redsheep.nio.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
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
public class DatagramChannelClientTest {
    // UDP协议客户端
    private String serverIp = "127.0.0.1";
    private int port = 9975;
    // private ServerSocketChannel serverSocketChannel;
    DatagramChannel channel;
    private Charset charset = Charset.forName("UTF-8");
    private Selector selector = null;

    public DatagramChannelClientTest() throws IOException {
        try {
            selector = Selector.open();
            channel = DatagramChannel.open();
        } catch (Exception e) {
            selector = null;
            channel = null;
            System.out.println("超时");
        }
        System.out.println("客户器启动");
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
        channel.connect(new InetSocketAddress(serverIp, port));// 连接服务端
        channel.write(ByteBuffer.wrap(new String("客户端请求获取消息").getBytes()));
        channel.register(selector, SelectionKey.OP_READ);
        /** 外循环，已经发生了SelectionKey数目 */
        while (selector.select() > 0) {
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

//  /*
//   * 接收   用read()读IO
//   *  */
//  synchronized public void reveice2(SelectionKey key) throws IOException {
//      if (key == null)
//          return;
//      // ***用channel.read()获取消息***//
//      // ：接收时需要考虑字节长度
//      DatagramChannel sc = (DatagramChannel) key.channel();
//      String content = "";
//      // create buffer with capacity of 48 bytes
//      ByteBuffer buf = ByteBuffer.allocate(3);// java里一个(utf-8)中文3字节,gbk中文占2个字节
//      int bytesRead = sc.read(buf); //read into buffer.
//
//      while (bytesRead >0) {
//        buf.flip();  //make buffer ready for read
//        while(buf.hasRemaining()){
//            buf.get(new byte[buf.limit()]); // read 1 byte at a time
//            content += new String(buf.array());
//        }
//        buf.clear(); //make buffer ready for writing
//        bytesRead = sc.read(buf);
//      }
//      System.out.println("接收：" + content);
//  }

    /* 接收 */
    synchronized public void reveice(SelectionKey key) throws IOException {
        String threadName = Thread.currentThread().getName();
        if (key == null) {
            return;
        }
        try {
            // ***用channel.receive()获取消息***//
            // ：接收时需要考虑字节长度
            DatagramChannel sc = (DatagramChannel) key.channel();
            String content = "";
            //第一次接；udp采用数据报模式，发送多少次，接收多少次
            ByteBuffer buf = ByteBuffer.allocate(65507);// java里一个(utf-8)中文3字节,gbk中文占2个字节
            buf.clear();
            SocketAddress address = sc.receive(buf); // read into buffer.
            String clientAddress = address.toString().replace("/", "").split(":")[0];
            String clientPost = address.toString().replace("/", "").split(":")[1];
            System.out.println(threadName + "\t" + address.toString());
            buf.flip(); // make buffer ready for read
            while (buf.hasRemaining()) {
                buf.get(new byte[buf.limit()]);// read 1 byte at a time
                byte[] tmp = buf.array();
                content += new String(tmp);
            }
            buf.clear(); // make buffer ready for writing次
            System.out.println(threadName + "接收：" + content.trim());
            //第二次接
            content = "";
            ByteBuffer buf2 = ByteBuffer.allocate(65507);// java里一个(utf-8)中文3字节,gbk中文占2个字节
            buf2.clear();
            SocketAddress address2 = sc.receive(buf2); // read into buffer.
            buf2.flip(); // make buffer ready for read
            while (buf2.hasRemaining()) {
                buf2.get(new byte[buf2.limit()]);// read 1 byte at a time
                byte[] tmp = buf2.array();
                content += new String(tmp);
            }
            buf2.clear(); // make buffer ready for writing次
            System.out.println(threadName + "接收2：" + content.trim());

        } catch (PortUnreachableException ex) {
            System.out.println(threadName + "服务端端口未找到!");
        }
        send(2);
    }

    boolean flag = false;

    public void send(int i) {
        if (flag) {
            return;
        }
        try {
            // channel.write(ByteBuffer.wrap(new String("客户端请求获取消息(第"+i+"次)").getBytes()));
            // channel.register(selector, SelectionKey.OP_READ );
            ByteBuffer buf2 = ByteBuffer.allocate(48);
            buf2.clear();
            buf2.put(("客户端请求获取消息(第" + i + "次)").getBytes());
            buf2.flip();
            channel.write(buf2);
            channel.register(selector, SelectionKey.OP_READ);
//          int bytesSent = channel.send(buf2, new InetSocketAddress(serverIp,port)); // 将消息回送给服务端
        } catch (IOException e) {
            e.printStackTrace();
        }
        flag = true;
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
        new Thread(() -> {
            try {
                new DatagramChannelClientTest().service();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

//      new Thread(new Runnable() {
//          public void start() {
//              try {
//                  new DatagramChannelClientDemo().service();
//              } catch (IOException e) {
//                  e.printStackTrace();
//              }
//          }
//      }).start();

    }
}
