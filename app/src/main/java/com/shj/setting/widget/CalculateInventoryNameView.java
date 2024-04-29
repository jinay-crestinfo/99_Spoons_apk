package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.widget.CalculateInventoryView;

/* loaded from: classes2.dex */
public class CalculateInventoryNameView extends LinearLayout {
    private Context context;
    private CalculateInventoryView.CIItemData gridItemData;
    private TextView tv_index;
    private TextView tv_name;

    public CalculateInventoryNameView(Context context, CalculateInventoryView.CIItemData cIItemData) {
        super(context);
        this.context = context;
        this.gridItemData = cIItemData;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_calculate_inventory_name, this);
        this.tv_index = (TextView) findViewById(R.id.tv_index);
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.tv_index.setText(this.gridItemData.index_name);
        this.tv_name.setText(this.gridItemData.value_name);
    }
}
