package com.yuqianhao.lighthttp.download;

public interface IDownloadAction {

    void setDownloadUrl(String url);

    void setDownloadSaveFile(String path);

    boolean isDownloading();

    void setOnDownloadListener(IDownloadCallback downloadListener);

    void start();

    void cancel();


}
