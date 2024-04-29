package com.xyshj.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
//import android.support.media.ExifInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.iflytek.cloud.SpeechEvent;
import com.loopj.android.http.HttpGet;
import com.oysb.app.R;
import com.oysb.utils.AndroidSystem;
import com.oysb.utils.Loger;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.io.AppUpdateHelper;
import com.oysb.utils.io.file.SDFileUtils;
import com.oysb.utils.video.TTSManager;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.shj.biz.order.OrderPayType;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.Constant;
//import com.tencent.wxpayface.WxfacePayCommonCode;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.UserSettingDao;
import com.xyshj.fragment.BaseFragment;
import com.xyshj.machine.tools.ProgressManager;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.Thread;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.nntp.NNTPReply;
import org.json.JSONArray;
import org.json.JSONObject;
//import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes.dex */
public class ShjAppHelper {
    private static final int AUTOCLOSE_TIMER = 4000;
    private static final int MSG_EXIT_APP = 9000;
    private static String[] PERMISSIONS = null;
    private static final int REQUEST_PERMISSIONS_CODE = 1;
    private static String appFolder = null;
    static String appType = "XY";
    static boolean autoClose = false;
    static TextView autoClose_messageText;
    static int cancelButtonIndex;
    private static String configFile;
    private static Dialog dialog;
    private static Handler handler;
    static OnItemClickListener listener;
    private static Activity mainActivity;
    static BaseFragment mainFragment;
    static Object obj;
    static PackageInfo packageInfo;
    static long progressDlgStartShowTime;
    static boolean scanPay;
    static Thread updateAppThread;
    static Timer updateTimer;
    static ImageView win_goods_img;
    private static HashMap<String, Object> appSetting = new HashMap<>();
    private static String lastError = "";

    /* loaded from: classes2.dex */
    public interface OnItemClickListener {
        void onItemClick(Object obj, int i);
    }

    private static void copyWinMp3ToSD() {
    }

    public static BaseFragment getMainFragment() {
        return mainFragment;
    }

    public static void setScanPay(boolean z) {
        scanPay = z;
    }

    public static boolean getScanPay() {
        return scanPay;
    }

    public static void setMainFragment(BaseFragment baseFragment) {
        mainFragment = baseFragment;
    }

