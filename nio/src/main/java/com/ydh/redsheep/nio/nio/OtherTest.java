package com.ydh.redsheep.nio.nio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: yangdehong
 * @version: 2017/11/16.
 */
public class OtherTest {

    public static void main(String[] args) throws Exception {
        asynchronousFileChannelTest();
//        fileTest();
//        pathTest();
    }

    private static void asynchronousFileChannelTest() throws Exception{
        // 读数据
        Path path = Paths.get("echo/volat.xml");
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        long position = 0;
        Future<Integer> operation = fileChannel.read(buffer, position);
        while(!operation.isDone());
        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        System.out.println(new String(data));
        buffer.clear();
//        // 写数据
//        Path path = Paths.get("echo/volat-write.txt");
//        AsynchronousFileChannel fileChannel =
//                AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
//        long position = 0;
//        buffer.put("volat echo".getBytes());
//        buffer.flip();
//        Future<Integer> operation = fileChannel.write(buffer, position);
//        buffer.clear();
//        while(!operation.isDone());
//        System.out.println("Write done");
//
//        Path path = Paths.get("echo/volat-write.txt");
//        if(!Files.exists(path)){
//            Files.createFile(path);
//        }
//        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        long position = 0;
//        buffer.put("volat echo".getBytes());
//        buffer.flip();
//        fileChannel.write(buffer, position, buffer, new CompletionHandler<Integer, ByteBuffer>() {
//            @Override
//            public void completed(Integer result, ByteBuffer attachment) {
//                System.out.println("bytes written: " + result);
//            }
//            @Override
//            public void failed(Throwable exc, ByteBuffer attachment) {
//                System.out.println("Write failed");
//                exc.printStackTrace();
//            }
//        });
    }

    private static void fileTest(){
        Path path = Paths.get("echo/logging.properties");

        boolean pathExists = Files.exists(path);
        //不应该跟随文件系统中的符号链接来确定路径是否存在
//        boolean pathExists = Files.exists(path, new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
        System.out.println(pathExists);
    }

    private static void pathTest(){
        // 绝对路径
        Path path = Paths.get("c:\\echo\\myfile.txt");
        // 相对路径
        Path path2 = Paths.get("c:\\echo", "projects\\a-project\\myfile.txt");
        System.out.println(path.toString());
        System.out.println(path2.toString());

        /////////规范化意味着它将删除路径字符串中间的所有代码.和..代码，并解析路径字符串引用的路径。
        String originalPath = "d:\\echo\\projects\\a-project\\..\\another-project";
        Path path3 = Paths.get(originalPath);
        System.out.println("path3 = " + path3);//path3 = d:\echo\projects\a-project\..\another-project
        Path path4 = path3.normalize();
        System.out.println("path4 = " + path4);//path4 = d:\echo\projects\another-project
    }

}
