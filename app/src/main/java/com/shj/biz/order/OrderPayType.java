package com.shj.biz.order;

/* loaded from: classes2.dex */
public enum OrderPayType {
    WEIXIN("微信支付", 1),
    ZFB("支付宝支付", 3),
    JD("京东支付", 4),
    YL("银联支付", 5),
    YL6("银联支付", 6),
    ICCard("IC卡支付", 8),
    ThridPay("第三方支付", 10),
    ThridPayScanQrcode("第三方正扫码", 11),
    MemberPay("第三方支付", 15),
    Face("人脸识别", 16),
    YLJH("银联聚合支付", 19),
    WxFace("微信人脸识别", 27),
    WEIXIN_Share("微信分享", 22),
    PickCode("取货码", 23),
    ECard("闪付支付", 98),
    CASH("现金支付", 99);

    private int index;
    private String name;

    OrderPayType(String str, int i) {
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

    public static OrderPayType int2Enum(int i) {
        OrderPayType orderPayType = CASH;
        for (OrderPayType orderPayType2 : values()) {
            if (orderPayType2.getIndex() == i) {
                return orderPayType2;
            }
        }
        return orderPayType;
    }

    public boolean isNetPay() {
        return this == WEIXIN || this == ZFB || this == JD || this == YL;
    }

    @Override // java.lang.Enum
    public String toString() {
        return getName() + "(" + getIndex() + ")";
    }
}
