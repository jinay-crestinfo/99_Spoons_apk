package com.shj.biz;

import android.os.Handler;
import android.support.media.ExifInterface;
import com.oysb.utils.Loger;
import com.oysb.utils.RunnableEx;
import com.oysb.xy.i.OnlineCardPayResultListener;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderPayType;
import com.shj.device.lfpos.LfPos;
import com.tencent.wxpayface.WxfacePayCommonCode;

/* loaded from: classes2.dex */
public class ShjOnlineCardPayResultListener implements OnlineCardPayResultListener {
    static String gmessage;
    static boolean gsuccess;
    static String gtype;
    Handler handler = new Handler() { // from class: com.shj.biz.ShjOnlineCardPayResultListener.1
        AnonymousClass1() {
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjOnlineCardPayResultListener$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }
    }

    @Override // com.oysb.xy.i.OnlineCardPayResultListener
    public void onPayResult(String str, boolean z, String str2) {
        gsuccess = z;
        gmessage = str2;
        gtype = str;
        this.handler.post(new RunnableEx(null) { // from class: com.shj.biz.ShjOnlineCardPayResultListener.2
            AnonymousClass2(Object obj) {
                super(obj);
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                try {
                    if (ShjOnlineCardPayResultListener.gsuccess) {
                        if (ShjOnlineCardPayResultListener.gtype.equalsIgnoreCase("1")) {
                            if (LfPos.isSigned()) {
                                LfPos.onPayResult03(ShjOnlineCardPayResultListener.gsuccess, ShjOnlineCardPayResultListener.gmessage);
                            } else {
                                try {
                                    Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
                                    if (resentOrder == null) {
                                        return;
                                    }
                                    String[] split = ShjOnlineCardPayResultListener.gmessage.split("\\*");
                                    resentOrder.getArgs().putArg("CardId", ShjManager.getSetting("online_pay_card").toString());
                                    resentOrder.setPayId(split[1]);
                                    resentOrder.getArgs().putArg("CardRealPay", split[3]);
                                    resentOrder.setPayPrice(Integer.parseInt(split[3]));
                                    resentOrder.getArgs().putArg("CardBalance", split[4]);
                                    resentOrder.getArgs().putArg("CardPayCount", split[7]);
                                    resentOrder.setPayType(OrderPayType.ICCard);
                                    ShjManager.getOrderListener().onOrderMessage("会员卡支付成功，即将出货");
                                } catch (Exception unused) {
                                }
                            }
                        } else if (ShjOnlineCardPayResultListener.gtype.equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
                            try {
                                ShjManager.getBizShjListener()._onUpdateICCardMoney(Integer.parseInt(ShjOnlineCardPayResultListener.gmessage.split("\\*")[4]), "");
                            } catch (Exception e) {
                                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                            }
                        }
                    } else {
                        ShjManager.putData("tmp_lastOrderUid", "");
                        if (LfPos.isSigned()) {
                            LfPos.onPayResult03(ShjOnlineCardPayResultListener.gsuccess, ShjOnlineCardPayResultListener.gmessage);
                        } else if (ShjOnlineCardPayResultListener.gmessage != null && ShjOnlineCardPayResultListener.gmessage.length() > 0) {
                            if (ShjOnlineCardPayResultListener.gmessage.startsWith("1*")) {
                                ShjOnlineCardPayResultListener.gmessage = "刷卡次数受限制";
                            } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("2*")) {
                                ShjOnlineCardPayResultListener.gmessage = "余额不足";
                            } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("3*")) {
                                ShjOnlineCardPayResultListener.gmessage = "卡类型错误";
                            } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("4*")) {
                                ShjOnlineCardPayResultListener.gmessage = "卡未激活";
                            } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("5*")) {
                                ShjOnlineCardPayResultListener.gmessage = "无效卡(卡和机器不匹配 )";
                            } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("6*")) {
                                ShjOnlineCardPayResultListener.gmessage = "机器未开通刷卡业务";
                            } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("7*")) {
                                ShjOnlineCardPayResultListener.gmessage = "单次刷卡金额受限制";
                            } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("8*")) {
                                ShjOnlineCardPayResultListener.gmessage = "今日刷卡总金额受限制";
                            } else {
                                ShjOnlineCardPayResultListener.gmessage = "刷卡服务暂停，请稍后再试";
                            }
                            ShjManager.getOrderListener().onOrderMessage(ShjOnlineCardPayResultListener.gmessage);
                        }
                    }
                } catch (Exception e2) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
                }
                ShjOnlineCardPayResultListener.this.handler.removeCallbacksAndMessages(null);
            }
        });
    }

    /* renamed from: com.shj.biz.ShjOnlineCardPayResultListener$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 extends RunnableEx {
        AnonymousClass2(Object obj) {
            super(obj);
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            try {
                if (ShjOnlineCardPayResultListener.gsuccess) {
                    if (ShjOnlineCardPayResultListener.gtype.equalsIgnoreCase("1")) {
                        if (LfPos.isSigned()) {
                            LfPos.onPayResult03(ShjOnlineCardPayResultListener.gsuccess, ShjOnlineCardPayResultListener.gmessage);
                        } else {
                            try {
                                Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
                                if (resentOrder == null) {
                                    return;
                                }
                                String[] split = ShjOnlineCardPayResultListener.gmessage.split("\\*");
                                resentOrder.getArgs().putArg("CardId", ShjManager.getSetting("online_pay_card").toString());
                                resentOrder.setPayId(split[1]);
                                resentOrder.getArgs().putArg("CardRealPay", split[3]);
                                resentOrder.setPayPrice(Integer.parseInt(split[3]));
                                resentOrder.getArgs().putArg("CardBalance", split[4]);
                                resentOrder.getArgs().putArg("CardPayCount", split[7]);
                                resentOrder.setPayType(OrderPayType.ICCard);
                                ShjManager.getOrderListener().onOrderMessage("会员卡支付成功，即将出货");
                            } catch (Exception unused) {
                            }
                        }
                    } else if (ShjOnlineCardPayResultListener.gtype.equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
                        try {
                            ShjManager.getBizShjListener()._onUpdateICCardMoney(Integer.parseInt(ShjOnlineCardPayResultListener.gmessage.split("\\*")[4]), "");
                        } catch (Exception e) {
                            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                        }
                    }
                } else {
                    ShjManager.putData("tmp_lastOrderUid", "");
                    if (LfPos.isSigned()) {
                        LfPos.onPayResult03(ShjOnlineCardPayResultListener.gsuccess, ShjOnlineCardPayResultListener.gmessage);
                    } else if (ShjOnlineCardPayResultListener.gmessage != null && ShjOnlineCardPayResultListener.gmessage.length() > 0) {
                        if (ShjOnlineCardPayResultListener.gmessage.startsWith("1*")) {
                            ShjOnlineCardPayResultListener.gmessage = "刷卡次数受限制";
                        } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("2*")) {
                            ShjOnlineCardPayResultListener.gmessage = "余额不足";
                        } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("3*")) {
                            ShjOnlineCardPayResultListener.gmessage = "卡类型错误";
                        } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("4*")) {
                            ShjOnlineCardPayResultListener.gmessage = "卡未激活";
                        } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("5*")) {
                            ShjOnlineCardPayResultListener.gmessage = "无效卡(卡和机器不匹配 )";
                        } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("6*")) {
                            ShjOnlineCardPayResultListener.gmessage = "机器未开通刷卡业务";
                        } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("7*")) {
                            ShjOnlineCardPayResultListener.gmessage = "单次刷卡金额受限制";
                        } else if (ShjOnlineCardPayResultListener.gmessage.startsWith("8*")) {
                            ShjOnlineCardPayResultListener.gmessage = "今日刷卡总金额受限制";
                        } else {
                            ShjOnlineCardPayResultListener.gmessage = "刷卡服务暂停，请稍后再试";
                        }
                        ShjManager.getOrderListener().onOrderMessage(ShjOnlineCardPayResultListener.gmessage);
                    }
                }
            } catch (Exception e2) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
            }
            ShjOnlineCardPayResultListener.this.handler.removeCallbacksAndMessages(null);
        }
    }
}
