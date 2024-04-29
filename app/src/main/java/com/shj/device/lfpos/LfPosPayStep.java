package com.shj.device.lfpos;

/* loaded from: classes2.dex */
public enum LfPosPayStep {
    NAK("终止", 0),
    TEST("发送测试连接报文", 1),
    PAYTYPE("取消费类型", 2),
    PAY("消费", 3),
    SEARCH("查询结果", 4);

    private int index;
    private String name;

    LfPosPayStep(String str, int i) {
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

    public static LfPosPayStep int2Enum(int i) {
        LfPosPayStep lfPosPayStep = TEST;
        for (LfPosPayStep lfPosPayStep2 : values()) {
            if (lfPosPayStep2.getIndex() == i) {
                return lfPosPayStep2;
            }
        }
        return lfPosPayStep;
    }
}
