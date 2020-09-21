package com.zlf.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 搞一个服务端
 */
public class NIOServer {
    public static void main(String[] args) throws Exception {
        //创建ServerSocketChannel，通过这个拿到ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //创建一个selector对象
        Selector selector = Selector.open();

        //绑定一个端口7777，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(7777));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel 注册到selector 关心事件为OP_ACCEPT，这边注册完后，可以通过循环监听，判断关心的事件有没有发生。
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true){
            //这里我们等待一秒，如果没有事件发生，返回
            if(selector.select(1000)==0){
                System.out.println("无连接线程"+Thread.currentThread().getName());
                System.out.println("服务器等待了一秒，无连接");
                continue;
            }
            /*
                如果返回的>0，就表示获取到了相关的selectionKey集合
                1.如果返回的>0，表示已经获取到了关注的事件了
                2.selector.selectedKeys()返回关注事件集合
                通过selectionKeys反向获取通道
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();

            while (selectionKeyIterator.hasNext()){
                //获取到selectionKey
                SelectionKey key = selectionKeyIterator.next();

                //根据key对应的通道发生的时间做相应处理
                if(key.isAcceptable()){//如果是OP_ACCEPT,有新的客户端连接
                    //该客户端生成一个新的socketchannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功了，生成了一个socketChannel"+socketChannel.hashCode());
                    System.out.println("客户端连接成功了后的线程："+Thread.currentThread().getName());
                    //将socketChannel设置成非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel注册到selector，关注事件为OP_READ，同时给socketChannel关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(key.isReadable()){//如果发生OP_READ
                    //通过key获取channel和buffer
                    SocketChannel channel =(SocketChannel) key.channel();
                    ByteBuffer buffer =(ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端"+ new String(buffer.array()));
                    System.out.println("客户端读取buffer后的线程："+Thread.currentThread().getName());
                }
                //手动从集合中移除当前的key，防止重复操作。
                selectionKeyIterator.remove();
            }

        }

    }
}
