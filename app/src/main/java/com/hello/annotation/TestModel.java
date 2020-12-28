package com.hello.annotation;

import com.hello.inject_annotation.Json;

@Json("TestModel")
public class TestModel {
    @Json("name") public String name;
    @Json("age") public String age;
    @Json("city") public String city;
    public String address;
}
