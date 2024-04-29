package com.shj.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/* loaded from: classes2.dex */
public class SearchSettingItemAdapter extends BaseAdapter {
    private Context context;
    private List<SearchAdapterData> dataList;
    private LayoutInflater layoutInflater;

    /* loaded from: classes2.dex */
    public static class SearchAdapterData {
        public String name;
        public int settingType;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    public SearchSettingItemAdapter(Context context, List<SearchAdapterData> list) {
        this.context = context;
        this.dataList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.dataList.size();
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = (TextView) this.layoutInflater.inflate(R.layout.layout_search_item, (ViewGroup) null);
        textView.setEnabled(false);
        textView.setFocusable(false);
        textView.setFocusableInTouchMode(false);
        textView.setText(this.dataList.get(i).name);
        return textView;
    }
}
