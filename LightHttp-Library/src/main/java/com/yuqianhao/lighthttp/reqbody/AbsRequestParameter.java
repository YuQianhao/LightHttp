package com.yuqianhao.lighthttp.reqbody;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

public class AbsRequestParameter implements IRequestParameter{

    private String mediaType;

    private Charset charset;

    private Map<String,String> headerMap;

    public AbsRequestParameter(String mediaType,Charset charset){
        this.mediaType=mediaType;
        this.charset=charset;
        this.headerMap=new HashMap<>();
    }

    public void addHeader(String key,String value){
        this.headerMap.put(key,value);
    }

    @Override
    public final String mediaType() {
        return mediaType;
    }

    @Override
    public final Charset charset() {
        return charset;
    }

    @Override
    public final Headers headers() {
        Headers.Builder builder=new Headers.Builder();
        for(Map.Entry<String,String> item:this.headerMap.entrySet()){
            builder.add(item.getKey(),item.getValue());
        }
        return builder.build();
    }

    @Override
    public String data() {
        return null;
    }
}
