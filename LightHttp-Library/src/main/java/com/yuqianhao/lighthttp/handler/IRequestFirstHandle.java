package com.yuqianhao.lighthttp.handler;

import com.yuqianhao.lighthttp.model.Cookie;

import java.util.List;
import java.util.Map;

public interface IRequestFirstHandle {

    String handlerUrl(String requestUrl);

    Map<String,String> handlerHeader(Map<String,String> header);

    String handlerBody(String body);

    String handlerResponse(String response);

    void cookie(String hosts, List<Cookie> value);

    List<Cookie> loadCookie(String hosts);

}
