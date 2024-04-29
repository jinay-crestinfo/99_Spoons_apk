package com.shj.setting.widget;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import com.shj.setting.widget.SpinnerItemView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class MultipleSpinnerItemView extends AbsItemView {
    private Context context;
    private List<SpinnerItemView.SpinnerItemData> selectData;
    private List<SpinnerItemView> spinnerList;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public MultipleSpinnerItemView(Context context, List<SpinnerItemView.SpinnerItemData> list) {
        super(context);
        this.context = context;
        this.selectData = list;
        initView();
    }

    private void initView() {
        if (this.selectData != null) {
            this.spinnerList = new ArrayList();
            Iterator<SpinnerItemView.SpinnerItemData> it = this.selectData.iterator();
            while (it.hasNext()) {
                SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, it.next());
                this.spinnerList.add(spinnerItemView);
                addContentView(spinnerItemView);
            }
        }
    }

    public int getSelect(int i) {
        if (i < this.spinnerList.size()) {
            return this.spinnerList.get(i).getSelectIndex();
        }
        return 0;
    }

    public int getDataIndex(int i) {
        if (i < this.spinnerList.size()) {
            return this.spinnerList.get(i).getDataIndex();
        }
        return -1;
    }

    public String getSelectValue(int i) {
        if (i < this.spinnerList.size()) {
            return this.spinnerList.get(i).getSelectValue();
        }
        return null;
    }

    public SpinnerItemView.SpinnerItemData getSelectData(int i) {
        if (i < this.selectData.size()) {
            return this.selectData.get(i);
        }
        return null;
    }

    public void setSelect(int i, int i2) {
        List<SpinnerItemView> list = this.spinnerList;
        if (list == null || i >= list.size()) {
            return;
        }
        this.spinnerList.get(i).setSelectIndex(i2);
    }

    public void setOnItemSelectedListener(int i, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        if (i < this.spinnerList.size()) {
            this.spinnerList.get(i).setOnItemSelectedListener(onItemSelectedListener);
        }
    }

    public void upDateData(int i, List<String> list) {
        if (i < this.spinnerList.size()) {
            this.spinnerList.get(i).upDateData(list);
        }
    }
}
