package com.oysb.utils.zxing.decoding;

import android.app.Activity;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public final class InactivityTimer {
    private static final int INACTIVITY_DELAY_SECONDS = 300;
    private final Activity activity;
    private final ScheduledExecutorService inactivityTimer = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
    private ScheduledFuture<?> inactivityFuture = null;

    public InactivityTimer(Activity activity) {
        this.activity = activity;
        onActivity();
    }

    public void onActivity() {
        cancel();
        this.inactivityFuture = this.inactivityTimer.schedule(new FinishListener(this.activity), 300L, TimeUnit.SECONDS);
    }

    private void cancel() {
        ScheduledFuture<?> scheduledFuture = this.inactivityFuture;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.inactivityFuture = null;
        }
    }

    public void shutdown() {
        cancel();
        this.inactivityTimer.shutdown();
    }

    /* loaded from: classes2.dex */
    private static final class DaemonThreadFactory implements ThreadFactory {
        private DaemonThreadFactory() {
        }


        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    }
}
