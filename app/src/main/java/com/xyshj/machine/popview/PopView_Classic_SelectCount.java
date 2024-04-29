package com.xyshj.machine.popview;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.oysb.utils.Loger;
import com.oysb.utils.view.BFPopView;
import com.oysb.utils.view.BasePopView;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.machine.R;
import com.xyshj.machine.app.SysApp;
import com.xyshj.machine.app.VmdHelper;
import com.xyshj.machine.listener.MyMoneyListener;
import com.xyshj.machine.listener.MyOrderListener;
import com.xyshj.machine.listener.MyShjStatusListener;
import com.xyshj.machine.popview.PopView_Info;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class PopView_Classic_SelectCount extends BasePopView implements View.OnClickListener {
    TextView bqlname;
    FrameLayout count1;
    FrameLayout count2;
    FrameLayout count3;
    TextView detail;
    TextView info;
    TextView label;
    TextView price;
    int selectCount = 1;
    TextView singlePrice;

    @Override // com.oysb.utils.view.BFPopView
    protected void onMessage(Message message) {
    }

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.popview_classic_detail, (ViewGroup) null);
        inflate.setOnClickListener(this);
        this.bqlname = (TextView) inflate.findViewById(R.id.name);
        this.label = (TextView) inflate.findViewById(R.id.label);
        this.detail = (TextView) inflate.findViewById(R.id.detail);
        this.count1 = (FrameLayout) inflate.findViewById(R.id.count1);
        this.count2 = (FrameLayout) inflate.findViewById(R.id.count2);
        this.count3 = (FrameLayout) inflate.findViewById(R.id.count3);
        this.price = (TextView) inflate.findViewById(R.id.price);
        this.singlePrice = (TextView) inflate.findViewById(R.id.singlePrice);
        this.info = (TextView) inflate.findViewById(R.id.info);
        this.count1.setOnClickListener(this);
        this.count2.setOnClickListener(this);
        this.count3.setOnClickListener(this);
        inflate.findViewById(R.id.bt_yhm).setOnClickListener(this);
        inflate.findViewById(R.id.bt_buy).setOnClickListener(this);
        inflate.findViewById(R.id.bt_back).setOnClickListener(this);
        return inflate;
    }

    @Override // com.oysb.utils.view.BasePopView
    protected void registActions(List<String> list) {
        list.add(MyShjStatusListener.ACTION_SHJ_FREE_TIME);
        list.add(MyMoneyListener.ACTION_MONEY_CHANGED);
        list.add(VmdHelper.ACTION_UPDATE_YYJE);
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onAction(String str, Bundle bundle) {
        if (isShowing()) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1342403763:
                    if (str.equals(MyShjStatusListener.ACTION_SHJ_FREE_TIME)) {
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
                case 1126309256:
                    if (str.equals(VmdHelper.ACTION_UPDATE_YYJE)) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    close();
                    return;
                case 1:
                    updateMoney();
                    return;
                case 2:
                    close();
                    return;
                case 3:
                    Loger.writeLog("SALES", "支付已取消");
                    return;
                case 4:
                    checkCount(null);
                    return;
                default:
                    return;
            }
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
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewWillShow() {
        boolean z;
        int i;
        super.onViewWillShow();
        updateMoney();
        checkCount(this.count1);
        VmdHelper.BqlOrder bqlOrder = VmdHelper.get().getBqlOrder();
        StringBuilder sb = new StringBuilder();
        if (bqlOrder.getType().equals(VmdHelper.BQL_TYPE_CUSTOMER)) {
            this.label.setText(ShjAppHelper.getString(R.string.custom_select));
            int i2 = bqlOrder.getTops().size() > 3 ? 2 : 1;
            int i3 = 0;
            for (String str : bqlOrder.getTops().keySet()) {
                sb.append(str);
                sb.append(Marker.ANY_MARKER);
                sb.append("" + bqlOrder.getTops().get(str));
                i3++;
                if (i3 % i2 == 0) {
                    sb.append(StringUtils.LF);
                } else {
                    sb.append(StringUtils.SPACE);
                }
            }
        } else {
            this.label.setText(ShjAppHelper.getString(R.string.classic_select));
            this.bqlname.setText(bqlOrder.getBqlItems().keySet().toArray()[0].toString());
            VmdHelper.BqlItem bqlItem = VmdHelper.get().getBqlItem(this.bqlname.getText().toString());
            TextView textView = this.singlePrice;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            double price = bqlItem.getPrice();
            Double.isNaN(price);
            sb2.append(price / 100.0d);
            textView.setText(ShjAppHelper.getString(R.string.labprice, "#X#", sb2.toString()).replace("$", SysApp.getPriceUnit()));
            int i4 = bqlItem.getTops().size() > 3 ? 2 : 1;
            int i5 = 0;
            for (String str2 : bqlItem.getTops().keySet()) {
                sb.append(str2);
                sb.append(Marker.ANY_MARKER);
                sb.append("" + bqlItem.getTops().get(str2));
                i5++;
                if (i5 % i4 == 0) {
                    sb.append(StringUtils.LF);
                } else {
                    sb.append(StringUtils.SPACE);
                }
            }
        }
        this.detail.setText(sb.toString());
        this.detail.setVisibility(View.VISIBLE);
        this.detail.setTextSize(0, 36.0f);
        TextView textView2 = this.price;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(SysApp.getPriceUnit());
        double orderPrice = bqlOrder.getOrderPrice();
        Double.isNaN(orderPrice);
        sb3.append(orderPrice / 100.0d);
        textView2.setText(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("");
        double orderPrice2 = bqlOrder.getOrderPrice();
        Double.isNaN(orderPrice2);
        sb4.append(orderPrice2 / 100.0d);
        String string = ShjAppHelper.getString(R.string.sale_price, "#X#", sb4.toString());
        StringBuilder sb5 = new StringBuilder();
        sb5.append("");
        double yhje = bqlOrder.getYhje();
        Double.isNaN(yhje);
        sb5.append(yhje / 100.0d);
        this.info.setText(string.replace("#0.0", sb5.toString()).replace("$", SysApp.getPriceUnit()));
        int intValue = Shj.getShelfInfo(201).getGoodsCount().intValue();
        int intValue2 = Shj.getShelfInfo(202).getGoodsCount().intValue();
        this.count3.setClickable(true);
        this.count2.setClickable(true);
        this.count1.setClickable(true);
        if (intValue < 3 || intValue2 < 3) {
            z = false;
            this.count3.setClickable(false);
            i = 2;
        } else {
            i = 2;
            z = false;
        }
        if (intValue < i || intValue2 < i) {
            this.count2.setClickable(z);
        }
        if (intValue < 1 || intValue2 < 1) {
            this.count1.setClickable(z);
        }
    }

    void checkCount(View view) {
        FrameLayout frameLayout = this.count1;
        int i = R.drawable.radio_classicbql_count_checked;
        frameLayout.setBackgroundResource(view == frameLayout ? R.drawable.radio_classicbql_count_checked : R.drawable.radio_classicbql_count_uncheck);
        FrameLayout frameLayout2 = this.count2;
        frameLayout2.setBackgroundResource(view == frameLayout2 ? R.drawable.radio_classicbql_count_checked : R.drawable.radio_classicbql_count_uncheck);
        FrameLayout frameLayout3 = this.count3;
        if (view != frameLayout3) {
            i = R.drawable.radio_classicbql_count_uncheck;
        }
        frameLayout3.setBackgroundResource(i);
        this.selectCount = view == this.count1 ? 1 : view == this.count2 ? 2 : 3;
        VmdHelper.BqlOrder bqlOrder = VmdHelper.get().getBqlOrder();
        VmdHelper.get().getBqlOrder().setBqlCount(bqlOrder.getBqlItems().keySet().toArray()[0].toString(), this.selectCount);
        TextView textView = this.price;
        StringBuilder sb = new StringBuilder();
        sb.append(SysApp.getPriceUnit());
        double orderPrice = bqlOrder.getOrderPrice();
        Double.isNaN(orderPrice);
        sb.append(orderPrice / 100.0d);
        textView.setText(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        double orderPrice2 = bqlOrder.getOrderPrice();
        Double.isNaN(orderPrice2);
        sb2.append(orderPrice2 / 100.0d);
        String string = ShjAppHelper.getString(R.string.sale_price, "#X#", sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        double yhje = bqlOrder.getYhje();
        Double.isNaN(yhje);
        sb3.append(yhje / 100.0d);
        this.info.setText(string.replace("#Y#", sb3.toString()).replace("$", SysApp.getPriceUnit()));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_back /* 2131230787 */:
                close();
                return;
            case R.id.bt_buy /* 2131230798 */:
                if (!VmdHelper.get().checkBqlTopEnnough(VmdHelper.get().getBqlOrder().getBqlItems())) {
                    PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + UUID.randomUUID().toString())).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("" + ShjAppHelper.getString(R.string.notice_reselect))).put("time_out", (Object) 5000).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("showTime", (Object) false));
                    return;
                }
                if (VmdHelper.get().checkVmdConnected() && VmdHelper.get().checkBqlZhibeiCount(this.selectCount)) {
                    VmdHelper.get().doCheckBqlStatus(30000, new VmdHelper.CheckBqlStatusListener() { // from class: com.xyshj.machine.popview.PopView_Classic_SelectCount.1
                        final /* synthetic */ String val$uid;

                        AnonymousClass1(String str) {
                            uuid = str;
                        }

                        @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
                        public void onStartWait2StatusOk(String str) {
                            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) str).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("time_out", (Object) 30000).put("showTime", (Object) false));
                        }

                        /* renamed from: com.xyshj.machine.popview.PopView_Classic_SelectCount$1$1 */
                        /* loaded from: classes2.dex */
                        class RunnableC00831 implements Runnable {
                            RunnableC00831() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                                PopView_Classic_SelectCount.this.close();
                            }
                        }

                        @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
                        public void onCheckBqlStatusResult(boolean z, String str) {
                            if (z) {
                                PopView_Info.closeInfo(uuid);
                                PopView_Classic_SelectCount.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Classic_SelectCount.1.1
                                    RunnableC00831() {
                                    }

                                    @Override // java.lang.Runnable
                                    public void run() {
                                        BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                                        PopView_Classic_SelectCount.this.close();
                                    }
                                });
                                return;
                            }
                            String mainServicePhone = ShjAppHelper.getMainServicePhone();
                            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("[error]" + str)).put("time_out", (Object) 5000).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("showTime", (Object) true).put("notice", (Object) (ShjAppHelper.getString(R.string.phone) + ":" + mainServicePhone)));
                        }
                    });
                    return;
                }
                return;
            case R.id.bt_yhm /* 2131230887 */:
                try {
                    VmdHelper.BqlOrder bqlOrder = VmdHelper.get().getBqlOrder();
                    String obj = bqlOrder.getBqlItems().keySet().toArray()[0].toString();
                    VmdHelper.BqlItem bqlItem = VmdHelper.get().getBqlItem(obj);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put(ShjDbHelper.COLUM_price, bqlOrder.getOrderPrice() * 100);
                    jSONObject.put("goodsName", obj);
                    jSONObject.put("goodsCode", bqlItem.getCode());
                    jSONObject.put("goodsPrice", bqlItem.getPrice());
                    jSONObject.put("goodsCount", this.selectCount);
                    BFPopView.showPopView("MainActivity", PopView_EnterCode.class, jSONObject.toString());
                    return;
                } catch (Exception unused) {
                    return;
                }
            case R.id.count1 /* 2131230948 */:
                checkCount(this.count1);
                return;
            case R.id.count2 /* 2131230949 */:
                checkCount(this.count2);
                return;
            case R.id.count3 /* 2131230950 */:
                checkCount(this.count3);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.popview.PopView_Classic_SelectCount$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements VmdHelper.CheckBqlStatusListener {
        final /* synthetic */ String val$uid;

        AnonymousClass1(String str) {
            uuid = str;
        }

        @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
        public void onStartWait2StatusOk(String str) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) str).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("time_out", (Object) 30000).put("showTime", (Object) false));
        }

        /* renamed from: com.xyshj.machine.popview.PopView_Classic_SelectCount$1$1 */
        /* loaded from: classes2.dex */
        class RunnableC00831 implements Runnable {
            RunnableC00831() {
            }

            @Override // java.lang.Runnable
            public void run() {
                BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                PopView_Classic_SelectCount.this.close();
            }
        }

        @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
        public void onCheckBqlStatusResult(boolean z, String str) {
            if (z) {
                PopView_Info.closeInfo(uuid);
                PopView_Classic_SelectCount.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Classic_SelectCount.1.1
                    RunnableC00831() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                        PopView_Classic_SelectCount.this.close();
                    }
                });
                return;
            }
            String mainServicePhone = ShjAppHelper.getMainServicePhone();
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("[error]" + str)).put("time_out", (Object) 5000).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("showTime", (Object) true).put("notice", (Object) (ShjAppHelper.getString(R.string.phone) + ":" + mainServicePhone)));
        }
    }
}
