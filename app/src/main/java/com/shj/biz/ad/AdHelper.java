package com.shj.biz.ad;

import com.alipay.api.AlipayConstants;
import com.github.mjdev.libaums.fs.UsbFile;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.oysb.utils.Loger;
import com.oysb.utils.ftp.FTPManager;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.Shj;
import com.tencent.wxpayface.WxfacePayCommonCode;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class AdHelper {
    static JSONArray adPropeties;
    static JSONObject jsonAvFilesInfo;
    static RequestItem queryAdInfoRequest;
    static ArrayList<DownloadFtpItem> toDownloadItems = new ArrayList<>();
    static boolean isServiceRunning = false;

    public static void checkAvFiles() {
    }

    public static void reportAppInfo(String str, String str2, int i, JSONArray jSONArray) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("terminalVersion", str);
            jSONObject.put("terminalType", i);
            jSONObject.put("terminalId", str2);
            jSONObject.put("adList", jSONArray);
            RequestParams requestParams = new RequestParams();
            requestParams.add(AlipayConstants.FORMAT_JSON, jSONObject.toString());
            RequestItem requestItem = new RequestItem("", requestParams, HttpGet.METHOD_NAME);
            requestItem.setRepeatDelay(60000);
            requestItem.setRequestMaxCount(Integer.MAX_VALUE);
            requestItem.setLostAble(true);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.ad.AdHelper.1
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                AnonymousClass1() {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i2, String str3, Throwable th) {
                    Loger.writeLog("广告", "上传广告位信息失败:" + i2 + StringUtils.SPACE + str3);
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i2, String str3) {
                    try {
                        String replace = str3.replace("\"[", "[").replace("]\"", "]").replace("\\", "");
                        JSONObject jSONObject2 = new JSONObject(replace);
                        if (!jSONObject2.has("code") || !jSONObject2.getString("code").equals("1")) {
                            return false;
                        }
                        Loger.writeLog("广告", "上传广告位信息成功:" + i2 + StringUtils.SPACE + replace);
                        return true;
                    } catch (Exception unused) {
                        return false;
                    }
                }
            });
            RequestHelper.request(requestItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: com.shj.biz.ad.AdHelper$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass1() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i2, String str3, Throwable th) {
            Loger.writeLog("广告", "上传广告位信息失败:" + i2 + StringUtils.SPACE + str3);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i2, String str3) {
            try {
                String replace = str3.replace("\"[", "[").replace("]\"", "]").replace("\\", "");
                JSONObject jSONObject2 = new JSONObject(replace);
                if (!jSONObject2.has("code") || !jSONObject2.getString("code").equals("1")) {
                    return false;
                }
                Loger.writeLog("广告", "上传广告位信息成功:" + i2 + StringUtils.SPACE + replace);
                return true;
            } catch (Exception unused) {
                return false;
            }
        }
    }

    public static void queryAdInfo() {
        queryAdInfo(null);
    }

    public static void queryAdInfo(JSONArray jSONArray) {
        adPropeties = jSONArray;
        try {
            RequestItem requestItem = queryAdInfoRequest;
            if (requestItem != null) {
                requestItem.setCanceled(true);
                queryAdInfoRequest = null;
            }
            RequestParams requestParams = new RequestParams();
            requestParams.add("jqbh", Shj.getMachineId());
            RequestItem requestItem2 = new RequestItem("", requestParams, HttpGet.METHOD_NAME);
            queryAdInfoRequest = requestItem2;
            requestItem2.setRepeatDelay(3600000);
            queryAdInfoRequest.setRequestMaxCount(Integer.MAX_VALUE);
            queryAdInfoRequest.setLostAble(true);
            queryAdInfoRequest.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.ad.AdHelper.2
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem3, boolean z) {
                }

                AnonymousClass2() {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem3, int i, String str, Throwable th) {
                    Loger.writeLog("广告", "查询广告位信息失败:" + i + StringUtils.SPACE + str);
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem3, int i, String str) {
                    try {
                        String replace = str.replace("\"[", "[").replace("]\"", "]").replace("\\", "");
                        JSONObject jSONObject = new JSONObject(replace);
                        if (jSONObject.has("code") && jSONObject.getString("code").equals("1")) {
                            Loger.writeLog("广告", "查询广告位信息成功:" + i + StringUtils.SPACE + replace);
                            AdHelper.jsonAvFilesInfo = RequestHelper.updateVersionedRequestResult(jSONObject, "getTerminalInfo");
                            Loger.writeLog("广告", AdHelper.jsonAvFilesInfo.toString());
                            AdHelper.checkAvFiles();
                            return false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            RequestHelper.request(queryAdInfoRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startAdDownloadService();
    }

    /* renamed from: com.shj.biz.ad.AdHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem3, boolean z) {
        }

        AnonymousClass2() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem3, int i, String str, Throwable th) {
            Loger.writeLog("广告", "查询广告位信息失败:" + i + StringUtils.SPACE + str);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem3, int i, String str) {
            try {
                String replace = str.replace("\"[", "[").replace("]\"", "]").replace("\\", "");
                JSONObject jSONObject = new JSONObject(replace);
                if (jSONObject.has("code") && jSONObject.getString("code").equals("1")) {
                    Loger.writeLog("广告", "查询广告位信息成功:" + i + StringUtils.SPACE + replace);
                    AdHelper.jsonAvFilesInfo = RequestHelper.updateVersionedRequestResult(jSONObject, "getTerminalInfo");
                    Loger.writeLog("广告", AdHelper.jsonAvFilesInfo.toString());
                    AdHelper.checkAvFiles();
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static void saveFtpFile(String str, int i, String str2, String str3, String str4, String str5) {
        FTPManager fTPManager = null;
        try {
            try {
                Loger.writeLog("广告", "正在下载广告:" + str4 + "..." + str5);
                FTPManager fTPManager2 = new FTPManager();
                try {
                    if (fTPManager2.connect(str, i, str2, str3) && fTPManager2.downloadFile(str4, str5)) {
                        Loger.writeLog("广告", "广告下载完成:" + str4 + "..." + str5);
                    }
                    fTPManager2.closeFTP();
                } catch (Exception e) {
                    e = e;
                    fTPManager = fTPManager2;
                    Loger.writeException("广告", e);
                    if (fTPManager != null) {
                        fTPManager.closeFTP();
                    }
                } catch (Throwable th) {
                    th = th;
                    fTPManager = fTPManager2;
                    if (fTPManager != null) {
                        fTPManager.closeFTP();
                    }
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* loaded from: classes2.dex */
    public static class DownloadFtpItem {
        private String ip;
        private boolean isDownloading = false;
        private int port;
        private String pwd;
        private String saveFolder;
        private String targeFileName;
        private String toDownloadFile;
        private String user;

        public DownloadFtpItem(String str, int i, String str2, String str3, String str4, String str5) {
            this.targeFileName = "";
            this.ip = str;
            this.port = i;
            this.user = str2;
            this.pwd = str3;
            if (!str4.endsWith(UsbFile.separator)) {
                str4 = str4 + UsbFile.separator;
            }
            this.saveFolder = str4;
            this.toDownloadFile = str5;
            int lastIndexOf = str5.lastIndexOf(UsbFile.separator);
            if (lastIndexOf != -1) {
                this.targeFileName = str5.substring(lastIndexOf + 1);
            } else {
                this.targeFileName = str5;
            }
        }
    }

    public static void addNeedDownloadItem(DownloadFtpItem downloadFtpItem) {
        boolean z;
        Iterator<DownloadFtpItem> it = toDownloadItems.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            DownloadFtpItem next = it.next();
            if (next.ip.equalsIgnoreCase(downloadFtpItem.ip) && next.port == downloadFtpItem.port && next.saveFolder.equalsIgnoreCase(downloadFtpItem.saveFolder) && next.toDownloadFile.equalsIgnoreCase(downloadFtpItem.toDownloadFile)) {
                next.user = downloadFtpItem.user;
                next.pwd = downloadFtpItem.pwd;
                z = true;
                break;
            }
        }
        if (z) {
            return;
        }
        toDownloadItems.add(downloadFtpItem);
    }

    public static void check2DeleteExpiredFiles() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Iterator<DownloadFtpItem> it = toDownloadItems.iterator();
        while (it.hasNext()) {
            DownloadFtpItem next = it.next();
            arrayList.add(next.saveFolder.toLowerCase() + next.targeFileName.toLowerCase());
            if (!arrayList2.contains(next.saveFolder)) {
                arrayList2.add(next.saveFolder);
            }
        }
        Iterator it2 = arrayList2.iterator();
        while (it2.hasNext()) {
            for (File file : SDFileUtils.getFiles((String) it2.next(), "")) {
                if (!file.isDirectory()) {
                    String replace = file.getAbsolutePath().toLowerCase().replace("_tmp", "");
                    if (!replace.substring(replace.lastIndexOf(UsbFile.separator) + 1).startsWith("_") && !arrayList.contains(replace)) {
                        try {
                            Loger.writeLog("APP", "delete file at check2DeleteExpiredFiles:" + replace);
                            SDFileUtils.safeDeleteFile(file);
                        } catch (Exception e) {
                            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                        }
                    }
                }
            }
        }
    }

    public static void startAdDownloadService() {
        if (isServiceRunning) {
            return;
        }
        isServiceRunning = true;
        new Thread(new Runnable() { // from class: com.shj.biz.ad.AdHelper.3
            AnonymousClass3() {
            }

            @Override // java.lang.Runnable
            public void run() {
                while (true) {
                    Loger.writeLog("FTP", "ftpDownloadService run next");
                    try {
                        int size = AdHelper.toDownloadItems.size();
                        for (int i = 0; i < size; i++) {
                            try {
                                DownloadFtpItem downloadFtpItem = AdHelper.toDownloadItems.get(i);
                                if (!downloadFtpItem.isDownloading) {
                                    if (!new File(downloadFtpItem.saveFolder + downloadFtpItem.targeFileName).exists()) {
                                        try {
                                            AdHelper.saveFtpFile(downloadFtpItem.ip, downloadFtpItem.port, downloadFtpItem.user, downloadFtpItem.pwd, downloadFtpItem.saveFolder, downloadFtpItem.toDownloadFile);
                                        } catch (Exception e) {
                                            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                                        }
                                        downloadFtpItem.isDownloading = false;
                                    }
                                }
                            } catch (Exception e2) {
                                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
                            }
                        }
                        Thread.sleep(60000L);
                    } catch (Exception e3) {
                        Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e3);
                    }
                }
            }
        }).start();
    }

    /* renamed from: com.shj.biz.ad.AdHelper$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                Loger.writeLog("FTP", "ftpDownloadService run next");
                try {
                    int size = AdHelper.toDownloadItems.size();
                    for (int i = 0; i < size; i++) {
                        try {
                            DownloadFtpItem downloadFtpItem = AdHelper.toDownloadItems.get(i);
                            if (!downloadFtpItem.isDownloading) {
                                if (!new File(downloadFtpItem.saveFolder + downloadFtpItem.targeFileName).exists()) {
                                    try {
                                        AdHelper.saveFtpFile(downloadFtpItem.ip, downloadFtpItem.port, downloadFtpItem.user, downloadFtpItem.pwd, downloadFtpItem.saveFolder, downloadFtpItem.toDownloadFile);
                                    } catch (Exception e) {
                                        Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                                    }
                                    downloadFtpItem.isDownloading = false;
                                }
                            }
                        } catch (Exception e2) {
                            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
                        }
                    }
                    Thread.sleep(60000L);
                } catch (Exception e3) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e3);
                }
            }
        }
    }
}
