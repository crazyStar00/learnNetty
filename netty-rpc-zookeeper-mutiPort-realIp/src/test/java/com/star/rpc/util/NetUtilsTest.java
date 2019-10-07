package com.star.rpc.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class NetUtilsTest {

    @Test
    public void getLocalHost() {
        String localHost = NetUtils.getLocalHost();
        System.out.println(localHost);

    }
}