package com.oysb.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.serialport.SerialPort;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.oysb.utils.date.DateUtil;
//import com.tencent.wxpayface.WxfacePayCommonCode;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class AndroidSystem {
    private static final String TAG = "RootUtil";
    static boolean needRebootSystem = false;
    static String needRebootSystemReson = "";
    static boolean needRestartApp = false;
    static String needRestartAppReson = "";

    public static void setSystemTime(Context context, Date date) {
    }

    public static int getNavigationBarHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= 17) {
            Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            Point point2 = new Point();
            defaultDisplay.getSize(point);
            defaultDisplay.getRealSize(point2);
            Resources resources = activity.getResources();
            int dimensionPixelSize = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
            if (point2.y - point.y > dimensionPixelSize - 10) {
                return dimensionPixelSize;
            }
            return 0;
        }
        boolean hasPermanentMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        boolean deviceHasKey = KeyCharacterMap.deviceHasKey(4);
        if (hasPermanentMenuKey || deviceHasKey) {
            return 0;
        }
        Resources resources2 = activity.getResources();
        return resources2.getDimensionPixelSize(resources2.getIdentifier("navigation_bar_height", "dimen", "android"));
    }

    public static String getSystemStartTime_str() {
        return DateUtil.format(new Date(System.currentTimeMillis() - SystemClock.elapsedRealtime()), "yyyy-MM-dd HH:mm:ss");
    }

    public static long getSystemStartTime() {
        return System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:44:0x005f -> B:10:0x007e). Please report as a decompilation issue!!! */

    public static class SystemTimeUtil {

        public static void setSystemTime(String str) {
            Process process = null;
            DataOutputStream dataOutputStream = null;
            try {
                process = Runtime.getRuntime().exec("su");
                dataOutputStream = new DataOutputStream(process.getOutputStream());
                dataOutputStream.writeBytes("setprop persist.sys.timezone GMT\n");
                dataOutputStream.writeBytes("/system/bin/date -s " + str + "\n");
                dataOutputStream.writeBytes("clock -w\n");
                dataOutputStream.writeBytes("exit\n");
                dataOutputStream.flush();
                Loger.writeLog("NET", "Executed su command to set system time: " + str);
            } catch (IOException e) {
                Loger.writeException("WxFacePayError", e);
            } finally {
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        Loger.writeException("WxFacePayError", e);
                    }
                }
                if (process != null) {
                    process.destroy();
                }
            }
        }
    }


    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getSystemBuildDisplay() {
        return Build.DISPLAY;
    }

    public static String getSystemSerial() {
        return Build.SERIAL;
    }

    public static String getSystemModelType() {
        return Build.MODEL;
    }

    public static String getSystemHadVersion() {
        return Build.BRAND;
    }

    public static double getFreeMemery(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            double d = memoryInfo.availMem;
            Double.isNaN(d);
            return ((d * 1.0d) / 1024.0d) / 1024.0d;
        } catch (Exception unused) {
            return -1.0d;
        }
    }

    public static double getTotalMemery(Context context) {
        try {
            double longValue = Long.valueOf(new BufferedReader(new FileReader("/proc/meminfo")).readLine().split("\\s+")[1]).longValue();
            Double.isNaN(longValue);
            return (longValue * 1.0d) / 1024.0d;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1.0d;
        } catch (IOException e2) {
            e2.printStackTrace();
            return -1.0d;
        }
    }

    public static double getTotalSpace() {
        try {
            double totalSpace = Environment.getExternalStorageDirectory().getTotalSpace();
            Double.isNaN(totalSpace);
            return (((totalSpace * 1.0d) / 1024.0d) / 1024.0d) / 1024.0d;
        } catch (Exception unused) {
            return -1.0d;
        }
    }

    public static double getFreeSpace() {
        try {
            double freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
            Double.isNaN(freeSpace);
            return (((freeSpace * 1.0d) / 1024.0d) / 1024.0d) / 1024.0d;
        } catch (Exception unused) {
            return -1.0d;
        }
    }

    public static int screenWidth(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        } catch (Exception unused) {
            return -1;
        }
    }

    public static int screenHeight(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        } catch (Exception unused) {
            return -1;
        }
    }

    public static boolean isDeviceHasNavigationBar(Context context) {
        boolean z = false;
        try {
            Resources resources = context.getResources();
            int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
            boolean z2 = identifier > 0 ? resources.getBoolean(identifier) : false;
            try {
                Class<?> cls = Class.forName("android.os.SystemProperties");
                String str = (String) cls.getMethod("get", String.class).invoke(cls, "qemu.hw.mainkeys");
                if (!"1".equals(str)) {
                    z = "0".equals(str) ? true : z2;
                }
                return z;
            } catch (Exception unused) {
                return z2;
            }
        } catch (Exception unused2) {
            return false;
        }
    }

    public static int getVirtualBarHeigh(Context context) {
        try {
            Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            try {
                Class.forName("android.view.Display").getMethod("getRealMetrics", DisplayMetrics.class).invoke(defaultDisplay, displayMetrics);
                return displayMetrics.heightPixels - ((Activity) context).getWindow().getDecorView().getHeight();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } catch (Exception unused) {
            return -1;
        }
    }

    public static String getApkStateInfo(Context context, String str, String str2) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(str, 0);
        } catch (Exception unused) {
            packageInfo = null;
        }
        if (packageInfo == null) {
            return str2 + "(" + str + "):-:未安装";
        }
        String str3 = str2 + "(" + str + "):" + packageInfo.versionName + "." + packageInfo.versionCode;
        if (str.equalsIgnoreCase(com.shj.setting.Utils.Constant.safeAppName)) {
            String launcherPackageName = getLauncherPackageName(context);
            if (launcherPackageName.length() <= 0) {
                return str3;
            }
            if (str.equals(launcherPackageName)) {
                return str3 + ":已设为启动程序";
            }
            return str3 + ":未设为启动程序";
        }
        return str3 + ":已安装";
    }

    public static String getLanguage(Context context) {
        String str;
        Locale locale = context.getResources().getConfiguration().locale;
        try {
            str = locale.getLanguage().split("_")[0];
        } catch (Exception unused) {
            str = "";
        }
        if (str.length() != 0) {
            return str;
        }
        try {
            return locale.getLanguage().split("-")[1];
        } catch (Exception unused2) {
            return str;
        }
    }

    public static boolean isTimeZoneAuto(Context context) {
        try {
            return Settings.Global.getInt(context.getContentResolver(), "auto_time_zone") > 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setAutoTimeZone(Context context, int i) {
        Settings.Global.putInt(context.getContentResolver(), "auto_time_zone", i);
    }

    public static String getTimeZone() {
        return TimeZone.getDefault().getDisplayName(false, 0);
    }

    public static boolean isDateTimeAuto(Context context) {
        try {
            return Settings.Global.getInt(context.getContentResolver(), "auto_time") > 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setAutoDateTime(Context context, int i) {
        Settings.Global.putInt(context.getContentResolver(), "auto_time", i);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static String GetNetworkType(Context context) {
        String string = context.getResources().getString(R.string.cutting_net);
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getType() != 1) {
                    if (activeNetworkInfo.getType() == 9) {
                        string = context.getResources().getString(R.string.netline);
                    } else if (activeNetworkInfo.getType() == 0) {
                        String subtypeName = activeNetworkInfo.getSubtypeName();
                        switch (activeNetworkInfo.getSubtype()) {
                            case 1:
                            case 2:
                            case 4:
                            case 7:
                            case 11:
                                string = "2G";
                                break;
                            case 3:
                            case 5:
                            case 6:
                            case 8:
                            case 9:
                            case 10:
                            case 12:
                            case 14:
                            case 15:
                                string = "3G";
                                break;
                            case 13:
                                string = "4G";
                                break;
                            default:
                                if (!subtypeName.equalsIgnoreCase("TD-SCDMA") && !subtypeName.equalsIgnoreCase("WCDMA") && !subtypeName.equalsIgnoreCase("CDMA2000")) {
                                    string = subtypeName;
                                    break;
                                }
                                string = "3G";
                                break;
                        }
                    }
                } else {
                    string = "WIFI";
                }
            }
        } catch (Exception unused) {
        }
        return string;
    }

    public static int getMobileDbm(Context context) {
        TelephonyManager telephonyManager = null;
        try {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        } catch (Exception e) {
            // Handle exception or log if needed
        }

        if (telephonyManager == null || ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            return -1;
        }

        List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
        int dbm = -1;

        if (Build.VERSION.SDK_INT >= 17 && allCellInfo != null) {
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoGsm) {
                    dbm = ((CellInfoGsm) cellInfo).getCellSignalStrength().getDbm();
                } else if (cellInfo instanceof CellInfoCdma) {
                    dbm = ((CellInfoCdma) cellInfo).getCellSignalStrength().getDbm();
                } else if (cellInfo instanceof CellInfoWcdma && Build.VERSION.SDK_INT >= 18) {
                    dbm = ((CellInfoWcdma) cellInfo).getCellSignalStrength().getDbm();
                } else if (cellInfo instanceof CellInfoLte) {
                    dbm = ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();
                }
            }
        }
        return dbm;
    }

    public static String getLauncherPackageName(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 0);
        return resolveActivity.activityInfo == null ? "" : resolveActivity.activityInfo.packageName;
    }

    public static boolean isSafeAppSetLauncher(Context context) {
        return com.shj.setting.Utils.Constant.safeAppName.equals(getLauncherPackageName(context));
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

    public static List<String> getMountPathList() {
        ArrayList arrayList = new ArrayList();
        try {
            Process exec = Runtime.getRuntime().exec("cat /proc/mounts");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(exec.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String str = TextUtils.split(readLine, StringUtils.SPACE)[1];
                File file = new File(str);
                if (file.isDirectory() && file.canRead() && file.canWrite()) {
                    arrayList.add(str);
                }
                if (exec.waitFor() != 0) {
                    exec.exitValue();
                }
            }
            bufferedReader.close();
            bufferedInputStream.close();
        } catch (Exception unused) {
            arrayList.add(Environment.getExternalStorageDirectory().getAbsolutePath());
        }
        return arrayList;
    }

    public static List<File> getFileList(String str) {
        ArrayList arrayList = new ArrayList();
        File[] listFiles = new File(str).listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                String name = listFiles[i].getName();
                if (listFiles[i].isDirectory()) {
                    getFileList(listFiles[i].getAbsolutePath());
                } else if (name.endsWith("apk")) {
                    listFiles[i].getAbsolutePath();
                    arrayList.add(listFiles[i]);
                }
            }
        }
        return arrayList;
    }

    public static String getApkPackageName(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(str, 1);
        if (packageArchiveInfo == null) {
            return null;
        }
        ApplicationInfo applicationInfo = packageArchiveInfo.applicationInfo;
        packageManager.getApplicationLabel(applicationInfo).toString();
        return applicationInfo.packageName;
    }

    public static String getApkVersionCode(Context context, String str) {
        PackageInfo packageArchiveInfo = context.getPackageManager().getPackageArchiveInfo(str, 1);
        if (packageArchiveInfo == null) {
            return "";
        }
        ApplicationInfo applicationInfo = packageArchiveInfo.applicationInfo;
        int i = packageArchiveInfo.versionCode;
        return packageArchiveInfo.versionName + "." + i;
    }

    public static boolean isRoot() {
        if (new File("/system/bin/su").exists() && isExecutable("/system/bin/su")) {
            return true;
        }
        return new File(SerialPort.DEFAULT_SU_PATH).exists() && isExecutable(SerialPort.DEFAULT_SU_PATH);
    }

    private static boolean isExecutable(String str) {
        Process process = null;
        try {
            try {
                process = Runtime.getRuntime().exec("ls -l " + str);
                String readLine = new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
                Log.i(TAG, readLine);
                if (readLine != null && readLine.length() >= 4) {
                    char charAt = readLine.charAt(3);
                    if (charAt == 's' || charAt == 'x') {
                        if (process != null) {
                            process.destroy();
                        }
                        return true;
                    }
                }
                if (process == null) {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (process == null) {
                    return false;
                }
            }
            process.destroy();
            return false;
        } catch (Throwable th) {
            if (process != null) {
                process.destroy();
            }
            throw th;
        }
    }

    public static void rebootSystem() {
        Loger.writeLog("UI", "su -c reboot");
        try {
            Runtime.getRuntime().exec("su -c reboot");
        } catch (Exception unused) {
        }
    }

    public static void restartApp(String str, int i) {
        Loger.writeLog("UI", "restartApp invoked:" + str);
        AppStatusLoger.addAppStatus(null, "APP", AppStatusLoger.Type_RestartApp, "01000001", "restartApp invoked:" + str);
        Loger.flush();
        if (i == 0) {
            System.exit(0);
        } else {
            new Timer().schedule(new TimerTask() { // from class: com.oysb.utils.AndroidSystem.1

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    System.exit(0);
                }
            }, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.AndroidSystem$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            System.exit(0);
        }
    }

    public static void rebootSystem(String str, int i) {
        Loger.writeLog("UI", "rebootSystem invoked:" + str);
        AppStatusLoger.addAppStatus(null, "APP", AppStatusLoger.Type_RebootSystem, "02000001", "rebootSystem invoked:" + str);
        Loger.flush();
        if (i == 0) {
            rebootSystem();
        } else {
            new Timer().schedule(new TimerTask() { // from class: com.oysb.utils.AndroidSystem.2


                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    AndroidSystem.rebootSystem();
                }
            }, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.AndroidSystem$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends TimerTask {

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            AndroidSystem.rebootSystem();
        }
    }

    public static void exitApp(String str, int i) {
        Loger.writeLog("UI", "exitApp invoked:" + str);
        AppStatusLoger.addAppStatus(null, "APP", AppStatusLoger.Type_ExitApp, "03000001", "exitApp invoked:" + str);
        Loger.flush();
        if (i == 0) {
            System.exit(0);
        } else {
            new Timer().schedule(new TimerTask() { // from class: com.oysb.utils.AndroidSystem.3

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    System.exit(0);
                }
            }, i);
        }
    }

    /* renamed from: com.oysb.utils.AndroidSystem$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 extends TimerTask {


        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            System.exit(0);
        }
    }

    public static void setNeedRestartApp(boolean z, String str) {
        needRestartApp = z;
        needRestartAppReson = str;
    }

    public static boolean isNeedRestartApp() {
        return needRestartApp;
    }

    public static String getNeedRestartAppReson() {
        return needRestartAppReson;
    }

    public static void setNeedRebootSystem(boolean z, String str) {
        needRebootSystem = z;
        needRebootSystemReson = str;
    }

    public static boolean isNeedRebootSystem() {
        return needRebootSystem;
    }

    public static String getNeedRebootSystemReson() {
        return needRebootSystemReson;
    }

    public static boolean isWifiEnable(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.isWifiEnabled();
    }

    public static boolean setWifiStatus(Context context, boolean z) {
        try {
            return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(z);
        } catch (Exception unused) {
            return false;
        }
    }
}
