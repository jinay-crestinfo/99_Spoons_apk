package com.shj.setting.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class ClickExecItemView extends AbsItemView {
    private Button button;
    private String instruction;
    private String name;
    private TextView tv_instruction;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public ClickExecItemView(Context context, String str, String str2) {
        super(context);
        this.instruction = str;
        this.name = str2;
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_click_exec_item, (ViewGroup) null);
        this.tv_instruction = (TextView) inflate.findViewById(R.id.tv_instruction);
        this.button = (Button) inflate.findViewById(R.id.button);
        if (!TextUtils.isEmpty(this.instruction)) {
            this.tv_instruction.setText(this.instruction);
        } else {
            this.tv_instruction.setVisibility(8);
        }
        this.button.setText(this.name);
        addContentView(inflate);
    }

    public void setExecClickListener(View.OnClickListener onClickListener) {
        this.button.setOnClickListener(onClickListener);
    }
}
