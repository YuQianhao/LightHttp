package com.yuqianhao.lighthttp.reqbody;

import java.nio.charset.Charset;

import okhttp3.Headers;

public interface IRequestParameter {

    String mediaType();

    Charset charset();

    Headers headers();

    String data();

}
