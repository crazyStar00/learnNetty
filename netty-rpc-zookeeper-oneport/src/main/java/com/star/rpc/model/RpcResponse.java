package com.star.rpc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * RPC Response
 * @author huangyong
 */
@Data
public class RpcResponse implements Serializable {
    private String requestId;
    private String error;
    private Object result;

}
