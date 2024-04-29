package com.shj;

import com.shj.command.Command;
import com.shj.commandV2.CommandV2_Down_CommandAnswer;
import com.shj.device.VMCStatus;
import java.util.List;

/* loaded from: classes2.dex */
public interface ShjListener {
    void _onBatchEnd();

    void _onBatchStart();

    void _onChangeFinished(int i, int i2);

    void _onCharging();

    void _onCoinBalanceChanged(int i);

    void _onDeselectGoodsOnShelf(Integer num);

    void _onDeviceMessage(String str, String str2);

    void _onDoorStatusChanged(int i, boolean z);

    void _onDownMachineDisconnect();

    void _onDownReportSetCmd();

    void _onDownSyn();

    void _onDriverShelfBlocked(int i);

    void _onDriverShelfError(int i);

    void _onDriverShelfSuccess(int i);

    void _onFindPeopleIn();

    void _onMoneyChanged();

    void _onNeedCheckOfferDeviceStatus();

    void _onNeedDriverShelf(int i);

    void _onNeedICCardPay(int i, String str, String str2);

    void _onOfferGoodsBlocked(int i, int i2, int i3);

    void _onOfferGoodsNone(int i, int i2);

    void _onOfferGoodsState(int i, String str, int i2);

    void _onOfferGoodsSuccess(int i, int i2);

    void _onOfferingGoods(int i, int i2);

    void _onPaperBalanceChanged(int i);

    void _onPostSetCommandAnswer(CommandV2_Down_CommandAnswer commandV2_Down_CommandAnswer);

    void _onReceivePosInfo(String str);

    void _onReciveMoney(MoneyType moneyType, int i);

    void _onReset();

    void _onResetFinished(boolean z);

    void _onResetMoneyInCatch(int i);

    void _onResetShjGoods(List<String> list);

    void _onSelectGoodsOnShelf(Integer num);

    void _onShelfBlockCleared(int i);

    void _onShelfBoorStateChanged(int i, int i2);

    void _onShjAlarm(int i, int i2);

    void _onUpdateCoinBalance(int i);

    void _onUpdateGoodsCount(String str, int i);

    void _onUpdateGoodsPrice(Integer num, String str, Integer num2);

    void _onUpdateHumidity(int i, int i2);

    void _onUpdateICCardMoney(long j, String str);

    void _onUpdatePaperMoneyBalance(int i);

    void _onUpdateShelfGoods(Integer num, String str, String str2, String str3);

    void _onUpdateShelfGoodsCount(Integer num, int i);

    void _onUpdateShelfInventory(Integer num, Integer num2);

    void _onUpdateShelfStatus(int i, int i2);

    void _onUpdateTemperature(int i, int i2);

    void _onVMCStatusChanged(VMCStatus vMCStatus, VMCStatus vMCStatus2);

    void _onWriteCommand(Command command);
}
