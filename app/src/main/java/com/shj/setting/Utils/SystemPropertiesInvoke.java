package com.shj.setting.Utils;

import android.util.Log;
import java.lang.reflect.Method;

/* loaded from: classes2.dex */
public class SystemPropertiesInvoke {
    private static final String TAG = "SystemPropertiesInvoke";
    private static Method getBooleanMethod;
    private static Method getLongMethod;
    private static Method setMethod;

    public static long getLong(String str, long j) {
        try {
            if (getLongMethod == null) {
                getLongMethod = Class.forName("android.os.SystemProperties").getMethod("getLong", String.class, Long.TYPE);
            }
            return ((Long) getLongMethod.invoke(null, str, Long.valueOf(j))).longValue();
        } catch (Exception e) {
            Log.e(TAG, "Platform error: " + e.toString());
            return j;
        }
    }

    public static boolean getBoolean(String str, boolean z) {
        try {
            if (getBooleanMethod == null) {
                getBooleanMethod = Class.forName("android.os.SystemProperties").getMethod("getBoolean", String.class, Boolean.TYPE);
            }
            return ((Boolean) getBooleanMethod.invoke(null, str, Boolean.valueOf(z))).booleanValue();
        } catch (Exception e) {
            Log.e(TAG, "Platform error: " + e.toString());
            return z;
        }
    }

    public static void setValue(String str, String str2) {
        try {
            if (setMethod == null) {
                setMethod = Class.forName("android.os.SystemProperties").getMethod("set", String.class, String.class);
            }
            setMethod.invoke(null, str, str2);
        } catch (Exception e) {
            Log.e(TAG, "Platform error: " + e.toString());
        }
    }
}
