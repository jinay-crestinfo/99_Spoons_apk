package com.xyshj.machine.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.mjdev.libaums.fs.UsbFile;
import com.oysb.utils.AndroidSystem;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.NetUtils;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.video.VideoHelper;
import com.oysb.utils.view.BFPopView;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.biz.ShjManager;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderManager;
import com.shj.biz.order.OrderPayItem_AME_NetPay;
import com.shj.biz.order.OrderPayType;
//import com.shj.device.cardreader.MdbReader_BDT;
import com.shj.setting.NetAddress.DefaultAME_USAddrss;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.Utils.SalesUtils;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SettingType;
import com.xyshj.machine.R;
import com.xyshj.machine.app.SysApp;
import com.xyshj.machine.app.VmdHelper;
import com.xyshj.machine.facepay.WxFacePayHelper;
import com.xyshj.machine.listener.MyGoodsStatusListener;
import com.xyshj.machine.listener.MyMoneyListener;
import com.xyshj.machine.listener.MyOrderListener;
import com.xyshj.machine.listener.MyShjStatusListener;
import com.xyshj.machine.model.SysModel;
import com.xyshj.machine.popview.PopView_ClassicCollocation;
import com.xyshj.machine.popview.PopView_FreeCollocation;
import com.xyshj.machine.popview.PopView_FreeCollocation_en;
import com.xyshj.machine.popview.PopView_FreeCollocation_st;
import com.xyshj.machine.popview.PopView_Info;
import com.xyshj.machine.popview.PopView_Wait_en;
import com.xyshj.machine.popview.PopView_Wait_st;
import com.xyshj.machine.tools.ProgressManager;

import org.slf4j.Marker;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    String rootFolder;
    SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
    Timer timer = null;
    Handler handler = new Handler() { // from class: com.xyshj.machine.activity.SplashActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1000) {
                try {
                    Intent intent = (Intent) message.obj;
//                    SplashActivity.this.onAction(intent.getAction(), intent.hasExtra(SpeechEvent.KEY_EVENT_RECORD_DATA) ? intent.getBundleExtra(SpeechEvent.KEY_EVENT_RECORD_DATA) : null);
                } catch (Exception unused) {
                }
            }
            super.handleMessage(message);
        }
    };
    long lastTouchTime = 0;
    long lastOpenSettingTouchTime = 0;
    long openSettingtouchCount = 0;

    @Override
    // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
