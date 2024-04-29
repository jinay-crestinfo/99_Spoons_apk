package com.shj.setting.generator;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import com.oysb.utils.Loger;
import com.oysb.utils.cache.CacheHelper;
import com.shj.device.scanor.Scanor;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.MutilTextTipDialog;
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
public class ScavengingWharfNote extends BaseDeviceNote {
    private List<BaseDeviceNote.ManufactorBaudRateData> ManufactorBaudRateDataList;
    private long clickTime;
    private Handler handler;
    private boolean isStartScanor;
    private boolean isTest;
    private LoadingDialog loadingDialog;
    private MutilTextTipDialog mutilTextTipDialog;
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

    public ScavengingWharfNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
        this.selectManufactorIndex = 0;
        this.clickTime = 0L;
        this.handler = new Handler() { // from class: com.shj.setting.generator.ScavengingWharfNote.3
            AnonymousClass3() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 0) {
                    ScavengingWharfNote scavengingWharfNote = ScavengingWharfNote.this;
                    scavengingWharfNote.setTestButtonName(scavengingWharfNote.context.getString(R.string.scavenging_test));
                    ToastUitl.showLong(ScavengingWharfNote.this.context, (String) message.obj);
                } else if (message.what == 1) {
                    String asString = CacheHelper.getFileCache().getAsString("SCANOR_LAST_DEV");
                    ScavengingWharfNote.this.serialPortAddress = asString;
                    ScavengingWharfNote.this.setSerialPortAddress(asString);
                } else if (message.what == 2) {
                    if (ScavengingWharfNote.this.mutilTextTipDialog != null) {
                        ScavengingWharfNote.this.mutilTextTipDialog.show();
                    } else {
                        ScavengingWharfNote.this.mutilTextTipDialog = new MutilTextTipDialog(ScavengingWharfNote.this.context);
                        ScavengingWharfNote.this.mutilTextTipDialog.show();
                    }
                    ScavengingWharfNote.this.mutilTextTipDialog.addTextShow((String) message.obj);
                }
                if (ScavengingWharfNote.this.loadingDialog != null) {
                    ScavengingWharfNote.this.loadingDialog.dismiss();
                }
            }
        };
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
        manufactorBaudRateData.manufactor = "鹰捷";
        manufactorBaudRateData.baudRate = 115200L;
        this.ManufactorBaudRateDataList.add(manufactorBaudRateData);
        BaseDeviceNote.ManufactorBaudRateData manufactorBaudRateData2 = new BaseDeviceNote.ManufactorBaudRateData();
        manufactorBaudRateData2.manufactor = "幕聊";
        manufactorBaudRateData2.baudRate = 115200L;
        this.ManufactorBaudRateDataList.add(manufactorBaudRateData2);
        BaseDeviceNote.ManufactorBaudRateData manufactorBaudRateData3 = new BaseDeviceNote.ManufactorBaudRateData();
        manufactorBaudRateData3.manufactor = "图腾易讯";
        manufactorBaudRateData3.baudRate = 115200L;
        this.ManufactorBaudRateDataList.add(manufactorBaudRateData3);
        setManufacturerSelectedListener();
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    public void manufacturerSelected(int i) {
        this.selectManufactorIndex = i;
        setBaudRate(String.valueOf(this.ManufactorBaudRateDataList.get(i).baudRate));
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected void serchSerialPortClick() {
        startScanor();
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    public void testButtonClick() {
        if (Math.abs(System.currentTimeMillis() - this.clickTime) < 1000) {
            return;
        }
        this.clickTime = System.currentTimeMillis();
        if (this.isTest) {
            this.isTest = false;
            setTestButtonName(this.context.getString(R.string.scavenging_test));
        } else {
            setTestButtonName(this.context.getString(R.string.stop_test));
            this.isTest = true;
        }
        startScanor();
    }

    /* renamed from: com.shj.setting.generator.ScavengingWharfNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Scanor.ScanorListener {
        AnonymousClass1() {
        }

        @Override // com.shj.device.scanor.Scanor.ScanorListener
        public void onMessage(String str) {
            Loger.writeLog("SCANOR", str);
            if (str.equalsIgnoreCase("noScanor") || str.contains("没有找到扫码头")) {
                ScavengingWharfNote.this.isStartScanor = false;
                ScavengingWharfNote.this.isTest = false;
                Message message = new Message();
                message.what = 0;
                message.obj = ScavengingWharfNote.this.context.getString(R.string.not_found_device);
                ScavengingWharfNote.this.handler.sendMessage(message);
                return;
            }
            if (!str.equalsIgnoreCase("connected") && !str.contains("已找到扫码头") && !"isRunning".equals(str)) {
                if (ScavengingWharfNote.this.isTest) {
                    String string = ScavengingWharfNote.this.context.getString(R.string.scavenging_data_is);
                    Message message2 = new Message();
                    message2.what = 2;
                    message2.obj = string + str;
                    ScavengingWharfNote.this.handler.sendMessage(message2);
                    return;
                }
                return;
            }
            Message message3 = new Message();
            message3.what = 1;
            ScavengingWharfNote.this.handler.sendMessage(message3);
            ScavengingWharfNote.this.isStartScanor = true;
        }
    }

    private void startScanor() {
        if (this.isStartScanor) {
            return;
        }
        Scanor.setScanorListener(new Scanor.ScanorListener() { // from class: com.shj.setting.generator.ScavengingWharfNote.1
            AnonymousClass1() {
            }

            @Override // com.shj.device.scanor.Scanor.ScanorListener
            public void onMessage(String str) {
                Loger.writeLog("SCANOR", str);
                if (str.equalsIgnoreCase("noScanor") || str.contains("没有找到扫码头")) {
                    ScavengingWharfNote.this.isStartScanor = false;
                    ScavengingWharfNote.this.isTest = false;
                    Message message = new Message();
                    message.what = 0;
                    message.obj = ScavengingWharfNote.this.context.getString(R.string.not_found_device);
                    ScavengingWharfNote.this.handler.sendMessage(message);
                    return;
                }
                if (!str.equalsIgnoreCase("connected") && !str.contains("已找到扫码头") && !"isRunning".equals(str)) {
                    if (ScavengingWharfNote.this.isTest) {
                        String string = ScavengingWharfNote.this.context.getString(R.string.scavenging_data_is);
                        Message message2 = new Message();
                        message2.what = 2;
                        message2.obj = string + str;
                        ScavengingWharfNote.this.handler.sendMessage(message2);
                        return;
                    }
                    return;
                }
                Message message3 = new Message();
                message3.what = 1;
                ScavengingWharfNote.this.handler.sendMessage(message3);
                ScavengingWharfNote.this.isStartScanor = true;
            }
        });
        LoadingDialog loadingDialog = new LoadingDialog(this.context, this.context.getString(R.string.searching));
        this.loadingDialog = loadingDialog;
        loadingDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.shj.setting.generator.ScavengingWharfNote.2
            AnonymousClass2() {
            }

            /* renamed from: com.shj.setting.generator.ScavengingWharfNote$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements Runnable {
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    Scanor.start(ScavengingWharfNote.this.context, false);
                }
            }

            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                new Thread(new Runnable() { // from class: com.shj.setting.generator.ScavengingWharfNote.2.1
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        Scanor.start(ScavengingWharfNote.this.context, false);
                    }
                }).start();
            }
        });
        this.loadingDialog.show();
    }

    /* renamed from: com.shj.setting.generator.ScavengingWharfNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements DialogInterface.OnShowListener {
        AnonymousClass2() {
        }

        /* renamed from: com.shj.setting.generator.ScavengingWharfNote$2$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                Scanor.start(ScavengingWharfNote.this.context, false);
            }
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            new Thread(new Runnable() { // from class: com.shj.setting.generator.ScavengingWharfNote.2.1
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    Scanor.start(ScavengingWharfNote.this.context, false);
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.generator.ScavengingWharfNote$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends Handler {
        AnonymousClass3() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 0) {
                ScavengingWharfNote scavengingWharfNote = ScavengingWharfNote.this;
                scavengingWharfNote.setTestButtonName(scavengingWharfNote.context.getString(R.string.scavenging_test));
                ToastUitl.showLong(ScavengingWharfNote.this.context, (String) message.obj);
            } else if (message.what == 1) {
                String asString = CacheHelper.getFileCache().getAsString("SCANOR_LAST_DEV");
                ScavengingWharfNote.this.serialPortAddress = asString;
                ScavengingWharfNote.this.setSerialPortAddress(asString);
            } else if (message.what == 2) {
                if (ScavengingWharfNote.this.mutilTextTipDialog != null) {
                    ScavengingWharfNote.this.mutilTextTipDialog.show();
                } else {
                    ScavengingWharfNote.this.mutilTextTipDialog = new MutilTextTipDialog(ScavengingWharfNote.this.context);
                    ScavengingWharfNote.this.mutilTextTipDialog.show();
                }
                ScavengingWharfNote.this.mutilTextTipDialog.addTextShow((String) message.obj);
            }
            if (ScavengingWharfNote.this.loadingDialog != null) {
                ScavengingWharfNote.this.loadingDialog.dismiss();
            }
        }
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
        return this.context.getResources().getString(R.string.scavenging_test);
    }
}
