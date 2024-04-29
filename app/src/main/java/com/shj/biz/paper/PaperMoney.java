package com.shj.biz.paper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;
import com.oysb.utils.Loger;
import com.shj.MoneyType;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.shj.biz.paper.ITLDeviceCom;
import device.itl.sspcoms.BarCodeReader;
import device.itl.sspcoms.DeviceEvent;
import device.itl.sspcoms.DeviceEventType;
import device.itl.sspcoms.ItlCurrency;
import device.itl.sspcoms.PayoutRoute;
import device.itl.sspcoms.SSPDevice;
import device.itl.sspcoms.SSPDeviceType;
import device.itl.sspcoms.SSPPayoutEvent;
import device.itl.sspcoms.SSPSystem;
import device.itl.sspcoms.SSPUpdate;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class PaperMoney {
    public static ITLDeviceCom deviceCom = null;
    private static D2xxManager ftD2xx = null;
    private static FT_Device ftDev = null;
    private static int intoCashboxMoney = 50000;
    public static boolean isChange = true;
    public static Activity mActivity;
    public static BroadcastReceiver mUsbReceiver = new BroadcastReceiver() { // from class: com.shj.biz.paper.PaperMoney.1
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                PaperMoney.openDevice();
            } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                PaperMoney.closeDevice();
            }
        }
    };
    private static String m_DeviceCountry;
    static ITLDeviceCom.PaperResultListener paperResultListener;
    private static int payamount;
    private static String[] pickerValues;
    private static SSPDevice sspDevice;
    private SSPUpdate sspUpdate = null;

    public static void findPapermoneyMachine(Activity activity) {
        mActivity = activity;
        try {
            ftD2xx = D2xxManager.getInstance(activity);
        } catch (D2xxManager.D2xxException e) {
            Log.e("SSP FTmanager", e.toString());
        }
        callBackData();
        if (deviceCom == null) {
            deviceCom = new ITLDeviceCom(activity, paperResultListener);
            openDevice();
            FT_Device fT_Device = ftDev;
            if (fT_Device != null) {
                deviceCom.setup(fT_Device, 0, false, true, 81985526925837671L);
                deviceCom.start();
                placePapermoney();
            } else {
                deviceCom = null;
                Toast.makeText(activity, "No USB connection detected!", 0).show();
            }
        }
    }

    public static void placePapermoney() {
        ITLDeviceCom iTLDeviceCom = deviceCom;
        if (iTLDeviceCom != null) {
            iTLDeviceCom.SetDeviceEnable(true);
        }
    }

    public static void closePapermoney() {
        ITLDeviceCom iTLDeviceCom = deviceCom;
        if (iTLDeviceCom != null) {
            iTLDeviceCom.SetDeviceEnable(false);
        }
    }

    public static void onSettlement(int i) {
        payamount = Shj.getWallet().getCatchMoney().intValue();
        Log.e("onSettlement", "余额： " + payamount);
        int i2 = payamount;
        if (i2 == i) {
            Log.e("onSettlement", "结算： " + payamount);
            return;
        }
        if (i2 < i) {
            Log.e("onSettlement", "请继续投币： " + payamount);
            return;
        }
        changePapermoney(i2 - i);
    }

    public static void intoCashbox() {
        SSPDevice sSPDevice = sspDevice;
        if (sSPDevice == null || sSPDevice.currency == null || sspDevice.currency.size() <= 0) {
            return;
        }
        for (int i = 0; i < sspDevice.currency.size(); i++) {
            deviceCom.SetPayoutRoute(sspDevice.currency.get(i), PayoutRoute.PayoutStore);
        }
    }

    public static void changePapermoney(int i) {
        int i2;
        int i3 = 0;
        while (true) {
            try {
                String[] strArr = pickerValues;
                if (i3 >= strArr.length) {
                    i2 = 0;
                    break;
                }
                i2 = Integer.valueOf(strArr[i3]).intValue() * 100;
                if (i == i2) {
                    break;
                } else {
                    i3++;
                }
            } catch (Exception unused) {
                Loger.writeLog("APP", "找零异常，纸币器可能断开连接");
                return;
            }
        }
        ShjManager.getMoneyListener().onChargStart(i2);
        if (i2 == 0) {
            return;
        }
        isChange = false;
        ItlCurrency itlCurrency = new ItlCurrency();
        itlCurrency.country = m_DeviceCountry;
        itlCurrency.value = i2;
        deviceCom.PayoutAmount(itlCurrency);
    }

    public static void clearEscrow() {
        deviceCom.EmptyPayout();
    }

    public static void onEscrowEnable(boolean z) {
        deviceCom.SetEscrowMode(z);
    }

    public static void isEscrowEnable(boolean z) {
        if (z) {
            deviceCom.SetEscrowAction(SSPSystem.BillAction.Accept);
        } else {
            deviceCom.SetEscrowAction(SSPSystem.BillAction.Reject);
        }
    }

    /* renamed from: com.shj.biz.paper.PaperMoney$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                PaperMoney.openDevice();
            } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                PaperMoney.closeDevice();
            }
        }
    }

    public static void openDevice() {
        FT_Device fT_Device = ftDev;
        if (fT_Device != null && fT_Device.isOpen()) {
            SetConfig(9600, (byte) 8, (byte) 2, (byte) 0, (byte) 0);
            ftDev.purge((byte) 3);
            ftDev.restartInTask();
            return;
        }
        D2xxManager d2xxManager = ftD2xx;
        if (d2xxManager != null) {
            int createDeviceInfoList = d2xxManager.createDeviceInfoList(mActivity);
            ftD2xx.getDeviceInfoList(createDeviceInfoList, new D2xxManager.FtDeviceInfoListNode[createDeviceInfoList]);
            if (createDeviceInfoList <= 0) {
                return;
            }
            FT_Device fT_Device2 = ftDev;
            if (fT_Device2 == null) {
                ftDev = ftD2xx.openByIndex(mActivity, 0);
            } else {
                synchronized (fT_Device2) {
                    ftDev = ftD2xx.openByIndex(mActivity, 0);
                }
            }
            if (ftDev.isOpen()) {
                SetConfig(9600, (byte) 8, (byte) 2, (byte) 0, (byte) 0);
                ftDev.purge((byte) 3);
                ftDev.restartInTask();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x002d, code lost:
    
        if (r7 != 4) goto L60;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void SetConfig(int r4, byte r5, byte r6, byte r7, byte r8) {
        /*
            com.ftdi.j2xx.FT_Device r0 = com.shj.biz.paper.PaperMoney.ftDev
            boolean r0 = r0.isOpen()
            if (r0 != 0) goto L9
            return
        L9:
            com.ftdi.j2xx.FT_Device r0 = com.shj.biz.paper.PaperMoney.ftDev
            r1 = 0
            r0.setBitMode(r1, r1)
            com.ftdi.j2xx.FT_Device r0 = com.shj.biz.paper.PaperMoney.ftDev
            r0.setBaudRate(r4)
            r4 = 7
            if (r5 == r4) goto L19
            r4 = 8
        L19:
            r5 = 1
            r0 = 2
            if (r6 == r5) goto L22
            if (r6 == r0) goto L20
            goto L22
        L20:
            r6 = 2
            goto L23
        L22:
            r6 = 0
        L23:
            r2 = 4
            r3 = 3
            if (r7 == 0) goto L36
            if (r7 == r5) goto L34
            if (r7 == r0) goto L32
            if (r7 == r3) goto L30
            if (r7 == r2) goto L37
            goto L36
        L30:
            r2 = 3
            goto L37
        L32:
            r2 = 2
            goto L37
        L34:
            r2 = 1
            goto L37
        L36:
            r2 = 0
        L37:
            com.ftdi.j2xx.FT_Device r7 = com.shj.biz.paper.PaperMoney.ftDev
            r7.setDataCharacteristics(r4, r6, r2)
            if (r8 == 0) goto L4d
            if (r8 == r5) goto L4b
            if (r8 == r0) goto L48
            if (r8 == r3) goto L45
            goto L4d
        L45:
            r1 = 1024(0x400, float:1.435E-42)
            goto L4d
        L48:
            r1 = 512(0x200, float:7.17E-43)
            goto L4d
        L4b:
            r1 = 256(0x100, float:3.59E-43)
        L4d:
            com.ftdi.j2xx.FT_Device r4 = com.shj.biz.paper.PaperMoney.ftDev
            r5 = 11
            r6 = 13
            r4.setFlowControl(r1, r5, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.paper.PaperMoney.SetConfig(int, byte, byte, byte, byte):void");
    }

    public static void closeDevice() {
        ITLDeviceCom iTLDeviceCom = deviceCom;
        if (iTLDeviceCom != null) {
            iTLDeviceCom.Stop();
        }
        FT_Device fT_Device = ftDev;
        if (fT_Device != null) {
            fT_Device.close();
        }
    }

    public static void DisplayChannels() {
        if (sspDevice.minPayout <= 0 || sspDevice.minPayout == -1) {
            return;
        }
        int i = sspDevice.storedPayoutValue / sspDevice.minPayout;
        int[] iArr = new int[i];
        int i2 = 0;
        for (int i3 = 1; i3 <= i; i3++) {
            SSPDevice sSPDevice = sspDevice;
            if (sSPDevice.IsSPPayoutPossible(sSPDevice.shortDatasetVersion, sspDevice.minPayout * i3)) {
                iArr[i2] = (sspDevice.minPayout / 100) * i3;
                i2++;
            }
        }
        if (i2 > 0) {
            pickerValues = new String[i2];
            for (int i4 = 0; i4 < i2; i4++) {
                pickerValues[i4] = Integer.toString(iArr[i4]);
            }
        }
    }

    /* renamed from: com.shj.biz.paper.PaperMoney$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements ITLDeviceCom.PaperResultListener {
        AnonymousClass2() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:17:0x0033, code lost:
        
            if (r8 != 4) goto L60;
         */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0046  */
        @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void SetConfig(int r5, byte r6, byte r7, byte r8, byte r9) {
            /*
                r4 = this;
                com.ftdi.j2xx.FT_Device r0 = com.shj.biz.paper.PaperMoney.access$100()
                boolean r0 = r0.isOpen()
                if (r0 != 0) goto Lb
                return
            Lb:
                com.ftdi.j2xx.FT_Device r0 = com.shj.biz.paper.PaperMoney.access$100()
                r1 = 0
                r0.setBitMode(r1, r1)
                com.ftdi.j2xx.FT_Device r0 = com.shj.biz.paper.PaperMoney.access$100()
                r0.setBaudRate(r5)
                r5 = 7
                if (r6 == r5) goto L1f
                r5 = 8
            L1f:
                r6 = 1
                r0 = 2
                if (r7 == r6) goto L28
                if (r7 == r0) goto L26
                goto L28
            L26:
                r7 = 2
                goto L29
            L28:
                r7 = 0
            L29:
                r2 = 4
                r3 = 3
                if (r8 == 0) goto L3c
                if (r8 == r6) goto L3a
                if (r8 == r0) goto L38
                if (r8 == r3) goto L36
                if (r8 == r2) goto L3d
                goto L3c
            L36:
                r2 = 3
                goto L3d
            L38:
                r2 = 2
                goto L3d
            L3a:
                r2 = 1
                goto L3d
            L3c:
                r2 = 0
            L3d:
                com.ftdi.j2xx.FT_Device r8 = com.shj.biz.paper.PaperMoney.access$100()
                r8.setDataCharacteristics(r5, r7, r2)
                if (r9 == 0) goto L55
                if (r9 == r6) goto L53
                if (r9 == r0) goto L50
                if (r9 == r3) goto L4d
                goto L55
            L4d:
                r1 = 1024(0x400, float:1.435E-42)
                goto L55
            L50:
                r1 = 512(0x200, float:7.17E-43)
                goto L55
            L53:
                r1 = 256(0x100, float:3.59E-43)
            L55:
                com.ftdi.j2xx.FT_Device r5 = com.shj.biz.paper.PaperMoney.access$100()
                r6 = 11
                r7 = 13
                r5.setFlowControl(r1, r6, r7)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.paper.PaperMoney.AnonymousClass2.SetConfig(int, byte, byte, byte, byte):void");
        }

        @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
        public void DisplaySetUp(SSPDevice sSPDevice) {
            SSPDevice unused = PaperMoney.sspDevice = sSPDevice;
            if (sSPDevice.type == SSPDeviceType.SmartPayout) {
                String unused2 = PaperMoney.m_DeviceCountry = sSPDevice.shortDatasetVersion;
                PaperMoney.DisplayChannels();
                if (sSPDevice.barCodeReader.hardWareConfig != SSPDevice.BarCodeStatus.None) {
                    BarCodeReader barCodeReader = new BarCodeReader();
                    barCodeReader.barcodeReadEnabled = true;
                    barCodeReader.billReadEnabled = true;
                    barCodeReader.numberOfCharacters = (byte) 18;
                    barCodeReader.format = SSPDevice.BarCodeFormat.Interleaved2of5;
                    barCodeReader.enabledConfig = SSPDevice.BarCodeStatus.Both;
                    PaperMoney.deviceCom.SetBarcocdeConfig(barCodeReader);
                }
                PaperMoney.intoCashbox();
                return;
            }
            Log.e("DisplaySetUp", "=Connected device is not SMART Payout：" + sSPDevice.type.toString());
        }

        @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
        public void DeviceDisconnected(SSPDevice sSPDevice) {
            Log.e("DeviceDisconnected", "=DISCONNECTED!!!");
        }

        @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
        public void DisplayEvents(DeviceEvent deviceEvent) {
            switch (AnonymousClass3.$SwitchMap$device$itl$sspcoms$DeviceEventType[deviceEvent.event.ordinal()]) {
                case 1:
                    Log.e("DisplayEvents", "BackInService=back in service");
                    return;
                case 2:
                    Log.e("DisplayEvents", "CommunicationsFailure=Device coms Failure");
                    return;
                case 3:
                    Log.e("DisplayEvents", "Ready");
                    return;
                case 4:
                    Log.e("DisplayEvents", "Reading");
                    return;
                case 5:
                    Log.e("DisplayEvents", "Bill Escrow：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                    return;
                case 6:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                default:
                    return;
                case 7:
                    Log.e("DisplayEvents", "Bill Reject");
                    return;
                case 8:
                    Log.e("DisplayEvents", "Bill jammed");
                    return;
                case 9:
                    Log.e("DisplayEvents", "Bill Fraud=" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                    return;
                case 10:
                    ShjManager.setMoney(MoneyType.Paper, ((int) deviceEvent.value) * 100, "");
                    Log.e("DisplayEvents", "Bill Credit=" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                    return;
                case 11:
                    Log.e("DisplayEvents", "Bill Cashbox full");
                    return;
                case 12:
                    Log.e("DisplayEvents", "Initialising");
                    return;
                case 13:
                    Log.e("DisplayEvents", "Disabled");
                    return;
                case 14:
                    Log.e("DisplayEvents", "Software error");
                    return;
                case 15:
                    Log.e("DisplayEvents", "All channels disabled");
                    return;
                case 16:
                    Log.e("DisplayEvents", "Cashbox removed");
                    return;
                case 17:
                    Log.e("DisplayEvents", "Cashbox replaced");
                    return;
                case 18:
                    Log.e("DisplayEvents", "Note path open");
                    return;
                case 19:
                    Log.e("DisplayEvents", "Barcode ticket escrow：" + deviceEvent.currency);
                    return;
                case 20:
                    Log.e("DisplayEvents", "Barcode ticket stacked：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                    return;
                case 21:
                    Log.e("DisplayEvents", "Bill Stored in payout：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                    ShjManager.setMoney(MoneyType.Paper, 0, "");
                    return;
                case 22:
                    Log.e("DisplayEvents", "Payout out of service!");
                    return;
                case 23:
                    PaperMoney.isChange = false;
                    Log.e("DisplayEvents", "Bill dispensing：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                    return;
                case 24:
                    PaperMoney.isChange = false;
                    Log.e("DisplayEvents", "Bill Dispensed：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                    return;
                case 25:
                    Log.e("DisplayEvents", "Payout emptying...");
                    return;
                case 26:
                    Log.e("DisplayEvents", "Payout emptied");
                    return;
                case 27:
                    Log.e("DisplayEvents", "Payout emptying：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                    return;
                case 28:
                    Log.e("DisplayEvents", "Payout emptied：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                    return;
                case 34:
                    Log.e("DisplayEvents", "NF detatched");
                    return;
                case 35:
                    Log.e("DisplayEvents", "NF attached");
                    return;
                case 36:
                    Log.e("DisplayEvents", "Payout Device Full");
                    return;
            }
        }

        @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
        public void DisplayPayoutEvents(SSPPayoutEvent sSPPayoutEvent) {
            switch (AnonymousClass3.$SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[sSPPayoutEvent.event.ordinal()]) {
                case 1:
                    PaperMoney.isChange = false;
                    Log.e("DisplayPayoutEvents", "CashPaidOut：" + ("Paying " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalue)) + " of  " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalueRequested))));
                    PaperMoney.DisplayChannels();
                    return;
                case 2:
                    PaperMoney.DisplayChannels();
                    return;
                case 3:
                    PaperMoney.DisplayChannels();
                    return;
                case 4:
                case 5:
                    Log.e("DisplayPayoutEvents", "FloatStartedORPayoutStarted：" + ("Request " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalue)) + " of  " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalueRequested))));
                    return;
                case 6:
                case 7:
                    Log.e("DisplayPayoutEvents", "PayoutEndedORFloatEnded：" + ("Paid " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalue)) + " of  " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalueRequested))));
                    ShjManager.setMoney(MoneyType.EAT, 0, "吞币");
                    PaperMoney.isChange = true;
                    ShjManager.getMoneyListener().onChargFinished(0, ((int) sSPPayoutEvent.realvalue) * 100);
                    return;
                case 8:
                case 9:
                case 12:
                case 14:
                default:
                    return;
                case 10:
                    Log.e("DisplayPayoutEvents", "EmptyStarted：Empty started");
                    return;
                case 11:
                    Log.e("DisplayPayoutEvents", "EmptyEnded：Empty ended");
                    return;
                case 13:
                    Log.e("DisplayPayoutEvents", "PayoutAmountInvalid：Payout request invalid amount；" + sSPPayoutEvent.country + StringUtils.SPACE + String.valueOf(sSPPayoutEvent.value));
                    return;
                case 15:
                    PaperMoney.DisplayChannels();
                    return;
                case 16:
                    Log.e("DisplayPayoutEvents", "PayoutDeviceNotConnected：Payout device not connected!");
                    return;
                case 17:
                    Log.e("DisplayPayoutEvents", "PayoutDeviceEmpty：Payout device is empty!");
                    return;
                case 18:
                    Log.e("DisplayPayoutEvents", "PayoutDeviceDisabled：Payout device is disabled");
                    return;
            }
        }

        @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
        public void UpdateFileDownload(SSPUpdate sSPUpdate) {
            int i = AnonymousClass3.$SwitchMap$device$itl$sspcoms$SSPUpdate$SSPDownloadStatus[sSPUpdate.UpdateStatus.ordinal()];
            if (i == 1) {
                Log.e("UpdateFileDownload", "dwnInitialise：Downloading Ram");
                return;
            }
            if (i == 2) {
                Log.e("UpdateFileDownload", "dwnRamCode");
                return;
            }
            if (i == 3) {
                Log.e("UpdateFileDownload", "dwnMainCode：Downloading flash");
            } else if (i == 4) {
                Log.e("UpdateFileDownload", "dwnComplete");
            } else {
                if (i != 5) {
                    return;
                }
                Log.e("UpdateFileDownload", "dwnError");
            }
        }
    }

    public static void callBackData() {
        paperResultListener = new ITLDeviceCom.PaperResultListener() { // from class: com.shj.biz.paper.PaperMoney.2
            AnonymousClass2() {
            }

            @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
            public void SetConfig(int i, byte b, byte b2, byte b3, byte b4) {
                /*  JADX ERROR: Method code generation error
                    java.lang.NullPointerException
                    */
                /*
                    this = this;
                    com.ftdi.j2xx.FT_Device r0 = com.shj.biz.paper.PaperMoney.access$100()
                    boolean r0 = r0.isOpen()
                    if (r0 != 0) goto Lb
                    return
                Lb:
                    com.ftdi.j2xx.FT_Device r0 = com.shj.biz.paper.PaperMoney.access$100()
                    r1 = 0
                    r0.setBitMode(r1, r1)
                    com.ftdi.j2xx.FT_Device r0 = com.shj.biz.paper.PaperMoney.access$100()
                    r0.setBaudRate(r5)
                    r5 = 7
                    if (r6 == r5) goto L1f
                    r5 = 8
                L1f:
                    r6 = 1
                    r0 = 2
                    if (r7 == r6) goto L28
                    if (r7 == r0) goto L26
                    goto L28
                L26:
                    r7 = 2
                    goto L29
                L28:
                    r7 = 0
                L29:
                    r2 = 4
                    r3 = 3
                    if (r8 == 0) goto L3c
                    if (r8 == r6) goto L3a
                    if (r8 == r0) goto L38
                    if (r8 == r3) goto L36
                    if (r8 == r2) goto L3d
                    goto L3c
                L36:
                    r2 = 3
                    goto L3d
                L38:
                    r2 = 2
                    goto L3d
                L3a:
                    r2 = 1
                    goto L3d
                L3c:
                    r2 = 0
                L3d:
                    com.ftdi.j2xx.FT_Device r8 = com.shj.biz.paper.PaperMoney.access$100()
                    r8.setDataCharacteristics(r5, r7, r2)
                    if (r9 == 0) goto L55
                    if (r9 == r6) goto L53
                    if (r9 == r0) goto L50
                    if (r9 == r3) goto L4d
                    goto L55
                L4d:
                    r1 = 1024(0x400, float:1.435E-42)
                    goto L55
                L50:
                    r1 = 512(0x200, float:7.17E-43)
                    goto L55
                L53:
                    r1 = 256(0x100, float:3.59E-43)
                L55:
                    com.ftdi.j2xx.FT_Device r5 = com.shj.biz.paper.PaperMoney.access$100()
                    r6 = 11
                    r7 = 13
                    r5.setFlowControl(r1, r6, r7)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.paper.PaperMoney.AnonymousClass2.SetConfig(int, byte, byte, byte, byte):void");
            }

            @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
            public void DisplaySetUp(SSPDevice sSPDevice) {
                SSPDevice unused = PaperMoney.sspDevice = sSPDevice;
                if (sSPDevice.type == SSPDeviceType.SmartPayout) {
                    String unused2 = PaperMoney.m_DeviceCountry = sSPDevice.shortDatasetVersion;
                    PaperMoney.DisplayChannels();
                    if (sSPDevice.barCodeReader.hardWareConfig != SSPDevice.BarCodeStatus.None) {
                        BarCodeReader barCodeReader = new BarCodeReader();
                        barCodeReader.barcodeReadEnabled = true;
                        barCodeReader.billReadEnabled = true;
                        barCodeReader.numberOfCharacters = (byte) 18;
                        barCodeReader.format = SSPDevice.BarCodeFormat.Interleaved2of5;
                        barCodeReader.enabledConfig = SSPDevice.BarCodeStatus.Both;
                        PaperMoney.deviceCom.SetBarcocdeConfig(barCodeReader);
                    }
                    PaperMoney.intoCashbox();
                    return;
                }
                Log.e("DisplaySetUp", "=Connected device is not SMART Payout：" + sSPDevice.type.toString());
            }

            @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
            public void DeviceDisconnected(SSPDevice sSPDevice) {
                Log.e("DeviceDisconnected", "=DISCONNECTED!!!");
            }

            @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
            public void DisplayEvents(DeviceEvent deviceEvent) {
                switch (AnonymousClass3.$SwitchMap$device$itl$sspcoms$DeviceEventType[deviceEvent.event.ordinal()]) {
                    case 1:
                        Log.e("DisplayEvents", "BackInService=back in service");
                        return;
                    case 2:
                        Log.e("DisplayEvents", "CommunicationsFailure=Device coms Failure");
                        return;
                    case 3:
                        Log.e("DisplayEvents", "Ready");
                        return;
                    case 4:
                        Log.e("DisplayEvents", "Reading");
                        return;
                    case 5:
                        Log.e("DisplayEvents", "Bill Escrow：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                        return;
                    case 6:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    default:
                        return;
                    case 7:
                        Log.e("DisplayEvents", "Bill Reject");
                        return;
                    case 8:
                        Log.e("DisplayEvents", "Bill jammed");
                        return;
                    case 9:
                        Log.e("DisplayEvents", "Bill Fraud=" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                        return;
                    case 10:
                        ShjManager.setMoney(MoneyType.Paper, ((int) deviceEvent.value) * 100, "");
                        Log.e("DisplayEvents", "Bill Credit=" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                        return;
                    case 11:
                        Log.e("DisplayEvents", "Bill Cashbox full");
                        return;
                    case 12:
                        Log.e("DisplayEvents", "Initialising");
                        return;
                    case 13:
                        Log.e("DisplayEvents", "Disabled");
                        return;
                    case 14:
                        Log.e("DisplayEvents", "Software error");
                        return;
                    case 15:
                        Log.e("DisplayEvents", "All channels disabled");
                        return;
                    case 16:
                        Log.e("DisplayEvents", "Cashbox removed");
                        return;
                    case 17:
                        Log.e("DisplayEvents", "Cashbox replaced");
                        return;
                    case 18:
                        Log.e("DisplayEvents", "Note path open");
                        return;
                    case 19:
                        Log.e("DisplayEvents", "Barcode ticket escrow：" + deviceEvent.currency);
                        return;
                    case 20:
                        Log.e("DisplayEvents", "Barcode ticket stacked：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                        return;
                    case 21:
                        Log.e("DisplayEvents", "Bill Stored in payout：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                        ShjManager.setMoney(MoneyType.Paper, 0, "");
                        return;
                    case 22:
                        Log.e("DisplayEvents", "Payout out of service!");
                        return;
                    case 23:
                        PaperMoney.isChange = false;
                        Log.e("DisplayEvents", "Bill dispensing：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                        return;
                    case 24:
                        PaperMoney.isChange = false;
                        Log.e("DisplayEvents", "Bill Dispensed：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                        return;
                    case 25:
                        Log.e("DisplayEvents", "Payout emptying...");
                        return;
                    case 26:
                        Log.e("DisplayEvents", "Payout emptied");
                        return;
                    case 27:
                        Log.e("DisplayEvents", "Payout emptying：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                        return;
                    case 28:
                        Log.e("DisplayEvents", "Payout emptied：" + deviceEvent.currency + StringUtils.SPACE + String.valueOf((int) deviceEvent.value) + ".00");
                        return;
                    case 34:
                        Log.e("DisplayEvents", "NF detatched");
                        return;
                    case 35:
                        Log.e("DisplayEvents", "NF attached");
                        return;
                    case 36:
                        Log.e("DisplayEvents", "Payout Device Full");
                        return;
                }
            }

            @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
            public void DisplayPayoutEvents(SSPPayoutEvent sSPPayoutEvent) {
                switch (AnonymousClass3.$SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[sSPPayoutEvent.event.ordinal()]) {
                    case 1:
                        PaperMoney.isChange = false;
                        Log.e("DisplayPayoutEvents", "CashPaidOut：" + ("Paying " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalue)) + " of  " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalueRequested))));
                        PaperMoney.DisplayChannels();
                        return;
                    case 2:
                        PaperMoney.DisplayChannels();
                        return;
                    case 3:
                        PaperMoney.DisplayChannels();
                        return;
                    case 4:
                    case 5:
                        Log.e("DisplayPayoutEvents", "FloatStartedORPayoutStarted：" + ("Request " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalue)) + " of  " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalueRequested))));
                        return;
                    case 6:
                    case 7:
                        Log.e("DisplayPayoutEvents", "PayoutEndedORFloatEnded：" + ("Paid " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalue)) + " of  " + sSPPayoutEvent.country + StringUtils.SPACE + String.format("%.2f", Double.valueOf(sSPPayoutEvent.realvalueRequested))));
                        ShjManager.setMoney(MoneyType.EAT, 0, "吞币");
                        PaperMoney.isChange = true;
                        ShjManager.getMoneyListener().onChargFinished(0, ((int) sSPPayoutEvent.realvalue) * 100);
                        return;
                    case 8:
                    case 9:
                    case 12:
                    case 14:
                    default:
                        return;
                    case 10:
                        Log.e("DisplayPayoutEvents", "EmptyStarted：Empty started");
                        return;
                    case 11:
                        Log.e("DisplayPayoutEvents", "EmptyEnded：Empty ended");
                        return;
                    case 13:
                        Log.e("DisplayPayoutEvents", "PayoutAmountInvalid：Payout request invalid amount；" + sSPPayoutEvent.country + StringUtils.SPACE + String.valueOf(sSPPayoutEvent.value));
                        return;
                    case 15:
                        PaperMoney.DisplayChannels();
                        return;
                    case 16:
                        Log.e("DisplayPayoutEvents", "PayoutDeviceNotConnected：Payout device not connected!");
                        return;
                    case 17:
                        Log.e("DisplayPayoutEvents", "PayoutDeviceEmpty：Payout device is empty!");
                        return;
                    case 18:
                        Log.e("DisplayPayoutEvents", "PayoutDeviceDisabled：Payout device is disabled");
                        return;
                }
            }

            @Override // com.shj.biz.paper.ITLDeviceCom.PaperResultListener
            public void UpdateFileDownload(SSPUpdate sSPUpdate) {
                int i = AnonymousClass3.$SwitchMap$device$itl$sspcoms$SSPUpdate$SSPDownloadStatus[sSPUpdate.UpdateStatus.ordinal()];
                if (i == 1) {
                    Log.e("UpdateFileDownload", "dwnInitialise：Downloading Ram");
                    return;
                }
                if (i == 2) {
                    Log.e("UpdateFileDownload", "dwnRamCode");
                    return;
                }
                if (i == 3) {
                    Log.e("UpdateFileDownload", "dwnMainCode：Downloading flash");
                } else if (i == 4) {
                    Log.e("UpdateFileDownload", "dwnComplete");
                } else {
                    if (i != 5) {
                        return;
                    }
                    Log.e("UpdateFileDownload", "dwnError");
                }
            }
        };
    }

    /* renamed from: com.shj.biz.paper.PaperMoney$3 */
    /* loaded from: classes2.dex */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$device$itl$sspcoms$DeviceEventType;
        static final /* synthetic */ int[] $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent;
        static final /* synthetic */ int[] $SwitchMap$device$itl$sspcoms$SSPUpdate$SSPDownloadStatus;

        static {
            int[] iArr = new int[SSPUpdate.SSPDownloadStatus.values().length];
            $SwitchMap$device$itl$sspcoms$SSPUpdate$SSPDownloadStatus = iArr;
            try {
                iArr[SSPUpdate.SSPDownloadStatus.dwnInitialise.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPUpdate$SSPDownloadStatus[SSPUpdate.SSPDownloadStatus.dwnRamCode.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPUpdate$SSPDownloadStatus[SSPUpdate.SSPDownloadStatus.dwnMainCode.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPUpdate$SSPDownloadStatus[SSPUpdate.SSPDownloadStatus.dwnComplete.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPUpdate$SSPDownloadStatus[SSPUpdate.SSPDownloadStatus.dwnError.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            int[] iArr2 = new int[SSPPayoutEvent.PayoutEvent.values().length];
            $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent = iArr2;
            try {
                iArr2[SSPPayoutEvent.PayoutEvent.CashPaidOut.ordinal()] = 1;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.CashStoreInPayout.ordinal()] = 2;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.CashLevelsChanged.ordinal()] = 3;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.PayoutStarted.ordinal()] = 4;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.FloatStarted.ordinal()] = 5;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.PayoutEnded.ordinal()] = 6;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.FloatEnded.ordinal()] = 7;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.PayinStarted.ordinal()] = 8;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.PayinEnded.ordinal()] = 9;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.EmptyStarted.ordinal()] = 10;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.EmptyEnded.ordinal()] = 11;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.PayoutConfigurationFail.ordinal()] = 12;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.PayoutAmountInvalid.ordinal()] = 13;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.PayoutRequestFail.ordinal()] = 14;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.RouteChanged.ordinal()] = 15;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.PayoutDeviceNotConnected.ordinal()] = 16;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.PayoutDeviceEmpty.ordinal()] = 17;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$SSPPayoutEvent$PayoutEvent[SSPPayoutEvent.PayoutEvent.PayoutDeviceDisabled.ordinal()] = 18;
            } catch (NoSuchFieldError unused23) {
            }
            int[] iArr3 = new int[DeviceEventType.values().length];
            $SwitchMap$device$itl$sspcoms$DeviceEventType = iArr3;
            try {
                iArr3[DeviceEventType.BackInService.ordinal()] = 1;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.CommunicationsFailure.ordinal()] = 2;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.Ready.ordinal()] = 3;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillRead.ordinal()] = 4;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillEscrow.ordinal()] = 5;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillStacked.ordinal()] = 6;
            } catch (NoSuchFieldError unused29) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillReject.ordinal()] = 7;
            } catch (NoSuchFieldError unused30) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillJammed.ordinal()] = 8;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillFraud.ordinal()] = 9;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillCredit.ordinal()] = 10;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.Full.ordinal()] = 11;
            } catch (NoSuchFieldError unused34) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.Initialising.ordinal()] = 12;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.Disabled.ordinal()] = 13;
            } catch (NoSuchFieldError unused36) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.SoftwareError.ordinal()] = 14;
            } catch (NoSuchFieldError unused37) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.AllDisabled.ordinal()] = 15;
            } catch (NoSuchFieldError unused38) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.CashboxRemoved.ordinal()] = 16;
            } catch (NoSuchFieldError unused39) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.CashboxReplaced.ordinal()] = 17;
            } catch (NoSuchFieldError unused40) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.NotePathOpen.ordinal()] = 18;
            } catch (NoSuchFieldError unused41) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BarCodeTicketEscrow.ordinal()] = 19;
            } catch (NoSuchFieldError unused42) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BarCodeTicketStacked.ordinal()] = 20;
            } catch (NoSuchFieldError unused43) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillStoredInPayout.ordinal()] = 21;
            } catch (NoSuchFieldError unused44) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.PayoutOutOfService.ordinal()] = 22;
            } catch (NoSuchFieldError unused45) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.Dispensing.ordinal()] = 23;
            } catch (NoSuchFieldError unused46) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.Dispensed.ordinal()] = 24;
            } catch (NoSuchFieldError unused47) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.Emptying.ordinal()] = 25;
            } catch (NoSuchFieldError unused48) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.Emptied.ordinal()] = 26;
            } catch (NoSuchFieldError unused49) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.SmartEmptying.ordinal()] = 27;
            } catch (NoSuchFieldError unused50) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.SmartEmptied.ordinal()] = 28;
            } catch (NoSuchFieldError unused51) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillTransferedToStacker.ordinal()] = 29;
            } catch (NoSuchFieldError unused52) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillHeldInBezel.ordinal()] = 30;
            } catch (NoSuchFieldError unused53) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillInStoreAtReset.ordinal()] = 31;
            } catch (NoSuchFieldError unused54) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillInStackerAtReset.ordinal()] = 32;
            } catch (NoSuchFieldError unused55) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.BillDispensedAtReset.ordinal()] = 33;
            } catch (NoSuchFieldError unused56) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.NoteFloatRemoved.ordinal()] = 34;
            } catch (NoSuchFieldError unused57) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.NoteFloatAttached.ordinal()] = 35;
            } catch (NoSuchFieldError unused58) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.DeviceFull.ordinal()] = 36;
            } catch (NoSuchFieldError unused59) {
            }
            try {
                $SwitchMap$device$itl$sspcoms$DeviceEventType[DeviceEventType.RefillBillCredit.ordinal()] = 37;
            } catch (NoSuchFieldError unused60) {
            }
        }
    }
}
