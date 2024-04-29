package com.xyshj.machine.popview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.hanlu.toolsdk.BqlManager;
//import com.iflytek.cloud.SpeechConstant;
//import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.date.DateUtil;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.io.file.SDFileUtils;
import com.oysb.utils.view.BFPopView;
import com.oysb.utils.view.BasePopView;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.biz.ShjManager;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderArgs;
import com.shj.biz.order.OrderPayType;
import com.shj.biz.yg.YGDBHelper;
//import com.shj.device.cardreader.MdbReader_BDT;
import com.shj.setting.NetAddress.DefaultAMEAddrss;
import com.shj.setting.NetAddress.NetAddress;
import com.squareup.picasso.Picasso;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.machine.R;
import com.xyshj.machine.app.SysApp;
import com.xyshj.machine.app.VmdHelper;
import com.xyshj.machine.app.ZaadPayHelper;
import com.xyshj.machine.listener.MyGoodsStatusListener;
import com.xyshj.machine.listener.MyMoneyListener;
import com.xyshj.machine.listener.MyOrderListener;
import com.xyshj.machine.popview.PopView_Info;
import com.xyshj.machine.tools.UsbReaderHelper;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class PopView_PaySummary_st extends BasePopView implements View.OnClickListener {
    public static final int MESSAGE_TIMER = 3004;
    static final int MSG_CANCEL_DETAIL = 2026;
    public static final String PAY_CARD_ST = "PAY_CARD_ST";
    public static final String PAY_CASH_ST = "PAY_CASH_ST";
    public static final String PAY_QRCODE_ST = "PAY_QRCODE_ST";
    MyListViewAdapter adapter;
    FrameLayout count1;
    FrameLayout count2;
    FrameLayout count3;
    AlertDialog dialog_card;
    AlertDialog dialog_cash;
    AlertDialog dialog_qrcode;
    AlertDialog dialog_scan;
    AlertDialog dialog_zaad;
    RadioButton jd;
    String langEx;
    RelativeLayout listFooter;
    RelativeLayout listHeader;
    ListView listView;
    TextView plainPrice;
    Typeface popingMedim;
    ImageView qrImage;
    TextView qrcodelabel;
    TextView timecard;
    TextView timecash;
    TextView timeqrcode;
    Timer timer;
    TextView timezaad;
    TextView totalBalance;
    TextView totalPrice;
    RadioButton wx;
    TextView zaad_notice;
    TextView zaad_price;
    TextView zaad_unit;
    RadioButton zfb;
    int selectCount = 1;
    String lang = "";
    long clickTime = 0;
    boolean count3Enable = false;
    boolean count2Enable = false;
    boolean updateDisCountSuccess = false;
    boolean canClick2Pay = false;
    OrderPayType curPayType = OrderPayType.CASH;
    int time = 60;
    OrderPayType lastSubmitOrderType = null;
    TextView cashInserted = null;
    boolean cashSubmited = false;
    EditText zaad_phone = null;
    RadioButton ust = null;
    boolean isZaadPaying = false;
    int qrPayViewId = -1;
    long lastQrPayType = 0;
    boolean qrSubmited = false;
    long last0PayTime = 0;
    String payNoticeId = "payNoticeIdXXWWAASSS";
    RequestItem codePayRequestItem = null;

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.st_popview_summary, (ViewGroup) null);
        this.lang = CommonTool.getLanguage(inflate.getContext());
        this.langEx = CommonTool.getLanguageEx(inflate.getContext());
        inflate.setOnClickListener(this);
        File file = new File(SDFileUtils.SDCardRoot + "xyShj/resource/th_topbar.png");
        if (file.exists()) {
            inflate.findViewById(R.id.toplog).setVisibility(View.GONE);
            Picasso.get().load(file).into((ImageView) inflate.findViewById(R.id.toplogimg));
        } else {
            inflate.findViewById(R.id.toplogimg).setVisibility(View.GONE);
        }
        inflate.findViewById(R.id.back).setOnClickListener(this);
        this.count1 = (FrameLayout) inflate.findViewById(R.id.count1);
        this.count2 = (FrameLayout) inflate.findViewById(R.id.count2);
        this.count3 = (FrameLayout) inflate.findViewById(R.id.count3);
        this.count1.setOnClickListener(this);
        this.count2.setOnClickListener(this);
        this.count3.setOnClickListener(this);
        this.popingMedim = Typeface.createFromAsset(inflate.getContext().getAssets(), this.lang.equalsIgnoreCase("th") ? "Kanit.ttf" : "Poppins-Medium.ttf");
        inflate.findViewById(R.id.swippay).setOnClickListener(this);
        inflate.findViewById(R.id.enterpromo).setOnClickListener(this);
        ((TextView) inflate.findViewById(R.id.gopay)).setOnClickListener(this);
        TextView textView = (TextView) inflate.findViewById(R.id.totalprice);
        this.totalPrice = textView;
        textView.setClickable(true);
        this.totalPrice.setOnClickListener(this);
        ListView listView = (ListView) inflate.findViewById(R.id.listview);
        this.listView = listView;
        listView.setDivider(new ColorDrawable(0));
        this.listView.setDividerHeight(0);
        this.listHeader = (RelativeLayout) inflate.findViewById(R.id.listheader);
        this.listFooter = (RelativeLayout) inflate.findViewById(R.id.listfooter);
        this.plainPrice = (TextView) this.listHeader.findViewById(R.id.price);
        ((TextView) this.listHeader.findViewById(R.id.name)).setText(ShjAppHelper.getString(R.string.st_plain_ice_cream));
        this.totalBalance = (TextView) this.listFooter.findViewById(R.id.price);
        ((TextView) this.listFooter.findViewById(R.id.name)).setText(ShjAppHelper.getString(R.string.bql_yhje));
        ((TextView) this.listFooter.findViewById(R.id.name)).getPaint().setFakeBoldText(true);
        ((TextView) this.listFooter.findViewById(R.id.promo)).setTypeface(this.popingMedim);
        MyListViewAdapter myListViewAdapter = new MyListViewAdapter();
        this.adapter = myListViewAdapter;
        this.listView.setAdapter((ListAdapter) myListViewAdapter);
        inflate.findViewById(R.id.pay_cash).setOnClickListener(this);
        inflate.findViewById(R.id.pay_card).setOnClickListener(this);
        inflate.findViewById(R.id.pay_qrcode).setOnClickListener(this);
        inflate.findViewById(R.id.pay_scan).setOnClickListener(this);
        Object data = ShjManager.getData("ZAADPAY");
        if (data != null && data.toString().equals("TRUE")) {
            ((ImageView) inflate.findViewById(R.id.ico_pay_qrcode)).setImageResource(R.drawable.st_zaad2);
            ((TextView) inflate.findViewById(R.id.txt_pay_qrcode)).setText("Go to Pay");
        }
        return inflate;
    }

    void checkCount(View view, boolean z) {
        FrameLayout frameLayout = this.count1;
        int i = R.drawable.st_radio_classicbql_count_checked;
        frameLayout.setBackgroundResource(view == frameLayout ? R.drawable.st_radio_classicbql_count_checked : R.drawable.st_radio_classicbql_count_uncheck);
        FrameLayout frameLayout2 = this.count2;
        frameLayout2.setBackgroundResource(view == frameLayout2 ? R.drawable.st_radio_classicbql_count_checked : R.drawable.st_radio_classicbql_count_uncheck);
        FrameLayout frameLayout3 = this.count3;
        if (view != frameLayout3) {
            i = R.drawable.st_radio_classicbql_count_uncheck;
        }
        frameLayout3.setBackgroundResource(i);
        FrameLayout frameLayout4 = this.count2;
        frameLayout4.setAlpha(frameLayout4.isClickable() ? 1.0f : 0.35f);
        FrameLayout frameLayout5 = this.count3;
        frameLayout5.setAlpha(frameLayout5.isClickable() ? 1.0f : 0.35f);
        this.selectCount = view == this.count1 ? 1 : view == this.count2 ? 2 : 3;
        if (this.langEx.equalsIgnoreCase("en-US")) {
            if (this.count2.isClickable() || this.count3.isClickable()) {
                findViewById(R.id.selectCount).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.selectCount).setVisibility(View.INVISIBLE);
            }
        }
        if (this.langEx.equalsIgnoreCase("en-US")) {
            ImageView imageView = (ImageView) findViewById(R.id.ck_count1);
            ImageView imageView2 = (ImageView) findViewById(R.id.ck_count2);
            ImageView imageView3 = (ImageView) findViewById(R.id.ck_count3);
            imageView.setVisibility(this.selectCount == 1 ? View.VISIBLE : View.INVISIBLE);
            imageView2.setVisibility(this.selectCount == 2 ? View.VISIBLE : View.INVISIBLE);
            imageView3.setVisibility(this.selectCount == 3 ? View.VISIBLE : View.INVISIBLE);
        }
        VmdHelper.get().getBqlOrder().setBqlCount(VmdHelper.get().getBqlOrder().getBqlItems().keySet().toArray()[0].toString(), this.selectCount);
        updateMoney();
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
            LayoutInflater layoutInflater = (LayoutInflater) PopView_PaySummary_st.this.getContentView().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                view = layoutInflater.inflate(R.layout.st_detail_item_layout, (ViewGroup) null);
                view.setLayoutParams(new RadioGroup.LayoutParams(-1, PopView_PaySummary_st.this.listView.getResources().getDimensionPixelSize(R.dimen.px140)));
            }
            TextView textView = (TextView) view.findViewById(R.id.name);
            TextView textView2 = (TextView) view.findViewById(R.id.price);
            String str = this.tops.get(i);
            if (i == 0) {
                textView.setText(ShjAppHelper.getString(R.string.st_solid_toppings));
            } else {
                textView.setText(ShjAppHelper.getString(R.string.st_liquid_toppings));
            }
            StringBuilder sb = new StringBuilder();
            sb.append(SysApp.getPriceUnit());
            sb.append(StringUtils.SPACE);
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
        list.add(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_START);
        list.add(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_STATUS);
        list.add(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_SUCCESS);
        list.add(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_BLOCKED);
        list.add(VmdHelper.ACTION_UPDATE_YYJE);
        list.add(UsbReaderHelper.ACTION_USB_CARD_READ_TEXT);
        list.add(PAY_CARD_ST);
        list.add(PAY_CASH_ST);
        list.add(PAY_QRCODE_ST);
    }

    private void showTextTip(String str, int i) {
        PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + UUID.randomUUID().toString())).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("info", (Object) str).put("notic", (Object) ShjAppHelper.getString(R.string.welcome)).put("time_out", (Object) Integer.valueOf(i)).put("showTime", (Object) false));
    }

    void updateMoney() {
        ((TextView) this.listFooter.findViewById(R.id.promo)).setText(ShjAppHelper.getString(R.string.bql_yhje));
        TextView textView = this.totalBalance;
        StringBuilder sb = new StringBuilder();
        sb.append("-");
        sb.append(SysApp.getPriceUnit());
        sb.append(StringUtils.SPACE);
        double yhje = VmdHelper.get().getBqlOrder().getYhje();
        Double.isNaN(yhje);
        sb.append(String.format("%.02f", Double.valueOf(yhje / 100.0d)));
        textView.setText(sb.toString());
        TextView textView2 = this.totalPrice;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(SysApp.getPriceUnit());
        sb2.append(StringUtils.SPACE);
        double orderPrice = VmdHelper.get().getBqlOrder().getOrderPrice();
        Double.isNaN(orderPrice);
        sb2.append(String.format("%.02f", Double.valueOf(orderPrice / 100.0d)));
        textView2.setText(sb2.toString());
        Loger.writeLog("SHJ", "-----------3-----");
        checkMoney(this.curPayType != OrderPayType.CASH);
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
                case -1441691477:
                    if (str.equals(UsbReaderHelper.ACTION_USB_CARD_READ_TEXT)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1274066004:
                    if (str.equals(MyMoneyListener.ACTION_MONEY_CHANGED)) {
                        c = 2;
                        break;
                    }
                    break;
                case -1058098734:
                    if (str.equals(MyOrderListener.ACTION_ORDER_PAY_SUCCESS)) {
                        c = 3;
                        break;
                    }
                    break;
                case -811774838:
                    if (str.equals(MyOrderListener.ACTION_ORDER_PAY_CANCELED)) {
                        c = 4;
                        break;
                    }
                    break;
                case -700718707:
                    if (str.equals(MyOrderListener.ACTION_ORDER_MESSAGE)) {
                        c = 5;
                        break;
                    }
                    break;
                case -407091481:
                    if (str.equals(MyOrderListener.ACTION_ORDER_TIME)) {
                        c = 6;
                        break;
                    }
                    break;
                case -236900188:
                    if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_START)) {
                        c = 7;
                        break;
                    }
                    break;
                case -230148807:
                    if (str.equals(PAY_CARD_ST)) {
                        c = '\b';
                        break;
                    }
                    break;
                case -229106122:
                    if (str.equals(PAY_CASH_ST)) {
                        c = '\t';
                        break;
                    }
                    break;
                case 2203589:
                    if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_SUCCESS)) {
                        c = '\n';
                        break;
                    }
                    break;
                case 1126309256:
                    if (str.equals(VmdHelper.ACTION_UPDATE_YYJE)) {
                        c = 11;
                        break;
                    }
                    break;
                case 1246030800:
                    if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_STATUS)) {
                        c = '\f';
                        break;
                    }
                    break;
                case 1325412071:
                    if (str.equals(MyOrderListener.ACTION_ORDER_TIMEOUT)) {
                        c = CharUtils.CR;
                        break;
                    }
                    break;
                case 1847935406:
                    if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_BLOCKED)) {
                        c = 14;
                        break;
                    }
                    break;
                case 2055670203:
                    if (str.equals(PAY_QRCODE_ST)) {
                        c = 15;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    Loger.writeLog("SALES", "ACTION_ORDER_QRIMAGE_CREATED 1");
                    updateQrImage((OrderPayType) bundle.getSerializable("payType"));
                    return;
                case 1:
                    payWithCode(bundle.getString("card"));
                    return;
                case 2:
                    Loger.writeLog("SHJ", "-----------1-----");
                    TextView textView = this.cashInserted;
                    if (textView != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(ShjAppHelper.getString(R.string.st_total_balance));
                        sb.append(StringUtils.SPACE);
                        sb.append(SysApp.getPriceUnit());
                        sb.append(StringUtils.SPACE);
                        double intValue = Shj.getWallet().getCatchMoney().intValue();
                        Double.isNaN(intValue);
                        sb.append(String.format("%.02f", Double.valueOf(intValue / 100.0d)));
                        textView.setText(sb.toString());
                    }
                    Loger.writeLog("SHJ", "-----------2----");
                    updateMoney();
                    return;
                case 3:
                    AlertDialog alertDialog = this.dialog_qrcode;
                    if (alertDialog != null) {
                        alertDialog.cancel();
                    }
                    close();
                    return;
                case 4:
                    Loger.writeLog("SALES", "支付已取消");
                    return;
                case 5:
                    String string = bundle.getString("MESSAGE");
                    PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) UUID.randomUUID().toString()).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("" + string)).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("time_out", (Object) 5000).put("showTime", (Object) false));
                    return;
                case 6:
                    if (bundle.getInt("time") == 0) {
                        this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.4


                            @Override // java.lang.Runnable
                            public void run() {
                                if (PopView_PaySummary_st.this.isShowing()) {
                                    PopView_PaySummary_st.this.close();
                                }
                            }
                        }, 1500L);
                        return;
                    }
                    return;
                case 7:
                case '\n':
                case '\f':
                case 14:
                    AlertDialog alertDialog2 = this.dialog_card;
                    if (alertDialog2 != null && alertDialog2.isShowing()) {
                        this.dialog_card.dismiss();
                    }
                    AlertDialog alertDialog3 = this.dialog_zaad;
                    if (alertDialog3 != null && alertDialog3.isShowing()) {
                        this.dialog_zaad.dismiss();
                    }
                    AlertDialog alertDialog4 = this.dialog_cash;
                    if (alertDialog4 != null && alertDialog4.isShowing()) {
                        this.dialog_cash.dismiss();
                    }
                    AlertDialog alertDialog5 = this.dialog_scan;
                    if (alertDialog5 != null && alertDialog5.isShowing()) {
                        this.dialog_scan.dismiss();
                    }
                    close();
                    return;
                case '\b':
                    this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.1


                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_PaySummary_st.this.resetTimeCount();
                            PopView_PaySummary_st.this.payWithCard();
                        }
                    }, 100L);
                    return;
                case '\t':
                    this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.2


                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_PaySummary_st.this.resetTimeCount();
                            PopView_PaySummary_st.this.payWithCash();
                        }
                    }, 100L);
                    return;
                case 11:
                    updateMoney();
                    return;
                case '\r':
                    close();
                    return;
                case 15:
