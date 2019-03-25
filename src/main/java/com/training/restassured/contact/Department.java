package com.training.restassured.contact;

import com.training.restassured.Api;
import com.training.restassured.WeWork;
import com.training.restassured.WeworkConfig;
import io.restassured.response.Response;

import java.util.HashMap;
import  java.net.URL;

import static io.restassured.RestAssured.given;

public class Department extends Api {
    public HashMap<String,Object>  map=new HashMap<String,Object>();



    public Response list( String id){
        map.put("access_token",WeWork.getToken());
        map.put("id",id);

        return  getRequest(map,"get","","https://qyapi.weixin.qq.com/cgi-bin/department/list");


    }


    public  Response create(){
        String  random=(String.valueOf(System.currentTimeMillis())).substring(9);
        String body="{\n" +
                "   \"name\": \"department"+random+"\",\n" +      //  BUG ： 部门名称必须为英文
                "   \"parentid\": 1,\n" +
                "   \"order\": 6,\n" +
                "   \"id\": \n" +
                "}" ;
        map.put("access_token",WeWork.getToken());


        return   getRequest(map,"post",body,"https://qyapi.weixin.qq.com/cgi-bin/department/create");




    }

    public  Response  update(){

        map.put("access_token",WeWork.getToken());
        String body="{\n" +
                "   \"name\": \"departmentivy\",\n" +       //  BUG ： 部门名称必须为英文
                "   \"parentid\": 1,\n" +
                "   \"order\": 6,\n" +
                "   \"id\":9 \n" +
                "}" ;
        return  getRequest(map,"post",body,"https://qyapi.weixin.qq.com/cgi-bin/department/update");



    }


    public  Response delete(String  id){
        map.put("access_token",WeWork.getToken());
        map.put("id",id);

        return  getRequest(map,"get","","https://qyapi.weixin.qq.com/cgi-bin/department/delete");


    }


}
