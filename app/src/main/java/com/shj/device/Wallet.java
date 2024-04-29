package com.shj.device;

import com.shj.MoneyType;

/* loaded from: classes2.dex */
public class Wallet {
    private MoneyType lastAddType;
    private MoneyType lastAddTypeBeforOfferGoods;
    private Integer catchMoney = 0;
    private Integer catchCaseMoney = 0;
    private Integer lastAdd = 0;
    private String lastAddMoneyInfo = "";
    private String lastAddMoneyInfoBeforOfferGoods = "";
    private Integer lastAddMoneyBeforOfferGoods = 0;
    private Integer lastDeduce = 0;
    private Integer lastPaperChange = 0;
    private Integer lastCoinChange = 0;
    private Integer lateastChange = 0;
    private Integer paperBalance = -1;
    private Integer coinBalance = -1;
    private Integer sn = 0;

    public Wallet() {
        setSn(0);
    }

    public Integer getCatchMoney() {
        return this.catchMoney;
    }

    public void setCatchMoney(Integer num) {
        this.catchMoney = num;
    }

    public Integer getPaperBalance() {
        return this.paperBalance;
    }

    public void setPaperBalance(Integer num) {
        this.paperBalance = num;
    }

    public Integer getCoinBalance() {
        return this.coinBalance;
    }

    public void setCoinBalance(Integer num) {
        this.coinBalance = num;
    }

    public Integer getSn() {
        return this.sn;
    }

    public void setSn(Integer num) {
        this.sn = num;
    }

    public Integer getLastAdd() {
        return this.lastAdd;
    }

    public void setLastAdd(Integer num, MoneyType moneyType) {
        this.lastAdd = num;
        this.lastAddType = moneyType;
        if (moneyType == MoneyType.Coin || moneyType == MoneyType.Paper) {
            this.catchCaseMoney = Integer.valueOf(this.catchCaseMoney.intValue() + num.intValue());
        }
    }

    public Integer getLastDeduce() {
        return this.lastDeduce;
    }

    public void setLastDeduce(Integer num) {
        this.lastDeduce = num;
    }

    public Integer getLastPaperChange() {
        return this.lastPaperChange;
    }

    public void setLastPaperChange(Integer num) {
        this.lastPaperChange = num;
    }

    public MoneyType getLastAddType() {
        return this.lastAddType;
    }

    public Integer getLateastChange() {
        return this.lateastChange;
    }

    public void setLateastChange(Integer num) {
        this.lateastChange = num;
    }

    public void reset() {
        this.catchMoney = 0;
        this.catchCaseMoney = 0;
        this.lastAdd = 0;
        this.lastAddType = MoneyType.Paper;
        this.lastDeduce = 0;
        this.lastPaperChange = 0;
        this.lateastChange = 0;
        this.paperBalance = -1;
        this.coinBalance = -1;
    }

    public Integer getLastCoinChange() {
        return this.lastCoinChange;
    }

    public void setLastCoinChange(Integer num) {
        this.lastCoinChange = num;
    }

    public MoneyType getLastAddTypeBeforOfferGoods() {
        return this.lastAddTypeBeforOfferGoods;
    }

    public void markLastAddTypeBeforOfferGoods() {
        this.lastAddTypeBeforOfferGoods = this.lastAddType;
    }

    public String getLastAddMoneyInfo() {
        return this.lastAddMoneyInfo;
    }

    public void setLastAddMoneyInfo(String str) {
        this.lastAddMoneyInfo = str;
    }

    public String getLastAddMoneyInfoBeforOfferGoods() {
        return this.lastAddMoneyInfoBeforOfferGoods;
    }

    public void markLastAddMoneyInfoBeforOfferGoods() {
        this.lastAddMoneyInfoBeforOfferGoods = this.lastAddMoneyInfo;
    }

    public Integer getLastAddMoneyBeforOfferGoods() {
        return this.lastAddMoneyBeforOfferGoods;
    }

    public void markLastAddMoneyBeforOfferGoods() {
        if (this.lastAddType == MoneyType.Coin || this.lastAddType == MoneyType.Paper) {
            this.lastAddMoneyBeforOfferGoods = this.catchMoney;
        } else {
            this.lastAddMoneyBeforOfferGoods = this.lastAdd;
        }
    }
}
