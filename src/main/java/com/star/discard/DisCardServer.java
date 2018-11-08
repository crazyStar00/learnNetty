package com.star.discard;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DisCardServer {
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
                            ch.pipeline().addLast(new DisCardHandler());
                        }
                    });
//       绑定端口，开始接受进来的请求
            ChannelFuture future = serverBootstrap.bind(9999).sync();
//       等待服务器，socket关闭
            ChannelFuture future1 = future.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }

    }
}
