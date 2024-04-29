package com.oysb.utils.http;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import java.util.HashMap;
import org.apache.commons.lang3.time.DateUtils;

/* loaded from: classes2.dex */
public class HttpClientUtils {
    public static final int JAVA_SERVER = 0;
    public static final int PHP_SERVER = 1;
    private static String needNewClientContentType = "";
    private static HashMap<String, AsyncHttpClient> clients = new HashMap<>();
    private static HashMap<String, Long> lastRequestTimes = new HashMap<>();
    public static int TIME_OUT = 20000;

    public static void setRequestTimeOut(int i) {
        TIME_OUT = i;
    }

    public static void setNeedNewClientContentType(String str) {
        needNewClientContentType = str;
    }

    public static AsyncHttpClient getInstance(Context context, String str, int i) {
        String str2;
        Long l = 0L;
        String str3 = str + ":" + i;
        boolean endsWith = str.endsWith("_NoEncode");
        if (endsWith) {
            str = str.substring(0, str.length() - 9);
        }
        if (lastRequestTimes.containsKey(str3)) {
            l = lastRequestTimes.get(str3);
        }
        AsyncHttpClient asyncHttpClient = (System.currentTimeMillis() - l.longValue() <= DateUtils.MILLIS_PER_HOUR && ((str2 = needNewClientContentType) == null || str2.length() == 0 || !needNewClientContentType.equalsIgnoreCase(str3)) && clients.containsKey(str3)) ? clients.get(str3) : null;
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient(true, 80, 443);
            PersistentCookieStore persistentCookieStore = new PersistentCookieStore(context);
            asyncHttpClient.setMaxConnections(5);
            asyncHttpClient.setCookieStore(persistentCookieStore);
            asyncHttpClient.setTimeout(i);
            asyncHttpClient.setConnectTimeout(i);
            asyncHttpClient.setResponseTimeout(i);
            asyncHttpClient.addHeader("Connection", "keep-alive");
            asyncHttpClient.addHeader("Content-Type", str);
            asyncHttpClient.addHeader(AsyncHttpClient.HEADER_CONTENT_ENCODING, "UTF-8");
            asyncHttpClient.setURLEncodingEnabled(!endsWith);
            clients.put(str3, asyncHttpClient);
        }
        lastRequestTimes.put(str3, Long.valueOf(System.currentTimeMillis()));
        return asyncHttpClient;
    }

    public static String getContentType(String str) {
        return (str.toLowerCase().contains(".php") || str.toLowerCase().contains("api/1") || str.toLowerCase().contains("api/1a/") || str.toLowerCase().contains("www.xyvend.cn/api") || str.toLowerCase().contains("xyPlat/") || str.toLowerCase().contains("abazhushou") || str.toLowerCase().contains("120.79.140.57:10009") || str.toLowerCase().contains("xynet-web-third-pay/") || str.toLowerCase().contains("www.xyvend.cn/outerController") || str.toLowerCase().contains("service-pay/pay/smile")) ? "application/x-www-form-urlencoded" : "application/json";
    }
}
