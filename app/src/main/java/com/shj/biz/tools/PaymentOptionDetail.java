package com.shj.biz.tools;

import android.graphics.drawable.Drawable;

/* loaded from: classes2.dex */
public class PaymentOptionDetail {
    private Drawable icon;
    private String logoURL;
    private String name;
    private String transfer_type;
    private int type;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getTransfer_type() {
        return this.transfer_type;
    }

    public void setTransfer_type(String str) {
        this.transfer_type = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable drawable) {
        this.icon = drawable;
    }

    public String getLogoURL() {
        return this.logoURL;
    }

    public void setLogoURL(String str) {
        this.logoURL = str;
    }
}
