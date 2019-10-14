package com.yuqianhao.lighthttp.reqheader;

public class PostRequest implements IRequestAddress{

    private String url;

    private int method;

    private Object tag;

    private PostRequest(){}

    public static PostRequest create(String url){
        PostRequest postRequest=new PostRequest();
        postRequest.method=MethodType.POST;
        postRequest.url=url;
        return postRequest;
    }

    public static PostRequest create(String url,Object tag){
        PostRequest postRequest=new PostRequest();
        postRequest.method=MethodType.POST;
        postRequest.url=url;
        postRequest.tag=tag;
        return postRequest;
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
