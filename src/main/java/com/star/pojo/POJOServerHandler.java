package com.star.pojo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class POJOServerHandler extends SimpleChannelInboundHandler<User> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, User msg) throws Exception {

    }
}
