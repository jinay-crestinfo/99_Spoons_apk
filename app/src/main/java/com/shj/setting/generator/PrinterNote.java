package com.shj.setting.generator;

import android.content.Context;
import android.text.TextUtils;
import com.oysb.utils.device.MsPrintor;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.Shj;
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
public class PrinterNote extends BaseDeviceNote {
    private List<BaseDeviceNote.ManufactorBaudRateData> ManufactorBaudRateDataList;
    private int selectManufactorIndex;
    private String serialPortAddress;

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

    public PrinterNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
        this.selectManufactorIndex = 0;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote, com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        SerialDeviceData serialDeviceData = new SerialDeviceData();
        serialDeviceData.accessPosition = getAccessPosition();
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
            if (serialDeviceData.accessPosition != -1) {
                setAccessPosition(serialDeviceData.accessPosition);
            }
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
            if (serialDeviceData.serialPortAddress != null) {
                setSerialPortAddress(serialDeviceData.serialPortAddress);
                return;
            }
            return;
        }
        setEnableDeviceIndex(1);
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
        manufactorBaudRateData.manufactor = "美松";
        manufactorBaudRateData.baudRate = 9600L;
        this.ManufactorBaudRateDataList.add(manufactorBaudRateData);
        setManufacturerSelectedListener();
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    public void manufacturerSelected(int i) {
        this.selectManufactorIndex = i;
        setBaudRate(String.valueOf(this.ManufactorBaudRateDataList.get(i).baudRate));
    }

    /* renamed from: com.shj.setting.generator.PrinterNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements MsPrintor.SerialPortSearchListener {
        AnonymousClass1() {
        }

        @Override // com.oysb.utils.device.MsPrintor.SerialPortSearchListener
        public void getAddress(String str) {
            PrinterNote.this.serialPortAddress = str;
            PrinterNote.this.setSerialPortAddress(str);
            MsPrintor.setSerialPortSearchListener(null);
        }

        @Override // com.oysb.utils.device.MsPrintor.SerialPortSearchListener
        public void notFound() {
            ToastUitl.showShort(PrinterNote.this.context, R.string.not_found_device);
            MsPrintor.setSerialPortSearchListener(null);
        }
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected void serchSerialPortClick() {
        ArrayList arrayList;
        MsPrintor.setSerialPortSearchListener(new MsPrintor.SerialPortSearchListener() { // from class: com.shj.setting.generator.PrinterNote.1
            AnonymousClass1() {
            }

            @Override // com.oysb.utils.device.MsPrintor.SerialPortSearchListener
            public void getAddress(String str) {
                PrinterNote.this.serialPortAddress = str;
                PrinterNote.this.setSerialPortAddress(str);
                MsPrintor.setSerialPortSearchListener(null);
            }

            @Override // com.oysb.utils.device.MsPrintor.SerialPortSearchListener
            public void notFound() {
                ToastUitl.showShort(PrinterNote.this.context, R.string.not_found_device);
                MsPrintor.setSerialPortSearchListener(null);
            }
        });
        String comPath = Shj.getComPath();
        if (TextUtils.isEmpty(comPath)) {
            arrayList = null;
        } else {
            arrayList = new ArrayList();
            arrayList.add(comPath);
            String linkPath = SDFileUtils.getLinkPath(comPath);
            if (linkPath != null) {
                arrayList.add(linkPath);
            }
        }
        MsPrintor.findPrinter(arrayList);
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected List<String> getAccessLocationNameList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.context.getResources().getString(R.string.singlechip));
        arrayList.add(this.context.getResources().getString(R.string.f11android));
        return arrayList;
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
        return this.context.getResources().getString(R.string.print_test);
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    public void testButtonClick() {
        ArrayList arrayList;
        String comPath = Shj.getComPath();
        if (TextUtils.isEmpty(comPath)) {
            arrayList = null;
        } else {
            arrayList = new ArrayList();
            arrayList.add(comPath);
            String linkPath = SDFileUtils.getLinkPath(comPath);
            if (linkPath != null) {
                arrayList.add(linkPath);
            }
        }
        if (!MsPrintor.findPrinter(arrayList)) {
            ToastUitl.showShort(this.context, R.string.not_found_device_print);
            return;
        }
        MsPrintor.clean();
        MsPrintor.printString("无锡兰桂医疗\n", true, 0, true, 2, 0, 2);
        MsPrintor.printString("如需发票请凭此小票到店换取\n", true, 0, false, 2, 0, 2);
        MsPrintor.printString("------------------------\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("药品名称:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("生产厂商:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("单价:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("购买数量:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("生产批号:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("药品规格:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("生产日期:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("保质日期:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("------------------------\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("订单单号:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("支付总额:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("购买时间:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("购买地点:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("支付方式:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("------------------------\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("咨询电话:创口贴\n", true, 0, false, 2, 0, 0);
        MsPrintor.printString("药品属于特殊商品，一经出售，如无质量问题，概不退换\n", true, 0, false, 2, 0, 0);
        MsPrintor.cutpaper(12);
        MsPrintor.close();
    }
}
