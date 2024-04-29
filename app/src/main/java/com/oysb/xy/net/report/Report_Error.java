package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.xyshj.database.setting.SettingType;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Error extends Report {
    private static final long serialVersionUID = 1;

    public Report_Error() {
//        this.dataType = ClosedCaptionCtrl.MISC_CHAN_2;
        this.bizType = (byte) 17;
        this.lostAble = true;
    }

    public void setParams(String str, String str2, String str3, String str4, String str5, String str6) {
        if (str4.length() > 190) {
            str4 = str4.substring(0, SettingType.DAILY_SALES);
        }
        this.data = (Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER + str4 + Marker.ANY_MARKER + str5 + Marker.ANY_MARKER + str6 + Marker.ANY_MARKER).getBytes();
    }
}
