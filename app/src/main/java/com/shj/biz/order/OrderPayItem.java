package com.shj.biz.order;

import com.shj.biz.order.OrderPayManager;

/* loaded from: classes2.dex */
public abstract class OrderPayItem {
    Order order;
    OrderPayType orderPayType = OrderPayType.CASH;
    boolean paySuccess = false;
    boolean isCanceled = false;

    public abstract void buildQrImage(OrderPayManager.QrImageBuildListener qrImageBuildListener);

    public abstract boolean cancel();

    public abstract void onTimer(int i);

    public abstract void queryOrderPayResult(OrderPayManager.OrderPayResultListener orderPayResultListener);

    public void setOrderPayType(OrderPayType orderPayType) {
    }

    public abstract boolean shuldBuildQrImage();

    public boolean isPaySuccess() {
        return this.paySuccess;
    }

    public boolean isCanceled() {
        return this.isCanceled;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderPayType getOrderPayType() {
        return this.orderPayType;
    }

    public void release() {
        this.order = null;
    }
}
