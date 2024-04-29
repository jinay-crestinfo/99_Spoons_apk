package com.shj.setting.widget;

import android.content.Context;
import android.view.View;
import com.shj.setting.widget.MultipleEditItemView;
import com.shj.setting.widget.SpinnerItemView;

/* loaded from: classes2.dex */
public class PickUpPortView extends AbsItemView {
    private EditeItemView editeItemView;
    private SpinnerItemView spinnerItemView;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public PickUpPortView(Context context, SpinnerItemView.SpinnerItemData spinnerItemData, MultipleEditItemView.EditTextDataInfo editTextDataInfo) {
        super(context);
        this.context = context;
        initView(spinnerItemData, editTextDataInfo);
    }

    private void initView(SpinnerItemView.SpinnerItemData spinnerItemData, MultipleEditItemView.EditTextDataInfo editTextDataInfo) {
        SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, spinnerItemData);
        this.spinnerItemView = spinnerItemView;
        addContentView(spinnerItemView);
        EditeItemView editeItemView = new EditeItemView(this.context, editTextDataInfo);
        this.editeItemView = editeItemView;
        editeItemView.getEditText().setInputType(2);
        addContentView(this.editeItemView);
    }

    public int getCabinet() {
        return this.spinnerItemView.getSelectIndex();
    }

    public void setDelayTime(String str) {
        this.editeItemView.setEditText(str);
    }

    public String getDelayTime() {
        return this.editeItemView.getEditText().getText().toString();
    }
}
