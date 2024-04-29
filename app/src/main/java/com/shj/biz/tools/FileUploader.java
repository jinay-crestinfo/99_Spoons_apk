package com.shj.biz.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import com.oysb.utils.Loger;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class FileUploader {

    /* loaded from: classes2.dex */
    public interface FileUploadListener {
        void onError(Object obj, String str, Object obj2);

        void onSecusses(String str, Object obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.tools.FileUploader$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends RunnableEx {


        AnonymousClass1(Object obj, String str) {
            super(obj);
            str = str;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0293 A[Catch: all -> 0x02b3, Exception -> 0x02b8, TryCatch #11 {Exception -> 0x02b8, all -> 0x02b3, blocks: (B:19:0x028d, B:21:0x0293, B:22:0x0296), top: B:18:0x028d }] */
        /* JADX WARN: Removed duplicated region for block: B:43:0x02f5 A[Catch: Exception -> 0x02f9, TryCatch #9 {Exception -> 0x02f9, blocks: (B:28:0x02e5, B:43:0x02f5, B:44:0x02f8), top: B:16:0x024a }] */
        /* JADX WARN: Removed duplicated region for block: B:81:0x01f8  */
        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 806
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.tools.FileUploader.AnonymousClass1.run():void");
        }

        /* renamed from: com.shj.biz.tools.FileUploader$1$1 */
        /* loaded from: classes2.dex */
        class C00521 implements FileUploadListener {
            C00521() {
            }

            @Override // com.shj.biz.tools.FileUploader.FileUploadListener
            public void onSecusses(String str, Object obj) {
                Loger.writeLog("PUSH", "上传日志文件成功");
            }

            @Override // com.shj.biz.tools.FileUploader.FileUploadListener
            public void onError(Object obj, String str, Object obj2) {
                Loger.writeLog("PUSH", "上传日志文件失败：" + obj);
            }
        }
    }

    public static void copyLog(String str) {
        new Thread(new RunnableEx(null) { // from class: com.shj.biz.tools.FileUploader.1
            final /* synthetic */ String val$msg;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Object obj, String str2) {
                super(obj);
                str = str2;
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                /*  JADX ERROR: Method code generation error
                    java.lang.NullPointerException
                    */
                /*
                    Method dump skipped, instructions count: 806
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.tools.FileUploader.AnonymousClass1.run():void");
            }

            /* renamed from: com.shj.biz.tools.FileUploader$1$1 */
            /* loaded from: classes2.dex */
            class C00521 implements FileUploadListener {
                C00521() {
                }

                @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                public void onSecusses(String str, Object obj) {
                    Loger.writeLog("PUSH", "上传日志文件成功");
                }

                @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                public void onError(Object obj, String str, Object obj2) {
                    Loger.writeLog("PUSH", "上传日志文件失败：" + obj);
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.tools.FileUploader$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends RunnableEx {
        final /* synthetic */ String val$msg;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Object obj, String str) {
            super(obj);
            str = str;
        }

        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x0093 -> B:29:0x009b). Please report as a decompilation issue!!! */
        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            try {
                if (str.startsWith("file")) {
                    Loger.writeLog("UI", "收到文件指令：" + str);
                    try {
                        String[] split = str.split(":");
                        String str = split[1];
                        if ("savescreenshotpicfile".equals(str)) {
                            FileUploader.saveScreenShotPic((Activity) ShjManager.getActivityContext(), "savescreenshotpicfile");
                        } else if ("getallfilename".equals(str)) {
                            FileUploader.copyAllFileNames("getallfilename");
                        } else if ("uploadfile".equals(str)) {
                            if (split.length >= 3) {
                                String str2 = split[2];
                                File file = new File(str2);
                                if (file.exists()) {
                                    FileUploader.uploadFile2Server(str2, "uploadfile", Shj.getMachineId(), file.getName(), new FileUploadListener() { // from class: com.shj.biz.tools.FileUploader.2.1
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
                            }
                        } else if ("deletefile".equals(str) && split.length >= 3) {
                            File file2 = new File(split[2]);
                            if (file2.exists()) {
                                SDFileUtils.safeDeleteFile(file2);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                Loger.writeException("UI", e2);
            }
        }

        /* renamed from: com.shj.biz.tools.FileUploader$2$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements FileUploadListener {
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
        }
    }

    public static void fileOperate(String str) {
        new Thread(new RunnableEx(null) { // from class: com.shj.biz.tools.FileUploader.2
            final /* synthetic */ String val$msg;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass2(Object obj, String str2) {
                super(obj);
                str = str2;
            }

            /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x0093 -> B:29:0x009b). Please report as a decompilation issue!!! */
            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                try {
                    if (str.startsWith("file")) {
                        Loger.writeLog("UI", "收到文件指令：" + str);
                        try {
                            String[] split = str.split(":");
                            String str2 = split[1];
                            if ("savescreenshotpicfile".equals(str2)) {
                                FileUploader.saveScreenShotPic((Activity) ShjManager.getActivityContext(), "savescreenshotpicfile");
                            } else if ("getallfilename".equals(str2)) {
                                FileUploader.copyAllFileNames("getallfilename");
                            } else if ("uploadfile".equals(str2)) {
                                if (split.length >= 3) {
                                    String str22 = split[2];
                                    File file = new File(str22);
                                    if (file.exists()) {
                                        FileUploader.uploadFile2Server(str22, "uploadfile", Shj.getMachineId(), file.getName(), new FileUploadListener() { // from class: com.shj.biz.tools.FileUploader.2.1
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
                                }
                            } else if ("deletefile".equals(str2) && split.length >= 3) {
                                File file2 = new File(split[2]);
                                if (file2.exists()) {
                                    SDFileUtils.safeDeleteFile(file2);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e2) {
                    Loger.writeException("UI", e2);
                }
            }

            /* renamed from: com.shj.biz.tools.FileUploader$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements FileUploadListener {
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
            }
        }).start();
    }

    /* renamed from: com.shj.biz.tools.FileUploader$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends RunnableEx {
        final /* synthetic */ String val$fileType;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Object obj, String str) {
            super(obj);
            str = str;
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            try {
                String recursiveFiles = FileUploader.recursiveFiles(SDFileUtils.SDCardRoot);
                File file = new File(SDFileUtils.SDCardRoot + "xyShj/allFileNames");
                if (!file.exists() && !file.isDirectory()) {
                    file.mkdir();
                }
                String str = new SimpleDateFormat("yyyy_MM_dd_").format(new Date(System.currentTimeMillis())) + Shj.getMachineId() + "_file_names.log";
                File file2 = new File(SDFileUtils.SDCardRoot + "xyShj/allFileNames", str);
                FileOutputStream fileOutputStream = null;
                try {
                    try {
                        if (!file2.exists()) {
                            file2.createNewFile();
                        }
                        byte[] bytes = recursiveFiles.getBytes();
                        FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
                        try {
                            fileOutputStream2.write(bytes);
                            Loger.writeLog("PUSH", "所有文件名日志文件已生成");
                            fileOutputStream2.close();
                        } catch (Exception e) {
                            e = e;
                            fileOutputStream = fileOutputStream2;
                            e.printStackTrace();
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            String str2 = SDFileUtils.SDCardRoot + "xyshj/allFileNames/" + str;
                            Loger.writeLog("PUSH", "准备上传所有文件名日志文件");
                            FileUploader.uploadMachineFile(str2, str, Shj.getMachineId(), str, new FileUploadListener() { // from class: com.shj.biz.tools.FileUploader.3.1
                                AnonymousClass1() {
                                }

                                @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                                public void onSecusses(String str3, Object obj) {
                                    Loger.writeLog("PUSH", "上传所有文件名日志文件成功");
                                }

                                @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                                public void onError(Object obj, String str3, Object obj2) {
                                    Loger.writeLog("PUSH", "上传所有文件名日志文件失败：" + obj);
                                }
                            }, 1);
                        } catch (Throwable th) {
                            th = th;
                            fileOutputStream = fileOutputStream2;
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            throw th;
                        }
                    } catch (Exception e2) {
                        e = e2;
                    }
                    String str22 = SDFileUtils.SDCardRoot + "xyshj/allFileNames/" + str;
                    Loger.writeLog("PUSH", "准备上传所有文件名日志文件");
                    FileUploader.uploadMachineFile(str22, str, Shj.getMachineId(), str, new FileUploadListener() { // from class: com.shj.biz.tools.FileUploader.3.1
                        AnonymousClass1() {
                        }

                        @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                        public void onSecusses(String str3, Object obj) {
                            Loger.writeLog("PUSH", "上传所有文件名日志文件成功");
                        }

                        @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                        public void onError(Object obj, String str3, Object obj2) {
                            Loger.writeLog("PUSH", "上传所有文件名日志文件失败：" + obj);
                        }
                    }, 1);
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e3) {
                Loger.writeException("UI", e3);
            }
        }

        /* renamed from: com.shj.biz.tools.FileUploader$3$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements FileUploadListener {
            AnonymousClass1() {
            }

            @Override // com.shj.biz.tools.FileUploader.FileUploadListener
            public void onSecusses(String str3, Object obj) {
                Loger.writeLog("PUSH", "上传所有文件名日志文件成功");
            }

            @Override // com.shj.biz.tools.FileUploader.FileUploadListener
            public void onError(Object obj, String str3, Object obj2) {
                Loger.writeLog("PUSH", "上传所有文件名日志文件失败：" + obj);
            }
        }
    }

    public static void copyAllFileNames(String str) {
        new Thread(new RunnableEx(null) { // from class: com.shj.biz.tools.FileUploader.3
            final /* synthetic */ String val$fileType;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass3(Object obj, String str2) {
                super(obj);
                str = str2;
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                try {
                    String recursiveFiles = FileUploader.recursiveFiles(SDFileUtils.SDCardRoot);
                    File file = new File(SDFileUtils.SDCardRoot + "xyShj/allFileNames");
                    if (!file.exists() && !file.isDirectory()) {
                        file.mkdir();
                    }
                    String str2 = new SimpleDateFormat("yyyy_MM_dd_").format(new Date(System.currentTimeMillis())) + Shj.getMachineId() + "_file_names.log";
                    File file2 = new File(SDFileUtils.SDCardRoot + "xyShj/allFileNames", str2);
                    FileOutputStream fileOutputStream = null;
                    try {
                        try {
                            if (!file2.exists()) {
                                file2.createNewFile();
                            }
                            byte[] bytes = recursiveFiles.getBytes();
                            FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
                            try {
                                fileOutputStream2.write(bytes);
                                Loger.writeLog("PUSH", "所有文件名日志文件已生成");
                                fileOutputStream2.close();
                            } catch (Exception e) {
                                e = e;
                                fileOutputStream = fileOutputStream2;
                                e.printStackTrace();
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                                String str22 = SDFileUtils.SDCardRoot + "xyshj/allFileNames/" + str2;
                                Loger.writeLog("PUSH", "准备上传所有文件名日志文件");
                                FileUploader.uploadMachineFile(str22, str, Shj.getMachineId(), str2, new FileUploadListener() { // from class: com.shj.biz.tools.FileUploader.3.1
                                    AnonymousClass1() {
                                    }

                                    @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                                    public void onSecusses(String str3, Object obj) {
                                        Loger.writeLog("PUSH", "上传所有文件名日志文件成功");
                                    }

                                    @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                                    public void onError(Object obj, String str3, Object obj2) {
                                        Loger.writeLog("PUSH", "上传所有文件名日志文件失败：" + obj);
                                    }
                                }, 1);
                            } catch (Throwable th) {
                                th = th;
                                fileOutputStream = fileOutputStream2;
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                                throw th;
                            }
                        } catch (Exception e2) {
                            e = e2;
                        }
                        String str222 = SDFileUtils.SDCardRoot + "xyshj/allFileNames/" + str2;
                        Loger.writeLog("PUSH", "准备上传所有文件名日志文件");
                        FileUploader.uploadMachineFile(str222, str, Shj.getMachineId(), str2, new FileUploadListener() { // from class: com.shj.biz.tools.FileUploader.3.1
                            AnonymousClass1() {
                            }

                            @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                            public void onSecusses(String str3, Object obj) {
                                Loger.writeLog("PUSH", "上传所有文件名日志文件成功");
                            }

                            @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                            public void onError(Object obj, String str3, Object obj2) {
                                Loger.writeLog("PUSH", "上传所有文件名日志文件失败：" + obj);
                            }
                        }, 1);
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Exception e3) {
                    Loger.writeException("UI", e3);
                }
            }

            /* renamed from: com.shj.biz.tools.FileUploader$3$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements FileUploadListener {
                AnonymousClass1() {
                }

                @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                public void onSecusses(String str3, Object obj) {
                    Loger.writeLog("PUSH", "上传所有文件名日志文件成功");
                }

                @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                public void onError(Object obj, String str3, Object obj2) {
                    Loger.writeLog("PUSH", "上传所有文件名日志文件失败：" + obj);
                }
            }
        }).start();
    }

    public static String recursiveFiles(String str) {
        File[] listFiles = new File(str).listFiles();
        String str2 = "";
        if (listFiles == null) {
            return "";
        }
        if (listFiles.length == 0) {
            Loger.writeLog("UI", str + "该文件夹下没有文件");
        }
        for (File file : listFiles) {
            if (file.isDirectory()) {
                Loger.writeLog("UI", "文件夹: " + file.getAbsolutePath());
                str2 = str2 + recursiveFiles(file.getAbsolutePath());
            } else if (file.isFile()) {
                String str3 = str2 + "文件: " + file.getAbsolutePath() + "大小：" + file.length() + StringUtils.LF;
                Loger.writeLog("UI", "文件: " + file.getAbsolutePath() + "大小：" + file.length());
                str2 = str3;
            } else {
                str2 = str2 + "文件: 未知错误文件\n";
                Loger.writeLog("UI", "未知错误文件");
            }
        }
        return str2;
    }

    public static String[] JSONAnalysis(String str) {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(str);
        } catch (Exception e) {
            e.printStackTrace();
            jSONObject = null;
        }
        return new String[]{jSONObject.optString("machineCode"), jSONObject.optString("startTime"), jSONObject.optString("overTime"), jSONObject.optInt("logeType") + ""};
    }

    public static void uploadFile(String str, String str2, String str3, FileUploadListener fileUploadListener, Object obj) {
        Loger.writeLog("PUSH", "开始上传日志文件");
        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            defaultHttpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
            String asString = CacheHelper.getFileCache().getAsString(ShjManager.URL_UPLOAD_FILE);
            if (asString == null) {
                asString = "http://114.55.54.35:9092/machinepush/receive/uploadLogeFile";
            }
            HttpPost httpPost = new HttpPost(asString);
            MultipartEntity multipartEntity = new MultipartEntity();
            File file = new File(str);
            multipartEntity.addPart("fileName", new StringBody(file.getName()));
            multipartEntity.addPart("file", new FileBody(file, "txt/plain"));
            multipartEntity.addPart("jsonStr", new StringBody("{\"machineCode\":\"" + str2 + "\",\"fileName\":\"" + str3 + "\"}", Charset.forName("UTF-8")));
            httpPost.setEntity(multipartEntity);
            HttpResponse execute = defaultHttpClient.execute(httpPost);
            InputStream content = execute.getEntity().getContent();
            int i = 0;
            int i2 = 0;
            while (i == 0) {
                i2++;
                if (i2 > 5120) {
                    break;
                }
                i = Integer.parseInt("" + execute.getEntity().getContentLength());
            }
            Loger.writeLog("PUSH", "后台返回数据 Count:" + i);
            byte[] bArr = new byte[i];
            for (int i3 = 0; i3 <= i && i3 != i; i3 += content.read(bArr, i3, i - i3)) {
            }
            fileUploadListener.onSecusses(new String(bArr), obj);
        } catch (Exception e) {
            fileUploadListener.onError(e, "", obj);
            Loger.writeException("PUSH", e);
        }
    }

    public static void uploadMachineFile(String str, String str2, String str3, String str4, FileUploadListener fileUploadListener, Object obj) {
        Loger.writeLog("PUSH", "开始上传日志文件");
        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            defaultHttpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
            String asString = CacheHelper.getFileCache().getAsString(ShjManager.URL_UPLOAD_FILE);
            if (asString == null) {
                asString = "http://114.55.54.35:9092/machinepush/receive/uploadFile";
            }
            HttpPost httpPost = new HttpPost(asString);
            MultipartEntity multipartEntity = new MultipartEntity();
            File file = new File(str);
            multipartEntity.addPart("fileName", new StringBody(file.getName()));
            multipartEntity.addPart("file", new FileBody(file, "txt/plain"));
            multipartEntity.addPart("jsonStr", new StringBody("{\"machineCode\":\"" + str3 + "\",\"fileName\":\"" + str4 + "\",\"fileType\":\"" + str2 + "\" }", Charset.forName("UTF-8")));
            httpPost.setEntity(multipartEntity);
            HttpResponse execute = defaultHttpClient.execute(httpPost);
            InputStream content = execute.getEntity().getContent();
            int i = 0;
            int i2 = 0;
            while (i == 0) {
                i2++;
                if (i2 > 5120) {
                    break;
                }
                i = Integer.parseInt("" + execute.getEntity().getContentLength());
            }
            Loger.writeLog("PUSH", "后台返回数据 Count:" + i);
            byte[] bArr = new byte[i];
            for (int i3 = 0; i3 <= i && i3 != i; i3 += content.read(bArr, i3, i - i3)) {
            }
            fileUploadListener.onSecusses(new String(bArr), obj);
        } catch (Exception e) {
            fileUploadListener.onError(e, "", obj);
            Loger.writeException("PUSH", e);
        }
    }

    /* renamed from: com.shj.biz.tools.FileUploader$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements Runnable {
        final /* synthetic */ String val$fileName;
        final /* synthetic */ String val$fileType;
        final /* synthetic */ FileUploadListener val$listener;
        final /* synthetic */ String val$machineCode;
        final /* synthetic */ String val$path;

        AnonymousClass4(String str, String str2, String str3, String str4, FileUploadListener fileUploadListener) {
            str = str;
            str2 = str2;
            str3 = str3;
            str4 = str4;
            fileUploadListener = fileUploadListener;
        }

        @Override // java.lang.Runnable
        public void run() {
            FileUploader.uploadMachineFile(str, str2, str3, str4, fileUploadListener, 1);
        }
    }

    public static void uploadFile2Server(String str, String str2, String str3, String str4, FileUploadListener fileUploadListener) {
        new Thread(new Runnable() { // from class: com.shj.biz.tools.FileUploader.4
            final /* synthetic */ String val$fileName;
            final /* synthetic */ String val$fileType;
            final /* synthetic */ FileUploadListener val$listener;
            final /* synthetic */ String val$machineCode;
            final /* synthetic */ String val$path;

            AnonymousClass4(String str5, String str22, String str32, String str42, FileUploadListener fileUploadListener2) {
                str = str5;
                str2 = str22;
                str3 = str32;
                str4 = str42;
                fileUploadListener = fileUploadListener2;
            }

            @Override // java.lang.Runnable
            public void run() {
                FileUploader.uploadMachineFile(str, str2, str3, str4, fileUploadListener, 1);
            }
        }).start();
    }

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
            uploadFile2Server(str3, str, Shj.getMachineId(), file2.getName(), new FileUploadListener() { // from class: com.shj.biz.tools.FileUploader.5
                AnonymousClass5() {
                }

                @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                public void onSecusses(String str4, Object obj) {
                    Loger.writeLog("PUSH", "上传文件成功");
                }

                @Override // com.shj.biz.tools.FileUploader.FileUploadListener
                public void onError(Object obj, String str4, Object obj2) {
                    Loger.writeLog("PUSH", "上传文件失败：" + obj);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: com.shj.biz.tools.FileUploader$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements FileUploadListener {
        AnonymousClass5() {
        }

        @Override // com.shj.biz.tools.FileUploader.FileUploadListener
        public void onSecusses(String str4, Object obj) {
            Loger.writeLog("PUSH", "上传文件成功");
        }

        @Override // com.shj.biz.tools.FileUploader.FileUploadListener
        public void onError(Object obj, String str4, Object obj2) {
            Loger.writeLog("PUSH", "上传文件失败：" + obj);
        }
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

    public static boolean isEmptyBitmap(Bitmap bitmap) {
        return bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0;
    }
}
