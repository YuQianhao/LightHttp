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
            builder.cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                    IRequestFirstHandle requestFirstHandle=HandlerManager.getRequestFirstHandle();
                    if(requestFirstHandle!=null){
                        List<com.yuqianhao.lighthttp.model.Cookie> valueArray=new ArrayList<>(list.size());
                        for(Cookie cookie : list){
                            com.yuqianhao.lighthttp.model.Cookie reqCookie=new com.yuqianhao.lighthttp.model.Cookie();
                            reqCookie.setDomain(cookie.domain());
                            reqCookie.setExpiresAt(cookie.expiresAt());
                            reqCookie.setName(cookie.name());
                            reqCookie.setPath(cookie.path());
                            reqCookie.setSecure(cookie.secure());
                            reqCookie.setHostOnly(cookie.hostOnly());
                            reqCookie.setHttpOnly(cookie.httpOnly());
                            reqCookie.setPersistent(cookie.persistent());
                            reqCookie.setValue(cookie.value());
                            valueArray.add(reqCookie);
                        }
                        requestFirstHandle.cookie(httpUrl.host(),valueArray);
                    }
                }

                @NotNull
                @Override
                public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                    IRequestFirstHandle requestFirstHandle=HandlerManager.getRequestFirstHandle();
                    if(requestFirstHandle!=null){
                        List<com.yuqianhao.lighthttp.model.Cookie> cookieArray=requestFirstHandle.loadCookie(httpUrl.host());
                        if(cookieArray==null){
                            return new ArrayList<>();
                        }
                        List<Cookie> cookieList=new ArrayList<>(cookieArray.size());
                        for(com.yuqianhao.lighthttp.model.Cookie item : cookieArray){
                            Cookie cookie=new Cookie.Builder()
                                    .name(item.getName())
                                    .value(item.getValue())
                                    .expiresAt(item.getExpiresAt())
                                    .domain(item.getDomain())
                                    .path(item.getPath())
                                    .build();
                            cookieList.add(cookie);
                        }
                        return cookieList;
                    }else{
                        return new ArrayList<>();
                    }
                }
            });
            if(requestConfig!=null){
                builder.connectTimeout(requestConfig.connectTimeout(),requestConfig.timeUnit());
                builder.callTimeout(requestConfig.callTimeout(),requestConfig.timeUnit());
                builder.writeTimeout(requestConfig.writeTimeOut(),requestConfig.timeUnit());
                builder.readTimeout(requestConfig.readTimeout(),requestConfig.timeUnit());
            }
            okHttpClient=builder.build();
        }
        return okHttpClient;
    }

}
