package com.oysb.xy.net.report;

import com.oysb.utils.ObjectHelper;
import com.oysb.utils.date.DateUtil;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Set_Settinginfo extends Report {
    private static final long serialVersionUID = 1;

    public Report_Set_Settinginfo() {
        this.dataType = (byte) 17;
        this.bizType = (byte) -112;
        this.lostAble = true;
    }

    public void setParams(String str, String str2, boolean z) {
        byte[] bytes;
        if (z) {
            bytes = (Marker.ANY_MARKER + str + "*0*android;" + str2 + Marker.ANY_MARKER).getBytes();
        } else {
            bytes = (Marker.ANY_MARKER + str + Marker.ANY_MARKER + DateUtil.format(new Date(), "yyyyMMddHHmmss") + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER).getBytes();
        }
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
