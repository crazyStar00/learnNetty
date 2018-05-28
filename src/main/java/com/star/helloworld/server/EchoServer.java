package com.star.helloworld.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServer {
	private static final Logger logger = LoggerFactory.getLogger(EchoServer.class);

	public static void main(String[] args) {
		NioEventLoopGroup group = new NioEventLoopGroup();
		NioEventLoopGroup group1 = new NioEventLoopGroup();
		EchoServerHannel echoServerHannel = new EchoServerHannel();

		ServerBootstrap serverBootstrap = new ServerBootstrap()
				.group(group, group1)
				.channel(NioServerSocketChannel.class)
				.localAddress(8888)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(echoServerHannel);
					}
				});

		try {
			ChannelFuture future = serverBootstrap.bind().sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			try {
				group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("EchoServer startting...");
	}
}
