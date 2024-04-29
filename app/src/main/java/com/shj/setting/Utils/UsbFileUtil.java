package com.shj.setting.Utils;

import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileOutputStream;
import com.oysb.utils.io.file.SDFileUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes2.dex */
public final class UsbFileUtil {
    public static final String DEFAULT_BIN_DIR = "usb";

    public static boolean checkSDcard() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static File getSaveFile(String str, String str2) {
        File file = new File(getSavePath(str) + File.separator + str2);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getSavePath(String str) {
        return getSaveFolder(str).getAbsolutePath();
    }

    public static File getSaveFolder(String str) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + str + File.separator);
        file.mkdirs();
        return file;
    }

    public static void closeIO(Closeable... closeableArr) {
        if (closeableArr == null || closeableArr.length <= 0) {
            return;
        }
        for (Closeable closeable : closeableArr) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void redFileStream(OutputStream outputStream, InputStream inputStream) throws IOException {
        byte[] bArr = new byte[8192];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                return;
            }
        }
    }

    public static void saveSDFile2OTG(File file, UsbFile usbFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            if (usbFile.isDirectory()) {
                UsbFile[] listFiles = usbFile.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    for (UsbFile usbFile2 : listFiles) {
                        if (usbFile2.getName().equals(file.getName())) {
                            usbFile2.delete();
                        }
                    }
                }
                try {
                    redFileStream(new UsbFileOutputStream(usbFile.createFile(file.getName())), fileInputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* renamed from: com.shj.setting.Utils.UsbFileUtil$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ Handler val$handler;
        final /* synthetic */ boolean val$isFull;
        final /* synthetic */ String val$path;

        AnonymousClass1(boolean z, String str, Handler handler) {
            z = z;
            str = str;
            handler = handler;
        }

        @Override // java.lang.Runnable
        public void run() {
            String str = SDFileUtils.SDCardRoot + "/xyShj";
            File file = new File(str);
            if (!file.exists()) {
                file.mkdir();
                file.setReadable(true, false);
                file.setWritable(true, false);
                file.setExecutable(true, false);
            }
            String str2 = str + "/avFiles";
            File file2 = new File(str2);
            if (!file2.exists()) {
                file2.mkdir();
                file2.setReadable(true, false);
                file2.setWritable(true, false);
                file2.setExecutable(true, false);
            }
            if (z) {
                str2 = str2 + "/FullScreen";
                File file3 = new File(str2);
                if (!file3.exists()) {
                    file3.mkdir();
                    file3.setReadable(true, false);
                    file3.setWritable(true, false);
                    file3.setExecutable(true, false);
                }
            }
            String fileName = SDFileUtils.getFileName(str);
            if (!TextUtils.isEmpty(fileName)) {
                String str3 = str2 + UsbFile.separator + fileName;
                File file4 = new File(str3);
                if (file4.exists()) {
                    file4.delete();
                }
                SDFileUtils.CopySdcardFile(str, str3);
            }
            handler.sendEmptyMessage(7);
        }
    }

    public static void copeAdFile(Handler handler, String str, boolean z) {
        new Thread(new Runnable() { // from class: com.shj.setting.Utils.UsbFileUtil.1
            final /* synthetic */ Handler val$handler;
            final /* synthetic */ boolean val$isFull;
            final /* synthetic */ String val$path;

            AnonymousClass1(boolean z2, String str2, Handler handler2) {
                z = z2;
                str = str2;
                handler = handler2;
            }

            @Override // java.lang.Runnable
            public void run() {
                String str2 = SDFileUtils.SDCardRoot + "/xyShj";
                File file = new File(str2);
                if (!file.exists()) {
                    file.mkdir();
                    file.setReadable(true, false);
                    file.setWritable(true, false);
                    file.setExecutable(true, false);
                }
                String str22 = str2 + "/avFiles";
                File file2 = new File(str22);
                if (!file2.exists()) {
                    file2.mkdir();
                    file2.setReadable(true, false);
                    file2.setWritable(true, false);
                    file2.setExecutable(true, false);
                }
                if (z) {
                    str22 = str22 + "/FullScreen";
                    File file3 = new File(str22);
                    if (!file3.exists()) {
                        file3.mkdir();
                        file3.setReadable(true, false);
                        file3.setWritable(true, false);
                        file3.setExecutable(true, false);
                    }
                }
                String fileName = SDFileUtils.getFileName(str);
                if (!TextUtils.isEmpty(fileName)) {
                    String str3 = str22 + UsbFile.separator + fileName;
                    File file4 = new File(str3);
                    if (file4.exists()) {
                        file4.delete();
                    }
                    SDFileUtils.CopySdcardFile(str, str3);
                }
                handler.sendEmptyMessage(7);
            }
        }).start();
    }
}
