package com.star.rpc.server;

import com.star.rpc.model.RpcRequest;
import com.star.rpc.model.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
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
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        Object invoke = serviceFastMethod.invoke(serviceBean, parameters);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(request.getRequestId());
        rpcResponse.setResult(invoke);
        log.info("result " + rpcResponse.toString());
        ctx.writeAndFlush(rpcResponse);
        ctx.close();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //服务端将关闭channel通道，当服务端没有收到客户端请求直到超时
        if(evt instanceof IdleStateEvent) {
            if(ctx.channel() != null && !ctx.channel().isActive()) {
                ctx.channel().close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
