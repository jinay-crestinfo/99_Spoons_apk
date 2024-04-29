package com.shj;

/* loaded from: classes2.dex */
public enum ShelfDriverState {
    OfferSuccess("出货成功", 1),
    NoEngine("电机不存在", 2),
    Blocked("卡货", 3),
    EngineError("电机未正常停止", 4),
    ShelfCheckFailed("货道检测失败", 4);

    private int index;
    private String name;

    ShelfDriverState(String str, int i) {
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

    public static ShelfDriverState int2Enum(int i) {
        ShelfDriverState shelfDriverState = OfferSuccess;
        for (ShelfDriverState shelfDriverState2 : values()) {
            if (shelfDriverState2.getIndex() == i) {
                return shelfDriverState2;
            }
        }
        return shelfDriverState;
    }
}
