package com.oysb.utils.zxing.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class AutoFocusCallback implements Camera.AutoFocusCallback {
    private static final long AUTOFOCUS_INTERVAL_MS = 1500;
    private static final String TAG = "AutoFocusCallback";
    private Handler autoFocusHandler;
    private int autoFocusMessage;

    public void setHandler(Handler handler, int i) {
        this.autoFocusHandler = handler;
        this.autoFocusMessage = i;
    }

    @Override // android.hardware.Camera.AutoFocusCallback
    public void onAutoFocus(boolean z, Camera camera) {
        Handler handler = this.autoFocusHandler;
        if (handler != null) {
            this.autoFocusHandler.sendMessageDelayed(handler.obtainMessage(this.autoFocusMessage, Boolean.valueOf(z)), AUTOFOCUS_INTERVAL_MS);
            this.autoFocusHandler = null;
            return;
        }
        Log.d(TAG, "Got auto-focus callback, but no handler for it");
    }
}
