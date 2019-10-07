package com.star.rpc.register;

import com.star.rpc.util.ZkUtil;
import org.apache.zookeeper.CreateMode;

public class ZookeeperRegister {
    public static void register(String interfaceName, String host, int port) throws Exception {
        if(ZkUtil.curatorFramework==null){
            ZkUtil.init();
        }
        ZkUtil.curatorFramework.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/zeus/" + interfaceName + "/provider/" + host + ":" + port);

    }
}
