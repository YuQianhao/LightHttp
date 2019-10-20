package com.yuqianhao.lighthttp.reqbody;

import com.yuqianhao.lighthttp.Utils;

import java.nio.charset.Charset;
import java.util.Map;

public class JsonRequestParameter extends AbsRequestParameter{

    private String jsonSource;

    private JsonRequestParameter(Charset charset) {
        super("application/json", charset);
    }

    public JsonRequestParameter json(Object obj){
        this.jsonSource= Utils.getGson().toJson(obj);
        return this;
    }

    public JsonRequestParameter jsonSource(String jsonSource){
        this.jsonSource=jsonSource;
        return this;
    }

    public JsonRequestParameter header(String key,String value){
        addHeader(key,value);
        return this;
    }

    public JsonRequestParameter header(Map<String,String> header){
        for(Map.Entry<String,String> item:header.entrySet()){
            addHeader(item.getKey(),item.getValue());
        }
        return this;
    }

    public static final JsonRequestParameter create(Charset charset){
        JsonRequestParameter jsonRequestParameter=new JsonRequestParameter(charset);
        return jsonRequestParameter;
    }

    public static final JsonRequestParameter create(){
        JsonRequestParameter jsonRequestParameter=new JsonRequestParameter(Charset.defaultCharset());
        return jsonRequestParameter;
    }


    @Override
    public String data() {
        return jsonSource;
    }
}
