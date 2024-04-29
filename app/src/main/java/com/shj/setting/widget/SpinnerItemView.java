package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.shj.setting.R;
import java.util.List;

/* loaded from: classes2.dex */
public class SpinnerItemView extends LinearLayout {
    private ArrayAdapter<String> arr_adapter;
    private Context context;
    private Spinner spinner;
    private SpinnerItemData spinnerItemData;
    private TextView tv_name;

    /* loaded from: classes2.dex */
    public static class SpinnerItemData {
        public List<String> dataList;
        public List<Integer> indexList;
        public String name;
    }

    public SpinnerItemView(Context context, SpinnerItemData spinnerItemData) {
        super(context);
        this.context = context;
        this.spinnerItemData = spinnerItemData;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_spinner_item, this);
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        this.tv_name.setText(this.spinnerItemData.name);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.context, R.layout.layout_spinner_item_simple, this.spinnerItemData.dataList);
        this.arr_adapter = arrayAdapter;
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter((SpinnerAdapter) this.arr_adapter);
    }

    public void upDateData(List<String> list) {
        this.spinnerItemData.dataList.clear();
        this.spinnerItemData.dataList.addAll(list);
        this.arr_adapter.notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return this.spinner.getSelectedItemPosition();
    }

    public int getDataIndex() {
        return this.spinnerItemData.indexList.get(this.spinner.getSelectedItemPosition()).intValue();
    }

    public void setSelectIndex(int i) {
        if (i < this.spinner.getCount()) {
            this.spinner.setSelection(i);
        }
    }

    public String getSelectValue() {
        return this.spinnerItemData.dataList.get(this.spinner.getSelectedItemPosition());
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    public void setSelection(int i, boolean z) {
        this.spinner.setSelection(i, z);
    }
}
