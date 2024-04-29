package com.xyshj.machine.popview;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hanlu.toolsdk.BqlManager;
import com.oysb.utils.Loger;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.view.BFPopView;
import com.oysb.utils.view.BasePopView;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.biz.ShjManager;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderArgs;
import com.shj.biz.order.OrderPayType;
import com.shj.device.cardreader.MdbReader_BDT;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.machine.R;
import com.xyshj.machine.app.VmdHelper;
import com.xyshj.machine.listener.MyMoneyListener;
import com.xyshj.machine.listener.MyOrderListener;
import com.xyshj.machine.popview.PopView_Info;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class PopView_Pay_en extends BasePopView implements View.OnClickListener {
    static final int MSG_CANCEL_DETAIL = 2026;
    MyListViewAdapter adapter;
    TextView back;
    CheckBox card;
    CheckBox cash;
    RelativeLayout listFooter;
    RelativeLayout listHeader;
    ListView listView;
    TextView plainPrice;
    Typeface popingMedim;
    TextView totalBalance;
    TextView totalPrice;
    long clickTime = 0;
    String payNoticeId = "payNoticeIdXXWWAASSS";

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.en_popview_pay, (ViewGroup) null);
        inflate.setOnClickListener(this);
        TextView textView = (TextView) inflate.findViewById(R.id.back);
        this.back = textView;
        textView.setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Medium.ttf"));
        this.back.setOnClickListener(this);
        this.cash = (CheckBox) inflate.findViewById(R.id.cash);
        this.card = (CheckBox) inflate.findViewById(R.id.card);
        this.cash.setOnClickListener(this);
        this.card.setOnClickListener(this);
        this.popingMedim = Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Medium.ttf");
        ((TextView) inflate.findViewById(R.id.tvDetails)).setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-ExtralBold.otf"));
        ((TextView) inflate.findViewById(R.id.cardnotice)).setTypeface(this.popingMedim);
        ((TextView) inflate.findViewById(R.id.cashnotice)).setTypeface(this.popingMedim);
        ((TextView) inflate.findViewById(R.id.enterpromo)).setTypeface(this.popingMedim);
        ((TextView) inflate.findViewById(R.id.swippay)).setTypeface(this.popingMedim);
        this.cash.setTypeface(this.popingMedim);
        this.card.setTypeface(this.popingMedim);
        inflate.findViewById(R.id.swippay).setOnClickListener(this);
        ((TextView) inflate.findViewById(R.id.enterpromo)).setOnClickListener(this);
        TextView textView2 = (TextView) inflate.findViewById(R.id.totalprice);
        this.totalPrice = textView2;
        textView2.setTypeface(this.popingMedim);
        ListView listView = (ListView) inflate.findViewById(R.id.listview);
        this.listView = listView;
        listView.setDivider(new ColorDrawable(0));
        this.listView.setDividerHeight(0);
        this.listHeader = (RelativeLayout) inflate.findViewById(R.id.listheader);
        this.listFooter = (RelativeLayout) inflate.findViewById(R.id.listfooter);
        this.plainPrice = (TextView) this.listHeader.findViewById(R.id.price);
        ((TextView) this.listHeader.findViewById(R.id.name)).setText("Plain ice cream");
        ((TextView) this.listHeader.findViewById(R.id.name)).setTypeface(this.popingMedim);
        this.plainPrice.setTypeface(this.popingMedim);
        this.totalBalance = (TextView) this.listFooter.findViewById(R.id.price);
        ((TextView) this.listFooter.findViewById(R.id.name)).setText("Total Balance");
        ((TextView) this.listFooter.findViewById(R.id.name)).setTypeface(this.popingMedim);
        this.totalBalance.setTypeface(this.popingMedim);
        ((TextView) this.listFooter.findViewById(R.id.name)).getPaint().setFakeBoldText(true);
        ((TextView) this.listFooter.findViewById(R.id.promo)).setVisibility(0);
        ((TextView) this.listFooter.findViewById(R.id.promo)).setTypeface(this.popingMedim);
        MyListViewAdapter myListViewAdapter = new MyListViewAdapter();
        this.adapter = myListViewAdapter;
        this.listView.setAdapter((ListAdapter) myListViewAdapter);
        return inflate;
    }

    /* loaded from: classes2.dex */
    class MyListViewAdapter extends BaseAdapter {
        ArrayList<String> tops = new ArrayList<>();

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        MyListViewAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.tops.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return this.tops.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = (LayoutInflater) PopView_Pay_en.this.getContentView().getContext().getSystemService("layout_inflater");
            if (view == null) {
                view = layoutInflater.inflate(R.layout.en_detail_item_layout, (ViewGroup) null);
                view.setLayoutParams(new RadioGroup.LayoutParams(-1, PopView_Pay_en.this.listView.getResources().getDimensionPixelSize(R.dimen.px170)));
                ((TextView) view.findViewById(R.id.name)).setTypeface(PopView_Pay_en.this.popingMedim);
                ((TextView) view.findViewById(R.id.price)).setTypeface(PopView_Pay_en.this.popingMedim);
            }
            TextView textView = (TextView) view.findViewById(R.id.name);
            TextView textView2 = (TextView) view.findViewById(R.id.price);
            String str = this.tops.get(i);
            if (i == 0) {
                textView.setText("Solid toppings");
            } else {
                textView.setText("Liquid toppings");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("$ ");
            double parseInt = Integer.parseInt(str);
            Double.isNaN(parseInt);
            sb.append(String.format("%.02f", Double.valueOf(parseInt / 100.0d)));
            textView2.setText(sb.toString());
            return view;
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
        list.add(VmdHelper.ACTION_UPDATE_YYJE);
    }

    private void showTextTip(String str, int i) {
        PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + UUID.randomUUID().toString())).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("info", (Object) str).put("notic", (Object) ShjAppHelper.getString(R.string.welcome)).put("time_out", (Object) Integer.valueOf(i)).put("showTime", (Object) false));
    }

    void updateMoney() {
        TextView textView = (TextView) this.listFooter.findViewById(R.id.promo);
        StringBuilder sb = new StringBuilder();
        sb.append(ShjAppHelper.getString(R.string.bql_yhje));
        sb.append(StringUtils.SPACE);
        double yhje = VmdHelper.get().getBqlOrder().getYhje();
        Double.isNaN(yhje);
        sb.append(String.format("%.02f", Double.valueOf(yhje / 100.0d)));
        textView.setText(sb.toString());
        TextView textView2 = this.totalBalance;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("$ ");
        double intValue = Shj.getWallet().getCatchMoney().intValue();
        Double.isNaN(intValue);
        sb2.append(String.format("%.02f", Double.valueOf(intValue / 100.0d)));
        textView2.setText(sb2.toString());
        TextView textView3 = this.totalPrice;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("$ ");
        double orderPrice = VmdHelper.get().getBqlOrder().getOrderPrice();
        Double.isNaN(orderPrice);
        sb3.append(String.format("%.02f", Double.valueOf(orderPrice / 100.0d)));
        textView3.setText(sb3.toString());
        checkMoney();
        if (Shj.getWallet().getCatchMoney().intValue() >= VmdHelper.get().getBqlOrder().getOrderPrice()) {
            PopView_Info.closeInfo(this.payNoticeId);
        }
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
                        this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Pay_en.1
                            AnonymousClass1() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                if (PopView_Pay_en.this.isShowing()) {
                                    PopView_Pay_en.this.close();
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

    /* renamed from: com.xyshj.machine.popview.PopView_Pay_en$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (PopView_Pay_en.this.isShowing()) {
                PopView_Pay_en.this.close();
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
        this.card.setChecked(false);
        this.cash.setChecked(true);
        try {
            VmdHelper.BqlOrder bqlOrder = VmdHelper.get().getBqlOrder();
            if (bqlOrder.getType().equals(VmdHelper.BQL_TYPE_CUSTOMER)) {
                int i = bqlOrder.getTops().size() > 3 ? 2 : 1;
                this.adapter.tops.clear();
                int i2 = 0;
                int i3 = 0;
                int i4 = 0;
                for (String str : bqlOrder.getTops().keySet()) {
                    if (bqlOrder.getTops().get(str).intValue() != 0) {
                        VmdHelper.TopItem topItem = VmdHelper.get().getTopItem(str);
                        if (topItem.getType() == 0) {
                            i2 += topItem.getPrice();
                        } else {
                            i3 += topItem.getPrice();
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(bqlOrder.getTops().get(str));
                        i4++;
                        int i5 = i4 % i;
                    }
                }
                this.adapter.tops.add("" + i2);
                this.adapter.tops.add("" + i3);
                int i6 = Build.VERSION.SDK_INT;
            } else {
                Iterator<String> it = bqlOrder.getBqlItems().keySet().iterator();
                while (it.hasNext()) {
                    bqlOrder.getBqlCount(it.next());
                }
                int i7 = Build.VERSION.SDK_INT;
            }
            TextView textView = this.plainPrice;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("$ ");
            Object[] objArr = new Object[1];
            double intValue = Shj.getShelfInfo(101).getPrice().intValue();
            Double.isNaN(intValue);
            objArr[0] = Double.valueOf(intValue / 100.0d);
            sb2.append(String.format("%.02f", objArr));
            textView.setText(sb2.toString());
            TextView textView2 = this.totalBalance;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("$ ");
            Object[] objArr2 = new Object[1];
            double intValue2 = Shj.getWallet().getCatchMoney().intValue();
            Double.isNaN(intValue2);
            objArr2[0] = Double.valueOf(intValue2 / 100.0d);
            sb3.append(String.format("%.02f", objArr2));
            textView2.setText(sb3.toString());
            TextView textView3 = this.totalPrice;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("$ ");
            Object[] objArr3 = new Object[1];
            double orderPrice = VmdHelper.get().getBqlOrder().getOrderPrice();
            Double.isNaN(orderPrice);
            objArr3[0] = Double.valueOf(orderPrice / 100.0d);
            sb4.append(String.format("%.02f", objArr3));
            textView3.setText(sb4.toString());
            this.adapter.notifyDataSetChanged();
        } catch (Exception unused) {
        }
        if (findViewById(R.id.payTypes_contain).getVisibility() != 0) {
            this.cash.setChecked(true);
            submitOrder();
        }
        if ((ShjManager.getOrderPayTypes().contains(OrderPayType.CASH) || ShjManager.getOrderPayTypes().contains(OrderPayType.ICCard)) && !MdbReader_BDT.get().isConnected()) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) UUID.randomUUID().toString()).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("" + ShjAppHelper.getString(R.string.bql_status_mdb_connecting))).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("time_out", (Object) 5000).put("showTime", (Object) false));
        }
    }

    void checkMoney() {
        if ((this.cash.isChecked() || this.card.isChecked()) && VmdHelper.get().getBqlOrder().getOrderPrice() <= Shj.getWallet().getCatchMoney().intValue()) {
            this.handler.postDelayed(new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Pay_en.2
                AnonymousClass2(Object obj) {
                    super(obj);
                }

                @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                public void run() {
                    try {
                        Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
                        Loger.writeLog("SHJ", "lsOrder:" + resentOrder.getUid());
                        OrderPayType orderPayType = PopView_Pay_en.this.cash.isChecked() ? OrderPayType.CASH : OrderPayType.ICCard;
                        resentOrder.getPrice();
                        ShjManager.getOrderManager().driverOfferLineOrder(orderPayType, resentOrder.getUid(), Shj.getWallet().getLastAddMoneyInfo() + ("" + System.currentTimeMillis()).substring(r2.length() - 4));
                    } catch (Exception unused) {
                    }
                    PopView_Pay_en.this.close();
                }
            }, 1000L);
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay_en$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends RunnableEx {
        AnonymousClass2(Object obj) {
            super(obj);
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            try {
                Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
                Loger.writeLog("SHJ", "lsOrder:" + resentOrder.getUid());
                OrderPayType orderPayType = PopView_Pay_en.this.cash.isChecked() ? OrderPayType.CASH : OrderPayType.ICCard;
                resentOrder.getPrice();
                ShjManager.getOrderManager().driverOfferLineOrder(orderPayType, resentOrder.getUid(), Shj.getWallet().getLastAddMoneyInfo() + ("" + System.currentTimeMillis()).substring(r2.length() - 4));
            } catch (Exception unused) {
            }
            PopView_Pay_en.this.close();
        }
    }

    void submitOrder() {
        Loger.writeLog("SALES", "submitOrder");
        ArrayList arrayList = new ArrayList();
        if (this.card.isChecked()) {
            arrayList.add(OrderPayType.ICCard);
        } else if (this.cash.isChecked()) {
            arrayList.add(OrderPayType.CASH);
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back /* 2131230762 */:
                close();
                break;
            case R.id.card /* 2131230918 */:
                this.cash.setChecked(false);
                this.card.setChecked(true);
                PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) this.payNoticeId).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("title", (Object) ShjAppHelper.getString(R.string.swipcard)).put("info", (Object) "Please pay within 30 seconds！").put("time_out", (Object) 30000).put("closeOnClick", (Object) true).put("showTime", (Object) true), 0, new PopView_Info.TimeTickListener() { // from class: com.xyshj.machine.popview.PopView_Pay_en.5
                    @Override // com.xyshj.machine.popview.PopView_Info.TimeTickListener
                    public boolean closeOnTimeTick(int i, JSONObject jSONObject) {
                        return false;
                    }

                    AnonymousClass5() {
                    }

                    @Override // com.xyshj.machine.popview.PopView_Info.TimeTickListener
                    public void onClosed(JSONObject jSONObject) {
                        PopView_Pay_en.this.close();
                    }
                });
                MdbReader_BDT.setEnabled(true);
                this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Pay_en.6
                    AnonymousClass6() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        PopView_Pay_en.this.submitOrder();
                    }
                }, 1000L);
                break;
            case R.id.cash /* 2131230921 */:
                this.card.setChecked(false);
                this.cash.setChecked(true);
                MdbReader_BDT.setEnabled(true);
                PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) this.payNoticeId).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("title", (Object) ShjAppHelper.getString(R.string.paybumoney)).put("info", (Object) "Please pay within 30 seconds！").put("time_out", (Object) 30000).put("showTime", (Object) true), 0, new PopView_Info.TimeTickListener() { // from class: com.xyshj.machine.popview.PopView_Pay_en.3
                    @Override // com.xyshj.machine.popview.PopView_Info.TimeTickListener
                    public boolean closeOnTimeTick(int i, JSONObject jSONObject) {
                        return false;
                    }

                    AnonymousClass3() {
                    }

                    @Override // com.xyshj.machine.popview.PopView_Info.TimeTickListener
                    public void onClosed(JSONObject jSONObject) {
                        PopView_Pay_en.this.close();
                    }
                });
                this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Pay_en.4
                    AnonymousClass4() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        PopView_Pay_en.this.submitOrder();
                    }
                }, 1000L);
                break;
            case R.id.enterpromo /* 2131230974 */:
                try {
                    VmdHelper.BqlOrder bqlOrder = VmdHelper.get().getBqlOrder();
                    String obj = bqlOrder.getBqlItems().keySet().toArray()[0].toString();
                    VmdHelper.BqlItem bqlItem = VmdHelper.get().getBqlItem(obj);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put(ShjDbHelper.COLUM_price, bqlOrder.getOrderPrice() * 100);
                    jSONObject.put("goodsName", obj);
                    jSONObject.put("goodsCode", bqlItem.getCode());
                    jSONObject.put("goodsPrice", bqlItem.getPrice());
                    BFPopView.showPopView("MainActivity", PopView_EnterCode.class, jSONObject.toString());
                    break;
                } catch (Exception unused) {
                    break;
                }
            case R.id.swippay /* 2131231270 */:
                onClick(this.card);
                break;
        }
        if (view.getId() == R.id.back) {
            close();
        } else if (view.getId() == R.id.pay) {
            checkMoney();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay_en$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements PopView_Info.TimeTickListener {
        @Override // com.xyshj.machine.popview.PopView_Info.TimeTickListener
        public boolean closeOnTimeTick(int i, JSONObject jSONObject) {
            return false;
        }

        AnonymousClass3() {
        }

        @Override // com.xyshj.machine.popview.PopView_Info.TimeTickListener
        public void onClosed(JSONObject jSONObject) {
            PopView_Pay_en.this.close();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay_en$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements Runnable {
        AnonymousClass4() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_Pay_en.this.submitOrder();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay_en$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements PopView_Info.TimeTickListener {
        @Override // com.xyshj.machine.popview.PopView_Info.TimeTickListener
        public boolean closeOnTimeTick(int i, JSONObject jSONObject) {
            return false;
        }

        AnonymousClass5() {
        }

        @Override // com.xyshj.machine.popview.PopView_Info.TimeTickListener
        public void onClosed(JSONObject jSONObject) {
            PopView_Pay_en.this.close();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Pay_en$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements Runnable {
        AnonymousClass6() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_Pay_en.this.submitOrder();
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
