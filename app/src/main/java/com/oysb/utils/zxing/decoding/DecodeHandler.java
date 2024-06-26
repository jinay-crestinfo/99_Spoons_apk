package com.oysb.utils.zxing.decoding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.oysb.utils.R;
import com.oysb.utils.zxing.camera.CameraManager;
import com.oysb.utils.zxing.camera.PlanarYUVLuminanceSource;
import java.util.Hashtable;

/* loaded from: classes2.dex */
final class DecodeHandler extends Handler {
    private static final String TAG = "DecodeHandler";
    private final IFZxDecode activity;
    private final MultiFormatReader multiFormatReader;

    public DecodeHandler(IFZxDecode iFZxDecode, Hashtable<DecodeHintType, Object> hashtable) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        this.multiFormatReader = multiFormatReader;
        multiFormatReader.setHints(hashtable);
        this.activity = iFZxDecode;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what == R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == R.id.quit) {
            Looper.myLooper().quit();
        }
    }

    private void decode(byte[] bArr, int i, int i2) {
        Result result;
        long currentTimeMillis = System.currentTimeMillis();
        byte[] bArr2 = new byte[bArr.length];
        for (int i3 = 0; i3 < i2; i3++) {
            for (int i4 = 0; i4 < i; i4++) {
                bArr2[(((i4 * i2) + i2) - i3) - 1] = bArr[(i3 * i) + i4];
            }
        }
        PlanarYUVLuminanceSource buildLuminanceSource = CameraManager.get().buildLuminanceSource(bArr2, i2, i);
        try {
            result = this.multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(buildLuminanceSource)));
            this.multiFormatReader.reset();
        } catch (ReaderException unused) {
            this.multiFormatReader.reset();
            result = null;
        } catch (Throwable th) {
            this.multiFormatReader.reset();
            throw th;
        }
        if (result != null) {
            long currentTimeMillis2 = System.currentTimeMillis();
            Log.d(TAG, "Found barcode (" + (currentTimeMillis2 - currentTimeMillis) + " ms):\n" + result.toString());
            Message obtain = Message.obtain(this.activity.getHandler(), R.id.decode_succeeded, result);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DecodeThread.BARCODE_BITMAP, buildLuminanceSource.renderCroppedGreyscaleBitmap());
            obtain.setData(bundle);
            obtain.sendToTarget();
            return;
        }
        Message.obtain(this.activity.getHandler(), R.id.decode_failed).sendToTarget();
    }
}
