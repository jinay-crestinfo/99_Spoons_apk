package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.widget.GargoRadioGridView;

/* loaded from: classes2.dex */
public class GridRadioItemView extends LinearLayout {
    private Context context;
    private GargoRadioGridView.GridRadioItemData gridRadioItemData;
    private RadioGroup radio_group;
    private RadioButton rb_01;
    private RadioButton rb_02;
    private TextView tv_index;

    public GridRadioItemView(Context context, GargoRadioGridView.GridRadioItemData gridRadioItemData) {
        super(context);
        this.context = context;
        this.gridRadioItemData = gridRadioItemData;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_grid_radio_item, this);
        this.tv_index = (TextView) findViewById(R.id.tv_index);
        this.radio_group = (RadioGroup) findViewById(R.id.radio_group);
        this.rb_01 = (RadioButton) findViewById(R.id.rb_01);
        this.rb_02 = (RadioButton) findViewById(R.id.rb_02);
        this.tv_index.setText(this.gridRadioItemData.index_name);
        this.rb_01.setText(this.gridRadioItemData.unable_name);
        this.rb_02.setText(this.gridRadioItemData.enable_name);
        if (this.gridRadioItemData.initState == 1) {
            this.rb_01.setChecked(true);
        } else if (this.gridRadioItemData.initState == 2) {
            this.rb_02.setChecked(true);
        }
    }

    public void setGroupOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener onCheckedChangeListener) {
        this.radio_group.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    public void setGroupTag(int i) {
        this.radio_group.setTag(Integer.valueOf(i));
    }

    public int getCheckIndex() {
        if (this.rb_01.isChecked()) {
            return 1;
        }
        return this.rb_02.isChecked() ? 2 : 0;
    }

    public GargoRadioGridView.GridRadioItemData getRridRadioItemData() {
        this.gridRadioItemData.selectState = getCheckIndex();
        return this.gridRadioItemData;
    }

    public void setCheckIndex(int i) {
        if (i == 1) {
            this.rb_01.setChecked(true);
        } else if (i == 2) {
            this.rb_02.setChecked(true);
        }
    }

    public void setBeforeSettingState(int i) {
        this.gridRadioItemData.beforeSettingState = i;
    }

    public void setCheckId(int i) {
        if (i == R.id.rb_01) {
            this.rb_01.setChecked(true);
        } else if (i == R.id.rb_02) {
            this.rb_02.setChecked(true);
        }
    }
}
