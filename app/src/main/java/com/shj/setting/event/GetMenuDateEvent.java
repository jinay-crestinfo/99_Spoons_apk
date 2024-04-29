package com.shj.setting.event;

import com.oysb.utils.Event.BaseEvent;

/* loaded from: classes2.dex */
public class GetMenuDateEvent extends BaseEvent {
    public Object data;
    public int menuCommandType;

    public GetMenuDateEvent(int i, Object obj) {
        this.menuCommandType = i;
        this.data = obj;
    }
}
