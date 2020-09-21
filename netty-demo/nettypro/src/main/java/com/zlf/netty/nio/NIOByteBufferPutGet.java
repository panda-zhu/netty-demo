package com.zlf.netty.nio;

import java.nio.ByteBuffer;

public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byteBuffer.putInt(100);
        byteBuffer.putLong(9);
        byteBuffer.putChar('c');
        byteBuffer.putShort((short)4);

        byteBuffer.flip();

        //必须按照顺讯，怎么顺序才存的，怎么顺序取
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());
    }
}
