package com.star.rpc.server;

import com.star.rpc.service.EchoService;
import com.star.rpc.service.EchoServiceImpl;
import com.star.rpc.service.HelloService;
import com.star.rpc.service.HelloServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private Map<String, Object> handlerMap = new HashMap<>();
    public void run(int port){
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
        if (!handlerMap.containsKey(interfaceName)) {
            logger.info("Loading service: {}", interfaceName);
            handlerMap.put(interfaceName, serviceBean);
        }

        return this;
    }
    public static void main(String[] args) throws InterruptedException {
        RpcServer rpcServer = new RpcServer();
        rpcServer.addService(EchoService.class.getName(), new EchoServiceImpl());
        rpcServer.addService(HelloService.class.getName(), new HelloServiceImpl());
        rpcServer.run(9999);
    }
}
