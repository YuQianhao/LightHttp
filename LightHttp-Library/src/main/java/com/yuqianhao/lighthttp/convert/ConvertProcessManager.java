package com.yuqianhao.lighthttp.convert;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ConvertProcessManager {

    private static final Map<Type,TypeConvertProcessor> TYPE_CONVERT_PROCESSOR_MAP=new HashMap<>();

    private ConvertProcessManager(){}

    public static void addProcessor(Type clazz,TypeConvertProcessor processor){
        if(!TYPE_CONVERT_PROCESSOR_MAP.containsKey(clazz)){
            TYPE_CONVERT_PROCESSOR_MAP.put(clazz,processor);
        }
    }

    public static TypeConvertProcessor get(Type clazz){
        return TYPE_CONVERT_PROCESSOR_MAP.get(clazz);
    }

}
