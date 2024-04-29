package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.xy.net.NetManager;
import java.util.ArrayList;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Pay_PickByCode_Request extends Report {
    private static final long serialVersionUID = 1;

    public Report_Pay_PickByCode_Request() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        this.bizType = (byte) 96;
        this.lostAble = true;
    }

    public void setParams(String str) {
        byte[] bytes = ("*0*" + str + Marker.ANY_MARKER + 0 + Marker.ANY_MARKER + 0 + Marker.ANY_MARKER).getBytes();
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }

    @Override // com.oysb.xy.net.report.Report
    public void onSuccess() {
        try {
            String str = new String(this.data).split("\\*")[2];
            String retMsg = getRetMsg();
            Loger.writeLog("NET", retMsg);
            String[] split = retMsg.split("\\*");
            if (!split[0].equalsIgnoreCase("0")) {
                if (NetManager.getPickCodeReportListener() != null) {
                    NetManager.getPickCodeReportListener().onPickcodeChecked(str, false, null, 0, null);
                    return;
                }
                return;
            }
            String str2 = split[1];
            int parseInt = Integer.parseInt(split[2]);
            int parseInt2 = Integer.parseInt(split[3]);
            ArrayList arrayList = new ArrayList();
            for (int i = 4; i < parseInt2 + 4; i++) {
                arrayList.add(split[i]);
            }
            if (NetManager.getPickCodeReportListener() != null) {
                NetManager.getPickCodeReportListener().onPickcodeChecked(str, true, str2, parseInt, arrayList);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.oysb.xy.net.report.Report
    public void onFailed() {
        String str = new String(this.data).split("\\*")[2];
        if (NetManager.getPickCodeReportListener() != null) {
            NetManager.getPickCodeReportListener().onPickcodeChecked(str, false, null, 0, null);
        }
    }
}
