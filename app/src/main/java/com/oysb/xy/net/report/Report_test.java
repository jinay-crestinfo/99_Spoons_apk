package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.ObjectHelper;
import java.util.Date;

/* loaded from: classes2.dex */
public class Report_test extends Report {
    private static final long serialVersionUID = 1;

    public Report_test() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        this.bizType = (byte) 65;
    }

    public void setParams() {
        byte[] bytes = "*112*7fe9b584*100*1*".getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