//        getWindow().addFlags(CASQ.SAMPLE_FLAG_DECODE_ONLY);
        getWindow().setFormat(1);
        String language = CommonTool.getLanguage(this);
        if (language.equalsIgnoreCase("es")) {
            Loger.writeLog("APP", "lang:" + language);
        }
        language.equalsIgnoreCase("th");
        try {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO", "android.permission.CAMERA", "android.permission.SYSTEM_ALERT_WINDOW", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_NUMBERS", "android.permission.READ_PHONE_STATE", "android.permission.READ_SMS", "android.permission.MOUNT_UNMOUNT_FILESYSTEMS", "android.permission.WRITE_SETTINGS", "android.permission.RECORD_AUDIO", "android.permission.RECEIVE_BOOT_COMPLETED", "android.permission.ACCESS_FINE_LOCATION", "android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE", "android.permission.ACCESS_WIFI_STATE", "android.permission.READ_LOGS", "android.permission.CHANGE_NETWORK_STATE", "android.permission.WAKE_LOCK", "android.permission.KILL_BACKGROUND_PROCESSES", "android.permission.VIBRATE", "android.permission.GET_TASKS", "android.permission.SET_TIME", "android.permission.SET_TIME_ZONE", "com.android.alarm.permission.SET_ALARM"}, 1);
            Loger.init(SysApp.sysApp);
            Loger.setSendBroadcast(true);
            Loger.writeLog("SHJ", "---------" + AndroidSystem.screenWidth(this) + Marker.ANY_MARKER + AndroidSystem.screenHeight(this));
            StringBuilder sb = new StringBuilder();
            sb.append("demen:100px=>");
            sb.append(getResources().getDimensionPixelSize(R.dimen.px100));
            Loger.writeLog("SHJ", sb.toString());
            try {
                boolean enableFacePay = AppSetting.getEnableFacePay(SysApp.sysApp, null);
                Loger.writeLog("SHJ;SALES", "isWxFacePayInstalled=" + SysApp.isWxFacePayInstalled + " FacePayEanbled=" + enableFacePay);
                if (SysApp.isWxFacePayInstalled && enableFacePay) {
                    Loger.writeLog("SHJ;SALES", "初始化微信刷脸");
                    WxFacePayHelper.init(this, null);
                    SalesUtils.reportWinxinFailCount();
                }
            } catch (Exception unused) {
            }
            this.lastTouchTime = System.currentTimeMillis();
            ShjAppHelper.setMainActivity(this);
            NetAddress.setIAddress(new DefaultAME_USAddrss());
            ShjAppHelper.verifyStoragePermissions(this);
            ShjAppHelper.startUpdateAppService();
            ShjManager.setActivityContext(this);
            ShjManager.putData("SOCKETENABLE", "FALSE");
            ShjManager.getVideoHelper().setLoadingImage(R.drawable.img_videoloading);
            ShjManager.setYljhOrderPayItemClass(OrderPayItem_AME_NetPay.class);
            ShjManager.setOrderTimeOut(120);
            View inflate = getLayoutInflater().inflate(R.layout.layout_mainactivity, (ViewGroup) null);
            if (ShjManager.getOrderPayTypes().contains(OrderPayType.CASH)) {
                inflate.findViewById(R.id.moneybar).setVisibility(View.GONE);
            }
            int screenHeight = AndroidSystem.screenHeight(this) - 1920;
            if (screenHeight < 0) {
                screenHeight = 0;
            }
            int screenWidth = (AndroidSystem.screenWidth(this) - 1080) / 2;
            if (screenWidth < 0) {
                screenWidth = 0;
            }
            if (screenHeight > 0) {
                RelativeLayout relativeLayout = new RelativeLayout(this);
                relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(1080, 1920);
                layoutParams.leftMargin = screenWidth;
                layoutParams.rightMargin = screenWidth;
                inflate.setLayoutParams(layoutParams);
                inflate.setMinimumHeight(1920);
                relativeLayout.addView(inflate);
                setContentView(relativeLayout);
            } else {
                setContentView(inflate);
            }
            BFPopView.registerPopViewParent("MainActivity", (RelativeLayout) inflate);
            initActions();
            if (SysApp.sysModel.getUiType() == SysModel.UI_TYPE_BQL_EN) {
                BFPopView.showPopView("MainActivity", PopView_FreeCollocation_en.class, (Serializable) null);
            } else {
                BFPopView.showPopView("MainActivity", PopView_FreeCollocation_st.class, (Serializable) null);
            }
            showWaitPopView();
            findViewById(R.id.bt_classic).setOnClickListener(this);
            findViewById(R.id.bt_charge).setOnClickListener(this);
            findViewById(R.id.bt_freecollocation).setOnClickListener(this);
            findViewById(R.id.phone).setOnClickListener(this);
            initDatas();
        } catch (Exception e) {
//            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            Loger.writeException("WxFacePayError", e);
        }
