package com.yuqianhao.lighthttp.handler;

import java.util.List;
import java.util.Map;

public interface IRequestFirstHandle {

    String handlerUrl(String requestUrl);

    Map<String,String> handlerHeader(Map<String,String> header);

    String handlerBody(String body);

    String handlerResponse(String response);

    void cookie(String hosts, List<String> value);

    List<String> loadCookie(String hosts);

}
