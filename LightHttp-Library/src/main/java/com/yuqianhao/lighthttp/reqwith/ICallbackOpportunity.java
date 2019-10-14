package com.yuqianhao.lighthttp.reqwith;

import android.app.Activity;

/**
 * 当网络请求应答接收到时，用来判断回调{@link com.yuqianhao.lighthttp.callback.ResponseCallback}的时机
 *，实现这个接口并重写{@link #callback()}这个方法用来返回是否回调，例如当Activity被调用{@link Activity#onDestroy()}
 * 的时候，这个方法应该返回为false，意味着请求结果不在执行。
 * */
public interface ICallbackOpportunity {

    /**
     * 是否进行Callback
     * */
    boolean callback();

}
