package com.training.restassured;

import static io.restassured.RestAssured.given;


public class WeWork {

private  static String token;

    /**
     * 获得token
     * @return
     */

    public static   String getAccess_token(){
        return  given().log().all().
                queryParam("corpid",WeworkConfig.geInstance().corpid).
                queryParam("corpsecret",WeworkConfig.geInstance().secret_contact).
                when().get("https://qyapi.weixin.qq.com/cgi-bin/gettoken").
                then().log().all().extract().path("access_token");
    }

    public  static  String   getToken(){
        //todo: 获取多种token
        if(token==null)
            token=getAccess_token();
        return token;
    }

}
