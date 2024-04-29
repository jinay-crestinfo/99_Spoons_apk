package com.shj.biz.lfpos;

import com.oysb.utils.ObjectHelper;
import com.oysb.xy.net.NetManager;
import com.oysb.xy.net.report.Report_Transf_PosServer;
import com.shj.biz.ShjManager;
import com.shj.device.lfpos.LfPosServerListener;

/* loaded from: classes2.dex */
public class ShjLfPosServerListener implements LfPosServerListener {
    @Override // com.shj.device.lfpos.LfPosServerListener
    public void sendMesssage2Server(byte[] bArr, byte[] bArr2) {
        Report_Transf_PosServer report_Transf_PosServer = new Report_Transf_PosServer();
        byte[] bArr3 = new byte[bArr2.length + 2];
        ObjectHelper.updateBytes(bArr3, bArr2.length, 0, 2);
        ObjectHelper.updateBytes(bArr3, bArr2, 2, bArr2.length);
        report_Transf_PosServer.setParams((byte) (bArr[bArr.length - 1] - 1), bArr3);
        NetManager.appendReport(report_Transf_PosServer);
    }

    @Override // com.shj.device.lfpos.LfPosServerListener
    public void onLineCardPay(int i, String str, int i2, int i3, int i4) {
        ShjManager.getBizShjListener()._onNeedICCardPay(i2, str, "");
    }
}
