package com.yuqianhao.lighthttp.reqheader.v2;

import com.yuqianhao.lighthttp.reqheader.IRequestAddress;

import java.lang.annotation.Annotation;

public class RequestMethodV2Manager {

    public static final IRequestAddress get(Class clazz){
        Annotation annotation;
        if((annotation=clazz.getAnnotation(GetRequest.class))!=null){
            return com.yuqianhao.lighthttp.reqheader.GetRequest.create(((GetRequest)annotation).url(),((GetRequest)annotation).charset());
        }else if((annotation=clazz.getAnnotation(PostRequest.class))!=null){
            return com.yuqianhao.lighthttp.reqheader.PostRequest.create(((PostRequest)annotation).url(),((PostRequest)annotation).charset());
        }else if((annotation=clazz.getAnnotation(PutRequest.class))!=null){
            return com.yuqianhao.lighthttp.reqheader.PutRequest.create(((PutRequest)annotation).url(),((PutRequest)annotation).charset());
        }else if((annotation=clazz.getAnnotation(DeleteRequest.class))!=null){
            return com.yuqianhao.lighthttp.reqheader.DeleteRequest.create(((DeleteRequest)annotation).url(),((DeleteRequest)annotation).charset());
        }else {
            return null;
        }
    }


}
