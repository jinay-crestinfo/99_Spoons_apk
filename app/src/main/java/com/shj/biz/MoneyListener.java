package com.shj.biz;

import com.shj.MoneyType;

/* loaded from: classes2.dex */
public interface MoneyListener {
    void onChargFinished(int i, int i2);

    void onChargStart(int i);

    void onCharging();

    void onCoinBalanceChanged(int i);

    void onMoneyChanged();

    void onPaperBalanceChanged(int i);

    void onReciveMoney(MoneyType moneyType, int i);
}
