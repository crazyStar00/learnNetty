package com.star.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RpcServer {
    private Map<String, Object> handlerMap = new HashMap<>();

    public void run(int port) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(boosGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ObjectDecoder(1024 * 1024,
                                            ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ServerHandler(handlerMap));
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture future = serverBootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅退出，释放资源池
            workGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }

    public RpcServer addService(String interfaceName, Object serviceBean) {
        if(!handlerMap.containsKey(interfaceName)) {
            log.info("Loading service: {}", interfaceName);
            handlerMap.put(interfaceName, serviceBean);
        }
        return this;
    }

}
