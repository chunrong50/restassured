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
    void  update(){
        department.update().then().extract().path("errmsg").equals("updated");
    }

    @Test
    void delete(){
        assertThat(department.delete("6").path("errmsg"),equalTo("deleted"));
    }
}