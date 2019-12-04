package com.yuqianhao.lighthttp;

import com.yuqianhao.lighthttp.callback.ResponseCallback;
import com.yuqianhao.lighthttp.convert.ConvertProcessManager;
import com.yuqianhao.lighthttp.convert.ConvertProcessor;
import com.yuqianhao.lighthttp.convert.TypeConvertProcessor;
import com.yuqianhao.lighthttp.core.RequestCollapse;
import com.yuqianhao.lighthttp.download.DownloadImpl;
import com.yuqianhao.lighthttp.download.IDownloadAction;
import com.yuqianhao.lighthttp.reqbody.FormRequestParameter;
import com.yuqianhao.lighthttp.reqbody.IRequestParameter;
import com.yuqianhao.lighthttp.reqheader.IRequestAddress;
import com.yuqianhao.lighthttp.reqheader.v2.RequestMethodV2Manager;
import com.yuqianhao.lighthttp.request.RequestConfig;
import com.yuqianhao.lighthttp.request.RequestMessage;

import java.nio.charset.Charset;

public class LightHttp {

    private RequestCollapse requestCollapse;

    private IRequestAddress requestAddress;

    private IRequestParameter requestParameter;

    private ResponseCallback responseCallback;

    private static LightHttp LIGHT_HTTP;

    public LightHttp(RequestConfig requestConfig){
        requestCollapse=new RequestCollapse(requestConfig);
    }

    public void clear(){
        requestAddress=null;
        requestParameter=null;
        responseCallback=null;
    }

    public final LightHttp params(IRequestParameter requestParameter){
        this.requestParameter=requestParameter;
        return this;
    }

    public final LightHttp callback(ResponseCallback responseCallback){
        this.responseCallback=responseCallback;
        return this;
    }

    private RequestMessage buildRequestMessage(){
        if(this.requestParameter==null){
            this.requestParameter= FormRequestParameter.create();
        }
        return new RequestMessage(requestAddress,requestParameter,responseCallback);
    }

    public void async(){
        requestCollapse.asynchronous(buildRequestMessage());
    }

    public void sync(){
        requestCollapse.synchronization(buildRequestMessage());
    }

    public static final void init(RequestConfig requestConfig){
        LIGHT_HTTP=new LightHttp(requestConfig);
    }

    public static final void loadTypeConvert(Class ...typeConvertProcessors) throws InstantiationException, IllegalAccessException {
        for(Class item:typeConvertProcessors){
            ConvertProcessor convertProcessor= (ConvertProcessor) item.getAnnotation(ConvertProcessor.class);
            if(convertProcessor!=null){
                ConvertProcessManager.addProcessor(convertProcessor.className(), (TypeConvertProcessor) item.newInstance());
            }
        }
    }

    private static final void initLightHttp(){
        if(LIGHT_HTTP==null){
            LIGHT_HTTP=new LightHttp(null);
        }
        LIGHT_HTTP.clear();
    }

    public static final LightHttp create(IRequestAddress address){
        initLightHttp();
        LIGHT_HTTP.requestAddress=address;
        return LIGHT_HTTP;
    }

    public static final IDownloadAction createDownload(){
        return new DownloadImpl();
    }

    public static final IDownloadAction createDownload(String url,String path){
        return new DownloadImpl(url,path);
    }

    public static final IDownloadAction createDownload(String url){
        return new DownloadImpl(url);
    }

    public static final LightHttp loadRequest(Class requestClass){
        initLightHttp();
        IRequestAddress requestAddress=RequestMethodV2Manager.get(requestClass);
        if(requestAddress==null){
            throw new RuntimeException("The class "+requestClass.getName()+" is not a class containing requests.");
        }
        LIGHT_HTTP.requestAddress=requestAddress;
        return LIGHT_HTTP;
    }


}
