package com.xyshj.machine.facepay;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import com.alipay.iot.sdk.APIManager;
//import com.alipay.iot.sdk.coll.TradeDataConstants;
//import com.alipay.zoloz.smile2pay.service.Zoloz;
//import com.alipay.zoloz.smile2pay.service.ZolozCallback;
//import com.iflytek.cloud.SpeechEvent;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.Loger;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.shj.biz.order.OrderPayType;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.Utils.NetUtils;
import com.xyshj.database.setting.AppSetting;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class FacePayHelper extends AbsFacePayHelper {
    static final String CODE_EXIT = "1003";
    static final String CODE_OTHER_PAY = "1005";
    static final String CODE_SUCCESS = "1000";
    static final String CODE_TIMEOUT = "1004";
    public static final String KEY_INIT_RESP_NAME = "zim.init.resp";
    static final String SMILEPAY_CODE_SUCCESS = "10000";
    static final String SMILEPAY_SUBCODE_BALANCE_NOT_ENOUGH = "ACQ.BUYER_BALANCE_NOT_ENOUGH";
    static final String SMILEPAY_SUBCODE_BANKCARD_BALANCE_NOT_ENOUGH = "ACQ.BUYER_BANKCARD_BALANCE_NOT_ENOUGH";
    static final String SMILEPAY_SUBCODE_LIMIT = "ACQ.PRODUCT_AMOUNT_LIMIT_ERROR";
    static final String SMILEPAY_TXT_BANKCARD_BALANCE_NOT_ENOUGH = "账户余额不足，支付失败";
    static final String SMILEPAY_TXT_EBALANCE_NOT_ENOUGH = "账户余额不足，支付失败";
    static final String SMILEPAY_TXT_FAIL = "抱歉未支付成功，请重新支付";
    static final String SMILEPAY_TXT_LIMIT = "刷脸支付超出限额，请选用其他支付方式";
    static final String SMILEPAY_TXT_SUCCESS = "刷脸支付成功";
    private static final String TAG = "FacePayHelper";
    static final String TXT_EXIT = "已退出刷脸支付";
    static final String TXT_NET_ERROR = "网络错误，请重试";
    static final String TXT_OTHER = "抱歉未支付成功，请重新支付";
    static final String TXT_OTHER_PAY = "已退出刷脸支付";
    static final String TXT_TIMEOUT = "操作超时";
    private FacePayResultListering facePayResultListering;
    private boolean isFws;
    private long startPayTime;
    private static final String PAY_INIT_URL = NetAddress.getZfbPayInitUrl();
    private static final String PAY_URL = NetAddress.getZfbPayUrl();
    private static final String PAY_INIT_URL_FWS = NetAddress.getFwsZfbPayInitUrl();
    private static final String PAY_URL_FWS = NetAddress.getFwsZfbPayUrl();
//    private TradeDataConstants.NetworkType networkType = null;
//    private TradeDataConstants.TradeFailType tradeFailType = null;

    @Override // com.xyshj.machine.facepay.AbsFacePayHelper
    public String getfacepayIdKey() {
        return "Zfb_facepayId";
    }

    @Override // com.xyshj.machine.facepay.AbsFacePayHelper
    public OrderPayType getPayType() {
        return OrderPayType.Face;
    }

    @Override // com.xyshj.machine.facepay.AbsFacePayHelper
    public void smilePay(Context context, String str, String str2, String str3) {
        this.startPayTime = System.currentTimeMillis();
//        this.networkType = getNetworkType(context);
        Loger.writeLog("SALES", "out_trade_no=" + str);
        if (str != null) {
            String substring = str.substring(5, 8);
            Loger.writeLog("SALES", "tag=" + substring);
            if ("00H".equals(substring)) {
                this.isFws = false;
            } else {
                this.isFws = true;
            }
        }
        Loger.writeLog("SALES", "开始请求 MetaInfo");
//        Zoloz.getInstance(context).zolozGetMetaInfo(MerchantInfo.mockInfo(context), new ZolozCallback() { // from class: com.xyshj.machine.facepay.FacePayHelper.1
//            final /* synthetic */ String val$amount;
//            final /* synthetic */ Context val$context;
//            final /* synthetic */ String val$out_trade_no;
//
//            AnonymousClass1(Context context2, String str4, String str22) {
//                context = context2;
//                str = str4;
//                str2 = str22;
//            }
//
//            @Override // com.alipay.zoloz.smile2pay.service.ZolozCallback
//            public void response(Map map) {
//                if (map == null) {
//                    Log.e(FacePayHelper.TAG, "response is null");
//                    FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
//                    Loger.writeLog("SALES", "请求 MetaInfo 返回为空");
//                    FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
//                    return;
//                }
//                String str4 = (String) map.get("code");
//                String str5 = (String) map.get("metainfo");
//                if (FacePayHelper.CODE_SUCCESS.equalsIgnoreCase(str4) && str5 != null) {
//                    Log.i(FacePayHelper.TAG, "metanfo is:" + str5);
//                    Loger.writeLog("SALES", "请求 MetaInfo 成功");
//                    FacePayHelper.this.zimData(context, str5, str, str2);
//                    return;
//                }
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
//                Loger.writeLog("SALES", "请求 MetaInfo 失败");
//                AppStatusLoger.addAppStatus_Count(null, "SALES", AppStatusLoger.Type_HttpRequest, "", "zfb face 请求 MetaInfo 失败");
//                FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
//            }
//        });
    }

    /* renamed from: com.xyshj.machine.facepay.FacePayHelper$1 */
    /* loaded from: classes2.dex */
//    class AnonymousClass1 implements ZolozCallback {
//        final /* synthetic */ String val$amount;
//        final /* synthetic */ Context val$context;
//        final /* synthetic */ String val$out_trade_no;
//
//        AnonymousClass1(Context context2, String str4, String str22) {
//            context = context2;
//            str = str4;
//            str2 = str22;
//        }
//
//        @Override // com.alipay.zoloz.smile2pay.service.ZolozCallback
//        public void response(Map map) {
//            if (map == null) {
//                Log.e(FacePayHelper.TAG, "response is null");
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
//                Loger.writeLog("SALES", "请求 MetaInfo 返回为空");
//                FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
//                return;
//            }
//            String str4 = (String) map.get("code");
//            String str5 = (String) map.get("metainfo");
//            if (FacePayHelper.CODE_SUCCESS.equalsIgnoreCase(str4) && str5 != null) {
//                Log.i(FacePayHelper.TAG, "metanfo is:" + str5);
//                Loger.writeLog("SALES", "请求 MetaInfo 成功");
//                FacePayHelper.this.zimData(context, str5, str, str2);
//                return;
//            }
//            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
//            Loger.writeLog("SALES", "请求 MetaInfo 失败");
//            AppStatusLoger.addAppStatus_Count(null, "SALES", AppStatusLoger.Type_HttpRequest, "", "zfb face 请求 MetaInfo 失败");
//            FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
//        }
//    }

    public void zimData(Context context, String str, String str2, String str3) {
        String str4 = PAY_INIT_URL;
        if (this.isFws) {
            str4 = PAY_INIT_URL_FWS;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("metaInfo", str);
        requestParams.put("out_trade_no", str2);
        RequestItem requestItem = new RequestItem(str4, requestParams, HttpGet.METHOD_NAME);
        requestItem.setRepeatDelay(4000);
        requestItem.setRequestMaxCount(3);
        Loger.writeLog("SALES", "开始请求 zimInitClientData");
        Loger.writeLog("SALES", "url=" + str4);
        Loger.writeLog("SALES", "params = (metaInfo=" + str);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.facepay.FacePayHelper.2
//            final /* synthetic */ String val$amount;
//            final /* synthetic */ Context val$context;
//            final /* synthetic */ String val$out_trade_no;
//
//            AnonymousClass2(Context context2, String str22, String str32) {
//                context = context2;
//                str2 = str22;
//                str3 = str32;
//            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str5, Throwable th) {
                Loger.writeLog("SALES", "onFailure response=" + str5);
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.NETWORK_EXCEPTION;
                Loger.writeLog("SALES", "请求 zimInitClientData error=" + th.toString());
                AppStatusLoger.addAppStatus_Count(null, "SALES", AppStatusLoger.Type_HttpRequest, "", "zfb face 请求 zimInitClientData error=" + th.toString());
                FacePayHelper.this.promptText(context, FacePayHelper.TXT_NET_ERROR);
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str5) {
                Loger.writeLog("SALES", "success response=" + str5);
                JSONObject parseObject = JSON.parseObject(str5);
                String string = parseObject.getString("code");
                Loger.writeLog("SALES", "zimInitClientData code=" + string);
                if ("H0000".equalsIgnoreCase(string)) {
//                    String string2 = parseObject.getString(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    String string2 = null;
                    if (string2 != null) {
                        try {
                            Loger.writeLog("SALES", "parse zimInitClientData");
                            JSONObject parseObject2 = JSON.parseObject(string2);
                            String string3 = parseObject2.getString("zimId");
                            String string4 = parseObject2.getString("zimInitClientData");
                            if (string3 != null && string4 != null) {
                                Loger.writeLog("SALES", "zimId =" + string3 + "; zimInitClientData" + string4);
                                if (FacePayHelper.this.facePayResultListering != null) {
                                    FacePayHelper.this.facePayResultListering.onFacePayStared();
                                }
                                Loger.writeLog("SALES", "请求 zimInitClientData 成功");
                                FacePayHelper.this.smile(context, string3, string4, str2, str3);
                                return true;
                            }
//                            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                            Loger.writeLog("SALES", "请求 zimInitClientData 失败");
                            FacePayHelper.this.promptText(context, string2);
                            return true;
                        } catch (Exception unused) {
//                            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                            Loger.writeLog("SALES", "请求 zimInitClientData 失败");
                            FacePayHelper.this.promptText(context, string2);
                            return true;
                        }
                    }
//                    FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                    Loger.writeLog("SALES", "请求 zimInitClientData 失败");
                    FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
                    return true;
                }
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                Loger.writeLog("SALES", "请求 zimInitClientData 失败");
                FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
                return true;
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
                Log.i(FacePayHelper.TAG, "onRequestFinished success=" + z);
            }
        });
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.xyshj.machine.facepay.FacePayHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements RequestItem.OnRequestResultListener {
//        final /* synthetic */ String val$amount;
//        final /* synthetic */ Context val$context;
//        final /* synthetic */ String val$out_trade_no;

//        AnonymousClass2(Context context2, String str22, String str32) {
//            context = context2;
//            str2 = str22;
//            str3 = str32;
//        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str5, Throwable th) {
            Loger.writeLog("SALES", "onFailure response=" + str5);
//            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.NETWORK_EXCEPTION;
            Loger.writeLog("SALES", "请求 zimInitClientData error=" + th.toString());
            AppStatusLoger.addAppStatus_Count(null, "SALES", AppStatusLoger.Type_HttpRequest, "", "zfb face 请求 zimInitClientData error=" + th.toString());
            FacePayHelper.this.promptText(context, FacePayHelper.TXT_NET_ERROR);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str5) {
            Loger.writeLog("SALES", "success response=" + str5);
            JSONObject parseObject = JSON.parseObject(str5);
            String string = parseObject.getString("code");
            Loger.writeLog("SALES", "zimInitClientData code=" + string);
            if ("H0000".equalsIgnoreCase(string)) {
//                String string2 = parseObject.getString(SpeechEvent.KEY_EVENT_RECORD_DATA);
                String string2 = null;
                if (string2 != null) {
                    try {
                        Loger.writeLog("SALES", "parse zimInitClientData");
                        JSONObject parseObject2 = JSON.parseObject(string2);
                        String string3 = parseObject2.getString("zimId");
                        String string4 = parseObject2.getString("zimInitClientData");
                        if (string3 != null && string4 != null) {
                            Loger.writeLog("SALES", "zimId =" + string3 + "; zimInitClientData" + string4);
                            if (FacePayHelper.this.facePayResultListering != null) {
                                FacePayHelper.this.facePayResultListering.onFacePayStared();
                            }
                            Loger.writeLog("SALES", "请求 zimInitClientData 成功");
//                            FacePayHelper.this.smile(context, string3, string4, str2, str3);
                            return true;
                        }
//                        FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                        Loger.writeLog("SALES", "请求 zimInitClientData 失败");
                        FacePayHelper.this.promptText(context, string2);
                        return true;
                    } catch (Exception unused) {
//                        FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                        Loger.writeLog("SALES", "请求 zimInitClientData 失败");
                        FacePayHelper.this.promptText(context, string2);
                        return true;
                    }
                }
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                Loger.writeLog("SALES", "请求 zimInitClientData 失败");
//                FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
                return true;
            }
//            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
            Loger.writeLog("SALES", "请求 zimInitClientData 失败");
            FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
            return true;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
            Log.i(FacePayHelper.TAG, "onRequestFinished success=" + z);
        }
    }

