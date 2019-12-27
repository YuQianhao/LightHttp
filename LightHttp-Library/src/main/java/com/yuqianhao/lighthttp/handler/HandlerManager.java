package com.yuqianhao.lighthttp.handler;

import java.util.Map;

public class HandlerManager {

    private static IRequestFirstHandle requestFirstHandle;

    public static final void setRequestFirstHandle(IRequestFirstHandle requestFirstHandle){
        HandlerManager.requestFirstHandle=requestFirstHandle;
    }

    public static final IRequestFirstHandle getRequestFirstHandle(){
        return HandlerManager.requestFirstHandle;
    }

}
