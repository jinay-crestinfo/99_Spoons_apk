package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;

/* loaded from: classes2.dex */
public class Report_OnlinePay_SearchResult extends Report {
    private static final long serialVersionUID = 1;

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        return false;
    }

    public Report_OnlinePay_SearchResult() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        this.bizType = (byte) 81;
    }
}
