package com.yuqianhao.lighthttp.request;

import com.yuqianhao.lighthttp.cookie.CookieCallback;
import com.yuqianhao.lighthttp.handler.IRequestFirstHandle;

import java.util.concurrent.TimeUnit;

/**
 * 网络请求配置类
 */
public class RequestConfig {

    public TimeUnit timeUnit(){return TimeUnit.MINUTES;}

    public long connectTimeout(){return 1L;}

    public long writeTimeOut(){return 5L;}

    public long readTimeout(){return 5L;}

    public long callTimeout(){return 1L;}

    public final CookieCallback cookieCallback(){return null;}

    /**
     * @deprecated 这个拦截方法已过时，
     *             建议使用功能更强大拦截器，详见
     *             {@link com.yuqianhao.lighthttp.LightHttp#setRequestFirstHandler(IRequestFirstHandle)}
     * */
    @Deprecated
    public RequestInterceptor requestInterceptor(){return null;}

}
