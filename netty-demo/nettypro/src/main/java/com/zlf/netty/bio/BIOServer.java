package com.zlf.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    public static void main(String[] args) throws Exception{
        //1.创建一个线程池
        //2.如果有客户端连接，就创建一个线程与之通讯（单独写一个方法）
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        System.out.println("最外层：线程id，id="+Thread.currentThread().getId()+" 线程名称为："+Thread.currentThread().getName());
        //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");

        while (true){
            System.out.println("线程监听循环：线程id，id="+Thread.currentThread().getId()+" 线程名称为："+Thread.currentThread().getName());
            //监听，等待客户端连接
            System.out.println("等待连接中。。。。");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            //客户端连接，就创建一个线程与之通讯（单独写一个方法）
            newCachedThreadPool.execute(() -> {
                //可以和客户端通讯了
                handler(socket);
            });
        }

    }
    //编写一个handler方法，和客户端通讯
    public static void handler(Socket socket){
        byte[] bytes = new byte[1024];
        try {
            System.out.println("处理客户端方法：线程id，id="+Thread.currentThread().getId()+" 线程名称为："+Thread.currentThread().getName());
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端数据
            while (true){
                int read = inputStream.read(bytes);
                if(read != -1){
                    System.out.println(new java.lang.String(bytes,0,read));
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
