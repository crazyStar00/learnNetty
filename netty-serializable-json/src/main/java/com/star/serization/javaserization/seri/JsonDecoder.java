package com.star.serization.javaserization.seri;

import com.google.gson.Gson;
import com.star.serization.javaserization.model.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author star
 * @descripton json
 * @date 2021/7/14
 **/
public class JsonDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int i = msg.readableBytes();
        byte[] data = new byte[i];
        //int i1 = msg.readInt();
        msg.getBytes(msg.readerIndex(), data, 0, i);
        new Gson().fromJson(new String(data), Response.class);
    }
}
