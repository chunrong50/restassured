package com.training.restassured.contact;

import com.training.restassured.Api;
import com.training.restassured.WeWork;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class Member extends  Api{
    HashMap<String,Object>  map=new HashMap<String,Object>();


    public Response create(){
        String  random=String.valueOf(System.currentTimeMillis());
        String  random1=random.substring(7);
        HashMap<String,Object>  datamap=new HashMap<String,Object>();
        datamap.put("userid",random1);
        datamap.put("name","lisi"+random1);
        datamap.put("department","3");
        datamap.put("is_leader_in_dept","0");
        datamap.put("email",random+"@qq.com");
        String  body=template("/data/member.json",datamap);
        map.put("access_token", WeWork.getToken());

        return getRequest(map,"post",body,"https://qyapi.weixin.qq.com/cgi-bin/user/create");


    }

    public  Response readMember(String userid){
        map.put("access_token",WeWork.getToken());
        map.put("userid",userid);
       return  getRequest(map,"get","","https://qyapi.weixin.qq.com/cgi-bin/user/get");

    }

}
