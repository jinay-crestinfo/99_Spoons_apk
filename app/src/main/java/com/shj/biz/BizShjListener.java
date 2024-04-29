package com.shj.biz;

import android.os.Build;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.date.DateUtil;
import com.oysb.utils.video.TTSManager;
import com.oysb.xy.i.EmptyCmdListener;
import com.oysb.xy.net.NetManager;
import com.oysb.xy.net.report.Report_EmptyCmd;
import com.oysb.xy.net.report.Report_Set_Settinginfo;
import com.shj.MoneyType;
import com.shj.OfferState;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.ShjListener;
import com.shj.biz.goods.BatchOfferGoodsInfo;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderArgs;
import com.shj.biz.order.OrderManager;
import com.shj.biz.order.OrderPayType;
import com.shj.command.Command;
import com.shj.commandV2.CommandV2_Down_CommandAnswer;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.device.VMCStatus;
import com.tencent.wxpayface.WxfacePayCommonCode;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class BizShjListener implements ShjListener {
    private static boolean inUpdateTimeReboot = false;
    static int lastShelf = -1;
    static int lastStatus = -1;
    private boolean goodsOffered = false;
    private long offerStartTime = 0;
    private long cmdProcTime = 0;

    @Override // com.shj.ShjListener
    public void _onDriverShelfBlocked(int i) {
    }

    @Override // com.shj.ShjListener
    public void _onDriverShelfError(int i) {
    }

    @Override // com.shj.ShjListener
    public void _onDriverShelfSuccess(int i) {
    }

    @Override // com.shj.ShjListener
    public void _onWriteCommand(Command command) {
    }

    public void onJCGOrder_PICK(String str, List<Integer> list) {
    }

    @Override // com.shj.ShjListener
    public void _onReset() {
        ShjManager.getGoodsManager()._onReset();
        ShjManager.getStatusListener().onReset();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v12, types: [java.util.List] */
    @Override // com.shj.ShjListener
    public void _onResetFinished(boolean z) {
        if (Shj.isDebug()) {
            ArrayList arrayList = null;
            try {
                arrayList = (List) CacheHelper.getFileCache().getAsObject("Debug:Blocks");
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ShelfInfo shelfInfo = Shj.getShelfInfo((Integer) it.next());
                if (shelfInfo != null) {
                    shelfInfo.setStatus(3);
                }
            }
        }
        ShjManager.getGoodsManager().getLateastUpdatedGoods(true);
        if (Shj.getMachineId().length() >= 10 && !Shj.getMachineId().startsWith("00")) {
            DataSynchronous.setReady(true);
        }
        DataSynchronous.report_con_clientVersion();
        DataSynchronous.report_con_android_info();
        DataSynchronous.report_con_apk_info();
        DataSynchronous.report_con_paytypes();
        DataSynchronous.report_con_shelves();
        Object data = ShjManager.getData("SOCKETENABLE");
        if (data != null && data.toString().equals("FALSE")) {
            ShjManager.initTasksAfterShjRestFinished();
            ShjManager.getStatusListener().onResetFinished(z);
        } else {
            Report_EmptyCmd report_EmptyCmd = new Report_EmptyCmd();
            report_EmptyCmd.setKey("onResetFinished", new EmptyCmdListener() { // from class: com.shj.biz.BizShjListener.1
                final /* synthetic */ boolean val$timeOut;

                AnonymousClass1(boolean z2) {
                    z = z2;
                }

                @Override // com.oysb.xy.i.EmptyCmdListener
                public void onEmptyCmdFinished(String str) {
                    ShjManager.initTasksAfterShjRestFinished();
                    ShjManager.getStatusListener().onResetFinished(z);
                }
            });
            NetManager.appendReport(report_EmptyCmd);
        }
        ShjManager.unSelectGoodsOnShelf();
        try {
            Loger.writeLog("SHJ", "本次为安卓系统重新后启动APP：" + ShjManager.isRunOnBoot());
            if (ShjManager.isRunOnBoot()) {
                Integer num = (Integer) CacheHelper.getFileCache().getAsObject("MONEY");
                if (num == null) {
                    num = 0;
                }
                _onResetMoneyInCatch(num.intValue());
                CacheHelper.getFileCache().put("MONEY", (Serializable) 0);
            }
        } catch (Exception e2) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
        }
    }

    /* renamed from: com.shj.biz.BizShjListener$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements EmptyCmdListener {
        final /* synthetic */ boolean val$timeOut;

        AnonymousClass1(boolean z2) {
            z = z2;
        }

        @Override // com.oysb.xy.i.EmptyCmdListener
        public void onEmptyCmdFinished(String str) {
            ShjManager.initTasksAfterShjRestFinished();
            ShjManager.getStatusListener().onResetFinished(z);
        }
    }

    @Override // com.shj.ShjListener
    public void _onResetShjGoods(List<String> list) {
        ShjManager.getGoodsManager()._onResetShjGoods(list);
    }

    @Override // com.shj.ShjListener
    public void _onUpdateShelfGoodsCount(Integer num, int i) {
        if (!Shj.isReseting() && !ShjManager.isBatchOfferingGoods()) {
            DataSynchronous.report_status_shelf(num.intValue());
        }
        if (Shj.isResetFinished()) {
            ShjManager.getGoodsStatusListener().onUpdateShelfGoodsCount(num, i);
        }
    }

    @Override // com.shj.ShjListener
    public void _onUpdateGoodsCount(String str, int i) {
        if (Shj.isResetFinished()) {
            Loger.writeLog("UI", "----------_onUpdateGoodsCount goodsCode" + str + " count" + i);
            ShjManager.getGoodsManager()._onUpdateGoodsCount(str, i);
        }
    }

    @Override // com.shj.ShjListener
    public void _onUpdateShelfGoods(Integer num, String str, String str2, String str3) {
        if (!Shj.isReseting()) {
            DataSynchronous.report_set_shelfGoodsCode(num.intValue());
        }
        ShjManager.getGoodsManager()._onUpdateShelfGoods(num, str, str2, str3);
    }

    @Override // com.shj.ShjListener
    public void _onUpdateShelfInventory(Integer num, Integer num2) {
        if (Shj.isReseting()) {
            return;
        }
        DataSynchronous.report_set_shelfCapacity(num.intValue());
    }

    @Override // com.shj.ShjListener
    public void _onUpdateGoodsPrice(Integer num, String str, Integer num2) {
        if (!Shj.isReseting()) {
            DataSynchronous.report_set_shelfPrice(num.intValue());
        }
        ShjManager.getGoodsManager()._onUpdateGoodsPrice(num, str, num2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v13, types: [java.util.List] */
    @Override // com.shj.ShjListener
    public void _onUpdateShelfStatus(int i, int i2) {
        if (Shj.isDebug()) {
            ArrayList arrayList = null;
            try {
                arrayList = (List) CacheHelper.getFileCache().getAsObject("Debug:Blocks");
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            if (i2 == 3 || i2 == 4) {
                if (!arrayList.contains(Integer.valueOf(i))) {
                    arrayList.add(Integer.valueOf(i));
                }
            } else if (i2 == 0) {
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    if (((Integer) arrayList.get(size)).intValue() == i) {
                        arrayList.remove(size);
                    }
                }
            }
            CacheHelper.getFileCache().put("Debug:Blocks", arrayList);
        }
        if (!Shj.isReseting()) {
            DataSynchronous.report_status_shelf(i);
        }
        ShjManager.getGoodsManager()._onUpdateShelfGoodsState(i, i2);
    }

    @Override // com.shj.ShjListener
    public void _onSelectGoodsOnShelf(Integer num) {
        ShjManager.updateLastTouchTime();
        if (ShjManager.isIsShjLocked()) {
            ShjManager.unSelectGoodsOnShelf();
        } else {
            ShjManager.getGoodsManager()._onSelectGoodsOnShelf(num);
            ShjManager.getOrderManager()._onSelectGoodsOnShelf(num);
        }
    }

    @Override // com.shj.ShjListener
    public void _onDeselectGoodsOnShelf(Integer num) {
        ShjManager.getGoodsManager()._onDeselectGoodsOnShelf(num);
        ShjManager.getOrderManager()._onDeselectGoodsOnShelf(num);
    }

    @Override // com.shj.ShjListener
    public void _onOfferingGoods(int i, int i2) {
        try {
            Object data = ShjManager.getData("XY_ICCARD_PAY");
            if ((data == null || !data.toString().equalsIgnoreCase("FALSE")) && ShjManager.getOrderPayTypes().contains(OrderPayType.ICCard)) {
                ShjManager.queryICCardInfo(false, 0);
            }
        } catch (Exception unused) {
        }
        ShjManager.getGoodsManager()._onOfferingGoods(i2);
        if (ShjManager.isBatchOfferingGoods()) {
            return;
        }
        ShjManager.getOrderManager().cancelPay();
    }

    private void checkShjMoney(OrderPayType orderPayType) {
        Loger.writeLog("SHJ", "payType:" + orderPayType.toString());
        if (orderPayType == null || orderPayType == OrderPayType.CASH || ShjManager.getBatchOfferGoodsInfo() == null || !ShjManager.getBatchOfferGoodsInfo().isBatchOfferFinished()) {
            return;
        }
        if (Shj.getVersion() == 1) {
            ShjManager.setMoney(MoneyType.Reset, 0, "eat");
        } else {
            ShjManager.setMoney(MoneyType.EAT, 0, "eat");
        }
    }

    public void _onPickCodeChecked(String str, boolean z) {
        ShjManager.getOrderListener().onPickCodeChecked(str, z);
    }

    public void _onPickCodeOfferGoodsError(String str, String str2) {
        ShjManager.getOrderListener().onPickCodeOfferGoodsError(str, str2);
    }

    public void _onPickCodeOfferGoodsFinished(String str) {
        ShjManager.getOrderListener().onPickCodeOfferGoodsFinished(str);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(22:1|(1:3)(1:114)|4|(1:6)|7|(1:9)|10|(1:12)(1:113)|13|(1:14)|(1:16)(9:80|81|(7:93|94|95|96|97|98|99)(2:83|(4:86|87|88|89)(1:85))|36|(3:38|(1:72)(1:42)|43)(1:73)|44|(1:46)|47|(7:49|50|(1:67)|54|(2:55|(2:57|(2:60|61)(1:59))(1:66))|62|64)(1:71))|17|18|(4:20|(6:22|23|24|(1:26)(1:74)|27|(1:31))(1:76)|32|(1:34))(1:77)|35|36|(0)(0)|44|(0)|47|(0)(0)|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0279, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:38:0x02c0  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x02f9  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0303 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x02ec  */
    /* JADX WARN: Type inference failed for: r7v3, types: [org.json.JSONArray] */
    /* JADX WARN: Type inference failed for: r9v17 */
    /* JADX WARN: Type inference failed for: r9v18 */
    /* JADX WARN: Type inference failed for: r9v5, types: [int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void _onOfferGoods(int r23, int r24, int r25) {
        /*
            Method dump skipped, instructions count: 896
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.BizShjListener._onOfferGoods(int, int, int):void");
    }

    @Override // com.shj.ShjListener
    public void _onOfferGoodsNone(int i, int i2) {
        _onOfferGoods(i, 0, i2);
    }

    @Override // com.shj.ShjListener
    public void _onOfferGoodsSuccess(int i, int i2) {
        _onOfferGoods(i, OfferState.OfferSuccess.getIndex(), i2);
    }

    @Override // com.shj.ShjListener
    public void _onOfferGoodsBlocked(int i, int i2, int i3) {
        _onOfferGoods(i, i2, i3);
    }

    @Override // com.shj.ShjListener
    public void _onShelfBlockCleared(int i) {
        DataSynchronous.report_status_shelf(i);
    }

    @Override // com.shj.ShjListener
    public void _onReciveMoney(MoneyType moneyType, int i) {
        ShjManager.getMoneyListener().onReciveMoney(moneyType, i);
        DataSynchronous.report_trad_money_accept();
    }

    @Override // com.shj.ShjListener
    public void _onMoneyChanged() {
        ShjManager.updateLastTouchTime();
        Loger.writeLog("SHJ", "_onMoneyChanged ");
        ShjManager.getMoneyListener().onMoneyChanged();
        if (Shj.getWallet().getCatchMoney().intValue() == 0 && this.goodsOffered) {
            ShjManager.getOrderManager().addTradSn();
        }
        this.goodsOffered = false;
        CacheHelper.getFileCache().put("MONEY", Shj.getWallet().getCatchMoney());
    }

    @Override // com.shj.ShjListener
    public void _onUpdateCoinBalance(int i) {
        DataSynchronous.report_status_chargMoneyBalance();
    }

    @Override // com.shj.ShjListener
    public void _onUpdatePaperMoneyBalance(int i) {
        DataSynchronous.report_status_chargMoneyBalance();
    }

    public void _onChargStart() {
        ShjManager.getMoneyListener().onChargStart(Shj.getWallet().getCatchMoney().intValue());
    }

    @Override // com.shj.ShjListener
    public void _onCharging() {
        ShjManager.getMoneyListener().onCharging();
    }

    @Override // com.shj.ShjListener
    public void _onChangeFinished(int i, int i2) {
        this.goodsOffered = false;
        ShjManager.getMoneyListener().onChargFinished(i, i2);
        if (i + i2 > 0) {
            DataSynchronous.report_trad_money_change();
            if (Shj.getWallet().getCatchMoney().intValue() == 0) {
                ShjManager.getOrderManager().addTradSn();
            }
        }
    }

    @Override // com.shj.ShjListener
    public void _onCoinBalanceChanged(int i) {
        ShjManager.getMoneyListener().onCoinBalanceChanged(i);
    }

    @Override // com.shj.ShjListener
    public void _onPaperBalanceChanged(int i) {
        ShjManager.getMoneyListener().onPaperBalanceChanged(i);
    }

    @Override // com.shj.ShjListener
    public void _onResetMoneyInCatch(int i) {
        this.goodsOffered = false;
        if (i > 0) {
            DataSynchronous.report_status_resetMoney(i);
            ShjManager.getOrderManager().addTradSn();
        }
    }

    @Override // com.shj.ShjListener
    public void _onNeedDriverShelf(int i) {
        ShjManager.getStatusListener().onNeedDriverShelf(i);
    }

    @Override // com.shj.ShjListener
    public void _onNeedCheckOfferDeviceStatus() {
        ShjManager.getStatusListener().onNeedCheckOfferDeviceStatus();
    }

    @Override // com.shj.ShjListener
    public void _onVMCStatusChanged(VMCStatus vMCStatus, VMCStatus vMCStatus2) {
        if (Shj.getVersion() > 1) {
            ShjManager.getStatusListener().onVMCStatusChanged(vMCStatus, vMCStatus2);
        }
    }

    @Override // com.shj.ShjListener
    public void _onReceivePosInfo(String str) {
        if (Shj.isResetFinished()) {
            ShjManager.getStatusListener().onReceivePosInfo(str);
        }
    }

    @Override // com.shj.ShjListener
    public void _onDeviceMessage(String str, String str2) {
        if (Shj.isResetFinished()) {
            ShjManager.getStatusListener().onMessage(str, str2);
        }
    }

    @Override // com.shj.ShjListener
    public void _onDoorStatusChanged(int i, boolean z) {
        DataSynchronous.report_status_door(i);
        if (Shj.isResetFinished()) {
            ShjManager.getStatusListener().onDoorStatusChanged(i, z);
        }
    }

    @Override // com.shj.ShjListener
    public void _onUpdateTemperature(int i, int i2) {
        if (Shj.isResetFinished()) {
            ShjManager.getStatusListener().onUpdateTemperature(i, i2);
        }
    }

    @Override // com.shj.ShjListener
    public void _onUpdateHumidity(int i, int i2) {
        if (Shj.isResetFinished()) {
            ShjManager.getStatusListener().onUpdateHumidity(i, i2);
        }
    }

    public void _onUpdateNetStatus(boolean z, Date date) {
        if (z) {
            try {
                Loger.writeLog("UI", "time=" + date.toString());
                CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand.setTime(true, date);
                Shj.postSetCommand(commandV2_Up_SetCommand, null);
                if (date.getTime() - System.currentTimeMillis() > 300000 || date.getTime() - System.currentTimeMillis() < -300000) {
                    AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_AppStatus, "", "安卓时间错误,后台时间:" + DateUtil.format(date, "yyyyMMddHHmmss") + " 系统时间:" + DateUtil.format(new Date(), "yyyyMMddHHmmss"));
                    try {
                        if (Build.VERSION.SDK_INT >= 23) {
                            String format = new SimpleDateFormat("MMddHHmmyyyy.ss").format(date);
                            if (Build.VERSION.SDK_INT >= 24) {
                                CommonTool.execShellCmd("date " + format + "\n hwclock -w\n");
                            } else {
                                CommonTool.execShellCmd("date " + format + "\n busybox hwclock -w\n");
                            }
                        } else {
                            CommonTool.execShellCmd("date -s" + new SimpleDateFormat("yyyyMMdd.HHmmss").format(date) + "\n clock  -w\n");
                        }
                    } catch (Exception unused) {
                    }
                }
                if (Math.abs(System.currentTimeMillis() - date.getTime()) > 1000) {
                    Shj.setAndroidSystemTimeError(true);
                } else {
                    Shj.setAndroidSystemTimeError(false);
                }
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
        }
        ShjManager.getStatusListener().onUpdateNetStatus(z, date);
    }

    public void _onPosMachineStateChanged(boolean z) {
        ShjManager.getStatusListener().onPosMachineStateChanged(z);
    }

    @Override // com.shj.ShjListener
    public void _onDownSyn() {
        try {
            Integer num = (Integer) CacheHelper.getFileCache().getAsObject("MONEY");
            if (num == null) {
                num = 0;
            }
            _onResetMoneyInCatch(num.intValue());
            CacheHelper.getFileCache().put("MONEY", (Serializable) 0);
        } catch (Exception e) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
        }
        ShjManager.getStatusListener().onDownSyn();
    }

    @Override // com.shj.ShjListener
    public void _onUpdateICCardMoney(long j, String str) {
        ShjManager.getStatusListener().onUpdateICCardMoney(j, str);
    }

    public long _onCheckICCard(String str, long j, String str2) {
        return ShjManager.getStatusListener().onCheckICCard(str, j, str2);
    }

    public void _onUpdateICCardMessage(String str, String str2) {
        ShjManager.getStatusListener().onUpdateICCardMessage(str, str2);
    }

    @Override // com.shj.ShjListener
    public void _onNeedICCardPay(int i, String str, String str2) {
        String str3;
        boolean z;
        if (NetManager.isLogined()) {
            ShjManager.getStatusListener().onICCardPay(i, str, str2);
            Order order = null;
            if (ShjManager.getOrderManager().hasOrder() && ShjManager.getOrderManager().orderIsReady()) {
                String obj = ShjManager.getData("tmp_lastOrderUid") == null ? "" : ShjManager.getData("tmp_lastOrderUid").toString();
                order = ShjManager.getOrderManager().getResentOrder(1, null);
                str3 = order.getUid();
                z = !obj.equals(str3);
                ShjManager.putData("tmp_lastOrderUid", str3);
            } else {
                ShjManager.putData("tmp_lastOrderUid", Order.nextUid());
                str3 = "0";
                z = true;
            }
            Object data = ShjManager.getData("XY_ICCARD_PAY");
            if (data == null || !data.toString().equalsIgnoreCase("FALSE")) {
                if (i == 1 && order == null) {
                    z = false;
                }
                if (z) {
                    String[] split = str.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                    String str4 = split[0];
                    String str5 = split.length > 1 ? split[1] : "0";
                    ShjManager.setting.put("online_pay_card", str);
                    DataSynchronous.report_transf_iccardpay_v2(i, str4, str5, str3, order);
                }
            }
        }
    }

    @Override // com.shj.ShjListener
    public void _onBatchStart() {
        ShjManager.getStatusListener().onCmdBatchStart();
    }

    @Override // com.shj.ShjListener
    public void _onBatchEnd() {
        ShjManager.getStatusListener().onCmdBatchEnd();
        if (ShjManager.getGoodsManager().getLateastUpdatedGoods(false).size() > 0) {
            ShjManager.getGoodsStatusListener().onGoodsUpdated();
        }
    }

    public void _onGpsUpdatedFormServer(double d, double d2, String str) {
        ShjManager.getStatusListener().onGpsUpdatedFormServer(d, d2, str);
    }

    public void _onNeedRestart(String str) {
        ShjManager.getStatusListener().onNeedRestart(str);
    }

    public void _onNeedRebootSystem(String str) {
        ShjManager.getStatusListener().onNeedRebootSystem(str);
    }

    @Override // com.shj.ShjListener
    public void _onDownReportSetCmd() {
        ShjManager.getStatusListener().onDownReportSetCmd();
    }

    public void _onFreeTime(long j) {
        ShjManager.getStatusListener().onFreeTime(j);
    }

    public void _onBatchOfferGoods_Finished(BatchOfferGoodsInfo batchOfferGoodsInfo, String str) {
        ShjManager.getGoodsStatusListener().onBatchOfferGoods_Finished(batchOfferGoodsInfo, str, 1);
    }

    public void _onServerMessage(String str) {
        ShjManager.getStatusListener().onMessage("SERVER", str);
        try {
            ShjPushMessageManager.onPushMessage(str);
        } catch (Exception unused) {
        }
    }

    public void onServerOfferGoodsCmdStateTimeOut(int i) {
        if (i > 0) {
            ShjManager.tryOfferNextYCCmdGoods();
        }
    }

    public void onServerOfferGoodsCmd(int i, String str) {
        String str2;
        String str3;
        boolean z;
        Loger.writeLog("SALES;NET", "state:" + i + " 出货任务：" + str);
        String str4 = "";
        try {
            if (i == 0) {
                String substring = str.startsWith(Marker.ANY_MARKER) ? str.substring(1) : str;
                String[] split = substring.split("\\*");
                if (ShjManager.isYCCHOfferingGoods()) {
                    ShjManager.getOfferCmdQueue().add(substring);
                    return;
                }
                ShjManager.setYCCHOfferingGoods(true);
                if (split.length <= 4) {
                    str3 = ShjManager.STR_offerGoods_failed;
                } else if (split[3].endsWith(VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                    str3 = ShjManager.STR_offerGoods_failed;
                    split[3] = split[3] + split[4];
                } else {
                    StringBuilder sb = new StringBuilder();
                    str3 = ShjManager.STR_offerGoods_failed;
                    sb.append(split[3]);
                    sb.append(VoiceWakeuperAidl.PARAMS_SEPARATE);
                    sb.append(split[4]);
                    split[3] = sb.toString();
                }
                String[] split2 = split[3].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                ShjManager.putData("lastOfferCmd_OrderId", split2[0]);
                ShjManager.putData("lastOfferCmd_payType", split2[1]);
                ShjManager.putData("lastOfferCmd_offerTaskId", split2[2]);
                ShjManager.putData("lastOfferCmd_lockCountBS", split2[3]);
                if (split2.length >= 5) {
                    ShjManager.putData("lastOfferCmd_justOfferGoods", split2[4]);
                    z = split2[4].equalsIgnoreCase("1");
                } else {
                    z = false;
                }
                if (split2.length >= 6) {
                    ShjManager.putData("lastOfferCmd_UUID", split2[5].substring(1));
                }
                ShjManager.putData("lastOfferCmd_shelf", split[0]);
                ShjManager.putData("lastOfferCmd_goodsid", split[1]);
                ShjManager.putData("lastOfferCmd_ly", split[2]);
                ShjManager.putData("lastOfferCmd_key", split[3]);
                this.offerStartTime = System.currentTimeMillis();
                if (split2[2].equalsIgnoreCase("yg")) {
                    YGOrderHelper.YG_OrderTask_NewOrder();
                    ShjManager.tryOfferNextYCCmdGoods();
                    return;
                }
                if (split2[2].equalsIgnoreCase("hdg")) {
                    HdgOrderHelper.HDG_OrderTask();
                    ShjManager.tryOfferNextYCCmdGoods();
                    return;
                }
                if (ShjManager.getOrderManager().getResentOrder(3, split2[0]) == ShjManager.getOrderManager().getResentOrder(1, null)) {
                    ShjManager.getOrderListener().onPaySuccess(ShjManager.getOrderManager().getResentOrder(1, null), OrderManager.serverType2OrderPayType(Integer.parseInt(split2[1])));
                }
                String[] split3 = split[0].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                if (Shj.getOfferGoodsDiviceState() == 0) {
                    ShjManager.getGoodsStatusListener().onOfferingGoods_State(Integer.parseInt(split3[0]), 0, ShjManager.getData(ShjManager.STR_ready2OfferGoods).toString(), 1);
                    if (z) {
                        Loger.writeLog("SALES;NET", "直接出货");
                        onServerOfferGoodsCmd(4, "0");
                    } else {
                        DataSynchronous.report_transf_offerGoodsCmd(split[2], 4, split[3], "0");
                    }
                } else if (split3.length == 0) {
                    DataSynchronous.report_transf_offerGoodsCmd(split[2], 9, split[3], "" + Shj.getOfferGoodsDiviceState());
                    ShjManager.getGoodsStatusListener().onOfferingGoods_State(Integer.parseInt(split3[0]), OfferState.BusinessStoped.getIndex(), ShjManager.getData(str3) + Shj.getOfferGoodsDiviceStateInfo(), 1);
                    ShjManager.getOrderListener().onOrderMessage(Shj.getOfferGoodsDiviceStateInfo());
                    TTSManager.addText(ShjManager.getData(str3) + Shj.getOfferGoodsDiviceStateInfo());
                } else {
                    DataSynchronous.report_transf_offerGoodsCmd(split[2], 4, split[3], "0");
                }
                return;
            }
            String obj = ShjManager.getData("lastOfferCmd_OrderId").toString();
            String obj2 = ShjManager.getData("lastOfferCmd_payType").toString();
            String obj3 = ShjManager.getData("lastOfferCmd_shelf").toString();
            String obj4 = ShjManager.getData("lastOfferCmd_goodsid").toString();
            if (obj4.endsWith(VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                try {
                    obj4.substring(0, obj4.length() - 1);
                } catch (Exception e) {
                    e = e;
                    Loger.writeException("SALES;NET", e);
                }
            }
            String obj5 = ShjManager.getData("lastOfferCmd_ly").toString();
            String obj6 = ShjManager.getData("lastOfferCmd_key").toString();
            if (ShjManager.getData("lastOfferCmd_offerTaskId") != null) {
                ShjManager.getData("lastOfferCmd_offerTaskId").toString();
            }
            if (ShjManager.getData("lastOfferCmd_lockCountBS") != null) {
                ShjManager.getData("lastOfferCmd_lockCountBS").toString();
            }
            if (ShjManager.getData("lastOfferCmd_justOfferGoods") != null) {
                ShjManager.getData("lastOfferCmd_justOfferGoods").toString();
            }
            if (ShjManager.getData("lastOfferCmd_UUID") != null) {
                ShjManager.getData("lastOfferCmd_UUID").toString();
            }
            if (i != 1 && i == 4) {
                try {
                    if (str.startsWith("0")) {
                        ShjManager.putData("offering_Order_key", obj);
                        new ArrayList();
                        Shj.getWallet().setLastAddMoneyInfo(obj);
                        Order resentOrder = ShjManager.getOrderManager().getResentOrder(2, null);
                        Loger.writeLog("SALES;NET", "order:" + resentOrder);
                        if (resentOrder != null) {
                            resentOrder.setPayId(obj);
                        }
                        OrderPayType serverType2OrderPayType = OrderManager.serverType2OrderPayType(Integer.parseInt(obj2));
                        MoneyType payType2MoneyType = OrderManager.payType2MoneyType(serverType2OrderPayType);
                        Loger.writeLog("SALES;NET", "moneyType:" + payType2MoneyType);
                        OrderArgs orderArgs = resentOrder == null ? new OrderArgs() : resentOrder.getArgs();
                        orderArgs.putArg("tradeNo", obj);
                        orderArgs.putArg("YC_CMD_OfferGoods", "TRUE");
                        orderArgs.putArg("payId", "" + obj);
                        orderArgs.putArg("orderId", "" + obj);
                        int parseInt = obj5.startsWith("PICKDOOR:") ? Integer.parseInt(obj5.split(":")[1]) : -1;
                        Loger.writeLog("SALES;NET", "shelf:" + obj3);
                        if (!obj3.equals("0")) {
                            String[] split4 = obj3.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                            if (split4.length == 1) {
                                int parseInt2 = Integer.parseInt(split4[0]);
                                ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(parseInt2));
                                if (shelfInfo != null && shelfInfo.isStatusOK() && shelfInfo.getGoodsCount().intValue() > 0) {
                                    Loger.writeLog("SALES;NET", "updateArgs");
                                    long currentTimeMillis = System.currentTimeMillis();
                                    this.cmdProcTime = currentTimeMillis - this.offerStartTime;
                                    this.offerStartTime = currentTimeMillis;
                                    orderArgs.autoSelect(false);
                                    orderArgs.selectByGoodsCode(false);
                                    orderArgs.setShelf(parseInt2);
                                    if (-1 != parseInt) {
                                        orderArgs.putArg("PICKDOOR", "" + parseInt);
                                    }
                                    ShjManager.getOrderManager().submitDriverShelfOrder(serverType2OrderPayType, orderArgs, resentOrder, 0, obj);
                                    Loger.writeLog("SALES;NET", "货道号:" + parseInt2 + "已驱动出货");
                                    return;
                                }
                                Loger.writeLog("SALES;NET", shelfInfo == null ? "货道不存在" : "货道状态:" + shelfInfo.getStatus() + " 库存:" + shelfInfo.getGoodsCount() + " 出货设备状态:" + Shj.getOfferGoodsDiviceStateInfo());
                                DataSynchronous.report_transf_offerGoodsCmd(obj5, 3, obj6, String.format("%03d", Integer.valueOf(parseInt2)) + ":1");
                                ShjManager.getGoodsStatusListener().onOfferingGoods_State(Integer.parseInt(split4[0]), OfferState.BusinessStoped.getIndex(), ShjManager.getData(ShjManager.STR_offerGoods_failed).toString(), 1);
                                TTSManager.addText(ShjManager.getData(ShjManager.STR_offerGoods_failed).toString());
                                ShjManager.tryOfferNextYCCmdGoods();
                                return;
                            }
                            Loger.writeLog("SHJ;SALES", "update offerGoodsInfo");
                            BatchOfferGoodsInfo batchOfferGoodsInfo = new BatchOfferGoodsInfo();
                            if (orderArgs != null) {
                                batchOfferGoodsInfo.setOrderArgs(orderArgs);
                            }
                            if (-1 != parseInt) {
                                str2 = obj;
                                batchOfferGoodsInfo.getOrderArgs().putArg("PICKDOOR", "" + parseInt);
                            } else {
                                str2 = obj;
                            }
                            batchOfferGoodsInfo.setRemark(resentOrder != null ? resentOrder.getPayId() : str2);
                            batchOfferGoodsInfo.setMoneyType(payType2MoneyType);
                            batchOfferGoodsInfo.setShouldSetMoney(true);
                            for (int i2 = 0; i2 < split4.length; i2++) {
                                str4 = str4 + split4[i2] + ":34" + VoiceWakeuperAidl.PARAMS_SEPARATE;
                                batchOfferGoodsInfo.addShelfItem(Integer.parseInt(split4[i2]));
                            }
                            String substring2 = str4.substring(0, str4.length() - 1);
                            Loger.writeLog("SHJ;SALES", "startBatchOfferGoods");
                            if (ShjManager.startBatchOfferGoods(batchOfferGoodsInfo, payType2MoneyType)) {
                                return;
                            }
                            Loger.writeLog("SHJ;SALES", "执行批量出货失败");
                            DataSynchronous.report_transf_offerGoodsCmd(obj5, 3, obj6, substring2);
                            ShjManager.getGoodsStatusListener().onOfferingGoods_State(Integer.parseInt(split4[0]), OfferState.BusinessStoped.getIndex(), ShjManager.getData(ShjManager.STR_offerGoods_failed).toString(), 1);
                            TTSManager.addText(ShjManager.getData(ShjManager.STR_offerGoods_failed).toString());
                            ShjManager.tryOfferNextYCCmdGoods();
                            return;
                        }
                        Loger.writeLog("SALES;NET", "shelf:" + obj4);
                        String[] split5 = obj4.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        if (split5.length == 1) {
                            ShelfInfo shelfInfo2 = Shj.getShelfInfo(Integer.valueOf(ShjManager.getGoodsManager().getNextGoodsShelf(split5[0])));
                            Loger.writeLog("SALES;NET", "shelfInfo:" + shelfInfo2.getShelf());
                            if (shelfInfo2 != null && shelfInfo2.isStatusOK() && shelfInfo2.getGoodsCount().intValue() > 0) {
                                Loger.writeLog("SALES;NET", "updateArgs");
                                long currentTimeMillis2 = System.currentTimeMillis();
                                this.cmdProcTime = currentTimeMillis2 - this.offerStartTime;
                                this.offerStartTime = currentTimeMillis2;
                                orderArgs.autoSelect(false);
                                orderArgs.selectByGoodsCode(false);
                                orderArgs.setShelf(shelfInfo2.getShelf().intValue());
                                if (-1 != parseInt) {
                                    orderArgs.putArg("PICKDOOR", "" + parseInt);
                                }
                                ShjManager.getOrderManager().submitDriverShelfOrder(serverType2OrderPayType, orderArgs, resentOrder, 0, obj);
                                Loger.writeLog("NET", "货道号:" + shelfInfo2.getShelf() + "已驱动出货");
                                return;
                            }
                            Loger.writeLog("SALES;NET", "report error");
                            DataSynchronous.report_transf_offerGoodsCmd(obj5, 3, obj6, String.format("%03d", shelfInfo2.getShelf()) + ":1");
                            ShjManager.getGoodsStatusListener().onOfferingGoods_State(shelfInfo2.getShelf().intValue(), OfferState.BusinessStoped.getIndex(), ShjManager.getData(ShjManager.STR_offerGoods_failed).toString(), 1);
                            TTSManager.addText(ShjManager.getData(ShjManager.STR_offerGoods_failed).toString());
                            ShjManager.tryOfferNextYCCmdGoods();
                            return;
                        }
                        Loger.writeLog("SALES;NET", "update offerGoodsInfo");
                        BatchOfferGoodsInfo batchOfferGoodsInfo2 = new BatchOfferGoodsInfo();
                        if (orderArgs != null) {
                            batchOfferGoodsInfo2.setOrderArgs(orderArgs);
                        }
                        if (-1 != parseInt) {
                            batchOfferGoodsInfo2.getOrderArgs().putArg("PICKDOOR", "" + parseInt);
                        }
                        batchOfferGoodsInfo2.setRemark(resentOrder != null ? resentOrder.getPayId() : obj);
                        batchOfferGoodsInfo2.setMoneyType(payType2MoneyType);
                        batchOfferGoodsInfo2.setShouldSetMoney(true);
                        for (String str5 : split5) {
                            batchOfferGoodsInfo2.addGoodsItem(str5);
                        }
                        ShjManager.startBatchOfferGoods(batchOfferGoodsInfo2, payType2MoneyType);
                        return;
                    }
                    ShjManager.tryOfferNextYCCmdGoods();
                } catch (Exception e2) {
                    e = e2;
                    Loger.writeException("SALES;NET", e);
                }
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    @Override // com.shj.ShjListener
    public void _onDownMachineDisconnect() {
        ShjManager.getStatusListener().onDownMachineDisconnect();
    }

    @Override // com.shj.ShjListener
    public void _onFindPeopleIn() {
        if (Shj.isResetFinished()) {
            ShjManager.getStatusListener().onDownMachineFindPeopleIn();
            ShjManager.updateLastFindPeopleTime();
        }
    }

    @Override // com.shj.ShjListener
    public void _onShjAlarm(int i, int i2) {
        if (Shj.isResetFinished()) {
            ShjManager.getStatusListener().onShjAlarm(i, i2);
        }
    }

    @Override // com.shj.ShjListener
    public void _onShelfBoorStateChanged(int i, int i2) {
        if (lastShelf == i && lastStatus == i2) {
            return;
        }
        lastShelf = i;
        lastStatus = i2;
        ShjManager.getGoodsStatusListener().onUpdateShelfDoorState(i, i2);
        HdgOrderHelper.HDG_OrderTask_shelfDoor_StateChanged(i, i2);
    }

    @Override // com.shj.ShjListener
    public void _onPostSetCommandAnswer(CommandV2_Down_CommandAnswer commandV2_Down_CommandAnswer) {
        try {
            CommandV2_Up_SetCommand setCommand = commandV2_Down_CommandAnswer.getSetCommand();
            if (setCommand == null || setCommand.getCmdType() == -103 || setCommand.getCmdType() == 101) {
                return;
            }
            byte[] rawCommand = setCommand.getRawCommand();
            byte[] answers = commandV2_Down_CommandAnswer.getAnswers();
            int length = rawCommand.length - 6;
            byte[] bArr = new byte[length];
            ObjectHelper.updateBytes(bArr, rawCommand, 5, 0, length);
            String hex2String = ObjectHelper.hex2String(bArr, length);
            String hex2String2 = ObjectHelper.hex2String(answers, answers.length);
            Report_Set_Settinginfo report_Set_Settinginfo = new Report_Set_Settinginfo();
            report_Set_Settinginfo.setParams("7071", hex2String + VoiceWakeuperAidl.PARAMS_SEPARATE + hex2String2, false);
            NetManager.appendReport(report_Set_Settinginfo);
        } catch (Exception unused) {
        }
    }

    @Override // com.shj.ShjListener
    public void _onOfferGoodsState(int i, String str, int i2) {
        ShelfInfo selectedShelf = Shj.getSelectedShelf();
        ShjManager.getGoodsStatusListener().onOfferingGoods_State(selectedShelf == null ? 0 : selectedShelf.getShelf().intValue(), i, str, i2);
    }
}
