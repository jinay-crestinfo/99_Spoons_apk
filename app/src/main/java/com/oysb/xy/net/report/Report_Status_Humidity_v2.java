package com.oysb.xy.net.report;

import com.oysb.utils.ObjectHelper;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Status_Humidity_v2 extends Report {
    private static final long serialVersionUID = 1;

    public Report_Status_Humidity_v2() {
        this.dataType = (byte) 18;
        this.bizType = (byte) 51;
        this.lostAble = true;
        this.mastReConnectOnTimeOut = false;
    }

    public void setParams(List<Map<String, Integer>> list) {
        String str = Marker.ANY_MARKER + list.size();
        for (Map<String, Integer> map : list) {
            str = str + Marker.ANY_MARKER + map.get("jgh") + Marker.ANY_MARKER + map.get("status") + Marker.ANY_MARKER + map.get("temperature");
        }
        if (list.size() == 0) {
            str = str + "*0*0*0";
        }
        byte[] bytes = (str + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
