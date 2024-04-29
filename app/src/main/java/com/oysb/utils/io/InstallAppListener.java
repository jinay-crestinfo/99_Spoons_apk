package com.oysb.utils.io;

/* loaded from: classes2.dex */
public interface InstallAppListener {
    void onError(String str);

    void onProgress(boolean z, int i);

    void onStart();

    void onSuccess(String str);
}
