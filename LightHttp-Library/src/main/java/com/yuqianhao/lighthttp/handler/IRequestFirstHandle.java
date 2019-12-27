package com.yuqianhao.lighthttp.handler;

import java.util.Map;

public interface IRequestFirstHandle {

    String handlerUrl(String requestUrl);

    Map<String,String> handlerHeader(Map<String,String> header);

    String handlerBody(String body);

    String handlerResponse(String response);

}
