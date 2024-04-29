package com.oysb.utils.io.file;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.github.mjdev.libaums.fs.UsbFile;
//import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.Constant;
import com.oysb.utils.Loger;
import com.oysb.utils.Utility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class SDFileUtils {
    public static File updateFile;
    static List<String> safeFiles = new ArrayList();
    public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static long getFreeBytes() {
        if (!isSDCardEnable()) {
            return 0L;
        }
        StatFs statFs = new StatFs(SDCardRoot);
        return statFs.getAvailableBlocks() * (statFs.getAvailableBlocks() - 4);
    }

    public static void addSafeFile(String str) {
        if (safeFiles.contains(str)) {
            return;
        }
        safeFiles.add(str);
    }

    public static String getFileName(String str) {
        return str != null ? str.substring(str.lastIndexOf(UsbFile.separator) + 1, str.length()) : "";
    }

    public static File createFileInSDCard(String str, String str2) {
        File file = new File(SDCardRoot + str2 + File.separator + str);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateFile = file;
        return file;
    }

    public static File createFileInDataCard(String str, String str2) {
        File file = new File(str2 + File.separator + str);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateFile = file;
        return file;
    }

    public static File creatSDDir(String str) {
        File file = new File(SDCardRoot + str + File.separator);
        file.mkdirs();
        return file;
    }

    public static File creatDataDir(String str) {
        File file = new File(str + File.separator);
        file.mkdirs();
        return file;
    }

    public static void safeDeleteFile(File file) {
        safeDeleteFile(file, true, null);
    }

    public static void safeDeleteFile(File file, boolean z) {
        safeDeleteFile(file, z, null);
    }

    public static void safeDeleteFile(File file, boolean z, ArrayList<String> arrayList) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File file2 : file.listFiles()) {
                    safeDeleteFile(file2, false, arrayList);
                }
                if (z) {
                    file.delete();
                }
            }
            if (file.isFile()) {
                String absolutePath = file.getAbsolutePath();
                String substring = absolutePath.substring(absolutePath.lastIndexOf(UsbFile.separator) + 1);
                if (safeFiles.contains(substring)) {
                    return;
                }
                if (arrayList == null || !arrayList.contains(substring)) {
                    file.delete();
                }
            }
        }
    }

    public static boolean isFileExist(String str, String str2) {
        return new File(SDCardRoot + str2 + File.separator + str).exists();
    }

    public static boolean isFileExist(String str) {
        return new File(str).exists();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r2v7 */
    public static File writeToSDFromInput(String directoryName, String fileName, InputStream inputStream, boolean append) {
        File file = null;
        FileOutputStream fileOutputStream = null;
        try {
            file = createFileInSDCard(directoryName, fileName);
            fileOutputStream = new FileOutputStream(file, append);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r1v7 */
    public static File writeToSDFromInput(String directoryName, String fileName, String content, boolean append) {
        File file = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            creatSDDir(directoryName);
            file = createFileInSDCard(fileName, directoryName);
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file, append));
            outputStreamWriter.write(content);
            outputStreamWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


//    public static File writeToDataFromInput(String str, String str2, Context context) {
//        OutputStreamWriter outputStreamWriter;
//        OutputStreamWriter outputStreamWriter2 = null;
//        try {
//            try {
//                outputStreamWriter = new OutputStreamWriter(context.openFileOutput(str, Context.MODE_APPEND));
//            } catch (Exception e) {
//                e = e;
//                outputStreamWriter = null;
//            } catch (Throwable th) {
//                th = th;
//                try {
//                    outputStreamWriter2.close();
//                } catch (Exception e2) {
//                    e2.printStackTrace();
//                }
//                throw th;
//            }
//        } catch (Exception e3) {
//            e3.printStackTrace();
//        }
//        try {
//            try {
//                outputStreamWriter.write(str2);
//                outputStreamWriter.flush();
//                outputStreamWriter.close();
//            } catch (Throwable th2) {
//                th = th2;
//                outputStreamWriter2 = outputStreamWriter;
//                outputStreamWriter2.close();
//                throw th;
//            }
//        } catch (Exception e4) {
//            e = e4;
//            e.printStackTrace();
//            outputStreamWriter.close();
//            return null;
//        }
//        return null;
//    }

//    public static ArrayList<String> readFileEx(String str) {
//        InputStreamReader inputStreamReader;
//        Throwable th;
//        ArrayList<String> arrayList = new ArrayList<>();
//        File file = new File(str);
//        if (file.isFile() && file.exists()) {
//            InputStreamReader inputStreamReader2 = null;
//            try {
//                try {
//                    inputStreamReader = new InputStreamReader(new FileInputStream(file));
//                } catch (Exception unused) {
//                } catch (Throwable th2) {
//                    inputStreamReader = null;
//                    th = th2;
//                }
//                try {
//                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                    while (true) {
//                        String readLine = bufferedReader.readLine();
//                        if (readLine == null) {
//                            break;
//                        }
//                        arrayList.add(0, readLine);
//                    }
//                    inputStreamReader.close();
//                } catch (Exception unused2) {
//                    inputStreamReader2 = inputStreamReader;
//                    if (inputStreamReader2 != null) {
//                        inputStreamReader2.close();
//                    }
//                    return arrayList;
//                } catch (Throwable th3) {
//                    th = th3;
//                    if (inputStreamReader != null) {
//                        try {
//                            inputStreamReader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    throw th;
//                }
//            } catch (IOException e2) {
//                e2.printStackTrace();
//            }
//        }
//        return arrayList;
//    }

//    public static String readFile(String str) {
//        InputStreamReader inputStreamReader;
//        Throwable th;
//        File file = new File(str);
//        String str2 = "";
//        if (file.isFile() && file.exists()) {
//            InputStreamReader inputStreamReader2 = null;
//            try {
//                try {
//                    inputStreamReader = new InputStreamReader(new FileInputStream(file));
//                } catch (Exception unused) {
//                } catch (Throwable th2) {
//                    inputStreamReader = null;
//                    th = th2;
//                }
//                try {
//                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                    while (true) {
//                        String readLine = bufferedReader.readLine();
//                        if (readLine == null) {
//                            break;
//                        }
//                        str2 = str2 + readLine;
//                    }
//                    inputStreamReader.close();
//                } catch (Exception unused2) {
//                    inputStreamReader2 = inputStreamReader;
//                    if (inputStreamReader2 != null) {
//                        inputStreamReader2.close();
//                    }
//                    return str2;
//                } catch (Throwable th3) {
//                    th = th3;
//                    if (inputStreamReader != null) {
//                        try {
//                            inputStreamReader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    throw th;
//                }
//            } catch (IOException e2) {
//                e2.printStackTrace();
//            }
//        }
//        return str2;
//    }

    public static List<File> getFiles(String directoryPath, String fileExtension) {
        ArrayList<File> filesList = new ArrayList<>();
        File[] files = new File(directoryPath).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (fileExtension == null || file.getName().endsWith(fileExtension))) {
                    filesList.add(file);
                }
            }
        } else {
            Loger.safe_inner_exception_catch(new Exception("Failed to list files in directory: " + directoryPath));
        }
        return filesList;
    }


    public static void moveFile(String str, String str2) {
        new File(str).renameTo(new File(str2));
    }

    public static boolean zipDirectory(String str, String str2) {
        try {
            ZipUtils.zipFiles(getFiles(str, null), new File(str2));
            safeDeleteFile(new File(str));
            return true;
        } catch (Exception unused) {
            Loger.writeLog("SYS", "压缩文件夹[" + str + "]失败");
            return false;
        }
    }

    public static boolean unZipFile(String str, String str2) {
        try {
            ZipUtils.upZipFile(new File(str), str2);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void copyAliProfile(Context context) {
        String str = SDCardRoot + "alipay/iotsdk/runtime";
        if (new File(str + UsbFile.separator + "profile.dat").exists()) {
            return;
        }
        copyAssetsFile(context, "profile.dat", str, "profile.dat");
    }

    public static void copyAssetsFile(Context context, String str, String str2, String str3) {
        String str4 = str2 + UsbFile.separator + str3;
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            if (new File(str4).exists()) {
                return;
            }
            InputStream open = context.getResources().getAssets().open(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str4);
            byte[] bArr = new byte[7168];
            while (true) {
                int read = open.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.close();
                    open.close();
                    File file2 = new File(str4);
                    file2.setWritable(true, false);
                    file2.setReadable(true, false);
                    file2.setExecutable(true, false);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean CopySdcardFile(String str, String str2) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (Exception unused) {
            return false;
        }
    }

    public static List<String> backupsShjApp2USBDrive(Context context, String str) {
        String backupsApp;
        ArrayList arrayList = new ArrayList();
        String str2 = str + "xyShj/";
        for (Constant.ConfigurePackageInfo configurePackageInfo : Constant.needPackageNames) {
            if ((configurePackageInfo.type & Constant.TYPE_BACKUPS) == Constant.TYPE_BACKUPS && (backupsApp = backupsApp(context, configurePackageInfo.packageName, str2, true)) != null) {
                arrayList.add(backupsApp);
            }
        }
        return arrayList;
    }

    public static void backupsShjApp(Context context) {
        String str = SDCardRoot + "xyShj/backUpApp/";
        for (Constant.ConfigurePackageInfo configurePackageInfo : Constant.needPackageNames) {
            if ((configurePackageInfo.type & Constant.TYPE_BACKUPS) == Constant.TYPE_BACKUPS) {
                backupsApp(context, configurePackageInfo.packageName, str, false);
            }
        }
    }

    public static String backupsApp(Context context, String str, String str2, boolean z) {
        String sourceApkPath;
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
            file.setExecutable(true, false);
            file.setWritable(true, false);
            file.setReadable(true, false);
        }
        String str3 = str2 + str + ".apk";
        File file2 = new File(str3);
        if (z && file2.exists()) {
            file2.delete();
        }
        if (!file2.exists() && (sourceApkPath = Utility.getSourceApkPath(context, str)) != null) {
            CopySdcardFile(sourceApkPath, str3);
        }
        return str3;
    }

    public static List<String> getAllExternalSdcardPath() {
        String lowerCase;
        String lowerCase2;
        ArrayList arrayList = new ArrayList();
        String path = Environment.getExternalStorageDirectory().getPath();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("mount").getInputStream()));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                Log.i("SDFileUtils", "line =" + readLine);
                if (!readLine.contains("proc") && !readLine.contains("tmpfs") && (!readLine.contains("media") || readLine.contains("media_rw"))) {
                    if (!readLine.contains("asec") && !readLine.contains("secure") && !readLine.contains("system") && !readLine.contains("cache") && !readLine.contains("sys") && !readLine.contains("") && !readLine.contains("shell") && !readLine.contains("root") && !readLine.contains("acct") && !readLine.contains("misc") && !readLine.contains("obb") && (readLine.contains("fat") || readLine.contains("fuse") || readLine.contains("ntfs") || readLine.contains("media_rw"))) {
                        String[] split = readLine.split(StringUtils.SPACE);
                        if (split != null && split.length > 1 && (lowerCase2 = split[1].toLowerCase(Locale.getDefault())) != null && !arrayList.contains(lowerCase2) && ((lowerCase2.contains("sd") && !lowerCase2.contains("internal_sd")) || (lowerCase2.contains("usb_storage") && lowerCase2.contains("udisk")))) {
                            arrayList.add(split[1]);
                        }
                        if (split != null && split.length > 2 && (lowerCase = split[2].toLowerCase(Locale.getDefault())) != null && !arrayList.contains(lowerCase) && lowerCase.contains("storage") && !lowerCase.contains("emulated")) {
                            arrayList.add(split[2]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!arrayList.contains(path)) {
            arrayList.add(path);
        }
        String str = SDCardRoot;
        if (arrayList.contains(str.substring(0, str.length() - 1))) {
            String str2 = SDCardRoot;
            arrayList.remove(str2.substring(0, str2.length() - 1));
        }
        return arrayList;
    }

    public static void ZipFolder(String str, String str2) throws Exception {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(str2));
        File file = new File(str);
        ZipFiles(file.getParent() + File.separator, file.getName(), zipOutputStream);
        zipOutputStream.finish();
        zipOutputStream.close();
    }

    private static void ZipFiles(String str, String str2, ZipOutputStream zipOutputStream) throws Exception {
        if (zipOutputStream == null) {
            return;
        }
        File file = new File(str + str2);
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(str2);
            FileInputStream fileInputStream = new FileInputStream(file);
            zipOutputStream.putNextEntry(zipEntry);
            byte[] bArr = new byte[4096];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read != -1) {
                    zipOutputStream.write(bArr, 0, read);
                } else {
                    zipOutputStream.closeEntry();
                    return;
                }
            }
        } else {
            String[] list = file.list();
            if (list.length <= 0) {
                zipOutputStream.putNextEntry(new ZipEntry(str2 + File.separator));
                zipOutputStream.closeEntry();
            }
            for (String str3 : list) {
                ZipFiles(str + str2 + UsbFile.separator, str3, zipOutputStream);
            }
        }
    }

    public static int isXml(int i) {
        File[] listFiles;
        File file = new File(SDCardRoot + "xyShj/fodder/xmls/" + i);
        if (!file.exists() || (listFiles = file.listFiles()) == null || listFiles.length <= 0) {
            return 0;
        }
        String name = listFiles[0].getName();
        int i2 = name.indexOf(".panxbundle") > 0 ? 1 : 0;
        if (name.indexOf(".xml") > 0) {
            return 2;
        }
        return i2;
    }

    public static String getLinkPath(String str) {
        String str2 = Build.DISPLAY;
        if (str2.startsWith("UBIOT") || str2.startsWith("msm")) {
            if ("/dev/ttyS1".equalsIgnoreCase(str)) {
                return "/dev/ttyHSL0";
            }
            if ("/dev/ttyS2".equalsIgnoreCase(str)) {
                return "/dev/ttyHSL1";
            }
            if ("/dev/ttyS3".equalsIgnoreCase(str)) {
                return "/dev/ttyHSL2";
            }
            if ("/dev/ttyS4".equalsIgnoreCase(str)) {
                return "/dev/ttyHSL3";
            }
            return null;
        }
        if ("/dev/ttyS1".equalsIgnoreCase(str)) {
            return "/dev/ttymxc1";
        }
        if ("/dev/ttyS2".equalsIgnoreCase(str)) {
            return "/dev/ttymxc2";
        }
        if ("/dev/ttyS3".equalsIgnoreCase(str)) {
            return "/dev/ttymxc3";
        }
        if ("/dev/ttyS4".equalsIgnoreCase(str)) {
            return "/dev/ttymxc4";
        }
        return null;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:48:0x001c -> B:11:0x0055). Please report as a decompilation issue!!! */
    public static Object readObject(File file) {
        FileInputStream fileInputStream;
        Throwable th;
        ObjectInputStream objectInputStream;
        Object obj = null;
        try {
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    objectInputStream = new ObjectInputStream(fileInputStream);
                } catch (Exception unused) {
                    objectInputStream = null;
                } catch (Throwable th2) {
                    th = th2;
                    objectInputStream = null;
                }
            } catch (Exception unused2) {
                objectInputStream = null;
                fileInputStream = null;
            } catch (Throwable th3) {
                fileInputStream = null;
                th = th3;
                objectInputStream = null;
            }
            try {
                obj = objectInputStream.readObject();
                try {
                    objectInputStream.close();
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                }
                fileInputStream.close();
            } catch (Exception unused3) {
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (Exception e2) {
                        Loger.safe_inner_exception_catch(e2);
                    }
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return obj;
            } catch (Throwable th4) {
                th = th4;
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (Exception e3) {
                        Loger.safe_inner_exception_catch(e3);
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                        throw th;
                    } catch (Exception e4) {
                        Loger.safe_inner_exception_catch(e4);
                        throw th;
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            Loger.safe_inner_exception_catch(e5);
        }
        return obj;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:48:0x001e -> B:11:0x0052). Please report as a decompilation issue!!! */
    public static void writeObject(File file, Object obj) {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;
        ObjectOutputStream objectOutputStream2 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(file);
                try {
                    objectOutputStream = new ObjectOutputStream(fileOutputStream);
                } catch (Exception unused) {
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Exception unused2) {
                fileOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = null;
            }
            try {
                objectOutputStream.writeObject(obj);
                objectOutputStream.flush();
                try {
                    objectOutputStream.close();
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                }
                fileOutputStream.close();
            } catch (Exception unused3) {
                objectOutputStream2 = objectOutputStream;
                if (objectOutputStream2 != null) {
                    try {
                        objectOutputStream2.close();
                    } catch (Exception e2) {
                        Loger.safe_inner_exception_catch(e2);
                    }
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Throwable th3) {
                th = th3;
                objectOutputStream2 = objectOutputStream;
                if (objectOutputStream2 != null) {
                    try {
                        objectOutputStream2.close();
                    } catch (Exception e3) {
                        Loger.safe_inner_exception_catch(e3);
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        throw th;
                    } catch (Exception e4) {
                        Loger.safe_inner_exception_catch(e4);
                        throw th;
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            Loger.safe_inner_exception_catch(e5);
        }
    }
}
