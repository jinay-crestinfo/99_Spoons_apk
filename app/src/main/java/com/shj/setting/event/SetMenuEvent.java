package com.shj.setting.event;

import com.oysb.utils.Event.BaseEvent;

/* loaded from: classes2.dex */
public class SetMenuEvent extends BaseEvent {
    public boolean isSuccess;
    public int menuCommandType;

    public SetMenuEvent(int i, boolean z) {
        this.menuCommandType = i;
        this.isSuccess = z;
    }
}
