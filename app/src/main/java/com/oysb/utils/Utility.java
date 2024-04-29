package com.oysb.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import java.security.SecureRandom;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Utility {
    private static final String LABEL_App_sign = "api_sign";
    private static final String LABEL_NONCE = "nonce";
    private static final String LABEL_TIME = "timestamp";
    private static final String LABEL_UID = "uid";
    private static final int MAX_NONCE = 10;
    private static final SecureRandom sRandom = new SecureRandom();
    private static char[] sHexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String getNonce() {
        byte[] bArr = new byte[5];
        sRandom.nextBytes(bArr);
        return hexString(bArr);
    }

    public static String hexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length <= 0) {
            return "";
        }

        char[] hexChars = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            int highNibble = (b >>> 4) & 0x0F;
            int lowNibble = b & 0x0F;
            hexChars[index++] = sHexDigits[highNibble];
            hexChars[index++] = sHexDigits[lowNibble];
        }

        return new String(hexChars);
    }


    private static long getTimestamp() {
        return new Date().getTime();
    }

    private static String getAPIsig(String str, long j, String str2, String str3) {
        String encode;
        StringBuilder sb = new StringBuilder();
        synchronized (sb) {
            sb.append(str);
            sb.append(j);
            sb.append(str2);
            sb.append(str3);
            encode = MD5.encode(sb.toString());
            sb.delete(0, sb.length());
        }
        return encode;
    }

    public static String getParams(String str) {
        String sb;
        try {
            String[] split = str.split(":");
            long timestamp = getTimestamp();
            String nonce = getNonce();
            String aPIsig = getAPIsig(str, timestamp, nonce, split[1]);
            StringBuilder sb2 = new StringBuilder();
            synchronized ("") {
                sb2.append(String.format("&uid=%s", split[1]));
                sb2.append(String.format("&nonce=%s", nonce));
                sb2.append(String.format("&timestamp=%s", Long.valueOf(timestamp)));
                sb2.append(String.format("&api_sign=%s", aPIsig));
                sb = sb2.toString();
                sb2.delete(0, sb2.length());
            }
            return sb;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getScreenParams(Activity activity) {
        String str;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        StringBuilder sb = new StringBuilder();
        sb.append("&screen=");
        if (displayMetrics.heightPixels > displayMetrics.widthPixels) {
            str = displayMetrics.widthPixels + Marker.ANY_MARKER + displayMetrics.heightPixels;
        } else {
            str = displayMetrics.heightPixels + Marker.ANY_MARKER + displayMetrics.widthPixels;
        }
        sb.append(str);
        return sb.toString();
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static String getSourceApkPath(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return context.getPackageManager().getApplicationInfo(str, 0).sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
