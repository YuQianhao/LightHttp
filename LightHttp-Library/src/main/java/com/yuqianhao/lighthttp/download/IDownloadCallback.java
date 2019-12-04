package com.yuqianhao.lighthttp.download;

import java.io.File;

public interface IDownloadCallback {

    void onDownloadStart();

    void onDownloadError(int core,Exception e);

    void onDownloadProgress(int size,int maxSize,double schedule);

    void onDownloadComplete(File file);

    void onDownloadCancel();

}
