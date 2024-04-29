package com.shj.biz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.media.ExifInterface;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alipay.zoloz.smile2pay.service.Zoloz;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.AndroidSystem;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Config;
import com.oysb.utils.Constant;
import com.oysb.utils.Loger;
import com.oysb.utils.MD5;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.PhoneHelper;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.activity.ActivityHelper;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.dialog.StepProgressDialog;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.image.ImageUtils;
import com.oysb.utils.io.AppUpdateHelper;
import com.oysb.utils.io.file.SDFileUtils;
import com.oysb.utils.video.TTSManager;
import com.oysb.utils.video.VideoHelper;
import com.oysb.xy.i.OnlinePayApplyResultListener;
import com.oysb.xy.i.PickByCodeRequestListener;
import com.oysb.xy.net.NetManager;
import com.oysb.xy.net.report.Report_CmdAck;
import com.oysb.xy.net.report.Report_Con_QMachineId;
import com.oysb.xy.net.report.Report_OnlinePay_Apply;
import com.oysb.xy.net.report.Report_Pay_PickByCode_Request;
import com.oysb.xy.net.report.Report_Transfdata;
import com.shj.FrontCommandFinishedListener;
import com.shj.MoneyType;
import com.shj.OfferState;
import com.shj.OnCommandAnswerListener;
import com.shj.ShelfInfo;
import com.shj.ShelfType;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.biz.goods.BatchOfferGoodsInfo;
import com.shj.biz.goods.Goods;
import com.shj.biz.goods.GoodsManager;
import com.shj.biz.goods.GoodsStatusListener;
import com.shj.biz.goods.SalesDBHelper;
import com.shj.biz.lfpos.ShjLfPosServerListener;
import com.shj.biz.lfpos.ShjPosTransfResultListener;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderListener;
import com.shj.biz.order.OrderManager;
import com.shj.biz.order.OrderPayItem;
import com.shj.biz.order.OrderPayItem_ICCard;
import com.shj.biz.order.OrderPayItem_LFPos;
import com.shj.biz.order.OrderPayItem_RFReader;
import com.shj.biz.order.OrderPayItem_XY_NetPay;
import com.shj.biz.order.OrderPayType;
import com.shj.biz.service.ShjBizServiceEx;
import com.shj.command.Command;
import com.shj.command.CommandError;
import com.shj.command.CommandManager;
import com.shj.command.CommandStatusListener;
import com.shj.command.Command_Up_ACK;
import com.shj.command.Command_Up_BatchEnd;
import com.shj.command.Command_Up_BatchStart;
import com.shj.command.Command_Up_ClearShelfBlock;
import com.shj.command.Command_Up_DoCharge;
import com.shj.command.Command_Up_DriverGoodsShelf;
import com.shj.command.Command_Up_EnablePosFindCard;
import com.shj.command.Command_Up_Reset;
import com.shj.command.Command_Up_SelectGoods;
import com.shj.command.Command_Up_SetCoinType;
import com.shj.command.Command_Up_SetMoney;
import com.shj.command.Command_Up_SetOfferGoodsCheck;
import com.shj.command.Command_Up_SetPaperType;
import com.shj.command.Command_Up_SetShelfCapacity;
import com.shj.command.Command_Up_SetShelfGoodsCode;
import com.shj.command.Command_Up_SetShelfGoodsCount;
import com.shj.command.Command_Up_SetShelfGoodsPrice;
import com.shj.command.Command_Up_TestGoodsIsValid;
import com.shj.command.Command_Up_TestGoodsShelfIsExist;
import com.shj.command.Command_Up_TopUp;
import com.shj.command.Command_Up_UnSelectGoods;
import com.shj.command.Command_Up_UnablePosFindCard;
import com.shj.commandV2.CommandV2_Up_BatchEnd;
import com.shj.commandV2.CommandV2_Up_Change;
import com.shj.commandV2.CommandV2_Up_CheckOfferDevices;
import com.shj.commandV2.CommandV2_Up_ControlLight;
import com.shj.commandV2.CommandV2_Up_DriverShelf;
import com.shj.commandV2.CommandV2_Up_Empty;
import com.shj.commandV2.CommandV2_Up_Pay;
import com.shj.commandV2.CommandV2_Up_PollTime;
import com.shj.commandV2.CommandV2_Up_QueryICCardInfo;
import com.shj.commandV2.CommandV2_Up_SelectGoods;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.commandV2.CommandV2_Up_SetMoney;
import com.shj.commandV2.CommandV2_Up_SetPollTime;
import com.shj.commandV2.CommandV2_Up_SetShelfCapacity;
import com.shj.commandV2.CommandV2_Up_SetShelfGoodsCode;
import com.shj.commandV2.CommandV2_Up_SetShelfGoodsCount;
import com.shj.commandV2.CommandV2_Up_SetShelfPrice;
import com.shj.commandV2.CommandV2_Up_Syn;
import com.shj.commandV2.CommandV2_Up_UnSelectGoods;
import com.shj.device.VMCStatus;
import com.shj.device.cardreader.ArmRfReader;
import com.shj.device.cardreader.JB_CardReader;
import com.shj.device.cardreader.MdbReader_BDT;
import com.shj.device.cardreader.WinCarcdReader;
import com.shj.device.lfpos.LfPos;
import com.shj.device.lfpos.service.PosService;
import com.shj.device.scanor.Scanor;
import com.shj.setting.NetAddress.NetAddress;
import com.tencent.wxpayface.WxfacePayCommonCode;
import com.xyshj.database.setting.SettingType;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.text.Typography;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Marker;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes2.dex */
public class ShjManager {
    public static final String CLINET_VERSION = "CLINET_VERSION";
    public static final String CONNECT_SERVER_RETRY_COUNT = "Net_Pwd_Get_failed";
    public static final String EAT_TIME = "EAT_TIME";
    public static final String ENABLE_PRICE_CHECK = "ENABLE_PRICE_CHECK";
    public static final String STR_offerGoods_failed = "STR_offerGoods_failed";
    public static final String STR_offerGoods_n = "STR_offerGoods_n";
    public static final String STR_offerGoods_timeOut = "STR_offerGoods_timeOut";
    public static final String STR_ready2OfferGoods = "STR_ready2OfferGoods";
    public static final String TTS_VOICE = "TTS_VOICE";
    public static final String URL_APP_BASE = "URL_APP_BASE";
    public static final String URL_SALE_APP_XY = "SALE_APP_XY";
    public static final String URL_UPLOAD_FILE = "UPLOAD_FILE";
    public static final String URL_XYNETWEB_BASE = "URL_XYNETWEB_BASE";
    public static final String VIDEO_VOICE = "VIDEO_VOICE";
    static Thread armRfReader_initThread;
    static int coinMState;
    static int coinMoney;
    static int doorState;
    private static double imageScale;
    static boolean isDebugLogined;
    static boolean isslxg;
    static long lastFindPeopleTime;
    static long lastTouchTime;
    static int paperMState;
    static int paperMoney;
    private static PhoneHelper phoneHelper;
    private static boolean queryingICCardInfo;
    private static long queryingICCardInfo_outtime;
    private static long queryingICCardInfo_time;
    private static ShjManager shjManager;
    static int temperature;
    static Timer touchTimer;
    private static VideoHelper videoHelper;
    static WeakReference<Context> wkActivityContext;
    static WeakReference<Context> wkAppContext;
    static GoodsManager goodsManager = new GoodsManager();
    static OrderManager orderManager = new OrderManager();
    static BizShjListener shjListener = new BizShjListener();
    static ShjOnlineCardPayResultListener myOnlineCardPayResultListener = new ShjOnlineCardPayResultListener();
    static ShjOfferCmdResultListener myOfferCmdResultListener = new ShjOfferCmdResultListener();
    static ShjTransfDataResultListener myShjTransfDataResultListener = new ShjTransfDataResultListener();
    static BatchOfferGoodsInfo batchOfferGoodsInfo = null;
    static MoneyListener moneyListener = null;
    static OrderListener orderListener = null;
    static GoodsStatusListener goodsStatusListener = null;
    static StatusListener statusListener = null;
    static List<OrderPayType> orderPayTypes = new ArrayList();
    static HashMap<String, Object> setting = new HashMap<>();
    static HashMap<String, Object> tmpData = new HashMap<>();
    private static boolean restartPerday = true;
    private static boolean isStarted = false;
    private static boolean debugNoLogin = false;
    private static boolean debugHasGoodsInPickdoor = false;
    private static boolean enableQrScanor = false;
    private static boolean enableWinCardReader = false;
    private static int pauseOnOfferGoodsCount = 10;
    private static int maxOfferPerGoodsTime = 150;
    private static String defDebugServerPort = "7088";
    private static boolean testNoOfferGoodsOnPay = false;
    private static boolean isShjLocked = false;
    private static Timer batchOfferTimer = null;
    private static long batchOfferTimerWaitStartTime = Long.MAX_VALUE;
    static int orderTimeOut = 60;
    static SalesDBHelper salesDBHelper = null;
    static OrderPayItem_ICCard icCardOrderPayItem = null;
    static Class yljhOrderPayItemClass = null;
    private static boolean icCardPaybyXyServer = true;
    private static boolean isYCCHOfferingGoods = false;
    private static boolean stopShjWhenOfferLineTooLong = false;
    private static boolean restartAppWhenOfferLineTooLong = false;
    static Queue<String> toOfferCmds = new LinkedList();
    private static boolean lightStatus = false;
    private static long netWorkWaringAmount = 10485760;
    static String appType = "-";
    public static String JQ_CONNECT_URL = null;
    static Handler handler = null;

    public static void reportError(String str, Throwable th) {
    }

    public static boolean requestNextBinUpdatePackge() {
        return false;
    }

    static {
        Config.initConfig(ShjManager.class, "shj.cfg");
        isslxg = false;
        armRfReader_initThread = null;
        lastTouchTime = 0L;
        lastFindPeopleTime = 0L;
        touchTimer = null;
        queryingICCardInfo = false;
        queryingICCardInfo_time = 0L;
        queryingICCardInfo_outtime = 0L;
        imageScale = 1.0d;
        isDebugLogined = true;
        paperMoney = 100;
        coinMoney = 100;
        temperature = 10;
        doorState = 0;
        paperMState = 0;
        coinMState = 0;
    }

    public static void setYljhOrderPayItemClass(Class cls) {
        yljhOrderPayItemClass = cls;
    }

    public static OrderPayItem getYljhOrderPayItem(OrderPayType orderPayType) {
        if (yljhOrderPayItemClass == null) {
            yljhOrderPayItemClass = OrderPayItem_XY_NetPay.class;
        }
        try {
            OrderPayItem orderPayItem = (OrderPayItem) yljhOrderPayItemClass.newInstance();
            orderPayItem.setOrderPayType(orderPayType);
            return orderPayItem;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setNetWorkWaringAmount(long j) {
        netWorkWaringAmount = j;
    }

    public static long getNetWorkWaringAmount() {
        return netWorkWaringAmount;
    }

    public static boolean isIsShjLocked() {
        return isShjLocked;
    }

    public static void setAppType(String str) {
        appType = str;
    }

    public static String getAppType() {
        return appType;
    }

    public static boolean isIsslxg() {
        return isslxg;
    }

    public static void setIsslxg(boolean z) {
        isslxg = z;
    }

    public static int getJhzfCode() {
        try {
            return Integer.parseInt(CacheHelper.getFileCache().getAsString("JHZF_CODE"));
        } catch (Exception unused) {
            return -1;
        }
    }

    public static void setJhzfCode(int i) {
        try {
            if (i != getJhzfCode()) {
                CacheHelper.getFileCache().put("JHZF_CODE", "" + i);
            }
        } catch (Exception unused) {
        }
    }

    public static void setPauseOnOfferGoodsCount(int i) {
        pauseOnOfferGoodsCount = i;
    }

    public static void setStopShjWhenOfferLineTooLong(boolean z) {
        stopShjWhenOfferLineTooLong = z;
    }

    public static boolean needStopShjWhenOfferLineTooLong() {
        return stopShjWhenOfferLineTooLong;
    }

    public static void setRestartAppWhenOfferLineTooLong(boolean z) {
        restartAppWhenOfferLineTooLong = z;
    }

    public static boolean needRestartAppWhenOfferLineTooLong() {
        return restartAppWhenOfferLineTooLong;
    }

    public static boolean isYCCHOfferingGoods() {
        return isYCCHOfferingGoods;
    }

    public static Queue<String> getOfferCmdQueue() {
        return toOfferCmds;
    }

    public static void tryOfferNextYCCmdGoods() {
        setYCCHOfferingGoods(false);
        if (getOfferCmdQueue().size() > 0) {
            getBizShjListener().onServerOfferGoodsCmd(0, getOfferCmdQueue().poll());
        }
    }

    public static void setYCCHOfferingGoods(boolean z) {
        isYCCHOfferingGoods = z;
    }

    public static SalesDBHelper getSalesDBHelper() {
        return salesDBHelper;
    }

    public static Context getAppContext() {
        WeakReference<Context> weakReference = wkAppContext;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public static void setIcCardOrderPayItem(OrderPayItem_ICCard orderPayItem_ICCard) {
        icCardOrderPayItem = orderPayItem_ICCard;
        if (orderPayItem_ICCard instanceof OrderPayItem_RFReader) {
            Thread thread = new Thread(new Runnable() { // from class: com.shj.biz.ShjManager.1
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ArmRfReader.findCom();
                    } catch (Exception unused) {
                    }
                    ShjManager.armRfReader_initThread = null;
                }
            });
            armRfReader_initThread = thread;
            thread.start();
        }
    }

    /* renamed from: com.shj.biz.ShjManager$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ArmRfReader.findCom();
            } catch (Exception unused) {
            }
            ShjManager.armRfReader_initThread = null;
        }
    }

    public static OrderPayItem_ICCard getIcCardOrderPayItem() {
        return icCardOrderPayItem;
    }

    public static void setRestartAppPerday(boolean z) {
        restartPerday = z;
    }

    public static int getOrderTimeOut() {
        return orderTimeOut;
    }

    public static void setOrderTimeOut(int i) {
        orderTimeOut = i;
    }

    public static PhoneHelper getPhoneHelper() {
        return phoneHelper;
    }

    public static void setActivityContext(Context context) {
        wkActivityContext = new WeakReference<>(context);
        StepProgressDialog.init(context);
    }

    public static Context getActivityContext() {
        try {
            return wkActivityContext.get();
        } catch (Exception unused) {
            return null;
        }
    }

    public static VideoHelper getVideoHelper() {
        if (videoHelper == null) {
            videoHelper = new VideoHelper(wkAppContext.get());
        }
        return videoHelper;
    }

    public static void LFPosReSign() {
        if (getOrderPayTypes().contains(OrderPayType.ECard) || getOrderPayTypes().contains(OrderPayType.ICCard)) {
            LfPos.Sign();
        }
    }

    public static boolean isGoodsChanged() {
        String asString = CacheHelper.getFileCache().getAsString("GOODSKEY_111");
        List<String> allGoodeCode = Shj.getAllGoodeCode();
        Collections.sort(allGoodeCode);
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = allGoodeCode.iterator();
        while (it.hasNext()) {
            sb.append(it.next() + VoiceWakeuperAidl.PARAMS_SEPARATE);
        }
        return !MD5.encode(sb.toString()).equals(asString);
    }

    public static ShjManager getInstance() {
        if (shjManager == null) {
            synchronized (ShjManager.class) {
                if (shjManager == null) {
                    shjManager = new ShjManager();
                }
            }
        }
        return shjManager;
    }

    public static void putSetting(String str, Object obj) {
        setting.put(str, obj);
    }

    public static Object getSetting(String str) {
        if (setting.containsKey(str)) {
            return setting.get(str);
        }
        return null;
    }

    public static void putData(String str, Object obj) {
        if (obj == null) {
            try {
                tmpData.remove(str);
            } catch (Exception unused) {
                return;
            }
        }
        tmpData.put(str, obj);
    }

    public static Object getData(String str) {
        if (tmpData.containsKey(str)) {
            return tmpData.get(str);
        }
        return null;
    }

    public static List<OrderPayType> getOrderPayTypes() {
        return orderPayTypes;
    }

    public static void addOrderPayType(OrderPayType orderPayType) {
        orderPayTypes.add(orderPayType);
    }

    public static boolean isBatchOfferingGoods() {
        Loger.writeLog("SHJ", "batchOfferGoodsInfo=" + batchOfferGoodsInfo);
        if (batchOfferGoodsInfo != null) {
            Loger.writeLog("SHJ", "getOfferedCount()=" + batchOfferGoodsInfo.getOfferedCount() + " getShelfs().size()=" + batchOfferGoodsInfo.getShelfs().size());
        }
        BatchOfferGoodsInfo batchOfferGoodsInfo2 = batchOfferGoodsInfo;
        return batchOfferGoodsInfo2 != null && batchOfferGoodsInfo2.getOfferedCount() < batchOfferGoodsInfo.getShelfs().size();
    }

    public static BatchOfferGoodsInfo getBatchOfferGoodsInfo() {
        return batchOfferGoodsInfo;
    }

    public static void initShjManager(Context context, String str) {
        wkAppContext = new WeakReference<>(context);
        handler = new Handler();
        Constant.addPackageInfo(new Constant.ConfigurePackageInfo(com.xyshj.machine.BuildConfig.APPLICATION_ID, "售货机程序", Constant.TYPE_UPGRADE | Constant.TYPE_BACKUPS));
        Constant.addPackageInfo(new Constant.ConfigurePackageInfo(com.shj.setting.Utils.Constant.safeAppName, "安全启动程序", Constant.TYPE_REPORT | Constant.TYPE_UPGRADE | Constant.TYPE_BACKUPS));
        Constant.addPackageInfo(new Constant.ConfigurePackageInfo(Zoloz.SMILE2PAY_PACKAGE, "支付宝刷脸程序", Constant.TYPE_REPORT | Constant.TYPE_UPGRADE | Constant.TYPE_BACKUPS));
        Constant.addPackageInfo(new Constant.ConfigurePackageInfo("com.alipay.iot.service", "支付宝刷脸服务程序", Constant.TYPE_REPORT | Constant.TYPE_UPGRADE | Constant.TYPE_BACKUPS));
        Constant.addPackageInfo(new Constant.ConfigurePackageInfo("com.alipay.iot.master", "支付宝授权程序", Constant.TYPE_REPORT | Constant.TYPE_UPGRADE | Constant.TYPE_BACKUPS));
        Constant.addPackageInfo(new Constant.ConfigurePackageInfo("com.tencent.wxpayface", "微信刷脸程序", Constant.TYPE_REPORT | Constant.TYPE_UPGRADE | Constant.TYPE_BACKUPS));
        Constant.addPackageInfo(new Constant.ConfigurePackageInfo("com.estrongs.android.pop", "Es文件浏览器", Constant.TYPE_REPORT | Constant.TYPE_UPGRADE | Constant.TYPE_BACKUPS));
        Constant.addPackageInfo(new Constant.ConfigurePackageInfo("com.juphoon.cloud.xinyuan", "网络电话", Constant.TYPE_REPORT | Constant.TYPE_UPGRADE | Constant.TYPE_BACKUPS));
        Constant.addPackageInfo(new Constant.ConfigurePackageInfo("com.bjw.ComAssistant", "串口调试工具", Constant.TYPE_REPORT | Constant.TYPE_UPGRADE | Constant.TYPE_BACKUPS));
        Constant.addPackageInfo(new Constant.ConfigurePackageInfo("com.zkteco.android.xyliveface", "中控身份识别程序", Constant.TYPE_REPORT | Constant.TYPE_UPGRADE | Constant.TYPE_BACKUPS));
        Constant.addPackageInfo(new Constant.ConfigurePackageInfo("com.oysb.floattestapp", "售货机日志查看工具", Constant.TYPE_REPORT | Constant.TYPE_UPGRADE | Constant.TYPE_BACKUPS));
        RequestHelper.init(context);
        setAppFolders();
        setCacheFile(str);
        setLogs();
        PhoneHelper phoneHelper2 = new PhoneHelper();
        phoneHelper = phoneHelper2;
        phoneHelper2.init(context);
    }

    static void setCacheFile(String str) {
        CacheHelper.setCacheFile(new File(str));
    }

    static void setLogs() {
        WeakReference<Context> weakReference = wkAppContext;
        Context context = weakReference == null ? null : weakReference.get();
        if (context == null) {
            return;
        }
        Loger.init(context);
        Loger.setLogFile("NET", "xyshj/log", "数据上报.log", 2592000000L, Shj.isDebug() ? 1024 : 0);
        Loger.setLogFile("SHJ", "xyshj/log", "售货机.log", 2592000000L, 0);
        Loger.setLogFile("COMMAND", "xyshj/log", "指令.log", 2592000000L, Shj.isDebug() ? 1024 : 0);
        Loger.setLogFile("SALES", "xyshj/log", "订单.log", 2592000000L, 1024);
        Loger.setLogFile(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, "xyshj/log", "故障.log", 604800000L, 1024);
        Loger.setLogFile("REQUEST", "xyshj/log", "请求.log", 604800000L, 0);
        Loger.setLogFile("SCANOR", "xyshj/log", "扫描设备.log", 604800000L, 0);
        Loger.setLogFile("APP", "xyshj/log", "APP信息.log", 604800000L, 0);
        Loger.setLogFile("SET", "xyshj/log", "机器设置.log", 2592000000L, 0);
    }

    public static void setAppFolders() {
        String str = SDFileUtils.SDCardRoot + "xyShj";
        try {
            File file = new File(str + "/avFiles");
            if (!file.exists()) {
                SDFileUtils.creatDataDir(file.getAbsolutePath());
            }
            File file2 = new File(str + "/images");
            if (file2.exists()) {
                return;
            }
            SDFileUtils.creatDataDir(file2.getAbsolutePath());
        } catch (Exception e) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
        }
    }

    public static boolean isShjStarted() {
        return isStarted;
    }

    public static void startShj(StatusListener statusListener2, MoneyListener moneyListener2, OrderListener orderListener2, GoodsStatusListener goodsStatusListener2, Bitmap bitmap, String str, boolean z) {
        Loger.writeLog("SHJ", "正在初始货售货机...安卓系统启动时间:" + AndroidSystem.getSystemStartTime_str());
        WeakReference<Context> weakReference = wkAppContext;
        Context context = weakReference == null ? null : weakReference.get();
        if (context == null) {
            return;
        }
        statusListener = statusListener2;
        moneyListener = moneyListener2;
        orderListener = orderListener2;
        goodsStatusListener = goodsStatusListener2;
        putSetting(OrderPayType.WEIXIN + "ZK", "NET_PRICE_wxzkjg");
        putSetting(OrderPayType.ZFB + "ZK", "NET_PRICE_alipayzkjg");
        putSetting(OrderPayType.YL + "ZK", "NET_PRICE_unionzkjg");
        putSetting(OrderPayType.JD + "ZK", "NET_PRICE_jdzkjg");
        putSetting(OrderPayType.ICCard + "ZK", "NET_PRICE_cardzkjg");
        initGoodsManager(bitmap);
        Shj.initShj(context);
        Shj.setShjListener(shjListener);
        initNetManager();
        initTTSManager();
        initVideoManager();
        AppUpdateHelper.init(context, Shj.getMachineId(), appType);
        reset();
        isStarted = true;
    }

    private static void initDevices() {
        if (isEnableQrScanor()) {
            Scanor.addExDevPath(Shj.getComPath());
            Scanor.setScanorListener(new Scanor.ScanorListener() { // from class: com.shj.biz.ShjManager.2
                AnonymousClass2() {
                }

                @Override // com.shj.device.scanor.Scanor.ScanorListener
                public void onMessage(String str) {
                    Loger.writeLog("SCANOR", str);
                    if (str.equalsIgnoreCase("noScanor")) {
                        TTSManager.addText("没有找到扫码头");
                        return;
                    }
                    if (str.equalsIgnoreCase("connected")) {
                        TTSManager.addText("已找到扫码头");
                        return;
                    }
                    if (str.contains("connectState")) {
                        if (str.contains("true")) {
                            ShjManager.getStatusListener().onScanorConnectChanged(true);
                            return;
                        } else {
                            ShjManager.getStatusListener().onScanorConnectChanged(false);
                            return;
                        }
                    }
                    if (!Scanor.isConnected() || str.startsWith("Notice:")) {
                        return;
                    }
                    ShjManager.getStatusListener().onMessage("SCANOR", str);
                }
            });
            Scanor.start(getAppContext());
        }
        if (isEnableWinCardReader()) {
            WinCarcdReader.get().start(getAppContext());
        }
        if (MdbReader_BDT.isEnabled()) {
            MdbReader_BDT.get().setDebug(Shj.getMachineId().equalsIgnoreCase("1707600009"));
            MdbReader_BDT.get().start(getAppContext());
        }
        if (JB_CardReader.isEnabled()) {
            JB_CardReader.get().start(getAppContext());
        }
    }

    /* renamed from: com.shj.biz.ShjManager$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements Scanor.ScanorListener {
        AnonymousClass2() {
        }

        @Override // com.shj.device.scanor.Scanor.ScanorListener
        public void onMessage(String str) {
            Loger.writeLog("SCANOR", str);
            if (str.equalsIgnoreCase("noScanor")) {
                TTSManager.addText("没有找到扫码头");
                return;
            }
            if (str.equalsIgnoreCase("connected")) {
                TTSManager.addText("已找到扫码头");
                return;
            }
            if (str.contains("connectState")) {
                if (str.contains("true")) {
                    ShjManager.getStatusListener().onScanorConnectChanged(true);
                    return;
                } else {
                    ShjManager.getStatusListener().onScanorConnectChanged(false);
                    return;
                }
            }
            if (!Scanor.isConnected() || str.startsWith("Notice:")) {
                return;
            }
            ShjManager.getStatusListener().onMessage("SCANOR", str);
        }
    }

    private static void restBizServiceChecker() {
        CacheHelper.getFileCache().put("SHJ_BIZ_SERVICE_CHECK_TIME", "" + System.currentTimeMillis());
        new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.3
            AnonymousClass3() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                Context context;
                String asString;
                try {
                    context = ShjManager.wkAppContext == null ? null : ShjManager.wkAppContext.get();
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
                if (context == null) {
                    return;
                }
                long currentTimeMillis = System.currentTimeMillis();
                String asString2 = CacheHelper.getFileCache().getAsString("SHJ_BIZ_SERVICE_CHECK_TIME");
                if (asString2 != null && currentTimeMillis - Long.parseLong(asString2) > 300000) {
                    Loger.writeLog("UI", "SHJ_BIZ_SERVICE 异常，重启系统");
                    AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_AppStatus, "", "SHJ_BIZ_SERVICE 异常，重启系统");
                    AndroidSystem.setNeedRestartApp(true, "SHJ_BIZ_SERVICE 异常，重启系统");
                }
                Object data = ShjManager.getData("SOCKETENABLE");
                if ((data == null || !data.toString().equals("FALSE")) && CommonTool.isNetworkAvailable(context) && (asString = CacheHelper.getFileCache().getAsString("SHJ_NET_SERVICE_CHECK_TIME")) != null && currentTimeMillis - Long.parseLong(asString) > 300000) {
                    Loger.writeLog("UI", "网络正常，但是SHJ_NET_SERVICE 异常，需重启APP");
                    AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_AppStatus, "", "网络正常，但是SHJ_NET_SERVICE 异常，需重启APP");
                    AndroidSystem.setNeedRestartApp(true, "网络正常，但是SHJ_NET_SERVICE 异常，需重启APP");
                }
                try {
                    Loger.writeLog("UI", "APP正在进行日志检查***");
                    Loger.checkLogs();
                } catch (Exception unused) {
                }
                try {
                    Loger.writeLog("UI", "APP正在清查内存***");
                    System.gc();
                } catch (Exception unused2) {
                }
            }
        }, 600000L, 300000L);
    }

    /* renamed from: com.shj.biz.ShjManager$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends TimerTask {
        AnonymousClass3() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Context context;
            String asString;
            try {
                context = ShjManager.wkAppContext == null ? null : ShjManager.wkAppContext.get();
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
            if (context == null) {
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            String asString2 = CacheHelper.getFileCache().getAsString("SHJ_BIZ_SERVICE_CHECK_TIME");
            if (asString2 != null && currentTimeMillis - Long.parseLong(asString2) > 300000) {
                Loger.writeLog("UI", "SHJ_BIZ_SERVICE 异常，重启系统");
                AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_AppStatus, "", "SHJ_BIZ_SERVICE 异常，重启系统");
                AndroidSystem.setNeedRestartApp(true, "SHJ_BIZ_SERVICE 异常，重启系统");
            }
            Object data = ShjManager.getData("SOCKETENABLE");
            if ((data == null || !data.toString().equals("FALSE")) && CommonTool.isNetworkAvailable(context) && (asString = CacheHelper.getFileCache().getAsString("SHJ_NET_SERVICE_CHECK_TIME")) != null && currentTimeMillis - Long.parseLong(asString) > 300000) {
                Loger.writeLog("UI", "网络正常，但是SHJ_NET_SERVICE 异常，需重启APP");
                AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_AppStatus, "", "网络正常，但是SHJ_NET_SERVICE 异常，需重启APP");
                AndroidSystem.setNeedRestartApp(true, "网络正常，但是SHJ_NET_SERVICE 异常，需重启APP");
            }
            try {
                Loger.writeLog("UI", "APP正在进行日志检查***");
                Loger.checkLogs();
            } catch (Exception unused) {
            }
            try {
                Loger.writeLog("UI", "APP正在清查内存***");
                System.gc();
            } catch (Exception unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements AppStatusLoger.AppStatusLogerListener {
        AnonymousClass4() {
        }

        @Override // com.oysb.utils.AppStatusLoger.AppStatusLogerListener
        public void onAppStatusItemAdded(AppStatusLoger.AppStatus appStatus) {
            DataSynchronous.report_appstauts(appStatus.getSn(), appStatus.getModule(), appStatus.getType(), appStatus.getCode(), appStatus.getInfo(), appStatus.getCount(), appStatus.getCreateTime());
        }
    }

    public static void initTasksAfterShjRestFinished() {
        AppStatusLoger.init(wkActivityContext.get(), new AppStatusLoger.AppStatusLogerListener() { // from class: com.shj.biz.ShjManager.4
            AnonymousClass4() {
            }

            @Override // com.oysb.utils.AppStatusLoger.AppStatusLogerListener
            public void onAppStatusItemAdded(AppStatusLoger.AppStatus appStatus) {
                DataSynchronous.report_appstauts(appStatus.getSn(), appStatus.getModule(), appStatus.getType(), appStatus.getCode(), appStatus.getInfo(), appStatus.getCount(), appStatus.getCreateTime());
            }
        });
        DataSynchronous.startLockTimer();
        initDevices();
        resetTouchTimer();
        restBizServiceChecker();
        finishUnSuccessRequest();
        getGoodsManager().startUpdateGoodsReserveInfosTask();
        getGoodsManager().startUpdateGoodsIsOnSale();
        getGoodsManager()._checkNeedDownloadGoodsInfo();
        getGoodsManager().onGoodsSynchronismFinished();
    }

    public static void updateLastFindPeopleTime() {
        lastFindPeopleTime = System.currentTimeMillis();
    }

    public static void updateLastTouchTime() {
        lastTouchTime = System.currentTimeMillis();
    }

    static void resetTouchTimer() {
        Timer timer = touchTimer;
        if (timer != null) {
            timer.cancel();
            touchTimer = null;
        }
        lastTouchTime = System.currentTimeMillis();
        Timer timer2 = new Timer();
        touchTimer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.5
            AnonymousClass5() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    int i = (Calendar.getInstance().get(11) * 60) + Calendar.getInstance().get(12);
                    long currentTimeMillis = System.currentTimeMillis() - ShjManager.lastFindPeopleTime;
                    if (Shj.hasPeopleFindDevice() && currentTimeMillis > 10000 && Shj.getWallet().getCatchMoney().intValue() == 0 && Shj.getSelectedShelf() == null) {
                        Loger.writeLog("PEOPLE", "_onFreeTime:" + currentTimeMillis);
                        ShjManager.shjListener._onFreeTime(currentTimeMillis);
                    }
                    long currentTimeMillis2 = System.currentTimeMillis() - ShjManager.lastTouchTime;
                    if (currentTimeMillis2 < 30000) {
                        return;
                    }
                    Loger.writeLog("UI", "touch pass time:" + currentTimeMillis2 + " hour:" + i);
                    ShjManager.getVideoHelper().updatePlayFiles();
                    if ((Shj.getWallet().getCatchMoney().intValue() != 0 || currentTimeMillis2 <= 90000) && currentTimeMillis2 < ((Long) ShjManager.getSetting(ShjManager.EAT_TIME)).longValue() + 15000) {
                        if (currentTimeMillis2 >= ((Long) ShjManager.getSetting(ShjManager.EAT_TIME)).longValue()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("should eat money:");
                            sb.append(Shj.getWallet().getCatchMoney().intValue() > 0);
                            Loger.writeLog("UI", sb.toString());
                            if (Shj.getWallet().getCatchMoney().intValue() > 0) {
                                Loger.writeLog("UI", "need eat money");
                                if (Shj.getVersion() == 1) {
                                    ShjManager.setMoney(MoneyType.Reset, 0, "eat");
                                    return;
                                } else {
                                    ShjManager.setMoney(MoneyType.EAT, 0, "eat");
                                    return;
                                }
                            }
                            return;
                        }
                        return;
                    }
                    ShjManager.lastTouchTime = System.currentTimeMillis();
                    Integer valueOf = Integer.valueOf(Calendar.getInstance().get(6));
                    Object asObject = CacheHelper.getFileCache().getAsObject("RESTARTDAY");
                    int intValue = asObject != null ? ((Integer) asObject).intValue() : -1;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("should reboot(hour >= 2 && hour <= 6 && day != lastRestartDay):");
                    double d = i;
                    sb2.append(d > (Math.random() * 180.0d) + 120.0d && i < 360 && valueOf.intValue() != intValue);
                    Loger.writeLog("UI", sb2.toString());
                    if (ShjManager.restartPerday && d >= (Math.random() * 180.0d) + 120.0d && i <= 360 && valueOf.intValue() != intValue) {
                        CacheHelper.getFileCache().put("RESTARTDAY", valueOf);
                        Loger.writeLog("UI", "RESTARTDAY:" + CacheHelper.getFileCache().getAsObject("RESTARTDAY"));
                        AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_TimerTask, "", "每天零晨2点至6点重启APP");
                        AndroidSystem.rebootSystem("每天零晨2点至6点重启APP", 0);
                        return;
                    }
                    Loger.writeLog("UI", "should isNeedRestartApp:" + AndroidSystem.isNeedRestartApp() + " reason:" + AndroidSystem.getNeedRestartAppReson() + " isNeedRebootSystem:" + AndroidSystem.isNeedRebootSystem() + " reasion:" + AndroidSystem.getNeedRebootSystemReson());
                    if (AndroidSystem.isNeedRebootSystem()) {
                        ShjManager.shjListener._onNeedRebootSystem(AndroidSystem.getNeedRestartAppReson());
                        return;
                    }
                    if (AndroidSystem.isNeedRestartApp()) {
                        ShjManager.shjListener._onNeedRestart(AndroidSystem.getNeedRestartAppReson());
                        return;
                    }
                    if (currentTimeMillis > 60000) {
                        ShjManager.shjListener._onFreeTime(currentTimeMillis2);
                    }
                    Object asObject2 = CacheHelper.getFileCache().getAsObject("LFPOS_RESIGN_DAY");
                    if (asObject2 != null) {
                        ((Integer) asObject2).intValue();
                    }
                } catch (Exception unused) {
                }
            }
        }, 30000L, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
    }

    /* renamed from: com.shj.biz.ShjManager$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 extends TimerTask {
        AnonymousClass5() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                int i = (Calendar.getInstance().get(11) * 60) + Calendar.getInstance().get(12);
                long currentTimeMillis = System.currentTimeMillis() - ShjManager.lastFindPeopleTime;
                if (Shj.hasPeopleFindDevice() && currentTimeMillis > 10000 && Shj.getWallet().getCatchMoney().intValue() == 0 && Shj.getSelectedShelf() == null) {
                    Loger.writeLog("PEOPLE", "_onFreeTime:" + currentTimeMillis);
                    ShjManager.shjListener._onFreeTime(currentTimeMillis);
                }
                long currentTimeMillis2 = System.currentTimeMillis() - ShjManager.lastTouchTime;
                if (currentTimeMillis2 < 30000) {
                    return;
                }
                Loger.writeLog("UI", "touch pass time:" + currentTimeMillis2 + " hour:" + i);
                ShjManager.getVideoHelper().updatePlayFiles();
                if ((Shj.getWallet().getCatchMoney().intValue() != 0 || currentTimeMillis2 <= 90000) && currentTimeMillis2 < ((Long) ShjManager.getSetting(ShjManager.EAT_TIME)).longValue() + 15000) {
                    if (currentTimeMillis2 >= ((Long) ShjManager.getSetting(ShjManager.EAT_TIME)).longValue()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("should eat money:");
                        sb.append(Shj.getWallet().getCatchMoney().intValue() > 0);
                        Loger.writeLog("UI", sb.toString());
                        if (Shj.getWallet().getCatchMoney().intValue() > 0) {
                            Loger.writeLog("UI", "need eat money");
                            if (Shj.getVersion() == 1) {
                                ShjManager.setMoney(MoneyType.Reset, 0, "eat");
                                return;
                            } else {
                                ShjManager.setMoney(MoneyType.EAT, 0, "eat");
                                return;
                            }
                        }
                        return;
                    }
                    return;
                }
                ShjManager.lastTouchTime = System.currentTimeMillis();
                Integer valueOf = Integer.valueOf(Calendar.getInstance().get(6));
                Object asObject = CacheHelper.getFileCache().getAsObject("RESTARTDAY");
                int intValue = asObject != null ? ((Integer) asObject).intValue() : -1;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("should reboot(hour >= 2 && hour <= 6 && day != lastRestartDay):");
                double d = i;
                sb2.append(d > (Math.random() * 180.0d) + 120.0d && i < 360 && valueOf.intValue() != intValue);
                Loger.writeLog("UI", sb2.toString());
                if (ShjManager.restartPerday && d >= (Math.random() * 180.0d) + 120.0d && i <= 360 && valueOf.intValue() != intValue) {
                    CacheHelper.getFileCache().put("RESTARTDAY", valueOf);
                    Loger.writeLog("UI", "RESTARTDAY:" + CacheHelper.getFileCache().getAsObject("RESTARTDAY"));
                    AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_TimerTask, "", "每天零晨2点至6点重启APP");
                    AndroidSystem.rebootSystem("每天零晨2点至6点重启APP", 0);
                    return;
                }
                Loger.writeLog("UI", "should isNeedRestartApp:" + AndroidSystem.isNeedRestartApp() + " reason:" + AndroidSystem.getNeedRestartAppReson() + " isNeedRebootSystem:" + AndroidSystem.isNeedRebootSystem() + " reasion:" + AndroidSystem.getNeedRebootSystemReson());
                if (AndroidSystem.isNeedRebootSystem()) {
                    ShjManager.shjListener._onNeedRebootSystem(AndroidSystem.getNeedRestartAppReson());
                    return;
                }
                if (AndroidSystem.isNeedRestartApp()) {
                    ShjManager.shjListener._onNeedRestart(AndroidSystem.getNeedRestartAppReson());
                    return;
                }
                if (currentTimeMillis > 60000) {
                    ShjManager.shjListener._onFreeTime(currentTimeMillis2);
                }
                Object asObject2 = CacheHelper.getFileCache().getAsObject("LFPOS_RESIGN_DAY");
                if (asObject2 != null) {
                    ((Integer) asObject2).intValue();
                }
            } catch (Exception unused) {
            }
        }
    }

    static void initTTSManager() {
        WeakReference<Context> weakReference = wkAppContext;
        Context context = weakReference == null ? null : weakReference.get();
        if (context == null) {
            return;
        }
        TTSManager.init(context);
        int intValue = ((Integer) getSetting(TTS_VOICE)).intValue();
        Loger.writeLog("UI", "initTTSManager ttsVoice=" + intValue);
        TTSManager.setTTSVoice(intValue);
    }

    static void initVideoManager() {
        int intValue = ((Integer) getSetting(VIDEO_VOICE)).intValue();
        Loger.writeLog("UI", "initVideoManager voideoVoice=" + intValue);
        getVideoHelper().setVideoVoice(wkAppContext.get(), intValue);
    }

    public static int requestJqConnectPwdFailedCount() {
        String asString = CacheHelper.getFileCache().getAsString(CONNECT_SERVER_RETRY_COUNT);
        if (asString == null) {
            return 0;
        }
        return Integer.parseInt(asString);
    }

    public static void requestJqConnectPwd(boolean z) {
        Object data = getData("SOCKETENABLE");
        if (data == null || !data.toString().equals("FALSE")) {
            NetManager.init(getAppContext(), Shj.getMachineId(), NetManager.getPwd());
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("jqbh", NetManager.getUser());
                jSONObject.put("shbh", "");
                jSONObject.put("pwd", NetManager.getPwd());
                String jqConnectUrl = getJqConnectUrl();
                Log.i("JQ_CONNECT_URL", jqConnectUrl);
                RequestItem requestItem = new RequestItem(jqConnectUrl, jSONObject, "POST");
                requestItem.setRepeatDelay(10000);
                requestItem.setRequestMaxCount(2);
                requestItem.setLostAble(true);
                requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.ShjManager.6
                    final /* synthetic */ boolean val$start;

                    AnonymousClass6(boolean z2) {
                        z = z2;
                    }

                    @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                    public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                        Loger.writeLog("NET", "statusCode:" + i + "请求接器接入地址出错" + str);
                        AppStatusLoger.addAppStatus(null, "NET", AppStatusLoger.Type_ConnectAuthority, "", "请求接器接入地址出错");
                        try {
                            String asString = CacheHelper.getFileCache().getAsString(ShjManager.CONNECT_SERVER_RETRY_COUNT);
                            if (asString == null) {
                                CacheHelper.getFileCache().put(ShjManager.CONNECT_SERVER_RETRY_COUNT, "1");
                            } else {
                                CacheHelper.getFileCache().put(ShjManager.CONNECT_SERVER_RETRY_COUNT, "" + (Integer.parseInt(asString) + 1));
                            }
                        } catch (Exception unused) {
                        }
                        try {
                            NetManager.setServerIp(CacheHelper.getFileCache().getAsString("NET_C_IP"));
                            NetManager.setServerPort(CacheHelper.getFileCache().getAsString("NET_C_PORT"));
                            NetManager.setUser(Shj.getMachineId(), CacheHelper.getFileCache().getAsString("NET_C_PWD"));
                        } catch (Exception unused2) {
                        }
                    }

