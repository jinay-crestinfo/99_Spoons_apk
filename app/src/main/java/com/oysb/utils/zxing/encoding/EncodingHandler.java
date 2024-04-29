package com.oysb.utils.zxing.encoding;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.oysb.utils.Loger;
import java.util.Hashtable;

/* loaded from: classes2.dex */
public final class EncodingHandler {
    private static final int BLACK = -16777216;

    public static synchronized Bitmap createQRCode(String str, int i) {
        Bitmap createBitmap;
        synchronized (EncodingHandler.class) {
            try {
                Hashtable hashtable = new Hashtable();
                hashtable.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                hashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
                hashtable.put(EncodeHintType.MARGIN, 1);
                BitMatrix deleteWhite = deleteWhite(new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, i, i, hashtable));
                int width = deleteWhite.getWidth();
                int height = deleteWhite.getHeight();
                int[] iArr = new int[width * height];
                for (int i2 = 0; i2 < height; i2++) {
                    for (int i3 = 0; i3 < width; i3++) {
                        if (deleteWhite.get(i3, i2)) {
                            iArr[(i2 * width) + i3] = -16777216;
                        }
                    }
                }
                createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                return null;
            }
        }
        return createBitmap;
    }

    private static BitMatrix deleteWhite(BitMatrix bitMatrix) {
        int[] enclosingRectangle = bitMatrix.getEnclosingRectangle();
        int i = enclosingRectangle[2] + 1;
        int i2 = enclosingRectangle[3] + 1;
        BitMatrix bitMatrix2 = new BitMatrix(i, i2);
        bitMatrix2.clear();
        for (int i3 = 0; i3 < i; i3++) {
            for (int i4 = 0; i4 < i2; i4++) {
                if (bitMatrix.get(enclosingRectangle[0] + i3, enclosingRectangle[1] + i4)) {
                    bitMatrix2.set(i3, i4);
                }
            }
        }
        return bitMatrix2;
    }
}
