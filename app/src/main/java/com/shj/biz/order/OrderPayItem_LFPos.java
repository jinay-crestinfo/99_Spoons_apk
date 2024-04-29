package com.shj.biz.order;

import com.oysb.utils.Loger;
import com.shj.MoneyType;
import com.shj.biz.ShjManager;
import com.shj.biz.order.OrderPayManager;
import com.shj.device.lfpos.LfPos;
import com.shj.device.lfpos.LfPosListener;
import com.tencent.wxpayface.WxfacePayCommonCode;

/* loaded from: classes2.dex */
public class OrderPayItem_LFPos extends OrderPayItem_ICCard {
    static OrderPayItem_LFPos posPayInstance = new OrderPayItem_LFPos();
    OrderPayManager.OrderPayResultListener payResultListener;
    boolean isQuerying = false;
    String payId = "";
    boolean timeOuted = true;
    MyLfPosListener lfPosListener = new MyLfPosListener();

    public void addPayType(String str) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void buildQrImage(OrderPayManager.QrImageBuildListener qrImageBuildListener) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public boolean cancel() {
        return true;
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void onTimer(int i) {
    }

    @Override // com.shj.biz.order.OrderPayItem_ICCard
    public void queryCardBanlance(int i) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void queryOrderPayResult(OrderPayManager.OrderPayResultListener orderPayResultListener) {
    }

    @Override // com.shj.biz.order.OrderPayItem_ICCard
    public void refund(long j, int i) {
    }

    public void setPayType(String str) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public boolean shuldBuildQrImage() {
        return false;
    }

    @Override // com.shj.biz.order.OrderPayItem_ICCard
    public void topUp(long j, int i) {
    }

    void updateOnlineCardInfo() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class MyLfPosListener implements LfPosListener {
        MyLfPosListener() {
        }

        @Override // com.shj.device.lfpos.LfPosListener
        public void onPayResult(boolean z, String str, String str2, int i, String str3) {
            try {
                if (z) {
                    OrderPayItem_LFPos.this.order.setPayId(str2);
                    OrderPayItem_LFPos.this.paySuccess = true;
                    MoneyType moneyType = MoneyType.ECard;
                    char c = 65535;
                    switch (str.hashCode()) {
                        case 1537:
                            if (str.equals("01")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 1538:
                            if (str.equals("02")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 1539:
                            if (str.equals("03")) {
                                c = 2;
                                break;
                            }
                            break;
                    }
                    if (c == 0 || c == 1) {
                        moneyType = MoneyType.ECard;
                        OrderPayItem_LFPos.this.orderPayType = OrderPayType.ECard;
                    } else if (c == 2) {
                        moneyType = MoneyType.ICCard;
                        OrderPayItem_LFPos.this.orderPayType = OrderPayType.ICCard;
                    }
                    OrderPayItem_LFPos.this.order.setPayType(OrderPayItem_LFPos.this.orderPayType);
                    ShjManager.setMoney(moneyType, OrderPayItem_LFPos.this.order.getPrice(), OrderPayItem_LFPos.this.order.getPayId());
                    OrderPayItem_LFPos.this.payResultListener.onOrderPaySuccess(OrderPayItem_LFPos.this);
                    return;
                }
                if (str3 == null || str3.length() <= 0) {
                    return;
                }
                if (str3.startsWith("ct*0")) {
                    str3 = "您还不是会员";
                } else {
                    if (!str3.startsWith("1*")) {
                        if (str3.startsWith("2*")) {
                            str3 = "余额不足";
                        } else if (!str3.startsWith("3*") && !str3.startsWith("4*") && !str3.startsWith("5*") && !str3.startsWith("6*")) {
                            if (str3.startsWith("7*") || str3.startsWith("8*") || str3.startsWith("9*")) {
                                str3 = "卡和机器类型不匹配";
                            }
                        }
                    }
                    str3 = "刷卡次数受限制";
                }
                ShjManager.getOrderListener().onOrderMessage(str3);
            } catch (Exception e) {
                Loger.writeException("SALES", e);
                LfPos.cancelPay();
            }
        }

        @Override // com.shj.device.lfpos.LfPosListener
        public void onQeryCardBalanceResult(boolean z, String str, String str2, int i, String str3) {
            try {
                ShjManager.getBizShjListener()._onUpdateICCardMoney(Integer.parseInt(str3.substring(2)), str3);
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
        }
    }

    public static OrderPayItem getInstance() {
        return posPayInstance;
    }
}
