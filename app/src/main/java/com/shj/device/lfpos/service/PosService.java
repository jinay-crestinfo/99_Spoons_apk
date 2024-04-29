package com.shj.device.lfpos.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.oysb.utils.Loger;
import com.shj.device.lfpos.command.CommandProcessor;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class PosService {
    static final int MSG_DO_CHECK = 1000;
    static Handler handler = new Handler() { // from class: com.shj.device.lfpos.service.PosService.1
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1000) {
                PosService.checkProcessors();
            }
            super.handleMessage(message);
        }
    };
    static WeakReference<Context> wkContext;

    /* renamed from: com.shj.device.lfpos.service.PosService$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1000) {
                PosService.checkProcessors();
            }
            super.handleMessage(message);
        }
    }

    public static void start(Context context) {
        wkContext = new WeakReference<>(context);
        Loger.writeLog("LFPOS", "LFPOSServiceEx start");
        startProcessors();
        registerTimeTickReciver();
    }

    /* renamed from: com.shj.device.lfpos.service.PosService$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends TimerTask {
        AnonymousClass2() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            PosService.handler.sendEmptyMessage(1000);
        }
    }

    public static void registerTimeTickReciver() {
        new Timer().schedule(new TimerTask() { // from class: com.shj.device.lfpos.service.PosService.2
            AnonymousClass2() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                PosService.handler.sendEmptyMessage(1000);
            }
        }, 1000L, 60000L);
    }

    public static void checkProcessors() {
        try {
            Context context = wkContext.get();
            Loger.writeLog("LFPOS", "LFPOSService 正在检查服务状态...");
            PosSerialProcessor.getProcessor().start(context);
            CommandProcessor.getProcessor().start(context);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }

    private static void startProcessors() {
        try {
            Loger.writeLog("LFPOS", "LFPOSService 正重新启动...111");
            Context context = wkContext.get();
            PosSerialProcessor.getProcessor().start(context);
            CommandProcessor.getProcessor().start(context);
            Loger.writeLog("LFPOS", "LFPOSService 已重新启动...");
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }
}
