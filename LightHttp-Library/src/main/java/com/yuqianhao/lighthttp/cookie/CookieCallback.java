package com.yuqianhao.lighthttp.cookie;

import java.io.IOException;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Response;

public class CookieCallback implements CookieJar {
    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        return null;
    }

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {

    }

}
