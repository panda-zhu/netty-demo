package com.zlf.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 将本地文件内容读取出来
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception{
       File file = new File("d:\\file01.txt");
       FileInputStream fileInputStream = new FileInputStream(file);

       FileChannel fileChannel = fileInputStream.getChannel();

       ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

       fileChannel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
