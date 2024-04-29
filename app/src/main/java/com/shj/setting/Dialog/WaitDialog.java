package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class WaitDialog extends Dialog {
    public static final int CHANGE_WAIT_DIALOG = 1;
    private Handler mHandler;
    private String message;
    private TextView messageText;

    public WaitDialog(Context context) {
        super(context);
        this.message = "数据获取中…  ";
        this.messageText = null;
        this.mHandler = new Handler() { // from class: com.shj.setting.Dialog.WaitDialog.1
            AnonymousClass1() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    return;
                }
                WaitDialog.this.messageText.setText(WaitDialog.this.message);
            }
        };
    }

    public WaitDialog(Context context, int i) {
        super(context, i);
        this.message = "数据获取中…  ";
        this.messageText = null;
        this.mHandler = new Handler() { // from class: com.shj.setting.Dialog.WaitDialog.1
            AnonymousClass1() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    return;
                }
                WaitDialog.this.messageText.setText(WaitDialog.this.message);
            }
        };
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        getWindow().setGravity(17);
        setContentView(R.layout.wait_dialog_setting);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.dimAmount = 0.8f;
        attributes.height = -2;
        attributes.width = -2;
        getWindow().setAttributes(attributes);
        getWindow().addFlags(2);
        TextView textView = (TextView) findViewById(R.id.waitMessage);
        this.messageText = textView;
        textView.setText(this.message);
        super.onCreate(bundle);
    }

    public void showIcon(boolean z) {
        findViewById(R.id.waitGif).setVisibility(z ? 0 : 8);
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return super.onKeyDown(i, keyEvent);
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public void changeMessage(String str) {
        if (this.messageText != null) {
            this.message = str;
            this.mHandler.sendEmptyMessage(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.Dialog.WaitDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            WaitDialog.this.messageText.setText(WaitDialog.this.message);
        }
    }
}
