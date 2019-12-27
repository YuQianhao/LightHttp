package com.yuqianhao.lighthttp.request;

import com.yuqianhao.lighthttp.callback.EmptyResponseCallback;
import com.yuqianhao.lighthttp.callback.ResponseCallback;
import com.yuqianhao.lighthttp.reqbody.IRequestParameter;
import com.yuqianhao.lighthttp.reqheader.IRequestAddress;

import java.nio.charset.Charset;

import okhttp3.Headers;

public class RequestMessage {

    private String url;

    private int method;

    private String contentType;

    private Charset charset;

    private Headers headers;

    private String data;

    private ResponseCallback responseCallback;

    private Object tag;

    public RequestMessage(String url, int method, String contentType, Charset charset, Headers headers, String data,ResponseCallback responseCallback,Object tag) {
        this.url = url;
        this.method = method;
        this.contentType = contentType;
        this.charset = charset;
        this.headers = headers;
        this.data = data;
        this.responseCallback=responseCallback;
        this.tag=tag;
    }

    public RequestMessage(IRequestAddress address, IRequestParameter parameter, ResponseCallback responseCallback) {
        this(address.url(),
                address.method(),
                parameter.mediaType(),
                parameter.charset(),
                parameter.headers(),
                parameter.data(),
                (responseCallback==null)? EmptyResponseCallback.getInstance():responseCallback,
                address.tag());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ResponseCallback getResponseCallback() {
        return responseCallback;
    }

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
