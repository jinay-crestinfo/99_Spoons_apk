package com.oysb.utils;

import android.content.Context;
//import com.downloader.Error;
//import com.downloader.OnDownloadListener;
//import com.downloader.OnProgressListener;
//import com.downloader.PRDownloader;
//import com.downloader.PRDownloaderConfig;
//import com.downloader.Progress;
import com.github.mjdev.libaums.fs.UsbFile;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes2.dex */
public class PRDownloaderTool {
    private static DownloadData currentDownloadData;
    private static Queue<DownloadData> queue = new LinkedList();
    private static long progressTime = 0;

    /* loaded from: classes2.dex */
    public static class DownloadData {
        public String dirPath;
        public String fileName;
        public OnDownloadConditionListener onDownloadConditionListener;
        public OnProgressConditionListener onProgressConditionListener;
        public String url;
    }

    /* loaded from: classes2.dex */
    public interface OnDownloadConditionListener {
        void downloadFileExists();

        void onDownloadComplete(String str);

        void onError(Error error);
    }

    /* loaded from: classes2.dex */
    public interface OnProgressConditionListener {
//        void onProgress(Progress progress);
    }

    public static void init(Context context) {
//        PRDownloader.initialize(context, PRDownloaderConfig.newBuilder().setDatabaseEnabled(true).setReadTimeout(30000).setConnectTimeout(30000).build());
    }

    public static void addSerialDownloadTask(String str, String str2, String str3, OnProgressConditionListener onProgressConditionListener, OnDownloadConditionListener onDownloadConditionListener) {
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (new File(str2 + UsbFile.separator + str3).exists()) {
            Loger.writeLog("SHJ", "下载文件：文件已存在" + str3);
            if (onDownloadConditionListener != null) {
                onDownloadConditionListener.downloadFileExists();
                return;
            }
            return;
        }
        DownloadData downloadData = new DownloadData();
        downloadData.url = str;
        downloadData.dirPath = str2;
        downloadData.fileName = str3;
        downloadData.onProgressConditionListener = onProgressConditionListener;
        downloadData.onDownloadConditionListener = onDownloadConditionListener;
        queue.offer(downloadData);
        startDownload();
    }

