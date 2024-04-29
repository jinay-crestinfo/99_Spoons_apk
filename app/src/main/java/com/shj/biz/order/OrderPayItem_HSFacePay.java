package com.shj.biz.order;

import com.shj.biz.order.OrderPayManager;

/* loaded from: classes2.dex */
public class OrderPayItem_HSFacePay extends OrderPayItem {
    @Override // com.shj.biz.order.OrderPayItem
    public void buildQrImage(OrderPayManager.QrImageBuildListener qrImageBuildListener) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void onTimer(int i) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void queryOrderPayResult(OrderPayManager.OrderPayResultListener orderPayResultListener) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public boolean shuldBuildQrImage() {
        return false;
    }

    @Override // com.shj.biz.order.OrderPayItem
    public boolean cancel() {
        this.isCanceled = true;
        return true;
    }
}
