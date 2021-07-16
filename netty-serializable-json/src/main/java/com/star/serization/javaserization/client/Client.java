package com.star.serization.javaserization.client;


import com.star.serization.javaserization.seri.JsonDecoder;
import com.star.serization.javaserization.seri.JsonEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new JsonDecoder())
                                    .addLast(new JsonEncoder())
                                    .addLast(new ClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect("localhost", 1111).sync();
            future.channel().closeFuture().sync();
        } finally {
            Thread.sleep(1000);
            group.shutdownGracefully();
        }
    }
}
