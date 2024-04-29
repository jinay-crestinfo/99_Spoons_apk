package com.shj.setting.event;

import com.oysb.utils.Event.BaseEvent;

/* loaded from: classes2.dex */
public class SelectShelfDrivingFileEvent extends BaseEvent {
    private String path;

    public SelectShelfDrivingFileEvent(String str) {
        this.path = str;
    }

    public String getPath() {
        return this.path;
    }
}
