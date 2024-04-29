package com.shj.device.scanor;

import android.content.Context;
import com.oysb.utils.Loger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.SocketClient;

/* loaded from: classes2.dex */
public class Scanor {
    private static ArrayList<String> exDevPath = null;
    private static boolean isConnected = false;
    private static ScanorListener listener = null;
    private static String preFix = "";
    private static ScanorSerialManager scanorSerialManager;
    private static WeakReference<Context> wkContext;

    /* loaded from: classes2.dex */
    public interface ScanorListener {
        void onMessage(String str);
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public static void start(Context context) {
        start(context, true);
    }

    public static void start(Context context, boolean z) {
        wkContext = new WeakReference<>(context);
        ScanorSerialManager scanorSerialManager2 = scanorSerialManager;
        if (scanorSerialManager2 == null) {
            ScanorSerialManager scanorSerialManager3 = new ScanorSerialManager();
            scanorSerialManager = scanorSerialManager3;
            scanorSerialManager3.start(context, z);
        } else if (!scanorSerialManager2.isRunning()) {
            scanorSerialManager.start(context, z);
        } else {
            listener.onMessage("isRunning");
        }
    }

    public static Context getContext() {
        return wkContext.get();
    }

    public static void addExDevPath(String str) {
        if (exDevPath == null) {
            exDevPath = new ArrayList<>();
        }
        exDevPath.add(str);
    }

    public static boolean isExDevPath(String str) {
        try {
            if (exDevPath == null) {
                exDevPath = new ArrayList<>();
            }
            return exDevPath.contains(str);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            return false;
        }
    }

    public static void onMessage(String str) {
        String trim = str.trim();
        if (trim.endsWith(SocketClient.NETASCII_EOL)) {
            trim = trim.substring(0, trim.length() - 2);
        }
        if (trim.endsWith("\n\r")) {
            trim = trim.substring(0, trim.length() - 2);
        }
        if (trim.endsWith(StringUtils.LF)) {
            trim = trim.substring(0, trim.length() - 1);
        }
        String trim2 = trim.trim();
        if (trim2.length() == 0) {
            return;
        }
        try {
            listener.onMessage(preFix + trim2);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SCANOR", e);
        }
        if (isConnected) {
            return;
        }
        isConnected = true;
    }

    public static void connectState(boolean z) {
        if (z) {
            listener.onMessage("connectState:true");
        } else {
            listener.onMessage("connectState:false");
        }
    }

    public static ScanorListener getScanorListener() {
        return listener;
    }

    public static void setScanorListener(ScanorListener scanorListener) {
        listener = scanorListener;
    }

    public static String getPreFix() {
        return preFix;
    }

    public static void setPreFix(String str) {
        preFix = str;
    }
}
