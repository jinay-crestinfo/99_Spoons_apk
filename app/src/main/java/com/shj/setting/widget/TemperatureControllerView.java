package com.shj.setting.widget;

import android.content.Context;
import android.view.View;
import com.shj.setting.widget.MultipleEditItemView;
import com.shj.setting.widget.SpinnerItemView;
import com.shj.setting.widget.TextItemView;

/* loaded from: classes2.dex */
public class TemperatureControllerView extends AbsItemView {
    private static final int LINE_COUNT = 3;
    private SpinnerItemView cabinetItemView;
    private EditeItemView controlTemperatureItemView;
    private TemperatureControllerData temperatureControllerData;
    private TextItemView temperatureControllerStateitem;
    private SpinnerItemView workingModeItemView;

    /* loaded from: classes2.dex */
    public static class TemperatureControllerData {
        public SpinnerItemView.SpinnerItemData cabinetNumberData;
        public TextItemView.TextItemData stateData;
        public MultipleEditItemView.EditTextDataInfo temperatureData;
        public SpinnerItemView.SpinnerItemData workingModeData;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public TemperatureControllerView(Context context, TemperatureControllerData temperatureControllerData) {
        super(context);
        this.temperatureControllerData = temperatureControllerData;
        initView();
        this.controlTemperatureItemView.getEditText().setInputType(4098);
    }

    private void initView() {
        addCabinetNumberView(this.temperatureControllerData.cabinetNumberData);
        addWorkingModeView(this.temperatureControllerData.workingModeData);
        addControlTemperature(this.temperatureControllerData.temperatureData);
        addTemperatureControllerStatus(this.temperatureControllerData.stateData);
    }

    private void addCabinetNumberView(SpinnerItemView.SpinnerItemData spinnerItemData) {
        SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, spinnerItemData);
        this.cabinetItemView = spinnerItemView;
        addContentView(spinnerItemView);
    }

    private void addWorkingModeView(SpinnerItemView.SpinnerItemData spinnerItemData) {
        SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, spinnerItemData);
        this.workingModeItemView = spinnerItemView;
        addContentView(spinnerItemView);
    }

    private void addControlTemperature(MultipleEditItemView.EditTextDataInfo editTextDataInfo) {
        EditeItemView editeItemView = new EditeItemView(this.context, editTextDataInfo);
        this.controlTemperatureItemView = editeItemView;
        addContentView(editeItemView);
    }

    private void addTemperatureControllerStatus(TextItemView.TextItemData textItemData) {
        TextItemView textItemView = new TextItemView(this.context, textItemData);
        this.temperatureControllerStateitem = textItemView;
        addContentView(textItemView);
    }

    public int getSelectCabinet() {
        return this.cabinetItemView.getSelectIndex();
    }

    public void setTemperatureControllerState(String str) {
        this.temperatureControllerStateitem.setTextValue(str);
    }

    public void setWorkingMode(int i) {
        this.workingModeItemView.setSelectIndex(i);
    }

    public int getWordingMode() {
        return this.workingModeItemView.getSelectIndex();
    }

    public void setControlTemperature(String str) {
        this.controlTemperatureItemView.setEditText(str);
    }

    public String getControlTemperature() {
        return this.controlTemperatureItemView.getEditText().getText().toString();
    }
}
