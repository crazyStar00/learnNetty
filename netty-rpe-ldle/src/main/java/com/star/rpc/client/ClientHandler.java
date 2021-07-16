package com.star.rpc.client;

import com.star.rpc.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        CompletableFuture completableFuture = DataSource.futureMap.get(msg.getRequestId());
        completableFuture.complete(msg);
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
}
