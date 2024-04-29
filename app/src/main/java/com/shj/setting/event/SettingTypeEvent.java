package com.shj.setting.event;

import com.oysb.utils.Event.BaseEvent;

/* loaded from: classes2.dex */
public class SettingTypeEvent extends BaseEvent {
    private Object data;
    private int settingType;

    public SettingTypeEvent(int i, Object obj) {
        this.settingType = i;
        this.data = obj;
    }

    public int getSettingType() {
        return this.settingType;
    }

    public Object getData() {
        return this.data;
    }
}
