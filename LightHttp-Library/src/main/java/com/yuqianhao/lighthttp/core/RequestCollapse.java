package com.yuqianhao.lighthttp.core;

import com.google.gson.JsonParser;
import com.yuqianhao.lighthttp.Utils;
import com.yuqianhao.lighthttp.callback.Nullptr;
import com.yuqianhao.lighthttp.callback.RequestCode;
import com.yuqianhao.lighthttp.callback.ResponseCallback;
import com.yuqianhao.lighthttp.convert.ConvertProcessManager;
import com.yuqianhao.lighthttp.convert.TypeConvertProcessor;
import com.yuqianhao.lighthttp.reqheader.MethodType;
import com.yuqianhao.lighthttp.request.RequestConfig;
import com.yuqianhao.lighthttp.request.RequestInterceptor;
import com.yuqianhao.lighthttp.request.RequestMessage;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kotlin.Pair;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

public class RequestCollapse {

     private static class _$Interceptor implements Interceptor{

        public _$Interceptor(RequestInterceptor requestInterceptor){
            this.interceptor=requestInterceptor;
        }

        private RequestInterceptor interceptor;

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request=chain.request();
            RequestBody requestBody=request.body();
            Map<String,String> headerMap=new HashMap<>();
            Headers headers=request.headers();
            Iterator<Pair<String,String>> headerIterator=headers.iterator();
            while(headerIterator.hasNext()){
                Pair<String,String> item=headerIterator.next();
                headerMap.put(item.getFirst(),item.getSecond());
            }
            String contentType=(requestBody!=null && requestBody.contentType()!=null)?requestBody.contentType().type():"";
            Charset charset=(requestBody!=null && requestBody.contentType()!=null)?requestBody.contentType().charset():Charset.defaultCharset();
            String requestData=(requestBody!=null)?requestBody.toString():"";
            interceptor.request(request.url().toString(),
                    request.method(),
                    contentType,
                    charset,
                    headerMap,
                    requestData);
            return chain.proceed(chain.request());
        }
    }

    private OkHttpClient okHttpClient;

    public RequestCollapse(final RequestConfig requestConfig){
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        if(requestConfig!=null){
            builder.connectTimeout(requestConfig.connectTimeout(),requestConfig.timeUnit());
            builder.callTimeout(requestConfig.callTimeout(),requestConfig.timeUnit());
            builder.writeTimeout(requestConfig.writeTimeOut(),requestConfig.timeUnit());
            builder.readTimeout(requestConfig.readTimeout(),requestConfig.timeUnit());
            if(requestConfig.cookieCallback()!=null){
                builder.cookieJar(requestConfig.cookieCallback());
            }
            if(requestConfig.requestInterceptor()!=null){
                builder.addInterceptor(new _$Interceptor(requestConfig.requestInterceptor()));
            }
        }
        okHttpClient=builder.build();
    }

    private Request buildRequest(RequestMessage requestMessage){
        Request.Builder builder=new Request.Builder();
        builder.url(requestMessage.getUrl());
        if(requestMessage.getMethod()== MethodType.GET){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(requestMessage.getUrl());
            stringBuilder.append("?");
            stringBuilder.append(requestMessage.getData());
            if(stringBuilder.charAt(stringBuilder.length()-1)=='?'){
                stringBuilder.deleteCharAt(stringBuilder.length()-1
                );
            }
            builder.url(stringBuilder.toString());
        }else{
            builder.post(RequestBody.create(requestMessage.getData(), MediaType.parse(requestMessage.getContentType()+";charset="+requestMessage.getCharset().toString())));
        }
        Headers headers=requestMessage.getHeaders();
        Iterator<Pair<String, String>> iterator=headers.iterator();
        while(iterator.hasNext()){
            Pair<String,String> item=iterator.next();
            builder.addHeader(item.getFirst(),item.getSecond());
        }
        if(requestMessage.getTag()!=null){
            builder.tag(requestMessage.getTag());
        }
        return builder.build();
    }

    private void callResult(ResponseCallback responseCallback,Response response){
        try {
            responseCallback.reSet(response);
        } catch (IOException e) {
            responseCallback.onFailure(RequestCode.LOCAL_ERROR,"Unable to create objects "+ResponseCallback.class.getName());
        }
        if(responseCallback.getCode()==RequestCode.REQUEST_SUCCESS){
            Type superclassType=responseCallback.getClass().getGenericSuperclass();
            Type type;
            if(superclassType instanceof ParameterizedType){
                type=((ParameterizedType)responseCallback.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            }else{
                type=Nullptr.class;
            }
            if(!type.equals(Nullptr.class) && !type.equals(Void.class)){
                TypeConvertProcessor typeConvertProcessor= ConvertProcessManager.get((Class) type);
                if(typeConvertProcessor!=null){
                    responseCallback.onSuccess(typeConvertProcessor.convertType(responseCallback.getResponseBuffer()));
                }else{
                    responseCallback.onSuccess(Utils.getGson().fromJson(responseCallback.getResponseBufferString(),type));
                }
            }
        }else{
            responseCallback.onFailure(responseCallback.getCode(),responseCallback.getMessage());
        }
        responseCallback.onCompany();
    }

    public void synchronization(RequestMessage requestMessage){
        ResponseCallback responseCallback=requestMessage.getResponseCallback();
        try {
            Response response=okHttpClient.newCall(buildRequest(requestMessage)).execute();
            callResult(requestMessage.getResponseCallback(),response);
        } catch (IOException e) {
            responseCallback.onFailure(RequestCode.LOCAL_ERROR,e.getMessage());
        }
    }

    public void asynchronous(final RequestMessage requestMessage){
        okHttpClient.newCall(buildRequest(requestMessage)).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requestMessage.getResponseCallback().onFailure(RequestCode.LOCAL_ERROR,e.getMessage());
                requestMessage.getResponseCallback().onCompany();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                callResult(requestMessage.getResponseCallback(),response);
            }
        });
    }

}
