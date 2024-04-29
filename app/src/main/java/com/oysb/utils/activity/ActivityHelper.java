package com.oysb.utils.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
//import com.alipay.zoloz.smile2pay.service.Zoloz;
import com.oysb.utils.Constant;
import com.oysb.utils.Loger;
import com.oysb.utils.dialog.LockScreenDialog;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ActivityHelper {
    static ArrayList<Activity> activities = new ArrayList<>();
    private static LockScreenDialog lockScreenDialog;

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void finishActivitys() {
        Iterator<Activity> it = activities.iterator();
        while (it.hasNext()) {
            Activity next = it.next();
            if (!next.isFinishing() && !next.isDestroyed()) {
                try {
                    next.finish();
                } catch (Exception unused) {
                }
            }
        }
    }

    public static ComponentName getTopActivity(Context context) {
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(Integer.MAX_VALUE).get(0).topActivity;
    }

    public static void startActivity(Context context, ComponentName componentName) {
        if (componentName.equals(getTopActivity(context))) {
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(componentName);
        context.startActivity(intent);
    }


    public static boolean isForeground(Context context) {
        if (context == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
            if (runningAppProcessInfo.processName.equals(context.getPackageName()) && runningAppProcessInfo.importance == 100) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkApkExist(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(str, 8192);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static boolean isFacePayExist(Context context) {
//        return checkApkExist(context, Zoloz.SMILE2PAY_PACKAGE);
        return true;
    }

    public static boolean isWxFacePayExist(Context context) {
        return checkApkExist(context, "com.tencent.wxpayface");
    }

    public static boolean isLand(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    public static void getAppVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        for (Constant.ConfigurePackageInfo configurePackageInfo : Constant.needPackageNames) {
            if (checkApkExist(context, configurePackageInfo.packageName)) {
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(configurePackageInfo.packageName, 128);
                    PackageInfo packageInfo = packageManager.getPackageInfo(configurePackageInfo.packageName, 0);
                    String str = packageInfo.versionName;
                    int i = packageInfo.versionCode;
                    String charSequence = packageManager.getApplicationLabel(applicationInfo).toString();
                    Loger.writeLog("APP", "appName=" + charSequence + " packageName=" + configurePackageInfo.packageName + " versionName=" + str + " versionCode=" + i);
                    Log.i("APP", "appName=" + charSequence + " packageName=" + configurePackageInfo.packageName + " versionName=" + str + " versionCode=" + i);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void showLockScreenDialog(Context context, String str) {
        if (lockScreenDialog == null) {
            lockScreenDialog = new LockScreenDialog(context);
        }
        if (!TextUtils.isEmpty(str)) {
            lockScreenDialog.setMessage(str);
        }
        if (lockScreenDialog.isShowing()) {
            return;
        }
        lockScreenDialog.show();
    }

    public static void closeLockScreenDialog() {
        LockScreenDialog lockScreenDialog2 = lockScreenDialog;
        if (lockScreenDialog2 != null) {
            lockScreenDialog2.dismiss();
        }
    }
}
