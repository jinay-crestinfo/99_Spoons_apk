package com.oysb.utils.zxing.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;

/* loaded from: classes2.dex */
public final class PreviewCallback implements Camera.PreviewCallback {
    private static final String TAG = "PreviewCallback";
    private final CameraConfigurationManager configManager;
    private Handler previewHandler;
    private int previewMessage;
    private final boolean useOneShotPreviewCallback;

    public PreviewCallback(CameraConfigurationManager cameraConfigurationManager, boolean z) {
        this.configManager = cameraConfigurationManager;
        this.useOneShotPreviewCallback = z;
    }

    public void setHandler(Handler handler, int i) {
        this.previewHandler = handler;
        this.previewMessage = i;
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] bArr, Camera camera) {
        Point cameraResolution = this.configManager.getCameraResolution();
        if (!this.useOneShotPreviewCallback) {
            camera.setPreviewCallback(null);
        }
        Handler handler = this.previewHandler;
        if (handler != null) {
            handler.obtainMessage(this.previewMessage, cameraResolution.x, cameraResolution.y, bArr).sendToTarget();
            this.previewHandler = null;
        } else {
            Log.d(TAG, "Got preview callback, but no handler for it");
        }
    }
}
