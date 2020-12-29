package com.ydh.redsheep.nio.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description:
 * @author: yangdehong
 * @version: 2017/11/14.
 */
public class FileChannelTest {

    public static void main(String[] args) throws Exception {
        testFileChannel();
    }

    /**
     * channel 是通道，
     *      FileChannel 文件操作
     * @throws Exception
     */
    private static void testFileChannel() throws Exception{

//        Charset charset = Charset.forName("TUF-8");//Java.nio.charset.Charset处理了字符转换问题。它通过构造CharsetEncoder和CharsetDecoder将字符序列转换成字节和逆转换。
//        CharsetDecoder decoder = charset.newDecoder();

        String path = "F:\\usr\\local\\crm\\log\\admin\\admin.log";
        ByteBuffer buf = ByteBuffer.allocate(1024);
//        /**
//         * 使用FileOutputStream/FileInputStream
//         */
//        // 清空，避免数据
//        buf.clear();
//        // 输出通道
//        FileChannel osChannel = new FileOutputStream(path, true).getChannel();
//        // 追加
//        osChannel.write(ByteBuffer.wrap("很好，生蚝！".getBytes()));
//        osChannel.close();
//        // 输入通道
//        FileChannel isChannel = new FileInputStream(path).getChannel();
//        // 读取
//        int len = isChannel.read(buf);
//        System.out.println(new String(buf.array(), 0, len));
//        isChannel.close();

        /**
         * 通过RandomAccessFile
         */
        // 清空，避免数据
        buf.clear();
        // 获取通道
        RandomAccessFile aFile = new RandomAccessFile(path, "rw");
        FileChannel isChannel2 = aFile.getChannel();
        // 写入
        String data = "我是高拆散！";
        buf.put(data.getBytes());
        // 转到读取
        buf.flip();
        while (buf.hasRemaining()) {
            isChannel2.write(buf);
        }

        FileChannel osChannel2 = aFile.getChannel();
        int bytes = osChannel2.read(buf);
        while (bytes != -1) {
            buf.flip();
            if (buf.hasRemaining()) {
                System.out.print(new String(buf.array(), 0, bytes));
            }

            buf.clear();
            bytes = osChannel2.read(buf);
        }

        osChannel2.close();
        isChannel2.close();

    }
}
