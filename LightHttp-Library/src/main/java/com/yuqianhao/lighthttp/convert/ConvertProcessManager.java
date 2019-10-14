package com.yuqianhao.lighthttp.convert;

import java.util.HashMap;
import java.util.Map;

public class ConvertProcessManager {

    private static final Map<Class,TypeConvertProcessor> TYPE_CONVERT_PROCESSOR_MAP=new HashMap<>();

    private ConvertProcessManager(){}

    public static void addProcessor(Class clazz,TypeConvertProcessor processor){
        if(!TYPE_CONVERT_PROCESSOR_MAP.containsKey(clazz)){
            TYPE_CONVERT_PROCESSOR_MAP.put(clazz,processor);
        }
    }

    public static TypeConvertProcessor get(Class clazz){
        return TYPE_CONVERT_PROCESSOR_MAP.get(clazz);
    }

}
