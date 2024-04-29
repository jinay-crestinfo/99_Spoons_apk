package com.xyshj.machine.listener;

import android.content.Intent;
import android.os.Bundle;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.app.R;
import com.oysb.utils.AndroidSystem;
import com.oysb.utils.Loger;
import com.oysb.utils.io.AppUpdateHelper;
import com.oysb.utils.video.TTSManager;
import com.oysb.utils.view.BFPopView;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.biz.ShjManager;
import com.shj.biz.StatusListener;
import com.shj.biz.order.OrderPayType;
import com.shj.device.VMCStatus;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;
import java.util.Date;
import java.util.List;

/* loaded from: classes2.dex */
public class MyShjStatusListener implements StatusListener {
    public static final String ACTION_CMDBATCH_END = "ACTION_CMDBATCH_END";
    public static final String ACTION_CMDBATCH_START = "ACTION_CMDBATCH_START";
    public static final String ACTION_NEET_CHECK_OFFER_DEVICE_STATE = "ACTION_NEET_CHECK_OFFER_DEVICE_STATE";
    public static final String ACTION_SCANOR_CONNECT_STATE = "ACTION_SCANOR_CONNECT_STATE";
    public static final String ACTION_SHJ_ALARM = "ACTION_SHJ_ALARM";
    public static final String ACTION_SHJ_DOOR_STATE_CHEANGED = "ACTION_SHJ_DOOR_STATE_CHEANGED";
    public static final String ACTION_SHJ_FREE_TIME = "ACTION_SHJ_FREE_TIME";
    public static final String ACTION_SHJ_ICCARD_MESSAGE = "ACTION_SHJ_ICCARD_MESSAGE";
    public static final String ACTION_SHJ_MESSAGE = "ACTION_SHJ_MESSAGE";
    public static final String ACTION_SHJ_NEED_DRIVE_SHELF = "ACTION_SHJ_NEED_DRIVE_SHELF";
    public static final String ACTION_SHJ_TRANSF_DATA = "ACTION_SHJ_TRANSF_DATA";
    public static final String ACTION_SHJ_VMCSTATUSCHANGED = "ACTION_SHJ_VMCSTATUSCHANGED";
    public static final String ACTION_SHULD_SHOW_SETTINGVIEW = "ACTION_SHULD_SHOW_SETTINGVIEW";
    public static final String ACTION_STATUS_CHANGED = "ACTION_STATUS_CHANGED";
    public static final String ACTION_STATUS_DOWN_MACHINE_DISCONNECT = "ACTION_STATUS_DOWN_MACHINE_DISCONNECT";
    public static final String ACTION_STATUS_HUMIDITY = "ACTION_STATUS_HUMIDITY";
    public static final String ACTION_STATUS_NET = "ACTION_STATUS_NET";
    public static final String ACTION_STATUS_POSMACHINE_STATE = "ACTION_STATUS_POSMACHINE_STATE";
    public static final String ACTION_STATUS_RECIVE_POSINFO = "ACTION_STATUS_RECIVE_POSINFO";
    public static final String ACTION_STATUS_RESET = "ACTION_STATUS_RESET";
    public static final String ACTION_STATUS_RESET_FINISHED = "ACTION_STATUS_RESET_FINISHED";
    public static final String ACTION_STATUS_TEMPRATURE = "ACTION_STATUS_TEMPRATURE";
    public static final String ACTION_UPDATE_ICCARDINFO = "ACTION_UPDATE_ICCARDINFO";
    public static final String ACTION_VMC_SYN = "ACTION_VMC_SYN";
    Boolean lastNetStateIsOffline = true;
    long checkICCardResult = 0;

    @Override // com.shj.biz.StatusListener
    public void onDownMachineFindPeopleIn() {
    }

    @Override // com.shj.biz.StatusListener
    public void onGpsUpdatedFormServer(double d, double d2, String str) {
    }

    @Override // com.shj.biz.StatusListener
    public void onICCardPay(int i, String str, String str2) {
    }

    @Override // com.shj.biz.StatusListener
    public void onShelvesReseted(List<Integer> list) {
    }

    @Override // com.shj.biz.StatusListener
    public void onReceivePosInfo(String str) {
        Intent intent = new Intent(ACTION_STATUS_RECIVE_POSINFO);
        Bundle bundle = new Bundle();
        bundle.putString("info", str);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_STATUS_RECIVE_POSINFO " + bundle.toString());
    }

