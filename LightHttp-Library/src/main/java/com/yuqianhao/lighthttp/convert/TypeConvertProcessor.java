package com.yuqianhao.lighthttp.convert;

public interface TypeConvertProcessor<_ConverType> {

    _ConverType convertType(byte[] sourceBuffer);

}
