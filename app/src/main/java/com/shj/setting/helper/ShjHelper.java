package com.shj.setting.helper;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
//import com.iflytek.cloud.SpeechEvent;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Config;
import com.oysb.utils.Loger;
import com.oysb.utils.http.AsyncHttp;
import com.oysb.utils.http.HttpClientUtils;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.http.StringHttpResponseHandlerEx;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.biz.ShjManager;
import com.shj.setting.Dialog.DownloadedGoodsInfoDialog;
import com.shj.setting.Dialog.ScrollTipDialog;
import com.shj.setting.Dialog.TipDialog;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.R;
import com.shj.setting.Utils.ProgressManager;
import com.shj.setting.Utils.ToastUitl;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.UserSettingDao;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ShjHelper {
    public static void downLoadGoodsDetailInfo() {
    }

    static {
        Config.initConfig(ShjManager.class, "shj.cfg");
    }

    public static void downLoadGoodsInfo(Context context, boolean z, UserSettingDao userSettingDao) {
        String goodsInfoUrl = NetAddress.getGoodsInfoUrl();
        RequestParams requestParams = new RequestParams();
        String machineId = AppSetting.getMachineId(context, userSettingDao);
        if (TextUtils.isEmpty(machineId)) {
            if (z) {
                ToastUitl.showLong(context, R.string.machine_code_null_tip);
                return;
            }
            return;
        }
        requestParams.put("machineCode", machineId);
        requestParams.put("queryType", 0);
        RequestItem requestItem = new RequestItem(goodsInfoUrl, requestParams, HttpGet.METHOD_NAME);
        requestItem.setRepeatDelay(5000);
        requestItem.setRequestMaxCount(1);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.helper.ShjHelper.1
//            final /* synthetic */ boolean val$alertMessage;
//            final /* synthetic */ Context val$context;
//
//            AnonymousClass1(boolean z2, Context context2) {
//                z = z2;
//                context = context2;
//            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                if (z) {
                    new TipDialog(context, 0, context.getString(R.string.lab_downloadgoodsinfoerror), context.getString(R.string.button_ok), "").show();
                }
            }