    @Override // com.shj.biz.StatusListener
    public void onReset() {
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_STATUS_RESET));
        TTSManager.clear();
        TTSManager.addText(ShjAppHelper.getString(R.string.lab_initing));
        Loger.writeLog("Broadcast", "ACTION_STATUS_RESET ");
    }

    @Override // com.shj.biz.StatusListener
    public void onResetFinished(boolean z) {
        Intent intent = new Intent(ACTION_STATUS_RESET_FINISHED);
        Bundle bundle = new Bundle();
        bundle.putSerializable("timeOut", Boolean.valueOf(z));
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        TTSManager.addText(ShjAppHelper.getString(R.string.lab_initfinish));
        List<Integer> shelves = Shj.getShelves();
        TTSManager.addText(ShjAppHelper.getString(R.string.lab_totalsheves, "0", Integer.valueOf(shelves.size())));
        Loger.writeLog("Broadcast", "ACTION_STATUS_RESET_FINISHED ");
        for (Integer num : shelves) {
            ShelfInfo shelfInfo = Shj.getShelfInfo(num);
            if (!shelfInfo.isStatusOK()) {
                int intValue = shelfInfo.getStatus().intValue();
                if (intValue == 1) {
                    TTSManager.addText(ShjAppHelper.getString(R.string.lab_shelferror, "0", num));
                } else if (intValue == 3) {
                    TTSManager.addText(ShjAppHelper.getString(R.string.lab_shelfblocked, "0", num));
                }
            } else {
                shelfInfo.getGoodsCount().intValue();
            }
        }
    }

    @Override // com.shj.biz.StatusListener
    public void onShjStatusChanged(VMCStatus vMCStatus) {
        Intent intent = new Intent(ACTION_STATUS_CHANGED);
        Bundle bundle = new Bundle();
        bundle.putSerializable("status", vMCStatus);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_STATUS_CHANGED " + bundle.toString());
    }

    @Override // com.shj.biz.StatusListener
    public void onUpdateTemperature(int i, int i2) {
        Intent intent = new Intent(ACTION_STATUS_TEMPRATURE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("jgh", Integer.valueOf(i));
        bundle.putSerializable("temperature", Integer.valueOf(i2));
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_STATUS_TEMPRATURE " + bundle.toString());
    }

    @Override // com.shj.biz.StatusListener
    public void onUpdateHumidity(int i, int i2) {
        Intent intent = new Intent(ACTION_STATUS_HUMIDITY);
        Bundle bundle = new Bundle();
        bundle.putSerializable("jgh", Integer.valueOf(i));
        bundle.putSerializable("Humidity", Integer.valueOf(i2));
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_STATUS_HUMIDITY " + bundle.toString());
    }

    @Override // com.shj.biz.StatusListener
    public void onUpdateNetStatus(boolean z, Date date) {
        Intent intent = new Intent(ACTION_STATUS_NET);
        Bundle bundle = new Bundle();
        bundle.putSerializable("net", Boolean.valueOf(z));
        bundle.putSerializable("time", date);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        synchronized (this.lastNetStateIsOffline) {
            if (z) {
                if (this.lastNetStateIsOffline.booleanValue()) {
                    this.lastNetStateIsOffline = false;
                    TTSManager.addText(ShjAppHelper.getString(R.string.lab_netconnected));
                }
            } else {
                this.lastNetStateIsOffline = true;
            }
        }
        Loger.writeLog("Broadcast", "ACTION_STATUS_NET " + bundle.toString());
    }

    @Override // com.shj.biz.StatusListener
    public void onPosMachineStateChanged(boolean z) {
        Loger.writeLog("Broadcast", "ACTION_STATUS_POSMACHINE_STATE ");
    }

    @Override // com.shj.biz.StatusListener
    public void onDownSyn() {
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_VMC_SYN));
    }

    @Override // com.shj.biz.StatusListener
    public void onUpdateICCardMoney(long j, String str) {
        Intent intent = new Intent(ACTION_UPDATE_ICCARDINFO);
        Bundle bundle = new Bundle();
        bundle.putLong("money", j);
        bundle.putString("message", str);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
    }

    @Override // com.shj.biz.StatusListener
    public long onCheckICCard(String str, long j, String str2) {
        this.checkICCardResult = 0L;
        return 0L;
    }

    @Override // com.shj.biz.StatusListener
    public void onUpdateICCardMessage(String str, String str2) {
        Intent intent = new Intent(ACTION_SHJ_ICCARD_MESSAGE);
        Bundle bundle = new Bundle();
        bundle.putString("card", str);
        bundle.putString("message", str2);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
    }

    @Override // com.shj.biz.StatusListener
    public void onCmdBatchStart() {
        Loger.writeLog("Broadcast", "ACTION_CMDBATCH_START ");
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_CMDBATCH_START));
    }

    @Override // com.shj.biz.StatusListener
    public void onCmdBatchEnd() {
        Loger.writeLog("Broadcast", "ACTION_CMDBATCH_END ");
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_CMDBATCH_END));
    }

    @Override // com.shj.biz.StatusListener
    public void onDownReportSetCmd() {
        Loger.writeLog("Broadcast", ACTION_SHULD_SHOW_SETTINGVIEW);
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_SHULD_SHOW_SETTINGVIEW));
    }

    @Override // com.shj.biz.StatusListener
    public void onDownMachineDisconnect() {
        Loger.writeLog("Broadcast", ACTION_STATUS_DOWN_MACHINE_DISCONNECT);
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_STATUS_DOWN_MACHINE_DISCONNECT));
    }

    @Override // com.shj.biz.StatusListener
    public void onNeedRestart(String str) {
        AndroidSystem.restartApp(str, 0);
    }

    @Override // com.shj.biz.StatusListener
    public void onNeedRebootSystem(String str) {
        AndroidSystem.rebootSystem(str, 0);
    }

    @Override // com.shj.biz.StatusListener
    public void onFreeTime(long j) {
        Intent intent = new Intent(ACTION_SHJ_FREE_TIME);
        Bundle bundle = new Bundle();
        bundle.putLong("freeTime", j);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
    }

    @Override // com.shj.biz.StatusListener
    public void onNeedClearUpdateAppFolder() {
        AppUpdateHelper.clearAppPackages(ShjAppBase.sysApp, ShjAppHelper.getPackageName(), ShjAppHelper.getAppFolder() + "/update");
    }

    @Override // com.shj.biz.StatusListener
    public void onNeedDriverShelf(int i) {
        Loger.writeLog("Broadcast", ACTION_SHJ_NEED_DRIVE_SHELF);
        Intent intent = new Intent(ACTION_SHJ_NEED_DRIVE_SHELF);
        Bundle bundle = new Bundle();
        bundle.putInt(ShjDbHelper.COLUM_shelf, i);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
    }

    @Override // com.shj.biz.StatusListener
    public void onShjAlarm(int i, int i2) {
        Loger.writeLog("Broadcast", ACTION_SHJ_ALARM);
        Intent intent = new Intent(ACTION_SHJ_ALARM);
        Bundle bundle = new Bundle();
        bundle.putInt("jgh", i);
        bundle.putInt("state", i2);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
    }

    @Override // com.shj.biz.StatusListener
    public void onDoorStatusChanged(int i, boolean z) {
        Loger.writeLog("Broadcast", ACTION_SHJ_DOOR_STATE_CHEANGED);
        Intent intent = new Intent(ACTION_SHJ_DOOR_STATE_CHEANGED);
        Bundle bundle = new Bundle();
        bundle.putInt("jgh", i);
        bundle.putBoolean("state", z);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
    }

    @Override // com.shj.biz.StatusListener
    public void onScanorConnectChanged(boolean z) {
        Intent intent = new Intent(ACTION_SCANOR_CONNECT_STATE);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isConnect", z);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
    }

    @Override // com.shj.biz.StatusListener
    public void onVMCStatusChanged(VMCStatus vMCStatus, VMCStatus vMCStatus2) {
        if (Shj.getVersion() == 2) {
            Intent intent = new Intent(ACTION_SHJ_VMCSTATUSCHANGED);
            Bundle bundle = new Bundle();
            if (vMCStatus2 == VMCStatus.Normal) {
                bundle.putInt("status", 0);
                Loger.writeLog("UI;SALES", "结束除冰操作");
            } else {
                bundle.putInt("status", 1);
                Loger.writeLog("UI;SALES", "开始除冰操作");
            }
            intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
            ShjAppBase.sysApp.sendBroadcast(intent);
        }
    }

    @Override // com.shj.biz.StatusListener
    public void onNeedCheckOfferDeviceStatus() {
        Intent intent = new Intent(ACTION_NEET_CHECK_OFFER_DEVICE_STATE);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, new Bundle());
        ShjAppBase.sysApp.sendBroadcast(intent);
    }

    @Override // com.shj.biz.StatusListener
    public void onMessage(String str, String str2) {
        BFPopView findPopView;
        Intent intent = new Intent(ACTION_SHJ_MESSAGE);
        Bundle bundle = new Bundle();
        bundle.putString("key", str);
        bundle.putString("message", str2);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog(str, str2);
        if (str.equalsIgnoreCase("SCANOR") && ShjManager.getOrderPayTypes().contains(OrderPayType.ICCard)) {
            if (ShjManager.isQueryingICCardInfo()) {
                Shj.onNeedICCardPay(2, str2 + "_FKM");
                return;
            }
            boolean z = false;
            BFPopView findPopView2 = BFPopView.findPopView("MainFragment", "GoodsDetailPopView");
            if (findPopView2 != null && findPopView2.isShowing()) {
                Shj.onNeedICCardPay(1, str2 + "_FKM");
                z = true;
            }
            if (z || (findPopView = BFPopView.findPopView("MainFragment", "PayPopView")) == null || !findPopView.isShowing()) {
                return;
            }
            Shj.onNeedICCardPay(1, str2 + "_FKM");
        }
    }

    @Override // com.shj.biz.StatusListener
    public void onTransfDataAck(int i, byte[] bArr) {
        Intent intent = new Intent(ACTION_SHJ_TRANSF_DATA);
        Bundle bundle = new Bundle();
        bundle.putInt("code", i);
        bundle.putByteArray(SpeechEvent.KEY_EVENT_RECORD_DATA, bArr);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
    }

    @Override // com.shj.biz.StatusListener
    public void onNeedReUpdateApp(String str) {
        ShjAppHelper.startUpdateAppService();
    }
}
