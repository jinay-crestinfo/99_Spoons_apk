package com.shj.biz.order;

import android.graphics.Bitmap;

/* loaded from: classes2.dex */
public interface OrderListener {
    void onError(Order order, OrderPayType orderPayType, int i, String str);

    void onNetPriceUpdated();

    void onOrderCanceled(Order order);

    void onOrderCanceling(Order order);

    void onOrderCreated(Order order);

    void onOrderMessage(String str);

    void onPayCanceled(Order order);

    void onPayFailed(Order order, OrderPayType orderPayType, String str);

    void onPaySuccess(Order order, OrderPayType orderPayType);

    void onPickCodeChecked(String str, boolean z);

    void onPickCodeOfferGoodsError(String str, String str2);

    void onPickCodeOfferGoodsFinished(String str);

    void onQrCodeImageCreated(Order order, OrderPayType orderPayType, Bitmap bitmap);

    void onStartPay(Order order);

    void onTime(Order order, int i);

    void onTimeOut(Order order);
}
