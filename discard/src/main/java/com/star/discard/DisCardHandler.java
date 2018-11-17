package com.star.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class DisCardHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf msg1 = (ByteBuf) msg;
//        讲收到的消息输出到控制台
        System.out.println(msg1.toString(CharsetUtil.UTF_8));
//        丢弃收到的消息
        msg1.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        当出现异常时，关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
