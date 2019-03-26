package com.training.restassured.contact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {
    Member  member;

    @BeforeEach
    void  beforeEach(){
       member=new Member();
    }

    @Test
    void create() {
        member.create().path("errmsg").equals("created");
    }
    @Test
    void readMember(){
        member.readMember("172113").path("name").equals("lisi172113");

    }

}