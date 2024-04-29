package com.oysb.xy.net.report;

import com.oysb.utils.ObjectHelper;
import java.util.Date;
import java.util.Map;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Status_Shelf extends Report {
    private static final long serialVersionUID = 1;

    public Report_Status_Shelf() {
        this.dataType = (byte) 18;
        this.bizType = (byte) 54;
    }

    public void setParams(Map<String, Integer> map, Map<String, Integer> map2) {
        StringBuilder sb = new StringBuilder();
        sb.append(Marker.ANY_MARKER + map.keySet().size());
        if (map2 != null) {
            for (String str : map.keySet()) {
                sb.append(Marker.ANY_MARKER + str + Marker.ANY_MARKER + map.get(str) + Marker.ANY_MARKER + map2.get(str));
            }
        } else {
            for (String str2 : map.keySet()) {
                sb.append(Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + map.get(str2));
            }
        }
        sb.append(Marker.ANY_MARKER);
        byte[] bytes = sb.toString().getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
