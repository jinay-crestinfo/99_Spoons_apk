package com.xyshj.machine.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class DefaultLauncherUtils {
    public static String getLauncherPackageName(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 0);
        return resolveActivity.activityInfo == null ? "" : resolveActivity.activityInfo.packageName;
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
}
