package com.oysb.utils.zxing.camera;

import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import com.google.zxing.LuminanceSource;
import kotlin.UByte;

/* loaded from: classes2.dex */
public final class PlanarYUVLuminanceSource extends LuminanceSource {
    private final int dataHeight;
    private final int dataWidth;
    private final int left;
    private final int top;
    private final byte[] yuvData;

    @Override // com.google.zxing.LuminanceSource
    public boolean isCropSupported() {
        return true;
    }

    public PlanarYUVLuminanceSource(byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6) {
        super(i5, i6);
        if (i5 + i3 > i || i6 + i4 > i2) {
            throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
        }
        this.yuvData = bArr;
        this.dataWidth = i;
        this.dataHeight = i2;
        this.left = i3;
        this.top = i4;
    }

    @Override // com.google.zxing.LuminanceSource
    public byte[] getRow(int i, byte[] bArr) {
        if (i < 0 || i >= getHeight()) {
            throw new IllegalArgumentException("Requested row is outside the image: " + i);
        }
        int width = getWidth();
        if (bArr == null || bArr.length < width) {
            bArr = new byte[width];
        }
        System.arraycopy(this.yuvData, ((i + this.top) * this.dataWidth) + this.left, bArr, 0, width);
        return bArr;
    }

    @Override // com.google.zxing.LuminanceSource
    public byte[] getMatrix() {
        int width = getWidth();
        int height = getHeight();
        int i = this.dataWidth;
        if (width == i && height == this.dataHeight) {
            return this.yuvData;
        }
        int i2 = width * height;
        byte[] bArr = new byte[i2];
        int i3 = (this.top * i) + this.left;
        if (width == i) {
            System.arraycopy(this.yuvData, i3, bArr, 0, i2);
            return bArr;
        }
        byte[] bArr2 = this.yuvData;
        for (int i4 = 0; i4 < height; i4++) {
            System.arraycopy(bArr2, i3, bArr, i4 * width, width);
            i3 += this.dataWidth;
        }
        return bArr;
    }

    public int getDataWidth() {
        return this.dataWidth;
    }

    public int getDataHeight() {
        return this.dataHeight;
    }

    public Bitmap renderCroppedGreyscaleBitmap() {
        int width = getWidth();
        int height = getHeight();
        int[] iArr = new int[width * height];
        byte[] bArr = this.yuvData;
        int i = (this.top * this.dataWidth) + this.left;
        for (int i2 = 0; i2 < height; i2++) {
            int i3 = i2 * width;
            for (int i4 = 0; i4 < width; i4++) {
                iArr[i3 + i4] = ((bArr[i + i4] & UByte.MAX_VALUE) * 65793) | ViewCompat.MEASURED_STATE_MASK;
            }
            i += this.dataWidth;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }
}
