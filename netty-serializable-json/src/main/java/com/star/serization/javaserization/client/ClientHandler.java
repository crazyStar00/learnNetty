package com.star.serization.javaserization.client;

import com.google.gson.Gson;
import com.star.serization.javaserization.model.Request;
import com.star.serization.javaserization.model.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

public class  ClientHandler extends SimpleChannelInboundHandler<Response> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
        System.out.println(msg.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 100; i++) {
            Request request = new Request();
            request.setAddress("南京市江宁区方山国家地质公园");
            request.setNumber("15311411152");
            request.setUserName("star");
            request.setId(i);
            request.setNumber("Netty 权威指南");
            byte[] bytes = new Gson().toJson(request).getBytes(StandardCharsets.UTF_8);
            ctx.write(bytes);
        }
        ctx.flush();

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        ctx.close();
    }
}
