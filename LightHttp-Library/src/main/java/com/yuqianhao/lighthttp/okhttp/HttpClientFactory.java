package com.yuqianhao.lighthttp.okhttp;

import com.yuqianhao.lighthttp.core.RequestCollapse;
import com.yuqianhao.lighthttp.handler.HandlerManager;
import com.yuqianhao.lighthttp.handler.IRequestFirstHandle;
import com.yuqianhao.lighthttp.request.RequestConfig;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
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
            final IRequestFirstHandle requestFirstHandle = HandlerManager.getRequestFirstHandle();
            if(requestConfig!=null){
                builder.connectTimeout(requestConfig.connectTimeout(),requestConfig.timeUnit());
                builder.callTimeout(requestConfig.callTimeout(),requestConfig.timeUnit());
                builder.writeTimeout(requestConfig.writeTimeOut(),requestConfig.timeUnit());
                builder.readTimeout(requestConfig.readTimeout(),requestConfig.timeUnit());
                if(requestFirstHandle!=null){
                    builder.cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                            List<String> valueArray=new ArrayList<>(list.size());
                            for(Cookie cookie : list){
                                valueArray.add(cookie.value());
                            }
                            requestFirstHandle.cookie(httpUrl.host(),valueArray);
                        }

                        @NotNull
                        @Override
                        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                            List<String> cookieArray=requestFirstHandle.loadCookie(httpUrl.host());
                            List<Cookie> cookieList=new ArrayList<>(cookieArray.size());
                            for(String item : cookieArray){
                                cookieList.add(Cookie.parse(httpUrl,item));
                            }
                            return cookieList;
                        }
                    });
                }
            }
            okHttpClient=builder.build();
        }
        return okHttpClient;
    }

}
