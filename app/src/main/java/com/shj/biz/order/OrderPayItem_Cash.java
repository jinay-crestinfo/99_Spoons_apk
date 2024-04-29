package com.shj.biz.order;

import com.oysb.utils.Loger;
import com.shj.MoneyType;
import com.shj.Shj;
import com.shj.biz.order.OrderPayManager;

/* loaded from: classes2.dex */
public class OrderPayItem_Cash extends OrderPayItem {
    OrderPayManager.OrderPayResultListener l = null;

    @Override // com.shj.biz.order.OrderPayItem
    public void buildQrImage(OrderPayManager.QrImageBuildListener qrImageBuildListener) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public boolean shuldBuildQrImage() {
        return false;
    }

    @Override // com.shj.biz.order.OrderPayItem
    public OrderPayType getOrderPayType() {
        return OrderPayType.CASH;
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void queryOrderPayResult(OrderPayManager.OrderPayResultListener orderPayResultListener) {
        this.isCanceled = false;
        this.l = orderPayResultListener;
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void onTimer(int i) {
        if (this.isCanceled || this.l == null || Shj.getWallet().getCatchMoney().intValue() <= this.order.getPrice()) {
            return;
        }
        if (Shj.getWallet().getLastAddType() == MoneyType.Paper || Shj.getWallet().getLastAddType() == MoneyType.Coin) {
            Loger.writeLog("SALES", "现金已满足支付要求");
            this.paySuccess = true;
        }
    }

    @Override // com.shj.biz.order.OrderPayItem
    public boolean cancel() {
        this.isCanceled = true;
        this.l = null;
        return true;
    }
}
