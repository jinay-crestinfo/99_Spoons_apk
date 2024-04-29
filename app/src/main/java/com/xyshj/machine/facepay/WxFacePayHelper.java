package com.xyshj.machine.facepay;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.exoplayer.text.ttml.TtmlNode;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.Loger;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.shj.biz.order.OrderPayType;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.tencent.wxpayface.FacePayConstants;
import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;
import com.tencent.wxpayface.WxfacePayCommonCode;
import com.xyshj.database.setting.AppSetting;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class WxFacePayHelper extends AbsFacePayHelper {
    private static final String PARAMS_APPID = "appid";
    private static final String PARAMS_ASK_FACE_PERMIT = "ask_face_permit";
    private static final String PARAMS_ASK_RET_PAGE = "ask_ret_page";
    private static final String PARAMS_AUTHINFO = "authinfo";
    private static final String PARAMS_BANNER_STATE = "banner_state";
    private static final String PARAMS_FACE_AUTHTYPE = "face_authtype";
    private static final String PARAMS_MCH_ID = "mch_id";
    private static final String PARAMS_MCH_NAME = "mch_name";
    private static final String PARAMS_OUT_TRADE_NO = "out_trade_no";
    private static final String PARAMS_REPORT_ITEM = "item";
    private static final String PARAMS_REPORT_ITEMVALUE = "item_value";
    private static final String PARAMS_REPORT_MCH_ID = "mch_id";
    private static final String PARAMS_REPORT_OUT_TRADE_NO = "out_trade_no";
    private static final String PARAMS_REPORT_SUT_MCH_ID = "sub_mch_id";
    private static final String PARAMS_STORE_ID = "store_id";
    private static final String PARAMS_SUB_APPID = "sub_appid";
    private static final String PARAMS_SUB_MCH_ID = "sub_mch_id";
    private static final String PARAMS_TELEPHONE = "telephone";
    private static final String PARAMS_TOTAL_FEE = "total_fee";
    public static final String RETURN_CODE = "return_code";
    public static final String RETURN_MSG = "return_msg";
    private static String mch_Id;
    private static String sub_mch_id;
    private static final String url_authInfo = NetAddress.getWinxinAuthInfoUrl();
    private static final String url_facepay = NetAddress.getWinxinFacepayUrl();
    private static final String url_queryPayResult = NetAddress.getQueryPayResult();
    private String amount;
    private String appId;
    private long enterTime;
    private String goodsName;
    private String mAuthInfo;
    private String machineId;
    private String payId;
    private String sub_appid;
    private FacePayResultListering wxFacePayResultListering;

    @Override // com.xyshj.machine.facepay.AbsFacePayHelper
    public String getfacepayIdKey() {
        return "Wx_facepayId";
    }

    @Override // com.xyshj.machine.facepay.AbsFacePayHelper
    public OrderPayType getPayType() {
        return OrderPayType.WxFace;
    }

    public static void init(Context context, InitListenring initListenring) {
        Loger.writeLog("SALES", "wxfacepay init");
        WxPayFace.getInstance().initWxpayface(context, new HashMap(), new IWxPayfaceCallback() { // from class: com.xyshj.machine.facepay.WxFacePayHelper.1
            AnonymousClass1() {
            }

            @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
            public void response(Map map) throws RemoteException {
                if (WxFacePayHelper.isSuccessInfo(map)) {
                    Loger.writeLog("SALES", "wxfacepay 初始化完成");
                    InitListenring initListenring2 = InitListenring.this;
                    if (initListenring2 != null) {
                        initListenring2.initCompelete();
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.facepay.WxFacePayHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends IWxPayfaceCallback {
        AnonymousClass1() {
        }

        @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
        public void response(Map map) throws RemoteException {
            if (WxFacePayHelper.isSuccessInfo(map)) {
                Loger.writeLog("SALES", "wxfacepay 初始化完成");
                InitListenring initListenring2 = InitListenring.this;
                if (initListenring2 != null) {
                    initListenring2.initCompelete();
                }
            }
        }
    }

    public static void releaseWxpayface(Context context) {
        WxPayFace.getInstance().releaseWxpayface(context);
    }

    @Override // com.xyshj.machine.facepay.AbsFacePayHelper
    public void smilePay(Context context, String str, String str2, String str3) {
        this.enterTime = System.currentTimeMillis();
        String machineId = AppSetting.getMachineId(context, null);
        this.machineId = machineId;
        if (machineId == null) {
            this.machineId = "00000000";
        }
        this.sub_appid = null;
        Loger.writeLog("SALES", "payid=" + str);
        String[] split = str.split("#");
        if (split != null && split.length >= 4) {
            this.payId = split[0];
            this.appId = split[1];
            mch_Id = split[2];
            sub_mch_id = split[3];
            if (split.length >= 5) {
                this.sub_appid = split[4];
            }
            Loger.writeLog("SALES", "appId=" + this.appId);
            Loger.writeLog("SALES", "mch_Id=" + mch_Id);
            Loger.writeLog("SALES", "sub_mch_id=" + sub_mch_id);
            Loger.writeLog("SALES", "sub_appid=" + this.sub_appid);
            this.amount = str2;
            this.goodsName = str3;
            Loger.writeLog("SALES", "订单号：" + this.payId);
            getWxpayfaceRawdata();
            return;
        }
        ToastUitl.showShort(context, R.string.wx_payid_error);
        FacePayResultListering facePayResultListering = this.wxFacePayResultListering;
        if (facePayResultListering != null) {
            facePayResultListering.onPayFail(false);
        }
    }

    /* renamed from: com.xyshj.machine.facepay.WxFacePayHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends IWxPayfaceCallback {
        AnonymousClass2() {
        }

        @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
        public void response(Map map) throws RemoteException {
            if (!WxFacePayHelper.isSuccessInfo(map)) {
                if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                    WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
                    return;
                }
                return;
            }
            String obj = map.get("rawdata").toString();
            Loger.writeLog("SALES", "response | getWxpayfaceRawdata rawdata=" + obj);
            try {
                WxFacePayHelper.this.getAuthInfo(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getWxpayfaceRawdata() {
        Loger.writeLog("SALES", "wxfacepay getWxpayfaceRawdata");
        WxPayFace.getInstance().getWxpayfaceRawdata(new IWxPayfaceCallback() { // from class: com.xyshj.machine.facepay.WxFacePayHelper.2
            AnonymousClass2() {
            }

            @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
            public void response(Map map) throws RemoteException {
                if (!WxFacePayHelper.isSuccessInfo(map)) {
                    if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                        WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
                        return;
                    }
                    return;
                }
                String obj = map.get("rawdata").toString();
                Loger.writeLog("SALES", "response | getWxpayfaceRawdata rawdata=" + obj);
                try {
                    WxFacePayHelper.this.getAuthInfo(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getAuthInfo(String str) throws IOException {
        RequestParams requestParams = new RequestParams();
        requestParams.put("out_trade_no", this.payId);
        requestParams.put("rawdata", str);
        requestParams.put("appid", this.appId);
        requestParams.put(FacePayConstants.KEY_REQ_PARAMS_MCHID, mch_Id);
        requestParams.put(FacePayConstants.KEY_REQ_PARAMS_SUB_MCH_ID, sub_mch_id);
        String str2 = this.sub_appid;
        if (str2 != null && str2.length() > 10) {
            requestParams.put(PARAMS_SUB_APPID, this.sub_appid);
        }
        RequestItem requestItem = new RequestItem(url_authInfo, requestParams, HttpGet.METHOD_NAME);
        requestItem.setRepeatDelay(4000);
        requestItem.setRequestMaxCount(3);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.facepay.WxFacePayHelper.3
            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            AnonymousClass3() {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str3, Throwable th) {
                Loger.writeLog("SALES", "onFailure getAuthInfo" + str3);
                AppStatusLoger.addAppStatus_Count(null, "SALES", AppStatusLoger.Type_HttpRequest, "", "winxin face /api/wx_xy_fws/getWxpayFaceAuthInfo.php onFail" + th.toString());
                if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                    WxFacePayHelper.this.wxFacePayResultListering.onPayFail(true);
                }
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str3) {
                Loger.writeLog("SALES", "getAuthInfo response=" + str3.toString());
                try {
                    if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                        WxFacePayHelper.this.wxFacePayResultListering.onFacePayStared();
                    }
                    WxFacePayHelper.this.mAuthInfo = ReturnXMLParser.parseGetAuthInfoXML(WxFacePayHelper.Str2Inputstr(str3));
                    Loger.writeLog("SALES", "wxfacepay Get authinfo SUCCESS");
                    WxFacePayHelper.this.codePay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Loger.writeLog("SALES", "getAuthInfo mAuthInfo=" + WxFacePayHelper.this.mAuthInfo);
                return true;
            }
        });
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.xyshj.machine.facepay.WxFacePayHelper$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass3() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str3, Throwable th) {
            Loger.writeLog("SALES", "onFailure getAuthInfo" + str3);
            AppStatusLoger.addAppStatus_Count(null, "SALES", AppStatusLoger.Type_HttpRequest, "", "winxin face /api/wx_xy_fws/getWxpayFaceAuthInfo.php onFail" + th.toString());
            if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                WxFacePayHelper.this.wxFacePayResultListering.onPayFail(true);
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str3) {
            Loger.writeLog("SALES", "getAuthInfo response=" + str3.toString());
            try {
                if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                    WxFacePayHelper.this.wxFacePayResultListering.onFacePayStared();
                }
                WxFacePayHelper.this.mAuthInfo = ReturnXMLParser.parseGetAuthInfoXML(WxFacePayHelper.Str2Inputstr(str3));
                Loger.writeLog("SALES", "wxfacepay Get authinfo SUCCESS");
                WxFacePayHelper.this.codePay();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Loger.writeLog("SALES", "getAuthInfo mAuthInfo=" + WxFacePayHelper.this.mAuthInfo);
            return true;
        }
    }

    public static InputStream Str2Inputstr(String str) {
        try {
            return new StringBufferInputStream(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void codePay() {
        Loger.writeLog("SALES", "wxfacepay codePay");
        HashMap hashMap = new HashMap();
        hashMap.put(PARAMS_FACE_AUTHTYPE, "FACEPAY");
        hashMap.put("appid", this.appId);
        hashMap.put(FacePayConstants.KEY_REQ_PARAMS_MCHID, mch_Id);
        hashMap.put(FacePayConstants.KEY_REQ_PARAMS_SUB_MCH_ID, sub_mch_id);
        String str = this.sub_appid;
        if (str != null && str.length() > 10) {
            hashMap.put(PARAMS_SUB_APPID, this.sub_appid);
        }
        hashMap.put(PARAMS_STORE_ID, this.machineId);
        hashMap.put("out_trade_no", this.payId);
        hashMap.put(PARAMS_TOTAL_FEE, this.amount);
        hashMap.put(PARAMS_AUTHINFO, this.mAuthInfo);
        hashMap.put(PARAMS_ASK_RET_PAGE, "0");
        hashMap.put(PARAMS_ASK_FACE_PERMIT, "0");
        WxPayFace.getInstance().getWxpayfaceCode(hashMap, new IWxPayfaceCallback() { // from class: com.xyshj.machine.facepay.WxFacePayHelper.4
            AnonymousClass4() {
            }

            @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
            public void response(Map map) throws RemoteException {
                if (!WxFacePayHelper.isSuccessInfo(map)) {
                    if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                        WxFacePayHelper.this.wxFacePayResultListering.onPayFail(true);
                        return;
                    }
                    return;
                }
                Loger.writeLog("SALES", "response | getWxpayfaceCode");
                String str2 = (String) map.get("return_code");
                if (TextUtils.equals(str2, "SUCCESS")) {
                    WxFacePayHelper.this.servicePay((String) map.get("face_code"), (String) map.get("openid"));
                    Loger.writeLog("SALES", "支付完成");
                    try {
                        Thread.sleep(2000L);
                    } catch (Exception unused) {
                    }
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("appid", WxFacePayHelper.this.appId);
                    hashMap2.put(FacePayConstants.KEY_REQ_PARAMS_MCHID, WxFacePayHelper.mch_Id);
                    hashMap2.put(FacePayConstants.KEY_REQ_PARAMS_SUB_MCH_ID, WxFacePayHelper.sub_mch_id);
                    if (WxFacePayHelper.this.sub_appid != null && WxFacePayHelper.this.sub_appid.length() > 10) {
                        hashMap2.put(WxFacePayHelper.PARAMS_SUB_APPID, WxFacePayHelper.this.sub_appid);
                    }
                    hashMap2.put(WxFacePayHelper.PARAMS_AUTHINFO, WxFacePayHelper.this.mAuthInfo);
                    hashMap2.put(WxFacePayHelper.PARAMS_STORE_ID, WxFacePayHelper.this.machineId);
                    hashMap2.put("payresult", "SUCCESS");
                    WxPayFace.getInstance().updateWxpayfacePayResult(hashMap2, new IWxPayfaceCallback() { // from class: com.xyshj.machine.facepay.WxFacePayHelper.4.1
                        AnonymousClass1() {
                        }

                        @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
                        public void response(Map map2) throws RemoteException {
                            WxFacePayHelper.this.notifyServerQuery();
                            if (!WxFacePayHelper.isSuccessInfo(map2)) {
                                if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                                    WxFacePayHelper.this.wxFacePayResultListering.onPayFail(true);
                                }
                            } else if (TextUtils.equals((String) map2.get("return_code"), "SUCCESS")) {
                                if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                                    WxFacePayHelper.this.wxFacePayResultListering.onPaySuccess();
                                }
                                WxFacePayHelper.reportTimeInfo((int) (System.currentTimeMillis() - WxFacePayHelper.this.enterTime));
                            } else if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                                WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
                            }
                        }
                    });
                    return;
                }
                if (TextUtils.equals(str2, WxfacePayCommonCode.VAL_RSP_PARAMS_USER_CANCEL)) {
                    Loger.writeLog("SALES", "用户取消");
                    if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                        WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
                        return;
                    }
                    return;
                }
                if (!TextUtils.equals(str2, WxfacePayCommonCode.VAL_RSP_PARAMS_SCAN_PAYMENT)) {
                    if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                        WxFacePayHelper.this.wxFacePayResultListering.onPayFail(true);
                    }
                } else {
                    Loger.writeLog("SALES", "扫码支付");
                    if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                        WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
                    }
                }
            }

            /* renamed from: com.xyshj.machine.facepay.WxFacePayHelper$4$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 extends IWxPayfaceCallback {
                AnonymousClass1() {
                }

                @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
                public void response(Map map2) throws RemoteException {
                    WxFacePayHelper.this.notifyServerQuery();
                    if (!WxFacePayHelper.isSuccessInfo(map2)) {
                        if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                            WxFacePayHelper.this.wxFacePayResultListering.onPayFail(true);
                        }
                    } else if (TextUtils.equals((String) map2.get("return_code"), "SUCCESS")) {
                        if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                            WxFacePayHelper.this.wxFacePayResultListering.onPaySuccess();
                        }
                        WxFacePayHelper.reportTimeInfo((int) (System.currentTimeMillis() - WxFacePayHelper.this.enterTime));
                    } else if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                        WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
                    }
                }
            }
        });
    }

    /* renamed from: com.xyshj.machine.facepay.WxFacePayHelper$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 extends IWxPayfaceCallback {
        AnonymousClass4() {
        }

        @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
        public void response(Map map) throws RemoteException {
            if (!WxFacePayHelper.isSuccessInfo(map)) {
                if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                    WxFacePayHelper.this.wxFacePayResultListering.onPayFail(true);
                    return;
                }
                return;
            }
            Loger.writeLog("SALES", "response | getWxpayfaceCode");
            String str2 = (String) map.get("return_code");
            if (TextUtils.equals(str2, "SUCCESS")) {
                WxFacePayHelper.this.servicePay((String) map.get("face_code"), (String) map.get("openid"));
                Loger.writeLog("SALES", "支付完成");
                try {
                    Thread.sleep(2000L);
                } catch (Exception unused) {
                }
                HashMap hashMap2 = new HashMap();
                hashMap2.put("appid", WxFacePayHelper.this.appId);
                hashMap2.put(FacePayConstants.KEY_REQ_PARAMS_MCHID, WxFacePayHelper.mch_Id);
                hashMap2.put(FacePayConstants.KEY_REQ_PARAMS_SUB_MCH_ID, WxFacePayHelper.sub_mch_id);
                if (WxFacePayHelper.this.sub_appid != null && WxFacePayHelper.this.sub_appid.length() > 10) {
                    hashMap2.put(WxFacePayHelper.PARAMS_SUB_APPID, WxFacePayHelper.this.sub_appid);
                }
                hashMap2.put(WxFacePayHelper.PARAMS_AUTHINFO, WxFacePayHelper.this.mAuthInfo);
                hashMap2.put(WxFacePayHelper.PARAMS_STORE_ID, WxFacePayHelper.this.machineId);
                hashMap2.put("payresult", "SUCCESS");
                WxPayFace.getInstance().updateWxpayfacePayResult(hashMap2, new IWxPayfaceCallback() { // from class: com.xyshj.machine.facepay.WxFacePayHelper.4.1
                    AnonymousClass1() {
                    }

                    @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
                    public void response(Map map2) throws RemoteException {
                        WxFacePayHelper.this.notifyServerQuery();
                        if (!WxFacePayHelper.isSuccessInfo(map2)) {
                            if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                                WxFacePayHelper.this.wxFacePayResultListering.onPayFail(true);
                            }
                        } else if (TextUtils.equals((String) map2.get("return_code"), "SUCCESS")) {
                            if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                                WxFacePayHelper.this.wxFacePayResultListering.onPaySuccess();
                            }
                            WxFacePayHelper.reportTimeInfo((int) (System.currentTimeMillis() - WxFacePayHelper.this.enterTime));
                        } else if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                            WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
                        }
                    }
                });
                return;
            }
            if (TextUtils.equals(str2, WxfacePayCommonCode.VAL_RSP_PARAMS_USER_CANCEL)) {
                Loger.writeLog("SALES", "用户取消");
                if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                    WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
                    return;
                }
                return;
            }
            if (!TextUtils.equals(str2, WxfacePayCommonCode.VAL_RSP_PARAMS_SCAN_PAYMENT)) {
                if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                    WxFacePayHelper.this.wxFacePayResultListering.onPayFail(true);
                }
            } else {
                Loger.writeLog("SALES", "扫码支付");
                if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                    WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
                }
            }
        }

        /* renamed from: com.xyshj.machine.facepay.WxFacePayHelper$4$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 extends IWxPayfaceCallback {
            AnonymousClass1() {
            }

            @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
            public void response(Map map2) throws RemoteException {
                WxFacePayHelper.this.notifyServerQuery();
                if (!WxFacePayHelper.isSuccessInfo(map2)) {
                    if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                        WxFacePayHelper.this.wxFacePayResultListering.onPayFail(true);
                    }
                } else if (TextUtils.equals((String) map2.get("return_code"), "SUCCESS")) {
                    if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                        WxFacePayHelper.this.wxFacePayResultListering.onPaySuccess();
                    }
                    WxFacePayHelper.reportTimeInfo((int) (System.currentTimeMillis() - WxFacePayHelper.this.enterTime));
                } else if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                    WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
                }
            }
        }
    }

    public void servicePay(String str, String str2) {
        String str3 = url_facepay;
        RequestParams requestParams = new RequestParams();
        requestParams.put("out_trade_no", this.payId);
        requestParams.put("appid", this.appId);
        requestParams.put(FacePayConstants.KEY_REQ_PARAMS_MCHID, mch_Id);
        requestParams.put(FacePayConstants.KEY_REQ_PARAMS_SUB_MCH_ID, sub_mch_id);
        String str4 = this.sub_appid;
        if (str4 != null && str4.length() > 10) {
            requestParams.put(PARAMS_SUB_APPID, this.sub_appid);
        }
        requestParams.put(PARAMS_TOTAL_FEE, this.amount);
        requestParams.put("openid", str2);
        requestParams.put("face_code", str);
        requestParams.put("jqbh", this.machineId);
        requestParams.put(TtmlNode.TAG_BODY, this.goodsName);
        RequestItem requestItem = new RequestItem(str3, requestParams, HttpGet.METHOD_NAME);
        requestItem.setRepeatDelay(4000);
        requestItem.setRequestMaxCount(3);
        Loger.writeLog("SALES", "开始支付");
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.facepay.WxFacePayHelper.5
            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            AnonymousClass5() {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str5, Throwable th) {
                Loger.writeLog("SALES", "servicePay pay onFailure response=" + str5);
                AppStatusLoger.addAppStatus_Count(null, "SALES", AppStatusLoger.Type_HttpRequest, "", "winxin face /api/wx_xy_fws/facepay.php onFail" + th.toString());
                if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                    WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
                }
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str5) {
                Loger.writeLog("SALES", "servicePay success response = " + str5);
                return true;
            }
        });
        RequestHelper.request(requestItem);
        reportOrder(this.payId);
        reportFacePayCount(1);
    }

    /* renamed from: com.xyshj.machine.facepay.WxFacePayHelper$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass5() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str5, Throwable th) {
            Loger.writeLog("SALES", "servicePay pay onFailure response=" + str5);
            AppStatusLoger.addAppStatus_Count(null, "SALES", AppStatusLoger.Type_HttpRequest, "", "winxin face /api/wx_xy_fws/facepay.php onFail" + th.toString());
            if (WxFacePayHelper.this.wxFacePayResultListering != null) {
                WxFacePayHelper.this.wxFacePayResultListering.onPayFail(false);
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str5) {
            Loger.writeLog("SALES", "servicePay success response = " + str5);
            return true;
        }
    }

    public void showBanner() {
        HashMap hashMap = new HashMap();
        hashMap.put(PARAMS_BANNER_STATE, 0);
        WxPayFace.getInstance().updateWxpayfaceBannerState(hashMap, new IWxPayfaceCallback() { // from class: com.xyshj.machine.facepay.WxFacePayHelper.6
            AnonymousClass6() {
            }

            @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
            public void response(Map map) throws RemoteException {
                if (WxFacePayHelper.isSuccessInfo(map)) {
                    Loger.writeLog("SALES", "response | showbanner");
                }
            }
        });
    }

    /* renamed from: com.xyshj.machine.facepay.WxFacePayHelper$6 */
    /* loaded from: classes2.dex */
    class AnonymousClass6 extends IWxPayfaceCallback {
        AnonymousClass6() {
        }

        @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
        public void response(Map map) throws RemoteException {
            if (WxFacePayHelper.isSuccessInfo(map)) {
                Loger.writeLog("SALES", "response | showbanner");
            }
        }
    }

    public void notifyServerQuery() {
        String str = url_queryPayResult;
        RequestParams requestParams = new RequestParams();
        requestParams.put("out_trade_no", this.payId);
        RequestItem requestItem = new RequestItem(str, requestParams, HttpGet.METHOD_NAME);
        requestItem.setRepeatDelay(4000);
        requestItem.setRequestMaxCount(3);
        RequestHelper.request(requestItem);
    }

    public void removeBanner() {
        HashMap hashMap = new HashMap();
        hashMap.put(PARAMS_BANNER_STATE, 1);
        WxPayFace.getInstance().updateWxpayfaceBannerState(hashMap, new IWxPayfaceCallback() { // from class: com.xyshj.machine.facepay.WxFacePayHelper.7
            AnonymousClass7() {
            }

            @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
            public void response(Map map) throws RemoteException {
                if (WxFacePayHelper.isSuccessInfo(map)) {
                    Loger.writeLog("SALES", "response | removebanner");
                }
            }
        });
    }

    /* renamed from: com.xyshj.machine.facepay.WxFacePayHelper$7 */
    /* loaded from: classes2.dex */
    class AnonymousClass7 extends IWxPayfaceCallback {
        AnonymousClass7() {
        }

        @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
        public void response(Map map) throws RemoteException {
            if (WxFacePayHelper.isSuccessInfo(map)) {
                Loger.writeLog("SALES", "response | removebanner");
            }
        }
    }

    public static boolean isSuccessInfo(Map map) {
        if (map == null) {
            Loger.writeLog("SALES", "调用返回为空, 请查看日志");
            new RuntimeException("调用返回为空").printStackTrace();
            return false;
        }
        String str = (String) map.get("return_code");
        String str2 = (String) map.get("return_msg");
        Loger.writeLog("SALES", "response | getWxpayfaceRawdata " + str + " | " + str2);
        if (str == null || !str.equals("SUCCESS")) {
            Loger.writeLog("SALES", "调用返回非成功信息, 请查看日志");
            new RuntimeException("调用返回非成功信息: " + str2).printStackTrace();
            return false;
        }
        Loger.writeLog("SALES", "调用返回成功");
        return true;
    }

    public static void reportOrder(String str) {
        Loger.writeLog("SALES", "reportorder out_trade_no=" + str);
        HashMap hashMap = new HashMap();
        hashMap.put(FacePayConstants.KEY_REQ_PARAMS_MCHID, mch_Id);
        hashMap.put(FacePayConstants.KEY_REQ_PARAMS_SUB_MCH_ID, sub_mch_id);
        hashMap.put("out_trade_no", str);
        WxPayFace.getInstance().reportOrder(hashMap, new IWxPayfaceCallback() { // from class: com.xyshj.machine.facepay.WxFacePayHelper.8
            AnonymousClass8() {
            }

            @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
            public void response(Map map) throws RemoteException {
                Loger.writeLog("SALES", "reportorder response xxx");
                if (WxFacePayHelper.isSuccessInfo(map)) {
                    Loger.writeLog("SALES", "reportorder sucess");
                }
            }
        });
    }

    /* renamed from: com.xyshj.machine.facepay.WxFacePayHelper$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 extends IWxPayfaceCallback {
        AnonymousClass8() {
        }

        @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
        public void response(Map map) throws RemoteException {
            Loger.writeLog("SALES", "reportorder response xxx");
            if (WxFacePayHelper.isSuccessInfo(map)) {
                Loger.writeLog("SALES", "reportorder sucess");
            }
        }
    }

    public static void reportTimeInfo(int i) {
        reportInfo("face.mch.facepay.timecost", Integer.valueOf(i));
    }

    public static void reportFacePayCount(int i) {
        reportInfo("face.mch.facepay.count", Integer.valueOf(i));
    }

    public static void reportInfo(String str, Object obj) {
        Loger.writeLog("SALES", "reportInfo");
        HashMap hashMap = new HashMap();
        hashMap.put(PARAMS_REPORT_ITEM, str);
        hashMap.put(PARAMS_REPORT_ITEMVALUE, obj);
        WxPayFace.getInstance().reportInfo(hashMap, new IWxPayfaceCallback() { // from class: com.xyshj.machine.facepay.WxFacePayHelper.9
            final /* synthetic */ String val$item;

            AnonymousClass9(String str2) {
                str = str2;
            }

            @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
            public void response(Map map) throws RemoteException {
                if (WxFacePayHelper.isSuccessInfo(map)) {
                    Loger.writeLog("SALES", "reportInfo " + str + " sucess");
                }
            }
        });
    }

    /* renamed from: com.xyshj.machine.facepay.WxFacePayHelper$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 extends IWxPayfaceCallback {
        final /* synthetic */ String val$item;

        AnonymousClass9(String str2) {
            str = str2;
        }

        @Override // com.tencent.wxpayface.IWxPayFaceCallbackAIDL
        public void response(Map map) throws RemoteException {
            if (WxFacePayHelper.isSuccessInfo(map)) {
                Loger.writeLog("SALES", "reportInfo " + str + " sucess");
            }
        }
    }

    @Override // com.xyshj.machine.facepay.AbsFacePayHelper
    public void setFacePayResultListering(FacePayResultListering facePayResultListering) {
        this.wxFacePayResultListering = facePayResultListering;
    }
}
