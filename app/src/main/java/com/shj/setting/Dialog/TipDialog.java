package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class TipDialog extends Dialog {
    private LinearLayout body;
    private Button button01;
    private Button button02;
    private Button button03;
    private String buttonName01;
    private String buttonName02;
    private String buttonName03;
    private boolean canBack;
    private int delayTime;
    private Handler handler;
    private boolean isLeft;
    private String msg;
    private TipDialogListener tipDialogListener;
    private TipDialogListenerEx tipDialogListenerEx;
    private TextView title;
    private TextView tv_msg;

    /* loaded from: classes2.dex */
    public interface TipDialogListener {
        void buttonClick_01();

        void buttonClick_02();

        void timeEnd();
    }

    /* loaded from: classes2.dex */
    public interface TipDialogListenerEx {
        void buttonClick_01();

        void buttonClick_02();

        void buttonClick_03();

        void timeEnd();
    }

    static /* synthetic */ int access$310(TipDialog tipDialog) {
        int i = tipDialog.delayTime;
        tipDialog.delayTime = i - 1;
        return i;
    }

    public TipDialog(Context context, String str, String str2) {
        this(context, 0, str, str2, "");
    }

    public TipDialog(Context context, String str, String str2, String str3) {
        this(context, 0, str, str2, str3);
    }

    public TipDialog(Context context, int i, String str, String str2, String str3) {
        super(context, R.style.loading_style);
        this.canBack = true;
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.TipDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                TipDialog.access$310(TipDialog.this);
                if (TipDialog.this.delayTime <= 0) {
                    try {
                        TipDialog.this.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (TipDialog.this.tipDialogListener != null) {
                        TipDialog.this.tipDialogListener.timeEnd();
                    }
                    if (TipDialog.this.tipDialogListenerEx != null) {
                        TipDialog.this.tipDialogListenerEx.timeEnd();
                        return;
                    }
                    return;
                }
                TipDialog tipDialog = TipDialog.this;
                tipDialog.setMssageText(tipDialog.isLeft);
                TipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
            }
        };
        this.delayTime = i;
        this.msg = str;
        this.buttonName01 = str2;
        this.buttonName02 = str3;
        initView();
        if (i > 0) {
            this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
    }

    public TipDialog(Context context, int i, int i2, int i3, int i4) {
        super(context, R.style.loading_style);
        this.canBack = true;
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.TipDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                TipDialog.access$310(TipDialog.this);
                if (TipDialog.this.delayTime <= 0) {
                    try {
                        TipDialog.this.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (TipDialog.this.tipDialogListener != null) {
                        TipDialog.this.tipDialogListener.timeEnd();
                    }
                    if (TipDialog.this.tipDialogListenerEx != null) {
                        TipDialog.this.tipDialogListenerEx.timeEnd();
                        return;
                    }
                    return;
                }
                TipDialog tipDialog = TipDialog.this;
                tipDialog.setMssageText(tipDialog.isLeft);
                TipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
            }
        };
        this.delayTime = i;
        this.msg = context.getString(i2);
        if (i3 > 0) {
            this.buttonName01 = context.getString(i3);
        }
        if (i4 > 0) {
            this.buttonName02 = context.getString(i4);
        }
        initView();
        if (i > 0) {
            this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
    }

    public TipDialog(Context context, int i, int i2, int i3, int i4, boolean z) {
        super(context, R.style.loading_style);
        this.canBack = true;
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.TipDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                TipDialog.access$310(TipDialog.this);
                if (TipDialog.this.delayTime <= 0) {
                    try {
                        TipDialog.this.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (TipDialog.this.tipDialogListener != null) {
                        TipDialog.this.tipDialogListener.timeEnd();
                    }
                    if (TipDialog.this.tipDialogListenerEx != null) {
                        TipDialog.this.tipDialogListenerEx.timeEnd();
                        return;
                    }
                    return;
                }
                TipDialog tipDialog = TipDialog.this;
                tipDialog.setMssageText(tipDialog.isLeft);
                TipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
            }
        };
        this.delayTime = i;
        this.msg = context.getString(i2);
        this.isLeft = z;
        if (i3 > 0) {
            this.buttonName01 = context.getString(i3);
        }
        if (i4 > 0) {
            this.buttonName02 = context.getString(i4);
        }
        initView();
        if (i > 0) {
            this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
    }

    public TipDialog(Context context, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(context, R.style.loading_style);
        this.canBack = true;
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.TipDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                TipDialog.access$310(TipDialog.this);
                if (TipDialog.this.delayTime <= 0) {
                    try {
                        TipDialog.this.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (TipDialog.this.tipDialogListener != null) {
                        TipDialog.this.tipDialogListener.timeEnd();
                    }
                    if (TipDialog.this.tipDialogListenerEx != null) {
                        TipDialog.this.tipDialogListenerEx.timeEnd();
                        return;
                    }
                    return;
                }
                TipDialog tipDialog = TipDialog.this;
                tipDialog.setMssageText(tipDialog.isLeft);
                TipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
            }
        };
        this.delayTime = i;
        this.msg = context.getString(i2);
        this.isLeft = z;
        if (i3 > 0) {
            this.buttonName01 = context.getString(i3);
        }
        if (i4 > 0) {
            this.buttonName02 = context.getString(i4);
        }
        if (i5 > 0) {
            this.buttonName03 = context.getString(i5);
        }
        initView();
        if (i > 0) {
            this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_dialog_tip);
        this.title = (TextView) findViewById(R.id.title);
        this.tv_msg = (TextView) findViewById(R.id.tv_msg);
        this.button01 = (Button) findViewById(R.id.button01);
        this.button02 = (Button) findViewById(R.id.button02);
        this.button03 = (Button) findViewById(R.id.button03);
        this.body = (LinearLayout) findViewById(R.id.body);
        setMssageText(this.isLeft);
        if (!TextUtils.isEmpty(this.buttonName01)) {
            this.button01.setText(this.buttonName01);
            this.button01.setVisibility(0);
        } else {
            this.button01.setVisibility(8);
        }
        if (!TextUtils.isEmpty(this.buttonName02)) {
            this.button02.setText(this.buttonName02);
            this.button02.setVisibility(0);
        } else {
            this.button02.setVisibility(8);
        }
        if (!TextUtils.isEmpty(this.buttonName03)) {
            this.button03.setText(this.buttonName03);
            this.button03.setVisibility(0);
        } else {
            this.button03.setVisibility(8);
        }
        setCanceledOnTouchOutside(false);
        setListener();
    }

    public void setWidth(int i) {
        this.tv_msg.setMinWidth(i);
    }

    public void setMessage(String str) {
        this.tv_msg.setText(str);
    }

    public void setMssageText(boolean z) {
        String str = this.msg;
        if (str.contains("#0#")) {
            str = str.replace("#0#", "" + this.delayTime);
        }
        this.tv_msg.setText(Html.fromHtml(str.replaceAll(StringUtils.LF, "<br>")));
        if (z) {
            this.tv_msg.setGravity(3);
        }
    }

    public void setTitle(String str) {
        TextView textView = this.title;
        if (textView != null) {
            textView.setText(str);
        }
    }

    /* renamed from: com.shj.setting.Dialog.TipDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TipDialog.this.dismiss();
            TipDialog.this.handler.removeMessages(0);
            if (TipDialog.this.tipDialogListener != null) {
                TipDialog.this.tipDialogListener.buttonClick_01();
            }
            if (TipDialog.this.tipDialogListenerEx != null) {
                TipDialog.this.tipDialogListenerEx.buttonClick_01();
            }
        }
    }

    private void setListener() {
        this.button01.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.TipDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TipDialog.this.dismiss();
                TipDialog.this.handler.removeMessages(0);
                if (TipDialog.this.tipDialogListener != null) {
                    TipDialog.this.tipDialogListener.buttonClick_01();
                }
                if (TipDialog.this.tipDialogListenerEx != null) {
                    TipDialog.this.tipDialogListenerEx.buttonClick_01();
                }
            }
        });
        this.button02.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.TipDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TipDialog.this.dismiss();
                TipDialog.this.handler.removeMessages(0);
                if (TipDialog.this.tipDialogListener != null) {
                    TipDialog.this.tipDialogListener.buttonClick_02();
                }
                if (TipDialog.this.tipDialogListenerEx != null) {
                    TipDialog.this.tipDialogListenerEx.buttonClick_02();
                }
            }
        });
        this.button03.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.TipDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TipDialog.this.dismiss();
                TipDialog.this.handler.removeMessages(0);
                if (TipDialog.this.tipDialogListenerEx != null) {
                    TipDialog.this.tipDialogListenerEx.buttonClick_03();
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.TipDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TipDialog.this.dismiss();
            TipDialog.this.handler.removeMessages(0);
            if (TipDialog.this.tipDialogListener != null) {
                TipDialog.this.tipDialogListener.buttonClick_02();
            }
            if (TipDialog.this.tipDialogListenerEx != null) {
                TipDialog.this.tipDialogListenerEx.buttonClick_02();
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.TipDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TipDialog.this.dismiss();
            TipDialog.this.handler.removeMessages(0);
            if (TipDialog.this.tipDialogListenerEx != null) {
                TipDialog.this.tipDialogListenerEx.buttonClick_03();
            }
        }
    }

    public void setTipDialogListener(TipDialogListener tipDialogListener) {
        this.tipDialogListener = tipDialogListener;
    }

    public void setTipDialogListenerEx(TipDialogListenerEx tipDialogListenerEx) {
        this.tipDialogListenerEx = tipDialogListenerEx;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.Dialog.TipDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 extends Handler {
        AnonymousClass4() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            TipDialog.access$310(TipDialog.this);
            if (TipDialog.this.delayTime <= 0) {
                try {
                    TipDialog.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TipDialog.this.tipDialogListener != null) {
                    TipDialog.this.tipDialogListener.timeEnd();
                }
                if (TipDialog.this.tipDialogListenerEx != null) {
                    TipDialog.this.tipDialogListenerEx.timeEnd();
                    return;
                }
                return;
            }
            TipDialog tipDialog = TipDialog.this;
            tipDialog.setMssageText(tipDialog.isLeft);
            TipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
    }

    public void setCanBack(boolean z) {
        this.canBack = z;
    }

    public void setButtonBackgroud(int i) {
        Button button = this.button01;
        if (button != null) {
            button.setBackgroundResource(i);
        }
        Button button2 = this.button02;
        if (button2 != null) {
            button2.setBackgroundResource(i);
        }
        Button button3 = this.button03;
        if (button3 != null) {
            button3.setBackgroundResource(i);
        }
    }

    public void setButtonBackgroud(int i, int i2) {
        Button button;
        if (i == 0) {
            Button button2 = this.button01;
            if (button2 != null) {
                button2.setBackgroundResource(i2);
                return;
            }
            return;
        }
        if (i == 1) {
            Button button3 = this.button02;
            if (button3 != null) {
                button3.setBackgroundResource(i2);
                return;
            }
            return;
        }
        if (i != 2 || (button = this.button03) == null) {
            return;
        }
        button.setBackgroundResource(i2);
    }

    public void setDialogWidth(int i) {
        this.body.setMinimumWidth(i);
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        if (this.canBack) {
            super.onBackPressed();
        }
    }
}
