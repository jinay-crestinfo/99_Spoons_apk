package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.oysb.utils.CommonTool;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class GoodDetailDialog extends Dialog {
    private Button button_close;
    private Context context;
    private String msg;
    private String title;
    private TextView tv_msg;
    private TextView tv_title;

    public GoodDetailDialog(Context context, String str, String str2) {
        super(context, R.style.goods_detail_style);
        this.context = context;
        this.msg = str2;
        this.title = str;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        if (this.msg.length() > (!"zh".equalsIgnoreCase(CommonTool.getLanguage(this.context)) ? 110 : 50)) {
            setContentView(R.layout.layout_dialog_goods_detail_big);
        } else {
            setContentView(R.layout.layout_dialog_goods_detail);
        }
        this.tv_title = (TextView) findViewById(R.id.tv_title);
        this.tv_msg = (TextView) findViewById(R.id.tv_msg);
        this.button_close = (Button) findViewById(R.id.button_close);
        this.tv_msg.setText("        " + this.msg);
        this.tv_title.setText(this.title);
        setCanceledOnTouchOutside(true);
        setListener();
    }

    public void updateDetailInfo(String str, String str2) {
        this.title = str;
        this.msg = str2;
        this.tv_msg.setText("        " + this.msg);
        this.tv_title.setText(str);
    }

    /* renamed from: com.shj.setting.Dialog.GoodDetailDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GoodDetailDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.button_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GoodDetailDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GoodDetailDialog.this.dismiss();
            }
        });
    }
}
