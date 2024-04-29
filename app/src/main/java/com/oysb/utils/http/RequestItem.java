package com.oysb.utils.http;

import com.loopj.android.http.RequestParams;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.date.DateUtil;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class RequestItem implements Serializable {
    private static final long serialVersionUID = -5578603567833947735L;
    private String applicationType;
    private JSONArray arrayParams;
    private boolean blockModel;
    private String createTime;
    Date finishTime;
    private boolean finished;
    private int id;
    boolean isArrayParams;
    private boolean isCanceled;
    private boolean isRequesting;
    private boolean isUnSuccessRequest;
    private String key;
    private long lastRequestTime;
    private int maxRepeatCount;
    private String mothed;
    private transient Object obj;
    private transient OnRequestResultListener onRequestResultListener;
    private JSONObject params;
    private boolean paused;
    boolean postParamsAsGet;
    Date pushTime;
    private long queryStartTime;
    private int repeatCount;
    private int repeatDelay;
    private transient ResponseHandler responseHandler;
    private String retCode;
    private int state;
    private boolean success;
    private String successStr;
    private transient String testResult;
    private int timeOut;
    private String url;

    /* loaded from: classes2.dex */
    public interface OnRequestListener {
        boolean shouldRequest();
    }

    /* loaded from: classes2.dex */
    public interface OnRequestResultListener {
        void onFailure(RequestItem requestItem, int i, String str, Throwable th);

        void onRequestFinished(RequestItem requestItem, boolean z);

        boolean onSuccess(RequestItem requestItem, int i, String str);
    }

    public boolean isJSONParams() {
        return true;
    }

    public void setLostAble(boolean z) {
    }

    public String getApplicationType() {
        return this.applicationType;
    }

    public void setApplicationType(String str) {
        this.applicationType = str;
    }

    public int getRequestSN() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.url);
        sb.append(this.isArrayParams ? getArrayParams() : getParams());
        return sb.toString().hashCode();
    }

    public void setQueryStartTime(long j) {
        this.queryStartTime = j;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj() {
        return this.obj;
    }

    public boolean isBlockModel() {
        return this.blockModel;
    }

    public void setBlockModel(boolean z) {
        this.blockModel = z;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean z) {
        this.success = z;
    }

    public String getTestResult() {
        if (this.testResult == null) {
            this.testResult = "";
        }
        return this.testResult;
    }

    public void setTestResult(String str) {
        this.testResult = str;
    }

    public RequestItem(String str, String str2) {
        this.applicationType = "application/json";
        this.mothed = "POST";
        this.timeOut = 20000;
        this.repeatCount = 0;
        this.maxRepeatCount = 1;
        this.repeatDelay = 20000;
        this.isArrayParams = false;
        this.isRequesting = false;
        this.finished = false;
        this.success = false;
        this.postParamsAsGet = false;
        this.lastRequestTime = 0L;
        this.queryStartTime = 0L;
        this.blockModel = false;
        this.obj = null;
        this.url = str;
        this.params = new JSONObject();
        this.mothed = str2;
        this.postParamsAsGet = false;
        this.createTime = DateUtil.format(new Date(), "HH:mm:ss:SSS");
    }

    public RequestItem(String str, JSONObject jSONObject, String str2, boolean z) {
        this.applicationType = "application/json";
        this.timeOut = 20000;
        this.repeatCount = 0;
        this.maxRepeatCount = 1;
        this.repeatDelay = 20000;
        this.isArrayParams = false;
        this.isRequesting = false;
        this.finished = false;
        this.success = false;
        this.lastRequestTime = 0L;
        this.queryStartTime = 0L;
        this.blockModel = false;
        this.obj = null;
        this.url = str;
        this.params = jSONObject;
        this.mothed = str2;
        this.postParamsAsGet = z;
        this.createTime = DateUtil.format(new Date(), "HH:mm:ss:SSS");
    }

    public RequestItem(String str, JSONObject jSONObject, String str2) {
        this.applicationType = "application/json";
        this.timeOut = 20000;
        this.repeatCount = 0;
        this.maxRepeatCount = 1;
        this.repeatDelay = 20000;
        this.isArrayParams = false;
        this.isRequesting = false;
        this.finished = false;
        this.success = false;
        this.postParamsAsGet = false;
        this.lastRequestTime = 0L;
        this.queryStartTime = 0L;
        this.blockModel = false;
        this.obj = null;
        this.url = str;
        this.params = jSONObject;
        this.mothed = str2;
        this.createTime = DateUtil.format(new Date(), "HH:mm:ss:SSS");
    }

    public RequestItem(String str, JSONArray jSONArray, String str2) {
        this.applicationType = "application/json";
        this.timeOut = 20000;
        this.repeatCount = 0;
        this.maxRepeatCount = 1;
        this.repeatDelay = 20000;
        this.isRequesting = false;
        this.finished = false;
        this.success = false;
        this.postParamsAsGet = false;
        this.lastRequestTime = 0L;
        this.queryStartTime = 0L;
        this.blockModel = false;
        this.obj = null;
        this.url = str;
        this.arrayParams = jSONArray;
        this.mothed = str2;
        this.isArrayParams = true;
        this.createTime = DateUtil.format(new Date(), "HH:mm:ss:SSS");
    }

    public JSONObject getJSONParams() {
        return this.params;
    }

    public RequestItem(String str, RequestParams requestParams, String str2, boolean z) {
        this.applicationType = "application/json";
        this.mothed = "POST";
        this.timeOut = 20000;
        this.repeatCount = 0;
        this.maxRepeatCount = 1;
        this.repeatDelay = 20000;
        this.isArrayParams = false;
        this.isRequesting = false;
        this.finished = false;
        this.success = false;
        this.postParamsAsGet = false;
        this.lastRequestTime = 0L;
        this.queryStartTime = 0L;
        this.blockModel = false;
        this.obj = null;
        try {
            this.url = str;
            JSONObject jSONObject = new JSONObject();
            String requestParams2 = requestParams.toString();
            String[] split = (requestParams2 == null ? "" : requestParams2).split("&");
            for (int i = 0; i < split.length; i++) {
                if (split[i].length() != 0) {
                    String[] split2 = split[i].split("=");
                    jSONObject.put(split2[0], split2[1]);
                }
            }
            this.params = jSONObject;
            this.mothed = str2;
            this.postParamsAsGet = z;
        } catch (Exception unused) {
        }
    }

    public RequestItem(String str, RequestParams requestParams, String str2) {
        this.applicationType = "application/json";
        this.mothed = "POST";
        this.timeOut = 20000;
        this.repeatCount = 0;
        this.maxRepeatCount = 1;
        this.repeatDelay = 20000;
        this.isArrayParams = false;
        this.isRequesting = false;
        this.finished = false;
        this.success = false;
        this.postParamsAsGet = false;
        this.lastRequestTime = 0L;
        this.queryStartTime = 0L;
        this.blockModel = false;
        this.obj = null;
        try {
            this.url = str;
            JSONObject jSONObject = new JSONObject();
            String requestParams2 = requestParams.toString();
            String[] split = (requestParams2 == null ? "" : requestParams2).split("&");
            for (int i = 0; i < split.length; i++) {
                if (split[i].length() != 0) {
                    String[] split2 = split[i].split("=");
                    jSONObject.put(split2[0], split2[1]);
                }
            }
            this.params = jSONObject;
            this.mothed = str2;
        } catch (Exception unused) {
        }
    }

    public void setSuccessStr(String str) {
        this.successStr = str;
    }

    public String getSuccessStr() {
        return this.successStr;
    }

    public void addRepeatCount() {
        this.repeatCount++;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void setOnRequestResultListener(OnRequestResultListener onRequestResultListener) {
        this.onRequestResultListener = onRequestResultListener;
    }

    public boolean needRequest() {
        return (this.finished || this.isCanceled || this.repeatCount >= this.maxRepeatCount) ? false : true;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int i) {
        this.state = i;
    }

    public String getRetCode() {
        return this.retCode;
    }

    public void setRetCode(String str) {
        this.retCode = str;
    }

    public Date getAppendTime() {
        return this.pushTime;
    }

    public void setAppendTime(Date date) {
        this.pushTime = date;
    }

    public static int parseSerialNumberFromAck(byte[] bArr) {
        return ObjectHelper.intFromBytes(bArr, 1, 1);
    }

    public Date getFinishTime() {
        return this.finishTime;
    }

    public void setUpdateTime(Date date) {
        this.finishTime = date;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public JSONObject getParams() {
        return this.params;
    }

    public void setParams(JSONObject jSONObject) {
        this.params = jSONObject;
    }

    public JSONArray getArrayParams() {
        return this.arrayParams;
    }

    public void setArrayParams(JSONArray jSONArray) {
        this.arrayParams = jSONArray;
        this.isArrayParams = true;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getMothed() {
        return this.mothed;
    }

    public void setMothed(String str) {
        this.mothed = str;
    }

    public int getRepeat() {
        return this.repeatCount;
    }

    public int getRequestMaxCount() {
        return this.maxRepeatCount;
    }

    public void setRequestMaxCount(int i) {
        this.maxRepeatCount = i;
    }

    public void onFailure(RequestItem requestItem, Headers headers, String str, Throwable th) {
        try {
            OnRequestResultListener onRequestResultListener = this.onRequestResultListener;
            if (onRequestResultListener != null) {
                onRequestResultListener.onFailure(this, 0, str, th);
            }
            if (needRequest()) {
                return;
            }
            onRequestFinished(this, false);
        } catch (Exception unused) {
        }
    }

    public boolean onSuccess(RequestItem requestItem, Headers headers, String str) {
        OnRequestResultListener onRequestResultListener = this.onRequestResultListener;
        if (onRequestResultListener != null) {
            return onRequestResultListener.onSuccess(this, 0, str);
        }
        return true;
    }

    public void onRequestFinished(RequestItem requestItem, boolean z) {
        this.success = z;
        if (this.finished) {
            if (this.blockModel) {
                synchronized (this) {
                    notifyAll();
                }
                return;
            }
            return;
        }
        try {
            this.finished = true;
            OnRequestResultListener onRequestResultListener = this.onRequestResultListener;
            if (onRequestResultListener != null) {
                onRequestResultListener.onRequestFinished(this, z);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.blockModel) {
            synchronized (this) {
                notifyAll();
            }
        }
    }

    public long getRepeatDelay() {
        return this.repeatDelay;
    }

    public void setRepeatDelay(int i) {
        this.repeatDelay = i;
    }

    public boolean isCanceled() {
        return this.isCanceled;
    }

    public void setCanceled(boolean z) {
        this.isCanceled = z;
    }

    public boolean isRequesting() {
        return this.isRequesting;
    }

    public void setRequesting(boolean z) {
        this.isRequesting = z;
        if (z) {
            return;
        }
        this.lastRequestTime = System.currentTimeMillis();
    }

    public ResponseHandler getResponseHandler() {
        if (this.responseHandler == null) {
            this.responseHandler = new ResponseHandler();
        }
        return this.responseHandler;
    }

    protected void setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public long getLastRequestTime() {
        return this.lastRequestTime;
    }

    public void setLastRequestTime(long j) {
        this.lastRequestTime = j;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setPaused(boolean z) {
        this.paused = z;
    }

    public boolean isUnSuccessRequest() {
        return this.isUnSuccessRequest;
    }

    public void setUnSuccessRequest(boolean z) {
        this.isUnSuccessRequest = z;
    }

    public int getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(int i) {
        this.timeOut = i;
    }

    /* loaded from: classes2.dex */
    public class ResponseHandler implements Callback {
        public ResponseHandler() {
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("fail timecost:");
                sb.append(System.currentTimeMillis() - RequestItem.this.queryStartTime);
                sb.append("  requestItem:");
                sb.append(RequestItem.this.getUrl());
                sb.append(" params");
                sb.append(RequestItem.this.isArrayParams ? RequestItem.this.getArrayParams() : RequestItem.this.getParams());
                sb.append(" response");
                sb.append(iOException.getMessage());
                Loger.writeLog("REQUEST", sb.toString());
                RequestItem requestItem = RequestItem.this;
                requestItem.onFailure(requestItem, call.request().headers(), "", iOException);
                if (iOException.getMessage().startsWith("failed to connect to") || iOException.getMessage().startsWith("SSL handshake timed out") || iOException.getMessage().startsWith("Unable to resolve host")) {
                    RequestHelper.testBaidu("REQUEST");
                }
            } catch (Exception unused) {
            }
            RequestItem.this.setRequesting(false);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String responseBody = response.body().string();
                StringBuilder logBuilder = new StringBuilder();
                logBuilder.append("success ");
                logBuilder.append(RequestItem.this.createTime);
                logBuilder.append(" timecost:");
                logBuilder.append(System.currentTimeMillis() - RequestItem.this.queryStartTime);
                logBuilder.append(" requestItem:");
                logBuilder.append(RequestItem.this.getUrl());
                logBuilder.append(" params");
                logBuilder.append(RequestItem.this.isArrayParams ? RequestItem.this.getArrayParams() : RequestItem.this.getParams());
                logBuilder.append(" response");
                logBuilder.append(RequestItem.this.url.contains("www.baidu.com") ? "百度请求成功" : responseBody);
                Loger.writeLog("REQUEST", logBuilder.toString());

                if (RequestItem.this.isCanceled()) {
                    RequestItem.this.onRequestFinished(RequestItem.this, false);
                } else {
                    if (RequestItem.this.onSuccess(RequestItem.this, response.headers(), responseBody)) {
                        RequestItem.this.onRequestFinished(RequestItem.this, true);
                    } else if (!RequestItem.this.needRequest()) {
                        RequestItem.this.onRequestFinished(RequestItem.this, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                RequestItem.this.setRequesting(false);
            }
        }
    }
}