//    public void zolozUninstall(Context context) {
//        Zoloz.getInstance(context).zolozUninstall();
//    }

    public void smile(Context context, String str, String str2, String str3, String str4) {
        HashMap hashMap = new HashMap();
        hashMap.put(KEY_INIT_RESP_NAME, str2);
        Loger.writeLog("SALES", "开始刷脸");
//        Zoloz.getInstance(context).zolozVerify(str, hashMap, new ZolozCallback() { // from class: com.xyshj.machine.facepay.FacePayHelper.3
//            final /* synthetic */ String val$amount;
//            final /* synthetic */ Context val$context;
//            final /* synthetic */ String val$out_trade_no;
//
//            AnonymousClass3(Context context2, String str42, String str32) {
//                context = context2;
//                str4 = str42;
//                str3 = str32;
//            }
//
//            @Override // com.alipay.zoloz.smile2pay.service.ZolozCallback
//            public void response(Map map) {
//                String str5 = "抱歉未支付成功，请重新支付";
//                if (map == null) {
//                    FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
//                    Loger.writeLog("SALES", "刷脸失败");
//                    FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
//                    return;
//                }
//                String str6 = (String) map.get("code");
//                String str7 = (String) map.get("ftoken");
//                String str8 = (String) map.get("subCode");
//                Log.d(FacePayHelper.TAG, "ftoken is:" + str7);
//                if (FacePayHelper.CODE_SUCCESS.equalsIgnoreCase(str6) && str7 != null) {
//                    try {
//                        if (FacePayHelper.this.facePayResultListering != null) {
//                            FacePayHelper.this.facePayResultListering.onFaceSuccess();
//                        }
//                        FacePayHelper.this.startPayTime = System.currentTimeMillis();
//                        Loger.writeLog("SALES", "刷脸成功");
//                        FacePayHelper.this.pay(context, str7, str4, str3);
//                        return;
//                    } catch (Exception e) {
//                        FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
//                        Loger.writeLog("SALES", "刷脸失败" + e.toString());
//                        FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
//                        return;
//                    }
//                }
//                if (FacePayHelper.CODE_EXIT.equalsIgnoreCase(str6)) {
//                    FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.OTHER;
//                    Loger.writeLog("SALES", "刷脸失败 已退出刷脸支付");
//                    FacePayHelper.this.promptText(context, "已退出刷脸支付");
//                    return;
//                }
//                if (FacePayHelper.CODE_TIMEOUT.equalsIgnoreCase(str6)) {
//                    FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.TIMEOUT;
//                    Loger.writeLog("SALES", "刷脸失败 操作超时");
//                    FacePayHelper.this.promptText(context, FacePayHelper.TXT_TIMEOUT);
//                    return;
//                }
//                if (FacePayHelper.CODE_OTHER_PAY.equalsIgnoreCase(str6)) {
//                    FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.OTHER;
//                    Loger.writeLog("SALES", "刷脸失败 已退出刷脸支付");
//                    FacePayHelper.this.promptText(context, "已退出刷脸支付");
//                    return;
//                }
//                if (!TextUtils.isEmpty(str8)) {
//                    str5 = "抱歉未支付成功，请重新支付(" + str8 + ")";
//                }
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
//                Loger.writeLog("SALES", "刷脸失败 " + str5);
//                FacePayHelper.this.promptText(context, str5);
//            }
//        });
    }

    /* renamed from: com.xyshj.machine.facepay.FacePayHelper$3 */
    /* loaded from: classes2.dex */
