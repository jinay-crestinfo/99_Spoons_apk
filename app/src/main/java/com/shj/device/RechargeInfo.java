package com.shj.device;

import java.sql.Date;

/* loaded from: classes2.dex */
public class RechargeInfo {
    private float money;
    private Date time;
    private int type;

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public float getMoney() {
        return this.money;
    }

    public void setMoney(float f) {
        this.money = f;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date date) {
        this.time = date;
    }
}