//        MdbReader_BDT.get().setAutoStopPaperMoney(false);
        updateMoney();
        updateMachineId();
        ShjAppHelper.getkfPhone();
        initTimer();
    }

    void updateMachineId() {
        try {
            String machineId = Shj.getMachineId();
            TextView textView = (TextView) findViewById(R.id.machineIdAndVer);
            StringBuilder sb = new StringBuilder();
            sb.append(machineId);
            sb.append(UsbFile.separator);
            sb.append(ShjAppHelper.getVersionName());
            sb.append(".");
            sb.append(ShjAppHelper.getVersionCode());
            sb.append(UsbFile.separator);
            sb.append(VmdHelper.get().isVmdConnected() ? ShjAppHelper.getString(R.string.connected) : ShjAppHelper.getString(R.string.disconnect));
            textView.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// TODO : Test Methods

//    void test() {
//        byte[] bArr = {83, 116, 111, 114, 109, 116, 101, 107, 8, -75, 0, 5, 0, 0, 0, 90, 0, ClosedCaptionCtrl.MISC_CHAN_2};
//        boolean z = false;
//        byte[] bArr2 = null;
//        for (int i = 0; i <= 8; i++) {
//            try {
//                if ((bArr[i] & UByte.MAX_VALUE) == 83 && (bArr[i + 1] & UByte.MAX_VALUE) == 116 && (bArr[i + 2] & UByte.MAX_VALUE) == 111) {
//                    int i2 = i + 8;
//                    int i3 = bArr[i2];
//                    int i4 = i2 + i3 + 1;
//                    byte b = bArr[i4];
//                    int i5 = 0;
//                    while (i2 < i4) {
//                        i5 += bArr[i2] & UByte.MAX_VALUE;
//                        i2++;
//                    }
//                    int i6 = i5 % 256;
//                    Loger.writeLog("SHJ", " check:" + ((int) b) + " v:" + i6);
//                    if ((i6 & 255) == (b & UByte.MAX_VALUE)) {
//                        int i7 = i3 + 10;
//                        bArr2 = new byte[i7];
//                        ObjectHelper.updateBytes(bArr2, bArr, i, 0, i7);
//                        break;
//                    }
//                    continue;
//                }
//            } catch (Exception unused) {
//            }
//        }
//        if (bArr2 != null) {
//            Loger.writeLog("SHJ", " 状态数据:" + ObjectHelper.hex2String(bArr2));
//            short s = (short) bArr2[12];
//            short s2 = (short) bArr2[13];
//            short s3 = (short) bArr2[14];
//            Shj.onDeviceMessage("SHJ", "故障码 " + Integer.toBinaryString(s));
//            if ((s & 255) != 0) {
//                Shj.onDeviceMessage("SHJ", "有故障 " + Integer.toBinaryString(s));
//            } else {
//                z = true;
//            }
//            Shj.onDeviceMessage("SHJ", "制糕比例 " + ((int) s3));
//            if (z && s3 < 50) {
//                Shj.onDeviceMessage("SHJ", "制糕比例 < 50");
//            }
//            StringBuilder sb = new StringBuilder();
//            sb.append("运行状态 ");
//            sb.append(Integer.toHexString(s2));
//            sb.append(StringUtils.SPACE);
//            sb.append(s2 == 0 ? "stop" : s2 == 1 ? "auto" : s2 == 2 ? "wash" : "overnaght");
//            Shj.onDeviceMessage("SHJ", sb.toString());
//        }
//    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        try {
//            switch (view.getId()) {
////                TODO :: Need to make this uncomment with solve isConnected
//                case R.id.bt_charge /* 2131230801 */:
//                    if (MdbReader_BDT.isEnabled() && MdbReader_BDT.get().isConnected()) {
//                        MdbReader_BDT.get().charge(Shj.getWallet().getCatchMoney().intValue());
//                        Shj.onResetCurrentMoney(0, true);
//                        break;
//                    }
//                    break;
//                case R.id.bt_classic /* 2131230805 */:
//                    Loger.writeLog("SHJ", "用户点击了经典按钮");
//                    BFPopView.closePopView(PopView_FreeCollocation.class);
//                    BFPopView.closePopView(PopView_ClassicCollocation.class);
//                    BFPopView.showPopView("MainActivity", PopView_ClassicCollocation.class, (Serializable) null);
//                    break;
//                case R.id.bt_freecollocation /* 2131230823 */:
//                    Loger.writeLog("SHJ", "用户点击了自由搭配按钮");
//                    BFPopView.closePopView(PopView_ClassicCollocation.class);
//                    BFPopView.closePopView(PopView_FreeCollocation.class);
//                    BFPopView.showPopView("MainActivity", PopView_FreeCollocation.class, (Serializable) null);
//                    break;
//                case R.id.phone /* 2131231182 */:
//                    String mainServicePhone = ShjAppHelper.getMainServicePhone();
//                    PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) "showphone").put("title", (Object) ShjAppHelper.getString(R.string.connect_us)).put("info", (Object) ("" + mainServicePhone)).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("small_left_image", (Object) Integer.valueOf(R.drawable.ico_phone)).put("time_out", (Object) 5000).put("closeOnClick", (Object) true).put("showTime", (Object) false));
//                    break;
//                default:
//                    break;
//            }
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class MyBroadcastReceiver extends BroadcastReceiver {
        MyBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Message obtain = Message.obtain();
            obtain.obj = intent;
            obtain.what = 1000;
            SplashActivity.this.handler.sendMessage(obtain);
        }
    }

    void initActions() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BFPopView.ACTION_CLOSE_POPVIEW);
        intentFilter.addAction(BFPopView.ACTION_SHOW_POPVIEW);
        intentFilter.addAction(MyShjStatusListener.ACTION_STATUS_RESET_FINISHED);
        intentFilter.addAction(MyShjStatusListener.ACTION_SHJ_NEED_DRIVE_SHELF);
        intentFilter.addAction(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_START);
        intentFilter.addAction(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_STATUS);
        intentFilter.addAction(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_BLOCKED);
        intentFilter.addAction(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_SUCCESS);
        intentFilter.addAction(MyGoodsStatusListener.ACTION_BATCH_OFFER_GOODS_FINISHED);
        intentFilter.addAction(MyShjStatusListener.ACTION_STATUS_NET);
        intentFilter.addAction(MyShjStatusListener.ACTION_SHJ_FREE_TIME);
        intentFilter.addAction(MyMoneyListener.ACTION_MONEY_CHANGED);
        intentFilter.addAction(MyShjStatusListener.ACTION_SHJ_MESSAGE);
        intentFilter.addAction(MyShjStatusListener.ACTION_NEET_CHECK_OFFER_DEVICE_STATE);
        registerReceiver(new MyBroadcastReceiver(), intentFilter);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:40:0x00be. Please report as an issue. */
    protected void onAction(String str, Bundle bundle) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1441579596:
                if (str.equals(MyShjStatusListener.ACTION_SHJ_MESSAGE)) {
                    c = 0;
                    break;
                }
                break;
            case -1342403763:
                if (str.equals(MyShjStatusListener.ACTION_SHJ_FREE_TIME)) {
                    c = 1;
                    break;
                }
                break;
            case -1274066004:
                if (str.equals(MyMoneyListener.ACTION_MONEY_CHANGED)) {
                    c = 2;
                    break;
                }
                break;
            case -943760039:
                if (str.equals(MyShjStatusListener.ACTION_STATUS_NET)) {
                    c = 3;
                    break;
                }
                break;
            case -332615828:
                if (str.equals(MyGoodsStatusListener.ACTION_BATCH_OFFER_GOODS_FINISHED)) {
                    c = 4;
                    break;
                }
                break;
            case -236900188:
                if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_START)) {
                    c = 5;
                    break;
                }
                break;
            case 2203589:
                if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_SUCCESS)) {
                    c = 6;
                    break;
                }
                break;
            case 1176350758:
                if (str.equals(MyShjStatusListener.ACTION_STATUS_RESET_FINISHED)) {
                    c = 7;
                    break;
                }
                break;
            case 1181838418:
                if (str.equals(MyShjStatusListener.ACTION_NEET_CHECK_OFFER_DEVICE_STATE)) {
                    c = '\b';
                    break;
                }
                break;
            case 1246030800:
                if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_STATUS)) {
                    c = '\t';
                    break;
                }
                break;
            case 1847935406:
                if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_BLOCKED)) {
                    c = '\n';
                    break;
                }
                break;
            case 1921836447:
                if (str.equals(MyShjStatusListener.ACTION_SHJ_NEED_DRIVE_SHELF)) {
                    c = 11;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                if (bundle.getString("key").equalsIgnoreCase("GATEWAY")) {
                    ShjAppHelper.showMessage("", bundle.getString("message"), 0, ShjAppHelper.getString(R.string.button_ok), (String) null, (Object) null, (ShjAppHelper.OnItemClickListener) null);
                    return;
                }
                if (bundle.getString("key").equalsIgnoreCase("SERVER")) {
                    String string = bundle.getString("message");
                    if (string.startsWith("notice:showmsg:")) {
                        string = string.substring(15);
                    }
                    try {
                        if (string.contains("UDATE_PAY_TYPE")) {
                            String[] split = string.split(":");
                            ShjAppHelper.udpateOrderTypeTypeSetting(OrderManager.serverType2OrderPayType(Integer.valueOf(Integer.parseInt(split[1])).intValue()), Integer.valueOf(Integer.parseInt(split[2])).intValue() == 1);
                        } else if (string.contains("UDATE_VOICE")) {
                            String[] split2 = string.split(":");
                            ShjAppHelper.updateAppVoice(Integer.valueOf(Integer.parseInt(split2[1])).intValue(), Integer.valueOf(Integer.parseInt(split2[2])).intValue());
                        } else if (string.contains("UDATE_VIDEO_PLAY_MODE")) {
                            String str2 = string.split(":")[1];
                            ShjManager.getVideoHelper();
                            VideoHelper.setPlayerMode(str2);
                        } else if (string.contains("IF_NET_TIMEOUT")) {
                            ShjAppHelper.updateProcessIfNetTimeOut(Integer.parseInt(string.split(":")[1]));
                        } else {
                            if (!string.contains("SHJ_LOCK_STATUS")) {
                                return;
                            }
                            if (string.split(":")[1].equals("1")) {
                                ProgressManager.showProgressEx(this, "暂停营业");
                            } else {
                                ProgressManager.closeProgress();
                            }
                        }
                        return;
                    } catch (Exception unused) {
                        return;
                    }
                }
                if (bundle.getString("key").equalsIgnoreCase("LOCAL")) {
                    String string2 = bundle.getString("message");
                    if (string2.equalsIgnoreCase("*notice*offelineTimeout*")) {
                        if (ShjManager.needStopShjWhenOfferLineTooLong()) {
                            ShjAppHelper.showMessage("", ShjAppHelper.getString(R.string.lab_net_timeout_restart_shj), 0, ShjAppHelper.getString(R.string.button_ok), (String) null, (Object) null, (ShjAppHelper.OnItemClickListener) null);
                            return;
                        } else {
                            if (ShjManager.needRestartAppWhenOfferLineTooLong()) {
                                ShjAppHelper.showMessage("", ShjAppHelper.getString(R.string.lab_net_timeout_restart_app), 30, ShjAppHelper.getString(R.string.button_ok), (String) null, (Object) null, new ShjAppHelper.OnItemClickListener() { // from class: com.xyshj.machine.activity.SplashActivity.2
                                    @Override // com.xyshj.app.ShjAppHelper.OnItemClickListener
                                    public void onItemClick(Object obj, int i) {
                                        if (i == 0) {
                                            AndroidSystem.restartApp("联网超时", 1000);
                                        }
                                    }
                                });
                                return;
                            }
                            return;
                        }
                    }
                    if (string2.equalsIgnoreCase("*notice*close*")) {
                        ShjAppHelper.cancelMessage();
                        return;
                    }
                    return;
                }
                return;
            case 1:
                showWaitPopView();
                return;
            case 2:
                VmdHelper.get().reportCashBalance();
                updateMoney();
                return;
            case 3:
                ((TextView) findViewById(R.id.net_type)).setText(NetUtils.GetNetworkType(this));
                return;
            case 4:
                VmdHelper.get().reportBqlStauts(true);
                VmdHelper.get().reportOfferGoodsResult();
                String language = CommonTool.getLanguage(this);
                if (language.equalsIgnoreCase("es") || language.equalsIgnoreCase("th")) {
//                    MdbReader_BDT.get().charge(Shj.getWallet().getCatchMoney().intValue());
                }
                return;
            case 5:
                Order resentOrder = ShjManager.getOrderManager().getResentOrder(2, null);
                resentOrder.getArgs().getArg("bqlOrdertype");
                ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(bundle.getInt(ShjDbHelper.COLUM_shelf)));
                PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + resentOrder.getUid())).put("title", (Object) ShjAppHelper.getString(R.string.making)).put("info", (Object) ("" + shelfInfo.getGoodsName() + "...")).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("right_image", (Object) Integer.valueOf(R.drawable.ico_bql2)).put("time_out", (Object) 60000).put("showTime", (Object) false));
                return;
            case 6:
                Order resentOrder2 = ShjManager.getOrderManager().getResentOrder(2, null);
                Shj.getShelfInfo(Integer.valueOf(bundle.getInt(ShjDbHelper.COLUM_shelf)));
                PopView_Info.closeInfo(resentOrder2.getUid());
                PopView_Info.BaseMap put = new PopView_Info.BaseMap().put("uid", (Object) ("" + resentOrder2.getPayId() + resentOrder2.getUid())).put("title", (Object) ShjAppHelper.getString(R.string.finished)).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color));
                StringBuilder sb = new StringBuilder();
                sb.append("[ok]");
                sb.append(ShjAppHelper.getString(R.string.notice_pikout));
                PopView_Info.showInfo(put.put("info", (Object) sb.toString()).put("notic", (Object) ShjAppHelper.getString(R.string.welcome)).put("time_out", (Object) 3000).put("showTime", (Object) false));
                return;
            case 7:
                VmdHelper.get().updateShelfInfos();
                VmdHelper.get().reportBqlStauts(false);
                ShjAppBase.sysApp.sendBroadcast(new Intent("Action_refresh_view"));
                return;
            case '\b':
                VmdHelper.get().CheckBqlPlcStatus();
                return;
            case '\t':
                Order resentOrder3 = ShjManager.getOrderManager().getResentOrder(2, null);
                ShelfInfo shelfInfo2 = Shj.getShelfInfo(Integer.valueOf(bundle.getInt(ShjDbHelper.COLUM_shelf)));
                PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + resentOrder3.getUid())).put("title", (Object) ShjAppHelper.getString(R.string.making)).put("info", (Object) ("" + shelfInfo2.getGoodsName() + "...")).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("right_image", (Object) Integer.valueOf(R.drawable.ico_bql2)).put("time_out", (Object) 60000).put("showTime", (Object) false));
                return;
            case '\n':
                Order resentOrder4 = ShjManager.getOrderManager().getResentOrder(2, null);
                Shj.getShelfInfo(Integer.valueOf(bundle.getInt(ShjDbHelper.COLUM_shelf)));
                PopView_Info.closeInfo(resentOrder4.getUid());
                PopView_Info.BaseMap put2 = new PopView_Info.BaseMap().put("uid", (Object) ("" + resentOrder4.getPayId() + resentOrder4.getUid())).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("title", (Object) ShjAppHelper.getString(R.string.finished));
                StringBuilder sb2 = new StringBuilder();
                sb2.append("[error]");
                sb2.append(ShjAppHelper.getString(R.string.auto_refund));
                PopView_Info.showInfo(put2.put("info", (Object) sb2.toString()).put("notic", (Object) ShjAppHelper.getString(R.string.welcome)).put("time_out", (Object) 3000).put("showTime", (Object) false));
                return;
            case 11:
                VmdHelper.get().offerBql(bundle.getInt(ShjDbHelper.COLUM_shelf));
                return;
            default:
                return;
        }
    }

    void updateMoney() {
        Button button = (Button) findViewById(R.id.bt_money);
        String replace = button.getContext().getResources().getString(R.string.lab_insertmoney).replace("$", SysApp.getPriceUnit());
        StringBuilder sb = new StringBuilder();
        sb.append("");
        double intValue = Shj.getWallet().getCatchMoney().intValue();
        Double.isNaN(intValue);
        sb.append(intValue / 100.0d);
        button.setText(replace.replace("#X#", sb.toString()));
    }

    void showWaitPopView() {
        BFPopView findPopView = BFPopView.findPopView("MainActivity", "PopView_EnterCode");
        if (findPopView != null && findPopView.isShowing()) {
            findPopView.close();
        }
        if (BFPopView.getShowingPopViewCount() == 0) {
            BFPopView.showPopView("MainActivity", "PopView_Wait", (SysApp.sysModel.getUiType() == SysModel.UI_TYPE_BQL_EN ? PopView_Wait_en.class : PopView_Wait_st.class).getName(), "", 0);
        }
    }

    private void initDatas() {
        if (ShjManager.isShjStarted()) {
            return;
        }
        VmdHelper.get().connectVmd(new VmdHelper.VmdConnectLisener() { // from class: com.xyshj.machine.activity.SplashActivity.3
            @Override // com.xyshj.machine.app.VmdHelper.VmdConnectLisener
            public void onConnectResult(boolean z, String str) {
                SplashActivity.this.updateMachineId();
            }
        });
        Shj.setDebug(true);
        Shj.setStoreGoodsInfoInVMC(false);
        ShjManager.startShj(new MyShjStatusListener(), new MyMoneyListener(), new MyOrderListener(), new MyGoodsStatusListener(), BitmapFactory.decodeResource(getResources(), R.drawable.ico_tuzhi1), "000000", true);
        Shj.setDebugOfferShelfByThirdApi(true);
        ShjManager.getGoodsManager().setDefaultGoodsMarkImage(BitmapFactory.decodeResource(getResources(), R.drawable.ico_tuzhi1));
        if (Shj.getMachineId().equals("1707600007") || Shj.getMachineId().equals("1707600008")) {
//            BqlManager.get().setDebug(true);
        }
        VmdHelper.get().initDatas();
    }

    void initTimer() {
        VmdHelper.get().synServer();
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        Timer timer2 = new Timer();
        this.timer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.xyshj.machine.activity.SplashActivity.4
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    SplashActivity.this.handler.post(new Runnable() { // from class: com.xyshj.machine.activity.SplashActivity.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            String string = ShjAppHelper.getString(AndroidSystem.getMobileDbm(ShjAppHelper.getMainActivity()) > -70 ? R.string.strong : R.string.weak);
                            ((TextView) SplashActivity.this.findViewById(R.id.net_type)).setText(AndroidSystem.GetNetworkType(ShjAppHelper.getMainActivity()) + "(" + string + ")");
                        }
                    });
                } catch (Exception unused) {
                }
                VmdHelper.get().checkAndReportError();
                if (VmdHelper.get().checkServerSynOk()) {
                    if (VmdHelper.get().isOutCleanCheck()) {
                        Loger.writeLog("SHJ", "需要清洗了,离上次清洗已超" + VmdHelper.get().getCleanCheckTime() + "小时");
                        PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) "outcleancheckid").put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("info", (Object) ShjAppHelper.getString(R.string.st_bql_clean_check_stoped)).put("time_out", (Object) 60000).put("showTime", (Object) false));
                        return;
                    }
                    Loger.writeLog("SHJ", "isBqlPowOn:" + VmdHelper.get().isBqlPowOn());
                    if (VmdHelper.get().isBqlPowOn()) {
                        long currentTimeMillis = System.currentTimeMillis();
                        VmdHelper.get().doCheckBqlStatusEx();
                        try {
                            Thread.sleep(2000L);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Loger.writeLog("SHJ", "isNeedStopBql:" + VmdHelper.get().isNeedStopBql());
                        if (VmdHelper.get().isNeedStopBql()) {
                            VmdHelper.get().doBqlGuoye();
                            SplashActivity.this.lastTouchTime = currentTimeMillis;
                        } else {
                            VmdHelper.get().doCheckBqlStatusEx();
                            try {
                                Thread.sleep(500L);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            if (!VmdHelper.get().isBqlQingxi() && (VmdHelper.get().isBqlStoped() || VmdHelper.get().isBqlGuoye())) {
                                Loger.writeLog("SHJ", "60秒检查 bqlStoped:" + VmdHelper.get().isBqlStoped() + " bqlGuoye:" + VmdHelper.get().isBqlGuoye());
                                VmdHelper.get().doBqlZhigao();
                            }
                        }
                    }
                    try {
                        VmdHelper.get().updateLightStatus(VmdHelper.get().isNeedOpenBqlLight());
                    } catch (Exception unused2) {
                    }
                }
            }
