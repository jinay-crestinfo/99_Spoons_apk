package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class RbootModelTipDialog extends Dialog {
    private Button button_close;
    private ImageView iv_activty;

    public RbootModelTipDialog(Context context) {
        super(context, R.style.ad_style);
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_dialog_reboot_model);
        this.button_close = (Button) findViewById(R.id.button_close);
        this.iv_activty = (ImageView) findViewById(R.id.iv_activty);
        setCanceledOnTouchOutside(true);
        setListener();
    }

    /* renamed from: com.shj.setting.Dialog.RbootModelTipDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            RbootModelTipDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.button_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.RbootModelTipDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RbootModelTipDialog.this.dismiss();
            }
        });
        this.iv_activty.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.RbootModelTipDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RbootModelTipDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.RbootModelTipDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            RbootModelTipDialog.this.dismiss();
        }
    }
}
