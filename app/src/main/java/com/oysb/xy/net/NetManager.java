package com.oysb.xy.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.cache.ACache;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.date.DateUtil;
import com.oysb.xy.db.ReportDBHelper;
import com.oysb.xy.i.OfferCmdTransfResultListener;
import com.oysb.xy.i.OnlineCardPayResultListener;
import com.oysb.xy.i.OnlinePayApplyResultListener;
import com.oysb.xy.i.PickByCodeRequestListener;
import com.oysb.xy.i.PosTransfResultListener;
import com.oysb.xy.i.TransfDataResultListener;
import com.oysb.xy.net.report.Report;
import com.oysb.xy.net.report.ReportState;
import com.oysb.xy.net.report.Report_Con_HistoryDataFinished;
import com.oysb.xy.net.report.Report_Con_Shelves;
import com.oysb.xy.net.report.Report_Con_SingIn;
import com.oysb.xy.net.report.Report_Heartbeat;
import com.oysb.xy.net.report.Report_Trad_OfferGoods;
import com.oysb.xy.net.report.Report_Trad_OfferGoods_v2;
import com.oysb.xy.net.socket.SocketProcessor;
//import com.tencent.wxpayface.WxfacePayCommonCode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class NetManager {
    public static final int HANDLE_CANCEL_HEARTBEAT = 4000;
    public static final int HANDLE_TCP_INIT = 1000;
    private static final int SLOW_NET_SPEED = 1000;
    static Context context;
    static boolean creatingHeartBeatReport;
    static Report_Con_HistoryDataFinished historyReport;
    static OnMessageListener messageListener;
    static NetProcessor messageProcessor;
    private static OfferCmdTransfResultListener offerCmdTransfResultListener;
    private static OnlineCardPayResultListener onlineCardPayResultListener;
    private static OnlinePayApplyResultListener onlinePayApplyResultListener;
    static PickByCodeRequestListener pickCodeReportListener;
    private static PosTransfResultListener posTransfResultListener;
    static String pwd;
    static String reportDbFile;
    private static String serverIp;
    private static String serverPort;
    static Report_Con_SingIn singReport;
    static SocketProcessor socketProcessor;
    private static TransfDataResultListener transfDataResultListener;
    static String user;
    static Queue<Report> sendReportQueue = new LinkedList();
    static ReportDBHelper dbHelper = null;
    static Boolean isLogined = false;
    private static String connectError = "";
    static boolean inited = false;
    static int debugCount = 8;
    static int debugModel = 0;
    private static long lastHeartBeatTime = 0;
    private static int testHeartBeatCount = 0;
    private static int testHeartBeatCostTime = 0;
    private static int netSpeed = 0;
    private static boolean netSpeedSlow = false;
    private static boolean hasHistoryData = false;
    static Handler handler = new Handler() { // from class: com.oysb.xy.net.NetManager.1

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                int i = message.what;
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                e.printStackTrace();
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface OnMessageListener {
        void onAck(byte[] bArr, Report report, boolean z, int i);

        void onAckTimeOut(Report report);

        void onAppendReport(Report report);

        void onError(byte[] bArr);

        void onLogin();

        void onLoginFailed(String str);

        void onLoginOk(Date date);

        void onMessage(byte[] bArr);

        void onSendFailed(byte[] bArr, Report report, String str);

        void onSendReport(Report report);

        void onTcpConnected();

        void onTcpDisConnected(String str);

        void onTcpInit();

        boolean shuldSendHeartBeatOnTcpFree();
    }

    public static String getUser() {
        String str = user;
        if (str == null || str.length() == 0) {
            user = CacheHelper.getFileCache().getAsString("NETUSER");
        }
        return user;
    }

    public static String getPwd() {
        String str = pwd;
        if (str == null || str.length() == 0) {
            pwd = CacheHelper.getFileCache().getAsString("NETPWD");
        }
        String str2 = pwd;
        if (str2 == null || str2.length() == 0) {
            pwd = "000000";
        }
        return pwd;
    }

    public static void setDebugModel(int i) {
        debugModel = i;
    }

    public static int getDebugModel() {
        return debugModel;
    }

    public static boolean debugError() {
        int i = debugModel;
        if (i == 1) {
            int i2 = debugCount - 1;
            debugCount = i2;
            if (i2 <= 0) {
                debugCount = (int) (Math.random() * 8.0d);
                return true;
            }
        } else if (i == 2) {
            return true;
        }
        return false;
    }

    public static boolean isInited() {
        return inited;
    }

    public static SocketProcessor getSocketProcessor() {
        if (socketProcessor == null) {
            SocketProcessor socketProcessor2 = new SocketProcessor();
            socketProcessor = socketProcessor2;
            socketProcessor2.setMessageProcessor(messageProcessor);
        }
        return socketProcessor;
    }

    public static void setReportDBFile(String str) {
        reportDbFile = str;
        ReportDBHelper.setDatabaseFile(str);
    }

    /* renamed from: com.oysb.xy.net.NetManager$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                int i = message.what;
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                e.printStackTrace();
            }
        }
    }

    public static Queue<Report> getReportQueue() {
        return sendReportQueue;
    }

    public static long getLastHeartBeatTime() {
        return lastHeartBeatTime;
    }

    public static void setLastHeartBeatTime(long j) {
        lastHeartBeatTime = j;
    }

    public static int getTestHeartBeatCount() {
        return testHeartBeatCount;
    }

    public static void setTestHeartBeatCount(int i) {
        testHeartBeatCount = i;
    }

    public static int getNetSpeed() {
        return netSpeed;
    }

    public static void setNetSpeed(int i) {
        netSpeed = i;
    }

    public static TransfDataResultListener getTransfDataResultListener() {
        return transfDataResultListener;
    }

    public static void setTransfDataResultListener(TransfDataResultListener transfDataResultListener2) {
        transfDataResultListener = transfDataResultListener2;
    }

    public static String getConnectError() {
        return connectError;
    }

    public static void setConnectError(String str) {
        connectError = str;
    }

    public static void setUser(String str, String str2) {
        user = str;
        pwd = str2;
        CacheHelper.getFileCache().put("NETUSER", str);
        CacheHelper.getFileCache().put("NETPWD", str2);
    }

    public static void init(Context context2, String str, String str2) {
        Loger.writeLog("NET", "启动NetManger");
        user = str;
        pwd = str2;
        CacheHelper.getFileCache().put("NETUSER", str);
        CacheHelper.getFileCache().put("NETPWD", str2);
        context = context2;

        ReportDBHelper reportDBHelper = new ReportDBHelper(context2);
        dbHelper = reportDBHelper;
        reportDBHelper.deleteReportsOutDate(60);

        List<Report> unSendReports = dbHelper.getUnSendReports();
        ArrayList<String> arrayList = new ArrayList<>();

        for (int size = unSendReports.size() - 1; size >= 0; size--) {
            Report report = unSendReports.get(size);

            if (report instanceof Report_Con_Shelves) {
                String hexData = ObjectHelper.hex2String(report.getRawData());
                String substring = hexData.substring(12, hexData.length() - 3);

                if (arrayList.contains(substring)) {
                    report.setState(ReportState.Finished);
                    report.setSendTime(new Date());
                    report.setRetCode("0");
                    report.setRetMsg("0");
                    dbHelper.updateReportState(report);
                } else {
                    arrayList.add(substring);
                }
            } else if (report instanceof Report_Trad_OfferGoods || report instanceof Report_Trad_OfferGoods_v2) {
                arrayList.clear();
            }
        }

        for (Report report : dbHelper.getUnSendReports()) {
            Loger.writeLog("NET", "重发上次未完成发送命令" + report.getClass().getSimpleName());
            appendReportEx(report, false);
        }

        inited = true;
        NetProcessor netProcessor = new NetProcessor();
        messageProcessor = netProcessor;
        netProcessor.startMessageSendProcessor(context2);
    }


    public static void setOnMessageListener(OnMessageListener onMessageListener) {
        messageListener = onMessageListener;
    }

    public static Report getCurrentReport() {
        if (!isLogined.booleanValue()) {
            return singReport;
        }
        return sendReportQueue.peek();
    }

    public static boolean shuldSendReport(Report report) {
        synchronized (isLogined) {
            if (report != null) {
                if (report instanceof Report_Con_SingIn) {
                    return true;
                }
            }
            return isLogined.booleanValue();
        }
    }

    private static void appendReportEx(Report report, boolean z) {
        if (messageProcessor != null && (report.getDataType() & UByte.MAX_VALUE) == 23) {
            messageProcessor.cancelCurrentLostAbleReport();
        }
        report.setAppendTime(new Date());
        synchronized (sendReportQueue) {
            report.setState(ReportState.Waitting);
            sendReportQueue.add(report);
        }
        if (z && !(report instanceof Report_Heartbeat)) {
            dbHelper.addReport(report);
        }
        try {
            OnMessageListener onMessageListener = messageListener;
            if (onMessageListener != null) {
                onMessageListener.onAppendReport(report);
            }
        } catch (Exception e) {
            Loger.writeException("NET", e);
        }
    }

    public static void appendReport(Report report) {
        if (inited) {
            appendReportEx(report, (report.lostAble() || report.doNotStoreInDb()) ? false : true);
        }
    }

    public static void onSendReport(Report report) {
        report.setState(ReportState.Send);
        boolean z = report instanceof Report_Heartbeat;
        if (!z && !(report instanceof Report_Con_SingIn) && !report.lostAble()) {
            dbHelper.updateReportState(report);
        } else if (z) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastHeartBeatTime > 60000) {
                testHeartBeatCount = 1;
                testHeartBeatCostTime = 0;
                netSpeed = 0;
            } else {
                testHeartBeatCount++;
            }
            lastHeartBeatTime = currentTimeMillis;
        }
        try {
            OnMessageListener onMessageListener = messageListener;
            if (onMessageListener != null) {
                onMessageListener.onSendReport(report);
            }
        } catch (Exception e) {
            Loger.writeException("NET", e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:73:0x004d A[Catch: all -> 0x015e, TryCatch #5 {, blocks: (B:4:0x0003, B:6:0x000d, B:8:0x0011, B:10:0x0017, B:11:0x0061, B:13:0x0065, B:15:0x006b, B:58:0x00bd, B:60:0x00c1, B:17:0x00cc, B:51:0x00d4, B:53:0x00d8, B:19:0x00e4, B:22:0x00ea, B:24:0x00ef, B:27:0x0130, B:29:0x0134, B:32:0x0139, B:34:0x012d, B:36:0x013f, B:38:0x0143, B:41:0x0152, B:43:0x014c, B:45:0x0157, B:46:0x015c, B:56:0x00df, B:63:0x0071, B:65:0x0084, B:67:0x0020, B:69:0x002a, B:71:0x0049, B:73:0x004d, B:75:0x0054, B:77:0x005c, B:78:0x0033, B:80:0x0041), top: B:3:0x0003, inners: #0, #1, #3, #4 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean onAck(byte[] r10) {
        /*
            Method dump skipped, instructions count: 353
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.xy.net.NetManager.onAck(byte[]):boolean");
    }

    public static void onAckTimeOut(Report report) {
        ACache fileCache;
        StringBuilder sb;
        String str = "1";
        try {
            String asString = CacheHelper.getFileCache().getAsString("NET_TIMEOUT_INFO");
            String format = DateUtil.format(new Date(), "yyyy-MM-dd");
            try {
                String[] split = asString.split("&#&");
                if (split[0].equalsIgnoreCase(format)) {
                    str = "" + (Integer.parseInt(split[1]) + 1);
                }
                Loger.writeLog("WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR", "TIMEOUT:" + str);
                fileCache = CacheHelper.getFileCache();
                sb = new StringBuilder();
                sb.append(format);
                sb.append("&#&");
                sb.append(str);
            } catch (Exception unused) {
                Loger.writeLog("WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR", "TIMEOUT:1");
                fileCache = CacheHelper.getFileCache();
                sb = new StringBuilder();
                sb.append(format);
                sb.append("&#&");
                sb.append("1");
            } catch (Throwable th) {
                Loger.writeLog("WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR", "TIMEOUT:1");
                CacheHelper.getFileCache().put("NET_TIMEOUT_INFO", format + "&#&1");
                throw th;
            }
            fileCache.put("NET_TIMEOUT_INFO", sb.toString());
        } catch (Exception unused2) {
        }
        try {
            if (!isLogined.booleanValue()) {
                try {
                    OnMessageListener onMessageListener = messageListener;
                    if (onMessageListener != null) {
                        onMessageListener.onSendFailed(null, report, "timeOut");
                        return;
                    }
                    return;
                } catch (Exception e) {
                    Loger.writeException("NET", e);
                    return;
                }
            }
        } catch (Exception unused3) {
        }
        report.setState(ReportState.Failed);
        if (!(report instanceof Report_Heartbeat)) {
            dbHelper.updateReportState(report);
        }
        dbHelper.updateReportState(report);
        try {
            OnMessageListener onMessageListener2 = messageListener;
            if (onMessageListener2 != null) {
                onMessageListener2.onSendFailed(null, report, "timeOut");
            }
        } catch (Exception e2) {
            Loger.writeException("NET", e2);
        }
    }

    public static void onMessage(byte[] bArr) {
        try {
            OnMessageListener onMessageListener = messageListener;
            if (onMessageListener != null) {
                onMessageListener.onMessage(bArr);
            }
        } catch (Exception e) {
            Loger.writeException("NET", e);
        }
    }

    public static boolean isLogined() {
        return isLogined.booleanValue();
    }

    public static void onConnected() {
        try {
            OnMessageListener onMessageListener = messageListener;
            if (onMessageListener != null) {
                onMessageListener.onTcpConnected();
            }
        } catch (Exception e) {
            Loger.writeException("NET", e);
        }
        if (user == null || pwd == null) {
            user = CacheHelper.getFileCache().getAsString("NETUSER");
            pwd = CacheHelper.getFileCache().getAsString("NETPWD");
        }
        login(user, pwd);
    }

    public static void onTcpDisConnect(String str) {
        getCurrentReport();
        synchronized (isLogined) {
            isLogined = false;
        }
        try {
            OnMessageListener onMessageListener = messageListener;
            if (onMessageListener != null) {
                onMessageListener.onTcpDisConnected(str);
            }
        } catch (Exception e) {
            Loger.writeException("NET", e);
        }
    }

    public static void onError(byte[] bArr) {
        getCurrentReport();
        try {
            OnMessageListener onMessageListener = messageListener;
            if (onMessageListener != null) {
                onMessageListener.onError(bArr);
            }
        } catch (Exception e) {
            Loger.writeException("NET", e);
        }
    }

    public static void login(String str, String str2) {
        Loger.writeLog("NET", "正在登陆...");
        synchronized (isLogined) {
            isLogined = false;
            if (singReport == null) {
                user = str;
                pwd = str2;
                singReport = new Report_Con_SingIn();
            }
            hasHistoryData = true;
            singReport.setParams(str, str2, "0008");
            singReport.setState(ReportState.Waitting);
            messageProcessor.sendReport(singReport);
        }
        try {
            OnMessageListener onMessageListener = messageListener;
            if (onMessageListener != null) {
                onMessageListener.onLogin();
            }
        } catch (Exception e) {
            Loger.writeException("NET", e);
        }
    }

    public static void onNeedHeartBeat() {
        if (isLogined.booleanValue()) {
            if (hasHistoryData) {
                Report_Con_HistoryDataFinished report_Con_HistoryDataFinished = new Report_Con_HistoryDataFinished();
                historyReport = report_Con_HistoryDataFinished;
                appendReport(report_Con_HistoryDataFinished);
                return;
            }
            try {
                OnMessageListener onMessageListener = messageListener;
                if (onMessageListener != null) {
                    if (!onMessageListener.shuldSendHeartBeatOnTcpFree()) {
                        return;
                    }
                }
            } catch (Exception e) {
                Loger.writeException("NET", e);
            }
            if (sendReportQueue.peek() instanceof Report_Heartbeat) {
                return;
            }
            appendReport(new Report_Heartbeat());
        }
    }

    public static PosTransfResultListener getPosTransfResultListener() {
        return posTransfResultListener;
    }

    public static void setPosTransfResultListener(PosTransfResultListener posTransfResultListener2) {
        posTransfResultListener = posTransfResultListener2;
    }

    public static void setPickCodeReportListener(PickByCodeRequestListener pickByCodeRequestListener) {
        pickCodeReportListener = pickByCodeRequestListener;
    }

    public static PickByCodeRequestListener getPickCodeReportListener() {
        return pickCodeReportListener;
    }

    public static OnlineCardPayResultListener getOnlineCardPayResultListener() {
        return onlineCardPayResultListener;
    }

    public static void setOnlineCardPayResultListener(OnlineCardPayResultListener onlineCardPayResultListener2) {
        onlineCardPayResultListener = onlineCardPayResultListener2;
    }

    public static OfferCmdTransfResultListener getOfferCmdTransfResultListener() {
        return offerCmdTransfResultListener;
    }

    public static void setOfferCmdTransfResultListener(OfferCmdTransfResultListener offerCmdTransfResultListener2) {
        offerCmdTransfResultListener = offerCmdTransfResultListener2;
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static void setServerIp(String str) {
        serverIp = str;
    }

    public static String getServerPort() {
        return serverPort;
    }

    public static void setServerPort(String str) {
        serverPort = str;
    }

    public static OnlinePayApplyResultListener getOnlinePayApplyResultListener() {
        return onlinePayApplyResultListener;
    }

    public static void setOnlinePayApplyResultListener(OnlinePayApplyResultListener onlinePayApplyResultListener2) {
        onlinePayApplyResultListener = onlinePayApplyResultListener2;
    }
}
