package com.shj;

/* loaded from: classes2.dex */
public enum MoneyType {
    Paper("纸币", 1),
    Coin("硬币", 2),
    ICCard("IC 卡", 3),
    ECard("金融卡", 4),
    Weixin("微信支付", 5),
    Zfb("支付宝支付", 6),
    JD("京东支付", 7),
    EAT("吞币", 8),
    YL("银联支付", 9),
    PickCode("取货码取货", 21),
    Weixin_Share("微信分享", 22),
    PaperChange("纸币找零金额", 150),
    CoinChange("硬币找零金额", 151),
    Reset("重置", 153);

    private int index;
    private String name;

    MoneyType(String str, int i) {
        setName(str);
        setIndex(i);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public static MoneyType int2Enum(int i) {
        MoneyType moneyType = Paper;
        for (MoneyType moneyType2 : values()) {
            if (moneyType2.getIndex() == i) {
                return moneyType2;
            }
        }
        return moneyType;
    }
}
