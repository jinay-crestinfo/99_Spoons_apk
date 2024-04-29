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
import com.shj.setting.R;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class MutilTextTipDialog extends Dialog {
    private Button bt_ok;
    private Context context;
    private List<String> dataList;
    private LayoutInflater inflater;
    private ListView listView;
    private LinearLayout ll_statistical;
    private MyAdapter myAdapter;
    private List<TextView> statisticalText;
    private TextView tv_title;

    /* loaded from: classes2.dex */
    public static class StatisticalInfo {
        public String name;
        public int times;
        public int type;
    }

    public MutilTextTipDialog(Context context) {
        super(context, R.style.loading_style);
        this.statisticalText = new ArrayList();
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_mutiltexttip);
        this.tv_title = (TextView) findViewById(R.id.title);
        this.ll_statistical = (LinearLayout) findViewById(R.id.ll_statistical);
        this.listView = (ListView) findViewById(R.id.listView);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        setCanceledOnTouchOutside(false);
        setListener();
        showList();
    }

    /* renamed from: com.shj.setting.Dialog.MutilTextTipDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            MutilTextTipDialog.this.dataList.clear();
            MutilTextTipDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.MutilTextTipDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MutilTextTipDialog.this.dataList.clear();
                MutilTextTipDialog.this.dismiss();
            }
        });
    }

    private void showList() {
        this.dataList = new ArrayList();
        MyAdapter myAdapter = new MyAdapter(this.context);
        this.myAdapter = myAdapter;
        this.listView.setAdapter((ListAdapter) myAdapter);
    }

    public void addStatisticalInfo(int i, String str) {
        boolean z = false;
        for (TextView textView : this.statisticalText) {
            StatisticalInfo statisticalInfo = (StatisticalInfo) textView.getTag();
            if (statisticalInfo.type == i) {
                statisticalInfo.times++;
                textView.setText(statisticalInfo.name + this.context.getString(R.string.times) + statisticalInfo.times);
                z = true;
            }
        }
        if (z) {
            return;
        }
        View inflate = this.inflater.inflate(R.layout.layout_mutiltexttip_item, (ViewGroup) null);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_name);
        StatisticalInfo statisticalInfo2 = new StatisticalInfo();
        statisticalInfo2.times = 1;
        statisticalInfo2.name = str;
        statisticalInfo2.type = i;
        textView2.setText(statisticalInfo2.name + StringUtils.SPACE + this.context.getString(R.string.times) + statisticalInfo2.times);
        textView2.setTag(statisticalInfo2);
        this.ll_statistical.addView(inflate);
        this.statisticalText.add(textView2);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.height = 1;
        View view = new View(this.context);
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(this.context.getResources().getColor(R.color.color_text));
        this.ll_statistical.addView(view);
    }

    public void addTextShow(String str) {
        this.dataList.add(0, str);
        this.myAdapter.notifyDataSetChanged();
    }

    public void addTextShow(String str, boolean z) {
        if (z) {
            str = (this.dataList.size() + 1) + ":" + str;
        }
        this.dataList.add(0, str);
        this.myAdapter.notifyDataSetChanged();
    }

    public void clearText() {
        this.dataList.clear();
        this.myAdapter.notifyDataSetChanged();
    }

    public void setTitle(String str) {
        this.tv_title.setText(str);
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
            return MutilTextTipDialog.this.dataList.size();
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = MutilTextTipDialog.this.inflater.inflate(R.layout.layout_mutiltexttip_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_name.setText((CharSequence) MutilTextTipDialog.this.dataList.get(i));
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
