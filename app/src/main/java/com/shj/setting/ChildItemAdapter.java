package com.shj.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.shj.setting.mainSettingItem.SettingTypeName;
import java.util.List;

/* loaded from: classes2.dex */
public class ChildItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Integer> typeList;

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    public ChildItemAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.typeList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        List<Integer> list = this.typeList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = this.layoutInflater.inflate(R.layout.layout_child_item, (ViewGroup) null);
        inflate.setEnabled(false);
        inflate.setFocusable(false);
        inflate.setFocusableInTouchMode(false);
        ((TextView) inflate.findViewById(R.id.tv_name)).setText(SettingTypeName.getSettingName(this.context, this.typeList.get(i).intValue()));
        return inflate;
    }
}
