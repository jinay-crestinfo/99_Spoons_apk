package com.shj.setting.generator;

import android.content.Context;
import android.serialport.SerialPortFinder;
import android.text.TextUtils;
import android.view.View;
import com.shj.Shj;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.MainControlEquipmentView;
import com.shj.setting.widget.RadioGroupItemView;
import com.shj.setting.widget.SpinnerItemView;
import com.shj.setting.widget.TextItemView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SerialDeviceData;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class MainControlEquipmentNote extends SettingNote {
    private List<String> baudRateList;
    private MainControlEquipmentView mainControlEquipmentView;
    private List<String> serialPortAddressList;

    private List<String> getBaudRateList() {
        return null;
    }

    private void setBaudRate(String str) {
    }

    protected String getBaudRate() {
        return "57600";
    }

    public MainControlEquipmentNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        SerialDeviceData serialDeviceData = new SerialDeviceData();
        serialDeviceData.protocolVersionNumber = this.mainControlEquipmentView.getProtocolVersionIndex() + 1;
        serialDeviceData.baudRate = 57600L;
        serialDeviceData.serialPortAddress = this.serialPortAddressList.get(this.mainControlEquipmentView.getSelect(0));
        AppSetting.saveAppSetting(this.context, this.settingType, serialDeviceData.toJson(), (UserSettingDao) null);
        if (z) {
            return;
        }
        ToastUitl.showSaveSuccessTip(this.context);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        String value = AppSetting.getValue(this.context, this.settingType, null);
        if (value != null) {
            SerialDeviceData serialDeviceData = new SerialDeviceData();
            serialDeviceData.formJson(value);
            if (serialDeviceData.protocolVersionNumber != -1) {
                this.mainControlEquipmentView.setProtocolVersionIndex(serialDeviceData.protocolVersionNumber - 1);
            }
            setPortAddress(serialDeviceData.serialPortAddress);
            return;
        }
        setPortAddress("/dev/ttyS1");
    }

    private void setPortAddress(String str) {
        Iterator<String> it = this.serialPortAddressList.iterator();
        int i = 0;
        while (it.hasNext() && !it.next().equals(str)) {
            i++;
        }
        this.mainControlEquipmentView.setSelect(0, i);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        super.querySettingData();
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        RadioGroupItemView.RadioGroupData radioGroupData = new RadioGroupItemView.RadioGroupData();
        radioGroupData.title = this.context.getString(R.string.protocol_version_number);
        radioGroupData.nameList = new ArrayList();
        radioGroupData.nameList.add("1.0");
        radioGroupData.nameList.add("2.0");
        ArrayList arrayList = new ArrayList();
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        spinnerItemData.name = this.context.getString(R.string.serial_port_address);
        List<String> serialPortAddressList = getSerialPortAddressList();
        this.serialPortAddressList = serialPortAddressList;
        spinnerItemData.dataList = serialPortAddressList;
        arrayList.add(spinnerItemData);
        TextItemView.TextItemData textItemData = new TextItemView.TextItemData();
        textItemData.name = this.context.getString(R.string.version_number);
        String machineBoardVersion = Shj.getMachineBoardVersion();
        if (!TextUtils.isEmpty(machineBoardVersion)) {
            textItemData.value = machineBoardVersion;
        } else {
            textItemData.value = "0203";
        }
        MainControlEquipmentView mainControlEquipmentView = new MainControlEquipmentView(this.context, radioGroupData, arrayList, textItemData);
        this.mainControlEquipmentView = mainControlEquipmentView;
        mainControlEquipmentView.setTitle(getSettingName());
        this.mainControlEquipmentView.setTitleVisibility(0);
        this.mainControlEquipmentView.setEventListener(this.eventListener);
        return this.mainControlEquipmentView;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.mainControlEquipmentView;
    }

    private List<String> getSerialPortAddressList() {
        String[] allDevicesPath = new SerialPortFinder().getAllDevicesPath();
        if (allDevicesPath.length == 0) {
            allDevicesPath = new String[]{this.context.getString(R.string.no_serial_device)};
        }
        ArrayList arrayList = new ArrayList();
        for (String str : allDevicesPath) {
            arrayList.add(str);
        }
        return arrayList;
    }
}
