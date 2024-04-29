package com.xyshj.machine.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.TextView;
import com.xyshj.machine.R;

/* loaded from: classes2.dex */
public class TextTipDialog extends Dialog {
    private int delayTime;
    private Handler handler;
    private int initDelayTime;
    DialogInterface.OnKeyListener keylistener;
    private String msg;
    private TextView tv_msg;

    private void setListener() {
    }

    static /* synthetic */ int access$010(TextTipDialog textTipDialog) {
        int i = textTipDialog.delayTime;
        textTipDialog.delayTime = i - 1;
        return i;
    }

    public TextTipDialog(Context context, String str) {
        super(context, R.style.loading_style);
        this.initDelayTime = 10;
        this.keylistener = new DialogInterface.OnKeyListener() { // from class: com.xyshj.machine.view.TextTipDialog.1
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4 && keyEvent.getRepeatCount() == 0;
            }
        };
        AnonymousClass2 anonymousClass2 = new Handler() { // from class: com.xyshj.machine.view.TextTipDialog.2
            AnonymousClass2() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                TextTipDialog.access$010(TextTipDialog.this);
                if (TextTipDialog.this.delayTime > 0) {
                    TextTipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                } else {
                    TextTipDialog.this.dismiss();
                }
            }
        };
        this.handler = anonymousClass2;
        this.msg = str;
        this.delayTime = this.initDelayTime;
        anonymousClass2.sendEmptyMessageDelayed(0, 1000L);
        initView();
    }

    public TextTipDialog(Context context, int i, String str) {
        super(context, R.style.loading_style);
        this.initDelayTime = 10;
        this.keylistener = new DialogInterface.OnKeyListener() { // from class: com.xyshj.machine.view.TextTipDialog.1
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i2, KeyEvent keyEvent) {
                return i2 == 4 && keyEvent.getRepeatCount() == 0;
            }
        };
        this.handler = new Handler() { // from class: com.xyshj.machine.view.TextTipDialog.2
            AnonymousClass2() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                TextTipDialog.access$010(TextTipDialog.this);
                if (TextTipDialog.this.delayTime > 0) {
                    TextTipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                } else {
                    TextTipDialog.this.dismiss();
                }
            }
        };
        this.delayTime = i;
        this.msg = str;
        initView();
        if (i > 0) {
            this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
    }

    public TextTipDialog(Context context, int i, int i2) {
        super(context, R.style.loading_style);
        this.initDelayTime = 10;
        this.keylistener = new DialogInterface.OnKeyListener() { // from class: com.xyshj.machine.view.TextTipDialog.1
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i22, KeyEvent keyEvent) {
                return i22 == 4 && keyEvent.getRepeatCount() == 0;
            }
        };
        this.handler = new Handler() { // from class: com.xyshj.machine.view.TextTipDialog.2
            AnonymousClass2() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                TextTipDialog.access$010(TextTipDialog.this);
                if (TextTipDialog.this.delayTime > 0) {
                    TextTipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                } else {
                    TextTipDialog.this.dismiss();
                }
            }
        };
        this.delayTime = i;
        this.msg = context.getString(i2);
        initView();
        if (i > 0) {
            this.handler.sendEmptyMessageDelayed(0, 1000L);
        }
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_dialog_tip_text);
        TextView textView = (TextView) findViewById(R.id.tv_msg);
        this.tv_msg = textView;
        textView.setText(this.msg);
        setCanceledOnTouchOutside(false);
        setListener();
        setOnKeyListener(this.keylistener);
        setCancelable(false);
    }

    public void setMsg(String str) {
        this.msg = str;
        this.tv_msg.setText(str);
    }

    /* renamed from: com.xyshj.machine.view.TextTipDialog$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements DialogInterface.OnKeyListener {
        AnonymousClass1() {
        }

        @Override // android.content.DialogInterface.OnKeyListener
        public boolean onKey(DialogInterface dialogInterface, int i22, KeyEvent keyEvent) {
            return i22 == 4 && keyEvent.getRepeatCount() == 0;
        }
    }

    /* renamed from: com.xyshj.machine.view.TextTipDialog$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 extends Handler {
        AnonymousClass2() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            TextTipDialog.access$010(TextTipDialog.this);
            if (TextTipDialog.this.delayTime > 0) {
                TextTipDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
            } else {
                TextTipDialog.this.dismiss();
            }
        }
    }
}