//    public class AnonymousClass3 implements ZolozCallback {
//        final /* synthetic */ String val$amount;
//        final /* synthetic */ Context val$context;
//        final /* synthetic */ String val$out_trade_no;
//
//        AnonymousClass3(Context context2, String str42, String str32) {
//            context = context2;
//            str4 = str42;
//            str3 = str32;
//        }
//
//        @Override // com.alipay.zoloz.smile2pay.service.ZolozCallback
//        public void response(Map map) {
//            String str5 = "抱歉未支付成功，请重新支付";
//            if (map == null) {
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
//                Loger.writeLog("SALES", "刷脸失败");
//                FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
//                return;
//            }
//            String str6 = (String) map.get("code");
//            String str7 = (String) map.get("ftoken");
//            String str8 = (String) map.get("subCode");
//            Log.d(FacePayHelper.TAG, "ftoken is:" + str7);
//            if (FacePayHelper.CODE_SUCCESS.equalsIgnoreCase(str6) && str7 != null) {
//                try {
//                    if (FacePayHelper.this.facePayResultListering != null) {
//                        FacePayHelper.this.facePayResultListering.onFaceSuccess();
//                    }
//                    FacePayHelper.this.startPayTime = System.currentTimeMillis();
//                    Loger.writeLog("SALES", "刷脸成功");
//                    FacePayHelper.this.pay(context, str7, str4, str3);
//                    return;
//                } catch (Exception e) {
//                    FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
//                    Loger.writeLog("SALES", "刷脸失败" + e.toString());
//                    FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
//                    return;
//                }
//            }
//            if (FacePayHelper.CODE_EXIT.equalsIgnoreCase(str6)) {
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.OTHER;
//                Loger.writeLog("SALES", "刷脸失败 已退出刷脸支付");
//                FacePayHelper.this.promptText(context, "已退出刷脸支付");
//                return;
//            }
//            if (FacePayHelper.CODE_TIMEOUT.equalsIgnoreCase(str6)) {
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.TIMEOUT;
//                Loger.writeLog("SALES", "刷脸失败 操作超时");
//                FacePayHelper.this.promptText(context, FacePayHelper.TXT_TIMEOUT);
//                return;
//            }
//            if (FacePayHelper.CODE_OTHER_PAY.equalsIgnoreCase(str6)) {
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.OTHER;
//                Loger.writeLog("SALES", "刷脸失败 已退出刷脸支付");
//                FacePayHelper.this.promptText(context, "已退出刷脸支付");
//                return;
//            }
//            if (!TextUtils.isEmpty(str8)) {
//                str5 = "抱歉未支付成功，请重新支付(" + str8 + ")";
//            }
//            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
//            Loger.writeLog("SALES", "刷脸失败 " + str5);
//            FacePayHelper.this.promptText(context, str5);
//        }
//    }

    public void promptText(Context context, String str) {
        if (this.facePayResultListering != null) {
            if (TXT_NET_ERROR.equals(str)) {
                this.facePayResultListering.onPayFail(true);
            } else {
                this.facePayResultListering.onPayFail(false);
            }
        }
        Loger.writeLog("SALES", "face error:" + str);
//        reportData(false, this.tradeFailType, str, this.networkType, (int) (System.currentTimeMillis() - this.startPayTime));
    }

    public void pay(Context context, String str, String str2, String str3) {
        String str4 = PAY_URL;
        if (this.isFws) {
            str4 = PAY_URL_FWS;
        }
        String machineId = AppSetting.getMachineId(context, null);
        String obj = machineId != null ? machineId.toString() : "00000000000";
        RequestParams requestParams = new RequestParams();
//        String signWithFaceToken = APIManager.getInstance().getPaymentAPI().signWithFaceToken(str, str2);
        String signWithFaceToken = "jndindijdnasdnasldnasdnduinDLIJDasdasd";
        requestParams.put("terminal_params", signWithFaceToken);
        requestParams.put("ftoken", str);
        requestParams.put("amount", str2);
        requestParams.put("out_trade_no", str3);
        requestParams.put("terminal_id", obj);
        RequestItem requestItem = new RequestItem(str4, requestParams, HttpGet.METHOD_NAME);
        requestItem.setRepeatDelay(4000);
        requestItem.setRequestMaxCount(3);
        Loger.writeLog("SALES", "开始支付");
        Loger.writeLog("SALES", "url=" + str4);
        Loger.writeLog("SALES", "params= (ftoken=" + str + "  terminal_params=" + signWithFaceToken + "  amount=" + str2 + "out_trade_no " + str3 + "  terminal_id=" + obj + ")");
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.facepay.FacePayHelper.4
//            final /* synthetic */ Context val$context;

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

//            AnonymousClass4(Context context2) {
//                context = context2;
//            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str5, Throwable th) {
                Loger.writeLog("SALES", "pay onFailure response=" + str5);
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.NETWORK_EXCEPTION;
                Loger.writeLog("SALES", "支付失败 网络错误，请重试");
                AppStatusLoger.addAppStatus_Count(null, "SALES", AppStatusLoger.Type_HttpRequest, "", "zfb face /service-pay/pay/smile/pay onFail" + th.toString());
                FacePayHelper.this.promptText(context, FacePayHelper.TXT_NET_ERROR);
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str5) {
                Loger.writeLog("SALES", "success response = " + str5);
                JSONObject parseObject = JSON.parseObject(str5);
                String string = parseObject.getString("code");
                if ("H0000".equalsIgnoreCase(string)) {
//                    String string2 = parseObject.getString(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    String string2 = null;
                    if (string2 != null) {
                        try {
                            JSONObject parseObject2 = JSON.parseObject(string2);
                            parseObject2.getString(NotificationCompat.CATEGORY_MESSAGE);
                            String string3 = parseObject2.getString("code");
                            parseObject2.getString("sub_msg");
                            if (FacePayHelper.SMILEPAY_CODE_SUCCESS.equals(string3)) {
                                Loger.writeLog("SALES", "支付成功 ");
                                if (FacePayHelper.this.facePayResultListering != null) {
                                    FacePayHelper.this.facePayResultListering.onPaySuccess();
                                }
//                                FacePayHelper.this.reportData(true, TradeDataConstants.TradeFailType.NONE, null, FacePayHelper.this.networkType, (int) (System.currentTimeMillis() - FacePayHelper.this.startPayTime));
                                return true;
                            }
                            String string4 = parseObject2.getString("sub_code");
                            if (FacePayHelper.SMILEPAY_SUBCODE_LIMIT.equalsIgnoreCase(string4)) {
//                                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.OTHER;
                                Loger.writeLog("SALES", "支付失败 刷脸支付超出限额，请选用其他支付方式");
                                FacePayHelper.this.promptText(context, FacePayHelper.SMILEPAY_TXT_LIMIT);
                            } else if (FacePayHelper.SMILEPAY_SUBCODE_BALANCE_NOT_ENOUGH.equalsIgnoreCase(string4)) {
//                                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.OTHER;
                                Loger.writeLog("SALES", "支付失败 账户余额不足，支付失败");
                                FacePayHelper.this.promptText(context, "账户余额不足，支付失败");
                            } else if (FacePayHelper.SMILEPAY_SUBCODE_BANKCARD_BALANCE_NOT_ENOUGH.equalsIgnoreCase(string4)) {
//                                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.OTHER;
                                Loger.writeLog("SALES", "支付失败 账户余额不足，支付失败");
                                FacePayHelper.this.promptText(context, "账户余额不足，支付失败");
                            } else {
//                                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                                Loger.writeLog("SALES", "支付失败 抱歉未支付成功，请重新支付");
                                FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
                            }
                            if (FacePayHelper.this.facePayResultListering == null) {
                                return true;
                            }
                            FacePayHelper.this.facePayResultListering.showUnderAgeTip();
                            return true;
                        } catch (Exception e) {
//                            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                            Loger.writeLog("SALES", "支付失败 " + e.toString());
                            long currentTimeMillis = System.currentTimeMillis() - FacePayHelper.this.startPayTime;
                            FacePayHelper facePayHelper = FacePayHelper.this;
                            facePayHelper.reportData(false, facePayHelper.tradeFailType, string2, FacePayHelper.this.networkType, (int) currentTimeMillis);
                            return true;
                        }
                    }
//                    FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                    Loger.writeLog("SALES", "支付失败 ");
                    FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
                    if (FacePayHelper.this.facePayResultListering == null) {
                        return true;
                    }
                    FacePayHelper.this.facePayResultListering.showUnderAgeTip();
                    return true;
                }
                if ("B0001".equalsIgnoreCase(string)) {
//                    FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                    Loger.writeLog("SALES", "支付失败 ");
                    FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
                    if (FacePayHelper.this.facePayResultListering == null) {
                        return true;
                    }
                    FacePayHelper.this.facePayResultListering.showNotRigister();
                    return true;
                }
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                Loger.writeLog("SALES", "支付失败 ");
                FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
                if (FacePayHelper.this.facePayResultListering == null) {
                    return true;
                }
                FacePayHelper.this.facePayResultListering.showUnderAgeTip();
                return true;
            }
        });
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.xyshj.machine.facepay.FacePayHelper$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements RequestItem.OnRequestResultListener {
//        final /* synthetic */ Context val$context;

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }



        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str5, Throwable th) {
            Loger.writeLog("SALES", "pay onFailure response=" + str5);
//            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.NETWORK_EXCEPTION;
            Loger.writeLog("SALES", "支付失败 网络错误，请重试");
            AppStatusLoger.addAppStatus_Count(null, "SALES", AppStatusLoger.Type_HttpRequest, "", "zfb face /service-pay/pay/smile/pay onFail" + th.toString());
//            FacePayHelper.this.promptText(context, FacePayHelper.TXT_NET_ERROR);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str5) {
            Loger.writeLog("SALES", "success response = " + str5);
            JSONObject parseObject = JSON.parseObject(str5);
            String string = parseObject.getString("code");
            if ("H0000".equalsIgnoreCase(string)) {
//                String string2 = parseObject.getString(SpeechEvent.KEY_EVENT_RECORD_DATA);
                String string2 = null;
                if (string2 != null) {
                    try {
                        JSONObject parseObject2 = JSON.parseObject(string2);
                        parseObject2.getString(NotificationCompat.CATEGORY_MESSAGE);
                        String string3 = parseObject2.getString("code");
                        parseObject2.getString("sub_msg");
                        if (FacePayHelper.SMILEPAY_CODE_SUCCESS.equals(string3)) {
                            Loger.writeLog("SALES", "支付成功 ");
                            if (FacePayHelper.this.facePayResultListering != null) {
                                FacePayHelper.this.facePayResultListering.onPaySuccess();
                            }
//                            FacePayHelper.this.reportData(true, TradeDataConstants.TradeFailType.NONE, null, FacePayHelper.this.networkType, (int) (System.currentTimeMillis() - FacePayHelper.this.startPayTime));
                            return true;
                        }
                        String string4 = parseObject2.getString("sub_code");
                        if (FacePayHelper.SMILEPAY_SUBCODE_LIMIT.equalsIgnoreCase(string4)) {
//                            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.OTHER;
                            Loger.writeLog("SALES", "支付失败 刷脸支付超出限额，请选用其他支付方式");
//                            FacePayHelper.this.promptText(context, FacePayHelper.SMILEPAY_TXT_LIMIT);
                        } else if (FacePayHelper.SMILEPAY_SUBCODE_BALANCE_NOT_ENOUGH.equalsIgnoreCase(string4)) {
//                            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.OTHER;
                            Loger.writeLog("SALES", "支付失败 账户余额不足，支付失败");
//                            FacePayHelper.this.promptText(context, "账户余额不足，支付失败");
                        } else if (FacePayHelper.SMILEPAY_SUBCODE_BANKCARD_BALANCE_NOT_ENOUGH.equalsIgnoreCase(string4)) {
//                            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.OTHER;
                            Loger.writeLog("SALES", "支付失败 账户余额不足，支付失败");
//                            FacePayHelper.this.promptText(context, "账户余额不足，支付失败");
                        } else {
//                            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                            Loger.writeLog("SALES", "支付失败 抱歉未支付成功，请重新支付");
//                            FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
                        }
                        if (FacePayHelper.this.facePayResultListering == null) {
                            return true;
                        }
                        FacePayHelper.this.facePayResultListering.showUnderAgeTip();
                        return true;
                    } catch (Exception e) {
//                        FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                        Loger.writeLog("SALES", "支付失败 " + e.toString());
                        long currentTimeMillis = System.currentTimeMillis() - FacePayHelper.this.startPayTime;
                        FacePayHelper facePayHelper = FacePayHelper.this;
//                        facePayHelper.reportData(false, facePayHelper.tradeFailType, string2, FacePayHelper.this.networkType, (int) currentTimeMillis);
                        return true;
                    }
                }
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                Loger.writeLog("SALES", "支付失败 ");
//                FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
                if (FacePayHelper.this.facePayResultListering == null) {
                    return true;
                }
                FacePayHelper.this.facePayResultListering.showUnderAgeTip();
                return true;
            }
            if ("B0001".equalsIgnoreCase(string)) {
//                FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
                Loger.writeLog("SALES", "支付失败 ");
//                FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
                if (FacePayHelper.this.facePayResultListering == null) {
                    return true;
                }
                FacePayHelper.this.facePayResultListering.showNotRigister();
                return true;
            }
//            FacePayHelper.this.tradeFailType = TradeDataConstants.TradeFailType.UNKNOW_EXCEPTION;
            Loger.writeLog("SALES", "支付失败 ");
//            FacePayHelper.this.promptText(context, "抱歉未支付成功，请重新支付");
            if (FacePayHelper.this.facePayResultListering == null) {
                return true;
            }
            FacePayHelper.this.facePayResultListering.showUnderAgeTip();
            return true;
        }
    }

