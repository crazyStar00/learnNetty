package com.star.rpc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * RPC Request
 * @author huangyong
 */
@Data
public class RpcRequest implements Serializable {
    private String requestId;
    private String className;
    private String methodName;
    private String[] parameterTypes;
    private Object[] parameters;

}