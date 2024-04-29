package com.oysb.utils.io;

import static com.shj.Shj.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

//import com.alipay.zoloz.smile2pay.service.Zoloz;
import com.github.mjdev.libaums.fs.UsbFile;
//import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.Constant;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.io.file.SDFileUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class AppUpdateHelper {
    static String appType = null;
    static String currentVersion = null;
    static int downProgress = 0;
    static DownloadThread downloadThread = null;
    static DownlaodListener listener = null;
    static String machineCode = null;
    static String packageName = null;
    static String remark = "";
    static int status = -1;
    static String updateVersion;

    public static void init(Context context, String str, String str2) {
        try {
            machineCode = str;
            appType = str2;
            packageName = context.getPackageName();
            PackageManager packageManager = context.getPackageManager();
            currentVersion = packageManager.getPackageInfo(packageName, 0).versionName + "." + String.format("%06d", Integer.valueOf(packageManager.getPackageInfo(packageName, 0).versionCode));
        } catch (Exception unused) {
        }
    }

    /* loaded from: classes2.dex */
    public static class MyDownLoadListenter implements DownlaodListener {
        MyDownLoadListenter() {
        }

        @Override // com.oysb.utils.io.DownlaodListener
        public void startDown() {
            AppUpdateHelper.status = 0;
            AppUpdateHelper.remark = "开始下载";
            AppUpdateHelper.reportAppUadate(AppUpdateHelper.machineCode, AppUpdateHelper.packageName, AppUpdateHelper.appType, AppUpdateHelper.currentVersion, AppUpdateHelper.updateVersion);
        }

        @Override // com.oysb.utils.io.DownlaodListener
        public void downEnd() {
            AppUpdateHelper.status = 2;
            AppUpdateHelper.remark = "下载完成";
            AppUpdateHelper.reportAppUadate(AppUpdateHelper.machineCode, AppUpdateHelper.packageName, AppUpdateHelper.appType, AppUpdateHelper.currentVersion, AppUpdateHelper.updateVersion);
        }

        @Override // com.oysb.utils.io.DownlaodListener
        public void downError(String str) {
            AppUpdateHelper.status = -1;
            AppUpdateHelper.remark = str;
            AppUpdateHelper.reportAppUadate(AppUpdateHelper.machineCode, AppUpdateHelper.packageName, AppUpdateHelper.appType, AppUpdateHelper.currentVersion, AppUpdateHelper.updateVersion);
        }

        @Override // com.oysb.utils.io.DownlaodListener
        public void onProgress(boolean z, int i) {
            AppUpdateHelper.downProgress = i;
            if (z) {
                return;
            }
            AppUpdateHelper.status = 1;
            AppUpdateHelper.remark = "下载中";
            if (AppUpdateHelper.downProgress % 25 == 0) {
                AppUpdateHelper.reportAppUadate(AppUpdateHelper.machineCode, AppUpdateHelper.packageName, AppUpdateHelper.appType, AppUpdateHelper.currentVersion, AppUpdateHelper.updateVersion);
            }
        }
    }

    public static void reportAppUadate(String str, String str2, String str3, String str4, String str5) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("machineCode", str);
            jSONObject.put("packageName", str2);
            jSONObject.put("appType", str3);
            jSONObject.put("currentVersion", str4);
            jSONObject.put("updateVersion", str5);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("status", status);
            jSONObject2.put("downProgress", downProgress);
            jSONObject2.put("remark", remark);
