package com.shj.biz.goods;

/* loaded from: classes2.dex */
public interface GoodsStatusListener {
    void onBatchOfferGoods_Finished(BatchOfferGoodsInfo batchOfferGoodsInfo, String str, int i);

    void onDeselectGoods(String str);

    void onGoodsListChanged();

    void onGoodsReloaded();

    void onGoodsSynchronismFinished();

    void onGoodsUpdated();

    void onOfferingGoods(int i, String str, int i2);

    void onOfferingGoods_Blocked(int i, String str, int i2);

    void onOfferingGoods_Failed(int i, String str, int i2);

    void onOfferingGoods_None(int i, String str, int i2);

    void onOfferingGoods_State(int i, int i2, String str, int i3);

    void onOfferingGoods_Success(int i, String str, int i2);

    void onSelectGoods(String str);

    void onUpdateGoodsCount(String str, int i);

    void onUpdateGoodsPrice(String str, Integer num);

    void onUpdateShelfDoorState(int i, int i2);

    void onUpdateShelfGoods(Integer num, String str);

    void onUpdateShelfGoodsCount(Integer num, int i);

    void onUpdateShelfGoodsStatus(int i, int i2);
}
