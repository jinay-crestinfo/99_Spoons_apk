package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Con_Shelves extends Report {
    private static final long serialVersionUID = 1;

    public Report_Con_Shelves() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_2;
        this.bizType = (byte) 49;
    }

    public void setParams(String str, String str2, String str3, String str4, int i, String str5, int i2, int i3, int i4, int i5) {
        this.data = (Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER + str4 + Marker.ANY_MARKER + i + Marker.ANY_MARKER + str5 + Marker.ANY_MARKER + i2 + Marker.ANY_MARKER + i3 + Marker.ANY_MARKER + i4 + Marker.ANY_MARKER).getBytes();
    }

    public void setParamsEx(String str, String str2, List<HashMap<String, Object>> list) {
        StringBuilder sb = new StringBuilder();
        sb.append(Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + list.size() + Marker.ANY_MARKER);
        for (HashMap<String, Object> hashMap : list) {
            sb.append(hashMap.get("hdh").toString() + Marker.ANY_MARKER);
            sb.append(hashMap.get("zt").toString() + Marker.ANY_MARKER);
            sb.append(hashMap.get("spbm").toString() + Marker.ANY_MARKER);
            sb.append(hashMap.get("hdjg").toString() + Marker.ANY_MARKER);
            sb.append(hashMap.get("hdcl").toString() + Marker.ANY_MARKER);
            sb.append(hashMap.get("hdrl").toString() + Marker.ANY_MARKER);
        }
        this.data = sb.toString().getBytes();
    }

    public int getDataSize() {
        try {
            return this.data.length;
        } catch (Exception unused) {
            return 0;
        }
    }
}
