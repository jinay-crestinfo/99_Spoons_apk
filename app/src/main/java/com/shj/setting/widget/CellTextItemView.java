package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class CellTextItemView extends LinearLayout {
    private CellTextItemData cellTextItemData;
    private Context context;

    /* loaded from: classes2.dex */
    public static class CellTextItemData {
        public boolean isTitle;
        public String name;
        public String value;
    }

    public CellTextItemView(Context context, CellTextItemData cellTextItemData) {
        super(context);
        this.context = context;
        this.cellTextItemData = cellTextItemData;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_cell_text_item, this);
        TextView textView = (TextView) findViewById(R.id.tv_index);
        TextView textView2 = (TextView) findViewById(R.id.tv_value);
        textView.setText(this.cellTextItemData.name);
        textView2.setText(this.cellTextItemData.value);
        if (this.cellTextItemData.isTitle) {
            setBackgroundColor(this.context.getResources().getColor(R.color.color_gargo));
        }
    }
}
