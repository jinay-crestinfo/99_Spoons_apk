package com.shj.biz.lfpos;

import com.oysb.xy.i.PosTransfResultListener;
import com.shj.device.lfpos.LfPos;

/* loaded from: classes2.dex */
public class ShjPosTransfResultListener implements PosTransfResultListener {
    @Override // com.oysb.xy.i.PosTransfResultListener
    public void onTransfPosServerResultAccept(byte[] bArr) {
        LfPos.onServerResult(bArr);
    }
}
