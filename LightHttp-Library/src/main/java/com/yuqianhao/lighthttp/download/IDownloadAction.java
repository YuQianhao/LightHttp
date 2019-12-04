package com.yuqianhao.lighthttp.download;

public interface IDownloadAction {

    IDownloadAction setDownloadUrl(String url);

    IDownloadAction setDownloadSaveFile(String path);

    boolean isDownloading();

    IDownloadAction setOnDownloadListener(IDownloadCallback downloadListener);

    IDownloadAction start();

    void cancel();


}
