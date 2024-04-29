package com.xyshj.machine.facepay;

import android.content.Context;
import com.shj.biz.order.OrderPayType;

/* loaded from: classes2.dex */
public abstract class AbsFacePayHelper {
    public abstract OrderPayType getPayType();

    public abstract String getfacepayIdKey();

    public abstract void setFacePayResultListering(FacePayResultListering facePayResultListering);

    public abstract void smilePay(Context context, String str, String str2, String str3);
}
