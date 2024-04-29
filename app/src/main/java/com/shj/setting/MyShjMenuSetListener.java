package com.shj.setting;

import com.shj.ShjMenuSetListener;
import com.shj.setting.event.GetMenuDateEvent;
import com.shj.setting.event.SetMenuEvent;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* loaded from: classes2.dex */
public class MyShjMenuSetListener implements ShjMenuSetListener {
    @Override // com.shj.ShjMenuSetListener
    public void setMenuState(int i, boolean z) {
        EventBus.getDefault().post(new SetMenuEvent(i, z));
    }

    @Override // com.shj.ShjMenuSetListener
    public void swallowingMoneyTime(int i, int i2) {
        EventBus.getDefault().post(new GetMenuDateEvent(i, Integer.valueOf(i2)));
    }

    @Override // com.shj.ShjMenuSetListener
    public void coinsFiveCents(int i, int i2) {
        EventBus.getDefault().post(new GetMenuDateEvent(i, Integer.valueOf(i2)));
    }

    @Override // com.shj.ShjMenuSetListener
    public void coinsOneYuan(int i, int i2) {
        EventBus.getDefault().post(new GetMenuDateEvent(i, Integer.valueOf(i2)));
    }

    @Override // com.shj.ShjMenuSetListener
    public void disposalOfSurplusAmount(int i, int i2) {
        EventBus.getDefault().post(new GetMenuDateEvent(i, Integer.valueOf(i2)));
    }

    @Override // com.shj.ShjMenuSetListener
    public void abinetCount(int i, int i2) {
        EventBus.getDefault().post(new GetMenuDateEvent(i, Integer.valueOf(i2)));
    }

    @Override // com.shj.ShjMenuSetListener
    public void transactionVolume(int i, List<String> list) {
        EventBus.getDefault().post(new GetMenuDateEvent(i, list));
    }
}
