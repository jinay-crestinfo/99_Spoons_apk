package com.oysb.xy.net.report;

//import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.xy.net.NetManager;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class Report_Transf_PosServer extends Report {
    private static final long serialVersionUID = 1;

    @Override // com.oysb.xy.net.report.Report
    public boolean needResend() {
        return false;
    }

    public Report_Transf_PosServer() {
//        this.dataType = ClosedCaptionCtrl.MID_ROW_CHAN_2;
        this.lostAble = true;
    }

    public void setParams(byte b, byte[] bArr) {
        this.bizType = b;
        this.data = bArr;
    }

    @Override // com.oysb.xy.net.report.Report
    public byte[] getRawData() {
        if (this.rawData != null) {
            return this.rawData;
        }
        updateSerialNumber();
        int length = this.data.length + 6;
        byte[] bArr = new byte[length];
        int i = 0;
        ObjectHelper.updateBytes(bArr, (int) this.dataType, 0, 1);
        ObjectHelper.updateBytes(bArr, (int) this.serialNumber, 1, 1);
        ObjectHelper.updateBytes(bArr, (int) this.bizType, 2, 1);
        ObjectHelper.updateBytes(bArr, this.data.length + 6, 3, 2);
        ObjectHelper.updateBytes(bArr, this.data, 5, this.data.length);
        this.check = (byte) 0;
        while (true) {
            int i2 = length - 1;
            if (i < i2) {
                this.check = (byte) (this.check + ((short) (bArr[i] & UByte.MAX_VALUE)));
                i++;
            } else {
                ObjectHelper.updateBytes(bArr, this.check & UByte.MAX_VALUE, i2, 1);
                this.rawData = bArr;
                return bArr;
            }
        }
    }

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        try {
            Loger.writeLog("NET", "Report_Transf_Pos1 " + ObjectHelper.hex2String(bArr, bArr.length));
            int length = bArr.length + (-6);
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
