package com.xyshj.machine.bean;

/* loaded from: classes2.dex */
public class AdsBean implements Comparable<AdsBean> {
    private String bs;
    private String endDate;
    private String endTime;
    private String ggid;
    private String ggnr;
    private Integer qz;
    private String startDate;
    private String startTime;

    public String getGgid() {
        return this.ggid;
    }

    public void setGgid(String str) {
        this.ggid = str;
    }

    public String getGgnr() {
        return this.ggnr;
    }

    public void setGgnr(String str) {
        this.ggnr = str;
    }

    public Integer getQz() {
        return this.qz;
    }

    public void setQz(Integer num) {
        this.qz = num;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String str) {
        this.startDate = str;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String str) {
        this.startTime = str;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String str) {
        this.endDate = str;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String str) {
        this.endTime = str;
    }

    public String getBs() {
        return this.bs;
    }

    public void setBs(String str) {
        this.bs = str;
    }

    @Override // java.lang.Comparable
    public int compareTo(AdsBean adsBean) {
        if (this.qz.intValue() > adsBean.getQz().intValue()) {
            return 1;
        }
        return this.qz.intValue() < adsBean.getQz().intValue() ? -1 : 0;
    }
}
