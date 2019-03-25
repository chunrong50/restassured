package com.training.restassured;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

import java.net.URL;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class Api {

    HashMap<String ,Object> query=new HashMap<String,Object>();
  public   RequestSpecification  request=given();


    /**
     * 修改模板中的json字段，用map中的字段替换
     * 在请求的数据较多时，使用模板数据配合部分字段替换
     * @param path
     * @param map
     * @return
     */

    public   String template(String path ,HashMap<String,Object> map){
        DocumentContext  documentContext= JsonPath.parse(Api.class.getResourceAsStream(path));
        map.entrySet().forEach(entry->{
            documentContext.set(entry.getKey(),entry.getValue());
        });
        return  documentContext.jsonString();
    }

    /**
     * 完整的请求模板
     * @param map
     * @param method
     * @param body
     * @param url
     * @return
     */

    public  Response  getRequest(HashMap<String,Object> map,String method,String body, String url){

           map.entrySet().forEach(entry->{
               request=  request.queryParam(entry.getKey(),entry.getValue()) ;
           });

               if(method.equals("get"))
                   return  request.log().all().when().get(url).then().log().all().extract().response();
               else
                   return  request.log().all().body(body).when().post(url).then().log().all().extract().response();
       }





public Response templateFromYaml(String path, HashMap<String,Object> map){
    //todo : 根据yaml生成接口定义并发送

    ObjectMapper mapper=new ObjectMapper(new YAMLFactory());
    try {
     Restful restful=   mapper.readValue(WeworkConfig.class.getResourceAsStream(path),Restful.class);
    }catch (IOException e){
        e.printStackTrace();
        return  null;
    }

return  null;
}

}
