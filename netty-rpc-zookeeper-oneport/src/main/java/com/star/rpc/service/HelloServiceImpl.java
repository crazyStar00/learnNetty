package com.star.rpc.service;

import com.star.rpc.model.Person;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello "+name;
    }

    @Override
    public Person hello(String name, int age) {
        return new Person(name,age);
    }
}
