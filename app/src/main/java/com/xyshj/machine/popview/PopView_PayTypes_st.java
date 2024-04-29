package com.xyshj.machine.popview;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hanlu.toolsdk.BqlManager;
import com.oysb.utils.Loger;
import com.oysb.utils.view.BasePopView;
import com.shj.biz.ShjManager;
import com.shj.biz.order.OrderPayType;
import com.shj.device.cardreader.MdbReader_BDT;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.machine.R;
import com.xyshj.machine.app.SysApp;
import com.xyshj.machine.app.VmdHelper;
import com.xyshj.machine.listener.MyMoneyListener;
import com.xyshj.machine.listener.MyOrderListener;
import com.xyshj.machine.popview.PopView_Info;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class PopView_PayTypes_st extends BasePopView implements View.OnClickListener {
    static final int MSG_CANCEL_DETAIL = 2026;
    TextView back;
    long clickTime = 0;
    String payNoticeId = "payNoticeIdXXWWAASSS";
    Typeface popingMedim;
    TextView totalPrice;

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.st_popview_paytypes, (ViewGroup) null);
        inflate.setOnClickListener(this);
        TextView textView = (TextView) inflate.findViewById(R.id.back);
        this.back = textView;
        textView.setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Medium.ttf"));
        this.back.setOnClickListener(this);
        this.popingMedim = Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Medium.ttf");
        ((TextView) inflate.findViewById(R.id.tvDetails)).setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-ExtralBold.otf"));
        TextView textView2 = (TextView) inflate.findViewById(R.id.totalprice);
        this.totalPrice = textView2;
        textView2.setTypeface(this.popingMedim);
        return inflate;
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
        list.add(VmdHelper.ACTION_UPDATE_YYJE);
    }

    private void showTextTip(String str, int i) {
        PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + UUID.randomUUID().toString())).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("info", (Object) str).put("notic", (Object) ShjAppHelper.getString(R.string.welcome)).put("time_out", (Object) Integer.valueOf(i)).put("showTime", (Object) false));
    }

    void updateMoney() {
        TextView textView = this.totalPrice;
        StringBuilder sb = new StringBuilder();
        sb.append(SysApp.getPriceUnit());
        sb.append(StringUtils.SPACE);
        double orderPrice = VmdHelper.get().getBqlOrder().getOrderPrice();
        Double.isNaN(orderPrice);
        sb.append(String.format("%.02f", Double.valueOf(orderPrice / 100.0d)));
        textView.setText(sb.toString());
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onAction(String str, Bundle bundle) {
        if (isShowing()) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1274066004:
                    if (str.equals(MyMoneyListener.ACTION_MONEY_CHANGED)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1058098734:
                    if (str.equals(MyOrderListener.ACTION_ORDER_PAY_SUCCESS)) {
                        c = 1;
                        break;
                    }
                    break;
                case -811774838:
                    if (str.equals(MyOrderListener.ACTION_ORDER_PAY_CANCELED)) {
                        c = 2;
                        break;
                    }
                    break;
                case -407091481:
                    if (str.equals(MyOrderListener.ACTION_ORDER_TIME)) {
                        c = 3;
                        break;
                    }
                    break;
                case 1126309256:
                    if (str.equals(VmdHelper.ACTION_UPDATE_YYJE)) {
                        c = 4;
                        break;
                    }
                    break;
                case 1325412071:
                    if (str.equals(MyOrderListener.ACTION_ORDER_TIMEOUT)) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    updateMoney();
                    return;
                case 1:
                    close();
                    return;
                case 2:
                    Loger.writeLog("SALES", "支付已取消");
                    return;
                case 3:
                    if (bundle.getInt("time") == 0) {
                        this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_PayTypes_st.1
                            AnonymousClass1() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                if (PopView_PayTypes_st.this.isShowing()) {
                                    PopView_PayTypes_st.this.close();
                                }
                            }
                        }, 1500L);
                        return;
                    }
                    return;
                case 4:
                    updateMoney();
                    return;
                case 5:
                    close();
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PayTypes_st$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (PopView_PayTypes_st.this.isShowing()) {
                PopView_PayTypes_st.this.close();
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

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidShow() {
        super.onViewDidShow();
        updateMoney();
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewWillShow() {
        super.onViewWillShow();
        MdbReader_BDT.setEnabled(false);
        MdbReader_BDT.get().setDebug(BqlManager.get().isDebug());
        try {
            VmdHelper.get().getBqlOrder();
            TextView textView = this.totalPrice;
            StringBuilder sb = new StringBuilder();
            sb.append(SysApp.getPriceUnit());
            sb.append(StringUtils.SPACE);
            Object[] objArr = new Object[1];
            double orderPrice = VmdHelper.get().getBqlOrder().getOrderPrice();
            Double.isNaN(orderPrice);
            objArr[0] = Double.valueOf(orderPrice / 100.0d);
            sb.append(String.format("%.02f", objArr));
            textView.setText(sb.toString());
        } catch (Exception unused) {
        }
        if ((ShjManager.getOrderPayTypes().contains(OrderPayType.CASH) || ShjManager.getOrderPayTypes().contains(OrderPayType.ICCard)) && !MdbReader_BDT.get().isConnected()) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) UUID.randomUUID().toString()).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("" + ShjAppHelper.getString(R.string.bql_status_mdb_connecting))).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("time_out", (Object) 5000).put("showTime", (Object) false));
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            close();
        }
        if (view.getId() == R.id.back) {
            close();
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidClose() {
        super.onViewDidClose();
        MdbReader_BDT.setEnabled(false);
        MdbReader_BDT.get().cancel();
        ShjAppBase.sysApp.sendBroadcast(new Intent("Action_clear_selections"));
        Loger.writeLog("Broadcast;SHJ", "Action_clear_selections");
    }
}
