package com.star.rpc.client;

import com.google.common.collect.Maps;
import com.star.rpc.model.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

public class DataSource {
    public static Map<String, CompletableFuture<RpcResponse>> futureMap = Maps.newHashMap();


}
