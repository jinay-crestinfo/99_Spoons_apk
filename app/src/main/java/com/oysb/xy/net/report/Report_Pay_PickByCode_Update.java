package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.ObjectHelper;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Pay_PickByCode_Update extends Report {
    private static final long serialVersionUID = 1;

    public Report_Pay_PickByCode_Update() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        this.bizType = (byte) 96;
        this.lostAble = true;
    }

    public void setParams(String str, boolean z, String str2, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(Marker.ANY_MARKER);
        sb.append(z ? 1 : 2);
        sb.append(Marker.ANY_MARKER);
        sb.append(str);
        sb.append(Marker.ANY_MARKER);
        sb.append(str2);
        sb.append(Marker.ANY_MARKER);
        sb.append(i);
        sb.append(Marker.ANY_MARKER);
        byte[] bytes = sb.toString().getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
