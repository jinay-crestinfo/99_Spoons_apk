package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class TextItemView extends AbsItemView {
    private TextItemData textItemData;
    private TextView tv_name;
    private TextView tv_value;

    /* loaded from: classes2.dex */
    public static class TextItemData {
        public String name;
        public String value;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public TextItemView(Context context, TextItemData textItemData) {
        super(context);
        this.textItemData = textItemData;
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_text_item, (ViewGroup) null);
        this.tv_name = (TextView) inflate.findViewById(R.id.tv_name);
        this.tv_value = (TextView) inflate.findViewById(R.id.tv_value);
        this.tv_name.setText(this.textItemData.name);
        this.tv_value.setText(this.textItemData.value);
        addContentView(inflate);
    }

    public TextView getTextValue() {
        return this.tv_value;
    }

    public void setTextValue(String str) {
        this.tv_value.setText(str);
    }
}
