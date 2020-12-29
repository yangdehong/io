package com.ydh.redsheep.nio.nio;

import java.nio.ByteBuffer;

/**
 * @description:
 * @author: yangdehong
 * @version: 2017/11/14.
 */
public class BufferTest {

    public static void main(String[] args) throws Exception {
        testBuffer();
    }


    /**
     * buffer 就是数组
     * @throws Exception
     */
    private static void testBuffer() throws Exception{
        // 分配缓冲区
        System.out.println("-----------初始化数据------------");
        ByteBuffer buf = ByteBuffer.allocate(1024);
        System.out.println(buf.capacity());
        System.out.println(buf.position());
        System.out.println(buf.limit());
        // 存入数据到缓冲区
        System.out.println("-----------写入5个数据之后------------");
        buf.put("abcde".getBytes());
        System.out.println(buf.capacity());
        System.out.println(buf.position());
        System.out.println(buf.limit());
        // 切换到读取数据模式
        System.out.println("-----------切换读取模式------------");
        buf.flip();
        System.out.println(buf.capacity());
        System.out.println(buf.position());
        System.out.println(buf.limit());
        // 读取数据
        System.out.println("-----------读取数据------------");
        byte[] b = new byte[buf.limit()];
        buf.get(b);
        System.out.println(new String(b, 0, b.length));
        System.out.println(buf.capacity());
        System.out.println(buf.position());
        System.out.println(buf.limit());
        // 重读
        System.out.println("-----------重读------------");
        buf.rewind();
        System.out.println(buf.capacity());
        System.out.println(buf.position());
        System.out.println(buf.limit());
        // 清空缓冲区,数据没有清空还存在
        System.out.println("-----------清空缓冲区------------");
        buf.clear();
//        buf.compact();// 只清空以读
        System.out.println(buf.capacity());
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println((char) buf.get());// 数据

//        buf.mark(); // 标记position位置
//        buf.reset(); // 回到mark标记

        // 可以操作数量
        System.out.println("-----------可以操作数量------------");
        System.out.println(buf.hasRemaining());
        System.out.println(buf.remaining());

//        equals()
//        当满足下列条件时，表示两个Buffer相等：
//        有相同的类型（byte、char、int等）。
//        Buffer中剩余的byte、char等的个数相等。
//        Buffer中所有剩余的byte、char等都相同。
//        如你所见，equals只是比较Buffer的一部分，不是每一个在它里面的元素都比较。实际上，它只比较Buffer中的剩余元素。
//
//        compareTo()方法
//        compareTo()方法比较两个Buffer的剩余元素(byte、char等)， 如果满足下列条件，则认为一个Buffer“小于”另一个Buffer：
//        第一个不相等的元素小于另一个Buffer中对应的元素 。
//        所有元素都相等，但第一个Buffer比另一个先耗尽(第一个Buffer的元素个数比另一个少)。

    }
}
