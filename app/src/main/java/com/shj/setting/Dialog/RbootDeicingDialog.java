package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class RbootDeicingDialog extends Dialog {
    private int delayTime;
    private Handler handler;
    private Context mContext;

    private void initView() {
    }

    static /* synthetic */ int access$010(RbootDeicingDialog rbootDeicingDialog) {
        int i = rbootDeicingDialog.delayTime;
        rbootDeicingDialog.delayTime = i - 1;
        return i;
    }

    public RbootDeicingDialog(Context context, int i) {
        super(context, R.style.ad_style);
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.RbootDeicingDialog.1
            AnonymousClass1() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                RbootDeicingDialog.access$010(RbootDeicingDialog.this);
                if (RbootDeicingDialog.this.delayTime > 0) {
                    RbootDeicingDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                } else {
                    RbootDeicingDialog.this.dismiss();
                }
            }
        };
        this.delayTime = i;
        this.mContext = context;
        initView();
        if (i > 0) {
            this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
    }

    /* renamed from: com.shj.setting.Dialog.RbootDeicingDialog$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            RbootDeicingDialog.access$010(RbootDeicingDialog.this);
            if (RbootDeicingDialog.this.delayTime > 0) {
                RbootDeicingDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
            } else {
                RbootDeicingDialog.this.dismiss();
            }
        }
    }
}
