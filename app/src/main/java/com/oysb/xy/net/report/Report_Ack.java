package com.oysb.xy.net.report;

/* loaded from: classes2.dex */
public class Report_Ack extends Report {
    static final byte[] ack = {29, 1, 5, 48, 50};
    private static final long serialVersionUID = 1;

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        return true;
    }

    @Override // com.oysb.xy.net.report.Report
    public byte[] getRawData() {
        return ack;
    }
}
