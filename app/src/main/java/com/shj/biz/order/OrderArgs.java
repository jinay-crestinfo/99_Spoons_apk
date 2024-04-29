package com.shj.biz.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public class OrderArgs implements Serializable {
    private static final long serialVersionUID = -2803925057355076152L;
    private String goodsCode = "-1";
    private int shelf = -1;
    private HashMap<String, String> args = new HashMap<>();
    private transient HashMap<String, List<GoodsBatchnumberItem>> goodsbatchnumbers = new HashMap<>();
    private boolean selByGoodsCode = true;
    private boolean autoSelect = true;
    private boolean autoOfferGoods = true;

    public void addRremark(String str) {
        if (getArg("Remark").length() != 0) {
            str = "|#|" + str;
        }
        putArg("Remark", str);
    }

    public String getRemark() {
        return getArg("Remark");
    }

    public Set<String> getKeys() {
        return this.args.keySet();
    }

    public String getArg(String str) {
        return this.args.containsKey(str) ? this.args.get(str) : "";
    }

    public void putArg(String str, String str2) {
        this.args.put(str, str2);
    }

    public String getGoodsCode() {
        return this.goodsCode;
    }

    public void setGoodsCode(String str) {
        this.goodsCode = str;
    }

    public int getShelf() {
        return this.shelf;
    }

    public void setShelf(int i) {
        this.shelf = i;
    }

    public boolean isSelectByGoodsCode() {
        return this.selByGoodsCode;
    }

    public void selectByGoodsCode(boolean z) {
        this.selByGoodsCode = z;
    }

    public boolean autoSelect() {
        return this.autoSelect;
    }

    public void autoSelect(boolean z) {
        this.autoSelect = z;
    }

    public boolean autoOfferGoods() {
        return this.autoOfferGoods;
    }

    public void setAutoOfferGoods(boolean z) {
        this.autoOfferGoods = z;
    }

    public void addGoodsBatchnumberItem(GoodsBatchnumberItem goodsBatchnumberItem) {
        if (!this.goodsbatchnumbers.containsKey(goodsBatchnumberItem.goodsCode)) {
            this.goodsbatchnumbers.put(goodsBatchnumberItem.goodsCode, new ArrayList());
        }
        this.goodsbatchnumbers.get(goodsBatchnumberItem.goodsCode).add(goodsBatchnumberItem);
    }

    public void restGoodsBatchnumberLockcount() {
        Iterator<List<GoodsBatchnumberItem>> it = this.goodsbatchnumbers.values().iterator();
        while (it.hasNext()) {
            Iterator<GoodsBatchnumberItem> it2 = it.next().iterator();
            while (it2.hasNext()) {
                it2.next().lockShelfCount = 0;
            }
        }
    }

    public String getNextGoodsbatchnumber(String str) {
        if (!this.goodsbatchnumbers.containsKey(str)) {
            return null;
        }
        for (GoodsBatchnumberItem goodsBatchnumberItem : this.goodsbatchnumbers.get(str)) {
            if (goodsBatchnumberItem.lockShelfCount < goodsBatchnumberItem.count) {
                goodsBatchnumberItem.lockShelfCount++;
                return goodsBatchnumberItem.batchNumber;
            }
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public static class GoodsBatchnumberItem {
        String batchNumber;
        int count;
        String goodsCode;
        int lockShelfCount = 0;

        public GoodsBatchnumberItem(String str, String str2, int i) {
            this.goodsCode = str;
            this.batchNumber = str2;
            this.count = i;
        }

        public int getCount() {
            return this.count;
        }

        public void setCount(int i) {
            this.count = i;
        }

        public int getLockShelfCount() {
            return this.lockShelfCount;
        }

        public void setLockShelfCount(int i) {
            this.lockShelfCount = i;
        }

        public String getGoodsCode() {
            return this.goodsCode;
        }

        public void setGoodsCode(String str) {
            this.goodsCode = str;
        }

        public String getBatchNumber() {
            return this.batchNumber;
        }

        public void setBatchNumber(String str) {
            this.batchNumber = str;
        }
    }
}
