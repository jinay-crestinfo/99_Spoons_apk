package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.ObjectHelper;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Trad_PaperMoney extends Report {
    private static final long serialVersionUID = 1;

    public Report_Trad_PaperMoney() {
//        this.dataType = ClosedCaptionCtrl.MISC_CHAN_1;
        this.bizType = (byte) 56;
    }

    public void setParams(int i, int i2, int i3, int i4, boolean z) {
        byte[] bytes = (Marker.ANY_MARKER + i + Marker.ANY_MARKER + i2 + Marker.ANY_MARKER + i3 + Marker.ANY_MARKER + i4 + Marker.ANY_MARKER + (z ? 1 : 0) + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