    public static int addImmediatelyDownloadTask(String str, String str2, String str3, OnProgressConditionListener onProgressConditionListener, OnDownloadConditionListener onDownloadConditionListener) {
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (new File(str2 + UsbFile.separator + str3).exists()) {
            Loger.writeLog("SHJ", "下载文件：文件已存在" + str3);
            if (onDownloadConditionListener == null) {
                return -1;
            }
            onDownloadConditionListener.downloadFileExists();
            return -1;
        }
        Loger.writeLog("SHJ", "下载文件：url=" + str + "dirPath=" + str2 + "fileName=" + str3);
        return PRDownloader.download(str, str2, str3).build().setOnProgressListener(new OnProgressListener() { // from class: com.oysb.utils.PRDownloaderTool.2


            @Override // com.downloader.OnProgressListener
            public void onProgress(Progress progress) {
                OnProgressConditionListener onProgressConditionListener2 = OnProgressConditionListener.this;
                if (onProgressConditionListener2 != null) {
                    onProgressConditionListener2.onProgress(progress);
                }
                if (System.currentTimeMillis() - PRDownloaderTool.progressTime > 1000) {
                    Loger.writeLog("SHJ", "下载进度：" + ((progress.currentBytes * 100) / progress.totalBytes));
                    Loger.writeLog("SHJ", "下载进度：" + progress.currentBytes + UsbFile.separator + progress.totalBytes);
                    long unused = PRDownloaderTool.progressTime = System.currentTimeMillis();
                }
            }
        }).start(new OnDownloadListener() { // from class: com.oysb.utils.PRDownloaderTool.1

            @Override // com.downloader.OnDownloadListener
            public void onDownloadComplete() {
                Loger.writeLog("SHJ", "下载完成：fileName=" + str3);
                OnDownloadConditionListener onDownloadConditionListener2 = onDownloadConditionListener;
                if (onDownloadConditionListener2 != null) {
                    onDownloadConditionListener2.onDownloadComplete(str2 + UsbFile.separator + str3);
                }
            }

            @Override // com.downloader.OnDownloadListener
            public void onError(Error error) {
                Loger.writeLog("SHJ", "下载出错：fileName=" + str3 + "serverError:" + error.isServerError() + "connectionError:" + error.isConnectionError());
                OnDownloadConditionListener onDownloadConditionListener2 = onDownloadConditionListener;
                if (onDownloadConditionListener2 != null) {
                    onDownloadConditionListener2.onError(error);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.PRDownloaderTool$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnProgressListener {
        AnonymousClass2() {
        }

        @Override // com.downloader.OnProgressListener
        public void onProgress(Progress progress) {
            OnProgressConditionListener onProgressConditionListener2 = OnProgressConditionListener.this;
            if (onProgressConditionListener2 != null) {
                onProgressConditionListener2.onProgress(progress);
            }
            if (System.currentTimeMillis() - PRDownloaderTool.progressTime > 1000) {
                Loger.writeLog("SHJ", "下载进度：" + ((progress.currentBytes * 100) / progress.totalBytes));
                Loger.writeLog("SHJ", "下载进度：" + progress.currentBytes + UsbFile.separator + progress.totalBytes);
                long unused = PRDownloaderTool.progressTime = System.currentTimeMillis();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.PRDownloaderTool$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnDownloadListener {
        final /* synthetic */ String val$dirPath;
        final /* synthetic */ String val$fileName;
        final /* synthetic */ OnDownloadConditionListener val$onDownloadConditionListener;

        AnonymousClass1(String str32, OnDownloadConditionListener onDownloadConditionListener2, String str22) {
            str3 = str32;
            onDownloadConditionListener = onDownloadConditionListener2;
            str2 = str22;
        }

        @Override // com.downloader.OnDownloadListener
        public void onDownloadComplete() {
            Loger.writeLog("SHJ", "下载完成：fileName=" + str3);
            OnDownloadConditionListener onDownloadConditionListener2 = onDownloadConditionListener;
            if (onDownloadConditionListener2 != null) {
                onDownloadConditionListener2.onDownloadComplete(str2 + UsbFile.separator + str3);
            }
        }

        @Override // com.downloader.OnDownloadListener
        public void onError(Error error) {
            Loger.writeLog("SHJ", "下载出错：fileName=" + str3 + "serverError:" + error.isServerError() + "connectionError:" + error.isConnectionError());
            OnDownloadConditionListener onDownloadConditionListener2 = onDownloadConditionListener;
            if (onDownloadConditionListener2 != null) {
                onDownloadConditionListener2.onError(error);
            }
        }
    }

    public static void pauseDownload(int i) {
        PRDownloader.pause(i);
    }

    public static void resumeDownload(int i) {
        PRDownloader.resume(i);
    }

    public static void startDownload() {
        if (currentDownloadData != null || queue.isEmpty()) {
            return;
        }
        DownloadData poll = queue.poll();
        currentDownloadData = poll;
        if (poll != null) {
            Loger.writeLog("SHJ", "下载文件：url=" + currentDownloadData.url + "dirPath=" + currentDownloadData.dirPath + "fileName=" + currentDownloadData.fileName);
            PRDownloader.download(currentDownloadData.url, currentDownloadData.dirPath, currentDownloadData.fileName).build().setOnProgressListener(new OnProgressListener() { // from class: com.oysb.utils.PRDownloaderTool.4
                AnonymousClass4() {
                }

                @Override // com.downloader.OnProgressListener
                public void onProgress(Progress progress) {
                    if (PRDownloaderTool.currentDownloadData.onProgressConditionListener != null) {
                        PRDownloaderTool.currentDownloadData.onProgressConditionListener.onProgress(progress);
                    }
                    if (System.currentTimeMillis() - PRDownloaderTool.progressTime > 1000) {
                        Loger.writeLog("SHJ", "下载进度：" + ((progress.currentBytes * 100) / progress.totalBytes));
                        Loger.writeLog("SHJ", "下载进度：" + progress.currentBytes + UsbFile.separator + progress.totalBytes);
                        long unused = PRDownloaderTool.progressTime = System.currentTimeMillis();
                    }
                }
            }).start(new OnDownloadListener() { // from class: com.oysb.utils.PRDownloaderTool.3
                AnonymousClass3() {
                }

                @Override // com.downloader.OnDownloadListener
                public void onDownloadComplete() {
                    Loger.writeLog("SHJ", "下载完成：fileName=" + PRDownloaderTool.currentDownloadData.fileName);
                    if (PRDownloaderTool.currentDownloadData.onDownloadConditionListener != null) {
                        PRDownloaderTool.currentDownloadData.onDownloadConditionListener.onDownloadComplete(PRDownloaderTool.currentDownloadData.dirPath + UsbFile.separator + PRDownloaderTool.currentDownloadData.fileName);
                    }
                    DownloadData unused = PRDownloaderTool.currentDownloadData = null;
                    PRDownloaderTool.startDownload();
                }

                @Override // com.downloader.OnDownloadListener
                public void onError(Error error) {
                    Loger.writeLog("SHJ", "下载出错：fileName=" + PRDownloaderTool.currentDownloadData.fileName + "serverError:" + error.isServerError() + "connectionError:" + error.isConnectionError());
                    if (PRDownloaderTool.currentDownloadData.onDownloadConditionListener != null) {
                        PRDownloaderTool.currentDownloadData.onDownloadConditionListener.onError(error);
                    }
                    DownloadData unused = PRDownloaderTool.currentDownloadData = null;
                    PRDownloaderTool.startDownload();
                }
            });
        }
    }

    /* renamed from: com.oysb.utils.PRDownloaderTool$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements OnProgressListener {
        AnonymousClass4() {
        }

        @Override // com.downloader.OnProgressListener
        public void onProgress(Progress progress) {
            if (PRDownloaderTool.currentDownloadData.onProgressConditionListener != null) {
                PRDownloaderTool.currentDownloadData.onProgressConditionListener.onProgress(progress);
            }
            if (System.currentTimeMillis() - PRDownloaderTool.progressTime > 1000) {
                Loger.writeLog("SHJ", "下载进度：" + ((progress.currentBytes * 100) / progress.totalBytes));
                Loger.writeLog("SHJ", "下载进度：" + progress.currentBytes + UsbFile.separator + progress.totalBytes);
                long unused = PRDownloaderTool.progressTime = System.currentTimeMillis();
            }
        }
    }

    /* renamed from: com.oysb.utils.PRDownloaderTool$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnDownloadListener {
        AnonymousClass3() {
        }

        @Override // com.downloader.OnDownloadListener
        public void onDownloadComplete() {
            Loger.writeLog("SHJ", "下载完成：fileName=" + PRDownloaderTool.currentDownloadData.fileName);
            if (PRDownloaderTool.currentDownloadData.onDownloadConditionListener != null) {
                PRDownloaderTool.currentDownloadData.onDownloadConditionListener.onDownloadComplete(PRDownloaderTool.currentDownloadData.dirPath + UsbFile.separator + PRDownloaderTool.currentDownloadData.fileName);
            }
            DownloadData unused = PRDownloaderTool.currentDownloadData = null;
            PRDownloaderTool.startDownload();
        }

        @Override // com.downloader.OnDownloadListener
        public void onError(Error error) {
            Loger.writeLog("SHJ", "下载出错：fileName=" + PRDownloaderTool.currentDownloadData.fileName + "serverError:" + error.isServerError() + "connectionError:" + error.isConnectionError());
            if (PRDownloaderTool.currentDownloadData.onDownloadConditionListener != null) {
                PRDownloaderTool.currentDownloadData.onDownloadConditionListener.onError(error);
            }
            DownloadData unused = PRDownloaderTool.currentDownloadData = null;
            PRDownloaderTool.startDownload();
        }
    }
}
