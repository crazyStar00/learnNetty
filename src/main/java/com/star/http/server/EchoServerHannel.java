package com.star.http.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author liuquan
 * @ChannelHandler.Sharable 标识一个ChannelHandler可以被多个Channel安全的共享
 */
@ChannelHandler.Sharable
public class EchoServerHannel extends ChannelInboundHandlerAdapter {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EchoServerHannel.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg;
		logger.info("Server received : {}",in.toString(CharsetUtil.UTF_8));
		//將接收到的信息寫給發送者，而不衝刷出站消息
		ctx.writeAndFlush(in);
	}


	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//將未決消息衝刷到遠程節點，并且关闭该Channel
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
				.addListener(ChannelFutureListener.CLOSE);
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//打印异常栈跟踪
		cause.printStackTrace();
		//关闭Channel
		ctx.close();

	}
}