//                    if (MdbReader_BDT.isEnabled()) {
//                        MdbReader_BDT.get().cancel();
//                    }
                    this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.3


                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_PaySummary_st.this.resetTimeCount();
                            PopView_PaySummary_st.this.payWithQrcode();
                        }
                    }, 100L);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_PaySummary_st.this.resetTimeCount();
            PopView_PaySummary_st.this.payWithCard();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_PaySummary_st.this.resetTimeCount();
            PopView_PaySummary_st.this.payWithCash();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_PaySummary_st.this.resetTimeCount();
            PopView_PaySummary_st.this.payWithQrcode();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 implements Runnable {
        AnonymousClass4() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (PopView_PaySummary_st.this.isShowing()) {
                PopView_PaySummary_st.this.close();
            }
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onMessage(Message message) {
        int i = message.what;
        if (i == MSG_CANCEL_DETAIL) {
            onClick(findViewById(R.id.bt_back));
            return;
        }
        if (i != 3004) {
            return;
        }
        AlertDialog alertDialog = this.dialog_card;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.timecard.setText("" + this.time + ShjAppHelper.getString(R.string.lab_seconds));
            if (this.time == 0) {
//                MdbReader_BDT.get().cancel();
                this.timecard.setText("" + ShjManager.getOrderTimeOut() + ShjAppHelper.getString(R.string.lab_seconds));
                this.dialog_card.dismiss();
                return;
            }
            return;
        }
        AlertDialog alertDialog2 = this.dialog_zaad;
        if (alertDialog2 != null && alertDialog2.isShowing()) {
            this.timezaad.setText("" + this.time + ShjAppHelper.getString(R.string.lab_seconds));
            if (this.time == 0) {
                this.timezaad.setText("" + ShjManager.getOrderTimeOut() + ShjAppHelper.getString(R.string.lab_seconds));
                this.dialog_zaad.dismiss();
                return;
            }
            return;
        }
        AlertDialog alertDialog3 = this.dialog_cash;
        if (alertDialog3 != null && alertDialog3.isShowing()) {
            this.timecash.setText("" + this.time + ShjAppHelper.getString(R.string.lab_seconds));
            if (this.time == 0) {
//                MdbReader_BDT.get().cancel();
                this.timecash.setText("" + ShjManager.getOrderTimeOut() + ShjAppHelper.getString(R.string.lab_seconds));
                this.dialog_cash.dismiss();
                return;
            }
            return;
        }
        AlertDialog alertDialog4 = this.dialog_scan;
        if (alertDialog4 != null && alertDialog4.isShowing()) {
            this.timecash.setText("" + this.time + ShjAppHelper.getString(R.string.lab_seconds));
            if (this.time == 0) {
                UsbReaderHelper.tryReadUsbICCardInfo(false);
                this.timecash.setText("" + ShjManager.getOrderTimeOut() + ShjAppHelper.getString(R.string.lab_seconds));
                this.dialog_scan.dismiss();
                return;
            }
            return;
        }
        AlertDialog alertDialog5 = this.dialog_qrcode;
        if (alertDialog5 == null || !alertDialog5.isShowing()) {
            return;
        }
        this.timeqrcode.setText("" + this.time + ShjAppHelper.getString(R.string.lab_seconds));
        if (this.time == 0) {
            ShjManager.getOrderManager().cancelOrder();
            this.timeqrcode.setText("" + ShjManager.getOrderTimeOut() + ShjAppHelper.getString(R.string.lab_seconds));
            this.dialog_qrcode.dismiss();
        }
    }

    boolean checkUpdateDiscount() {
        return !CommonTool.getLanguage(getParent().getContext()).equalsIgnoreCase("th") || this.updateDisCountSuccess;
    }

    void updateDisCount() {
        this.updateDisCountSuccess = false;
        if (NetAddress.getiAddress() instanceof DefaultAMEAddrss) {
            this.count3Enable = true;
            this.count2Enable = true;
        } else {
            this.count2Enable = false;
            this.count3Enable = false;
        }
        try {
            if (NetAddress.queryDiscountsUrl() == null) {
                return;
            }
            VmdHelper.get().updateDiscount(1.0d, 1.0d);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("jqbh", Shj.getMachineId());
            JSONArray jSONArray = new JSONArray();
//            jSONObject.put(SpeechEvent.KEY_EVENT_RECORD_DATA, jSONArray);
            VmdHelper.BqlOrder bqlOrder = VmdHelper.get().getBqlOrder();
            for (String str : bqlOrder.getBqlItems().keySet()) {
                VmdHelper.BqlItem bqlItem = VmdHelper.get().getBqlItem(str);
                for (int i = 0; i < bqlOrder.getBqlCount(str); i++) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("name", str);
                    jSONObject2.put("spbh", bqlItem.getCode());
                    jSONObject2.put(ShjDbHelper.COLUM_price, bqlItem.getPrice());
                    jSONArray.put(jSONObject2);
                }
            }
            if (bqlOrder.getType().equals(VmdHelper.BQL_TYPE_CUSTOMER)) {
                for (String str2 : bqlOrder.getTops().keySet()) {
                    VmdHelper.TopItem topItem = VmdHelper.get().getTopItem(str2);
                    for (int i2 = 0; i2 < bqlOrder.getTopCount(str2); i2++) {
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put("name", str2);
                        jSONObject3.put("spbh", topItem.getCode());
                        jSONObject3.put(ShjDbHelper.COLUM_price, topItem.getPrice());
                        jSONArray.put(jSONObject3);
                    }
                }
            }
            RequestItem requestItem = new RequestItem(NetAddress.queryDiscountsUrl(), jSONObject, "POST");
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.5
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i3, String str3, Throwable th) {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }



                /* JADX WARN: Removed duplicated region for block: B:23:0x008a A[Catch: Exception -> 0x00bd, TryCatch #0 {Exception -> 0x00bd, blocks: (B:3:0x0003, B:5:0x0016, B:7:0x001c, B:8:0x0026, B:11:0x0037, B:15:0x0054, B:18:0x0062, B:21:0x007d, B:23:0x008a, B:24:0x0095, B:27:0x0069, B:29:0x006f, B:31:0x0075, B:32:0x003e, B:34:0x0044, B:36:0x004a, B:37:0x0021, B:38:0x00af), top: B:2:0x0003 }] */
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public boolean onSuccess(com.oysb.utils.http.RequestItem r8, int r9, java.lang.String r10) {
                    /*
                        r7 = this;
                        java.lang.String r8 = "data"
                        r9 = 1
                        org.json.JSONObject r0 = new org.json.JSONObject     // Catch: java.lang.Exception -> Lbd
                        r0.<init>(r10)     // Catch: java.lang.Exception -> Lbd
                        java.lang.String r10 = "code"
                        java.lang.String r10 = r0.getString(r10)     // Catch: java.lang.Exception -> Lbd
                        java.lang.String r1 = "H0000"
                        boolean r10 = r10.equals(r1)     // Catch: java.lang.Exception -> Lbd
                        if (r10 == 0) goto Laf
                        boolean r10 = r0.has(r8)     // Catch: java.lang.Exception -> Lbd
                        if (r10 == 0) goto L21
                        org.json.JSONObject r8 = r0.getJSONObject(r8)     // Catch: java.lang.Exception -> Lbd
                        goto L26
                    L21:
                        org.json.JSONObject r8 = new org.json.JSONObject     // Catch: java.lang.Exception -> Lbd
                        r8.<init>()     // Catch: java.lang.Exception -> Lbd
                    L26:
                        com.xyshj.machine.popview.PopView_PaySummary_st r10 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                        r0 = 0
                        r10.count2Enable = r0     // Catch: java.lang.Exception -> Lbd
                        com.shj.setting.NetAddress.IAddress r10 = com.shj.setting.NetAddress.NetAddress.getiAddress()     // Catch: java.lang.Exception -> Lbd
                        boolean r10 = r10 instanceof com.shj.setting.NetAddress.DefaultAMEAddrss     // Catch: java.lang.Exception -> Lbd
                        r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
                        java.lang.String r3 = "count2discount"
                        if (r10 == 0) goto L3e
                        boolean r10 = r8.has(r3)     // Catch: java.lang.Exception -> Lbd
                        if (r10 != 0) goto L3e
                        goto L53
                    L3e:
                        boolean r10 = r8.has(r3)     // Catch: java.lang.Exception -> Lbd
                        if (r10 == 0) goto L53
                        java.lang.Object r10 = r8.get(r3)     // Catch: java.lang.Exception -> Lbd
                        if (r10 == 0) goto L53
                        com.xyshj.machine.popview.PopView_PaySummary_st r10 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                        r10.count2Enable = r9     // Catch: java.lang.Exception -> Lbd
                        double r3 = r8.getDouble(r3)     // Catch: java.lang.Exception -> Lbd
                        goto L54
                    L53:
                        r3 = r1
                    L54:
                        com.xyshj.machine.popview.PopView_PaySummary_st r10 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                        r10.count3Enable = r0     // Catch: java.lang.Exception -> Lbd
                        com.shj.setting.NetAddress.IAddress r10 = com.shj.setting.NetAddress.NetAddress.getiAddress()     // Catch: java.lang.Exception -> Lbd
                        boolean r10 = r10 instanceof com.shj.setting.NetAddress.DefaultAMEAddrss     // Catch: java.lang.Exception -> Lbd
                        java.lang.String r0 = "count3discount"
                        if (r10 == 0) goto L69
                        boolean r10 = r8.has(r0)     // Catch: java.lang.Exception -> Lbd
                        if (r10 != 0) goto L69
                        goto L7d
                    L69:
                        boolean r10 = r8.has(r0)     // Catch: java.lang.Exception -> Lbd
                        if (r10 == 0) goto L7d
                        java.lang.Object r10 = r8.get(r0)     // Catch: java.lang.Exception -> Lbd
                        if (r10 == 0) goto L7d
                        com.xyshj.machine.popview.PopView_PaySummary_st r10 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                        r10.count3Enable = r9     // Catch: java.lang.Exception -> Lbd
                        double r1 = r8.getDouble(r0)     // Catch: java.lang.Exception -> Lbd
                    L7d:
                        com.xyshj.machine.app.VmdHelper r8 = com.xyshj.machine.app.VmdHelper.get()     // Catch: java.lang.Exception -> Lbd
                        r10 = 2
                        double r5 = r8.getDiscount(r10)     // Catch: java.lang.Exception -> Lbd
                        int r8 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                        if (r8 != 0) goto L95
                        com.xyshj.machine.app.VmdHelper r8 = com.xyshj.machine.app.VmdHelper.get()     // Catch: java.lang.Exception -> Lbd
                        r10 = 3
                        double r5 = r8.getDiscount(r10)     // Catch: java.lang.Exception -> Lbd
                        int r8 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
                    L95:
                        com.xyshj.machine.app.VmdHelper r8 = com.xyshj.machine.app.VmdHelper.get()     // Catch: java.lang.Exception -> Lbd
                        r8.updateDiscount(r3, r1)     // Catch: java.lang.Exception -> Lbd
                        com.xyshj.machine.popview.PopView_PaySummary_st r8 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                        android.os.Handler r8 = com.xyshj.machine.popview.PopView_PaySummary_st.access$000(r8)     // Catch: java.lang.Exception -> Lbd
                        com.xyshj.machine.popview.PopView_PaySummary_st$5$1 r10 = new com.xyshj.machine.popview.PopView_PaySummary_st$5$1     // Catch: java.lang.Exception -> Lbd
                        r10.<init>()     // Catch: java.lang.Exception -> Lbd
                        r8.postAtFrontOfQueue(r10)     // Catch: java.lang.Exception -> Lbd
                        com.xyshj.machine.popview.PopView_PaySummary_st r8 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                        r8.updateDisCountSuccess = r9     // Catch: java.lang.Exception -> Lbd
                        goto Lbd
                    Laf:
                        com.xyshj.machine.popview.PopView_PaySummary_st r8 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                        android.os.Handler r8 = com.xyshj.machine.popview.PopView_PaySummary_st.access$100(r8)     // Catch: java.lang.Exception -> Lbd
                        com.xyshj.machine.popview.PopView_PaySummary_st$5$2 r10 = new com.xyshj.machine.popview.PopView_PaySummary_st$5$2     // Catch: java.lang.Exception -> Lbd
                        r10.<init>()     // Catch: java.lang.Exception -> Lbd
                        r8.postAtFrontOfQueue(r10)     // Catch: java.lang.Exception -> Lbd
                    Lbd:
                        return r9
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.popview.PopView_PaySummary_st.AnonymousClass5.onSuccess(com.oysb.utils.http.RequestItem, int, java.lang.String):boolean");
                }

                /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$5$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements Runnable {
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        PopView_PaySummary_st.this.count2.setClickable(PopView_PaySummary_st.this.count2Enable);
                        PopView_PaySummary_st.this.count3.setClickable(PopView_PaySummary_st.this.count3Enable);
                        PopView_PaySummary_st.this.checkCount(PopView_PaySummary_st.this.count1, true);
                    }
                }

                /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$5$2 */
                /* loaded from: classes2.dex */
            });
            RequestHelper.request(requestItem);
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i3, String str3, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass5() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem, int i, String str) {
            /*  JADX ERROR: Method code generation error
                java.lang.NullPointerException
                */
            /*
                this = this;
                java.lang.String r8 = "data"
                r9 = 1
                org.json.JSONObject r0 = new org.json.JSONObject     // Catch: java.lang.Exception -> Lbd
                r0.<init>(r10)     // Catch: java.lang.Exception -> Lbd
                java.lang.String r10 = "code"
                java.lang.String r10 = r0.getString(r10)     // Catch: java.lang.Exception -> Lbd
                java.lang.String r1 = "H0000"
                boolean r10 = r10.equals(r1)     // Catch: java.lang.Exception -> Lbd
                if (r10 == 0) goto Laf
                boolean r10 = r0.has(r8)     // Catch: java.lang.Exception -> Lbd
                if (r10 == 0) goto L21
                org.json.JSONObject r8 = r0.getJSONObject(r8)     // Catch: java.lang.Exception -> Lbd
                goto L26
            L21:
                org.json.JSONObject r8 = new org.json.JSONObject     // Catch: java.lang.Exception -> Lbd
                r8.<init>()     // Catch: java.lang.Exception -> Lbd
            L26:
                com.xyshj.machine.popview.PopView_PaySummary_st r10 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                r0 = 0
                r10.count2Enable = r0     // Catch: java.lang.Exception -> Lbd
                com.shj.setting.NetAddress.IAddress r10 = com.shj.setting.NetAddress.NetAddress.getiAddress()     // Catch: java.lang.Exception -> Lbd
                boolean r10 = r10 instanceof com.shj.setting.NetAddress.DefaultAMEAddrss     // Catch: java.lang.Exception -> Lbd
                r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
                java.lang.String r3 = "count2discount"
                if (r10 == 0) goto L3e
                boolean r10 = r8.has(r3)     // Catch: java.lang.Exception -> Lbd
                if (r10 != 0) goto L3e
                goto L53
            L3e:
                boolean r10 = r8.has(r3)     // Catch: java.lang.Exception -> Lbd
                if (r10 == 0) goto L53
                java.lang.Object r10 = r8.get(r3)     // Catch: java.lang.Exception -> Lbd
                if (r10 == 0) goto L53
                com.xyshj.machine.popview.PopView_PaySummary_st r10 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                r10.count2Enable = r9     // Catch: java.lang.Exception -> Lbd
                double r3 = r8.getDouble(r3)     // Catch: java.lang.Exception -> Lbd
                goto L54
            L53:
                r3 = r1
            L54:
                com.xyshj.machine.popview.PopView_PaySummary_st r10 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                r10.count3Enable = r0     // Catch: java.lang.Exception -> Lbd
                com.shj.setting.NetAddress.IAddress r10 = com.shj.setting.NetAddress.NetAddress.getiAddress()     // Catch: java.lang.Exception -> Lbd
                boolean r10 = r10 instanceof com.shj.setting.NetAddress.DefaultAMEAddrss     // Catch: java.lang.Exception -> Lbd
                java.lang.String r0 = "count3discount"
                if (r10 == 0) goto L69
                boolean r10 = r8.has(r0)     // Catch: java.lang.Exception -> Lbd
                if (r10 != 0) goto L69
                goto L7d
            L69:
                boolean r10 = r8.has(r0)     // Catch: java.lang.Exception -> Lbd
                if (r10 == 0) goto L7d
                java.lang.Object r10 = r8.get(r0)     // Catch: java.lang.Exception -> Lbd
                if (r10 == 0) goto L7d
                com.xyshj.machine.popview.PopView_PaySummary_st r10 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                r10.count3Enable = r9     // Catch: java.lang.Exception -> Lbd
                double r1 = r8.getDouble(r0)     // Catch: java.lang.Exception -> Lbd
            L7d:
                com.xyshj.machine.app.VmdHelper r8 = com.xyshj.machine.app.VmdHelper.get()     // Catch: java.lang.Exception -> Lbd
                r10 = 2
                double r5 = r8.getDiscount(r10)     // Catch: java.lang.Exception -> Lbd
                int r8 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r8 != 0) goto L95
                com.xyshj.machine.app.VmdHelper r8 = com.xyshj.machine.app.VmdHelper.get()     // Catch: java.lang.Exception -> Lbd
                r10 = 3
                double r5 = r8.getDiscount(r10)     // Catch: java.lang.Exception -> Lbd
                int r8 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            L95:
                com.xyshj.machine.app.VmdHelper r8 = com.xyshj.machine.app.VmdHelper.get()     // Catch: java.lang.Exception -> Lbd
                r8.updateDiscount(r3, r1)     // Catch: java.lang.Exception -> Lbd
                com.xyshj.machine.popview.PopView_PaySummary_st r8 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                android.os.Handler r8 = com.xyshj.machine.popview.PopView_PaySummary_st.access$000(r8)     // Catch: java.lang.Exception -> Lbd
                com.xyshj.machine.popview.PopView_PaySummary_st$5$1 r10 = new com.xyshj.machine.popview.PopView_PaySummary_st$5$1     // Catch: java.lang.Exception -> Lbd
                r10.<init>()     // Catch: java.lang.Exception -> Lbd
                r8.postAtFrontOfQueue(r10)     // Catch: java.lang.Exception -> Lbd
                com.xyshj.machine.popview.PopView_PaySummary_st r8 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                r8.updateDisCountSuccess = r9     // Catch: java.lang.Exception -> Lbd
                goto Lbd
            Laf:
                com.xyshj.machine.popview.PopView_PaySummary_st r8 = com.xyshj.machine.popview.PopView_PaySummary_st.this     // Catch: java.lang.Exception -> Lbd
                android.os.Handler r8 = com.xyshj.machine.popview.PopView_PaySummary_st.access$100(r8)     // Catch: java.lang.Exception -> Lbd
                com.xyshj.machine.popview.PopView_PaySummary_st$5$2 r10 = new com.xyshj.machine.popview.PopView_PaySummary_st$5$2     // Catch: java.lang.Exception -> Lbd
                r10.<init>()     // Catch: java.lang.Exception -> Lbd
                r8.postAtFrontOfQueue(r10)     // Catch: java.lang.Exception -> Lbd
            Lbd:
                return r9
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.popview.PopView_PaySummary_st.AnonymousClass5.onSuccess(com.oysb.utils.http.RequestItem, int, java.lang.String):boolean");
        }

        /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$5$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                PopView_PaySummary_st.this.count2.setClickable(PopView_PaySummary_st.this.count2Enable);
                PopView_PaySummary_st.this.count3.setClickable(PopView_PaySummary_st.this.count3Enable);
                PopView_PaySummary_st.this.checkCount(PopView_PaySummary_st.this.count1, true);
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$5$2 */
        /* loaded from: classes2.dex */

    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidShow() {
        super.onViewDidShow();
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewWillShow() {
        int i;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        super.onViewWillShow();
        this.isZaadPaying = false;
        this.last0PayTime = 0L;
        setCloseTimeCount(120);
        updateDisCount();
        this.canClick2Pay = true;
//        MdbReader_BDT.setEnabled(false);
//        MdbReader_BDT.get().setDebug(BqlManager.get().isDebug());
        if (ShjManager.getOrderPayTypes().contains(OrderPayType.WEIXIN) || ShjManager.getOrderPayTypes().contains(OrderPayType.ZFB) || ShjManager.getOrderPayTypes().contains(OrderPayType.YLJH)) {
            i = 4;
            z = true;
        } else {
            findViewById(R.id.pay_qrcode).setVisibility(View.GONE);
            i = 3;
            z = false;
        }
        if (ShjManager.getOrderPayTypes().contains(OrderPayType.ICCard) || ShjManager.getOrderPayTypes().contains(OrderPayType.ECard)) {
            z2 = true;
        } else {
            i--;
            findViewById(R.id.pay_card).setVisibility(View.GONE);
            z2 = false;
        }
        if (ShjManager.getOrderPayTypes().contains(OrderPayType.CASH)) {
            z3 = true;
        } else {
            i--;
            findViewById(R.id.pay_cash).setVisibility(View.GONE);
            z3 = false;
        }
        if (ShjAppHelper.getScanPay()) {
            z4 = true;
        } else {
            i--;
            findViewById(R.id.pay_scan).setVisibility(View.GONE);
            z4 = false;
        }
        int i2 = 2;
        if (i < 2) {
            findViewById(R.id.split0).setVisibility(View.GONE);
            findViewById(R.id.split1).setVisibility(View.GONE);
            findViewById(R.id.split2).setVisibility(View.GONE);
        } else if (i == 2) {
            if (z3) {
                findViewById(R.id.split1).setVisibility(View.GONE);
                findViewById(R.id.split2).setVisibility(View.GONE);
            } else {
                findViewById(R.id.split0).setVisibility(View.GONE);
                if (z2) {
                    findViewById(R.id.split2).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.split1).setVisibility(View.GONE);
                }
            }
        } else if (i == 3) {
            if (!z3) {
                findViewById(R.id.split0).setVisibility(View.GONE);
            } else if (!z2) {
                findViewById(R.id.split1).setVisibility(View.GONE);
            } else if (!z4) {
                findViewById(R.id.split1).setVisibility(View.GONE);
            } else if (!z) {
                findViewById(R.id.split2).setVisibility(View.GONE);
            }
        }
        int intValue = Shj.getShelfInfo(201).getGoodsCount().intValue();
        int intValue2 = Shj.getShelfInfo(202).getGoodsCount().intValue();
        this.count3.setClickable(this.count3Enable);
        this.count2.setClickable(this.count2Enable);
        this.count1.setClickable(true);
        if (intValue < 3 || intValue2 < 3) {
            this.count3.setClickable(false);
        }
        if (intValue < 2 || intValue2 < 2) {
            this.count2.setClickable(false);
        }
        if (intValue < 1 || intValue2 < 1) {
            this.count1.setClickable(false);
        }
        try {
            VmdHelper.BqlOrder bqlOrder = VmdHelper.get().getBqlOrder();
            if (bqlOrder.getType().equals(VmdHelper.BQL_TYPE_CUSTOMER)) {
                if (bqlOrder.getTops().size() <= 3) {
                    i2 = 1;
                }
                this.adapter.tops.clear();
                int i3 = 0;
                int i4 = 0;
                int i5 = 0;
                for (String str : bqlOrder.getTops().keySet()) {
                    if (bqlOrder.getTops().get(str).intValue() != 0) {
                        VmdHelper.TopItem topItem = VmdHelper.get().getTopItem(str);
                        if (topItem.getType() == 0) {
                            i3 += topItem.getPrice();
                        } else {
                            i4 += topItem.getPrice();
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(bqlOrder.getTops().get(str));
                        i5++;
                        int i6 = i5 % i2;
                    }
                }
                this.adapter.tops.add("" + i3);
                this.adapter.tops.add("" + i4);
                int i7 = Build.VERSION.SDK_INT;
            } else {
                Iterator<String> it = bqlOrder.getBqlItems().keySet().iterator();
                while (it.hasNext()) {
                    bqlOrder.getBqlCount(it.next());
                }
                int i8 = Build.VERSION.SDK_INT;
            }
            TextView textView = this.plainPrice;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(SysApp.getPriceUnit());
            sb2.append(StringUtils.SPACE);
            Object[] objArr = new Object[1];
            double intValue3 = Shj.getShelfInfo(101).getPrice().intValue();
            Double.isNaN(intValue3);
            objArr[0] = Double.valueOf(intValue3 / 100.0d);
            sb2.append(String.format("%.02f", objArr));
            textView.setText(sb2.toString());
            TextView textView2 = this.totalBalance;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(SysApp.getPriceUnit());
            sb3.append(StringUtils.SPACE);
            Object[] objArr2 = new Object[1];
            double intValue4 = Shj.getWallet().getCatchMoney().intValue();
            Double.isNaN(intValue4);
            objArr2[0] = Double.valueOf(intValue4 / 100.0d);
            sb3.append(String.format("%.02f", objArr2));
            textView2.setText(sb3.toString());
            TextView textView3 = this.totalPrice;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(SysApp.getPriceUnit());
            sb4.append(StringUtils.SPACE);
            Object[] objArr3 = new Object[1];
            double orderPrice = VmdHelper.get().getBqlOrder().getOrderPrice();
            Double.isNaN(orderPrice);
            objArr3[0] = Double.valueOf(orderPrice / 100.0d);
            sb4.append(String.format("%.02f", objArr3));
            textView3.setText(sb4.toString());
            this.adapter.notifyDataSetChanged();
        } catch (Exception unused) {
        }
        checkCount(this.count1, false);
    }

    void checkMoney(boolean z) {
        if (!((z && this.curPayType == OrderPayType.CASH) || this.curPayType == OrderPayType.ICCard) || VmdHelper.get().getBqlOrder().getOrderPrice() > Shj.getWallet().getCatchMoney().intValue()) {
            return;
        }
        this.canClick2Pay = false;
        this.handler.postDelayed(new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.6

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                if (PopView_PaySummary_st.this.dialog_card != null && PopView_PaySummary_st.this.dialog_card.isShowing()) {
                    PopView_PaySummary_st.this.dialog_card.dismiss();
                }
                if (PopView_PaySummary_st.this.dialog_zaad != null && PopView_PaySummary_st.this.dialog_zaad.isShowing()) {
                    PopView_PaySummary_st.this.dialog_zaad.dismiss();
                }
                if (PopView_PaySummary_st.this.dialog_cash != null && PopView_PaySummary_st.this.dialog_cash.isShowing()) {
                    PopView_PaySummary_st.this.dialog_cash.dismiss();
                }
                if (PopView_PaySummary_st.this.dialog_qrcode != null && PopView_PaySummary_st.this.dialog_qrcode.isShowing()) {
                    PopView_PaySummary_st.this.dialog_qrcode.dismiss();
                }
                try {
                    Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
                    Loger.writeLog("SHJ", "lsOrder:" + resentOrder.getUid());
                    resentOrder.getPrice();
//                    ShjManager.getOrderManager().driverOfferLineOrder(PopView_PaySummary_st.this.curPayType, resentOrder.getUid(), Shj.getWallet().getLastAddMoneyInfo() + ("" + System.currentTimeMillis()).substring(r2.length() - 4));
                } catch (Exception e) {
                    Loger.writeException("SHJ", e);
                }
                PopView_PaySummary_st.this.close();
            }
        }, 1000L);
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 extends RunnableEx {
        AnonymousClass6(Object obj) {
            super(obj);
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            if (PopView_PaySummary_st.this.dialog_card != null && PopView_PaySummary_st.this.dialog_card.isShowing()) {
                PopView_PaySummary_st.this.dialog_card.dismiss();
            }
            if (PopView_PaySummary_st.this.dialog_zaad != null && PopView_PaySummary_st.this.dialog_zaad.isShowing()) {
                PopView_PaySummary_st.this.dialog_zaad.dismiss();
            }
            if (PopView_PaySummary_st.this.dialog_cash != null && PopView_PaySummary_st.this.dialog_cash.isShowing()) {
                PopView_PaySummary_st.this.dialog_cash.dismiss();
            }
            if (PopView_PaySummary_st.this.dialog_qrcode != null && PopView_PaySummary_st.this.dialog_qrcode.isShowing()) {
                PopView_PaySummary_st.this.dialog_qrcode.dismiss();
            }
            try {
                Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
                Loger.writeLog("SHJ", "lsOrder:" + resentOrder.getUid());
                resentOrder.getPrice();
//                ShjManager.getOrderManager().driverOfferLineOrder(PopView_PaySummary_st.this.curPayType, resentOrder.getUid(), Shj.getWallet().getLastAddMoneyInfo() + ("" + System.currentTimeMillis()).substring(r2.length() - 4));
            } catch (Exception e) {
                Loger.writeException("SHJ", e);
            }
            PopView_PaySummary_st.this.close();
        }
    }

    void resetTimer() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        this.time = ShjManager.getOrderTimeOut();
        Timer timer2 = new Timer();
        this.timer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.7


            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    PopView_PaySummary_st popView_PaySummary_st = PopView_PaySummary_st.this;
                    popView_PaySummary_st.time--;
                    PopView_PaySummary_st.this.handler.sendEmptyMessage(PopView_PaySummary_st.MESSAGE_TIMER);
                    if (PopView_PaySummary_st.this.time == 0) {
                        PopView_PaySummary_st.this.timer.cancel();
                        PopView_PaySummary_st.this.timer = null;
                    }
                } catch (Exception unused) {
                }
            }
        }, 1000L, 1000L);
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 extends TimerTask {
        AnonymousClass7() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                PopView_PaySummary_st popView_PaySummary_st = PopView_PaySummary_st.this;
                popView_PaySummary_st.time--;
                PopView_PaySummary_st.this.handler.sendEmptyMessage(PopView_PaySummary_st.MESSAGE_TIMER);
                if (PopView_PaySummary_st.this.time == 0) {
                    PopView_PaySummary_st.this.timer.cancel();
                    PopView_PaySummary_st.this.timer = null;
                }
            } catch (Exception unused) {
            }
        }
    }

    void submitOrder(OrderPayType orderPayType) {
        Loger.writeLog("SALES", "submitOrder");
        ArrayList arrayList = new ArrayList();
        arrayList.add(orderPayType);
        this.curPayType = orderPayType;
        this.lastSubmitOrderType = orderPayType;
        ArrayList arrayList2 = new ArrayList();
        VmdHelper.BqlOrder bqlOrder = VmdHelper.get().getBqlOrder();
        for (String str : bqlOrder.getBqlItems().keySet()) {
            VmdHelper.BqlItem bqlItem = VmdHelper.get().getBqlItem(str);
            for (int i = 0; i < bqlOrder.getBqlCount(str); i++) {
                arrayList2.add(ShjManager.getGoodsManager().getGoodsByCode(bqlItem.getCode()));
                if (bqlOrder.getType().equals(VmdHelper.BQL_TYPE_CUSTOMER)) {
                    for (String str2 : bqlOrder.getTops().keySet()) {
                        VmdHelper.TopItem topItem = VmdHelper.get().getTopItem(str2);
                        for (int i2 = 0; i2 < bqlOrder.getTopCount(str2); i2++) {
                            arrayList2.add(ShjManager.getGoodsManager().getGoodsByCode(topItem.getCode()));
                        }
                    }
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

    void payWithCash() {
        if (Shj.getMachineId().equals("1707600007") || checkUpdateDiscount()) {
            resetTimeCount();
//            MdbReader_BDT.setEnabled(true);
            this.cashSubmited = false;
            Context context = getParent().getContext();
            this.qrPayViewId = -1;
            AlertDialog alertDialog = this.dialog_cash;
            if (alertDialog == null) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.st_popview_cashcard, (ViewGroup) null);
                ((ImageView) inflate.findViewById(R.id.image)).setImageResource(R.drawable.img_paycash2);
                this.timecash = (TextView) inflate.findViewById(R.id.time);
                inflate.findViewById(R.id.moneybar).setVisibility(View.VISIBLE);
                ((TextView) inflate.findViewById(R.id.title)).setText(R.string.lab_please2pay);
                TextView textView = (TextView) inflate.findViewById(R.id.money);
                this.cashInserted = textView;
                if (textView != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ShjAppHelper.getString(R.string.st_total_balance));
                    sb.append(StringUtils.SPACE);
                    sb.append(SysApp.getPriceUnit());
                    sb.append(StringUtils.SPACE);
                    double intValue = Shj.getWallet().getCatchMoney().intValue();
                    Double.isNaN(intValue);
                    sb.append(String.format("%.02f", Double.valueOf(intValue / 100.0d)));
                    textView.setText(sb.toString());
                }
                inflate.findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.8


                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        PopView_PaySummary_st.this.dialog_cash.cancel();
                    }
                });
                inflate.findViewById(R.id.dopay).setEnabled(true);
                inflate.findViewById(R.id.dopay).setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.9


                    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$9$1 */
                    /* loaded from: classes2.dex */


                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        PopView_PaySummary_st.this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.9.1


                            @Override // java.lang.Runnable
                            public void run() {
                                if (PopView_PaySummary_st.this.cashSubmited) {
                                    return;
                                }
                                PopView_PaySummary_st.this.cashSubmited = true;
                                view.setEnabled(false);
                                PopView_PaySummary_st.this.submitOrder(OrderPayType.CASH);
                                PopView_PaySummary_st.this.checkMoney(true);
                            }
                        }, 1000L);
                    }
                });
                AlertDialog create = new AlertDialog.Builder(context).create();
                this.dialog_cash = create;
                create.show();
                Window window = this.dialog_cash.getWindow();
                window.setBackgroundDrawable(null);
                window.setContentView(inflate);
            } else {
                alertDialog.show();
            }
            resetTimer();
            TextView textView2 = this.cashInserted;
            if (textView2 != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(ShjAppHelper.getString(R.string.st_total_balance));
                sb2.append(StringUtils.SPACE);
                sb2.append(SysApp.getPriceUnit());
                sb2.append(StringUtils.SPACE);
                double intValue2 = Shj.getWallet().getCatchMoney().intValue();
                Double.isNaN(intValue2);
                sb2.append(String.format("%.02f", Double.valueOf(intValue2 / 100.0d)));
                textView2.setText(sb2.toString());
            }
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements View.OnClickListener {
        AnonymousClass8() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PopView_PaySummary_st.this.dialog_cash.cancel();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements View.OnClickListener {
        AnonymousClass9() {
        }

        /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$9$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements Runnable {
            @Override // java.lang.Runnable
            public void run() {
                if (PopView_PaySummary_st.this.cashSubmited) {
                    return;
                }
                PopView_PaySummary_st.this.cashSubmited = true;
//                view.setEnabled(false);
                PopView_PaySummary_st.this.submitOrder(OrderPayType.CASH);
                PopView_PaySummary_st.this.checkMoney(true);
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            PopView_PaySummary_st.this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.9.1


                @Override // java.lang.Runnable
                public void run() {
                    if (PopView_PaySummary_st.this.cashSubmited) {
                        return;
                    }
                    PopView_PaySummary_st.this.cashSubmited = true;
//                    view.setEnabled(false);
                    PopView_PaySummary_st.this.submitOrder(OrderPayType.CASH);
                    PopView_PaySummary_st.this.checkMoney(true);
                }
            }, 1000L);
        }
    }

    void payWithCard() {
        if (Shj.getMachineId().equals("1707600007") || checkUpdateDiscount()) {
            resetTimeCount();
            Context context = getParent().getContext();
            this.qrPayViewId = -1;
            AlertDialog alertDialog = this.dialog_card;
            if (alertDialog == null) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.st_popview_cashcard, (ViewGroup) null);
                ((ImageView) inflate.findViewById(R.id.image)).setImageResource(R.drawable.img_paycard2);
                this.timecard = (TextView) inflate.findViewById(R.id.time);
                ((TextView) inflate.findViewById(R.id.title)).setText(R.string.lab_please2swipecard);
                UsbReaderHelper.attachUseReadEditText((EditText) inflate.findViewById(R.id.cardNo));
                inflate.findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.10

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        ShjManager.getOrderManager().cancelOrder();
                        UsbReaderHelper.tryReadUsbICCardInfo(false);
                        PopView_PaySummary_st.this.dialog_card.cancel();
                    }
                });
                inflate.setOnTouchListener(new View.OnTouchListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.11


                    @Override // android.view.View.OnTouchListener
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        UsbReaderHelper.tryReadUsbICCardInfo(true);
                        return false;
                    }
                });
                AlertDialog create = new AlertDialog.Builder(context).create();
                this.dialog_card = create;
                create.show();
                Window window = this.dialog_card.getWindow();
                window.setBackgroundDrawable(null);
                window.setContentView(inflate);
            } else {
                alertDialog.show();
            }
            resetTimer();
