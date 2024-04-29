package com.oysb.utils.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.google.zxing.ResultPoint;
import com.oysb.utils.R;
import com.oysb.utils.zxing.camera.CameraManager;
import com.xyshj.database.setting.SettingType;
import java.util.Collection;
import java.util.HashSet;

/* loaded from: classes2.dex */
public final class ViewfinderView extends View {
    private static final long ANIMATION_DELAY = 10;
    private static final int CORNER_WIDTH = 10;
    private static final int OPAQUE = 255;
    private static final int SPEEN_DISTANCE = 5;
    private static final int TEXT_PADDING_TOP = 30;
    private static final int TEXT_SIZE = 16;
    private static float density;
    private int ScreenRate;
    boolean isFirst;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private final int maskColor;
    private Paint paint;
    private Collection<ResultPoint> possibleResultPoints;
    private Bitmap resultBitmap;
    private final int resultColor;
    private final int resultPointColor;
    private int slideTop;

    public ViewfinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        float f = context.getResources().getDisplayMetrics().density;
        density = f;
        this.ScreenRate = (int) (f * 20.0f);
        this.paint = new Paint();
        Resources resources = getResources();
        this.maskColor = resources.getColor(R.color.viewfinder_mask);
        this.resultColor = resources.getColor(R.color.result_view);
        this.resultPointColor = resources.getColor(R.color.possible_result_points);
        this.possibleResultPoints = new HashSet(5);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        Rect framingRect = CameraManager.get().getFramingRect();
        if (framingRect == null) {
            return;
        }
        if (!this.isFirst) {
            this.isFirst = true;
            this.slideTop = framingRect.top;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        this.paint.setColor(this.resultBitmap != null ? this.resultColor : this.maskColor);
        float f = width;
        canvas.drawRect(0.0f, 0.0f, f, framingRect.top, this.paint);
        canvas.drawRect(0.0f, framingRect.top, framingRect.left, framingRect.bottom + 1, this.paint);
        canvas.drawRect(framingRect.right + 1, framingRect.top, f, framingRect.bottom + 1, this.paint);
        canvas.drawRect(0.0f, framingRect.bottom + 1, f, height, this.paint);
        if (this.resultBitmap != null) {
            this.paint.setAlpha(255);
            canvas.drawBitmap(this.resultBitmap, framingRect.left, framingRect.top, this.paint);
            return;
        }
        this.paint.setColor(Color.parseColor("#02AA80"));
        canvas.drawRect(framingRect.left, framingRect.top, framingRect.left + this.ScreenRate, framingRect.top + 10, this.paint);
        canvas.drawRect(framingRect.left, framingRect.top, framingRect.left + 10, framingRect.top + this.ScreenRate, this.paint);
        canvas.drawRect(framingRect.right - this.ScreenRate, framingRect.top, framingRect.right, framingRect.top + 10, this.paint);
        canvas.drawRect(framingRect.right - 10, framingRect.top, framingRect.right, framingRect.top + this.ScreenRate, this.paint);
        canvas.drawRect(framingRect.left, framingRect.bottom - 10, framingRect.left + this.ScreenRate, framingRect.bottom, this.paint);
        canvas.drawRect(framingRect.left, framingRect.bottom - this.ScreenRate, framingRect.left + 10, framingRect.bottom, this.paint);
        canvas.drawRect(framingRect.right - this.ScreenRate, framingRect.bottom - 10, framingRect.right, framingRect.bottom, this.paint);
        canvas.drawRect(framingRect.right - 10, framingRect.bottom - this.ScreenRate, framingRect.right, framingRect.bottom, this.paint);
        int i = this.slideTop + 5;
        this.slideTop = i;
        if (i >= framingRect.bottom) {
            this.slideTop = framingRect.top;
        }
        Rect rect = new Rect();
        rect.left = framingRect.left;
        rect.right = framingRect.right;
        rect.top = this.slideTop;
        rect.bottom = this.slideTop + 18;
        this.paint.setColor(Color.parseColor("#55000000"));
        this.paint.setTextSize(density * 16.0f);
        int i2 = ((framingRect.right - framingRect.left) / 2) + framingRect.left;
        float measureText = this.paint.measureText("请对准条形码");
        float f2 = i2 - (measureText / 2.0f);
        float f3 = framingRect.bottom + (density * 30.0f);
        float textSize = this.paint.getTextSize() + f3;
        float f4 = density;
        float f5 = 30.0f * f4;
        float f6 = f4 * 7.0f;
        RectF rectF = new RectF();
        rectF.left = f2 - f5;
        rectF.top = f3 - f6;
        rectF.right = measureText + f2 + f5;
        rectF.bottom = f6 + textSize;
        canvas.drawRoundRect(rectF, 180.0f, 180.0f, this.paint);
        this.paint.setColor(Color.rgb(SettingType.DAILY_SALES, SettingType.DAILY_SALES, SettingType.DAILY_SALES));
        canvas.drawText("请对准条形码", f2, textSize, this.paint);
        Collection<ResultPoint> collection = this.possibleResultPoints;
        Collection<ResultPoint> collection2 = this.lastPossibleResultPoints;
        if (collection.isEmpty()) {
            this.lastPossibleResultPoints = null;
        } else {
            this.possibleResultPoints = new HashSet(5);
            this.lastPossibleResultPoints = collection;
            this.paint.setAlpha(255);
            this.paint.setColor(this.resultPointColor);
            for (ResultPoint resultPoint : collection) {
                canvas.drawCircle(framingRect.left + resultPoint.getX(), framingRect.top + resultPoint.getY(), 6.0f, this.paint);
            }
        }
        if (collection2 != null) {
            this.paint.setAlpha(127);
            this.paint.setColor(this.resultPointColor);
            for (ResultPoint resultPoint2 : collection2) {
                canvas.drawCircle(framingRect.left + resultPoint2.getX(), framingRect.top + resultPoint2.getY(), 3.0f, this.paint);
            }
        }
        postInvalidateDelayed(ANIMATION_DELAY, framingRect.left, framingRect.top, framingRect.right, framingRect.bottom);
    }

    public void drawViewfinder() {
        this.resultBitmap = null;
        invalidate();
    }

    public void drawResultBitmap(Bitmap bitmap) {
        this.resultBitmap = bitmap;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint resultPoint) {
        this.possibleResultPoints.add(resultPoint);
    }
}
