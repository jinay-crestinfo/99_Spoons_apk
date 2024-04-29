package com.xyshj.database.setting;

/* loaded from: classes2.dex */
public class UserSetting {
    private int eid;
    private String key;
    private int parentId;
    private String value;

    public UserSetting(int i, int i2, String str, String str2) {
        this.eid = i;
        this.parentId = i2;
        this.key = str;
        this.value = str2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public int getEid() {
        return this.eid;
    }

    public void setEid(int i) {
        this.eid = i;
    }

    public int getParentId() {
        return this.parentId;
    }

    public void setParentId(int i) {
        this.parentId = i;
    }

    public String toString() {
        return "[key:" + this.key + " , value:" + this.value + "eid:" + this.eid + "parentId:" + this.parentId + "]\n";
    }
}
