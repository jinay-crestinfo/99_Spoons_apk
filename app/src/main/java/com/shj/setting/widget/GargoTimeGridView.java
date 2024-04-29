package com.shj.setting.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.oysb.utils.CommonTool;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class GargoTimeGridView extends AbsItemView {
    private static int LINE_COUNT = 7;
    private GridTimeItemData gridItemData;
    private List<GridTimeItemView> itemViewList;

    /* loaded from: classes2.dex */
    public static class GridTimeItemData {
        public String index_name;
        public List<TimeItemData> timeItemDataList;
        public String time_end;
        public String time_start;
        public int totalCount;
    }

    /* loaded from: classes2.dex */
    public static class TimeItemData {
        public int identifier;
        public String textIdentifier;
        public int upperLevelNumber;
        public int star = -1;
        public int end = -1;
        public int beforeSettingStar = -1;
        public int beforeSettingEnd = -1;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public GargoTimeGridView(Context context, GridTimeItemData gridTimeItemData) {
        super(context);
        this.gridItemData = gridTimeItemData;
        if (!"zh".equalsIgnoreCase(CommonTool.getLanguage(context))) {
            LINE_COUNT = 5;
        }
        initView();
    }

    private void initView() {
        this.itemViewList = new ArrayList();
        LinearLayout linearLayout = null;
        for (int i = 0; i < this.gridItemData.totalCount; i++) {
            if (i % LINE_COUNT == 0) {
                linearLayout = new LinearLayout(this.context);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                linearLayout.setOrientation(0);
                addContentView(linearLayout);
                linearLayout.addView(new GridTimeItemNameView(this.context, this.gridItemData));
            }
            GridTimeItemView gridTimeItemView = new GridTimeItemView(this.context, this.gridItemData.timeItemDataList.get(i));
            this.itemViewList.add(gridTimeItemView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = -1;
            linearLayout.addView(gridTimeItemView, layoutParams);
        }
    }

    public List<TimeItemData> getTimeItemDataList() {
        ArrayList arrayList = new ArrayList();
        Iterator<GridTimeItemView> it = this.itemViewList.iterator();
        while (it.hasNext()) {
            TimeItemData itemData = it.next().getItemData();
            if (itemData.star != itemData.beforeSettingStar || itemData.end != itemData.beforeSettingEnd) {
                arrayList.add(itemData);
            }
        }
        return arrayList;
    }

    public void setTimeItemData(int i, String str, String str2) {
        for (GridTimeItemView gridTimeItemView : this.itemViewList) {
            if (gridTimeItemView.getIdentifier() == i) {
                gridTimeItemView.setItemData(str, str2);
                gridTimeItemView.setBeforeSettingData(str, str2);
                return;
            }
        }
    }

    public void setTimeLayerData(int i, String str, String str2) {
        for (GridTimeItemView gridTimeItemView : this.itemViewList) {
            if (gridTimeItemView.getUpperLevelNumber() == i) {
                gridTimeItemView.setItemData(str, str2);
                gridTimeItemView.setBeforeSettingData(str, str2);
            }
        }
    }

    public void setTimeData(String str, String str2) {
        for (GridTimeItemView gridTimeItemView : this.itemViewList) {
            gridTimeItemView.setItemData(str, str2);
            gridTimeItemView.setBeforeSettingData(str, str2);
        }
    }
}
