package com.oysb.utils.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Process;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ACache {
    private static final int MAX_COUNT = Integer.MAX_VALUE;
    private static final int MAX_SIZE = 50000000;
    public static final int TIME_DAY = 86400;
    public static final int TIME_HOUR = 3600;
    private static Map<String, ACache> mInstanceMap = new HashMap();
    private ACacheManager mCache;

    public static ACache get(Context context) {
        return get(context, "ACache");
    }

    public static ACache get(Context context, String str) {
        return get(new File(context.getCacheDir(), str), 50000000L, Integer.MAX_VALUE);
    }

    public static ACache get(File file) {
        return get(file, 50000000L, Integer.MAX_VALUE);
    }

    public static ACache get(Context context, long j, int i) {
        return get(new File(context.getCacheDir(), "ACache"), j, i);
    }

    public static ACache get(File file, long j, int i) {
        ACache aCache = mInstanceMap.get(file.getAbsoluteFile() + myPid());
        if (aCache != null) {
            return aCache;
        }
        ACache aCache2 = new ACache(file, j, i);
        mInstanceMap.put(file.getAbsolutePath() + myPid(), aCache2);
        return aCache2;
    }

    private static String myPid() {
        return "_" + Process.myPid();
    }

    private ACache(File file, long j, int i) {
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("can't make dirs in " + file.getAbsolutePath());
        }
        this.mCache = new ACacheManager(file, j, i);
    }

    /* loaded from: classes2.dex */
    class xFileOutputStream extends FileOutputStream {
        File file;

        public xFileOutputStream(File file) throws FileNotFoundException {
            super(file);
            this.file = file;
        }

        @Override // java.io.FileOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            super.close();
            ACache.this.mCache.put(this.file);
        }
    }

    public void put(String key, String data) {
        File newFile = mCache.newFile(key);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(newFile), 1024);
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mCache.put(newFile);
    }


    public void absSaveKey(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.mCache.newFile(str));
            fileInputStream.getFD().sync();
            fileInputStream.getFD().sync();
            fileInputStream.close();
        } catch (Exception unused) {
        }
    }

    public void put(String str, String str2, int i) {
        put(str, Utils.newStringWithDateInfo(i, str2));
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x006b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getAsString(java.lang.String r6) {
        /*
            r5 = this;
            com.oysb.utils.cache.ACache$ACacheManager r0 = r5.mCache
            java.io.File r0 = com.oysb.utils.cache.ACache.ACacheManager.access$500(r0, r6)
            boolean r1 = r0.exists()
            r2 = 0
            if (r1 != 0) goto Le
            return r2
        Le:
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L57
            java.io.FileReader r3 = new java.io.FileReader     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L57
            r3.<init>(r0)     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L57
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L57
            java.lang.String r0 = ""
        L1a:
            java.lang.String r3 = r1.readLine()     // Catch: java.io.IOException -> L53 java.lang.Throwable -> L67
            if (r3 == 0) goto L30
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.io.IOException -> L53 java.lang.Throwable -> L67
            r4.<init>()     // Catch: java.io.IOException -> L53 java.lang.Throwable -> L67
            r4.append(r0)     // Catch: java.io.IOException -> L53 java.lang.Throwable -> L67
            r4.append(r3)     // Catch: java.io.IOException -> L53 java.lang.Throwable -> L67
            java.lang.String r0 = r4.toString()     // Catch: java.io.IOException -> L53 java.lang.Throwable -> L67
            goto L1a
        L30:
            boolean r3 = com.oysb.utils.cache.ACache.Utils.access$600(r0)     // Catch: java.io.IOException -> L53 java.lang.Throwable -> L67
            if (r3 != 0) goto L47
            java.lang.String r6 = com.oysb.utils.cache.ACache.Utils.access$700(r0)     // Catch: java.io.IOException -> L53 java.lang.Throwable -> L67
            java.lang.String r6 = r6.toString()     // Catch: java.io.IOException -> L53 java.lang.Throwable -> L67
            r1.close()     // Catch: java.io.IOException -> L42
            goto L46
        L42:
            r0 = move-exception
            r0.printStackTrace()
        L46:
            return r6
        L47:
            r1.close()     // Catch: java.io.IOException -> L4b
            goto L4f
        L4b:
            r0 = move-exception
            r0.printStackTrace()
        L4f:
            r5.remove(r6)
            return r2
        L53:
            r6 = move-exception
            goto L59
        L55:
            r6 = move-exception
            goto L69
        L57:
            r6 = move-exception
            r1 = r2
        L59:
            r6.printStackTrace()     // Catch: java.lang.Throwable -> L67
            if (r1 == 0) goto L66
            r1.close()     // Catch: java.io.IOException -> L62
            goto L66
        L62:
            r6 = move-exception
            r6.printStackTrace()
        L66:
            return r2
        L67:
            r6 = move-exception
            r2 = r1
        L69:
            if (r2 == 0) goto L73
            r2.close()     // Catch: java.io.IOException -> L6f
            goto L73
        L6f:
            r0 = move-exception
            r0.printStackTrace()
        L73:
            goto L75
        L74:
            throw r6
        L75:
            goto L74
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.cache.ACache.getAsString(java.lang.String):java.lang.String");
    }

    public void put(String str, JSONObject jSONObject) {
        put(str, jSONObject.toString());
    }

    public void put(String str, JSONObject jSONObject, int i) {
        put(str, jSONObject.toString(), i);
    }

    public JSONObject getAsJSONObject(String str) {
        try {
            return new JSONObject(getAsString(str));
        } catch (Exception unused) {
            return null;
        }
    }

    public void put(String str, JSONArray jSONArray) {
        put(str, jSONArray.toString());
    }

    public void put(String str, JSONArray jSONArray, int i) {
        put(str, jSONArray.toString(), i);
    }

    public JSONArray getAsJSONArray(String str) {
        try {
            return new JSONArray(getAsString(str));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void put(String key, byte[] data) {
        File newFile = mCache.newFile(key);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(newFile);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mCache.put(newFile);
    }


    public OutputStream put(String str) throws FileNotFoundException {
        return new xFileOutputStream(this.mCache.newFile(str));
    }

    public InputStream get(String str) throws FileNotFoundException {
        File file = this.mCache.get(str);
        if (file.exists()) {
            return new FileInputStream(file);
        }
        return null;
    }

    public void put(String str, byte[] bArr, int i) {
        put(str, Utils.newByteArrayWithDateInfo(i, bArr));
    }

    /* JADX WARN: Not initialized variable reg: 2, insn: 0x0053: MOVE (r0 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:37:0x0053 */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0056 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public byte[] getAsBinary(java.lang.String r6) {
        /*
            r5 = this;
            r0 = 0
            com.oysb.utils.cache.ACache$ACacheManager r1 = r5.mCache     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            java.io.File r1 = com.oysb.utils.cache.ACache.ACacheManager.access$500(r1, r6)     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            boolean r2 = r1.exists()     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            if (r2 != 0) goto Le
            return r0
        Le:
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            java.lang.String r3 = "r"
            r2.<init>(r1, r3)     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            long r3 = r2.length()     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L52
            int r1 = (int) r3     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L52
            byte[] r1 = new byte[r1]     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L52
            r2.read(r1)     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L52
            boolean r3 = com.oysb.utils.cache.ACache.Utils.access$900(r1)     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L52
            if (r3 != 0) goto L32
            byte[] r6 = com.oysb.utils.cache.ACache.Utils.access$1000(r1)     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L52
            r2.close()     // Catch: java.io.IOException -> L2d
            goto L31
        L2d:
            r0 = move-exception
            r0.printStackTrace()
        L31:
            return r6
        L32:
            r2.close()     // Catch: java.io.IOException -> L36
            goto L3a
        L36:
            r1 = move-exception
            r1.printStackTrace()
        L3a:
            r5.remove(r6)
            return r0
        L3e:
            r6 = move-exception
            goto L44
        L40:
            r6 = move-exception
            goto L54
        L42:
            r6 = move-exception
            r2 = r0
        L44:
            r6.printStackTrace()     // Catch: java.lang.Throwable -> L52
            if (r2 == 0) goto L51
            r2.close()     // Catch: java.io.IOException -> L4d
            goto L51
        L4d:
            r6 = move-exception
            r6.printStackTrace()
        L51:
            return r0
        L52:
            r6 = move-exception
            r0 = r2
        L54:
            if (r0 == 0) goto L5e
            r0.close()     // Catch: java.io.IOException -> L5a
            goto L5e
        L5a:
            r0 = move-exception
            r0.printStackTrace()
        L5e:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.cache.ACache.getAsBinary(java.lang.String):byte[]");
    }

    public void put(String str, Serializable serializable) {
        put(str, serializable, -1);
    }

    public void put(String key, Serializable serializable, int bufferSize) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            if (bufferSize != -1) {
                put(key, byteArray, bufferSize);
            } else {
                put(key, byteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v1, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r5v10 */
    /* JADX WARN: Type inference failed for: r5v2, types: [java.io.ObjectInputStream] */
    /* JADX WARN: Type inference failed for: r5v5 */
    public Object getAsObject(String key) {
        byte[] serializedData = getAsBinary(key);

        if (serializedData == null || serializedData.length == 0) {
            return null;
        }

        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            byteArrayInputStream = new ByteArrayInputStream(serializedData);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    public void put(String str, Bitmap bitmap) {
        put(str, Utils.Bitmap2Bytes(bitmap));
    }

    public void put(String str, Bitmap bitmap, int i) {
        put(str, Utils.Bitmap2Bytes(bitmap), i);
    }

    public Bitmap getAsBitmap(String str) {
        if (getAsBinary(str) == null) {
            return null;
        }
        return Utils.Bytes2Bimap(getAsBinary(str));
    }

    public void put(String str, Drawable drawable) {
        put(str, Utils.drawable2Bitmap(drawable));
    }

    public void put(String str, Drawable drawable, int i) {
        put(str, Utils.drawable2Bitmap(drawable), i);
    }

    public Drawable getAsDrawable(String str) {
        if (getAsBinary(str) == null) {
            return null;
        }
        return Utils.bitmap2Drawable(Utils.Bytes2Bimap(getAsBinary(str)));
    }

    public File file(String str) {
        File newFile = this.mCache.newFile(str);
        if (newFile.exists()) {
            return newFile;
        }
        return null;
    }

    public boolean remove(String str) {
        return this.mCache.remove(str);
    }

    public void clear() {
        this.mCache.clear();
    }

    /* loaded from: classes2.dex */
    public class ACacheManager {
        private final AtomicInteger cacheCount;
        protected File cacheDir;
        private final AtomicLong cacheSize;
        private final int countLimit;
        private final Map<File, Long> lastUsageDates;
        private final long sizeLimit;

        /* synthetic */ ACacheManager(ACache aCache, File file, long j, int i, AnonymousClass1 anonymousClass1) {
            this(file, j, i);
        }

        private ACacheManager(File file, long j, int i) {
            this.lastUsageDates = Collections.synchronizedMap(new HashMap());
            this.cacheDir = file;
            this.sizeLimit = j;
            this.countLimit = i;
            this.cacheSize = new AtomicLong();
            this.cacheCount = new AtomicInteger();
            calculateCacheSizeAndCacheCount();
        }

        /* renamed from: com.oysb.utils.cache.ACache$ACacheManager$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                File[] listFiles = ACacheManager.this.cacheDir.listFiles();
                if (listFiles != null) {
                    int i = 0;
                    int i2 = 0;
                    for (File file : listFiles) {
                        i = (int) (i + ACacheManager.this.calculateSize(file));
                        i2++;
                        ACacheManager.this.lastUsageDates.put(file, Long.valueOf(file.lastModified()));
                    }
                    ACacheManager.this.cacheSize.set(i);
                    ACacheManager.this.cacheCount.set(i2);
                }
            }
        }

        private void calculateCacheSizeAndCacheCount() {
            new Thread(new Runnable() { // from class: com.oysb.utils.cache.ACache.ACacheManager.1

                @Override // java.lang.Runnable
                public void run() {
                    File[] listFiles = ACacheManager.this.cacheDir.listFiles();
                    if (listFiles != null) {
                        int i = 0;
                        int i2 = 0;
                        for (File file : listFiles) {
                            i = (int) (i + ACacheManager.this.calculateSize(file));
                            i2++;
                            ACacheManager.this.lastUsageDates.put(file, Long.valueOf(file.lastModified()));
                        }
                        ACacheManager.this.cacheSize.set(i);
                        ACacheManager.this.cacheCount.set(i2);
                    }
                }
            }).start();
        }

        public void put(File file) {
            int i = this.cacheCount.get();
            while (i + 1 > this.countLimit) {
                this.cacheSize.addAndGet(-removeNext());
                i = this.cacheCount.addAndGet(-1);
            }
            this.cacheCount.addAndGet(1);
            long calculateSize = calculateSize(file);
            long j = this.cacheSize.get();
            while (j + calculateSize > this.sizeLimit) {
                j = this.cacheSize.addAndGet(-removeNext());
            }
            this.cacheSize.addAndGet(calculateSize);
            Long valueOf = Long.valueOf(System.currentTimeMillis());
            file.setLastModified(valueOf.longValue());
            this.lastUsageDates.put(file, valueOf);
        }

        public File get(String str) {
            File newFile = newFile(str);
            Long valueOf = Long.valueOf(System.currentTimeMillis());
            newFile.setLastModified(valueOf.longValue());
            this.lastUsageDates.put(newFile, valueOf);
            return newFile;
        }

        public File newFile(String str) {
            return new File(this.cacheDir, str.hashCode() + "");
        }

        public boolean remove(String str) {
            return get(str).delete();
        }

        public void clear() {
            this.lastUsageDates.clear();
            this.cacheSize.set(0L);
            File[] listFiles = this.cacheDir.listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    file.delete();
                }
            }
        }

        private long removeNext() {
            File file;
            if (this.lastUsageDates.isEmpty()) {
                return 0L;
            }
            Set<Map.Entry<File, Long>> entrySet = this.lastUsageDates.entrySet();
            synchronized (this.lastUsageDates) {
                file = null;
                Long l = null;
                for (Map.Entry<File, Long> entry : entrySet) {
                    if (file == null) {
                        file = entry.getKey();
                        l = entry.getValue();
                    } else {
                        Long value = entry.getValue();
                        if (value.longValue() < l.longValue()) {
                            file = entry.getKey();
                            l = value;
                        }
                    }
                }
            }
            long calculateSize = calculateSize(file);
            if (file.delete()) {
                this.lastUsageDates.remove(file);
            }
            return calculateSize;
        }

        public long calculateSize(File file) {
            return file.length();
        }
    }

    /* loaded from: classes2.dex */
    public static class Utils {
        private static final char mSeparator = ' ';

        private Utils() {
        }

        public static boolean isDue(String str) {
            return isDue(str.getBytes());
        }

        public static boolean isDue(byte[] bArr) {
            String[] dateInfoFromDate = getDateInfoFromDate(bArr);
            if (dateInfoFromDate != null && dateInfoFromDate.length == 2) {
                String str = dateInfoFromDate[0];
                while (str.startsWith("0")) {
                    str = str.substring(1, str.length());
                }
                if (System.currentTimeMillis() > Long.valueOf(str).longValue() + (Long.valueOf(dateInfoFromDate[1]).longValue() * 1000)) {
                    return true;
                }
            }
            return false;
        }

        public static String newStringWithDateInfo(int i, String str) {
            return createDateInfo(i) + str;
        }

        public static byte[] newByteArrayWithDateInfo(int i, byte[] bArr) {
            byte[] bytes = createDateInfo(i).getBytes();
            byte[] bArr2 = new byte[bytes.length + bArr.length];
            System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
            System.arraycopy(bArr, 0, bArr2, bytes.length, bArr.length);
            return bArr2;
        }

        public static String clearDateInfo(String str) {
            return (str == null || !hasDateInfo(str.getBytes())) ? str : str.substring(str.indexOf(32) + 1, str.length());
        }

        public static byte[] clearDateInfo(byte[] bArr) {
            return hasDateInfo(bArr) ? copyOfRange(bArr, indexOf(bArr, mSeparator) + 1, bArr.length) : bArr;
        }

        private static boolean hasDateInfo(byte[] bArr) {
            return bArr != null && bArr.length > 15 && bArr[13] == 45 && indexOf(bArr, mSeparator) > 14;
        }

        private static String[] getDateInfoFromDate(byte[] bArr) {
            if (hasDateInfo(bArr)) {
                return new String[]{new String(copyOfRange(bArr, 0, 13)), new String(copyOfRange(bArr, 14, indexOf(bArr, mSeparator)))};
            }
            return null;
        }

        private static int indexOf(byte[] bArr, char c) {
            for (int i = 0; i < bArr.length; i++) {
                if (bArr[i] == c) {
                    return i;
                }
            }
            return -1;
        }

        private static byte[] copyOfRange(byte[] bArr, int i, int i2) {
            int i3 = i2 - i;
            if (i3 < 0) {
                throw new IllegalArgumentException(i + " > " + i2);
            }
            byte[] bArr2 = new byte[i3];
            System.arraycopy(bArr, i, bArr2, 0, Math.min(bArr.length - i, i3));
            return bArr2;
        }

        private static String createDateInfo(int i) {
            String str = System.currentTimeMillis() + "";
            while (str.length() < 13) {
                str = "0" + str;
            }
            return str + "-" + i + mSeparator;
        }

        public static byte[] Bitmap2Bytes(Bitmap bitmap) {
            if (bitmap == null) {
                return null;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }

        public static Bitmap Bytes2Bimap(byte[] bArr) {
            if (bArr.length == 0) {
                return null;
            }
            return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        }

        public static Bitmap drawable2Bitmap(Drawable drawable) {
            if (drawable == null) {
                return null;
            }
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != PixelFormat.UNKNOWN ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
            drawable.draw(canvas);
            return createBitmap;
        }

        public static Drawable bitmap2Drawable(Bitmap bitmap) {
            if (bitmap == null) {
                return null;
            }
            new BitmapDrawable(bitmap).setTargetDensity(bitmap.getDensity());
            return new BitmapDrawable(bitmap);
        }
    }
}