//        }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS, 60000L);
        }, 60000L);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
        } catch (Exception unused) {
        } catch (Throwable th) {
            this.lastTouchTime = System.currentTimeMillis();
            ShjManager.updateLastTouchTime();
            throw th;
        }
        if (System.currentTimeMillis() - this.lastTouchTime < 100) {
            boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
            this.lastTouchTime = System.currentTimeMillis();
            ShjManager.updateLastTouchTime();
            return dispatchTouchEvent;
        }
        if (motionEvent.getRawY() < 200.0f && motionEvent.getRawX() > CommonTool.screenWidth(this) - 220) {
            if (System.currentTimeMillis() - this.lastOpenSettingTouchTime < 1000) {
                long j = this.openSettingtouchCount + 1;
                this.openSettingtouchCount = j;
                if (j > 4) {
                    this.openSettingtouchCount = 0L;
                }
            } else {
                this.openSettingtouchCount = 0L;
            }
            if (this.openSettingtouchCount == 4) {
                if (Shj.isDebug() || !Shj.isVMCConnected()) {
                    Intent intent = new Intent(this, (Class<?>) SettingActivity.class);
                    intent.putExtra("showLoginDialog", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                this.lastOpenSettingTouchTime = 0L;
            } else {
                this.lastOpenSettingTouchTime = System.currentTimeMillis();
            }
        }
        this.lastTouchTime = System.currentTimeMillis();
        ShjManager.updateLastTouchTime();
        return super.dispatchTouchEvent(motionEvent);
    }

    public void showSettingLoginView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(ShjAppHelper.getString(R.string.lab_theapp));
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.leftMargin = 40;
        layoutParams.rightMargin = 40;
        final EditText editText = new EditText(this);
        final TextView textView = new TextView(this);
        editText.setHint(ShjAppHelper.getString(R.string.lab_callphone));
        editText.setLayoutParams(layoutParams);
        editText.setInputType(SettingType.COMMODITY_ONE_BUTTON_SETUP);
        editText.addTextChangedListener(new TextWatcher() { // from class: com.xyshj.machine.activity.SplashActivity.5
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                textView.setText("");
            }
        });
        linearLayout.addView(editText);
        textView.setLayoutParams(new LinearLayout.LayoutParams(-1, 44));
//        textView.setTextColor(SupportMenu.CATEGORY_MASK);
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);
        builder.setView(linearLayout);
        builder.setNegativeButton(ShjAppHelper.getString(R.string.button_cancel), new DialogInterface.OnClickListener() { // from class: com.xyshj.machine.activity.SplashActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        });
        builder.setPositiveButton(ShjAppHelper.getString(R.string.button_ok), new DialogInterface.OnClickListener() { // from class: com.xyshj.machine.activity.SplashActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    String asString = CacheHelper.getFileCache().getAsString("passWord");
                    if (asString == null || asString.length() == 0) {
                        asString = "888888";
                    }
                    try {
                        if (editText.getText().toString().equals(asString)) {
                            ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            ShjAppHelper.enterSetting(SplashActivity.this, false);
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } else {
                            textView.setText(SplashActivity.this.getResources().getString(R.string.lab_please2pwderror));
                            Field declaredField2 = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField2.setAccessible(true);
                            declaredField2.set(dialogInterface, false);
                        }
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        if (SysApp.isWxFacePayInstalled) {
            WxFacePayHelper.releaseWxpayface(this);
        }
        super.onDestroy();
    }
};
