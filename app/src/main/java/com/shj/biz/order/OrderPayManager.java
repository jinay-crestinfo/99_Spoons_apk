package com.shj.biz.order;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import com.oysb.utils.Loger;
import com.oysb.utils.RunnableEx;
import com.shj.biz.ShjManager;
import com.shj.biz.goods.SalesDBHelper;
import com.tencent.wxpayface.WxfacePayCommonCode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class OrderPayManager {
    public static final int EVENT_CLEAR_HANDLER = 2003;
    public static final int EVENT_PAY_MSG_TIMEOUT = 1003;
    public static final int EVENT_PAY_MSG_UPDATE = 1002;
    public static final int EVENT_QRCODE_CREATED = 1000;
    public static final int EVENT_QRCODE_ERROR = 1001;
    protected Order order;
    protected Timer timer;
    private List<OrderPayItem> mapPayItems = new ArrayList();
    int timeOut = 120;
    MyRunnable myRunnable = null;
    QrImageBuildListener qrImageBuildlistener = new QrImageBuildListener() { // from class: com.shj.biz.order.OrderPayManager.1
        AnonymousClass1() {
        }

        @Override // com.shj.biz.order.OrderPayManager.QrImageBuildListener
        public void onQrImageBuildFinished(OrderPayItem orderPayItem, Bitmap bitmap) {
            Loger.writeLog("SALES", "qrImageBuildlistener 1");
            if (OrderPayManager.this.mapPayItems.contains(orderPayItem)) {
                Message obtain = Message.obtain();
                obtain.what = 1000;
                obtain.obj = new Object[]{orderPayItem, bitmap};
                OrderPayManager.this.handler.sendMessage(obtain);
                OrderPayManager.this.myRunnable = new MyRunnable(orderPayItem);
                OrderPayManager.this.handler.postDelayed(OrderPayManager.this.myRunnable, 8000L);
            }
        }
    };
    OrderPayResultListener payResultListener = new OrderPayResultListener() { // from class: com.shj.biz.order.OrderPayManager.2
        AnonymousClass2() {
        }

        @Override // com.shj.biz.order.OrderPayManager.OrderPayResultListener
        public void onOrderPaySuccess(OrderPayItem orderPayItem) {
            if (OrderPayManager.this.mapPayItems.contains(orderPayItem)) {
                Loger.writeLog("SHJ;SALES", orderPayItem.getOrderPayType() + "支付成功");
                OrderPayManager.this.order.setPayType(orderPayItem.getOrderPayType());
                OrderPayManager.this.order.setStatus(1);
                OrderPayManager.this.cancelWaitTimer();
                OrderPayManager.this._cancelPayItems();
                OrderListener orderListener = ShjManager.getOrderListener();
                if (orderListener != null) {
                    orderListener.onPaySuccess(OrderPayManager.this.order, orderPayItem.getOrderPayType());
                }
                try {
                    ShjManager.getSalesDBHelper().addData(new SalesDBHelper.SaleItem(SalesDBHelper.SaleItem.TYPE_PAY, orderPayItem.getOrderPayType().getName(), OrderPayManager.this.order.getPrice(), "", 1, new Date()));
                } catch (Exception unused) {
                }
            }
        }

        @Override // com.shj.biz.order.OrderPayManager.OrderPayResultListener
        public void onError(OrderPayItem orderPayItem, String str) {
            OrderListener orderListener;
            if (OrderPayManager.this.mapPayItems.contains(orderPayItem) && (orderListener = ShjManager.getOrderListener()) != null) {
                orderListener.onError(OrderPayManager.this.order, orderPayItem.getOrderPayType(), -1, str);
            }
        }
    };
    Handler handler = new Handler() { // from class: com.shj.biz.order.OrderPayManager.3
        AnonymousClass3() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                int i = message.what;
                if (i == 1000) {
                    Loger.writeLog("SALES", "EVENT_QRCODE_CREATED");
                    Object[] objArr = (Object[]) message.obj;
                    OrderPayItem orderPayItem = (OrderPayItem) objArr[0];
                    Bitmap bitmap = (Bitmap) objArr[1];
                    OrderListener orderListener = ShjManager.getOrderListener();
                    if (orderListener != null) {
                        orderListener.onQrCodeImageCreated(OrderPayManager.this.order, orderPayItem.getOrderPayType(), bitmap);
                    }
                } else if (i == 1002) {
                    Iterator it = OrderPayManager.this.mapPayItems.iterator();
                    while (it.hasNext()) {
                        ((OrderPayItem) it.next()).onTimer(OrderPayManager.this.timeOut);
                    }
                    OrderListener orderListener2 = ShjManager.getOrderListener();
                    if (orderListener2 != null) {
                        orderListener2.onTime(OrderPayManager.this.order, OrderPayManager.this.timeOut);
                    }
                } else if (i == 1003) {
                    OrderPayManager.this.cancelWaitTimer();
                    OrderPayManager.this._cancelPayItems();
                    OrderListener orderListener3 = ShjManager.getOrderListener();
                    if (orderListener3 != null) {
                        orderListener3.onTimeOut(OrderPayManager.this.order);
                    }
                }
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
            super.handleMessage(message);
        }
    };

    /* loaded from: classes2.dex */
    public interface OrderPayResultListener {
        void onError(OrderPayItem orderPayItem, String str);

        void onOrderPaySuccess(OrderPayItem orderPayItem);
    }

    /* loaded from: classes2.dex */
    public interface QrImageBuildListener {
        void onQrImageBuildFinished(OrderPayItem orderPayItem, Bitmap bitmap);
    }

    public void onDpjModelNetPayResult(int i, int i2, String str) {
        try {
            for (OrderPayItem orderPayItem : this.mapPayItems) {
                try {
                    if ((orderPayItem instanceof OrderPayItem_XY_NetPay) && orderPayItem.getOrderPayType().getIndex() == i2) {
                        ((OrderPayItem_XY_NetPay) orderPayItem).onDpjModelNetPayResult(i, str);
                    }
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
            }
        } catch (Exception e2) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
        }
    }

    public void onNewPlateformNetPayResult(int i, boolean z) {
        try {
            for (OrderPayItem orderPayItem : this.mapPayItems) {
                try {
                    if ((orderPayItem instanceof OrderPayItem_XY_NetPay) && orderPayItem.getOrderPayType().getIndex() == i) {
                        ((OrderPayItem_XY_NetPay) orderPayItem).onNewPlateformNetPayResult(z);
                    }
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
            }
        } catch (Exception e2) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class MyRunnable extends RunnableEx {
        public MyRunnable(Object obj) {
            super(obj);
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            OrderPayItem orderPayItem = (OrderPayItem) getObj();
            if (orderPayItem != null && !orderPayItem.isCanceled()) {
                orderPayItem.queryOrderPayResult(OrderPayManager.this.payResultListener);
            }
            OrderPayManager.this.handler.removeCallbacks(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.order.OrderPayManager$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements QrImageBuildListener {
        AnonymousClass1() {
        }

        @Override // com.shj.biz.order.OrderPayManager.QrImageBuildListener
        public void onQrImageBuildFinished(OrderPayItem orderPayItem, Bitmap bitmap) {
            Loger.writeLog("SALES", "qrImageBuildlistener 1");
            if (OrderPayManager.this.mapPayItems.contains(orderPayItem)) {
                Message obtain = Message.obtain();
                obtain.what = 1000;
                obtain.obj = new Object[]{orderPayItem, bitmap};
                OrderPayManager.this.handler.sendMessage(obtain);
                OrderPayManager.this.myRunnable = new MyRunnable(orderPayItem);
                OrderPayManager.this.handler.postDelayed(OrderPayManager.this.myRunnable, 8000L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.order.OrderPayManager$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OrderPayResultListener {
        AnonymousClass2() {
        }

        @Override // com.shj.biz.order.OrderPayManager.OrderPayResultListener
        public void onOrderPaySuccess(OrderPayItem orderPayItem) {
            if (OrderPayManager.this.mapPayItems.contains(orderPayItem)) {
                Loger.writeLog("SHJ;SALES", orderPayItem.getOrderPayType() + "支付成功");
                OrderPayManager.this.order.setPayType(orderPayItem.getOrderPayType());
                OrderPayManager.this.order.setStatus(1);
                OrderPayManager.this.cancelWaitTimer();
                OrderPayManager.this._cancelPayItems();
                OrderListener orderListener = ShjManager.getOrderListener();
                if (orderListener != null) {
                    orderListener.onPaySuccess(OrderPayManager.this.order, orderPayItem.getOrderPayType());
                }
                try {
                    ShjManager.getSalesDBHelper().addData(new SalesDBHelper.SaleItem(SalesDBHelper.SaleItem.TYPE_PAY, orderPayItem.getOrderPayType().getName(), OrderPayManager.this.order.getPrice(), "", 1, new Date()));
                } catch (Exception unused) {
                }
            }
        }

        @Override // com.shj.biz.order.OrderPayManager.OrderPayResultListener
        public void onError(OrderPayItem orderPayItem, String str) {
            OrderListener orderListener;
            if (OrderPayManager.this.mapPayItems.contains(orderPayItem) && (orderListener = ShjManager.getOrderListener()) != null) {
                orderListener.onError(OrderPayManager.this.order, orderPayItem.getOrderPayType(), -1, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.order.OrderPayManager$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends Handler {
        AnonymousClass3() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                int i = message.what;
                if (i == 1000) {
                    Loger.writeLog("SALES", "EVENT_QRCODE_CREATED");
                    Object[] objArr = (Object[]) message.obj;
                    OrderPayItem orderPayItem = (OrderPayItem) objArr[0];
                    Bitmap bitmap = (Bitmap) objArr[1];
                    OrderListener orderListener = ShjManager.getOrderListener();
                    if (orderListener != null) {
                        orderListener.onQrCodeImageCreated(OrderPayManager.this.order, orderPayItem.getOrderPayType(), bitmap);
                    }
                } else if (i == 1002) {
                    Iterator it = OrderPayManager.this.mapPayItems.iterator();
                    while (it.hasNext()) {
                        ((OrderPayItem) it.next()).onTimer(OrderPayManager.this.timeOut);
                    }
                    OrderListener orderListener2 = ShjManager.getOrderListener();
                    if (orderListener2 != null) {
                        orderListener2.onTime(OrderPayManager.this.order, OrderPayManager.this.timeOut);
                    }
                } else if (i == 1003) {
                    OrderPayManager.this.cancelWaitTimer();
                    OrderPayManager.this._cancelPayItems();
                    OrderListener orderListener3 = ShjManager.getOrderListener();
                    if (orderListener3 != null) {
                        orderListener3.onTimeOut(OrderPayManager.this.order);
                    }
                }
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
            super.handleMessage(message);
        }
    }

    public void setTimeOut(int i) {
        this.timeOut = i;
    }

    public void cancelWaitTimer() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
    }

    public void resetWaitTimer() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        this.timer = new Timer();
        this.handler.sendEmptyMessage(1002);
        this.timer.schedule(new TimerTask() { // from class: com.shj.biz.order.OrderPayManager.4
            AnonymousClass4() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    if (OrderPayManager.this.timeOut < 0) {
                        return;
                    }
                    OrderPayManager orderPayManager = OrderPayManager.this;
                    orderPayManager.timeOut--;
                    OrderPayManager.this.handler.sendEmptyMessage(1002);
                    if (OrderPayManager.this.timeOut <= 0) {
                        OrderPayManager.this.handler.sendEmptyMessage(1003);
                    }
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
            }
        }, 1000L, 1000L);
    }

    /* renamed from: com.shj.biz.order.OrderPayManager$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 extends TimerTask {
        AnonymousClass4() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                if (OrderPayManager.this.timeOut < 0) {
                    return;
                }
                OrderPayManager orderPayManager = OrderPayManager.this;
                orderPayManager.timeOut--;
                OrderPayManager.this.handler.sendEmptyMessage(1002);
                if (OrderPayManager.this.timeOut <= 0) {
                    OrderPayManager.this.handler.sendEmptyMessage(1003);
                }
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
        }
    }

    public boolean hasLFPosPayItem() {
        return this.mapPayItems.contains(OrderPayItem_LFPos.getInstance());
    }

    public void addOrderPayItem(OrderPayItem orderPayItem) {
        if (orderPayItem != null) {
            this.mapPayItems.add(orderPayItem);
        }
    }

    public void startPay(Order order) {
        if (this.order != null) {
            return;
        }
        Loger.writeLog("SALES", "启动支付监听程序");
        this.order = order;
        OrderListener orderListener = ShjManager.getOrderListener();
        if (orderListener != null) {
            orderListener.onStartPay(order);
        }
        for (OrderPayItem orderPayItem : this.mapPayItems) {
            orderPayItem.setOrder(order);
            if (orderPayItem.shuldBuildQrImage()) {
                orderPayItem.buildQrImage(this.qrImageBuildlistener);
            } else {
                orderPayItem.queryOrderPayResult(this.payResultListener);
            }
        }
        if (order.getPayTypes().contains(OrderPayType.Face) || order.getPayTypes().contains(OrderPayType.WxFace)) {
            Timer timer = this.timer;
            if (timer != null) {
                timer.cancel();
                this.timer = null;
                return;
            }
            return;
        }
        resetWaitTimer();
    }

    public void cancelPay() {
        if (this.order != null) {
            Loger.writeLog("SALES", "正取消支付");
            cancelWaitTimer();
            _cancelPayItems();
            try {
                OrderListener orderListener = ShjManager.getOrderListener();
                if (orderListener != null) {
                    orderListener.onPayCanceled(this.order);
                }
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
            this.order = null;
        }
    }

    void _cancelPayItems() {
        try {
            ArrayList<OrderPayItem> arrayList = new ArrayList();
            arrayList.addAll(this.mapPayItems);
            this.mapPayItems.clear();
            for (OrderPayItem orderPayItem : arrayList) {
                if (!orderPayItem.isPaySuccess()) {
                    Loger.writeLog("SALES", "正在取消支付:" + orderPayItem.getOrderPayType().getName());
                    try {
                        orderPayItem.cancel();
                        orderPayItem.release();
                    } catch (Exception e) {
                        Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                    }
                }
            }
            arrayList.clear();
        } catch (Exception e2) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
        }
    }
}
