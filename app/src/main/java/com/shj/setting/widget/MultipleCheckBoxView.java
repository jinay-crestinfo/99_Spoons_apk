package com.shj.setting.widget;

import android.content.Context;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MultipleCheckBoxView extends AbsItemView {
    private List<CheckBoxData> checkBoxDataList;
    private List<CheckBoxItemView> checkBoxItemViewList;

    /* loaded from: classes2.dex */
    public static class CheckBoxData {
        public boolean isHide;
        public String name;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public MultipleCheckBoxView(Context context, List<CheckBoxData> list) {
        super(context);
        this.checkBoxDataList = list;
        initView();
    }

    private void initView() {
        this.checkBoxItemViewList = new ArrayList();
        List<CheckBoxData> list = this.checkBoxDataList;
        if (list != null) {
            for (CheckBoxData checkBoxData : list) {
                CheckBoxItemView checkBoxItemView = new CheckBoxItemView(this.context, checkBoxData.name);
                if (checkBoxData.isHide) {
                    checkBoxItemView.setVisibility(8);
                }
                this.checkBoxItemViewList.add(checkBoxItemView);
                addContentView(checkBoxItemView);
            }
        }
    }

    public boolean isChecked(int i) {
        if (i < this.checkBoxItemViewList.size()) {
            return this.checkBoxItemViewList.get(i).getCheckBox().isChecked();
        }
        return false;
    }

    public void setChecked(int i, boolean z) {
        if (i < this.checkBoxItemViewList.size()) {
            this.checkBoxItemViewList.get(i).getCheckBox().setChecked(z);
        }
    }
}
