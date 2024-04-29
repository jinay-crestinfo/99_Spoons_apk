package com.shj.device;

import android.content.Context;

/* loaded from: classes2.dex */
public class Device {
    private String descript;
    private String name;

    public void init(Context context) {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getDescript() {
        return this.descript;
    }

    public void setDescript(String str) {
        this.descript = str;
    }
}
