package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.widget.SpinnerItemView;

/* loaded from: classes2.dex */
public class QueryItemView extends AbsItemView {
    private Button bt_query;
    private String buttonName;
    private SpinnerItemView.SpinnerItemData newAddspinnerItemData;
    private SpinnerItemView newAddspinnerItemView;
    private TextView tv_value;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public QueryItemView(Context context, String str, SpinnerItemView.SpinnerItemData spinnerItemData) {
        super(context);
        this.buttonName = str;
        this.newAddspinnerItemData = spinnerItemData;
        initView();
    }

    public void initView() {
        if (this.newAddspinnerItemData != null) {
            SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, this.newAddspinnerItemData);
            this.newAddspinnerItemView = spinnerItemView;
            addContentView(spinnerItemView);
        }
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_query_item, (ViewGroup) null);
        this.bt_query = (Button) inflate.findViewById(R.id.bt_query);
        this.tv_value = (TextView) inflate.findViewById(R.id.tv_value);
        this.bt_query.setText(this.buttonName);
        addContentView(inflate);
    }

    public int getNewAddSelectIndex() {
        return this.newAddspinnerItemView.getSelectIndex();
    }

    public void setQueryListener(View.OnClickListener onClickListener) {
        this.bt_query.setOnClickListener(onClickListener);
    }

    public void setValueText(String str) {
        this.tv_value.setText(str);
    }

    public void setValueTextSize(float f) {
        this.tv_value.setTextSize(f);
    }

    public String getValueText() {
        return this.tv_value.getText().toString();
    }
}
