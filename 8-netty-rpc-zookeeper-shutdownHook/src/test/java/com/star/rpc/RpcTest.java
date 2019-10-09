package com.star.rpc;

import com.star.rpc.client.RpcClientTest;
import com.star.rpc.server.RpcServerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;


@Slf4j
public class RpcTest {

    @Before
    public void init() {
        new Thread(() -> RpcServerTest.main(null)).start();
    }

    @Test
    public void call() {
        RpcClientTest.main(null);
    }
}