                    @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                    public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                        String str2;
                        String str3;
                        try {
                            CacheHelper.getFileCache().put(ShjManager.CONNECT_SERVER_RETRY_COUNT, "0");
                            JSONObject jSONObject2 = new JSONObject(str);
                            String string = jSONObject2.getString("code");
                            if (string.equalsIgnoreCase("H0000")) {
                                JSONObject jSONObject3 = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
                                String[] split = jSONObject3.getString(IjkMediaPlayer.OnNativeInvokeListener.ARG_URL).split(":");
                                NetManager.setServerIp(split[0]);
                                NetManager.setServerPort(split[1]);
                                NetManager.setUser(NetManager.getUser(), jSONObject3.getString("pwd"));
                                CacheHelper.getFileCache().put("NET_C_IP", split[0]);
                                CacheHelper.getFileCache().put("NET_C_PORT", split[1]);
                                CacheHelper.getFileCache().put("NET_C_PWD", jSONObject3.getString("pwd"));
                            } else {
                                NetManager.setServerIp("000.000.000.000");
                                String language = CommonTool.getLanguage(ShjManager.wkActivityContext.get());
                                if (string.equalsIgnoreCase("H0001")) {
                                    str2 = "MachineId Error";
                                    if (!language.equalsIgnoreCase("en") && !language.equalsIgnoreCase("fr") && !language.equalsIgnoreCase("it") && !language.equalsIgnoreCase("pl")) {
                                        str2 = "机器号错误";
                                    }
                                } else if (string.equalsIgnoreCase("H0002")) {
                                    str2 = "Password Error";
                                    if (!language.equalsIgnoreCase("en") && !language.equalsIgnoreCase("fr") && !language.equalsIgnoreCase("it") && !language.equalsIgnoreCase("pl")) {
                                        str3 = "连接密码错误";
                                        str2 = str3;
                                    }
                                } else if (string.equalsIgnoreCase("H0003")) {
                                    str2 = "MachineId Invalide";
                                    if (!language.equalsIgnoreCase("en") && !language.equalsIgnoreCase("fr") && !language.equalsIgnoreCase("it") && !language.equalsIgnoreCase("pl")) {
                                        str3 = "机器不属于该商户";
                                        str2 = str3;
                                    }
                                } else if (string.equalsIgnoreCase("H0004")) {
                                    str2 = "Unkown Error";
                                    if (!language.equalsIgnoreCase("en") && !language.equalsIgnoreCase("fr") && !language.equalsIgnoreCase("it") && !language.equalsIgnoreCase("pl")) {
                                        str3 = "未知错误";
                                        str2 = str3;
                                    }
                                } else {
                                    str2 = "";
                                }
                                ShjManager.getStatusListener().onMessage("GATEWAY", str2);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppStatusLoger.addAppStatus(null, "NET", AppStatusLoger.Type_ConnectAuthority, "", "解新设备接入地址数据出错 error:" + e.getLocalizedMessage());
                        }
                        return true;
                    }

                    @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                    public void onRequestFinished(RequestItem requestItem2, boolean z2) {
                        if (z) {
                            ShjBizServiceEx.start(ShjManager.getAppContext());
                        }
                    }
                });
                RequestHelper.request(requestItem);
            } catch (Exception unused) {
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ boolean val$start;

        AnonymousClass6(boolean z2) {
            z = z2;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            Loger.writeLog("NET", "statusCode:" + i + "请求接器接入地址出错" + str);
            AppStatusLoger.addAppStatus(null, "NET", AppStatusLoger.Type_ConnectAuthority, "", "请求接器接入地址出错");
            try {
                String asString = CacheHelper.getFileCache().getAsString(ShjManager.CONNECT_SERVER_RETRY_COUNT);
                if (asString == null) {
                    CacheHelper.getFileCache().put(ShjManager.CONNECT_SERVER_RETRY_COUNT, "1");
                } else {
                    CacheHelper.getFileCache().put(ShjManager.CONNECT_SERVER_RETRY_COUNT, "" + (Integer.parseInt(asString) + 1));
                }
            } catch (Exception unused) {
            }
            try {
                NetManager.setServerIp(CacheHelper.getFileCache().getAsString("NET_C_IP"));
                NetManager.setServerPort(CacheHelper.getFileCache().getAsString("NET_C_PORT"));
                NetManager.setUser(Shj.getMachineId(), CacheHelper.getFileCache().getAsString("NET_C_PWD"));
            } catch (Exception unused2) {
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            String str2;
            String str3;
            try {
                CacheHelper.getFileCache().put(ShjManager.CONNECT_SERVER_RETRY_COUNT, "0");
                JSONObject jSONObject2 = new JSONObject(str);
                String string = jSONObject2.getString("code");
                if (string.equalsIgnoreCase("H0000")) {
                    JSONObject jSONObject3 = jSONObject2.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    String[] split = jSONObject3.getString(IjkMediaPlayer.OnNativeInvokeListener.ARG_URL).split(":");
                    NetManager.setServerIp(split[0]);
                    NetManager.setServerPort(split[1]);
                    NetManager.setUser(NetManager.getUser(), jSONObject3.getString("pwd"));
                    CacheHelper.getFileCache().put("NET_C_IP", split[0]);
                    CacheHelper.getFileCache().put("NET_C_PORT", split[1]);
                    CacheHelper.getFileCache().put("NET_C_PWD", jSONObject3.getString("pwd"));
                } else {
                    NetManager.setServerIp("000.000.000.000");
                    String language = CommonTool.getLanguage(ShjManager.wkActivityContext.get());
                    if (string.equalsIgnoreCase("H0001")) {
                        str2 = "MachineId Error";
                        if (!language.equalsIgnoreCase("en") && !language.equalsIgnoreCase("fr") && !language.equalsIgnoreCase("it") && !language.equalsIgnoreCase("pl")) {
                            str2 = "机器号错误";
                        }
                    } else if (string.equalsIgnoreCase("H0002")) {
                        str2 = "Password Error";
                        if (!language.equalsIgnoreCase("en") && !language.equalsIgnoreCase("fr") && !language.equalsIgnoreCase("it") && !language.equalsIgnoreCase("pl")) {
                            str3 = "连接密码错误";
                            str2 = str3;
                        }
                    } else if (string.equalsIgnoreCase("H0003")) {
                        str2 = "MachineId Invalide";
                        if (!language.equalsIgnoreCase("en") && !language.equalsIgnoreCase("fr") && !language.equalsIgnoreCase("it") && !language.equalsIgnoreCase("pl")) {
                            str3 = "机器不属于该商户";
                            str2 = str3;
                        }
                    } else if (string.equalsIgnoreCase("H0004")) {
                        str2 = "Unkown Error";
                        if (!language.equalsIgnoreCase("en") && !language.equalsIgnoreCase("fr") && !language.equalsIgnoreCase("it") && !language.equalsIgnoreCase("pl")) {
                            str3 = "未知错误";
                            str2 = str3;
                        }
                    } else {
                        str2 = "";
                    }
                    ShjManager.getStatusListener().onMessage("GATEWAY", str2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                AppStatusLoger.addAppStatus(null, "NET", AppStatusLoger.Type_ConnectAuthority, "", "解新设备接入地址数据出错 error:" + e.getLocalizedMessage());
            }
            return true;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z2) {
            if (z) {
                ShjBizServiceEx.start(ShjManager.getAppContext());
            }
        }
    }

    static void initNetManager() {
        NetManager.setReportDBFile(SDFileUtils.SDCardRoot + "xyShj/report.db");
        NetManager.setOnMessageListener(new NetMessagaeListener());
        NetManager.setOnlineCardPayResultListener(myOnlineCardPayResultListener);
        NetManager.setOfferCmdTransfResultListener(myOfferCmdResultListener);
        NetManager.setTransfDataResultListener(myShjTransfDataResultListener);
        NetManager.setOnlinePayApplyResultListener(new OnlinePayApplyResultListener() { // from class: com.shj.biz.ShjManager.7
            AnonymousClass7() {
            }

            @Override // com.oysb.xy.i.OnlinePayApplyResultListener
            public void onPayResult(int i, int i2, String str) {
                ShjManager.getOrderManager().onDpjModelNetPayResult(i, OrderManager.serverType2OrderPayType(i2).getIndex(), str);
            }
        });
        NetManager.setPickCodeReportListener(new PickByCodeRequestListener() { // from class: com.shj.biz.ShjManager.8
            AnonymousClass8() {
            }

            @Override // com.oysb.xy.i.PickByCodeRequestListener
            public void onPickcodeChecked(String str, boolean z, String str2, int i, List<String> list) {
                ShjManager.onPickcodeChecked(str, z, str2, i, list);
            }
        });
        if ((getOrderPayTypes().contains(OrderPayType.ECard) || getOrderPayTypes().contains(OrderPayType.ICCard)) && (getIcCardOrderPayItem() instanceof OrderPayItem_LFPos)) {
            LfPos.setLfPosServerListener(new ShjLfPosServerListener());
            NetManager.setPosTransfResultListener(new ShjPosTransfResultListener());
            PosService.start(getAppContext());
            new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.9
                AnonymousClass9() {
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    LfPos.Sign();
                }
            }, 10000L);
        }
        NetManager.setDebugModel(0);
        requestJqConnectPwd(true);
    }

    /* renamed from: com.shj.biz.ShjManager$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements OnlinePayApplyResultListener {
        AnonymousClass7() {
        }

        @Override // com.oysb.xy.i.OnlinePayApplyResultListener
        public void onPayResult(int i, int i2, String str) {
            ShjManager.getOrderManager().onDpjModelNetPayResult(i, OrderManager.serverType2OrderPayType(i2).getIndex(), str);
        }
    }

    /* renamed from: com.shj.biz.ShjManager$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements PickByCodeRequestListener {
        AnonymousClass8() {
        }

        @Override // com.oysb.xy.i.PickByCodeRequestListener
        public void onPickcodeChecked(String str, boolean z, String str2, int i, List<String> list) {
            ShjManager.onPickcodeChecked(str, z, str2, i, list);
        }
    }

    /* renamed from: com.shj.biz.ShjManager$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 extends TimerTask {
        AnonymousClass9() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            LfPos.Sign();
        }
    }

    static void initGoodsManager(Bitmap bitmap) {
        getGoodsManager().setGoodsImageFolder(SDFileUtils.SDCardRoot + "xyShj/images");
        getGoodsManager().setDefaultGoodsImage(bitmap);
        getGoodsManager().reLoadGoods();
    }

    /* renamed from: com.shj.biz.ShjManager$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 extends TimerTask {
        AnonymousClass10() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                Date date = new Date();
                date.setTime(System.currentTimeMillis() - 180000);
                RequestHelper.deleteRequests(2);
                for (RequestItem requestItem : RequestHelper.unSuccessRequests(null, date)) {
                    requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.ShjManager.10.1
                        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                        }

                        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                        public void onRequestFinished(RequestItem requestItem2, boolean z) {
                        }

                        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                            return true;
                        }

                        AnonymousClass1() {
                        }
                    });
                    RequestHelper.request(requestItem);
                    Loger.writeLog("REQUEST", "batchRequest ...");
                }
                Thread.sleep(HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
        }

        /* renamed from: com.shj.biz.ShjManager$10$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements RequestItem.OnRequestResultListener {
            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                return true;
            }

            AnonymousClass1() {
            }
        }
    }

    public static void finishUnSuccessRequest() {
        new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.10
            AnonymousClass10() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    Date date = new Date();
                    date.setTime(System.currentTimeMillis() - 180000);
                    RequestHelper.deleteRequests(2);
                    for (RequestItem requestItem : RequestHelper.unSuccessRequests(null, date)) {
                        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.ShjManager.10.1
                            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                            public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                            }

                            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                            public void onRequestFinished(RequestItem requestItem2, boolean z) {
                            }

                            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                            public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                                return true;
                            }

                            AnonymousClass1() {
                            }
                        });
                        RequestHelper.request(requestItem);
                        Loger.writeLog("REQUEST", "batchRequest ...");
                    }
                    Thread.sleep(HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
            }

            /* renamed from: com.shj.biz.ShjManager$10$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements RequestItem.OnRequestResultListener {
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                    return true;
                }

                AnonymousClass1() {
                }
            }
        }, 60000L, 60000L);
    }

    public static BizShjListener getBizShjListener() {
        return shjListener;
    }

    public static MoneyListener getMoneyListener() {
        return moneyListener;
    }

    public static OrderListener getOrderListener() {
        return orderListener;
    }

    public static StatusListener getStatusListener() {
        return statusListener;
    }

    public static GoodsStatusListener getGoodsStatusListener() {
        return goodsStatusListener;
    }

    public static GoodsManager getGoodsManager() {
        return goodsManager;
    }

    public static OrderManager getOrderManager() {
        return orderManager;
    }

    private static void _postCmmand(Command command) {
        CommandManager.appendSendCommand(command);
    }

    public static void ack() {
        _postCmmand(new Command_Up_ACK());
    }

    public static void clearShelfBlock() {
        if (Shj.getVersion() == 1) {
            _postCmmand(new Command_Up_ClearShelfBlock());
        }
    }

    public static void shopCartPay(int i) {
        if (Shj.getVersion() == 1) {
            return;
        }
        CommandV2_Up_Pay commandV2_Up_Pay = new CommandV2_Up_Pay();
        commandV2_Up_Pay.setParams(i);
        _postCmmand(commandV2_Up_Pay);
        if (isEnableWinCardReader()) {
            Loger.writeLog("SALES;" + WinCarcdReader.get().getLogTag(), " WinCarcdReader connect:" + WinCarcdReader.get().isConnected() + " isLastPayFinished:" + WinCarcdReader.get().isLastPayFinished());
            if (WinCarcdReader.get().isConnected()) {
                getOrderManager().setCanCancelOrderByApp(false);
                if (WinCarcdReader.get().isLastPayFinished()) {
                    Shj.getWallet().setLastAddMoneyInfo("");
                    WinCarcdReader.get().pay(i, new WinCarcdReader.WinCarcdReaderListener() { // from class: com.shj.biz.ShjManager.11
                        final /* synthetic */ int val$money;

                        AnonymousClass11(int i2) {
                            i = i2;
                        }

                        @Override // com.shj.device.cardreader.WinCarcdReader.WinCarcdReaderListener
                        public void onResult(String str, int i2, String str2, String str3) {
                            if (str.equalsIgnoreCase("pay")) {
                                if (i2 == 0) {
//                                    Loger.writeLog("SALES;" + WinCarcdReader.get().getLogTag(), " WinCarcdReader pay successfull money:" + i + " MoneyType:" + MoneyType.Paper);
                                    ShjManager.setMoney(MoneyType.Paper, i, "WinCarcdReaderPay*" + str2);
                                    return;
                                }
//                                Loger.writeLog("SALES;" + WinCarcdReader.get().getLogTag(), " WinCarcdReader cancel pay");
                                ShjManager.unSelectGoodsOnShelf();
                            }
                        }
                    });
                }
            }
        }
