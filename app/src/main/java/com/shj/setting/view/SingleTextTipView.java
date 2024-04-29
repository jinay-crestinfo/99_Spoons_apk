package com.shj.setting.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class SingleTextTipView extends LinearLayout {
    private Context context;

    public SingleTextTipView(Context context, String str) {
        super(context);
        this.context = context;
        initView(str);
    }

    private void initView(String str) {
        ((TextView) LayoutInflater.from(this.context).inflate(R.layout.layout_single_text_tip, this).findViewById(R.id.tv_error)).setText(str);
    }
}
