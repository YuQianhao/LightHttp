package com.yuqianhao.lighthttp.reqheader;

public class DeleteRequest implements IRequestAddress{

    private String url;

    private int method;

    private Object tag;

    private DeleteRequest(){}

    public static DeleteRequest create(String url){
        DeleteRequest deleteRequest=new DeleteRequest();
        deleteRequest.method=MethodType.DELETE;
        deleteRequest.url=url;
        return deleteRequest;
    }

    public static DeleteRequest create(String url,Object tag){
        DeleteRequest deleteRequest=new DeleteRequest();
        deleteRequest.method=MethodType.DELETE;
        deleteRequest.url=url;
        deleteRequest.tag=tag;
        return deleteRequest;
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
