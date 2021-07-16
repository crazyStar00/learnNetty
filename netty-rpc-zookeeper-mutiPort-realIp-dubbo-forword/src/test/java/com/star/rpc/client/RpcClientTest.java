package com.star.rpc.client;

import com.star.rpc.model.Person;
import com.star.rpc.service.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcClientTest {
    public static void main(String[] args) {
        //RpcClient rpcClient = RpcClient.getInstance();
        RpcClient rpcClient2 = RpcClient.getInstance();
        //EchoService echoService = rpcClient.create(EchoService.class);
        //String star = echoService.echo("star");
        //log.info("server resutl :" + star);
        HelloService helloService = rpcClient2.locate(HelloService.class);
        Person star1 = helloService.hello("star", 18);
        log.info("server result :" + star1.toString());
    }

}