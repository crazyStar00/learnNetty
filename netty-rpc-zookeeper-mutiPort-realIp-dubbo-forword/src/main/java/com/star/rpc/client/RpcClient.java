package com.star.rpc.client;


import com.google.common.reflect.Reflection;
import com.google.gson.Gson;
import com.star.rpc.model.Person;
import com.star.rpc.model.RpcRequest;
import com.star.rpc.service.EchoService;
import com.star.rpc.service.HelloService;
import com.star.rpc.util.ZkUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;


public class RpcClient {

    static RpcClient rpcClient = new RpcClient();

    boolean isStart = false;
    static NioEventLoopGroup group = new NioEventLoopGroup();
    static Channel channel;
    String host;
    int port;

    private RpcClient() {
    }

    public static RpcClient getInstance() {
        return new RpcClient();
    }

    public <T> T create(Class<T> serviceInterface) {
        try {
            String interfaceName = serviceInterface.getName();
            if(ZkUtil.curatorFramework == null) {
                ZkUtil.init();
            }
            List<String> strings = ZkUtil.curatorFramework.getChildren().forPath("/zeus/" + interfaceName + "/provider");
            if(strings == null || strings.size() == 0) {
                throw new RuntimeException(interfaceName + "没有提供者");
            }
            String hosts = strings.get(0);
            String[] split = hosts.split(":");
            host = split[0];
            port = Integer.parseInt(split[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Reflection.newProxy(serviceInterface, this::invoke);
    }

    public void call(RpcRequest rpcRequest) {
        channel.writeAndFlush(rpcRequest);

    }

    private synchronized void start() {
        if(isStart) {
            return;
        }
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ObjectDecoder(1024,
                                            ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ClientHandler());
                        }
                    })
                    //.option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,2000);
            ;
            ChannelFuture future = bootstrap.connect(host, port).sync();
            isStart = true;
            channel = future.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if(group != null) {
            group.shutdownGracefully();
        }
    }


    private Object invoke(Object proxy, Method method, Object[] args) throws ExecutionException, InterruptedException {
        start();
        if(Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if("equals".equals(name)) {
                return proxy == args[0];
            } else if("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) +
                        ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterTypeStr = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypeStr[i] = parameterTypes[i].getName();
        }
        rpcRequest.setParameterTypes(parameterTypeStr);
        rpcRequest.setParameters(args);
        DataSource.futureMap.put(rpcRequest.getRequestId(), new CompletableFuture<>());
        call(rpcRequest);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        channel.writeAndFlush(rpcRequest).addListener(future -> countDownLatch.countDown());
        countDownLatch.await();
//        while (DataSource.futureMap.get(rpcRequest.getRequestId())==null){
//        }
        Class<?> returnType = method.getReturnType();
        Object result = DataSource.futureMap.get(rpcRequest.getRequestId()).get().getResult();
        String s = new Gson().toJson(result);
        Object o = new Gson().fromJson(s, returnType);
        return o;
    }


}
