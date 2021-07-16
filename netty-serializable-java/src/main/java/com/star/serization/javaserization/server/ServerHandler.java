package com.star.serization.javaserization.server;

import com.star.serization.javaserization.model.Request;
import com.star.serization.javaserization.model.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request msg1 = (Request) msg;
        System.out.println(msg1.toString());
        ctx.writeAndFlush(msg1.getId());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public Response response(int id) {
        Response response = new Response();
        response.setId(id);
        response.setCode(0);
        response.setDesc("Netty book order successed,3 days later,sent to the destignated addresss");
        return response;
    }
}
