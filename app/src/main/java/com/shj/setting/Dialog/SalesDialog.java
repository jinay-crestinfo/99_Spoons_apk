package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.shj.setting.R;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class SalesDialog extends Dialog {
    private Button button;
    private Context context;
    private List<String> dataList;
    private TextView tv_left;
    private TextView tv_right;

    public SalesDialog(Context context, List<String> list) {
        super(context, R.style.loading_style);
        this.context = context;
        this.dataList = list;
        initView();
        setListener();
        showContent();
    }

    private void initView() {
        getWindow().setGravity(17);
        setContentView(R.layout.layout_dialog_sales);
        this.tv_left = (TextView) findViewById(R.id.tv_left);
        this.tv_right = (TextView) findViewById(R.id.tv_right);
        this.button = (Button) findViewById(R.id.button);
    }

    /* renamed from: com.shj.setting.Dialog.SalesDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SalesDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.button.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SalesDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SalesDialog.this.dismiss();
            }
        });
    }

    private void showContent() {
        String[] stringArray = this.context.getResources().getStringArray(R.array.transaction_volume);
        String str = "";
        String str2 = "";
        for (int i = 0; i < stringArray.length && i < this.dataList.size(); i++) {
            if (i % 2 == 0) {
                str = str + String.format(stringArray[i], this.dataList.get(i)) + StringUtils.LF;
            } else {
                str2 = str2 + String.format(stringArray[i], this.dataList.get(i)) + StringUtils.LF;
            }
        }
        this.tv_left.setText(str);
        this.tv_right.setText(str2);
    }
}
