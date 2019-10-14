package com.yuqianhao.lighthttp.reqheader;

public class GetRequest implements IRequestAddress{

    private String url;

    private int method;

    private Object tag;

    private GetRequest(){}

    public static GetRequest create(String url){
        GetRequest getRequest=new GetRequest();
        getRequest.method=MethodType.GET;
        getRequest.url=url;
        return getRequest;
    }

    public static GetRequest create(String url,Object tag){
        GetRequest getRequest=new GetRequest();
        getRequest.method=MethodType.GET;
        getRequest.url=url;
        getRequest.tag=tag;
        return getRequest;
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
