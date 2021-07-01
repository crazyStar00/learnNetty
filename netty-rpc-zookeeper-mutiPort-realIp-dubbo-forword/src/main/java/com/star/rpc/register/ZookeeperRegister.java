package com.star.rpc.register;

import com.star.rpc.util.ZkUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class ZookeeperRegister {
    public static void register(String interfaceName, String host, int port) throws Exception {
        if(ZkUtil.curatorFramework==null){
            ZkUtil.init();
        }
        Stat stat = ZkUtil.curatorFramework.checkExists().forPath("/zeus");

        ZkUtil.curatorFramework.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/zeus/" + interfaceName + "/provider/" + host + ":" + port);

    }
}
