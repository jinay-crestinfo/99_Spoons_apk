package com.shj.biz.order;

import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.zxing.encoding.EncodingHandler;
import com.shj.MoneyType;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.shj.biz.order.OrderPayManager;
import com.shj.biz.yg.YGDBHelper;
import com.shj.setting.NetAddress.NetAddress;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class OrderPayItem_AME_NetPay extends OrderPayItem {
    MoneyType moneyType;
    OrderPayManager.OrderPayResultListener payResultListener;
    private OrderPayManager.QrImageBuildListener qrImageBuildListener;
    String payId = "";
    boolean qrcodeCreated = false;
    RequestItem resultRequestItem = null;

    @Override // com.shj.biz.order.OrderPayItem
    public void onTimer(int i) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public boolean shuldBuildQrImage() {
        return true;
    }

    public OrderPayItem_AME_NetPay() {
    }

    public OrderPayItem_AME_NetPay(OrderPayType orderPayType) {
        setOrderPayType(orderPayType);
    }

    /* renamed from: com.shj.biz.order.OrderPayItem_AME_NetPay$3 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$biz$order$OrderPayType;

        static {
            int[] iArr = new int[OrderPayType.values().length];
            $SwitchMap$com$shj$biz$order$OrderPayType = iArr;
            try {
                iArr[OrderPayType.WEIXIN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.ZFB.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.JD.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YL.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YL6.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YLJH.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void setOrderPayType(OrderPayType orderPayType) {
        this.orderPayType = orderPayType;
        switch (AnonymousClass3.$SwitchMap$com$shj$biz$order$OrderPayType[this.orderPayType.ordinal()]) {
            case 1:
                this.moneyType = MoneyType.Weixin;
                return;
            case 2:
                this.moneyType = MoneyType.Zfb;
                return;
            case 3:
                this.moneyType = MoneyType.JD;
                return;
            case 4:
            case 5:
                this.moneyType = MoneyType.YL;
                return;
            case 6:
                this.moneyType = MoneyType.YL;
                return;
            default:
                return;
        }
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void buildQrImage(OrderPayManager.QrImageBuildListener qrImageBuildListener) {
        this.qrImageBuildListener = qrImageBuildListener;
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("jqbh", Shj.getMachineId());
            jSONObject.put("amount", this.order.getPrice());
            jSONObject.put(YGDBHelper.COLUM_ORDERID, this.order.getUid());
            JSONArray jSONArray = new JSONObject(this.order.getArgs().getArg("detail")).getJSONArray("goods");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                jSONObject2.put("name", Shj.getShelfInfo(Integer.valueOf(Integer.parseInt(jSONObject2.getString("hdbh")))).getGoodsName());
            }
            jSONObject.put(SpeechEvent.KEY_EVENT_RECORD_DATA, jSONArray);
            RequestItem requestItem = new RequestItem(NetAddress.queryQrcodeUrl(), jSONObject, "POST");
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.order.OrderPayItem_AME_NetPay.1
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                AnonymousClass1() {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i2, String str, Throwable th) {
                    OrderPayItem_AME_NetPay.this.qrImageBuildListener.onQrImageBuildFinished(OrderPayItem_AME_NetPay.this, EncodingHandler.createQRCode("qrcode create with error", 150));
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i2, String str) {
                    try {
                        JSONObject jSONObject3 = new JSONObject(str);
                        String string = jSONObject3.getString("qrcode");
                        OrderPayItem_AME_NetPay.this.payId = jSONObject3.getString("payid");
                        OrderPayItem_AME_NetPay.this.order.setPayId(jSONObject3.getString("payid"));
                        OrderPayItem_AME_NetPay.this.qrImageBuildListener.onQrImageBuildFinished(OrderPayItem_AME_NetPay.this, EncodingHandler.createQRCode(string, 150));
                        OrderPayItem_AME_NetPay.this.qrcodeCreated = true;
                    } catch (Exception unused) {
                    }
                    return true;
                }
            });
            RequestHelper.request(requestItem);
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.shj.biz.order.OrderPayItem_AME_NetPay$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass1() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i2, String str, Throwable th) {
            OrderPayItem_AME_NetPay.this.qrImageBuildListener.onQrImageBuildFinished(OrderPayItem_AME_NetPay.this, EncodingHandler.createQRCode("qrcode create with error", 150));
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i2, String str) {
            try {
                JSONObject jSONObject3 = new JSONObject(str);
                String string = jSONObject3.getString("qrcode");
                OrderPayItem_AME_NetPay.this.payId = jSONObject3.getString("payid");
                OrderPayItem_AME_NetPay.this.order.setPayId(jSONObject3.getString("payid"));
                OrderPayItem_AME_NetPay.this.qrImageBuildListener.onQrImageBuildFinished(OrderPayItem_AME_NetPay.this, EncodingHandler.createQRCode(string, 150));
                OrderPayItem_AME_NetPay.this.qrcodeCreated = true;
            } catch (Exception unused) {
            }
            return true;
        }
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void queryOrderPayResult(OrderPayManager.OrderPayResultListener orderPayResultListener) {
        this.payResultListener = orderPayResultListener;
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("jqbh", Shj.getMachineId());
            jSONObject.put("payid", this.payId);
            jSONObject.put(YGDBHelper.COLUM_ORDERID, this.order.getUid());
            RequestItem requestItem = new RequestItem(NetAddress.queryQrcodeResultUrl(), jSONObject, "POST");
            this.resultRequestItem = requestItem;
            requestItem.setRequestMaxCount(100);
            this.resultRequestItem.setRepeatDelay(2000);
            this.resultRequestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.order.OrderPayItem_AME_NetPay.2
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                AnonymousClass2() {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                    try {
                        if (!new JSONObject(str).getString("code").equals("H0000")) {
                            return false;
                        }
                        OrderPayItem_AME_NetPay.this.order.setPayId(OrderPayItem_AME_NetPay.this.payId);
                        ShjManager.getOrderManager().driverOfferLineOrder(OrderPayItem_AME_NetPay.this.orderPayType, OrderPayItem_AME_NetPay.this.order.getUid(), OrderPayItem_AME_NetPay.this.payId);
                        OrderPayItem_AME_NetPay.this.payResultListener.onOrderPaySuccess(OrderPayItem_AME_NetPay.this);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            });
            RequestHelper.request(this.resultRequestItem);
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.shj.biz.order.OrderPayItem_AME_NetPay$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass2() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            try {
                if (!new JSONObject(str).getString("code").equals("H0000")) {
                    return false;
                }
                OrderPayItem_AME_NetPay.this.order.setPayId(OrderPayItem_AME_NetPay.this.payId);
                ShjManager.getOrderManager().driverOfferLineOrder(OrderPayItem_AME_NetPay.this.orderPayType, OrderPayItem_AME_NetPay.this.order.getUid(), OrderPayItem_AME_NetPay.this.payId);
                OrderPayItem_AME_NetPay.this.payResultListener.onOrderPaySuccess(OrderPayItem_AME_NetPay.this);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private void cancelPayRequest() {
        RequestItem requestItem = this.resultRequestItem;
        if (requestItem != null) {
            requestItem.setCanceled(true);
        }
    }

    @Override // com.shj.biz.order.OrderPayItem
    public boolean cancel() {
        this.payResultListener = null;
        if (this.paySuccess) {
            return false;
        }
        cancelPayRequest();
        this.isCanceled = true;
        return true;
    }
}
