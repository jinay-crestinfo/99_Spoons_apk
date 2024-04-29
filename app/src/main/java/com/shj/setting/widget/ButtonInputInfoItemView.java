package com.shj.setting.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class ButtonInputInfoItemView extends AbsItemView {
    private Button button;
    private ButtonInputInfoData buttonInputInfoData;
    private EditText et_input;

    /* loaded from: classes2.dex */
    public static class ButtonInputInfoData {
        public String name;
        public String tipInfo;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public ButtonInputInfoItemView(Context context, ButtonInputInfoData buttonInputInfoData) {
        super(context);
        this.buttonInputInfoData = buttonInputInfoData;
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_button_input_info_item, (ViewGroup) null);
        this.button = (Button) inflate.findViewById(R.id.button);
        this.et_input = (EditText) inflate.findViewById(R.id.et_input);
        this.button.setText(this.buttonInputInfoData.name);
        if (this.buttonInputInfoData.tipInfo != null) {
            SpannableString spannableString = new SpannableString(this.buttonInputInfoData.tipInfo);
            spannableString.setSpan(new AbsoluteSizeSpan(this.context.getResources().getDimensionPixelSize(R.dimen.hint_size), false), 0, spannableString.length(), 33);
            this.et_input.setHint(new SpannedString(spannableString));
        }
        addContentView(inflate);
    }

    public void setEditeTypeInterger() {
        this.et_input.setInputType(2);
    }

    public void setButtonOnClickListener(View.OnClickListener onClickListener) {
        this.button.setOnClickListener(onClickListener);
    }

    public String getInputString() {
        return this.et_input.getText().toString();
    }

    public void setEditText(String str) {
        this.et_input.setText(str);
    }

    public EditText getEditText() {
        return this.et_input;
    }
}
