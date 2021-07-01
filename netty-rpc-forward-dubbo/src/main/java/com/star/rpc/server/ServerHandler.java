package com.star.rpc.server;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.star.rpc.model.RpcRequest;
import com.star.rpc.model.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.util.Map;

@ChannelHandler.Sharable
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, Object> handlerMap;

    public ServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest request = (RpcRequest) msg;
        log.info("channelRead0");
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);
        ReferenceConfig referenceConfig=new ReferenceConfig();
        referenceConfig.setApplication(new ApplicationConfig("test"));
        referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        referenceConfig.setInterface(request.getClassName());
        referenceConfig.setGeneric(true);
        GenericService genericService = (GenericService) referenceConfig.get();
        Object result = genericService.$invoke(request.getMethodName(), request.getParameterTypes(), request.getParameters());
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(request.getRequestId());
        rpcResponse.setResult(result);
        ctx.writeAndFlush(rpcResponse);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
