package com.training.restassured;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ApiTest {

    @Test
    void template() {

        HashMap<String,Object>  map=new HashMap<String,Object>();
        map.put("userid","009");
        map.put("name",";lisi");
       System.out.println( new Api().template("/json/CreatMember.json",map));

        System.out.println(String.valueOf(System.currentTimeMillis()));
    }

    @Test
    void templateFromYaml() {
    }
}