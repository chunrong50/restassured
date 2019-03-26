package com.training.restassured.contact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DepartmentTest {
    Department  department;

    @BeforeEach
    void beforeEach(){
        if(department==null)
            department=new Department();
    }

    @Test
    void list() {
        department.list("").then().statusCode(200).body("department.name[1]",equalTo("department2"));
        department.list("３").then().statusCode(200).body("department.name[2]",equalTo("测试部"));

    }

    @Test
    void create(){
        department.create().then().extract().path("errmsg").equals("created");
    }


    @Test
    void create1(){
        //todo: 运行时修改参数
        department.create("test8","7").then().statusCode(200).body("errmsg",equalTo("created"));
      //  department.create("demo5","3").then().statusCode(200).body("errmsg",equalTo("department existed"));
    }



    @Test
    void  update(){
        department.update("demoupdate").then().statusCode(200).body("errmsg",equalTo("updated"));
    }

    @Test
    void delete(){
        //todo:每次运行需修改id
        assertThat(department.delete("13").path("errmsg"),equalTo("deleted"));
    }
}