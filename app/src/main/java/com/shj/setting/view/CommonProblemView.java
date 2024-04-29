package com.shj.setting.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.shj.setting.R;
import com.shj.setting.helper.ShjHelper;

/* loaded from: classes2.dex */
public class CommonProblemView extends LinearLayout {
    private Context context;

    public CommonProblemView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        ((Button) LayoutInflater.from(this.context).inflate(R.layout.layout_common_problem, this).findViewById(R.id.bt_problem)).setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.view.CommonProblemView.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ShjHelper.showHelpDialog(CommonProblemView.this.context);
            }
        });
    }

    /* renamed from: com.shj.setting.view.CommonProblemView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ShjHelper.showHelpDialog(CommonProblemView.this.context);
        }
    }
}
