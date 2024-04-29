package com.shj.setting.widget;

import android.content.Context;
import android.view.View;

/* loaded from: classes2.dex */
public class MultipleStyleChildItemView extends AbsItemView {
    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public MultipleStyleChildItemView(Context context) {
        super(context);
    }

    @Override // com.shj.setting.widget.AbsItemView
    public void setSaveButtonVisibility(int i) {
        super.setSaveButtonVisibility(i);
    }
}
