package com.oysb.xy.net.report;

import com.oysb.utils.ObjectHelper;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Status_phone_v2 extends Report {
    private static final long serialVersionUID = 1;

    public Report_Status_phone_v2() {
        this.dataType = (byte) -5;
        this.bizType = (byte) 51;
        this.lostAble = true;
        this.mastReConnectOnTimeOut = false;
    }

    public void setParams(int i, String str, String str2, String str3) {
        byte[] bytes = (Marker.ANY_MARKER + i + Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
