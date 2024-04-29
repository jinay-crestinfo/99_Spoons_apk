package com.shj.setting;

import com.shj.MoneyType;
import com.shj.ShjListener;
import com.shj.biz.ShjManager;
import com.shj.command.Command;
import com.shj.commandV2.CommandV2_Down_CommandAnswer;
import com.shj.device.VMCStatus;
import com.shj.setting.event.BatchEndEvent;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* loaded from: classes2.dex */
public class SettingShjListener implements ShjListener {
    @Override // com.shj.ShjListener
    public void _onBatchStart() {
    }

    @Override // com.shj.ShjListener
    public void _onChangeFinished(int i, int i2) {
    }

    @Override // com.shj.ShjListener
    public void _onCharging() {
    }

    @Override // com.shj.ShjListener
    public void _onCoinBalanceChanged(int i) {
    }

    @Override // com.shj.ShjListener
    public void _onDeselectGoodsOnShelf(Integer num) {
    }

    @Override // com.shj.ShjListener
    public void _onDeviceMessage(String str, String str2) {
    }

    @Override // com.shj.ShjListener
    public void _onDoorStatusChanged(int i, boolean z) {
    }

    @Override // com.shj.ShjListener
    public void _onDownMachineDisconnect() {
    }

    @Override // com.shj.ShjListener
    public void _onDownReportSetCmd() {
    }

    @Override // com.shj.ShjListener
    public void _onDownSyn() {
    }

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
    public void _onFindPeopleIn() {
    }

    @Override // com.shj.ShjListener
    public void _onMoneyChanged() {
    }

    @Override // com.shj.ShjListener
    public void _onNeedCheckOfferDeviceStatus() {
    }

    @Override // com.shj.ShjListener
    public void _onNeedDriverShelf(int i) {
    }

    @Override // com.shj.ShjListener
    public void _onNeedICCardPay(int i, String str, String str2) {
    }

    @Override // com.shj.ShjListener
    public void _onOfferGoodsBlocked(int i, int i2, int i3) {
    }

    @Override // com.shj.ShjListener
    public void _onOfferGoodsNone(int i, int i2) {
    }

    @Override // com.shj.ShjListener
    public void _onOfferGoodsState(int i, String str, int i2) {
    }

    @Override // com.shj.ShjListener
    public void _onOfferGoodsSuccess(int i, int i2) {
    }

    @Override // com.shj.ShjListener
    public void _onOfferingGoods(int i, int i2) {
    }

    @Override // com.shj.ShjListener
    public void _onPaperBalanceChanged(int i) {
    }

    @Override // com.shj.ShjListener
    public void _onPostSetCommandAnswer(CommandV2_Down_CommandAnswer commandV2_Down_CommandAnswer) {
    }

    @Override // com.shj.ShjListener
    public void _onReceivePosInfo(String str) {
    }

    @Override // com.shj.ShjListener
    public void _onReciveMoney(MoneyType moneyType, int i) {
    }

    @Override // com.shj.ShjListener
    public void _onReset() {
    }

    @Override // com.shj.ShjListener
    public void _onResetFinished(boolean z) {
    }

    @Override // com.shj.ShjListener
    public void _onResetMoneyInCatch(int i) {
    }

    @Override // com.shj.ShjListener
    public void _onResetShjGoods(List<String> list) {
    }

    @Override // com.shj.ShjListener
    public void _onSelectGoodsOnShelf(Integer num) {
    }

    @Override // com.shj.ShjListener
    public void _onShelfBlockCleared(int i) {
    }

    @Override // com.shj.ShjListener
    public void _onShelfBoorStateChanged(int i, int i2) {
    }

    @Override // com.shj.ShjListener
    public void _onShjAlarm(int i, int i2) {
    }

    @Override // com.shj.ShjListener
    public void _onUpdateCoinBalance(int i) {
    }

    @Override // com.shj.ShjListener
    public void _onUpdateGoodsCount(String str, int i) {
    }

    @Override // com.shj.ShjListener
    public void _onUpdateGoodsPrice(Integer num, String str, Integer num2) {
    }

    @Override // com.shj.ShjListener
    public void _onUpdateHumidity(int i, int i2) {
    }

    @Override // com.shj.ShjListener
    public void _onUpdateICCardMoney(long j, String str) {
    }

    @Override // com.shj.ShjListener
    public void _onUpdatePaperMoneyBalance(int i) {
    }

    @Override // com.shj.ShjListener
    public void _onUpdateShelfGoodsCount(Integer num, int i) {
    }

    @Override // com.shj.ShjListener
    public void _onUpdateShelfInventory(Integer num, Integer num2) {
    }

    @Override // com.shj.ShjListener
    public void _onUpdateShelfStatus(int i, int i2) {
    }

    @Override // com.shj.ShjListener
    public void _onUpdateTemperature(int i, int i2) {
    }

    @Override // com.shj.ShjListener
    public void _onVMCStatusChanged(VMCStatus vMCStatus, VMCStatus vMCStatus2) {
    }

    @Override // com.shj.ShjListener
    public void _onWriteCommand(Command command) {
    }

    @Override // com.shj.ShjListener
    public void _onUpdateShelfGoods(Integer num, String str, String str2, String str3) {
        ShjManager.getGoodsManager()._onUpdateShelfGoods(num, str, str2, str3);
    }

    @Override // com.shj.ShjListener
    public void _onBatchEnd() {
        EventBus.getDefault().post(new BatchEndEvent());
    }
}
