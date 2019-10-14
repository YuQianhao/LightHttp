package com.yuqianhao.lighthttp.reqheader;

public interface IRequestAddress {

    /**
     * 请求地址
     * */
    String url();

    /**
     * 请求方法，详见{@link MethodType}
     * */
    int method();

    Object tag();

}
