package com.star.serization.javaserization;

import java.io.Serializable;

public class Request implements Serializable {

    private static final long serialVersionUID = -2943293490313705361L;
    /**
     * 订购编号
     */
    private int id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 订购的产品名称
     */
    private String productName;
    /**
     * 订购者的电话号码
     */
    private String number;
    /**
     * 订购者的家庭住址
     */
    private String address;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", productName='" + productName + '\'' +
                ", number='" + number + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
