package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import java.util.Map;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Con_MachineStatus extends Report {
    private static final long serialVersionUID = 1;

    public Report_Con_MachineStatus() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_2;
        this.bizType = (byte) 52;
    }

    public void setParams(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(Marker.ANY_MARKER + map.keySet().size());
        for (String str : map.keySet()) {
            sb.append(str + Marker.ANY_MARKER + map.get(str));
        }
        this.data = sb.toString().getBytes();
    }
}
