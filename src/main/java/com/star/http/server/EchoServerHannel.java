package com.star.http.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
/**
 * @author liuquan
 * @ChannelHandler.Sharable 标识一个ChannelHandler可以被多个Channel安全的共享
 */
@ChannelHandler.Sharable
public class EchoServerHannel extends ChannelInboundHandlerAdapter {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EchoServerHannel.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest httpRequest = (HttpRequest) msg;
			String uri = httpRequest.uri();
			logger.info("Server received url: {}",uri);
		}
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, HttpResponseStatus.OK,Unpooled.wrappedBuffer("hello http".getBytes()));
		response.headers().set(CONNECTION, "text/plain");
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		ctx.writeAndFlush(response);

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
