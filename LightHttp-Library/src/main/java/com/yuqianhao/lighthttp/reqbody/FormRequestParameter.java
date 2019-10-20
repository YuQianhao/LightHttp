package com.yuqianhao.lighthttp.reqbody;

import com.yuqianhao.lighthttp.Utils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class FormRequestParameter extends AbsRequestParameter{

    private Map<String,String> parameterMap;

    private FormRequestParameter(Charset charset) {
        super("application/x-www-form-urlencoded", charset);
        this.parameterMap=new HashMap<>();
    }

    public FormRequestParameter add(String key,String value){
        this.parameterMap.put(key,value);
        return this;
    }

    public FormRequestParameter addMap(Map<String,String> map){
        for(Map.Entry<String,String> item:map.entrySet()){
            this.parameterMap.put(item.getKey(),item.getValue());
        }
        return this;
    }

    public FormRequestParameter header(String key,String value){
        addHeader(key,value);
        return this;
    }

    public FormRequestParameter header(Map<String,String> header){
        for(Map.Entry<String,String> item:header.entrySet()){
            addHeader(item.getKey(),item.getValue());
        }
        return this;
    }

    public static FormRequestParameter create(){
        return new FormRequestParameter(Charset.defaultCharset());
    }

    public static FormRequestParameter create(Charset charset){
        return new FormRequestParameter(charset);
    }

    @Override
    public String data() {
        return Utils.map2string(parameterMap);
    }
}
