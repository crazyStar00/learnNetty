package com.star.rpc.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.star.rpc.service.HelloService;
import com.star.rpc.service.HelloServiceImpl;

import java.util.concurrent.CountDownLatch;

/**
 * @author star
 * @descripton
 * @date 2021/7/1
 **/
public class Provider {
    public static void main(String[] args) throws InterruptedException {
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setApplication(new ApplicationConfig("PRO"));
        serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        serviceConfig.setInterface(HelloService.class);
        serviceConfig.setRef(new HelloServiceImpl());
        serviceConfig.export();
        new CountDownLatch(1).await();
    }
}
