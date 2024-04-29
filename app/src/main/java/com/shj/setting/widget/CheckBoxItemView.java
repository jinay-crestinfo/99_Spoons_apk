package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class CheckBoxItemView extends LinearLayout {
    private CheckBox check_box;
    private Context context;
    private String name;
    private TextView tv_name;

    public CheckBoxItemView(Context context, String str) {
        super(context);
        this.context = context;
        this.name = str;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_checkbox_item, this);
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.check_box = (CheckBox) findViewById(R.id.check_box);
        this.tv_name.setText(this.name);
    }

    public CheckBox getCheckBox() {
        return this.check_box;
    }
}
