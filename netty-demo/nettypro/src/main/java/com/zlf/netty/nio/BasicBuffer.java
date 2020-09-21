package com.zlf.netty.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //举例说明buffer的使用
        //创建一个intbuffer，大小为5，可以放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i*10);
        }

        //如何从buffer读取数据
        //将buffer转换，读写切换
        intBuffer.flip();

        while (intBuffer.hasRemaining()){
            //每次get，指针都会往后移动
            System.out.println(intBuffer.get());
        }
    }
}
