package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.ObjectHelper;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Trad_OfflineOrder extends Report {
    private static final long serialVersionUID = 1;

    public Report_Trad_OfflineOrder() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        this.bizType = (byte) 113;
    }

    public void setParams_hs(String str, int i, String str2, String str3, int i2, String str4, String str5, int i3, int i4, String str6, String str7) {
        byte[] bytes = ("*4*" + i2 + "*NA*" + str2 + Marker.ANY_MARKER + i3 + Marker.ANY_MARKER + i + Marker.ANY_MARKER + str6 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER + str4 + ":" + str5 + ":" + i3 + ":" + i4 + ";*" + str + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }

    public void setParams(int i, String str, String str2, int i2, String str3, String str4, int i3, int i4, String str5, String str6) {
        byte[] bytes = ("*3*" + i2 + "*NA*" + str + Marker.ANY_MARKER + i3 + Marker.ANY_MARKER + i + Marker.ANY_MARKER + str5 + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + ":" + str4 + ":" + i3 + ":" + i4 + ";*").getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
