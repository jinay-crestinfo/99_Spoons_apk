package com.shj.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.shj.setting.Dialog.SelectEnabledDialog;
import com.shj.setting.mainSettingItem.AbsMainSettingItem;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class MainItemAdapter extends BaseAdapter {
    private Context context;
    private boolean isShowChild;
    private boolean isShowCommandItem;
    private LayoutInflater layoutInflater;
    private UserSettingDao mUserSettingDao;
    private List<AbsMainSettingItem> mainSettingItems;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    public MainItemAdapter(Context context, List<AbsMainSettingItem> list, boolean z, UserSettingDao userSettingDao, boolean z2) {
        this.context = context;
        this.mainSettingItems = list;
        this.isShowChild = z;
        this.mUserSettingDao = userSettingDao;
        this.isShowCommandItem = z2;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public boolean isShowChild() {
        return this.isShowChild;
    }

    public void setShowChild(boolean z) {
        this.isShowChild = z;
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        List<AbsMainSettingItem> list = this.mainSettingItems;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        List<AbsMainSettingItem> list = this.mainSettingItems;
        if (list == null) {
            return null;
        }
        return list.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = this.layoutInflater.inflate(R.layout.layout_main_item, (ViewGroup) null);
        RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.cl_left);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_name);
        ListView listView = (ListView) inflate.findViewById(R.id.lv_child);
        listView.setClickable(false);
        listView.setEnabled(false);
        listView.setFocusableInTouchMode(false);
        AbsMainSettingItem absMainSettingItem = this.mainSettingItems.get(i);
        textView.setText(absMainSettingItem.getName());
        if (this.isShowChild) {
            listView.setVisibility(0);
            relativeLayout.setVisibility(8);
            List<Integer> childTypeList = absMainSettingItem.getChildTypeList();
            if (childTypeList.size() == 1 || childTypeList.get(0).intValue() == 105) {
                childTypeList.clear();
                childTypeList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_CASH));
                childTypeList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_WEIXIN));
                childTypeList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_ZFB));
                childTypeList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YL));
                childTypeList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YLX));
                childTypeList.add(211);
                childTypeList.add(212);
                childTypeList.add(213);
            }
            ArrayList arrayList = new ArrayList();
            List<Integer> commandItem = SelectEnabledDialog.getCommandItem();
            Iterator<Integer> it = childTypeList.iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                if (this.isShowCommandItem || !commandItem.contains(Integer.valueOf(intValue))) {
                    if (AppSetting.isSettingEnabled(this.context, intValue, this.mUserSettingDao)) {
                        arrayList.add(Integer.valueOf(intValue));
                    }
                }
            }
            listView.setAdapter((ListAdapter) new ChildItemAdapter(this.context, arrayList));
            SettingActivity.setListViewBasedOnChildren(listView, false);
        } else {
            relativeLayout.setVisibility(0);
            listView.setVisibility(8);
        }
        if (absMainSettingItem.isSelect()) {
            inflate.setBackgroundColor(this.context.getResources().getColor(R.color.main_item_select));
            textView.setTextColor(this.context.getResources().getColor(R.color.setting_white));
        } else {
            inflate.setBackgroundColor(-1249537);
            textView.setTextColor(this.context.getResources().getColor(R.color.main_setting_text));
        }
        return inflate;
    }
}
