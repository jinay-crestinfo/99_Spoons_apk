package com.xyshj.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.oysb.utils.Loger;
import com.oysb.utils.activity.ActivityHelper;
import com.tencent.wxpayface.WxfacePayCommonCode;
import com.xyshj.fragment.BaseFragment;
import com.xyshj.fragment.MainFragmentManager;
import com.xyshj.machine.app.VmdHelper;
import com.xyshj.machine.model.SysModel;

/* loaded from: classes.dex */
public class ShjAppBase extends MultiDexApplication {
    public static final int MainActivity_contnetView_id = 1000;
    public static boolean isBoxlunch = false;
    public static boolean isFacePayInstalled = false;
    public static boolean isOther = false;
    public static boolean isWxFacePayInstalled = false;
    public static BaseFragment mainFragment;
    public static MainFragmentManager mainFragmentManager;
    public static long mainThreadId;
    public static ShjAppBase sysApp;
    public static SysModel sysModel;

    public static ShjAppBase getInstance() {
        return sysApp;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.multidex.MultiDexApplication, android.content.ContextWrapper
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        try {
            sysApp = this;
            sysModel = new SysModel();
            mainThreadId = Thread.currentThread().getId();
            sysModel.setVirtualMode(false);
            isWxFacePayInstalled = ActivityHelper.isWxFacePayExist(this);
            ShjAppHelper.init(sysApp);
            mainFragmentManager = new MainFragmentManager();
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            e.printStackTrace();
        }
    }

    private String getAppType(Context context) {
        try {
            return getPackageManager().getApplicationInfo(getPackageName(), 128).metaData.getString("APP_TYPE");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return VmdHelper.BQL_TYPE_NORMAL;
        }
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onLowMemory() {
        Loger.writeLog(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, "APP内存不足***");
        Loger.flush();
        super.onLowMemory();
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
        Loger.writeLog("APP", "onTerminate");
    }

    public static void quite() {
        sysApp.onTerminate();
        Loger.writeLog("APP", "quite");
    }
}
