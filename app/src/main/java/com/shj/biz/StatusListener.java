package com.shj.biz;

import com.shj.device.VMCStatus;
import java.util.Date;
import java.util.List;

/* loaded from: classes2.dex */
public interface StatusListener {
    long onCheckICCard(String str, long j, String str2);

    void onCmdBatchEnd();

    void onCmdBatchStart();

    void onDoorStatusChanged(int i, boolean z);

    void onDownMachineDisconnect();

    void onDownMachineFindPeopleIn();

    void onDownReportSetCmd();

    void onDownSyn();

    void onFreeTime(long j);

    void onGpsUpdatedFormServer(double d, double d2, String str);

    void onICCardPay(int i, String str, String str2);

    void onMessage(String str, String str2);

    void onNeedCheckOfferDeviceStatus();

    void onNeedClearUpdateAppFolder();

    void onNeedDriverShelf(int i);

    void onNeedReUpdateApp(String str);

    void onNeedRebootSystem(String str);

    void onNeedRestart(String str);

    void onPosMachineStateChanged(boolean z);

    void onReceivePosInfo(String str);

    void onReset();

    void onResetFinished(boolean z);

    void onScanorConnectChanged(boolean z);

    void onShelvesReseted(List<Integer> list);

    void onShjAlarm(int i, int i2);

    void onShjStatusChanged(VMCStatus vMCStatus);

    void onTransfDataAck(int i, byte[] bArr);

    void onUpdateHumidity(int i, int i2);

    void onUpdateICCardMessage(String str, String str2);

    void onUpdateICCardMoney(long j, String str);

    void onUpdateNetStatus(boolean z, Date date);

    void onUpdateTemperature(int i, int i2);

    void onVMCStatusChanged(VMCStatus vMCStatus, VMCStatus vMCStatus2);
}
