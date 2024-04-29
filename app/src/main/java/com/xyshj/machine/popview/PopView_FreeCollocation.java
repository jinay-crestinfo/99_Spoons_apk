package com.xyshj.machine.popview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.oysb.utils.Loger;
import com.oysb.utils.view.BFPopView;
import com.oysb.utils.view.BasePopView;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.machine.R;
import com.xyshj.machine.app.SysApp;
import com.xyshj.machine.app.VmdHelper;
import com.xyshj.machine.listener.MyShjStatusListener;
import com.xyshj.machine.popview.PopView_Info;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class PopView_FreeCollocation extends BasePopView implements View.OnClickListener {
    GridView gridView;
    MyGridViewAdapter gridViewAdapter;
    TextView name;
    TextView price;
    TextView totalPrice;
    ArrayList<VmdHelper.TopItem> datas = new ArrayList<>();
    String ywbqlName = ShjAppHelper.getString(R.string.plain_ice_cream);

    @Override // com.oysb.utils.view.BFPopView
    protected void onMessage(Message message) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class MyGridViewAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        MyGridViewAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return PopView_FreeCollocation.this.datas.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return PopView_FreeCollocation.this.datas.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            int i2;
            if (view == null) {
                view = LayoutInflater.from(PopView_FreeCollocation.this.gridView.getContext()).inflate(R.layout.free_collocation_item_layout, (ViewGroup) null, false);
                viewHolder = new ViewHolder();
                viewHolder.image = (ImageView) view.findViewById(R.id.image);
                viewHolder.saleout = (ImageView) view.findViewById(R.id.saleout);
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                viewHolder.title = (TextView) view.findViewById(R.id.title);
                viewHolder.price = (TextView) view.findViewById(R.id.price);
                viewHolder.buy = (Button) view.findViewById(R.id.buy);
                viewHolder.count = (TextView) view.findViewById(R.id.count);
                viewHolder.reduce = (Button) view.findViewById(R.id.bt_reduce);
                viewHolder.add = (Button) view.findViewById(R.id.bt_add);
                viewHolder.shopCar = view.findViewById(R.id.shopCar);
                viewHolder.buy.setTag(viewHolder);
                viewHolder.reduce.setTag(viewHolder);
                viewHolder.add.setTag(viewHolder);
                viewHolder.buy.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation.MyGridViewAdapter.1
                    AnonymousClass1() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                        String name = viewHolder2.topItem.getName();
                        if (VmdHelper.get().getBqlOrder().getTopCount(name) == 1) {
                            VmdHelper.get().getBqlOrder().setTopCount(name, 0);
                        } else {
                            VmdHelper.get().getBqlOrder().setTopCount(name, 1);
                        }
                        viewHolder2.updateView();
                        PopView_FreeCollocation.this.updateTotalMoney();
                    }
                });
                viewHolder.reduce.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation.MyGridViewAdapter.2
                    AnonymousClass2() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                        String name = viewHolder2.topItem.getName();
                        int topCount = VmdHelper.get().getBqlOrder().getTopCount(name) - 1;
                        if (topCount < 0) {
                            topCount = 0;
                        }
                        VmdHelper.get().getBqlOrder().setTopCount(name, topCount);
                        viewHolder2.updateView();
                        PopView_FreeCollocation.this.updateTotalMoney();
                    }
                });
                viewHolder.add.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation.MyGridViewAdapter.3
                    AnonymousClass3() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                        String name = viewHolder2.topItem.getName();
                        int topCount = VmdHelper.get().getBqlOrder().getTopCount(name) + 1;
                        if (topCount > 3) {
                            topCount = 3;
                        }
                        VmdHelper.get().getBqlOrder().setTopCount(name, topCount);
                        viewHolder2.updateView();
                        PopView_FreeCollocation.this.updateTotalMoney();
                    }
                });
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            try {
                if (i % 2 == 0) {
                    i2 = i / 2;
                } else {
                    i2 = (i / 2) + 3;
                }
                viewHolder.topItem = (VmdHelper.TopItem) getItem(i2);
                viewHolder.updateView();
            } catch (Exception e) {
                Loger.writeException("SHJ", e);
            }
            return view;
        }

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation$MyGridViewAdapter$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                String name = viewHolder2.topItem.getName();
                if (VmdHelper.get().getBqlOrder().getTopCount(name) == 1) {
                    VmdHelper.get().getBqlOrder().setTopCount(name, 0);
                } else {
                    VmdHelper.get().getBqlOrder().setTopCount(name, 1);
                }
                viewHolder2.updateView();
                PopView_FreeCollocation.this.updateTotalMoney();
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation$MyGridViewAdapter$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements View.OnClickListener {
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                String name = viewHolder2.topItem.getName();
                int topCount = VmdHelper.get().getBqlOrder().getTopCount(name) - 1;
                if (topCount < 0) {
                    topCount = 0;
                }
                VmdHelper.get().getBqlOrder().setTopCount(name, topCount);
                viewHolder2.updateView();
                PopView_FreeCollocation.this.updateTotalMoney();
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation$MyGridViewAdapter$3 */
        /* loaded from: classes2.dex */
        class AnonymousClass3 implements View.OnClickListener {
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                String name = viewHolder2.topItem.getName();
                int topCount = VmdHelper.get().getBqlOrder().getTopCount(name) + 1;
                if (topCount > 3) {
                    topCount = 3;
                }
                VmdHelper.get().getBqlOrder().setTopCount(name, topCount);
                viewHolder2.updateView();
                PopView_FreeCollocation.this.updateTotalMoney();
            }
        }

        /* loaded from: classes2.dex */
        class ViewHolder {
            Button add;
            Button buy;
            TextView count;
            ImageView image;
            TextView name;
            TextView price;
            Button reduce;
            ImageView saleout;
            View shopCar;
            TextView title;
            VmdHelper.TopItem topItem;

            ViewHolder() {
            }

            void updateView() {
                this.image.setImageBitmap(ShjManager.getGoodsManager().getGoodsImage(this.topItem.getCode(), false));
                if (ShjManager.getGoodsManager().getGoodsCount(this.topItem.getCode()) <= 0) {
                    this.saleout.setVisibility(0);
                    this.buy.setClickable(false);
                    this.add.setClickable(false);
                    this.reduce.setClickable(false);
                } else {
                    this.saleout.setVisibility(8);
                    this.buy.setClickable(true);
                    this.add.setClickable(true);
                    this.reduce.setClickable(true);
                }
                this.name.setText(this.topItem.getName());
                this.title.setText(this.topItem.getTitle());
                TextView textView = this.price;
                StringBuilder sb = new StringBuilder();
                sb.append(SysApp.getPriceUnit());
                double price = this.topItem.getPrice();
                Double.isNaN(price);
                sb.append(price / 100.0d);
                textView.setText(sb.toString());
                if (VmdHelper.get().getBqlOrder().getTopCount(this.topItem.getName()) > 0) {
                    this.buy.setBackgroundResource(R.drawable.bt_normal2);
                    this.buy.setTextColor(Color.parseColor("#eb6a9b"));
                    this.buy.setText(ShjAppHelper.getString(R.string.selected));
                } else {
                    this.buy.setTextColor(Color.parseColor("#ffffff"));
                    this.buy.setBackgroundResource(R.drawable.selector_button_v2);
                    this.buy.setText(ShjAppHelper.getString(R.string.select));
                }
                this.count.setText("" + VmdHelper.get().getBqlOrder().getTopCount(this.topItem.getName()));
            }
        }
    }

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.popview_free_collocation, (ViewGroup) null);
        this.gridViewAdapter = new MyGridViewAdapter();
        GridView gridView = (GridView) inflate.findViewById(R.id.gridview);
        this.gridView = gridView;
        gridView.setAdapter((ListAdapter) this.gridViewAdapter);
        this.name = (TextView) inflate.findViewById(R.id.name);
        this.price = (TextView) inflate.findViewById(R.id.price);
        this.totalPrice = (TextView) inflate.findViewById(R.id.totalPrice);
        inflate.findViewById(R.id.back).setOnClickListener(this);
        inflate.findViewById(R.id.pay).setOnClickListener(this);
        return inflate;
    }

    @Override // com.oysb.utils.view.BasePopView
    protected void registActions(List<String> list) {
        list.add(MyShjStatusListener.ACTION_SHJ_FREE_TIME);
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onAction(String str, Bundle bundle) {
        if (isShowing()) {
            str.hashCode();
            if (str.equals(MyShjStatusListener.ACTION_SHJ_FREE_TIME)) {
                close();
            }
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewWillShow() {
        Loger.writeLog("SHJ", "freeCollocation will show");
        try {
            this.datas = VmdHelper.get().getTopDatas();
            VmdHelper.get().createOrder(VmdHelper.BQL_TYPE_CUSTOMER);
            this.ywbqlName = Shj.getShelfInfo(101).getGoodsName();
            VmdHelper.get().getBqlOrder().setBqlCount(this.ywbqlName, 1);
            this.name.setText(this.ywbqlName);
        } catch (Exception unused) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) UUID.randomUUID().toString()).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("[error]" + ShjAppHelper.getString(R.string.bqlseterror))).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("time_out", (Object) 3000).put("closeOnClick", (Object) true).put("showTime", (Object) false));
            this.handler.postDelayed(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation.1
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    PopView_FreeCollocation.this.close();
                }
            }, 3000L);
        }
        super.onViewWillShow();
    }

    /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_FreeCollocation.this.close();
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidShow() {
        super.onViewDidShow();
        Loger.writeLog("SHJ", "freeCollocation did show");
        if (VmdHelper.get().getBqlItem(this.ywbqlName) == null) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + UUID.randomUUID())).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("[error]" + ShjAppHelper.getString(R.string.bqlseterror))).put("notic", (Object) ShjAppHelper.getString(R.string.welcome)).put("time_out", (Object) 3000).put("showTime", (Object) false));
            close();
            return;
        }
        TextView textView = this.price;
        StringBuilder sb = new StringBuilder();
        sb.append(SysApp.getPriceUnit());
        double price = VmdHelper.get().getBqlItem(this.ywbqlName).getPrice();
        Double.isNaN(price);
        sb.append(price / 100.0d);
        textView.setText(sb.toString());
        updateTotalMoney();
        this.gridViewAdapter.notifyDataSetChanged();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            close();
        } else if (id == R.id.pay && VmdHelper.get().checkVmdConnected() && VmdHelper.get().checkBqlZhibeiCount(1)) {
            VmdHelper.get().doCheckBqlStatus(30000, new VmdHelper.CheckBqlStatusListener() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation.2
                final /* synthetic */ String val$uid;

                AnonymousClass2(String str) {
                    uuid = str;
                }

                @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
                public void onStartWait2StatusOk(String str) {
                    PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) str).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("time_out", (Object) 30000).put("showTime", (Object) false));
                }

                /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation$2$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements Runnable {
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                        BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                        PopView_FreeCollocation.this.close();
                    }
                }

                @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
                public void onCheckBqlStatusResult(boolean z, String str) {
                    if (z) {
                        PopView_Info.closeInfo(uuid);
                        PopView_FreeCollocation.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation.2.1
                            AnonymousClass1() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                                BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                                PopView_FreeCollocation.this.close();
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
    /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements VmdHelper.CheckBqlStatusListener {
        final /* synthetic */ String val$uid;

        AnonymousClass2(String str) {
            uuid = str;
        }

        @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
        public void onStartWait2StatusOk(String str) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) str).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("time_out", (Object) 30000).put("showTime", (Object) false));
        }

        /* renamed from: com.xyshj.machine.popview.PopView_FreeCollocation$2$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                PopView_FreeCollocation.this.close();
            }
        }

        @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
        public void onCheckBqlStatusResult(boolean z, String str) {
            if (z) {
                PopView_Info.closeInfo(uuid);
                PopView_FreeCollocation.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_FreeCollocation.2.1
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        Loger.writeLog("SHJ", "冰淇淋机状态正常,跳转到支付界面");
                        BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                        PopView_FreeCollocation.this.close();
                    }
                });
                return;
            }
            String mainServicePhone = ShjAppHelper.getMainServicePhone();
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("[error]" + str)).put("time_out", (Object) 5000).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("showTime", (Object) true).put("notice", (Object) (ShjAppHelper.getString(R.string.phone) + ":" + mainServicePhone)));
        }
    }

    void updateTotalMoney() {
        int orderPrice = VmdHelper.get().getBqlOrder().getOrderPrice();
        String replace = getContentView().getContext().getResources().getString(R.string.lab_insertmoney).replace("$", SysApp.getPriceUnit());
        StringBuilder sb = new StringBuilder();
        sb.append("");
        double intValue = Shj.getWallet().getCatchMoney().intValue();
        Double.isNaN(intValue);
        sb.append(intValue / 100.0d);
        String replace2 = replace.replace("#X#", sb.toString());
        TextView textView = this.totalPrice;
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        double d = orderPrice;
        Double.isNaN(d);
        sb3.append(d / 100.0d);
        sb2.append(ShjAppHelper.getString(R.string.total, "#X#", sb3.toString()).replace("$", SysApp.getPriceUnit()));
        sb2.append(StringUtils.SPACE);
        sb2.append(replace2);
        textView.setText(sb2.toString());
    }
}