    static {
        configFile = "";
        appFolder = "";
        new File("/mnt/shared/Other/");
        appFolder = SDFileUtils.SDCardRoot + "xyShj";
        configFile = SDFileUtils.SDCardRoot + "xyShj/config.cfg";
        handler = new Handler() { // from class: com.xyshj.app.ShjAppHelper.1


            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                try {
                    int i = message.what;
                    if (i != 4000) {
                        if (i == ShjAppHelper.MSG_EXIT_APP) {
                            System.exit(0);
                        }
                    } else if (ShjAppHelper.autoClose) {
                        Integer valueOf = Integer.valueOf(((Integer) message.obj).intValue() - 1);
                        Loger.writeLog("UI", "Timer:" + valueOf);
                        if (valueOf.intValue() == 0) {
                            ShjAppHelper.cancelMessage(true);
                        } else {
                            try {
                                String obj2 = ShjAppHelper.autoClose_messageText.getTag().toString();
                                if (obj2.contains("#0#")) {
                                    obj2 = obj2.replace("#0#", "" + valueOf);
                                } else if (obj2.contains("0")) {
                                    obj2 = obj2.replace("0", "" + valueOf);
                                }
                                ShjAppHelper.autoClose_messageText.setText(obj2);
                            } catch (Exception unused) {
                            }
                            Message obtain = Message.obtain();
                            obtain.what = 4000;
                            obtain.obj = valueOf;
                            ShjAppHelper.handler.sendMessageDelayed(obtain, 1000L);
                        }
                    }
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                    Loger.writeException("UI", e);
                }
            }
        };
        progressDlgStartShowTime = 0L;
        updateAppThread = null;
        updateTimer = null;
        PERMISSIONS = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_NUMBERS", "android.permission.READ_PHONE_STATE", "android.permission.READ_SMS", "android.permission.MOUNT_UNMOUNT_FILESYSTEMS", "android.permission.WRITE_SETTINGS", "android.permission.RECORD_AUDIO", "android.permission.RECEIVE_BOOT_COMPLETED", "android.permission.ACCESS_FINE_LOCATION", "android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE", "android.permission.ACCESS_WIFI_STATE", "android.permission.READ_LOGS", "android.permission.CHANGE_NETWORK_STATE", "android.permission.WAKE_LOCK", "android.permission.KILL_BACKGROUND_PROCESSES", "android.permission.VIBRATE", "android.permission.GET_TASKS", "android.permission.SET_TIME", "android.permission.SET_TIME_ZONE", "com.android.alarm.permission.SET_ALARM"};
    }

    public static String getAppType() {
        String str = appType;
        if (str == null || str.isEmpty()) {
            appType = appSetting.get("APPTYPE").toString();
        }
        return appType;
    }

    public static void setAppType(String str) {
        appType = str;
        appSetting.put("APPTYPE", str);
    }

    public static String getAppFolder() {
        return appFolder;
    }

    /* renamed from: com.xyshj.app.ShjAppHelper$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                int i = message.what;
                if (i != 4000) {
                    if (i == ShjAppHelper.MSG_EXIT_APP) {
                        System.exit(0);
                    }
                } else if (ShjAppHelper.autoClose) {
                    Integer valueOf = Integer.valueOf(((Integer) message.obj).intValue() - 1);
                    Loger.writeLog("UI", "Timer:" + valueOf);
                    if (valueOf.intValue() == 0) {
                        ShjAppHelper.cancelMessage(true);
                    } else {
                        try {
                            String obj2 = ShjAppHelper.autoClose_messageText.getTag().toString();
                            if (obj2.contains("#0#")) {
                                obj2 = obj2.replace("#0#", "" + valueOf);
                            } else if (obj2.contains("0")) {
                                obj2 = obj2.replace("0", "" + valueOf);
                            }
                            ShjAppHelper.autoClose_messageText.setText(obj2);
                        } catch (Exception unused) {
                        }
                        Message obtain = Message.obtain();
                        obtain.what = 4000;
                        obtain.obj = valueOf;
                        ShjAppHelper.handler.sendMessageDelayed(obtain, 1000L);
                    }
                }
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                Loger.writeException("UI", e);
            }
        }
    }

    public static Handler getMainHandler() {
        return handler;
    }

    public static void showMessage(String str, View view, int i, String str2, String str3, Object obj2, OnItemClickListener onItemClickListener) {
        showMessage(str, null, view, i, str2, str3, obj2, onItemClickListener);
    }

    public static void showMessage(String str, String str2, int i, String str3, String str4, Object obj2, OnItemClickListener onItemClickListener) {
        showMessage(str, str2, null, i, str3, str4, obj2, onItemClickListener);
    }

    private static void showMessage(String str, String str2, View view, int i, String str3, String str4, Object obj2, OnItemClickListener onItemClickListener) {
        cancelMessage(false);
        obj = obj2;
        listener = onItemClickListener;
        autoClose = i > 0;
        if (mainActivity == null) {
            return;
        }
        Dialog dialog2 = new Dialog(mainActivity, R.style.AlertActivity_AlertStyle);
        dialog = dialog2;
        dialog2.setCanceledOnTouchOutside(false);
        View inflate = View.inflate(mainActivity, R.layout.dialog_msg, null);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.body);
        inflate.setMinimumWidth(800);
        inflate.setMinimumHeight(NNTPReply.POSTING_NOT_ALLOWED);
        TextView textView = (TextView) inflate.findViewById(R.id.title);
        textView.setText(str);
        textView.setVisibility(str.length() > 0 ? View.VISIBLE : View.GONE);
        inflate.findViewById(R.id.divider_line).setVisibility((str == null || str.length() == 0) ? View.INVISIBLE : View.VISIBLE);
        if (view != null) {
            inflate.findViewById(R.id.msg).setVisibility(View.GONE);
            new LinearLayout.LayoutParams(-2, -2);
            linearLayout.addView(view);
        } else {
            TextView textView2 = (TextView) inflate.findViewById(R.id.msg);
            autoClose_messageText = textView2;
            textView2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            autoClose_messageText.setText(str2.replace("#0#", "" + i));
            autoClose_messageText.setTag(str2);
            autoClose_messageText.setGravity(str2.contains(StringUtils.LF) ? 19 : 17);
            autoClose_messageText.setMinHeight(200);
        }
        dialog.setContentView(inflate);
        dialog.show();
        dialog.getWindow().setFlags(8, 8);
        Button button = (Button) inflate.findViewById(R.id.confirmBtn);
        Button button2 = (Button) inflate.findViewById(R.id.cancelBtn);
        Button button3 = (Button) inflate.findViewById(R.id.closeBtn);
        button.setText(str3);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.app.ShjAppHelper.2

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ShjAppHelper.dialog.dismiss();
                if (ShjAppHelper.listener != null) {
                    Loger.writeLog("UI", "button 0 clicked");
                    ShjAppHelper.listener.onItemClick(ShjAppHelper.obj, 0);
                }
                ShjAppHelper.autoClose = false;
                ShjAppHelper.handler.removeMessages(4000);
                ShjAppHelper.listener = null;
                ShjAppHelper.obj = null;
                ShjAppHelper.cancelButtonIndex = 0;
                ShjAppHelper.autoClose_messageText = null;
            }
        });
        cancelButtonIndex = 0;
        if (str4 != null && str4.length() > 0) {
            cancelButtonIndex = 1;
            button2.setText(str4);
            button2.setVisibility(View.VISIBLE);
            button2.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.app.ShjAppHelper.3


                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    ShjAppHelper.dialog.dismiss();
                    if (ShjAppHelper.listener != null) {
                        Loger.writeLog("UI", "button 1 clicked");
                        ShjAppHelper.listener.onItemClick(ShjAppHelper.obj, 1);
                    }
                    ShjAppHelper.autoClose = false;
                    ShjAppHelper.handler.removeMessages(4000);
                    ShjAppHelper.listener = null;
                    ShjAppHelper.obj = null;
                    ShjAppHelper.cancelButtonIndex = 0;
                    ShjAppHelper.autoClose_messageText = null;
                }
            });
        }
        if (str2 != null && str2.contains("请联系客服领取奖品")) {
            cancelButtonIndex = 1;
            button3.setVisibility(View.VISIBLE);
            button3.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.app.ShjAppHelper.4


                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    ShjAppHelper.dialog.dismiss();
                    ShjAppHelper.autoClose = false;
                    ShjAppHelper.handler.removeMessages(4000);
                    ShjAppHelper.listener = null;
                    ShjAppHelper.obj = null;
                    ShjAppHelper.cancelButtonIndex = 0;
                    ShjAppHelper.autoClose_messageText = null;
                }
            });
        }
        if (i > 0) {
            handler.removeMessages(4000);
            Message obtain = Message.obtain();
            obtain.what = 4000;
            obtain.obj = Integer.valueOf(i);
            handler.sendMessageDelayed(obtain, 1000L);
        }
    }

    /* renamed from: com.xyshj.app.ShjAppHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            ShjAppHelper.dialog.dismiss();
            if (ShjAppHelper.listener != null) {
                Loger.writeLog("UI", "button 0 clicked");
                ShjAppHelper.listener.onItemClick(ShjAppHelper.obj, 0);
            }
            ShjAppHelper.autoClose = false;
            ShjAppHelper.handler.removeMessages(4000);
            ShjAppHelper.listener = null;
            ShjAppHelper.obj = null;
            ShjAppHelper.cancelButtonIndex = 0;
            ShjAppHelper.autoClose_messageText = null;
        }
    }

    /* renamed from: com.xyshj.app.ShjAppHelper$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            ShjAppHelper.dialog.dismiss();
            if (ShjAppHelper.listener != null) {
                Loger.writeLog("UI", "button 1 clicked");
                ShjAppHelper.listener.onItemClick(ShjAppHelper.obj, 1);
            }
            ShjAppHelper.autoClose = false;
            ShjAppHelper.handler.removeMessages(4000);
            ShjAppHelper.listener = null;
            ShjAppHelper.obj = null;
            ShjAppHelper.cancelButtonIndex = 0;
            ShjAppHelper.autoClose_messageText = null;
        }
    }

    /* renamed from: com.xyshj.app.ShjAppHelper$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            ShjAppHelper.dialog.dismiss();
            ShjAppHelper.autoClose = false;
            ShjAppHelper.handler.removeMessages(4000);
            ShjAppHelper.listener = null;
            ShjAppHelper.obj = null;
            ShjAppHelper.cancelButtonIndex = 0;
            ShjAppHelper.autoClose_messageText = null;
        }
    }

    public static void setListenerNull() {
        listener = null;
    }

    public static void cancelMessage(boolean z) {
        if (dialog != null) {
            if (listener != null) {
                Loger.writeLog("UI", "cancelMessage,timeOut:" + z);
                listener.onItemClick(obj, !z ? 1 : 0);
            }
            dialog.dismiss();
            dialog = null;
        }
        autoClose = false;
        handler.removeMessages(4000);
        listener = null;
        obj = null;
        cancelButtonIndex = 0;
        autoClose_messageText = null;
        Loger.writeLog("UI", "timer canceled");
    }

    public static void cancelMessage() {
        cancelMessage(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:112:0x0467  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0470  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0484  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0469  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void updateAppSetting() {
        /*
            Method dump skipped, instructions count: 1171
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.app.ShjAppHelper.updateAppSetting():void");
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x0088 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:? A[Catch: Exception -> 0x0098, SYNTHETIC, TryCatch #4 {Exception -> 0x0098, blocks: (B:2:0x0000, B:26:0x004d, B:31:0x0045, B:45:0x0070, B:57:0x0090, B:56:0x008d, B:63:0x0083, B:74:0x0091, B:42:0x006b, B:51:0x0088, B:28:0x0040, B:60:0x007e), top: B:1:0x0000, inners: #0, #1, #9, #11 }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x007e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getConfigDataFromAssets() {
        /*
            java.io.File r0 = new java.io.File     // Catch: java.lang.Exception -> L98
            java.lang.String r1 = com.xyshj.app.ShjAppHelper.configFile     // Catch: java.lang.Exception -> L98
            r0.<init>(r1)     // Catch: java.lang.Exception -> L98
            boolean r0 = r0.exists()     // Catch: java.lang.Exception -> L98
            if (r0 != 0) goto L91
            r0 = 0
            com.xyshj.app.ShjAppBase r1 = com.xyshj.app.ShjAppBase.sysApp     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L62
            android.content.res.AssetManager r1 = r1.getAssets()     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L62
            java.lang.String r2 = "config.cfg"
            java.io.InputStream r1 = r1.open(r2)     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L62
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L58
            java.lang.String r3 = com.xyshj.app.ShjAppHelper.configFile     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L58
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L58
            int r0 = r1.available()     // Catch: java.lang.Exception -> L51 java.lang.Throwable -> L7b
        L25:
            if (r0 <= 0) goto L3b
            r3 = 1024(0x400, float:1.435E-42)
            if (r0 <= r3) goto L2e
            byte[] r0 = new byte[r3]     // Catch: java.lang.Exception -> L51 java.lang.Throwable -> L7b
            goto L30
        L2e:
            byte[] r0 = new byte[r0]     // Catch: java.lang.Exception -> L51 java.lang.Throwable -> L7b
        L30:
            r1.read(r0)     // Catch: java.lang.Exception -> L51 java.lang.Throwable -> L7b
            r2.write(r0)     // Catch: java.lang.Exception -> L51 java.lang.Throwable -> L7b
            int r0 = r1.available()     // Catch: java.lang.Exception -> L51 java.lang.Throwable -> L7b
            goto L25
        L3b:
            r2.flush()     // Catch: java.lang.Exception -> L51 java.lang.Throwable -> L7b
            if (r1 == 0) goto L48
            r1.close()     // Catch: java.lang.Exception -> L44
            goto L48
        L44:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Exception -> L98
        L48:
            r2.close()     // Catch: java.lang.Exception -> L4c
            goto L91
        L4c:
            r0 = move-exception
        L4d:
            r0.printStackTrace()     // Catch: java.lang.Exception -> L98
            goto L91
        L51:
            r0 = move-exception
            goto L66
        L53:
            r2 = move-exception
            r4 = r2
            r2 = r0
            r0 = r4
            goto L7c
        L58:
            r2 = move-exception
            r4 = r2
            r2 = r0
            r0 = r4
            goto L66
        L5d:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
            goto L7c
        L62:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L66:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L7b
            if (r1 == 0) goto L73
            r1.close()     // Catch: java.lang.Exception -> L6f
            goto L73
        L6f:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Exception -> L98
        L73:
            if (r2 == 0) goto L91
            r2.close()     // Catch: java.lang.Exception -> L79
            goto L91
        L79:
            r0 = move-exception
            goto L4d
        L7b:
            r0 = move-exception
        L7c:
            if (r1 == 0) goto L86
            r1.close()     // Catch: java.lang.Exception -> L82
            goto L86
        L82:
            r1 = move-exception
            r1.printStackTrace()     // Catch: java.lang.Exception -> L98
        L86:
            if (r2 == 0) goto L90
            r2.close()     // Catch: java.lang.Exception -> L8c
            goto L90
        L8c:
            r1 = move-exception
            r1.printStackTrace()     // Catch: java.lang.Exception -> L98
        L90:
            throw r0     // Catch: java.lang.Exception -> L98
        L91:
            java.lang.String r0 = com.xyshj.app.ShjAppHelper.configFile     // Catch: java.lang.Exception -> L98
            java.lang.String r0 = com.oysb.utils.io.file.SDFileUtils.readFile(r0)     // Catch: java.lang.Exception -> L98
            goto L9e
        L98:
            r0 = move-exception
            r0.printStackTrace()
            java.lang.String r0 = ""
        L9e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.app.ShjAppHelper.getConfigDataFromAssets():java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:108:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0137  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x017f  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01a9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void initConfigData(android.content.Context r7) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 571
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.app.ShjAppHelper.initConfigData(android.content.Context):void");
    }

    public static void updateShopViewType(int i, int i2) {
        if (15 < i2) {
            i2 = 15;
        }
        if (i2 == 0) {
            i2 = 15;
        }
        try {
            JSONObject jSONObject = new JSONObject(SDFileUtils.readFile(SDFileUtils.SDCardRoot + "xyShj/config.cfg"));
            appSetting.put("界面风格", Integer.valueOf(i));
            appSetting.put("每页显示商品数量", Integer.valueOf(i2));
            if (((Integer) appSetting.get("每页显示商品数量")).intValue() > 15) {
                appSetting.put("每页显示商品数量", 15);
            }
            jSONObject.put("界面风格", i);
            jSONObject.put("每页显示商品数量", appSetting.get("每页显示商品数量"));
            if (jSONObject.toString().length() > 10) {
                SDFileUtils.moveFile(configFile, configFile + "_backup");
                SDFileUtils.safeDeleteFile(new File(configFile));
                SDFileUtils.writeToSDFromInput("xyShj", "config.cfg", jSONObject.toString(), false);
            }
            AndroidSystem.setNeedRestartApp(true, "远程指令更新界面风格和商品数量");
        } catch (Exception unused) {
        }
    }

    public static void updateAppVoice(int i, int i2) {
        if (100 < i2) {
            i2 = 100;
        }
        try {
            JSONObject jSONObject = new JSONObject(SDFileUtils.readFile(SDFileUtils.SDCardRoot + "xyShj/config.cfg"));
            if (!jSONObject.has("音量设置")) {
                jSONObject.put("音量设置", new JSONObject());
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("音量设置");
            if (i == 0) {
                appSetting.put("音量设置/广告音量", Integer.valueOf(i2));
                jSONObject2.put("广告音量", appSetting.get("音量设置/广告音量"));
                ShjManager.putSetting(ShjManager.VIDEO_VOICE, appSetting.get("音量设置/广告音量"));
                ShjManager.getVideoHelper().setVideoVoice(ShjAppBase.sysApp, i2);
            } else if (i == 1) {
                appSetting.put("音量设置/语音播报音量", Integer.valueOf(i2));
                jSONObject2.put("语音播报音量", appSetting.get("音量设置/语音播报音量"));
                ShjManager.putSetting(ShjManager.TTS_VOICE, appSetting.get("音量设置/语音播报音量"));
                TTSManager.setTTSVoice(i2);
            }
            if (jSONObject.toString().length() > 10) {
                SDFileUtils.moveFile(configFile, configFile + "_backup");
                SDFileUtils.safeDeleteFile(new File(configFile));
                SDFileUtils.writeToSDFromInput("xyShj", "config.cfg", jSONObject.toString(), false);
            }
        } catch (Exception unused) {
        }
    }

    public static void updateProcessIfNetTimeOut(int i) {
        try {
            JSONObject jSONObject = new JSONObject(SDFileUtils.readFile(SDFileUtils.SDCardRoot + "xyShj/config.cfg"));
            if (!jSONObject.has("联网超时")) {
                jSONObject.put("联网超时", new JSONObject());
            }
            jSONObject.put("联网超时", i);
            appSetting.put("联网超时", Integer.valueOf(i));
            if (jSONObject.toString().length() > 10) {
                SDFileUtils.moveFile(configFile, configFile + "_backup");
                SDFileUtils.safeDeleteFile(new File(configFile));
                SDFileUtils.writeToSDFromInput("xyShj", "config.cfg", jSONObject.toString(), false);
            }
        } catch (Exception unused) {
        }
    }

    public static void udpateOrderTypeTypeSetting(OrderPayType orderPayType, boolean z) {
        JSONObject jSONObject;
        JSONObject jSONObject2 = null;
        try {
            jSONObject = new JSONObject(SDFileUtils.readFile(SDFileUtils.SDCardRoot + "xyShj/config.cfg"));
            try {
                if (!jSONObject.has("支付方式")) {
                    jSONObject.put("支付方式", new JSONObject());
                }
                jSONObject2 = jSONObject.getJSONObject("支付方式");
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            jSONObject = null;
        }
        try {
            switch (AnonymousClass8.$SwitchMap$com$shj$biz$order$OrderPayType[orderPayType.ordinal()]) {
                case 1:
                    appSetting.put("支付方式/现金", Boolean.valueOf(z));
                    if (((Boolean) appSetting.get("支付方式/现金")).booleanValue()) {
                        ShjManager.addOrderPayType(OrderPayType.CASH);
                    } else {
                        ShjManager.getOrderPayTypes().remove(OrderPayType.CASH);
                    }
                    jSONObject2.put("现金", appSetting.get("支付方式/现金"));
                    break;
                case 2:
                    appSetting.put("支付方式/微信扫码", Boolean.valueOf(z));
                    if (((Boolean) appSetting.get("支付方式/微信扫码")).booleanValue()) {
                        ShjManager.addOrderPayType(OrderPayType.WEIXIN);
                    } else {
                        ShjManager.getOrderPayTypes().remove(OrderPayType.WEIXIN);
                    }
                    jSONObject2.put("微信扫码", appSetting.get("支付方式/微信扫码"));
                    break;
                case 3:
                    appSetting.put("支付方式/支付宝扫码", Boolean.valueOf(z));
                    if (((Boolean) appSetting.get("支付方式/支付宝扫码")).booleanValue()) {
                        ShjManager.addOrderPayType(OrderPayType.ZFB);
                    } else {
                        ShjManager.getOrderPayTypes().remove(OrderPayType.ZFB);
                    }
                    jSONObject2.put("支付宝扫码", appSetting.get("支付方式/支付宝扫码"));
                    break;
                case 4:
                    appSetting.put("支付方式/IC卡支付", Boolean.valueOf(z));
                    if (((Boolean) appSetting.get("支付方式/IC卡支付")).booleanValue()) {
                        ShjManager.addOrderPayType(OrderPayType.ICCard);
                    } else {
                        ShjManager.getOrderPayTypes().remove(OrderPayType.ICCard);
                    }
                    jSONObject2.put("IC卡支付", appSetting.get("支付方式/IC卡支付"));
                    break;
                case 5:
                    appSetting.put("支付方式/银联扫码", Boolean.valueOf(z));
                    if (((Boolean) appSetting.get("支付方式/银联扫码")).booleanValue()) {
                        ShjManager.addOrderPayType(OrderPayType.YL);
                    } else {
                        ShjManager.getOrderPayTypes().remove(OrderPayType.YL);
                    }
                    jSONObject2.put("银联扫码", appSetting.get("支付方式/银联扫码"));
                    break;
                case 6:
                    appSetting.put("支付方式/银联扫码X", Boolean.valueOf(z));
                    if (((Boolean) appSetting.get("支付方式/银联扫码X")).booleanValue()) {
                        ShjManager.addOrderPayType(OrderPayType.YL6);
                    } else {
                        ShjManager.getOrderPayTypes().remove(OrderPayType.YL6);
                    }
                    jSONObject2.put("银联扫码X", appSetting.get("支付方式/银联扫码X"));
                    break;
                case 7:
                    appSetting.put("支付方式/银联聚合码", Boolean.valueOf(z));
                    if (((Boolean) appSetting.get("支付方式/银联聚合码")).booleanValue()) {
                        ShjManager.addOrderPayType(OrderPayType.YLJH);
                    } else {
                        ShjManager.getOrderPayTypes().remove(OrderPayType.YLJH);
                    }
                    jSONObject2.put("银联聚合码", appSetting.get("支付方式/银联聚合码"));
                    break;
                case 8:
                    appSetting.put("支付方式/京东扫码", Boolean.valueOf(z));
                    if (((Boolean) appSetting.get("支付方式/京东扫码")).booleanValue()) {
                        ShjManager.addOrderPayType(OrderPayType.JD);
                    } else {
                        ShjManager.getOrderPayTypes().remove(OrderPayType.JD);
                    }
                    jSONObject2.put("京东扫码", appSetting.get("支付方式/京东扫码"));
                    break;
                case 9:
                case 10:
                    UserSettingDao userSettingDao = new UserSettingDao(ShjAppBase.sysApp);
                    AppSetting.saveEnableFacePay(ShjManager.getActivityContext(), z, userSettingDao);
                    userSettingDao.close();
                    AndroidSystem.setNeedRestartApp(true, "修改了支付方式");
                    break;
            }
            if (jSONObject.toString().length() > 10) {
                SDFileUtils.moveFile(configFile, configFile + "_backup");
                SDFileUtils.safeDeleteFile(new File(configFile));
                SDFileUtils.writeToSDFromInput("xyShj", "config.cfg", jSONObject.toString(), false);
            }
        } catch (Exception unused3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.app.ShjAppHelper$8 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass8 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$biz$order$OrderPayType;

        static {
            int[] iArr = new int[OrderPayType.values().length];
            $SwitchMap$com$shj$biz$order$OrderPayType = iArr;
            try {
                iArr[OrderPayType.CASH.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.WEIXIN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.ZFB.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.ICCard.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YL.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YL6.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YLJH.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.JD.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.WxFace.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.Face.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    public static void saveAppSetting() {
        Loger.writeLog("SHJ", "保存售货机参数信息...");
        try {
            JSONObject jSONObject = new JSONObject(SDFileUtils.readFile(SDFileUtils.SDCardRoot + "xyShj/config.cfg"));
            if (!jSONObject.has("支付方式")) {
                jSONObject.put("支付方式", new JSONObject());
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("支付方式");
            jSONObject2.put("现金", appSetting.get("支付方式/现金"));
            jSONObject2.put("微信扫码", appSetting.get("支付方式/微信扫码"));
            jSONObject2.put("支付宝扫码", appSetting.get("支付方式/支付宝扫码"));
            jSONObject2.put("银联扫码", appSetting.get("支付方式/银联扫码"));
            jSONObject2.put("银联扫码X", appSetting.get("支付方式/银联扫码X"));
            jSONObject2.put("银联聚合码", appSetting.get("支付方式/银联聚合码"));
            jSONObject2.put("IC卡支付", appSetting.get("支付方式/IC卡支付"));
            jSONObject2.put("京东扫码", appSetting.get("支付方式/京东扫码"));
            if (!jSONObject.has("串口设置")) {
                jSONObject.put("串口设置", new JSONObject());
            }
            JSONObject jSONObject3 = jSONObject.getJSONObject("串口设置");
            if (!jSONObject3.has("下位机")) {
                jSONObject3.put("下位机", new JSONObject());
            }
            JSONObject jSONObject4 = jSONObject3.getJSONObject("下位机");
            jSONObject4.put("协议版本号", appSetting.get("串口设置/下位机/协议版本号"));
            jSONObject4.put("串口地址", appSetting.get("串口设置/下位机/串口地址").toString());
            jSONObject4.put("波特率", appSetting.get("串口设置/下位机/波特率"));
            if (!jSONObject.has("音量设置")) {
                jSONObject.put("音量设置", new JSONObject());
            }
            JSONObject jSONObject5 = jSONObject.getJSONObject("音量设置");
            jSONObject5.put("广告音量", appSetting.get("音量设置/广告音量"));
            jSONObject5.put("语音播报音量", appSetting.get("音量设置/语音播报音量"));
            if (!jSONObject.has("机器设置")) {
                jSONObject.put("机器设置", new JSONObject());
            }
            JSONObject jSONObject6 = jSONObject.getJSONObject("机器设置");
            jSONObject6.put("机器编号", appSetting.get("机器设置/机器编号").toString());
            jSONObject6.put("机器类型", appSetting.get("机器设置/机器类型"));
            jSONObject6.put("主控板版本号", appSetting.get("机器设置/主控板版本号").toString());
            if (((Long) appSetting.get("机器设置/吞币时间")).longValue() < 300) {
                appSetting.put("机器设置/吞币时间", 300L);
            }
            jSONObject6.put("吞币时间", appSetting.get("机器设置/吞币时间"));
            jSONObject6.put("启用商品价格检查", appSetting.get("机器设置/启用商品价格检查"));
            jSONObject.put("货币符号", appSetting.get("货币符号").toString());
            jSONObject.put("温馨提示", appSetting.get("温馨提示").toString());
            jSONObject.put("活动网页地址", appSetting.get("活动网页地址").toString());
            jSONObject.put("空闲时间自动转到活动页面", appSetting.get("空闲时间自动转到活动页面"));
            jSONObject.put("界面风格", appSetting.get("界面风格"));
            jSONObject.put("启用购物车", appSetting.get("启用购物车"));
            jSONObject.put("打印小票", appSetting.get("打印小票"));
            jSONObject.put("显示营销", appSetting.get("显示营销"));
            jSONObject.put("联网超时", appSetting.get("联网超时"));
            if (((Integer) appSetting.get("每页显示商品数量")).intValue() > 15) {
                appSetting.put("每页显示商品数量", 15);
            }
            jSONObject.put("每页显示商品数量", appSetting.get("每页显示商品数量"));
            if (jSONObject.toString().length() > 10) {
                SDFileUtils.moveFile(configFile, configFile + "_backup");
                SDFileUtils.safeDeleteFile(new File(configFile));
                SDFileUtils.writeToSDFromInput("xyShj", "config.cfg", jSONObject.toString(), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v11 */
    /* JADX WARN: Type inference failed for: r8v12 */
    /* JADX WARN: Type inference failed for: r8v14, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r8v2 */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v4, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r8v7, types: [java.io.ByteArrayOutputStream] */
    public static boolean saveImage(String str, String str2) {
        InputStream inputStream;
        HttpURLConnection httpURLConnection;
        BufferedOutputStream bufferedOutputStream = null;
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            try {
                progressDlgStartShowTime = System.currentTimeMillis();
                httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod(HttpGet.METHOD_NAME);
                inputStream = httpURLConnection.getInputStream();
                try {
                } catch (Exception e2) {
                    e = e2;
                    str = 0;
                } catch (Throwable th) {
                    th = th;
                    str = 0;
                }
            } catch (Exception e3) {
                e = e3;
                str = 0;
                inputStream = null;
            } catch (Throwable th2) {
                th = th2;
                str = 0;
                inputStream = null;
            }
            if (httpURLConnection.getResponseCode() != 200) {
                if (inputStream != null) {
                    inputStream.close();
                }
                return false;
            }
            str = new ByteArrayOutputStream();
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    str.write(bArr, 0, read);
                }
                byte[] byteArray = str.toByteArray();
                Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(new File(SDFileUtils.SDCardRoot + "xyshj/images/" + str2)));
                try {
                    decodeByteArray.compress(Bitmap.CompressFormat.PNG, 80, bufferedOutputStream2);
                    bufferedOutputStream2.flush();
                    try {
                        bufferedOutputStream2.close();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    try {
                        str.close();
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
                    if (str != 0) {
                        try {
                            str.close();
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
                    if (str != 0) {
                        try {
                            str.close();
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
            } catch (Exception e13) {
                e = e13;
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }

    public static void setShelves_allFull(Context context) {
        try {
            List<Integer> shelves = Shj.getShelves();
            ShjManager.batchStart();
            for (int i = 0; i < shelves.size(); i++) {
                ShelfInfo shelfInfo = Shj.getShelfInfo(shelves.get(i));
                ShjManager.setShelfGoodsCount(shelfInfo.getShelf().intValue(), shelfInfo.getCapacity().intValue());
            }
            ShjManager.batchEnd();
        } catch (Exception unused) {
            ProgressManager.changeProgressShowMessage(IjkMediaPlayer.OnNativeInvokeListener.ARG_ERROR);
            ShjManager.batchEnd();
        }
    }

    public static void init(Context context) {
        SDFileUtils.addSafeFile("config.cfg");
        SDFileUtils.addSafeFile("report.db");
        Shj.setLogSerialDetail(true);
        if (!new File(SDFileUtils.SDCardRoot).canWrite()) {
            Toast.makeText(context, "SD card not to write", 0).show();
        }
        ShjManager.initShjManager(context, SDFileUtils.SDCardRoot + "xyshj/cache.data");
        Loger.writeLog("APP", "ShjAppHelper init---");
        updateAppSetting();
        setLastError(CacheHelper.getFileCache().getAsString("lastError"));
        Loger.setLogFile("SYS", "xyshj/log", "系统.log", 2592000000L, 1024);
        Loger.setLogFile("UI", "xyshj/log", "界面操作.log", 2592000000L, 1024);
        Loger.setLogFile(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, "xyshj/log", "故障.log", 2592000000L, 1024);
        Loger.setLogFile("VIDEO", "xyshj/log", "视频.log", 2592000000L, 1024);
        Loger.setLogFile("APP", "xyshj/log", "APP信息.log", 2592000000L, 1024);
        Loger.writeLog("APP", "App启动***");
        Loger.writeLog("UI", "App启动***");
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: com.xyshj.app.ShjAppHelper.5
            AnonymousClass5() {
            }

            @Override // java.lang.Thread.UncaughtExceptionHandler
            public void uncaughtException(Thread thread, Throwable th) {
                ShjAppHelper.saveUncatchExceptoinError(th);
                System.exit(1);
            }
        });
    }

    /* renamed from: com.xyshj.app.ShjAppHelper$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements Thread.UncaughtExceptionHandler {
        AnonymousClass5() {
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable th) {
            ShjAppHelper.saveUncatchExceptoinError(th);
            System.exit(1);
        }
    }

    public static void saveUncatchExceptoinError(Throwable th) {
        try {
            th.printStackTrace();
            Loger.writeLog(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, "App出现未捕捉异常***");
            Loger.writeLog(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, "" + th.getMessage());
            Loger.writeLog(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, "" + th.getLocalizedMessage());
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, th);
        } catch (Exception unused) {
        }
        Loger.flush();
        try {
            StringBuffer stringBuffer = new StringBuffer();
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                stringBuffer.append(stackTraceElement.toString() + StringUtils.LF);
            }
            CacheHelper.getFileCache().put("lastError", stringBuffer.toString());
        } catch (Exception unused2) {
        }
    }

    private static void updateVersionInfo() {
        if (packageInfo != null) {
            return;
        }
        try {
            packageInfo = ShjAppBase.getInstance().getPackageManager().getPackageInfo(ShjAppBase.getInstance().getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPackageName() {
        updateVersionInfo();
        return packageInfo.packageName;
    }

    public static String getVersionName() {
        updateVersionInfo();
        return packageInfo.versionName;
    }

    public static int getVersionCode() {
        updateVersionInfo();
        return packageInfo.versionCode;
    }

    public static HashMap<String, Object> getAppSetting() {
        return appSetting;
    }

    public static String getLastError() {
        return lastError;
    }

    public static void setLastError(String str) {
        lastError = str;
    }

    public static boolean isXySafeAppRunning(Context context) {
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            Loger.writeLogEx("xyShj/log", "bootlog.log", runningAppProcessInfo.processName + StringUtils.LF);
            if (runningAppProcessInfo.processName.equals(Constant.safeAppName)) {
                Loger.writeLogEx("xyShj/log", "bootlog.log", " safe App isRunning");
                return true;
            }
        }
        return false;
    }

    public static Activity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(Activity activity) {
        mainActivity = activity;
    }

    public static String getString(int i) {
        return ShjAppBase.getInstance().getResources().getString(i).replace("%%", "%").replace("/n", StringUtils.LF);
    }

    public static int getColor(int i) {
        return ContextCompat.getColor(ShjAppBase.getInstance(), i);
    }

    public static String getString(int i, String str, Object obj2) {
        return ShjAppBase.getInstance().getResources().getString(i).replace(str, obj2.toString()).replace("%%", "%").replace("/n", StringUtils.LF);
    }

    public static String getString(int i, String str) {
        String string = ShjAppBase.getInstance().getResources().getString(i);
        if (!string.contains("#购药码#")) {
            return string;
        }
        return string.replace("#购药码#", "" + str);
    }

    public static void startUpdateAppService() {
        Timer timer = updateTimer;
        if (timer != null) {
            timer.cancel();
            updateTimer = null;
        }
        Timer timer2 = new Timer();
        updateTimer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.xyshj.app.ShjAppHelper.6
            AnonymousClass6() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                Throwable th;
                InputStream inputStream;
                ByteArrayOutputStream byteArrayOutputStream = null;
                try {
                    try {
                        try {
                            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(NetAddress.getQueryAppUrl() + "?packageName=" + ShjAppHelper.getPackageName() + "&versionType=" + ShjAppHelper.appSetting.get("APPTYPE").toString() + "&deviceType=0&jqbh=" + Shj.getMachineId()).openConnection();
                            httpURLConnection.setRequestMethod(HttpGet.METHOD_NAME);
                            httpURLConnection.setConnectTimeout(5000);
                            if (httpURLConnection.getResponseCode() == 200) {
                                inputStream = httpURLConnection.getInputStream();
                                try {
                                    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                                    try {
                                        byte[] bArr = new byte[1024];
                                        while (true) {
                                            int read = inputStream.read(bArr);
                                            if (read == -1) {
                                                break;
                                            } else {
                                                byteArrayOutputStream2.write(bArr, 0, read);
                                            }
                                        }
                                        JSONObject jSONObject = new JSONObject(new String(byteArrayOutputStream2.toByteArray()));
                                        Loger.writeLog("REQUEST", "startUpdateAppService:" + jSONObject.toString());
                                        Log.e("REQUEST", "startUpdateAppService:" + jSONObject.toString());
                                        if (jSONObject.has(SpeechEvent.KEY_EVENT_RECORD_DATA)) {
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
                                            if ((string2 + "." + String.format("%06d", Integer.valueOf(Integer.parseInt(string3)))).compareToIgnoreCase(ShjAppHelper.getVersionName() + "." + String.format("%06d", Integer.valueOf(ShjAppHelper.getVersionCode()))) > 0) {
                                                Log.e("REQUEST", "startUpdateAppService:" + string);
                                            }
                                            AppUpdateHelper.startDownlaodTask(ShjAppHelper.appFolder + "/update", string, string2 + "." + Integer.parseInt(string3), null);
                                        }
                                        byteArrayOutputStream = byteArrayOutputStream2;
                                    } catch (Exception e) {
                                        e = e;
                                        byteArrayOutputStream = byteArrayOutputStream2;
                                        Loger.safe_inner_exception_catch(e);
                                        if (byteArrayOutputStream != null) {
                                            try {
                                                byteArrayOutputStream.close();
                                            } catch (Exception e2) {
                                                e2.printStackTrace();
                                            }
                                        }
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                        AppUpdateHelper.clearAppPackages(ShjAppHelper.getMainActivity(), ShjAppHelper.getPackageName(), ShjAppHelper.appFolder + "/update");
                                    } catch (Throwable th2) {
                                        th = th2;
                                        byteArrayOutputStream = byteArrayOutputStream2;
                                        if (byteArrayOutputStream != null) {
                                            try {
                                                byteArrayOutputStream.close();
                                            } catch (Exception e3) {
                                                e3.printStackTrace();
                                            }
                                        }
                                        if (inputStream == null) {
                                            throw th;
                                        }
                                        try {
                                            inputStream.close();
                                            throw th;
                                        } catch (Exception e4) {
                                            e4.printStackTrace();
                                            throw th;
                                        }
                                    }
                                } catch (Exception e5) {
                                    e = e5;
                                }
                            } else {
                                inputStream = null;
                            }
                            if (byteArrayOutputStream != null) {
                                try {
                                    byteArrayOutputStream.close();
                                } catch (Exception e6) {
                                    e6.printStackTrace();
                                }
                            }
                        } catch (Exception e7) {
                            e = e7;
                            inputStream = null;
                        } catch (Throwable th3) {
                            th = th3;
                            inputStream = null;
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (Exception e8) {
                        e8.printStackTrace();
                    }
                    try {
                        AppUpdateHelper.clearAppPackages(ShjAppHelper.getMainActivity(), ShjAppHelper.getPackageName(), ShjAppHelper.appFolder + "/update");
                    } catch (Exception e9) {
                        Loger.safe_inner_exception_catch(e9);
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        }, 10000L, 600000L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.app.ShjAppHelper$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 extends TimerTask {
        AnonymousClass6() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Throwable th;
            InputStream inputStream;
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                try {
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(NetAddress.getQueryAppUrl() + "?packageName=" + ShjAppHelper.getPackageName() + "&versionType=" + ShjAppHelper.appSetting.get("APPTYPE").toString() + "&deviceType=0&jqbh=" + Shj.getMachineId()).openConnection();
                        httpURLConnection.setRequestMethod(HttpGet.METHOD_NAME);
                        httpURLConnection.setConnectTimeout(5000);
                        if (httpURLConnection.getResponseCode() == 200) {
                            inputStream = httpURLConnection.getInputStream();
                            try {
                                ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                                try {
                                    byte[] bArr = new byte[1024];
                                    while (true) {
                                        int read = inputStream.read(bArr);
                                        if (read == -1) {
                                            break;
                                        } else {
                                            byteArrayOutputStream2.write(bArr, 0, read);
                                        }
                                    }
                                    JSONObject jSONObject = new JSONObject(new String(byteArrayOutputStream2.toByteArray()));
                                    Loger.writeLog("REQUEST", "startUpdateAppService:" + jSONObject.toString());
                                    Log.e("REQUEST", "startUpdateAppService:" + jSONObject.toString());
                                    if (jSONObject.has(SpeechEvent.KEY_EVENT_RECORD_DATA)) {
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
                                        if ((string2 + "." + String.format("%06d", Integer.valueOf(Integer.parseInt(string3)))).compareToIgnoreCase(ShjAppHelper.getVersionName() + "." + String.format("%06d", Integer.valueOf(ShjAppHelper.getVersionCode()))) > 0) {
                                            Log.e("REQUEST", "startUpdateAppService:" + string);
                                        }
                                        AppUpdateHelper.startDownlaodTask(ShjAppHelper.appFolder + "/update", string, string2 + "." + Integer.parseInt(string3), null);
                                    }
                                    byteArrayOutputStream = byteArrayOutputStream2;
                                } catch (Exception e) {
                                    e = e;
                                    byteArrayOutputStream = byteArrayOutputStream2;
                                    Loger.safe_inner_exception_catch(e);
                                    if (byteArrayOutputStream != null) {
                                        try {
                                            byteArrayOutputStream.close();
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                    AppUpdateHelper.clearAppPackages(ShjAppHelper.getMainActivity(), ShjAppHelper.getPackageName(), ShjAppHelper.appFolder + "/update");
                                } catch (Throwable th2) {
                                    th = th2;
                                    byteArrayOutputStream = byteArrayOutputStream2;
                                    if (byteArrayOutputStream != null) {
                                        try {
                                            byteArrayOutputStream.close();
                                        } catch (Exception e3) {
                                            e3.printStackTrace();
                                        }
                                    }
                                    if (inputStream == null) {
                                        throw th;
                                    }
                                    try {
                                        inputStream.close();
                                        throw th;
                                    } catch (Exception e4) {
                                        e4.printStackTrace();
                                        throw th;
                                    }
                                }
                            } catch (Exception e5) {
                                e = e5;
                            }
                        } else {
                            inputStream = null;
                        }
                        if (byteArrayOutputStream != null) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (Exception e6) {
                                e6.printStackTrace();
                            }
                        }
                    } catch (Exception e7) {
                        e = e7;
                        inputStream = null;
                    } catch (Throwable th3) {
                        th = th3;
                        inputStream = null;
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e8) {
                    e8.printStackTrace();
                }
                try {
                    AppUpdateHelper.clearAppPackages(ShjAppHelper.getMainActivity(), ShjAppHelper.getPackageName(), ShjAppHelper.appFolder + "/update");
                } catch (Exception e9) {
                    Loger.safe_inner_exception_catch(e9);
                }
            } catch (Throwable th4) {
                th = th4;
            }
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
    }

    public static void enterSetting(Activity activity, boolean z) {
        Intent intent = new Intent(activity, (Class<?>) SettingActivity.class);
        intent.putExtra("showLoginDialog", z);
        intent.addFlags(268435456);
        activity.startActivity(intent);
    }

    public static void getkfPhone() {
        try {
            String kFPhoneUrl = NetAddress.getKFPhoneUrl();
            if (kFPhoneUrl != null && kFPhoneUrl.length() != 0) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("jqbh", Shj.getMachineId());
                Loger.writeLog("SALES", "查询客服电话参数:" + jSONObject.toString());
                RequestItem requestItem = new RequestItem(kFPhoneUrl, jSONObject, "POST");
                requestItem.setRepeatDelay(21600000);
                requestItem.setRequestMaxCount(Integer.MAX_VALUE);
                requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.app.ShjAppHelper.7
                    @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                    public void onRequestFinished(RequestItem requestItem2, boolean z) {
                    }

                    AnonymousClass7() {
                    }

                    @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                    public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                        Loger.writeLog("SALES", "查询客服电话:" + i + StringUtils.SPACE + str);
                    }

                    @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                    public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                        try {
                            Loger.writeLog("SALES", "查询客服电话:" + str);
                            JSONObject jSONObject2 = new JSONObject(str);
                            if (jSONObject2.getString("code").equals("H0000") && !jSONObject2.isNull(SpeechEvent.KEY_EVENT_RECORD_DATA)) {
                                JSONObject jSONObject3 = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
                                if (jSONObject3.has("servicePhone")) {
                                    ShjManager.putData("PHONE_PT", jSONObject3.getJSONObject("servicePhone").getString("phone"));
                                    ShjManager.putData("PHONE_USER", null);
                                    return false;
                                }
                                if (!jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).isNull(SpeechEvent.KEY_EVENT_RECORD_DATA)) {
                                    ShjAppBase.sysModel.setPhoneList(jSONObject3);
                                    ShjManager.putData("PHONE_PT", jSONObject3.getString("phone"));
                                    if (jSONObject3.has(SpeechEvent.KEY_EVENT_RECORD_DATA)) {
                                        JSONArray jSONArray = jSONObject3.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                                        if (jSONArray.length() > 0) {
                                            ShjManager.putData("PHONE_USER", jSONArray.toString());
                                        }
                                    }
                                    return false;
                                }
                            }
                        } catch (Exception e) {
                            Loger.writeException("SALES", e);
                        }
                        return false;
                    }
                });
                RequestHelper.request(requestItem);
            }
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.app.ShjAppHelper$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass7() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            Loger.writeLog("SALES", "查询客服电话:" + i + StringUtils.SPACE + str);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            try {
                Loger.writeLog("SALES", "查询客服电话:" + str);
                JSONObject jSONObject2 = new JSONObject(str);
                if (jSONObject2.getString("code").equals("H0000") && !jSONObject2.isNull(SpeechEvent.KEY_EVENT_RECORD_DATA)) {
                    JSONObject jSONObject3 = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    if (jSONObject3.has("servicePhone")) {
                        ShjManager.putData("PHONE_PT", jSONObject3.getJSONObject("servicePhone").getString("phone"));
                        ShjManager.putData("PHONE_USER", null);
                        return false;
                    }
                    if (!jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).isNull(SpeechEvent.KEY_EVENT_RECORD_DATA)) {
                        ShjAppBase.sysModel.setPhoneList(jSONObject3);
                        ShjManager.putData("PHONE_PT", jSONObject3.getString("phone"));
                        if (jSONObject3.has(SpeechEvent.KEY_EVENT_RECORD_DATA)) {
                            JSONArray jSONArray = jSONObject3.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                            if (jSONArray.length() > 0) {
                                ShjManager.putData("PHONE_USER", jSONArray.toString());
                            }
                        }
                        return false;
                    }
                }
            } catch (Exception e) {
                Loger.writeException("SALES", e);
            }
            return false;
        }
    }

    public static String getMainServicePhone() {
        String str = "";
        try {
            Object data = ShjManager.getData("PHONE_PT");
            Object data2 = ShjManager.getData("PHONE_USER");
            if (data2 != null) {
                JSONArray jSONArray = new JSONArray(data2.toString());
                if (jSONArray.length() > 0) {
                    JSONObject jSONObject = jSONArray.getJSONObject(0);
                    str = jSONObject.getString("telephone") + StringUtils.SPACE + jSONObject.getString("contacts");
                }
            }
            return (str.length() != 0 || data == null) ? str : data.toString();
        } catch (Exception unused) {
            return "";
        }
    }
}
