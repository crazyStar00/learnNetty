package com.star.rpc.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZkUtil {
    public static String connectionString = "tencent:2181";
    public static CuratorFramework curatorFramework;

    public static void init() {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3, Integer.MAX_VALUE);
        curatorFramework = CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
        curatorFramework.start();
    }
}
