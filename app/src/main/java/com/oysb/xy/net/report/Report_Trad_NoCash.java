package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.ObjectHelper;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Trad_NoCash extends Report {
    private static final long serialVersionUID = 1;

    public Report_Trad_NoCash() {
//        this.dataType = ClosedCaptionCtrl.MISC_CHAN_1;
        this.bizType = (byte) 67;
    }

    public void setParams(int i, int i2, String str, int i3, int i4, int i5, boolean z, String str2) {
        byte[] bytes = (Marker.ANY_MARKER + i + Marker.ANY_MARKER + i2 + Marker.ANY_MARKER + str + Marker.ANY_MARKER + i3 + Marker.ANY_MARKER + i4 + Marker.ANY_MARKER + i5 + Marker.ANY_MARKER + (z ? 1 : 0) + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
