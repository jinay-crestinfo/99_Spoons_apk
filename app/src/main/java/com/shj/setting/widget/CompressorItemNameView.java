package com.shj.setting.widget;

import android.content.Context;
import android.support.media.ExifInterface;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class CompressorItemNameView extends LinearLayout {
    private Context context;

    public CompressorItemNameView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_compressor_item_name, this);
        TextView textView = (TextView) findViewById(R.id.tv_index);
        TextView textView2 = (TextView) findViewById(R.id.tv_time01);
        TextView textView3 = (TextView) findViewById(R.id.tv_time02);
        TextView textView4 = (TextView) findViewById(R.id.tv_time03);
        textView.setText(R.string.cabinet_number);
        String string = this.context.getResources().getString(R.string.working_time);
        textView2.setText(String.format(string, "1"));
        textView3.setText(String.format(string, ExifInterface.GPS_MEASUREMENT_2D));
        textView4.setText(String.format(string, ExifInterface.GPS_MEASUREMENT_3D));
    }
}
