package com.oysb.utils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.oysb.utils.Loger;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class SurfaceViewEx extends SurfaceView implements SurfaceHolder.Callback {
    private WeakReference<Bitmap> bitmap;

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    public SurfaceViewEx(Context context) {
        super(context);
    }

    public SurfaceViewEx(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void drawCanvas(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        Canvas lockCanvas = getHolder().lockCanvas();
        if (lockCanvas != null) {
            lockCanvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        }
        getHolder().unlockCanvasAndPost(lockCanvas);
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            this.bitmap = null;
            destroyDrawingCache();
        } else {
            this.bitmap = new WeakReference<>(bitmap);
            drawCanvas(bitmap);
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Loger.writeLog("VIDEO", "surfaceChanged");
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        WeakReference<Bitmap> weakReference = this.bitmap;
        if (weakReference != null) {
            drawCanvas(weakReference.get());
        }
        Loger.writeLog("VIDEO", "surfaceCreated");
    }

    @Override // android.view.SurfaceView, android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