//            MdbReader_BDT.setEnabled(true);
            UsbReaderHelper.tryReadUsbICCardInfo(true);
            this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.12


                @Override // java.lang.Runnable
                public void run() {
                    PopView_PaySummary_st.this.submitOrder(OrderPayType.ICCard);
                }
            }, 1000L);
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 implements View.OnClickListener {


        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ShjManager.getOrderManager().cancelOrder();
            UsbReaderHelper.tryReadUsbICCardInfo(false);
            PopView_PaySummary_st.this.dialog_card.cancel();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$11 */
    /* loaded from: classes2.dex */
    public class AnonymousClass11 implements View.OnTouchListener {

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            UsbReaderHelper.tryReadUsbICCardInfo(true);
            return false;
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$12 */
    /* loaded from: classes2.dex */
    public class AnonymousClass12 implements Runnable {
        AnonymousClass12() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_PaySummary_st.this.submitOrder(OrderPayType.ICCard);
        }
    }

    void payWithScan() {
        if (Shj.getMachineId().equals("1707600007") || checkUpdateDiscount()) {
            resetTimeCount();
            Context context = getParent().getContext();
            this.qrPayViewId = -1;
            AlertDialog alertDialog = this.dialog_scan;
            if (alertDialog == null) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.st_popview_cashcard, (ViewGroup) null);
                ((ImageView) inflate.findViewById(R.id.image)).setImageResource(R.drawable.img_payscan2);
                this.timecard = (TextView) inflate.findViewById(R.id.time);
                ((TextView) inflate.findViewById(R.id.title)).setText("");
                ((TextView) inflate.findViewById(R.id.notice)).setText(context.getString(R.string.st_scan_in_30_seconds));
                UsbReaderHelper.attachUseReadEditText((EditText) inflate.findViewById(R.id.cardNo));
                inflate.findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.13


                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        ShjManager.getOrderManager().cancelOrder();
                        UsbReaderHelper.tryReadUsbICCardInfo(false);
                        PopView_PaySummary_st.this.dialog_scan.cancel();
                    }
                });
                inflate.setOnTouchListener(new View.OnTouchListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.14


                    @Override // android.view.View.OnTouchListener
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        UsbReaderHelper.tryReadUsbICCardInfo(true);
                        return false;
                    }
                });
                AlertDialog create = new AlertDialog.Builder(context).create();
                this.dialog_scan = create;
                create.show();
                Window window = this.dialog_scan.getWindow();
                window.setBackgroundDrawable(null);
                window.setContentView(inflate);
            } else {
                alertDialog.show();
            }
            resetTimer();
