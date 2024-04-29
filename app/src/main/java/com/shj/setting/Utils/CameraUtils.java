package com.shj.setting.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import java.io.ByteArrayOutputStream;

/* loaded from: classes2.dex */
public class CameraUtils {
    /* JADX WARN: Code restructure failed: missing block: B:34:0x007c, code lost:
    
        if (r3 > r4) goto L69;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.shj.setting.Utils.CameraUtils.CameraBean isColorAndIrCamera(android.content.Context r10, boolean r11) {
        /*
            r0 = 0
            java.lang.String r1 = "usb"
            java.lang.Object r10 = r10.getSystemService(r1)     // Catch: java.lang.Exception -> L8b
            android.hardware.usb.UsbManager r10 = (android.hardware.usb.UsbManager) r10     // Catch: java.lang.Exception -> L8b
            java.util.HashMap r10 = r10.getDeviceList()     // Catch: java.lang.Exception -> L8b
            java.util.Set r1 = r10.keySet()     // Catch: java.lang.Exception -> L8b
            java.util.Iterator r1 = r1.iterator()     // Catch: java.lang.Exception -> L8b
            r2 = -1
            r3 = -1
            r4 = -1
        L18:
            boolean r5 = r1.hasNext()     // Catch: java.lang.Exception -> L8b
            r6 = 1
            if (r5 == 0) goto L70
            java.lang.Object r5 = r1.next()     // Catch: java.lang.Exception -> L8b
            java.lang.String r5 = (java.lang.String) r5     // Catch: java.lang.Exception -> L8b
            java.lang.Object r5 = r10.get(r5)     // Catch: java.lang.Exception -> L8b
            android.hardware.usb.UsbDevice r5 = (android.hardware.usb.UsbDevice) r5     // Catch: java.lang.Exception -> L8b
            if (r5 == 0) goto L18
            int r7 = r5.getProductId()     // Catch: java.lang.Exception -> L8b
            r8 = 304(0x130, float:4.26E-43)
            java.lang.String r9 = "/"
            if (r7 != r8) goto L4f
            int r7 = r5.getVendorId()     // Catch: java.lang.Exception -> L8b
            r8 = 6257(0x1871, float:8.768E-42)
            if (r7 != r8) goto L4f
            java.lang.String r3 = r5.getDeviceName()     // Catch: java.lang.Exception -> L8b
            java.lang.String[] r3 = r3.split(r9)     // Catch: java.lang.Exception -> L8b
            int r7 = r3.length     // Catch: java.lang.Exception -> L8b
            int r7 = r7 - r6
            r3 = r3[r7]     // Catch: java.lang.Exception -> L8b
            int r3 = java.lang.Integer.parseInt(r3)     // Catch: java.lang.Exception -> L8b
        L4f:
            int r7 = r5.getProductId()     // Catch: java.lang.Exception -> L8b
            r8 = 25446(0x6366, float:3.5657E-41)
            if (r7 != r8) goto L18
            int r7 = r5.getVendorId()     // Catch: java.lang.Exception -> L8b
            r8 = 3141(0xc45, float:4.401E-42)
            if (r7 != r8) goto L18
            java.lang.String r4 = r5.getDeviceName()     // Catch: java.lang.Exception -> L8b
            java.lang.String[] r4 = r4.split(r9)     // Catch: java.lang.Exception -> L8b
            int r5 = r4.length     // Catch: java.lang.Exception -> L8b
            int r5 = r5 - r6
            r4 = r4[r5]     // Catch: java.lang.Exception -> L8b
            int r4 = java.lang.Integer.parseInt(r4)     // Catch: java.lang.Exception -> L8b
            goto L18
        L70:
            if (r3 != r2) goto L7a
            if (r4 != r2) goto L7a
            com.shj.setting.Utils.CameraUtils$CameraBean r10 = new com.shj.setting.Utils.CameraUtils$CameraBean     // Catch: java.lang.Exception -> L8b
            r10.<init>(r0)     // Catch: java.lang.Exception -> L8b
            return r10
        L7a:
            if (r11 == 0) goto L82
            if (r3 <= r4) goto L80
        L7e:
            r10 = 1
            goto L85
        L80:
            r10 = 0
            goto L85
        L82:
            if (r4 <= r3) goto L80
            goto L7e
        L85:
            com.shj.setting.Utils.CameraUtils$CameraBean r11 = new com.shj.setting.Utils.CameraUtils$CameraBean     // Catch: java.lang.Exception -> L8b
            r11.<init>(r10, r6)     // Catch: java.lang.Exception -> L8b
            return r11
        L8b:
            r10 = move-exception
            java.lang.String r10 = r10.getLocalizedMessage()
            java.lang.String r11 = "CameraUtils"
            android.util.Log.e(r11, r10)
            com.shj.setting.Utils.CameraUtils$CameraBean r10 = new com.shj.setting.Utils.CameraUtils$CameraBean
            r10.<init>(r0)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.setting.Utils.CameraUtils.isColorAndIrCamera(android.content.Context, boolean):com.shj.setting.Utils.CameraUtils$CameraBean");
    }

    /* loaded from: classes2.dex */
    public static class CameraBean {
        int cameraIndex;
        boolean isColorAndIrCamera;

        public CameraBean(int i, boolean z) {
            this.cameraIndex = i;
            this.isColorAndIrCamera = z;
        }

        public CameraBean(boolean z) {
            this.isColorAndIrCamera = z;
        }

        public int getCameraIndex() {
            return this.cameraIndex;
        }

        public boolean isColorAndIrCamera() {
            return this.isColorAndIrCamera;
        }
    }

    public static Bitmap getBitMap(byte[] bArr, Camera camera) {
        int i = camera.getParameters().getPreviewSize().width;
        int i2 = camera.getParameters().getPreviewSize().height;
        YuvImage yuvImage = new YuvImage(bArr, camera.getParameters().getPreviewFormat(), i, i2, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, i, i2), 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Matrix matrix = new Matrix();
        matrix.reset();
        Bitmap copy = Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix, true).copy(Bitmap.Config.ARGB_8888, true);
        float height = (copy.getHeight() > copy.getWidth() ? copy.getHeight() : copy.getWidth()) / 800.0f;
        return height > 1.0f ? Bitmap.createScaledBitmap(copy, (int) (copy.getWidth() / height), (int) (copy.getHeight() / height), false) : copy;
    }
}
