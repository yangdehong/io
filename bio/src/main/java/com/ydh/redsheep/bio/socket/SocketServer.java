package com.ydh.redsheep.bio.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
*
* @author : yangdehong
* @date : 2020-05-12 17:11
*/
public class SocketServer {

    public static void main(String[] args) throws Exception {
        // 监听指定的端口
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress("127.0.0.1",8888));
        // server将一直等待连接的到来
        System.out.println("server启动");
        while (true) {
            Socket socket = server.accept();
            // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
            while ((len = inputStream.read(bytes)) != -1) {
                // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                System.out.println("接受到client的信息: " + new String(bytes, 0, len, "UTF-8"));
            }

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("server已经接收到信息".getBytes("UTF-8"));

            inputStream.close();
            outputStream.close();
            socket.close();
//            server.close();
        }
    }


}
