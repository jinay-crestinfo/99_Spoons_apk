package com.oysb.utils;

/* loaded from: classes2.dex */
public interface SendCallback {
    void onFailure(String str);

    void onFailure(Throwable th);

    void onSuccess();
}
