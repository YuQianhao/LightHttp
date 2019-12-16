package com.yuqianhao.lighthttp.core;

import android.os.Handler;
import android.os.Looper;

import com.yuqianhao.lighthttp.Utils;
import com.yuqianhao.lighthttp.callback.Nullptr;
import com.yuqianhao.lighthttp.callback.RequestCode;
import com.yuqianhao.lighthttp.callback.ResponseCallback;
import com.yuqianhao.lighthttp.convert.ConvertProcessManager;
import com.yuqianhao.lighthttp.convert.TypeConvertProcessor;
import com.yuqianhao.lighthttp.okhttp.HttpClientFactory;
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

public class RequestCollapse {

    public static class _$Interceptor implements Interceptor{

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

    private static final Handler HANDLER=new Handler(Looper.getMainLooper());

    public RequestCollapse(final RequestConfig requestConfig){
        okHttpClient= HttpClientFactory.getOkHttpClient(requestConfig);
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
//            builder.post(RequestBody.create(requestMessage.getData(), MediaType.parse(requestMessage.getContentType()+";charset="+requestMessage.getCharset().toString())));
            String _ReqData=requestMessage.getData();
            builder.post(RequestBody.create(_ReqData==null?"":_ReqData, MediaType.parse(requestMessage.getContentType())));
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

    private void callResult(final ResponseCallback responseCallback, Response response,Type refType){
        if(responseCallback==null){return;}
        try {
            responseCallback.reSet(response);
        } catch (IOException e) {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    responseCallback.onFailure(RequestCode.LOCAL_ERROR,"Unable to create objects "+ResponseCallback.class.getName());
                }
            });
        }
        if(responseCallback.getCode()==RequestCode.REQUEST_SUCCESS){
            Type superclassType=responseCallback.getClass().getGenericSuperclass();
            Type type;
            if(refType==null){
                if(superclassType instanceof ParameterizedType){
                    type=((ParameterizedType)responseCallback.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                }else{
                    type=Nullptr.class;
                }
            }else{
                type=refType;
            }
            if(!type.equals(Nullptr.class) && !type.equals(Void.class) && !type.equals(Object.class)){
                final TypeConvertProcessor typeConvertProcessor= ConvertProcessManager.get(type);
                if(typeConvertProcessor!=null){
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            responseCallback.onSuccess(typeConvertProcessor.convertType(responseCallback.getResponseBuffer()));
                        }
                    });
                }else{
                    final Type _SendType=type;
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            responseCallback.onSuccess(Utils.getGson().fromJson(responseCallback.getResponseBufferString(),_SendType));
                        }
                    });
                }
            }else{
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        responseCallback.onSuccess(null);
                    }
                });
            }
        }else{
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    responseCallback.onFailure(responseCallback.getCode(),responseCallback.getMessage());
                }
            });
        }
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                responseCallback.onCompany();
            }
        });
    }

    public void synchronization(RequestMessage requestMessage,Type type){
        ResponseCallback responseCallback=requestMessage.getResponseCallback();
        try {
            Response response=okHttpClient.newCall(buildRequest(requestMessage)).execute();
            callResult(requestMessage.getResponseCallback(),response,type);
        } catch (IOException e) {
            responseCallback.onFailure(RequestCode.LOCAL_ERROR,e.getMessage());
        }
    }

    public void asynchronous(final RequestMessage requestMessage){
        okHttpClient.newCall(buildRequest(requestMessage)).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        requestMessage.getResponseCallback().onFailure(RequestCode.LOCAL_ERROR,e.getMessage());
                        requestMessage.getResponseCallback().onCompany();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                callResult(requestMessage.getResponseCallback(),response,null);
            }
        });
    }

}
