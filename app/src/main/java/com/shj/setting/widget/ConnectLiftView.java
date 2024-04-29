package com.shj.setting.widget;

import android.content.Context;
import android.view.View;
import com.shj.setting.widget.RadioGroupItemView;
import com.shj.setting.widget.SpinnerItemView;

/* loaded from: classes2.dex */
public class ConnectLiftView extends AbsItemView {
    private RadioGroupItemView liftRadioGroupItemView;
    private RadioGroupItemView protectRadioGroupItemView;
    private SpinnerItemView spinnerItemView;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public ConnectLiftView(Context context, SpinnerItemView.SpinnerItemData spinnerItemData, RadioGroupItemView.RadioGroupData radioGroupData, RadioGroupItemView.RadioGroupData radioGroupData2) {
        super(context);
        this.context = context;
        initView(spinnerItemData, radioGroupData, radioGroupData2);
    }

    private void initView(SpinnerItemView.SpinnerItemData spinnerItemData, RadioGroupItemView.RadioGroupData radioGroupData, RadioGroupItemView.RadioGroupData radioGroupData2) {
        SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, spinnerItemData);
        this.spinnerItemView = spinnerItemView;
        addContentView(spinnerItemView);
        RadioGroupItemView radioGroupItemView = new RadioGroupItemView(this.context, radioGroupData);
        this.protectRadioGroupItemView = radioGroupItemView;
        addContentView(radioGroupItemView);
        RadioGroupItemView radioGroupItemView2 = new RadioGroupItemView(this.context, radioGroupData2);
        this.liftRadioGroupItemView = radioGroupItemView2;
        addContentView(radioGroupItemView2);
    }

    public int getCabinet() {
        return this.spinnerItemView.getSelectIndex();
    }

    public void setLift(int i) {
        this.liftRadioGroupItemView.setRadioButtonCheck(i);
    }

    public int getLift() {
        return this.liftRadioGroupItemView.getRadioButtonCheckIndex();
    }

    public void setProtect(int i) {
        this.protectRadioGroupItemView.setRadioButtonCheck(i);
    }

    public int getProtect() {
        return this.protectRadioGroupItemView.getRadioButtonCheckIndex();
    }
}
