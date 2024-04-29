package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shj.setting.R;
import java.util.List;

/* loaded from: classes2.dex */
public class MutipleTextItemView extends AbsItemView {
    private List<String> nameList;
    private TextView tv_name;
    private TextView tv_value;
    private String value;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public MutipleTextItemView(Context context, List<String> list) {
        super(context);
        this.nameList = list;
        initView();
    }

    private void initView() {
        List<String> list = this.nameList;
        if (list != null) {
            for (String str : list) {
                View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_text_item, (ViewGroup) null);
                this.tv_name = (TextView) inflate.findViewById(R.id.tv_name);
                this.tv_value = (TextView) inflate.findViewById(R.id.tv_value);
                this.tv_name.setText(str);
                addContentView(inflate);
            }
        }
    }

    public TextView getTextValue() {
        return this.tv_value;
    }
}
