package com.shj.setting.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.Utils.SetUtils;

/* loaded from: classes2.dex */
public class NetCheckView extends LinearLayout {
    private Context context;

    public NetCheckView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        String str;
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_net_check, this);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_value);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_error_solution);
        if (SetUtils.isConnected(this.context)) {
            str = "" + this.context.getString(R.string.normal);
        } else {
            String str2 = "" + this.context.getString(R.string.net_error_tip);
            textView2.setVisibility(0);
            str = str2;
        }
        textView.setText(str);
    }
}
