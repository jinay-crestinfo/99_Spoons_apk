package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.widget.GargoTimeGridView;

/* loaded from: classes2.dex */
public class GridTimeItemNameView extends LinearLayout {
    private Context context;
    private String index_name;
    private GargoTimeGridView.GridTimeItemData itemData;
    private TextView tv_index;
    private TextView tv_time_end;
    private TextView tv_time_start;

    public GridTimeItemNameView(Context context, GargoTimeGridView.GridTimeItemData gridTimeItemData) {
        super(context);
        this.context = context;
        this.itemData = gridTimeItemData;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_grid_time_name_item, this);
        this.tv_index = (TextView) findViewById(R.id.tv_index);
        this.tv_time_start = (TextView) findViewById(R.id.tv_time_start);
        this.tv_time_end = (TextView) findViewById(R.id.tv_time_end);
        GargoTimeGridView.GridTimeItemData gridTimeItemData = this.itemData;
        if (gridTimeItemData != null) {
            this.tv_index.setText(gridTimeItemData.index_name);
            this.tv_time_start.setText(this.itemData.time_start);
            this.tv_time_end.setText(this.itemData.time_end);
            int color = this.context.getResources().getColor(R.color.color_gargo);
            this.tv_index.setBackgroundColor(color);
            this.tv_time_start.setBackgroundColor(color);
            this.tv_time_end.setBackgroundColor(color);
        }
        String str = this.index_name;
        if (str != null) {
            this.tv_index.setText(str);
        }
    }
}
