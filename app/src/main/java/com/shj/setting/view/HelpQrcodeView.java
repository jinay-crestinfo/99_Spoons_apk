package com.shj.setting.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class HelpQrcodeView extends LinearLayout {
    private Context context;

    public HelpQrcodeView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_help_qrcode, this);
    }
}
