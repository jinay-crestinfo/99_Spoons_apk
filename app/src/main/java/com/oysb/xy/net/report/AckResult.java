package com.oysb.xy.net.report;

/* loaded from: classes2.dex */
public class AckResult {
    private ReportAckCode code;
    private String data;

    public ReportAckCode getCode() {
        return this.code;
    }

    public void setCode(ReportAckCode reportAckCode) {
        this.code = reportAckCode;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String str) {
        this.data = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(this.code.getIndex());
        sb.append(":");
        sb.append(this.code.getName());
        sb.append("}");
        String str = this.data;
        if (str == null) {
            str = "";
        }
        sb.append(str);
        return sb.toString();
    }
}
