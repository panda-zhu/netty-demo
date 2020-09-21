package com.zlf.netty.nio.groupchat;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT =6667;

    //构造器
    public GroupChatServer(){
        try {
            //创建选择器
            selector = Selector.open();
            //创建ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口号
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将该listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void listen(){
        try {
            while (true){
                int count = selector.select();
                if(count >0){//监听到有设备接入
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();

                        //监听到连接的事件
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //将该通道注册到selector上
                            sc.register(selector,SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress()+"上线");
                        }
                        //监听到read事件，即通道可读了
                        if(key.isReadable()){
                            //处理读（专门写方法）
                            readDate(key);
                        }
                        //移除已经处理完的key
                        iterator.remove();
                    }

                }else {
                    System.out.println("等待.....");
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readDate(SelectionKey key){
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            //创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            if(count>0){
                String msg = new String(buffer.array());
                //客户端打印各个服务端发送的消息
                System.out.println("from客户端： "+msg);
                //将一个客户端发来的消息转发给其他客户端(排除自己)
                sendInfoToOtherClient(msg,channel);
            }
        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress()+"离线了....");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //转发消息给其他客户端
    private void sendInfoToOtherClient(String msg,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        //遍历所有注册到selector上的socketChannel，并派出self
        for (SelectionKey key:selector.selectedKeys()){
            //通过key取出对应的socketChannel
            Channel targetChannel = key.channel();
            //排除自己
            if(targetChannel instanceof  SocketChannel && targetChannel != self){
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
