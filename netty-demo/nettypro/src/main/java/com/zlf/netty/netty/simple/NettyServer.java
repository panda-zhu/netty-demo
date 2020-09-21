package com.zlf.netty.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyServer {
    public static void main(String[] args) throws Exception{
        //创建bossgroup和workGroup
        /*
            说明：
            1.创建两个线程组 bossgroup和workgroup
            2.bossgroup只处理连接请求，真正和客户端业务处理，会交给workgroup处理
            3.两个都是无限循环
            4.bossGroup和workGroup含有的子线程的个数默认是cpu的核数*2
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //创建服务端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式变成来设置参数
            bootstrap.group(bossGroup, workGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象（匿名对象）
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //可以使用一个集合管理socketChannel，想再次给客户端推送消息时，可以将业务加入到各个channel队形的NIOEventLoop的taskqueue
                            //或者scheduleTaskQueue中
                            System.out.println("客户socketchannel hashcode="+ch.hashCode());
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });//给我们的workerGroup的EventLoop对应的管道设置处理器
            System.out.println("服务器端已经准备好了...");
            //绑定一个端口，并且同步处理，生成一个channelFuture对象。
            //启动服务器（并绑定端口）
            ChannelFuture channelFuture = bootstrap.bind(8886).sync();

            //使用netty异步机制，可以对操作结构进行异步监听
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if(channelFuture.isSuccess()){
                        System.out.println("绑定端口成功");
                    }else {
                        System.out.println("绑定端口失败");
                    }
                }
            });
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
