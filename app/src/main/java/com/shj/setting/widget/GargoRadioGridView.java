package com.shj.setting.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import com.oysb.utils.CommonTool;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class GargoRadioGridView extends AbsItemView {
    private static int LINE_COUNT = 4;
    private GridRadioItemData gridRadioItemData;
    private List<RadioData> radioDataList;

    /* loaded from: classes2.dex */
    public static class GridRadioItemData {
        public int beforeSettingState;
        public String enable_name;
        public int identifier;
        public String index_name;
        public int initState;
        public List<RadioItemData> radioItemData;
        public int selectState;
        public int totalCount;
        public String unable_name;
    }

    /* loaded from: classes2.dex */
    public static class RadioData {
        public List<GridRadioItemView> radioList;
        public GridRadioItemView radio_all;
    }

    /* loaded from: classes2.dex */
    public static class RadioItemData {
        public int beforeSettingState;
        public int identifier;
        public String identifierName;
        public int initState;
        public int selectState;
        public int upperLevelNumber;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public GargoRadioGridView(Context context, GridRadioItemData gridRadioItemData) {
        super(context);
        this.gridRadioItemData = gridRadioItemData;
        initView();
    }

    private void initView() {
        if (!"zh".equalsIgnoreCase(CommonTool.getLanguage(this.context))) {
            LINE_COUNT = 3;
        }
        this.radioDataList = new ArrayList();
        RadioData radioData = null;
        LinearLayout linearLayout = null;
        int i = 0;
        for (int i2 = 0; i2 < this.gridRadioItemData.totalCount; i2++) {
            if (i2 % LINE_COUNT == 0) {
                LinearLayout linearLayout2 = new LinearLayout(this.context);
                linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                linearLayout2.setOrientation(0);
                addContentView(linearLayout2);
                GridRadioItemView gridRadioItemView = new GridRadioItemView(this.context, this.gridRadioItemData);
                linearLayout2.addView(gridRadioItemView);
                RadioData radioData2 = new RadioData();
                radioData2.radio_all = gridRadioItemView;
                gridRadioItemView.setGroupTag(i);
                radioData2.radioList = new ArrayList();
                this.radioDataList.add(radioData2);
                gridRadioItemView.setGroupOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.shj.setting.widget.GargoRadioGridView.1
                    AnonymousClass1() {
                    }

                    @Override // android.widget.RadioGroup.OnCheckedChangeListener
                    public void onCheckedChanged(RadioGroup radioGroup, int i3) {
                        Iterator<GridRadioItemView> it = ((RadioData) GargoRadioGridView.this.radioDataList.get(((Integer) radioGroup.getTag()).intValue())).radioList.iterator();
                        while (it.hasNext()) {
                            it.next().setCheckId(i3);
                        }
                    }
                });
                i++;
                linearLayout = linearLayout2;
                radioData = radioData2;
            }
            GridRadioItemData gridRadioItemData = new GridRadioItemData();
            gridRadioItemData.identifier = this.gridRadioItemData.radioItemData.get(i2).identifier;
            if (TextUtils.isEmpty(this.gridRadioItemData.radioItemData.get(i2).identifierName)) {
                gridRadioItemData.index_name = String.valueOf(gridRadioItemData.identifier);
            } else {
                gridRadioItemData.index_name = this.gridRadioItemData.radioItemData.get(i2).identifierName;
            }
            gridRadioItemData.enable_name = this.gridRadioItemData.enable_name;
            gridRadioItemData.unable_name = this.gridRadioItemData.unable_name;
            gridRadioItemData.initState = this.gridRadioItemData.radioItemData.get(i2).initState;
            GridRadioItemView gridRadioItemView2 = new GridRadioItemView(this.context, gridRadioItemData);
            radioData.radioList.add(gridRadioItemView2);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = -1;
            linearLayout.addView(gridRadioItemView2, layoutParams);
        }
    }

    /* renamed from: com.shj.setting.widget.GargoRadioGridView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RadioGroup.OnCheckedChangeListener {
        AnonymousClass1() {
        }

        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup, int i3) {
            Iterator<GridRadioItemView> it = ((RadioData) GargoRadioGridView.this.radioDataList.get(((Integer) radioGroup.getTag()).intValue())).radioList.iterator();
            while (it.hasNext()) {
                it.next().setCheckId(i3);
            }
        }
    }

    public List<RadioItemData> getSelectData(boolean z) {
        ArrayList arrayList = new ArrayList();
        Iterator<RadioData> it = this.radioDataList.iterator();
        while (it.hasNext()) {
            for (GridRadioItemView gridRadioItemView : it.next().radioList) {
                GridRadioItemData rridRadioItemData = gridRadioItemView.getRridRadioItemData();
                RadioItemData radioItemData = new RadioItemData();
                radioItemData.identifier = rridRadioItemData.identifier;
                radioItemData.selectState = gridRadioItemView.getCheckIndex();
                if (z) {
                    if (radioItemData.selectState != rridRadioItemData.beforeSettingState) {
                        arrayList.add(radioItemData);
                    }
                } else {
                    arrayList.add(radioItemData);
                }
            }
        }
        return arrayList;
    }

    public void upDateState(int i, int i2) {
        Iterator<RadioData> it = this.radioDataList.iterator();
        while (it.hasNext()) {
            for (GridRadioItemView gridRadioItemView : it.next().radioList) {
                if (i == gridRadioItemView.getRridRadioItemData().identifier) {
                    gridRadioItemView.setCheckIndex(i2);
                    gridRadioItemView.setBeforeSettingState(i2);
                    return;
                }
            }
        }
    }

    public void upDateAllState(int i) {
        Iterator<RadioData> it = this.radioDataList.iterator();
        while (it.hasNext()) {
            for (GridRadioItemView gridRadioItemView : it.next().radioList) {
                gridRadioItemView.setCheckIndex(i);
                gridRadioItemView.setBeforeSettingState(i);
            }
        }
    }
}
