package com.star.hello2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf msg1 = (ByteBuf) msg;
        String content = msg1.toString(CharsetUtil.UTF_8);
        System.out.println("receive : "+content);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //將未決消息衝刷到遠程節點，并且关闭该Channel
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client!", CharsetUtil.UTF_8))
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
