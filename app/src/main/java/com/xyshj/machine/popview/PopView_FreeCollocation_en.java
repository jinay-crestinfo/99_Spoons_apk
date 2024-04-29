package com.xyshj.machine.popview;

import android.graphics.Typeface;
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
import android.widget.TextView;
import com.oysb.utils.Loger;
import com.oysb.utils.image.ImageUtils;
import com.oysb.utils.io.file.SDFileUtils;
import com.oysb.utils.view.BFPopView;
import com.oysb.utils.view.BasePopView;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.machine.R;
import com.xyshj.machine.app.SysApp;
import com.xyshj.machine.app.VmdHelper;
import com.xyshj.machine.listener.MyShjStatusListener;
import com.xyshj.machine.model.SysModel;
import com.xyshj.machine.popview.PopView_Info;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/* loaded from: classes2.dex */
public class PopView_FreeCollocation_en extends BasePopView implements View.OnClickListener {
    public static final String Action_clear_selections = "Action_clear_selections";
    public static final String Action_refresh_view = "Action_refresh_view";
    TextView back;
    GridView gridView1;
    GridView gridView2;
    MyGridViewAdapter gridViewAdapter1;
    MyGridViewAdapter gridViewAdapter2;
    HashMap<String, Integer> tops = new HashMap<>();
    String ywbqlName;

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
                view = LayoutInflater.from(PopView_FreeCollocation_en.this.gridView1.getContext()).inflate(R.layout.en_free_collocation_item_layout, (ViewGroup) null, false);
                viewHolder = new ViewHolder();
                viewHolder.goods = (ImageView) view.findViewById(R.id.img_goods);
                viewHolder.added = (ImageView) view.findViewById(R.id.img_added);
                viewHolder.conner = (ImageView) view.findViewById(R.id.img_conner);
                viewHolder.conner.setVisibility(this.showConner ? 0 : 4);
                viewHolder.saleout = (ImageView) view.findViewById(R.id.img_saleout);
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                viewHolder.price = (TextView) view.findViewById(R.id.price);
                view.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_en.MyGridViewAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                    }

                    AnonymousClass1() {
                    }
                });
                viewHolder.goods.setTag(viewHolder);
                viewHolder.goods.setClickable(true);
                viewHolder.goods.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_en.MyGridViewAdapter.2
                    AnonymousClass2() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                        String name = viewHolder2.topItem.getName();
                        if ((PopView_FreeCollocation_en.this.tops.containsKey(name) ? PopView_FreeCollocation_en.this.tops.get(name).intValue() : 0) == 1) {
                            PopView_FreeCollocation_en.this.tops.remove(name);
                        } else {
                            PopView_FreeCollocation_en.this.tops.put(name, 1);
                        }
                        viewHolder2.updateView();
                        PopView_FreeCollocation_en.this.updateTotalMoney();
                    }
                });
                viewHolder.added.setTag(viewHolder);
                viewHolder.added.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_en.MyGridViewAdapter.3
                    AnonymousClass3() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                        String name = viewHolder2.topItem.getName();
                        if ((PopView_FreeCollocation_en.this.tops.containsKey(name) ? PopView_FreeCollocation_en.this.tops.get(name).intValue() : 0) == 1) {
                            PopView_FreeCollocation_en.this.tops.remove(name);
                        } else {
                            PopView_FreeCollocation_en.this.tops.put(name, 1);
                        }
                        viewHolder2.updateView();
                        PopView_FreeCollocation_en.this.updateTotalMoney();
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

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_en$MyGridViewAdapter$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements View.OnClickListener {
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
            }

            AnonymousClass1() {
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_en$MyGridViewAdapter$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements View.OnClickListener {
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                String name = viewHolder2.topItem.getName();
                if ((PopView_FreeCollocation_en.this.tops.containsKey(name) ? PopView_FreeCollocation_en.this.tops.get(name).intValue() : 0) == 1) {
                    PopView_FreeCollocation_en.this.tops.remove(name);
                } else {
                    PopView_FreeCollocation_en.this.tops.put(name, 1);
                }
                viewHolder2.updateView();
                PopView_FreeCollocation_en.this.updateTotalMoney();
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_en$MyGridViewAdapter$3 */
        /* loaded from: classes2.dex */
        class AnonymousClass3 implements View.OnClickListener {
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                String name = viewHolder2.topItem.getName();
                if ((PopView_FreeCollocation_en.this.tops.containsKey(name) ? PopView_FreeCollocation_en.this.tops.get(name).intValue() : 0) == 1) {
                    PopView_FreeCollocation_en.this.tops.remove(name);
                } else {
                    PopView_FreeCollocation_en.this.tops.put(name, 1);
                }
                viewHolder2.updateView();
                PopView_FreeCollocation_en.this.updateTotalMoney();
            }
        }

        /* loaded from: classes2.dex */
        class ViewHolder {
            ImageView added;
            ImageView conner;
            ImageView goods;
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
                this.conner.setImageBitmap(ShjManager.getGoodsManager().getGoodsImage(this.topItem.getCode(), false));
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
                sb.append(price / 100.0d);
                textView.setText(sb.toString());
                if (PopView_FreeCollocation_en.this.tops.containsKey(this.topItem.getName()) && PopView_FreeCollocation_en.this.tops.get(this.topItem.getName()).intValue() > 0) {
                    this.added.setVisibility(0);
                } else {
                    this.added.setVisibility(8);
                }
            }
        }
    }

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.en_popview_free_collocation, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.back);
        this.back = textView;
        textView.setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Medium.ttf"));
        this.back.setOnClickListener(this);
        ((TextView) inflate.findViewById(R.id.tvSelTops)).setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-ExtralBold.otf"));
        ((TextView) inflate.findViewById(R.id.tvBuildDessert)).setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Black.otf"));
        ((TextView) inflate.findViewById(R.id.tvAddSauce)).setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Black.otf"));
        ((TextView) inflate.findViewById(R.id.tvNotice)).setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Medium.ttf"));
        ((TextView) inflate.findViewById(R.id.next)).setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Medium.ttf"));
        ((TextView) inflate.findViewById(R.id.pay)).setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Medium.ttf"));
        inflate.findViewById(R.id.next).setOnClickListener(this);
        inflate.findViewById(R.id.pay).setOnClickListener(this);
        MyGridViewAdapter myGridViewAdapter = new MyGridViewAdapter();
        this.gridViewAdapter1 = myGridViewAdapter;
        myGridViewAdapter.showConner = false;
        GridView gridView = (GridView) inflate.findViewById(R.id.gridview1);
        this.gridView1 = gridView;
        gridView.setAdapter((ListAdapter) this.gridViewAdapter1);
        MyGridViewAdapter myGridViewAdapter2 = new MyGridViewAdapter();
        this.gridViewAdapter2 = myGridViewAdapter2;
        myGridViewAdapter2.showConner = false;
        GridView gridView2 = (GridView) inflate.findViewById(R.id.gridview2);
        this.gridView2 = gridView2;
        gridView2.setAdapter((ListAdapter) this.gridViewAdapter2);
        if (!this.gridViewAdapter1.showConner && !this.gridViewAdapter2.showConner) {
            int dimensionPixelSize = inflate.getResources().getDimensionPixelSize(R.dimen.px50);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.gridView1.getLayoutParams();
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = dimensionPixelSize;
            this.gridView1.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.gridView2.getLayoutParams();
            layoutParams2.leftMargin = 0;
            layoutParams2.rightMargin = dimensionPixelSize;
            this.gridView2.setLayoutParams(layoutParams2);
        }
        return inflate;
    }

    @Override // com.oysb.utils.view.BasePopView
    protected void registActions(List<String> list) {
        list.add(MyShjStatusListener.ACTION_SHJ_FREE_TIME);
        list.add(MyShjStatusListener.ACTION_STATUS_RESET_FINISHED);
        list.add("Action_clear_selections");
        list.add("Action_refresh_view");
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
                case 285707362:
                    if (str.equals("Action_clear_selections")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1176350758:
                    if (str.equals(MyShjStatusListener.ACTION_STATUS_RESET_FINISHED)) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    this.tops.clear();
                    updateTotalMoney();
                    this.gridViewAdapter1.notifyDataSetChanged();
                    this.gridViewAdapter2.notifyDataSetChanged();
                    return;
                case 1:
                    BFPopView.showPopView("MainActivity", "PopView_Wait", (SysApp.sysModel.getUiType() == SysModel.UI_TYPE_BQL_EN ? PopView_Wait_en.class : PopView_Wait_st.class).getName(), "", 0);
                    return;
                case 2:
                    this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_en.1
                        AnonymousClass1() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_FreeCollocation_en.this.onViewWillShow();
                        }
                    });
                    return;
                case 3:
                    this.tops.clear();
                    this.gridViewAdapter1.notifyDataSetChanged();
                    this.gridViewAdapter2.notifyDataSetChanged();
                    return;
                case 4:
                    this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_en.2
                        AnonymousClass2() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_FreeCollocation_en.this.reloadData();
                        }
                    }, 1000L);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_en$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_FreeCollocation_en.this.onViewWillShow();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_en$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_FreeCollocation_en.this.reloadData();
        }
    }

    void reloadData() {
        int i;
        try {
            ArrayList<VmdHelper.TopItem> topDatas = VmdHelper.get().getTopDatas();
            int i2 = 0;
            while (true) {
                if (i2 >= 3 || i2 >= topDatas.size()) {
                    break;
                }
                this.gridViewAdapter1.datas.add(topDatas.get(i2));
                i2++;
            }
            for (i = 3; i < 6 && i < topDatas.size(); i++) {
                this.gridViewAdapter2.datas.add(topDatas.get(i));
            }
            this.ywbqlName = Shj.getShelfInfo(101).getGoodsName();
            this.gridViewAdapter1.notifyDataSetChanged();
            this.gridViewAdapter2.notifyDataSetChanged();
        } catch (Exception unused) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) UUID.randomUUID().toString()).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("[error]" + ShjAppHelper.getString(R.string.bqlseterror))).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("time_out", (Object) 3000).put("closeOnClick", (Object) true).put("showTime", (Object) false));
            this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_en.3
                AnonymousClass3() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    BFPopView.showPopView("MainActivity", "PopView_Wait", (SysApp.sysModel.getUiType() == SysModel.UI_TYPE_BQL_EN ? PopView_Wait_en.class : PopView_Wait_st.class).getName(), "", 0);
                }
            }, 3000L);
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_en$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            BFPopView.showPopView("MainActivity", "PopView_Wait", (SysApp.sysModel.getUiType() == SysModel.UI_TYPE_BQL_EN ? PopView_Wait_en.class : PopView_Wait_st.class).getName(), "", 0);
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewWillShow() {
        Loger.writeLog("SHJ", "freeCollocation will show");
        this.ywbqlName = Shj.getShelfInfo(101).getGoodsName();
        this.tops.clear();
        super.onViewWillShow();
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidShow() {
        super.onViewDidShow();
        updateTotalMoney();
        this.gridViewAdapter1.notifyDataSetChanged();
        this.gridViewAdapter2.notifyDataSetChanged();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            BFPopView.showPopView("MainActivity", "PopView_Wait", (SysApp.sysModel.getUiType() == SysModel.UI_TYPE_BQL_EN ? PopView_Wait_en.class : PopView_Wait_st.class).getName(), "", 0);
        } else if ((id == R.id.next || id == R.id.pay) && VmdHelper.get().checkVmdConnected() && VmdHelper.get().checkBqlZhibeiCount(1)) {
            VmdHelper.get().doCheckBqlStatus(30000, new VmdHelper.CheckBqlStatusListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_en.4
                final /* synthetic */ String val$uid;

                AnonymousClass4(String str) {
                    uuid = str;
                }

                @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
                public void onStartWait2StatusOk(String str) {
                    PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) str).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("time_out", (Object) 30000).put("showTime", (Object) false));
                }

                /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_en$4$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements Runnable {
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        VmdHelper.get().createOrder(VmdHelper.BQL_TYPE_CUSTOMER);
                        VmdHelper.get().getBqlOrder().setBqlCount(PopView_FreeCollocation_en.this.ywbqlName, 1);
                        for (String str : PopView_FreeCollocation_en.this.tops.keySet()) {
                            if (PopView_FreeCollocation_en.this.tops.get(str).intValue() != 0) {
                                VmdHelper.get().getBqlOrder().setTopCount(str, 1);
                            }
                        }
                        Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                        BFPopView.showPopView("MainActivity", PopView_Pay_en.class, "");
                    }
                }

                @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
                public void onCheckBqlStatusResult(boolean z, String str) {
                    if (z) {
                        PopView_Info.closeInfo(uuid);
                        PopView_FreeCollocation_en.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_en.4.1
                            AnonymousClass1() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                VmdHelper.get().createOrder(VmdHelper.BQL_TYPE_CUSTOMER);
                                VmdHelper.get().getBqlOrder().setBqlCount(PopView_FreeCollocation_en.this.ywbqlName, 1);
                                for (String str2 : PopView_FreeCollocation_en.this.tops.keySet()) {
                                    if (PopView_FreeCollocation_en.this.tops.get(str2).intValue() != 0) {
                                        VmdHelper.get().getBqlOrder().setTopCount(str2, 1);
                                    }
                                }
                                Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                                BFPopView.showPopView("MainActivity", PopView_Pay_en.class, "");
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
    /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_en$4 */
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

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation_en$4$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                VmdHelper.get().createOrder(VmdHelper.BQL_TYPE_CUSTOMER);
                VmdHelper.get().getBqlOrder().setBqlCount(PopView_FreeCollocation_en.this.ywbqlName, 1);
                for (String str2 : PopView_FreeCollocation_en.this.tops.keySet()) {
                    if (PopView_FreeCollocation_en.this.tops.get(str2).intValue() != 0) {
                        VmdHelper.get().getBqlOrder().setTopCount(str2, 1);
                    }
                }
                Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                BFPopView.showPopView("MainActivity", PopView_Pay_en.class, "");
            }
        }

        @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
        public void onCheckBqlStatusResult(boolean z, String str) {
            if (z) {
                PopView_Info.closeInfo(uuid);
                PopView_FreeCollocation_en.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation_en.4.1
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        VmdHelper.get().createOrder(VmdHelper.BQL_TYPE_CUSTOMER);
                        VmdHelper.get().getBqlOrder().setBqlCount(PopView_FreeCollocation_en.this.ywbqlName, 1);
                        for (String str2 : PopView_FreeCollocation_en.this.tops.keySet()) {
                            if (PopView_FreeCollocation_en.this.tops.get(str2).intValue() != 0) {
                                VmdHelper.get().getBqlOrder().setTopCount(str2, 1);
                            }
                        }
                        Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                        BFPopView.showPopView("MainActivity", PopView_Pay_en.class, "");
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
            sb.append("$ ");
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
