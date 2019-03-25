package com.training.restassured;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;

public class WeworkConfig {

   public   String  corpid="";
   public   String  secret_contact="";


    private  static  WeworkConfig  weworkConfig;

    public  static WeworkConfig geInstance() {

        if(weworkConfig==null)
        {
            weworkConfig=load("/conf/WeworkConfig.yaml");
           /* System.out.println(weworkConfig);
            System.out.println(weworkConfig.corpid);*/
        }

        return weworkConfig;
    }


    public static WeworkConfig load(String path){

        // read from yaml or json

        ObjectMapper mapper=new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(WeworkConfig.class.getResourceAsStream(path), WeworkConfig.class);  // 把读入的数据作为WeworkConfig类的数据
        }catch (IOException e){
            e.printStackTrace();
            return  null;
        }

    }
}
