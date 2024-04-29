package com.oysb.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.oysb.utils.R;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/* loaded from: classes2.dex */
public class RbootDeicingDialog extends Dialog {
    private int delayTime;
    private Handler handler;
    private GifImageView iv_activty;
    private Context mContext;

    static /* synthetic */ int access$010(RbootDeicingDialog rbootDeicingDialog) {
        int i = rbootDeicingDialog.delayTime;
        rbootDeicingDialog.delayTime = i - 1;
        return i;
    }

    public RbootDeicingDialog(Context context, int i) {
        super(context, R.style.ad_style);
        this.handler = new Handler() { // from class: com.oysb.utils.dialog.RbootDeicingDialog.2


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

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_dialog_reboot_deicing);
        GifImageView gifImageView = (GifImageView) findViewById(R.id.iv_activty);
        this.iv_activty = gifImageView;
        ((GifDrawable) gifImageView.getDrawable()).setLoopCount(0);
        setCanceledOnTouchOutside(false);
        setListener();
    }

    /* renamed from: com.oysb.utils.dialog.RbootDeicingDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            RbootDeicingDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.iv_activty.setOnClickListener(new View.OnClickListener() { // from class: com.oysb.utils.dialog.RbootDeicingDialog.1

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RbootDeicingDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.oysb.utils.dialog.RbootDeicingDialog$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 extends Handler {
        AnonymousClass2() {
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
