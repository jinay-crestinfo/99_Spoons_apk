package com.shj.setting.helper;

import android.content.Context;
import android.support.media.ExifInterface;
import android.text.TextUtils;
import com.iflytek.cloud.SpeechEvent;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.oysb.utils.Loger;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.shj.setting.NetAddress.NetAddress;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.machine.BuildConfig;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class AppManageHelper {

    /* loaded from: classes2.dex */
    public interface GetDownloadUrlListener {
        void getDownloadUrl(String str);
    }

    public static void getAppDownloadUrl(Context context, String str, String str2, String str3, int i, GetDownloadUrlListener getDownloadUrlListener) {
        String appType = AppSetting.getAppType(context, null);
        String machineId = AppSetting.getMachineId(context, null);
        RequestParams requestParams = new RequestParams();
        requestParams.put("packageName", str2);
        if (TextUtils.isEmpty(str) || BuildConfig.APPLICATION_ID.equalsIgnoreCase(str2)) {
            requestParams.put("versionType", appType);
        } else {
            requestParams.put("versionType", str);
        }
        requestParams.put("deviceType", 0);
        requestParams.put("jqbh", machineId);
        RequestItem requestItem = new RequestItem(NetAddress.getQueryAppUrl(), requestParams, HttpGet.METHOD_NAME);
        requestItem.setRepeatDelay(5000);
        requestItem.setRequestMaxCount(1);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.helper.AppManageHelper.1
            final /* synthetic */ int val$currentVersionCode;
            final /* synthetic */ String val$currentVersionName;

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            AnonymousClass1(String str32, int i2) {
                str3 = str32;
                i = i2;
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i2, String str4, Throwable th) {
                GetDownloadUrlListener getDownloadUrlListener2 = GetDownloadUrlListener.this;
                if (getDownloadUrlListener2 != null) {
                    getDownloadUrlListener2.getDownloadUrl(null);
                }
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i2, String str4) {
                if (str4 != null) {
                    try {
                        JSONObject jSONObject = new JSONObject(str4);
                        Loger.writeLog("REQUEST", "startUpdateAppService:" + jSONObject.toString());
                        JSONObject jSONObject2 = jSONObject.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        String string = jSONObject2.getString("downloadSite");
                        if (jSONObject2.has("bdownloadSite") && !jSONObject2.isNull("bdownloadSite")) {
                            string = jSONObject2.getString("bdownloadSite");
                        }
                        String string2 = jSONObject2.getString("versionId");
                        String string3 = jSONObject2.getString("code");
                        if (string2.startsWith("v") || string2.startsWith(ExifInterface.GPS_MEASUREMENT_INTERRUPTED)) {
                            string2 = string2.substring(1);
                        }
                        if (str3 != null && i != -1) {
                            if ((string2 + "." + String.format("%06d", Integer.valueOf(Integer.parseInt(string3)))).compareToIgnoreCase(str3 + "." + String.format("%06d", Integer.valueOf(i))) > 0) {
                                GetDownloadUrlListener getDownloadUrlListener2 = GetDownloadUrlListener.this;
                                if (getDownloadUrlListener2 != null) {
                                    getDownloadUrlListener2.getDownloadUrl(string);
                                }
                            } else {
                                GetDownloadUrlListener getDownloadUrlListener3 = GetDownloadUrlListener.this;
                                if (getDownloadUrlListener3 != null) {
                                    getDownloadUrlListener3.getDownloadUrl(null);
                                }
                            }
                        }
                        GetDownloadUrlListener getDownloadUrlListener4 = GetDownloadUrlListener.this;
                        if (getDownloadUrlListener4 != null) {
                            getDownloadUrlListener4.getDownloadUrl(string);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        GetDownloadUrlListener getDownloadUrlListener5 = GetDownloadUrlListener.this;
                        if (getDownloadUrlListener5 != null) {
                            getDownloadUrlListener5.getDownloadUrl(null);
                        }
                    }
                } else {
                    GetDownloadUrlListener getDownloadUrlListener6 = GetDownloadUrlListener.this;
                    if (getDownloadUrlListener6 != null) {
                        getDownloadUrlListener6.getDownloadUrl(null);
                    }
                }
                return true;
            }
        });
        RequestHelper.request(requestItem);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.helper.AppManageHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ int val$currentVersionCode;
        final /* synthetic */ String val$currentVersionName;

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass1(String str32, int i2) {
            str3 = str32;
            i = i2;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i2, String str4, Throwable th) {
            GetDownloadUrlListener getDownloadUrlListener2 = GetDownloadUrlListener.this;
            if (getDownloadUrlListener2 != null) {
                getDownloadUrlListener2.getDownloadUrl(null);
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i2, String str4) {
            if (str4 != null) {
                try {
                    JSONObject jSONObject = new JSONObject(str4);
                    Loger.writeLog("REQUEST", "startUpdateAppService:" + jSONObject.toString());
                    JSONObject jSONObject2 = jSONObject.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    String string = jSONObject2.getString("downloadSite");
                    if (jSONObject2.has("bdownloadSite") && !jSONObject2.isNull("bdownloadSite")) {
                        string = jSONObject2.getString("bdownloadSite");
                    }
                    String string2 = jSONObject2.getString("versionId");
                    String string3 = jSONObject2.getString("code");
                    if (string2.startsWith("v") || string2.startsWith(ExifInterface.GPS_MEASUREMENT_INTERRUPTED)) {
                        string2 = string2.substring(1);
                    }
                    if (str3 != null && i != -1) {
                        if ((string2 + "." + String.format("%06d", Integer.valueOf(Integer.parseInt(string3)))).compareToIgnoreCase(str3 + "." + String.format("%06d", Integer.valueOf(i))) > 0) {
                            GetDownloadUrlListener getDownloadUrlListener2 = GetDownloadUrlListener.this;
                            if (getDownloadUrlListener2 != null) {
                                getDownloadUrlListener2.getDownloadUrl(string);
                            }
                        } else {
                            GetDownloadUrlListener getDownloadUrlListener3 = GetDownloadUrlListener.this;
                            if (getDownloadUrlListener3 != null) {
                                getDownloadUrlListener3.getDownloadUrl(null);
                            }
                        }
                    }
                    GetDownloadUrlListener getDownloadUrlListener4 = GetDownloadUrlListener.this;
                    if (getDownloadUrlListener4 != null) {
                        getDownloadUrlListener4.getDownloadUrl(string);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    GetDownloadUrlListener getDownloadUrlListener5 = GetDownloadUrlListener.this;
                    if (getDownloadUrlListener5 != null) {
                        getDownloadUrlListener5.getDownloadUrl(null);
                    }
                }
            } else {
                GetDownloadUrlListener getDownloadUrlListener6 = GetDownloadUrlListener.this;
                if (getDownloadUrlListener6 != null) {
                    getDownloadUrlListener6.getDownloadUrl(null);
                }
            }
            return true;
        }
    }
}
