package com.shj.biz.order;

import android.graphics.Bitmap;
import android.support.media.ExifInterface;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.Loger;
import com.oysb.utils.zxing.encoding.EncodingHandler;
import com.oysb.xy.i.ReportListener;
import com.oysb.xy.net.NetManager;
import com.oysb.xy.net.report.Report;
import com.oysb.xy.net.report.ReportState;
import com.oysb.xy.net.report.Report_OnlinePay_Apply;
import com.shj.MoneyType;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.shj.biz.order.OrderPayManager;

/* loaded from: classes2.dex */
public class OrderPayItem_XY_NetPay extends OrderPayItem {
    MoneyType moneyType;
    String payId = "";
    Report_OnlinePay_Apply payReport = null;
    OrderPayManager.OrderPayResultListener payResultListener;
    private OrderPayManager.QrImageBuildListener qrImageBuildListener;

    @Override // com.shj.biz.order.OrderPayItem
    public void onTimer(int i) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void queryOrderPayResult(OrderPayManager.OrderPayResultListener orderPayResultListener) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public boolean shuldBuildQrImage() {
        return true;
    }

    public void onNewPlateformNetPayResult(boolean z) {
        try {
            if (this.isCanceled) {
                return;
            }
            Loger.writeLog("SALES", this.orderPayType + "扫码支付结果:" + z);
            if (!z || this.paySuccess) {
                return;
            }
            this.paySuccess = true;
            if (!ShjManager.isTestNoOfferGoodsOnPay() && !this.order.getGoodsCode().equals("-1") && this.order.getArgs().autoOfferGoods()) {
                ShjManager.setMoney(this.moneyType, this.order.getPrice(), this.order.getPayId());
            }
            this.payResultListener.onOrderPaySuccess(this);
        } catch (Exception unused) {
        }
    }

    public void onDpjModelNetPayResult(int i, String str) {
        try {
            if (i != 1) {
                if (i == 2) {
                    if (this.isCanceled) {
                        return;
                    }
                    Loger.writeLog("SALES", this.orderPayType + "扫码支付结果" + str);
                    String[] split = str.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                    if (split[0].equalsIgnoreCase(ExifInterface.GPS_DIRECTION_TRUE) && split[1].equalsIgnoreCase(this.payId) && split[2].equalsIgnoreCase(ExifInterface.GPS_DIRECTION_TRUE) && !this.paySuccess) {
                        this.order.setPayId(this.payId);
                        this.paySuccess = true;
                        if (!ShjManager.isTestNoOfferGoodsOnPay() && !this.order.getGoodsCode().equals("-1") && this.order.getArgs().autoOfferGoods()) {
                            ShjManager.setMoney(this.moneyType, this.order.getPrice(), this.order.getPayId());
                        }
                        this.payResultListener.onOrderPaySuccess(this);
                        return;
                    }
                    return;
                }
                if (i == 3 && str.split(VoiceWakeuperAidl.PARAMS_SEPARATE)[0].equalsIgnoreCase(ExifInterface.GPS_DIRECTION_TRUE)) {
                    Loger.writeLog("SALES", "取消" + this.orderPayType + "[" + this.payId + "]成功:" + str);
                    return;
                }
                return;
            }
            String[] split2 = str.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
            if (new String(split2[0]).equalsIgnoreCase(ExifInterface.GPS_DIRECTION_TRUE)) {
                this.payId = split2[1];
                this.order.setPayId(this.payId);
                this.order.setPayPrice(Integer.parseInt(split2[4]));
                if (ShjManager.getOrderManager().curOrderArgs != null) {
                    ShjManager.getOrderManager().curOrderArgs.putArg("payId", this.payId);
                }
                if (this.orderPayType == OrderPayType.YLJH && split2.length >= 7) {
                    ShjManager.setJhzfCode(Integer.parseInt(split2[6]));
                }
                if (this.orderPayType == OrderPayType.Face) {
                    if (split2.length >= 3 && split2[2] != null) {
                        Loger.writeLog("SALES", "获取" + this.orderPayType + "人脸支付订单编码成功：" + str);
                        this.order.getArgs().putArg("Zfb_facepayId", split2[2]);
                        ShjManager.getOrderListener().onOrderCreated(this.order);
                        return;
                    }
                    Loger.writeLog("SALES", "获取" + this.orderPayType + "人脸支付订单编码失败：" + str);
                    return;
                }
                if (this.orderPayType == OrderPayType.WxFace) {
                    if (split2.length >= 3 && split2[2] != null) {
                        Loger.writeLog("SALES", "获取" + this.orderPayType + "微信人脸支付订单编码成功：" + str);
                        this.order.getArgs().putArg("Wx_facepayId", split2[2]);
                        ShjManager.getOrderListener().onOrderCreated(this.order);
                        return;
                    }
                    Loger.writeLog("SALES", "获取" + this.orderPayType + "微信人脸支付订单编码失败：" + str);
                    return;
                }
                Bitmap bitmap = null;
                try {
                    bitmap = EncodingHandler.createQRCode(split2[2], 300);
                } catch (Exception unused) {
                }
                Loger.writeLog("SALES", "获取" + this.orderPayType + "二维码成功:" + str);
                this.qrImageBuildListener.onQrImageBuildFinished(this, bitmap);
            }
        } catch (Exception unused2) {
        }
    }

