package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.oysb.utils.CommonTool;
import com.shj.setting.R;
import com.shj.setting.widget.MultipleEditItemView;
import com.shj.setting.widget.SpinnerItemView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class DeviceItemView extends AbsItemView {
    public static final String BAUD_RATE = "baud_rate";
    public static final String SELECT_BAUD_RATE = "select_baud_rate";
    public static final String SELECT_SERIAL_PORT_ADDRESS = "select_serial_port_address";
    public static final String TYPE_ACCESS_OCATION = "access_location";
    public static final String TYPE_ENABLE_DEVICE = "enable_device";
    public static final String TYPE_PROTOCOL_VERSION_NUMBER = "protocol_version_number";
    public static final String TYPE_SELECTION_MANUFACTURER = "selection_manufacturer";
    public static final String VERSION_NUMBER = "version_number";
    private HashMap<String, TextView> baudRateMap;
    private HashMap<String, List<RadioButton>> radioMap;
    private HashMap<String, SpinnerItemView> spinnerMap;
    private Button testButton;
    private EditeItemView workingTimeItemView;

    /* loaded from: classes2.dex */
    public static class RadioItemData {
        public List<String> dataList;
        public String name;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public DeviceItemView(Context context) {
        super(context);
        this.radioMap = new HashMap<>();
        this.spinnerMap = new HashMap<>();
        this.baudRateMap = new HashMap<>();
    }

    public void addAccessLocationView(RadioItemData radioItemData) {
        ArrayList arrayList = new ArrayList();
        addRadioGroupView(radioItemData.name, radioItemData.dataList, arrayList);
        this.radioMap.put(TYPE_ACCESS_OCATION, arrayList);
    }

    public int getAccessPosition() {
        List<RadioButton> list = this.radioMap.get(TYPE_ACCESS_OCATION);
        if (list == null || list.size() != 2) {
            return -1;
        }
        return list.get(0).isChecked() ? 0 : 1;
    }

    public void setAccessPosition(int i) {
        List<RadioButton> list = this.radioMap.get(TYPE_ACCESS_OCATION);
        if (list == null || i >= list.size()) {
            return;
        }
        list.get(i).setChecked(true);
    }

    public void selectionManufacturer(SpinnerItemView.SpinnerItemData spinnerItemData) {
        SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, spinnerItemData);
        addContentView(spinnerItemView);
        this.spinnerMap.put(TYPE_SELECTION_MANUFACTURER, spinnerItemView);
    }

    public void setManufacturerSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        SpinnerItemView spinnerItemView = this.spinnerMap.get(TYPE_SELECTION_MANUFACTURER);
        if (spinnerItemView != null) {
            spinnerItemView.setOnItemSelectedListener(onItemSelectedListener);
        }
    }

    public int getManuFacturerIndex() {
        SpinnerItemView spinnerItemView = this.spinnerMap.get(TYPE_SELECTION_MANUFACTURER);
        if (spinnerItemView != null) {
            return spinnerItemView.getSelectIndex();
        }
        return 0;
    }

    public void setManuFacturerIndex(int i) {
        SpinnerItemView spinnerItemView = this.spinnerMap.get(TYPE_SELECTION_MANUFACTURER);
        if (spinnerItemView != null) {
            spinnerItemView.setSelectIndex(i);
        }
    }

    public void addBaudRate(String str) {
        addTextView(this.context.getResources().getString(R.string.baud_rate), str, BAUD_RATE);
    }

    public void setBaudRate(String str) {
        this.baudRateMap.get(BAUD_RATE).setText(str);
    }

    public void addSelectBaudRate(SpinnerItemView.SpinnerItemData spinnerItemData) {
        SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, spinnerItemData);
        addContentView(spinnerItemView);
        this.spinnerMap.put(SELECT_BAUD_RATE, spinnerItemView);
    }

    public void addTestButton(String str) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_click_exec_item, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_instruction);
        this.testButton = (Button) inflate.findViewById(R.id.button);
        textView.setVisibility(8);
        this.testButton.setText(str);
        addContentView(inflate);
    }

    public void setTestButtonListener(View.OnClickListener onClickListener) {
        Button button = this.testButton;
        if (button != null) {
            button.setOnClickListener(onClickListener);
        }
    }

    public void setTestButtonName(String str) {
        this.testButton.setText(str);
    }

    public void addSelectSerialPortAddress(SpinnerItemView.SpinnerItemData spinnerItemData) {
        SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, spinnerItemData);
        addContentView(spinnerItemView);
        this.spinnerMap.put(SELECT_SERIAL_PORT_ADDRESS, spinnerItemView);
    }

    public void addEnableDevice(RadioItemData radioItemData) {
        ArrayList arrayList = new ArrayList();
        addRadioGroupView(radioItemData.name, radioItemData.dataList, arrayList);
        this.radioMap.put(TYPE_ENABLE_DEVICE, arrayList);
    }

    public int getEnableDeviceIndex() {
        List<RadioButton> list = this.radioMap.get(TYPE_ENABLE_DEVICE);
        if (list == null || list.size() != 2) {
            return -1;
        }
        return list.get(0).isChecked() ? 0 : 1;
    }

    public void setEnableDeviceIndex(int i) {
        List<RadioButton> list = this.radioMap.get(TYPE_ENABLE_DEVICE);
        if (list == null || i >= list.size()) {
            return;
        }
        list.get(i).setChecked(true);
    }

    public void addProtocolVersionNumber(RadioItemData radioItemData) {
        ArrayList arrayList = new ArrayList();
        addRadioGroupView(radioItemData.name, radioItemData.dataList, arrayList);
        this.radioMap.put(TYPE_PROTOCOL_VERSION_NUMBER, arrayList);
    }

    public void addVersionNumber(String str) {
        addTextView(this.context.getResources().getString(R.string.version_number), str, VERSION_NUMBER);
    }

    public void addWorkingTime() {
        MultipleEditItemView.EditTextDataInfo editTextDataInfo = new MultipleEditItemView.EditTextDataInfo();
        editTextDataInfo.name = String.format(this.context.getResources().getString(R.string.working_time), "(h)");
        if (CommonTool.getLanguage(this.context).equals("en") || CommonTool.getLanguage(this.context).equals("fr")) {
            editTextDataInfo.tipInfo = this.context.getResources().getString(R.string.please_input) + StringUtils.SPACE + String.format(this.context.getResources().getString(R.string.working_time), "");
        } else {
            editTextDataInfo.tipInfo = this.context.getResources().getString(R.string.please_input) + String.format(this.context.getResources().getString(R.string.working_time), "");
        }
        EditeItemView editeItemView = new EditeItemView(this.context, editTextDataInfo);
        this.workingTimeItemView = editeItemView;
        addContentView(editeItemView);
    }

    public void setWorkingTime(String str) {
        EditeItemView editeItemView = this.workingTimeItemView;
        if (editeItemView != null) {
            editeItemView.setEditText(str);
        }
    }

    public String getWorkingTime() {
        EditeItemView editeItemView = this.workingTimeItemView;
        if (editeItemView != null) {
            return editeItemView.getEditText().getText().toString();
        }
        return null;
    }

    private void addTextView(String str, String str2, String str3) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_text_item, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_name);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_value);
        textView.setText(str);
        textView2.setText(str2);
        this.baudRateMap.put(str3, textView2);
        addContentView(inflate);
    }

    private void addRadioGroupView(String str, List<String> list, List<RadioButton> list2) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_radio_group, (ViewGroup) null);
        ((TextView) inflate.findViewById(R.id.tv_name)).setText(str);
        RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.radio_group);
        addContentView(inflate);
        int childCount = radioGroup.getChildCount();
        Iterator<String> it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            addRadioButton(i, childCount, it.next(), radioGroup, list2);
            i++;
        }
    }

    private void addRadioButton(int i, int i2, String str, RadioGroup radioGroup, List<RadioButton> list) {
        if (i < i2) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setVisibility(0);
            radioButton.setText(str);
            list.add(radioButton);
        }
    }

    public int getAccessLocationCheckIndex() {
        List<RadioButton> list = this.radioMap.get(TYPE_ACCESS_OCATION);
        if (list == null) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                return i;
            }
        }
        return -1;
    }

    public void setAccessLocationCheck(int i) {
        List<RadioButton> list = this.radioMap.get(TYPE_ACCESS_OCATION);
        if (list == null || i >= list.size()) {
            return;
        }
        list.get(i).setChecked(true);
    }
}
