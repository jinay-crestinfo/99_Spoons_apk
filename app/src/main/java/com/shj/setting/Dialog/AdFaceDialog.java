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
public class AdFaceDialog extends Dialog {
    private static final int CLOSE_TIME = 30;
    private Bitmap bitmap;
    private Button button_close;
    private Button button_face;
    private int delayTime;
    private FaceDialogListener faceDialogListener;
    private Handler handler;
    private ImageView iv_activty;

    /* loaded from: classes2.dex */
    public interface FaceDialogListener {
        void faceButtonClick();
    }

    static /* synthetic */ int access$210(AdFaceDialog adFaceDialog) {
        int i = adFaceDialog.delayTime;
        adFaceDialog.delayTime = i - 1;
        return i;
    }

    public AdFaceDialog(Context context, Bitmap bitmap) {
        super(context, R.style.ad_style);
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.AdFaceDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                AdFaceDialog.access$210(AdFaceDialog.this);
                if (AdFaceDialog.this.delayTime > 0) {
                    AdFaceDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                    return;
                }
                try {
                    AdFaceDialog.this.dismiss();
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
        setContentView(R.layout.layout_dialog_adface);
        this.button_close = (Button) findViewById(R.id.button_close);
        this.button_face = (Button) findViewById(R.id.button_face);
        ImageView imageView = (ImageView) findViewById(R.id.iv_activty);
        this.iv_activty = imageView;
        Bitmap bitmap = this.bitmap;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        setCanceledOnTouchOutside(true);
        setListener();
    }

    public void setFaceDialogListener(FaceDialogListener faceDialogListener) {
        this.faceDialogListener = faceDialogListener;
    }

    /* renamed from: com.shj.setting.Dialog.AdFaceDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            AdFaceDialog.this.dismiss();
            AdFaceDialog.this.handler.removeMessages(0);
        }
    }

    private void setListener() {
        this.button_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.AdFaceDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AdFaceDialog.this.dismiss();
                AdFaceDialog.this.handler.removeMessages(0);
            }
        });
        this.button_face.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.AdFaceDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AdFaceDialog.this.dismiss();
                AdFaceDialog.this.handler.removeMessages(0);
                if (AdFaceDialog.this.faceDialogListener != null) {
                    AdFaceDialog.this.faceDialogListener.faceButtonClick();
                }
            }
        });
        this.iv_activty.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.AdFaceDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AdFaceDialog.this.dismiss();
                AdFaceDialog.this.handler.removeMessages(0);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.AdFaceDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            AdFaceDialog.this.dismiss();
            AdFaceDialog.this.handler.removeMessages(0);
            if (AdFaceDialog.this.faceDialogListener != null) {
                AdFaceDialog.this.faceDialogListener.faceButtonClick();
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.AdFaceDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            AdFaceDialog.this.dismiss();
            AdFaceDialog.this.handler.removeMessages(0);
        }
    }

    /* renamed from: com.shj.setting.Dialog.AdFaceDialog$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 extends Handler {
        AnonymousClass4() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            AdFaceDialog.access$210(AdFaceDialog.this);
            if (AdFaceDialog.this.delayTime > 0) {
                AdFaceDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                return;
            }
            try {
                AdFaceDialog.this.dismiss();
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
