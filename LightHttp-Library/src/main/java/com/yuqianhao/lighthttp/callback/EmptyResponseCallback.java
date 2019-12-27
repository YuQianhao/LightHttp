package com.yuqianhao.lighthttp.callback;

public class EmptyResponseCallback extends ResponseCallback{

    private static final EmptyResponseCallback EMPTY_RESPONSE_CALLBACK=new EmptyResponseCallback();

    public static final EmptyResponseCallback getInstance(){
        return EMPTY_RESPONSE_CALLBACK;
    }

}
