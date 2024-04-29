package com.shj.setting.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.github.mjdev.libaums.fs.UsbFile;
import com.iflytek.cloud.SpeechEvent;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.oysb.utils.Loger;
import com.oysb.utils.cache.BitmapCache;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.io.file.SDFileUtils;
import com.oysb.utils.video.TTSManager;
import com.shj.setting.Dialog.TipDialog;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.R;
import com.shj.setting.Utils.ProgressManager;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.bean.Fodder;
import com.shj.setting.event.UpdataGoodsInfoUIEvent;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class GoodsImagesHelper {
    private static final int MSG_EXIT_APP = 9000;
    private static final int PROCES_CLOSE = 3005;
    private static final int PROCES_CURRENT = 3003;
    private static final int PROCES_END = 3001;
    private static final int PROCES_MESSAGE = 3004;
    private static final int PROCES_START = 3000;
    private static final int PROCES_TIMEOUT = 3002;
    private Context context;
    protected ProgressDialog myProgressDialog;
    Map<Integer, List<String>> smImgitems = new HashMap();
    Map<Integer, List<String>> xqImgitems = new HashMap();
    private Handler handler = new Handler() { // from class: com.shj.setting.helper.GoodsImagesHelper.8
        AnonymousClass8() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                int i = message.what;
                if (i == GoodsImagesHelper.MSG_EXIT_APP) {
                    System.exit(0);
                } else {
                    switch (i) {
                        case 3000:
                            Object[] objArr = (Object[]) message.obj;
                            GoodsImagesHelper.this.showProgressDlg();
                            GoodsImagesHelper.this.myProgressDialog.setMax(((Integer) objArr[0]).intValue());
                            GoodsImagesHelper.this.myProgressDialog.setProgress(0);
                            break;
                        case GoodsImagesHelper.PROCES_END /* 3001 */:
                            GoodsImagesHelper.this.myProgressDialog.setMessage(GoodsImagesHelper.this.context.getString(R.string.lab_downloadsuccess));
                            Message obtain = Message.obtain();
                            obtain.what = GoodsImagesHelper.PROCES_CLOSE;
                            obtain.obj = message.obj;
                            GoodsImagesHelper.this.handler.sendMessageDelayed(obtain, 1000L);
                            break;
                        case GoodsImagesHelper.PROCES_TIMEOUT /* 3002 */:
                            GoodsImagesHelper.this.closeProgressDlg();
                            new TipDialog(GoodsImagesHelper.this.context, 3, GoodsImagesHelper.this.context.getString(R.string.lab_downloadimagetimeout), GoodsImagesHelper.this.context.getString(R.string.button_ok), "").show();
                            break;
                        case GoodsImagesHelper.PROCES_CURRENT /* 3003 */:
                            Object[] objArr2 = (Object[]) message.obj;
                            GoodsImagesHelper.this.myProgressDialog.setProgress(((Integer) objArr2[0]).intValue());
                            GoodsImagesHelper.this.myProgressDialog.setMessage(objArr2[1].toString());
                            break;
                        case 3004:
                            GoodsImagesHelper.this.myProgressDialog.setMessage(message.obj.toString());
                            break;
                        case GoodsImagesHelper.PROCES_CLOSE /* 3005 */:
                            BitmapCache.clearCache();
                            EventBus.getDefault().post(new UpdataGoodsInfoUIEvent());
                            GoodsImagesHelper.this.closeProgressDlg();
                            Object[] objArr3 = (Object[]) message.obj;
                            if (!((Boolean) objArr3[0]).booleanValue()) {
                                break;
                            } else if (!((Boolean) objArr3[1]).booleanValue()) {
                                new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.lab_imageupdatefinished), GoodsImagesHelper.this.context.getString(R.string.button_ok), "").show();
                                break;
                            } else if (((Boolean) objArr3[2]).booleanValue()) {
                                TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 10, GoodsImagesHelper.this.context.getString(R.string.lab_imageupdatefinished_restart), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.8.1
                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_02() {
                                    }

                                    AnonymousClass1() {
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_01() {
                                        GoodsImagesHelper.this.exitApp(GoodsImagesHelper.this.context, 1000L);
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void timeEnd() {
                                        GoodsImagesHelper.this.exitApp(GoodsImagesHelper.this.context, 1000L);
                                    }
                                });
                                tipDialog.show();
                                break;
                            }
                            break;
                    }
                }
            } catch (Exception unused) {
            }
        }

        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$8$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements TipDialog.TipDialogListener {
            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_02() {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_01() {
                GoodsImagesHelper.this.exitApp(GoodsImagesHelper.this.context, 1000L);
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void timeEnd() {
                GoodsImagesHelper.this.exitApp(GoodsImagesHelper.this.context, 1000L);
            }
        }
    };

    /* loaded from: classes2.dex */
    public static class DownloadImageData {
        public String id;
        public String image;
        public String saveName;
        public String savePath;
        public String url;
    }

    public GoodsImagesHelper(Context context) {
        this.context = context;
    }

    public void downLoadGoodsImages(boolean z, String str, boolean z2) {
        String goodsInfoUrl = NetAddress.getGoodsInfoUrl();
        RequestParams requestParams = new RequestParams();
        if (TextUtils.isEmpty(str)) {
            if (z) {
                ToastUitl.showLong(this.context, R.string.machine_code_null_tip);
                return;
            }
            return;
        }
        requestParams.put("machineCode", str);
        requestParams.put("queryType", 0);
        RequestItem requestItem = new RequestItem(goodsInfoUrl, requestParams, HttpGet.METHOD_NAME);
        requestItem.setRepeatDelay(5000);
        requestItem.setRequestMaxCount(1);
        requestItem.setOnRequestResultListener(new AnonymousClass1(z, z2, str));
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ boolean val$alertMessage;
        final /* synthetic */ String val$machineid;
        final /* synthetic */ boolean val$needRestart;

        AnonymousClass1(boolean z, boolean z2, String str) {
            this.val$alertMessage = z;
            this.val$needRestart = z2;
            this.val$machineid = str;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem, int i, String str, Throwable th) {
            if (this.val$alertMessage) {
                ToastUitl.showLong(GoodsImagesHelper.this.context, R.string.lab_downloadgoodsinfoerror);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x005a A[Catch: Exception -> 0x02c2, TRY_ENTER, TryCatch #0 {Exception -> 0x02c2, blocks: (B:3:0x000c, B:5:0x0025, B:7:0x002d, B:8:0x004c, B:9:0x0052, B:12:0x005a, B:14:0x0068, B:19:0x0075, B:22:0x0092, B:24:0x0098, B:25:0x00a3, B:27:0x00d9, B:29:0x00e1, B:30:0x00f8, B:33:0x008a, B:18:0x00fb, B:38:0x0102, B:40:0x0108, B:42:0x0166, B:44:0x0184, B:45:0x0187, B:47:0x018d, B:48:0x0191, B:52:0x019b, B:54:0x01a1, B:57:0x01b1, B:59:0x0225, B:63:0x024c, B:64:0x022c, B:66:0x0230, B:69:0x0268, B:71:0x026e, B:72:0x0277, B:74:0x027d, B:76:0x028d, B:79:0x02a7, B:81:0x02ab, B:83:0x02ba, B:85:0x0250, B:86:0x0256, B:88:0x025c, B:91:0x0040, B:93:0x0045), top: B:2:0x000c }] */
        /* JADX WARN: Removed duplicated region for block: B:37:0x0101  */
        /* JADX WARN: Removed duplicated region for block: B:44:0x0184 A[Catch: Exception -> 0x02c2, TryCatch #0 {Exception -> 0x02c2, blocks: (B:3:0x000c, B:5:0x0025, B:7:0x002d, B:8:0x004c, B:9:0x0052, B:12:0x005a, B:14:0x0068, B:19:0x0075, B:22:0x0092, B:24:0x0098, B:25:0x00a3, B:27:0x00d9, B:29:0x00e1, B:30:0x00f8, B:33:0x008a, B:18:0x00fb, B:38:0x0102, B:40:0x0108, B:42:0x0166, B:44:0x0184, B:45:0x0187, B:47:0x018d, B:48:0x0191, B:52:0x019b, B:54:0x01a1, B:57:0x01b1, B:59:0x0225, B:63:0x024c, B:64:0x022c, B:66:0x0230, B:69:0x0268, B:71:0x026e, B:72:0x0277, B:74:0x027d, B:76:0x028d, B:79:0x02a7, B:81:0x02ab, B:83:0x02ba, B:85:0x0250, B:86:0x0256, B:88:0x025c, B:91:0x0040, B:93:0x0045), top: B:2:0x000c }] */
        /* JADX WARN: Removed duplicated region for block: B:47:0x018d A[Catch: Exception -> 0x02c2, TryCatch #0 {Exception -> 0x02c2, blocks: (B:3:0x000c, B:5:0x0025, B:7:0x002d, B:8:0x004c, B:9:0x0052, B:12:0x005a, B:14:0x0068, B:19:0x0075, B:22:0x0092, B:24:0x0098, B:25:0x00a3, B:27:0x00d9, B:29:0x00e1, B:30:0x00f8, B:33:0x008a, B:18:0x00fb, B:38:0x0102, B:40:0x0108, B:42:0x0166, B:44:0x0184, B:45:0x0187, B:47:0x018d, B:48:0x0191, B:52:0x019b, B:54:0x01a1, B:57:0x01b1, B:59:0x0225, B:63:0x024c, B:64:0x022c, B:66:0x0230, B:69:0x0268, B:71:0x026e, B:72:0x0277, B:74:0x027d, B:76:0x028d, B:79:0x02a7, B:81:0x02ab, B:83:0x02ba, B:85:0x0250, B:86:0x0256, B:88:0x025c, B:91:0x0040, B:93:0x0045), top: B:2:0x000c }] */
        /* JADX WARN: Removed duplicated region for block: B:51:0x019a  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x026e A[Catch: Exception -> 0x02c2, TryCatch #0 {Exception -> 0x02c2, blocks: (B:3:0x000c, B:5:0x0025, B:7:0x002d, B:8:0x004c, B:9:0x0052, B:12:0x005a, B:14:0x0068, B:19:0x0075, B:22:0x0092, B:24:0x0098, B:25:0x00a3, B:27:0x00d9, B:29:0x00e1, B:30:0x00f8, B:33:0x008a, B:18:0x00fb, B:38:0x0102, B:40:0x0108, B:42:0x0166, B:44:0x0184, B:45:0x0187, B:47:0x018d, B:48:0x0191, B:52:0x019b, B:54:0x01a1, B:57:0x01b1, B:59:0x0225, B:63:0x024c, B:64:0x022c, B:66:0x0230, B:69:0x0268, B:71:0x026e, B:72:0x0277, B:74:0x027d, B:76:0x028d, B:79:0x02a7, B:81:0x02ab, B:83:0x02ba, B:85:0x0250, B:86:0x0256, B:88:0x025c, B:91:0x0040, B:93:0x0045), top: B:2:0x000c }] */
        /* JADX WARN: Removed duplicated region for block: B:79:0x02a7 A[Catch: Exception -> 0x02c2, TryCatch #0 {Exception -> 0x02c2, blocks: (B:3:0x000c, B:5:0x0025, B:7:0x002d, B:8:0x004c, B:9:0x0052, B:12:0x005a, B:14:0x0068, B:19:0x0075, B:22:0x0092, B:24:0x0098, B:25:0x00a3, B:27:0x00d9, B:29:0x00e1, B:30:0x00f8, B:33:0x008a, B:18:0x00fb, B:38:0x0102, B:40:0x0108, B:42:0x0166, B:44:0x0184, B:45:0x0187, B:47:0x018d, B:48:0x0191, B:52:0x019b, B:54:0x01a1, B:57:0x01b1, B:59:0x0225, B:63:0x024c, B:64:0x022c, B:66:0x0230, B:69:0x0268, B:71:0x026e, B:72:0x0277, B:74:0x027d, B:76:0x028d, B:79:0x02a7, B:81:0x02ab, B:83:0x02ba, B:85:0x0250, B:86:0x0256, B:88:0x025c, B:91:0x0040, B:93:0x0045), top: B:2:0x000c }] */
        /* JADX WARN: Removed duplicated region for block: B:85:0x0250 A[Catch: Exception -> 0x02c2, TryCatch #0 {Exception -> 0x02c2, blocks: (B:3:0x000c, B:5:0x0025, B:7:0x002d, B:8:0x004c, B:9:0x0052, B:12:0x005a, B:14:0x0068, B:19:0x0075, B:22:0x0092, B:24:0x0098, B:25:0x00a3, B:27:0x00d9, B:29:0x00e1, B:30:0x00f8, B:33:0x008a, B:18:0x00fb, B:38:0x0102, B:40:0x0108, B:42:0x0166, B:44:0x0184, B:45:0x0187, B:47:0x018d, B:48:0x0191, B:52:0x019b, B:54:0x01a1, B:57:0x01b1, B:59:0x0225, B:63:0x024c, B:64:0x022c, B:66:0x0230, B:69:0x0268, B:71:0x026e, B:72:0x0277, B:74:0x027d, B:76:0x028d, B:79:0x02a7, B:81:0x02ab, B:83:0x02ba, B:85:0x0250, B:86:0x0256, B:88:0x025c, B:91:0x0040, B:93:0x0045), top: B:2:0x000c }] */
        /* JADX WARN: Removed duplicated region for block: B:90:0x0190  */
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean onSuccess(com.oysb.utils.http.RequestItem r17, int r18, java.lang.String r19) {
            /*
                Method dump skipped, instructions count: 721
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.shj.setting.helper.GoodsImagesHelper.AnonymousClass1.onSuccess(com.oysb.utils.http.RequestItem, int, java.lang.String):boolean");
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$1$1 */
        /* loaded from: classes2.dex */
        public class RunnableC00671 implements Runnable {
            RunnableC00671() {
            }

            @Override // java.lang.Runnable
            public void run() {
                Resources resources = GoodsImagesHelper.this.context.getResources();
                TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, resources.getString(R.string.lab_noneedupdateimages), resources.getString(R.string.lab_redownload), resources.getString(R.string.button_cancel));
                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.1.1.1
                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    C00681() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                        try {
                            File file = new File(SDFileUtils.SDCardRoot + "xyShj/goodsImagesInfo.dat");
                            file.delete();
                            Loger.writeLog("APP", "delete file at downLoadGoodsImages:" + file.getAbsolutePath());
                        } catch (Exception unused) {
                        }
                        GoodsImagesHelper.this.downLoadGoodsImages(true, AnonymousClass1.this.val$machineid, AnonymousClass1.this.val$needRestart);
                    }
                });
                tipDialog.show();
            }

            /* renamed from: com.shj.setting.helper.GoodsImagesHelper$1$1$1 */
            /* loaded from: classes2.dex */
            class C00681 implements TipDialog.TipDialogListener {
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                C00681() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    try {
                        File file = new File(SDFileUtils.SDCardRoot + "xyShj/goodsImagesInfo.dat");
                        file.delete();
                        Loger.writeLog("APP", "delete file at downLoadGoodsImages:" + file.getAbsolutePath());
                    } catch (Exception unused) {
                    }
                    GoodsImagesHelper.this.downLoadGoodsImages(true, AnonymousClass1.this.val$machineid, AnonymousClass1.this.val$needRestart);
                }
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem, boolean z) {
            if (z) {
                return;
            }
            ToastUitl.showLong(GoodsImagesHelper.this.context, R.string.lab_neterror);
        }
    }

    public void downLoadGoodsSmImages(boolean z, String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                if (z) {
                    ToastUitl.showLong(this.context, R.string.machine_code_null_tip);
                    return;
                }
                return;
            }
            String sinoKangGoodsQueryUrl = NetAddress.getSinoKangGoodsQueryUrl();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("machineCode", str);
            jSONObject.put("queryType", 0);
            Loger.writeLog("SALES", "查询商品分类参数:" + jSONObject.toString());
            RequestItem requestItem = new RequestItem(sinoKangGoodsQueryUrl, jSONObject, "POST");
            requestItem.setRepeatDelay(3000);
            requestItem.setRequestMaxCount(3);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.2
                final /* synthetic */ boolean val$alertMessage;
                final /* synthetic */ String val$machineid;

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z2) {
                }

                AnonymousClass2(boolean z2, String str2) {
                    z = z2;
                    str = str2;
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
                    Loger.writeLog("SALES", "查询商品分类失败:" + i + StringUtils.SPACE + str2);
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
                    JSONArray jSONArray;
                    try {
                        if (GoodsImagesHelper.this.smImgitems != null) {
                            GoodsImagesHelper.this.smImgitems.clear();
                        }
                        Loger.writeLog("SALES", "查询商品分类:" + str2);
                        JSONObject jSONObject2 = new JSONObject(str2);
                        if (!jSONObject2.getString("code").equals("H0000")) {
                            return true;
                        }
                        if (jSONObject2.get(SpeechEvent.KEY_EVENT_RECORD_DATA) instanceof JSONObject) {
                            jSONArray = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        } else {
                            jSONArray = jSONObject2.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        }
                        if (jSONArray == null || jSONArray.length() <= 0) {
                            GoodsImagesHelper.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.shj.setting.helper.GoodsImagesHelper.2.1
                                AnonymousClass1() {
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                                    tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.2.1.1
                                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                        public void buttonClick_01() {
                                        }

                                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                        public void buttonClick_02() {
                                        }

                                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                        public void timeEnd() {
                                        }

                                        C00691() {
                                        }
                                    });
                                    tipDialog.show();
                                }

                                /* renamed from: com.shj.setting.helper.GoodsImagesHelper$2$1$1 */
                                /* loaded from: classes2.dex */
                                class C00691 implements TipDialog.TipDialogListener {
                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_01() {
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_02() {
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void timeEnd() {
                                    }

                                    C00691() {
                                    }
                                }
                            });
                            return true;
                        }
                        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                            JSONArray jSONArray2 = ((JSONObject) jSONArray.get(i2)).getJSONArray("classGoods");
                            if (jSONArray2 != null) {
                                for (int i3 = 0; i3 < jSONArray2.length(); i3++) {
                                    JSONObject jSONObject3 = (JSONObject) jSONArray2.get(i3);
                                    if (!jSONObject3.isNull("smsimg")) {
                                        String[] split = jSONObject3.getString("smsimg").split(",");
                                        ArrayList arrayList = new ArrayList();
                                        for (String str3 : split) {
                                            arrayList.add(str3);
                                        }
                                        if (arrayList.size() > 0) {
                                            GoodsImagesHelper.this.smImgitems.put(Integer.valueOf(Integer.parseInt(jSONObject3.getString("goodsCode"))), arrayList);
                                        }
                                    }
                                }
                            }
                        }
                        GoodsImagesHelper goodsImagesHelper = GoodsImagesHelper.this;
                        goodsImagesHelper.downLoadGoodsImagesToZk(z, str, goodsImagesHelper.smImgitems);
                        return true;
                    } catch (Exception e) {
                        Loger.writeException("SALES", e);
                        return false;
                    }
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: com.shj.setting.helper.GoodsImagesHelper$2$1 */
                /* loaded from: classes2.dex */
                public class AnonymousClass1 implements Runnable {
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                        tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.2.1.1
                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_01() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_02() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void timeEnd() {
                            }

                            C00691() {
                            }
                        });
                        tipDialog.show();
                    }

                    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$2$1$1 */
                    /* loaded from: classes2.dex */
                    class C00691 implements TipDialog.TipDialogListener {
                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_01() {
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_02() {
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void timeEnd() {
                        }

                        C00691() {
                        }
                    }
                }
            });
            RequestHelper.request(requestItem);
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ boolean val$alertMessage;
        final /* synthetic */ String val$machineid;

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z2) {
        }

        AnonymousClass2(boolean z2, String str2) {
            z = z2;
            str = str2;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
            Loger.writeLog("SALES", "查询商品分类失败:" + i + StringUtils.SPACE + str2);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
            JSONArray jSONArray;
            try {
                if (GoodsImagesHelper.this.smImgitems != null) {
                    GoodsImagesHelper.this.smImgitems.clear();
                }
                Loger.writeLog("SALES", "查询商品分类:" + str2);
                JSONObject jSONObject2 = new JSONObject(str2);
                if (!jSONObject2.getString("code").equals("H0000")) {
                    return true;
                }
                if (jSONObject2.get(SpeechEvent.KEY_EVENT_RECORD_DATA) instanceof JSONObject) {
                    jSONArray = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                } else {
                    jSONArray = jSONObject2.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                }
                if (jSONArray == null || jSONArray.length() <= 0) {
                    GoodsImagesHelper.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.shj.setting.helper.GoodsImagesHelper.2.1
                        AnonymousClass1() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                            tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.2.1.1
                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_01() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_02() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void timeEnd() {
                                }

                                C00691() {
                                }
                            });
                            tipDialog.show();
                        }

                        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$2$1$1 */
                        /* loaded from: classes2.dex */
                        class C00691 implements TipDialog.TipDialogListener {
                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_01() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_02() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void timeEnd() {
                            }

                            C00691() {
                            }
                        }
                    });
                    return true;
                }
                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                    JSONArray jSONArray2 = ((JSONObject) jSONArray.get(i2)).getJSONArray("classGoods");
                    if (jSONArray2 != null) {
                        for (int i3 = 0; i3 < jSONArray2.length(); i3++) {
                            JSONObject jSONObject3 = (JSONObject) jSONArray2.get(i3);
                            if (!jSONObject3.isNull("smsimg")) {
                                String[] split = jSONObject3.getString("smsimg").split(",");
                                ArrayList arrayList = new ArrayList();
                                for (String str3 : split) {
                                    arrayList.add(str3);
                                }
                                if (arrayList.size() > 0) {
                                    GoodsImagesHelper.this.smImgitems.put(Integer.valueOf(Integer.parseInt(jSONObject3.getString("goodsCode"))), arrayList);
                                }
                            }
                        }
                    }
                }
                GoodsImagesHelper goodsImagesHelper = GoodsImagesHelper.this;
                goodsImagesHelper.downLoadGoodsImagesToZk(z, str, goodsImagesHelper.smImgitems);
                return true;
            } catch (Exception e) {
                Loger.writeException("SALES", e);
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$2$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.2.1.1
                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    C00691() {
                    }
                });
                tipDialog.show();
            }

            /* renamed from: com.shj.setting.helper.GoodsImagesHelper$2$1$1 */
            /* loaded from: classes2.dex */
            class C00691 implements TipDialog.TipDialogListener {
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                C00691() {
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0281 A[Catch: Exception -> 0x01cc, TryCatch #2 {Exception -> 0x01cc, blocks: (B:63:0x0023, B:65:0x0029, B:67:0x002f, B:68:0x0037, B:70:0x003d, B:73:0x0053, B:75:0x007e, B:76:0x00a0, B:79:0x00a8, B:80:0x00af, B:82:0x00b5, B:84:0x0133, B:88:0x0155, B:89:0x013a, B:96:0x016a, B:97:0x018a, B:99:0x0190, B:101:0x0082, B:103:0x008a, B:105:0x0098, B:107:0x009b, B:9:0x027b, B:12:0x0281, B:14:0x0287, B:15:0x0293, B:6:0x01d1, B:32:0x01e0, B:33:0x01e8, B:35:0x01ee, B:38:0x0203, B:40:0x022e, B:42:0x024d, B:44:0x0253, B:49:0x0232, B:51:0x023a, B:53:0x0246, B:55:0x0249), top: B:62:0x0023 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x02cd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void downLoadGoodsImagesToZk(boolean r23, java.lang.String r24, java.util.Map<java.lang.Integer, java.util.List<java.lang.String>> r25) {
        /*
            Method dump skipped, instructions count: 747
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.setting.helper.GoodsImagesHelper.downLoadGoodsImagesToZk(boolean, java.lang.String, java.util.Map):void");
    }

    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Runnable {
        final /* synthetic */ String val$machineid;

        AnonymousClass3(String str) {
            r2 = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            Resources resources = GoodsImagesHelper.this.context.getResources();
            TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, resources.getString(R.string.lab_noneedupdateimages), resources.getString(R.string.lab_redownload), resources.getString(R.string.button_cancel));
            tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.3.1
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    try {
                        CacheHelper.getFileCache().put("goodsSmImagesInfo", new HashMap());
                        Loger.writeLog("APP", "delete file at goodsSmImagesInfo");
                    } catch (Exception unused) {
                    }
                    GoodsImagesHelper.this.downLoadGoodsSmImages(true, r2);
                }
            });
            tipDialog.show();
        }

        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$3$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements TipDialog.TipDialogListener {
            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_02() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void timeEnd() {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_01() {
                try {
                    CacheHelper.getFileCache().put("goodsSmImagesInfo", new HashMap());
                    Loger.writeLog("APP", "delete file at goodsSmImagesInfo");
                } catch (Exception unused) {
                }
                GoodsImagesHelper.this.downLoadGoodsSmImages(true, r2);
            }
        }
    }

    public void downLoadGoodsImage(String str, String str2) {
        Loger.writeLog("UI", "download image for spbh:" + str2);
        String goodsInfoUrl = NetAddress.getGoodsInfoUrl();
        RequestParams requestParams = new RequestParams();
        requestParams.put("machineCode", str);
        requestParams.put("spbh", str2);
        requestParams.put("queryType", 0);
        RequestItem requestItem = new RequestItem(goodsInfoUrl, requestParams, HttpGet.METHOD_NAME);
        requestItem.setRepeatDelay(5000);
        requestItem.setRequestMaxCount(1);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.4
            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str3, Throwable th) {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            AnonymousClass4() {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str3) {
                JSONArray jSONArray;
                try {
                    JSONObject jSONObject = new JSONObject(str3);
                    Loger.writeLog("UI", "imageInfo:" + jSONObject.toString());
                    if (jSONObject.getString("code").equalsIgnoreCase("H0000")) {
                        jSONArray = jSONObject.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        new JSONObject().put("goods", jSONArray);
                    } else {
                        jSONArray = jSONObject.getJSONArray("goods");
                    }
                    SDFileUtils.creatDataDir(SDFileUtils.SDCardRoot + "xyShj/images");
                    ArrayList arrayList = new ArrayList();
                    for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                        if (jSONObject2.has("image")) {
                            arrayList.add(jSONObject2);
                        }
                    }
                    Loger.writeLog("UI", "to download image count:" + arrayList.size());
                    if (arrayList.size() <= 0) {
                        GoodsImagesHelper.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.shj.setting.helper.GoodsImagesHelper.4.1
                            AnonymousClass1() {
                            }

                            /* renamed from: com.shj.setting.helper.GoodsImagesHelper$4$1$1 */
                            /* loaded from: classes2.dex */
                            class C00701 implements TipDialog.TipDialogListener {
                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_01() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_02() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void timeEnd() {
                                }

                                C00701() {
                                }
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, "暂无商品图片", "确 定", "");
                                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.4.1.1
                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_01() {
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_02() {
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void timeEnd() {
                                    }

                                    C00701() {
                                    }
                                });
                                tipDialog.show();
                            }
                        });
                        return true;
                    }
                    SaveImageRunnable saveImageRunnable = new SaveImageRunnable(GoodsImagesHelper.this, null);
                    saveImageRunnable.items = GoodsImagesHelper.this.changeData(arrayList);
                    saveImageRunnable.restart = false;
                    new Thread(saveImageRunnable).start();
                    return true;
                } catch (Exception e) {
                    Loger.writeException("UI", e);
                    return true;
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: com.shj.setting.helper.GoodsImagesHelper$4$1 */
            /* loaded from: classes2.dex */
            public class AnonymousClass1 implements Runnable {
                AnonymousClass1() {
                }

                /* renamed from: com.shj.setting.helper.GoodsImagesHelper$4$1$1 */
                /* loaded from: classes2.dex */
                class C00701 implements TipDialog.TipDialogListener {
                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    C00701() {
                    }
                }

                @Override // java.lang.Runnable
                public void run() {
                    TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, "暂无商品图片", "确 定", "");
                    tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.4.1.1
                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_01() {
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_02() {
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void timeEnd() {
                        }

                        C00701() {
                        }
                    });
                    tipDialog.show();
                }
            }
        });
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str3, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass4() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str3) {
            JSONArray jSONArray;
            try {
                JSONObject jSONObject = new JSONObject(str3);
                Loger.writeLog("UI", "imageInfo:" + jSONObject.toString());
                if (jSONObject.getString("code").equalsIgnoreCase("H0000")) {
                    jSONArray = jSONObject.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    new JSONObject().put("goods", jSONArray);
                } else {
                    jSONArray = jSONObject.getJSONArray("goods");
                }
                SDFileUtils.creatDataDir(SDFileUtils.SDCardRoot + "xyShj/images");
                ArrayList arrayList = new ArrayList();
                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                    if (jSONObject2.has("image")) {
                        arrayList.add(jSONObject2);
                    }
                }
                Loger.writeLog("UI", "to download image count:" + arrayList.size());
                if (arrayList.size() <= 0) {
                    GoodsImagesHelper.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.shj.setting.helper.GoodsImagesHelper.4.1
                        AnonymousClass1() {
                        }

                        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$4$1$1 */
                        /* loaded from: classes2.dex */
                        class C00701 implements TipDialog.TipDialogListener {
                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_01() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_02() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void timeEnd() {
                            }

                            C00701() {
                            }
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, "暂无商品图片", "确 定", "");
                            tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.4.1.1
                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_01() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_02() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void timeEnd() {
                                }

                                C00701() {
                                }
                            });
                            tipDialog.show();
                        }
                    });
                    return true;
                }
                SaveImageRunnable saveImageRunnable = new SaveImageRunnable(GoodsImagesHelper.this, null);
                saveImageRunnable.items = GoodsImagesHelper.this.changeData(arrayList);
                saveImageRunnable.restart = false;
                new Thread(saveImageRunnable).start();
                return true;
            } catch (Exception e) {
                Loger.writeException("UI", e);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$4$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            /* renamed from: com.shj.setting.helper.GoodsImagesHelper$4$1$1 */
            /* loaded from: classes2.dex */
            class C00701 implements TipDialog.TipDialogListener {
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                C00701() {
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, "暂无商品图片", "确 定", "");
                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.4.1.1
                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    C00701() {
                    }
                });
                tipDialog.show();
            }
        }
    }

    public void requestGoodsXqImage(boolean z, String str, String str2) {
        try {
            if (TextUtils.isEmpty(str)) {
                if (z) {
                    ToastUitl.showLong(this.context, R.string.machine_code_null_tip);
                    return;
                }
                return;
            }
            String str3 = NetAddress.getAppBaseUrl() + "/service-app/shjty/getGoodsXqt";
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("machineCode", str);
            if (!TextUtils.isEmpty(str2)) {
                jSONObject.put("spbh", str2);
            }
            Loger.writeLog("SALES", "查询商品详情图参数:" + jSONObject.toString());
            RequestItem requestItem = new RequestItem(str3, jSONObject, "POST");
            requestItem.setRepeatDelay(3000);
            requestItem.setRequestMaxCount(3);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.5
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z2) {
                }

                AnonymousClass5() {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str4, Throwable th) {
                    Loger.writeLog("SALES", "查询商品详情图失败:" + i + StringUtils.SPACE + str4);
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str4) {
                    JSONArray jSONArray;
                    try {
                        Loger.writeLog("SALES", "查询商品详情图:" + str4);
                        JSONObject jSONObject2 = new JSONObject(str4);
                        if (jSONObject2.getString("code").equals("H0000")) {
                            JSONArray jSONArray2 = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                            if (jSONArray2 == null || jSONArray2.length() <= 0) {
                                GoodsImagesHelper.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.shj.setting.helper.GoodsImagesHelper.5.1
                                    AnonymousClass1() {
                                    }

                                    @Override // java.lang.Runnable
                                    public void run() {
                                        TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                                        tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.5.1.1
                                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                            public void buttonClick_01() {
                                            }

                                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                            public void buttonClick_02() {
                                            }

                                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                            public void timeEnd() {
                                            }

                                            C00711() {
                                            }
                                        });
                                        tipDialog.show();
                                    }

                                    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$5$1$1 */
                                    /* loaded from: classes2.dex */
                                    class C00711 implements TipDialog.TipDialogListener {
                                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                        public void buttonClick_01() {
                                        }

                                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                        public void buttonClick_02() {
                                        }

                                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                        public void timeEnd() {
                                        }

                                        C00711() {
                                        }
                                    }
                                });
                            } else {
                                JSONObject jSONObject3 = (JSONObject) jSONArray2.get(0);
                                if (!jSONObject3.isNull("xqt") && (jSONArray = jSONObject3.getJSONArray("xqt")) != null) {
                                    ArrayList arrayList = new ArrayList();
                                    for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                                        arrayList.add(jSONArray.getString(i2));
                                    }
                                    if (arrayList.size() <= 0) {
                                        return true;
                                    }
                                    ArrayList arrayList2 = new ArrayList();
                                    int parseInt = Integer.parseInt(jSONObject3.getString("code"));
                                    File file = new File(SDFileUtils.SDCardRoot + "xyShj/images/" + String.format("%04d", Integer.valueOf(parseInt)));
                                    if (!file.exists()) {
                                        file.mkdirs();
                                    } else {
                                        for (File file2 : file.listFiles()) {
                                            if (!arrayList.contains(file2.getName())) {
                                                file2.delete();
                                            }
                                        }
                                    }
                                    for (int i3 = 0; i3 < arrayList.size(); i3++) {
                                        String str5 = (String) arrayList.get(i3);
                                        JSONObject jSONObject4 = new JSONObject();
                                        jSONObject4.put("image", str5);
                                        jSONObject4.put("code", str5);
                                        jSONObject4.put("path", String.format("%04d", Integer.valueOf(parseInt)));
                                        arrayList2.add(jSONObject4);
                                    }
                                    if (arrayList2.size() > 0) {
                                        TTSManager.clear();
                                        TTSManager.addText("准备下载商品图片共" + arrayList2.size() + "张图片");
                                        SaveImageRunnable saveImageRunnable = new SaveImageRunnable(GoodsImagesHelper.this, null);
                                        saveImageRunnable.items = GoodsImagesHelper.this.changeData(arrayList2);
                                        saveImageRunnable.restart = false;
                                        new Thread(saveImageRunnable).start();
                                    }
                                }
                            }
                        }
                        return true;
                    } catch (Exception e) {
                        Loger.writeException("SALES", e);
                        return false;
                    }
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: com.shj.setting.helper.GoodsImagesHelper$5$1 */
                /* loaded from: classes2.dex */
                public class AnonymousClass1 implements Runnable {
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                        tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.5.1.1
                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_01() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_02() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void timeEnd() {
                            }

                            C00711() {
                            }
                        });
                        tipDialog.show();
                    }

                    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$5$1$1 */
                    /* loaded from: classes2.dex */
                    class C00711 implements TipDialog.TipDialogListener {
                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_01() {
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_02() {
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void timeEnd() {
                        }

                        C00711() {
                        }
                    }
                }
            });
            RequestHelper.request(requestItem);
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z2) {
        }

        AnonymousClass5() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str4, Throwable th) {
            Loger.writeLog("SALES", "查询商品详情图失败:" + i + StringUtils.SPACE + str4);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str4) {
            JSONArray jSONArray;
            try {
                Loger.writeLog("SALES", "查询商品详情图:" + str4);
                JSONObject jSONObject2 = new JSONObject(str4);
                if (jSONObject2.getString("code").equals("H0000")) {
                    JSONArray jSONArray2 = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    if (jSONArray2 == null || jSONArray2.length() <= 0) {
                        GoodsImagesHelper.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.shj.setting.helper.GoodsImagesHelper.5.1
                            AnonymousClass1() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.5.1.1
                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_01() {
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_02() {
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void timeEnd() {
                                    }

                                    C00711() {
                                    }
                                });
                                tipDialog.show();
                            }

                            /* renamed from: com.shj.setting.helper.GoodsImagesHelper$5$1$1 */
                            /* loaded from: classes2.dex */
                            class C00711 implements TipDialog.TipDialogListener {
                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_01() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_02() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void timeEnd() {
                                }

                                C00711() {
                                }
                            }
                        });
                    } else {
                        JSONObject jSONObject3 = (JSONObject) jSONArray2.get(0);
                        if (!jSONObject3.isNull("xqt") && (jSONArray = jSONObject3.getJSONArray("xqt")) != null) {
                            ArrayList arrayList = new ArrayList();
                            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                                arrayList.add(jSONArray.getString(i2));
                            }
                            if (arrayList.size() <= 0) {
                                return true;
                            }
                            ArrayList arrayList2 = new ArrayList();
                            int parseInt = Integer.parseInt(jSONObject3.getString("code"));
                            File file = new File(SDFileUtils.SDCardRoot + "xyShj/images/" + String.format("%04d", Integer.valueOf(parseInt)));
                            if (!file.exists()) {
                                file.mkdirs();
                            } else {
                                for (File file2 : file.listFiles()) {
                                    if (!arrayList.contains(file2.getName())) {
                                        file2.delete();
                                    }
                                }
                            }
                            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                                String str5 = (String) arrayList.get(i3);
                                JSONObject jSONObject4 = new JSONObject();
                                jSONObject4.put("image", str5);
                                jSONObject4.put("code", str5);
                                jSONObject4.put("path", String.format("%04d", Integer.valueOf(parseInt)));
                                arrayList2.add(jSONObject4);
                            }
                            if (arrayList2.size() > 0) {
                                TTSManager.clear();
                                TTSManager.addText("准备下载商品图片共" + arrayList2.size() + "张图片");
                                SaveImageRunnable saveImageRunnable = new SaveImageRunnable(GoodsImagesHelper.this, null);
                                saveImageRunnable.items = GoodsImagesHelper.this.changeData(arrayList2);
                                saveImageRunnable.restart = false;
                                new Thread(saveImageRunnable).start();
                            }
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                Loger.writeException("SALES", e);
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$5$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.5.1.1
                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    C00711() {
                    }
                });
                tipDialog.show();
            }

            /* renamed from: com.shj.setting.helper.GoodsImagesHelper$5$1$1 */
            /* loaded from: classes2.dex */
            class C00711 implements TipDialog.TipDialogListener {
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                C00711() {
                }
            }
        }
    }

    public void requestGoodsXqImages(boolean z, String str, String str2) {
        try {
            if (TextUtils.isEmpty(str)) {
                if (z) {
                    ToastUitl.showLong(this.context, R.string.machine_code_null_tip);
                    return;
                }
                return;
            }
            String str3 = NetAddress.getAppBaseUrl() + "/service-app/shjty/getGoodsXqt";
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("machineCode", str);
            if (!TextUtils.isEmpty(str2)) {
                jSONObject.put("spbh", str2);
            }
            Loger.writeLog("SALES", "查询商品详情图参数:" + jSONObject.toString());
            RequestItem requestItem = new RequestItem(str3, jSONObject, "POST");
            requestItem.setRepeatDelay(3000);
            requestItem.setRequestMaxCount(3);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.6
                final /* synthetic */ boolean val$alertMessage;
                final /* synthetic */ String val$machineid;

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z2) {
                }

                AnonymousClass6(boolean z2, String str4) {
                    z = z2;
                    str = str4;
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str4, Throwable th) {
                    Loger.writeLog("SALES", "查询商品详情图失败:" + i + StringUtils.SPACE + str4);
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str4) {
                    JSONArray jSONArray;
                    JSONArray jSONArray2;
                    try {
                        if (GoodsImagesHelper.this.xqImgitems != null) {
                            GoodsImagesHelper.this.xqImgitems.clear();
                        }
                        Loger.writeLog("SALES", "查询商品详情图:" + str4);
                        JSONObject jSONObject2 = new JSONObject(str4);
                        if (!jSONObject2.getString("code").equals("H0000")) {
                            return true;
                        }
                        if (jSONObject2.get(SpeechEvent.KEY_EVENT_RECORD_DATA) instanceof JSONObject) {
                            jSONArray = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        } else {
                            jSONArray = jSONObject2.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        }
                        if (jSONArray == null || jSONArray.length() <= 0) {
                            GoodsImagesHelper.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.shj.setting.helper.GoodsImagesHelper.6.1
                                AnonymousClass1() {
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                                    tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.6.1.1
                                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                        public void buttonClick_01() {
                                        }

                                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                        public void buttonClick_02() {
                                        }

                                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                        public void timeEnd() {
                                        }

                                        C00721() {
                                        }
                                    });
                                    tipDialog.show();
                                }

                                /* renamed from: com.shj.setting.helper.GoodsImagesHelper$6$1$1 */
                                /* loaded from: classes2.dex */
                                class C00721 implements TipDialog.TipDialogListener {
                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_01() {
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_02() {
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void timeEnd() {
                                    }

                                    C00721() {
                                    }
                                }
                            });
                            return true;
                        }
                        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                            JSONObject jSONObject3 = (JSONObject) jSONArray.get(i2);
                            if (!jSONObject3.isNull("xqt") && (jSONArray2 = jSONObject3.getJSONArray("xqt")) != null) {
                                ArrayList arrayList = new ArrayList();
                                for (int i3 = 0; i3 < jSONArray2.length(); i3++) {
                                    arrayList.add(jSONArray2.getString(i3));
                                }
                                if (arrayList.size() > 0) {
                                    GoodsImagesHelper.this.xqImgitems.put(Integer.valueOf(Integer.parseInt(jSONObject3.getString("code"))), arrayList);
                                }
                            }
                        }
                        if (GoodsImagesHelper.this.xqImgitems.size() <= 0) {
                            return true;
                        }
                        GoodsImagesHelper goodsImagesHelper = GoodsImagesHelper.this;
                        goodsImagesHelper.downLoadGoodsXQImages(z, str, goodsImagesHelper.xqImgitems);
                        return true;
                    } catch (Exception e) {
                        Loger.writeException("SALES", e);
                        return false;
                    }
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: com.shj.setting.helper.GoodsImagesHelper$6$1 */
                /* loaded from: classes2.dex */
                public class AnonymousClass1 implements Runnable {
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                        tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.6.1.1
                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_01() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_02() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void timeEnd() {
                            }

                            C00721() {
                            }
                        });
                        tipDialog.show();
                    }

                    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$6$1$1 */
                    /* loaded from: classes2.dex */
                    class C00721 implements TipDialog.TipDialogListener {
                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_01() {
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void buttonClick_02() {
                        }

                        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                        public void timeEnd() {
                        }

                        C00721() {
                        }
                    }
                }
            });
            RequestHelper.request(requestItem);
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ boolean val$alertMessage;
        final /* synthetic */ String val$machineid;

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z2) {
        }

        AnonymousClass6(boolean z2, String str4) {
            z = z2;
            str = str4;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str4, Throwable th) {
            Loger.writeLog("SALES", "查询商品详情图失败:" + i + StringUtils.SPACE + str4);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str4) {
            JSONArray jSONArray;
            JSONArray jSONArray2;
            try {
                if (GoodsImagesHelper.this.xqImgitems != null) {
                    GoodsImagesHelper.this.xqImgitems.clear();
                }
                Loger.writeLog("SALES", "查询商品详情图:" + str4);
                JSONObject jSONObject2 = new JSONObject(str4);
                if (!jSONObject2.getString("code").equals("H0000")) {
                    return true;
                }
                if (jSONObject2.get(SpeechEvent.KEY_EVENT_RECORD_DATA) instanceof JSONObject) {
                    jSONArray = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                } else {
                    jSONArray = jSONObject2.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                }
                if (jSONArray == null || jSONArray.length() <= 0) {
                    GoodsImagesHelper.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.shj.setting.helper.GoodsImagesHelper.6.1
                        AnonymousClass1() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                            tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.6.1.1
                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_01() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_02() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void timeEnd() {
                                }

                                C00721() {
                                }
                            });
                            tipDialog.show();
                        }

                        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$6$1$1 */
                        /* loaded from: classes2.dex */
                        class C00721 implements TipDialog.TipDialogListener {
                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_01() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_02() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void timeEnd() {
                            }

                            C00721() {
                            }
                        }
                    });
                    return true;
                }
                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                    JSONObject jSONObject3 = (JSONObject) jSONArray.get(i2);
                    if (!jSONObject3.isNull("xqt") && (jSONArray2 = jSONObject3.getJSONArray("xqt")) != null) {
                        ArrayList arrayList = new ArrayList();
                        for (int i3 = 0; i3 < jSONArray2.length(); i3++) {
                            arrayList.add(jSONArray2.getString(i3));
                        }
                        if (arrayList.size() > 0) {
                            GoodsImagesHelper.this.xqImgitems.put(Integer.valueOf(Integer.parseInt(jSONObject3.getString("code"))), arrayList);
                        }
                    }
                }
                if (GoodsImagesHelper.this.xqImgitems.size() <= 0) {
                    return true;
                }
                GoodsImagesHelper goodsImagesHelper = GoodsImagesHelper.this;
                goodsImagesHelper.downLoadGoodsXQImages(z, str, goodsImagesHelper.xqImgitems);
                return true;
            } catch (Exception e) {
                Loger.writeException("SALES", e);
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$6$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.nodetailpic), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.6.1.1
                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    C00721() {
                    }
                });
                tipDialog.show();
            }

            /* renamed from: com.shj.setting.helper.GoodsImagesHelper$6$1$1 */
            /* loaded from: classes2.dex */
            class C00721 implements TipDialog.TipDialogListener {
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                C00721() {
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x0229 A[Catch: Exception -> 0x02a3, TryCatch #1 {Exception -> 0x02a3, blocks: (B:67:0x0023, B:69:0x0029, B:71:0x002f, B:72:0x0037, B:74:0x003d, B:77:0x0053, B:79:0x007e, B:80:0x00a0, B:82:0x00a6, B:83:0x00ad, B:85:0x00b3, B:87:0x00f4, B:91:0x0125, B:92:0x00fb, B:100:0x013d, B:102:0x0143, B:104:0x0082, B:106:0x008a, B:108:0x0098, B:110:0x009b, B:38:0x0223, B:41:0x0229, B:43:0x022f, B:44:0x0237, B:46:0x023d, B:48:0x024d, B:49:0x0258, B:6:0x017b, B:8:0x018a, B:9:0x0192, B:11:0x0198, B:14:0x01ab, B:16:0x01d6, B:18:0x01f5, B:20:0x01fb, B:25:0x01da, B:27:0x01e2, B:29:0x01ee, B:31:0x01f1), top: B:66:0x0023 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0292  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void downLoadGoodsXQImages(boolean r22, java.lang.String r23, java.util.Map<java.lang.Integer, java.util.List<java.lang.String>> r24) {
        /*
            Method dump skipped, instructions count: 690
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.setting.helper.GoodsImagesHelper.downLoadGoodsXQImages(boolean, java.lang.String, java.util.Map):void");
    }

    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements Runnable {
        AnonymousClass7() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Resources resources = GoodsImagesHelper.this.context.getResources();
            TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, resources.getString(R.string.lab_noneedupdateimages), resources.getString(R.string.button_ok), "");
            tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.7.1
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                AnonymousClass1() {
                }
            });
            tipDialog.show();
        }

        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$7$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements TipDialog.TipDialogListener {
            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_01() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_02() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void timeEnd() {
            }

            AnonymousClass1() {
            }
        }
    }

    public void showProgressDlg() {
        ProgressDialog progressDialog = new ProgressDialog(this.context);
        this.myProgressDialog = progressDialog;
        progressDialog.getWindow().setGravity(17);
        this.myProgressDialog.setMax(100);
        this.myProgressDialog.setProgressStyle(1);
        this.myProgressDialog.setTitle(this.context.getString(R.string.lab_copyfiles));
        this.myProgressDialog.setCancelable(false);
        this.myProgressDialog.show();
    }

    public void closeProgressDlg() {
        this.myProgressDialog.cancel();
        this.myProgressDialog = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 extends Handler {
        AnonymousClass8() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                int i = message.what;
                if (i == GoodsImagesHelper.MSG_EXIT_APP) {
                    System.exit(0);
                } else {
                    switch (i) {
                        case 3000:
                            Object[] objArr = (Object[]) message.obj;
                            GoodsImagesHelper.this.showProgressDlg();
                            GoodsImagesHelper.this.myProgressDialog.setMax(((Integer) objArr[0]).intValue());
                            GoodsImagesHelper.this.myProgressDialog.setProgress(0);
                            break;
                        case GoodsImagesHelper.PROCES_END /* 3001 */:
                            GoodsImagesHelper.this.myProgressDialog.setMessage(GoodsImagesHelper.this.context.getString(R.string.lab_downloadsuccess));
                            Message obtain = Message.obtain();
                            obtain.what = GoodsImagesHelper.PROCES_CLOSE;
                            obtain.obj = message.obj;
                            GoodsImagesHelper.this.handler.sendMessageDelayed(obtain, 1000L);
                            break;
                        case GoodsImagesHelper.PROCES_TIMEOUT /* 3002 */:
                            GoodsImagesHelper.this.closeProgressDlg();
                            new TipDialog(GoodsImagesHelper.this.context, 3, GoodsImagesHelper.this.context.getString(R.string.lab_downloadimagetimeout), GoodsImagesHelper.this.context.getString(R.string.button_ok), "").show();
                            break;
                        case GoodsImagesHelper.PROCES_CURRENT /* 3003 */:
                            Object[] objArr2 = (Object[]) message.obj;
                            GoodsImagesHelper.this.myProgressDialog.setProgress(((Integer) objArr2[0]).intValue());
                            GoodsImagesHelper.this.myProgressDialog.setMessage(objArr2[1].toString());
                            break;
                        case 3004:
                            GoodsImagesHelper.this.myProgressDialog.setMessage(message.obj.toString());
                            break;
                        case GoodsImagesHelper.PROCES_CLOSE /* 3005 */:
                            BitmapCache.clearCache();
                            EventBus.getDefault().post(new UpdataGoodsInfoUIEvent());
                            GoodsImagesHelper.this.closeProgressDlg();
                            Object[] objArr3 = (Object[]) message.obj;
                            if (!((Boolean) objArr3[0]).booleanValue()) {
                                break;
                            } else if (!((Boolean) objArr3[1]).booleanValue()) {
                                new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.lab_imageupdatefinished), GoodsImagesHelper.this.context.getString(R.string.button_ok), "").show();
                                break;
                            } else if (((Boolean) objArr3[2]).booleanValue()) {
                                TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 10, GoodsImagesHelper.this.context.getString(R.string.lab_imageupdatefinished_restart), GoodsImagesHelper.this.context.getString(R.string.button_ok), "");
                                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.8.1
                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_02() {
                                    }

                                    AnonymousClass1() {
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void buttonClick_01() {
                                        GoodsImagesHelper.this.exitApp(GoodsImagesHelper.this.context, 1000L);
                                    }

                                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                    public void timeEnd() {
                                        GoodsImagesHelper.this.exitApp(GoodsImagesHelper.this.context, 1000L);
                                    }
                                });
                                tipDialog.show();
                                break;
                            }
                            break;
                    }
                }
            } catch (Exception unused) {
            }
        }

        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$8$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements TipDialog.TipDialogListener {
            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_02() {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_01() {
                GoodsImagesHelper.this.exitApp(GoodsImagesHelper.this.context, 1000L);
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void timeEnd() {
                GoodsImagesHelper.this.exitApp(GoodsImagesHelper.this.context, 1000L);
            }
        }
    }

    public void exitApp(Context context, long j) {
        Loger.writeLog("APP", "app 重启" + Log.getStackTraceString(new Throwable()));
        Loger.flush();
        context.sendBroadcast(new Intent("XY_APP_SERVICE_TICKER_STOP"));
        this.handler.removeMessages(MSG_EXIT_APP);
        this.handler.sendEmptyMessageDelayed(MSG_EXIT_APP, j);
    }

    public List<DownloadImageData> changeData(List<JSONObject> list) {
        ArrayList arrayList = new ArrayList();
        try {
            for (JSONObject jSONObject : list) {
                DownloadImageData downloadImageData = new DownloadImageData();
                downloadImageData.url = jSONObject.getString("image");
                downloadImageData.url = downloadImageData.url.replace("/XyPlat", "");
                if (!downloadImageData.url.contains("http:") && !downloadImageData.url.contains("https:")) {
                    downloadImageData.url = NetAddress.getSPImgUrl() + downloadImageData.url;
                }
                Loger.writeLog("REQUEST", "download:" + downloadImageData.url);
                String string = jSONObject.getString("code");
                if (string.endsWith(".png")) {
                    string = string.replace(".png", "");
                }
                downloadImageData.savePath = SDFileUtils.SDCardRoot + "xyShj/images/";
                if (jSONObject.has("path")) {
                    downloadImageData.savePath = SDFileUtils.SDCardRoot + "xyShj/images/" + jSONObject.getString("path") + UsbFile.separator;
                }
                String substring = downloadImageData.url.indexOf(UsbFile.separator) == -1 ? downloadImageData.url : downloadImageData.url.substring(downloadImageData.url.lastIndexOf(UsbFile.separator));
                downloadImageData.saveName = string + (substring.lastIndexOf(".") != -1 ? substring.substring(substring.lastIndexOf(".")) : ".png");
                arrayList.add(downloadImageData);
                arrayList.add(downloadImageData);
            }
        } catch (Exception unused) {
        }
        return arrayList;
    }

    /* loaded from: classes2.dex */
    public class SaveImageRunnable implements Runnable {
        List<DownloadImageData> items;
        boolean restart;

        private SaveImageRunnable() {
            this.restart = false;
        }

        /* synthetic */ SaveImageRunnable(GoodsImagesHelper goodsImagesHelper, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // java.lang.Runnable
        public void run() {
            Message obtain = Message.obtain();
            obtain.what = 3000;
            obtain.obj = new Object[]{Integer.valueOf(this.items.size())};
            GoodsImagesHelper.this.handler.sendMessage(obtain);
            GoodsImagesHelper.this.handler.sendEmptyMessageDelayed(GoodsImagesHelper.PROCES_TIMEOUT, this.items.size() * 10 * 1000);
            int i = 0;
            for (int i2 = 0; i2 < this.items.size(); i2++) {
                try {
                    DownloadImageData downloadImageData = this.items.get(i2);
                    Loger.writeLog("REQUEST", "download:" + downloadImageData.url);
                    String str = downloadImageData.saveName;
                    try {
                        GoodsImagesHelper.this.handler.removeMessages(GoodsImagesHelper.PROCES_CURRENT);
                    } catch (Exception unused) {
                    }
                    Message obtain2 = Message.obtain();
                    obtain2.what = GoodsImagesHelper.PROCES_CURRENT;
                    obtain2.obj = new Object[]{Integer.valueOf(i2 + 1), GoodsImagesHelper.this.getDealString(R.string.lab_downlingimage, "001", str)};
                    GoodsImagesHelper.this.handler.sendMessage(obtain2);
                    if (GoodsImagesHelper.this.saveImage(downloadImageData.url, downloadImageData.savePath + downloadImageData.saveName)) {
                        i++;
                    }
                } catch (Exception unused2) {
                }
            }
            GoodsImagesHelper.this.handler.removeMessages(GoodsImagesHelper.PROCES_TIMEOUT);
            Message obtain3 = Message.obtain();
            obtain3.what = GoodsImagesHelper.PROCES_END;
            Object[] objArr = new Object[3];
            objArr[0] = true;
            objArr[1] = Boolean.valueOf(this.restart);
            objArr[2] = Boolean.valueOf(i > 0);
            obtain3.obj = objArr;
            GoodsImagesHelper.this.handler.sendMessage(obtain3);
        }
    }

    public String getDealString(int i, String str, Object obj) {
        return this.context.getString(i).replace(str, obj.toString()).replace("%%", "%").replace("/n", StringUtils.LF);
    }

    public boolean saveImage(String str, String str2) {
        ByteArrayOutputStream byteArrayOutputStream;
        InputStream inputStream;
        HttpURLConnection httpURLConnection;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            try {
                httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod(HttpGet.METHOD_NAME);
                inputStream = httpURLConnection.getInputStream();
                try {
                } catch (Exception e) {
                    e = e;
                    byteArrayOutputStream = null;
                } catch (Throwable th) {
                    th = th;
                    byteArrayOutputStream = null;
                }
            } catch (Exception e2) {
                e = e2;
                byteArrayOutputStream = null;
                inputStream = null;
            } catch (Throwable th2) {
                th = th2;
                byteArrayOutputStream = null;
                inputStream = null;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        if (httpURLConnection.getResponseCode() != 200) {
            if (inputStream != null) {
                inputStream.close();
            }
            return false;
        }
        byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(new File(str2)));
                try {
                    decodeByteArray.compress(Bitmap.CompressFormat.PNG, 80, bufferedOutputStream2);
                    bufferedOutputStream2.flush();
                    try {
                        bufferedOutputStream2.close();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e6) {
                            e6.printStackTrace();
                        }
                    }
                    return true;
                } catch (Exception e7) {
                    bufferedOutputStream = bufferedOutputStream2;
                    e = e7;
                    e.printStackTrace();
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (Exception e8) {
                            e8.printStackTrace();
                        }
                    }
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (Exception e9) {
                            e9.printStackTrace();
                        }
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    return false;
                } catch (Throwable th3) {
                    bufferedOutputStream = bufferedOutputStream2;
                    th = th3;
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (Exception e10) {
                            e10.printStackTrace();
                        }
                    }
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (Exception e11) {
                            e11.printStackTrace();
                        }
                    }
                    if (inputStream == null) {
                        throw th;
                    }
                    try {
                        inputStream.close();
                        throw th;
                    } catch (Exception e12) {
                        e12.printStackTrace();
                        throw th;
                    }
                }
            } catch (Throwable th4) {
                th = th4;
            }
        } catch (Exception e13) {
            e = e13;
        }
    }

    public void downLoadFodderFileDatas(boolean z, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("jqbh", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Loger.writeLog("SALES", "查询商品素材参数:" + jSONObject.toString());
        RequestItem requestItem = new RequestItem("http://www.xynetweb.cn:8090/service-goods/ShjApp/zy/fodderdown", jSONObject, "POST");
        requestItem.setRepeatDelay(5000);
        requestItem.setRequestMaxCount(1);
        requestItem.setOnRequestResultListener(new AnonymousClass9(z, str));
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.setting.helper.GoodsImagesHelper$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ boolean val$alertMessage;
        final /* synthetic */ String val$machineid;

        AnonymousClass9(boolean z, String str) {
            this.val$alertMessage = z;
            this.val$machineid = str;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem, int i, String str, Throwable th) {
            Loger.writeLog("SALES", "查询商品素材信息:" + i + StringUtils.SPACE + str);
            if (this.val$alertMessage) {
                ToastUitl.showLong(GoodsImagesHelper.this.context, R.string.lab_downloadgoodsinfoerror);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:90:0x01f4, code lost:
        
            if (r5 == null) goto L317;
         */
        /* JADX WARN: Removed duplicated region for block: B:105:? A[Catch: Exception -> 0x021f, SYNTHETIC, TRY_ENTER, TRY_LEAVE, TryCatch #16 {Exception -> 0x021f, blocks: (B:3:0x001c, B:5:0x002f, B:6:0x0039, B:19:0x008e, B:23:0x009c, B:25:0x00a2, B:26:0x00b3, B:28:0x00b9, B:32:0x0120, B:33:0x00c8, B:35:0x00e9, B:36:0x00ec, B:40:0x0114, B:43:0x0123, B:45:0x0166, B:47:0x016c, B:48:0x0175, B:50:0x017b, B:52:0x0189, B:55:0x0190, B:57:0x0196, B:62:0x01a1, B:82:0x01f7, B:101:0x01ea, B:117:0x020d, B:119:0x0211, B:123:0x0128, B:125:0x012e, B:127:0x013c, B:130:0x0143, B:132:0x0149, B:136:0x0160, B:137:0x0154, B:140:0x0163, B:165:0x007e), top: B:2:0x001c }] */
        /* JADX WARN: Removed duplicated region for block: B:106:0x01e0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:117:0x020d A[Catch: Exception -> 0x021f, TryCatch #16 {Exception -> 0x021f, blocks: (B:3:0x001c, B:5:0x002f, B:6:0x0039, B:19:0x008e, B:23:0x009c, B:25:0x00a2, B:26:0x00b3, B:28:0x00b9, B:32:0x0120, B:33:0x00c8, B:35:0x00e9, B:36:0x00ec, B:40:0x0114, B:43:0x0123, B:45:0x0166, B:47:0x016c, B:48:0x0175, B:50:0x017b, B:52:0x0189, B:55:0x0190, B:57:0x0196, B:62:0x01a1, B:82:0x01f7, B:101:0x01ea, B:117:0x020d, B:119:0x0211, B:123:0x0128, B:125:0x012e, B:127:0x013c, B:130:0x0143, B:132:0x0149, B:136:0x0160, B:137:0x0154, B:140:0x0163, B:165:0x007e), top: B:2:0x001c }] */
        /* JADX WARN: Removed duplicated region for block: B:122:0x0127  */
        /* JADX WARN: Removed duplicated region for block: B:151:0x008a A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:155:0x0083 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:163:0x007b A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:169:? A[Catch: Exception -> 0x021f, SYNTHETIC, TRY_ENTER, TRY_LEAVE, TryCatch #16 {Exception -> 0x021f, blocks: (B:3:0x001c, B:5:0x002f, B:6:0x0039, B:19:0x008e, B:23:0x009c, B:25:0x00a2, B:26:0x00b3, B:28:0x00b9, B:32:0x0120, B:33:0x00c8, B:35:0x00e9, B:36:0x00ec, B:40:0x0114, B:43:0x0123, B:45:0x0166, B:47:0x016c, B:48:0x0175, B:50:0x017b, B:52:0x0189, B:55:0x0190, B:57:0x0196, B:62:0x01a1, B:82:0x01f7, B:101:0x01ea, B:117:0x020d, B:119:0x0211, B:123:0x0128, B:125:0x012e, B:127:0x013c, B:130:0x0143, B:132:0x0149, B:136:0x0160, B:137:0x0154, B:140:0x0163, B:165:0x007e), top: B:2:0x001c }] */
        /* JADX WARN: Removed duplicated region for block: B:170:0x0074 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:22:0x009b  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x016c A[Catch: Exception -> 0x021f, TryCatch #16 {Exception -> 0x021f, blocks: (B:3:0x001c, B:5:0x002f, B:6:0x0039, B:19:0x008e, B:23:0x009c, B:25:0x00a2, B:26:0x00b3, B:28:0x00b9, B:32:0x0120, B:33:0x00c8, B:35:0x00e9, B:36:0x00ec, B:40:0x0114, B:43:0x0123, B:45:0x0166, B:47:0x016c, B:48:0x0175, B:50:0x017b, B:52:0x0189, B:55:0x0190, B:57:0x0196, B:62:0x01a1, B:82:0x01f7, B:101:0x01ea, B:117:0x020d, B:119:0x0211, B:123:0x0128, B:125:0x012e, B:127:0x013c, B:130:0x0143, B:132:0x0149, B:136:0x0160, B:137:0x0154, B:140:0x0163, B:165:0x007e), top: B:2:0x001c }] */
        /* JADX WARN: Removed duplicated region for block: B:99:0x01e7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean onSuccess(com.oysb.utils.http.RequestItem r18, int r19, java.lang.String r20) {
            /*
                Method dump skipped, instructions count: 545
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.shj.setting.helper.GoodsImagesHelper.AnonymousClass9.onSuccess(com.oysb.utils.http.RequestItem, int, java.lang.String):boolean");
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.setting.helper.GoodsImagesHelper$9$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                TipDialog tipDialog = new TipDialog(GoodsImagesHelper.this.context, 0, GoodsImagesHelper.this.context.getString(R.string.lab_noneedupdatefodderimages), GoodsImagesHelper.this.context.getString(R.string.lab_redownload), GoodsImagesHelper.this.context.getString(R.string.button_cancel));
                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.helper.GoodsImagesHelper.9.1.1
                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    C00731() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                        try {
                            File file = new File(SDFileUtils.SDCardRoot + "xyShj/fodderImagesInfo.dat");
                            file.delete();
                            Loger.writeLog("APP", "delete file at downLoadFodderfiles:" + file.getAbsolutePath());
                        } catch (Exception unused) {
                        }
                        GoodsImagesHelper.this.downLoadFodderFileDatas(true, AnonymousClass9.this.val$machineid);
                    }
                });
                tipDialog.show();
            }

            /* renamed from: com.shj.setting.helper.GoodsImagesHelper$9$1$1 */
            /* loaded from: classes2.dex */
            class C00731 implements TipDialog.TipDialogListener {
                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_02() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void timeEnd() {
                }

                C00731() {
                }

                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                public void buttonClick_01() {
                    try {
                        File file = new File(SDFileUtils.SDCardRoot + "xyShj/fodderImagesInfo.dat");
                        file.delete();
                        Loger.writeLog("APP", "delete file at downLoadFodderfiles:" + file.getAbsolutePath());
                    } catch (Exception unused) {
                    }
                    GoodsImagesHelper.this.downLoadFodderFileDatas(true, AnonymousClass9.this.val$machineid);
                }
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem, boolean z) {
            if (z) {
                return;
            }
            ProgressManager.showProgress(GoodsImagesHelper.this.context, GoodsImagesHelper.this.context.getString(R.string.lab_neterror));
        }
    }

    /* loaded from: classes2.dex */
    private class SaveImageAndFileRunnable implements Runnable {
        List<Fodder> items;
        boolean restart;

        private SaveImageAndFileRunnable() {
            this.restart = false;
        }

        /* synthetic */ SaveImageAndFileRunnable(GoodsImagesHelper goodsImagesHelper, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // java.lang.Runnable
        public void run() {
            Message obtain = Message.obtain();
            obtain.what = 3000;
            obtain.obj = new Object[]{Integer.valueOf(this.items.size())};
            GoodsImagesHelper.this.handler.sendMessage(obtain);
            GoodsImagesHelper.this.handler.sendEmptyMessageDelayed(GoodsImagesHelper.PROCES_TIMEOUT, this.items.size() * 10 * 1000);
            int i = 0;
            for (int i2 = 0; i2 < this.items.size(); i2++) {
                try {
                    Fodder fodder = this.items.get(i2);
                    String xmlurl = fodder.getXmlurl();
                    String spbh = fodder.getSpbh();
                    String str = "xyShj/fodder/xmls/" + Integer.parseInt(spbh);
                    String replace = xmlurl.replace("/XyPlat", "");
                    if (!replace.contains("http:") && !replace.contains("https:")) {
                        replace = "http://www.xynetweb.cn:8086/" + replace;
                    }
                    Loger.writeLog("REQUEST", "download:" + replace);
                    try {
                        GoodsImagesHelper.this.handler.removeMessages(GoodsImagesHelper.PROCES_CURRENT);
                    } catch (Exception unused) {
                    }
                    Message obtain2 = Message.obtain();
                    obtain2.what = GoodsImagesHelper.PROCES_CURRENT;
                    obtain2.obj = new Object[]{Integer.valueOf(i2 + 1), GoodsImagesHelper.this.getString(R.string.lab_downlingfile, "001", replace)};
                    GoodsImagesHelper.this.handler.sendMessage(obtain2);
                    String str2 = Integer.parseInt(spbh) + UsbFile.separator + xmlurl.replace("softFile/", "");
                    if (!new File(SDFileUtils.SDCardRoot + str).exists()) {
                        SDFileUtils.creatSDDir(str);
                    } else {
                        SDFileUtils.safeDeleteFile(new File(SDFileUtils.SDCardRoot + str), false);
                    }
                    if (GoodsImagesHelper.saveFodderImage(replace, str2)) {
                        i++;
                    }
                } catch (Exception unused2) {
                }
            }
            GoodsImagesHelper.this.handler.removeMessages(GoodsImagesHelper.PROCES_TIMEOUT);
            Message obtain3 = Message.obtain();
            obtain3.what = GoodsImagesHelper.PROCES_END;
            Object[] objArr = new Object[3];
            objArr[0] = true;
            objArr[1] = Boolean.valueOf(this.restart);
            objArr[2] = Boolean.valueOf(i > 0);
            obtain3.obj = objArr;
            GoodsImagesHelper.this.handler.sendMessage(obtain3);
        }
    }

    public static boolean saveFodderImage(String str, String str2) {
        InputStream inputStream;
        HttpURLConnection httpURLConnection;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            try {
                try {
                    httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestMethod(HttpGet.METHOD_NAME);
                    inputStream = httpURLConnection.getInputStream();
                    try {
                    } catch (Exception e) {
                        e = e;
                    }
                } catch (Exception e2) {
                    e = e2;
                    inputStream = null;
                } catch (Throwable th) {
                    th = th;
                    inputStream = null;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            if (httpURLConnection.getResponseCode() != 200) {
                if (inputStream != null) {
                    inputStream.close();
                }
                return false;
            }
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream2.write(bArr, 0, read);
                }
                new FileOutputStream(new File(SDFileUtils.SDCardRoot + "xyShj/fodder/xmls/" + str2)).write(byteArrayOutputStream2.toByteArray());
                try {
                    byteArrayOutputStream2.close();
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                }
                return true;
            } catch (Exception e6) {
                e = e6;
                byteArrayOutputStream = byteArrayOutputStream2;
                e.printStackTrace();
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e7) {
                        e7.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                return false;
            } catch (Throwable th2) {
                th = th2;
                byteArrayOutputStream = byteArrayOutputStream2;
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e8) {
                        e8.printStackTrace();
                    }
                }
                if (inputStream == null) {
                    throw th;
                }
                try {
                    inputStream.close();
                    throw th;
                } catch (Exception e9) {
                    e9.printStackTrace();
                    throw th;
                }
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public String getString(int i, String str, Object obj) {
        return this.context.getString(i).replace(str, obj.toString()).replace("%%", "%").replace("/n", StringUtils.LF);
    }
}
