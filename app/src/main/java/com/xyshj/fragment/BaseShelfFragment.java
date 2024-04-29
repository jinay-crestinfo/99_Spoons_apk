package com.xyshj.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import com.oysb.utils.Loger;
import com.shj.biz.goods.Goods;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BaseShelfFragment extends BaseFragment {
    private static int goodsSize;
    private List<Goods> datas = new ArrayList();
    private int itemVersion;

    @Override // com.xyshj.fragment.BaseFragment
    public View createView(LayoutInflater layoutInflater) {
        return null;
    }

    @Override // com.xyshj.fragment.BaseFragment
    public void initViews(Context context) {
    }

    @Override // com.xyshj.fragment.BaseFragment
    protected void onAction(String str, Bundle bundle) {
    }

    @Override // com.xyshj.fragment.BaseFragment
    protected void onMessage(Message message) {
    }

    @Override // com.xyshj.fragment.BaseFragment
    protected void registActions(List<String> list) {
    }

    public abstract void updateGoods(String str);

    public void setVisible(boolean z) {
        try {
            getContentView().setVisibility(z ? 0 : 8);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }

    public static void setGoodsSize(int i) {
        goodsSize = i;
    }

    public static int getGoodsSize() {
        return goodsSize;
    }

    public List<Goods> getDatas() {
        return this.datas;
    }

    public int goodsCount() {
        return this.datas.size();
    }

    public void addGoods(Goods goods) {
        this.datas.add(goods);
    }

    public boolean isFull() {
        return this.datas.size() >= getGoodsSize();
    }

    public int getItemVersion() {
        return this.itemVersion;
    }

    public void setItemViewVersion(int i) {
        this.itemVersion = i;
    }
}
