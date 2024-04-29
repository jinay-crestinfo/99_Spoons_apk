package com.xyshj.machine.app;

import com.alipay.api.AlipayConstants;
import com.iflytek.cloud.SpeechConstant;
import com.oysb.utils.Loger;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.date.DateUtil;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import java.util.Date;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ZaadPayHelper {
    public static String ApiKey = "API-1173755504AHX";
    public static String ApiUserId = "1007165";
    public static String MerchantUid = "M0913528";
    private static ResultListener listener;

    /* loaded from: classes2.dex */
    public interface ResultListener {
        void onResult(boolean z, String str, String str2, String str3);
    }

    public static float usd2slsh(float f) {
        try {
            return Float.parseFloat(CacheHelper.getFileCache().getAsString("usd2slsh")) * f;
        } catch (Exception unused) {
            return 9000.0f * f;
        }
    }

    public static void PreAuthorize(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, ResultListener resultListener) {
        try {
            listener = resultListener;
            RequestItem requestItem = new RequestItem("https://pg.waafipay.com/PaymentGateway/asm", new JSONObject("{\"schemaVersion\": \"" + str + "\",\"requestId\": \"" + str2 + "\",\"timestamp\": \"" + str3 + "\",\"channelName\": \"" + str4 + "\",\"serviceName\": \"" + str5 + "\",serviceParams: {\"merchantUid\": \"" + MerchantUid + "\",\"apiUserId\": \"" + ApiUserId + "\",\"apiKey\": \"" + ApiKey + "\",\"paymentMethod\": \"" + str6 + "\",payerInfo:{\"accountNo\": \"" + str7 + "\"},transactionInfo:{\"referenceId\": \"" + str8 + "\",\"invoiceId\": \"" + str9 + "\",\"amount\": \"" + str10 + "\",\"currency\": \"" + str11 + "\",\"description\": \"" + str12 + "\",\"paymentBrand\": \"" + str13 + "\",\"transactionCategory\": \"" + str14 + "\"}}}"), "POST");
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.app.ZaadPayHelper.1
                final /* synthetic */ String val$referenceId;
                final /* synthetic */ String val$requestId;

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                AnonymousClass1(String str22, String str82) {
                    str2 = str22;
                    str8 = str82;
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str15, Throwable th) {
                    Loger.writeLog("SALES", str15);
                    if (!th.getMessage().contains(SpeechConstant.NET_TIMEOUT) || ZaadPayHelper.listener == null) {
                        return;
                    }
                    ZaadPayHelper.listener.onResult(false, "PreAuthorize", "", SpeechConstant.NET_TIMEOUT);
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str15) {
                    try {
                        JSONObject jSONObject = new JSONObject(str15);
                        jSONObject.getString("schemaVersion");
                        jSONObject.getString("responseId");
                        jSONObject.getString(AlipayConstants.TIMESTAMP);
                        String string = jSONObject.getString("responseCode");
                        String string2 = jSONObject.getString("errorCode");
                        String string3 = jSONObject.getString("responseMsg");
                        String string4 = jSONObject.getString(SpeechConstant.PARAMS);
                        if (!string.equals("2001")) {
                            if (ZaadPayHelper.listener != null) {
                                ZaadPayHelper.listener.onResult(false, "PreAuthorize", string2, string3);
                            }
                        } else {
                            JSONObject jSONObject2 = new JSONObject(string4);
                            jSONObject2.getString("state");
                            jSONObject2.getString("referenceId");
                            String string5 = jSONObject2.getString("transactionId");
                            jSONObject2.getString("txAmount");
                            ZaadPayHelper.PreAuthorizeCommit("1.0", str2, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"), "WEB", "API_PREAUTHORIZE_COMMIT", string5, "Transaction Commited", str8);
                        }
                    } catch (Exception unused) {
                    }
                    return false;
                }
            });
            RequestHelper.request(requestItem);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.app.ZaadPayHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ String val$referenceId;
        final /* synthetic */ String val$requestId;

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass1(String str22, String str82) {
            str2 = str22;
            str8 = str82;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str15, Throwable th) {
            Loger.writeLog("SALES", str15);
            if (!th.getMessage().contains(SpeechConstant.NET_TIMEOUT) || ZaadPayHelper.listener == null) {
                return;
            }
            ZaadPayHelper.listener.onResult(false, "PreAuthorize", "", SpeechConstant.NET_TIMEOUT);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str15) {
            try {
                JSONObject jSONObject = new JSONObject(str15);
                jSONObject.getString("schemaVersion");
                jSONObject.getString("responseId");
                jSONObject.getString(AlipayConstants.TIMESTAMP);
                String string = jSONObject.getString("responseCode");
                String string2 = jSONObject.getString("errorCode");
                String string3 = jSONObject.getString("responseMsg");
                String string4 = jSONObject.getString(SpeechConstant.PARAMS);
                if (!string.equals("2001")) {
                    if (ZaadPayHelper.listener != null) {
                        ZaadPayHelper.listener.onResult(false, "PreAuthorize", string2, string3);
                    }
                } else {
                    JSONObject jSONObject2 = new JSONObject(string4);
                    jSONObject2.getString("state");
                    jSONObject2.getString("referenceId");
                    String string5 = jSONObject2.getString("transactionId");
                    jSONObject2.getString("txAmount");
                    ZaadPayHelper.PreAuthorizeCommit("1.0", str2, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"), "WEB", "API_PREAUTHORIZE_COMMIT", string5, "Transaction Commited", str8);
                }
            } catch (Exception unused) {
            }
            return false;
        }
    }

    public static void PreAuthorizeCommit(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        try {
            RequestItem requestItem = new RequestItem("https://pg.waafipay.com/PaymentGateway/asm", new JSONObject("{\"schemaVersion\": \"" + str + "\",\"requestId\": \"" + str2 + "\",\"timestamp\": \"" + str3 + "\",\"channelName\": \"" + str4 + "\",\"serviceName\": \"" + str5 + "\",serviceParams: {\"merchantUid\": \"" + MerchantUid + "\",\"apiUserId\": \"" + ApiUserId + "\",\"apiKey\": \"" + ApiKey + "\",\"transactionId\": \"" + str6 + "\",\"description\": \"" + str7 + "\",\"referenceId\": \"" + str8 + "\"}}"), "POST");
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.app.ZaadPayHelper.2
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                AnonymousClass2() {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str9, Throwable th) {
                    Loger.writeLog("SALES", str9);
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str9) {
                    try {
                        JSONObject jSONObject = new JSONObject(str9);
                        jSONObject.getString("schemaVersion");
                        jSONObject.getString("responseId");
                        jSONObject.getString(AlipayConstants.TIMESTAMP);
                        String string = jSONObject.getString("responseCode");
                        String string2 = jSONObject.getString("errorCode");
                        String string3 = jSONObject.getString("responseMsg");
                        String string4 = jSONObject.getString(SpeechConstant.PARAMS);
                        if (!string.equals("2001")) {
                            if (ZaadPayHelper.listener != null) {
                                ZaadPayHelper.listener.onResult(false, "PreAuthorizeCommit", string2, string3);
                            }
                        } else {
                            JSONObject jSONObject2 = new JSONObject(string4);
                            jSONObject2.getString("description");
                            jSONObject2.getString("state");
                            jSONObject2.getString("transactionId");
                            jSONObject2.getString("referenceId");
                            if (ZaadPayHelper.listener != null) {
                                ZaadPayHelper.listener.onResult(true, "PreAuthorizeCommit", string2, string3);
                            }
                        }
                    } catch (Exception unused) {
                    }
                    return false;
                }
            });
            RequestHelper.request(requestItem);
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.xyshj.machine.app.ZaadPayHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass2() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str9, Throwable th) {
            Loger.writeLog("SALES", str9);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str9) {
            try {
                JSONObject jSONObject = new JSONObject(str9);
                jSONObject.getString("schemaVersion");
                jSONObject.getString("responseId");
                jSONObject.getString(AlipayConstants.TIMESTAMP);
                String string = jSONObject.getString("responseCode");
                String string2 = jSONObject.getString("errorCode");
                String string3 = jSONObject.getString("responseMsg");
                String string4 = jSONObject.getString(SpeechConstant.PARAMS);
                if (!string.equals("2001")) {
                    if (ZaadPayHelper.listener != null) {
                        ZaadPayHelper.listener.onResult(false, "PreAuthorizeCommit", string2, string3);
                    }
                } else {
                    JSONObject jSONObject2 = new JSONObject(string4);
                    jSONObject2.getString("description");
                    jSONObject2.getString("state");
                    jSONObject2.getString("transactionId");
                    jSONObject2.getString("referenceId");
                    if (ZaadPayHelper.listener != null) {
                        ZaadPayHelper.listener.onResult(true, "PreAuthorizeCommit", string2, string3);
                    }
                }
            } catch (Exception unused) {
            }
            return false;
        }
    }
}
