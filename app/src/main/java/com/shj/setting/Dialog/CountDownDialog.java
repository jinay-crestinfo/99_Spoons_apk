package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.shj.setting.Dialog.TipDialog;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class CountDownDialog extends Dialog {
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
    private TipDialog.TipDialogListener tipDialogListener;
    private TipDialog.TipDialogListenerEx tipDialogListenerEx;
    private TextView tv_count_down;
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

    static /* synthetic */ int access$310(CountDownDialog countDownDialog) {
        int i = countDownDialog.delayTime;
        countDownDialog.delayTime = i - 1;
        return i;
    }

    public CountDownDialog(Context context, int i, String str, String str2, String str3) {
        super(context, R.style.translucent_dialog_style);
        this.canBack = true;
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.CountDownDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                CountDownDialog.access$310(CountDownDialog.this);
                if (CountDownDialog.this.delayTime > 0) {
                    CountDownDialog.this.setCountDownText();
                    CountDownDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                    return;
                }
                try {
                    CountDownDialog.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (CountDownDialog.this.tipDialogListener != null) {
                    CountDownDialog.this.tipDialogListener.timeEnd();
                }
                if (CountDownDialog.this.tipDialogListenerEx != null) {
                    CountDownDialog.this.tipDialogListenerEx.timeEnd();
                }
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

    public CountDownDialog(Context context, int i, int i2, int i3, int i4) {
        super(context, R.style.loading_style);
        this.canBack = true;
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.CountDownDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                CountDownDialog.access$310(CountDownDialog.this);
                if (CountDownDialog.this.delayTime > 0) {
                    CountDownDialog.this.setCountDownText();
                    CountDownDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                    return;
                }
                try {
                    CountDownDialog.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (CountDownDialog.this.tipDialogListener != null) {
                    CountDownDialog.this.tipDialogListener.timeEnd();
                }
                if (CountDownDialog.this.tipDialogListenerEx != null) {
                    CountDownDialog.this.tipDialogListenerEx.timeEnd();
                }
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

    public CountDownDialog(Context context, int i, int i2, int i3, int i4, boolean z) {
        super(context, R.style.loading_style);
        this.canBack = true;
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.CountDownDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                CountDownDialog.access$310(CountDownDialog.this);
                if (CountDownDialog.this.delayTime > 0) {
                    CountDownDialog.this.setCountDownText();
                    CountDownDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                    return;
                }
                try {
                    CountDownDialog.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (CountDownDialog.this.tipDialogListener != null) {
                    CountDownDialog.this.tipDialogListener.timeEnd();
                }
                if (CountDownDialog.this.tipDialogListenerEx != null) {
                    CountDownDialog.this.tipDialogListenerEx.timeEnd();
                }
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

    public CountDownDialog(Context context, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(context, R.style.loading_style);
        this.canBack = true;
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.CountDownDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                CountDownDialog.access$310(CountDownDialog.this);
                if (CountDownDialog.this.delayTime > 0) {
                    CountDownDialog.this.setCountDownText();
                    CountDownDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                    return;
                }
                try {
                    CountDownDialog.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (CountDownDialog.this.tipDialogListener != null) {
                    CountDownDialog.this.tipDialogListener.timeEnd();
                }
                if (CountDownDialog.this.tipDialogListenerEx != null) {
                    CountDownDialog.this.tipDialogListenerEx.timeEnd();
                }
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
        setContentView(R.layout.layout_count_down_dialog);
        this.tv_count_down = (TextView) findViewById(R.id.tv_count_down);
        this.tv_msg = (TextView) findViewById(R.id.tv_msg);
        this.button01 = (Button) findViewById(R.id.button01);
        this.button02 = (Button) findViewById(R.id.button02);
        this.button03 = (Button) findViewById(R.id.button03);
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
        setCanceledOnTouchOutside(true);
        setListener();
    }

    private void setMssageText(boolean z) {
        this.tv_msg.setText(Html.fromHtml(this.msg));
        if (z) {
            this.tv_msg.setGravity(3);
        }
        setCountDownText();
    }

    public void setCountDownText() {
        this.tv_count_down.setText(String.valueOf(this.delayTime) + "s");
    }

    /* renamed from: com.shj.setting.Dialog.CountDownDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CountDownDialog.this.dismiss();
            CountDownDialog.this.handler.removeMessages(0);
            if (CountDownDialog.this.tipDialogListener != null) {
                CountDownDialog.this.tipDialogListener.buttonClick_01();
            }
            if (CountDownDialog.this.tipDialogListenerEx != null) {
                CountDownDialog.this.tipDialogListenerEx.buttonClick_01();
            }
        }
    }

    private void setListener() {
        this.button01.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.CountDownDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CountDownDialog.this.dismiss();
                CountDownDialog.this.handler.removeMessages(0);
                if (CountDownDialog.this.tipDialogListener != null) {
                    CountDownDialog.this.tipDialogListener.buttonClick_01();
                }
                if (CountDownDialog.this.tipDialogListenerEx != null) {
                    CountDownDialog.this.tipDialogListenerEx.buttonClick_01();
                }
            }
        });
        this.button02.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.CountDownDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CountDownDialog.this.dismiss();
                CountDownDialog.this.handler.removeMessages(0);
                if (CountDownDialog.this.tipDialogListener != null) {
                    CountDownDialog.this.tipDialogListener.buttonClick_02();
                }
                if (CountDownDialog.this.tipDialogListenerEx != null) {
                    CountDownDialog.this.tipDialogListenerEx.buttonClick_02();
                }
            }
        });
        this.button03.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.CountDownDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CountDownDialog.this.dismiss();
                CountDownDialog.this.handler.removeMessages(0);
                if (CountDownDialog.this.tipDialogListenerEx != null) {
                    CountDownDialog.this.tipDialogListenerEx.buttonClick_03();
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.CountDownDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CountDownDialog.this.dismiss();
            CountDownDialog.this.handler.removeMessages(0);
            if (CountDownDialog.this.tipDialogListener != null) {
                CountDownDialog.this.tipDialogListener.buttonClick_02();
            }
            if (CountDownDialog.this.tipDialogListenerEx != null) {
                CountDownDialog.this.tipDialogListenerEx.buttonClick_02();
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.CountDownDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CountDownDialog.this.dismiss();
            CountDownDialog.this.handler.removeMessages(0);
            if (CountDownDialog.this.tipDialogListenerEx != null) {
                CountDownDialog.this.tipDialogListenerEx.buttonClick_03();
            }
        }
    }

    public void setTipDialogListener(TipDialog.TipDialogListener tipDialogListener) {
        this.tipDialogListener = tipDialogListener;
    }

    public void setTipDialogListenerEx(TipDialog.TipDialogListenerEx tipDialogListenerEx) {
        this.tipDialogListenerEx = tipDialogListenerEx;
    }

    /* renamed from: com.shj.setting.Dialog.CountDownDialog$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 extends Handler {
        AnonymousClass4() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            CountDownDialog.access$310(CountDownDialog.this);
            if (CountDownDialog.this.delayTime > 0) {
                CountDownDialog.this.setCountDownText();
                CountDownDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                return;
            }
            try {
                CountDownDialog.this.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (CountDownDialog.this.tipDialogListener != null) {
                CountDownDialog.this.tipDialogListener.timeEnd();
            }
            if (CountDownDialog.this.tipDialogListenerEx != null) {
                CountDownDialog.this.tipDialogListenerEx.timeEnd();
            }
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

    @Override // android.app.Dialog
    public void onBackPressed() {
        if (this.canBack) {
            super.onBackPressed();
        }
    }
}
