package com.xyshj.machine.popview;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class PopView_ClassicCollocation extends BasePopView implements View.OnClickListener {
    GridView gridView;
    MyGridViewAdapter gridViewAdapter;
    TextView totalPrice;
    ArrayList<VmdHelper.BqlItem> datas = new ArrayList<>();
    HashMap<VmdHelper.BqlItem, Integer> selCounts = new HashMap<>();
    int bqlMaxCount = 0;
    int zhibeiMaxCount = 0;

    @Override // com.oysb.utils.view.BFPopView
    protected void onMessage(Message message) {
    }

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
            return PopView_ClassicCollocation.this.datas.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return PopView_ClassicCollocation.this.datas.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(PopView_ClassicCollocation.this.gridView.getContext()).inflate(R.layout.classic_collocation_item_layout, (ViewGroup) null, false);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(0, 0);
                layoutParams.width = PopView_ClassicCollocation.this.getContentView().getResources().getDimensionPixelSize(R.dimen.px240);
                layoutParams.height = PopView_ClassicCollocation.this.getContentView().getResources().getDimensionPixelSize(R.dimen.px440);
                view.setLayoutParams(layoutParams);
                viewHolder = new ViewHolder();
                viewHolder.image = (ImageView) view.findViewById(R.id.image);
                viewHolder.saleout = (ImageView) view.findViewById(R.id.saleout);
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                viewHolder.price = (TextView) view.findViewById(R.id.price);
                viewHolder.reduce = (Button) view.findViewById(R.id.bt_reduce);
                viewHolder.add = (Button) view.findViewById(R.id.bt_add);
                viewHolder.count = (TextView) view.findViewById(R.id.count);
                View findViewById = view.findViewById(R.id.bql);
                viewHolder.bql = findViewById;
                findViewById.setTag(viewHolder);
                findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_ClassicCollocation.MyGridViewAdapter.1
                    AnonymousClass1() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        VmdHelper.get().getBqlOrder().setBqlCount(((ViewHolder) view2.getTag()).bqlItem.getName(), 1);
                        BFPopView.showPopView("MainActivity", PopView_Classic_SelectCount.class, (Serializable) null);
                        PopView_ClassicCollocation.this.close();
                    }
                });
                viewHolder.reduce.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_ClassicCollocation.MyGridViewAdapter.2
                    AnonymousClass2() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                        int bqlCount = VmdHelper.get().getBqlOrder().getBqlCount(viewHolder2.bqlItem.getName()) - 1;
                        if (bqlCount < 0) {
                            bqlCount = 0;
                        }
                        int intValue = PopView_ClassicCollocation.this.selCounts.get(viewHolder2.bqlItem).intValue() - 1;
                        PopView_ClassicCollocation.this.selCounts.put(viewHolder2.bqlItem, Integer.valueOf(intValue >= 0 ? intValue : 0));
                        VmdHelper.get().getBqlOrder().setBqlCount(viewHolder2.bqlItem.getName(), bqlCount);
                        viewHolder2.updateView();
                        PopView_ClassicCollocation.this.updateTotalMoney();
                    }
                });
                viewHolder.add.setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_ClassicCollocation.MyGridViewAdapter.3
                    AnonymousClass3() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                        int totalBqlCount = VmdHelper.get().getBqlOrder().getTotalBqlCount();
                        int i2 = PopView_ClassicCollocation.this.bqlMaxCount < 3 ? PopView_ClassicCollocation.this.bqlMaxCount : 3;
                        if (PopView_ClassicCollocation.this.zhibeiMaxCount < i2) {
                            i2 = PopView_ClassicCollocation.this.zhibeiMaxCount;
                        }
                        if (VmdHelper.get().checkBqlZhibeiCount(i2 > 0 ? i2 : 1) && totalBqlCount < i2) {
                            int bqlCount = VmdHelper.get().getBqlOrder().getBqlCount(viewHolder2.bqlItem.getName()) + 1;
                            if (bqlCount > i2) {
                                bqlCount = i2;
                            }
                            int intValue = PopView_ClassicCollocation.this.selCounts.get(viewHolder2.bqlItem).intValue() + 1;
                            if (intValue <= i2) {
                                i2 = intValue;
                            }
                            PopView_ClassicCollocation.this.selCounts.put(viewHolder2.bqlItem, Integer.valueOf(i2));
                            VmdHelper.get().getBqlOrder().setBqlCount(viewHolder2.bqlItem.getName(), bqlCount);
                            viewHolder2.updateView();
                            PopView_ClassicCollocation.this.updateTotalMoney();
                        }
                    }
                });
                viewHolder.add.setTag(viewHolder);
                viewHolder.reduce.setTag(viewHolder);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.bqlItem = (VmdHelper.BqlItem) getItem(i);
            if (!PopView_ClassicCollocation.this.selCounts.containsKey(viewHolder.bqlItem)) {
                PopView_ClassicCollocation.this.selCounts.put(viewHolder.bqlItem, 0);
            }
            viewHolder.updateView();
            return view;
        }

        /* renamed from: com.xyshj.machine.popview.PopView_ClassicCollocation$MyGridViewAdapter$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                VmdHelper.get().getBqlOrder().setBqlCount(((ViewHolder) view2.getTag()).bqlItem.getName(), 1);
                BFPopView.showPopView("MainActivity", PopView_Classic_SelectCount.class, (Serializable) null);
                PopView_ClassicCollocation.this.close();
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_ClassicCollocation$MyGridViewAdapter$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements View.OnClickListener {
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                int bqlCount = VmdHelper.get().getBqlOrder().getBqlCount(viewHolder2.bqlItem.getName()) - 1;
                if (bqlCount < 0) {
                    bqlCount = 0;
                }
                int intValue = PopView_ClassicCollocation.this.selCounts.get(viewHolder2.bqlItem).intValue() - 1;
                PopView_ClassicCollocation.this.selCounts.put(viewHolder2.bqlItem, Integer.valueOf(intValue >= 0 ? intValue : 0));
                VmdHelper.get().getBqlOrder().setBqlCount(viewHolder2.bqlItem.getName(), bqlCount);
                viewHolder2.updateView();
                PopView_ClassicCollocation.this.updateTotalMoney();
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_ClassicCollocation$MyGridViewAdapter$3 */
        /* loaded from: classes2.dex */
        class AnonymousClass3 implements View.OnClickListener {
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ViewHolder viewHolder2 = (ViewHolder) view2.getTag();
                int totalBqlCount = VmdHelper.get().getBqlOrder().getTotalBqlCount();
                int i2 = PopView_ClassicCollocation.this.bqlMaxCount < 3 ? PopView_ClassicCollocation.this.bqlMaxCount : 3;
                if (PopView_ClassicCollocation.this.zhibeiMaxCount < i2) {
                    i2 = PopView_ClassicCollocation.this.zhibeiMaxCount;
                }
                if (VmdHelper.get().checkBqlZhibeiCount(i2 > 0 ? i2 : 1) && totalBqlCount < i2) {
                    int bqlCount = VmdHelper.get().getBqlOrder().getBqlCount(viewHolder2.bqlItem.getName()) + 1;
                    if (bqlCount > i2) {
                        bqlCount = i2;
                    }
                    int intValue = PopView_ClassicCollocation.this.selCounts.get(viewHolder2.bqlItem).intValue() + 1;
                    if (intValue <= i2) {
                        i2 = intValue;
                    }
                    PopView_ClassicCollocation.this.selCounts.put(viewHolder2.bqlItem, Integer.valueOf(i2));
                    VmdHelper.get().getBqlOrder().setBqlCount(viewHolder2.bqlItem.getName(), bqlCount);
                    viewHolder2.updateView();
                    PopView_ClassicCollocation.this.updateTotalMoney();
                }
            }
        }

        /* loaded from: classes2.dex */
        class ViewHolder {
            Button add;
            View bql;
            VmdHelper.BqlItem bqlItem;
            TextView count;
            ImageView image;
            TextView name;
            TextView price;
            Button reduce;
            ImageView saleout;

            ViewHolder() {
            }

            void updateView() {
                this.image.setImageBitmap(ShjManager.getGoodsManager().getGoodsImage(this.bqlItem.getCode(), false));
                boolean z = true;
                boolean z2 = Shj.getShelfInfo(201).getGoodsCount().intValue() < 1;
                if (!z2 && ShjManager.getGoodsManager().getNextGoodsShelf(this.bqlItem.getCode()) == -1) {
                    z2 = true;
                }
                if (!z2) {
                    Iterator<String> it = this.bqlItem.getTops().keySet().iterator();
                    while (it.hasNext()) {
                        if (ShjManager.getGoodsManager().getGoodsCount(VmdHelper.get().getTopItem(it.next()).getCode()) <= 0) {
                            break;
                        }
                    }
                }
                z = z2;
                this.bql.setClickable(!z);
                this.add.setClickable(!z);
                this.reduce.setClickable(!z);
                this.saleout.setVisibility(z ? 0 : 8);
                this.name.setText(this.bqlItem.getName());
                TextView textView = this.price;
                StringBuilder sb = new StringBuilder();
                sb.append(SysApp.getPriceUnit());
                double price = this.bqlItem.getPrice();
                Double.isNaN(price);
                sb.append(price / 100.0d);
                textView.setText(sb.toString());
                this.count.setText("" + PopView_ClassicCollocation.this.selCounts.get(this.bqlItem));
            }
        }
    }

    void updateTotalMoney() {
        int orderPrice = VmdHelper.get().getBqlOrder().getOrderPrice();
        VmdHelper.get().getBqlOrder().getTotalBqlCount();
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

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.popview_classic_collocation, (ViewGroup) null);
        this.gridViewAdapter = new MyGridViewAdapter();
        GridView gridView = (GridView) inflate.findViewById(R.id.gridview);
        this.gridView = gridView;
        gridView.setAdapter((ListAdapter) this.gridViewAdapter);
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
        this.datas.clear();
        this.selCounts.clear();
        this.bqlMaxCount = Shj.getShelfInfo(201).getGoodsCount().intValue();
        this.zhibeiMaxCount = Shj.getShelfInfo(202).getGoodsCount().intValue();
        this.datas.addAll(VmdHelper.get().getBqlDatas());
        VmdHelper.get().createOrder(VmdHelper.BQL_TYPE_NORMAL);
        updateTotalMoney();
        super.onViewWillShow();
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidShow() {
        super.onViewDidShow();
        this.gridViewAdapter.notifyDataSetChanged();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            close();
            return;
        }
        if (id != R.id.pay) {
            return;
        }
        if (!VmdHelper.get().checkBqlTopEnnough(VmdHelper.get().getBqlOrder().getBqlItems())) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + UUID.randomUUID().toString())).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("" + ShjAppHelper.getString(R.string.notice_reselect))).put("time_out", (Object) 5000).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("showTime", (Object) false));
            return;
        }
        if (VmdHelper.get().checkVmdConnected() && VmdHelper.get().getBqlOrder() != null && VmdHelper.get().getBqlOrder().getTotalBqlCount() != 0 && VmdHelper.get().checkBqlZhibeiCount(VmdHelper.get().getBqlOrder().getTotalBqlCount())) {
            VmdHelper.get().doCheckBqlStatus(30000, new VmdHelper.CheckBqlStatusListener() { // from class: com.xyshj.machine.popview.PopView_ClassicCollocation.1
                final /* synthetic */ String val$uid;

                AnonymousClass1(String str) {
                    uuid = str;
                }

                @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
                public void onStartWait2StatusOk(String str) {
                    PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) str).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("time_out", (Object) 30000).put("showTime", (Object) false));
                }

                /* renamed from: com.xyshj.machine.popview.PopView_ClassicCollocation$1$1 */
                /* loaded from: classes2.dex */
                class RunnableC00821 implements Runnable {
                    RunnableC00821() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                        PopView_ClassicCollocation.this.close();
                    }
                }

                @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
                public void onCheckBqlStatusResult(boolean z, String str) {
                    if (z) {
                        PopView_Info.closeInfo(uuid);
                        PopView_ClassicCollocation.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_ClassicCollocation.1.1
                            RunnableC00821() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                                PopView_ClassicCollocation.this.close();
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
    /* renamed from: com.xyshj.machine.popview.PopView_ClassicCollocation$1 */
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

        /* renamed from: com.xyshj.machine.popview.PopView_ClassicCollocation$1$1 */
        /* loaded from: classes2.dex */
        class RunnableC00821 implements Runnable {
            RunnableC00821() {
            }

            @Override // java.lang.Runnable
            public void run() {
                BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                PopView_ClassicCollocation.this.close();
            }
        }

        @Override // com.xyshj.machine.app.VmdHelper.CheckBqlStatusListener
        public void onCheckBqlStatusResult(boolean z, String str) {
            if (z) {
                PopView_Info.closeInfo(uuid);
                PopView_ClassicCollocation.this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_ClassicCollocation.1.1
                    RunnableC00821() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        BFPopView.showPopView("MainActivity", PopView_Pay.class, (Serializable) null);
                        PopView_ClassicCollocation.this.close();
                    }
                });
                return;
            }
            String mainServicePhone = ShjAppHelper.getMainServicePhone();
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("[error]" + str)).put("time_out", (Object) 5000).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("showTime", (Object) true).put("notice", (Object) (ShjAppHelper.getString(R.string.phone) + ":" + mainServicePhone)));
        }
    }
}
