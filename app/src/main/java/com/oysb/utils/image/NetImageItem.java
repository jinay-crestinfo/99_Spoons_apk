package com.oysb.utils.image;

/* loaded from: classes2.dex */
public class NetImageItem {
    private String catchPrefix = "";
    private String catchRootPrefix = "";
    private String saveName;
    private String savePath;
    private String url;

    public NetImageItem(String str, String str2, String str3) {
        setUrl(str);
        setSavePath(str2);
        setSaveName(str3);
    }

    public NetImageItem(String str, String str2, String str3, String str4, String str5) {
        setUrl(str);
        setSavePath(str2);
        setSaveName(str3);
        setCatchRootPrefix(str4);
        setCatchPrefix(str5);
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getSavePath() {
        return this.savePath;
    }

    public void setSavePath(String str) {
        this.savePath = str;
    }

    public String getSaveName() {
        return this.saveName;
    }

    public void setSaveName(String str) {
        this.saveName = str;
    }

    public String getCatchPrefix() {
        return this.catchPrefix;
    }

    public void setCatchPrefix(String str) {
        this.catchPrefix = str;
    }

    public String getCatchRootPrefix() {
        return this.catchRootPrefix;
    }

    public void setCatchRootPrefix(String str) {
        this.catchRootPrefix = str;
    }
}
