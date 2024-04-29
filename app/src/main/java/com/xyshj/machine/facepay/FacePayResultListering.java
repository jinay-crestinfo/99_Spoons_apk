package com.xyshj.machine.facepay;

/* loaded from: classes2.dex */
public interface FacePayResultListering {
    void onFacePayStared();

    void onFaceSuccess();

    void onPayFail(boolean z);

    void onPaySuccess();

    void showNotRigister();

    void showUnderAgeTip();
}
