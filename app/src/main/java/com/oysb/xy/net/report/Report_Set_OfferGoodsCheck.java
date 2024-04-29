package com.oysb.xy.net.report;

import com.oysb.utils.ObjectHelper;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Set_OfferGoodsCheck extends Report {
    private static final long serialVersionUID = 1;

    public Report_Set_OfferGoodsCheck() {
        this.dataType = (byte) 17;
        this.bizType = (byte) 57;
    }

    public void setParams(String str, int i, short s, int i2) {
        byte[] bytes = (Marker.ANY_MARKER + str + Marker.ANY_MARKER + i + Marker.ANY_MARKER + ((int) s) + Marker.ANY_MARKER + i2 + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
