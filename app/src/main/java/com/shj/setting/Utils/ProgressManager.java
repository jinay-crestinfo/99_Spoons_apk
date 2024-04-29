package com.shj.setting.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import com.shj.setting.Dialog.WaitDialog;
import com.shj.setting.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class ProgressManager {
    private static WaitDialog mProgress;
    private static Timer timer;
    private static ArrayList<WaitDialog> mProgressList = new ArrayList<>();
    static Handler h = null;

    /* loaded from: classes2.dex */
    public interface OnShowMessageTimeoutListener {
        void onShowMessageTimeoutListener();
    }

    /* renamed from: com.shj.setting.Utils.ProgressManager$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements DialogInterface.OnKeyListener {
        AnonymousClass1() {
        }

        @Override // android.content.DialogInterface.OnKeyListener
        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            if (i != 4) {
                return false;
            }
            dialogInterface.dismiss();
            return true;
        }
    }

    private static WaitDialog createDialog(Context context, String str) {
        WaitDialog waitDialog = new WaitDialog(context, R.style.SettingWaitingDialogStyle);
        mProgress = waitDialog;
        waitDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.shj.setting.Utils.ProgressManager.1
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return false;
                }
                dialogInterface.dismiss();
                return true;
            }
        });
        if (str == null || str.length() == 0) {
            str = context.getString(R.string.lab_waiting);
        }
        mProgress.setMessage(str);
        return mProgress;
    }

    public static WaitDialog showProgress(Context context, String str) {
        closeProgress();
        WaitDialog createDialog = createDialog(context, str);
        try {
            createDialog.show();
            mProgressList.add(createDialog);
            initHander();
            new Timer().schedule(new TimerTask() { // from class: com.shj.setting.Utils.ProgressManager.2
                AnonymousClass2() {
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    ProgressManager.h.sendEmptyMessage(1000);
                }
            }, 2000L);
            return createDialog;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.Utils.ProgressManager$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends TimerTask {
        AnonymousClass2() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ProgressManager.h.sendEmptyMessage(1000);
        }
    }

    public static WaitDialog showProgressEx(Context context, String str) {
        return showProgressEx(context, str, true);
    }

    public static WaitDialog showProgressEx(Context context, String str, boolean z) {
        closeProgress();
        WaitDialog createDialog = createDialog(context, str);
        try {
            createDialog.show();
            createDialog.showIcon(z);
            mProgressList.add(createDialog);
            return createDialog;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Handler getHandler() {
        return h;
    }

    /* renamed from: com.shj.setting.Utils.ProgressManager$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends Handler {
        AnonymousClass3() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            ProgressManager.closeProgress();
            if (message.what == 1000) {
                try {
                    OnShowMessageTimeoutListener onShowMessageTimeoutListener = (OnShowMessageTimeoutListener) message.obj;
                    if (onShowMessageTimeoutListener == null) {
                    } else {
                        onShowMessageTimeoutListener.onShowMessageTimeoutListener();
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    static void initHander() {
        if (h == null) {
            h = new Handler() { // from class: com.shj.setting.Utils.ProgressManager.3
                AnonymousClass3() {
                }

                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    super.handleMessage(message);
                    ProgressManager.closeProgress();
                    if (message.what == 1000) {
                        try {
                            OnShowMessageTimeoutListener onShowMessageTimeoutListener = (OnShowMessageTimeoutListener) message.obj;
                            if (onShowMessageTimeoutListener == null) {
                            } else {
                                onShowMessageTimeoutListener.onShowMessageTimeoutListener();
                            }
                        } catch (Exception unused) {
                        }
                    }
                }
            };
        }
    }

    public static WaitDialog showProgress(Context context, String str, int i, OnShowMessageTimeoutListener onShowMessageTimeoutListener) {
        closeProgress();
        WaitDialog createDialog = createDialog(context, str);
        try {
            createDialog.show();
            mProgressList.add(createDialog);
            initHander();
            Timer timer2 = timer;
            if (timer2 != null) {
                synchronized (timer2) {
                    Timer timer3 = timer;
                    if (timer3 != null) {
                        timer3.cancel();
                        timer = null;
                    }
                }
            }
            Timer timer4 = new Timer();
            timer = timer4;
            timer4.schedule(new TimerTask() { // from class: com.shj.setting.Utils.ProgressManager.4
                AnonymousClass4() {
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    Message obtain = Message.obtain();
                    obtain.obj = OnShowMessageTimeoutListener.this;
                    obtain.what = 1000;
                    ProgressManager.h.sendMessage(obtain);
                }
            }, i);
            return createDialog;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: com.shj.setting.Utils.ProgressManager$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 extends TimerTask {
        AnonymousClass4() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Message obtain = Message.obtain();
            obtain.obj = OnShowMessageTimeoutListener.this;
            obtain.what = 1000;
            ProgressManager.h.sendMessage(obtain);
        }
    }

    public static void changeProgressShowMessage(String str) {
        WaitDialog waitDialog = mProgress;
        if (waitDialog == null || !waitDialog.isShowing()) {
            return;
        }
        mProgress.changeMessage(str);
    }

    public static boolean isProgressShowing() {
        WaitDialog waitDialog = mProgress;
        return waitDialog != null && waitDialog.isShowing();
    }

    public static boolean closeProgress() {
        Timer timer2 = timer;
        if (timer2 != null) {
            synchronized (timer2) {
                timer.cancel();
                timer = null;
            }
        }
        try {
            if (mProgressList.isEmpty()) {
                return true;
            }
            Iterator<WaitDialog> it = mProgressList.iterator();
            while (it.hasNext()) {
                WaitDialog next = it.next();
                if (next.isShowing()) {
                    next.dismiss();
                }
            }
            mProgressList.clear();
            return true;
        } catch (Exception unused) {
            return true;
        }
    }

    public static void closeProgress(String str) {
        changeProgressShowMessage(str);
        new Timer().schedule(new TimerTask() { // from class: com.shj.setting.Utils.ProgressManager.5
            AnonymousClass5() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                ProgressManager.closeProgress();
            }
        }, 1000L);
    }

    /* renamed from: com.shj.setting.Utils.ProgressManager$5 */
    /* loaded from: classes2.dex */
    class AnonymousClass5 extends TimerTask {
        AnonymousClass5() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ProgressManager.closeProgress();
        }
    }
}
