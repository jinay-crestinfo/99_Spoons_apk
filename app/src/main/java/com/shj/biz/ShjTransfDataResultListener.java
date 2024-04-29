package com.shj.biz;

import com.oysb.xy.i.TransfDataResultListener;

/* loaded from: classes2.dex */
public class ShjTransfDataResultListener implements TransfDataResultListener {
    @Override // com.oysb.xy.i.TransfDataResultListener
    public void onTransfDataResultAccept(int i, byte[] bArr) {
        ShjManager.getStatusListener().onTransfDataAck(i, bArr);
    }
}
