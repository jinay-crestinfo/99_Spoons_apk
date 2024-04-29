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
import com.shj.ShjDbHelper;
import com.shj.setting.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class DownloadedGoodsInfoDialog extends Dialog {
    private Button bt_cancel;
    private Button bt_ok;
    private Context context;
    private JSONArray goodsItems;
    private LayoutInflater inflater;
    private ListView listView;
    private LinearLayout ll_column;
    private MyAdapter myAdapter;
    private OkClickListener okClickListener;

    /* loaded from: classes2.dex */
    public interface OkClickListener {
        void okClick();
    }

    public DownloadedGoodsInfoDialog(Context context, JSONArray jSONArray) {
        super(context, R.style.loading_style);
        this.goodsItems = jSONArray;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_downloaded_goods_info_dialog);
        this.ll_column = (LinearLayout) findViewById(R.id.ll_column);
        this.listView = (ListView) findViewById(R.id.listView);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        this.bt_cancel = (Button) findViewById(R.id.bt_cancel);
        setTitle();
        setCanceledOnTouchOutside(false);
        setListener();
        MyAdapter myAdapter = new MyAdapter(this.context);
        this.myAdapter = myAdapter;
        this.listView.setAdapter((ListAdapter) myAdapter);
    }

    private void setTitle() {
        View inflate = this.inflater.inflate(R.layout.layout_downloaded_goods_info_item, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_shelf);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_price);
        TextView textView3 = (TextView) inflate.findViewById(R.id.tv_code);
        this.ll_column.addView(inflate);
        textView.setText(R.string.lab_shelf);
        textView2.setText(R.string.lab_price);
        textView3.setText(R.string.code);
    }

    /* renamed from: com.shj.setting.Dialog.DownloadedGoodsInfoDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (DownloadedGoodsInfoDialog.this.okClickListener != null) {
                DownloadedGoodsInfoDialog.this.okClickListener.okClick();
            }
            DownloadedGoodsInfoDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.DownloadedGoodsInfoDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DownloadedGoodsInfoDialog.this.okClickListener != null) {
                    DownloadedGoodsInfoDialog.this.okClickListener.okClick();
                }
                DownloadedGoodsInfoDialog.this.dismiss();
            }
        });
        this.bt_cancel.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.DownloadedGoodsInfoDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DownloadedGoodsInfoDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.DownloadedGoodsInfoDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            DownloadedGoodsInfoDialog.this.dismiss();
        }
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
            return DownloadedGoodsInfoDialog.this.goodsItems.length();
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = DownloadedGoodsInfoDialog.this.inflater.inflate(R.layout.layout_downloaded_goods_info_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_shelf = (TextView) view.findViewById(R.id.tv_shelf);
                viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
                viewHolder.tv_code = (TextView) view.findViewById(R.id.tv_code);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            try {
                JSONObject jSONObject = DownloadedGoodsInfoDialog.this.goodsItems.getJSONObject(i);
                int parseInt = Integer.parseInt(jSONObject.getString(ShjDbHelper.COLUM_shelf));
                int i2 = jSONObject.isNull(ShjDbHelper.COLUM_price) ? 90000 : jSONObject.getInt(ShjDbHelper.COLUM_price);
                int parseInt2 = Integer.parseInt(jSONObject.getString("code"));
                viewHolder.tv_shelf.setText(String.format("%03d", Integer.valueOf(parseInt)));
                if (i2 <= 200) {
                    viewHolder.tv_price.setTextColor(DownloadedGoodsInfoDialog.this.context.getResources().getColor(R.color.color_red));
                } else {
                    viewHolder.tv_price.setTextColor(DownloadedGoodsInfoDialog.this.context.getResources().getColor(R.color.color_text));
                }
                viewHolder.tv_price.setText(String.valueOf(i2 / 100.0f));
                viewHolder.tv_code.setText(String.valueOf(parseInt2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public TextView tv_code;
            public TextView tv_price;
            public TextView tv_shelf;

            public ViewHolder() {
            }
        }
    }

    public void setOkClickListener(OkClickListener okClickListener) {
        this.okClickListener = okClickListener;
    }
}
