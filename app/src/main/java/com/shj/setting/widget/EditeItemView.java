package com.shj.setting.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.widget.MultipleEditItemView;

/* loaded from: classes2.dex */
public class EditeItemView extends LinearLayout {
    private Context context;
    private MultipleEditItemView.EditTextDataInfo data;
    private EditText et_input;
    private TextView tv_name;

    public EditeItemView(Context context, MultipleEditItemView.EditTextDataInfo editTextDataInfo) {
        super(context);
        this.context = context;
        this.data = editTextDataInfo;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_edite_item, this);
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.et_input = (EditText) findViewById(R.id.et_input);
        this.tv_name.setText(this.data.name);
        if (this.data.tipInfo != null) {
            SpannableString spannableString = new SpannableString(this.data.tipInfo);
            spannableString.setSpan(new AbsoluteSizeSpan(this.context.getResources().getDimensionPixelSize(R.dimen.hint_size), false), 0, spannableString.length(), 33);
            this.et_input.setHint(new SpannedString(spannableString));
        }
    }

    public void setInputTypeNumber() {
        this.et_input.setInputType(2);
    }

    public EditText getEditText() {
        return this.et_input;
    }

    public void setEditText(String str) {
        this.et_input.setText(str);
    }
}
