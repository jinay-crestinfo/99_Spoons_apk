package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;

/* loaded from: classes2.dex */
public class Report_ServerTime extends Report {
    private static final long serialVersionUID = 1;

    public Report_ServerTime() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_2;
        this.bizType = (byte) 54;
        this.data = "*0*".getBytes();
        this.lostAble = true;
    }

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        return super.acceptAck(bArr);
    }
}
