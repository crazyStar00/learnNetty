package com.star.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class TimeServer {
//    private Selector selector;
//    private SocketChannel socketChannel;
//    private volatile boolean stop;

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("server starting ");
        while (true) {
            selector.select(3000);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            SelectionKey selectionKey;
            while (iterator.hasNext()) {
                selectionKey = iterator.next();
                iterator.remove();
                try {
                    handleInput(selectionKey, selector);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (selectionKey != null) {
                        selectionKey.cancel();
                        if (selectionKey.channel() != null) {
                            selectionKey.channel().close();
                        }
                    }
                }
            }
        }
    }

    private static void handleInput(SelectionKey selectionKey, Selector selector) throws IOException {
        if (selectionKey.isValid()) {
            //处理新接入的请求消息
            if (selectionKey.isAcceptable()) {
                //接受新的连接
                ServerSocketChannel socketChannel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel channel = socketChannel.accept();
                channel.configureBlocking(false);
                //添加新的连接到selector
                channel.register(selector, SelectionKey.OP_READ);
            }
            if (selectionKey.isReadable()) {
                //读取数据
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("The time server receive order :" + body);
                    String currentTime = "QUERY TIME ORDER\\r\\n".equalsIgnoreCase(body)
                            ? new Date(System.currentTimeMillis()).toString()
                            : "BAD ORDER";
                    doWrite(socketChannel,currentTime);

                }
            }
        }
    }

    private static void doWrite(SocketChannel socketChannel, String reponse) throws IOException {
        if (reponse!=null && reponse.trim().length()>0) {
            byte[] bytes = reponse.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
        }
    }
}
