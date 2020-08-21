package com.zlf.netty.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws Exception{
        //创建bossgroup和workGroup
        /*
            说明：
            1.创建两个线程组 bossgroup和workgroup
            2.bossgroup只处理连接请求，真正和客户端业务处理，会交给workgroup处理
            3.两个都是无限循环
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //创建服务端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        //使用链式变成来设置参数
        bootstrap.group(bossGroup,workGroup)//设置两个线程组
                .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128)//设置线程队列得到的连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象（匿名对象）
                    //给pipeline设置处理器
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(null);
                    }
                });//给我们的workerGroup的EventLoop对应的管道设置处理器
        System.out.println("服务器端已经准备好了...");
        //绑定一个端口，并且同步处理，生成一个channelFuture对象。
        //启动服务器（并绑定端口）
        ChannelFuture channelFuture = bootstrap.bind(6668).sync();
        //对关闭通道进行监听
        channelFuture.channel().closeFuture().sync();
    }
}
