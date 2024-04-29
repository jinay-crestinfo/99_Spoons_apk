package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class ButtonInfoItemView extends AbsItemView {
    private Button button;
    private ClickEventListener clickEventListener;
    private String initText;
    private String name;
    private TextView tv_info;

    /* loaded from: classes2.dex */
    public interface ClickEventListener {
        void TextClick(View view);

        void buttonClick(View view);
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public ButtonInfoItemView(Context context, String str, String str2) {
        super(context);
        this.name = str;
        this.initText = str2;
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_button_info_item, (ViewGroup) null);
        this.button = (Button) inflate.findViewById(R.id.button);
        this.tv_info = (TextView) inflate.findViewById(R.id.tv_info);
        this.button.setText(this.name);
        this.tv_info.setText(this.initText);
        addContentView(inflate);
        setListener();
    }

    /* renamed from: com.shj.setting.widget.ButtonInfoItemView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ButtonInfoItemView.this.clickEventListener != null) {
                ButtonInfoItemView.this.clickEventListener.buttonClick(view);
            }
        }
    }

    private void setListener() {
        this.button.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.ButtonInfoItemView.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ButtonInfoItemView.this.clickEventListener != null) {
                    ButtonInfoItemView.this.clickEventListener.buttonClick(view);
                }
            }
        });
        this.tv_info.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.ButtonInfoItemView.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ButtonInfoItemView.this.clickEventListener != null) {
                    ButtonInfoItemView.this.clickEventListener.TextClick(view);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.widget.ButtonInfoItemView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ButtonInfoItemView.this.clickEventListener != null) {
                ButtonInfoItemView.this.clickEventListener.TextClick(view);
            }
        }
    }

    public void setTextString(String str) {
        this.tv_info.setText(str);
    }

    public String getTextString() {
        return this.tv_info.getText().toString();
    }

    public void setClickEventListener(ClickEventListener clickEventListener) {
        this.clickEventListener = clickEventListener;
    }
}
