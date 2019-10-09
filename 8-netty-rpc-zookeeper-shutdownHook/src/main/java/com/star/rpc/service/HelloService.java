package com.star.rpc.service;

import com.star.rpc.model.Person;

public interface HelloService {
    String hello(String name);
    Person hello(String name, int age);
}
