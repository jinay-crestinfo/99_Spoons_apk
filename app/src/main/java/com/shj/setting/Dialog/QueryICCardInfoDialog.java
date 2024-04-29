package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class QueryICCardInfoDialog extends Dialog {
    private Button button01;
    private Button button02;
    private String buttonName01;
    private String buttonName02;
    private int delayTime;
    private Handler handler;
    private boolean isLeft;
    private String msg;
    private TipDialogListener tipDialogListener;
    private TextView tv_msg;

    /* loaded from: classes2.dex */
    public interface TipDialogListener {
        void buttonClick_01();

        void buttonClick_02();

        void onDismiss();

        void timeEnd();
    }

    static /* synthetic */ int access$210(QueryICCardInfoDialog queryICCardInfoDialog) {
        int i = queryICCardInfoDialog.delayTime;
        queryICCardInfoDialog.delayTime = i - 1;
        return i;
    }

    public QueryICCardInfoDialog(Context context, int i, String str, String str2, String str3) {
        super(context, R.style.loading_style);
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.QueryICCardInfoDialog.3
            AnonymousClass3() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                QueryICCardInfoDialog.access$210(QueryICCardInfoDialog.this);
                if (QueryICCardInfoDialog.this.delayTime <= 0) {
                    QueryICCardInfoDialog.this.dismiss();
                    if (QueryICCardInfoDialog.this.tipDialogListener != null) {
                        QueryICCardInfoDialog.this.tipDialogListener.timeEnd();
                        QueryICCardInfoDialog.this.tipDialogListener.onDismiss();
                        return;
                    }
                    return;
                }
                QueryICCardInfoDialog queryICCardInfoDialog = QueryICCardInfoDialog.this;
                queryICCardInfoDialog.setMssageText(queryICCardInfoDialog.isLeft);
                QueryICCardInfoDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
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

    public QueryICCardInfoDialog(Context context, int i, int i2, int i3, int i4) {
        super(context, R.style.loading_style);
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.QueryICCardInfoDialog.3
            AnonymousClass3() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                QueryICCardInfoDialog.access$210(QueryICCardInfoDialog.this);
                if (QueryICCardInfoDialog.this.delayTime <= 0) {
                    QueryICCardInfoDialog.this.dismiss();
                    if (QueryICCardInfoDialog.this.tipDialogListener != null) {
                        QueryICCardInfoDialog.this.tipDialogListener.timeEnd();
                        QueryICCardInfoDialog.this.tipDialogListener.onDismiss();
                        return;
                    }
                    return;
                }
                QueryICCardInfoDialog queryICCardInfoDialog = QueryICCardInfoDialog.this;
                queryICCardInfoDialog.setMssageText(queryICCardInfoDialog.isLeft);
                QueryICCardInfoDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
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

    public QueryICCardInfoDialog(Context context, int i, int i2, int i3, int i4, boolean z) {
        super(context, R.style.loading_style);
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.QueryICCardInfoDialog.3
            AnonymousClass3() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                QueryICCardInfoDialog.access$210(QueryICCardInfoDialog.this);
                if (QueryICCardInfoDialog.this.delayTime <= 0) {
                    QueryICCardInfoDialog.this.dismiss();
                    if (QueryICCardInfoDialog.this.tipDialogListener != null) {
                        QueryICCardInfoDialog.this.tipDialogListener.timeEnd();
                        QueryICCardInfoDialog.this.tipDialogListener.onDismiss();
                        return;
                    }
                    return;
                }
                QueryICCardInfoDialog queryICCardInfoDialog = QueryICCardInfoDialog.this;
                queryICCardInfoDialog.setMssageText(queryICCardInfoDialog.isLeft);
                QueryICCardInfoDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
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

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.layout_queryiccardinfo_dialog);
        this.tv_msg = (TextView) findViewById(R.id.tv_msg);
        this.button01 = (Button) findViewById(R.id.button01);
        this.button02 = (Button) findViewById(R.id.button02);
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
        setCanceledOnTouchOutside(false);
        setListener();
    }

    public void setMssageText(boolean z) {
        String str = this.msg;
        if (str.contains("#0#")) {
            str = str.replace("#0#", "" + this.delayTime);
        } else if (str.contains("0")) {
            str = str.replace("0", "" + this.delayTime);
        }
        this.tv_msg.setText(str);
        if (z) {
            this.tv_msg.setGravity(3);
        }
    }

    /* renamed from: com.shj.setting.Dialog.QueryICCardInfoDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            QueryICCardInfoDialog.this.dismiss();
            QueryICCardInfoDialog.this.handler.removeMessages(0);
            if (QueryICCardInfoDialog.this.tipDialogListener != null) {
                QueryICCardInfoDialog.this.tipDialogListener.buttonClick_01();
                QueryICCardInfoDialog.this.tipDialogListener.onDismiss();
            }
        }
    }

    private void setListener() {
        this.button01.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.QueryICCardInfoDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                QueryICCardInfoDialog.this.dismiss();
                QueryICCardInfoDialog.this.handler.removeMessages(0);
                if (QueryICCardInfoDialog.this.tipDialogListener != null) {
                    QueryICCardInfoDialog.this.tipDialogListener.buttonClick_01();
                    QueryICCardInfoDialog.this.tipDialogListener.onDismiss();
                }
            }
        });
        this.button02.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.QueryICCardInfoDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                QueryICCardInfoDialog.this.dismiss();
                QueryICCardInfoDialog.this.handler.removeMessages(0);
                if (QueryICCardInfoDialog.this.tipDialogListener != null) {
                    QueryICCardInfoDialog.this.tipDialogListener.buttonClick_02();
                    QueryICCardInfoDialog.this.tipDialogListener.onDismiss();
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.QueryICCardInfoDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            QueryICCardInfoDialog.this.dismiss();
            QueryICCardInfoDialog.this.handler.removeMessages(0);
            if (QueryICCardInfoDialog.this.tipDialogListener != null) {
                QueryICCardInfoDialog.this.tipDialogListener.buttonClick_02();
                QueryICCardInfoDialog.this.tipDialogListener.onDismiss();
            }
        }
    }

    public void setTipDialogListener(TipDialogListener tipDialogListener) {
        this.tipDialogListener = tipDialogListener;
    }

    /* renamed from: com.shj.setting.Dialog.QueryICCardInfoDialog$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 extends Handler {
        AnonymousClass3() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            QueryICCardInfoDialog.access$210(QueryICCardInfoDialog.this);
            if (QueryICCardInfoDialog.this.delayTime <= 0) {
                QueryICCardInfoDialog.this.dismiss();
                if (QueryICCardInfoDialog.this.tipDialogListener != null) {
                    QueryICCardInfoDialog.this.tipDialogListener.timeEnd();
                    QueryICCardInfoDialog.this.tipDialogListener.onDismiss();
                    return;
                }
                return;
            }
            QueryICCardInfoDialog queryICCardInfoDialog = QueryICCardInfoDialog.this;
            queryICCardInfoDialog.setMssageText(queryICCardInfoDialog.isLeft);
            QueryICCardInfoDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
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
    }
}
