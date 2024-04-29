package com.xyshj.database.setting;

/* loaded from: classes2.dex */
public class SettingsEnabled {
    private String key;
    private String value;

    public SettingsEnabled(String str, String str2) {
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

    public String toString() {
        return "[key:" + this.key + " , value:" + this.value + "]\n";
    }
}
