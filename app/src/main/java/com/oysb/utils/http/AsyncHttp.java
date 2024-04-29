package com.oysb.utils.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.oysb.utils.Loger;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import cz.msebera.android.httpclient.entity.StringEntity;

/* loaded from: classes2.dex */
public class AsyncHttp {
    public static final int CONNECTION_REFUSED = 258;
    public static final int SOCKET_TIMEOUT = 257;
    public static final int TIME_OUT = 20000;
    private static AsyncHttp instance;
    private WeakReference<Context> context;

    public static AsyncHttp getInstance(Context context) {
        if (instance == null) {
            AsyncHttp asyncHttp = new AsyncHttp();
            instance = asyncHttp;
            asyncHttp.setContext(context);
        }
        return instance;
    }

    private void setContext(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void post(String str, String str2, RequestParams requestParams, int i, AsyncHttpResponseHandler asyncHttpResponseHandler, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("request(");
        sb.append(z ? HttpGet.METHOD_NAME : "POST");
        sb.append("):");
        sb.append(requestParams.toString());
        Loger.writeLog("REQUEST", sb.toString());
        requestParams.setContentEncoding("UTF-8");
        requestParams.setUseJsonStreamer(false);
        try {
            if (z) {
                HttpClientUtils.getInstance(this.context.get(), str, i).post(str2 + "?" + requestParams.toString(), new RequestParams(), asyncHttpResponseHandler);
            } else {
                HttpClientUtils.getInstance(this.context.get(), str, i).post(str2, requestParams, asyncHttpResponseHandler);
            }
        } catch (Exception unused) {
        }
    }

    public void post(String str, String str2, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler, boolean z) {
        post(str, str2, requestParams, HttpClientUtils.TIME_OUT, asyncHttpResponseHandler, z);
    }

    public void post(String baseUrl, String endpoint, JSONObject jsonObject, int timeout, AsyncHttpResponseHandler responseHandler) {
        try {
            if (jsonObject.length() == 0) {
                post(baseUrl, endpoint, timeout, responseHandler);
            } else {
                AsyncHttpClient httpClientUtils = HttpClientUtils.getInstance(this.context.get(), baseUrl, timeout);
                StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
                httpClientUtils.post(this.context.get(), endpoint, entity, baseUrl, responseHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void post(String str, String str2, JSONObject jSONObject, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        post(str, str2, jSONObject, HttpClientUtils.TIME_OUT, asyncHttpResponseHandler);
    }

    public void post(String baseUrl, String endpoint, JSONArray jsonArray, int timeout, AsyncHttpResponseHandler responseHandler) {
        try {
            if (jsonArray.length() == 0) {
                post(baseUrl, endpoint, timeout, responseHandler);
            } else {
                AsyncHttpClient httpClientUtils = HttpClientUtils.getInstance(this.context.get(), baseUrl, timeout);
                StringEntity entity = new StringEntity(jsonArray.toString(), "UTF-8");
                httpClientUtils.post(this.context.get(), endpoint, entity, baseUrl, responseHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void post(String str, String str2, JSONArray jSONArray, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        post(str, str2, jSONArray, HttpClientUtils.TIME_OUT, asyncHttpResponseHandler);
    }

    public void post(String str, String str2, int i, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        HttpClientUtils.getInstance(this.context.get(), str, i).post(str2, asyncHttpResponseHandler);
    }

    public void get(String str, String str2, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        HttpClientUtils.getInstance(this.context.get(), str, HttpClientUtils.TIME_OUT).get(str2, asyncHttpResponseHandler);
    }

    public void get(String str, String str2, RequestParams requestParams, int i, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        Loger.writeLog("REQUEST", "request(get):" + requestParams.toString());
        HttpClientUtils.getInstance(this.context.get(), str, i).get(str2, requestParams, asyncHttpResponseHandler);
    }

    public void get(String str, String str2, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        get(str, str2, requestParams, HttpClientUtils.TIME_OUT, asyncHttpResponseHandler);
    }
}
