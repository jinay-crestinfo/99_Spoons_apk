package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.setting.R;
import java.util.List;

/* loaded from: classes2.dex */
public class GoodsInfoListDialog extends Dialog {
    private Button bt_ok;
    private Context context;
    private LayoutInflater inflater;
    private ListView listView;
    private LinearLayout ll_column;
    private MyAdapter myAdapter;
    private List<Integer> shelfList;

    public GoodsInfoListDialog(Context context, List<Integer> list) {
        super(context, R.style.loading_style);
        this.shelfList = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_goods_info_list_dialog);
        this.ll_column = (LinearLayout) findViewById(R.id.ll_column);
        this.listView = (ListView) findViewById(R.id.listView);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        setTitle();
        setCanceledOnTouchOutside(false);
        setListener();
        MyAdapter myAdapter = new MyAdapter(this.context);
        this.myAdapter = myAdapter;
        this.listView.setAdapter((ListAdapter) myAdapter);
    }

    private void setTitle() {
        View inflate = this.inflater.inflate(R.layout.layout_goods_info_list_item, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_shelf);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_price);
        TextView textView3 = (TextView) inflate.findViewById(R.id.tv_inventory);
        TextView textView4 = (TextView) inflate.findViewById(R.id.tv_capacity);
        TextView textView5 = (TextView) inflate.findViewById(R.id.tv_code);
        this.ll_column.addView(inflate);
        textView.setText(R.string.lab_shelf);
        textView2.setText(R.string.lab_price);
        textView3.setText(R.string.lab_stock);
        textView4.setText(R.string.lab_capacity);
        textView5.setText(R.string.code);
    }

    /* renamed from: com.shj.setting.Dialog.GoodsInfoListDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GoodsInfoListDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GoodsInfoListDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GoodsInfoListDialog.this.dismiss();
            }
        });
    }

    /* loaded from: classes2.dex */
    public class MyAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        public MyAdapter(Context context) {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (GoodsInfoListDialog.this.shelfList == null) {
                return 0;
            }
            return GoodsInfoListDialog.this.shelfList.size();
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = GoodsInfoListDialog.this.inflater.inflate(R.layout.layout_goods_info_list_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_shelf = (TextView) view.findViewById(R.id.tv_shelf);
                viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
                viewHolder.tv_inventory = (TextView) view.findViewById(R.id.tv_inventory);
                viewHolder.tv_capacity = (TextView) view.findViewById(R.id.tv_capacity);
                viewHolder.tv_code = (TextView) view.findViewById(R.id.tv_code);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            int intValue = ((Integer) GoodsInfoListDialog.this.shelfList.get(i)).intValue();
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
            viewHolder.tv_shelf.setText(String.format("%03d", Integer.valueOf(intValue)));
            if (shelfInfo != null) {
                int intValue2 = shelfInfo.getPrice().intValue();
                if (intValue2 <= 200) {
                    viewHolder.tv_price.setTextColor(GoodsInfoListDialog.this.context.getResources().getColor(R.color.color_red));
                } else {
                    viewHolder.tv_price.setTextColor(GoodsInfoListDialog.this.context.getResources().getColor(R.color.color_text));
                }
                viewHolder.tv_price.setText(String.valueOf(intValue2 / 100.0f));
                viewHolder.tv_inventory.setText(String.valueOf(shelfInfo.getGoodsCount()));
                viewHolder.tv_capacity.setText(String.valueOf(shelfInfo.getCapacity()));
                viewHolder.tv_code.setText(String.valueOf(shelfInfo.getGoodsCode()));
            }
            return view;
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public TextView tv_capacity;
            public TextView tv_code;
            public TextView tv_inventory;
            public TextView tv_price;
            public TextView tv_shelf;

            public ViewHolder() {
            }
        }
    }
}
