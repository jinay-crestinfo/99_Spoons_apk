package com.oysb.utils.zxing.decoding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.oysb.utils.R;
import com.oysb.utils.zxing.camera.CameraManager;
import com.oysb.utils.zxing.view.ViewfinderResultPointCallback;
import java.util.Vector;

/* loaded from: classes2.dex */
public final class CaptureActivityHandler extends Handler {
    private static final String TAG = "CaptureActivityHandler";
    private final IFZxDecode activity;
    private final DecodeThread decodeThread;
    private State state;

    /* loaded from: classes2.dex */
    public enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CaptureActivityHandler(IFZxDecode iFZxDecode, Vector<BarcodeFormat> vector, String str) {
        this.activity = iFZxDecode;
        DecodeThread decodeThread = new DecodeThread(iFZxDecode, vector, str, new ViewfinderResultPointCallback(iFZxDecode.getViewfinderView()));
        this.decodeThread = decodeThread;
        decodeThread.start();
        this.state = State.SUCCESS;
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what == R.id.auto_focus) {
            if (this.state == State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
                return;
            }
            return;
        }
        if (message.what == R.id.restart_preview) {
            restartPreviewAndDecode();
            return;
        }
        if (message.what == R.id.decode_succeeded) {
            this.state = State.SUCCESS;
            Bundle data = message.getData();
            this.activity.handleDecode((Result) message.obj, data == null ? null : (Bitmap) data.getParcelable(DecodeThread.BARCODE_BITMAP));
        } else if (message.what == R.id.decode_failed) {
            this.state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), R.id.decode);
        } else if (message.what != R.id.return_scan_result && message.what == R.id.launch_product_query) {
            new Intent("android.intent.action.VIEW", Uri.parse((String) message.obj)).addFlags(524288);
        }
    }

    public void quitSynchronously() {
        this.state = State.DONE;
        CameraManager.get().stopPreview();
        Message.obtain(this.decodeThread.getHandler(), R.id.quit).sendToTarget();
        try {
            this.decodeThread.join();
        } catch (InterruptedException unused) {
        }
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (this.state == State.SUCCESS) {
            this.state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            this.activity.drawViewfinder();
        }
    }
}
