package com.yuqianhao.lighthttp.model;

import com.yuqianhao.lighthttp.callback.ResponseCallback;

public class Response<_Tx> extends ResponseCallback<_Tx>{

    private _Tx _ReObject=null;

    @Override
    public void onSuccess(_Tx responseData) {
        this._ReObject=responseData;
    }

    @Override
    public void onFailure(int code, String message) {
        mResponseMessage=message;
        mCode=code;
    }

    @Override
    public void onCompany() {
    }

    public _Tx to(){
        return _ReObject;
    }

}
