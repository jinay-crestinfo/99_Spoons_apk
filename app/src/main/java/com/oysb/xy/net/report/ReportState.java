package com.oysb.xy.net.report;

/* loaded from: classes2.dex */
public enum ReportState {
    Waitting("排队中", 0),
    Send("已发送", 1),
    TimeOut("已超时", 2),
    Finished("发送成功", 3),
    Failed("发送失败", 9);

    private int index;
    private String name;

    ReportState(String str, int i) {
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

    public static ReportState int2Enum(int i) {
        ReportState reportState = Waitting;
        for (ReportState reportState2 : values()) {
            if (reportState2.getIndex() == i) {
                return reportState2;
            }
        }
        return reportState;
    }
}
