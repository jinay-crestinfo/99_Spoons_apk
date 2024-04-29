package com.shj;

/* loaded from: classes2.dex */
public enum ShelfType {
    Normal("弹簧", 1),
    Belt("皮带", 2),
    Hanger("挂钩", 3),
    Box("格子", 4),
    Skidway("电磁滑道", 5),
    Other("其他", 9);

    private int index;
    private String name;

    ShelfType(String str, int i) {
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

    public static ShelfType int2Enum(int i) {
        ShelfType shelfType = Normal;
        for (ShelfType shelfType2 : values()) {
            if (shelfType2.getIndex() == i) {
                return shelfType2;
            }
        }
        return shelfType;
    }
}
