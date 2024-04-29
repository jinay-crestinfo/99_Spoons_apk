package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Con_SingIn extends Report {
    private static final long serialVersionUID = 1;

    public Report_Con_SingIn() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_2;
        this.bizType = (byte) 48;
    }

    public void setParams(String str, String str2, String str3) {
        this.data = (Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER).getBytes();
    }
}
