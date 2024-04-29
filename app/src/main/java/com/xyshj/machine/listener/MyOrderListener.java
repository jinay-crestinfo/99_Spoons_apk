package com.xyshj.machine.listener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.app.R;
import com.oysb.utils.Loger;
import com.oysb.utils.video.TTSManager;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderListener;
import com.shj.biz.order.OrderPayType;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes2.dex */
public class MyOrderListener implements OrderListener {
    public static final String ACTION_NET_PRICE_UPDATED = "ACTION_NET_PRICE_UPDATED";
    public static final String ACTION_ORDER_CANCELED = "ACTION_ORDER_CANCELED";
    public static final String ACTION_ORDER_CANCELING = "ACTION_ORDER_CANCELING";
    public static final String ACTION_ORDER_CREATED = "ACTION_ORDER_CREATED";
    public static final String ACTION_ORDER_ERROR = "ACTION_ORDER_ERROR";
    public static final String ACTION_ORDER_MESSAGE = "ACTION_ORDER_MESSAGE";
    public static final String ACTION_ORDER_PAY_CANCELED = "ACTION_ORDER_PAY_CANCELED";
    public static final String ACTION_ORDER_PAY_START = "ACTION_ORDER_PAY_START";
    public static final String ACTION_ORDER_PAY_SUCCESS = "ACTION_ORDER_PAY_SUCCESS";
    public static final String ACTION_ORDER_QRIMAGE_CREATED = "ACTION_ORDER_QRIMAGE_CREATED";
    public static final String ACTION_ORDER_TIME = "ACTION_ORDER_TIME";
    public static final String ACTION_ORDER_TIMEOUT = "ACTION_ORDER_TIMEOUT";
    public static final String ACTION_PICKCODE_CHECK = "ACTION_PICKCODE_CHECK";
    public static final String ACTION_PICKCODE_ERROR = "ACTION_PICKCODE_ERROR";
    public static final String ACTION_PICKCODE_FINISHED = "ACTION_PICKCODE_FINISHED";

    @Override // com.shj.biz.order.OrderListener
    public void onPayFailed(Order order, OrderPayType orderPayType, String str) {
    }

    @Override // com.shj.biz.order.OrderListener
    public void onOrderCreated(Order order) {
        Intent intent = new Intent(ACTION_ORDER_CREATED);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("SALE", "11111111111111111111111111111");
        Loger.writeLog("SALES;Broadcast", "ACTION_ORDER_CREATED " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onOrderCanceling(Order order) {
        Intent intent = new Intent(ACTION_ORDER_CANCELING);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_ORDER_CANCELING " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onOrderCanceled(Order order) {
        Intent intent = new Intent(ACTION_ORDER_CANCELED);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_ORDER_CANCELED " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onError(Order order, OrderPayType orderPayType, int i, String str) {
        Intent intent = new Intent(ACTION_ORDER_ERROR);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        bundle.putString(IjkMediaPlayer.OnNativeInvokeListener.ARG_ERROR, str);
        bundle.putSerializable("payType", orderPayType);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_ORDER_ERROR " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onStartPay(Order order) {
        Intent intent = new Intent(ACTION_ORDER_PAY_START);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_ORDER_PAY_START " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onQrCodeImageCreated(Order order, OrderPayType orderPayType, Bitmap bitmap) {
        Loger.writeLog("SALES", "onQrCodeImageCreated 1");
        Intent intent = new Intent(ACTION_ORDER_QRIMAGE_CREATED);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        bundle.putSerializable("payType", orderPayType);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        Loger.writeLog("SALES", "type:" + orderPayType + " qrImage:" + bitmap);
        ShjAppBase.sysModel.pubBitmap(orderPayType, bitmap);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast;SALES", "ACTION_ORDER_QRIMAGE_CREATED " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onPayCanceled(Order order) {
        Intent intent = new Intent(ACTION_ORDER_PAY_CANCELED);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_ORDER_PAY_CANCELED " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onPaySuccess(Order order, OrderPayType orderPayType) {
        try {
            Intent intent = new Intent(ACTION_ORDER_PAY_SUCCESS);
            Bundle bundle = new Bundle();
            bundle.putSerializable("order", order);
            bundle.putSerializable("payType", orderPayType);
            intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
            ShjAppBase.sysApp.sendBroadcast(intent);
            TTSManager.addText(orderPayType.getName() + ShjAppHelper.getString(R.string.lab_paysuccess));
            Loger.writeLog("Broadcast", "ACTION_ORDER_PAY_SUCCESS " + bundle.toString());
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            e.printStackTrace();
        }
    }

    @Override // com.shj.biz.order.OrderListener
    public void onTime(Order order, int i) {
        Intent intent = new Intent(ACTION_ORDER_TIME);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        bundle.putInt("time", i);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_ORDER_TIME " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onTimeOut(Order order) {
        Intent intent = new Intent(ACTION_ORDER_TIMEOUT);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_ORDER_TIMEOUT " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onPickCodeChecked(String str, boolean z) {
        Intent intent = new Intent(ACTION_PICKCODE_CHECK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("code", str);
        bundle.putBoolean("valid", z);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_PICKCODE_CHECK " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onPickCodeOfferGoodsError(String str, String str2) {
        Intent intent = new Intent(ACTION_PICKCODE_ERROR);
        Bundle bundle = new Bundle();
        bundle.putSerializable("code", str);
        bundle.putString(IjkMediaPlayer.OnNativeInvokeListener.ARG_ERROR, str2);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_PICKCODE_ERROR " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onPickCodeOfferGoodsFinished(String str) {
        Intent intent = new Intent(ACTION_PICKCODE_FINISHED);
        Bundle bundle = new Bundle();
        bundle.putSerializable("code", str);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        Loger.writeLog("Broadcast", "ACTION_PICKCODE_FINISHED " + bundle.toString());
    }

    @Override // com.shj.biz.order.OrderListener
    public void onOrderMessage(String str) {
        Intent intent = new Intent(ACTION_ORDER_MESSAGE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("MESSAGE", str);
        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        ShjAppBase.sysApp.sendBroadcast(intent);
        TTSManager.addText(str);
    }

    @Override // com.shj.biz.order.OrderListener
    public void onNetPriceUpdated() {
        ShjAppBase.sysApp.sendBroadcast(new Intent(ACTION_NET_PRICE_UPDATED));
    }
}
