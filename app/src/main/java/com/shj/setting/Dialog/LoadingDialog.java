package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;
import com.xyshj.database.setting.SettingType;

/* loaded from: classes2.dex */
public class LoadingDialog extends Dialog {
    private LoadingDialog loadingDialog;
    private String tipText;

    /* renamed from: tv */
    private TextView f9tv;

    public LoadingDialog(Context context) {
        super(context, R.style.loading_style);
    }

    public LoadingDialog(Context context, String str) {
        super(context, R.style.loading_style);
        this.tipText = str;
    }

    public LoadingDialog(Context context, int i) {
        super(context, R.style.loading_style);
        this.tipText = context.getString(i);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_loading);
        TextView textView = (TextView) findViewById(R.id.f10tv);
        this.f9tv = textView;
        String str = this.tipText;
        if (str != null) {
            textView.setText(str);
        }
        ((LinearLayout) findViewById(R.id.LinearLayout)).getBackground().setAlpha(SettingType.PAYMENT_METHOD_YLX);
        setCanceledOnTouchOutside(true);
    }

    public void showLoading(Context context) {
        if (this.loadingDialog == null) {
            LoadingDialog loadingDialog = new LoadingDialog(context);
            this.loadingDialog = loadingDialog;
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        this.loadingDialog.show();
    }

    public void dismissLoading() {
        LoadingDialog loadingDialog = this.loadingDialog;
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
