package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;

/* loaded from: classes2.dex */
public class Report_Con_HistoryDataFinished extends Report {
    private static final long serialVersionUID = 1;

    public Report_Con_HistoryDataFinished() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_2;
        this.bizType = (byte) -112;
        this.data = new byte[]{0};
        this.lostAble = true;
    }
}
