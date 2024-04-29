package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class PureTextItemView extends AbsItemView {
    private TextView tv_value;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public PureTextItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_puretext, (ViewGroup) null);
        this.tv_value = (TextView) inflate.findViewById(R.id.tv_value);
        addContentView(inflate);
    }

    public void setText(String str) {
        this.tv_value.setText(str);
    }
}
