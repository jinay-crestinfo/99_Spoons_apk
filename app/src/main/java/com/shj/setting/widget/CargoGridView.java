package com.shj.setting.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class CargoGridView extends AbsItemView {
    private static final int LINE_COUNT = 10;
    private GridItemData gridItemData;
    private boolean isShowAddAndReduce;
    private List<GridItemView> itemList;
    private int lineCount;

    /* loaded from: classes2.dex */
    public static class GridItemData {
        public List<IndexData> indexDataList;
        public String index_name;
        public int totalCount;
        public String value_name;
    }

    /* loaded from: classes2.dex */
    public static class IndexData {
        public String beforeSettingText;
        public int identifier;
        public String initText;
        public String inputText;
        public boolean needUpdate;
        public String textIdentifier;
        public int upperLevelNumber;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public CargoGridView(Context context, GridItemData gridItemData, boolean z) {
        super(context);
        this.lineCount = 10;
        this.gridItemData = gridItemData;
        this.isShowAddAndReduce = z;
        initView();
    }

    public CargoGridView(Context context, GridItemData gridItemData, boolean z, int i) {
        super(context);
        this.gridItemData = gridItemData;
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
                linearLayout.addView(new GridItemNameView(this.context, this.gridItemData));
            }
            GridItemView gridItemView = new GridItemView(this.context, this.gridItemData.indexDataList.get(i).identifier, this.gridItemData.indexDataList.get(i).textIdentifier, this.gridItemData.indexDataList.get(i).initText, this.gridItemData.indexDataList.get(i).upperLevelNumber);
            gridItemView.setShowAddAndReduce(this.isShowAddAndReduce);
            this.itemList.add(gridItemView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = -1;
            linearLayout.addView(gridItemView, layoutParams);
        }
    }

    public void setGridText(String str) {
        Iterator<GridItemView> it = this.itemList.iterator();
        while (it.hasNext()) {
            it.next().setInputText(str, true);
        }
    }

    public GridItemData getGridItemData() {
        return this.gridItemData;
    }

    public List<IndexData> getInputData() {
        ArrayList arrayList = new ArrayList();
        for (GridItemView gridItemView : this.itemList) {
            IndexData indexData = new IndexData();
            indexData.identifier = gridItemView.getIndex();
            indexData.inputText = gridItemView.getInputText();
            indexData.beforeSettingText = gridItemView.getBeforeSettingText();
            arrayList.add(indexData);
        }
        return arrayList;
    }

    public void setInputData(List<IndexData> list) {
        for (GridItemView gridItemView : this.itemList) {
            Iterator<IndexData> it = list.iterator();
            while (true) {
                if (it.hasNext()) {
                    IndexData next = it.next();
                    if (next.identifier == gridItemView.getIndex()) {
                        if (!TextUtils.isEmpty(next.inputText)) {
                            gridItemView.setInputText(next.inputText, false);
                        }
                    }
                }
            }
        }
    }

    public void setBeforeSettingText(int i, String str) {
        for (GridItemView gridItemView : this.itemList) {
            if (i == gridItemView.getIndex()) {
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                gridItemView.setBeforeSettingText(str);
                return;
            }
        }
    }

    public void setText(int i, String str) {
        for (GridItemView gridItemView : this.itemList) {
            if (i == gridItemView.getIndex()) {
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                gridItemView.setInputText(str, true);
                return;
            }
        }
    }

    public void setLayerData(List<IndexData> list) {
        for (IndexData indexData : list) {
            if (indexData.needUpdate) {
                for (GridItemView gridItemView : this.itemList) {
                    if (indexData.identifier == gridItemView.getUpperLevelNumber() && !TextUtils.isEmpty(indexData.inputText)) {
                        gridItemView.setInputText(indexData.inputText, true);
                    }
                }
            }
        }
    }

    public void updateData(GridItemData gridItemData) {
        for (int i = 0; i < this.itemList.size() && i < gridItemData.indexDataList.size(); i++) {
            GridItemView gridItemView = this.itemList.get(i);
            IndexData indexData = gridItemData.indexDataList.get(i);
            if (indexData.identifier == gridItemView.getIndex() && !TextUtils.isEmpty(indexData.initText)) {
                gridItemView.setInputText(indexData.initText, true);
            }
        }
    }

    public void setWholeData(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        Iterator<GridItemView> it = this.itemList.iterator();
        while (it.hasNext()) {
            it.next().setInputText(str, true);
        }
    }
}
