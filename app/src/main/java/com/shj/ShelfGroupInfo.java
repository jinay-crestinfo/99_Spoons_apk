package com.shj;

/* loaded from: classes2.dex */
public class ShelfGroupInfo {
    private static int groupIdStart;
    private int groupId;
    private String name;

    public ShelfGroupInfo(String str) {
        this.name = str;
        int i = groupIdStart;
        groupIdStart = i + 1;
        this.groupId = i * 10000;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getGroupId() {
        return this.groupId;
    }
}
