package com.oysb.xy.net.report;

import com.oysb.utils.ObjectHelper;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class Report_Heartbeat extends Report {
    static byte[] heartbeat = {-1, 1, 0};
    private static final long serialVersionUID = 1;

    public Report_Heartbeat() {
        this.dataType = (byte) -1;
        this.bizType = (byte) 0;
        this.needResendCount = 2;
    }

    @Override // com.oysb.xy.net.report.Report
    public byte[] getRawData() {
        if (this.rawData != null) {
            return this.rawData;
        }
        updateSerialNumber();
        ObjectHelper.updateBytes(heartbeat, (int) this.serialNumber, 1, 1);
        int i = 0;
        this.check = (byte) 0;
        while (true) {
            byte[] bArr = heartbeat;
            if (i < bArr.length - 1) {
                this.check = (byte) (this.check + ((short) (heartbeat[i] & UByte.MAX_VALUE)));
                i++;
            } else {
                ObjectHelper.updateBytes(bArr, this.check & UByte.MAX_VALUE, heartbeat.length - 1, 1);
                this.rawData = heartbeat;
                return heartbeat;
            }
        }
    }

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        setRetCode("0");
        setRetMsg("成功");
        AckResult ackResult = new AckResult();
        ackResult.setCode(ReportAckCode.int2Enum(0));
        ackResult.setData("");
        setResult(ackResult);
        return true;
    }
}