//    public void reportData(boolean z, TradeDataConstants.TradeFailType tradeFailType, String str, TradeDataConstants.NetworkType networkType, int i) {
//        if (str != null) {
//            try {
//                str = URLEncoder.encode(str, "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
////        APIManager.getInstance().getCollectionAPI().reportTradeData(z, tradeFailType, str, networkType, i);
//    }

//    private TradeDataConstants.NetworkType getNetworkType(Context context) {
//        String GetNetworkType = NetUtils.GetNetworkType(context);
//        if ("2G".equalsIgnoreCase(GetNetworkType)) {
//            return TradeDataConstants.NetworkType._2G;
//        }
//        if ("3G".equalsIgnoreCase(GetNetworkType)) {
//            return TradeDataConstants.NetworkType._3G;
//        }
//        if ("4G".equalsIgnoreCase(GetNetworkType)) {
//            return TradeDataConstants.NetworkType._4G;
//        }
//        if ("wifi".equalsIgnoreCase(GetNetworkType)) {
//            return TradeDataConstants.NetworkType._WIFI;
//        }
//        return TradeDataConstants.NetworkType._LAN;
//    }

    @Override // com.xyshj.machine.facepay.AbsFacePayHelper
    public void setFacePayResultListering(FacePayResultListering facePayResultListering) {
        this.facePayResultListering = facePayResultListering;
    }
}
