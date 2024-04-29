package com.shj.setting.widget;

import android.content.Context;
import android.view.View;
import com.shj.setting.widget.ButtonInputInfoItemView;
import com.shj.setting.widget.TextItemView;

/* loaded from: classes2.dex */
public class BanknoteGiveChangeView extends AbsItemView {
    private ButtonInputInfoItemView buttonInputInfoItemView;
    private QueryItemView queryItemView;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public BanknoteGiveChangeView(Context context, TextItemView.TextItemData textItemData, ButtonInputInfoItemView.ButtonInputInfoData buttonInputInfoData) {
        super(context);
        initView(textItemData, buttonInputInfoData);
    }

    private void initView(TextItemView.TextItemData textItemData, ButtonInputInfoItemView.ButtonInputInfoData buttonInputInfoData) {
        QueryItemView queryItemView = new QueryItemView(this.context, textItemData.name, null);
        this.queryItemView = queryItemView;
        addContentView(queryItemView);
        ButtonInputInfoItemView buttonInputInfoItemView = new ButtonInputInfoItemView(this.context, buttonInputInfoData);
        this.buttonInputInfoItemView = buttonInputInfoItemView;
        addContentView(buttonInputInfoItemView);
    }

    public void setTextItemText(String str) {
        this.queryItemView.setValueText(str);
    }

    public String getInputString() {
        return this.buttonInputInfoItemView.getInputString();
    }

    public void setEditeTypeInterger() {
        this.buttonInputInfoItemView.getEditText().setInputType(2);
    }

    public void setButtonOnClickListener(View.OnClickListener onClickListener) {
        this.buttonInputInfoItemView.setButtonOnClickListener(onClickListener);
    }

    public void setQueryItemOnClickListener(View.OnClickListener onClickListener) {
        this.queryItemView.setQueryListener(onClickListener);
    }
}
