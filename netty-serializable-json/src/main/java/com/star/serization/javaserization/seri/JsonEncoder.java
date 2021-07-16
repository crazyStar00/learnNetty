package com.star.serization.javaserization.seri;

import com.google.gson.Gson;
import com.star.serization.javaserization.model.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author star
 * @descripton json
 * @date 2021/7/14
 **/
public class JsonEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        String json = new Gson().toJson(msg);
        //out.writeInt(json.length());

        out.writeBytes(json.getBytes(StandardCharsets.UTF_8));
    }
}
