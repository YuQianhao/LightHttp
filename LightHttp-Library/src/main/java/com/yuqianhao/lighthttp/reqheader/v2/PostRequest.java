package com.yuqianhao.lighthttp.reqheader.v2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostRequest {
    String url();
    String charset() default "UTF-8";
}
