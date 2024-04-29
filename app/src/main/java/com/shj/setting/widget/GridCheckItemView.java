package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.widget.GargoCheckGridView;

/* loaded from: classes2.dex */
public class GridCheckItemView extends LinearLayout {
    private GargoCheckGridView.CheckData checkData;
    private CheckBox check_box;
    private Context context;
    private boolean isName;
    private String text;
    private TextView tv_index;

    public GridCheckItemView(Context context, GargoCheckGridView.CheckData checkData) {
        super(context);
        this.context = context;
        this.checkData = checkData;
        this.isName = false;
        initView();
    }

    public GridCheckItemView(Context context, String str) {
        super(context);
        this.context = context;
        this.text = str;
        this.isName = true;
        initView();
    }

    private void initView() {
        if (this.isName) {
            LayoutInflater.from(this.context).inflate(R.layout.layout_grid_check_item_name, this);
        } else {
            LayoutInflater.from(this.context).inflate(R.layout.layout_grid_check_item, this);
        }
        this.tv_index = (TextView) findViewById(R.id.tv_index);
        this.check_box = (CheckBox) findViewById(R.id.check_box);
        String str = this.text;
        if (str != null) {
            this.tv_index.setText(str);
        }
        GargoCheckGridView.CheckData checkData = this.checkData;
        if (checkData != null) {
            if (checkData.textIdentifier != null) {
                this.tv_index.setText(this.checkData.textIdentifier);
            } else {
                this.tv_index.setText(String.valueOf(this.checkData.identifier));
            }
        }
    }

    public CheckBox getCheckBox() {
        return this.check_box;
    }

    public void setBeforeSettingState(Boolean bool) {
        this.checkData.beforeSettingState = bool.booleanValue();
    }

    public GargoCheckGridView.CheckData getCheckData() {
        this.checkData.selectState = this.check_box.isChecked();
        return this.checkData;
    }
}
