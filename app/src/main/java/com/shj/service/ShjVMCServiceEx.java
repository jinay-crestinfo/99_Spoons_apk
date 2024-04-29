package com.shj.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.oysb.utils.Loger;
import com.shj.Shj;
import com.shj.command.CommandProcessor;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class ShjVMCServiceEx {
    static final int MSG_DO_CHECK = 1000;
    static Handler handler = new Handler() { // from class: com.shj.service.ShjVMCServiceEx.1
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1000) {
                ShjVMCServiceEx.checkProcessors();
            }
            super.handleMessage(message);
        }
    };
    static WeakReference<Context> wkContext;

    /* renamed from: com.shj.service.ShjVMCServiceEx$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1000) {
                ShjVMCServiceEx.checkProcessors();
            }
            super.handleMessage(message);
        }
    }

    public static void start(Context context) {
        wkContext = new WeakReference<>(context);
        Loger.writeLog("SHJ", "ShjServiceEx start");
        startProcessors();
        registerTimeTickReciver();
    }

    /* renamed from: com.shj.service.ShjVMCServiceEx$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends TimerTask {
        AnonymousClass2() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ShjVMCServiceEx.handler.sendEmptyMessage(1000);
        }
    }

    public static void registerTimeTickReciver() {
        new Timer().schedule(new TimerTask() { // from class: com.shj.service.ShjVMCServiceEx.2
            AnonymousClass2() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                ShjVMCServiceEx.handler.sendEmptyMessage(1000);
            }
        }, 1000L, 60000L);
    }

    public static void checkProcessors() {
        try {
            Context context = wkContext.get();
            Loger.writeLog("APP", "ShjService checking...");
            if (Shj.getVersion() == 1) {
                ShjVMCSerialProcessorV1.getProcessor().start(context);
            } else {
                ShjVMCSerialProcessorV2.getProcessor().start(context);
            }
            CommandProcessor.getProcessor().start(context);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }

    private static void startProcessors() {
        try {
            Loger.writeLog("SHJ", "ShjService 正重新启动...");
            Context context = wkContext.get();
            if (Shj.getVersion() == 1) {
                ShjVMCSerialProcessorV1.getProcessor().start(context);
            } else {
                ShjVMCSerialProcessorV2.getProcessor().start(context);
            }
            CommandProcessor.getProcessor().start(context);
            Loger.writeLog("SHJ", "ShjService 已重新启动...");
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}
