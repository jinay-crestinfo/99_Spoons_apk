package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.ObjectHelper;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Non_Cash_Collection extends Report {
    private static final long serialVersionUID = 1;

    public Report_Non_Cash_Collection() {
//        this.dataType = ClosedCaptionCtrl.MISC_CHAN_1;
        this.bizType = (byte) 68;
        this.lostAble = false;
    }

    public void setParams(String str, String str2, String str3, String str4, String str5) {
        byte[] bytes = (Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER + str4 + Marker.ANY_MARKER + str5 + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
