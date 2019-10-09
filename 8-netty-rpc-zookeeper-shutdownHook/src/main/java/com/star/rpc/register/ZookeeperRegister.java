package com.star.rpc.register;

import com.star.rpc.model.RegisterServer;
import com.star.rpc.util.ZkUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ZookeeperRegister {
    static {
        Thread thread = new Thread(() -> {
            log.info("服务停止，准备清理zookeeper服务注册信息");
            try {
                ZookeeperRegister.unRegister();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        log.info("注册服务停止钩子");
        Runtime.getRuntime().addShutdownHook(thread);
    }

    public static List<RegisterServer> services = new ArrayList<>();

    public static void register(String interfaceName, String host, int port) throws Exception {
        if(ZkUtil.curatorFramework == null) {
            ZkUtil.init();
        }
        services.add(new RegisterServer(interfaceName, host, port));
        ZkUtil.curatorFramework.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/zeus/" + interfaceName + "/provider/" + host + ":" + port);
    }

    public static void unRegister() throws Exception {
        for (RegisterServer service : services) {
            String path = "/zeus/" + service.getInterfaceName() + "/provider/" + service.getHost() + ":" + service.getPort();
            log.info("删除[{}]节点",path);
            ZkUtil.curatorFramework.delete()
                    .forPath(path);
        }
    }
}
