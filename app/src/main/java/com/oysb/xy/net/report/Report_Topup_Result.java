package com.oysb.xy.net.report;

import com.oysb.utils.ObjectHelper;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Topup_Result extends Report {
    private static final long serialVersionUID = 1;

    public Report_Topup_Result() {
        this.dataType = (byte) 21;
        this.bizType = (byte) 49;
    }

    public void setParams(boolean z, String str, int i, int i2) {
        byte[] bytes = (Marker.ANY_MARKER + (!z ? 1 : 0) + Marker.ANY_MARKER + str + Marker.ANY_MARKER + i + Marker.ANY_MARKER + i2 + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
