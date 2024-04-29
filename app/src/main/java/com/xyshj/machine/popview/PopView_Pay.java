package com.xyshj.machine.popview;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hanlu.toolsdk.BqlManager;
import com.oysb.utils.Loger;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.view.BasePopView;
import com.shj.MoneyType;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderArgs;
import com.shj.biz.order.OrderPayType;
import com.shj.device.cardreader.MdbReader_BDT;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.machine.R;
import com.xyshj.machine.app.SysApp;
import com.xyshj.machine.app.VmdHelper;
import com.xyshj.machine.facepay.FacePayResultListering;
import com.xyshj.machine.facepay.InitListenring;
import com.xyshj.machine.facepay.WxFacePayHelper;
import com.xyshj.machine.listener.MyMoneyListener;
import com.xyshj.machine.listener.MyOrderListener;
import com.xyshj.machine.popview.PopView_Info;
import com.xyshj.machine.tools.NetConnectTipUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class PopView_Pay extends BasePopView implements View.OnClickListener {
    static final int MSG_CANCEL_DETAIL = 2026;
    static final int MSG_SMILE_PAY = 2025;
    RadioButton card;
    RadioButton cash;
    TextView detail;
    RadioButton face;
    private Order faceOrder;
    private boolean isFacePayInstalled;
    private boolean isFacePayOpen;
    private boolean isFacePayOrderSubmit;
    RadioButton jd;
    TextView label;
    TextView name;
    RadioGroup payTypes;
    RadioGroup payTypes_face;
    RadioButton pay_other;
    ImageView qrImage;
    TextView qrcodelabel;
    private boolean releaseWxpayface;
    TextView timeoutinfo;
    TextView totalPrice;
    RadioButton wx;
    private WxFacePayHelper wxFacePayHelper;
    RadioButton zfb;
    boolean isFirstShow = false;
    long clickTime = 0;
    Timer facetimer = null;
    int facetimerCount = 60;

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.popview_pay, (ViewGroup) null);
        inflate.setOnClickListener(this);
        this.payTypes_face = (RadioGroup) inflate.findViewById(R.id.payTypes_face);
        this.payTypes = (RadioGroup) inflate.findViewById(R.id.payTypes);
        this.pay_other = (RadioButton) inflate.findViewById(R.id.pay_other);
        this.wx = (RadioButton) inflate.findViewById(R.id.pay_wx);
        this.zfb = (RadioButton) inflate.findViewById(R.id.pay_zfb);
        this.jd = (RadioButton) inflate.findViewById(R.id.pay_jd);
        this.card = (RadioButton) inflate.findViewById(R.id.pay_card);
        this.cash = (RadioButton) inflate.findViewById(R.id.pay_cash);
        this.face = (RadioButton) inflate.findViewById(R.id.pay_face);
        this.label = (TextView) inflate.findViewById(R.id.label);
        this.name = (TextView) inflate.findViewById(R.id.name);
        this.detail = (TextView) inflate.findViewById(R.id.detail);
        this.totalPrice = (TextView) inflate.findViewById(R.id.totalPrice);
        this.timeoutinfo = (TextView) inflate.findViewById(R.id.timeoutinfo);
        inflate.findViewById(R.id.bt_back).setOnClickListener(this);
        inflate.findViewById(R.id.bt_buy).setOnClickListener(this);
        this.qrImage = (ImageView) inflate.findViewById(R.id.qrcode);
        this.qrcodelabel = (TextView) inflate.findViewById(R.id.qrcodelabel);
        this.isFirstShow = true;
        ((RadioGroup) inflate.findViewById(R.id.payTypes_face)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.xyshj.machine.popview.PopView_Pay.1
            AnonymousClass1() {
            }

            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.pay_face) {
                    PopView_Pay.this.qrImage.setImageResource(R.drawable.img_facepay);
                    PopView_Pay.this.qrImage.setClickable(true);
                } else {
                    if (i != R.id.pay_other) {
                        return;
                    }
                    PopView_Pay.this.payTypes_face.setVisibility(8);
                    PopView_Pay.this.payTypes.setVisibility(0);
                    PopView_Pay.this.qrImage.setClickable(false);
                    synchronized (this) {
                        PopView_Pay.this.facetimer.cancel();
                        PopView_Pay.this.facetimer = null;
                    }
                    PopView_Pay.this.updateOtherPayView();
                }
            }
        });
        ((RadioGroup) inflate.findViewById(R.id.payTypes)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.xyshj.machine.popview.PopView_Pay.2
            AnonymousClass2() {
            }

            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (MdbReader_BDT.isEnabled()) {
                    MdbReader_BDT.get().cancel();
                }
                switch (i) {
                    case R.id.pay_card /* 2131231172 */:
                        PopView_Pay.this.findViewById(R.id.moneybar).setVisibility(0);
                        Loger.writeLog("SHJ", "更改为刷卡支付");
                        PopView_Pay.this.qrImage.setImageResource(R.drawable.img_card);
                        PopView_Pay.this.qrcodelabel.setText(ShjAppHelper.getString(R.string.swipcard));
                        MdbReader_BDT.setEnabled(true);
                        break;
                    case R.id.pay_cash /* 2131231173 */:
                        PopView_Pay.this.findViewById(R.id.moneybar).setVisibility(0);
                        Loger.writeLog("SHJ", "更改为现金支付");
                        MdbReader_BDT.setEnabled(true);
                        PopView_Pay.this.qrImage.setImageResource(R.drawable.img_cash);
                        PopView_Pay.this.qrcodelabel.setText(ShjAppHelper.getString(R.string.paybumoney));
                        break;
                    case R.id.pay_face /* 2131231174 */:
                        PopView_Pay.this.payTypes_face.setVisibility(0);
                        PopView_Pay.this.payTypes.setVisibility(8);
                        PopView_Pay.this.qrImage.setImageResource(R.drawable.img_facepay);
                        PopView_Pay.this.qrImage.setClickable(true);
                        break;
                    case R.id.pay_wx /* 2131231179 */:
                    case R.id.pay_zfb /* 2131231180 */:
                        PopView_Pay.this.findViewById(R.id.moneybar).setVisibility(8);
                        PopView_Pay.this.qrImage.setImageResource(R.drawable.img_bg_gray);
                        PopView_Pay.this.qrcodelabel.setText(ShjAppHelper.getString(R.string.creatingqrcode));
                        break;
                }
                Loger.writeLog("SHJ", "isFirstShow:" + PopView_Pay.this.isFirstShow);
                if (!PopView_Pay.this.isFirstShow) {
                    PopView_Pay.this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Pay.2.1
                        AnonymousClass1() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_Pay.this.submitOrder();
                        }
                    }, 1000L);
                } else {
                    PopView_Pay.this.isFirstShow = false;
                }
            }

            /* renamed from: com.xyshj.machine.popview.PopView_Pay$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements Runnable {
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    PopView_Pay.this.submitOrder();
                }
            }
        });
        return inflate;
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements RadioGroup.OnCheckedChangeListener {
        AnonymousClass1() {
        }

        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (i == R.id.pay_face) {
                PopView_Pay.this.qrImage.setImageResource(R.drawable.img_facepay);
                PopView_Pay.this.qrImage.setClickable(true);
            } else {
                if (i != R.id.pay_other) {
                    return;
                }
                PopView_Pay.this.payTypes_face.setVisibility(8);
                PopView_Pay.this.payTypes.setVisibility(0);
                PopView_Pay.this.qrImage.setClickable(false);
                synchronized (this) {
                    PopView_Pay.this.facetimer.cancel();
                    PopView_Pay.this.facetimer = null;
                }
                PopView_Pay.this.updateOtherPayView();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.popview.PopView_Pay$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements RadioGroup.OnCheckedChangeListener {
        AnonymousClass2() {
        }

        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (MdbReader_BDT.isEnabled()) {
                MdbReader_BDT.get().cancel();
            }
            switch (i) {
                case R.id.pay_card /* 2131231172 */:
                    PopView_Pay.this.findViewById(R.id.moneybar).setVisibility(0);
                    Loger.writeLog("SHJ", "更改为刷卡支付");
                    PopView_Pay.this.qrImage.setImageResource(R.drawable.img_card);
                    PopView_Pay.this.qrcodelabel.setText(ShjAppHelper.getString(R.string.swipcard));
                    MdbReader_BDT.setEnabled(true);
                    break;
                case R.id.pay_cash /* 2131231173 */:
                    PopView_Pay.this.findViewById(R.id.moneybar).setVisibility(0);
                    Loger.writeLog("SHJ", "更改为现金支付");
                    MdbReader_BDT.setEnabled(true);
                    PopView_Pay.this.qrImage.setImageResource(R.drawable.img_cash);
                    PopView_Pay.this.qrcodelabel.setText(ShjAppHelper.getString(R.string.paybumoney));
                    break;
                case R.id.pay_face /* 2131231174 */:
                    PopView_Pay.this.payTypes_face.setVisibility(0);
                    PopView_Pay.this.payTypes.setVisibility(8);
                    PopView_Pay.this.qrImage.setImageResource(R.drawable.img_facepay);
                    PopView_Pay.this.qrImage.setClickable(true);
                    break;
                case R.id.pay_wx /* 2131231179 */:
                case R.id.pay_zfb /* 2131231180 */:
                    PopView_Pay.this.findViewById(R.id.moneybar).setVisibility(8);
                    PopView_Pay.this.qrImage.setImageResource(R.drawable.img_bg_gray);
                    PopView_Pay.this.qrcodelabel.setText(ShjAppHelper.getString(R.string.creatingqrcode));
                    break;
            }
            Loger.writeLog("SHJ", "isFirstShow:" + PopView_Pay.this.isFirstShow);
            if (!PopView_Pay.this.isFirstShow) {
                PopView_Pay.this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Pay.2.1
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        PopView_Pay.this.submitOrder();
                    }
                }, 1000L);
            } else {
                PopView_Pay.this.isFirstShow = false;
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_Pay$2$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                PopView_Pay.this.submitOrder();
            }
        }
    }

    @Override // com.oysb.utils.view.BasePopView
    protected void registActions(List<String> list) {
        list.add(MyOrderListener.ACTION_ORDER_CANCELED);
        list.add(MyOrderListener.ACTION_ORDER_ERROR);
        list.add(MyOrderListener.ACTION_ORDER_CREATED);
        list.add(MyOrderListener.ACTION_ORDER_QRIMAGE_CREATED);
        list.add(MyOrderListener.ACTION_ORDER_PAY_CANCELED);
        list.add(MyOrderListener.ACTION_ORDER_PAY_SUCCESS);
        list.add(MyOrderListener.ACTION_ORDER_TIME);
        list.add(MyOrderListener.ACTION_ORDER_TIMEOUT);
        list.add(MyOrderListener.ACTION_ORDER_MESSAGE);
        list.add(MyMoneyListener.ACTION_MONEY_CHANGED);
    }

    private void showTextTip(String str, int i) {
        PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + UUID.randomUUID().toString())).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("info", (Object) str).put("notic", (Object) ShjAppHelper.getString(R.string.welcome)).put("time_out", (Object) Integer.valueOf(i)).put("showTime", (Object) false));
    }

    public void enterFacePay() {
        if (NetConnectTipUtils.checkNetConnect(SysApp.sysApp)) {
            if (this.faceOrder != null && this.wxFacePayHelper != null) {
                Loger.writeLog("SALES", "已有刷脸订单，再次刷脸");
                showTextTip(ShjAppHelper.getString(R.string.start_face_pay_tip), 5000);
                ShjManager.getOrderManager().resetWaitTimer(10);
                this.wxFacePayHelper.smilePay(SysApp.sysApp, this.faceOrder.getArgs().getArg("Wx_facepayId"), String.valueOf(this.faceOrder.getPayPrice()), this.faceOrder.getGoodsName());
                return;
            }
            if (this.isFacePayOrderSubmit) {
                return;
            }
            showTextTip(ShjAppHelper.getString(R.string.start_face_pay_tip), 5000);
            submitOrder();
            ShjManager.getOrderManager().resetWaitTimer(10);
            this.isFacePayOrderSubmit = true;
            Loger.writeLog("SALES", "提交刷脸订单");
        }
    }

    private void smilePay(Order order) {
        if (order == null) {
            return;
        }
        this.wxFacePayHelper.smilePay(SysApp.sysApp, order.getArgs().getArg("Wx_facepayId"), String.valueOf(order.getPayPrice()), order.getGoodsName());
        ShjManager.getOrderManager().cancelWaitTimer();
        this.wxFacePayHelper.setFacePayResultListering(new FacePayResultListering() { // from class: com.xyshj.machine.popview.PopView_Pay.3
            @Override // com.xyshj.machine.facepay.FacePayResultListering
            public void onFacePayStared() {
            }

            @Override // com.xyshj.machine.facepay.FacePayResultListering
            public void onFaceSuccess() {
            }

            @Override // com.xyshj.machine.facepay.FacePayResultListering
            public void showNotRigister() {
            }

            @Override // com.xyshj.machine.facepay.FacePayResultListering
            public void showUnderAgeTip() {
            }

            AnonymousClass3() {
            }

            @Override // com.xyshj.machine.facepay.FacePayResultListering
            public void onPaySuccess() {
                Loger.writeLog("SALES", "detail 刷脸支付成功");
            }

            @Override // com.xyshj.machine.facepay.FacePayResultListering
            public void onPayFail(boolean z) {
                if (z) {
                    PopView_Pay.this.handler.sendEmptyMessageDelayed(PopView_Pay.MSG_CANCEL_DETAIL, 1000L);
                } else {
                    ShjManager.getOrderManager().resetWaitTimer(60);
                }
                Loger.writeLog("SALES", "detail 刷脸支付失败");
            }
        });
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements FacePayResultListering {
        @Override // com.xyshj.machine.facepay.FacePayResultListering
        public void onFacePayStared() {
        }

        @Override // com.xyshj.machine.facepay.FacePayResultListering
        public void onFaceSuccess() {
        }

        @Override // com.xyshj.machine.facepay.FacePayResultListering
        public void showNotRigister() {
        }

        @Override // com.xyshj.machine.facepay.FacePayResultListering
        public void showUnderAgeTip() {
        }

        AnonymousClass3() {
        }

        @Override // com.xyshj.machine.facepay.FacePayResultListering
        public void onPaySuccess() {
            Loger.writeLog("SALES", "detail 刷脸支付成功");
        }

        @Override // com.xyshj.machine.facepay.FacePayResultListering
        public void onPayFail(boolean z) {
            if (z) {
                PopView_Pay.this.handler.sendEmptyMessageDelayed(PopView_Pay.MSG_CANCEL_DETAIL, 1000L);
            } else {
                ShjManager.getOrderManager().resetWaitTimer(60);
            }
            Loger.writeLog("SALES", "detail 刷脸支付失败");
        }
    }

    void updateMoney() {
        TextView textView = (TextView) findViewById(R.id.money);
        String replace = textView.getContext().getResources().getString(R.string.lab_insertmoney).replace("$", SysApp.getPriceUnit());
        StringBuilder sb = new StringBuilder();
        sb.append("");
        double intValue = Shj.getWallet().getCatchMoney().intValue();
        Double.isNaN(intValue);
        sb.append(intValue / 100.0d);
        textView.setText(replace.replace("#X#", sb.toString()));
        if (this.card.isChecked() || Shj.getWallet().getLastAddType() == MoneyType.ICCard) {
            checkMoney();
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onAction(String str, Bundle bundle) {
        if (isShowing()) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1886487831:
                    if (str.equals(MyOrderListener.ACTION_ORDER_QRIMAGE_CREATED)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1274066004:
                    if (str.equals(MyMoneyListener.ACTION_MONEY_CHANGED)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1058098734:
                    if (str.equals(MyOrderListener.ACTION_ORDER_PAY_SUCCESS)) {
                        c = 2;
                        break;
                    }
                    break;
                case -811774838:
                    if (str.equals(MyOrderListener.ACTION_ORDER_PAY_CANCELED)) {
                        c = 3;
                        break;
                    }
                    break;
                case -627089298:
                    if (str.equals(MyOrderListener.ACTION_ORDER_CREATED)) {
                        c = 4;
                        break;
                    }
                    break;
                case -407091481:
                    if (str.equals(MyOrderListener.ACTION_ORDER_TIME)) {
                        c = 5;
                        break;
                    }
                    break;
                case 1325412071:
                    if (str.equals(MyOrderListener.ACTION_ORDER_TIMEOUT)) {
                        c = 6;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    Loger.writeLog("SALES", "ACTION_ORDER_QRIMAGE_CREATED 1");
                    if (this.payTypes_face.getVisibility() == 0) {
                        return;
                    }
                    updateQrImage((OrderPayType) bundle.getSerializable("payType"));
                    return;
                case 1:
                    updateMoney();
                    return;
                case 2:
                    if (SysApp.isWxFacePayInstalled && ((OrderPayType) bundle.getSerializable("payType")) == OrderPayType.WEIXIN) {
                        String obj = ShjManager.getData("lastOfferCmd_OrderId").toString();
                        Loger.writeLog("SALES", "上报微信后台订单 orderId=" + obj);
                        if (!TextUtils.isEmpty(obj)) {
                            WxFacePayHelper.reportOrder(obj);
                        }
                    }
                    close();
                    return;
                case 3:
                    Loger.writeLog("SALES", "支付已取消");
                    return;
                case 4:
                    Order order = (Order) bundle.getSerializable("order");
                    Loger.writeLog("SALES", "订单创建成功");
                    Loger.writeLog("SALES", "Wx_facepayId=" + order.getArgs().getArg("Wx_facepayId"));
                    if (order.getPayTypes().contains(OrderPayType.WxFace)) {
                        this.faceOrder = order;
                        Loger.writeLog("SALES", "刷脸订单创建成功");
                        this.wxFacePayHelper = new WxFacePayHelper();
                        if (this.releaseWxpayface) {
                            Loger.writeLog("SALES", "wxFacePayHelper.init");
                            WxFacePayHelper.init(SysApp.sysApp, new InitListenring() { // from class: com.xyshj.machine.popview.PopView_Pay.4
                                AnonymousClass4() {
                                }

                                @Override // com.xyshj.machine.facepay.InitListenring
                                public void initCompelete() {
                                    PopView_Pay.this.releaseWxpayface = false;
                                    PopView_Pay.this.handler.sendEmptyMessageDelayed(PopView_Pay.MSG_SMILE_PAY, 1000L);
                                }
                            });
                            return;
                        } else {
                            smilePay(order);
                            return;
                        }
                    }
                    return;
                case 5:
                    this.timeoutinfo.setText(ShjAppHelper.getString(R.string.time_notice, "#X#", "" + bundle.getInt("time")));
                    if (bundle.getInt("time") == 0) {
                        this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Pay.5
                            AnonymousClass5() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                if (PopView_Pay.this.isShowing()) {
                                    PopView_Pay.this.close();
                                }
                            }
                        }, 1500L);
                        return;
                    }
                    return;
                case 6:
                    close();
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements InitListenring {
        AnonymousClass4() {
        }

        @Override // com.xyshj.machine.facepay.InitListenring
        public void initCompelete() {
            PopView_Pay.this.releaseWxpayface = false;
            PopView_Pay.this.handler.sendEmptyMessageDelayed(PopView_Pay.MSG_SMILE_PAY, 1000L);
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements Runnable {
        AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (PopView_Pay.this.isShowing()) {
                PopView_Pay.this.close();
            }
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onMessage(Message message) {
        if (message.what != MSG_CANCEL_DETAIL) {
            return;
        }
        onClick(findViewById(R.id.bt_back));
    }

    void updateOtherPayView() {
        this.qrImage.setClickable(false);
        try {
            OrderPayType orderPayType = OrderPayType.CASH;
            this.qrcodelabel.setVisibility(0);
            this.wx.setVisibility(ShjManager.getOrderPayTypes().contains(OrderPayType.WEIXIN) ? 0 : 8);
            this.zfb.setVisibility(ShjManager.getOrderPayTypes().contains(OrderPayType.ZFB) ? 0 : 8);
            this.card.setVisibility(ShjManager.getOrderPayTypes().contains(OrderPayType.ICCard) ? 0 : 8);
            this.cash.setVisibility(ShjManager.getOrderPayTypes().contains(OrderPayType.CASH) ? 0 : 8);
            this.jd.setVisibility(ShjManager.getOrderPayTypes().contains(OrderPayType.YLJH) ? 0 : 8);
            if (ShjManager.getOrderPayTypes().contains(OrderPayType.WEIXIN) && orderPayType == OrderPayType.CASH) {
                orderPayType = OrderPayType.WEIXIN;
            }
            if (ShjManager.getOrderPayTypes().contains(OrderPayType.ZFB) && orderPayType == OrderPayType.CASH) {
                orderPayType = OrderPayType.ZFB;
            }
            if (ShjManager.getOrderPayTypes().contains(OrderPayType.YLJH) && orderPayType == OrderPayType.CASH) {
                orderPayType = OrderPayType.YLJH;
            }
            this.wx.setChecked(orderPayType == OrderPayType.WEIXIN);
            this.zfb.setChecked(orderPayType == OrderPayType.ZFB);
            this.jd.setChecked(orderPayType == OrderPayType.YLJH);
            this.card.setChecked(orderPayType == OrderPayType.ICCard);
            this.cash.setChecked(orderPayType == OrderPayType.CASH);
            if (this.cash.isChecked()) {
                this.qrImage.setImageResource(R.drawable.img_cash);
            } else if (this.card.isChecked()) {
                this.qrImage.setImageResource(R.drawable.img_card);
            }
            updateMoney();
            submitOrder();
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    void updateFacePayView() {
        this.qrImage.setImageResource(R.drawable.img_facepay);
        this.qrcodelabel.setVisibility(4);
        findViewById(R.id.moneybar).setVisibility(4);
        boolean z = true;
        this.qrImage.setClickable(true);
        this.qrImage.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_Pay.6
            AnonymousClass6() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (Math.abs(System.currentTimeMillis() - PopView_Pay.this.clickTime) < 1000) {
                    return;
                }
                PopView_Pay.this.clickTime = System.currentTimeMillis();
                PopView_Pay.this.enterFacePay();
            }
        });
        this.face.setChecked(true);
        Iterator<OrderPayType> it = ShjManager.getOrderPayTypes().iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            OrderPayType next = it.next();
            if (next != OrderPayType.Face && next != OrderPayType.WxFace) {
                break;
            }
        }
        if (z) {
            this.pay_other.setVisibility(0);
        } else {
            this.pay_other.setVisibility(8);
        }
        synchronized (this) {
            Timer timer = this.facetimer;
            if (timer != null) {
                timer.cancel();
                this.facetimer = null;
            }
        }
        this.facetimerCount = 60;
        Timer timer2 = new Timer();
        this.facetimer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.xyshj.machine.popview.PopView_Pay.7
            AnonymousClass7() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", null);
                PopView_Pay popView_Pay = PopView_Pay.this;
                popView_Pay.facetimerCount--;
                bundle.putInt("time", PopView_Pay.this.facetimerCount);
                if (PopView_Pay.this.facetimerCount > 0) {
                    PopView_Pay.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Pay.7.1
                        final /* synthetic */ Bundle val$bundle;

                        AnonymousClass1(Bundle bundle2) {
                            bundle = bundle2;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_Pay.this.onAction(MyOrderListener.ACTION_ORDER_TIME, bundle);
                        }
                    });
                    return;
                }
                PopView_Pay.this.facetimerCount = 0;
                PopView_Pay.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Pay.7.2
                    final /* synthetic */ Bundle val$bundle;

                    AnonymousClass2(Bundle bundle2) {
                        bundle = bundle2;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        PopView_Pay.this.onAction(MyOrderListener.ACTION_ORDER_TIMEOUT, bundle);
                    }
                });
                synchronized (this) {
                    PopView_Pay.this.facetimer.cancel();
                    PopView_Pay.this.facetimer = null;
                }
            }

            /* renamed from: com.xyshj.machine.popview.PopView_Pay$7$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements Runnable {
                final /* synthetic */ Bundle val$bundle;

                AnonymousClass1(Bundle bundle2) {
                    bundle = bundle2;
                }

                @Override // java.lang.Runnable
                public void run() {
                    PopView_Pay.this.onAction(MyOrderListener.ACTION_ORDER_TIME, bundle);
                }
            }

            /* renamed from: com.xyshj.machine.popview.PopView_Pay$7$2 */
            /* loaded from: classes2.dex */
            class AnonymousClass2 implements Runnable {
                final /* synthetic */ Bundle val$bundle;

                AnonymousClass2(Bundle bundle2) {
                    bundle = bundle2;
                }

                @Override // java.lang.Runnable
                public void run() {
                    PopView_Pay.this.onAction(MyOrderListener.ACTION_ORDER_TIMEOUT, bundle);
                }
            }
        }, 1000L, 1000L);
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements View.OnClickListener {
        AnonymousClass6() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (Math.abs(System.currentTimeMillis() - PopView_Pay.this.clickTime) < 1000) {
                return;
            }
            PopView_Pay.this.clickTime = System.currentTimeMillis();
            PopView_Pay.this.enterFacePay();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 extends TimerTask {
        AnonymousClass7() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Bundle bundle2 = new Bundle();
            bundle2.putSerializable("order", null);
            PopView_Pay popView_Pay = PopView_Pay.this;
            popView_Pay.facetimerCount--;
            bundle2.putInt("time", PopView_Pay.this.facetimerCount);
            if (PopView_Pay.this.facetimerCount > 0) {
                PopView_Pay.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Pay.7.1
                    final /* synthetic */ Bundle val$bundle;

                    AnonymousClass1(Bundle bundle22) {
                        bundle = bundle22;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        PopView_Pay.this.onAction(MyOrderListener.ACTION_ORDER_TIME, bundle);
                    }
                });
                return;
            }
            PopView_Pay.this.facetimerCount = 0;
            PopView_Pay.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Pay.7.2
                final /* synthetic */ Bundle val$bundle;

                AnonymousClass2(Bundle bundle22) {
                    bundle = bundle22;
                }

                @Override // java.lang.Runnable
                public void run() {
                    PopView_Pay.this.onAction(MyOrderListener.ACTION_ORDER_TIMEOUT, bundle);
                }
            });
            synchronized (this) {
                PopView_Pay.this.facetimer.cancel();
                PopView_Pay.this.facetimer = null;
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_Pay$7$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ Bundle val$bundle;

            AnonymousClass1(Bundle bundle22) {
                bundle = bundle22;
            }

            @Override // java.lang.Runnable
            public void run() {
                PopView_Pay.this.onAction(MyOrderListener.ACTION_ORDER_TIME, bundle);
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_Pay$7$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements Runnable {
            final /* synthetic */ Bundle val$bundle;

            AnonymousClass2(Bundle bundle22) {
                bundle = bundle22;
            }

            @Override // java.lang.Runnable
            public void run() {
                PopView_Pay.this.onAction(MyOrderListener.ACTION_ORDER_TIMEOUT, bundle);
            }
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewWillShow() {
        super.onViewWillShow();
        MdbReader_BDT.get().setDebug(BqlManager.get().isDebug());
        this.timeoutinfo.setText(ShjAppHelper.getString(R.string.time_notice, "#X#", "60"));
        this.qrImage.setImageResource(R.drawable.img_bg_gray);
        try {
            VmdHelper.BqlOrder bqlOrder = VmdHelper.get().getBqlOrder();
            this.detail.setText("");
            this.detail.setVisibility(8);
            if (bqlOrder.getType().equals(VmdHelper.BQL_TYPE_CUSTOMER)) {
                this.name.setVisibility(0);
                this.name.setText(bqlOrder.getBqlItems().keySet().toArray()[0].toString());
                this.label.setText(ShjAppHelper.getString(R.string.custom_select));
                StringBuilder sb = new StringBuilder();
                int i = bqlOrder.getTops().size() > 3 ? 2 : 1;
                int i2 = 0;
                for (String str : bqlOrder.getTops().keySet()) {
                    sb.append(str);
                    sb.append(" * ");
                    sb.append("" + bqlOrder.getTops().get(str));
                    i2++;
                    if (i2 % i == 0) {
                        sb.append(StringUtils.LF);
                    } else {
                        sb.append(StringUtils.SPACE);
                    }
                }
                this.detail.setText(sb.toString());
                this.detail.setVisibility(0);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.detail.getLayoutParams();
                layoutParams.topMargin = getContentView().getContext().getResources().getDimensionPixelSize(R.dimen.px160);
                this.detail.setLayoutParams(layoutParams);
                if (Build.VERSION.SDK_INT >= 28) {
                    this.detail.setLineHeight(getContentView().getContext().getResources().getDimensionPixelSize(R.dimen.px50));
                }
                this.detail.setTextSize(0, 36.0f);
            } else {
                this.name.setVisibility(4);
                StringBuilder sb2 = new StringBuilder();
                for (String str2 : bqlOrder.getBqlItems().keySet()) {
                    sb2.append(str2);
                    sb2.append(" * ");
                    sb2.append("" + bqlOrder.getBqlCount(str2));
                    sb2.append(StringUtils.LF);
                }
                this.detail.setText(sb2.toString());
                this.detail.setVisibility(0);
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.detail.getLayoutParams();
                layoutParams2.topMargin = getContentView().getContext().getResources().getDimensionPixelSize(R.dimen.px100);
                this.detail.setLayoutParams(layoutParams2);
                if (Build.VERSION.SDK_INT >= 28) {
                    this.detail.setLineHeight(getContentView().getContext().getResources().getDimensionPixelSize(R.dimen.px64));
                }
                this.detail.setTextSize(0, 48.0f);
                this.label.setText(ShjAppHelper.getString(R.string.classic_select));
            }
            TextView textView = this.totalPrice;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            double orderPrice = bqlOrder.getOrderPrice();
            Double.isNaN(orderPrice);
            sb3.append(orderPrice / 100.0d);
            textView.setText(ShjAppHelper.getString(R.string.total, "#X#", sb3.toString()).replace("$", SysApp.getPriceUnit()));
        } catch (Exception unused) {
        }
        this.isFacePayOpen = AppSetting.getEnableFacePay(SysApp.sysApp, null);
        boolean z = SysApp.isWxFacePayInstalled;
        this.isFacePayInstalled = z;
        if (!z || !this.isFacePayOpen) {
            this.face.setVisibility(8);
            this.payTypes_face.setVisibility(8);
            this.qrImage.setClickable(false);
            updateOtherPayView();
        } else {
            this.payTypes.setVisibility(8);
            this.payTypes_face.setVisibility(0);
            updateFacePayView();
        }
        if ((ShjManager.getOrderPayTypes().contains(OrderPayType.CASH) || ShjManager.getOrderPayTypes().contains(OrderPayType.ICCard)) && !MdbReader_BDT.get().isConnected()) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) UUID.randomUUID().toString()).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("" + ShjAppHelper.getString(R.string.bql_status_mdb_connecting))).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("time_out", (Object) 5000).put("showTime", (Object) false));
        }
    }

    void checkMoney() {
        if ((this.cash.isChecked() || this.card.isChecked()) && VmdHelper.get().getBqlOrder().getOrderPrice() <= Shj.getWallet().getCatchMoney().intValue()) {
            this.handler.postDelayed(new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Pay.8
                AnonymousClass8(Object obj) {
                    super(obj);
                }

                @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                public void run() {
                    try {
                        Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
                        Loger.writeLog("SHJ", "lsOrder:" + resentOrder.getUid());
                        OrderPayType orderPayType = PopView_Pay.this.cash.isChecked() ? OrderPayType.CASH : OrderPayType.ICCard;
                        resentOrder.getPrice();
                        ShjManager.getOrderManager().driverOfferLineOrder(orderPayType, resentOrder.getUid(), Shj.getWallet().getLastAddMoneyInfo() + ("" + System.currentTimeMillis()).substring(r2.length() - 4));
                    } catch (Exception unused) {
                    }
                    PopView_Pay.this.close();
                }
            }, 1000L);
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 extends RunnableEx {
        AnonymousClass8(Object obj) {
            super(obj);
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            try {
                Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
                Loger.writeLog("SHJ", "lsOrder:" + resentOrder.getUid());
                OrderPayType orderPayType = PopView_Pay.this.cash.isChecked() ? OrderPayType.CASH : OrderPayType.ICCard;
                resentOrder.getPrice();
                ShjManager.getOrderManager().driverOfferLineOrder(orderPayType, resentOrder.getUid(), Shj.getWallet().getLastAddMoneyInfo() + ("" + System.currentTimeMillis()).substring(r2.length() - 4));
            } catch (Exception unused) {
            }
            PopView_Pay.this.close();
        }
    }

    void submitOrder() {
        Loger.writeLog("SALES", "submitOrder");
        ArrayList arrayList = new ArrayList();
        if (this.wx.isChecked()) {
            arrayList.add(OrderPayType.WEIXIN);
        } else if (this.zfb.isChecked()) {
            arrayList.add(OrderPayType.ZFB);
        } else if (this.card.isChecked()) {
            arrayList.add(OrderPayType.ICCard);
        } else if (this.cash.isChecked()) {
            arrayList.add(OrderPayType.CASH);
        } else if (this.jd.isChecked()) {
            arrayList.add(OrderPayType.YLJH);
        } else {
            arrayList.add(OrderPayType.WxFace);
        }
        ArrayList arrayList2 = new ArrayList();
        VmdHelper.BqlOrder bqlOrder = VmdHelper.get().getBqlOrder();
        Iterator<String> it = bqlOrder.getBqlItems().keySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String next = it.next();
            VmdHelper.BqlItem bqlItem = VmdHelper.get().getBqlItem(next);
            for (int i = 0; i < bqlOrder.getBqlCount(next); i++) {
                arrayList2.add(ShjManager.getGoodsManager().getGoodsByCode(bqlItem.getCode()));
            }
        }
        if (bqlOrder.getType().equals(VmdHelper.BQL_TYPE_CUSTOMER)) {
            for (String str : bqlOrder.getTops().keySet()) {
                VmdHelper.TopItem topItem = VmdHelper.get().getTopItem(str);
                for (int i2 = 0; i2 < bqlOrder.getBqlCount(str); i2++) {
                    arrayList2.add(ShjManager.getGoodsManager().getGoodsByCode(topItem.getCode()));
                }
            }
        }
        OrderArgs orderArgs = new OrderArgs();
        orderArgs.putArg("bqlOrdertype", VmdHelper.get().getBqlOrder().getType());
        orderArgs.putArg("bqlOrderSn", bqlOrder.getSn());
        orderArgs.putArg("ThirdOrderKey", bqlOrder.getSn());
        orderArgs.setAutoOfferGoods(false);
        Loger.writeLog("SHJ;SALES", "submitOrderEx-----");
        ShjManager.getOrderManager().submitOrderEx(arrayList, arrayList2, bqlOrder.getOrderPrice(), orderArgs);
    }

    void updateQrImage(OrderPayType orderPayType) {
        try {
            Loger.writeLog("SALES", "updateQrImage 1");
            Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
            if (resentOrder == null) {
                Loger.writeLog("SALES", "on order on updateQrImage");
            }
            resentOrder.getSumPrice();
            resentOrder.getPayPrice();
            try {
                String arg = resentOrder.getArgs().getArg(ShjManager.getSetting(orderPayType + "ZK").toString());
                if (arg.length() > 0) {
                    Double.parseDouble(arg);
                }
            } catch (Exception unused) {
            }
            if (orderPayType != OrderPayType.ZFB && orderPayType != OrderPayType.WEIXIN && orderPayType != OrderPayType.YL6) {
                OrderPayType orderPayType2 = OrderPayType.YLJH;
            }
            int i = AnonymousClass9.$SwitchMap$com$shj$biz$order$OrderPayType[orderPayType.ordinal()];
            this.qrImage.setVisibility(0);
            Loger.writeLog("SALES", "set qrImage:" + ShjAppBase.sysModel.getBitmap(orderPayType));
            this.qrImage.setImageBitmap(ShjAppBase.sysModel.getBitmap(orderPayType));
            this.qrcodelabel.setText(ShjAppHelper.getString(R.string.orderid) + ": " + resentOrder.getPayId());
        } catch (Exception e) {
            Loger.writeException("SALES", e);
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay$9 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass9 {
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
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YL6.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YLJH.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.bt_back) {
            if (this.isFacePayOpen && this.isFacePayInstalled && this.payTypes_face.getVisibility() == 8) {
                this.payTypes.setVisibility(8);
                this.payTypes_face.setVisibility(0);
                ShjManager.getOrderManager().cancelOrder();
                updateFacePayView();
                return;
            }
            close();
            return;
        }
        if (view.getId() == R.id.bt_buy) {
            checkMoney();
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidClose() {
        super.onViewDidClose();
        this.faceOrder = null;
        this.wxFacePayHelper = null;
        this.isFacePayOrderSubmit = false;
        MdbReader_BDT.get().cancel();
    }
}
