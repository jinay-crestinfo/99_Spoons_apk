package com.shj.setting.widget;

import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import com.xyshj.database.setting.SettingType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class MultipleEditItemView extends AbsItemView {
    private List<EditTextDataInfo> dataList;
    private List<EditeItemView> editeItemViewList;

    /* loaded from: classes2.dex */
    public static class EditTextDataInfo {
        public String name;
        public String tipInfo;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public MultipleEditItemView(Context context, List<EditTextDataInfo> list) {
        super(context);
        this.dataList = list;
        initView();
    }

    private void initView() {
        this.editeItemViewList = new ArrayList();
        List<EditTextDataInfo> list = this.dataList;
        if (list != null) {
            Iterator<EditTextDataInfo> it = list.iterator();
            while (it.hasNext()) {
                EditeItemView editeItemView = new EditeItemView(this.context, it.next());
                this.editeItemViewList.add(editeItemView);
                addContentView(editeItemView);
            }
        }
    }

    public String getEditeText(int i) {
        if (i < this.editeItemViewList.size()) {
            return this.editeItemViewList.get(i).getEditText().getText().toString().trim();
        }
        return null;
    }

    public List<EditeItemView> getEditeItemViewList() {
        return this.editeItemViewList;
    }

    public void setEditeText(int i, String str) {
        if (i < this.editeItemViewList.size()) {
            this.editeItemViewList.get(i).getEditText().setText(str);
        }
    }

    public void setEditeTypePassword(int i) {
        if (i < this.editeItemViewList.size()) {
            this.editeItemViewList.get(i).getEditText().setInputType(SettingType.COMMODITY_ONE_BUTTON_SETUP);
        }
    }

    public void setEditeTypeInterger(int i) {
        if (i < this.editeItemViewList.size()) {
            this.editeItemViewList.get(i).getEditText().setInputType(2);
        }
    }

    public void setEditeTypeSignedInterger(int i) {
        if (i < this.editeItemViewList.size()) {
            this.editeItemViewList.get(i).getEditText().setInputType(4098);
        }
    }

    public void setEditeTypeDecimal(int i) {
        if (i < this.editeItemViewList.size()) {
            this.editeItemViewList.get(i).getEditText().setInputType(8194);
        }
    }

    public void setMaxInputCount(int i, int i2) {
        if (i < this.editeItemViewList.size()) {
            this.editeItemViewList.get(i).getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(i2)});
        }
    }
}
