package com.xyshj.machine.popview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.image.ImageUtils;
import com.oysb.utils.io.file.SDFileUtils;
import com.oysb.utils.view.BFPopView;
import com.oysb.utils.view.BasePopView;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.squareup.picasso.Picasso;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.database.setting.SettingType;
import com.xyshj.machine.R;
import com.xyshj.machine.app.SysApp;
import com.xyshj.machine.app.VmdHelper;
import com.xyshj.machine.listener.MyGoodsStatusListener;
import com.xyshj.machine.listener.MyShjStatusListener;
import com.xyshj.machine.model.SysModel;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/* loaded from: classes2.dex */
public class PopView_FreeCollocation_st extends BasePopView implements View.OnClickListener {
    public static final String Action_clear_selections = "Action_clear_selections";
    public static final String Action_refresh_view = "Action_refresh_view";
    GridView gridView1;
    MyGridViewAdapter gridViewAdapter1;
    TextView ywbqlHdh;
    String ywbqlName;
    RelativeLayout ywbql = null;
    HashMap<String, Integer> tops = new HashMap<>();
    String lang = "";
    String langEx = "";

    @Override // com.oysb.utils.view.BFPopView
    protected void onMessage(Message message) {
    }

    /* loaded from: classes2.dex */
    public class MyGridViewAdapter extends BaseAdapter {
        boolean showConner = true;
        ArrayList<VmdHelper.TopItem> datas = new ArrayList<>();

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        MyGridViewAdapter() {
        }

        public ArrayList<VmdHelper.TopItem> getDatas() {
            return this.datas;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.datas.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return this.datas.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(PopView_FreeCollocation_st.this.gridView1.getContext()).inflate(R.layout.st_free_collocation_item_layout, (ViewGroup) null, false);
                viewHolder = new ViewHolder();
                viewHolder.goods = (ImageView) view.findViewById(R.id.img_goods);
                viewHolder.added = (ImageView) view.findViewById(R.id.img_added);
                viewHolder.saleout = (ImageView) view.findViewById(R.id.img_saleout);
                if (PopView_FreeCollocation_st.this.lang.equalsIgnoreCase("th")) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.saleout.getLayoutParams();
                    layoutParams.width = PopView_FreeCollocation_st.this.gridView1.getContext().getResources().getDimensionPixelSize(R.dimen.px320);
                    layoutParams.height = PopView_FreeCollocation_st.this.gridView1.getContext().getResources().getDimensionPixelSize(R.dimen.px330);
                    layoutParams.rightMargin = 0;
                    viewHolder.saleout.setPadding(0, 0, 0, 0);
                    layoutParams.removeRule(11);
                    layoutParams.addRule(14);
                    layoutParams.topMargin = PopView_FreeCollocation_st.this.gridView1.getContext().getResources().getDimensionPixelSize(R.dimen.px10);
                    viewHolder.saleout.setLayoutParams(layoutParams);
                }
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                viewHolder.price = (TextView) view.findViewById(R.id.price);
                viewHolder.hdh = (TextView) view.findViewById(R.id.hdh);
                view.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_st.MyGridViewAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                    }

