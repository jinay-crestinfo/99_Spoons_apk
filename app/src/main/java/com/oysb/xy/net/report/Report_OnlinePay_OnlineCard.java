package com.oysb.xy.net.report;

import android.support.media.ExifInterface;
//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.xy.net.NetManager;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_OnlinePay_OnlineCard extends Report {
    private static final long serialVersionUID = 1;
    String type = "1";

    public Report_OnlinePay_OnlineCard() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        this.bizType = (byte) 64;
        this.reSendCount = 2;
        this.mastReConnectOnTimeOut = false;
    }

    public void setParams(String str, String str2, String str3, String str4, int i) {
        this.type = str;
        byte[] bytes = (Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER + str4 + Marker.ANY_MARKER + i + Marker.ANY_MARKER).getBytes();
        Loger.writeLog("SALES", Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER + str4 + Marker.ANY_MARKER + i + Marker.ANY_MARKER);
        byte[] createTimeBytes = createTimeBytes(new Date());
        this.data = new byte[bytes.length + createTimeBytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
        ObjectHelper.updateBytes(this.data, createTimeBytes, bytes.length, createTimeBytes.length);
    }

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        try {
            boolean acceptAck = super.acceptAck(bArr);
            Loger.writeLog("NET", "Report_OnlinePay_OnlineCard " + ObjectHelper.hex2String(bArr, bArr.length));
            Loger.writeLog("NET", "reCode:" + getRetCode() + " MSG:" + getRetMsg());
            NetManager.getOnlineCardPayResultListener().onPayResult(this.type, acceptAck, getRetMsg());
            if (getRetMsg().startsWith("9")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            return false;
        }
    }
}
