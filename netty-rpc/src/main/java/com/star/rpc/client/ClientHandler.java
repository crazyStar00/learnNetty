package com.star.rpc.client;

import com.star.rpc.model.RpcRequest;
import com.star.rpc.model.RpcResponse;
import com.star.rpc.server.RpcServer;
import com.star.rpc.service.EchoService;
import com.star.rpc.service.HelloService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        CompletableFuture completableFuture = DataSource.futureMap.get(msg.getRequestId());
        completableFuture.complete(msg);
    }


}
