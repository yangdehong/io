package com.ydh.redsheep.bio.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
*
* @author : yangdehong
* @date : 2020-05-12 17:12
*/
public class SocketClient {
    public static void main(String args[]) throws Exception {
        // 与服务端建立连接
        Socket socket = new Socket("127.0.0.1", 8888);
        // 建立连接后获得输出流
        OutputStream outputStream = socket.getOutputStream();
        String message = "我是一个同步阻塞的请求";
        socket.getOutputStream().write(message.getBytes("UTF-8"));
        //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(bytes)) != -1) {
            //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(bytes, 0, len,"UTF-8"));
        }
        System.out.println("接收到server的信息: " + sb);

        inputStream.close();
        outputStream.close();
        socket.close();
    }

}
