package com.oysb.xy.net.report;

import com.oysb.utils.ObjectHelper;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Status_phone extends Report {
    private static final long serialVersionUID = 1;

    public Report_Status_phone() {
        this.dataType = (byte) 18;
        this.bizType = (byte) 96;
        this.lostAble = true;
        this.mastReConnectOnTimeOut = false;
    }

    public void setParams(String str, long j, long j2, int i, int i2, String str2, String str3) {
        byte[] bytes = (Marker.ANY_MARKER + str + Marker.ANY_MARKER + j + Marker.ANY_MARKER + j2 + Marker.ANY_MARKER + i + Marker.ANY_MARKER + i2 + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
