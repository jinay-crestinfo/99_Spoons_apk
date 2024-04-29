package com.shj.biz;

import android.app.Activity;
import com.oysb.utils.test.ClickCmd;
import com.oysb.utils.test.Shell;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class ShjAppTest {
    static Timer timer;

    public static void resetTimer() {
        Timer timer2 = timer;
        if (timer2 != null) {
            timer2.cancel();
            timer = null;
        }
        Timer timer3 = new Timer();
        timer = timer3;
        timer3.schedule(new TimerTask() { // from class: com.shj.biz.ShjAppTest.1
            AnonymousClass1() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (Shell.isReady()) {
                    double random = Math.random();
                    double d = Shell.SCREEN_WIDTH;
                    Double.isNaN(d);
                    double random2 = Math.random();
                    double d2 = Shell.SCREEN_HEIGHT - 610;
                    Double.isNaN(d2);
                    Shell.addCommand(new ClickCmd((int) (random * d), (int) (random2 * d2)));
                }
            }
        }, 1000L, 1000L);
    }

    /* renamed from: com.shj.biz.ShjAppTest$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (Shell.isReady()) {
                double random = Math.random();
                double d = Shell.SCREEN_WIDTH;
                Double.isNaN(d);
                double random2 = Math.random();
                double d2 = Shell.SCREEN_HEIGHT - 610;
                Double.isNaN(d2);
                Shell.addCommand(new ClickCmd((int) (random * d), (int) (random2 * d2)));
            }
        }
    }

    public static void cancleTimer() {
        Timer timer2 = timer;
        if (timer2 != null) {
            timer2.cancel();
            timer = null;
        }
    }

    public static void startTest(Activity activity) {
        Shell.start(activity);
        resetTimer();
    }

    public static void stop() {
        cancleTimer();
    }
}
