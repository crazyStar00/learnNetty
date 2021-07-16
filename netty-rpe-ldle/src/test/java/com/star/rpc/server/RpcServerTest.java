package com.star.rpc.server;

import com.star.rpc.service.EchoService;
import com.star.rpc.service.EchoServiceImpl;
import com.star.rpc.service.HelloService;
import com.star.rpc.service.HelloServiceImpl;

import java.util.Random;

import static org.junit.Assert.*;

public class RpcServerTest {
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        rpcServer.addService(EchoService.class.getName(), new EchoServiceImpl());
        rpcServer.addService(HelloService.class.getName(), new HelloServiceImpl());
        rpcServer.run(1111);
    }
}