    public OrderPayItem_XY_NetPay() {
    }

    public OrderPayItem_XY_NetPay(OrderPayType orderPayType) {
        setOrderPayType(orderPayType);
    }

    /* renamed from: com.shj.biz.order.OrderPayItem_XY_NetPay$2 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
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
        switch (AnonymousClass2.$SwitchMap$com$shj$biz$order$OrderPayType[this.orderPayType.ordinal()]) {
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
        Report_OnlinePay_Apply report_OnlinePay_Apply = new Report_OnlinePay_Apply();
        this.payReport = report_OnlinePay_Apply;
        report_OnlinePay_Apply.setNeedResendCount(8);
        this.payReport.setReportListener(new ReportListener() { // from class: com.shj.biz.order.OrderPayItem_XY_NetPay.1
            AnonymousClass1() {
            }

            @Override // com.oysb.xy.i.ReportListener
            public void onReportResult(Report report) {
                if (report.getState() == ReportState.Failed) {
                    AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_QueryQrCode, "", "在线交易通讯失败：Report_OnlinePay_Apply");
                }
            }
        });
        String remark = this.order.getArgs().getRemark();
        if (remark.length() == 0) {
            remark = "0";
        }
        Report_OnlinePay_Apply report_OnlinePay_Apply2 = this.payReport;
        int orderPayType2ServerType = OrderManager.orderPayType2ServerType(this.orderPayType);
        String machineId = Shj.getMachineId();
        String goodsCode = this.order.getGoodsCode();
        String format = String.format("%03d", Integer.valueOf(this.order.getShelf()));
        int price = this.order.getPrice();
        report_OnlinePay_Apply2.setParams(1, orderPayType2ServerType, "NA", machineId, goodsCode, format, price, remark, "" + ShjManager.getOrderManager().getTradSn() + VoiceWakeuperAidl.PARAMS_SEPARATE + this.order.getUid() + VoiceWakeuperAidl.PARAMS_SEPARATE + this.order.getArgs().getArg("detail"));
        NetManager.appendReport(this.payReport);
    }

    /* renamed from: com.shj.biz.order.OrderPayItem_XY_NetPay$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements ReportListener {
        AnonymousClass1() {
        }

        @Override // com.oysb.xy.i.ReportListener
        public void onReportResult(Report report) {
            if (report.getState() == ReportState.Failed) {
                AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_QueryQrCode, "", "在线交易通讯失败：Report_OnlinePay_Apply");
            }
        }
    }

    private void cancelPayRequest() {
        Report_OnlinePay_Apply report_OnlinePay_Apply = this.payReport;
        if (report_OnlinePay_Apply != null) {
            report_OnlinePay_Apply.cancelReport();
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
