package com.training.restassured;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.objectMapper;

public class Api {

    HashMap<String, Object> query = new HashMap<String, Object>();
    public RequestSpecification req = given().log().all();

    public  RequestSpecification  getDefaultRequestSpecification(){
        return  given().log().all();

    }


    /**
     *
     * 在请求的数据较多时，使用模板数据配合部分字段替换
     *
     * @param path　json模板路径
     * @param map　　需要替换的字段
     * @return　返回替换之后的模板字符串
     */

    public String template(String path, HashMap<String, Object> map) {
        DocumentContext documentContext = JsonPath.parse(Api.class.getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return documentContext.jsonString();
    }

    /**
     * 完整的请求模板
     *
     * @param map　
     * @param method　　
     * @param body
     * @param url
     * @return　　根据参数返回响应
     */

    public Response getRequest(HashMap<String, Object> map, String method, String body, String url) {
        map.entrySet().forEach(entry -> {
            req = req.queryParam(entry.getKey(), entry.getValue());
        });

        if (method.toLowerCase().equals("get"))
            return req.when().get(url).then().log().all().extract().response();
        else
            return req.body(body).when().post(url).then().log().all().extract().response();
    }


    /**
     * 根据配置文件数据来发送请求
     *
     * @param path yaml文件路径
     * @param map　　
     * @return　返回根据yaml的参数发送请求的响应
     */
    public Response fromYaml(String path, HashMap<String, Object> map) {
        //fixed: 根据yaml生成接口定义并发送
        Restful restful = null;

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            restful = mapper.readValue(WeworkConfig.class.getResourceAsStream(path), Restful.class);
            map.entrySet().forEach(entry -> {
                req = req.queryParam(entry.getKey(), entry.getValue());
            });

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (restful.method.toLowerCase().equals("get"))
            return req.when().get(restful.url).then().log().all().extract().response();
        else
            return req.body(restful.body).when().post(restful.url).then().log().all().extract().response();

    }


    /**
     *
     * @param path  har路径
     * @param pattern　url
     * @param map  需替换模板
     * @return　　响应结果
     */


    public Response getResponseFromHar(String path,String pattern, HashMap<String,Object> map){
        Restful restful=getApiFromHar(path,pattern);
        restful=updateApiFromMap(restful,map);
        return  getResponseFromRestful(restful);
    }


    /**
     * Reading HAR from File:
     * @param path
     * @param pattern
     * @return　Restful
     */


    public Restful  getApiFromHar(String path, String pattern) {
        //Reading HAR from File:
        HarReader harReader = new HarReader();
        try {
            Har har = harReader.readFromFile(new File(URLDecoder.decode(getClass().getResource(path).getPath(), "utf-8")));

            HarRequest request = new HarRequest();
            Boolean match = false;
            for (HarEntry entry : har.getLog().getEntries()) {
                request = entry.getRequest();
                if (request.getUrl().matches(pattern)) {
                    match = true;
                    break;
                }
            }
            if (match == false) {
                request = null;
                throw new Exception("ddd");
            }
            Restful restful = new Restful();
            restful.method = request.getMethod().name().toLowerCase();
            //todo: 去掉url中的query部分
            restful.url = request.getUrl();
            request.getQueryString().forEach(q -> {
                restful.query.put(q.getName(), q.getValue());
            });
            restful.body = request.getPostData().getText();
            return restful;
        } catch (HarReaderException e) {
            e.printStackTrace();
            return  null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



    public Restful updateApiFromMap(Restful restful,HashMap<String,Object> map){
        if(map==null){
            return  restful;
        }
        if(restful.method.toLowerCase().contains("get")){
            map.entrySet().forEach(entry ->{
             restful.query.replace(entry.getKey(),entry.getValue().toString());
            });
        }
        if(restful.method.toLowerCase().contains("post")){
            if(map.containsKey("_body")){
                restful.body=map.get("_body").toString();
            }
            if(map.containsKey("_file")){
                String filePath=map.get("_file").toString();
                map.remove("_file");
                restful.body=template(filePath,map);
            }
        }
       return  restful;
    }




    public Response getResponseFromRestful(Restful restful){
        RequestSpecification  requestSpecification=getDefaultRequestSpecification();
        if(restful.query !=null){
            restful.query.entrySet().forEach(entry->{
                requestSpecification.queryParam(entry.getKey(),entry.getValue());
            });
        }
        if(restful.body!=null){
            requestSpecification.body(restful.body);
        }
        String[] url=updateUrl(restful.url);
        return requestSpecification.when().request(restful.method,restful.url).
                then().log().all().
                extract().response();

    }


    private String[] updateUrl(String url){
        //fixed: 多环境支持，替换url,更新host的header

        HashMap<String,String> hosts=WeworkConfig.geInstance().env.get(WeworkConfig.geInstance().current);
        String host="";
        String urlNew="";
        for(Map.Entry<String,String> entry :hosts.entrySet()){
            if(url.contains(entry.getKey())){
                host=entry.getKey();
                urlNew=url.replace(entry.getKey(),entry.getKey());
            }
        }
      return  new String[]{host ,urlNew};
    }



    public Response templateFromSwagger(String path, String pattern, HashMap<String, Object> map) {
        //todo: 支持从swagger自动生成接口定义并发送
        //todo: 分析swagger codegen
        //从har中读取请求，进行更新
        DocumentContext documentContext = JsonPath.parse(Api.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });

        String method = documentContext.read("method");
        String url = documentContext.read("url");
        return getDefaultRequestSpecification().when().request(method, url);
    }

}
