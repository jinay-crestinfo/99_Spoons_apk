package com.oysb.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.oysb.utils.Loger;
import com.oysb.utils.PRDownloaderTool;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.cache.BitmapCache;
import com.oysb.utils.io.file.SDFileUtils;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;

/* loaded from: classes2.dex */
public class ImageUtils {
    public static void checkImages(String str, String str2) {
        for (File file : SDFileUtils.getFiles(str, str2)) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
                    file.delete();
                }
            } catch (Exception e) {
                try {
                    file.delete();
                } catch (Exception unused) {
                }
                e.printStackTrace();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00dd A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0027 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void resizeImages(java.lang.String r7, java.lang.String r8, java.lang.String r9, int r10) {
        /*
            Method dump skipped, instructions count: 273
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.image.ImageUtils.resizeImages(java.lang.String, java.lang.String, java.lang.String, int):void");
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, float f, float f2, boolean z, boolean z2) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        matrix.postScale(f / width, f2 / height);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
            }
        }
        if (z) {
            try {
                Loger.writeLog("BITMAP", "recycle:" + bitmap.toString());
            } catch (Exception unused) {
            }
        }
        return createBitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, float f, float f2, boolean z) {
        Matrix matrix = new Matrix();
        matrix.postScale(f / bitmap.getWidth(), f2 / bitmap.getHeight());
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        if (z) {
            try {
                Loger.writeLog("BITMAP", "recycle:" + bitmap.toString());
            } catch (Exception unused) {
            }
        }
        return createBitmap;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0061, code lost:
    
        if (r4.isRecycled() != false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0063, code lost:
    
        com.oysb.utils.Loger.writeLog("BITMAP", "recycle:" + r4.toString());
        r4.recycle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0080, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:?, code lost:
    
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static Bitmap getBitmap(String filePath, int reqWidth, int reqHeight) {
        if (filePath == null) return null;

        File file = new File(filePath);
        if (!file.exists()) return null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        if (reqWidth != 0 && reqHeight > 0) {
            options.inSampleSize = options.outHeight / reqHeight;
        } else if (reqWidth > 0 && reqHeight == 0) {
            options.inSampleSize = options.outWidth / reqWidth;
        } else if (reqWidth != 0 && reqHeight != 0) {
            options.inSampleSize = options.outWidth / reqWidth;
            options.outHeight = reqHeight;
            options.outWidth = reqWidth;
        }

        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        if (bitmap != null && bitmap.getWidth() != -1 && bitmap.getHeight() != -1) {
            return bitmap;
        }

        if (bitmap != null && !bitmap.isRecycled()) {
            Loger.writeLog("BITMAP", "recycle:" + bitmap.toString());
            bitmap.recycle();
        }

        return null;
    }


    public static ImageSize getImageSize(InputStream inputStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    /* loaded from: classes2.dex */
    public static class ImageSize {
        int height;
        int width;

        public ImageSize() {
        }

        public ImageSize(int i, int i2) {
            this.width = i;
            this.height = i2;
        }

        public String toString() {
            return "ImageSize{width=" + this.width + ", height=" + this.height + '}';
        }
    }

    public static int calculateInSampleSize(ImageSize imageSize, ImageSize imageSize2) {
        int i = imageSize.width;
        int i2 = imageSize.height;
        int i3 = imageSize2.width;
        int i4 = imageSize2.height;
        if (i <= i3 || i2 <= i4) {
            return 1;
        }
        return Math.max(Math.round(i / i3), Math.round(i2 / i4));
    }

    public static ImageSize getImageViewSize(View view) {
        ImageSize imageSize = new ImageSize();
        imageSize.width = getExpectWidth(view);
        imageSize.height = getExpectHeight(view);
        return imageSize;
    }

    private static int getExpectHeight(View view) {
        int i = 0;
        if (view == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.height != -2) {
            i = view.getWidth();
        }
        if (i <= 0 && layoutParams != null) {
            i = layoutParams.height;
        }
        if (i <= 0) {
            i = getImageViewFieldValue(view, "mMaxHeight");
        }
        return i <= 0 ? view.getContext().getResources().getDisplayMetrics().heightPixels : i;
    }

    private static int getExpectWidth(View view) {
        int i = 0;
        if (view == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.width != -2) {
            i = view.getWidth();
        }
        if (i <= 0 && layoutParams != null) {
            i = layoutParams.width;
        }
        if (i <= 0) {
            i = getImageViewFieldValue(view, "mMaxWidth");
        }
        return i <= 0 ? view.getContext().getResources().getDisplayMetrics().widthPixels : i;
    }

    private static int getImageViewFieldValue(Object obj, String str) {
        try {
            Field declaredField = ImageView.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            int i = declaredField.getInt(obj);
            if (i <= 0 || i >= Integer.MAX_VALUE) {
                return 0;
            }
            return i;
        } catch (Exception unused) {
            return 0;
        }
    }

    public static void saveBitmap(Bitmap bitmap, String str, Bitmap.CompressFormat compressFormat) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
            if (bitmap.compress(compressFormat, 100, fileOutputStream)) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v1, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r7v11 */
    /* JADX WARN: Type inference failed for: r7v12 */
    /* JADX WARN: Type inference failed for: r7v14, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r7v4 */
    /* JADX WARN: Type inference failed for: r7v5 */
    /* JADX WARN: Type inference failed for: r7v7, types: [java.io.ByteArrayOutputStream] */
    public static boolean saveImageEx(@NonNull String imageUrl, @NonNull String imageName, @NonNull String saveDirectory) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            // Establish HTTP connection
            URL url = new URL(imageUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("GET");

            // Check response code
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("saveImageEx", "Failed to download image. Response code: " + httpURLConnection.getResponseCode());
                return false;
            }

            // Read input stream
            inputStream = httpURLConnection.getInputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // Convert byte array to Bitmap
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            // Ensure save directory ends with separator
            if (!saveDirectory.endsWith(File.separator)) {
                saveDirectory += File.separator;
            }

            // Save bitmap to file
            File imageFile = new File(saveDirectory + imageName);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            return true;
        } catch (Exception e) {
            Log.e("saveImageEx", "Exception while saving image: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Close streams and connections
            try {
                if (inputStream != null) inputStream.close();
                if (byteArrayOutputStream != null) byteArrayOutputStream.close();
                if (httpURLConnection != null) httpURLConnection.disconnect();
            } catch (Exception e) {
                Log.e("saveImageEx", "Exception while closing streams: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v11 */
    /* JADX WARN: Type inference failed for: r7v12 */
    /* JADX WARN: Type inference failed for: r7v13, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r7v2 */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r7v4, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r7v7, types: [java.io.ByteArrayOutputStream] */
    public static boolean saveImage(String imageUrl, String imageName, String saveDirectory) {
        InputStream inputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            // Open HTTP connection
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("GET");

            // Check response code
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }

            // Read input stream
            inputStream = httpURLConnection.getInputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[256];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // Convert byte array to bitmap
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            // Ensure save directory ends with separator
            if (!saveDirectory.endsWith(File.separator)) {
                saveDirectory += File.separator;
            }

            // Save bitmap to file
            File imageFile = new File(saveDirectory + imageName);
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(imageFile));
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, bufferedOutputStream);
            bufferedOutputStream.flush();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // Close streams
            try {
                if (inputStream != null) inputStream.close();
                if (byteArrayOutputStream != null) byteArrayOutputStream.close();
                if (bufferedOutputStream != null) bufferedOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.oysb.utils.image.ImageUtils$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends RunnableEx {
        AnonymousClass1(Object obj) {
            super(obj);
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            System.currentTimeMillis();
            Object[] objArr = (Object[]) getObj();
            List list = (List) objArr[0];
            DownloadImagesListener downloadImagesListener = (DownloadImagesListener) objArr[1];
            if (downloadImagesListener != null) {
                try {
                    downloadImagesListener.onStartDownloadImages(list.size());
                } catch (Exception unused) {
                }
            }
            int i = 0;
            for (int i2 = 0; i2 < list.size(); i2++) {
                try {
                    NetImageItem netImageItem = (NetImageItem) list.get(i2);
                    if (downloadImagesListener != null) {
                        try {
                            downloadImagesListener.onDownloadImage(netImageItem, list.size(), i2);
                        } catch (Exception unused2) {
                        }
                    }
                    if (ImageUtils.saveImage(netImageItem.getUrl(), netImageItem.getSaveName(), netImageItem.getSavePath())) {
                        i++;
                        if (netImageItem.getCatchPrefix().length() > 0) {
                            BitmapCache.clearCacheStartWithKey(netImageItem.getCatchPrefix());
                        }
                    }
                } catch (Exception unused3) {
                }
            }
            if (downloadImagesListener != null) {
                try {
                    downloadImagesListener.onDownloadImagesFinished(list.size(), i);
                } catch (Exception unused4) {
                }
            }
        }
    }

    public static void downloadImages(List<NetImageItem> itemList, DownloadImagesListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                if (listener != null) {
                    try {
                        listener.onStartDownloadImages(itemList.size());
                    } catch (Exception ignored) {
                    }
                }

                int successCount = 0;
                for (int i = 0; i < itemList.size(); i++) {
                    try {
                        NetImageItem item = itemList.get(i);
                        if (listener != null) {
                            try {
                                listener.onDownloadImage(item, itemList.size(), i);
                            } catch (Exception ignored) {
                            }
                        }
                        if (ImageUtils.saveImage(item.getUrl(), item.getSaveName(), item.getSavePath())) {
                            successCount++;
                            if (item.getCatchPrefix().length() > 0) {
                                BitmapCache.clearCacheStartWithKey(item.getCatchPrefix());
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }

                if (listener != null) {
                    try {
                        listener.onDownloadImagesFinished(itemList.size(), successCount);
                    } catch (Exception ignored) {
                    }
                }
            }
        }).start();
    }


    public static void downloadImages(List<NetImageItem> itemList, int currentIndex, int successCount, DownloadImagesListener listener) {
        if (itemList == null || itemList.isEmpty() || currentIndex >= itemList.size()) {
            if (listener != null) {
                try {
                    listener.onDownloadImagesFinished(itemList.size(), successCount);
                } catch (Exception ignored) {
                }
            }
            return;
        }

        if (currentIndex == 0 && listener != null) {
            try {
                listener.onStartDownloadImages(itemList.size());
            } catch (Exception ignored) {
            }
        }

        NetImageItem currentItem = itemList.get(currentIndex);
        if (listener != null) {
            try {
                listener.onDownloadImage(currentItem, itemList.size(), currentIndex);
            } catch (Exception ignored) {
            }
        }

        PRDownloaderTool.addImmediatelyDownloadTask(currentItem.getUrl(), currentItem.getSavePath(), currentItem.getSaveName(), null, new PRDownloaderTool.OnDownloadConditionListener() {
            @Override
            public void downloadFileExists() {
                if (currentItem.getCatchPrefix().length() > 0) {
                    BitmapCache.clearCacheStartWithKey(currentItem.getCatchPrefix());
                }
                downloadImages(itemList, currentIndex + 1, successCount + 1, listener);
            }

            @Override
            public void onDownloadComplete(String str) {
                if (currentItem.getCatchPrefix().length() > 0) {
                    BitmapCache.clearCacheStartWithKey(currentItem.getCatchPrefix());
                }
                downloadImages(itemList, currentIndex + 1, successCount + 1, listener);
            }

            @Override
            public void onError(Error error) {
                downloadImages(itemList, currentIndex + 1, successCount, listener);
            }
        });
    }


    /* renamed from: com.oysb.utils.image.ImageUtils$2 */
    /* loaded from: classes2.dex */
    public class DownloadConditionListener implements PRDownloaderTool.OnDownloadConditionListener {
        private final List<NetImageItem> items;
        private final int index;
        private final int successItemCount;
        private final DownloadImagesListener listener;

        public DownloadConditionListener(List<NetImageItem> items, int index, int successItemCount, DownloadImagesListener listener) {
            this.items = items;
            this.index = index;
            this.successItemCount = successItemCount;
            this.listener = listener;
        }

        @Override
        public void downloadFileExists() {
            NetImageItem currentItem = items.get(index);
            if (currentItem.getCatchPrefix().length() > 0) {
                BitmapCache.clearCacheStartWithKey(currentItem.getCatchPrefix());
            }
            ImageUtils.downloadImages(items, index + 1, successItemCount + 1, listener);
        }

        @Override
        public void onDownloadComplete(String str) {
            NetImageItem currentItem = items.get(index);
            if (currentItem.getCatchPrefix().length() > 0) {
                BitmapCache.clearCacheStartWithKey(currentItem.getCatchPrefix());
            }
            ImageUtils.downloadImages(items, index + 1, successItemCount + 1, listener);
        }

        @Override
        public void onError(Error error) {
            ImageUtils.downloadImages(items, index + 1, successItemCount, listener);
        }
    }


    public static Bitmap createQrcodeWithImage(String str, int i, int i2, Bitmap bitmap) {
        int i3;
        int i4;
        int i5;
        int i6;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            Bitmap scaleLogo = getScaleLogo(bitmap, i, i2);
            int i7 = i / 2;
            int i8 = i2 / 2;
            if (scaleLogo != null) {
                int width = scaleLogo.getWidth();
                int height = scaleLogo.getHeight();
                i5 = width;
                i6 = height;
                i3 = (i - width) / 2;
                i4 = (i2 - height) / 2;
            } else {
                i3 = i7;
                i4 = i8;
                i5 = 0;
                i6 = 0;
            }
            Hashtable hashtable = new Hashtable();
            hashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hashtable.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hashtable.put(EncodeHintType.MARGIN, 0);
            BitMatrix encode = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, i, i2, hashtable);
            int[] iArr = new int[i * i2];
            for (int i9 = 0; i9 < i2; i9++) {
                for (int i10 = 0; i10 < i; i10++) {
                    int i11 = ViewCompat.MEASURED_STATE_MASK;
                    if (i10 >= i3 && i10 < i3 + i5 && i9 >= i4 && i9 < i4 + i6) {
                        int pixel = scaleLogo.getPixel(i10 - i3, i9 - i4);
                        if (pixel != 0) {
                            i11 = pixel;
                        } else if (!encode.get(i10, i9)) {
                            i11 = -1;
                        }
                        iArr[(i9 * i) + i10] = i11;
                    } else if (encode.get(i10, i9)) {
                        iArr[(i9 * i) + i10] = -16777216;
                    } else {
                        iArr[(i9 * i) + i10] = -1;
                    }
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            createBitmap.setPixels(iArr, 0, i, 0, 0, i, i2);
            return createBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Bitmap getScaleLogo(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        float min = Math.min(((i * 1.0f) / 5.0f) / bitmap.getWidth(), ((i2 * 1.0f) / 5.0f) / bitmap.getHeight());
        matrix.postScale(min, min);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap Drawable2Bitmap(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

    public static Drawable bitmap2Drawable(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
        bitmapDrawable.setTargetDensity(bitmap.getDensity());
        return bitmapDrawable;
    }

    public static Bitmap cutBitmap(Bitmap bitmap, int i, int i2, Rect rect) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, rect, new Rect(0, 0, i, i2), new Paint());
        return createBitmap;
    }
}
