package com.oysb.xy.net.report;

import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.xy.net.NetManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_CmdAck extends Report {
    private static final long serialVersionUID = 1;

    @Override // com.oysb.xy.net.report.Report
    public boolean needResend() {
        return false;
    }

    public Report_CmdAck() {
        this.dataType = (byte) 34;
        this.bizType = (byte) 112;
        this.lostAble = true;
    }

    public void setParams(String str, String str2, String str3, String str4, String str5) {
        this.data = (Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + Marker.ANY_MARKER + str4 + Marker.ANY_MARKER + str5 + Marker.ANY_MARKER).getBytes();
    }

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        try {
            Loger.writeLog("NET", "Report_Transf_Pos1 " + ObjectHelper.hex2String(bArr, bArr.length));
            if (bArr.length <= 6) {
                return true;
            }
            int length = bArr.length - 6;
            byte[] bArr2 = new byte[length];
            ObjectHelper.updateBytes(bArr2, bArr, 5, 0, length);
            NetManager.getPosTransfResultListener().onTransfPosServerResultAccept(bArr2);
            return true;
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            return true;
        }
    }
}
