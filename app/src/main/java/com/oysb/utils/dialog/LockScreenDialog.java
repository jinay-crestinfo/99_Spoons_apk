package com.oysb.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.oysb.utils.R;

/* loaded from: classes2.dex */
public class LockScreenDialog extends Dialog {
    private Context context;
    private TextView textView;

    @Override // android.app.Dialog
    public void onBackPressed() {
    }

    public LockScreenDialog(Context context) {
        super(context, R.style.lock_screen_style);
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setBackgroundResource(R.drawable.select_dialog_bg);
        TextView textView = new TextView(this.context);
        this.textView = textView;
        textView.setWidth(540);
        this.textView.setHeight(200);
        this.textView.setGravity(17);
        this.textView.setTextSize(36.0f);
        this.textView.setText("暂停营业...");
        linearLayout.addView(this.textView);
        setContentView(linearLayout);
        setCanceledOnTouchOutside(false);
    }

    public void setMessage(String str) {
        TextView textView = this.textView;
        if (textView != null) {
            textView.setText(str);
        }
    }
}
