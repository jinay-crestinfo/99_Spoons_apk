package com.oysb.utils.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.oysb.utils.Loger;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class StepProgressDialog {
    private static final long MAX_SHOW_TIME = 40000L;
    private static final long CHECK_INTERVAL = 10000L;
    private static final long AUTO_CLOSE_DELAY = 3000L;

    private static Handler handler = new Handler();
    private static ProgressDialog myProgressDialog = null;
    private static Timer progressDlgCheckTimer = null;
    private static long progressDlgStartShowTime = Long.MAX_VALUE;
    private static WeakReference<Context> wkContext;

    public static void init(Context context) {
        wkContext = new WeakReference<>(context);
    }

    public static void showProgressDlg(String title) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    progressDlgStartShowTime = System.currentTimeMillis();
                    myProgressDialog = new ProgressDialog(wkContext.get());
                    myProgressDialog.getWindow().setGravity(17);
                    myProgressDialog.setMax(100);
                    myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    myProgressDialog.setTitle(title);
                    myProgressDialog.setCancelable(false);
                    myProgressDialog.show();
                    resetProgressDlgStateCheckTimer(true);
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                }
            }
        });
    }

    public static void closeProgressDlg(int delayMillis) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myProgressDialog != null && myProgressDialog.isShowing()) {
                    myProgressDialog.dismiss();
                    myProgressDialog = null;
                    resetProgressDlgStateCheckTimer(false);
                }
            }
        }, delayMillis);
    }

    public static void resetProgressDlgStateCheckTimer(boolean enable) {
        if (enable) {
            progressDlgCheckTimer = new Timer();
            progressDlgCheckTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (System.currentTimeMillis() - progressDlgStartShowTime > MAX_SHOW_TIME) {
                        if (myProgressDialog != null && myProgressDialog.isShowing()) {
                            myProgressDialog.dismiss();
                            myProgressDialog = null;
                        }
                    }
                }
            }, 60000L, CHECK_INTERVAL);
        } else {
            if (progressDlgCheckTimer != null) {
                progressDlgCheckTimer.cancel();
                progressDlgCheckTimer = null;
            }
        }
    }

    public static void update(int max, int progress, String message, boolean autoClose) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (myProgressDialog == null) return;
                myProgressDialog.setMax(max);
                myProgressDialog.setProgress(progress);
                myProgressDialog.setMessage(message);
                if (autoClose && progress == max - 1) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (myProgressDialog != null && myProgressDialog.isShowing()) {
                                myProgressDialog.dismiss();
                                myProgressDialog = null;
                            }
                        }
                    }, AUTO_CLOSE_DELAY);
                }
            }
        });
    }
}
