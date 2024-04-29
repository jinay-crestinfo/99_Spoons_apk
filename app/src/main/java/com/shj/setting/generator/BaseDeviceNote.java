package com.shj.setting.generator;

import android.content.Context;
import android.serialport.SerialPortFinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import com.oysb.utils.Loger;
import com.shj.setting.R;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.ButtonInfoItemView;
import com.shj.setting.widget.DeviceItemView;
import com.shj.setting.widget.SpinnerItemView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BaseDeviceNote extends SettingNote {
    protected DeviceItemView deviceItemView;
    private ButtonInfoItemView serialPortAddressView;

    protected abstract List<String> getAccessLocationNameList();

    protected abstract String getBaudRate();

    protected abstract List<String> getManufacturerList();

    protected abstract List<String> getProtocolVersionNumberList();

    protected abstract String getTestButtonName();

    protected abstract String getVersionNumber();

    protected abstract boolean isHaveEnableItem();

    protected abstract boolean isHaveSerialPortSearch();

    protected abstract boolean isHaveWorkingTime();

    protected abstract boolean isShow();

    public void manufacturerSelected(int i) {
    }

    protected abstract void queryData();

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
    }

    protected abstract void serchSerialPortClick();

    public void testButtonClick() {
    }

    public BaseDeviceNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        if (!isShow()) {
            return null;
        }
        DeviceItemView deviceItemView = new DeviceItemView(this.context);
        this.deviceItemView = deviceItemView;
        deviceItemView.setTitle(getSettingName());
        this.deviceItemView.setTitleVisibility(0);
        this.deviceItemView.setEventListener(this.eventListener);
        addAccessLocationView();
        addProtocolVersionNumber();
        selectionManufacturer();
        addBaudRate();
        addTestButton();
        addSelectSerialPortAddress();
        addWorkingTime();
        addEnableDevice();
        addVersionNumber();
        queryData();
        setListener();
        setManufacturerSelectedListener();
        return this.deviceItemView;
    }

    /* renamed from: com.shj.setting.generator.BaseDeviceNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements ButtonInfoItemView.ClickEventListener {
        @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
        public void TextClick(View view) {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
        public void buttonClick(View view) {
            BaseDeviceNote.this.serchSerialPortClick();
        }
    }

    private void setListener() {
        ButtonInfoItemView buttonInfoItemView = this.serialPortAddressView;
        if (buttonInfoItemView != null) {
            buttonInfoItemView.setClickEventListener(new ButtonInfoItemView.ClickEventListener() { // from class: com.shj.setting.generator.BaseDeviceNote.1
                @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
                public void TextClick(View view) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
                public void buttonClick(View view) {
                    BaseDeviceNote.this.serchSerialPortClick();
                }
            });
        }
        this.deviceItemView.setTestButtonListener(new View.OnClickListener() { // from class: com.shj.setting.generator.BaseDeviceNote.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
                BaseDeviceNote.this.testButtonClick();
            }
        });
    }

    /* renamed from: com.shj.setting.generator.BaseDeviceNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
            BaseDeviceNote.this.testButtonClick();
        }
    }

    public void setTestButtonName(String str) {
        this.deviceItemView.setTestButtonName(str);
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

    private void addAccessLocationView() {
        List<String> accessLocationNameList = getAccessLocationNameList();
        if (accessLocationNameList != null) {
            DeviceItemView.RadioItemData radioItemData = new DeviceItemView.RadioItemData();
            radioItemData.name = this.context.getResources().getString(R.string.access_location);
            radioItemData.dataList = accessLocationNameList;
            this.deviceItemView.addAccessLocationView(radioItemData);
        }
    }

    public int getAccessPosition() {
        return this.deviceItemView.getAccessPosition();
    }

    public void setAccessPosition(int i) {
        this.deviceItemView.setAccessPosition(i);
    }

    private void selectionManufacturer() {
        List<String> manufacturerList = getManufacturerList();
        if (manufacturerList != null) {
            SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
            spinnerItemData.name = this.context.getResources().getString(R.string.selection_manufacturer);
            spinnerItemData.dataList = manufacturerList;
            this.deviceItemView.selectionManufacturer(spinnerItemData);
        }
    }

    /* renamed from: com.shj.setting.generator.BaseDeviceNote$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements AdapterView.OnItemSelectedListener {
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        AnonymousClass3() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            BaseDeviceNote.this.manufacturerSelected(i);
        }
    }

    public void setManufacturerSelectedListener() {
        DeviceItemView deviceItemView = this.deviceItemView;
        if (deviceItemView != null) {
            deviceItemView.setManufacturerSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.shj.setting.generator.BaseDeviceNote.3
                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

                AnonymousClass3() {
                }

                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                    BaseDeviceNote.this.manufacturerSelected(i);
                }
            });
        }
    }

    public int getManuFacturerIndex() {
        return this.deviceItemView.getManuFacturerIndex();
    }

    public void setManuFacturerIndex(int i) {
        this.deviceItemView.setManuFacturerIndex(i);
    }

    private void addBaudRate() {
        String baudRate = getBaudRate();
        if (TextUtils.isEmpty(baudRate)) {
            return;
        }
        this.deviceItemView.addBaudRate(baudRate);
    }

    public void setBaudRate(String str) {
        this.deviceItemView.setBaudRate(str);
    }

    private void addTestButton() {
        String testButtonName = getTestButtonName();
        if (TextUtils.isEmpty(testButtonName)) {
            return;
        }
        this.deviceItemView.addTestButton(testButtonName);
    }

    private void addSelectSerialPortAddress() {
        if (isHaveSerialPortSearch()) {
            ButtonInfoItemView buttonInfoItemView = new ButtonInfoItemView(this.context, this.context.getResources().getString(R.string.seach_port_address), "");
            this.serialPortAddressView = buttonInfoItemView;
            this.deviceItemView.addContentView(buttonInfoItemView);
        }
    }

    public void setSerialPortAddress(String str) {
        ButtonInfoItemView buttonInfoItemView = this.serialPortAddressView;
        if (buttonInfoItemView == null || str == null) {
            return;
        }
        buttonInfoItemView.setTextString(str);
    }

    private void addEnableDevice() {
        if (isHaveEnableItem()) {
            DeviceItemView.RadioItemData radioItemData = new DeviceItemView.RadioItemData();
            radioItemData.name = this.context.getResources().getString(R.string.is_it_enabled);
            radioItemData.dataList = new ArrayList();
            radioItemData.dataList.add(this.context.getResources().getString(R.string.lab_enable));
            radioItemData.dataList.add(this.context.getResources().getString(R.string.lab_disable));
            this.deviceItemView.addEnableDevice(radioItemData);
        }
    }

    public int getEnableDeviceIndex() {
        return this.deviceItemView.getEnableDeviceIndex();
    }

    public void setEnableDeviceIndex(int i) {
        this.deviceItemView.setEnableDeviceIndex(i);
    }

    private void addProtocolVersionNumber() {
        List<String> protocolVersionNumberList = getProtocolVersionNumberList();
        if (protocolVersionNumberList != null) {
            DeviceItemView.RadioItemData radioItemData = new DeviceItemView.RadioItemData();
            radioItemData.name = this.context.getResources().getString(R.string.protocol_version_number);
            radioItemData.dataList = protocolVersionNumberList;
            this.deviceItemView.addProtocolVersionNumber(radioItemData);
        }
    }

    private void addVersionNumber() {
        String versionNumber = getVersionNumber();
        if (TextUtils.isEmpty(versionNumber)) {
            return;
        }
        this.deviceItemView.addVersionNumber(versionNumber);
    }

    private void addWorkingTime() {
        if (isHaveWorkingTime()) {
            this.deviceItemView.addWorkingTime();
        }
    }

    protected void setWorkingTime(String str) {
        if (isHaveWorkingTime()) {
            this.deviceItemView.setWorkingTime(str);
        }
    }

    protected String getWokingTime() {
        if (isHaveWorkingTime()) {
            return this.deviceItemView.getWorkingTime();
        }
        return null;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.deviceItemView;
    }

    /* loaded from: classes2.dex */
    public class ManufactorBaudRateData {
        public long baudRate;
        public String manufactor;

        public ManufactorBaudRateData() {
        }
    }
}
