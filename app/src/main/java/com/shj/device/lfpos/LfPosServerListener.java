package com.shj.device.lfpos;

/* loaded from: classes2.dex */
public interface LfPosServerListener {
    void onLineCardPay(int i, String str, int i2, int i3, int i4);

    void sendMesssage2Server(byte[] bArr, byte[] bArr2);
}
