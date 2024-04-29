package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.oysb.utils.cache.CacheHelper;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;

/* loaded from: classes2.dex */
public class LoginDialog extends Dialog {
    private static final int PASSWORD_INTERVAL_TIME = 30;
    private Button button01;
    private Button button02;
    private Button button03;
    private ButtonListener buttonListener;
    private Context context;
    private EditText et_password;
    private boolean isNeedUpdatePassword;
    private boolean isPasswordRight;
    private int[] location_edite_password;
    private int passWordErrorCount;
    private TextView tv_error_tip;

    /* loaded from: classes2.dex */
    public interface ButtonListener {
        void updatePassword();
    }

    static /* synthetic */ int access$308(LoginDialog loginDialog) {
        int i = loginDialog.passWordErrorCount;
        loginDialog.passWordErrorCount = i + 1;
        return i;
    }

    public LoginDialog(Context context) {
        super(context, R.style.login_dialog_style);
        this.context = context;
        this.isPasswordRight = false;
        this.isNeedUpdatePassword = false;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_dialog_login);
        this.et_password = (EditText) findViewById(R.id.et_password);
        this.tv_error_tip = (TextView) findViewById(R.id.tv_error_tip);
        this.button01 = (Button) findViewById(R.id.button01);
        this.button02 = (Button) findViewById(R.id.button02);
        this.button03 = (Button) findViewById(R.id.button03);
        setCanceledOnTouchOutside(false);
        setListener();
        setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.shj.setting.Dialog.LoginDialog.1
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                LoginDialog.this.location_edite_password = new int[2];
                LoginDialog.this.et_password.getLocationOnScreen(LoginDialog.this.location_edite_password);
                new Thread(new Runnable() { // from class: com.shj.setting.Dialog.LoginDialog.1.1
                    RunnableC00561() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        int i = 0;
                        while (i < 20) {
                            i++;
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (LoginDialog.this.isSoftShowing()) {
                                return;
                            }
                            LoginDialog.this.showKeyboard(LoginDialog.this.et_password);
                            try {
                                Thread.sleep(200L);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                            if (LoginDialog.this.isSoftShowing()) {
                                return;
                            }
                        }
                    }
                }).start();
            }

            /* renamed from: com.shj.setting.Dialog.LoginDialog$1$1 */
            /* loaded from: classes2.dex */
            class RunnableC00561 implements Runnable {
                RunnableC00561() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    int i = 0;
                    while (i < 20) {
                        i++;
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (LoginDialog.this.isSoftShowing()) {
                            return;
                        }
                        LoginDialog.this.showKeyboard(LoginDialog.this.et_password);
                        try {
                            Thread.sleep(200L);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        if (LoginDialog.this.isSoftShowing()) {
                            return;
                        }
                    }
                }
            }
        });
        String asString = CacheHelper.getFileCache().getAsString("passWordInputErrorCount");
        if (TextUtils.isEmpty(asString)) {
            return;
        }
        this.passWordErrorCount = Integer.valueOf(asString).intValue();
        showPasswordErrorInputTip();
    }

    /* renamed from: com.shj.setting.Dialog.LoginDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements DialogInterface.OnShowListener {
        AnonymousClass1() {
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            LoginDialog.this.location_edite_password = new int[2];
            LoginDialog.this.et_password.getLocationOnScreen(LoginDialog.this.location_edite_password);
            new Thread(new Runnable() { // from class: com.shj.setting.Dialog.LoginDialog.1.1
                RunnableC00561() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    int i = 0;
                    while (i < 20) {
                        i++;
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (LoginDialog.this.isSoftShowing()) {
                            return;
                        }
                        LoginDialog.this.showKeyboard(LoginDialog.this.et_password);
                        try {
                            Thread.sleep(200L);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        if (LoginDialog.this.isSoftShowing()) {
                            return;
                        }
                    }
                }
            }).start();
        }

        /* renamed from: com.shj.setting.Dialog.LoginDialog$1$1 */
        /* loaded from: classes2.dex */
        class RunnableC00561 implements Runnable {
            RunnableC00561() {
            }

            @Override // java.lang.Runnable
            public void run() {
                int i = 0;
                while (i < 20) {
                    i++;
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (LoginDialog.this.isSoftShowing()) {
                        return;
                    }
                    LoginDialog.this.showKeyboard(LoginDialog.this.et_password);
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    if (LoginDialog.this.isSoftShowing()) {
                        return;
                    }
                }
            }
        }
    }

    public boolean isSoftShowing() {
        int[] iArr = new int[2];
        this.et_password.getLocationOnScreen(iArr);
        return this.location_edite_password[1] != iArr[1];
    }

    public void showKeyboard(EditText editText) {
        if (editText != null) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            ((InputMethodManager) this.context.getSystemService("input_method")).showSoftInput(editText, 0);
        }
    }

    public boolean isPasswordRight() {
        return this.isPasswordRight;
    }

    public boolean isNeedUpdatePassword() {
        return this.isNeedUpdatePassword;
    }

    /* renamed from: com.shj.setting.Dialog.LoginDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (LoginDialog.this.passWordErrorCount >= 3 && LoginDialog.this.passWordErrorCount % 3 == 0 && LoginDialog.this.showPasswordErrorInputTip()) {
                return;
            }
            String asString = CacheHelper.getFileCache().getAsString("passWord");
            if (asString == null || asString.length() == 0) {
                asString = "888888";
            }
            String obj = LoginDialog.this.et_password.getText().toString();
            if (TextUtils.isEmpty(obj)) {
                ToastUitl.showShort(LoginDialog.this.context, R.string.pls_input_password);
                return;
            }
            if (asString.equals(obj)) {
                LoginDialog.this.passWordErrorCount = 0;
                CacheHelper.getFileCache().put("passWordInputErrorCount", "0");
                LoginDialog.this.tv_error_tip.setVisibility(8);
                LoginDialog.this.isPasswordRight = true;
                LoginDialog.this.dismiss();
                return;
            }
            LoginDialog.access$308(LoginDialog.this);
            CacheHelper.getFileCache().put("passWordInputErrorCount", String.valueOf(LoginDialog.this.passWordErrorCount));
            if (LoginDialog.this.passWordErrorCount % 3 == 0) {
                CacheHelper.getFileCache().put("passWordInputErrorTime", String.valueOf(System.currentTimeMillis()));
                LoginDialog.this.tv_error_tip.setVisibility(0);
                LoginDialog.this.tv_error_tip.setText(String.format(LoginDialog.this.context.getString(R.string.password_input_error_tip), String.valueOf(LoginDialog.this.passWordErrorCount), String.valueOf((LoginDialog.this.passWordErrorCount / 3) * 30)));
            }
            ToastUitl.showShort(LoginDialog.this.context, R.string.error_password);
        }
    }

    private void setListener() {
        this.button01.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.LoginDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (LoginDialog.this.passWordErrorCount >= 3 && LoginDialog.this.passWordErrorCount % 3 == 0 && LoginDialog.this.showPasswordErrorInputTip()) {
                    return;
                }
                String asString = CacheHelper.getFileCache().getAsString("passWord");
                if (asString == null || asString.length() == 0) {
                    asString = "888888";
                }
                String obj = LoginDialog.this.et_password.getText().toString();
                if (TextUtils.isEmpty(obj)) {
                    ToastUitl.showShort(LoginDialog.this.context, R.string.pls_input_password);
                    return;
                }
                if (asString.equals(obj)) {
                    LoginDialog.this.passWordErrorCount = 0;
                    CacheHelper.getFileCache().put("passWordInputErrorCount", "0");
                    LoginDialog.this.tv_error_tip.setVisibility(8);
                    LoginDialog.this.isPasswordRight = true;
                    LoginDialog.this.dismiss();
                    return;
                }
                LoginDialog.access$308(LoginDialog.this);
                CacheHelper.getFileCache().put("passWordInputErrorCount", String.valueOf(LoginDialog.this.passWordErrorCount));
                if (LoginDialog.this.passWordErrorCount % 3 == 0) {
                    CacheHelper.getFileCache().put("passWordInputErrorTime", String.valueOf(System.currentTimeMillis()));
                    LoginDialog.this.tv_error_tip.setVisibility(0);
                    LoginDialog.this.tv_error_tip.setText(String.format(LoginDialog.this.context.getString(R.string.password_input_error_tip), String.valueOf(LoginDialog.this.passWordErrorCount), String.valueOf((LoginDialog.this.passWordErrorCount / 3) * 30)));
                }
                ToastUitl.showShort(LoginDialog.this.context, R.string.error_password);
            }
        });
        this.button02.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.LoginDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LoginDialog.this.isNeedUpdatePassword = true;
                if (LoginDialog.this.buttonListener != null) {
                    LoginDialog.this.buttonListener.updatePassword();
                }
            }
        });
        this.button03.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.LoginDialog.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LoginDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.LoginDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            LoginDialog.this.isNeedUpdatePassword = true;
            if (LoginDialog.this.buttonListener != null) {
                LoginDialog.this.buttonListener.updatePassword();
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.LoginDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            LoginDialog.this.dismiss();
        }
    }

    public boolean showPasswordErrorInputTip() {
        String asString = CacheHelper.getFileCache().getAsString("passWordInputErrorTime");
        long currentTimeMillis = System.currentTimeMillis();
        if (TextUtils.isEmpty(asString)) {
            return false;
        }
        long longValue = currentTimeMillis - Long.valueOf(asString).longValue();
        long j = (this.passWordErrorCount / 3) * 30 * 60 * 1000;
        if (longValue >= j) {
            return false;
        }
        long j2 = ((j - longValue) / 1000) / 60;
        if (j2 == 0) {
            j2 = 1;
        }
        this.tv_error_tip.setVisibility(0);
        this.tv_error_tip.setText(String.format(this.context.getString(R.string.password_input_error_tip), String.valueOf(this.passWordErrorCount), String.valueOf(j2)));
        return true;
    }

    public void setButtonListener(ButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }
}
