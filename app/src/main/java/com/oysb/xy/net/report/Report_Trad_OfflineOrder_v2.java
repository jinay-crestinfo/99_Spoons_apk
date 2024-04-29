package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
//import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.ObjectHelper;
import com.shj.ShjDbHelper;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Trad_OfflineOrder_v2 extends Report {
    private static final long serialVersionUID = 1;

    public Report_Trad_OfflineOrder_v2() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        this.bizType = (byte) 114;
    }

    public void setParams(int i, String str, String str2, int i2, List<Map<String, Object>> list, String str3, String str4, String str5) {
        String str6 = "";
        int i3 = 0;
        int i4 = 0;
        while (i3 < list.size()) {
            Map<String, Object> map = list.get(i3);
            String obj = map.get(ShjDbHelper.COLUM_shelf).toString();
            String obj2 = map.get("goodsCode").toString();
            int intValue = ((Integer) map.get(ShjDbHelper.COLUM_price)).intValue();
            int intValue2 = ((Integer) map.get("success")).intValue();
            i4 += intValue;
//            str6 = str6 + (i3 > 0 ? VoiceWakeuperAidl.PARAMS_SEPARATE : "") + obj + ":" + obj2 + ":" + intValue + ":" + intValue2;
            i3++;
        }
        byte[] bytes = ("*3*" + i2 + "*NA*" + str + Marker.ANY_MARKER + i4 + Marker.ANY_MARKER + i + Marker.ANY_MARKER + str4 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str6 + Marker.ANY_MARKER + str5 + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }
}
