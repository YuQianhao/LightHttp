package com.yuqianhao.lighthttp.okhttp;

import com.yuqianhao.lighthttp.core.RequestCollapse;
import com.yuqianhao.lighthttp.request.RequestConfig;

import okhttp3.OkHttpClient;

public class HttpClientFactory {

    private static OkHttpClient okHttpClient;

    public static okhttp3.OkHttpClient getOkHttpClient() {
        if(okHttpClient==null){
            return getOkHttpClient(null);
        }
        return okHttpClient;
    }

    public static okhttp3.OkHttpClient getOkHttpClient(RequestConfig requestConfig) {
        if(okHttpClient==null){
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
                    builder.addInterceptor(new RequestCollapse._$Interceptor(requestConfig.requestInterceptor()));
                }
            }
            okHttpClient=builder.build();
        }
        return okHttpClient;
    }

}
