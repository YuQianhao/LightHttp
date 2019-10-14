package com.yuqianhao.lighthttp.callback;

public class RequestCode {

    /**
     * 本地发生的错误
     * */
    public static final int LOCAL_ERROR=-1;

    /**
     * 请求成功
     * */
    public static final int REQUEST_SUCCESS=200;

    public static boolean checkLocalError(int code){
        return LOCAL_ERROR==code;
    }

}
