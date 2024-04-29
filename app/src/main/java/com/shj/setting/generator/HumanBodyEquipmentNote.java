package com.shj.setting.generator;

import android.content.Context;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.generator.BaseDeviceNote;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SerialDeviceData;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class HumanBodyEquipmentNote extends BaseDeviceNote {
    private List<BaseDeviceNote.ManufactorBaudRateData> ManufactorBaudRateDataList;
    private int selectManufactorIndex;
    private String serialPortAddress;

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected List<String> getAccessLocationNameList() {
        return null;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected List<String> getProtocolVersionNumberList() {
        return null;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected String getVersionNumber() {
        return null;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected boolean isHaveEnableItem() {
        return true;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected boolean isHaveSerialPortSearch() {
        return true;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected boolean isHaveWorkingTime() {
        return false;
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected void queryData() {
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected void serchSerialPortClick() {
    }

    public HumanBodyEquipmentNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
        this.selectManufactorIndex = 0;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote, com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        SerialDeviceData serialDeviceData = new SerialDeviceData();
        int manuFacturerIndex = getManuFacturerIndex();
        serialDeviceData.manufactor = this.ManufactorBaudRateDataList.get(manuFacturerIndex).manufactor;
        serialDeviceData.baudRate = this.ManufactorBaudRateDataList.get(manuFacturerIndex).baudRate;
        serialDeviceData.serialPortAddress = this.serialPortAddress;
        serialDeviceData.enable = getEnableDeviceIndex();
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
            int i = 0;
            Iterator<BaseDeviceNote.ManufactorBaudRateData> it = this.ManufactorBaudRateDataList.iterator();
            while (it.hasNext() && !it.next().manufactor.equals(serialDeviceData.manufactor)) {
                i++;
            }
            setManuFacturerIndex(i);
            setBaudRate(String.valueOf(serialDeviceData.baudRate));
            if (serialDeviceData.enable != -1) {
                setEnableDeviceIndex(serialDeviceData.enable);
            }
        }
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected boolean isShow() {
        initData();
        List<BaseDeviceNote.ManufactorBaudRateData> list = this.ManufactorBaudRateDataList;
        return list != null && list.size() > 0;
    }

    private void initData() {
        this.ManufactorBaudRateDataList = new ArrayList();
        BaseDeviceNote.ManufactorBaudRateData manufactorBaudRateData = new BaseDeviceNote.ManufactorBaudRateData();
        manufactorBaudRateData.manufactor = "厂家1";
        manufactorBaudRateData.baudRate = 115200L;
        this.ManufactorBaudRateDataList.add(manufactorBaudRateData);
        setManufacturerSelectedListener();
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected List<String> getManufacturerList() {
        ArrayList arrayList = new ArrayList();
        Iterator<BaseDeviceNote.ManufactorBaudRateData> it = this.ManufactorBaudRateDataList.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().manufactor);
        }
        return arrayList;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected String getBaudRate() {
        return String.valueOf(this.ManufactorBaudRateDataList.get(this.selectManufactorIndex).baudRate);
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected String getTestButtonName() {
        return this.context.getResources().getString(R.string.induction_test);
    }
}
