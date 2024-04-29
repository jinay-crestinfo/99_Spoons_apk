package com.oysb.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
//import com.iflytek.cloud.SpeechEvent;
//import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.io.file.SDFileUtils;
//import com.tencent.wxpayface.WxfacePayCommonCode;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.SocketClient;

/* loaded from: classes.dex */
public class Loger {
    public static final String InfoType_Error = "Error";
    public static final String InfoType_Info = "Info";
    public static final String InfoType_Warning = "Warning";
    public static final String OYSB_LOG_ADD_LOG = "OYSB_LOG_ADD_LOG";
    public static final String OYSB_LOG_NEED_LOG = "OYSB_LOG_NEED_LOG";
    private static Context context;
    private static AppStatusLoger dbHelper;
    private static Map<String, LogItem> map = new HashMap();
    private static Map<String, StringBuilder> logs = new HashMap();
    static long lastTime = 0;
    static boolean sendBroadcast = false;
    static Date date = new Date();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public static void setSendBroadcast(boolean z) {
        sendBroadcast = z;
    }

    public static boolean getSendBroadcast() {
        return sendBroadcast;
    }

    /* loaded from: classes2.dex */
    public static class LogItem {
        public String file;
        public int flushSize;
        public String path;
        public String prefix = "";
        public int step = 0;
        public long timeOut;

        LogItem(String str, String str2, long j, int i) {
            this.path = str;
            this.file = str2;
            this.timeOut = j;
            this.flushSize = i;
        }
    }

    public static Map<String, LogItem> getLogSetting() {
        return map;
    }

    public static void init(Context context2) {
        context = context2;
        rigisterBroadcast();
    }

    public static void setLogFile(String str, String str2, String str3, long j, int i) {
        map.put(str, new LogItem(str2, str3, j, i));
    }

    public static void checkLogs() {
        for (LogItem logItem : map.values()) {
            for (File file : SDFileUtils.getFiles(SDFileUtils.SDCardRoot + logItem.path, "log")) {
                String name = file.getName();
                if (name.length() > 12 && name.substring(8).contains(logItem.file) && file.lastModified() + logItem.timeOut < System.currentTimeMillis()) {
                    writeLog("APP;LOG", "delete file at checkLogs:" + file.getAbsolutePath());
                    SDFileUtils.safeDeleteFile(file);
                }
            }
        }
    }

    public static void writeLogEx(String str, String str2, String str3) {
        SDFileUtils.writeToSDFromInput(str, str2, str3, true);
    }

    public static synchronized void writeLog(String str, String str2) {
        StringBuilder sb;
        synchronized (Loger.class) {
            try {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                }
                if (date == null) {
                    date = new Date();
                }
                date.setTime(System.currentTimeMillis());
                String format = dateFormat.format(date);
//                for (String str3 : str.split(VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                for (String str3 : str.split("")) {
                    if (str3.length() != 0) {
                        LogItem logItem = map.get(str3);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(format);
                        sb2.append("(");
                        sb2.append(Thread.currentThread().getId());
                        sb2.append(") ");
                        sb2.append(logItem == null ? "" : logItem.prefix);
                        sb2.append(str2);
                        String sb3 = sb2.toString();
                        if (sendBroadcast) {
                            sendAddLogBroadcast(str3, sb3);
                        }
                        if (logItem != null && logItem.timeOut > 0) {
                            if (logItem.flushSize == 0) {
                                SDFileUtils.writeToSDFromInput(logItem.path, format.substring(0, 10).replace("-", "") + logItem.file, sb3 + SocketClient.NETASCII_EOL, true);
                                return;
                            }
                            if (logs.containsKey(str3)) {
                                sb = logs.get(str3);
                            } else {
                                sb = new StringBuilder();
                                logs.put(str3, sb);
                            }
                            sb.append(sb3);
                            sb.append(SocketClient.NETASCII_EOL);
                            if (sb.length() > logItem.flushSize) {
                                SDFileUtils.writeToSDFromInput(logItem.path, format.substring(0, 10).replace("-", "") + logItem.file, sb.toString(), true);
                                logs.put(str3, new StringBuilder());
                            }
                        }
                    }
                }
            } catch (Exception unused) {
            }
        }
    }

    public static void writeException(String str, Throwable th) {
        try {
            writeLog(str, th.getMessage());
            StringBuffer stringBuffer = new StringBuffer();
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                stringBuffer.append(stackTraceElement.toString() + StringUtils.LF);
            }
            writeLog(str, stringBuffer.toString());
        } catch (Exception unused) {
        }
    }

    public static void flush() {
        try {
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            }
            String replace = dateFormat.format(new Date()).substring(0, 10).replace("-", "");
            for (String str : logs.keySet()) {
                LogItem logItem = map.get(str);
                StringBuilder sb = logs.get(str);
                SDFileUtils.writeToSDFromInput(logItem.path, replace + logItem.file, sb.toString(), true);
            }
            logs.clear();
        } catch (Exception unused) {
        }
    }

    public static void sendAddLogBroadcast(String str, String str2) {
        Intent intent = new Intent(OYSB_LOG_ADD_LOG);
        Bundle bundle = new Bundle();
        bundle.putSerializable("title", str);
        bundle.putSerializable("log", str2);
//        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        context.sendBroadcast(intent);
    }

    private static void rigisterBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OYSB_LOG_NEED_LOG);
        context.registerReceiver(new MyBroadcastReceiver(), intentFilter);
    }

    /* loaded from: classes2.dex */
    public static class MyBroadcastReceiver extends BroadcastReceiver {
        MyBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Loger.lastTime = System.currentTimeMillis();
        }
    }

    public static void in_class_method(String str, Class cls, String str2) {
        writeLog(str, "into [" + cls.getSimpleName() + "]" + str2);
        LogItem logItem = map.get(str);
        logItem.step = logItem.step + 1;
        updatePrefix(str);
    }

    public static void out_class_method(String str, Class cls, String str2) {
        writeLog(str, "outoff [" + cls.getSimpleName() + "]" + str2);
        LogItem logItem = map.get(str);
        logItem.step = logItem.step + (-1);
        updatePrefix(str);
    }

    static void updatePrefix(String str) {
        LogItem logItem = map.get(str);
        if (logItem.step == 0) {
            logItem.prefix = "";
            return;
        }
        if (logItem.step == 1) {
            logItem.prefix = "--->";
            return;
        }
        for (int i = 1; i < logItem.step; i++) {
            logItem.prefix = "----" + logItem.prefix;
        }
    }

    public static void safe_inner_exception_catch(Exception exc) {
        writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, exc);
    }
}
