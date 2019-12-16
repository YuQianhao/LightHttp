package com.yuqianhao.lighthttp.model;

import java.nio.charset.Charset;

public class MediaType {

    private String type;

    private String subType;

    private Charset charset;

    public MediaType() {
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
