package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;

/* loaded from: classes2.dex */
public class EditTextInputDialog extends Dialog {
    private Button bt_cancel;
    private Button bt_ok;
    private Context context;
    private EditText et_input;
    private OkClickListener okClickListener;

    /* loaded from: classes2.dex */
    public interface OkClickListener {
        void onClick(String str);
    }

    public EditTextInputDialog(Context context, OkClickListener okClickListener) {
        super(context, R.style.loading_style);
        this.context = context;
        this.okClickListener = okClickListener;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.layout_edittext_input);
        this.bt_cancel = (Button) findViewById(R.id.bt_cancel);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        this.et_input = (EditText) findViewById(R.id.et_input);
        setCanceledOnTouchOutside(false);
        setListener();
    }

    /* renamed from: com.shj.setting.Dialog.EditTextInputDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            EditTextInputDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_cancel.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.EditTextInputDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                EditTextInputDialog.this.dismiss();
            }
        });
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.EditTextInputDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String obj = EditTextInputDialog.this.et_input.getText().toString();
                if (TextUtils.isEmpty(obj)) {
                    ToastUitl.showShort(EditTextInputDialog.this.context, "请输入条形码");
                    return;
                }
                if (EditTextInputDialog.this.okClickListener != null) {
                    EditTextInputDialog.this.okClickListener.onClick(obj);
                }
                EditTextInputDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.EditTextInputDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String obj = EditTextInputDialog.this.et_input.getText().toString();
            if (TextUtils.isEmpty(obj)) {
                ToastUitl.showShort(EditTextInputDialog.this.context, "请输入条形码");
                return;
            }
            if (EditTextInputDialog.this.okClickListener != null) {
                EditTextInputDialog.this.okClickListener.onClick(obj);
            }
            EditTextInputDialog.this.dismiss();
        }
    }
}