//        Loger.writeLog("SHJ", "MdbReader_BDT:" + MdbReader_BDT.isEnabled() + "connected:" + MdbReader_BDT.get().isConnected());
//        Loger.writeLog("SHJ", "JB_CardReader:" + JB_CardReader.isEnabled() + "connected:" + JB_CardReader.get().isConnected());
//        if (MdbReader_BDT.isEnabled() && MdbReader_BDT.get().isConnected()) {
//            ShelfInfo selectedShelf = Shj.getSelectedShelf();
//            MdbReader_BDT.get().pay(selectedShelf != null ? selectedShelf.getShelf().intValue() : 1, i2);
//        }
//        if (JB_CardReader.isEnabled() && JB_CardReader.get().isConnected()) {
//            JB_CardReader.get().pay(1, i2);
//        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$11 */
    /* loaded from: classes2.dex */
    public class AnonymousClass11 implements WinCarcdReader.WinCarcdReaderListener {
        final /* synthetic */ int val$money;

        AnonymousClass11(int i2) {
            i = i2;
        }

        @Override // com.shj.device.cardreader.WinCarcdReader.WinCarcdReaderListener
        public void onResult(String str, int i2, String str2, String str3) {
            if (str.equalsIgnoreCase("pay")) {
                if (i2 == 0) {
                    Loger.writeLog("SALES;" + WinCarcdReader.get().getLogTag(), " WinCarcdReader pay successfull money:" + i + " MoneyType:" + MoneyType.Paper);
                    ShjManager.setMoney(MoneyType.Paper, i, "WinCarcdReaderPay*" + str2);
                    return;
                }
                Loger.writeLog("SALES;" + WinCarcdReader.get().getLogTag(), " WinCarcdReader cancel pay");
                ShjManager.unSelectGoodsOnShelf();
            }
        }
    }

    public static void charge() {
        unSelectGoodsOnShelf();
        shjListener._onChargStart();
        if (Shj.getVersion() == 1) {
            _postCmmand(new Command_Up_DoCharge());
        } else {
            _postCmmand(new CommandV2_Up_Change());
        }
        if (MdbReader_BDT.isEnabled()) {
            MdbReader_BDT.get().charge(Shj.getWallet().getCatchMoney().intValue());
        }
        if (JB_CardReader.isEnabled()) {
            JB_CardReader.get().charge(Shj.getWallet().getCatchMoney().intValue());
        }
        if (Shj.isDebug()) {
            Shj.onChangeFinish(Shj.getWallet().getCoinBalance().intValue(), Shj.getWallet().getPaperBalance().intValue());
            Shj.onUpdateCoinBalance(0);
            Shj.onUpdatePaperMoneyBalance(0);
            Shj.onResetCurrentMoney(0, false);
        }
    }

    public static void testShelfIsExist(int i) {
        if (Shj.getVersion() == 1) {
            Command_Up_TestGoodsShelfIsExist command_Up_TestGoodsShelfIsExist = new Command_Up_TestGoodsShelfIsExist();
            command_Up_TestGoodsShelfIsExist.setShelf(i);
            _postCmmand(command_Up_TestGoodsShelfIsExist);
        }
    }

    public static void testGoodsIsValid(int i) {
        if (Shj.getVersion() == 1) {
            Command_Up_TestGoodsIsValid command_Up_TestGoodsIsValid = new Command_Up_TestGoodsIsValid();
            command_Up_TestGoodsIsValid.setParams(i);
            _postCmmand(command_Up_TestGoodsIsValid);
        }
    }

    public static void setOfferGoodsCheck(int i, boolean z) {
        if (Shj.getVersion() == 1) {
            Command_Up_SetOfferGoodsCheck command_Up_SetOfferGoodsCheck = new Command_Up_SetOfferGoodsCheck();
            command_Up_SetOfferGoodsCheck.setParams(i, z);
            _postCmmand(command_Up_SetOfferGoodsCheck);
        }
    }

    public static void enablePosFindCard() {
        if (Shj.getVersion() == 1) {
            _postCmmand(new Command_Up_EnablePosFindCard());
        }
    }

    public static void unablePosFindCard() {
        if (Shj.getVersion() == 1) {
            _postCmmand(new Command_Up_UnablePosFindCard());
        }
    }

    public static void startToUp() {
        if (Shj.getVersion() == 1) {
            Command_Up_TopUp command_Up_TopUp = new Command_Up_TopUp();
            command_Up_TopUp.setParams(true);
            _postCmmand(command_Up_TopUp);
        }
    }

    public static void exitTopUp() {
        if (Shj.getVersion() == 1) {
            Command_Up_TopUp command_Up_TopUp = new Command_Up_TopUp();
            command_Up_TopUp.setParams(false);
            _postCmmand(command_Up_TopUp);
        }
    }

    public static void batchStart() {
        if (Shj.getVersion() == 1) {
            _postCmmand(new Command_Up_BatchStart());
        } else {
            _postCmmand(new Command_Up_BatchStart());
        }
        if (Shj.isDebug()) {
            getStatusListener().onCmdBatchStart();
        }
    }

    public static void batchEnd() {
        if (Shj.getVersion() == 1) {
            _postCmmand(new Command_Up_BatchEnd());
        } else {
            _postCmmand(new CommandV2_Up_BatchEnd());
        }
        if (Shj.isDebug()) {
            new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.12
                AnonymousClass12() {
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    ShjManager.getStatusListener().onCmdBatchEnd();
                }
            }, 10000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$12 */
    /* loaded from: classes2.dex */
    public class AnonymousClass12 extends TimerTask {
        AnonymousClass12() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ShjManager.getStatusListener().onCmdBatchEnd();
        }
    }

    public static void setPollTime(int i) {
        if (Shj.getVersion() == 1) {
            return;
        }
        CommandV2_Up_SetPollTime commandV2_Up_SetPollTime = new CommandV2_Up_SetPollTime();
        commandV2_Up_SetPollTime.setParams(5);
        _postCmmand(commandV2_Up_SetPollTime);
    }

    public static void reset() {
        if (Shj.getVersion() == 1) {
            Loger.writeLog("SHJ", "下发复位指令（排队）...");
            _postCmmand(new Command_Up_Reset());
            return;
        }
        CommandV2_Up_PollTime commandV2_Up_PollTime = new CommandV2_Up_PollTime();
        commandV2_Up_PollTime.setParams(3);
        _postCmmand(commandV2_Up_PollTime);
        _postCmmand(new CommandV2_Up_Syn());
        if (Shj.isDebug()) {
            Shj.onReset();
        }
    }

    public static void setMoney(MoneyType moneyType, int i, String str) {
        if (moneyType == MoneyType.EAT && Shj.getWallet().getCatchMoney().intValue() > 0) {
            AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_Command, "", "吞币 money：" + i + " catchMoney:" + Shj.getWallet().getCatchMoney());
        }
        if (Shj.getVersion() == 1) {
            Command_Up_SetMoney command_Up_SetMoney = new Command_Up_SetMoney();
            command_Up_SetMoney.setParams(moneyType, i, str);
            _postCmmand(command_Up_SetMoney);
        } else {
            CommandV2_Up_SetMoney commandV2_Up_SetMoney = new CommandV2_Up_SetMoney();
            commandV2_Up_SetMoney.setParams(moneyType, i, str);
            _postCmmand(commandV2_Up_SetMoney);
        }
        if (Shj.isDebug()) {
            if (moneyType == MoneyType.EAT && isBatchOfferingGoods()) {
                return;
            }
            Shj.debugAcceptMoney(i, moneyType, str);
        }
    }

    public static void setCoinType(int i) {
        if (Shj.getVersion() == 1) {
            Command_Up_SetCoinType command_Up_SetCoinType = new Command_Up_SetCoinType();
            command_Up_SetCoinType.setParams(i);
            _postCmmand(command_Up_SetCoinType);
        }
    }

    public static void setPaperType(int i) {
        if (Shj.getVersion() == 1) {
            Command_Up_SetPaperType command_Up_SetPaperType = new Command_Up_SetPaperType();
            command_Up_SetPaperType.setParams(i);
            _postCmmand(command_Up_SetPaperType);
        }
    }

    public static void driverShelf(int i, boolean z, boolean z2, int i2) {
        Loger.writeLog("SALES", "下发驱动货道命令 shelf:" + i);
        if (Shj.getVersion() == 1) {
            Command_Up_DriverGoodsShelf command_Up_DriverGoodsShelf = new Command_Up_DriverGoodsShelf();
            command_Up_DriverGoodsShelf.setParams(i, false);
            _postCmmand(command_Up_DriverGoodsShelf);
        } else {
            CommandV2_Up_DriverShelf commandV2_Up_DriverShelf = new CommandV2_Up_DriverShelf();
            if (z) {
                commandV2_Up_DriverShelf.setParamsV2(i, true, false, z2, i2);
            } else {
                commandV2_Up_DriverShelf.setParamsV2(i, false, false, z2, i2);
            }
            _postCmmand(commandV2_Up_DriverShelf);
        }
        if (Shj.isDebug()) {
            Shj.debugOfferGoods(i, z);
        }
    }

    public static void driverShelf(int i) {
        driverShelf(i, true, false, 1);
    }

    public static void driverShelf(int i, int i2) {
        driverShelf(i, true, false, i2);
    }

    public static void checkOfferGoodsDevices(int i) {
        Loger.writeLog("SALES", "下发查询货道状态指令 shelf:" + i);
        if (Shj.getShelfInfo(Integer.valueOf(i)) == null) {
            Loger.writeLog("SALES", "货道不存在" + i);
            return;
        }
        if (Shj.getSelectedShelf() == null || Shj.getSelectedShelf().getShelf().intValue() != i) {
            if (Shj.getVersion() == 1) {
                Command_Up_SelectGoods command_Up_SelectGoods = new Command_Up_SelectGoods();
                command_Up_SelectGoods.setParams(i);
                _postCmmand(command_Up_SelectGoods);
            } else {
                CommandV2_Up_CheckOfferDevices commandV2_Up_CheckOfferDevices = new CommandV2_Up_CheckOfferDevices();
                commandV2_Up_CheckOfferDevices.setParams(i);
                _postCmmand(commandV2_Up_CheckOfferDevices);
            }
            if (Shj.isDebug()) {
                Loger.writeLog("SHJ;SALES", "更新货道状态 debug shelf" + i);
                Shj.getShelfInfo(Integer.valueOf(i)).setStatus(0);
                if (debugHasGoodsInPickdoor || Shj.isDebugShelfStopSale()) {
                    Shj.debugUnslectShelf(i);
                } else {
                    Shj.debugSelectShelf(i);
                }
                if (Shj.isDebugShelfStopSale()) {
                    Shj.onUpdateShelfState(i, 4);
                    Shj.setOfferGoodsDiviceState(4);
                } else if (debugHasGoodsInPickdoor) {
                    Shj.setOfferGoodsDiviceState(0);
                } else {
                    if (Shj.needCheckOfferStatus()) {
                        return;
                    }
                    Shj.setOfferGoodsDiviceState(0);
                }
            }
        }
    }

    public static void selectGoodsOnShelf(int i) {
        Loger.writeLog("SALES", "下发选货命令 shelf:" + i);
        if (Shj.getShelfInfo(Integer.valueOf(i)) == null) {
            Loger.writeLog("SALES", "货道不存在" + i);
            return;
        }
        if (Shj.getSelectedShelf() != null && Shj.getSelectedShelf().getShelf().intValue() == i && Shj.getWallet().getCatchMoney().intValue() == 0) {
            return;
        }
        if (Shj.getVersion() == 1) {
            Command_Up_SelectGoods command_Up_SelectGoods = new Command_Up_SelectGoods();
            command_Up_SelectGoods.setParams(i);
            _postCmmand(command_Up_SelectGoods);
        } else {
            CommandV2_Up_SelectGoods commandV2_Up_SelectGoods = new CommandV2_Up_SelectGoods();
            commandV2_Up_SelectGoods.setParams(i);
            _postCmmand(commandV2_Up_SelectGoods);
        }
        if (Shj.isDebug()) {
            if (debugHasGoodsInPickdoor) {
                Shj.debugUnslectShelf(i);
            } else {
                Shj.debugSelectShelf(i);
            }
            Shj.setOfferGoodsDiviceState(debugHasGoodsInPickdoor ? 5 : 0);
        }
    }

    public static void unSelectGoodsOnShelf() {
        if (Shj.getVersion() == 1) {
            _postCmmand(new Command_Up_UnSelectGoods());
        } else {
            _postCmmand(new CommandV2_Up_UnSelectGoods());
        }
        if (Shj.isDebug()) {
            Shj.debugUnslectShelf(0);
        }
    }

    public static void setShelfGoodsName(int i, String str) {
        if (Shj.isStoreGoodsInfoInVMC()) {
            CacheHelper.getFileCache().put("shelfGoodsName:" + i, "" + str);
        }
        Shj.getShelfInfo(Integer.valueOf(i)).setGoodsName(str);
    }

    public static void setShelfGoodsCode(int i, String str) {
        if (Shj.getVersion() == 1) {
            Command_Up_SetShelfGoodsCode command_Up_SetShelfGoodsCode = new Command_Up_SetShelfGoodsCode();
            command_Up_SetShelfGoodsCode.setExpiredTime(300000L);
            command_Up_SetShelfGoodsCode.setParams(i, str);
            _postCmmand(command_Up_SetShelfGoodsCode);
        } else {
            try {
                CommandV2_Up_SetShelfGoodsCode commandV2_Up_SetShelfGoodsCode = new CommandV2_Up_SetShelfGoodsCode();
                commandV2_Up_SetShelfGoodsCode.setExpiredTime(300000L);
                commandV2_Up_SetShelfGoodsCode.setParams(i, str);
                _postCmmand(commandV2_Up_SetShelfGoodsCode);
            } catch (Exception unused) {
            }
        }
        if (Shj.isStoreGoodsInfoInVMC()) {
            CacheHelper.getFileCache().put("shelfGoodsCode:" + i, "" + str);
        }
        if (Shj.isDebug()) {
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
            Shj.onUpdateShelfInfo(i, shelfInfo.getPrice().intValue(), shelfInfo.getGoodsCount().intValue(), shelfInfo.getCapacity().intValue(), str, shelfInfo.getStatus().intValue(), 0, -1, -1);
        }
    }

    public static void setShelfInventory(int i, int i2) {
        if (Shj.getVersion() == 1) {
            Command_Up_SetShelfCapacity command_Up_SetShelfCapacity = new Command_Up_SetShelfCapacity();
            command_Up_SetShelfCapacity.setExpiredTime(300000L);
            command_Up_SetShelfCapacity.setParams(i, i2);
            _postCmmand(command_Up_SetShelfCapacity);
        } else {
            CommandV2_Up_SetShelfCapacity commandV2_Up_SetShelfCapacity = new CommandV2_Up_SetShelfCapacity();
            commandV2_Up_SetShelfCapacity.setExpiredTime(300000L);
            commandV2_Up_SetShelfCapacity.setParams(i, i2);
            _postCmmand(commandV2_Up_SetShelfCapacity);
        }
        if (Shj.isStoreGoodsInfoInVMC()) {
            CacheHelper.getFileCache().put("shelfInventory:" + i, "" + i2);
        }
        if (Shj.isDebug()) {
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
            Shj.onUpdateShelfInfo(i, shelfInfo.getPrice().intValue(), shelfInfo.getGoodsCount().intValue(), i2, shelfInfo.getGoodsCode(), shelfInfo.getStatus().intValue(), 0, -1, -1);
        }
    }

    public static void setShelfGoodsCount(int i, int i2) {
        if (Shj.getVersion() == 1) {
            Command_Up_SetShelfGoodsCount command_Up_SetShelfGoodsCount = new Command_Up_SetShelfGoodsCount();
            command_Up_SetShelfGoodsCount.setExpiredTime(300000L);
            command_Up_SetShelfGoodsCount.setParams(i, i2);
            _postCmmand(command_Up_SetShelfGoodsCount);
        } else {
            CommandV2_Up_SetShelfGoodsCount commandV2_Up_SetShelfGoodsCount = new CommandV2_Up_SetShelfGoodsCount();
            commandV2_Up_SetShelfGoodsCount.setExpiredTime(300000L);
            commandV2_Up_SetShelfGoodsCount.setParams(i, i2);
            _postCmmand(commandV2_Up_SetShelfGoodsCount);
        }
        if (Shj.isStoreGoodsInfoInVMC()) {
            CacheHelper.getFileCache().put("shelfGoodsCount:" + i, "" + i2);
        }
        if (Shj.isDebug()) {
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
            Shj.onUpdateShelfInfo(i, shelfInfo.getPrice().intValue(), i2, shelfInfo.getCapacity().intValue(), shelfInfo.getGoodsCode(), shelfInfo.getStatus().intValue(), 0, -1, -1);
        }
    }

    public static void setShelfGoodsPrice(int i, int i2) {
        if (Shj.getVersion() == 1) {
            Command_Up_SetShelfGoodsPrice command_Up_SetShelfGoodsPrice = new Command_Up_SetShelfGoodsPrice();
            command_Up_SetShelfGoodsPrice.setExpiredTime(300000L);
            command_Up_SetShelfGoodsPrice.setParams(i, i2);
            _postCmmand(command_Up_SetShelfGoodsPrice);
        } else {
            CommandV2_Up_SetShelfPrice commandV2_Up_SetShelfPrice = new CommandV2_Up_SetShelfPrice();
            commandV2_Up_SetShelfPrice.setExpiredTime(300000L);
            commandV2_Up_SetShelfPrice.setParams(i, i2);
            _postCmmand(commandV2_Up_SetShelfPrice);
        }
        if (Shj.isStoreGoodsInfoInVMC()) {
            CacheHelper.getFileCache().put("shelfGoodsPrice:" + i, "" + i2);
        }
        if (Shj.isDebug()) {
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
            Shj.onUpdateShelfInfo(i, i2, shelfInfo.getGoodsCount().intValue(), shelfInfo.getCapacity().intValue(), shelfInfo.getGoodsCode(), shelfInfo.getStatus().intValue(), 0, -1, -1);
        }
    }

    public static void setLightStatus(int i, int i2) {
        if (i2 == 0) {
            lightStatus = false;
        } else {
            lightStatus = true;
        }
        if (Shj.getVersion() == 1) {
            return;
        }
        CommandV2_Up_ControlLight commandV2_Up_ControlLight = new CommandV2_Up_ControlLight();
        commandV2_Up_ControlLight.setExpiredTime(300000L);
        commandV2_Up_ControlLight.setParams(i, i2);
        _postCmmand(commandV2_Up_ControlLight);
    }

    public static boolean isQueryingICCardInfo() {
        return queryingICCardInfo && System.currentTimeMillis() - queryingICCardInfo_time < queryingICCardInfo_outtime;
    }

    public static void queryICCardInfo(boolean z, int i) {
        if (z) {
            queryingICCardInfo_time = System.currentTimeMillis();
            queryingICCardInfo = true;
            queryingICCardInfo_outtime = i;
        } else {
            queryingICCardInfo = false;
        }
        CommandV2_Up_QueryICCardInfo commandV2_Up_QueryICCardInfo = new CommandV2_Up_QueryICCardInfo();
        commandV2_Up_QueryICCardInfo.setParams(z);
        _postCmmand(commandV2_Up_QueryICCardInfo);
        if (icCardOrderPayItem != null) {
            if (getOrderPayTypes().contains(OrderPayType.ECard) || getOrderPayTypes().contains(OrderPayType.ICCard)) {
                if (z) {
                    icCardOrderPayItem.queryCardBanlance(i);
                } else {
                    icCardOrderPayItem.cancel();
                }
            }
        }
    }

    public static void icCardTopUp(boolean z, long j, int i) {
        if (icCardOrderPayItem != null) {
            if (getOrderPayTypes().contains(OrderPayType.ECard) || getOrderPayTypes().contains(OrderPayType.ICCard)) {
                if (z) {
                    icCardOrderPayItem.topUp(j, i);
                } else {
                    icCardOrderPayItem.cancel();
                }
            }
        }
    }

    public static void icCardRefund(boolean z, long j, int i) {
        if (icCardOrderPayItem != null) {
            if (getOrderPayTypes().contains(OrderPayType.ECard) || getOrderPayTypes().contains(OrderPayType.ICCard)) {
                if (z) {
                    icCardOrderPayItem.refund(j, i);
                } else {
                    icCardOrderPayItem.cancel();
                }
            }
        }
    }

    public static void queryGoodsByPickcode(String str) {
        Report_Pay_PickByCode_Request report_Pay_PickByCode_Request = new Report_Pay_PickByCode_Request();
        report_Pay_PickByCode_Request.setParams(str);
        NetManager.appendReport(report_Pay_PickByCode_Request);
    }

    public static void updateBatchOfferGoodsResult() {
        BatchOfferGoodsInfo batchOfferGoodsInfo2 = batchOfferGoodsInfo;
        if (batchOfferGoodsInfo2 == null) {
            return;
        }
        String offerResult_ycch = batchOfferGoodsInfo2.getOfferResult_ycch();
        YGOrderHelper.updateBatchOfferGoodsResult();
        shjListener._onBatchOfferGoods_Finished(batchOfferGoodsInfo, offerResult_ycch);
    }

    public static boolean isBatchOfferingGoods(int i) {
        BatchOfferGoodsInfo batchOfferGoodsInfo2 = batchOfferGoodsInfo;
        return batchOfferGoodsInfo2 != null && batchOfferGoodsInfo2.getOfferedCount() < batchOfferGoodsInfo.getShelfs().size();
    }

    public static void onPickcodeChecked(String str, boolean z, String str2, int i, List<String> list) {
        shjListener._onPickCodeChecked(str, z);
        if (z) {
            offerGoodsByPickcode(str, str2, i, list);
        }
    }

    public static void continueBatchOfferGoods() {
        try {
            batchOfferGoodsInfo.setPaused(false);
        } catch (Exception unused) {
        }
    }

    public static void onBatchOfferGoodsFinished(int i, int i2) {
        OrderPayType orderPayType;
        if (batchOfferGoodsInfo == null) {
            return;
        }
        try {
            batchOfferTimerWaitStartTime = System.currentTimeMillis();
            BatchOfferGoodsInfo batchOfferGoodsInfo2 = batchOfferGoodsInfo;
            batchOfferGoodsInfo2.setOfferedCount(batchOfferGoodsInfo2.getOfferedCount() + 1);
            batchOfferGoodsInfo.setOfferingShelf(null);
            Iterator<HashMap<String, Integer>> it = batchOfferGoodsInfo.getShelfs().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                HashMap<String, Integer> next = it.next();
                int intValue = next.get(ShjDbHelper.COLUM_shelf).intValue();
                int intValue2 = next.get(SpeechUtility.TAG_RESOURCE_RESULT).intValue();
                StringBuilder sb = new StringBuilder();
                Iterator<HashMap<String, Integer>> it2 = it;
                sb.append("onBatchOfferGoodsFinished s=");
                sb.append(intValue);
                sb.append("shelf=");
                sb.append(i);
                sb.append("V=");
                sb.append(intValue2);
                sb.append("result=");
                sb.append(i2);
                Loger.writeLog("SHJ", sb.toString());
                if (intValue2 <= 0 && i == intValue && intValue2 == 0) {
                    next.put(SpeechUtility.TAG_RESOURCE_RESULT, Integer.valueOf(i2));
                    break;
                }
                it = it2;
            }
            Loger.writeLog("SHJ", "getOfferedCount:" + batchOfferGoodsInfo.getOfferedCount() + " size:" + batchOfferGoodsInfo.getShelfs().size());
            if (batchOfferGoodsInfo.getOfferedCount() == batchOfferGoodsInfo.getShelfs().size()) {
                getOrderManager().addTradSn();
                Order resentOrder = getOrderManager().getResentOrder(2, null);
                if (resentOrder != null) {
                    orderPayType = resentOrder.getPayType();
                    resentOrder.setStatus(5);
                } else {
                    orderPayType = null;
                }
                Loger.writeLog("SHJ", "-------------------------------批量出货完成--------------------------------");
                if (orderPayType != null) {
                    try {
                        if (orderPayType == OrderPayType.PickCode) {
                            shjListener._onPickCodeOfferGoodsFinished(batchOfferGoodsInfo.getRemark());
                        }
                    } catch (Exception e) {
                        Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                        AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_BatchOfferGoods, "", "onBatchOfferGoodsFinished(shelf：" + i + ", result:" + i2 + ") error:" + e.getLocalizedMessage());
                    }
                }
                updateBatchOfferGoodsResult();
                getOrderManager()._onOrderFinished();
                HashMap hashMap = new HashMap();
                HashMap hashMap2 = new HashMap();
                for (Iterator<Integer> it3 = batchOfferGoodsInfo.getOfferedShelves().iterator(); it3.hasNext(); it3 = it3) {
                    Integer next2 = it3.next();
                    hashMap.put(String.format("%03d", next2), Shj.getShelfInfo(next2).getStatus2Server());
                    hashMap2.put(String.format("%03d", next2), Shj.getShelfInfo(next2).getGoodsCount());
                }
                DataSynchronous.report_status_shelf(hashMap, hashMap2);
                if (resentOrder.getArgs().getArg("YC_CMD_OfferGoods").equals("TRUE")) {
                    DataSynchronous.report_transf_offerGoodsCmd(getData("lastOfferCmd_ly").toString(), 3, getData("lastOfferCmd_key").toString(), batchOfferGoodsInfo.getOfferResult_ycch());
                    resentOrder.setStatus(6);
                }
                Loger.writeLog("SHJ", ("order payType:" + orderPayType) != null ? orderPayType + "" : "");
                resentOrder.getArgs().putArg("batchOfferResult", batchOfferGoodsInfo.getOfferJSONResult(resentOrder));
                if (orderPayType != null && orderPayType != OrderPayType.CASH && Shj.getWallet().getCatchMoney().intValue() > 0) {
                    setMoney(MoneyType.EAT, 0, "-");
                }
                batchOfferGoodsInfo = null;
                selectGoodsOnShelf(0);
                return;
            }
            if (batchOfferGoodsInfo.getOfferedCount() % (pauseOnOfferGoodsCount - 1) == 0) {
                batchOfferGoodsInfo.setPaused(true);
                getGoodsStatusListener().onOfferingGoods_State(-1, OfferState.OfferGoodsPaused.getIndex(), "", 1);
            }
        } catch (Exception e2) {
            AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_BatchOfferGoods, "", "onBatchOfferGoodsFinished(shelf：" + i + ", result:" + i2 + ") error:" + e2.getLocalizedMessage());
        }
    }

    static void offerGoodsByPickcode(String str, String str2, int i, List<String> list) {
        BatchOfferGoodsInfo batchOfferGoodsInfo2 = new BatchOfferGoodsInfo();
        batchOfferGoodsInfo2.setRemark(str);
        if (batchOfferGoodsInfo2.getOrderArgs() != null) {
            batchOfferGoodsInfo2.getOrderArgs().restGoodsBatchnumberLockcount();
        }
        for (String str3 : list) {
            if (i == 1) {
                try {
                    batchOfferGoodsInfo2.addShelfItem(Integer.parseInt(str3));
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
            } else {
                batchOfferGoodsInfo2.addGoodsItem(str3);
            }
        }
        startBatchOfferGoods(batchOfferGoodsInfo2, MoneyType.PickCode);
    }

    public static void setMaxOfferPerGoodsTime(int i) {
        maxOfferPerGoodsTime = i;
    }

    public static boolean startBatchOfferGoods(BatchOfferGoodsInfo batchOfferGoodsInfo2, MoneyType moneyType) {
        if (batchOfferGoodsInfo2.getState() == 1) {
            Loger.writeLog("SHJ", "批量出货任务正在执行，不能启动新的出货任务");
            return false;
        }
        Loger.writeLog("SHJ", "-------------------------------开始批量出货--------------------------------");
        batchOfferGoodsInfo2.setState(1);
        if (-1 == batchOfferGoodsInfo2.getMaxOfferPerGoodsTime()) {
            batchOfferGoodsInfo2.setMaxOfferPerGoodsTime(maxOfferPerGoodsTime);
        }
        batchOfferGoodsInfo = batchOfferGoodsInfo2;
        getGoodsStatusListener().onOfferingGoods_State(batchOfferGoodsInfo2.getCurOfferingShelf(), 0, getData(STR_ready2OfferGoods).toString(), 1);
        batchOfferGoodsInfo.setMoneyType(moneyType);
        try {
            if (batchOfferGoodsInfo2.getOrderArgs().getArg("orderId").toString().length() > 5) {
                putData("lastOfferCmd_OrderId", batchOfferGoodsInfo2.getOrderArgs().getArg("orderId"));
            }
        } catch (Exception unused) {
        }
        Loger.writeLog("SHJ", "检查库存....");
        if (batchOfferGoodsInfo2.checkGoodsCount()) {
            for (Integer num : batchOfferGoodsInfo.getMapShelfCount().keySet()) {
                int intValue = batchOfferGoodsInfo.getMapShelfCount().get(num).intValue();
                ShelfInfo shelfInfo = Shj.getShelfInfo(num);
                if (shelfInfo == null || shelfInfo.getGoodsCount().intValue() < intValue) {
                    Loger.writeLog("SHJ;SALES", "货道:" + shelfInfo.getShelf() + " 库存:" + shelfInfo.getGoodsCount() + " 要求出货数量:" + intValue);
                    AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_BatchOfferGoods, "", "批量出货中止 货道:" + shelfInfo.getShelf() + " 库存:" + shelfInfo.getGoodsCount() + " 要求出货数量:" + intValue);
                    Loger.writeLog("SHJ", "-------------------------------批量出货中止--------------------------------");
                    batchOfferGoodsInfo = null;
                    return false;
                }
                shelfInfo.getPrice().intValue();
            }
        }
        if (batchOfferGoodsInfo.isShouldSetMoney()) {
            if (moneyType != MoneyType.PickCode) {
                batchOfferGoodsInfo.setMoney(0);
            }
            Loger.writeLog("SALES", "moneyType:" + batchOfferGoodsInfo.getMoneyType() + " money:" + batchOfferGoodsInfo.getMoney() + " remark:" + batchOfferGoodsInfo.getRemark());
        }
        batchOfferGoodsInfo.reset();
        offerBatchOfferGoods();
        return true;
    }

    public static void cancelBatchOfferGoods() {
        try {
            BatchOfferGoodsInfo batchOfferGoodsInfo2 = batchOfferGoodsInfo;
            if (batchOfferGoodsInfo2 != null) {
                batchOfferGoodsInfo2.cancelOfferJob();
            }
        } catch (Exception unused) {
        }
    }

    public static void offerBatchOfferGoods() {
        Timer timer = batchOfferTimer;
        if (timer != null) {
            timer.cancel();
            batchOfferTimer = null;
        }
        batchOfferTimerWaitStartTime = System.currentTimeMillis() - 1;
        Timer timer2 = new Timer();
        batchOfferTimer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.13
            boolean isLooping = false;

            AnonymousClass13() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                long j;
                Loger.writeLog("SALES", "---");
                if (ShjManager.batchOfferGoodsInfo == null || ShjManager.batchOfferGoodsInfo.isBatchOfferFinished()) {
                    this.isLooping = false;
                    ShjManager.batchOfferTimer.cancel();
                    Timer unused = ShjManager.batchOfferTimer = null;
                    return;
                }
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - ShjManager.batchOfferGoodsInfo.getLastSelectGoodsTime() < 1000) {
                    return;
                }
                long j2 = (currentTimeMillis - ShjManager.batchOfferTimerWaitStartTime) / 1000;
                if (!ShjManager.batchOfferGoodsInfo.isPaused() || j2 >= 60) {
                    if (j2 > 10) {
                        ShjManager.getGoodsStatusListener().onOfferingGoods_State(ShjManager.batchOfferGoodsInfo.getCurOfferingShelf(), 1, "" + (ShjManager.batchOfferGoodsInfo.getMaxOfferPerGoodsTime() - j2), 1);
                    }
                    if (!Shj.isOfferingGoods() || j2 >= 60) {
                        if (Shj.isOfferingGoods()) {
                            AppStatusLoger.addAppStatus_no_repeat(null, "BIZ", AppStatusLoger.Type_BatchOfferGoods, "", "出货时间超60秒");
                        }
                        if (ShjManager.batchOfferGoodsInfo.isCanceled()) {
                            int i = 0;
                            while (i != -1) {
                                try {
                                    i = ShjManager.batchOfferGoodsInfo.getNextOfferShelf();
                                    ShjManager.batchOfferGoodsInfo.setOfferingShelf(Shj.getShelfInfo(Integer.valueOf(i)));
                                    ShjManager.onBatchOfferGoodsFinished(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), 2);
                                } catch (Exception unused2) {
                                }
                            }
                            this.isLooping = false;
                            return;
                        }
                        if (this.isLooping) {
                            return;
                        }
                        this.isLooping = true;
                        try {
                        } catch (Exception e) {
                            Loger.writeException("SHJ;SALES;NET", e);
                        }
                        if (ShjManager.batchOfferGoodsInfo.getOfferingShelf() == null) {
                            long unused3 = ShjManager.batchOfferTimerWaitStartTime = System.currentTimeMillis();
                            ShjManager.batchOfferGoodsInfo.setOfferingShelf(Shj.getShelfInfo(Integer.valueOf(ShjManager.batchOfferGoodsInfo.getNextOfferShelf())));
                            ShjManager.getGoodsStatusListener().onOfferingGoods_State(ShjManager.batchOfferGoodsInfo.getCurOfferingShelf(), 0, ShjManager.getData(ShjManager.STR_offerGoods_n).toString().replace("#n#", "" + (ShjManager.batchOfferGoodsInfo.getOfferedCount() + 1)), 1);
                            this.isLooping = false;
                            return;
                        }
                        if (j2 > ShjManager.batchOfferGoodsInfo.getMaxOfferPerGoodsTime()) {
                            ShjManager.getGoodsStatusListener().onOfferingGoods_State(ShjManager.batchOfferGoodsInfo.getCurOfferingShelf(), 2, ShjManager.getData(ShjManager.STR_offerGoods_timeOut).toString(), 1);
                            ShjManager.onBatchOfferGoodsFinished(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), 2);
                            AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_BatchOfferGoods, "", "出货异常的，为避免出货了也退款，这里返回成功，需客服联系售后进行退款");
                            this.isLooping = false;
                            return;
                        }
                        if (Shj.getSelectedShelf() != null) {
                            Loger.writeLog("SHJ", "   " + Shj.getSelectedShelf().getShelf() + " = " + ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf());
                            if (Shj.getSelectedShelf().getShelf() == ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf()) {
                                if (!ShjManager.batchOfferGoodsInfo.isNextOfferMoneySetted()) {
                                    Loger.writeLog("SHJ;SALES", "已选择货道:" + Shj.getSelectedShelf().getShelf() + " 投币后直接出货");
                                    if (ShjManager.batchOfferGoodsInfo.getMoneyType() != MoneyType.PickCode) {
                                        ShjManager.setMoney(ShjManager.batchOfferGoodsInfo.getMoneyType(), 0, ShjManager.batchOfferGoodsInfo.getRemark());
                                    }
                                    ShjManager.batchOfferGoodsInfo.setNextOfferMoneySetted(true);
                                    long unused4 = ShjManager.batchOfferTimerWaitStartTime = System.currentTimeMillis();
                                }
                            } else {
                                ShjManager.unSelectGoodsOnShelf();
                            }
                            this.isLooping = false;
                            return;
                        }
                        if (ShjManager.batchOfferGoodsInfo.checkShelfStatue() && (!ShjManager.batchOfferGoodsInfo.getOfferingShelf().isStatusOK() || ShjManager.batchOfferGoodsInfo.getOfferingShelf().isTempCheckErrorButNeedStopOfferGoods())) {
                            ShjManager.getGoodsStatusListener().onOfferingGoods_State(ShjManager.batchOfferGoodsInfo.getCurOfferingShelf(), 0, Shj.getOfferGoodsDiviceState() > 0 ? Shj.getOfferGoodsDiviceStateInfo() : ShjManager.batchOfferGoodsInfo.getOfferingShelf().getStatusInfo(), 1);
                            ShjManager.onBatchOfferGoodsFinished(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), ShjManager.batchOfferGoodsInfo.getOfferingShelf().getStatus2OfferResult().intValue());
                            AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_BatchOfferGoods, "", "出货异常的，为避免出货了也退款，这里返回成功，需客服联系售后进行退款");
                            this.isLooping = false;
                            return;
                        }
                        if (Shj.getVersion() > 1 && Shj.needCheckOfferStatus()) {
                            Loger.writeLog("SHJ", "开始检查出货设备状态");
                            long currentTimeMillis2 = System.currentTimeMillis();
                            Loger.writeLog("SHJ", "检查开始时间：" + currentTimeMillis2);
                            ShjManager.checkOfferGoodsDevices(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue());
                            Loger.writeLog("SHJ", "设备状态更新时间：" + Shj.getOfferGoodsDiviceStateUpdateTime() + " 等待时长:" + Shj.getCheckOfferStatusTimeOut());
                            while (true) {
                                if (Shj.getOfferGoodsDiviceStateUpdateTime() > currentTimeMillis2) {
                                    j = currentTimeMillis2;
                                    break;
                                }
                                j = currentTimeMillis2;
                                if (System.currentTimeMillis() - currentTimeMillis2 >= Shj.getCheckOfferStatusTimeOut()) {
                                    break;
                                }
                                if (Shj.isOfferingGoods()) {
                                    Loger.writeLog("SHJ", "设备正在出货，络止当前新的查询，并等待出货结果");
                                    this.isLooping = false;
                                    return;
                                } else {
                                    Loger.writeLog("SHJ", ".");
                                    try {
                                        Thread.sleep(100L);
                                    } catch (Exception unused5) {
                                    }
                                    currentTimeMillis2 = j;
                                }
                            }
                            if (Shj.getOfferGoodsDiviceStateUpdateTime() > j) {
                                Loger.writeLog("SHJ", "出货设备状态：" + Shj.getOfferGoodsDiviceState() + StringUtils.SPACE + Shj.getOfferGoodsDiviceStateInfo());
                                if (Shj.getOfferGoodsDiviceState() > 0) {
                                    ShjManager.getGoodsStatusListener().onOfferingGoods_State(ShjManager.batchOfferGoodsInfo.getCurOfferingShelf(), 0, Shj.getOfferGoodsDiviceStateInfo(), 1);
                                    try {
                                        Thread.sleep(1000L);
                                    } catch (Exception unused6) {
                                    }
                                    this.isLooping = false;
                                    return;
                                }
                            } else {
                                Loger.writeLog("SHJ", "出货设备状态查询超时");
                                AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_BatchOfferGoods, "", "出货设备状态查询超时");
                                ShjManager.onBatchOfferGoodsFinished(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), 255);
                                try {
                                    Thread.sleep(3000L);
                                } catch (Exception unused7) {
                                }
                                this.isLooping = false;
                                return;
                            }
                        }
                        if (!ShjManager.batchOfferGoodsInfo.isNextOfferMoneySetted()) {
                            ShjManager.batchOfferGoodsInfo.setNextOfferMoneySetted(true);
                            long unused8 = ShjManager.batchOfferTimerWaitStartTime = System.currentTimeMillis();
                            ShjManager.getOrderManager()._onSelectGoodsOnShelf(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf());
                            Shj.getWallet().setLastAdd(0, ShjManager.batchOfferGoodsInfo.getMoneyType());
                            if (ShjManager.batchOfferGoodsInfo.getOrderArgs().getArg("PICKDOOR").length() > 0) {
                                ShjManager.driverShelf(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), true, ShjManager.batchOfferGoodsInfo.get2OfferCount() - ShjManager.batchOfferGoodsInfo.getOfferedCount() > 1, 2);
                            } else {
                                ShjManager.driverShelf(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), true, ShjManager.batchOfferGoodsInfo.get2OfferCount() - ShjManager.batchOfferGoodsInfo.getOfferedCount() > 1, 1);
                            }
                            ShjManager.batchOfferGoodsInfo.setLastSelectGoodsTime(System.currentTimeMillis());
                        }
                        this.isLooping = false;
                    }
                }
            }
        }, 0L, 500L);
    }

    /* renamed from: com.shj.biz.ShjManager$13 */
    /* loaded from: classes2.dex */
    public class AnonymousClass13 extends TimerTask {
        boolean isLooping = false;

        AnonymousClass13() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            long j;
            Loger.writeLog("SALES", "---");
            if (ShjManager.batchOfferGoodsInfo == null || ShjManager.batchOfferGoodsInfo.isBatchOfferFinished()) {
                this.isLooping = false;
                ShjManager.batchOfferTimer.cancel();
                Timer unused = ShjManager.batchOfferTimer = null;
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - ShjManager.batchOfferGoodsInfo.getLastSelectGoodsTime() < 1000) {
                return;
            }
            long j2 = (currentTimeMillis - ShjManager.batchOfferTimerWaitStartTime) / 1000;
            if (!ShjManager.batchOfferGoodsInfo.isPaused() || j2 >= 60) {
                if (j2 > 10) {
                    ShjManager.getGoodsStatusListener().onOfferingGoods_State(ShjManager.batchOfferGoodsInfo.getCurOfferingShelf(), 1, "" + (ShjManager.batchOfferGoodsInfo.getMaxOfferPerGoodsTime() - j2), 1);
                }
                if (!Shj.isOfferingGoods() || j2 >= 60) {
                    if (Shj.isOfferingGoods()) {
                        AppStatusLoger.addAppStatus_no_repeat(null, "BIZ", AppStatusLoger.Type_BatchOfferGoods, "", "出货时间超60秒");
                    }
                    if (ShjManager.batchOfferGoodsInfo.isCanceled()) {
                        int i = 0;
                        while (i != -1) {
                            try {
                                i = ShjManager.batchOfferGoodsInfo.getNextOfferShelf();
                                ShjManager.batchOfferGoodsInfo.setOfferingShelf(Shj.getShelfInfo(Integer.valueOf(i)));
                                ShjManager.onBatchOfferGoodsFinished(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), 2);
                            } catch (Exception unused2) {
                            }
                        }
                        this.isLooping = false;
                        return;
                    }
                    if (this.isLooping) {
                        return;
                    }
                    this.isLooping = true;
                    try {
                    } catch (Exception e) {
                        Loger.writeException("SHJ;SALES;NET", e);
                    }
                    if (ShjManager.batchOfferGoodsInfo.getOfferingShelf() == null) {
                        long unused3 = ShjManager.batchOfferTimerWaitStartTime = System.currentTimeMillis();
                        ShjManager.batchOfferGoodsInfo.setOfferingShelf(Shj.getShelfInfo(Integer.valueOf(ShjManager.batchOfferGoodsInfo.getNextOfferShelf())));
                        ShjManager.getGoodsStatusListener().onOfferingGoods_State(ShjManager.batchOfferGoodsInfo.getCurOfferingShelf(), 0, ShjManager.getData(ShjManager.STR_offerGoods_n).toString().replace("#n#", "" + (ShjManager.batchOfferGoodsInfo.getOfferedCount() + 1)), 1);
                        this.isLooping = false;
                        return;
                    }
                    if (j2 > ShjManager.batchOfferGoodsInfo.getMaxOfferPerGoodsTime()) {
                        ShjManager.getGoodsStatusListener().onOfferingGoods_State(ShjManager.batchOfferGoodsInfo.getCurOfferingShelf(), 2, ShjManager.getData(ShjManager.STR_offerGoods_timeOut).toString(), 1);
                        ShjManager.onBatchOfferGoodsFinished(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), 2);
                        AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_BatchOfferGoods, "", "出货异常的，为避免出货了也退款，这里返回成功，需客服联系售后进行退款");
                        this.isLooping = false;
                        return;
                    }
                    if (Shj.getSelectedShelf() != null) {
                        Loger.writeLog("SHJ", "   " + Shj.getSelectedShelf().getShelf() + " = " + ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf());
                        if (Shj.getSelectedShelf().getShelf() == ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf()) {
                            if (!ShjManager.batchOfferGoodsInfo.isNextOfferMoneySetted()) {
                                Loger.writeLog("SHJ;SALES", "已选择货道:" + Shj.getSelectedShelf().getShelf() + " 投币后直接出货");
                                if (ShjManager.batchOfferGoodsInfo.getMoneyType() != MoneyType.PickCode) {
                                    ShjManager.setMoney(ShjManager.batchOfferGoodsInfo.getMoneyType(), 0, ShjManager.batchOfferGoodsInfo.getRemark());
                                }
                                ShjManager.batchOfferGoodsInfo.setNextOfferMoneySetted(true);
                                long unused4 = ShjManager.batchOfferTimerWaitStartTime = System.currentTimeMillis();
                            }
                        } else {
                            ShjManager.unSelectGoodsOnShelf();
                        }
                        this.isLooping = false;
                        return;
                    }
                    if (ShjManager.batchOfferGoodsInfo.checkShelfStatue() && (!ShjManager.batchOfferGoodsInfo.getOfferingShelf().isStatusOK() || ShjManager.batchOfferGoodsInfo.getOfferingShelf().isTempCheckErrorButNeedStopOfferGoods())) {
                        ShjManager.getGoodsStatusListener().onOfferingGoods_State(ShjManager.batchOfferGoodsInfo.getCurOfferingShelf(), 0, Shj.getOfferGoodsDiviceState() > 0 ? Shj.getOfferGoodsDiviceStateInfo() : ShjManager.batchOfferGoodsInfo.getOfferingShelf().getStatusInfo(), 1);
                        ShjManager.onBatchOfferGoodsFinished(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), ShjManager.batchOfferGoodsInfo.getOfferingShelf().getStatus2OfferResult().intValue());
                        AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_BatchOfferGoods, "", "出货异常的，为避免出货了也退款，这里返回成功，需客服联系售后进行退款");
                        this.isLooping = false;
                        return;
                    }
                    if (Shj.getVersion() > 1 && Shj.needCheckOfferStatus()) {
                        Loger.writeLog("SHJ", "开始检查出货设备状态");
                        long currentTimeMillis2 = System.currentTimeMillis();
                        Loger.writeLog("SHJ", "检查开始时间：" + currentTimeMillis2);
                        ShjManager.checkOfferGoodsDevices(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue());
                        Loger.writeLog("SHJ", "设备状态更新时间：" + Shj.getOfferGoodsDiviceStateUpdateTime() + " 等待时长:" + Shj.getCheckOfferStatusTimeOut());
                        while (true) {
                            if (Shj.getOfferGoodsDiviceStateUpdateTime() > currentTimeMillis2) {
                                j = currentTimeMillis2;
                                break;
                            }
                            j = currentTimeMillis2;
                            if (System.currentTimeMillis() - currentTimeMillis2 >= Shj.getCheckOfferStatusTimeOut()) {
                                break;
                            }
                            if (Shj.isOfferingGoods()) {
                                Loger.writeLog("SHJ", "设备正在出货，络止当前新的查询，并等待出货结果");
                                this.isLooping = false;
                                return;
                            } else {
                                Loger.writeLog("SHJ", ".");
                                try {
                                    Thread.sleep(100L);
                                } catch (Exception unused5) {
                                }
                                currentTimeMillis2 = j;
                            }
                        }
                        if (Shj.getOfferGoodsDiviceStateUpdateTime() > j) {
                            Loger.writeLog("SHJ", "出货设备状态：" + Shj.getOfferGoodsDiviceState() + StringUtils.SPACE + Shj.getOfferGoodsDiviceStateInfo());
                            if (Shj.getOfferGoodsDiviceState() > 0) {
                                ShjManager.getGoodsStatusListener().onOfferingGoods_State(ShjManager.batchOfferGoodsInfo.getCurOfferingShelf(), 0, Shj.getOfferGoodsDiviceStateInfo(), 1);
                                try {
                                    Thread.sleep(1000L);
                                } catch (Exception unused6) {
                                }
                                this.isLooping = false;
                                return;
                            }
                        } else {
                            Loger.writeLog("SHJ", "出货设备状态查询超时");
                            AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_BatchOfferGoods, "", "出货设备状态查询超时");
                            ShjManager.onBatchOfferGoodsFinished(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), 255);
                            try {
                                Thread.sleep(3000L);
                            } catch (Exception unused7) {
                            }
                            this.isLooping = false;
                            return;
                        }
                    }
                    if (!ShjManager.batchOfferGoodsInfo.isNextOfferMoneySetted()) {
                        ShjManager.batchOfferGoodsInfo.setNextOfferMoneySetted(true);
                        long unused8 = ShjManager.batchOfferTimerWaitStartTime = System.currentTimeMillis();
                        ShjManager.getOrderManager()._onSelectGoodsOnShelf(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf());
                        Shj.getWallet().setLastAdd(0, ShjManager.batchOfferGoodsInfo.getMoneyType());
                        if (ShjManager.batchOfferGoodsInfo.getOrderArgs().getArg("PICKDOOR").length() > 0) {
                            ShjManager.driverShelf(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), true, ShjManager.batchOfferGoodsInfo.get2OfferCount() - ShjManager.batchOfferGoodsInfo.getOfferedCount() > 1, 2);
                        } else {
                            ShjManager.driverShelf(ShjManager.batchOfferGoodsInfo.getOfferingShelf().getShelf().intValue(), true, ShjManager.batchOfferGoodsInfo.get2OfferCount() - ShjManager.batchOfferGoodsInfo.getOfferedCount() > 1, 1);
                        }
                        ShjManager.batchOfferGoodsInfo.setLastSelectGoodsTime(System.currentTimeMillis());
                    }
                    this.isLooping = false;
                }
            }
        }
    }

    public static boolean isRunOnBoot() {
        return System.currentTimeMillis() - AndroidSystem.getSystemStartTime() < 60000;
    }

    public static void checkImages(int i) {
        checkImages(i, false);
    }

    public static void checkImages(int i, boolean z) {
        if (i > 12) {
            imageScale = 1.0d;
        } else if (i > 9) {
            imageScale = 1.5d;
        } else if (i > 6) {
            imageScale = 2.0d;
        } else {
            imageScale = 3.0d;
        }
        if (z) {
            imageScale = 1.5d;
        }
        new Thread(new RunnableEx(null) { // from class: com.shj.biz.ShjManager.14
            AnonymousClass14(Object obj) {
                super(obj);
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                try {
                    ImageUtils.resizeImages(SDFileUtils.SDCardRoot + "xyShj/images", "png", "png", (int) (ShjManager.imageScale * 320.0d));
                    ImageUtils.resizeImages(SDFileUtils.SDCardRoot + "xyShj/avFiles", "png", "png", 1200);
                    ImageUtils.resizeImages(SDFileUtils.SDCardRoot + "xyShj/avFiles", "bmp", "jpg", 1200);
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
            }
        }).start();
    }

    /* renamed from: com.shj.biz.ShjManager$14 */
    /* loaded from: classes2.dex */
    public class AnonymousClass14 extends RunnableEx {
        AnonymousClass14(Object obj) {
            super(obj);
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            try {
                ImageUtils.resizeImages(SDFileUtils.SDCardRoot + "xyShj/images", "png", "png", (int) (ShjManager.imageScale * 320.0d));
                ImageUtils.resizeImages(SDFileUtils.SDCardRoot + "xyShj/avFiles", "png", "png", 1200);
                ImageUtils.resizeImages(SDFileUtils.SDCardRoot + "xyShj/avFiles", "bmp", "jpg", 1200);
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
        }
    }

    public static void showDebugBarOnTestMachine(FrameLayout frameLayout) {
        String machineId = Shj.getMachineId();
        int i = frameLayout.getResources().getConfiguration().orientation;
        if (machineId.equalsIgnoreCase("1605600001") || machineId.equalsIgnoreCase("1606200011") || (machineId.compareTo("1707600001") >= 0 && machineId.compareTo("1707600009") <= 0)) {
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(frameLayout.getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
            layoutParams.gravity = 80;
            layoutParams.bottomMargin = 80;
            horizontalScrollView.setLayoutParams(layoutParams);
            frameLayout.addView(horizontalScrollView);
            LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
            linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            linearLayout.setOrientation(1);
            horizontalScrollView.addView(linearLayout);
            ArrayList<TextView> arrayList = new ArrayList();
            TextView createTestTextView = createTestTextView(frameLayout.getContext(), "登录");
            createTestTextView.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.15
                AnonymousClass15() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Context context = view.getContext();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(((TextView) view).getText());
                    builder.setCancelable(false);
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    linearLayout2.setOrientation(1);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                    layoutParams2.leftMargin = 40;
                    layoutParams2.rightMargin = 40;
                    EditText editText = new EditText(context);
                    editText.setText("");
                    editText.setInputType(SettingType.COMMODITY_ONE_BUTTON_SETUP);
                    editText.setLayoutParams(layoutParams2);
                    linearLayout2.addView(editText);
                    builder.setView(linearLayout2);
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.15.1
                        AnonymousClass1() {
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.15.2
                        final /* synthetic */ EditText val$pwdText;

                        AnonymousClass2(EditText editText2) {
                            editText = editText2;
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            try {
                                if (editText.getText().toString().trim().equals("xykj2016123")) {
                                    ShjManager.isDebugLogined = true;
                                }
                                InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.show();
                }

                /* renamed from: com.shj.biz.ShjManager$15$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$15$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$pwdText;

                    AnonymousClass2(EditText editText2) {
                        editText = editText2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            if (editText.getText().toString().trim().equals("xykj2016123")) {
                                ShjManager.isDebugLogined = true;
                            }
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView);
            TextView createTestTextView2 = createTestTextView(frameLayout.getContext(), "校时");
            createTestTextView2.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.16
                AnonymousClass16() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        ShjManager.queryServerTime();
                    }
                }
            });
            arrayList.add(createTestTextView2);
            TextView createTestTextView3 = createTestTextView(frameLayout.getContext(), "货道状态");
            createTestTextView3.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.17
                AnonymousClass17() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    List list;
                    if (ShjManager.isDebugLogined) {
                        try {
                            List<Integer> shelves = Shj.getShelves();
                            HashMap hashMap = new HashMap();
                            Iterator<Integer> it = shelves.iterator();
                            while (it.hasNext()) {
                                int intValue = it.next().intValue();
                                int layerByShelf = Shj.getLayerByShelf(intValue);
                                if (hashMap.containsKey(Integer.valueOf(layerByShelf))) {
                                    list = (List) hashMap.get(Integer.valueOf(layerByShelf));
                                } else {
                                    ArrayList arrayList2 = new ArrayList();
                                    hashMap.put(Integer.valueOf(layerByShelf), arrayList2);
                                    list = arrayList2;
                                }
                                list.add(Integer.valueOf(intValue));
                            }
                            Iterator it2 = hashMap.keySet().iterator();
                            while (it2.hasNext()) {
                                try {
                                    for (Integer num : (List) hashMap.get(Integer.valueOf(((Integer) it2.next()).intValue()))) {
                                        ShelfInfo shelfInfo = Shj.getShelfInfo(num);
                                        Loger.writeLog("状态", "shelf:" + num + " zt:" + shelfInfo.getStatus() + " spbm:" + shelfInfo.getGoodsCode() + " hdjg:" + shelfInfo.getPrice() + " hdcl:" + shelfInfo.getGoodsCount() + " hdrl:" + shelfInfo.getCapacity());
                                    }
                                } catch (Exception e) {
                                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                                }
                            }
                        } catch (Exception e2) {
                            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
                        }
                        for (Goods goods : ShjManager.getGoodsManager().getAllGoods()) {
                            Loger.writeLog("状态", "goodsCode:" + goods.getCode() + " name:" + goods.getName() + " count:" + goods.getCount() + " price:" + goods.getPrice());
                        }
                    }
                }
            });
            arrayList.add(createTestTextView3);
            TextView createTestTextView4 = createTestTextView(frameLayout.getContext(), "机器状态");
            createTestTextView4.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.18
                AnonymousClass18() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        try {
                            double d = ShjManager.temperature;
                            double random = Math.random() * 4.0d;
                            Double.isNaN(d);
                            ShjManager.temperature = (int) (d + random);
                            int i2 = 1;
                            ShjManager.paperMState = ShjManager.paperMState == 0 ? 1 : 0;
                            if (ShjManager.coinMState != 0) {
                                i2 = 0;
                            }
                            ShjManager.coinMState = i2;
                            double d2 = ShjManager.paperMoney;
                            double random2 = Math.random() * 10.0d;
                            Double.isNaN(d2);
                            ShjManager.paperMoney = (int) (d2 + random2);
                            double d3 = ShjManager.coinMoney;
                            double random3 = Math.random() * 10.0d;
                            Double.isNaN(d3);
                            ShjManager.coinMoney = (int) (d3 + random3);
                            Shj.onUpdateShjStatusV2(ShjManager.paperMState, ShjManager.coinMState, 0, 0, ShjManager.temperature, ShjManager.doorState, ShjManager.paperMoney, ShjManager.coinMoney, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
                            Loger.writeLog("SHJ", "paperMachineState:" + ShjManager.paperMState + " coinMachineState:" + ShjManager.coinMState + " posMachineState:0 wkqState:0 temperature:" + ShjManager.temperature + " paperMoney:" + ShjManager.paperMoney + " coinMoney:" + ShjManager.coinMoney);
                        } catch (Exception e) {
                            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                        }
                    }
                }
            });
            arrayList.add(createTestTextView4);
            TextView createTestTextView5 = createTestTextView(frameLayout.getContext(), "升降机自检");
            createTestTextView5.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.19
                AnonymousClass19() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        try {
                            Shj.setStoped(true);
                            Shj.onUpdateShjStatus(VMCStatus.LiftChecking);
                            new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.19.1
                                AnonymousClass1() {
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    Shj.setStoped(false);
                                    Shj.onUpdateShjStatus(VMCStatus.Normal);
                                }
                            }, 10000L);
                        } catch (Exception e) {
                            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$19$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 extends TimerTask {
                    AnonymousClass1() {
                    }

                    @Override // java.util.TimerTask, java.lang.Runnable
                    public void run() {
                        Shj.setStoped(false);
                        Shj.onUpdateShjStatus(VMCStatus.Normal);
                    }
                }
            });
            arrayList.add(createTestTextView5);
            TextView createTestTextView6 = createTestTextView(frameLayout.getContext(), "投硬币1分");
            createTestTextView6.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.20
                AnonymousClass20() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        ShjManager.setMoney(MoneyType.Coin, 1, "");
                    }
                }
            });
            arrayList.add(createTestTextView6);
            TextView createTestTextView7 = createTestTextView(frameLayout.getContext(), "投纸币1分");
            createTestTextView7.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.21
                AnonymousClass21() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        ShjManager.setMoney(MoneyType.Paper, 1, "");
                    }
                }
            });
            arrayList.add(createTestTextView7);
            TextView createTestTextView8 = createTestTextView(frameLayout.getContext(), "吞币");
            createTestTextView8.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.22
                AnonymousClass22() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        ShjManager.setMoney(MoneyType.EAT, 0, "吞币");
                    }
                }
            });
            arrayList.add(createTestTextView8);
            TextView createTestTextView9 = createTestTextView(frameLayout.getContext(), "测试云柜订单");
            createTestTextView9.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.23
                AnonymousClass23() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    YGOrderHelper.YG_OrderTask_Test();
                }
            });
            arrayList.add(createTestTextView9);
            TextView createTestTextView10 = createTestTextView(frameLayout.getContext(), "关柜门");
            createTestTextView10.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.24
                AnonymousClass24() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Context context = view.getContext();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(((TextView) view).getText());
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    linearLayout2.setOrientation(1);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                    layoutParams2.leftMargin = 40;
                    layoutParams2.rightMargin = 40;
                    EditText editText = new EditText(context);
                    editText.setText("");
                    editText.setHint("柜门编号");
                    editText.setInputType(SettingType.COMMODITY_ONE_BUTTON_SETUP);
                    editText.setLayoutParams(layoutParams2);
                    linearLayout2.addView(editText);
                    builder.setView(linearLayout2);
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.24.1
                        AnonymousClass1() {
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.24.2
                        final /* synthetic */ EditText val$pwdText;

                        AnonymousClass2(EditText editText2) {
                            editText = editText2;
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            try {
                                Shj.onShelfDoorStatusUpdated(Integer.parseInt(editText.getText().toString().trim()), 2);
                                InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.show();
                }

                /* renamed from: com.shj.biz.ShjManager$24$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$24$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$pwdText;

                    AnonymousClass2(EditText editText2) {
                        editText = editText2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Shj.onShelfDoorStatusUpdated(Integer.parseInt(editText.getText().toString().trim()), 2);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView10);
            TextView createTestTextView11 = createTestTextView(frameLayout.getContext(), "换电");
            createTestTextView11.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.25
                AnonymousClass25() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Context context = view.getContext();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(((TextView) view).getText());
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    linearLayout2.setOrientation(1);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                    layoutParams2.leftMargin = 40;
                    layoutParams2.rightMargin = 40;
                    EditText editText = new EditText(context);
                    editText.setText("");
                    editText.setHint("操作类型：0 换电，1归还，2借用");
                    editText.setInputType(SettingType.COMMODITY_ONE_BUTTON_SETUP);
                    editText.setLayoutParams(layoutParams2);
                    linearLayout2.addView(editText);
                    builder.setView(linearLayout2);
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.25.1
                        AnonymousClass1() {
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.25.2
                        final /* synthetic */ EditText val$pwdText;

                        AnonymousClass2(EditText editText2) {
                            editText = editText2;
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            try {
                                HdgOrderHelper.HDG_OrderTask_Test(Integer.parseInt(editText.getText().toString().trim()));
                                InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.show();
                }

                /* renamed from: com.shj.biz.ShjManager$25$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$25$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$pwdText;

                    AnonymousClass2(EditText editText2) {
                        editText = editText2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            HdgOrderHelper.HDG_OrderTask_Test(Integer.parseInt(editText.getText().toString().trim()));
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView11);
            TextView createTestTextView12 = createTestTextView(frameLayout.getContext(), "云柜订单结束");
            createTestTextView12.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.26
                AnonymousClass26() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    YGOrderHelper.YG_OrderTask_FinishOrder();
                }
            });
            arrayList.add(createTestTextView12);
            TextView createTestTextView13 = createTestTextView(frameLayout.getContext(), "清除缓存");
            createTestTextView13.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.27
                AnonymousClass27() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        ShjDbHelper.clear();
                    }
                }
            });
            arrayList.add(createTestTextView13);
            TextView createTestTextView14 = createTestTextView(frameLayout.getContext(), "打印货道和商品");
            createTestTextView14.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.28
                AnonymousClass28() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Loger.writeLog("PRINT", "----------------start-----------------");
                        Iterator<Integer> it = Shj.getShelves().iterator();
                        while (it.hasNext()) {
                            int intValue = it.next().intValue();
                            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
                            Loger.writeLog("PRINT", "shelf:" + intValue + " code:" + shelfInfo.getGoodsCode() + " status:" + shelfInfo.getStatus() + " isOK:" + shelfInfo.isStatusOK() + " pickcode_only:" + shelfInfo.getDatas("pickcode_only").toString() + " isClock:" + shelfInfo.getDatas("isClock").toString() + " count:" + shelfInfo.getGoodsCount());
                        }
                        for (String str : ShjManager.getGoodsManager().getGoodsKeys()) {
                            Loger.writeLog("PRINT", "----------goods key: " + str + " start-----------------");
                            for (Goods goods : ShjManager.getGoodsManager().getGoodsByKey(str, ";MFL;")) {
                                Loger.writeLog("PRINT", "goods:" + goods.getCode() + " keys:" + ObjectHelper.ary2String(goods.getKeys()) + " count:" + goods.getCount() + " reserveCount:" + goods.getReserveCount());
                            }
                            Loger.writeLog("PRINT", "----------goods key: " + str + " end-----------------");
                        }
                        Iterator<Integer> it2 = Shj.getShelves().iterator();
                        while (it2.hasNext()) {
                            int intValue2 = it2.next().intValue();
                            ShelfInfo shelfInfo2 = Shj.getShelfInfo(Integer.valueOf(intValue2));
                            Loger.writeLog("PRINT", "shelf:" + intValue2 + " code:" + shelfInfo2.getGoodsCode() + " status:" + shelfInfo2.getStatus() + " count:" + shelfInfo2.getGoodsCount());
                        }
                        Loger.writeLog("PRINT", "----------------end-----------------");
                    }
                }
            });
            arrayList.add(createTestTextView14);
            TextView createTestTextView15 = createTestTextView(frameLayout.getContext(), "卡货");
            createTestTextView15.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.29
                AnonymousClass29() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        if (!Shj.isDebugBlockOfferGoods()) {
                            Shj.setDebugBlockOfferGoods(true);
                            view.setBackgroundColor(Color.parseColor("#88FF0000"));
                        } else {
                            Shj.setDebugBlockOfferGoods(false);
                            view.setBackgroundColor(Color.parseColor("#88000000"));
                        }
                    }
                }
            });
            arrayList.add(createTestTextView15);
            TextView createTestTextView16 = createTestTextView(frameLayout.getContext(), "清除卡货");
            createTestTextView16.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.30
                AnonymousClass30() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        ShjManager.debugClearBlocks();
                    }
                }
            });
            arrayList.add(createTestTextView16);
            TextView createTestTextView17 = createTestTextView(frameLayout.getContext(), "货道暂停售卖");
            createTestTextView17.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.31
                AnonymousClass31() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        if (!Shj.isDebugShelfStopSale()) {
                            view.setBackgroundColor(Color.parseColor("#88FF0000"));
                            Shj.setDebugShelfStopSale(true);
                        } else {
                            view.setBackgroundColor(Color.parseColor("#88000000"));
                            Shj.setDebugShelfStopSale(false);
                        }
                    }
                }
            });
            arrayList.add(createTestTextView17);
            TextView createTestTextView18 = createTestTextView(frameLayout.getContext(), "货道缺货");
            createTestTextView18.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.32
                AnonymousClass32() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        if (!Shj.isDebugNoGoodsOnShelf()) {
                            view.setBackgroundColor(Color.parseColor("#88FF0000"));
                            Shj.setDebugNoGoodsOnShelf(true);
                        } else {
                            view.setBackgroundColor(Color.parseColor("#88000000"));
                            Shj.setDebugNoGoodsOnShelf(false);
                        }
                    }
                }
            });
            arrayList.add(createTestTextView18);
            TextView createTestTextView19 = createTestTextView(frameLayout.getContext(), "断网");
            createTestTextView19.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.33
                AnonymousClass33() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int parseColor;
                    if (ShjManager.isDebugLogined) {
                        int i2 = NetManager.getDebugModel() == 2 ? 0 : 2;
                        NetManager.setDebugModel(i2);
                        RequestHelper.setDebugModel(i2);
                        if (i2 == 2) {
                            parseColor = Color.parseColor("#88FF0000");
                        } else {
                            parseColor = Color.parseColor("#88000000");
                        }
                        view.setBackgroundColor(parseColor);
                    }
                }
            });
            arrayList.add(createTestTextView19);
            TextView createTestTextView20 = createTestTextView(frameLayout.getContext(), "高频断网");
            createTestTextView20.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.34
                AnonymousClass34() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int parseColor;
                    if (ShjManager.isDebugLogined) {
                        int i2 = NetManager.getDebugModel() == 1 ? 0 : 1;
                        NetManager.setDebugModel(i2);
                        RequestHelper.setDebugModel(i2);
                        if (i2 == 1) {
                            parseColor = Color.parseColor("#88FF0000");
                        } else {
                            parseColor = Color.parseColor("#88000000");
                        }
                        view.setBackgroundColor(parseColor);
                    }
                }
            });
            arrayList.add(createTestTextView20);
            TextView createTestTextView21 = createTestTextView(frameLayout.getContext(), "换端口");
            createTestTextView21.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.35
                AnonymousClass35() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        EditText editText2 = new EditText(context);
                        editText.setText(NetManager.getServerIp() == null ? "120.27.194.135" : NetManager.getServerIp());
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        editText2.setText(NetManager.getServerPort() == null ? ShjManager.defDebugServerPort : NetManager.getServerPort());
                        if (ShjManager.isDebugNoLogin()) {
                            editText2.setEnabled(false);
                        }
                        editText2.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText2);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.35.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.35.2
                            final /* synthetic */ EditText val$ipText;
                            final /* synthetic */ EditText val$portText;

                            AnonymousClass2(EditText editText3, EditText editText22) {
                                editText = editText3;
                                editText2 = editText22;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    NetManager.setServerIp(editText.getText().toString());
                                    NetManager.setServerPort(editText2.getText().toString());
                                    CacheHelper.getFileCache().put("TEST_IP", editText.getText().toString());
                                    CacheHelper.getFileCache().put("TEST_PORT", editText2.getText().toString());
                                    NetManager.getSocketProcessor().stop();
                                    NetManager.getSocketProcessor().setHost(NetManager.getServerIp(), Integer.parseInt(NetManager.getServerPort()));
                                    NetManager.getSocketProcessor().start(ShjManager.wkAppContext.get());
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$35$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$35$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$ipText;
                    final /* synthetic */ EditText val$portText;

                    AnonymousClass2(EditText editText3, EditText editText22) {
                        editText = editText3;
                        editText2 = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            NetManager.setServerIp(editText.getText().toString());
                            NetManager.setServerPort(editText2.getText().toString());
                            CacheHelper.getFileCache().put("TEST_IP", editText.getText().toString());
                            CacheHelper.getFileCache().put("TEST_PORT", editText2.getText().toString());
                            NetManager.getSocketProcessor().stop();
                            NetManager.getSocketProcessor().setHost(NetManager.getServerIp(), Integer.parseInt(NetManager.getServerPort()));
                            NetManager.getSocketProcessor().start(ShjManager.wkAppContext.get());
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView21);
            TextView createTestTextView22 = createTestTextView(frameLayout.getContext(), "测试机器号");
            createTestTextView22.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.36
                AnonymousClass36() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        editText.setText("");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.36.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.36.2
                            final /* synthetic */ EditText val$machineIdText;

                            AnonymousClass2(EditText editText2) {
                                editText = editText2;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Shj.setTestMachineId(editText.getText().toString());
                                    NetManager.setUser(Shj.getMachineId(), "000000");
                                    NetManager.getSocketProcessor().stop();
                                    NetManager.getSocketProcessor().start(ShjManager.wkAppContext.get());
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$36$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$36$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$machineIdText;

                    AnonymousClass2(EditText editText2) {
                        editText = editText2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Shj.setTestMachineId(editText.getText().toString());
                            NetManager.setUser(Shj.getMachineId(), "000000");
                            NetManager.getSocketProcessor().stop();
                            NetManager.getSocketProcessor().start(ShjManager.wkAppContext.get());
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView22);
            TextView createTestTextView23 = createTestTextView(frameLayout.getContext(), "测试优惠码");
            createTestTextView23.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.37
                AnonymousClass37() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        editText.setText("");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.37.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.37.2
                            final /* synthetic */ EditText val$yhCodeText;

                            AnonymousClass2(EditText editText2) {
                                editText = editText2;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    ShjManager.putData("YHM", editText.getText().toString());
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$37$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$37$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$yhCodeText;

                    AnonymousClass2(EditText editText2) {
                        editText = editText2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            ShjManager.putData("YHM", editText.getText().toString());
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView23);
            TextView createTestTextView24 = createTestTextView(frameLayout.getContext(), "测二维码");
            createTestTextView24.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.38
                AnonymousClass38() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Report_OnlinePay_Apply report_OnlinePay_Apply = new Report_OnlinePay_Apply();
                        report_OnlinePay_Apply.setParams(1, 1, "NA", Shj.getMachineId(), "1", String.format("%03d", 1), 1, "测试", "1");
                        NetManager.appendReport(report_OnlinePay_Apply);
                    }
                }
            });
            arrayList.add(createTestTextView24);
            TextView createTestTextView25 = createTestTextView(frameLayout.getContext(), "取货口有商品");
            createTestTextView25.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.39
                AnonymousClass39() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int parseColor;
                    if (ShjManager.isDebugLogined) {
                        boolean unused = ShjManager.debugHasGoodsInPickdoor = !ShjManager.debugHasGoodsInPickdoor;
                        Shj.debugOfferDivicError(ShjManager.debugHasGoodsInPickdoor ? 5 : 0);
                        if (ShjManager.debugHasGoodsInPickdoor) {
                            parseColor = Color.parseColor("#88FF0000");
                        } else {
                            parseColor = Color.parseColor("#88000000");
                        }
                        view.setBackgroundColor(parseColor);
                    }
                }
            });
            arrayList.add(createTestTextView25);
            TextView createTestTextView26 = createTestTextView(frameLayout.getContext(), "测支付不出货");
            createTestTextView26.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.40
                AnonymousClass40() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int parseColor;
                    if (ShjManager.isDebugLogined) {
                        boolean unused = ShjManager.testNoOfferGoodsOnPay = !ShjManager.testNoOfferGoodsOnPay;
                        if (ShjManager.testNoOfferGoodsOnPay) {
                            parseColor = Color.parseColor("#88FF0000");
                        } else {
                            parseColor = Color.parseColor("#88000000");
                        }
                        view.setBackgroundColor(parseColor);
                    }
                }
            });
            arrayList.add(createTestTextView26);
            TextView createTestTextView27 = createTestTextView(frameLayout.getContext(), "取货口异常");
            createTestTextView27.setTag(0);
            createTestTextView27.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.41
                AnonymousClass41() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int parseColor;
                    if (ShjManager.isDebugLogined) {
                        view.setTag(Integer.valueOf(((Integer) view.getTag()).intValue() == 0 ? 1 : 0));
                        if (((Integer) view.getTag()).intValue() == 1) {
                            parseColor = Color.parseColor("#88FF0000");
                        } else {
                            parseColor = Color.parseColor("#88000000");
                        }
                        view.setBackgroundColor(parseColor);
                        Shj.debugOfferDivicError(((Integer) view.getTag()).intValue() == 1 ? 1 : 0);
                    }
                }
            });
            arrayList.add(createTestTextView27);
            TextView createTestTextView28 = createTestTextView(frameLayout.getContext(), "出货故障");
            createTestTextView28.setTag(0);
            createTestTextView28.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.42
                AnonymousClass42() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int parseColor;
                    if (ShjManager.isDebugLogined) {
                        view.setTag(Integer.valueOf(((Integer) view.getTag()).intValue() == 0 ? 1 : 0));
                        if (((Integer) view.getTag()).intValue() == 1) {
                            parseColor = Color.parseColor("#88FF0000");
                        } else {
                            parseColor = Color.parseColor("#88000000");
                        }
                        view.setBackgroundColor(parseColor);
                        Shj.debugOfferGoodsError(((Integer) view.getTag()).intValue() == 1);
                    }
                }
            });
            arrayList.add(createTestTextView28);
            TextView createTestTextView29 = createTestTextView(frameLayout.getContext(), "测获取机器码");
            createTestTextView29.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.43
                AnonymousClass43() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        editText.setText("");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.43.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.43.2
                            final /* synthetic */ EditText val$imeiIdText;

                            AnonymousClass2(EditText editText2) {
                                editText = editText2;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    String obj = editText.getText().toString();
                                    Report_Con_QMachineId report_Con_QMachineId = new Report_Con_QMachineId();
                                    report_Con_QMachineId.setParams(obj);
                                    NetManager.appendReport(report_Con_QMachineId);
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$43$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$43$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$imeiIdText;

                    AnonymousClass2(EditText editText2) {
                        editText = editText2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj = editText.getText().toString();
                            Report_Con_QMachineId report_Con_QMachineId = new Report_Con_QMachineId();
                            report_Con_QMachineId.setParams(obj);
                            NetManager.appendReport(report_Con_QMachineId);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView29);
            TextView createTestTextView30 = createTestTextView(frameLayout.getContext(), "测试会员卡");
            createTestTextView30.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.44
                AnonymousClass44() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        editText.setText("");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.44.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.44.2
                            final /* synthetic */ EditText val$imeiIdText;

                            AnonymousClass2(EditText editText2) {
                                editText = editText2;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    String obj = editText.getText().toString();
                                    ShjManager.selectGoodsOnShelf(2);
                                    new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.44.2.1
                                        final /* synthetic */ String val$imei;

                                        AnonymousClass1(String obj2) {
                                            obj = obj2;
                                        }

                                        @Override // java.util.TimerTask, java.lang.Runnable
                                        public void run() {
                                            ShjManager.getBizShjListener()._onNeedICCardPay(1, obj, "");
                                        }
                                    }, 2000L);
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            /* renamed from: com.shj.biz.ShjManager$44$2$1 */
                            /* loaded from: classes2.dex */
                            class AnonymousClass1 extends TimerTask {
                                final /* synthetic */ String val$imei;

                                AnonymousClass1(String obj2) {
                                    obj = obj2;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    ShjManager.getBizShjListener()._onNeedICCardPay(1, obj, "");
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$44$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: com.shj.biz.ShjManager$44$2 */
                /* loaded from: classes2.dex */
                public class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$imeiIdText;

                    AnonymousClass2(EditText editText2) {
                        editText = editText2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj2 = editText.getText().toString();
                            ShjManager.selectGoodsOnShelf(2);
                            new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.44.2.1
                                final /* synthetic */ String val$imei;

                                AnonymousClass1(String obj22) {
                                    obj = obj22;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    ShjManager.getBizShjListener()._onNeedICCardPay(1, obj, "");
                                }
                            }, 2000L);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    /* renamed from: com.shj.biz.ShjManager$44$2$1 */
                    /* loaded from: classes2.dex */
                    class AnonymousClass1 extends TimerTask {
                        final /* synthetic */ String val$imei;

                        AnonymousClass1(String obj22) {
                            obj = obj22;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            ShjManager.getBizShjListener()._onNeedICCardPay(1, obj, "");
                        }
                    }
                }
            });
            arrayList.add(createTestTextView30);
            TextView createTestTextView31 = createTestTextView(frameLayout.getContext(), "测试新平台会员卡");
            createTestTextView31.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.45
                AnonymousClass45() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        editText.setText("");
                        editText.setHint("指令类型:1申请扣款,2查余额");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        EditText editText2 = new EditText(context);
                        editText2.setText("");
                        editText2.setHint("物理卡号");
                        editText2.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText2);
                        EditText editText3 = new EditText(context);
                        editText3.setText("");
                        editText3.setHint("卡扩展信息");
                        editText3.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText3);
                        EditText editText4 = new EditText(context);
                        editText4.setText("");
                        editText4.setHint("货道编号");
                        editText4.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText4);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.45.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.45.2
                            final /* synthetic */ EditText val$imeiCardText;
                            final /* synthetic */ EditText val$imeiExText;
                            final /* synthetic */ EditText val$imeiGoodsCodeText;
                            final /* synthetic */ EditText val$imeiTypeText;

                            AnonymousClass2(EditText editText5, EditText editText22, EditText editText32, EditText editText42) {
                                editText = editText5;
                                editText2 = editText22;
                                editText3 = editText32;
                                editText4 = editText42;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    String obj = editText.getText().toString();
                                    String obj2 = editText2.getText().toString();
                                    editText3.getText().toString();
                                    ShjManager.selectGoodsOnShelf(Integer.parseInt(editText4.getText().toString()));
                                    new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.45.2.1
                                        final /* synthetic */ String val$card;
                                        final /* synthetic */ String val$type;

                                        AnonymousClass1(String obj3, String obj22) {
                                            obj = obj3;
                                            obj2 = obj22;
                                        }

                                        @Override // java.util.TimerTask, java.lang.Runnable
                                        public void run() {
                                            Shj.onNeedICCardPay(Integer.parseInt(obj), obj2);
                                        }
                                    }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText2.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                                    InputMethodManager inputMethodManager2 = (InputMethodManager) editText3.getContext().getSystemService("input_method");
                                    inputMethodManager2.hideSoftInputFromWindow(editText3.getWindowToken(), 0);
                                    inputMethodManager2.hideSoftInputFromWindow(editText3.getWindowToken(), 0);
                                    InputMethodManager inputMethodManager3 = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager3.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager3.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    InputMethodManager inputMethodManager4 = (InputMethodManager) editText4.getContext().getSystemService("input_method");
                                    inputMethodManager4.hideSoftInputFromWindow(editText4.getWindowToken(), 0);
                                    inputMethodManager4.hideSoftInputFromWindow(editText4.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            /* renamed from: com.shj.biz.ShjManager$45$2$1 */
                            /* loaded from: classes2.dex */
                            class AnonymousClass1 extends TimerTask {
                                final /* synthetic */ String val$card;
                                final /* synthetic */ String val$type;

                                AnonymousClass1(String obj3, String obj22) {
                                    obj = obj3;
                                    obj2 = obj22;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    Shj.onNeedICCardPay(Integer.parseInt(obj), obj2);
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$45$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: com.shj.biz.ShjManager$45$2 */
                /* loaded from: classes2.dex */
                public class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$imeiCardText;
                    final /* synthetic */ EditText val$imeiExText;
                    final /* synthetic */ EditText val$imeiGoodsCodeText;
                    final /* synthetic */ EditText val$imeiTypeText;

                    AnonymousClass2(EditText editText5, EditText editText22, EditText editText32, EditText editText42) {
                        editText = editText5;
                        editText2 = editText22;
                        editText3 = editText32;
                        editText4 = editText42;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj3 = editText.getText().toString();
                            String obj22 = editText2.getText().toString();
                            editText3.getText().toString();
                            ShjManager.selectGoodsOnShelf(Integer.parseInt(editText4.getText().toString()));
                            new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.45.2.1
                                final /* synthetic */ String val$card;
                                final /* synthetic */ String val$type;

                                AnonymousClass1(String obj32, String obj222) {
                                    obj = obj32;
                                    obj2 = obj222;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    Shj.onNeedICCardPay(Integer.parseInt(obj), obj2);
                                }
                            }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText2.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            InputMethodManager inputMethodManager2 = (InputMethodManager) editText3.getContext().getSystemService("input_method");
                            inputMethodManager2.hideSoftInputFromWindow(editText3.getWindowToken(), 0);
                            inputMethodManager2.hideSoftInputFromWindow(editText3.getWindowToken(), 0);
                            InputMethodManager inputMethodManager3 = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager3.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager3.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            InputMethodManager inputMethodManager4 = (InputMethodManager) editText4.getContext().getSystemService("input_method");
                            inputMethodManager4.hideSoftInputFromWindow(editText4.getWindowToken(), 0);
                            inputMethodManager4.hideSoftInputFromWindow(editText4.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    /* renamed from: com.shj.biz.ShjManager$45$2$1 */
                    /* loaded from: classes2.dex */
                    class AnonymousClass1 extends TimerTask {
                        final /* synthetic */ String val$card;
                        final /* synthetic */ String val$type;

                        AnonymousClass1(String obj32, String obj222) {
                            obj = obj32;
                            obj2 = obj222;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            Shj.onNeedICCardPay(Integer.parseInt(obj), obj2);
                        }
                    }
                }
            });
            arrayList.add(createTestTextView31);
            TextView createTestTextView32 = createTestTextView(frameLayout.getContext(), "测试扫码头");
            createTestTextView32.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.46
                AnonymousClass46() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        editText.setText("");
                        editText.setHint("扫码或输入码值");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.46.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.46.2
                            final /* synthetic */ EditText val$imeiIdText;

                            AnonymousClass2(EditText editText2) {
                                editText = editText2;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.46.2.1
                                        final /* synthetic */ String val$imei;

                                        AnonymousClass1(String str) {
                                            obj = str;
                                        }

                                        @Override // java.util.TimerTask, java.lang.Runnable
                                        public void run() {
                                            ShjManager.getStatusListener().onMessage("SCANOR", obj);
                                        }
                                    }, 2000L);
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            /* renamed from: com.shj.biz.ShjManager$46$2$1 */
                            /* loaded from: classes2.dex */
                            class AnonymousClass1 extends TimerTask {
                                final /* synthetic */ String val$imei;

                                AnonymousClass1(String str) {
                                    obj = str;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    ShjManager.getStatusListener().onMessage("SCANOR", obj);
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$46$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: com.shj.biz.ShjManager$46$2 */
                /* loaded from: classes2.dex */
                public class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$imeiIdText;

                    AnonymousClass2(EditText editText2) {
                        editText = editText2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.46.2.1
                                final /* synthetic */ String val$imei;

                                AnonymousClass1(String str) {
                                    obj = str;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    ShjManager.getStatusListener().onMessage("SCANOR", obj);
                                }
                            }, 2000L);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    /* renamed from: com.shj.biz.ShjManager$46$2$1 */
                    /* loaded from: classes2.dex */
                    class AnonymousClass1 extends TimerTask {
                        final /* synthetic */ String val$imei;

                        AnonymousClass1(String str) {
                            obj = str;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            ShjManager.getStatusListener().onMessage("SCANOR", obj);
                        }
                    }
                }
            });
            arrayList.add(createTestTextView32);
            TextView createTestTextView33 = createTestTextView(frameLayout.getContext(), "下位机上报指令测试");
            createTestTextView33.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.47
                AnonymousClass47() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        editText.setText("");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.47.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.47.2
                            final /* synthetic */ EditText val$cmdText;

                            AnonymousClass2(EditText editText2) {
                                editText = editText2;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    String obj = editText.getText().toString();
                                    ArrayList arrayList2 = new ArrayList();
                                    String replace = obj.replace(StringUtils.SPACE, "");
                                    int i3 = 0;
                                    while (i3 < replace.length()) {
                                        int i4 = i3 + 2;
                                        arrayList2.add(Integer.valueOf(ObjectHelper.string2Short(replace.substring(i3, i4))));
                                        i3 = i4;
                                    }
                                    int size = arrayList2.size() - 0;
                                    byte[] bArr = new byte[size];
                                    for (int i5 = 0; i5 < size - 1; i5++) {
                                        bArr[i5 + 0] = (byte) ((Integer) arrayList2.get(i5)).intValue();
                                    }
                                    Loger.writeLog("COMMAND", ObjectHelper.hex2String(bArr, size));
                                    CommandManager.appendReceivedRawCommand(bArr);
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$47$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$47$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$cmdText;

                    AnonymousClass2(EditText editText2) {
                        editText = editText2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj = editText.getText().toString();
                            ArrayList arrayList2 = new ArrayList();
                            String replace = obj.replace(StringUtils.SPACE, "");
                            int i3 = 0;
                            while (i3 < replace.length()) {
                                int i4 = i3 + 2;
                                arrayList2.add(Integer.valueOf(ObjectHelper.string2Short(replace.substring(i3, i4))));
                                i3 = i4;
                            }
                            int size = arrayList2.size() - 0;
                            byte[] bArr = new byte[size];
                            for (int i5 = 0; i5 < size - 1; i5++) {
                                bArr[i5 + 0] = (byte) ((Integer) arrayList2.get(i5)).intValue();
                            }
                            Loger.writeLog("COMMAND", ObjectHelper.hex2String(bArr, size));
                            CommandManager.appendReceivedRawCommand(bArr);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView33);
            TextView createTestTextView34 = createTestTextView(frameLayout.getContext(), "下位机选货");
            createTestTextView34.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.48
                AnonymousClass48() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        editText.setText("");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.48.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.48.2
                            final /* synthetic */ EditText val$shelfText;

                            AnonymousClass2(EditText editText2) {
                                editText = editText2;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.48.2.1
                                        final /* synthetic */ String val$shelf;

                                        AnonymousClass1(String str) {
                                            obj = str;
                                        }

                                        @Override // java.util.TimerTask, java.lang.Runnable
                                        public void run() {
                                            ShjManager.selectGoodsOnShelf(Integer.parseInt(obj));
                                        }
                                    }, 2000L);
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            /* renamed from: com.shj.biz.ShjManager$48$2$1 */
                            /* loaded from: classes2.dex */
                            class AnonymousClass1 extends TimerTask {
                                final /* synthetic */ String val$shelf;

                                AnonymousClass1(String str) {
                                    obj = str;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    ShjManager.selectGoodsOnShelf(Integer.parseInt(obj));
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$48$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: com.shj.biz.ShjManager$48$2 */
                /* loaded from: classes2.dex */
                public class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$shelfText;

                    AnonymousClass2(EditText editText2) {
                        editText = editText2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.48.2.1
                                final /* synthetic */ String val$shelf;

                                AnonymousClass1(String str) {
                                    obj = str;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    ShjManager.selectGoodsOnShelf(Integer.parseInt(obj));
                                }
                            }, 2000L);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    /* renamed from: com.shj.biz.ShjManager$48$2$1 */
                    /* loaded from: classes2.dex */
                    class AnonymousClass1 extends TimerTask {
                        final /* synthetic */ String val$shelf;

                        AnonymousClass1(String str) {
                            obj = str;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            ShjManager.selectGoodsOnShelf(Integer.parseInt(obj));
                        }
                    }
                }
            });
            arrayList.add(createTestTextView34);
            TextView createTestTextView35 = createTestTextView(frameLayout.getContext(), "设商品编码");
            createTestTextView35.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.49
                AnonymousClass49() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        EditText editText2 = new EditText(context);
                        editText.setText("1");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        editText2.setText("1");
                        editText2.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText2);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.49.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.49.2
                            final /* synthetic */ EditText val$priceText;
                            final /* synthetic */ EditText val$shelfText;

                            AnonymousClass2(EditText editText3, EditText editText22) {
                                editText = editText3;
                                editText2 = editText22;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    String obj = editText.getText().toString();
                                    ShjManager.setShelfGoodsCode(Integer.parseInt(obj), editText2.getText().toString());
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$49$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$49$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$priceText;
                    final /* synthetic */ EditText val$shelfText;

                    AnonymousClass2(EditText editText3, EditText editText22) {
                        editText = editText3;
                        editText2 = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj = editText.getText().toString();
                            ShjManager.setShelfGoodsCode(Integer.parseInt(obj), editText2.getText().toString());
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView35);
            TextView createTestTextView36 = createTestTextView(frameLayout.getContext(), "设价格");
            createTestTextView36.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.50
                AnonymousClass50() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        EditText editText2 = new EditText(context);
                        editText.setText("1");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        editText2.setText("1");
                        editText2.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText2);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.50.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.50.2
                            final /* synthetic */ EditText val$priceText;
                            final /* synthetic */ EditText val$shelfText;

                            AnonymousClass2(EditText editText3, EditText editText22) {
                                editText = editText3;
                                editText2 = editText22;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    ShjManager.setShelfGoodsPrice(Integer.parseInt(editText.getText().toString()), Integer.parseInt(editText2.getText().toString()));
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$50$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$50$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$priceText;
                    final /* synthetic */ EditText val$shelfText;

                    AnonymousClass2(EditText editText3, EditText editText22) {
                        editText = editText3;
                        editText2 = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            ShjManager.setShelfGoodsPrice(Integer.parseInt(editText.getText().toString()), Integer.parseInt(editText2.getText().toString()));
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView36);
            TextView createTestTextView37 = createTestTextView(frameLayout.getContext(), "设库存");
            createTestTextView37.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.51
                AnonymousClass51() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        EditText editText2 = new EditText(context);
                        editText.setText("1");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        editText2.setText("1");
                        editText2.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText2);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.51.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.51.2
                            final /* synthetic */ EditText val$countText;
                            final /* synthetic */ EditText val$shelfText;

                            AnonymousClass2(EditText editText3, EditText editText22) {
                                editText = editText3;
                                editText2 = editText22;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    ShjManager.setShelfGoodsCount(Integer.parseInt(editText.getText().toString()), Integer.parseInt(editText2.getText().toString()));
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$51$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$51$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$countText;
                    final /* synthetic */ EditText val$shelfText;

                    AnonymousClass2(EditText editText3, EditText editText22) {
                        editText = editText3;
                        editText2 = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            ShjManager.setShelfGoodsCount(Integer.parseInt(editText.getText().toString()), Integer.parseInt(editText2.getText().toString()));
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView37);
            TextView createTestTextView38 = createTestTextView(frameLayout.getContext(), "有人靠近");
            createTestTextView38.setTag("0");
            createTestTextView38.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.52
                AnonymousClass52() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int parseColor;
                    if (ShjManager.isDebugLogined) {
                        view.setTag(view.getTag().toString() == "0" ? "1" : "0");
                        if (view.getTag().toString() == "1") {
                            parseColor = Color.parseColor("#88FF0000");
                        } else {
                            parseColor = Color.parseColor("#88000000");
                        }
                        view.setBackgroundColor(parseColor);
                    }
                }
            });
            arrayList.add(createTestTextView38);
            TextView createTestTextView39 = createTestTextView(frameLayout.getContext(), "盒饭机");
            createTestTextView39.setTag("0");
            createTestTextView39.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.53
                AnonymousClass53() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int parseColor;
                    if (ShjManager.isDebugLogined) {
                        view.setTag(view.getTag().toString() == "0" ? "1" : "0");
                        if (view.getTag().toString() == "1") {
                            parseColor = Color.parseColor("#88FF0000");
                        } else {
                            parseColor = Color.parseColor("#88000000");
                        }
                        view.setBackgroundColor(parseColor);
                        Shj.setDebugMachineType(view.getTag().toString() == "1" ? 1 : 0);
                    }
                }
            });
            arrayList.add(createTestTextView39);
            TextView createTestTextView40 = createTestTextView(frameLayout.getContext(), "打开主柜机");
            createTestTextView40.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.54
                AnonymousClass54() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Shj.getMachine(0, false).setDoorIsOpen(true);
                        ShjManager.getStatusListener().onDoorStatusChanged(0, true);
                    }
                }
            });
            arrayList.add(createTestTextView40);
            TextView createTestTextView41 = createTestTextView(frameLayout.getContext(), "关闭主柜机");
            createTestTextView41.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.55
                AnonymousClass55() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Shj.getMachine(0, false).setDoorIsOpen(false);
                    }
                }
            });
            arrayList.add(createTestTextView41);
            TextView createTestTextView42 = createTestTextView(frameLayout.getContext(), "表情刷卡打印");
            createTestTextView42.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.56
                AnonymousClass56() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        try {
                            WinCarcdReader.get().onMessage("#SOPrint*GALLER CH\n       SPIRE SPU90\n IDEAL SOLUTIONS COMPANY\n       QIC TESTING\n\nTID :           35010122\nMID:     350100144030110\n\n<CARDHOLDER COPY>\n\nCUP            \n625809******2907\nAID: A000000333010102\nCGB PBOC CREDIT\nSALE\n\n\nINVOICE: 000012\n\n[06-05 14:16:42:305] DATE: JUN 05, 20\nTIME:  09:16:05\n\nBATCH: 000001\n\nTOTAL           QAR 0.01\n\u001eTRANSACTION \u001f\n#EO");
                        } catch (Exception unused) {
                        }
                    }
                }
            });
            arrayList.add(createTestTextView42);
            TextView createTestTextView43 = createTestTextView(frameLayout.getContext(), "第三方支付出货");
            createTestTextView43.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.57
                AnonymousClass57() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(((TextView) view).getText());
                        LinearLayout linearLayout2 = new LinearLayout(context);
                        linearLayout2.setOrientation(1);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                        layoutParams2.leftMargin = 40;
                        layoutParams2.rightMargin = 40;
                        EditText editText = new EditText(context);
                        editText.setText("");
                        editText.setHint("输入货道号");
                        editText.setLayoutParams(layoutParams2);
                        linearLayout2.addView(editText);
                        builder.setView(linearLayout2);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.57.1
                            AnonymousClass1() {
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                    declaredField.setAccessible(true);
                                    declaredField.set(dialogInterface, true);
                                } catch (Exception unused) {
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.57.2
                            final /* synthetic */ EditText val$imeiIdText;

                            AnonymousClass2(EditText editText2) {
                                editText = editText2;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                try {
                                    String obj = editText.getText().toString();
                                    String str = "THIRDORDER" + System.currentTimeMillis();
                                    ShjManager.getOrderManager().driverThirdPayOrder(str, Shj.getShelfInfo(Integer.valueOf(Integer.parseInt(obj))).getPrice().intValue(), OrderPayType.ThridPay, str, "" + obj);
                                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    try {
                                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        declaredField.setAccessible(true);
                                        declaredField.set(dialogInterface, true);
                                    } catch (Exception unused) {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.show();
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$57$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements DialogInterface.OnClickListener {
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                }

                /* renamed from: com.shj.biz.ShjManager$57$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements DialogInterface.OnClickListener {
                    final /* synthetic */ EditText val$imeiIdText;

                    AnonymousClass2(EditText editText2) {
                        editText = editText2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj = editText.getText().toString();
                            String str = "THIRDORDER" + System.currentTimeMillis();
                            ShjManager.getOrderManager().driverThirdPayOrder(str, Shj.getShelfInfo(Integer.valueOf(Integer.parseInt(obj))).getPrice().intValue(), OrderPayType.ThridPay, str, "" + obj);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            arrayList.add(createTestTextView43);
            TextView createTestTextView44 = createTestTextView(frameLayout.getContext(), "隐藏按扭");
            createTestTextView44.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.58
                final /* synthetic */ List val$textViewList;

                AnonymousClass58(List arrayList2) {
                    arrayList = arrayList2;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            ((TextView) it.next()).setVisibility(8);
                        }
                    }
                }
            });
            arrayList2.add(createTestTextView44);
            TextView createTestTextView45 = createTestTextView(frameLayout.getContext(), "打印");
            createTestTextView45.setOnClickListener(new View.OnClickListener() { // from class: com.shj.biz.ShjManager.59
                AnonymousClass59() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ShjManager.isDebugLogined) {
                        ShjManager.getStatusListener().onMessage("WinCarcdReader", "Print*打印测试\n-------------\n测试商品  999 1件");
                    }
                }
            });
            arrayList2.add(createTestTextView45);
            int i2 = frameLayout.getContext().getResources().getDisplayMetrics().widthPixels;
            TextPaint paint = createTestTextView41.getPaint();
            float f = 0.0f;
            LinearLayout linearLayout2 = new LinearLayout(frameLayout.getContext());
            linearLayout2.setOrientation(0);
            linearLayout.addView(linearLayout2);
            for (TextView textView : arrayList2) {
                float measureText = paint.measureText(textView.getText().toString());
                if (f + measureText > i2) {
                    LinearLayout linearLayout3 = new LinearLayout(frameLayout.getContext());
                    linearLayout3.setOrientation(0);
                    linearLayout.addView(linearLayout3);
                    linearLayout2 = linearLayout3;
                    f = measureText + 30.0f;
                } else {
                    f += measureText + 30.0f;
                }
                linearLayout2.addView(textView);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$15 */
    /* loaded from: classes2.dex */
    public class AnonymousClass15 implements View.OnClickListener {
        AnonymousClass15() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Context context = view.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(((TextView) view).getText());
            builder.setCancelable(false);
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(1);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
            layoutParams2.leftMargin = 40;
            layoutParams2.rightMargin = 40;
            EditText editText2 = new EditText(context);
            editText2.setText("");
            editText2.setInputType(SettingType.COMMODITY_ONE_BUTTON_SETUP);
            editText2.setLayoutParams(layoutParams2);
            linearLayout2.addView(editText2);
            builder.setView(linearLayout2);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.15.1
                AnonymousClass1() {
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.15.2
                final /* synthetic */ EditText val$pwdText;

                AnonymousClass2(EditText editText22) {
                    editText = editText22;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    try {
                        if (editText.getText().toString().trim().equals("xykj2016123")) {
                            ShjManager.isDebugLogined = true;
                        }
                        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        }

        /* renamed from: com.shj.biz.ShjManager$15$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$15$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$pwdText;

            AnonymousClass2(EditText editText22) {
                editText = editText22;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    if (editText.getText().toString().trim().equals("xykj2016123")) {
                        ShjManager.isDebugLogined = true;
                    }
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$16 */
    /* loaded from: classes2.dex */
    class AnonymousClass16 implements View.OnClickListener {
        AnonymousClass16() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                ShjManager.queryServerTime();
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$17 */
    /* loaded from: classes2.dex */
    class AnonymousClass17 implements View.OnClickListener {
        AnonymousClass17() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            List list;
            if (ShjManager.isDebugLogined) {
                try {
                    List<Integer> shelves = Shj.getShelves();
                    HashMap hashMap = new HashMap();
                    Iterator<Integer> it = shelves.iterator();
                    while (it.hasNext()) {
                        int intValue = it.next().intValue();
                        int layerByShelf = Shj.getLayerByShelf(intValue);
                        if (hashMap.containsKey(Integer.valueOf(layerByShelf))) {
                            list = (List) hashMap.get(Integer.valueOf(layerByShelf));
                        } else {
                            ArrayList arrayList2 = new ArrayList();
                            hashMap.put(Integer.valueOf(layerByShelf), arrayList2);
                            list = arrayList2;
                        }
                        list.add(Integer.valueOf(intValue));
                    }
                    Iterator it2 = hashMap.keySet().iterator();
                    while (it2.hasNext()) {
                        try {
                            for (Integer num : (List) hashMap.get(Integer.valueOf(((Integer) it2.next()).intValue()))) {
                                ShelfInfo shelfInfo = Shj.getShelfInfo(num);
                                Loger.writeLog("状态", "shelf:" + num + " zt:" + shelfInfo.getStatus() + " spbm:" + shelfInfo.getGoodsCode() + " hdjg:" + shelfInfo.getPrice() + " hdcl:" + shelfInfo.getGoodsCount() + " hdrl:" + shelfInfo.getCapacity());
                            }
                        } catch (Exception e) {
                            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                        }
                    }
                } catch (Exception e2) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
                }
                for (Goods goods : ShjManager.getGoodsManager().getAllGoods()) {
                    Loger.writeLog("状态", "goodsCode:" + goods.getCode() + " name:" + goods.getName() + " count:" + goods.getCount() + " price:" + goods.getPrice());
                }
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$18 */
    /* loaded from: classes2.dex */
    class AnonymousClass18 implements View.OnClickListener {
        AnonymousClass18() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                try {
                    double d = ShjManager.temperature;
                    double random = Math.random() * 4.0d;
                    Double.isNaN(d);
                    ShjManager.temperature = (int) (d + random);
                    int i2 = 1;
                    ShjManager.paperMState = ShjManager.paperMState == 0 ? 1 : 0;
                    if (ShjManager.coinMState != 0) {
                        i2 = 0;
                    }
                    ShjManager.coinMState = i2;
                    double d2 = ShjManager.paperMoney;
                    double random2 = Math.random() * 10.0d;
                    Double.isNaN(d2);
                    ShjManager.paperMoney = (int) (d2 + random2);
                    double d3 = ShjManager.coinMoney;
                    double random3 = Math.random() * 10.0d;
                    Double.isNaN(d3);
                    ShjManager.coinMoney = (int) (d3 + random3);
                    Shj.onUpdateShjStatusV2(ShjManager.paperMState, ShjManager.coinMState, 0, 0, ShjManager.temperature, ShjManager.doorState, ShjManager.paperMoney, ShjManager.coinMoney, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
                    Loger.writeLog("SHJ", "paperMachineState:" + ShjManager.paperMState + " coinMachineState:" + ShjManager.coinMState + " posMachineState:0 wkqState:0 temperature:" + ShjManager.temperature + " paperMoney:" + ShjManager.paperMoney + " coinMoney:" + ShjManager.coinMoney);
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$19 */
    /* loaded from: classes2.dex */
    public class AnonymousClass19 implements View.OnClickListener {
        AnonymousClass19() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                try {
                    Shj.setStoped(true);
                    Shj.onUpdateShjStatus(VMCStatus.LiftChecking);
                    new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.19.1
                        AnonymousClass1() {
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            Shj.setStoped(false);
                            Shj.onUpdateShjStatus(VMCStatus.Normal);
                        }
                    }, 10000L);
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$19$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 extends TimerTask {
            AnonymousClass1() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                Shj.setStoped(false);
                Shj.onUpdateShjStatus(VMCStatus.Normal);
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$20 */
    /* loaded from: classes2.dex */
    class AnonymousClass20 implements View.OnClickListener {
        AnonymousClass20() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                ShjManager.setMoney(MoneyType.Coin, 1, "");
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$21 */
    /* loaded from: classes2.dex */
    class AnonymousClass21 implements View.OnClickListener {
        AnonymousClass21() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                ShjManager.setMoney(MoneyType.Paper, 1, "");
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$22 */
    /* loaded from: classes2.dex */
    class AnonymousClass22 implements View.OnClickListener {
        AnonymousClass22() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                ShjManager.setMoney(MoneyType.EAT, 0, "吞币");
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$23 */
    /* loaded from: classes2.dex */
    class AnonymousClass23 implements View.OnClickListener {
        AnonymousClass23() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            YGOrderHelper.YG_OrderTask_Test();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$24 */
    /* loaded from: classes2.dex */
    public class AnonymousClass24 implements View.OnClickListener {
        AnonymousClass24() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Context context = view.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(((TextView) view).getText());
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(1);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
            layoutParams2.leftMargin = 40;
            layoutParams2.rightMargin = 40;
            EditText editText2 = new EditText(context);
            editText2.setText("");
            editText2.setHint("柜门编号");
            editText2.setInputType(SettingType.COMMODITY_ONE_BUTTON_SETUP);
            editText2.setLayoutParams(layoutParams2);
            linearLayout2.addView(editText2);
            builder.setView(linearLayout2);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.24.1
                AnonymousClass1() {
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.24.2
                final /* synthetic */ EditText val$pwdText;

                AnonymousClass2(EditText editText22) {
                    editText = editText22;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    try {
                        Shj.onShelfDoorStatusUpdated(Integer.parseInt(editText.getText().toString().trim()), 2);
                        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        }

        /* renamed from: com.shj.biz.ShjManager$24$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$24$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$pwdText;

            AnonymousClass2(EditText editText22) {
                editText = editText22;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Shj.onShelfDoorStatusUpdated(Integer.parseInt(editText.getText().toString().trim()), 2);
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$25 */
    /* loaded from: classes2.dex */
    public class AnonymousClass25 implements View.OnClickListener {
        AnonymousClass25() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Context context = view.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(((TextView) view).getText());
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(1);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
            layoutParams2.leftMargin = 40;
            layoutParams2.rightMargin = 40;
            EditText editText2 = new EditText(context);
            editText2.setText("");
            editText2.setHint("操作类型：0 换电，1归还，2借用");
            editText2.setInputType(SettingType.COMMODITY_ONE_BUTTON_SETUP);
            editText2.setLayoutParams(layoutParams2);
            linearLayout2.addView(editText2);
            builder.setView(linearLayout2);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.25.1
                AnonymousClass1() {
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.25.2
                final /* synthetic */ EditText val$pwdText;

                AnonymousClass2(EditText editText22) {
                    editText = editText22;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    try {
                        HdgOrderHelper.HDG_OrderTask_Test(Integer.parseInt(editText.getText().toString().trim()));
                        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        }

        /* renamed from: com.shj.biz.ShjManager$25$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$25$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$pwdText;

            AnonymousClass2(EditText editText22) {
                editText = editText22;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    HdgOrderHelper.HDG_OrderTask_Test(Integer.parseInt(editText.getText().toString().trim()));
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$26 */
    /* loaded from: classes2.dex */
    class AnonymousClass26 implements View.OnClickListener {
        AnonymousClass26() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            YGOrderHelper.YG_OrderTask_FinishOrder();
        }
    }

    /* renamed from: com.shj.biz.ShjManager$27 */
    /* loaded from: classes2.dex */
    class AnonymousClass27 implements View.OnClickListener {
        AnonymousClass27() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                ShjDbHelper.clear();
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$28 */
    /* loaded from: classes2.dex */
    class AnonymousClass28 implements View.OnClickListener {
        AnonymousClass28() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Loger.writeLog("PRINT", "----------------start-----------------");
                Iterator<Integer> it = Shj.getShelves().iterator();
                while (it.hasNext()) {
                    int intValue = it.next().intValue();
                    ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
                    Loger.writeLog("PRINT", "shelf:" + intValue + " code:" + shelfInfo.getGoodsCode() + " status:" + shelfInfo.getStatus() + " isOK:" + shelfInfo.isStatusOK() + " pickcode_only:" + shelfInfo.getDatas("pickcode_only").toString() + " isClock:" + shelfInfo.getDatas("isClock").toString() + " count:" + shelfInfo.getGoodsCount());
                }
                for (String str : ShjManager.getGoodsManager().getGoodsKeys()) {
                    Loger.writeLog("PRINT", "----------goods key: " + str + " start-----------------");
                    for (Goods goods : ShjManager.getGoodsManager().getGoodsByKey(str, ";MFL;")) {
                        Loger.writeLog("PRINT", "goods:" + goods.getCode() + " keys:" + ObjectHelper.ary2String(goods.getKeys()) + " count:" + goods.getCount() + " reserveCount:" + goods.getReserveCount());
                    }
                    Loger.writeLog("PRINT", "----------goods key: " + str + " end-----------------");
                }
                Iterator<Integer> it2 = Shj.getShelves().iterator();
                while (it2.hasNext()) {
                    int intValue2 = it2.next().intValue();
                    ShelfInfo shelfInfo2 = Shj.getShelfInfo(Integer.valueOf(intValue2));
                    Loger.writeLog("PRINT", "shelf:" + intValue2 + " code:" + shelfInfo2.getGoodsCode() + " status:" + shelfInfo2.getStatus() + " count:" + shelfInfo2.getGoodsCount());
                }
                Loger.writeLog("PRINT", "----------------end-----------------");
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$29 */
    /* loaded from: classes2.dex */
    class AnonymousClass29 implements View.OnClickListener {
        AnonymousClass29() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                if (!Shj.isDebugBlockOfferGoods()) {
                    Shj.setDebugBlockOfferGoods(true);
                    view.setBackgroundColor(Color.parseColor("#88FF0000"));
                } else {
                    Shj.setDebugBlockOfferGoods(false);
                    view.setBackgroundColor(Color.parseColor("#88000000"));
                }
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$30 */
    /* loaded from: classes2.dex */
    class AnonymousClass30 implements View.OnClickListener {
        AnonymousClass30() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                ShjManager.debugClearBlocks();
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$31 */
    /* loaded from: classes2.dex */
    class AnonymousClass31 implements View.OnClickListener {
        AnonymousClass31() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                if (!Shj.isDebugShelfStopSale()) {
                    view.setBackgroundColor(Color.parseColor("#88FF0000"));
                    Shj.setDebugShelfStopSale(true);
                } else {
                    view.setBackgroundColor(Color.parseColor("#88000000"));
                    Shj.setDebugShelfStopSale(false);
                }
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$32 */
    /* loaded from: classes2.dex */
    class AnonymousClass32 implements View.OnClickListener {
        AnonymousClass32() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                if (!Shj.isDebugNoGoodsOnShelf()) {
                    view.setBackgroundColor(Color.parseColor("#88FF0000"));
                    Shj.setDebugNoGoodsOnShelf(true);
                } else {
                    view.setBackgroundColor(Color.parseColor("#88000000"));
                    Shj.setDebugNoGoodsOnShelf(false);
                }
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$33 */
    /* loaded from: classes2.dex */
    class AnonymousClass33 implements View.OnClickListener {
        AnonymousClass33() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int parseColor;
            if (ShjManager.isDebugLogined) {
                int i2 = NetManager.getDebugModel() == 2 ? 0 : 2;
                NetManager.setDebugModel(i2);
                RequestHelper.setDebugModel(i2);
                if (i2 == 2) {
                    parseColor = Color.parseColor("#88FF0000");
                } else {
                    parseColor = Color.parseColor("#88000000");
                }
                view.setBackgroundColor(parseColor);
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$34 */
    /* loaded from: classes2.dex */
    class AnonymousClass34 implements View.OnClickListener {
        AnonymousClass34() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int parseColor;
            if (ShjManager.isDebugLogined) {
                int i2 = NetManager.getDebugModel() == 1 ? 0 : 1;
                NetManager.setDebugModel(i2);
                RequestHelper.setDebugModel(i2);
                if (i2 == 1) {
                    parseColor = Color.parseColor("#88FF0000");
                } else {
                    parseColor = Color.parseColor("#88000000");
                }
                view.setBackgroundColor(parseColor);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$35 */
    /* loaded from: classes2.dex */
    public class AnonymousClass35 implements View.OnClickListener {
        AnonymousClass35() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText3 = new EditText(context);
                EditText editText22 = new EditText(context);
                editText3.setText(NetManager.getServerIp() == null ? "120.27.194.135" : NetManager.getServerIp());
                editText3.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText3);
                editText22.setText(NetManager.getServerPort() == null ? ShjManager.defDebugServerPort : NetManager.getServerPort());
                if (ShjManager.isDebugNoLogin()) {
                    editText22.setEnabled(false);
                }
                editText22.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText22);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.35.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.35.2
                    final /* synthetic */ EditText val$ipText;
                    final /* synthetic */ EditText val$portText;

                    AnonymousClass2(EditText editText32, EditText editText222) {
                        editText = editText32;
                        editText2 = editText222;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            NetManager.setServerIp(editText.getText().toString());
                            NetManager.setServerPort(editText2.getText().toString());
                            CacheHelper.getFileCache().put("TEST_IP", editText.getText().toString());
                            CacheHelper.getFileCache().put("TEST_PORT", editText2.getText().toString());
                            NetManager.getSocketProcessor().stop();
                            NetManager.getSocketProcessor().setHost(NetManager.getServerIp(), Integer.parseInt(NetManager.getServerPort()));
                            NetManager.getSocketProcessor().start(ShjManager.wkAppContext.get());
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$35$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$35$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$ipText;
            final /* synthetic */ EditText val$portText;

            AnonymousClass2(EditText editText32, EditText editText222) {
                editText = editText32;
                editText2 = editText222;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    NetManager.setServerIp(editText.getText().toString());
                    NetManager.setServerPort(editText2.getText().toString());
                    CacheHelper.getFileCache().put("TEST_IP", editText.getText().toString());
                    CacheHelper.getFileCache().put("TEST_PORT", editText2.getText().toString());
                    NetManager.getSocketProcessor().stop();
                    NetManager.getSocketProcessor().setHost(NetManager.getServerIp(), Integer.parseInt(NetManager.getServerPort()));
                    NetManager.getSocketProcessor().start(ShjManager.wkAppContext.get());
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$36 */
    /* loaded from: classes2.dex */
    public class AnonymousClass36 implements View.OnClickListener {
        AnonymousClass36() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText2 = new EditText(context);
                editText2.setText("");
                editText2.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText2);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.36.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.36.2
                    final /* synthetic */ EditText val$machineIdText;

                    AnonymousClass2(EditText editText22) {
                        editText = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Shj.setTestMachineId(editText.getText().toString());
                            NetManager.setUser(Shj.getMachineId(), "000000");
                            NetManager.getSocketProcessor().stop();
                            NetManager.getSocketProcessor().start(ShjManager.wkAppContext.get());
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$36$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$36$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$machineIdText;

            AnonymousClass2(EditText editText22) {
                editText = editText22;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Shj.setTestMachineId(editText.getText().toString());
                    NetManager.setUser(Shj.getMachineId(), "000000");
                    NetManager.getSocketProcessor().stop();
                    NetManager.getSocketProcessor().start(ShjManager.wkAppContext.get());
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$37 */
    /* loaded from: classes2.dex */
    public class AnonymousClass37 implements View.OnClickListener {
        AnonymousClass37() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText2 = new EditText(context);
                editText2.setText("");
                editText2.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText2);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.37.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.37.2
                    final /* synthetic */ EditText val$yhCodeText;

                    AnonymousClass2(EditText editText22) {
                        editText = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            ShjManager.putData("YHM", editText.getText().toString());
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$37$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$37$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$yhCodeText;

            AnonymousClass2(EditText editText22) {
                editText = editText22;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    ShjManager.putData("YHM", editText.getText().toString());
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$38 */
    /* loaded from: classes2.dex */
    class AnonymousClass38 implements View.OnClickListener {
        AnonymousClass38() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Report_OnlinePay_Apply report_OnlinePay_Apply = new Report_OnlinePay_Apply();
                report_OnlinePay_Apply.setParams(1, 1, "NA", Shj.getMachineId(), "1", String.format("%03d", 1), 1, "测试", "1");
                NetManager.appendReport(report_OnlinePay_Apply);
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$39 */
    /* loaded from: classes2.dex */
    class AnonymousClass39 implements View.OnClickListener {
        AnonymousClass39() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int parseColor;
            if (ShjManager.isDebugLogined) {
                boolean unused = ShjManager.debugHasGoodsInPickdoor = !ShjManager.debugHasGoodsInPickdoor;
                Shj.debugOfferDivicError(ShjManager.debugHasGoodsInPickdoor ? 5 : 0);
                if (ShjManager.debugHasGoodsInPickdoor) {
                    parseColor = Color.parseColor("#88FF0000");
                } else {
                    parseColor = Color.parseColor("#88000000");
                }
                view.setBackgroundColor(parseColor);
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$40 */
    /* loaded from: classes2.dex */
    class AnonymousClass40 implements View.OnClickListener {
        AnonymousClass40() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int parseColor;
            if (ShjManager.isDebugLogined) {
                boolean unused = ShjManager.testNoOfferGoodsOnPay = !ShjManager.testNoOfferGoodsOnPay;
                if (ShjManager.testNoOfferGoodsOnPay) {
                    parseColor = Color.parseColor("#88FF0000");
                } else {
                    parseColor = Color.parseColor("#88000000");
                }
                view.setBackgroundColor(parseColor);
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$41 */
    /* loaded from: classes2.dex */
    class AnonymousClass41 implements View.OnClickListener {
        AnonymousClass41() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int parseColor;
            if (ShjManager.isDebugLogined) {
                view.setTag(Integer.valueOf(((Integer) view.getTag()).intValue() == 0 ? 1 : 0));
                if (((Integer) view.getTag()).intValue() == 1) {
                    parseColor = Color.parseColor("#88FF0000");
                } else {
                    parseColor = Color.parseColor("#88000000");
                }
                view.setBackgroundColor(parseColor);
                Shj.debugOfferDivicError(((Integer) view.getTag()).intValue() == 1 ? 1 : 0);
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$42 */
    /* loaded from: classes2.dex */
    class AnonymousClass42 implements View.OnClickListener {
        AnonymousClass42() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int parseColor;
            if (ShjManager.isDebugLogined) {
                view.setTag(Integer.valueOf(((Integer) view.getTag()).intValue() == 0 ? 1 : 0));
                if (((Integer) view.getTag()).intValue() == 1) {
                    parseColor = Color.parseColor("#88FF0000");
                } else {
                    parseColor = Color.parseColor("#88000000");
                }
                view.setBackgroundColor(parseColor);
                Shj.debugOfferGoodsError(((Integer) view.getTag()).intValue() == 1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$43 */
    /* loaded from: classes2.dex */
    public class AnonymousClass43 implements View.OnClickListener {
        AnonymousClass43() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText2 = new EditText(context);
                editText2.setText("");
                editText2.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText2);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.43.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.43.2
                    final /* synthetic */ EditText val$imeiIdText;

                    AnonymousClass2(EditText editText22) {
                        editText = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj = editText.getText().toString();
                            Report_Con_QMachineId report_Con_QMachineId = new Report_Con_QMachineId();
                            report_Con_QMachineId.setParams(obj);
                            NetManager.appendReport(report_Con_QMachineId);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$43$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$43$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$imeiIdText;

            AnonymousClass2(EditText editText22) {
                editText = editText22;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    String obj = editText.getText().toString();
                    Report_Con_QMachineId report_Con_QMachineId = new Report_Con_QMachineId();
                    report_Con_QMachineId.setParams(obj);
                    NetManager.appendReport(report_Con_QMachineId);
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$44 */
    /* loaded from: classes2.dex */
    public class AnonymousClass44 implements View.OnClickListener {
        AnonymousClass44() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText2 = new EditText(context);
                editText2.setText("");
                editText2.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText2);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.44.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.44.2
                    final /* synthetic */ EditText val$imeiIdText;

                    AnonymousClass2(EditText editText22) {
                        editText = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj22 = editText.getText().toString();
                            ShjManager.selectGoodsOnShelf(2);
                            new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.44.2.1
                                final /* synthetic */ String val$imei;

                                AnonymousClass1(String obj222) {
                                    obj = obj222;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    ShjManager.getBizShjListener()._onNeedICCardPay(1, obj, "");
                                }
                            }, 2000L);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    /* renamed from: com.shj.biz.ShjManager$44$2$1 */
                    /* loaded from: classes2.dex */
                    class AnonymousClass1 extends TimerTask {
                        final /* synthetic */ String val$imei;

                        AnonymousClass1(String obj222) {
                            obj = obj222;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            ShjManager.getBizShjListener()._onNeedICCardPay(1, obj, "");
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$44$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.biz.ShjManager$44$2 */
        /* loaded from: classes2.dex */
        public class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$imeiIdText;

            AnonymousClass2(EditText editText22) {
                editText = editText22;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    String obj222 = editText.getText().toString();
                    ShjManager.selectGoodsOnShelf(2);
                    new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.44.2.1
                        final /* synthetic */ String val$imei;

                        AnonymousClass1(String obj2222) {
                            obj = obj2222;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            ShjManager.getBizShjListener()._onNeedICCardPay(1, obj, "");
                        }
                    }, 2000L);
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /* renamed from: com.shj.biz.ShjManager$44$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 extends TimerTask {
                final /* synthetic */ String val$imei;

                AnonymousClass1(String obj2222) {
                    obj = obj2222;
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    ShjManager.getBizShjListener()._onNeedICCardPay(1, obj, "");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$45 */
    /* loaded from: classes2.dex */
    public class AnonymousClass45 implements View.OnClickListener {
        AnonymousClass45() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText5 = new EditText(context);
                editText5.setText("");
                editText5.setHint("指令类型:1申请扣款,2查余额");
                editText5.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText5);
                EditText editText22 = new EditText(context);
                editText22.setText("");
                editText22.setHint("物理卡号");
                editText22.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText22);
                EditText editText32 = new EditText(context);
                editText32.setText("");
                editText32.setHint("卡扩展信息");
                editText32.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText32);
                EditText editText42 = new EditText(context);
                editText42.setText("");
                editText42.setHint("货道编号");
                editText42.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText42);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.45.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.45.2
                    final /* synthetic */ EditText val$imeiCardText;
                    final /* synthetic */ EditText val$imeiExText;
                    final /* synthetic */ EditText val$imeiGoodsCodeText;
                    final /* synthetic */ EditText val$imeiTypeText;

                    AnonymousClass2(EditText editText52, EditText editText222, EditText editText322, EditText editText422) {
                        editText = editText52;
                        editText2 = editText222;
                        editText3 = editText322;
                        editText4 = editText422;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj32 = editText.getText().toString();
                            String obj222 = editText2.getText().toString();
                            editText3.getText().toString();
                            ShjManager.selectGoodsOnShelf(Integer.parseInt(editText4.getText().toString()));
                            new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.45.2.1
                                final /* synthetic */ String val$card;
                                final /* synthetic */ String val$type;

                                AnonymousClass1(String obj322, String obj2222) {
                                    obj = obj322;
                                    obj2 = obj2222;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    Shj.onNeedICCardPay(Integer.parseInt(obj), obj2);
                                }
                            }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText2.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            InputMethodManager inputMethodManager2 = (InputMethodManager) editText3.getContext().getSystemService("input_method");
                            inputMethodManager2.hideSoftInputFromWindow(editText3.getWindowToken(), 0);
                            inputMethodManager2.hideSoftInputFromWindow(editText3.getWindowToken(), 0);
                            InputMethodManager inputMethodManager3 = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager3.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager3.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            InputMethodManager inputMethodManager4 = (InputMethodManager) editText4.getContext().getSystemService("input_method");
                            inputMethodManager4.hideSoftInputFromWindow(editText4.getWindowToken(), 0);
                            inputMethodManager4.hideSoftInputFromWindow(editText4.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    /* renamed from: com.shj.biz.ShjManager$45$2$1 */
                    /* loaded from: classes2.dex */
                    class AnonymousClass1 extends TimerTask {
                        final /* synthetic */ String val$card;
                        final /* synthetic */ String val$type;

                        AnonymousClass1(String obj322, String obj2222) {
                            obj = obj322;
                            obj2 = obj2222;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            Shj.onNeedICCardPay(Integer.parseInt(obj), obj2);
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$45$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.biz.ShjManager$45$2 */
        /* loaded from: classes2.dex */
        public class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$imeiCardText;
            final /* synthetic */ EditText val$imeiExText;
            final /* synthetic */ EditText val$imeiGoodsCodeText;
            final /* synthetic */ EditText val$imeiTypeText;

            AnonymousClass2(EditText editText52, EditText editText222, EditText editText322, EditText editText422) {
                editText = editText52;
                editText2 = editText222;
                editText3 = editText322;
                editText4 = editText422;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    String obj322 = editText.getText().toString();
                    String obj2222 = editText2.getText().toString();
                    editText3.getText().toString();
                    ShjManager.selectGoodsOnShelf(Integer.parseInt(editText4.getText().toString()));
                    new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.45.2.1
                        final /* synthetic */ String val$card;
                        final /* synthetic */ String val$type;

                        AnonymousClass1(String obj3222, String obj22222) {
                            obj = obj3222;
                            obj2 = obj22222;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            Shj.onNeedICCardPay(Integer.parseInt(obj), obj2);
                        }
                    }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                    InputMethodManager inputMethodManager = (InputMethodManager) editText2.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                    InputMethodManager inputMethodManager2 = (InputMethodManager) editText3.getContext().getSystemService("input_method");
                    inputMethodManager2.hideSoftInputFromWindow(editText3.getWindowToken(), 0);
                    inputMethodManager2.hideSoftInputFromWindow(editText3.getWindowToken(), 0);
                    InputMethodManager inputMethodManager3 = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager3.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager3.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    InputMethodManager inputMethodManager4 = (InputMethodManager) editText4.getContext().getSystemService("input_method");
                    inputMethodManager4.hideSoftInputFromWindow(editText4.getWindowToken(), 0);
                    inputMethodManager4.hideSoftInputFromWindow(editText4.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /* renamed from: com.shj.biz.ShjManager$45$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 extends TimerTask {
                final /* synthetic */ String val$card;
                final /* synthetic */ String val$type;

                AnonymousClass1(String obj3222, String obj22222) {
                    obj = obj3222;
                    obj2 = obj22222;
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    Shj.onNeedICCardPay(Integer.parseInt(obj), obj2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$46 */
    /* loaded from: classes2.dex */
    public class AnonymousClass46 implements View.OnClickListener {
        AnonymousClass46() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText2 = new EditText(context);
                editText2.setText("");
                editText2.setHint("扫码或输入码值");
                editText2.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText2);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.46.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.46.2
                    final /* synthetic */ EditText val$imeiIdText;

                    AnonymousClass2(EditText editText22) {
                        editText = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.46.2.1
                                final /* synthetic */ String val$imei;

                                AnonymousClass1(String str) {
                                    obj = str;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    ShjManager.getStatusListener().onMessage("SCANOR", obj);
                                }
                            }, 2000L);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    /* renamed from: com.shj.biz.ShjManager$46$2$1 */
                    /* loaded from: classes2.dex */
                    class AnonymousClass1 extends TimerTask {
                        final /* synthetic */ String val$imei;

                        AnonymousClass1(String str) {
                            obj = str;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            ShjManager.getStatusListener().onMessage("SCANOR", obj);
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$46$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.biz.ShjManager$46$2 */
        /* loaded from: classes2.dex */
        public class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$imeiIdText;

            AnonymousClass2(EditText editText22) {
                editText = editText22;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.46.2.1
                        final /* synthetic */ String val$imei;

                        AnonymousClass1(String str) {
                            obj = str;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            ShjManager.getStatusListener().onMessage("SCANOR", obj);
                        }
                    }, 2000L);
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /* renamed from: com.shj.biz.ShjManager$46$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 extends TimerTask {
                final /* synthetic */ String val$imei;

                AnonymousClass1(String str) {
                    obj = str;
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    ShjManager.getStatusListener().onMessage("SCANOR", obj);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$47 */
    /* loaded from: classes2.dex */
    public class AnonymousClass47 implements View.OnClickListener {
        AnonymousClass47() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText2 = new EditText(context);
                editText2.setText("");
                editText2.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText2);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.47.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.47.2
                    final /* synthetic */ EditText val$cmdText;

                    AnonymousClass2(EditText editText22) {
                        editText = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj = editText.getText().toString();
                            ArrayList arrayList2 = new ArrayList();
                            String replace = obj.replace(StringUtils.SPACE, "");
                            int i3 = 0;
                            while (i3 < replace.length()) {
                                int i4 = i3 + 2;
                                arrayList2.add(Integer.valueOf(ObjectHelper.string2Short(replace.substring(i3, i4))));
                                i3 = i4;
                            }
                            int size = arrayList2.size() - 0;
                            byte[] bArr = new byte[size];
                            for (int i5 = 0; i5 < size - 1; i5++) {
                                bArr[i5 + 0] = (byte) ((Integer) arrayList2.get(i5)).intValue();
                            }
                            Loger.writeLog("COMMAND", ObjectHelper.hex2String(bArr, size));
                            CommandManager.appendReceivedRawCommand(bArr);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$47$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$47$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$cmdText;

            AnonymousClass2(EditText editText22) {
                editText = editText22;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    String obj = editText.getText().toString();
                    ArrayList arrayList2 = new ArrayList();
                    String replace = obj.replace(StringUtils.SPACE, "");
                    int i3 = 0;
                    while (i3 < replace.length()) {
                        int i4 = i3 + 2;
                        arrayList2.add(Integer.valueOf(ObjectHelper.string2Short(replace.substring(i3, i4))));
                        i3 = i4;
                    }
                    int size = arrayList2.size() - 0;
                    byte[] bArr = new byte[size];
                    for (int i5 = 0; i5 < size - 1; i5++) {
                        bArr[i5 + 0] = (byte) ((Integer) arrayList2.get(i5)).intValue();
                    }
                    Loger.writeLog("COMMAND", ObjectHelper.hex2String(bArr, size));
                    CommandManager.appendReceivedRawCommand(bArr);
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$48 */
    /* loaded from: classes2.dex */
    public class AnonymousClass48 implements View.OnClickListener {
        AnonymousClass48() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText2 = new EditText(context);
                editText2.setText("");
                editText2.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText2);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.48.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.48.2
                    final /* synthetic */ EditText val$shelfText;

                    AnonymousClass2(EditText editText22) {
                        editText = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.48.2.1
                                final /* synthetic */ String val$shelf;

                                AnonymousClass1(String str) {
                                    obj = str;
                                }

                                @Override // java.util.TimerTask, java.lang.Runnable
                                public void run() {
                                    ShjManager.selectGoodsOnShelf(Integer.parseInt(obj));
                                }
                            }, 2000L);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    /* renamed from: com.shj.biz.ShjManager$48$2$1 */
                    /* loaded from: classes2.dex */
                    class AnonymousClass1 extends TimerTask {
                        final /* synthetic */ String val$shelf;

                        AnonymousClass1(String str) {
                            obj = str;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            ShjManager.selectGoodsOnShelf(Integer.parseInt(obj));
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$48$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.shj.biz.ShjManager$48$2 */
        /* loaded from: classes2.dex */
        public class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$shelfText;

            AnonymousClass2(EditText editText22) {
                editText = editText22;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    new Timer().schedule(new TimerTask() { // from class: com.shj.biz.ShjManager.48.2.1
                        final /* synthetic */ String val$shelf;

                        AnonymousClass1(String str) {
                            obj = str;
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            ShjManager.selectGoodsOnShelf(Integer.parseInt(obj));
                        }
                    }, 2000L);
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /* renamed from: com.shj.biz.ShjManager$48$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 extends TimerTask {
                final /* synthetic */ String val$shelf;

                AnonymousClass1(String str) {
                    obj = str;
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    ShjManager.selectGoodsOnShelf(Integer.parseInt(obj));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$49 */
    /* loaded from: classes2.dex */
    public class AnonymousClass49 implements View.OnClickListener {
        AnonymousClass49() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText3 = new EditText(context);
                EditText editText22 = new EditText(context);
                editText3.setText("1");
                editText3.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText3);
                editText22.setText("1");
                editText22.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText22);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.49.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.49.2
                    final /* synthetic */ EditText val$priceText;
                    final /* synthetic */ EditText val$shelfText;

                    AnonymousClass2(EditText editText32, EditText editText222) {
                        editText = editText32;
                        editText2 = editText222;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj = editText.getText().toString();
                            ShjManager.setShelfGoodsCode(Integer.parseInt(obj), editText2.getText().toString());
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$49$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$49$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$priceText;
            final /* synthetic */ EditText val$shelfText;

            AnonymousClass2(EditText editText32, EditText editText222) {
                editText = editText32;
                editText2 = editText222;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    String obj = editText.getText().toString();
                    ShjManager.setShelfGoodsCode(Integer.parseInt(obj), editText2.getText().toString());
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$50 */
    /* loaded from: classes2.dex */
    public class AnonymousClass50 implements View.OnClickListener {
        AnonymousClass50() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText3 = new EditText(context);
                EditText editText22 = new EditText(context);
                editText3.setText("1");
                editText3.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText3);
                editText22.setText("1");
                editText22.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText22);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.50.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.50.2
                    final /* synthetic */ EditText val$priceText;
                    final /* synthetic */ EditText val$shelfText;

                    AnonymousClass2(EditText editText32, EditText editText222) {
                        editText = editText32;
                        editText2 = editText222;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            ShjManager.setShelfGoodsPrice(Integer.parseInt(editText.getText().toString()), Integer.parseInt(editText2.getText().toString()));
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$50$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$50$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$priceText;
            final /* synthetic */ EditText val$shelfText;

            AnonymousClass2(EditText editText32, EditText editText222) {
                editText = editText32;
                editText2 = editText222;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    ShjManager.setShelfGoodsPrice(Integer.parseInt(editText.getText().toString()), Integer.parseInt(editText2.getText().toString()));
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$51 */
    /* loaded from: classes2.dex */
    public class AnonymousClass51 implements View.OnClickListener {
        AnonymousClass51() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText3 = new EditText(context);
                EditText editText22 = new EditText(context);
                editText3.setText("1");
                editText3.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText3);
                editText22.setText("1");
                editText22.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText22);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.51.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.51.2
                    final /* synthetic */ EditText val$countText;
                    final /* synthetic */ EditText val$shelfText;

                    AnonymousClass2(EditText editText32, EditText editText222) {
                        editText = editText32;
                        editText2 = editText222;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            ShjManager.setShelfGoodsCount(Integer.parseInt(editText.getText().toString()), Integer.parseInt(editText2.getText().toString()));
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$51$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$51$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$countText;
            final /* synthetic */ EditText val$shelfText;

            AnonymousClass2(EditText editText32, EditText editText222) {
                editText = editText32;
                editText2 = editText222;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    ShjManager.setShelfGoodsCount(Integer.parseInt(editText.getText().toString()), Integer.parseInt(editText2.getText().toString()));
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$52 */
    /* loaded from: classes2.dex */
    class AnonymousClass52 implements View.OnClickListener {
        AnonymousClass52() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int parseColor;
            if (ShjManager.isDebugLogined) {
                view.setTag(view.getTag().toString() == "0" ? "1" : "0");
                if (view.getTag().toString() == "1") {
                    parseColor = Color.parseColor("#88FF0000");
                } else {
                    parseColor = Color.parseColor("#88000000");
                }
                view.setBackgroundColor(parseColor);
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$53 */
    /* loaded from: classes2.dex */
    class AnonymousClass53 implements View.OnClickListener {
        AnonymousClass53() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int parseColor;
            if (ShjManager.isDebugLogined) {
                view.setTag(view.getTag().toString() == "0" ? "1" : "0");
                if (view.getTag().toString() == "1") {
                    parseColor = Color.parseColor("#88FF0000");
                } else {
                    parseColor = Color.parseColor("#88000000");
                }
                view.setBackgroundColor(parseColor);
                Shj.setDebugMachineType(view.getTag().toString() == "1" ? 1 : 0);
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$54 */
    /* loaded from: classes2.dex */
    class AnonymousClass54 implements View.OnClickListener {
        AnonymousClass54() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Shj.getMachine(0, false).setDoorIsOpen(true);
                ShjManager.getStatusListener().onDoorStatusChanged(0, true);
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$55 */
    /* loaded from: classes2.dex */
    class AnonymousClass55 implements View.OnClickListener {
        AnonymousClass55() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Shj.getMachine(0, false).setDoorIsOpen(false);
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$56 */
    /* loaded from: classes2.dex */
    class AnonymousClass56 implements View.OnClickListener {
        AnonymousClass56() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                try {
                    WinCarcdReader.get().onMessage("#SOPrint*GALLER CH\n       SPIRE SPU90\n IDEAL SOLUTIONS COMPANY\n       QIC TESTING\n\nTID :           35010122\nMID:     350100144030110\n\n<CARDHOLDER COPY>\n\nCUP            \n625809******2907\nAID: A000000333010102\nCGB PBOC CREDIT\nSALE\n\n\nINVOICE: 000012\n\n[06-05 14:16:42:305] DATE: JUN 05, 20\nTIME:  09:16:05\n\nBATCH: 000001\n\nTOTAL           QAR 0.01\n\u001eTRANSACTION \u001f\n#EO");
                } catch (Exception unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$57 */
    /* loaded from: classes2.dex */
    public class AnonymousClass57 implements View.OnClickListener {
        AnonymousClass57() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(((TextView) view).getText());
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.leftMargin = 40;
                layoutParams2.rightMargin = 40;
                EditText editText2 = new EditText(context);
                editText2.setText("");
                editText2.setHint("输入货道号");
                editText2.setLayoutParams(layoutParams2);
                linearLayout2.addView(editText2);
                builder.setView(linearLayout2);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.57.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            declaredField.setAccessible(true);
                            declaredField.set(dialogInterface, true);
                        } catch (Exception unused) {
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shj.biz.ShjManager.57.2
                    final /* synthetic */ EditText val$imeiIdText;

                    AnonymousClass2(EditText editText22) {
                        editText = editText22;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        try {
                            String obj = editText.getText().toString();
                            String str = "THIRDORDER" + System.currentTimeMillis();
                            ShjManager.getOrderManager().driverThirdPayOrder(str, Shj.getShelfInfo(Integer.valueOf(Integer.parseInt(obj))).getPrice().intValue(), OrderPayType.ThridPay, str, "" + obj);
                            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            try {
                                Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                declaredField.setAccessible(true);
                                declaredField.set(dialogInterface, true);
                            } catch (Exception unused) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        }

        /* renamed from: com.shj.biz.ShjManager$57$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                    declaredField.setAccessible(true);
                    declaredField.set(dialogInterface, true);
                } catch (Exception unused) {
                }
            }
        }

        /* renamed from: com.shj.biz.ShjManager$57$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            final /* synthetic */ EditText val$imeiIdText;

            AnonymousClass2(EditText editText22) {
                editText = editText22;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    String obj = editText.getText().toString();
                    String str = "THIRDORDER" + System.currentTimeMillis();
                    ShjManager.getOrderManager().driverThirdPayOrder(str, Shj.getShelfInfo(Integer.valueOf(Integer.parseInt(obj))).getPrice().intValue(), OrderPayType.ThridPay, str, "" + obj);
                    InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    try {
                        Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                        declaredField.setAccessible(true);
                        declaredField.set(dialogInterface, true);
                    } catch (Exception unused) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$58 */
    /* loaded from: classes2.dex */
    class AnonymousClass58 implements View.OnClickListener {
        final /* synthetic */ List val$textViewList;

        AnonymousClass58(List arrayList2) {
            arrayList = arrayList2;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    ((TextView) it.next()).setVisibility(8);
                }
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$59 */
    /* loaded from: classes2.dex */
    class AnonymousClass59 implements View.OnClickListener {
        AnonymousClass59() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShjManager.isDebugLogined) {
                ShjManager.getStatusListener().onMessage("WinCarcdReader", "Print*打印测试\n-------------\n测试商品  999 1件");
            }
        }
    }

    private static TextView createTestTextView(Context context, String str) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, 60);
        layoutParams.leftMargin = 5;
        TextView textView = new TextView(context);
        textView.setPadding(10, 0, 10, 10);
        textView.setText(str);
        textView.setBackgroundColor(Color.parseColor("#88000000"));
        textView.setTextColor(-1);
        textView.setTextSize(SDFileUtils.SDCardRoot.contains("shared") ? 14.0f : 30.0f);
        textView.setGravity(17);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    public static void queryServerTime() {
        DataSynchronous.report_queryServerTime();
    }

    public static boolean isTestNoOfferGoodsOnPay() {
        return testNoOfferGoodsOnPay;
    }

    public static void setTestNoOfferGoodsOnPay(boolean z) {
        testNoOfferGoodsOnPay = z;
    }

    public static void debugClearBlocks() {
        List<Integer> list;
        try {
            list = (List) CacheHelper.getFileCache().getAsObject("Debug:Blocks");
        } catch (Exception e) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            list = null;
        }
        if (list == null) {
            list = new ArrayList();
        }
        for (Integer num : list) {
            if (Shj.getShelfInfo(num) != null) {
                ShelfInfo shelfInfo = Shj.getShelfInfo(num);
                Shj.onUpdateShelfInfo(num.intValue(), shelfInfo.getPrice().intValue(), shelfInfo.getGoodsCount().intValue(), shelfInfo.getCapacity().intValue(), shelfInfo.getGoodsCode(), 0, 0, -1, -1);
            }
        }
    }

    public static boolean isDebugNoLogin() {
        return debugNoLogin;
    }

    public static void setDebugNoLogin(boolean z) {
        debugNoLogin = z;
        if (z) {
            isDebugLogined = true;
        }
    }

    public static void setDefDebugServerPort(String str) {
        defDebugServerPort = str;
    }

    public static boolean isIcCardPaybyXyServer() {
        return icCardPaybyXyServer;
    }

    public static void setIcCardPaybyXyServer(boolean z) {
        icCardPaybyXyServer = z;
    }

    public static boolean isEnableQrScanor() {
        return enableQrScanor;
    }

    public static void setEnableQrScanor(boolean z) {
        enableQrScanor = z;
    }

    public static boolean isEnableWinCardReader() {
        return enableWinCardReader;
    }

    public static void setEnableWinCardReader(boolean z) {
        enableWinCardReader = z;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:12:0x0278. Please report as an issue. */
    public static void onServerCmd(String str) {
        char c;
        String str2 = str;
        boolean z = true;
        if (str2.startsWith(Marker.ANY_MARKER)) {
            str2 = str2.substring(1);
        }
        String[] split = str2.split("\\*");
        Report_CmdAck report_CmdAck = new Report_CmdAck();
        try {
            try {
                String str3 = split[0];
                switch (str3.hashCode()) {
                    case 1478598:
                        if (str3.equals("0105")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1478599:
                        if (str3.equals("0106")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1478600:
                        if (str3.equals("0107")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1481477:
                        if (str3.equals("0401")) {
                            c = 11;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1482438:
                        if (str3.equals("0501")) {
                            c = '\f';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1483399:
                        if (str3.equals("0601")) {
                            c = CharUtils.CR;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1484360:
                        if (str3.equals("0701")) {
                            c = 14;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1509346:
                        if (str3.equals("1201")) {
                            c = 15;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1510307:
                        if (str3.equals("1301")) {
                            c = 16;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1511268:
                        if (str3.equals("1401")) {
                            c = 17;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1512229:
                        if (str3.equals("1501")) {
                            c = 18;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1513190:
                        if (str3.equals("1601")) {
                            c = 19;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1514151:
                        if (str3.equals("1701")) {
                            c = 20;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1515112:
                        if (str3.equals("1801")) {
                            c = 21;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1516073:
                        if (str3.equals("1901")) {
                            c = 22;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1537215:
                        if (str3.equals("2001")) {
                            c = 23;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1538176:
                        if (str3.equals("2101")) {
                            c = 24;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1539137:
                        if (str3.equals("2201")) {
                            c = 25;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1540098:
                        if (str3.equals("2301")) {
                            c = 26;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1541059:
                        if (str3.equals("2401")) {
                            c = 27;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1542020:
                        if (str3.equals("2501")) {
                            c = 28;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1542981:
                        if (str3.equals("2601")) {
                            c = 29;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1543942:
                        if (str3.equals("2701")) {
                            c = 30;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1544903:
                        if (str3.equals("2801")) {
                            c = 31;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1545864:
                        if (str3.equals("2901")) {
                            c = ' ';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1568928:
                        if (str3.equals("3201")) {
                            c = '!';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1569889:
                        if (str3.equals("3301")) {
                            c = Typography.quote;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1570850:
                        if (str3.equals("3401")) {
                            c = '#';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1571811:
                        if (str3.equals("3501")) {
                            c = '$';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1573733:
                        if (str3.equals("3701")) {
                            c = '%';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1574694:
                        if (str3.equals("3801")) {
                            c = Typography.amp;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1575655:
                        if (str3.equals("3901")) {
                            c = '\'';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1604485:
                        if (str3.equals("4801")) {
                            c = '(';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1605446:
                        if (str3.equals("4901")) {
                            c = ')';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1633315:
                        if (str3.equals("5701")) {
                            c = ClassUtils.PACKAGE_SEPARATOR_CHAR;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1659262:
                        if (str3.equals("6301")) {
                            c = '\n';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1660223:
                        if (str3.equals("6401")) {
                            c = '*';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1661184:
                        if (str3.equals("6501")) {
                            c = '+';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1690014:
                        if (str3.equals("7401")) {
                            c = 5;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1690975:
                        if (str3.equals("7501")) {
                            c = 6;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1691936:
                        if (str3.equals("7601")) {
                            c = 7;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1692897:
                        if (str3.equals("7701")) {
                            c = '\b';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1693858:
                        if (str3.equals("7801")) {
                            c = '\t';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1694819:
                        if (str3.equals("7901")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1694820:
                        if (str3.equals("7902")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1719805:
                        if (str3.equals("8401")) {
                            c = ',';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1720766:
                        if (str3.equals("8501")) {
                            c = '-';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1750557:
                        if (str3.equals("9501")) {
                            c = '/';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1751518:
                        if (str3.equals("9601")) {
                            c = '0';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1752479:
                        if (str3.equals("9701")) {
                            c = '1';
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        getBizShjListener()._onServerMessage("RESTART");
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 1:
                        isShjLocked = true;
                        if (Integer.parseInt(split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE)[0]) != 1) {
                            z = false;
                        }
                        isShjLocked = z;
                        handler.post(new Runnable() { // from class: com.shj.biz.ShjManager.60
                            AnonymousClass60() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                if (ShjManager.isShjLocked) {
                                    ActivityHelper.showLockScreenDialog(ShjManager.getActivityContext(), AndroidSystem.getLanguage(ShjManager.getActivityContext()).equalsIgnoreCase("zh") ? "暂停营业" : "CLOSED");
                                } else {
                                    ActivityHelper.closeLockScreenDialog();
                                }
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 2:
                        getBizShjListener()._onServerMessage(split[1]);
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 3:
                        getGoodsManager().startUpdateGoodsInfo(null);
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 4:
                    case '(':
                    default:
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 5:
                        String[] split2 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        setShelfGoodsPrice(Integer.parseInt(split2[0]), Integer.parseInt(split2[1]));
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 6:
                        String[] split3 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        int parseInt = Integer.parseInt(split3[0]);
                        setShelfGoodsCount(parseInt, Integer.parseInt(split3[1]));
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(Integer.valueOf(parseInt));
                        CommandV2_Up_Empty commandV2_Up_Empty = new CommandV2_Up_Empty();
                        commandV2_Up_Empty.setCommandStatusListener(new CommandStatusListener() { // from class: com.shj.biz.ShjManager.61
                            final /* synthetic */ List val$changedShelves;

                            @Override // com.shj.command.CommandStatusListener
                            public void onCommandError(Command command, CommandError commandError) {
                            }

                            AnonymousClass61(List arrayList2) {
                                arrayList = arrayList2;
                            }

                            @Override // com.shj.command.CommandStatusListener
                            public void onCommandFinished(Command command) {
                                ShjManager.getStatusListener().onShelvesReseted(arrayList);
                            }
                        });
                        CommandManager.appendSendCommand(commandV2_Up_Empty);
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 7:
                        String[] split4 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        setShelfInventory(Integer.parseInt(split4[0]), Integer.parseInt(split4[1]));
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '\b':
                        String[] split5 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        setShelfGoodsCode(Integer.parseInt(split5[0]), split5[1]);
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '\t':
                        String[] split6 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        int parseInt2 = Integer.parseInt(split6[0]);
                        String str4 = split6[1];
                        int parseInt3 = Integer.parseInt(split6[2]);
                        int parseInt4 = Integer.parseInt(split6[3]);
                        ArrayList arrayList2 = new ArrayList();
                        if (parseInt2 != 0 && parseInt2 < 1000) {
                            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(parseInt2));
                            arrayList2.add(Integer.valueOf(parseInt2));
                            if (shelfInfo.getGoodsCode() != str4) {
                                setShelfGoodsCode(parseInt2, str4);
                            }
                            if (shelfInfo.getPrice().intValue() != parseInt3) {
                                setShelfGoodsPrice(parseInt2, parseInt3);
                            }
                            if (parseInt4 >= 0 && shelfInfo.getGoodsCount().intValue() != parseInt4) {
                                setShelfGoodsCount(parseInt2, parseInt4);
                            }
                            CommandV2_Up_Empty commandV2_Up_Empty2 = new CommandV2_Up_Empty();
                            commandV2_Up_Empty2.setCommandStatusListener(new CommandStatusListener() { // from class: com.shj.biz.ShjManager.62
                                final /* synthetic */ List val$changedShelves;

                                @Override // com.shj.command.CommandStatusListener
                                public void onCommandError(Command command, CommandError commandError) {
                                }

                                AnonymousClass62(List arrayList22) {
                                    arrayList2 = arrayList22;
                                }

                                @Override // com.shj.command.CommandStatusListener
                                public void onCommandFinished(Command command) {
                                    ShjManager.getStatusListener().onShelvesReseted(arrayList2);
                                }
                            });
                            CommandManager.appendSendCommand(commandV2_Up_Empty2);
                            report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                            break;
                        }
                        if (!Shj.isDebug()) {
                            arrayList22.add(Integer.valueOf(parseInt2));
                            setShelfGoodsCode(parseInt2, str4);
                            setShelfGoodsPrice(parseInt2, parseInt3);
                            if (parseInt4 >= 0) {
                                setShelfGoodsCount(parseInt2, parseInt4);
                            }
                        } else if (parseInt2 == 0) {
                            for (Integer num : Shj.getShelves()) {
                                arrayList22.add(num);
                                setShelfGoodsCode(num.intValue(), str4);
                                setShelfGoodsPrice(num.intValue(), parseInt3);
                                if (parseInt4 >= 0) {
                                    setShelfGoodsCount(num.intValue(), parseInt4);
                                }
                            }
                        } else {
                            int i = parseInt2 % 1000;
                            for (Integer num2 : Shj.getShelves()) {
                                if (i == Shj.getLayerByShelf(num2.intValue())) {
                                    arrayList22.add(num2);
                                    setShelfGoodsCode(num2.intValue(), str4);
                                    setShelfGoodsPrice(num2.intValue(), parseInt3);
                                    if (parseInt4 >= 0) {
                                        setShelfGoodsCount(num2.intValue(), parseInt4);
                                    }
                                }
                            }
                        }
                        CommandV2_Up_Empty commandV2_Up_Empty22 = new CommandV2_Up_Empty();
                        commandV2_Up_Empty22.setCommandStatusListener(new CommandStatusListener() { // from class: com.shj.biz.ShjManager.62
                            final /* synthetic */ List val$changedShelves;

                            @Override // com.shj.command.CommandStatusListener
                            public void onCommandError(Command command, CommandError commandError) {
                            }

                            AnonymousClass62(List arrayList22) {
                                arrayList2 = arrayList22;
                            }

                            @Override // com.shj.command.CommandStatusListener
                            public void onCommandFinished(Command command) {
                                ShjManager.getStatusListener().onShelvesReseted(arrayList2);
                            }
                        });
                        CommandManager.appendSendCommand(commandV2_Up_Empty22);
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '\n':
                        ReportManager.reportSetFull();
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand.setOneKeyFullGoods();
                        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.63
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass63() {
                            }
                        });
                        if (Shj.isDebug()) {
                            for (Integer num3 : Shj.getShelves()) {
                                try {
                                    setShelfGoodsCount(num3.intValue(), Shj.getShelfInfo(num3).getCapacity().intValue());
                                } catch (Exception unused) {
                                }
                            }
                        }
                        CommandV2_Up_Empty commandV2_Up_Empty3 = new CommandV2_Up_Empty();
                        commandV2_Up_Empty3.setCommandStatusListener(new CommandStatusListener() { // from class: com.shj.biz.ShjManager.64
                            @Override // com.shj.command.CommandStatusListener
                            public void onCommandError(Command command, CommandError commandError) {
                            }

                            AnonymousClass64() {
                            }

                            @Override // com.shj.command.CommandStatusListener
                            public void onCommandFinished(Command command) {
                                ShjManager.getStatusListener().onShelvesReseted(Shj.getShelves());
                            }
                        });
                        CommandManager.appendSendCommand(commandV2_Up_Empty3);
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 11:
                        String[] split7 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand2.setMergeShelf(true, Integer.parseInt(split7[0]), Integer.parseInt(split7[1]));
                        Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.65
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass65() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '\f':
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand3.ClearMergeShelf();
                        Shj.postSetCommand(commandV2_Up_SetCommand3, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.66
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass66() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '\r':
                        String[] split8 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand4 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand4.setMergeShelSynRunTime(true, Integer.parseInt(split8[0]), Integer.parseInt(split8[1]));
                        Shj.postSetCommand(commandV2_Up_SetCommand4, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.67
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass67() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 14:
                        String[] split9 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand5 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand5.setEnginDLYZ(true, Integer.parseInt(split9[0]), Integer.parseInt(split9[1]));
                        Shj.postSetCommand(commandV2_Up_SetCommand5, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.68
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass68() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 15:
                        String[] split10 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand6 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand6.setConnectLift(true, Integer.parseInt(split10[0]), Integer.parseInt(split10[1]) == 1, Integer.parseInt(split10[2]) == 1);
                        Shj.postSetCommand(commandV2_Up_SetCommand6, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.69
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass69() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 16:
                        String[] split11 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand7 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand7.setGuardDoorCloseTime(true, Integer.parseInt(split11[0]), Integer.parseInt(split11[1]));
                        Shj.postSetCommand(commandV2_Up_SetCommand7, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.70
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass70() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 17:
                        String[] split12 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand8 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand8.setCoinCount_05(true, Integer.parseInt(split12[0]));
                        Shj.postSetCommand(commandV2_Up_SetCommand8, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.71
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass71() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 18:
                        String[] split13 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand9 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand9.setCoinCount_10(true, Integer.parseInt(split13[0]));
                        Shj.postSetCommand(commandV2_Up_SetCommand9, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.72
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass72() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 19:
                        String[] split14 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand10 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand10.setLight(true, Integer.parseInt(split14[0]), Integer.parseInt(split14[1]));
                        Shj.postSetCommand(commandV2_Up_SetCommand10, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.73
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass73() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 20:
                        String[] split15 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand11 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand11.setYLICCard(true, Integer.parseInt(split15[0]) == 0);
                        Shj.postSetCommand(commandV2_Up_SetCommand11, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.74
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass74() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 21:
                        String[] split16 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand12 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand12.setPaperMoney(true, Integer.parseInt(split16[0]));
                        Shj.postSetCommand(commandV2_Up_SetCommand12, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.75
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass75() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 22:
                        String[] split17 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand13 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand13.setPaperModel(true, Integer.parseInt(split17[0]));
                        Shj.postSetCommand(commandV2_Up_SetCommand13, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.76
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass76() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 23:
                        String[] split18 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand14 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand14.setAcceptPaperMinChargeMoney(true, Integer.parseInt(split18[0]));
                        Shj.postSetCommand(commandV2_Up_SetCommand14, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.77
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass77() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 24:
                        String[] split19 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand15 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand15.setAutoChargeTime(true, Integer.parseInt(split19[0]));
                        Shj.postSetCommand(commandV2_Up_SetCommand15, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.78
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass78() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 25:
                        String[] split20 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand16 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand16.setAutoEatMoneyTime(true, Integer.parseInt(split20[0]));
                        Shj.postSetCommand(commandV2_Up_SetCommand16, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.79
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass79() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 26:
                        String[] split21 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand17 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand17.setAutoEatMoneyTime(true, Integer.parseInt(split21[0]));
                        Shj.postSetCommand(commandV2_Up_SetCommand17, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.80
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass80() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 27:
                        String[] split22 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        ReportManager.reportSetDropCheck(Integer.parseInt(split22[0]) == 100 ? 3 : 2, Integer.parseInt(split22[0]), Integer.parseInt(split22[1]));
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand18 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand18.setGoodsDroopCheck(true, Integer.parseInt(split22[0]), Integer.parseInt(split22[1]) == 1);
                        Shj.postSetCommand(commandV2_Up_SetCommand18, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.81
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass81() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 28:
                        String[] split23 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand19 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand19.setBeltTime(true, Integer.parseInt(split23[0]), Integer.parseInt(split23[1]), Integer.parseInt(split23[2]));
                        Shj.postSetCommand(commandV2_Up_SetCommand19, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.82
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass82() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 29:
                        String[] split24 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand20 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand20.setBlock41(true, Integer.parseInt(split24[0]), Integer.parseInt(split24[1]) == 0);
                        Shj.postSetCommand(commandV2_Up_SetCommand20, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.83
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass83() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 30:
                        String[] split25 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand21 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand21.setcontinueSaleAfterBlock(true, Integer.parseInt(split25[0]), Integer.parseInt(split25[1]) == 2);
                        Shj.postSetCommand(commandV2_Up_SetCommand21, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.84
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass84() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case 31:
                        try {
                            String[] split26 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                            CommandV2_Up_SetCommand commandV2_Up_SetCommand22 = new CommandV2_Up_SetCommand();
                            int parseInt5 = Integer.parseInt(split26[1]);
                            if (Shj.getPicker(0) != null) {
                                Loger.writeLog("UI", "设置盒饭机温控仪");
                                if (parseInt5 == 1) {
                                    parseInt5 = 2;
                                } else if (parseInt5 == 2) {
                                    parseInt5 = 1;
                                }
                            }
                            commandV2_Up_SetCommand22.setWkY(true, Integer.parseInt(split26[0]), parseInt5, Integer.parseInt(split26[2]));
                            Shj.postSetCommand(commandV2_Up_SetCommand22, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.85
                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandReadAnswer(byte[] bArr) {
                                }

                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandSetAnswer(boolean z2) {
                                }

                                AnonymousClass85() {
                                }
                            });
                            ReportManager.reportSetTemperature(Integer.parseInt(split26[2]), parseInt5);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case ' ':
                        String[] split27 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand23 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand23.setYSJ(true, Integer.parseInt(split27[0]), Integer.parseInt(split27[1]), Integer.parseInt(split27[2]), Integer.parseInt(split27[3]), Integer.parseInt(split27[4]), Integer.parseInt(split27[5]), Integer.parseInt(split27[6]));
                        Shj.postSetCommand(commandV2_Up_SetCommand23, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.86
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass86() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '!':
                    case '\"':
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand24 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand24.setClearBlocks(true);
                        Shj.postSetCommand(commandV2_Up_SetCommand24, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.87
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            AnonymousClass87() {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                                try {
                                    ShjManager.clearShelfBlock();
                                    Iterator<Integer> it = Shj.getShelves().iterator();
                                    while (it.hasNext()) {
                                        try {
                                            Shj.onUpdateShelfState(it.next().intValue(), 0);
                                        } catch (Exception unused2) {
                                        }
                                    }
                                } catch (Exception unused3) {
                                }
                            }
                        });
                        if (Shj.isDebug()) {
                            Iterator<Integer> it = Shj.getShelves().iterator();
                            while (it.hasNext()) {
                                try {
                                    Shj.onUpdateShelfState(it.next().intValue(), 0);
                                } catch (Exception unused2) {
                                }
                            }
                        }
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand25 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand25.setClearEnginError(true);
                        Shj.postSetCommand(commandV2_Up_SetCommand25, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.88
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass88() {
                            }
                        });
                        if (Shj.isDebug()) {
                            Iterator<Integer> it2 = Shj.getShelves().iterator();
                            while (it2.hasNext()) {
                                try {
                                    Shj.onUpdateShelfState(it2.next().intValue(), 0);
                                } catch (Exception unused3) {
                                }
                            }
                        }
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '#':
                        split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand26 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand26.setClearLiftError(true);
                        Shj.postSetCommand(commandV2_Up_SetCommand26, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.89
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass89() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '$':
                        split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand27 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand27.setClearBlock41s(true);
                        Shj.postSetCommand(commandV2_Up_SetCommand27, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.90
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass90() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '%':
                        String[] split28 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand28 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand28.setWKYSets(true, Integer.parseInt(split28[0]), Integer.parseInt(split28[1]), Integer.parseInt(split28[2]), Integer.parseInt(split28[3]), Integer.parseInt(split28[4]), Integer.parseInt(split28[5]), Integer.parseInt(split28[6]), Integer.parseInt(split28[7]), Integer.parseInt(split28[8]));
                        Shj.postSetCommand(commandV2_Up_SetCommand28, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.91
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass91() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '&':
                        String[] split29 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand29 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand29.TestShelf(true, Integer.parseInt(split29[0]), Integer.parseInt(split29[1]));
                        Shj.postSetCommand(commandV2_Up_SetCommand29, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.92
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass92() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '\'':
                        String[] split30 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand30 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand30.TestChargeCoin(true, Integer.parseInt(split30[0]), Integer.parseInt(split30[1]), Integer.parseInt(split30[2]));
                        Shj.postSetCommand(commandV2_Up_SetCommand30, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.93
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass93() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case ')':
                        String[] split31 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand31 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand31.setOfferGoodsCheck_Sensitivity(true, Integer.parseInt(split31[0]), Integer.parseInt(split31[1]));
                        Shj.postSetCommand(commandV2_Up_SetCommand31, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.94
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass94() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '*':
                        String[] split32 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand32 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand32.setWBLHeatTime(true, Integer.parseInt(split32[1]), Integer.parseInt(split32[0]));
                        Shj.postSetCommand(commandV2_Up_SetCommand32, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.95
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass95() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '+':
                        String[] split33 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand33 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand33.setWBLHeat(true, Integer.parseInt(split33[1]) == 1);
                        Shj.postSetCommand(commandV2_Up_SetCommand33, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.96
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass96() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case ',':
                        OpenGridMachineGrid(Integer.valueOf(split[1]).intValue(), -1);
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '-':
                        String[] split34 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        OpenGridMachineGrid(Integer.valueOf(split34[0]).intValue(), Integer.valueOf(split34[1]).intValue());
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '.':
                        String[] split35 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand34 = new CommandV2_Up_SetCommand();
                        int intValue = Integer.valueOf(split35[0]).intValue();
                        int intValue2 = Integer.valueOf(split35[1]).intValue();
                        if (intValue2 == 2 && intValue == 0) {
                            intValue2 = 3;
                        }
                        commandV2_Up_SetCommand34.TestHFJ_JG(intValue, intValue2, Integer.valueOf(split35[2]).intValue());
                        Shj.postSetCommand(commandV2_Up_SetCommand34, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.97
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass97() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '/':
                        String[] split36 = split[1].split(VoiceWakeuperAidl.PARAMS_SEPARATE);
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand35 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand35.setTopLight(true, Integer.valueOf(split36[0]).intValue(), Integer.valueOf(split36[1]).intValue());
                        Shj.postSetCommand(commandV2_Up_SetCommand35, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.98
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass98() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '0':
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand36 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand36.setLightBoxRollingInterval(true, Integer.valueOf(split[1]).intValue());
                        Shj.postSetCommand(commandV2_Up_SetCommand36, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.99
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass99() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                    case '1':
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand37 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand37.rebootMachine();
                        Shj.postSetCommand(commandV2_Up_SetCommand37, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.100
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z2) {
                            }

                            AnonymousClass100() {
                            }
                        });
                        report_CmdAck.setParams(split[0], "1", "accept", split[3], split[4]);
                        break;
                }
            } catch (Exception unused4) {
            }
        } catch (Exception unused5) {
            report_CmdAck.setParams(split[0], "4", "accept", split[3], split[4]);
        }
        NetManager.appendReport(report_CmdAck);
        Shj.onFrontCommandFinished(split, new FrontCommandFinishedListener() { // from class: com.shj.biz.ShjManager.101
            AnonymousClass101() {
            }

            @Override // com.shj.FrontCommandFinishedListener
            public void onFrontCommandFinished(Object obj) {
                String[] strArr = (String[]) obj;
                Report_CmdAck report_CmdAck2 = new Report_CmdAck();
                report_CmdAck2.setParams(strArr[0], ExifInterface.GPS_MEASUREMENT_3D, "command finished", strArr[3], strArr[4]);
                NetManager.appendReport(report_CmdAck2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$60 */
    /* loaded from: classes2.dex */
    public class AnonymousClass60 implements Runnable {
        AnonymousClass60() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ShjManager.isShjLocked) {
                ActivityHelper.showLockScreenDialog(ShjManager.getActivityContext(), AndroidSystem.getLanguage(ShjManager.getActivityContext()).equalsIgnoreCase("zh") ? "暂停营业" : "CLOSED");
            } else {
                ActivityHelper.closeLockScreenDialog();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$61 */
    /* loaded from: classes2.dex */
    public class AnonymousClass61 implements CommandStatusListener {
        final /* synthetic */ List val$changedShelves;

        @Override // com.shj.command.CommandStatusListener
        public void onCommandError(Command command, CommandError commandError) {
        }

        AnonymousClass61(List arrayList2) {
            arrayList = arrayList2;
        }

        @Override // com.shj.command.CommandStatusListener
        public void onCommandFinished(Command command) {
            ShjManager.getStatusListener().onShelvesReseted(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$62 */
    /* loaded from: classes2.dex */
    public class AnonymousClass62 implements CommandStatusListener {
        final /* synthetic */ List val$changedShelves;

        @Override // com.shj.command.CommandStatusListener
        public void onCommandError(Command command, CommandError commandError) {
        }

        AnonymousClass62(List arrayList22) {
            arrayList2 = arrayList22;
        }

        @Override // com.shj.command.CommandStatusListener
        public void onCommandFinished(Command command) {
            ShjManager.getStatusListener().onShelvesReseted(arrayList2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$63 */
    /* loaded from: classes2.dex */
    public class AnonymousClass63 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass63() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$64 */
    /* loaded from: classes2.dex */
    public class AnonymousClass64 implements CommandStatusListener {
        @Override // com.shj.command.CommandStatusListener
        public void onCommandError(Command command, CommandError commandError) {
        }

        AnonymousClass64() {
        }

        @Override // com.shj.command.CommandStatusListener
        public void onCommandFinished(Command command) {
            ShjManager.getStatusListener().onShelvesReseted(Shj.getShelves());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$65 */
    /* loaded from: classes2.dex */
    public class AnonymousClass65 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass65() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$66 */
    /* loaded from: classes2.dex */
    public class AnonymousClass66 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass66() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$67 */
    /* loaded from: classes2.dex */
    public class AnonymousClass67 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass67() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$68 */
    /* loaded from: classes2.dex */
    public class AnonymousClass68 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass68() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$69 */
    /* loaded from: classes2.dex */
    public class AnonymousClass69 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass69() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$70 */
    /* loaded from: classes2.dex */
    public class AnonymousClass70 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass70() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$71 */
    /* loaded from: classes2.dex */
    public class AnonymousClass71 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass71() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$72 */
    /* loaded from: classes2.dex */
    public class AnonymousClass72 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass72() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$73 */
    /* loaded from: classes2.dex */
    public class AnonymousClass73 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass73() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$74 */
    /* loaded from: classes2.dex */
    public class AnonymousClass74 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass74() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$75 */
    /* loaded from: classes2.dex */
    public class AnonymousClass75 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass75() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$76 */
    /* loaded from: classes2.dex */
    public class AnonymousClass76 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass76() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$77 */
    /* loaded from: classes2.dex */
    public class AnonymousClass77 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass77() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$78 */
    /* loaded from: classes2.dex */
    public class AnonymousClass78 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass78() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$79 */
    /* loaded from: classes2.dex */
    public class AnonymousClass79 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass79() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$80 */
    /* loaded from: classes2.dex */
    public class AnonymousClass80 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass80() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$81 */
    /* loaded from: classes2.dex */
    public class AnonymousClass81 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass81() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$82 */
    /* loaded from: classes2.dex */
    public class AnonymousClass82 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass82() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$83 */
    /* loaded from: classes2.dex */
    public class AnonymousClass83 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass83() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$84 */
    /* loaded from: classes2.dex */
    public class AnonymousClass84 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass84() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$85 */
    /* loaded from: classes2.dex */
    public class AnonymousClass85 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass85() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$86 */
    /* loaded from: classes2.dex */
    public class AnonymousClass86 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass86() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$87 */
    /* loaded from: classes2.dex */
    public class AnonymousClass87 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass87() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            try {
                ShjManager.clearShelfBlock();
                Iterator<Integer> it = Shj.getShelves().iterator();
                while (it.hasNext()) {
                    try {
                        Shj.onUpdateShelfState(it.next().intValue(), 0);
                    } catch (Exception unused2) {
                    }
                }
            } catch (Exception unused3) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$88 */
    /* loaded from: classes2.dex */
    public class AnonymousClass88 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass88() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$89 */
    /* loaded from: classes2.dex */
    public class AnonymousClass89 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass89() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$90 */
    /* loaded from: classes2.dex */
    public class AnonymousClass90 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass90() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$91 */
    /* loaded from: classes2.dex */
    public class AnonymousClass91 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass91() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$92 */
    /* loaded from: classes2.dex */
    public class AnonymousClass92 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass92() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$93 */
    /* loaded from: classes2.dex */
    public class AnonymousClass93 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass93() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$94 */
    /* loaded from: classes2.dex */
    public class AnonymousClass94 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass94() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$95 */
    /* loaded from: classes2.dex */
    public class AnonymousClass95 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass95() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$96 */
    /* loaded from: classes2.dex */
    public class AnonymousClass96 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass96() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$97 */
    /* loaded from: classes2.dex */
    public class AnonymousClass97 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass97() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$98 */
    /* loaded from: classes2.dex */
    public class AnonymousClass98 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass98() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$99 */
    /* loaded from: classes2.dex */
    public class AnonymousClass99 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass99() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$100 */
    /* loaded from: classes2.dex */
    public class AnonymousClass100 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass100() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$101 */
    /* loaded from: classes2.dex */
    public class AnonymousClass101 implements FrontCommandFinishedListener {
        AnonymousClass101() {
        }

        @Override // com.shj.FrontCommandFinishedListener
        public void onFrontCommandFinished(Object obj) {
            String[] strArr = (String[]) obj;
            Report_CmdAck report_CmdAck2 = new Report_CmdAck();
            report_CmdAck2.setParams(strArr[0], ExifInterface.GPS_MEASUREMENT_3D, "command finished", strArr[3], strArr[4]);
            NetManager.appendReport(report_CmdAck2);
        }
    }

    private static void OpenGridMachineGrid(int i, int i2) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setOpenGridMachineGrid(i, i2);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.biz.ShjManager.102
            final /* synthetic */ int val$jgh;
            final /* synthetic */ int val$layer;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass102(int i22, int i3) {
                i2 = i22;
                i = i3;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (z) {
                    return;
                }
                if (i2 == -1) {
                    if (i == 99) {
                        List<Integer> shelves = Shj.getShelves();
                        if (shelves != null) {
                            Iterator<Integer> it = shelves.iterator();
                            while (it.hasNext()) {
                                int intValue = it.next().intValue();
                                ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
                                if (shelfInfo != null && shelfInfo.getShelfType() == ShelfType.Box) {
                                    CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                                    commandV2_Up_SetCommand2.TestShelf(false, 1, intValue);
                                    Shj.postSetCommand(commandV2_Up_SetCommand2, null);
                                }
                            }
                            return;
                        }
                        return;
                    }
                    List<Integer> shelves2 = Shj.getShelves();
                    if (shelves2 != null) {
                        Iterator<Integer> it2 = shelves2.iterator();
                        while (it2.hasNext()) {
                            int intValue2 = it2.next().intValue();
                            ShelfInfo shelfInfo2 = Shj.getShelfInfo(Integer.valueOf(intValue2));
                            if (shelfInfo2 != null && shelfInfo2.getJgh().intValue() == i && shelfInfo2.getShelfType() == ShelfType.Box) {
                                CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
                                commandV2_Up_SetCommand3.TestShelf(false, 1, intValue2);
                                Shj.postSetCommand(commandV2_Up_SetCommand3, null);
                            }
                        }
                        return;
                    }
                    return;
                }
                List<Integer> shelves3 = Shj.getShelves();
                if (shelves3 != null) {
                    Iterator<Integer> it3 = shelves3.iterator();
                    while (it3.hasNext()) {
                        int intValue3 = it3.next().intValue();
                        ShelfInfo shelfInfo3 = Shj.getShelfInfo(Integer.valueOf(intValue3));
                        if (shelfInfo3 != null && shelfInfo3.getLayer().intValue() == i2 && shelfInfo3.getShelfType() == ShelfType.Box) {
                            CommandV2_Up_SetCommand commandV2_Up_SetCommand4 = new CommandV2_Up_SetCommand();
                            commandV2_Up_SetCommand4.TestShelf(false, 1, intValue3);
                            Shj.postSetCommand(commandV2_Up_SetCommand4, null);
                        }
                    }
                }
            }
        });
        if (Shj.isDebug()) {
            Iterator<Integer> it = Shj.getMachine(i3, false).getShelves().iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
                if (shelfInfo != null && shelfInfo.getLayer().intValue() == i22 && shelfInfo.getShelfType() == ShelfType.Box) {
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                    commandV2_Up_SetCommand2.TestShelf(true, 1, intValue);
                    CommandManager.appendSendCommand(commandV2_Up_SetCommand2);
                }
            }
        }
    }

    /* renamed from: com.shj.biz.ShjManager$102 */
    /* loaded from: classes2.dex */
    public class AnonymousClass102 implements OnCommandAnswerListener {
        final /* synthetic */ int val$jgh;
        final /* synthetic */ int val$layer;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass102(int i22, int i3) {
            i2 = i22;
            i = i3;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (z) {
                return;
            }
            if (i2 == -1) {
                if (i == 99) {
                    List<Integer> shelves = Shj.getShelves();
                    if (shelves != null) {
                        Iterator<Integer> it = shelves.iterator();
                        while (it.hasNext()) {
                            int intValue = it.next().intValue();
                            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
                            if (shelfInfo != null && shelfInfo.getShelfType() == ShelfType.Box) {
                                CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                                commandV2_Up_SetCommand2.TestShelf(false, 1, intValue);
                                Shj.postSetCommand(commandV2_Up_SetCommand2, null);
                            }
                        }
                        return;
                    }
                    return;
                }
                List<Integer> shelves2 = Shj.getShelves();
                if (shelves2 != null) {
                    Iterator<Integer> it2 = shelves2.iterator();
                    while (it2.hasNext()) {
                        int intValue2 = it2.next().intValue();
                        ShelfInfo shelfInfo2 = Shj.getShelfInfo(Integer.valueOf(intValue2));
                        if (shelfInfo2 != null && shelfInfo2.getJgh().intValue() == i && shelfInfo2.getShelfType() == ShelfType.Box) {
                            CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
                            commandV2_Up_SetCommand3.TestShelf(false, 1, intValue2);
                            Shj.postSetCommand(commandV2_Up_SetCommand3, null);
                        }
                    }
                    return;
                }
                return;
            }
            List<Integer> shelves3 = Shj.getShelves();
            if (shelves3 != null) {
                Iterator<Integer> it3 = shelves3.iterator();
                while (it3.hasNext()) {
                    int intValue3 = it3.next().intValue();
                    ShelfInfo shelfInfo3 = Shj.getShelfInfo(Integer.valueOf(intValue3));
                    if (shelfInfo3 != null && shelfInfo3.getLayer().intValue() == i2 && shelfInfo3.getShelfType() == ShelfType.Box) {
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand4 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand4.TestShelf(false, 1, intValue3);
                        Shj.postSetCommand(commandV2_Up_SetCommand4, null);
                    }
                }
            }
        }
    }

    public static void onAcceptBinUpdatePacke(byte[] bArr) {
        int intFromBytes;
        int intFromBytes2;
        int intFromBytes3;
        byte[] bArr2;
        String asString;
        String[] split;
        Loger.writeLog("NET", "Accept file package len :" + bArr.length);
        try {
            intFromBytes = ObjectHelper.intFromBytes(bArr, 5, 1);
            intFromBytes2 = ObjectHelper.intFromBytes(bArr, 6, 2);
            intFromBytes3 = ObjectHelper.intFromBytes(bArr, 8, 2);
            int length = bArr.length - 11;
            bArr2 = new byte[length];
            ObjectHelper.updateBytes(bArr2, bArr, 10, 0, length);
            CacheHelper.getFileCache().getAsString("ToUpdateBinFile_dpj_stoped");
            String asString2 = CacheHelper.getFileCache().getAsString("ToUpdateBinFile_dpj");
            asString = CacheHelper.getFileCache().getAsString("ToUpdateBinFile_dpj_current_no");
            split = asString2.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
        } catch (Exception unused) {
        }
        if (split[0].equalsIgnoreCase("" + intFromBytes3)) {
            if (asString.equalsIgnoreCase("" + intFromBytes2)) {
                if (intFromBytes == 1) {
                    Loger.writeLog("NET", "update bin file CANCELED");
                    CacheHelper.getFileCache().put("ToUpdateBinFile_dpj_stoped", "CANCELED");
                    SDFileUtils.safeDeleteFile(new File(SDFileUtils.SDCardRoot + "xyShj/update/" + split[2]));
                    return;
                }
                if (intFromBytes == 2) {
                    CacheHelper.getFileCache().put("ToUpdateBinFile_dpj_stoped", "TRUE");
                }
                try {
                    try {
                        try {
                            new FileOutputStream(new File(SDFileUtils.SDCardRoot + "xyShj/update/" + split[2]), true).write(bArr2);
                            if (intFromBytes2 == intFromBytes3 - 1) {
                                Loger.writeLog("NET", "file download finished");
                                CacheHelper.getFileCache().put("ToUpdateBinFile_dpj_stoped", "FINISHED");
                            } else {
                                CacheHelper.getFileCache().put("ToUpdateBinFile_dpj_current_no", "" + (intFromBytes2 + 1));
                            }
                            if (System.out != null) {
                                System.out.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (System.out != null) {
                                System.out.close();
                            }
                        }
                    } catch (Throwable th) {
                        try {
                            if (System.out != null) {
                                System.out.close();
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        throw th;
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                requestNextBinUpdatePackge();
            }
        }
    }

    public static void onRequestNextBinUpdatePackge_needPause() {
        CacheHelper.getFileCache().put("ToUpdateBinFile_dpj_wait_step", "0");
    }

    public static void setBoxShelfEmpty(int i) {
        try {
            setShelfGoodsCount(i, 1);
        } catch (Exception unused) {
        }
    }

    public static void setBoxShelfFull(int i) {
        try {
            setShelfGoodsCount(i, 2);
        } catch (Exception unused) {
        }
    }

    public static void weiXinAntiScanCodePay(Context context, String str, String str2, Order order) {
        Report_Transfdata report_Transfdata = new Report_Transfdata();
        report_Transfdata.setParams(259, str + Marker.ANY_MARKER + str2 + "*01*" + order.getPayId() + Marker.ANY_MARKER + order.getPayPrice() + Marker.ANY_MARKER + order.getGoodsName() + Marker.ANY_MARKER + System.currentTimeMillis() + Marker.ANY_MARKER);
        NetManager.appendReport(report_Transfdata);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ShjManager$103 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass103 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$OfferState;

        static {
            int[] iArr = new int[OfferState.values().length];
            $SwitchMap$com$shj$OfferState = iArr;
            try {
                iArr[OfferState.Blocked.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.EngineError.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.LiftError.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.LiftUpError.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.LiftDownError.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.NoWBLCloseFrontDoorError.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.NoWBLOpenBackDoorError.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.NoWBLCloseBackDoorError.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.CGResetError.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.LiftCGPushError.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.LiftInWBLError.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.LiftOutWBLError.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.WBLInnerTGPushError.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.WBLInnerTGBackError.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.CargoLaneInvalid.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.NoEngine.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.BeltCheckError.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.BusinessStoped.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.WBLWaitting2PickGoods.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.ShelfOfferGoodsPaused.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.ShelfNoGoods.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
        }
    }

    public static int offerDeviceErrorState2ServerState(OfferState offerState) {
        switch (AnonymousClass103.$SwitchMap$com$shj$OfferState[offerState.ordinal()]) {
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 6;
            case 4:
                return 10;
            case 5:
                return 14;
            case 6:
                return 24;
            case 7:
                return 25;
            case 8:
                return 26;
            case 9:
                return 28;
            case 10:
                return 29;
            case 11:
                return 30;
            case 12:
                return 31;
            case 13:
                return 32;
            case 14:
                return 33;
            case 15:
                return 12;
            case 16:
                return 5;
            case 17:
                return 11;
            case 18:
            default:
                return 99;
            case 19:
                return 8;
            case 20:
                return 4;
            case 21:
                return 34;
        }
    }

    public static String getJqConnectUrl() {
        if (JQ_CONNECT_URL == null) {
            JQ_CONNECT_URL = NetAddress.getJQConnectUrl();
        }
        return JQ_CONNECT_URL;
    }

    public static void setJqConnectUrl(String str) {
        SDFileUtils.isFileExist("shj.cfg", SDFileUtils.SDCardRoot);
        JQ_CONNECT_URL = str;
    }

    public static boolean isLightStatus() {
        return lightStatus;
    }

    public static void setLightStatus(boolean z) {
        lightStatus = z;
    }
}
