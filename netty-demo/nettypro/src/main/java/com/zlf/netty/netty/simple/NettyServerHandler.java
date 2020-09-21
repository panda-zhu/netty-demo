package com.zlf.netty.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/*
    说明：
    1.我们自定一个handler需要继承netty规定好的某个handlerAdapter
    2.这时我们自定义一个handler，才能成为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取实际数据（读取客户端发送的消息）
    /*
        1.ChannelHandlerContext ctx ：上下文对象，含有管道pipeline，通道channel，地址
        2.msg：客户端发送的数据 默认是Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("server ctx:"+ctx);
//        //将msg转成ByteBuf
//        //ByteBuf是Netty提供的，不是NIO的ByteBuffer
//        ByteBuf byf = (ByteBuf) msg;
//        System.out.println("客户端发送的消息是："+byf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端的地址是："+ctx.channel().remoteAddress());

        //模拟一个场景：如果业务处理耗时特别长，导致客户端阻塞怎么办？
        //netty支持异步执行业务，提交该channel对应的NIOEventLoop的taskQueue中
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(10*1000);
                    //模拟向客户端发送一个讯息
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~~~~~我是服务端特备耗时的业务，为了测试阻塞发送一条讯息给你",CharsetUtil.UTF_8));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //使用场景：需要定时执行某项业务
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try{
                    //模拟向客户端发送一个讯息
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~~~~~我是服务端定时的业务，为了定时发送一条讯息给你",CharsetUtil.UTF_8));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },5, TimeUnit.SECONDS);
    }

    //数据读取完毕后执行该方法
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入缓存，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~~~~~我是服务端",CharsetUtil.UTF_8));
    }

    //处理异常，一般是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
