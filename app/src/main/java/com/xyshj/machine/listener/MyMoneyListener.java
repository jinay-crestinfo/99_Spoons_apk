package com.xyshj.machine.listener;

import android.content.Intent;
import android.os.Bundle;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.app.R;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.video.TTSManager;
import com.shj.MoneyType;
import com.shj.Shj;
import com.shj.biz.MoneyListener;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.database.setting.AppSetting;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class MyMoneyListener implements MoneyListener {
    public static final String ACTION_MONEY_CHANGED = "ACTION_MONEY_CHANGED";
    public static final String ACTION_MONEY_CHARGING = "ACTION_MONEY_CHARGING";
    public static final String ACTION_MONEY_CHARG_FINISHED = "ACTION_MONEY_CHARG_FINISHED";
    public static final String ACTION_MONEY_CHARG_START = "ACTION_MONEY_CHARG_START";
    public static final String ACTION_MONEY_COIN_CHANGED = "ACTION_MONEY_COIN_CHANGED";
    public static final String ACTION_MONEY_PAPER_CHANGED = "ACTION_MONEY_PAPER_CHANGED";
    public static final String ACTION_MONEY_RECIVE = "ACTION_MONEY_RECIVE";

    @Override // com.shj.biz.MoneyListener
    public void onReciveMoney(MoneyType moneyType, int i) {
        Intent intent = new Intent(ACTION_MONEY_RECIVE);
        Bundle bundle = new Bundle();
        bundle.putInt(IjkMediaMeta.IJKM_KEY_TYPE, moneyType.getIndex());
        bundle.putInt("count", i);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        if ((moneyType == MoneyType.Coin || moneyType == MoneyType.Paper) && i > 0) {
            TTSManager.clear();
            int i2 = R.string.lab_insertmoney;
            double d = i;
            Double.isNaN(d);
            String string = ShjAppHelper.getString(i2, "0", Double.valueOf(d / 100.0d));
            if (CommonTool.getLanguage(ShjAppBase.sysApp).equals("en")) {
                string = string.replace("$", AppSetting.getMonetarySymbol(ShjAppBase.sysApp, null));
            }
            TTSManager.addText(string);
        }
        Loger.writeLog("Broadcast", "ACTION_MONEY_RECIVE " + bundle.toString());
    }

    @Override // com.shj.biz.MoneyListener
    public void onMoneyChanged() {
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_MONEY_CHANGED));
        Loger.writeLog("Broadcast;SHJ", ACTION_MONEY_CHANGED);
    }

    @Override // com.shj.biz.MoneyListener
    public void onCoinBalanceChanged(int i) {
        Intent intent = new Intent(ACTION_MONEY_COIN_CHANGED);
        Bundle bundle = new Bundle();
        bundle.putInt("balance", i);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_MONEY_COIN_CHANGED " + bundle.toString());
    }

    @Override // com.shj.biz.MoneyListener
    public void onPaperBalanceChanged(int i) {
        Intent intent = new Intent(ACTION_MONEY_PAPER_CHANGED);
        Bundle bundle = new Bundle();
        bundle.putInt("balance", i);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_MONEY_PAPER_CHANGED " + bundle.toString());
    }

    @Override // com.shj.biz.MoneyListener
    public void onChargStart(int i) {
        Intent intent = new Intent(ACTION_MONEY_CHARG_START);
        Bundle bundle = new Bundle();
        bundle.putInt("money", i);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        TTSManager.clear();
        TTSManager.addText(ShjAppHelper.getString(R.string.lab_change));
        Loger.writeLog("Broadcast", "ACTION_MONEY_CHARG_START " + bundle.toString());
    }

    @Override // com.shj.biz.MoneyListener
    public void onCharging() {
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_MONEY_CHARGING));
        Loger.writeLog("Broadcast", "ACTION_MONEY_CHARGING ");
    }

    @Override // com.shj.biz.MoneyListener
    public void onChargFinished(int i, int i2) {
        Intent intent = new Intent(ACTION_MONEY_CHARG_FINISHED);
        Bundle bundle = new Bundle();
        bundle.putInt("coinChange", i);
        bundle.putInt("paperChange", i2);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        TTSManager.addText(ShjAppHelper.getString(((Shj.getWallet().getCatchMoney().intValue() - i) - i2 == 0 || Shj.getWallet().getCatchMoney().intValue() == 0) ? R.string.lab_changesuccess : R.string.lab_changeunsuccess));
        Loger.writeLog("Broadcast", "ACTION_MONEY_CHARG_FINISHED " + bundle.toString());
    }
}
