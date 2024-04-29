package com.oysb.utils;

import android.text.TextUtils;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.io.file.SDFileUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.net.SocketClient;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class NetLoger {
    public static final String LOG_APP_REBOOT_COUNT = "app_reboot_count";
    public static final String LOG_APP_REBOOT_SET = "app_reboot_set";
    public static final String LOG_NETWORKING_MODE = "networking_mode";
    public static final String LOG_NET_MODULE_STATUS_RECORD = "net_module_status_record";
    public static final String LOG_NET_OPERATORS = "net_operators";
    public static final String LOG_PASSWORD_ISRIGHT = "password_isright";
    public static final String LOG_REBOOT_MODULE_SET = "reboot_module_set";
    public static final String LOG_SAFEAPP_ISOPEN = "safeapp_isopen";
    public static final String LOG_SCREEN_TOUCH = "screen_touch";
    public static final String LOG_SIGNAL_RECORD = "signal_record";
    public static final String LOG_VISIT_BAIDU_RECORD = "visit_baidu_record";
    public static final String LOG_VISIT_BAIDU_TEST = "visit_baidu_test";
    public static final String LOG_WIFI_ISCONFLICT = "wifi_isconflict";
    private static NetAnalysisData currentNetAnalysisItem;
    public static boolean isInit;
    private static boolean isTcpConnect;
    public static final String LogDir = "xyShj/netLog";
    public static final String logPath = SDFileUtils.SDCardRoot + LogDir;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS");
    private static Date date = new Date();

    public static void init() {
        isInit = true;
        File file = new File(logPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        deleteOverdueFile(file);
    }

    public static void deleteOverdueFile(File file) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            for (File file2 : file.listFiles()) {
                if (currentTimeMillis - file2.lastModified() > 604800000) {
                    file2.delete();
                }
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }

    public static String getStrDate() {
        Date date2 = new Date();
        date2.setTime(System.currentTimeMillis());
        return dateFormat.format(date2);
    }

    public static void addVisitBaiduRecordLogs(String str, boolean z) {
        if (isTcpConnect) {
            return;
        }
        NetAnalysisData netAnalysisData = currentNetAnalysisItem;
        if (netAnalysisData != null) {
            if (!z) {
                netAnalysisData.visitBaiduFailCount++;
            } else {
                netAnalysisData.visitBaiduSuccessCount++;
            }
            CacheHelper.getFileCache().put("NetAnalysisItem", currentNetAnalysisItem.write2Json());
        }
        addLogs(LOG_VISIT_BAIDU_RECORD, str);
    }

    public static void addLogs(String str, String str2) {
        if (isTcpConnect) {
            return;
        }
        if (!isInit) {
            init();
        }
        try {
            if (currentNetAnalysisItem != null) {
                if (LOG_SCREEN_TOUCH.equalsIgnoreCase(str)) {
                    currentNetAnalysisItem.touchCount++;
                    CacheHelper.getFileCache().put("NetAnalysisItem", currentNetAnalysisItem.write2Json());
                } else if (LOG_PASSWORD_ISRIGHT.equalsIgnoreCase(str)) {
                    currentNetAnalysisItem.passwordIsRight = str2;
                    CacheHelper.getFileCache().put("NetAnalysisItem", currentNetAnalysisItem.write2Json());
                }
                if (date == null) {
                    date = new Date();
                }
                date.setTime(System.currentTimeMillis());
                String format = dateFormat.format(date);
                if (!LOG_APP_REBOOT_COUNT.equalsIgnoreCase(str)) {
                    str2 = format + "(" + Thread.currentThread().getId() + ") " + str2;
                }
                SDFileUtils.writeToSDFromInput(LogDir, currentNetAnalysisItem.startTime + "_" + str + ".log", str2 + SocketClient.NETASCII_EOL, true);
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }

    public static void addTcpDisconnectLog() {
        isTcpConnect = false;
        try {
            String asString = CacheHelper.getFileCache().getAsString("NetAnalysisItem");
            if (TextUtils.isEmpty(asString)) {
                NetAnalysisData netAnalysisData = new NetAnalysisData();
                currentNetAnalysisItem = netAnalysisData;
                netAnalysisData.startTime = getStrDate();
                if (!TextUtils.isEmpty(CacheHelper.getFileCache().getAsString("NetLogerOpenAppTime"))) {
                    CacheHelper.getFileCache().remove("NetLogerOpenAppTime");
                }
                CacheHelper.getFileCache().put("NetAnalysisItem", currentNetAnalysisItem.write2Json());
                return;
            }
            currentNetAnalysisItem = NetAnalysisData.CreateFromJson(asString);
            String asString2 = CacheHelper.getFileCache().getAsString("NetLogerOpenAppTime");
            if (TextUtils.isEmpty(asString2)) {
                return;
            }
            currentNetAnalysisItem.rebootCount++;
            CacheHelper.getFileCache().put("NetAnalysisItem", currentNetAnalysisItem.write2Json());
            CacheHelper.getFileCache().remove("NetLogerOpenAppTime");
            String str = "app启动时间：" + asString2;
            addLogs(LOG_APP_REBOOT_COUNT, str + ",系统启动时间：" + CacheHelper.getFileCache().getAsString("NetLogerBootCompleteTime"));
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }

    public static void addTcpConnectLog() {
        isTcpConnect = true;
        try {
            String asString = CacheHelper.getFileCache().getAsString("NetAnalysisItem");
            if (TextUtils.isEmpty(asString)) {
                return;
            }
            NetAnalysisData CreateFromJson = NetAnalysisData.CreateFromJson(asString);
            CreateFromJson.endTime = getStrDate();
            SDFileUtils.writeToSDFromInput(LogDir, CreateFromJson.startTime + "_statistics.log", CreateFromJson.write2Json(), false);
            CacheHelper.getFileCache().remove("NetAnalysisItem");
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }

    /* loaded from: classes2.dex */
    public static class NetAnalysisData {
        public String endTime;
        public String passwordIsRight;
        public int rebootCount;
        public String startTime;
        public String systemRebootTime;
        public int touchCount;
        public int visitBaiduFailCount;
        public int visitBaiduSuccessCount;

        public String write2Json() {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("startTime", this.startTime);
                jSONObject.put("endTime", this.endTime);
                jSONObject.put("systemRebootTime", this.systemRebootTime);
                jSONObject.put("visitBaiduSuccessCount", this.visitBaiduSuccessCount);
                jSONObject.put("visitBaiduFailCount", this.visitBaiduFailCount);
                jSONObject.put("rebootCount", this.rebootCount);
                jSONObject.put("touchCount", this.touchCount);
                jSONObject.put("passwordIsRight", this.passwordIsRight);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jSONObject.toString();
        }

        public static NetAnalysisData CreateFromJson(String str) {
            NetAnalysisData netAnalysisData = new NetAnalysisData();
            try {
                JSONObject jSONObject = new JSONObject(str);
                netAnalysisData.startTime = jSONObject.optString("startTime");
                netAnalysisData.endTime = jSONObject.optString("endTime");
                netAnalysisData.systemRebootTime = jSONObject.optString("systemRebootTime");
                netAnalysisData.visitBaiduSuccessCount = jSONObject.optInt("visitBaiduSuccessCount");
                netAnalysisData.visitBaiduFailCount = jSONObject.optInt("visitBaiduFailCount");
                netAnalysisData.rebootCount = jSONObject.optInt("rebootCount");
                netAnalysisData.touchCount = jSONObject.optInt("touchCount");
                netAnalysisData.passwordIsRight = jSONObject.optString("passwordIsRight");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return netAnalysisData;
        }
    }
}
