package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.ObjectHelper;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Trad_OfflineCard extends Report {
    private static final long serialVersionUID = 1;

    public Report_Trad_OfflineCard() {
//        this.dataType = ClosedCaptionCtrl.MISC_CHAN_1;
        this.bizType = (byte) 70;
    }

    public void setParams(int i, String str, String str2, String str3, String str4, int i2, int i3, int i4, int i5) {
        byte[] bytes = (Marker.ANY_MARKER + i + Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER + str4 + Marker.ANY_MARKER + i2 + Marker.ANY_MARKER + i3 + Marker.ANY_MARKER + i4 + Marker.ANY_MARKER + i5 + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
