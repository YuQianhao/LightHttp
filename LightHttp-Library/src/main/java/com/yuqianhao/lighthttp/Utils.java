package com.yuqianhao.lighthttp;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.util.Map;

public class Utils {

    private static final Gson GSON=new Gson();

    public static Gson getGson(){
        return GSON;
    }

    public static String map2string(Map<String,String> map){
        StringBuilder stringBuilder=new StringBuilder();
        for(Map.Entry<String,String> item:map.entrySet()){
            stringBuilder.append(item.getKey());
            stringBuilder.append("=");
            stringBuilder.append(item.getValue());
            stringBuilder.append("&");
        }
        if(stringBuilder.length()>0){
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder.toString();
    }
}
