package com.training.restassured.contact;

import com.jayway.jsonpath.JsonPath;
import com.training.restassured.Api;
import com.training.restassured.WeWork;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class Department extends Api {
    public HashMap<String,Object>  map=new HashMap<String,Object>();

    public Response list( String id){
        map.put("access_token",WeWork.getToken());
        map.put("id",id);

      //  return  getRequest(map,"get","","https://qyapi.weixin.qq.com/cgi-bin/department/list");　　//通过类对接口的拼装来发送请求
        return  templateFromYaml("/restfulApi/list.yaml",map);   // 通过接口配置文件来完成请求数据的填充，来发送请求
    }

    /**
     * 自动创建部门
     * @return
     */
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


    /**
     * 创建部门－－根据模板body修改部门名＼父节点ＩＤ
     * @param name
     * @param parentid
     * @return
     */
    public  Response create(String name,String parentid){
        String body= JsonPath.parse(Department.class.getResourceAsStream("/data/department.json")).
                set("$.name",name).
                set("$.parentid",parentid).
                delete("$.id").jsonString();
        map.put("access_token",WeWork.getToken());
        return   getRequest(map,"post",body,"https://qyapi.weixin.qq.com/cgi-bin/department/create");
    }





    public  Response  update(String newName){
        map.put("access_token",WeWork.getToken());
        String body=JsonPath.parse(Department.class.getResourceAsStream("/data/department.json")).
                set("$.name",newName).jsonString();
        return  getRequest(map,"post",body,"https://qyapi.weixin.qq.com/cgi-bin/department/update");
    }


    public  Response delete(String  id){
        map.put("access_token",WeWork.getToken());
        map.put("id",id);

        return  getRequest(map,"get","","https://qyapi.weixin.qq.com/cgi-bin/department/delete");
    }

    public  void deleteAll() {
        ArrayList<String> list = list("").path("department.id");

        for (String s : list) {
            delete(s);
        }

    }
}
