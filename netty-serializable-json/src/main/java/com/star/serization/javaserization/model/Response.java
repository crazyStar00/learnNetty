package com.star.serization.javaserization.model;

import java.io.Serializable;

public class Response implements Serializable {

    private static final long serialVersionUID = 7369365767684275941L;
    /**
     * 订购编号
     */
    private int id;
    /**
     * 订购结果：0 表示成功
     */
    private int code;
    /**
     * 可选的详细信息描述
     */
    private String desc;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
