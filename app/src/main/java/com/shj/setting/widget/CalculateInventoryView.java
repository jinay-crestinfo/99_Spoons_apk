package com.shj.setting.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class CalculateInventoryView extends AbsItemView {
    private static final int LINE_COUNT = 10;
    private CIItemData gridItemData;
    private boolean isShowAddAndReduce;
    private List<CalculateInventoryItemView> itemList;
    private int lineCount;

    /* loaded from: classes2.dex */
    public static class CIIndexData {
        public String beforeSettingText;
        public int calculateCount;
        public int identifier;
        public String initText;
        public String inputText;
        public int localCount;
        public boolean needUpdate;
        public String textIdentifier;
        public int upperLevelNumber;
    }

    /* loaded from: classes2.dex */
    public static class CIItemData {
        public List<CIIndexData> indexDataList;
        public String index_name;
        public int totalCount;
        public String value_name;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public CalculateInventoryView(Context context, CIItemData cIItemData, boolean z) {
        super(context);
        this.lineCount = 10;
        this.gridItemData = cIItemData;
        this.isShowAddAndReduce = z;
        initView();
    }

    public CalculateInventoryView(Context context, CIItemData cIItemData, boolean z, int i) {
        super(context);
        this.gridItemData = cIItemData;
        this.isShowAddAndReduce = z;
        this.lineCount = i;
        initView();
    }

    private void initView() {
        this.itemList = new ArrayList();
        LinearLayout linearLayout = null;
        for (int i = 0; i < this.gridItemData.totalCount; i++) {
            if (i % this.lineCount == 0) {
                linearLayout = new LinearLayout(this.context);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                linearLayout.setOrientation(0);
                addContentView(linearLayout);
                linearLayout.addView(new CalculateInventoryNameView(this.context, this.gridItemData));
            }
            String str = this.gridItemData.indexDataList.get(i).initText;
            int i2 = this.gridItemData.indexDataList.get(i).upperLevelNumber;
            CalculateInventoryItemView calculateInventoryItemView = new CalculateInventoryItemView(this.context, this.gridItemData.indexDataList.get(i));
            calculateInventoryItemView.setShowAddAndReduce(this.isShowAddAndReduce);
            this.itemList.add(calculateInventoryItemView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = -1;
            linearLayout.addView(calculateInventoryItemView, layoutParams);
        }
    }

    public CIItemData getGridItemData() {
        return this.gridItemData;
    }

    public List<CIIndexData> getInputData() {
        ArrayList arrayList = new ArrayList();
        for (CalculateInventoryItemView calculateInventoryItemView : this.itemList) {
            CIIndexData cIIndexData = new CIIndexData();
            cIIndexData.identifier = calculateInventoryItemView.getIndex();
            cIIndexData.inputText = calculateInventoryItemView.getInputText();
            cIIndexData.calculateCount = calculateInventoryItemView.getCalculateCount();
            arrayList.add(cIIndexData);
        }
        return arrayList;
    }

    public void setInputData(List<CIIndexData> list) {
        for (CalculateInventoryItemView calculateInventoryItemView : this.itemList) {
            Iterator<CIIndexData> it = list.iterator();
            while (true) {
                if (it.hasNext()) {
                    CIIndexData next = it.next();
                    if (next.identifier == calculateInventoryItemView.getIndex()) {
                        if (!TextUtils.isEmpty(next.inputText)) {
                            calculateInventoryItemView.setLengthData(next.inputText);
                        }
                    }
                }
            }
        }
    }

    public void updateCalculateCount(int i, int i2) {
        for (int i3 = 0; i3 < this.itemList.size(); i3++) {
            CalculateInventoryItemView calculateInventoryItemView = this.itemList.get(i3);
            if (i == calculateInventoryItemView.getIndex()) {
                calculateInventoryItemView.setCalculateCount(i2);
            }
        }
    }
}
