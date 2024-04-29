package com.oysb.xy.net.report;

import com.oysb.utils.ObjectHelper;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_location extends Report {
    private static final long serialVersionUID = 1;

    public Report_location() {
        this.dataType = (byte) 18;
        this.bizType = (byte) 82;
        this.lostAble = true;
    }

    public void setParams(int i, double d, double d2) {
        byte[] bytes = (Marker.ANY_MARKER + i + Marker.ANY_MARKER + d + Marker.ANY_MARKER + d2 + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
