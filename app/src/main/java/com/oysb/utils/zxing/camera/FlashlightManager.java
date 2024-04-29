package com.oysb.utils.zxing.camera;

import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes2.dex */
final class FlashlightManager {
    private static final String TAG = "FlashlightManager";
    private static final Object iHardwareService;
    private static final Method setFlashEnabledMethod;

    static {
        Object hardwareService = getHardwareService();
        iHardwareService = hardwareService;
        setFlashEnabledMethod = getSetFlashEnabledMethod(hardwareService);
        if (hardwareService == null) {
            Log.v(TAG, "This device does supports control of a flashlight");
        } else {
            Log.v(TAG, "This device does not support control of a flashlight");
        }
    }

    private FlashlightManager() {
    }

    public static void enableFlashlight() {
        setFlashlight(false);
    }

    public static void disableFlashlight() {
        setFlashlight(false);
    }

    private static Object getHardwareService() {
        Method maybeGetMethod;
        Object invoke;
        Class<?> maybeForName;
        Method maybeGetMethod2;
        Class<?> maybeForName2 = maybeForName("android.os.ServiceManager");
        if (maybeForName2 == null || (maybeGetMethod = maybeGetMethod(maybeForName2, "getService", String.class)) == null || (invoke = invoke(maybeGetMethod, null, "hardware")) == null || (maybeForName = maybeForName("android.os.IHardwareService$Stub")) == null || (maybeGetMethod2 = maybeGetMethod(maybeForName, "asInterface", IBinder.class)) == null) {
            return null;
        }
        return invoke(maybeGetMethod2, null, invoke);
    }

    private static Method getSetFlashEnabledMethod(Object obj) {
        if (obj == null) {
            return null;
        }
        return maybeGetMethod(obj.getClass(), "setFlashlightEnabled", Boolean.TYPE);
    }

    private static Class<?> maybeForName(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException unused) {
            return null;
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error while finding class " + str, e);
            return null;
        }
    }

    private static Method maybeGetMethod(Class<?> cls, String str, Class<?>... clsArr) {
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException unused) {
            return null;
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error while finding method " + str, e);
            return null;
        }
    }

    private static Object invoke(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "Unexpected error while invoking " + method, e);
            return null;
        } catch (RuntimeException e2) {
            Log.w(TAG, "Unexpected error while invoking " + method, e2);
            return null;
        } catch (InvocationTargetException e3) {
            Log.w(TAG, "Unexpected error while invoking " + method, e3.getCause());
            return null;
        }
    }

    public static void setFlashlight(boolean z) {
        Object obj = iHardwareService;
        if (obj != null) {
            invoke(setFlashEnabledMethod, obj, Boolean.valueOf(z));
        }
    }
}