                    AnonymousClass1() {
                    }
                });
                viewHolder.goods.setTag(viewHolder);
                viewHolder.goods.setClickable(true);
                viewHolder.goods.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_st.MyGridViewAdapter.2
                    AnonymousClass2() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                        String name = viewHolder2.topItem.getName();
                        if (ShjManager.getGoodsManager().getGoodsCount(viewHolder2.topItem.getCode()) <= 0) {
                            return;
                        }
                        if ((PopView_FreeCollocation_st.this.tops.containsKey(name) ? PopView_FreeCollocation_st.this.tops.get(name).intValue() : 0) == 1) {
                            PopView_FreeCollocation_st.this.tops.remove(name);
                        } else {
                            PopView_FreeCollocation_st.this.tops.put(name, 1);
                        }
                        viewHolder2.updateView();
                        PopView_FreeCollocation_st.this.updateTotalMoney();
                    }
                });
                viewHolder.added.setTag(viewHolder);
                viewHolder.added.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_st.MyGridViewAdapter.3
                    AnonymousClass3() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                        String name = viewHolder2.topItem.getName();
                        if (ShjManager.getGoodsManager().getGoodsCount(viewHolder2.topItem.getCode()) <= 0) {
                            return;
                        }
                        if ((PopView_FreeCollocation_st.this.tops.containsKey(name) ? PopView_FreeCollocation_st.this.tops.get(name).intValue() : 0) == 1) {
                            PopView_FreeCollocation_st.this.tops.remove(name);
                        } else {
                            PopView_FreeCollocation_st.this.tops.put(name, 1);
                        }
                        viewHolder2.updateView();
                        PopView_FreeCollocation_st.this.updateTotalMoney();
                    }
                });
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            try {
                viewHolder.topItem = (VmdHelper.TopItem) getItem(i);
                viewHolder.idx = i;
                viewHolder.updateView();
            } catch (Exception e) {
                Loger.writeException("SHJ", e);
            }
            return view;
        }

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_st$MyGridViewAdapter$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements View.OnClickListener {
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
            }

            AnonymousClass1() {
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_st$MyGridViewAdapter$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements View.OnClickListener {
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                String name = viewHolder2.topItem.getName();
                if (ShjManager.getGoodsManager().getGoodsCount(viewHolder2.topItem.getCode()) <= 0) {
                    return;
                }
                if ((PopView_FreeCollocation_st.this.tops.containsKey(name) ? PopView_FreeCollocation_st.this.tops.get(name).intValue() : 0) == 1) {
                    PopView_FreeCollocation_st.this.tops.remove(name);
                } else {
                    PopView_FreeCollocation_st.this.tops.put(name, 1);
                }
                viewHolder2.updateView();
                PopView_FreeCollocation_st.this.updateTotalMoney();
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_st$MyGridViewAdapter$3 */
        /* loaded from: classes2.dex */
        class AnonymousClass3 implements View.OnClickListener {
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                String name = viewHolder2.topItem.getName();
                if (ShjManager.getGoodsManager().getGoodsCount(viewHolder2.topItem.getCode()) <= 0) {
                    return;
                }
                if ((PopView_FreeCollocation_st.this.tops.containsKey(name) ? PopView_FreeCollocation_st.this.tops.get(name).intValue() : 0) == 1) {
                    PopView_FreeCollocation_st.this.tops.remove(name);
                } else {
                    PopView_FreeCollocation_st.this.tops.put(name, 1);
                }
                viewHolder2.updateView();
                PopView_FreeCollocation_st.this.updateTotalMoney();
            }
        }

        /* loaded from: classes2.dex */
        class ViewHolder {
            ImageView added;
            ImageView conner;
            ImageView goods;
            TextView hdh;
            int idx = -1;
            TextView name;
            TextView price;
            ImageView saleout;
            VmdHelper.TopItem topItem;

            ViewHolder() {
            }

            void updateView() {
                String name = this.topItem.getName();
                String str = SDFileUtils.SDCardRoot + "xyshj/images/bql_" + name.toLowerCase(Locale.ROOT) + ".png";
                if (SDFileUtils.isFileExist(str)) {
                    this.goods.setImageBitmap(ImageUtils.getBitmap(str, 0, 0));
                } else {
                    this.goods.setImageBitmap(ShjManager.getGoodsManager().getGoodsImage(this.topItem.getCode(), false));
                }
                if (ShjManager.getGoodsManager().getGoodsCount(this.topItem.getCode()) <= 0) {
                    this.saleout.setVisibility(0);
                    this.added.setClickable(false);
                } else {
                    this.saleout.setVisibility(8);
                    this.added.setClickable(true);
                }
                this.name.setText(name);
                TextView textView = this.price;
                StringBuilder sb = new StringBuilder();
                sb.append(SysApp.getPriceUnit());
                double price = this.topItem.getPrice();
                Double.isNaN(price);
                sb.append(String.format("%.02f", Double.valueOf(price / 100.0d)));
                textView.setText(sb.toString());
                this.hdh.setText(String.format("%03d", Integer.valueOf(this.idx + 1)));
                if (PopView_FreeCollocation_st.this.tops.containsKey(this.topItem.getName()) && PopView_FreeCollocation_st.this.tops.get(this.topItem.getName()).intValue() > 0) {
                    this.added.setVisibility(0);
                    this.goods.setBackgroundResource(R.drawable.shape_corner_border2);
                } else {
                    this.added.setVisibility(8);
                    this.goods.setBackgroundResource(R.drawable.shape_corner_border);
                }
            }
        }
    }

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.st_popview_free_collocation, (ViewGroup) null);
        this.lang = CommonTool.getLanguage(inflate.getContext());
        this.langEx = CommonTool.getLanguageEx(inflate.getContext());
        inflate.findViewById(R.id.back).setOnClickListener(this);
        inflate.findViewById(R.id.next).setOnClickListener(this);
        inflate.findViewById(R.id.pay).setOnClickListener(this);
        File file = new File(SDFileUtils.SDCardRoot + "xyShj/resource/th_topbar.png");
        if (file.exists()) {
            inflate.findViewById(R.id.toplog).setVisibility(8);
            Picasso.get().load(file).into((ImageView) inflate.findViewById(R.id.toplogimg));
        } else {
            inflate.findViewById(R.id.toplogimg).setVisibility(8);
        }
        RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.ywbql);
        this.ywbql = relativeLayout;
        relativeLayout.findViewById(R.id.img_added).setVisibility(0);
        this.ywbqlHdh = (TextView) this.ywbql.findViewById(R.id.hdh);
        MyGridViewAdapter myGridViewAdapter = new MyGridViewAdapter();
        this.gridViewAdapter1 = myGridViewAdapter;
        myGridViewAdapter.showConner = false;
        GridView gridView = (GridView) inflate.findViewById(R.id.gridview1);
        this.gridView1 = gridView;
        gridView.setAdapter((ListAdapter) this.gridViewAdapter1);
        if (this.langEx.equalsIgnoreCase("en-US")) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.ywbqlHdh.getLayoutParams();
            layoutParams.leftMargin = this.ywbql.getResources().getDimensionPixelSize(R.dimen.px7);
            this.ywbqlHdh.setLayoutParams(layoutParams);
            View findViewById = this.ywbql.findViewById(R.id.name);
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) findViewById.getLayoutParams();
            layoutParams2.height = this.ywbql.getResources().getDimensionPixelSize(R.dimen.px135);
            findViewById.setLayoutParams(layoutParams2);
            int dimensionPixelSize = this.gridView1.getContext().getResources().getDimensionPixelSize(R.dimen.px65);
            LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.ywbql.getLayoutParams();
            layoutParams3.width = (dimensionPixelSize * SettingType.SHOW_BALANCE) / 65;
            layoutParams3.height = (dimensionPixelSize * 380) / 65;
            this.ywbql.setLayoutParams(layoutParams3);
            this.gridView1.setHorizontalSpacing(dimensionPixelSize);
            LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) this.gridView1.getLayoutParams();
            layoutParams4.setMargins(dimensionPixelSize, (dimensionPixelSize * 25) / 65, dimensionPixelSize, 0);
            layoutParams4.height = (dimensionPixelSize * 800) / 65;
            this.gridView1.setLayoutParams(layoutParams4);
        }
        if (this.lang.contains("es") || ((TextView) inflate.findViewById(R.id.tvSelTops)).getText().toString().length() > 32) {
            try {
                ((TextView) inflate.findViewById(R.id.tvSelTops)).setTextSize(inflate.getContext().getResources().getDimensionPixelSize(R.dimen.px1) * 30);
            } catch (Exception unused) {
            }
        }
        return inflate;
    }

    @Override // com.oysb.utils.view.BasePopView
    protected void registActions(List<String> list) {
        list.add(MyShjStatusListener.ACTION_SHJ_FREE_TIME);
        list.add(MyShjStatusListener.ACTION_STATUS_RESET_FINISHED);
        list.add("Action_clear_selections");
        list.add("Action_refresh_view");
        list.add(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_START);
        list.add(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_STATUS);
        list.add(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_BLOCKED);
        list.add(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_SUCCESS);
        list.add("ACTION_FULL_ADVIEW_CLOSED");
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onAction(String str, Bundle bundle) {
        if (isShowing()) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1540984004:
                    if (str.equals("ACTION_FULL_ADVIEW_CLOSED")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1342403763:
                    if (str.equals(MyShjStatusListener.ACTION_SHJ_FREE_TIME)) {
                        c = 1;
                        break;
                    }
                    break;
                case -775361326:
                    if (str.equals("Action_refresh_view")) {
                        c = 2;
                        break;
                    }
                    break;
                case -236900188:
                    if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_START)) {
                        c = 3;
                        break;
                    }
                    break;
                case 2203589:
                    if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_SUCCESS)) {
                        c = 4;
                        break;
                    }
                    break;
                case 285707362:
                    if (str.equals("Action_clear_selections")) {
                        c = 5;
                        break;
                    }
                    break;
                case 1176350758:
                    if (str.equals(MyShjStatusListener.ACTION_STATUS_RESET_FINISHED)) {
                        c = 6;
                        break;
                    }
                    break;
                case 1246030800:
                    if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_STATUS)) {
                        c = 7;
                        break;
                    }
                    break;
                case 1847935406:
                    if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_BLOCKED)) {
                        c = '\b';
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    this.tops.clear();
                    updateTotalMoney();
                    this.gridViewAdapter1.notifyDataSetChanged();
                    return;
                case 1:
                    if (BFPopView.getShowingPopViewCount() == 1) {
                        BFPopView.showPopView("MainActivity", "PopView_Wait", (SysApp.sysModel.getUiType() == SysModel.UI_TYPE_BQL_EN ? PopView_Wait_en.class : PopView_Wait_st.class).getName(), "", zindex);
                        return;
                    }
                    return;
                case 2:
                    this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_st.1
                        AnonymousClass1() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_FreeCollocation_st.this.onViewWillShow();
                        }
                    });
                    return;
                case 3:
                case 4:
                case 7:
                case '\b':
                    BFPopView.showPopView("MainActivity", "PopView_Wait", (SysApp.sysModel.getUiType() == SysModel.UI_TYPE_BQL_EN ? PopView_Wait_en.class : PopView_Wait_st.class).getName(), "", zindex + 1);
                    return;
                case 5:
                    this.tops.clear();
                    this.gridViewAdapter1.notifyDataSetChanged();
                    updateTotalMoney();
                    return;
                case 6:
                    this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_st.2
                        AnonymousClass2() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_FreeCollocation_st.this.reloadData();
                        }
                    }, 1000L);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_st$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_FreeCollocation_st.this.onViewWillShow();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_st$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_FreeCollocation_st.this.reloadData();
        }
    }

    void reloadData() {
        try {
            ArrayList<VmdHelper.TopItem> topDatas = VmdHelper.get().getTopDatas();
            for (int i = 0; i < topDatas.size(); i++) {
                this.gridViewAdapter1.datas.add(topDatas.get(i));
            }
            this.ywbqlName = Shj.getShelfInfo(101).getGoodsName();
            this.gridViewAdapter1.notifyDataSetChanged();
        } catch (Exception unused) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) UUID.randomUUID().toString()).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("[error]" + ShjAppHelper.getString(R.string.bqlseterror))).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("time_out", (Object) 3000).put("closeOnClick", (Object) true).put("showTime", (Object) false));
            this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_st.3
                AnonymousClass3() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    BFPopView.showPopView("MainActivity", "PopView_Wait", (SysApp.sysModel.getUiType() == SysModel.UI_TYPE_BQL_EN ? PopView_Wait_en.class : PopView_Wait_st.class).getName(), "", BFPopView.zindex);
                }
            }, 3000L);
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_st$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            BFPopView.showPopView("MainActivity", "PopView_Wait", (SysApp.sysModel.getUiType() == SysModel.UI_TYPE_BQL_EN ? PopView_Wait_en.class : PopView_Wait_st.class).getName(), "", BFPopView.zindex);
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewWillShow() {
        Loger.writeLog("SHJ", "freeCollocation will show");
        Shj.getShelves();
        this.ywbqlName = Shj.getShelfInfo(101).getGoodsName();
        this.ywbqlHdh.setText("101");
        TextView textView = (TextView) this.ywbql.findViewById(R.id.price);
        StringBuilder sb = new StringBuilder();
        sb.append(SysApp.getPriceUnit());
        sb.append(StringUtils.SPACE);
        double intValue = Shj.getShelfInfo(101).getPrice().intValue();
        Double.isNaN(intValue);
        sb.append(String.format("%.2f", Double.valueOf(intValue / 100.0d)));
        textView.setText(sb.toString());
        if (this.langEx.equalsIgnoreCase("en-US")) {
            ((TextView) this.ywbql.findViewById(R.id.name)).setText("");
            ((TextView) findViewById(R.id.ywbqlName)).setText(this.ywbqlName);
        } else {
            ((TextView) this.ywbql.findViewById(R.id.name)).setText(this.ywbqlName);
        }
        Bitmap goodsImage = ShjManager.getGoodsManager().getGoodsImage(Shj.getShelfInfo(101).getGoodsCode(), false);
        if (goodsImage != null) {
            ((ImageView) this.ywbql.findViewById(R.id.img_goods)).setImageBitmap(goodsImage);
        } else {
            ((ImageView) this.ywbql.findViewById(R.id.img_goods)).setImageResource(R.drawable.st_ywbql);
        }
        ((ImageView) this.ywbql.findViewById(R.id.img_goods)).setBackgroundResource(R.drawable.shape_corner_border2);
        this.tops.clear();
        super.onViewWillShow();
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidShow() {
        super.onViewDidShow();
        updateTotalMoney();
        this.gridViewAdapter1.notifyDataSetChanged();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            BFPopView.showPopView("MainActivity", "PopView_Wait", (SysApp.sysModel.getUiType() == SysModel.UI_TYPE_BQL_EN ? PopView_Wait_en.class : PopView_Wait_st.class).getName(), "", zindex);
        } else if ((id == R.id.next || id == R.id.pay) && VmdHelper.get().checkVmdConnected() && VmdHelper.get().checkBqlZhibeiCount(1)) {
            VmdHelper.get().doCheckBqlStatus(30000, new VmdHelper.CheckBqlStatusListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_st.4
                final /* synthetic */ String val$uid;

                AnonymousClass4(String str) {
                    uuid = str;
                }

                @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
                public void onStartWait2StatusOk(String str) {
                    PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) str).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("time_out", (Object) 30000).put("showTime", (Object) false));
                }

                /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_st$4$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements Runnable {
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        VmdHelper.get().createOrder(VmdHelper.BQL_TYPE_CUSTOMER);
                        VmdHelper.get().getBqlOrder().setBqlCount(PopView_FreeCollocation_st.this.ywbqlName, 1);
                        for (String str : PopView_FreeCollocation_st.this.tops.keySet()) {
                            if (PopView_FreeCollocation_st.this.tops.get(str).intValue() != 0) {
                                VmdHelper.get().getBqlOrder().setTopCount(str, 1);
                            }
                        }
                        Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                        BFPopView.showPopView("MainActivity", PopView_PaySummary_st.class, "");
                    }
                }

                @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
                public void onCheckBqlStatusResult(boolean z, String str) {
                    if (z) {
                        PopView_Info.closeInfo(uuid);
                        PopView_FreeCollocation_st.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_st.4.1
                            AnonymousClass1() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                VmdHelper.get().createOrder(VmdHelper.BQL_TYPE_CUSTOMER);
                                VmdHelper.get().getBqlOrder().setBqlCount(PopView_FreeCollocation_st.this.ywbqlName, 1);
                                for (String str2 : PopView_FreeCollocation_st.this.tops.keySet()) {
                                    if (PopView_FreeCollocation_st.this.tops.get(str2).intValue() != 0) {
                                        VmdHelper.get().getBqlOrder().setTopCount(str2, 1);
                                    }
                                }
                                Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                                BFPopView.showPopView("MainActivity", PopView_PaySummary_st.class, "");
                            }
                        });
                        return;
                    }
                    String mainServicePhone = ShjAppHelper.getMainServicePhone();
                    PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("[error]" + str)).put("time_out", (Object) 5000).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("showTime", (Object) true).put("notice", (Object) (ShjAppHelper.getString(R.string.phone) + ":" + mainServicePhone)));
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_st$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements VmdHelper.CheckBqlStatusListener {
        final /* synthetic */ String val$uid;

        AnonymousClass4(String str) {
            uuid = str;
        }

        @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
        public void onStartWait2StatusOk(String str) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) str).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("time_out", (Object) 30000).put("showTime", (Object) false));
        }

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_st$4$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                VmdHelper.get().createOrder(VmdHelper.BQL_TYPE_CUSTOMER);
                VmdHelper.get().getBqlOrder().setBqlCount(PopView_FreeCollocation_st.this.ywbqlName, 1);
                for (String str2 : PopView_FreeCollocation_st.this.tops.keySet()) {
                    if (PopView_FreeCollocation_st.this.tops.get(str2).intValue() != 0) {
                        VmdHelper.get().getBqlOrder().setTopCount(str2, 1);
                    }
                }
                Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                BFPopView.showPopView("MainActivity", PopView_PaySummary_st.class, "");
            }
        }

        @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
        public void onCheckBqlStatusResult(boolean z, String str) {
            if (z) {
                PopView_Info.closeInfo(uuid);
                PopView_FreeCollocation_st.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_st.4.1
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        VmdHelper.get().createOrder(VmdHelper.BQL_TYPE_CUSTOMER);
                        VmdHelper.get().getBqlOrder().setBqlCount(PopView_FreeCollocation_st.this.ywbqlName, 1);
                        for (String str2 : PopView_FreeCollocation_st.this.tops.keySet()) {
                            if (PopView_FreeCollocation_st.this.tops.get(str2).intValue() != 0) {
                                VmdHelper.get().getBqlOrder().setTopCount(str2, 1);
                            }
                        }
                        Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                        BFPopView.showPopView("MainActivity", PopView_PaySummary_st.class, "");
                    }
                });
                return;
            }
            String mainServicePhone = ShjAppHelper.getMainServicePhone();
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("[error]" + str)).put("time_out", (Object) 5000).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("showTime", (Object) true).put("notice", (Object) (ShjAppHelper.getString(R.string.phone) + ":" + mainServicePhone)));
        }
    }

    void updateTotalMoney() {
        try {
            int intValue = Shj.getShelfInfo(101).getPrice().intValue();
            for (String str : this.tops.keySet()) {
                if (this.tops.get(str).intValue() != 0) {
                    intValue += VmdHelper.get().getTopItem(str).getPrice();
                }
            }
            TextView textView = (TextView) findViewById(R.id.pay);
            StringBuilder sb = new StringBuilder();
            sb.append(SysApp.getPriceUnit());
            sb.append(StringUtils.SPACE);
            Object[] objArr = new Object[1];
            double d = intValue;
            Double.isNaN(d);
            objArr[0] = Double.valueOf(d / 100.0d);
            sb.append(String.format("%.02f", objArr));
            textView.setText(sb.toString());
        } catch (Exception unused) {
        }
    }
}
