package com.star.serization.javaserization.server;

import com.star.serization.javaserization.seri.JsonDecoder;
import com.star.serization.javaserization.seri.JsonEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class Server {
    public static void main(String[] args) throws InterruptedException {
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
                                    .addLast(new JsonEncoder())
                                    .addLast(new JsonDecoder());
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture future = serverBootstrap.bind(1111).sync();
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            //优雅退出，释放资源池
            workGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }
}
