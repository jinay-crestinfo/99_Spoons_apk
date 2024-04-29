package com.shj.device.lfpos;

import java.sql.Date;

/* loaded from: classes2.dex */
public class LfPosPayInfo {
    private String card;
    private Date createTime;
    private Integer id;
    private String info;
    private Integer money;
    private Date payTime;
    private String payType;
    private String posId;
    private boolean queryBalance = false;
    private LfPosPayStep step;
    private String user;

    public LfPosPayStep getStep() {
        return this.step;
    }

    public void setStep(LfPosPayStep lfPosPayStep) {
        this.step = lfPosPayStep;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public Integer getMoney() {
        return this.money;
    }

    public void setMoney(Integer num) {
        this.money = num;
    }

    public String getPayType() {
        return this.payType;
    }

    public void setPayType(String str) {
        this.payType = str;
    }

    public String getCard() {
        return this.card;
    }

    public void setCard(String str) {
        this.card = str;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date date) {
        this.createTime = date;
    }

    public Date getPayTime() {
        return this.payTime;
    }

    public void setPayTime(Date date) {
        this.payTime = date;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String str) {
        this.info = str;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String str) {
        this.user = str;
    }

    public String getPosId() {
        return this.posId;
    }

    public void setPosId(String str) {
        this.posId = str;
    }

    public boolean isQueryBalance() {
        return this.queryBalance;
    }

    public void setQueryBalance(boolean z) {
        this.queryBalance = z;
    }
}
