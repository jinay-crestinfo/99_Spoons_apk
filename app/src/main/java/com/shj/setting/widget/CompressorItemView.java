package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class CompressorItemView extends LinearLayout {
    private CompressorItemData compressorItemData;
    private Context context;

    /* loaded from: classes2.dex */
    public static class CompressorItemData {
        public int index;
    }

    public CompressorItemView(Context context, CompressorItemData compressorItemData) {
        super(context);
        this.context = context;
        this.compressorItemData = compressorItemData;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_compressor_item, this);
        ((TextView) findViewById(R.id.tv_index)).setText(String.valueOf(this.compressorItemData.index + 1));
    }
}
