package com.ydh.redsheep.netty.netty.wheel;

import io.netty.util.HashedWheelTimer;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
* 时间轮的算法
* @author : yangdehong
* @date : 2020-01-14 20:33
*/
public class WheelTest {

    public static void main(String[] args) {
        // 一个时间轮
        HashedWheelTimer timer = new HashedWheelTimer(Executors.defaultThreadFactory(),
                1000, // 每tick一次的时间间隔, 每tick一次就会到达下一个槽位
                TimeUnit.MILLISECONDS, // 每tick一次的时间间隔的单位
                10); // 轮中的槽位数

        timer.newTimeout((timeout) -> {
            System.out.println("ydh");
        }, 20000, TimeUnit.MILLISECONDS);

        timer.start();


    }

}
