package com.yuqianhao.lighthttp.download;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yuqianhao.lighthttp.okhttp.HttpClientFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadImpl implements IDownloadAction, Handler.Callback {

    /**检查到不是有效的url*/
    public static final int ERRORCODE_CHECKURL=0;

    /**检查到无效的存储路径*/
    public static final int ERRORCODE_CHECKSAVEPATH=1;

    public static final int ERRORCODE_DOWNLOAD=2;

    private static final int MESSAGEWHAT_ERROR=0;

    private static final int MESSAGEWHAT_START=1;

    private static final int MESSAGEWHAT_PROGRESS=2;

    private static final int MESSAGEWHAT_COMPLETE=3;

    private static final int MESSAGEWHAT_CANCEL=4;

    private OkHttpClient okHttpClient;

    private String mDownloadUrl;

    private String mDownloadSavePath;

    private boolean downloading;

    private IDownloadCallback downloadCallback;

    private Handler handler;

    private boolean canRunning;

    public DownloadImpl(){
        this(null,null,null);
    }

    public DownloadImpl(String mDownloadUrl) {
        this(mDownloadUrl,null,null);
    }

    public DownloadImpl(String mDownloadUrl, String mDownloadSavePath) {
        this(mDownloadUrl,mDownloadSavePath,null);
    }

    public DownloadImpl(String mDownloadUrl, String mDownloadSavePath, IDownloadCallback downloadCallback) {
        this.mDownloadUrl = mDownloadUrl;
        this.mDownloadSavePath = mDownloadSavePath;
        this.downloadCallback = downloadCallback;
        this.okHttpClient= HttpClientFactory.getOkHttpClient();
        this.handler=new Handler(Looper.getMainLooper(),this);
        this.canRunning=true;
    }

    @Override
    public IDownloadAction setDownloadUrl(String url) {
        this.mDownloadUrl=url;
        return this;
    }

    @Override
    public IDownloadAction setDownloadSaveFile(String path) {
        this.mDownloadSavePath=path;
        return this;
    }

    @Override
    public boolean isDownloading() {
        return downloading;
    }

    @Override
    public IDownloadAction setOnDownloadListener(IDownloadCallback downloadListener) {
        this.downloadCallback=downloadListener;
        return this;
    }

    @Override
    public IDownloadAction start() {
        if(check()){
            this.canRunning=true;
            Message startMessage=createMessage();
            startMessage.what=MESSAGEWHAT_START;
            sendMessage(startMessage);
            Request request=new Request.Builder().url(mDownloadUrl).get().build();
            HttpClientFactory.getOkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call,IOException e) {
                    Message message=createMessage();
                    message.what=MESSAGEWHAT_ERROR;
                    message.obj=e.getMessage();
                    message.arg1=ERRORCODE_DOWNLOAD;
                    sendMessage(message);
                }

                @Override
                public void onResponse(Call call,Response response) throws IOException {
                    if(response.code()!=200){
                        Message message=createMessage();
                        message.what=MESSAGEWHAT_ERROR;
                        message.arg1=ERRORCODE_DOWNLOAD;
                        String body=response.body().string();
                        message.obj="RequestCode="+response.code()+","+((body==null)?"":body);
                        sendMessage(message);
                    }else{
                        BufferedInputStream bufferedInputStream=new BufferedInputStream(response.body().byteStream());
                        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(mDownloadSavePath));
                        int maxValue= (int) response.body().contentLength();
                        byte[] readDataV0=new byte[4096];
                        int readlength=0;
                        int readTmpLen;
                        while((readTmpLen=bufferedInputStream.read(readDataV0))!=-1 && canRunning) {
                            bufferedOutputStream.write(readDataV0,0,readTmpLen);
                            readlength += readTmpLen;
                            Message msg_update = createMessage();
                            msg_update.what = MESSAGEWHAT_PROGRESS;
                            msg_update.arg1 = readlength;
                            msg_update.arg2 = maxValue;
                            msg_update.obj = (double) readlength / (double) maxValue;
                            sendMessage(msg_update);
                        }
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                        bufferedInputStream.close();
                        Message completeMsg=createMessage();
                        if(canRunning){
                            completeMsg.what=MESSAGEWHAT_COMPLETE;
                            sendMessage(completeMsg);
                        }
                    }
                }
            });
        }
        return this;
    }

    private Message createMessage(){
        return handler.obtainMessage();
    }

    private void sendMessage(Message message){
        handler.sendMessage(message);
    }

    private boolean check(){
        Message message=createMessage();
        if(this.mDownloadUrl==null || this.mDownloadUrl.isEmpty()){
            message.what=MESSAGEWHAT_ERROR;
            message.arg1=ERRORCODE_CHECKURL;
            message.obj="Invalid file download address.";
            sendMessage(message);
            return false;
        }else if(this.mDownloadSavePath==null || this.mDownloadSavePath.isEmpty()){
            message.what=MESSAGEWHAT_ERROR;
            message.arg1=ERRORCODE_CHECKSAVEPATH;
            message.obj="Invalid storage path.";
            sendMessage(message);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void cancel() {
        this.canRunning=false;
        Message message=createMessage();
        message.what=MESSAGEWHAT_CANCEL;
        sendMessage(message);
    }

    @Override
    public boolean handleMessage( Message msg) {
        if(this.downloadCallback==null){return false;}
        switch (msg.what){
            case MESSAGEWHAT_ERROR:{
                this.downloadCallback.onDownloadError(msg.arg1,new DownloadException(msg.obj.toString()));
            }break;
            case MESSAGEWHAT_START:{
                downloading=true;
                this.downloadCallback.onDownloadStart();
            }break;
            case MESSAGEWHAT_PROGRESS:{
                this.downloadCallback.onDownloadProgress(msg.arg1,msg.arg2,((Double)msg.obj).doubleValue());
            }break;
            case MESSAGEWHAT_COMPLETE:{
                this.downloadCallback.onDownloadComplete(new File(this.mDownloadSavePath));
            }break;
            case MESSAGEWHAT_CANCEL:{
                this.downloadCallback.onDownloadCancel();
            }break;
        }
        return true;
    }
}
