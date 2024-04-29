package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class HeatDialog extends Dialog {
    private static final int CLOSE_TIME = 30;
    private Button bt_heating;
    private Button bt_unheating;
    private ButtonClickListener buttonClickListener;
    private int delayTime;
    private Handler handler;
    private TextView tip;

    /* loaded from: classes2.dex */
    public interface ButtonClickListener {
        void heatingClick();

        void unheatingClick();
    }

    static /* synthetic */ int access$210(HeatDialog heatDialog) {
        int i = heatDialog.delayTime;
        heatDialog.delayTime = i - 1;
        return i;
    }

    public HeatDialog(Context context, String str) {
        super(context, R.style.ad_style);
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.HeatDialog.3
            AnonymousClass3() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                HeatDialog.access$210(HeatDialog.this);
                if (HeatDialog.this.delayTime > 0) {
                    HeatDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                } else {
                    try {
                        HeatDialog.this.dismiss();
                    } catch (Exception unused) {
                    }
                }
            }
        };
        initView(str);
    }

    private void initView(String str) {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_heat_dialog);
        this.tip = (TextView) findViewById(R.id.swippay);
        this.bt_heating = (Button) findViewById(R.id.bt_heating);
        this.bt_unheating = (Button) findViewById(R.id.bt_unheating);
        this.tip.setText(str);
        setCanceledOnTouchOutside(false);
        setListener();
    }

    /* renamed from: com.shj.setting.Dialog.HeatDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (HeatDialog.this.buttonClickListener != null) {
                HeatDialog.this.buttonClickListener.heatingClick();
            }
            HeatDialog.this.dismiss();
            HeatDialog.this.handler.removeMessages(0);
        }
    }

    private void setListener() {
        this.bt_heating.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.HeatDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (HeatDialog.this.buttonClickListener != null) {
                    HeatDialog.this.buttonClickListener.heatingClick();
                }
                HeatDialog.this.dismiss();
                HeatDialog.this.handler.removeMessages(0);
            }
        });
        this.bt_unheating.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.HeatDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (HeatDialog.this.buttonClickListener != null) {
                    HeatDialog.this.buttonClickListener.unheatingClick();
                }
                HeatDialog.this.dismiss();
                HeatDialog.this.handler.removeMessages(0);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.HeatDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (HeatDialog.this.buttonClickListener != null) {
                HeatDialog.this.buttonClickListener.unheatingClick();
            }
            HeatDialog.this.dismiss();
            HeatDialog.this.handler.removeMessages(0);
        }
    }

    /* renamed from: com.shj.setting.Dialog.HeatDialog$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 extends Handler {
        AnonymousClass3() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            HeatDialog.access$210(HeatDialog.this);
            if (HeatDialog.this.delayTime > 0) {
                HeatDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
            } else {
                try {
                    HeatDialog.this.dismiss();
                } catch (Exception unused) {
                }
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

    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }
}
