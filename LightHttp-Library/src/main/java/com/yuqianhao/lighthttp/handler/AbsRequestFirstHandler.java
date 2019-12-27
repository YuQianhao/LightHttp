package com.yuqianhao.lighthttp.handler;

import java.util.Map;

public class AbsRequestFirstHandler implements IRequestFirstHandle{
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
}
