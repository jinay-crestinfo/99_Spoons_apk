package com.shj.biz.order;

/* loaded from: classes2.dex */
public abstract class OrderPayItem_ICCard extends OrderPayItem {
    public abstract void queryCardBanlance(int i);

    public abstract void refund(long j, int i);

    public abstract void topUp(long j, int i);
}
