package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.shj.setting.R;
import java.util.List;

/* loaded from: classes2.dex */
public class SelectBatchNumberDialog extends Dialog {
    private Context context;
    private List<String> dataList;
    private LayoutInflater inflater;
    private ListView listView;
    private MyAdapter myAdapter;
    private SelectListener selectListener;

    /* loaded from: classes2.dex */
    public interface SelectListener {
        void select(int i, String str);
    }

    public SelectBatchNumberDialog(Context context, List<String> list, SelectListener selectListener) {
        super(context, R.style.ad_style);
        this.dataList = list;
        this.selectListener = selectListener;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_select_batch_number);
        this.listView = (ListView) findViewById(R.id.listView);
        int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(R.dimen.y80);
        ViewGroup.LayoutParams layoutParams = this.listView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = this.context.getResources().getDimensionPixelSize(R.dimen.y80) + (dimensionPixelSize * this.dataList.size());
        setCanceledOnTouchOutside(false);
        setListener();
        showList();
    }

    /* renamed from: com.shj.setting.Dialog.SelectBatchNumberDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements AdapterView.OnItemClickListener {
        AnonymousClass1() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (SelectBatchNumberDialog.this.selectListener != null) {
                SelectBatchNumberDialog.this.selectListener.select(i, (String) SelectBatchNumberDialog.this.dataList.get(i));
            }
            SelectBatchNumberDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shj.setting.Dialog.SelectBatchNumberDialog.1
            AnonymousClass1() {
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (SelectBatchNumberDialog.this.selectListener != null) {
                    SelectBatchNumberDialog.this.selectListener.select(i, (String) SelectBatchNumberDialog.this.dataList.get(i));
                }
                SelectBatchNumberDialog.this.dismiss();
            }
        });
    }

    private void showList() {
        MyAdapter myAdapter = new MyAdapter(this.context);
        this.myAdapter = myAdapter;
        this.listView.setAdapter((ListAdapter) myAdapter);
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
            return SelectBatchNumberDialog.this.dataList.size();
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = SelectBatchNumberDialog.this.inflater.inflate(R.layout.layout_select_batch_number_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_name.setText((CharSequence) SelectBatchNumberDialog.this.dataList.get(i));
            return view;
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public TextView tv_name;

            public ViewHolder() {
            }
        }
    }
}
