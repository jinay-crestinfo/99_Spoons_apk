package com.shj.setting.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes2.dex */
public class TwoLevelItemView extends AbsItemView {
    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public TwoLevelItemView(Context context) {
        super(context);
    }

    @Override // com.shj.setting.widget.AbsItemView
    public void setSaveButtonVisibility(int i) {
        ViewGroup centerContentView = getCenterContentView();
        int childCount = centerContentView.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = centerContentView.getChildAt(i2);
            if (childAt instanceof AbsItemView) {
                ((AbsItemView) childAt).setSaveButtonVisibility(i);
            }
        }
    }
}
