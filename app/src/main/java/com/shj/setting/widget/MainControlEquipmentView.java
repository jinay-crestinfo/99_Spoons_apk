package com.shj.setting.widget;

import android.content.Context;
import android.view.View;
import com.shj.setting.widget.RadioGroupItemView;
import com.shj.setting.widget.SpinnerItemView;
import com.shj.setting.widget.TextItemView;
import java.util.List;

/* loaded from: classes2.dex */
public class MainControlEquipmentView extends AbsItemView {
    private TextItemView VersionNumberItem;
    private MultipleSpinnerItemView multipleSpinnerItemView;
    private RadioGroupItemView protocolVersionItem;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public MainControlEquipmentView(Context context, RadioGroupItemView.RadioGroupData radioGroupData, List<SpinnerItemView.SpinnerItemData> list, TextItemView.TextItemData textItemData) {
        super(context);
        initView(radioGroupData, list, textItemData);
    }

    public void initView(RadioGroupItemView.RadioGroupData radioGroupData, List<SpinnerItemView.SpinnerItemData> list, TextItemView.TextItemData textItemData) {
        RadioGroupItemView radioGroupItemView = new RadioGroupItemView(this.context, radioGroupData);
        this.protocolVersionItem = radioGroupItemView;
        addContentView(radioGroupItemView);
        MultipleSpinnerItemView multipleSpinnerItemView = new MultipleSpinnerItemView(this.context, list);
        this.multipleSpinnerItemView = multipleSpinnerItemView;
        addContentView(multipleSpinnerItemView);
        TextItemView textItemView = new TextItemView(this.context, textItemData);
        this.VersionNumberItem = textItemView;
        addContentView(textItemView);
    }

    public void setProtocolVersionIndex(int i) {
        this.protocolVersionItem.setRadioButtonCheck(i);
    }

    public int getProtocolVersionIndex() {
        return this.protocolVersionItem.getRadioButtonCheckIndex();
    }

    public void setSelect(int i, int i2) {
        this.multipleSpinnerItemView.setSelect(i, i2);
    }

    public int getSelect(int i) {
        return this.multipleSpinnerItemView.getSelect(i);
    }
}
