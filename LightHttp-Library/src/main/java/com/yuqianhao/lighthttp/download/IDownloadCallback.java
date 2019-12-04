package com.yuqianhao.lighthttp.download;

import java.io.File;

public interface IDownloadCallback {

    void onStart();

    void onError(int core,Exception e);

    void onProgress(int size,int maxSize,double schedule);

    void onComplete(File file);

    void onCancel();

}
