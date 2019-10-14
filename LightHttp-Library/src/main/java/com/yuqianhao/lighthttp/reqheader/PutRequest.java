package com.yuqianhao.lighthttp.reqheader;

public class PutRequest implements IRequestAddress{

    private String url;

    private int method;

    private Object tag;

    private PutRequest(){}

    public static PutRequest create(String url){
        PutRequest putRequest=new PutRequest();
        putRequest.method=MethodType.PUT;
        putRequest.url=url;
        return putRequest;
    }

    public static PutRequest create(String url,Object tag){
        PutRequest putRequest=new PutRequest();
        putRequest.method=MethodType.PUT;
        putRequest.url=url;
        putRequest.tag=tag;
        return putRequest;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public int method() {
        return method;
    }

    @Override
    public Object tag() {
        return tag;
    }
}
