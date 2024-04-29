package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import java.util.ArrayList;
import java.util.Iterator;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Con_apkinfo extends Report {
    private static final long serialVersionUID = 1;

    public Report_Con_apkinfo() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_2;
        this.bizType = (byte) 64;
        this.lostAble = true;
    }

    public void setParams(ArrayList<String> arrayList) {
        Iterator<String> it = arrayList.iterator();
        String str = Marker.ANY_MARKER;
        while (it.hasNext()) {
            str = str + it.next() + Marker.ANY_MARKER;
        }
        this.data = str.getBytes();
    }
}
