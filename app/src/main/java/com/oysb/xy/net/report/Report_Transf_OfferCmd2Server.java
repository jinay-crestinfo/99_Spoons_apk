package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.xy.net.NetManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import kotlin.UByte;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Report_Transf_OfferCmd2Server extends Report {
    private static final long serialVersionUID = 1;
    int stepState = 0;

    public int getStepState() {
        return this.stepState;
    }

    public Report_Transf_OfferCmd2Server() {
//        this.dataType = ClosedCaptionCtrl.TAB_OFFSET_CHAN_1;
        this.bizType = (byte) 112;
        this.lostAble = true;
    }

    public void setParams(String str, int i, String str2, String str3) {
        this.stepState = i;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        this.lostAble = i != 3;
        if (this.lostAble) {
            this.needResendCount = 3;
        }
        byte[] bytes = ("*1*0*" + i + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER + simpleDateFormat.format(new Date()) + Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER).getBytes();
        this.data = new byte[bytes.length];
        ObjectHelper.updateBytes(this.data, bytes, 0, bytes.length);
    }

    @Override // com.oysb.xy.net.report.Report
    public boolean lostAble() {
        if (4 == this.stepState) {
            return !needResend();
        }
        return super.lostAble();
    }

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        Exception e;
        boolean z = true;
        try {
        } catch (Exception e2) {
            e = e2;
            z = false;
        }
        if (ObjectHelper.intFromBytes(bArr, 1, 1) != ((short) (this.serialNumber & UByte.MAX_VALUE))) {
            return false;
        }
        super.acceptAck(bArr);
        try {
            NetManager.getOfferCmdTransfResultListener().onTransfOfferCmdResultAccept(this.stepState, getRetMsg());
        } catch (Exception e3) {
            e = e3;
            Loger.safe_inner_exception_catch(e);
            Loger.writeLog("NET", "Report_Transf_OfferCmd2Server " + ObjectHelper.hex2String(bArr, bArr.length) + " re:" + z);
            return z;
        }
        Loger.writeLog("NET", "Report_Transf_OfferCmd2Server " + ObjectHelper.hex2String(bArr, bArr.length) + " re:" + z);
        return z;
    }
}
