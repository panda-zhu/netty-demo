package com.zlf.netty.netty.simple;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
        super.channelRead(ctx, msg);
    }
}
