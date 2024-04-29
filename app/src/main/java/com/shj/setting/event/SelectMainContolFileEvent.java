package com.shj.setting.event;

import com.oysb.utils.Event.BaseEvent;

/* loaded from: classes2.dex */
public class SelectMainContolFileEvent extends BaseEvent {
    private String path;

    public SelectMainContolFileEvent(String str) {
        this.path = str;
    }

    public String getPath() {
        return this.path;
    }
}
