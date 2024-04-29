package com.shj.biz.service;

import android.content.Context;
import com.oysb.utils.Loger;
import com.oysb.utils.PropertiesUtil;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.xy.net.NetManager;
import com.shj.Shj;
import com.tencent.wxpayface.WxfacePayCommonCode;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class ShjBizServiceEx {
    static long lastCheckTime = Long.MAX_VALUE;
    static Timer timer;
    static WeakReference<Context> wkContext;

    public static void start(Context context) {
        wkContext = new WeakReference<>(context);
        Loger.writeLog("SHJ", "ShjBizServiceEx start");
        startProcessors();
        startServiceCheckor();
    }

    public static void startServiceCheckor() {
        Timer timer2;
        if (System.currentTimeMillis() - lastCheckTime > 120000 && (timer2 = timer) != null) {
            try {
                timer2.cancel();
            } catch (Exception unused) {
            }
            timer = null;
        }
        if (timer != null) {
            return;
        }
        Timer timer3 = new Timer();
        timer = timer3;
        timer3.schedule(new TimerTask() { // from class: com.shj.biz.service.ShjBizServiceEx.1
            AnonymousClass1() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                ShjBizServiceEx.lastCheckTime = System.currentTimeMillis();
                ShjBizServiceEx.checkProcessors();
            }
        }, 1000L, 60000L);
    }

    /* renamed from: com.shj.biz.service.ShjBizServiceEx$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ShjBizServiceEx.lastCheckTime = System.currentTimeMillis();
            ShjBizServiceEx.checkProcessors();
        }
    }

    public static void checkProcessors() {
        try {
            CacheHelper.getFileCache().put("SHJ_BIZ_SERVICE_CHECK_TIME", "" + System.currentTimeMillis());
            Loger.writeLog("APP", "ShjBizService checking... NetManager.isInited:" + NetManager.isInited());
            if (NetManager.isInited()) {
                String serverIp = NetManager.getServerIp();
                if (serverIp == null || serverIp.length() == 0) {
                    serverIp = getProperty("TXServer");
                }
                String serverPort = NetManager.getServerPort();
                if (serverPort == null || serverPort.length() == 0) {
                    serverPort = getProperty("TXPort");
                }
                NetManager.getSocketProcessor().setHost(serverIp, Integer.parseInt(serverPort));
                NetManager.getSocketProcessor().start(wkContext.get());
            }
        } catch (Exception e) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
        }
    }

    private static void startProcessors() {
        try {
            if (NetManager.isInited()) {
                String serverIp = NetManager.getServerIp();
                if (serverIp == null || serverIp.length() == 0) {
                    serverIp = getProperty("TXServer");
                }
                String serverPort = NetManager.getServerPort();
                if (serverPort == null || serverPort.length() == 0) {
                    serverPort = getProperty("TXPort");
                }
                NetManager.getSocketProcessor().setHost(serverIp, Integer.parseInt(serverPort));
                NetManager.getSocketProcessor().start(wkContext.get());
            }
        } catch (Exception e) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
        }
    }

    public static String getProperty(String str) {
        return PropertiesUtil.getProperties(wkContext.get(), Shj.propertyFile, str);
    }
}
