package com.yuqianhao.lighthttp.callback;

import com.yuqianhao.lighthttp.handler.HandlerManager;
import com.yuqianhao.lighthttp.handler.IRequestFirstHandle;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kotlin.Pair;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Response;

public abstract class ResponseCallback<_RespType> {

    /**
     * 服务器应答的数据原始格式
     * */
    private byte[] mResponceBufferSource;

    /**
     * 服务器返回的消息
     * */
    protected String mResponseMessage;

    /**
     * 服务器响应码
     * */
    protected int mCode;

    /**
     * 服务器返回的数据类型
     * */
    private MediaType mMediaType;

    /**
     * 服务器返回的数据的长度
     * */
    private Long mContentLength;

    private Headers mHeaders;

    public ResponseCallback(){}

    public final void reSet(Response response) throws IOException {
        IRequestFirstHandle requestFirstHandle= HandlerManager.getRequestFirstHandle();
        if(requestFirstHandle!=null){
            String responseData=requestFirstHandle.handlerResponse(new String(response.body().bytes()));
            this.mResponceBufferSource=responseData.getBytes();
        }else{
            this.mResponceBufferSource=response.body().bytes();
        }
        this.mResponseMessage = response.message();
        this.mCode = response.code();
        this.mMediaType = response.body().contentType();
        this.mContentLength=response.body().contentLength();
        this.mHeaders=response.headers();
    }

    /**
     * 获取服务器应答的Headers
     * */
    public final Map<String,String> getHeaders(){
        Map<String,String> headerMap=new HashMap<>();
        if(mHeaders!=null){
            Iterator<Pair<String,String>> iter=mHeaders.iterator();
            while(iter.hasNext()){
                Pair<String,String> value=iter.next();
                headerMap.put(value.getFirst(),value.getSecond());
            }
        }
        return headerMap;
    }

    /**
     * 获取服务器返回数据的长度
     * */
    public final Long getContentLength(){
        return mContentLength;
    }

    /**
     * 获取服务器返回的数据的原始数据
     * */
    public final byte[] getResponseBuffer(){
        return mResponceBufferSource;
    }

    /**
     * 将服务器返回的数据转换为字符串并使用默认编码进行返回
     * */
    public final String getResponseBufferString(){
        return new String(mResponceBufferSource);
    }
    /**
     * 根据指定的编码将服务器返回的数据转换为字符串
     * */
    public final String getResponseBufferString(Charset charset){
        return new String(mResponceBufferSource,charset);
    }

    /**
     * 获取http状态消息
     * */
    public final String getMessage(){
        return mResponseMessage;
    }

    /**
     * 获取http状态码
     * */
    public final int getCode(){
        return mCode;
    }

    /**
     * 获取服务器应答数据的格式
     * */
    public final com.yuqianhao.lighthttp.model.MediaType getMediaType(){
        if(mMediaType!=null){
            com.yuqianhao.lighthttp.model.MediaType result=new com.yuqianhao.lighthttp.model.MediaType();
            result.setCharset(mMediaType.charset());
            result.setType(mMediaType.type());
            result.setSubType(mMediaType.subtype());
            return result;
        }
        return null;
    }

    /**
     * 当服务器应答的状态码不为200的时候，即请求出错，
     * 将会回调这个方法，并将状态码和状态信息传给这个方法。
     * @param code          服务器应答码
     * @param message       服务器应答信息
     * */
    public void onFailure(int code,String message){}

    /**
     * 当服务器的应答状态码为200时，视为请求并处理成功，
     * 将回调这个方法，并会根据类的泛型参数将数据转换为
     * 指定类型的对象，如果这个泛型参数为{@link Nullptr}
     * ，则不会进行转换，并且这个参数会传为null。
     * */
    public void onSuccess(_RespType responseData){}

    /**
     * 无论成功或者失败，等{@link #onFailure(int, String)}或者{@link #onSuccess(Object)}
     * 处理完成后都会回调这个方法。
     * */
    public void onCompany(){}

}
