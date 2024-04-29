package com.xyshj.machine.tools;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.oysb.app.R;
import com.xyshj.app.ShjAppHelper;

/* loaded from: classes2.dex */
public class ProgressDialog extends Dialog {
    static final int MSG_TIMER = 4000;
    private static ProgressDialog myDialog;
    Handler handler;
    int secondTime;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.tools.ProgressDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 4000) {
                return;
            }
            if (((Integer) message.obj).intValue() == 0) {
                ShjAppHelper.cancelMessage(false);
                ProgressDialog.myDialog.dismiss();
            } else {
                Message obtain = Message.obtain();
                obtain.what = 4000;
                obtain.obj = Integer.valueOf(r4.intValue() - 1);
                ProgressDialog.this.handler.sendMessageDelayed(obtain, 1000L);
            }
        }
    }

    public ProgressDialog(Context context, int i) {
        super(context, i);
        this.handler = new Handler() { // from class: com.xyshj.machine.tools.ProgressDialog.1
            AnonymousClass1() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 4000) {
                    return;
                }
                if (((Integer) message.obj).intValue() == 0) {
                    ShjAppHelper.cancelMessage(false);
                    ProgressDialog.myDialog.dismiss();
                } else {
                    Message obtain = Message.obtain();
                    obtain.what = 4000;
                    obtain.obj = Integer.valueOf(r4.intValue() - 1);
                    ProgressDialog.this.handler.sendMessageDelayed(obtain, 1000L);
                }
            }
        };
        getWindow().setGravity(17);
    }

    public static ProgressDialog creatDialog(Context context, View view) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.GameDialog);
        myDialog = progressDialog;
        progressDialog.setContentView(view);
        return myDialog;
    }

    public void setDuration(int i) {
        this.secondTime = i;
        Message obtain = Message.obtain();
        obtain.what = 4000;
        obtain.obj = Integer.valueOf(i);
        this.handler.sendMessageDelayed(obtain, 1000L);
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            return false;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Dialog
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.handler.removeMessages(4000);
        setDuration(this.secondTime);
        return super.onTouchEvent(motionEvent);
    }
}
