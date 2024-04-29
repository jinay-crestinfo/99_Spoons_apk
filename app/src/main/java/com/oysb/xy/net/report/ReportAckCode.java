package com.oysb.xy.net.report;

/* loaded from: classes2.dex */
public enum ReportAckCode {
    Success("网关接收成功", 0),
    Failed("数据接收失败或保存出错", 1),
    CheckFailed("数据包校验错误或包不合法", 2),
    MachineInvalidate("密码错误或机器不存在", 3),
    UnLogined("数据包未登录或未知错误", 9);

    private int index;
    private String name;

    ReportAckCode(String str, int i) {
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

    public static ReportAckCode int2Enum(int i) {
        ReportAckCode reportAckCode = UnLogined;
        for (ReportAckCode reportAckCode2 : values()) {
            if (reportAckCode2.getIndex() == i) {
                return reportAckCode2;
            }
        }
        return reportAckCode;
    }
}
