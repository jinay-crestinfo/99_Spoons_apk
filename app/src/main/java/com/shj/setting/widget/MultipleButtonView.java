package com.shj.setting.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.shj.setting.R;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class MultipleButtonView extends AbsItemView {
    private ButtonClickListenr buttonClickListenr;
    protected Context context;
    private LinearLayout linearLayout;
    private List<Integer> shelfList;

    /* loaded from: classes2.dex */
    public interface ButtonClickListenr {
        void onClick(int i);
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public MultipleButtonView(Context context, List<Integer> list) {
        super(context);
        this.context = context;
        this.shelfList = list;
        Collections.sort(list);
        initView();
    }

    public void initView() {
        this.linearLayout = new LinearLayout(this.context);
        this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.linearLayout.setOrientation(0);
        addContentView(this.linearLayout);
        addButton();
    }

    private void addButton() {
        int dimensionPixelOffset = this.context.getResources().getDimensionPixelOffset(R.dimen.x100);
        int dimensionPixelOffset2 = this.context.getResources().getDimensionPixelOffset(R.dimen.y55);
        int color = this.context.getResources().getColor(R.color.setting_white);
        int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(R.dimen.text_small);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.topMargin = 10;
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.leftMargin = 10;
        layoutParams2.bottomMargin = 10;
        int size = this.shelfList.size();
        int size2 = size % 10 != 0 ? (this.shelfList.size() / ((size / 10) + 1)) + 1 : 10;
        LinearLayout linearLayout = null;
        for (int i = 0; i < size; i++) {
            if (i % size2 == 0) {
                linearLayout = new LinearLayout(this.context);
                this.linearLayout.addView(linearLayout, layoutParams);
            }
            Button button = new Button(this.context);
            button.setText(String.format("%03d", this.shelfList.get(i)));
            button.setTag(this.shelfList.get(i));
            button.setWidth(dimensionPixelOffset);
            button.setHeight(dimensionPixelOffset2);
            button.setBackgroundResource(R.drawable.selector_button_blue);
            button.setTextColor(color);
            button.setTextSize(dimensionPixelSize);
            button.setGravity(17);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.MultipleButtonView.1
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int intValue = ((Integer) view.getTag()).intValue();
                    if (MultipleButtonView.this.buttonClickListenr != null) {
                        MultipleButtonView.this.buttonClickListenr.onClick(intValue);
                    }
                }
            });
            linearLayout.addView(button, layoutParams2);
        }
    }

    /* renamed from: com.shj.setting.widget.MultipleButtonView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int intValue = ((Integer) view.getTag()).intValue();
            if (MultipleButtonView.this.buttonClickListenr != null) {
                MultipleButtonView.this.buttonClickListenr.onClick(intValue);
            }
        }
    }

    public void setButtonClickListenr(ButtonClickListenr buttonClickListenr) {
        this.buttonClickListenr = buttonClickListenr;
    }
}
