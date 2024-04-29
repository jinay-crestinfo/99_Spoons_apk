package com.shj.setting.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import com.oysb.utils.Loger;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.Shj;
import com.shj.biz.tools.FileUploader;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes2.dex */
public class ScreenShotUtils {
    public static void saveScreenShotPic(Activity activity, String str) {
        String str2 = SDFileUtils.SDCardRoot + "xyShj/screenShot/";
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        String str3 = str2 + new SimpleDateFormat("yyyy_MM_dd_").format(new Date(System.currentTimeMillis())) + "screenshot.jpg";
        File file2 = new File(str3);
        Bitmap screenShot = screenShot(activity);
        try {
            if (!file2.exists()) {
                file2.createNewFile();
            }
            if (save(screenShot, file2, Bitmap.CompressFormat.JPEG, true)) {
                Loger.writeLog("UI", "\"截图已保持至" + file2.getAbsolutePath());
            }
            uploadFile(str3, file2, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: com.shj.setting.Utils.ScreenShotUtils$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements FileUploader.FileUploadListener {
        AnonymousClass1() {
        }

        @Override // com.shj.biz.tools.FileUploader.FileUploadListener
        public void onSecusses(String str, Object obj) {
            Loger.writeLog("PUSH", "上传文件成功");
        }

        @Override // com.shj.biz.tools.FileUploader.FileUploadListener
        public void onError(Object obj, String str, Object obj2) {
            Loger.writeLog("PUSH", "上传文件失败：" + obj);
        }
    }

    public static void uploadFile(String str, File file, String str2) {
        FileUploader.uploadFile2Server(str, str2, Shj.getMachineId(), file.getName(), new FileUploader.FileUploadListener() { // from class: com.shj.setting.Utils.ScreenShotUtils.1
            AnonymousClass1() {
            }

            @Override // com.shj.biz.tools.FileUploader.FileUploadListener
            public void onSecusses(String str3, Object obj) {
                Loger.writeLog("PUSH", "上传文件成功");
            }

            @Override // com.shj.biz.tools.FileUploader.FileUploadListener
            public void onError(Object obj, String str3, Object obj2) {
                Loger.writeLog("PUSH", "上传文件失败：" + obj);
            }
        });
    }

    public static boolean save(Bitmap bitmap, File file, Bitmap.CompressFormat compressFormat, boolean z) {
        boolean z2 = false;
        if (isEmptyBitmap(bitmap)) {
            return false;
        }
        try {
            z2 = bitmap.compress(compressFormat, 100, new BufferedOutputStream(new FileOutputStream(file)));
            if (z && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return z2;
    }

    public static Bitmap screenShot(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap drawingCache = decorView.getDrawingCache();
        int statusBarHeight = getStatusBarHeight(activity);
        Bitmap createBitmap = Bitmap.createBitmap(drawingCache, 0, statusBarHeight, (int) getDeviceDisplaySize(activity)[0], ((int) getDeviceDisplaySize(activity)[1]) - statusBarHeight);
        decorView.destroyDrawingCache();
        return createBitmap;
    }

    public static float[] getDeviceDisplaySize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return new float[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    public static int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static boolean isEmptyBitmap(Bitmap bitmap) {
        return bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0;
    }
}
