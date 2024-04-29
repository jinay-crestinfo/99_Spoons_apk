package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.widget.CargoGridView;

/* loaded from: classes2.dex */
public class GridItemNameView extends LinearLayout {
    private Context context;
    private CargoGridView.GridItemData gridItemData;
    private TextView tv_index;
    private TextView tv_name;

    public GridItemNameView(Context context, CargoGridView.GridItemData gridItemData) {
        super(context);
        this.context = context;
        this.gridItemData = gridItemData;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_grid_item_name, this);
        this.tv_index = (TextView) findViewById(R.id.tv_index);
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.tv_index.setText(this.gridItemData.index_name);
        this.tv_name.setText(this.gridItemData.value_name);
    }
}
