package com.oysb.utils.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
//import com.alipay.api.AlipayConstants;
//import com.google.android.exoplayer.hls.HlsChunkSource;
import com.loopj.android.http.HttpGet;
import com.oysb.utils.Loger;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.http.RequestItem;
//import com.tencent.wxpayface.WxfacePayCommonCode;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class RequestHelper {
    static final int REQUEST_ONCEMORE = 1000;
    static final int REQUEST_REOMOVE = 2000;
    static final int REQUEST_TEST = 3000;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static List<RequestItem> requestItems = new ArrayList();
    static RequestRunnable requestRunnable = null;
    static RequestQueueRunnable requestQueueRunnable = null;
    static int noRepeatRequestCount = 0;
    static OkHttpClient http = null;
    static long lastLogNetErrorTime = 0;
    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int what = message.what;
            if (what == 1000) {
                // Handle individual request
                RequestItem requestItem = (RequestItem) message.obj;
                RequestHelper.request(requestItem, requestItem.getResponseHandler());
            } else if (what == 2000) {
                // Handle batch request
                ArrayList<RequestItem> requestItems = (ArrayList<RequestItem>) message.obj;
                for (RequestItem requestItem : requestItems) {
                    requestItem.onRequestFinished(requestItem, false);
                }
            }
            // Ignore other message types (e.g., 3000)
        }
    };

    static WeakReference<Context> wkContext = null;
    static HashMap<String, Queue<RequestItem>> mapQueues = new HashMap<>();
    static long lastFailureTime = 0;

    public static void deleteRequests(int i) {
    }

    public static void setDebugModel(int i) {
    }

    public static void testBaidu(String str) {
    }

    static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (Exception unused) {
        }
    }

    public static void stop() {
        requestRunnable.stop();
        requestQueueRunnable.stop();
    }

    /* loaded from: classes2.dex */
    public static class RequestRunnable implements Runnable {
        boolean run;
        ArrayList<RequestItem> toDelRequest;

        private RequestRunnable() {
            this.run = true;
            this.toDelRequest = null;
        }

        /* synthetic */ RequestRunnable(AnonymousClass1 anonymousClass1) {
            this();
        }

        public void stop() {
            this.run = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            long j;
            while (this.run) {
                try {
                    synchronized (RequestHelper.requestItems) {
                        if (RequestHelper.requestItems.size() == 0) {
                            RequestHelper.requestItems.wait();
                        }
                    }
                    if (this.toDelRequest == null) {
                        this.toDelRequest = new ArrayList<>();
                    }
                    this.toDelRequest.clear();
                    long currentTimeMillis = System.currentTimeMillis();
                    synchronized (RequestHelper.requestItems) {
                        Iterator<RequestItem> it = RequestHelper.requestItems.iterator();
                        while (true) {
                            j = 10;
                            if (!it.hasNext()) {
                                break;
                            }
                            RequestItem next = it.next();
                            if (!next.isRequesting() && !next.isPaused()) {
                                if (!next.needRequest()) {
                                    this.toDelRequest.add(next);
                                    if (next.getRepeat() == 1) {
                                        RequestHelper.noRepeatRequestCount--;
                                    }
                                } else {
                                    next.getLastRequestTime();
                                    if (currentTimeMillis - next.getLastRequestTime() > next.getRepeatDelay()) {
                                        next.setRequesting(true);
                                        Message obtain = Message.obtain();
                                        obtain.what = 1000;
                                        obtain.obj = next;
                                        RequestHelper.handler.sendMessage(obtain);
                                    }
                                    try {
                                        Thread.sleep(10L);
                                    } catch (Exception unused) {
                                    }
                                }
                            }
                        }
                        RequestHelper.requestItems.removeAll(this.toDelRequest);
                    }
                    Message obtain2 = Message.obtain();
                    obtain2.what = 2000;
                    obtain2.obj = this.toDelRequest;
                    RequestHelper.handler.sendMessage(obtain2);
                    if (RequestHelper.noRepeatRequestCount <= 0) {
                        j = 1000;
                    }
                    Thread.sleep(j);
                } catch (Exception e) {
                    Loger.writeException("WxFacePayException", e);
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class RequestQueueRunnable implements Runnable {
        boolean run;

        private RequestQueueRunnable() {
            this.run = true;
        }

        /* synthetic */ RequestQueueRunnable(AnonymousClass1 anonymousClass1) {
            this();
        }

        public void stop() {
            this.run = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            while (this.run) {
                try {
                    if (RequestHelper.mapQueues.size() == 0) {
                        RequestHelper.sleep(1000);
                    }
                    ArrayList arrayList = new ArrayList();
                    for (String str : RequestHelper.mapQueues.keySet()) {
                        Queue<RequestItem> queue = RequestHelper.mapQueues.get(str);
                        if (queue.size() == 0) {
                            arrayList.add(str);
                        } else {
                            RequestItem peek = queue.peek();
                            if (peek != null && !peek.isRequesting()) {
                                if (!peek.isFinished() && peek.needRequest()) {
                                    long currentTimeMillis = System.currentTimeMillis();
                                    peek.getLastRequestTime();
                                    if (!RequestHelper.requestItems.contains(peek) && currentTimeMillis - peek.getLastRequestTime() > peek.getRepeatDelay()) {
                                        RequestHelper.request(peek);
                                    }
                                    RequestHelper.sleep(10);
                                }
                                queue.poll();
                            }
                        }
                    }
                    RequestHelper.sleep(50);
                } catch (Exception e) {
                    Loger.writeException("WxFacePayException", e);
                }
            }
        }
    }

    /* renamed from: com.oysb.utils.http.RequestHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 1000) {
                RequestItem requestItem = (RequestItem) message.obj;
                RequestHelper.request(requestItem, requestItem.getResponseHandler());
            } else if (i != 2000) {
                if (i != 3000) {
                    return;
                }
            } else {
                Iterator it = ((ArrayList) message.obj).iterator();
                while (it.hasNext()) {
                    RequestItem requestItem2 = (RequestItem) it.next();
                    requestItem2.onRequestFinished(requestItem2, false);
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class SSLSocketClient {
        public static SSLSocketFactory getSSLSocketFactory() {
            try {
                SSLContext sSLContext = SSLContext.getInstance("SSL");
                sSLContext.init(null, getTrustManager(), new SecureRandom());
                return sSLContext.getSocketFactory();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /* renamed from: com.oysb.utils.http.RequestHelper$SSLSocketClient$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements X509TrustManager {
            @Override // javax.net.ssl.X509TrustManager
            public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
            }

            @Override // javax.net.ssl.X509TrustManager
            public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
            }

            @Override // javax.net.ssl.X509TrustManager
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            AnonymousClass1() {
            }
        }

        private static TrustManager[] getTrustManager() {
            return new TrustManager[]{new X509TrustManager() { // from class: com.oysb.utils.http.RequestHelper.SSLSocketClient.1
                @Override // javax.net.ssl.X509TrustManager
                public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
                }

                @Override // javax.net.ssl.X509TrustManager
                public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
                }

                @Override // javax.net.ssl.X509TrustManager
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

            }};
        }

        /* renamed from: com.oysb.utils.http.RequestHelper$SSLSocketClient$2 */
        /* loaded from: classes2.dex */
        public class AnonymousClass2 implements HostnameVerifier {
            @Override // javax.net.ssl.HostnameVerifier
            public boolean verify(String str, SSLSession sSLSession) {
                return true;
            }

        }

        public static HostnameVerifier getHostnameVerifier() {
            return new HostnameVerifier() { // from class: com.oysb.utils.http.RequestHelper.SSLSocketClient.2
                @Override // javax.net.ssl.HostnameVerifier
                public boolean verify(String str, SSLSession sSLSession) {
                    return true;
                }

            };
        }
    }

    public static void init(Context context) {
        wkContext = new WeakReference<>(context);
        http = new OkHttpClient.Builder().retryOnConnectionFailure(true).sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).connectTimeout(10000, TimeUnit.MILLISECONDS).readTimeout(10000, TimeUnit.MILLISECONDS).writeTimeout(10000, TimeUnit.MILLISECONDS).build();
        requestRunnable = new RequestRunnable();
        new Thread(requestRunnable).start();
        requestQueueRunnable = new RequestQueueRunnable();
        new Thread(requestQueueRunnable).start();
    }

    public static void request(RequestItem requestItem) {
        boolean z = true;
        if (requestItem.getRequestMaxCount() == 1 && !requestItem.isUnSuccessRequest()) {
            Message obtain = Message.obtain();
            obtain.what = 1000;
            obtain.obj = requestItem;
            handler.sendMessage(obtain);
            return;
        }
        synchronized (requestItems) {
            if (requestItem.getRequestMaxCount() == 1) {
                noRepeatRequestCount++;
                Loger.writeLog("REQUEST", "noRepeatRequestCount:" + noRepeatRequestCount);
            }
            Iterator<RequestItem> it = requestItems.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                } else if (it.next().getRequestSN() == requestItem.getRequestSN()) {
                    Loger.writeLog("REQUEST", "请求:" + requestItem.getUrl() + "已存在");
                    break;
                }
            }
            if (!z) {
                requestItems.add(requestItem);
            }
            requestItems.notifyAll();
        }
    }

    public static void requestInQueue(RequestItem requestItem, String str) {
        if (!mapQueues.containsKey(str)) {
            mapQueues.put(str, new LinkedList());
        }
        mapQueues.get(str).offer(requestItem);
    }

    public static boolean blockRequest(RequestItem requestItem) {
        synchronized (requestItem) {
            requestItem.setBlockModel(true);
            Message obtain = Message.obtain();
            obtain.what = 1000;
            obtain.obj = requestItem;
            handler.sendMessage(obtain);
            try {
                requestItem.wait(HlsChunkSource.DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS);
            } catch (Exception unused) {
                requestItem.notifyAll();
            }
        }
        return requestItem.isSuccess();
    }

    public static void request(RequestItem requestItem, RequestItem.ResponseHandler responseHandler) {
        try {
            if (requestItem.getTestResult().length() > 0) {
                requestItem.setRequesting(true);
                requestItem.addRepeatCount();
                Message obtain = Message.obtain();
                obtain.what = 3000;
                obtain.obj = requestItem;
                handler.sendMessageDelayed(obtain, 500L);
                return;
            }
            requestItem.setRequesting(true);
            requestItem.addRepeatCount();
            RequestBody requestBody = null;
            if (!requestItem.getMothed().equalsIgnoreCase(HttpGet.METHOD_NAME)) {
                requestBody = RequestBody.create((requestItem.isArrayParams ? requestItem.getArrayParams() : requestItem.getParams()).toString().getBytes(), JSON);
            } else if (requestItem.getParams() != null) {
                JSONObject params = requestItem.getParams();
                JSONArray names = params.names();
                if (names.length() > 0) {
                    String str = "?";
                    for (int i = 0; i < names.length(); i++) {
                        String string = names.getString(i);
                        str = str + string + "=" + params.getString(string) + "&";
                    }
                    requestItem.setUrl(requestItem.getUrl() + str.substring(0, str.length() - 1));
                }
            }
            Request build = new Request.Builder().url(requestItem.getUrl()).method(requestItem.getMothed(), requestBody).build();
            requestItem.setQueryStartTime(System.currentTimeMillis());
            http.newCall(build).enqueue(responseHandler);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("WxFacePayException", e);
        }
    }

    public static void cancelRequestItem(String str) {
        for (RequestItem requestItem : requestItems) {
            if (requestItem.getKey() != null && requestItem.getKey().equalsIgnoreCase(str)) {
                requestItem.setCanceled(true);
            }
        }
    }

    public static boolean isNewRequestResult(JSONObject jSONObject, String str) {
        try {
            return CacheHelper.getFileCache().getAsJSONObject(str).toString().hashCode() != jSONObject.toString().hashCode();
        } catch (Exception unused) {
            return true;
        }
    }

    public static void clearVersionedRequestResult(String str) {
        try {
            CacheHelper.getFileCache().remove(str);
        } catch (Exception unused) {
        }
    }

    public static JSONObject updateVersionedRequestResult(JSONObject jSONObject, String str) {
        JSONObject jSONObject2 = null;
        try {
            JSONObject asJSONObject = CacheHelper.getFileCache().getAsJSONObject(str);
            if (jSONObject.has(AlipayConstants.VERSION)) {
                String string = jSONObject.getString(AlipayConstants.VERSION);
                if (asJSONObject != null && (!asJSONObject.has(AlipayConstants.VERSION) || asJSONObject.get(AlipayConstants.VERSION).equals(string))) {
                    jSONObject2 = asJSONObject;
                }
                if (asJSONObject != null && asJSONObject.has(AlipayConstants.VERSION)) {
                    if (asJSONObject.get(AlipayConstants.VERSION).equals(string)) {
                        jSONObject2 = asJSONObject;
                    }
                }
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
        if (jSONObject2 != null) {
            return jSONObject2;
        }
        CacheHelper.getFileCache().put(str, jSONObject);
        return jSONObject;
    }

    /* renamed from: com.oysb.utils.http.RequestHelper$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements Callback {
        final /* synthetic */ String val$logTag;

        AnonymousClass2(String str) {
            this.val$logTag = str;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            Loger.writeLog(this.val$logTag, "耗时:" + (System.currentTimeMillis() - RequestHelper.lastFailureTime) + " 百度连接测试结果:" + iOException.getMessage());
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, Response response) throws IOException {
            Loger.writeLog(this.val$logTag, "耗时:" + (System.currentTimeMillis() - RequestHelper.lastFailureTime) + " 百度连接测试结果:OK");
        }
    }

    public static List<RequestItem> unSuccessRequests(String str, Date date) {
        return new ArrayList();
    }
}
