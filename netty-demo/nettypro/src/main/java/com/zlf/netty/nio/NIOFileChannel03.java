package com.zlf.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 将本地文件内容读取出来
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception{
       FileInputStream fileInputStream = new FileInputStream("1.txt");
       FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel streamChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true){
            /*
                public final Buffer clear() {
                    position = 0;
                    limit = capacity;
                    mark = -1;
                    return this;
                }
             */
            byteBuffer.clear();
            //把数据读到buffer里
            int read = fileChannel01.read(byteBuffer);
            if(read == -1){
                break;
            }
            //将buffer中的数据写到channel02中
            byteBuffer.flip();
            streamChannel02.write(byteBuffer);
        }

        //关闭相关的流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