//            jSONObject.put(SpeechEvent.KEY_EVENT_RECORD_DATA, jSONObject2);
            Loger.writeLog("APP", "下载上报参数：" + jSONObject.toString());
            RequestItem requestItem = new RequestItem("http://www.xynetweb.cn:8090/service-app/shjty/reportDownProgress", jSONObject, "POST");
            requestItem.setRepeatDelay(5000);
            requestItem.setRequestMaxCount(1);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.oysb.utils.io.AppUpdateHelper.1
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str6, Throwable th) {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }



                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str6) {
                    try {
                        JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(new JSONObject(str6), "reportAppUadate");
                        Loger.writeLog("APP", "下载上报：" + str6);
                        updateVersionedRequestResult.getString("code").equalsIgnoreCase("H0000");
                        return true;
                    } catch (Exception unused) {
                        return true;
                    }
                }
            });
            RequestHelper.request(requestItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* renamed from: com.oysb.utils.io.AppUpdateHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str6, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass1() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str6) {
            try {
                JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(new JSONObject(str6), "reportAppUadate");
                Loger.writeLog("APP", "下载上报：" + str6);
                updateVersionedRequestResult.getString("code").equalsIgnoreCase("H0000");
                return true;
            } catch (Exception unused) {
                return true;
            }
        }
    }

    public static void reportAppInstall(String str, String str2, String str3, String str4, String str5, String str6, int i, String str7) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("machineCode", str);
            jSONObject.put(IjkMediaMeta.IJKM_KEY_TYPE, str2);
            jSONObject.put("packageName", str3);
            jSONObject.put("appType", str4);
            jSONObject.put("currentVersion", str5);
            jSONObject.put("newsVersion", str6);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("status", i);
            jSONObject2.put("remark", str7);
            jSONObject.put(SpeechEvent.KEY_EVENT_RECORD_DATA, jSONObject2);
            Loger.writeLog("APP", "安装上报参数：" + jSONObject.toString());
            RequestItem requestItem = new RequestItem("http://www.xynetweb.cn:8090/service-app/shjty/reportInstall", jSONObject, "POST");
            requestItem.setRepeatDelay(5000);
            requestItem.setRequestMaxCount(1);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.oysb.utils.io.AppUpdateHelper.2
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i2, String str8, Throwable th) {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }


                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i2, String str8) {
                    try {
                        JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(new JSONObject(str8), "reportAppInstall");
                        Loger.writeLog("APP", "安装上报：" + str8);
                        updateVersionedRequestResult.getString("code").equalsIgnoreCase("H0000");
                        return true;
                    } catch (Exception unused) {
                        return true;
                    }
                }
            });
            RequestHelper.request(requestItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* renamed from: com.oysb.utils.io.AppUpdateHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i2, String str8, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass2() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i2, String str8) {
            try {
                JSONObject updateVersionedRequestResult = RequestHelper.updateVersionedRequestResult(new JSONObject(str8), "reportAppInstall");
                Loger.writeLog("APP", "安装上报：" + str8);
                updateVersionedRequestResult.getString("code").equalsIgnoreCase("H0000");
                return true;
            } catch (Exception unused) {
                return true;
            }
        }
    }

    public static void startDownlaodTask(String str, String str2, String str3, DownlaodListener downlaodListener) {
        String trim;
        updateVersion = str3;
        listener = downlaodListener;
        if (downlaodListener == null) {
            listener = new MyDownLoadListenter();
        }
        DownloadThread downloadThread2 = downloadThread;
        if (downloadThread2 != null && downloadThread2.isRunning()) {
            DownloadInfo downloadInfo = downloadThread.getDownloadInfo();
            if (downloadInfo != null && downloadInfo.getUrl() == null && downloadInfo.getUrl().equalsIgnoreCase(str2)) {
                return;
            } else {
                downloadThread.stopTask();
            }
        }
        DownloadInfo downloadInfo2 = null;
        downloadThread = null;
        if (str2.contains("&fn=") && str2.contains(".apk&")) {
            int indexOf = str2.indexOf("&fn=") + 4;
            trim = str2.substring(indexOf, str2.indexOf(".apk&", indexOf) + 4).trim();
        } else {
            trim = SDFileUtils.getFileName(str2).trim();
        }
        if (new File(str + UsbFile.separator + trim).exists()) {
            return;
        }
        try {
            downloadInfo2 = (DownloadInfo) ObjectHelper.loadObject(str + UsbFile.separator + trim + DownloadInfo.afxDownloadinfo);
        } catch (Exception unused) {
        }
        if (downloadInfo2 == null) {
            downloadInfo2 = new DownloadInfo();
        }
        downloadInfo2.setUrl(str2);
        downloadInfo2.setFileDir(str);
        DownloadThread downloadThread3 = new DownloadThread(downloadInfo2, listener);
        downloadThread = downloadThread3;
        downloadThread3.start();
    }

    public static void startDownloadLibsTask(String str, String str2, DownlaodListener downlaodListener) {
        DownloadThread downloadThread2 = downloadThread;
        if (downloadThread2 != null && downloadThread2.isRunning()) {
            DownloadInfo downloadInfo = downloadThread.getDownloadInfo();
            if (downloadInfo != null && downloadInfo.getUrl() == null && downloadInfo.getUrl().equalsIgnoreCase(str2)) {
                return;
            } else {
                downloadThread.stopTask();
            }
        }
        DownloadInfo downloadInfo2 = null;
        downloadThread = null;
        if (new File(str + UsbFile.separator + "xyshj_libs.zip").exists()) {
            downlaodListener.onProgress(true, 100);
            return;
        }
        try {
            downloadInfo2 = (DownloadInfo) ObjectHelper.loadObject(str + UsbFile.separator + "xyshj_libs.zip" + DownloadInfo.afxDownloadinfo);
        } catch (Exception unused) {
        }
        if (downloadInfo2 == null) {
            downloadInfo2 = new DownloadInfo();
        }
        downloadInfo2.setUrl(str2);
        downloadInfo2.setFileDir(str);
        DownloadThread downloadThread3 = new DownloadThread(downloadInfo2, downlaodListener);
        downloadThread = downloadThread3;
        downloadThread3.start();
    }


    public static boolean check2UpdateApp(Context context, String str) {
        return check2UpdateAppFromPackageName(context, str, context.getPackageName());
    }

    public static boolean checkFacePayUpdateApp(Context context, String str) {
        ArrayList<String> packageNames = new ArrayList<>();
        packageNames.add("com.alipay.iot.master");
        packageNames.add(Zoloz.SMILE2PAY_PACKAGE);
        packageNames.add("com.alipay.iot.service");
        packageNames.add("com.tencent.wxpayface");

        PackageManager packageManager = context.getPackageManager();
        for (String packageName : packageNames) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                if (get2UpdateApk(context, str, packageName, packageInfo.versionName, packageInfo.versionCode, false) != null) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Package Error", e.toString());
            }
        }
        return false;
    }


    public static boolean facePay2UpdateApp(Context context, String str) {
        ArrayList<String> packageNames = new ArrayList<>();
        packageNames.add("com.alipay.iot.master");
        packageNames.add(Zoloz.SMILE2PAY_PACKAGE);
        packageNames.add("com.alipay.iot.service");
        packageNames.add("com.tencent.wxpayface");

        boolean hasUpdated = false;
        for (String packageName : packageNames) {
            hasUpdated = hasUpdated || check2UpdateAppFromPackageName(context, str, packageName);
        }
        return hasUpdated;
    }


    public static void clearUpdateCount() {
        for (Constant.ConfigurePackageInfo configurePackageInfo : com.oysb.utils.Constant.getNeedPackageNames()) {
            if ((configurePackageInfo.type & com.oysb.utils.Constant.TYPE_UPGRADE) == com.oysb.utils.Constant.TYPE_UPGRADE) {
                CacheHelper.getFileCache().remove("lastUpdateApp" + configurePackageInfo.packageName);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0116  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean check2UpdateAppFromPackageName(android.content.Context r14, java.lang.String r15, java.lang.String r16) {
        /*
            Method dump skipped, instructions count: 303
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.io.AppUpdateHelper.check2UpdateAppFromPackageName(android.content.Context, java.lang.String, java.lang.String):boolean");
    }

    /* renamed from: com.oysb.utils.io.AppUpdateHelper$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Loger.writeLog("APP", "app 重启" + Log.getStackTraceString(new Throwable()));
            Loger.flush();
            System.exit(0);
        }
    }

    /* renamed from: com.oysb.utils.io.AppUpdateHelper$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements InstallAppListener {
        private final String packageName;
        private final PackageInfo packageInfo;
        private final String version;

        public AnonymousClass4(String packageName, PackageInfo packageInfo, String version) {
            this.packageName = packageName;
            this.packageInfo = packageInfo;
            this.version = version;
        }

        @Override
        public void onProgress(boolean success, int progress) {
            // No action needed
        }

        @Override
        public void onStart() {
            AppUpdateHelper.reportAppInstall(
                    AppUpdateHelper.machineCode,
                    "1",
                    packageName,
                    AppUpdateHelper.appType,
                    packageInfo.versionName + "." + packageInfo.versionCode,
                    version,
                    0,
                    ""
            );
        }

        @Override
        public void onSuccess(String message) {
            CacheHelper.getFileCache().remove("lastUpdateApp" + packageName);
            AppUpdateHelper.reportAppInstall(
                    AppUpdateHelper.machineCode,
                    "1",
                    packageName,
                    AppUpdateHelper.appType,
                    packageInfo.versionName + "." + packageInfo.versionCode,
                    version,
                    1,
                    message
            );
        }

        @Override
        public void onError(String errorMessage) {
            AppUpdateHelper.reportAppInstall(
                    AppUpdateHelper.machineCode,
                    "1",
                    packageName,
                    AppUpdateHelper.appType,
                    packageInfo.versionName + "." + packageInfo.versionCode,
                    version,
                    -1,
                    errorMessage
            );
        }
    }


    public static void clearAppPackages(Context context, String str) {
        try {
            context.getPackageManager();
            SDFileUtils.creatDataDir(str);
            for (File file : SDFileUtils.getFiles(str, "")) {
                try {
                    Loger.writeLog("APP", "delete file at clearAppPackages:" + file.getAbsolutePath());
                    file.delete();
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void clearAppPackages(Context context, String str, String str2) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(str, 0);
            SDFileUtils.creatDataDir(str2);
            List<File> files = SDFileUtils.getFiles(str2, "apk");
            files.addAll(SDFileUtils.getFiles(str2, "APK"));
            String str3 = packageInfo.versionName + "." + String.format("%06d", Integer.valueOf(packageInfo.versionCode));
            for (File file : files) {
                try {
                    PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(file.getAbsolutePath(), 1);
                    if (str.equalsIgnoreCase(packageArchiveInfo.packageName)) {
                        int compareToIgnoreCase = (packageArchiveInfo.versionName + "." + String.format("%06d", Integer.valueOf(packageArchiveInfo.versionCode))).compareToIgnoreCase(str3);
                        System.currentTimeMillis();
                        file.lastModified();
                        if (compareToIgnoreCase < 0) {
                            Loger.writeLog("APP", "delete file at clearAppPackages:" + file.getAbsolutePath());
                            file.delete();
                        }
                    }
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static boolean checkInstalledAppVersion(Context context, String str, String str2, int i) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 0);
            if (packageInfo.versionName.equalsIgnoreCase(str2)) {
                return packageInfo.versionCode == i;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String get2UpdateApk(Context context, String directory, String packageName, String versionName, int versionCode, boolean isUpdate) {
        Loger.writeLog("APP", directory + StringUtils.SPACE + packageName + StringUtils.SPACE + versionName + StringUtils.SPACE + versionCode + StringUtils.SPACE + isUpdate);
        try {
            SDFileUtils.creatDataDir(directory);
            List<File> apkFiles = new ArrayList<>(SDFileUtils.getFiles(directory, "apk"));
            apkFiles.addAll(SDFileUtils.getFiles(directory, "APK"));
            PackageManager packageManager = context.getPackageManager();
            String targetVersion = versionName + "." + String.format("%06d", versionCode);
            File targetFile = null;
            for (File file : apkFiles) {
                PackageInfo packageInfo = null;
                try {
                    packageInfo = packageManager.getPackageArchiveInfo(file.getAbsolutePath(), 1);
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                    Loger.writeException("APP", e);
                }
                if (packageInfo != null) {
                    Loger.writeLog("APP", packageInfo.packageName);
                    if (packageName.equalsIgnoreCase(packageInfo.packageName)) {
                        String apkVersion = packageInfo.versionName + "." + String.format("%06d", packageInfo.versionCode);
                        int comparisonResult = apkVersion.compareToIgnoreCase(targetVersion);
                        Loger.writeLog("APP", "ver:" + apkVersion + " cmp:" + comparisonResult);
                        if ((isUpdate && comparisonResult >= 0) || (!isUpdate && comparisonResult > 0)) {
                            targetFile = file;
                            targetVersion = apkVersion;
                        }
                    }
                } else {
                    SDFileUtils.safeDeleteFile(file);
                }
            }
            Loger.writeLog("APP", "v:" + targetFile);
            return targetFile != null ? targetFile.getAbsolutePath() : null;
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            return null;
        }
    }


    /* renamed from: com.oysb.utils.io.AppUpdateHelper$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 extends RunnableEx {
        final String apkFile;
        final InstallAppListener listener;

        AnonymousClass5(Object obj, InstallAppListener installAppListener, String str) {
            super(obj);
            this.listener = installAppListener;
            this.apkFile = str;
        }

        @Override
        public void run() {
            try {
                listener.onStart();
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(Uri.parse("file://" + apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                listener.onSuccess(apkFile);
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                listener.onError(e.getMessage());
            }
        }
    }


    public static void silentInstall(String apkFile, InstallAppListener listener) {
        Loger.writeLog("APP;UI", "install:" + apkFile);
        new Thread(() -> {
            try {
                listener.onStart();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                listener.onSuccess(apkFile);
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                listener.onError(e.getMessage());
            }
        }).start();
    }


    public static int getVersionCode(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getVersionName(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
