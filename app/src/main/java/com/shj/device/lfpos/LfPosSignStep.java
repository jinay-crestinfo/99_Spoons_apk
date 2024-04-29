package com.shj.device.lfpos;

/* loaded from: classes2.dex */
public enum LfPosSignStep {
    NAK("终止", 0),
    TEST("发送测试连接报文", 1),
    CLEAR_RECORDS("清流水", 2),
    PSIGN("预签到", 3),
    SIGN1("签到1", 4),
    SIGN2("签到2", 5);

    private int index;
    private String name;

    LfPosSignStep(String str, int i) {
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

    public static LfPosSignStep int2Enum(int i) {
        LfPosSignStep lfPosSignStep = TEST;
        for (LfPosSignStep lfPosSignStep2 : values()) {
            if (lfPosSignStep2.getIndex() == i) {
                return lfPosSignStep2;
            }
        }
        return lfPosSignStep;
    }
}
