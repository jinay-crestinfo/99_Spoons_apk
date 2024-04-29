package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class AdDialog extends Dialog {
    private static final int CLOSE_TIME = 30;
    private Bitmap bitmap;
    private Button button_close;
    private int delayTime;
    private Handler handler;
    private ImageView iv_activty;

    static /* synthetic */ int access$110(AdDialog adDialog) {
        int i = adDialog.delayTime;
        adDialog.delayTime = i - 1;
        return i;
    }

    public AdDialog(Context context, Bitmap bitmap) {
        super(context, R.style.ad_style);
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.AdDialog.3
            AnonymousClass3() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                AdDialog.access$110(AdDialog.this);
                if (AdDialog.this.delayTime > 0) {
                    AdDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                    return;
                }
                try {
                    AdDialog.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.bitmap = bitmap;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.layout_dialog_ad);
        this.button_close = (Button) findViewById(R.id.button_close);
        ImageView imageView = (ImageView) findViewById(R.id.iv_activty);
        this.iv_activty = imageView;
        Bitmap bitmap = this.bitmap;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        setCanceledOnTouchOutside(true);
        setListener();
    }

    /* renamed from: com.shj.setting.Dialog.AdDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            AdDialog.this.dismiss();
            AdDialog.this.handler.removeMessages(0);
        }
    }

    private void setListener() {
        this.button_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.AdDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AdDialog.this.dismiss();
                AdDialog.this.handler.removeMessages(0);
            }
        });
        this.iv_activty.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.AdDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AdDialog.this.dismiss();
                AdDialog.this.handler.removeMessages(0);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.AdDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            AdDialog.this.dismiss();
            AdDialog.this.handler.removeMessages(0);
        }
    }

    /* renamed from: com.shj.setting.Dialog.AdDialog$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 extends Handler {
        AnonymousClass3() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            AdDialog.access$110(AdDialog.this);
            if (AdDialog.this.delayTime > 0) {
                AdDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                return;
            }
            try {
                AdDialog.this.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        this.handler.removeMessages(0);
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
        this.delayTime = 30;
        this.handler.sendEmptyMessageDelayed(0, 1000L);
    }
}
