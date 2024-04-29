package com.shj.device.lfpos;

/* loaded from: classes2.dex */
public interface LfPosListener {
    void onPayResult(boolean z, String str, String str2, int i, String str3);

    void onQeryCardBalanceResult(boolean z, String str, String str2, int i, String str3);
}
