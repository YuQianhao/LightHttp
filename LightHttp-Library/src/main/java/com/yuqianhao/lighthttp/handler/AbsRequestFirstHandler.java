package com.yuqianhao.lighthttp.handler;

import com.yuqianhao.lighthttp.model.Cookie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbsRequestFirstHandler implements IRequestFirstHandle{

    private Map<String,List<Cookie>> cookieMap=new HashMap<>();

    @Override
    public String handlerUrl(String requestUrl) {
        return requestUrl;
    }

    @Override
    public Map<String, String> handlerHeader(Map<String, String> header) {
        return header;
    }

    @Override
    public String handlerBody(String body) {
        return body;
    }

    @Override
    public String handlerResponse(String response) {
        return response;
    }

    @Override
    public void cookie(String hosts, List<Cookie> value) {
        cookieMap.put(hosts,value);
    }

    @Override
    public List<Cookie> loadCookie(String hosts) {
        return cookieMap.get(hosts);
    }
}
