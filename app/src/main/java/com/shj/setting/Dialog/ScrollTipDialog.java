package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import com.shj.setting.R;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class ScrollTipDialog extends Dialog {
    private Button button01;
    private Button button02;
    private Button button03;
    private String buttonName01;
    private String buttonName02;
    private String buttonName03;
    private int delayTime;
    private Handler handler;
    private boolean isLeft;
    private String msg;
    private ScrollView sv_msg;
    private TipDialogListener tipDialogListener;
    private String title;
    private TextView tv_msg;
    private TextView tv_title;

    /* loaded from: classes2.dex */
    public interface TipDialogListener {
        void buttonClick_01();

        void buttonClick_02();

        void buttonClick_03();

        void timeEnd();
    }

    static /* synthetic */ int access$210(ScrollTipDialog scrollTipDialog) {
        int i = scrollTipDialog.delayTime;
        scrollTipDialog.delayTime = i - 1;
        return i;
    }

    public ScrollTipDialog(Context context, int i, String str, String str2, String str3, String str4, String str5, boolean z) {
        super(context, R.style.loading_style);
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.ScrollTipDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                try {
                    ScrollTipDialog.access$210(ScrollTipDialog.this);
                    if (ScrollTipDialog.this.delayTime <= 0) {
                        ScrollTipDialog.this.dismiss();
                        if (ScrollTipDialog.this.tipDialogListener != null) {
                            ScrollTipDialog.this.tipDialogListener.timeEnd();
                        }
                    } else {
                        ScrollTipDialog scrollTipDialog = ScrollTipDialog.this;
                        scrollTipDialog.setMssageText(scrollTipDialog.isLeft, true);
                        ScrollTipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                    }
                } catch (Exception unused) {
                }
            }
        };
        this.delayTime = i;
        this.title = str;
        this.msg = str2;
        this.buttonName01 = str3;
        this.buttonName02 = str4;
        this.buttonName03 = str5;
        this.isLeft = z;
        initView();
        if (i > 0) {
            this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
    }

    public ScrollTipDialog(Context context, int i, int i2, int i3, int i4, int i5) {
        super(context, R.style.loading_style);
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.ScrollTipDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                try {
                    ScrollTipDialog.access$210(ScrollTipDialog.this);
                    if (ScrollTipDialog.this.delayTime <= 0) {
                        ScrollTipDialog.this.dismiss();
                        if (ScrollTipDialog.this.tipDialogListener != null) {
                            ScrollTipDialog.this.tipDialogListener.timeEnd();
                        }
                    } else {
                        ScrollTipDialog scrollTipDialog = ScrollTipDialog.this;
                        scrollTipDialog.setMssageText(scrollTipDialog.isLeft, true);
                        ScrollTipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                    }
                } catch (Exception unused) {
                }
            }
        };
        this.delayTime = i;
        if (i2 > 0) {
            this.title = context.getString(i2);
        }
        if (i3 > 0) {
            this.msg = context.getString(i3);
        }
        if (i4 > 0) {
            this.buttonName01 = context.getString(i4);
        }
        if (i5 > 0) {
            this.buttonName02 = context.getString(i5);
        }
        initView();
        if (i > 0) {
            this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
    }

    public ScrollTipDialog(Context context, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(context, R.style.loading_style);
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.ScrollTipDialog.4
            AnonymousClass4() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                try {
                    ScrollTipDialog.access$210(ScrollTipDialog.this);
                    if (ScrollTipDialog.this.delayTime <= 0) {
                        ScrollTipDialog.this.dismiss();
                        if (ScrollTipDialog.this.tipDialogListener != null) {
                            ScrollTipDialog.this.tipDialogListener.timeEnd();
                        }
                    } else {
                        ScrollTipDialog scrollTipDialog = ScrollTipDialog.this;
                        scrollTipDialog.setMssageText(scrollTipDialog.isLeft, true);
                        ScrollTipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                    }
                } catch (Exception unused) {
                }
            }
        };
        this.delayTime = i;
        this.isLeft = z;
        if (i2 > 0) {
            this.title = context.getString(i2);
        }
        if (i3 > 0) {
            this.msg = context.getString(i3);
        }
        if (i4 > 0) {
            this.buttonName01 = context.getString(i4);
        }
        if (i5 > 0) {
            this.buttonName02 = context.getString(i5);
        }
        initView();
        if (i > 0) {
            this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
    }

    public void setTitleVisibility(int i) {
        this.tv_title.setVisibility(i);
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_dialog_tip_scroll);
        this.sv_msg = (ScrollView) findViewById(R.id.sv_msg);
        this.tv_title = (TextView) findViewById(R.id.title);
        this.tv_msg = (TextView) findViewById(R.id.tv_msg);
        this.button01 = (Button) findViewById(R.id.button01);
        this.button02 = (Button) findViewById(R.id.button02);
        this.button03 = (Button) findViewById(R.id.button03);
        setMssageText(this.isLeft, false);
        if (!TextUtils.isEmpty(this.title)) {
            this.tv_title.setText(this.title);
        }
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

    public void setSvMsgSize(int i, int i2) {
        this.sv_msg.setMinimumWidth(i);
        this.sv_msg.getLayoutParams().height = i2;
    }

    public void setMsgTextSize(int i) {
        this.tv_msg.setTextSize(i);
    }

    public void setMssageText(boolean z, boolean z2) {
        String str = this.msg;
        if (z2) {
            this.tv_title.setText(this.title + StringUtils.SPACE + this.delayTime);
        }
        this.tv_msg.setText(str);
        if (z) {
            this.tv_msg.setGravity(3);
        }
    }

    /* renamed from: com.shj.setting.Dialog.ScrollTipDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ScrollTipDialog.this.dismiss();
            ScrollTipDialog.this.handler.removeMessages(0);
            if (ScrollTipDialog.this.tipDialogListener != null) {
                ScrollTipDialog.this.tipDialogListener.buttonClick_01();
            }
        }
    }

    private void setListener() {
        this.button01.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScrollTipDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ScrollTipDialog.this.dismiss();
                ScrollTipDialog.this.handler.removeMessages(0);
                if (ScrollTipDialog.this.tipDialogListener != null) {
                    ScrollTipDialog.this.tipDialogListener.buttonClick_01();
                }
            }
        });
        this.button02.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScrollTipDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ScrollTipDialog.this.dismiss();
                ScrollTipDialog.this.handler.removeMessages(0);
                if (ScrollTipDialog.this.tipDialogListener != null) {
                    ScrollTipDialog.this.tipDialogListener.buttonClick_02();
                }
            }
        });
        this.button03.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ScrollTipDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ScrollTipDialog.this.dismiss();
                ScrollTipDialog.this.handler.removeMessages(0);
                if (ScrollTipDialog.this.tipDialogListener != null) {
                    ScrollTipDialog.this.tipDialogListener.buttonClick_03();
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.ScrollTipDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ScrollTipDialog.this.dismiss();
            ScrollTipDialog.this.handler.removeMessages(0);
            if (ScrollTipDialog.this.tipDialogListener != null) {
                ScrollTipDialog.this.tipDialogListener.buttonClick_02();
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.ScrollTipDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ScrollTipDialog.this.dismiss();
            ScrollTipDialog.this.handler.removeMessages(0);
            if (ScrollTipDialog.this.tipDialogListener != null) {
                ScrollTipDialog.this.tipDialogListener.buttonClick_03();
            }
        }
    }

    public void setTipDialogListener(TipDialogListener tipDialogListener) {
        this.tipDialogListener = tipDialogListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.Dialog.ScrollTipDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 extends Handler {
        AnonymousClass4() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                ScrollTipDialog.access$210(ScrollTipDialog.this);
                if (ScrollTipDialog.this.delayTime <= 0) {
                    ScrollTipDialog.this.dismiss();
                    if (ScrollTipDialog.this.tipDialogListener != null) {
                        ScrollTipDialog.this.tipDialogListener.timeEnd();
                    }
                } else {
                    ScrollTipDialog scrollTipDialog = ScrollTipDialog.this;
                    scrollTipDialog.setMssageText(scrollTipDialog.isLeft, true);
                    ScrollTipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                }
            } catch (Exception unused) {
            }
        }
    }
}
