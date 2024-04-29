package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class OutItemView extends LinearLayout {
    private Button bt_save;
    private Button bt_save_signle;
    protected Context context;
    private LinearLayout ll_center_content;

    public OutItemView(Context context) {
        super(context);
        this.context = context;
        initBaseView();
    }

    public void initBaseView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_out_item, this);
        this.ll_center_content = (LinearLayout) findViewById(R.id.ll_center_content);
        this.bt_save = (Button) findViewById(R.id.bt_save);
        this.bt_save_signle = (Button) findViewById(R.id.bt_save_signle);
    }

    public void addContentView(View view) {
        this.ll_center_content.addView(view);
    }

    public LinearLayout getCenterContentView() {
        return this.ll_center_content;
    }

    public Button getSaveButton() {
        return this.bt_save;
    }

    public Button getSaveSignleButton() {
        return this.bt_save_signle;
    }
}