//            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
//            public boolean onSuccess(RequestItem requestItem2, int i, String str) {
//                JSONArray jSONArray;
//                try {
//                    JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(new JSONObject(str), "getMachineAllGood");
//                    if (updateVersionedRequestResult.getString("code").equalsIgnoreCase("H0000")) {
//                        if (updateVersionedRequestResult.get(SpeechEvent.KEY_EVENT_RECORD_DATA) instanceof JSONObject) {
//                            jSONArray = updateVersionedRequestResult.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
//                        } else {
//                            jSONArray = updateVersionedRequestResult.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
//                        }
//                        try {
//                            JSONArray jSONArray2 = updateVersionedRequestResult.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray("bqxx");
//                            updateVersionedRequestResult = new JSONObject();
//                            updateVersionedRequestResult.put("classification", jSONArray2);
//                            updateVersionedRequestResult.put("goods", jSONArray);
//                        } catch (Exception unused) {
//                            updateVersionedRequestResult = new JSONObject();
//                            updateVersionedRequestResult.put("goods", jSONArray);
//                        }
//                    }
//                    SDFileUtils.writeToSDFromInput("xyShj", "goodsNet.json", updateVersionedRequestResult.toString(), false);
//                    if (!z) {
//                        return true;
//                    }
//                    new TipDialog(context, 0, context.getString(R.string.lab_goodsinfoupdated), context.getString(R.string.button_ok), "").show();
//                    return true;
//                } catch (Exception unused2) {
//                    return true;
//                }
//            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z2) {
                if (z2) {
                    return;
                }
                ToastUitl.showLong(context, R.string.lab_neterror);
            }

            @Override
            public boolean onSuccess(RequestItem requestItem, int i, String str) {
                return false;
            }
        });
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.setting.helper.ShjHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ boolean val$alertMessage;
        final /* synthetic */ Context val$context;

        AnonymousClass1(boolean z2, Context context2) {
            z = z2;
            context = context2;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            if (z) {
                new TipDialog(context, 0, context.getString(R.string.lab_downloadgoodsinfoerror), context.getString(R.string.button_ok), "").show();
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            JSONArray jSONArray;
            try {
                JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(new JSONObject(str), "getMachineAllGood");
                if (updateVersionedRequestResult.getString("code").equalsIgnoreCase("H0000")) {
                    if (updateVersionedRequestResult.get(SpeechEvent.KEY_EVENT_RECORD_DATA) instanceof JSONObject) {
                        jSONArray = updateVersionedRequestResult.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    } else {
                        jSONArray = updateVersionedRequestResult.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    }
                    try {
                        JSONArray jSONArray2 = updateVersionedRequestResult.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray("bqxx");
                        updateVersionedRequestResult = new JSONObject();
                        updateVersionedRequestResult.put("classification", jSONArray2);
                        updateVersionedRequestResult.put("goods", jSONArray);
                    } catch (Exception unused) {
                        updateVersionedRequestResult = new JSONObject();
                        updateVersionedRequestResult.put("goods", jSONArray);
                    }
                }
                SDFileUtils.writeToSDFromInput("xyShj", "goodsNet.json", updateVersionedRequestResult.toString(), false);
                if (!z) {
                    return true;
                }
                new TipDialog(context, 0, context.getString(R.string.lab_goodsinfoupdated), context.getString(R.string.button_ok), "").show();
                return true;
            } catch (Exception unused2) {
                return true;
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z2) {
            if (z2) {
                return;
            }
            ToastUitl.showLong(context, R.string.lab_neterror);
        }
    }

    public static void oneButtonSetup(Context context, UserSettingDao userSettingDao, Handler handler) {
        TipDialog tipDialog = new TipDialog(context, 0, context.getString(R.string.lab_onekeysetting), context.getString(R.string.button_onekeyset), context.getString(R.string.button_cancel));
        tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.ShjHelper.2
            final /* synthetic */ Context val$context;
            final /* synthetic */ Handler val$handler;
            final /* synthetic */ UserSettingDao val$mUserSettingDao;

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_02() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void timeEnd() {
            }

            AnonymousClass2(Context context2, UserSettingDao userSettingDao2, Handler handler2) {
                context = context2;
                userSettingDao = userSettingDao2;
                handler = handler2;
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_01() {
                ShjHelper.setShelvesByNet_start(context, userSettingDao, handler, true);
            }
        });
        tipDialog.show();
    }

    /* renamed from: com.shj.setting.helper.ShjHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements TipDialog.TipDialogListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ Handler val$handler;
        final /* synthetic */ UserSettingDao val$mUserSettingDao;

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_02() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void timeEnd() {
        }

        AnonymousClass2(Context context2, UserSettingDao userSettingDao2, Handler handler2) {
            context = context2;
            userSettingDao = userSettingDao2;
            handler = handler2;
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_01() {
            ShjHelper.setShelvesByNet_start(context, userSettingDao, handler, true);
        }
    }

    public static void setShelvesByNet_start(Context context, UserSettingDao userSettingDao, Handler handler, boolean z) {
        RequestParams requestParams = new RequestParams();
        String machineId = AppSetting.getMachineId(context, userSettingDao);
        if (TextUtils.isEmpty(machineId)) {
            ToastUitl.showLong(context, R.string.machine_code_null_tip);
            return;
        }
        requestParams.put("machineCode", machineId);
        requestParams.put("queryType", 1);
        String goodsInfoUrl = NetAddress.getGoodsInfoUrl();
        Loger.writeLog("REQUEST", goodsInfoUrl);
        AsyncHttp.getInstance(context).get(HttpClientUtils.getContentType(goodsInfoUrl), goodsInfoUrl, requestParams, new StringHttpResponseHandlerEx() { // from class: com.shj.setting.helper.ShjHelper.3
            final /* synthetic */ Context val$context;
            final /* synthetic */ Handler val$handler;
            final /* synthetic */ boolean val$isShowTipInfo;

            @Override // com.oysb.utils.http.StringHttpResponseHandlerEx
            public void onStartEx() {
            }

            AnonymousClass3(boolean z2, Context context2, Handler handler2) {
                z = z2;
                context = context2;
                handler = handler2;
            }

            @Override // com.oysb.utils.http.StringHttpResponseHandlerEx
            public void onFailureEx(int i, String str, Throwable th) {
                if (z) {
                    new TipDialog(context, 0, context.getString(R.string.lab_downloadgoodsinfoerror), context.getString(R.string.button_ok), "").show();
                }
            }

            @Override // com.oysb.utils.http.StringHttpResponseHandlerEx
            public void onSuccessEx(int i, String str) {
                JSONArray jSONArray;
                Loger.writeLog("UI", "response=" + str);
                try {
                    JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(new JSONObject(str), "getMachineAllGood_queryType1");
                    if (updateVersionedRequestResult.getString("code").equalsIgnoreCase("H0000")) {
                        if (updateVersionedRequestResult.get(SpeechEvent.KEY_EVENT_RECORD_DATA) instanceof JSONObject) {
                            jSONArray = updateVersionedRequestResult.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        } else {
                            jSONArray = updateVersionedRequestResult.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        }
                    } else {
                        jSONArray = updateVersionedRequestResult.getJSONArray("goods");
                    }
                    boolean z2 = z;
                    if (z2) {
                        ShjHelper.showGoodsInfoDialog(context, jSONArray, handler, z2);
                    } else {
                        ShjHelper.setDownMachineInfo(context, jSONArray, handler, z2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (z) {
                        ToastUitl.showLong(context, R.string.button_onekeyset_error);
                    }
                    Loger.writeLog("UI", "一键设置的商品信息错误");
                }
            }
        });
    }

    /* renamed from: com.shj.setting.helper.ShjHelper$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends StringHttpResponseHandlerEx {
        final /* synthetic */ Context val$context;
        final /* synthetic */ Handler val$handler;
        final /* synthetic */ boolean val$isShowTipInfo;

        @Override // com.oysb.utils.http.StringHttpResponseHandlerEx
        public void onStartEx() {
        }

        AnonymousClass3(boolean z2, Context context2, Handler handler2) {
            z = z2;
            context = context2;
            handler = handler2;
        }

        @Override // com.oysb.utils.http.StringHttpResponseHandlerEx
        public void onFailureEx(int i, String str, Throwable th) {
            if (z) {
                new TipDialog(context, 0, context.getString(R.string.lab_downloadgoodsinfoerror), context.getString(R.string.button_ok), "").show();
            }
        }

        @Override // com.oysb.utils.http.StringHttpResponseHandlerEx
        public void onSuccessEx(int i, String str) {
            JSONArray jSONArray;
            Loger.writeLog("UI", "response=" + str);
            try {
                JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(new JSONObject(str), "getMachineAllGood_queryType1");
                if (updateVersionedRequestResult.getString("code").equalsIgnoreCase("H0000")) {
                    if (updateVersionedRequestResult.get(SpeechEvent.KEY_EVENT_RECORD_DATA) instanceof JSONObject) {
                        jSONArray = updateVersionedRequestResult.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    } else {
                        jSONArray = updateVersionedRequestResult.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    }
                } else {
                    jSONArray = updateVersionedRequestResult.getJSONArray("goods");
                }
                boolean z2 = z;
                if (z2) {
                    ShjHelper.showGoodsInfoDialog(context, jSONArray, handler, z2);
                } else {
                    ShjHelper.setDownMachineInfo(context, jSONArray, handler, z2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (z) {
                    ToastUitl.showLong(context, R.string.button_onekeyset_error);
                }
                Loger.writeLog("UI", "一键设置的商品信息错误");
            }
        }
    }

    /* renamed from: com.shj.setting.helper.ShjHelper$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements DownloadedGoodsInfoDialog.OkClickListener {
        final /* synthetic */ Context val$context;
        final /* synthetic */ JSONArray val$goodsItems;
        final /* synthetic */ Handler val$handler;
        final /* synthetic */ boolean val$isShowTipInfo;

        AnonymousClass4(Context context, JSONArray jSONArray, Handler handler, boolean z) {
            context = context;
            jSONArray = jSONArray;
            handler = handler;
            z = z;
        }

        @Override // com.shj.setting.Dialog.DownloadedGoodsInfoDialog.OkClickListener
        public void okClick() {
            ShjHelper.setDownMachineInfo(context, jSONArray, handler, z);
        }
    }

    public static void showGoodsInfoDialog(Context context, JSONArray jSONArray, Handler handler, boolean z) {
        DownloadedGoodsInfoDialog downloadedGoodsInfoDialog = new DownloadedGoodsInfoDialog(context, jSONArray);
        downloadedGoodsInfoDialog.setOkClickListener(new DownloadedGoodsInfoDialog.OkClickListener() { // from class: com.shj.setting.helper.ShjHelper.4
            final /* synthetic */ Context val$context;
            final /* synthetic */ JSONArray val$goodsItems;
            final /* synthetic */ Handler val$handler;
            final /* synthetic */ boolean val$isShowTipInfo;

            AnonymousClass4(Context context2, JSONArray jSONArray2, Handler handler2, boolean z2) {
                context = context2;
                jSONArray = jSONArray2;
                handler = handler2;
                z = z2;
            }

            @Override // com.shj.setting.Dialog.DownloadedGoodsInfoDialog.OkClickListener
            public void okClick() {
                ShjHelper.setDownMachineInfo(context, jSONArray, handler, z);
            }
        });
        downloadedGoodsInfoDialog.show();
    }

    public static void setDownMachineInfo(Context context, JSONArray jSONArray, Handler handler, boolean z) {
        int i;
        handler.sendEmptyMessage(2);
        try {
            List<Integer> shelves = Shj.getShelves();
            ShjManager.batchStart();
            Loger.writeLog("UI", "goodsItems.length=" + jSONArray.length());
            int i2 = 0;
            for (int i3 = 0; i3 < jSONArray.length(); i3++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i3);
                int parseInt = Integer.parseInt(jSONObject.getString(ShjDbHelper.COLUM_shelf));
                Iterator<Integer> it = shelves.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Integer next = it.next();
                    if (next.intValue() == parseInt) {
                        shelves.remove(next);
                        break;
                    }
                }
                Shj.getInstance(context);
                if (Shj.getShelfInfo(Integer.valueOf(parseInt)) == null) {
                    Loger.writeLog("UI", "shelf = null" + parseInt);
                } else {
                    if (jSONObject.isNull(ShjDbHelper.COLUM_price)) {
                        Loger.writeLog("UI", "一键设置 货道:" + parseInt + "没有取到价格");
                        i = 90000;
                    } else {
                        i = jSONObject.getInt(ShjDbHelper.COLUM_price);
                    }
                    String string = jSONObject.getString("code");
                    ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(parseInt));
                    if (shelfInfo != null) {
                        if (!shelfInfo.getGoodsCode().equals(string)) {
                            ShjManager.setShelfGoodsCode(parseInt, string);
                            i2++;
                        }
                        if (i != 90000 && shelfInfo.getPrice().intValue() != i) {
                            ShjManager.setShelfGoodsPrice(parseInt, i);
                        }
                    } else {
                        ShjManager.setShelfGoodsCode(parseInt, string);
                        ShjManager.setShelfGoodsPrice(parseInt, i);
                        i2++;
                    }
                    i2++;
                }
            }
            for (Integer num : shelves) {
                ShelfInfo shelfInfo2 = Shj.getShelfInfo(num);
                if (shelfInfo2 != null) {
                    if (!shelfInfo2.getGoodsCode().equals("0")) {
                        ShjManager.setShelfGoodsCode(num.intValue(), "0");
                        i2++;
                    }
                    if (shelfInfo2.getPrice().intValue() != 90000) {
                        ShjManager.setShelfGoodsPrice(num.intValue(), 90000);
                    }
                } else {
                    ShjManager.setShelfGoodsCode(num.intValue(), "0");
                    ShjManager.setShelfGoodsPrice(num.intValue(), 90000);
                    i2++;
                }
                i2++;
            }
            ShjManager.batchEnd();
            Loger.writeLog("UI", "commandCount=" + i2);
            handler.sendEmptyMessageDelayed(3, (long) (i2 * 500));
        } catch (Exception e) {
            e.printStackTrace();
            if (z) {
                ToastUitl.showLong(context, R.string.button_onekeyset_error);
            }
            Loger.writeLog("UI", "一键设置的商品信息错误");
            ShjManager.batchEnd();
            handler.sendEmptyMessageDelayed(3, 1000L);
        }
    }

    public static void setShelvesByNet_setting(Context context) {
        Loger.writeLog("UI", "正在执行商品一键设置");
        ProgressManager.showProgressEx(context, context.getString(R.string.lab_setgoods));
    }

    public static void setShelvesByNet_end(Context context, UserSettingDao userSettingDao, Handler handler) {
        Loger.writeLog("UI", "正在执行商品一键设置完成");
        downLoadGoodsInfo(context, false, userSettingDao);
        ProgressManager.closeProgress();
        TipDialog tipDialog = new TipDialog(context, 0, context.getString(R.string.lab_onekeysetsuccess), context.getString(R.string.lab_downloadgoodsimage), context.getString(R.string.button_restartnow));
        tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.ShjHelper.5
            final /* synthetic */ Handler val$handler;

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void timeEnd() {
            }

            AnonymousClass5(Handler handler2) {
                handler = handler2;
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_01() {
                handler.sendEmptyMessage(5);
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_02() {
                handler.sendEmptyMessage(4);
            }
        });
        tipDialog.show();
    }

    /* renamed from: com.shj.setting.helper.ShjHelper$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements TipDialog.TipDialogListener {
        final /* synthetic */ Handler val$handler;

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void timeEnd() {
        }

        AnonymousClass5(Handler handler2) {
            handler = handler2;
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_01() {
            handler.sendEmptyMessage(5);
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_02() {
            handler.sendEmptyMessage(4);
        }
    }

    public static void showHelpDialog(Context context) {
        String fromAssets;
        String language = CommonTool.getLanguage(context);
        if (language.equalsIgnoreCase("en")) {
            fromAssets = getFromAssets(context, "help_en.txt");
        } else if (language.equalsIgnoreCase("fr")) {
            fromAssets = getFromAssets(context, "help_fr.txt");
        } else {
            fromAssets = getFromAssets(context, "help.txt");
        }
        ScrollTipDialog scrollTipDialog = new ScrollTipDialog(context, 0, "", fromAssets, context.getString(R.string.button_ok), "", "", true);
        scrollTipDialog.setTitleVisibility(8);
        scrollTipDialog.setSvMsgSize(context.getResources().getDimensionPixelSize(R.dimen.x600), context.getResources().getDimensionPixelSize(R.dimen.y850));
        scrollTipDialog.show();
    }

    public static String getFromAssets(Context context, String str) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open(str)));
            String str2 = "";
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return str2;
                }
                str2 = str2 + readLine + StringUtils.LF;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
