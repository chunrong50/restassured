package com.training.restassured.contact;

import com.training.restassured.Api;
import com.training.restassured.WeWork;
import com.training.restassured.WeworkConfig;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class Member extends  Api{
    HashMap<String,Object>  map=new HashMap<String,Object>();


    public Response create(){
        String  random=String.valueOf(System.currentTimeMillis());
        String  random1=random.substring(7);
        HashMap<String,Object>  templatemap=new HashMap<String,Object>();
        templatemap.put("userid",random1);
        templatemap.put("name","lisi"+random1);
        templatemap.put("department","3");
        templatemap.put("is_leader_in_dept","0");
        templatemap.put("email",random+"@qq.com");
        String  body=template("/json/CreatMember.json",templatemap);
        map.put("access_token", WeWork.getToken());



        return getRequest(map,"post",body,"https://qyapi.weixin.qq.com/cgi-bin/user/create");


    }

    public  Response readMember(String userid){
        map.put("access_token",WeWork.getToken());
        map.put("userid",userid);

       return  getRequest(map,"get","","https://qyapi.weixin.qq.com/cgi-bin/user/get");

    }

}
