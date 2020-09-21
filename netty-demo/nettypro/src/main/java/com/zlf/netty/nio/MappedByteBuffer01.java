package com.zlf.netty.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/*
    说明：
    1.MappedByteBuffer 可以让文件直接在内存（堆外内存）中修改，操作系统不需要拷贝一份
 */
public class MappedByteBuffer01 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        /*
            参数1：使用读写模式
            参数2：可以直接修改的起始位置
            参数3：是映射到内存的大小，即将1.txt的多少个子杰映射到内存，可以修改的范围就是5个字节，是大小不是索引
         */
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0,(byte) 'S');
        mappedByteBuffer.put(4,(byte) '9');
        randomAccessFile.close();
        System.out.println("修改成功！");
    }
}
