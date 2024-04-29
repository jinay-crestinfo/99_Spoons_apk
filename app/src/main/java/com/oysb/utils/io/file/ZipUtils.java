package com.oysb.utils.io.file;

import com.github.mjdev.libaums.fs.UsbFile;
import com.google.zxing.common.StringUtils;
import com.oysb.utils.Loger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/* loaded from: classes2.dex */
public class ZipUtils {
    private static final int BUFF_SIZE = 1048576;

    public static void zipFiles(Collection<File> collection, File file) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file), 1048576));
        try {
            try {
                Iterator<File> it = collection.iterator();
                while (it.hasNext()) {
                    zipFile(it.next(), zipOutputStream, "");
                }
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
            }
        } finally {
            zipOutputStream.close();
        }
    }

    public static void zipFiles(Collection<File> collection, File file, String str) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file), 1048576));
        try {
            Iterator<File> it = collection.iterator();
            while (it.hasNext()) {
                zipFile(it.next(), zipOutputStream, "");
            }
            zipOutputStream.setComment(str);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            zipOutputStream.close();
        }
    }

    public static void upZipFile(File file, String str) throws IOException {
        File file2 = new File(str);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry nextElement = entries.nextElement();
            InputStream inputStream = zipFile.getInputStream(nextElement);
            String str2 = new String((str + File.separator + nextElement.getName()).getBytes("8859_1"), StringUtils.GB2312);
            File file3 = new File(str2);
            if (str2.endsWith(UsbFile.separator)) {
                file3.mkdir();
                inputStream.close();
            } else {
                if (!file3.exists()) {
                    File parentFile = file3.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    file3.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                byte[] bArr = new byte[1048576];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read <= 0) {
                        break;
                    } else {
                        fileOutputStream.write(bArr, 0, read);
                    }
                }
                fileOutputStream.close();
                inputStream.close();
            }
        }
        zipFile.close();
    }

    public static ArrayList<File> upZipSelectedFile(File file, String str, String str2) throws IOException {
        ArrayList<File> arrayList = new ArrayList<>();
        File file2 = new File(str);
        if (!file2.exists()) {
            file2.mkdir();
        }
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry nextElement = entries.nextElement();
            if (nextElement.getName().contains(str2)) {
                InputStream inputStream = zipFile.getInputStream(nextElement);
                File file3 = new File(new String((str + File.separator + nextElement.getName()).getBytes("8859_1"), StringUtils.GB2312));
                if (!file3.exists()) {
                    File parentFile = file3.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    file3.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                byte[] bArr = new byte[1048576];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read <= 0) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
                fileOutputStream.close();
                inputStream.close();
                arrayList.add(file3);
            }
        }
        zipFile.close();
        return arrayList;
    }

    public static ArrayList<String> getEntriesNames(File file) throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        Enumeration<?> entriesEnumeration = getEntriesEnumeration(file);
        while (entriesEnumeration.hasMoreElements()) {
            arrayList.add(new String(getEntryName((ZipEntry) entriesEnumeration.nextElement()).getBytes(StringUtils.GB2312), "8859_1"));
        }
        return arrayList;
    }

    public static Enumeration<?> getEntriesEnumeration(File file) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        zipFile.close();
        return entries;
    }

    public static String getEntryComment(ZipEntry zipEntry) throws UnsupportedEncodingException {
        return new String(zipEntry.getComment().getBytes(StringUtils.GB2312), "8859_1");
    }

    public static String getEntryName(ZipEntry zipEntry) throws UnsupportedEncodingException {
        return new String(zipEntry.getName().getBytes(StringUtils.GB2312), "8859_1");
    }

    private static void zipFile(File file, ZipOutputStream zipOutputStream, String str) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str.trim().length() == 0 ? "" : File.separator);
        sb.append(file.getName());
        String str2 = new String(sb.toString().getBytes("8859_1"), StringUtils.GB2312);
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                zipFile(file2, zipOutputStream, str2);
            }
            return;
        }
        byte[] bArr = new byte[1048576];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file), 1048576);
        zipOutputStream.putNextEntry(new ZipEntry(str2));
        while (true) {
            int read = bufferedInputStream.read(bArr);
            if (read != -1) {
                zipOutputStream.write(bArr, 0, read);
            } else {
                bufferedInputStream.close();
                zipOutputStream.flush();
                zipOutputStream.closeEntry();
                return;
            }
        }
    }
}
