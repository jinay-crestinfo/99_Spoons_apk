package com.shj.biz.tools;

import android.util.Log;
import com.bumptech.glide.Glide;
import com.shj.biz.ShjManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class DownloadService {
    private static final String CACHE_FILENAME_PREFIX = "cache_";
    private static final ExecutorService DEFAULT_TASK_EXECUTOR;
    private static final ExecutorService FULL_TASK_EXECUTOR = null;
    public static final int IO_BUFFER_SIZE = 8192;
    private static ExecutorService LIMITED_TASK_EXECUTOR = null;
    private static ExecutorService SINGLE_TASK_EXECUTOR = null;
    private static String TAG = "DownloadService";
    private static Object lock = new Object();
    private String downloadPath;
    private Map<String, PaymentOptionDetail> listURL;
    DownloadStateListener listener;
    private int size = 0;

    /* loaded from: classes2.dex */
    public interface DownloadStateListener {
        void onFailed();

        void onFinish();

        void onitemFinish(PaymentOptionDetail paymentOptionDetail);
    }

    public void setDefaultExecutor() {
    }

    static {
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(1);
        LIMITED_TASK_EXECUTOR = newFixedThreadPool;
        DEFAULT_TASK_EXECUTOR = newFixedThreadPool;
    }

    public DownloadService(String str, Map<String, PaymentOptionDetail> map, DownloadStateListener downloadStateListener) {
        this.downloadPath = str;
        this.listURL = map;
        this.listener = downloadStateListener;
    }

    public void startDownload() {
        File file = new File(this.downloadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        Iterator<String> it = this.listURL.keySet().iterator();
        while (it.hasNext()) {
            try {
                DEFAULT_TASK_EXECUTOR.execute(new Runnable() { // from class: com.shj.biz.tools.DownloadService.1
                    final /* synthetic */ String val$name;

                    AnonymousClass1(String str) {
                        str = str;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        DownloadService downloadService = DownloadService.this;
                        downloadService.downloadBitmap(str, (PaymentOptionDetail) downloadService.listURL.get(str));
                    }
                });
            } catch (RejectedExecutionException e) {
                e.printStackTrace();
                Log.e(TAG, "thread pool rejected error");
                this.listener.onFailed();
            } catch (Exception e2) {
                e2.printStackTrace();
                this.listener.onFailed();
            }
        }
    }

    /* renamed from: com.shj.biz.tools.DownloadService$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ String val$name;

        AnonymousClass1(String str) {
            str = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            DownloadService downloadService = DownloadService.this;
            downloadService.downloadBitmap(str, (PaymentOptionDetail) downloadService.listURL.get(str));
        }
    }

    public void copy(File file, File file2) {
        FileOutputStream fileOutputStream;
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2 = null;
        try {
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    fileOutputStream = new FileOutputStream(file2);
                } catch (Exception e) {
                    e = e;
                    fileOutputStream = null;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = null;
                }
            } catch (Exception e2) {
                e = e2;
                fileOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = null;
            }
            try {
                byte[] bArr = new byte[1024];
                while (fileInputStream.read(bArr) > 0) {
                    fileOutputStream.write(bArr);
                }
                fileInputStream.close();
                fileOutputStream.close();
            } catch (Exception e3) {
                e = e3;
                fileInputStream2 = fileInputStream;
                try {
                    e.printStackTrace();
                    fileInputStream2.close();
                    fileOutputStream.close();
                } catch (Throwable th3) {
                    th = th3;
                    try {
                        fileInputStream2.close();
                        fileOutputStream.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                fileInputStream2 = fileInputStream;
                fileInputStream2.close();
                fileOutputStream.close();
                throw th;
            }
        } catch (IOException e5) {
            e5.printStackTrace();
        }
    }

    public File downloadBitmap(String str, PaymentOptionDetail paymentOptionDetail) {
        try {
            File file = new File(this.downloadPath + str + ".png");
            if (!file.exists()) {
                copy(Glide.with(ShjManager.getActivityContext()).load(paymentOptionDetail.getLogoURL()).downloadOnly(200, 200).get(), file);
            }
            statDownloadNum(paymentOptionDetail);
            return file;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String createFilePath(File file, String str) {
        try {
            return file.getAbsolutePath() + File.separator + CACHE_FILENAME_PREFIX + URLEncoder.encode(str.replace(Marker.ANY_MARKER, ""), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "createFilePath - " + e);
            return null;
        }
    }

    private void statDownloadNum(PaymentOptionDetail paymentOptionDetail) {
        synchronized (lock) {
            this.listener.onitemFinish(paymentOptionDetail);
            int i = this.size + 1;
            this.size = i;
            if (i == this.listURL.size()) {
                Log.d(TAG, "download finished total " + this.size);
                DEFAULT_TASK_EXECUTOR.shutdownNow();
                this.listener.onFinish();
            }
        }
    }
}
