package com.shj.biz;

import com.oysb.xy.i.OfferCmdTransfResultListener;

/* loaded from: classes2.dex */
public class ShjOfferCmdResultListener implements OfferCmdTransfResultListener {
    @Override // com.oysb.xy.i.OfferCmdTransfResultListener
    public void onTransfOfferCmdResultAccept(int i, String str) {
        ShjManager.getBizShjListener().onServerOfferGoodsCmd(i, str);
    }
}
