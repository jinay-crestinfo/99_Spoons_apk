package com.oysb.utils.io;

/* loaded from: classes2.dex */
public interface DownlaodListener {
    void downEnd();

    void downError(String str);

    void onProgress(boolean z, int i);

    void startDown();
}
