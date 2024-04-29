package com.shj.setting.bean;

import java.io.Serializable;

/* loaded from: classes2.dex */
public class Fodder implements Serializable {
    String filetype;
    String spbh;
    String xmlurl;

    public Fodder(String str, String str2) {
        this.spbh = str;
        this.xmlurl = str2;
    }

    public Fodder(String str, String str2, String str3) {
        this.spbh = str;
        this.filetype = str2;
        this.xmlurl = str3;
    }

    public String getFiletype() {
        return this.filetype;
    }

    public void setFiletype(String str) {
        this.filetype = str;
    }

    public String getSpbh() {
        return this.spbh;
    }

    public void setSpbh(String str) {
        this.spbh = str;
    }

    public String getXmlurl() {
        return this.xmlurl;
    }

    public void setXmlurl(String str) {
        this.xmlurl = str;
    }
}