//            MdbReader_BDT.setEnabled(false);
            UsbReaderHelper.tryReadUsbICCardInfo(true);
            this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.15


                @Override // java.lang.Runnable
                public void run() {
                    PopView_PaySummary_st.this.submitOrder(OrderPayType.ICCard);
                }
            }, 1000L);
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$13 */
    /* loaded from: classes2.dex */
    public class AnonymousClass13 implements View.OnClickListener {
        AnonymousClass13() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ShjManager.getOrderManager().cancelOrder();
            UsbReaderHelper.tryReadUsbICCardInfo(false);
            PopView_PaySummary_st.this.dialog_scan.cancel();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$14 */
    /* loaded from: classes2.dex */
    public class AnonymousClass14 implements View.OnTouchListener {
        AnonymousClass14() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            UsbReaderHelper.tryReadUsbICCardInfo(true);
            return false;
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$15 */
    /* loaded from: classes2.dex */
    public class AnonymousClass15 implements Runnable {
        AnonymousClass15() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_PaySummary_st.this.submitOrder(OrderPayType.ICCard);
        }
    }

    void update_zaadPrice() {
        float orderPrice = (VmdHelper.get().getBqlOrder().getOrderPrice() * 1.0f) / 100.0f;
        if (this.ust.isChecked()) {
            this.zaad_price.setText(String.format("%.2f", Float.valueOf(orderPrice)));
            this.zaad_unit.setText("USD");
        } else {
            this.zaad_price.setText(String.format("%.2f", Float.valueOf(ZaadPayHelper.usd2slsh(orderPrice))));
            this.zaad_unit.setText("SLSH");
        }
    }

    void payWithZaad() {
        if (Shj.getMachineId().equals("1707600007") || checkUpdateDiscount()) {
            resetTimeCount();
            Context context = getParent().getContext();
            this.qrPayViewId = -1;
            AlertDialog alertDialog = this.dialog_zaad;
            if (alertDialog == null) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.st_popview_zaad, (ViewGroup) null);
                this.timezaad = (TextView) inflate.findViewById(R.id.time);
                EditText editText = (EditText) inflate.findViewById(R.id.zaad_phone);
                this.zaad_phone = editText;
                editText.setText("4112653");
                TextView textView = (TextView) inflate.findViewById(R.id.notice);
                this.zaad_notice = textView;
                textView.setText("Please enter your phone number");
                this.ust = (RadioButton) inflate.findViewById(R.id.ust);
                this.zaad_price = (TextView) inflate.findViewById(R.id.price);
                this.zaad_unit = (TextView) inflate.findViewById(R.id.unit);
                ((RadioGroup) inflate.findViewById(R.id.raGroup)).check(this.ust.getId());
                update_zaadPrice();
                ((RadioGroup) inflate.findViewById(R.id.raGroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.16


                    @Override // android.widget.RadioGroup.OnCheckedChangeListener
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        PopView_PaySummary_st.this.update_zaadPrice();
                    }
                });
                this.zaad_phone.addTextChangedListener(new TextWatcher() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.17
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }



                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        try {
                            PopView_PaySummary_st.this.zaad_notice.setVisibility(PopView_PaySummary_st.this.zaad_phone.getText().toString().length() == 0 ? View.VISIBLE : View.INVISIBLE);
                        } catch (Exception unused) {
                        }
                    }
                });
                inflate.findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.18


                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        ShjManager.getOrderManager().cancelOrder();
                        PopView_PaySummary_st.this.dialog_zaad.cancel();
                    }
                });
                inflate.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.19


                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (PopView_PaySummary_st.this.isZaadPaying) {
                            Toast.makeText(view.getContext(), "Please wait pay result!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (PopView_PaySummary_st.this.zaad_phone.getText().toString().trim().length() == 0) {
                            PopView_PaySummary_st.this.zaad_notice.setVisibility(View.VISIBLE);
                            return;
                        }
                        PopView_PaySummary_st.this.isZaadPaying = true;
                        PopView_PaySummary_st.this.zaad_notice.setText("Please enter your phone number");
                        try {
                            String orderLid = VmdHelper.get().getBqlOrder().getOrderLid();
                            float orderPrice = (VmdHelper.get().getBqlOrder().getOrderPrice() * 1.0f) / 100.0f;
                            Object[] objArr = new Object[1];
                            if (!PopView_PaySummary_st.this.ust.isChecked()) {
                                orderPrice = ZaadPayHelper.usd2slsh(orderPrice);
                            }
                            objArr[0] = Float.valueOf(orderPrice);
                            String format = String.format("%.2f", objArr);
                            String format2 = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
                            String str = "25263" + PopView_PaySummary_st.this.zaad_phone.getText().toString().trim();
//                            AnonymousClass1 anonymousClass1 = new ZaadPayHelper.ResultListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.19.1
//
//                                @Override // com.xyshj.machine.app.ZaadPayHelper.ResultListener
////                                public void onResult(boolean z, String str2, String str3, String str4) {
////                                    try {
////                                        if (z) {
////                                            Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
////                                            resentOrder.setPayId(str + "_" + orderLid);
////                                            resentOrder.setPayTime(new Date());
////                                            ShjManager.getOrderManager().driverOfferLineOrder(PopView_PaySummary_st.this.curPayType, resentOrder.getUid(), str + "_" + orderLid);
////                                        } else {
////                                            if (str2.equals("PreAuthorize") && str4.equals(SpeechConstant.NET_TIMEOUT)) {
////                                                PopView_PaySummary_st.this.zaad_notice.setText("User preauthorize timeout!");
////
////                                            }
////                                            PopView_PaySummary_st.this.dialog_zaad.dismiss();
////                                            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) "ZaadPayResult").put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("info", (Object) ("[error]" + str2 + StringUtils.SPACE + str3 + StringUtils.SPACE + str4)).put("time_out", (Object) 3000).put("showTime", (Object) false));
////                                            return;
////                                        }
////                                    } catch (Exception unused) {
////                                    }
////                                    PopView_PaySummary_st.this.isZaadPaying = false;
////                                }
//                            };
                            if (PopView_PaySummary_st.this.ust.isChecked()) {
//                                ZaadPayHelper.PreAuthorize("1.0", orderLid2, format2, "WEB", "API_PREAUTHORIZE", "MWALLET_ACCOUNT", str2, orderLid2, orderLid2, format + "", "USD", "test amount", "ZAAD", "ECOMMERCE", anonymousClass1);
                                return;
                            }
//                            ZaadPayHelper.PreAuthorize("1.0", orderLid2, format2, "WEB", "API_PREAUTHORIZE", "MWALLET_ACCOUNT", str2, orderLid2, orderLid2, format + "", "SLSH", "test amount", "ZAAD", "ECOMMERCE", anonymousClass1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            PopView_PaySummary_st.this.isZaadPaying = false;
                        }
                    }

                    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$19$1 */
                    /* loaded from: classes2.dex */
                    class AnonymousClass1 implements ZaadPayHelper.ResultListener {
//                        final /* synthetic */ String val$id;
//                        final /* synthetic */ String val$phone;
//
//                        AnonymousClass1(String str2, String orderLid2) {
//                            str = str2;
//                            orderLid = orderLid2;
//                        }

                        @Override // com.xyshj.machine.app.ZaadPayHelper.ResultListener
                        public void onResult(boolean z, String str2, String str3, String str4) {
                            try {
                                if (z) {
                                    Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
//                                    resentOrder.setPayId(str + "_" + orderLid);
                                    resentOrder.setPayTime(new Date());
//                                    ShjManager.getOrderManager().driverOfferLineOrder(PopView_PaySummary_st.this.curPayType, resentOrder.getUid(), str + "_" + orderLid);
                                } else {
//                                    if (str2.equals("PreAuthorize") && str4.equals(SpeechConstant.NET_TIMEOUT)) {
//                                        PopView_PaySummary_st.this.zaad_notice.setText("User preauthorize timeout!");
//                                        return;
//                                    }
                                    PopView_PaySummary_st.this.dialog_zaad.dismiss();
                                    PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) "ZaadPayResult").put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("info", (Object) ("[error]" + str2 + StringUtils.SPACE + str3 + StringUtils.SPACE + str4)).put("time_out", (Object) 3000).put("showTime", (Object) false));
                                }
                            } catch (Exception unused) {
                            }
                            PopView_PaySummary_st.this.isZaadPaying = false;
                        }
                    }
                });
                inflate.setOnTouchListener(new View.OnTouchListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.20
                    @Override // android.view.View.OnTouchListener
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return false;
                    }


                });
                AlertDialog create = new AlertDialog.Builder(context).create();
                this.dialog_zaad = create;
                create.show();
                this.zaad_notice.setVisibility(View.INVISIBLE);
                Window window = this.dialog_zaad.getWindow();
                window.setBackgroundDrawable(null);
                window.setContentView(inflate);
                window.clearFlags(131072);
            } else {
                alertDialog.show();
            }
            resetTimer();
