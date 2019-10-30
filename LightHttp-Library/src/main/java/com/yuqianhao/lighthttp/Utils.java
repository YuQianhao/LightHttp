package com.yuqianhao.lighthttp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

public class Utils {

    private static Gson GSON;

    static {
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        GSON=gsonBuilder.create();
    }

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
