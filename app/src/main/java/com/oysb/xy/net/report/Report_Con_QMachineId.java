package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;

/* loaded from: classes2.dex */
public class Report_Con_QMachineId extends Report {
    private static final long serialVersionUID = 1;

    public Report_Con_QMachineId() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_2;
        this.bizType = (byte) 57;
        this.lostAble = true;
    }

    public void setParams(String str) {
        this.data = str.getBytes();
    }

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        try {
            int length = bArr.length - 4;
            byte[] bArr2 = new byte[length];
            ObjectHelper.updateBytes(bArr2, bArr, 3, 0, length);
            Loger.writeLog("NET", new String(bArr2, "UTF-8"));
            return true;
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            return true;
        }
    }
}