//            MdbReader_BDT.setEnabled(false);
            UsbReaderHelper.tryReadUsbICCardInfo(false);
            this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.21


                @Override // java.lang.Runnable
                public void run() {
                    PopView_PaySummary_st.this.submitOrder(OrderPayType.ICCard);
                }
            }, 1000L);
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$16 */
    /* loaded from: classes2.dex */
    public class AnonymousClass16 implements RadioGroup.OnCheckedChangeListener {
        AnonymousClass16() {
        }

        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            PopView_PaySummary_st.this.update_zaadPrice();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$17 */
    /* loaded from: classes2.dex */
    public class AnonymousClass17 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass17() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            try {
                PopView_PaySummary_st.this.zaad_notice.setVisibility(PopView_PaySummary_st.this.zaad_phone.getText().toString().length() == 0 ? View.VISIBLE : View.INVISIBLE);
            } catch (Exception unused) {
            }
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$18 */
    /* loaded from: classes2.dex */
    public class AnonymousClass18 implements View.OnClickListener {
        AnonymousClass18() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ShjManager.getOrderManager().cancelOrder();
            PopView_PaySummary_st.this.dialog_zaad.cancel();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$19 */
    /* loaded from: classes2.dex */
    public class AnonymousClass19 implements View.OnClickListener {
        AnonymousClass19() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (PopView_PaySummary_st.this.isZaadPaying) {
                Toast.makeText(view.getContext(), "Please wait pay result!", Toast.LENGTH_LONG).show();
                return;
            }
            if (PopView_PaySummary_st.this.zaad_phone.getText().toString().trim().length() == 0) {
                PopView_PaySummary_st.this.zaad_notice.setVisibility(View.VISIBLE);
                return;
            }
            PopView_PaySummary_st.this.isZaadPaying = true;
            PopView_PaySummary_st.this.zaad_notice.setText("Please enter your phone number");
            try {
                String orderLid2 = VmdHelper.get().getBqlOrder().getOrderLid();
                float orderPrice = (VmdHelper.get().getBqlOrder().getOrderPrice() * 1.0f) / 100.0f;
                Object[] objArr = new Object[1];
                if (!PopView_PaySummary_st.this.ust.isChecked()) {
                    orderPrice = ZaadPayHelper.usd2slsh(orderPrice);
                }
                objArr[0] = Float.valueOf(orderPrice);
                String format = String.format("%.2f", objArr);
                String format2 = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
                String str2 = "25263" + PopView_PaySummary_st.this.zaad_phone.getText().toString().trim();
//                AnonymousClass1 anonymousClass1 = new ZaadPayHelper.ResultListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.19.1
////                    final /* synthetic */ String val$id;
////                    final /* synthetic */ String val$phone;
////
////                    AnonymousClass1(String str22, String orderLid22) {
////                        str = str22;
////                        orderLid = orderLid22;
////                    }
//
//                    @Override // com.xyshj.machine.app.ZaadPayHelper.ResultListener
//                    public void onResult(boolean z, String str22, String str3, String str4) {
//                        try {
//                            if (z) {
//                                Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
////                                resentOrder.setPayId(str + "_" + orderLid);
//                                resentOrder.setPayTime(new Date());
//                                ShjManager.getOrderManager().driverOfferLineOrder(PopView_PaySummary_st.this.curPayType, resentOrder.getUid(), str + "_" + orderLid);
//                            } else {
////                                if (str22.equals("PreAuthorize") && str4.equals(SpeechConstant.NET_TIMEOUT)) {
////                                    PopView_PaySummary_st.this.zaad_notice.setText("User preauthorize timeout!");
////                                    return;
////                                }
//                                PopView_PaySummary_st.this.dialog_zaad.dismiss();
//                                PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) "ZaadPayResult").put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("info", (Object) ("[error]" + str22 + StringUtils.SPACE + str3 + StringUtils.SPACE + str4)).put("time_out", (Object) 3000).put("showTime", (Object) false));
//                            }
//                        } catch (Exception unused) {
//                        }
//                        PopView_PaySummary_st.this.isZaadPaying = false;
//                    }
//                };
                if (PopView_PaySummary_st.this.ust.isChecked()) {
//                    ZaadPayHelper.PreAuthorize("1.0", orderLid22, format2, "WEB", "API_PREAUTHORIZE", "MWALLET_ACCOUNT", str22, orderLid22, orderLid22, format + "", "USD", "test amount", "ZAAD", "ECOMMERCE", anonymousClass1);
                    return;
                }
//                ZaadPayHelper.PreAuthorize("1.0", orderLid22, format2, "WEB", "API_PREAUTHORIZE", "MWALLET_ACCOUNT", str22, orderLid22, orderLid22, format + "", "SLSH", "test amount", "ZAAD", "ECOMMERCE", anonymousClass1);
            } catch (Exception e) {
                e.printStackTrace();
                PopView_PaySummary_st.this.isZaadPaying = false;
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$19$1 */
        /* loaded from: classes2.dex */

    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$20 */
    /* loaded from: classes2.dex */
    public class AnonymousClass20 implements View.OnTouchListener {
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }

        AnonymousClass20() {
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$21 */
    /* loaded from: classes2.dex */
    public class AnonymousClass21 implements Runnable {
        AnonymousClass21() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_PaySummary_st.this.submitOrder(OrderPayType.ICCard);
        }
    }

    void payWithQrcode() {
        if (Shj.getMachineId().equals("1707600007") || checkUpdateDiscount()) {
            resetTimeCount();
            Context context = getParent().getContext();
            this.qrPayViewId = -1;
            this.qrSubmited = false;
            AlertDialog alertDialog = this.dialog_qrcode;
            if (alertDialog == null) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.st_popview_qrcode, (ViewGroup) null);
                this.qrImage = (ImageView) inflate.findViewById(R.id.qrcode);
                this.timeqrcode = (TextView) inflate.findViewById(R.id.time);
                this.qrcodelabel = (TextView) inflate.findViewById(R.id.qrcodelabel);
                this.wx = (RadioButton) inflate.findViewById(R.id.pay_wx);
                this.zfb = (RadioButton) inflate.findViewById(R.id.pay_zfb);
                this.jd = (RadioButton) inflate.findViewById(R.id.pay_jd);
                this.wx.setVisibility(ShjManager.getOrderPayTypes().contains(OrderPayType.WEIXIN) ? View.VISIBLE : View.GONE);
                this.zfb.setVisibility(ShjManager.getOrderPayTypes().contains(OrderPayType.ZFB) ? View.VISIBLE : View.GONE);
                this.jd.setVisibility(ShjManager.getOrderPayTypes().contains(OrderPayType.YLJH) ? View.VISIBLE : View.GONE);
                inflate.findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.22


                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        ShjManager.getOrderManager().cancelOrder();
                        PopView_PaySummary_st.this.dialog_qrcode.cancel();
                    }
                });
                ((RadioGroup) inflate.findViewById(R.id.payTypes)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.23


                    @Override // android.widget.RadioGroup.OnCheckedChangeListener
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (i == PopView_PaySummary_st.this.qrPayViewId) {
                            return;
                        }
                        PopView_PaySummary_st.this.qrPayViewId = i;
                        PopView_PaySummary_st.this.qrImage.setImageResource(R.drawable.img_bg_gray);
                        PopView_PaySummary_st.this.qrcodelabel.setText(ShjAppHelper.getString(R.string.creatingqrcode));
                        OrderPayType orderPayType = OrderPayType.CASH;
                        long j = i;
                        if (j != PopView_PaySummary_st.this.lastQrPayType) {
                            PopView_PaySummary_st.this.lastQrPayType = j;
                            PopView_PaySummary_st.this.qrSubmited = false;
                        }
//                        switch (i) {
//                            case R.id.pay_jd /* 2131231175 */:
//                                orderPayType = OrderPayType.YLJH;
//                                break;
//                            case R.id.pay_wx /* 2131231179 */:
//                                orderPayType = OrderPayType.WEIXIN;
//                                break;
//                            case R.id.pay_zfb /* 2131231180 */:
//                                orderPayType = OrderPayType.ZFB;
//                                break;
//                        }
                        if (PopView_PaySummary_st.this.qrSubmited) {
                            return;
                        }
                        PopView_PaySummary_st.this.qrSubmited = true;
                        PopView_PaySummary_st.this.submitOrder(orderPayType);
                    }
                });
                AlertDialog create = new AlertDialog.Builder(context).create();
                this.dialog_qrcode = create;
                create.show();
                Window window = this.dialog_qrcode.getWindow();
                window.setBackgroundDrawable(null);
                window.setContentView(inflate);
            } else {
                alertDialog.show();
            }
            if (!this.qrSubmited) {
                this.qrSubmited = true;
                if (this.wx.getVisibility() == View.VISIBLE) {
                    this.wx.setChecked(true);
                    this.qrPayViewId = R.id.pay_wx;
                    submitOrder(OrderPayType.WEIXIN);
                } else if (this.zfb.getVisibility() == View.VISIBLE) {
                    this.zfb.setChecked(true);
                    this.qrPayViewId = R.id.pay_zfb;
                    submitOrder(OrderPayType.ZFB);
                } else if (this.jd.getVisibility() == View.VISIBLE) {
                    this.jd.setChecked(true);
                    this.qrPayViewId = R.id.pay_jd;
                    submitOrder(OrderPayType.YLJH);
                }
            }
            resetTimer();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$22 */
    /* loaded from: classes2.dex */
    public class AnonymousClass22 implements View.OnClickListener {
        AnonymousClass22() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ShjManager.getOrderManager().cancelOrder();
            PopView_PaySummary_st.this.dialog_qrcode.cancel();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$23 */
    /* loaded from: classes2.dex */
    public class AnonymousClass23 implements RadioGroup.OnCheckedChangeListener {
        AnonymousClass23() {
        }

        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (i == PopView_PaySummary_st.this.qrPayViewId) {
                return;
            }
            PopView_PaySummary_st.this.qrPayViewId = i;
            PopView_PaySummary_st.this.qrImage.setImageResource(R.drawable.img_bg_gray);
            PopView_PaySummary_st.this.qrcodelabel.setText(ShjAppHelper.getString(R.string.creatingqrcode));
            OrderPayType orderPayType = OrderPayType.CASH;
            long j = i;
            if (j != PopView_PaySummary_st.this.lastQrPayType) {
                PopView_PaySummary_st.this.lastQrPayType = j;
                PopView_PaySummary_st.this.qrSubmited = false;
            }
//            switch (i) {
//                case R.id.pay_jd /* 2131231175 */:
//                    orderPayType = OrderPayType.YLJH;
//                    break;
//                case R.id.pay_wx /* 2131231179 */:
//                    orderPayType = OrderPayType.WEIXIN;
//                    break;
//                case R.id.pay_zfb /* 2131231180 */:
//                    orderPayType = OrderPayType.ZFB;
//                    break;
//            }
            if (PopView_PaySummary_st.this.qrSubmited) {
                return;
            }
            PopView_PaySummary_st.this.qrSubmited = true;
            PopView_PaySummary_st.this.submitOrder(orderPayType);
        }
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
            int i = AnonymousClass26.$SwitchMap$com$shj$biz$order$OrderPayType[orderPayType.ordinal()];
            this.qrImage.setVisibility(View.VISIBLE);
            Loger.writeLog("SALES", "set qrImage:" + ShjAppBase.sysModel.getBitmap(orderPayType));
            this.qrImage.setImageBitmap(ShjAppBase.sysModel.getBitmap(orderPayType));
            this.qrcodelabel.setText(ShjAppHelper.getString(R.string.orderid) + ": " + resentOrder.getPayId());
        } catch (Exception e) {
            Loger.writeException("SALES", e);
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$26 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass26 {
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
//        switch (view.getId()) {
//            case R.id.back /* 2131230762 */:
//                close();
//                break;
//            case R.id.count1 /* 2131230948 */:
//                resetTimeCount();
//                checkCount(this.count1, false);
//                break;
//            case R.id.count2 /* 2131230949 */:
//                resetTimeCount();
//                checkCount(this.count2, false);
//                break;
//            case R.id.count3 /* 2131230950 */:
//                resetTimeCount();
//                checkCount(this.count3, false);
//                break;
//            case R.id.enterpromo /* 2131230974 */:
//                resetTimeCount();
//                try {
//                    VmdHelper.BqlOrder bqlOrder = VmdHelper.get().getBqlOrder();
//                    String obj = bqlOrder.getBqlItems().keySet().toArray()[0].toString();
//                    VmdHelper.BqlItem bqlItem = VmdHelper.get().getBqlItem(obj);
//                    JSONObject jSONObject = new JSONObject();
//                    jSONObject.put(ShjDbHelper.COLUM_price, bqlOrder.getOrderPrice() * 100);
//                    jSONObject.put("goodsName", obj);
//                    jSONObject.put("goodsCode", bqlItem.getCode());
//                    jSONObject.put("goodsPrice", bqlItem.getPrice());
//                    jSONObject.put("goodsCount", this.selectCount);
//                    BFPopView.showPopView("MainActivity", PopView_EnterCode.class, jSONObject.toString());
//                    break;
//                } catch (Exception unused) {
//                    break;
//                }
//            case R.id.gopay /* 2131231010 */:
//                resetTimeCount();
//                BFPopView.showPopView("MainActivity", PopView_Sel_PayTypes_st.class, (Serializable) null);
//                break;
//            case R.id.pay_card /* 2131231172 */:
//                resetTimeCount();
//                payWithCard();
//                break;
//            case R.id.pay_cash /* 2131231173 */:
//                resetTimeCount();
//                payWithCash();
//                break;
//            case R.id.pay_qrcode /* 2131231177 */:
//                resetTimeCount();
//                Object data = ShjManager.getData("ZAADPAY");
//                if (data != null && data.toString().equals("TRUE")) {
//                    payWithZaad();
//                    break;
//                } else {
//                    payWithQrcode();
//                    break;
//                }
//                break;
//            case R.id.pay_scan /* 2131231178 */:
//                resetTimeCount();
//                payWithScan();
//                break;
//            case R.id.totalprice /* 2131231301 */:
//                if (VmdHelper.get().getBqlOrder().getOrderPrice() <= 0 && System.currentTimeMillis() - this.last0PayTime > 10000) {
//                    this.last0PayTime = System.currentTimeMillis();
//                    submitOrder(OrderPayType.CASH);
//                    checkMoney(true);
//                    break;
//                }
//                break;
//        }
        if (view.getId() == R.id.back) {
            close();
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidClose() {
        super.onViewDidClose();
        RequestItem requestItem = this.codePayRequestItem;
        if (requestItem != null) {
            requestItem.setCanceled(true);
            this.codePayRequestItem = null;
        }
        AlertDialog alertDialog = this.dialog_card;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.dialog_card.dismiss();
        }
        AlertDialog alertDialog2 = this.dialog_cash;
        if (alertDialog2 != null && alertDialog2.isShowing()) {
            this.dialog_cash.dismiss();
        }
        AlertDialog alertDialog3 = this.dialog_qrcode;
        if (alertDialog3 != null && alertDialog3.isShowing()) {
            this.dialog_qrcode.dismiss();
        }
        AlertDialog alertDialog4 = this.dialog_zaad;
        if (alertDialog4 != null && alertDialog4.isShowing()) {
            this.dialog_zaad.dismiss();
        }
//        MdbReader_BDT.setEnabled(false);
//        MdbReader_BDT.get().cancel();
        ShjAppBase.sysApp.sendBroadcast(new Intent("Action_clear_selections"));
        Loger.writeLog("Broadcast;SHJ", "Action_clear_selections");
        UsbReaderHelper.tryReadUsbICCardInfo(false);
        this.dialog_card = null;
        this.dialog_cash = null;
        this.dialog_qrcode = null;
        this.dialog_zaad = null;
    }

    void payWithCode_xjpgame(String str) {
        try {
            Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("jqbh", Shj.getMachineId());
            jSONObject.put("point", resentOrder.getPrice());
            jSONObject.put("code", str);
            jSONObject.put("orderdata", resentOrder.getUid());
            RequestItem requestItem = new RequestItem("https://samplequeen.sg/api/scannewgiftcode.php", jSONObject, "POST");
            this.codePayRequestItem = requestItem;
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.24
//                final /* synthetic */ String val$payCode;
//
//                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
//                public void onRequestFinished(RequestItem requestItem2, boolean z) {
//                }
//
//                AnonymousClass24(String str2) {
//                    str = str2;
//                }

                void showInfo(boolean z) {
                    PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("payCode:" + str)).put("info", (Object) ("" + ShjAppHelper.getString(R.string.pay_fail))).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("small_left_image", (Object) Integer.valueOf(R.drawable.ico_error)).put("time_out", (Object) 5000).put("closeOnClick", (Object) true).put("showTime", (Object) false));
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
                    showInfo(false);
                }

                @Override
                public void onRequestFinished(RequestItem requestItem, boolean z) {

                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
                    try {
                        JSONObject jSONObject2 = new JSONObject(str2);
                        if (!jSONObject2.isNull("ResponseCode") && jSONObject2.getString("ResponseCode").equals("1")) {
                            Order resentOrder2 = ShjManager.getOrderManager().getResentOrder(1, null);
                            resentOrder2.setPayId(str);
                            resentOrder2.setPayTime(new Date());
                            ShjManager.getOrderManager().driverOfferLineOrder(PopView_PaySummary_st.this.curPayType, resentOrder2.getUid(), str);
                        } else {
                            showInfo(false);
                        }
                    } catch (Exception unused) {
                    }
                    return false;
                }
            });
            RequestHelper.request(this.codePayRequestItem);
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$24 */
    /* loaded from: classes2.dex */
    public class AnonymousClass24 implements RequestItem.OnRequestResultListener {


        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }



        void showInfo(boolean z) {
//            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("payCode:" + str)).put("info", (Object) ("" + ShjAppHelper.getString(R.string.pay_fail))).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("small_left_image", (Object) Integer.valueOf(R.drawable.ico_error)).put("time_out", (Object) 5000).put("closeOnClick", (Object) true).put("showTime", (Object) false));
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
            showInfo(false);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
            try {
                JSONObject jSONObject2 = new JSONObject(str2);
                if (!jSONObject2.isNull("ResponseCode") && jSONObject2.getString("ResponseCode").equals("1")) {
                    Order resentOrder2 = ShjManager.getOrderManager().getResentOrder(1, null);
//                    resentOrder2.setPayId(str);
                    resentOrder2.setPayTime(new Date());
//                    ShjManager.getOrderManager().driverOfferLineOrder(PopView_PaySummary_st.this.curPayType, resentOrder2.getUid(), str);
                } else {
                    showInfo(false);
                }
            } catch (Exception unused) {
            }
            return false;
        }
    }

    public void payWithCode(String str) {
        Loger.writeLog("SHJ", "payWithCode:" + str);
        Object data = ShjManager.getData("payWithCodeType");
        if (data != null && data.toString().equals("xjpgame")) {
            Loger.writeLog("SHJ", "pay xjpgame");
            payWithCode_xjpgame(str);
            return;
        }
        try {
            Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("jqbh", Shj.getMachineId());
            jSONObject.put("amount", resentOrder.getPrice());
            jSONObject.put("paycode", str);
            jSONObject.put(YGDBHelper.COLUM_ORDERID, resentOrder.getUid());
            RequestItem requestItem = new RequestItem(NetAddress.payWithCodeUrl(), jSONObject, "POST");
            this.codePayRequestItem = requestItem;
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.popview.PopView_PaySummary_st.25
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }


                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
                    try {
                        JSONObject jSONObject2 = new JSONObject(str2);
                        if (jSONObject2.getString("code").equals("H0000")) {
//                            JSONObject jSONObject3 = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
                            Order resentOrder2 = ShjManager.getOrderManager().getResentOrder(1, null);
//                            String string = jSONObject3.getString("payid");
//                            if (string == null) {
//                                return false;
//                            }
//                            resentOrder2.setPayId(string);
//                            resentOrder2.setPayTime(DateUtil.parse(jSONObject3.getString("paytime"), "yyyy-MM-dd HH:mm:ss"));
//                            ShjManager.getOrderManager().driverOfferLineOrder(PopView_PaySummary_st.this.curPayType, resentOrder2.getUid(), string);
                            return true;
                        }
                    } catch (Exception unused) {
                    }
                    return false;
                }
            });
            RequestHelper.request(this.codePayRequestItem);
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_PaySummary_st$25 */
    /* loaded from: classes2.dex */
    public class AnonymousClass25 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass25() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
            try {
                JSONObject jSONObject2 = new JSONObject(str2);
                if (jSONObject2.getString("code").equals("H0000")) {
////                    JSONObject jSONObject3 = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
//                    Order resentOrder2 = ShjManager.getOrderManager().getResentOrder(1, null);
//                    String string = jSONObject3.getString("payid");
//                    if (string == null) {
//                        return false;
//                    }
//                    resentOrder2.setPayId(string);
//                    resentOrder2.setPayTime(DateUtil.parse(jSONObject3.getString("paytime"), "yyyy-MM-dd HH:mm:ss"));
//                    ShjManager.getOrderManager().driverOfferLineOrder(PopView_PaySummary_st.this.curPayType, resentOrder2.getUid(), string);
                    return true;
                }
            } catch (Exception unused) {
            }
            return false;
        }
    }
}
