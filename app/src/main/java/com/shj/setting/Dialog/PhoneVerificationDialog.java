package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.Utils.SetUtils;
import com.shj.setting.Utils.ToastUitl;

/* loaded from: classes2.dex */
public class PhoneVerificationDialog extends Dialog {
    private Button bt_get_verification_code;
    private Button button_close;
    private Button button_ok;
    private Context context;
    private int delayTime;
    private EditText et_phone;
    private EditText et_verification_number;
    private Handler handler;
    private ImageView iv_qrcode;
    private TextView tv_count_down;
    private TextView tv_title;
    private VerificationListener verificationListener;

    /* loaded from: classes2.dex */
    public interface VerificationListener {
        void getVerificationVode(String str);

        void verification(String str);
    }

    static /* synthetic */ int access$410(PhoneVerificationDialog phoneVerificationDialog) {
        int i = phoneVerificationDialog.delayTime;
        phoneVerificationDialog.delayTime = i - 1;
        return i;
    }

    public PhoneVerificationDialog(Context context) {
        super(context, R.style.translucent_dialog_style);
        this.delayTime = 120;
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.PhoneVerificationDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                PhoneVerificationDialog.access$410(PhoneVerificationDialog.this);
                if (PhoneVerificationDialog.this.delayTime > 0) {
                    PhoneVerificationDialog.this.tv_count_down.setText(PhoneVerificationDialog.this.delayTime + "s");
                    PhoneVerificationDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                    return;
                }
                PhoneVerificationDialog.this.dismiss();
            }
        };
        this.context = context;
        initView();
        this.handler.sendEmptyMessageDelayed(0, 1000L);
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.layout_phone_verification);
        this.tv_title = (TextView) findViewById(R.id.tv_title);
        this.tv_count_down = (TextView) findViewById(R.id.tv_count_down);
        this.iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
        this.et_phone = (EditText) findViewById(R.id.et_phone);
        this.et_verification_number = (EditText) findViewById(R.id.et_verification_number);
        this.bt_get_verification_code = (Button) findViewById(R.id.bt_get_verification_code);
        this.button_close = (Button) findViewById(R.id.button_close);
        this.button_ok = (Button) findViewById(R.id.button_ok);
        this.tv_count_down.setText(this.delayTime + "s");
        setCanceledOnTouchOutside(true);
        setListener();
    }

    /* renamed from: com.shj.setting.Dialog.PhoneVerificationDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PhoneVerificationDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.button_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.PhoneVerificationDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PhoneVerificationDialog.this.dismiss();
            }
        });
        this.bt_get_verification_code.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.PhoneVerificationDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String obj = PhoneVerificationDialog.this.et_phone.getText().toString();
                if (TextUtils.isEmpty(obj) || !SetUtils.isPhoneLegal(obj)) {
                    ToastUitl.showShort(PhoneVerificationDialog.this.context, R.string.pls_input_correct_phone_number);
                } else if (PhoneVerificationDialog.this.verificationListener != null) {
                    PhoneVerificationDialog.this.verificationListener.getVerificationVode(obj);
                }
            }
        });
        this.button_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.PhoneVerificationDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String obj = PhoneVerificationDialog.this.et_verification_number.getText().toString();
                if (TextUtils.isEmpty(obj)) {
                    ToastUitl.showShort(PhoneVerificationDialog.this.context, R.string.pls_input_correct_verification_code);
                } else if (PhoneVerificationDialog.this.verificationListener != null) {
                    PhoneVerificationDialog.this.verificationListener.verification(obj);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.PhoneVerificationDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String obj = PhoneVerificationDialog.this.et_phone.getText().toString();
            if (TextUtils.isEmpty(obj) || !SetUtils.isPhoneLegal(obj)) {
                ToastUitl.showShort(PhoneVerificationDialog.this.context, R.string.pls_input_correct_phone_number);
            } else if (PhoneVerificationDialog.this.verificationListener != null) {
                PhoneVerificationDialog.this.verificationListener.getVerificationVode(obj);
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.PhoneVerificationDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String obj = PhoneVerificationDialog.this.et_verification_number.getText().toString();
            if (TextUtils.isEmpty(obj)) {
                ToastUitl.showShort(PhoneVerificationDialog.this.context, R.string.pls_input_correct_verification_code);
            } else if (PhoneVerificationDialog.this.verificationListener != null) {
                PhoneVerificationDialog.this.verificationListener.verification(obj);
            }
        }
    }

    public void setQrcodeBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.iv_qrcode.setImageBitmap(bitmap);
        }
    }

    public void setButtonBackgroundResource(int i) {
        this.button_ok.setBackgroundResource(i);
    }

    public void setVerificationListener(VerificationListener verificationListener) {
        this.verificationListener = verificationListener;
    }

    /* renamed from: com.shj.setting.Dialog.PhoneVerificationDialog$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 extends Handler {
        AnonymousClass4() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            PhoneVerificationDialog.access$410(PhoneVerificationDialog.this);
            if (PhoneVerificationDialog.this.delayTime > 0) {
                PhoneVerificationDialog.this.tv_count_down.setText(PhoneVerificationDialog.this.delayTime + "s");
                PhoneVerificationDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                return;
            }
            PhoneVerificationDialog.this.dismiss();
        }
    }
}
