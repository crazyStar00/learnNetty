package com.star.rpc.service;

public class EchoServiceImpl implements EchoService {
    @Override
    public String echo(String str) {
        return "echo "+str;
    }
}
