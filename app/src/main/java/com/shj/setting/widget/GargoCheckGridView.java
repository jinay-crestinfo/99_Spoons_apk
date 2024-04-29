package com.shj.setting.widget;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class GargoCheckGridView extends AbsItemView {
    private static final int LINE_COUNT = 10;
    private List<CheckBoxData> checkBoxDataList;
    private GridCheckItemData gridItemData;

    /* loaded from: classes2.dex */
    public static class CheckBoxData {
        public CheckBox checkBox_all;
        public List<GridCheckItemView> itemViewList;
    }

    /* loaded from: classes2.dex */
    public static class CheckData {
        public boolean beforeSettingState;
        public int identifier;
        public boolean selectState;
        public String textIdentifier;
        public int upperLevelNumber;
    }

    /* loaded from: classes2.dex */
    public static class GridCheckItemData {
        public List<CheckData> checkDataList;
        public String index_name;
        public int totalCount;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public GargoCheckGridView(Context context, GridCheckItemData gridCheckItemData) {
        super(context);
        this.gridItemData = gridCheckItemData;
        initView();
    }

    private void initView() {
        this.checkBoxDataList = new ArrayList();
        CheckBoxData checkBoxData = null;
        LinearLayout linearLayout = null;
        int i = 0;
        for (int i2 = 0; i2 < this.gridItemData.totalCount; i2++) {
            if (i2 % 10 == 0) {
                LinearLayout linearLayout2 = new LinearLayout(this.context);
                linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                linearLayout2.setOrientation(0);
                addContentView(linearLayout2);
                GridCheckItemView gridCheckItemView = new GridCheckItemView(this.context, this.gridItemData.index_name);
                linearLayout2.addView(gridCheckItemView);
                CheckBoxData checkBoxData2 = new CheckBoxData();
                CheckBox checkBox = gridCheckItemView.getCheckBox();
                checkBoxData2.checkBox_all = checkBox;
                checkBox.setTag(Integer.valueOf(i));
                checkBoxData2.itemViewList = new ArrayList();
                this.checkBoxDataList.add(checkBoxData2);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shj.setting.widget.GargoCheckGridView.1
                    AnonymousClass1() {
                    }

                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                        Iterator<GridCheckItemView> it = ((CheckBoxData) GargoCheckGridView.this.checkBoxDataList.get(((Integer) compoundButton.getTag()).intValue())).itemViewList.iterator();
                        while (it.hasNext()) {
                            it.next().getCheckBox().setChecked(z);
                        }
                    }
                });
                i++;
                linearLayout = linearLayout2;
                checkBoxData = checkBoxData2;
            }
            GridCheckItemView gridCheckItemView2 = new GridCheckItemView(this.context, this.gridItemData.checkDataList.get(i2));
            checkBoxData.itemViewList.add(gridCheckItemView2);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = -1;
            linearLayout.addView(gridCheckItemView2, layoutParams);
        }
    }

    /* renamed from: com.shj.setting.widget.GargoCheckGridView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements CompoundButton.OnCheckedChangeListener {
        AnonymousClass1() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            Iterator<GridCheckItemView> it = ((CheckBoxData) GargoCheckGridView.this.checkBoxDataList.get(((Integer) compoundButton.getTag()).intValue())).itemViewList.iterator();
            while (it.hasNext()) {
                it.next().getCheckBox().setChecked(z);
            }
        }
    }

    public void upDataView(int i, boolean z) {
        Iterator<CheckBoxData> it = this.checkBoxDataList.iterator();
        while (it.hasNext()) {
            List<GridCheckItemView> list = it.next().itemViewList;
            if (list != null) {
                for (GridCheckItemView gridCheckItemView : list) {
                    if (gridCheckItemView.getCheckData().identifier == i) {
                        gridCheckItemView.getCheckBox().setChecked(z);
                        gridCheckItemView.setBeforeSettingState(Boolean.valueOf(z));
                        return;
                    }
                }
            }
        }
    }

    public void upDataLayerView(int i, boolean z) {
        Iterator<CheckBoxData> it = this.checkBoxDataList.iterator();
        while (it.hasNext()) {
            List<GridCheckItemView> list = it.next().itemViewList;
            if (list != null) {
                for (GridCheckItemView gridCheckItemView : list) {
                    if (gridCheckItemView.getCheckData().upperLevelNumber == i) {
                        gridCheckItemView.getCheckBox().setChecked(z);
                        gridCheckItemView.setBeforeSettingState(Boolean.valueOf(z));
                    }
                }
            }
        }
    }

    public void upDateView(boolean z) {
        Iterator<CheckBoxData> it = this.checkBoxDataList.iterator();
        while (it.hasNext()) {
            List<GridCheckItemView> list = it.next().itemViewList;
            if (list != null) {
                for (GridCheckItemView gridCheckItemView : list) {
                    gridCheckItemView.getCheckBox().setChecked(z);
                    gridCheckItemView.setBeforeSettingState(Boolean.valueOf(z));
                }
            }
        }
    }

    public List<CheckBoxData> getCheckBoxDataList() {
        return this.checkBoxDataList;
    }
}
