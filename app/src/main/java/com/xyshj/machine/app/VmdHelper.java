package com.xyshj.machine.app;

import android.serialport.SerialPort;
import android.support.v4.view.PointerIconCompat;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.platform.comapi.d;
import com.hanlu.toolsdk.BqlManager;
import com.hanlu.toolsdk.SerialPortUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.AndroidSystem;
import com.oysb.utils.Loger;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.date.DateUtil;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.shj.OfferState;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.biz.ShjManager;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderPayType;
import com.shj.biz.yg.YGDBHelper;
import com.shj.device.cardreader.MdbReader_BDT;
import com.shj.setting.NetAddress.DefaultAMEAddrss;
import com.shj.setting.NetAddress.NetAddress;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.machine.R;
import com.xyshj.machine.popview.PopView_Info;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.internal.ws.WebSocketProtocol;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class VmdHelper {
    public static final String ACTION_UPDATE_YYJE = "ACTION_UPDATE_YYJE";
    public static final String BQL_TYPE_CUSTOMER = "customer";
    public static final String BQL_TYPE_NORMAL = "normal";
    static VmdHelper vmdHelper = new VmdHelper();
    BqlOrder bqlOrder;
    SerialPort serialPortSTK920;
    ArrayList<TopItem> topDatas = new ArrayList<>();
    ArrayList<BqlItem> bqlDatas = new ArrayList<>();
    HashMap<String, BqlItem> mapBqlDatas = new HashMap<>();
    HashMap<String, TopItem> mapTopDatas = new HashMap<>();
    BqlItem freeCollocationBql = null;
    int d9 = 4;
    boolean _isBqlStoped = false;
    boolean _isBqlQingxi = false;
    boolean _isBqlGuoye = false;
    boolean _isRestarting = false;
    boolean _isQueliao = false;
    boolean _isdonggan = false;
    boolean _isbianpingError = false;
    boolean _isqianyaError = false;
    boolean _isgaoyaError = false;
    boolean _isGaoWenError = false;
    boolean _isbqlstatusChanged = false;
    short tempretrue = 0;
    JSONObject disCounts = null;
    boolean shelvesUpdated = false;
    boolean isServerSynOk = true;
    List<BqlOrder> orderList = new ArrayList();
    ExecutorService threadService = null;
    ExecutorService threadService2 = null;
    long lastDonganTime = Long.MAX_VALUE;
    String zhibei = ShjAppHelper.getString(R.string.paper_cup);
    String iceCream = ShjAppHelper.getString(R.string.app_name);
    ArrayList<ShelfInfo> shelfInfos = new ArrayList<>();
    boolean isVmdConnected = false;
    boolean isSTK920Connected = false;
    Timer timer = new Timer();
    boolean isOffering = false;
    long lastFinishTime = 0;
    long lastQxTime = 0;
    String bqlError = "";
    boolean lastReportBqlStop = false;
    boolean lastReportBqlError = false;
    boolean lastReportBqlClean = false;
    boolean lastReportBqlguoye = false;
    boolean isLastReportBqlXiumian = false;
    boolean isRunningGuoye = false;
    long lastCheckBqlPlcStatusTime = 0;
    boolean canCheckingBqlPlcStatus = true;
    int countAfterQianliao = 0;
    boolean isBqlPowOffAfterDongan = false;
    String lastReportV = "";
    long lastcheckServerSyn = 0;
    long lastCheckErrorTime = 0;
    int lastBalance = -1;

    /* loaded from: classes2.dex */
    public interface CheckBqlStatusListener {
        void onCheckBqlStatusResult(boolean z, String str);

        void onStartWait2StatusOk(String str);
    }

    /* loaded from: classes2.dex */
    public interface VmdConnectLisener {
        void onConnectResult(boolean z, String str);
    }

    /* loaded from: classes2.dex */
    public interface VmdResultLisener {
        void onVmdResult(Object obj);
    }

    short convertTempreture(char c, char c2) {
        return (short) (-(((short) ((c ^ 255) << 8)) + ((short) (c2 ^ 255)) + 1));
    }

    public void order_updateOfferStatus(String str) {
    }

    void reportOrder2Server() {
    }

    private VmdHelper() {
    }

    void updateTempreture(short s) {
        this.tempretrue = (short) (s - 5);
        Loger.writeLog("SHJ", "温度:" + ((int) s) + " 校正后:" + ((int) this.tempretrue));
        int warnningTempreture = getWarnningTempreture();
        int stopbyTempretureTimelen = getStopbyTempretureTimelen();
        Loger.writeLog("SHJ", "er_tempretrue:" + warnningTempreture + " er_timelenset:" + stopbyTempretureTimelen);
        if (warnningTempreture != 0 && stopbyTempretureTimelen != 0) {
            if (isStoped_byTempretrue()) {
                this.tempretrue = (short) (warnningTempreture + 5);
            } else if (this.tempretrue < warnningTempreture) {
                Loger.writeLog("SHJ", "没有超时就恢复了");
                updateTempreTureWarinningStartTime(-1L);
            } else {
                long tempreTureWarinningStartTime = getTempreTureWarinningStartTime();
                StringBuilder sb = new StringBuilder();
                sb.append("之前是否温度预警?");
                sb.append(tempreTureWarinningStartTime != -1);
                Loger.writeLog("SHJ", sb.toString());
                if (tempreTureWarinningStartTime != -1) {
                    long currentTimeMillis = System.currentTimeMillis() - tempreTureWarinningStartTime;
                    Loger.writeLog("SHJ", "预警持续时长?" + currentTimeMillis);
                    if ((currentTimeMillis / 1000) / 60 > stopbyTempretureTimelen) {
                        setStoped_byTempretrue(true);
                    }
                } else {
                    Loger.writeLog("SHJ", ">>更新温度预警时间");
                    updateTempreTureWarinningStartTime(System.currentTimeMillis());
                }
            }
        }
        Shj.setTemperature(0, this.tempretrue);
    }

    public void updateDiscount(double d, double d2) {
        if (this.disCounts == null) {
            JSONObject jSONObject = new JSONObject();
            this.disCounts = jSONObject;
            jSONObject.put("d1", (Object) Double.valueOf(1.0d));
            this.disCounts.put("d2", (Object) Double.valueOf(1.0d));
            this.disCounts.put("d3", (Object) Double.valueOf(1.0d));
        }
        this.disCounts.put("d2", (Object) Double.valueOf(d));
        this.disCounts.put("d3", (Object) Double.valueOf(d2));
    }

    public double getDiscount(int i) {
        return this.disCounts.getDouble(d.a + i).doubleValue();
    }

    public boolean isNeedStopBql() {
        boolean z;
        if (isStoped_byTempretrue()) {
            Loger.writeLog("SHJ", "温度值过高,已停机");
            return true;
        }
        try {
            String asString = CacheHelper.getFileCache().getAsString("BqlStopTimeStart");
            String asString2 = CacheHelper.getFileCache().getAsString("BqlStopTimeStop");
            int parseInt = Integer.parseInt(asString);
            int parseInt2 = Integer.parseInt(asString2);
            if (parseInt == parseInt2) {
                return false;
            }
            int i = Calendar.getInstance().get(11);
            Loger.writeLog("SHJ", "cur hour:" + i + " start:" + parseInt + " stop:" + parseInt2);
            if (parseInt2 > parseInt) {
                StringBuilder sb = new StringBuilder();
                sb.append("need stop:");
                sb.append(i > parseInt && i < parseInt2);
                Loger.writeLog("SHJ", sb.toString());
                return i >= parseInt && i < parseInt2;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("need stop:");
            if (i >= parseInt2 && i <= parseInt) {
                z = false;
                sb2.append(z);
                Loger.writeLog("SHJ", sb2.toString());
                return i < parseInt2 || i >= parseInt;
            }
            z = true;
            sb2.append(z);
            Loger.writeLog("SHJ", sb2.toString());
            if (i < parseInt2) {
                return true;
            }
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean isNeedOpenBqlLight() {
        try {
            String asString = CacheHelper.getFileCache().getAsString("BqlLightTimeStart");
            String asString2 = CacheHelper.getFileCache().getAsString("BqlLightTimeStop");
            int parseInt = Integer.parseInt(asString);
            int parseInt2 = Integer.parseInt(asString2);
            int i = Calendar.getInstance().get(11);
            if (parseInt == parseInt2) {
                return false;
            }
            return parseInt2 > parseInt ? i >= parseInt && i < parseInt2 : i < parseInt2 || i >= parseInt;
        } catch (Exception unused) {
            return false;
        }
    }

    public int getChugao_time() {
        try {
            int parseInt = Integer.parseInt(CacheHelper.getFileCache().getAsString("BqlChugaoTime"));
            Loger.writeLog("SHJ", "出糕时长为：" + parseInt);
            return parseInt;
        } catch (Exception unused) {
            return 30;
        }
    }

    public int getZhigaoNoticePercent() {
        try {
            int parseInt = Integer.parseInt(CacheHelper.getFileCache().getAsString("BqlZhigaoNoticePercent"));
            Loger.writeLog("SHJ", "制糕提醒比例：" + parseInt);
            return parseInt;
        } catch (Exception unused) {
            return 20;
        }
    }

    public int getZhigaoCanOfferPercent() {
        try {
            int parseInt = Integer.parseInt(CacheHelper.getFileCache().getAsString("BqlZhigaoCanOfferPercent"));
            Loger.writeLog("SHJ", "制糕允许出货比例：" + parseInt);
            return parseInt;
        } catch (Exception unused) {
            return 20;
        }
    }

    public int getWarnningTempreture() {
        try {
            int parseInt = Integer.parseInt(CacheHelper.getFileCache().getAsString("BqlStoptempreture"));
            Loger.writeLog("SHJ", "报警温度值：" + parseInt);
            return parseInt;
        } catch (Exception unused) {
            return 0;
        }
    }

    public int getCleanCheckTime() {
        try {
            String asString = CacheHelper.getFileCache().getAsString("BqlCleanCheckTime");
            if (asString == null) {
                asString = "99999";
            }
            return Integer.parseInt(asString);
        } catch (Exception unused) {
            return 99999;
        }
    }

    public boolean isOutCleanCheck() {
        try {
            int cleanCheckTime = getCleanCheckTime();
            if (cleanCheckTime > 90000) {
                return false;
            }
            String asString = CacheHelper.getFileCache().getAsString("BqlCleanCheckTime_last");
            if (asString == null) {
                asString = "0";
            }
            long currentTimeMillis = System.currentTimeMillis() - Long.parseLong(asString);
            Loger.writeLog("SHJ", "BqlCleanCheckTime_last:" + asString + " pass:" + currentTimeMillis);
            return currentTimeMillis - (((((long) cleanCheckTime) * 1000) * 60) * 60) > 0;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean isStoped_byTempretrue() {
        Loger.writeLog("SHJ", "判断是否温度停机");
        try {
            String asString = CacheHelper.getFileCache().getAsString("BqlStoptempreture_stoped");
            Loger.writeLog("SHJ", "BqlStoptempreture_stoped：" + asString);
            int parseInt = Integer.parseInt(asString);
            StringBuilder sb = new StringBuilder();
            sb.append("因温度异常，已停机：");
            sb.append(parseInt == 1);
            Loger.writeLog("SHJ", sb.toString());
            return parseInt == 1;
        } catch (Exception unused) {
            return false;
        }
    }

    public void setStoped_byTempretrue(boolean z) {
        try {
            this._isGaoWenError = z;
            CacheHelper.getFileCache().put("BqlStoptempreture_stoped", z ? "1" : "0");
            Loger.writeLog("SHJ", "标记温度异常，停机");
            reportBqlStauts(false);
        } catch (Exception unused) {
        }
    }

    public long getTempreTureWarinningStartTime() {
        try {
            long parseLong = Long.parseLong(CacheHelper.getFileCache().getAsString("BqlStoptempreture_starttime"));
            Loger.writeLog("SHJ", "温度异常开始时间:" + parseLong);
            return parseLong;
        } catch (Exception unused) {
            return -1L;
        }
    }

    public void updateTempreTureWarinningStartTime(long j) {
        try {
            CacheHelper.getFileCache().put("BqlStoptempreture_starttime", "" + System.currentTimeMillis());
            Loger.writeLog("SHJ", "更新温度异常开始时间");
        } catch (Exception unused) {
        }
    }

    public int getStopbyTempretureTimelen() {
        try {
            int parseInt = Integer.parseInt(CacheHelper.getFileCache().getAsString("BqlStoptempreture_time"));
            Loger.writeLog("SHJ", "温度异常超时值：" + parseInt);
            return parseInt;
        } catch (Exception unused) {
            return 0;
        }
    }

    public static VmdHelper get() {
        return vmdHelper;
    }

    public ArrayList<TopItem> getTopDatas() {
        return this.topDatas;
    }

    public ArrayList<BqlItem> getBqlDatas() {
        return this.bqlDatas;
    }

    public BqlItem getBqlItem(String str) {
        return this.mapBqlDatas.get(str);
    }

    public TopItem getTopItem(String str) {
        return this.mapTopDatas.get(str);
    }

    public void updateLightStatus(boolean z) {
        if (this.threadService == null) {
            this.threadService = Executors.newFixedThreadPool(1);
        }
        this.threadService.execute(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.1
            final /* synthetic */ boolean val$needStopBqlLight;

            AnonymousClass1(boolean z2) {
                z = z2;
            }

            @Override // java.lang.Runnable
            public void run() {
                BqlManager.get().setLight(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.app.VmdHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ boolean val$needStopBqlLight;

        AnonymousClass1(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public void run() {
            BqlManager.get().setLight(z);
        }
    }

    /* loaded from: classes2.dex */
    public static class TopItem {
        String code;
        int imageResurceId;
        String name;
        int price;
        int shelf;
        String title;

        public int getType() {
            return this.shelf <= 3 ? 0 : 1;
        }

        public void setShelf(int i) {
            this.shelf = i;
        }

        public int getShelf() {
            return this.shelf;
        }

        public String getName() {
            return this.name;
        }

        public String getCode() {
            return this.code;
        }

        public int getPrice() {
            return this.price;
        }

        public String getTitle() {
            return this.title;
        }

        public int getImageResurceId() {
            return this.imageResurceId;
        }

        public void setTitle(String str) {
            this.title = str;
        }

        public void setName(String str) {
            this.name = str;
        }

        public void setPrice(int i) {
            this.price = i;
        }

        public void setImageResurceId(int i) {
            this.imageResurceId = i;
        }

        public void setCode(String str) {
            this.code = str;
        }
    }

    /* loaded from: classes2.dex */
    public static class BqlItem {
        String code;
        int imageResurceId;
        String name;
        int price;
        HashMap<String, Integer> tops = new HashMap<>();

        public HashMap<String, Integer> getTops() {
            return this.tops;
        }

        public String getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public int getPrice() {
            return this.price;
        }

        public int getImageResurceId() {
            return this.imageResurceId;
        }

        public void setName(String str) {
            this.name = str;
        }

        public void setPrice(int i) {
            this.price = i;
        }

        public void setImageResurceId(int i) {
            this.imageResurceId = i;
        }

        public void setCode(String str) {
            this.code = str;
        }
    }

    /* loaded from: classes2.dex */
    public static class BqlOrder {
        String name;
        String orderLid;
        String type;
        double discount = 1.0d;
        int successCount = 0;
        int yhje = 0;
        String yhm = "";
        HashMap<String, Integer> bqlItems = new HashMap<>();
        HashMap<String, Integer> tops = new HashMap<>();
        String sn = UUID.randomUUID().toString().replace("-", "");

        public void addSuccessCount() {
            this.successCount++;
        }

        public int getSuccessCount() {
            return this.successCount;
        }

        public void setYhje(int i) {
            this.yhje = i;
        }

        public void setYhm(String str) {
            this.yhm = str;
        }

        public int getYhje() {
            return this.yhje;
        }

        public int getTotalYhje() {
            int i = 0;
            for (String str : this.bqlItems.keySet()) {
                if (str.length() != 0) {
                    i += VmdHelper.get().mapBqlDatas.get(str).getPrice() * this.bqlItems.get(str).intValue();
                }
            }
            int totalBqlCount = getTotalBqlCount();
            for (String str2 : this.tops.keySet()) {
                if (str2.length() != 0) {
                    i += VmdHelper.get().mapTopDatas.get(str2).getPrice() * totalBqlCount;
                }
            }
            double discount = VmdHelper.get().getDiscount(totalBqlCount);
            this.discount = discount;
            double d = i;
            Double.isNaN(d);
            return i - (((int) (d * discount)) - this.yhje);
        }

        public String getYhm() {
            return this.yhm;
        }

        public double getOrderDiscount() {
            return VmdHelper.get().getDiscount(getTotalBqlCount());
        }

        public void setBqlCount(String str, int i) {
            this.bqlItems.put(str, Integer.valueOf(i));
        }

        public int getBqlCount(String str) {
            if (this.bqlItems.containsKey(str)) {
                return this.bqlItems.get(str).intValue();
            }
            return 0;
        }

        public int getTotalBqlCount() {
            Iterator<Integer> it = this.bqlItems.values().iterator();
            int i = 0;
            while (it.hasNext()) {
                i += it.next().intValue();
            }
            return i;
        }

        public int getOrderPrice() {
            int i = 0;
            for (String str : this.bqlItems.keySet()) {
                i += VmdHelper.get().mapBqlDatas.get(str).getPrice() * this.bqlItems.get(str).intValue();
            }
            int totalBqlCount = getTotalBqlCount();
            Iterator<String> it = this.tops.keySet().iterator();
            while (it.hasNext()) {
                i += VmdHelper.get().mapTopDatas.get(it.next()).getPrice() * totalBqlCount;
            }
            double d = i;
            double discount = VmdHelper.get().getDiscount(totalBqlCount);
            Double.isNaN(d);
            int i2 = ((int) (d * discount)) - this.yhje;
            if (i2 > 0) {
                return i2;
            }
            return 0;
        }

        public int getOrderPrice_noYhje() {
            int i = 0;
            for (String str : this.bqlItems.keySet()) {
                BqlItem bqlItem = VmdHelper.get().mapBqlDatas.get(str);
                i += bqlItem.getPrice() * this.bqlItems.get(str).intValue();
            }
            int totalBqlCount = getTotalBqlCount();
            Iterator<String> it = this.tops.keySet().iterator();
            while (it.hasNext()) {
                i += VmdHelper.get().mapTopDatas.get(it.next()).getPrice() * totalBqlCount;
            }
            return i;
        }

        public int getTopCount(String str) {
            if (this.tops.containsKey(str)) {
                return this.tops.get(str).intValue();
            }
            return 0;
        }

        public void setTopCount(String str, int i) {
            this.tops.put(str, Integer.valueOf(i));
        }

        public HashMap<String, Integer> getTops() {
            return this.tops;
        }

        public HashMap<String, Integer> getBqlItems() {
            return this.bqlItems;
        }

        public BqlOrder() {
            this.orderLid = "0L";
            this.orderLid = (Long.parseLong(Shj.getMachineId()) / 10000) + DateUtil.format(new Date(), "mmddHHmmss");
        }

        public String getOrderLid() {
            return this.orderLid;
        }

        public String getName() {
            return this.name;
        }

        public String getType() {
            return this.type;
        }

        public void setName(String str) {
            this.name = str;
        }

        public void setType(String str) {
            this.type = str;
        }

        public void setSn(String str) {
            this.sn = str;
        }

        public String getSn() {
            return this.sn;
        }
    }

    public boolean checkBqlTopEnnough(HashMap<String, Integer> hashMap) {
        HashMap hashMap2 = new HashMap();
        for (String str : hashMap.keySet()) {
            Loger.writeLog("SHJ", "bql:" + str + " count:" + hashMap.get(str));
            HashMap<String, Integer> tops = getBqlItem(str).getTops();
            for (String str2 : tops.keySet()) {
                if (!hashMap2.containsKey(str2)) {
                    hashMap2.put(str2, 0);
                }
                hashMap2.put(str2, Integer.valueOf(((Integer) hashMap2.get(str2)).intValue() + (tops.get(str2).intValue() * hashMap.get(str).intValue())));
            }
        }
        for (String str3 : hashMap2.keySet()) {
            int goodsCount = ShjManager.getGoodsManager().getGoodsCount(getTopItem(str3).getCode());
            Loger.writeLog("SHJ", "top:" + str3 + " count:" + hashMap2.get(str3) + " 库存:" + goodsCount);
            if (goodsCount < ((Integer) hashMap2.get(str3)).intValue()) {
                return false;
            }
        }
        return true;
    }

    public void initDatas() {
        Shj.setNeedCheckOfferStatus(false, 60000);
        TopItem topItem = new TopItem();
        topItem.title = "TOP1";
        topItem.setCode("1000");
        topItem.name = "一份草莓果酱";
        topItem.price = 1;
        topItem.imageResurceId = R.drawable.img_strawberry;
        this.mapTopDatas.put(topItem.name, topItem);
        this.topDatas.add(topItem);
        TopItem topItem2 = new TopItem();
        topItem2.title = "TOP2";
        topItem2.setCode("1001");
        topItem2.name = "一份芒果果酱";
        topItem2.price = 1;
        topItem2.imageResurceId = R.drawable.img_mogo;
        this.mapTopDatas.put(topItem2.name, topItem2);
        this.topDatas.add(topItem2);
        TopItem topItem3 = new TopItem();
        topItem3.title = "TOP3";
        topItem3.setCode("1002");
        topItem3.name = "一份草菠萝酱";
        topItem3.price = 1;
        topItem3.imageResurceId = R.drawable.img_pineapple;
        this.mapTopDatas.put(topItem3.name, topItem3);
        this.topDatas.add(topItem3);
        TopItem topItem4 = new TopItem();
        topItem4.title = "TOP4";
        topItem4.setCode("1003");
        topItem4.name = "一份碎花生";
        topItem4.price = 1;
        topItem4.imageResurceId = R.drawable.img_shs;
        this.mapTopDatas.put(topItem4.name, topItem4);
        this.topDatas.add(topItem4);
        TopItem topItem5 = new TopItem();
        topItem5.title = "TOP5";
        topItem5.setCode("" + PointerIconCompat.TYPE_WAIT);
        topItem5.name = "一份奥利奥";
        topItem5.price = 1;
        topItem5.imageResurceId = R.drawable.img_ala;
        this.mapTopDatas.put(topItem5.name, topItem5);
        this.topDatas.add(topItem5);
        TopItem topItem6 = new TopItem();
        topItem6.title = "TOP6";
        topItem6.setCode("" + WebSocketProtocol.CLOSE_NO_STATUS_CODE);
        topItem6.name = "一份燕麦";
        topItem6.price = 1;
        topItem6.imageResurceId = R.drawable.img_ym;
        this.mapTopDatas.put(topItem6.name, topItem6);
        this.topDatas.add(topItem6);
        BqlItem bqlItem = new BqlItem();
        bqlItem.name = "原味冰淇淋";
        bqlItem.setCode("8000");
        bqlItem.price = 1;
        bqlItem.imageResurceId = R.drawable.img_bql_yw;
        this.bqlDatas.add(bqlItem);
        this.mapBqlDatas.put(bqlItem.name, bqlItem);
        this.freeCollocationBql = bqlItem;
        BqlItem bqlItem2 = new BqlItem();
        bqlItem2.name = "草莓味冰淇淋";
        bqlItem2.setCode("8001");
        bqlItem2.price = 1;
        bqlItem2.imageResurceId = R.drawable.img_bql_yw;
        bqlItem2.getTops().put("一份草莓果酱", 1);
        this.mapBqlDatas.put(bqlItem2.name, bqlItem2);
        this.bqlDatas.add(bqlItem2);
        BqlItem bqlItem3 = new BqlItem();
        bqlItem3.name = "奥利奥冰淇淋";
        bqlItem3.setCode("8002");
        bqlItem3.price = 1;
        bqlItem3.imageResurceId = R.drawable.img_bql_yw;
        bqlItem3.getTops().put("一份奥利奥", 1);
        this.mapBqlDatas.put(bqlItem3.name, bqlItem3);
        this.bqlDatas.add(bqlItem3);
        BqlItem bqlItem4 = new BqlItem();
        bqlItem4.name = "抹茶味冰淇淋";
        bqlItem4.setCode("8003");
        bqlItem4.price = 1;
        bqlItem4.imageResurceId = R.drawable.img_bql_yw;
        this.mapBqlDatas.put(bqlItem4.name, bqlItem4);
        this.bqlDatas.add(bqlItem4);
        BqlItem bqlItem5 = new BqlItem();
        bqlItem5.name = "芒果味冰淇淋";
        bqlItem5.setCode("8004");
        bqlItem5.price = 1;
        bqlItem5.imageResurceId = R.drawable.img_bql_yw;
        this.mapBqlDatas.put(bqlItem5.name, bqlItem5);
        bqlItem5.getTops().put("一份芒果果酱", 1);
        this.bqlDatas.add(bqlItem5);
        BqlItem bqlItem6 = new BqlItem();
        bqlItem6.name = "蓝莓味冰淇淋";
        bqlItem6.setCode("8005");
        bqlItem6.price = 1;
        bqlItem6.imageResurceId = R.drawable.img_bql_yw;
        this.mapBqlDatas.put(bqlItem6.name, bqlItem6);
        this.bqlDatas.add(bqlItem6);
        BqlItem bqlItem7 = new BqlItem();
        bqlItem7.name = "巧克力冰淇淋";
        bqlItem7.setCode("8006");
        bqlItem7.price = 1;
        bqlItem7.imageResurceId = R.drawable.img_bql_yw;
        this.mapBqlDatas.put(bqlItem7.name, bqlItem7);
        this.bqlDatas.add(bqlItem7);
        BqlItem bqlItem8 = new BqlItem();
        bqlItem8.name = "豪华冰淇淋";
        bqlItem8.setCode("8007");
        bqlItem8.price = 1;
        bqlItem8.imageResurceId = R.drawable.img_bql_yw;
        this.mapBqlDatas.put(bqlItem8.name, bqlItem8);
        this.bqlDatas.add(bqlItem8);
        BqlItem bqlItem9 = new BqlItem();
        bqlItem9.name = "葡萄味冰淇淋";
        bqlItem9.setCode("8008");
        bqlItem9.price = 1;
        bqlItem9.imageResurceId = R.drawable.img_bql_yw;
        this.mapBqlDatas.put(bqlItem9.name, bqlItem9);
        this.bqlDatas.add(bqlItem9);
        Shj.clearDebugShelfInfos(false, false, false, false, true);
        Iterator<TopItem> it = this.topDatas.iterator();
        int i = 1;
        while (it.hasNext()) {
            TopItem next = it.next();
            this.shelfInfos.add(createShelfInfo(0 + i, next.getCode(), next.getName(), next.getPrice()));
            i++;
        }
        Iterator<BqlItem> it2 = this.bqlDatas.iterator();
        int i2 = 1;
        while (it2.hasNext()) {
            BqlItem next2 = it2.next();
            this.shelfInfos.add(createShelfInfo(100 + i2, next2.getCode(), next2.getName(), next2.getPrice()));
            i2++;
        }
        ArrayList<ShelfInfo> arrayList = this.shelfInfos;
        String str = this.iceCream;
        arrayList.add(createShelfInfo(201, str, str, 1));
        ArrayList<ShelfInfo> arrayList2 = this.shelfInfos;
        String str2 = this.zhibei;
        arrayList2.add(createShelfInfo(202, str2, str2, 1));
        Shj.createDebugShelfInfos(this.shelfInfos);
    }

    ShelfInfo createShelfInfo(int i, String str, String str2, int i2) {
        ShelfInfo shelfInfo = new ShelfInfo();
        shelfInfo.setShelf(Integer.valueOf(i));
        shelfInfo.setPrice(Integer.valueOf(i2));
        shelfInfo.setGoodsName(str2);
        shelfInfo.setGoodsCode(str);
        shelfInfo.setCapacity(100);
        shelfInfo.setGoodsCount(100);
        return shelfInfo;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(19:1|(18:5|6|(2:10|11)|14|15|16|(2:19|17)|20|21|(1:23)|24|25|(8:28|29|(1:54)|33|(3:35|(2:37|38)(3:40|41|(3:43|44|(2:46|47)(1:48))(1:51))|39)|52|53|26)|57|58|59|60|61)|68|(3:8|10|11)|14|15|16|(1:17)|20|21|(0)|24|25|(1:26)|57|58|59|60|61) */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0225, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0226, code lost:
    
        com.oysb.utils.Loger.writeException("SHJ", r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:19:0x004f A[Catch: Exception -> 0x0225, LOOP:0: B:17:0x004a->B:19:0x004f, LOOP_END, TryCatch #3 {Exception -> 0x0225, blocks: (B:16:0x003f, B:19:0x004f, B:21:0x00aa, B:23:0x00b0, B:24:0x00ba, B:59:0x01e8), top: B:15:0x003f }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00b0 A[Catch: Exception -> 0x0225, TryCatch #3 {Exception -> 0x0225, blocks: (B:16:0x003f, B:19:0x004f, B:21:0x00aa, B:23:0x00b0, B:24:0x00ba, B:59:0x01e8), top: B:15:0x003f }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00c5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void updateShelfInfos() {
        /*
            Method dump skipped, instructions count: 599
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.app.VmdHelper.updateShelfInfos():void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.app.VmdHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements Comparator<TopItem> {
        AnonymousClass2() {
        }

        @Override // java.util.Comparator
        public int compare(TopItem topItem, TopItem topItem2) {
            return topItem.getShelf() < topItem2.getShelf() ? -1 : 1;
        }
    }

    public BqlItem getFreeCollocationBql() {
        return this.freeCollocationBql;
    }

    public BqlOrder createOrder(String str) {
        if (this.disCounts == null) {
            JSONObject jSONObject = new JSONObject();
            this.disCounts = jSONObject;
            jSONObject.put("d1", (Object) Double.valueOf(1.0d));
            this.disCounts.put("d2", (Object) Double.valueOf(1.0d));
            this.disCounts.put("d3", (Object) Double.valueOf(1.0d));
        }
        BqlOrder bqlOrder = new BqlOrder();
        this.bqlOrder = bqlOrder;
        bqlOrder.setType(str);
        this.orderList.add(0, this.bqlOrder);
        if (this.orderList.size() > 10) {
            for (int size = this.orderList.size() - 1; size >= 10; size--) {
                this.orderList.remove(size);
            }
        }
        return this.bqlOrder;
    }

    public BqlOrder getBqlOrder() {
        return this.bqlOrder;
    }

    public BqlOrder getBqlOrder(String str) {
        for (BqlOrder bqlOrder : this.orderList) {
            if (bqlOrder.getSn().equals(str)) {
                return bqlOrder;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.app.VmdHelper$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends TimerTask {
        final /* synthetic */ VmdConnectLisener val$l;

        AnonymousClass3(VmdConnectLisener vmdConnectLisener) {
            vmdConnectLisener = vmdConnectLisener;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (VmdHelper.this.isVmdConnected && VmdHelper.this.isSTK920Connected) {
                if (VmdHelper.this.timer != null) {
                    VmdHelper.this.timer.cancel();
                    VmdHelper.this.timer.purge();
                    VmdHelper.this.timer = null;
                    return;
                }
                return;
            }
            if (!VmdHelper.this.isVmdConnected) {
                Loger.writeLog("SHJ", "---------------------------------------------------");
                Loger.writeLog("SHJ", "openPort /dev/ttyS1 9600");
                SerialPortUtil.get().openPort("/dev/ttyS1", 9600, new SerialPortUtil.OpenCallback() { // from class: com.xyshj.machine.app.VmdHelper.3.1
                    AnonymousClass1() {
                    }

                    @Override // com.hanlu.toolsdk.SerialPortUtil.OpenCallback
                    public void onSuccess() {
                        Loger.writeLog("SHJ", "/dev/ttyS1  9600 连接成功");
                        VmdHelper.this.isVmdConnected = true;
                        try {
                            String asString = CacheHelper.getFileCache().getAsString("machineId");
                            if (asString == null) {
                                CacheHelper.getFileCache().put("machineId", "0000000000");
                                asString = "0000000000";
                            }
                            if (asString.equalsIgnoreCase("0000000000")) {
                                vmdConnectLisener.onConnectResult(VmdHelper.this.isVmdConnected, "/dev/ttyS1  9600 连接成功 Debug模式");
                            } else {
                                vmdConnectLisener.onConnectResult(VmdHelper.this.isVmdConnected, "/dev/ttyS1  9600 连接成功");
                            }
                        } catch (Exception unused) {
                        }
                        new Thread(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.3.1.1
                            RunnableC00811() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                BqlManager.get().reset((short) 1);
                                Loger.writeLog("SHJ", "复位完成");
                            }
                        }).start();
                    }

                    /* renamed from: com.xyshj.machine.app.VmdHelper$3$1$1 */
                    /* loaded from: classes2.dex */
                    class RunnableC00811 implements Runnable {
                        RunnableC00811() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            BqlManager.get().reset((short) 1);
                            Loger.writeLog("SHJ", "复位完成");
                        }
                    }

                    @Override // com.hanlu.toolsdk.SerialPortUtil.OpenCallback
                    public void onFailure(Throwable th) {
                        Loger.writeLog("SHJ", "/dev/ttyS1  9600 连接失败");
                        Loger.writeException("SHJ", th);
                        VmdHelper.this.isVmdConnected = BqlManager.get().isDebug();
                        if (CacheHelper.getFileCache().getAsString("machineId") == null) {
                            CacheHelper.getFileCache().put("machineId", "0000000000");
                        }
                        VmdConnectLisener vmdConnectLisener = vmdConnectLisener;
                        boolean z = VmdHelper.this.isVmdConnected;
                        StringBuilder sb = new StringBuilder();
                        sb.append("/dev/ttyS1  9600 ");
                        sb.append(BqlManager.get().isDebug() ? "Debug模式" : "连接失败");
                        vmdConnectLisener.onConnectResult(z, sb.toString());
                    }
                });
            }
            if (VmdHelper.this.isSTK920Connected) {
                return;
            }
            if (BqlManager.get().isDebug()) {
                VmdHelper.this.isSTK920Connected = true;
                return;
            }
            VmdHelper.this.bql_connect();
            VmdHelper.this.isSTK920Connected = true;
            Loger.writeLog("SHJ", "serialPortSTK920 连接成功");
            VmdHelper.this.bql_checkStatus(false);
            VmdHelper.this.tryStopBqlPowAfterDongan();
            VmdHelper.this.sleep(3000);
            if (VmdHelper.this._isdonggan || VmdHelper.this._isbianpingError || VmdHelper.this._isqianyaError || VmdHelper.this._isgaoyaError) {
                return;
            }
            VmdHelper.this.doBqlZhigao();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.xyshj.machine.app.VmdHelper$3$1 */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements SerialPortUtil.OpenCallback {
            AnonymousClass1() {
            }

            @Override // com.hanlu.toolsdk.SerialPortUtil.OpenCallback
            public void onSuccess() {
                Loger.writeLog("SHJ", "/dev/ttyS1  9600 连接成功");
                VmdHelper.this.isVmdConnected = true;
                try {
                    String asString = CacheHelper.getFileCache().getAsString("machineId");
                    if (asString == null) {
                        CacheHelper.getFileCache().put("machineId", "0000000000");
                        asString = "0000000000";
                    }
                    if (asString.equalsIgnoreCase("0000000000")) {
                        vmdConnectLisener.onConnectResult(VmdHelper.this.isVmdConnected, "/dev/ttyS1  9600 连接成功 Debug模式");
                    } else {
                        vmdConnectLisener.onConnectResult(VmdHelper.this.isVmdConnected, "/dev/ttyS1  9600 连接成功");
                    }
                } catch (Exception unused) {
                }
                new Thread(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.3.1.1
                    RunnableC00811() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        BqlManager.get().reset((short) 1);
                        Loger.writeLog("SHJ", "复位完成");
                    }
                }).start();
            }

            /* renamed from: com.xyshj.machine.app.VmdHelper$3$1$1 */
            /* loaded from: classes2.dex */
            class RunnableC00811 implements Runnable {
                RunnableC00811() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    BqlManager.get().reset((short) 1);
                    Loger.writeLog("SHJ", "复位完成");
                }
            }

            @Override // com.hanlu.toolsdk.SerialPortUtil.OpenCallback
            public void onFailure(Throwable th) {
                Loger.writeLog("SHJ", "/dev/ttyS1  9600 连接失败");
                Loger.writeException("SHJ", th);
                VmdHelper.this.isVmdConnected = BqlManager.get().isDebug();
                if (CacheHelper.getFileCache().getAsString("machineId") == null) {
                    CacheHelper.getFileCache().put("machineId", "0000000000");
                }
                VmdConnectLisener vmdConnectLisener = vmdConnectLisener;
                boolean z = VmdHelper.this.isVmdConnected;
                StringBuilder sb = new StringBuilder();
                sb.append("/dev/ttyS1  9600 ");
                sb.append(BqlManager.get().isDebug() ? "Debug模式" : "连接失败");
                vmdConnectLisener.onConnectResult(z, sb.toString());
            }
        }
    }

    public void connectVmd(VmdConnectLisener vmdConnectLisener) {
        this.timer.schedule(new TimerTask() { // from class: com.xyshj.machine.app.VmdHelper.3
            final /* synthetic */ VmdConnectLisener val$l;

            AnonymousClass3(VmdConnectLisener vmdConnectLisener2) {
                vmdConnectLisener = vmdConnectLisener2;
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (VmdHelper.this.isVmdConnected && VmdHelper.this.isSTK920Connected) {
                    if (VmdHelper.this.timer != null) {
                        VmdHelper.this.timer.cancel();
                        VmdHelper.this.timer.purge();
                        VmdHelper.this.timer = null;
                        return;
                    }
                    return;
                }
                if (!VmdHelper.this.isVmdConnected) {
                    Loger.writeLog("SHJ", "---------------------------------------------------");
                    Loger.writeLog("SHJ", "openPort /dev/ttyS1 9600");
                    SerialPortUtil.get().openPort("/dev/ttyS1", 9600, new SerialPortUtil.OpenCallback() { // from class: com.xyshj.machine.app.VmdHelper.3.1
                        AnonymousClass1() {
                        }

                        @Override // com.hanlu.toolsdk.SerialPortUtil.OpenCallback
                        public void onSuccess() {
                            Loger.writeLog("SHJ", "/dev/ttyS1  9600 连接成功");
                            VmdHelper.this.isVmdConnected = true;
                            try {
                                String asString = CacheHelper.getFileCache().getAsString("machineId");
                                if (asString == null) {
                                    CacheHelper.getFileCache().put("machineId", "0000000000");
                                    asString = "0000000000";
                                }
                                if (asString.equalsIgnoreCase("0000000000")) {
                                    vmdConnectLisener.onConnectResult(VmdHelper.this.isVmdConnected, "/dev/ttyS1  9600 连接成功 Debug模式");
                                } else {
                                    vmdConnectLisener.onConnectResult(VmdHelper.this.isVmdConnected, "/dev/ttyS1  9600 连接成功");
                                }
                            } catch (Exception unused) {
                            }
                            new Thread(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.3.1.1
                                RunnableC00811() {
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    BqlManager.get().reset((short) 1);
                                    Loger.writeLog("SHJ", "复位完成");
                                }
                            }).start();
                        }

                        /* renamed from: com.xyshj.machine.app.VmdHelper$3$1$1 */
                        /* loaded from: classes2.dex */
                        class RunnableC00811 implements Runnable {
                            RunnableC00811() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                BqlManager.get().reset((short) 1);
                                Loger.writeLog("SHJ", "复位完成");
                            }
                        }

                        @Override // com.hanlu.toolsdk.SerialPortUtil.OpenCallback
                        public void onFailure(Throwable th) {
                            Loger.writeLog("SHJ", "/dev/ttyS1  9600 连接失败");
                            Loger.writeException("SHJ", th);
                            VmdHelper.this.isVmdConnected = BqlManager.get().isDebug();
                            if (CacheHelper.getFileCache().getAsString("machineId") == null) {
                                CacheHelper.getFileCache().put("machineId", "0000000000");
                            }
                            VmdConnectLisener vmdConnectLisener2 = vmdConnectLisener;
                            boolean z = VmdHelper.this.isVmdConnected;
                            StringBuilder sb = new StringBuilder();
                            sb.append("/dev/ttyS1  9600 ");
                            sb.append(BqlManager.get().isDebug() ? "Debug模式" : "连接失败");
                            vmdConnectLisener2.onConnectResult(z, sb.toString());
                        }
                    });
                }
                if (VmdHelper.this.isSTK920Connected) {
                    return;
                }
                if (BqlManager.get().isDebug()) {
                    VmdHelper.this.isSTK920Connected = true;
                    return;
                }
                VmdHelper.this.bql_connect();
                VmdHelper.this.isSTK920Connected = true;
                Loger.writeLog("SHJ", "serialPortSTK920 连接成功");
                VmdHelper.this.bql_checkStatus(false);
                VmdHelper.this.tryStopBqlPowAfterDongan();
                VmdHelper.this.sleep(3000);
                if (VmdHelper.this._isdonggan || VmdHelper.this._isbianpingError || VmdHelper.this._isqianyaError || VmdHelper.this._isgaoyaError) {
                    return;
                }
                VmdHelper.this.doBqlZhigao();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: com.xyshj.machine.app.VmdHelper$3$1 */
            /* loaded from: classes2.dex */
            public class AnonymousClass1 implements SerialPortUtil.OpenCallback {
                AnonymousClass1() {
                }

                @Override // com.hanlu.toolsdk.SerialPortUtil.OpenCallback
                public void onSuccess() {
                    Loger.writeLog("SHJ", "/dev/ttyS1  9600 连接成功");
                    VmdHelper.this.isVmdConnected = true;
                    try {
                        String asString = CacheHelper.getFileCache().getAsString("machineId");
                        if (asString == null) {
                            CacheHelper.getFileCache().put("machineId", "0000000000");
                            asString = "0000000000";
                        }
                        if (asString.equalsIgnoreCase("0000000000")) {
                            vmdConnectLisener.onConnectResult(VmdHelper.this.isVmdConnected, "/dev/ttyS1  9600 连接成功 Debug模式");
                        } else {
                            vmdConnectLisener.onConnectResult(VmdHelper.this.isVmdConnected, "/dev/ttyS1  9600 连接成功");
                        }
                    } catch (Exception unused) {
                    }
                    new Thread(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.3.1.1
                        RunnableC00811() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            BqlManager.get().reset((short) 1);
                            Loger.writeLog("SHJ", "复位完成");
                        }
                    }).start();
                }

                /* renamed from: com.xyshj.machine.app.VmdHelper$3$1$1 */
                /* loaded from: classes2.dex */
                class RunnableC00811 implements Runnable {
                    RunnableC00811() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        BqlManager.get().reset((short) 1);
                        Loger.writeLog("SHJ", "复位完成");
                    }
                }

                @Override // com.hanlu.toolsdk.SerialPortUtil.OpenCallback
                public void onFailure(Throwable th) {
                    Loger.writeLog("SHJ", "/dev/ttyS1  9600 连接失败");
                    Loger.writeException("SHJ", th);
                    VmdHelper.this.isVmdConnected = BqlManager.get().isDebug();
                    if (CacheHelper.getFileCache().getAsString("machineId") == null) {
                        CacheHelper.getFileCache().put("machineId", "0000000000");
                    }
                    VmdConnectLisener vmdConnectLisener2 = vmdConnectLisener;
                    boolean z = VmdHelper.this.isVmdConnected;
                    StringBuilder sb = new StringBuilder();
                    sb.append("/dev/ttyS1  9600 ");
                    sb.append(BqlManager.get().isDebug() ? "Debug模式" : "连接失败");
                    vmdConnectLisener2.onConnectResult(z, sb.toString());
                }
            }
        }, 1000L, 60000L);
    }

    public boolean isVmdConnected() {
        return this.isVmdConnected && this.isSTK920Connected;
    }

    public List<Integer> fruit2deviceshelf(String str, int i) {
        ArrayList arrayList = new ArrayList();
        Iterator<Integer> it = Shj.getMachine(0, false).getShelves().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Integer next = it.next();
            ShelfInfo shelfInfo = Shj.getShelfInfo(next);
            if (shelfInfo.getGoodsCode().equalsIgnoreCase(str) || shelfInfo.getGoodsName().toLowerCase(Locale.ROOT).contains(str.toLowerCase(Locale.ROOT))) {
                if (shelfInfo.getGoodsCount().intValue() >= i) {
                    arrayList.add(next);
                    break;
                }
            }
        }
        return arrayList;
    }

    public JSONObject bqlItem2Peifang(BqlOrder bqlOrder, String str) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("ok", (Object) false);
        JSONArray jSONArray = new JSONArray();
        jSONObject.put(SpeechEvent.KEY_EVENT_RECORD_DATA, (Object) jSONArray);
        BqlItem bqlItem = get().getBqlItem(str);
        Loger.writeLog("SHJ", " bqlItem name:" + bqlItem.getName() + " tops count:" + bqlItem.getTops().size());
        HashMap hashMap = new HashMap();
        hashMap.putAll(bqlItem.tops);
        hashMap.putAll(bqlOrder.getTops());
        boolean z = true;
        for (String str2 : hashMap.keySet()) {
            Loger.writeLog("SHJ", " topName " + str2 + " count:" + hashMap.get(str2));
            if (((Integer) hashMap.get(str2)).intValue() != 0) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("name", (Object) str2);
                jSONObject2.put("weight", hashMap.get(str2));
                jSONArray.add(jSONObject2);
                List<Integer> fruit2deviceshelf = fruit2deviceshelf(str2, ((Integer) hashMap.get(str2)).intValue());
                jSONObject2.put("deviceshelf", (Object) fruit2deviceshelf);
                if (z) {
                    z = fruit2deviceshelf.size() > 0;
                }
                Loger.writeLog("SHJ", " topName " + str2 + " isOK:" + z);
                if (!z) {
                    break;
                }
            }
        }
        jSONObject.put("ok", Boolean.valueOf(z));
        return jSONObject;
    }

    public void offerBql(int i) {
        if (i < 100) {
            Shj.onOfferGoods(i, OfferState.OfferSuccess, 0);
            return;
        }
        ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
        offerGoods(bqlItem2Peifang(get().getBqlOrder(ShjManager.getOrderManager().getResentOrder(2, null).getArgs().getArg("bqlOrderSn")), shelfInfo.getGoodsName()), i);
    }

    private void offerGoods(JSONObject jSONObject, int i) {
        if (this.isOffering) {
            return;
        }
        this.isOffering = true;
        ShjManager.getOrderManager().addTradSn();
        new Thread(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.4
            final /* synthetic */ int val$bqlShelf;
            final /* synthetic */ JSONObject val$peifang;

            AnonymousClass4(int i2, JSONObject jSONObject2) {
                i = i2;
                jSONObject = jSONObject2;
            }

            /* JADX WARN: Code restructure failed: missing block: B:74:0x022c, code lost:
            
                com.xyshj.machine.app.VmdHelper.this.isOffering = false;
             */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Removed duplicated region for block: B:138:0x0404 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Type inference failed for: r2v1 */
            /* JADX WARN: Type inference failed for: r2v18 */
            /* JADX WARN: Type inference failed for: r2v4, types: [int] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void run() {
                /*
                    Method dump skipped, instructions count: 1090
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.app.VmdHelper.AnonymousClass4.run():void");
            }
        }).start();
    }

    /* renamed from: com.xyshj.machine.app.VmdHelper$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements Runnable {
        final /* synthetic */ int val$bqlShelf;
        final /* synthetic */ JSONObject val$peifang;

        AnonymousClass4(int i2, JSONObject jSONObject2) {
            i = i2;
            jSONObject = jSONObject2;
        }

        @Override // java.lang.Runnable
        public void run() {
            /*  JADX ERROR: Method code generation error
                java.lang.NullPointerException
                */
            /*
                Method dump skipped, instructions count: 1090
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.app.VmdHelper.AnonymousClass4.run():void");
        }
    }

    void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:119:0x03a8 A[Catch: Exception -> 0x04b8, all -> 0x04dc, TryCatch #0 {Exception -> 0x04b8, blocks: (B:16:0x0024, B:17:0x0080, B:19:0x008d, B:182:0x0099, B:22:0x009d, B:23:0x00b0, B:42:0x0100, B:47:0x0109, B:49:0x0147, B:52:0x017d, B:55:0x0193, B:59:0x01b5, B:62:0x01d7, B:63:0x01f6, B:65:0x0212, B:66:0x0218, B:68:0x022e, B:70:0x0234, B:74:0x025b, B:76:0x0261, B:77:0x0296, B:78:0x049b, B:80:0x04a1, B:82:0x04a7, B:84:0x04ad, B:89:0x04b3, B:97:0x02a1, B:99:0x02a8, B:102:0x02f9, B:105:0x030d, B:107:0x0370, B:109:0x038c, B:111:0x0390, B:113:0x0394, B:117:0x039c, B:119:0x03a8, B:121:0x03b1, B:122:0x03b8, B:124:0x03bc, B:125:0x03c3, B:127:0x03c7, B:128:0x03ce, B:130:0x03d2, B:131:0x03d9, B:133:0x03dd, B:135:0x03e1, B:137:0x03e5, B:142:0x03ef, B:145:0x03f2, B:147:0x0408, B:149:0x040e, B:151:0x0471, B:154:0x0435, B:156:0x043b, B:161:0x032f, B:164:0x034f, B:172:0x047b), top: B:15:0x0024, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x03ef A[Catch: Exception -> 0x04b8, all -> 0x04dc, TryCatch #0 {Exception -> 0x04b8, blocks: (B:16:0x0024, B:17:0x0080, B:19:0x008d, B:182:0x0099, B:22:0x009d, B:23:0x00b0, B:42:0x0100, B:47:0x0109, B:49:0x0147, B:52:0x017d, B:55:0x0193, B:59:0x01b5, B:62:0x01d7, B:63:0x01f6, B:65:0x0212, B:66:0x0218, B:68:0x022e, B:70:0x0234, B:74:0x025b, B:76:0x0261, B:77:0x0296, B:78:0x049b, B:80:0x04a1, B:82:0x04a7, B:84:0x04ad, B:89:0x04b3, B:97:0x02a1, B:99:0x02a8, B:102:0x02f9, B:105:0x030d, B:107:0x0370, B:109:0x038c, B:111:0x0390, B:113:0x0394, B:117:0x039c, B:119:0x03a8, B:121:0x03b1, B:122:0x03b8, B:124:0x03bc, B:125:0x03c3, B:127:0x03c7, B:128:0x03ce, B:130:0x03d2, B:131:0x03d9, B:133:0x03dd, B:135:0x03e1, B:137:0x03e5, B:142:0x03ef, B:145:0x03f2, B:147:0x0408, B:149:0x040e, B:151:0x0471, B:154:0x0435, B:156:0x043b, B:161:0x032f, B:164:0x034f, B:172:0x047b), top: B:15:0x0024, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0408 A[Catch: Exception -> 0x04b8, all -> 0x04dc, TryCatch #0 {Exception -> 0x04b8, blocks: (B:16:0x0024, B:17:0x0080, B:19:0x008d, B:182:0x0099, B:22:0x009d, B:23:0x00b0, B:42:0x0100, B:47:0x0109, B:49:0x0147, B:52:0x017d, B:55:0x0193, B:59:0x01b5, B:62:0x01d7, B:63:0x01f6, B:65:0x0212, B:66:0x0218, B:68:0x022e, B:70:0x0234, B:74:0x025b, B:76:0x0261, B:77:0x0296, B:78:0x049b, B:80:0x04a1, B:82:0x04a7, B:84:0x04ad, B:89:0x04b3, B:97:0x02a1, B:99:0x02a8, B:102:0x02f9, B:105:0x030d, B:107:0x0370, B:109:0x038c, B:111:0x0390, B:113:0x0394, B:117:0x039c, B:119:0x03a8, B:121:0x03b1, B:122:0x03b8, B:124:0x03bc, B:125:0x03c3, B:127:0x03c7, B:128:0x03ce, B:130:0x03d2, B:131:0x03d9, B:133:0x03dd, B:135:0x03e1, B:137:0x03e5, B:142:0x03ef, B:145:0x03f2, B:147:0x0408, B:149:0x040e, B:151:0x0471, B:154:0x0435, B:156:0x043b, B:161:0x032f, B:164:0x034f, B:172:0x047b), top: B:15:0x0024, outer: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    synchronized boolean bql_checkStatus(boolean r18) {
        /*
            Method dump skipped, instructions count: 1249
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.app.VmdHelper.bql_checkStatus(boolean):boolean");
    }

    boolean bql_wait_statusOk(boolean z) {
        boolean z2;
        Loger.writeLog("SHJ", "查询BQL状态是否OK....");
        long currentTimeMillis = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - currentTimeMillis >= 300000) {
                z2 = false;
                break;
            }
            if (bql_checkStatus(z)) {
                z2 = true;
                break;
            }
            sleep(1500);
        }
        if (!z2) {
            Loger.writeLog("SHJ", "bql_wait_statusOk 超时");
        }
        return z2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x00b7, code lost:
    
        com.shj.Shj.onDeviceMessage("SHJ", "发送指令成功");
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00bc, code lost:
    
        r6 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    boolean bql_chugao() {
        /*
            r12 = this;
            java.lang.String r0 = "SHJ"
            com.hanlu.toolsdk.BqlManager r1 = com.hanlu.toolsdk.BqlManager.get()
            boolean r1 = r1.isDebug()
            r2 = 1
            if (r1 == 0) goto Le
            return r2
        Le:
            r1 = 0
            r12.bql_connect()     // Catch: java.lang.Exception -> Lc6
            java.lang.String r3 = "发送指令，出糕"
            com.shj.Shj.onDeviceMessage(r0, r3)     // Catch: java.lang.Exception -> Lc6
            r3 = 6
            byte[] r3 = new byte[r3]     // Catch: java.lang.Exception -> Lc6
            r4 = 67
            r3[r1] = r4     // Catch: java.lang.Exception -> Lc6
            r4 = 104(0x68, float:1.46E-43)
            r3[r2] = r4     // Catch: java.lang.Exception -> Lc6
            r4 = 2
            r5 = 103(0x67, float:1.44E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc6
            r4 = 3
            r5 = 76
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc6
            r4 = 4
            r5 = 43
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc6
            r4 = 5
            int r5 = r12.getChugao_time()     // Catch: java.lang.Exception -> Lc6
            byte r5 = (byte) r5     // Catch: java.lang.Exception -> Lc6
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc6
            android.serialport.SerialPort r4 = r12.serialPortSTK920     // Catch: java.lang.Exception -> Lc6
            java.io.OutputStream r4 = r4.getOutputStream()     // Catch: java.lang.Exception -> Lc6
            r4.write(r3)     // Catch: java.lang.Exception -> Lc6
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lc6
            r4.<init>()     // Catch: java.lang.Exception -> Lc6
            java.lang.String r5 = ">>"
            r4.append(r5)     // Catch: java.lang.Exception -> Lc6
            java.lang.String r3 = com.oysb.utils.ObjectHelper.hex2String(r3)     // Catch: java.lang.Exception -> Lc6
            r4.append(r3)     // Catch: java.lang.Exception -> Lc6
            java.lang.String r3 = r4.toString()     // Catch: java.lang.Exception -> Lc6
            com.oysb.utils.Loger.writeLog(r0, r3)     // Catch: java.lang.Exception -> Lc6
            long r3 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Lc6
            r5 = 100
            r12.sleep(r5)     // Catch: java.lang.Exception -> Lc6
            r6 = 0
        L64:
            long r7 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Lc4
            long r7 = r7 - r3
            r9 = 3000(0xbb8, double:1.482E-320)
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 >= 0) goto Lcb
            android.serialport.SerialPort r7 = r12.serialPortSTK920     // Catch: java.lang.Exception -> Lc4
            java.io.InputStream r7 = r7.getInputStream()     // Catch: java.lang.Exception -> Lc4
            int r7 = r7.available()     // Catch: java.lang.Exception -> Lc4
            if (r7 != 0) goto L7f
            r12.sleep(r5)     // Catch: java.lang.Exception -> Lc4
            goto L64
        L7f:
            byte[] r8 = new byte[r7]     // Catch: java.lang.Exception -> Lc4
            android.serialport.SerialPort r9 = r12.serialPortSTK920     // Catch: java.lang.Exception -> Lc4
            java.io.InputStream r9 = r9.getInputStream()     // Catch: java.lang.Exception -> Lc4
            r9.read(r8)     // Catch: java.lang.Exception -> Lc4
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lc4
            r9.<init>()     // Catch: java.lang.Exception -> Lc4
            java.lang.String r10 = "<<"
            r9.append(r10)     // Catch: java.lang.Exception -> Lc4
            java.lang.String r10 = com.oysb.utils.ObjectHelper.hex2String(r8)     // Catch: java.lang.Exception -> Lc4
            r9.append(r10)     // Catch: java.lang.Exception -> Lc4
            java.lang.String r9 = r9.toString()     // Catch: java.lang.Exception -> Lc4
            com.oysb.utils.Loger.writeLog(r0, r9)     // Catch: java.lang.Exception -> Lc4
            r9 = 0
        La3:
            if (r9 >= r7) goto Lc1
            r10 = r8[r9]     // Catch: java.lang.Exception -> Lc4
            r10 = r10 & 255(0xff, float:3.57E-43)
            r11 = 79
            if (r10 != r11) goto Lbe
            int r10 = r9 + 1
            r10 = r8[r10]     // Catch: java.lang.Exception -> Lc4
            r10 = r10 & 255(0xff, float:3.57E-43)
            r11 = 75
            if (r10 != r11) goto Lbe
            java.lang.String r6 = "发送指令成功"
            com.shj.Shj.onDeviceMessage(r0, r6)     // Catch: java.lang.Exception -> Lc7
            r6 = 1
            goto Lc1
        Lbe:
            int r9 = r9 + 1
            goto La3
        Lc1:
            if (r6 == 0) goto L64
            goto Lcb
        Lc4:
            r2 = r6
            goto Lc7
        Lc6:
            r2 = 0
        Lc7:
            r0 = 0
            r12.serialPortSTK920 = r0
            r6 = r2
        Lcb:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.app.VmdHelper.bql_chugao():boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x00b1, code lost:
    
        com.shj.Shj.onDeviceMessage("SHJ", "发送指令成功");
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00b6, code lost:
    
        r5 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean bql_zhigao() {
        /*
            r11 = this;
            java.lang.String r0 = "SHJ"
            com.hanlu.toolsdk.BqlManager r1 = com.hanlu.toolsdk.BqlManager.get()
            boolean r1 = r1.isDebug()
            r2 = 1
            if (r1 == 0) goto Le
            return r2
        Le:
            r1 = 0
            r11.bql_connect()     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = "发送指令，制糕"
            com.shj.Shj.onDeviceMessage(r0, r3)     // Catch: java.lang.Exception -> Lc0
            r3 = 6
            byte[] r3 = new byte[r3]     // Catch: java.lang.Exception -> Lc0
            r4 = 67
            r3[r1] = r4     // Catch: java.lang.Exception -> Lc0
            r4 = 109(0x6d, float:1.53E-43)
            r3[r2] = r4     // Catch: java.lang.Exception -> Lc0
            r4 = 2
            r5 = 65
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 3
            r5 = 117(0x75, float:1.64E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 4
            r5 = 116(0x74, float:1.63E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 5
            r5 = 111(0x6f, float:1.56E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            android.serialport.SerialPort r4 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lc0
            java.io.OutputStream r4 = r4.getOutputStream()     // Catch: java.lang.Exception -> Lc0
            r4.write(r3)     // Catch: java.lang.Exception -> Lc0
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lc0
            r4.<init>()     // Catch: java.lang.Exception -> Lc0
            java.lang.String r5 = ">>"
            r4.append(r5)     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = com.oysb.utils.ObjectHelper.hex2String(r3)     // Catch: java.lang.Exception -> Lc0
            r4.append(r3)     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = r4.toString()     // Catch: java.lang.Exception -> Lc0
            com.oysb.utils.Loger.writeLog(r0, r3)     // Catch: java.lang.Exception -> Lc0
            long r3 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Lc0
            r5 = 1000(0x3e8, double:4.94E-321)
            java.lang.Thread.sleep(r5)     // Catch: java.lang.Exception -> L60
        L60:
            r5 = 0
        L61:
            long r6 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Lbe
            long r6 = r6 - r3
            r8 = 3000(0xbb8, double:1.482E-320)
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 >= 0) goto Lc5
            android.serialport.SerialPort r6 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lbe
            java.io.InputStream r6 = r6.getInputStream()     // Catch: java.lang.Exception -> Lbe
            int r6 = r6.available()     // Catch: java.lang.Exception -> Lbe
            if (r6 != 0) goto L79
            goto L61
        L79:
            byte[] r7 = new byte[r6]     // Catch: java.lang.Exception -> Lbe
            android.serialport.SerialPort r8 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lbe
            java.io.InputStream r8 = r8.getInputStream()     // Catch: java.lang.Exception -> Lbe
            r8.read(r7)     // Catch: java.lang.Exception -> Lbe
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lbe
            r8.<init>()     // Catch: java.lang.Exception -> Lbe
            java.lang.String r9 = "<<"
            r8.append(r9)     // Catch: java.lang.Exception -> Lbe
            java.lang.String r9 = com.oysb.utils.ObjectHelper.hex2String(r7)     // Catch: java.lang.Exception -> Lbe
            r8.append(r9)     // Catch: java.lang.Exception -> Lbe
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Exception -> Lbe
            com.oysb.utils.Loger.writeLog(r0, r8)     // Catch: java.lang.Exception -> Lbe
            r8 = 0
        L9d:
            if (r8 >= r6) goto Lbb
            r9 = r7[r8]     // Catch: java.lang.Exception -> Lbe
            r9 = r9 & 255(0xff, float:3.57E-43)
            r10 = 79
            if (r9 != r10) goto Lb8
            int r9 = r8 + 1
            r9 = r7[r9]     // Catch: java.lang.Exception -> Lbe
            r9 = r9 & 255(0xff, float:3.57E-43)
            r10 = 75
            if (r9 != r10) goto Lb8
            java.lang.String r5 = "发送指令成功"
            com.shj.Shj.onDeviceMessage(r0, r5)     // Catch: java.lang.Exception -> Lc1
            r5 = 1
            goto Lbb
        Lb8:
            int r8 = r8 + 1
            goto L9d
        Lbb:
            if (r5 == 0) goto L61
            goto Lc5
        Lbe:
            r2 = r5
            goto Lc1
        Lc0:
            r2 = 0
        Lc1:
            r0 = 0
            r11.serialPortSTK920 = r0
            r5 = r2
        Lc5:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.app.VmdHelper.bql_zhigao():boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x00b1, code lost:
    
        com.shj.Shj.onDeviceMessage("SHJ", "发送指令成功");
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00b6, code lost:
    
        r5 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean bql_qingxi() {
        /*
            r11 = this;
            java.lang.String r0 = "SHJ"
            com.hanlu.toolsdk.BqlManager r1 = com.hanlu.toolsdk.BqlManager.get()
            boolean r1 = r1.isDebug()
            r2 = 1
            if (r1 == 0) goto Le
            return r2
        Le:
            r1 = 0
            r11.bql_connect()     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = "发送指令，清洗"
            com.shj.Shj.onDeviceMessage(r0, r3)     // Catch: java.lang.Exception -> Lc0
            r3 = 6
            byte[] r3 = new byte[r3]     // Catch: java.lang.Exception -> Lc0
            r4 = 67
            r3[r1] = r4     // Catch: java.lang.Exception -> Lc0
            r4 = 109(0x6d, float:1.53E-43)
            r3[r2] = r4     // Catch: java.lang.Exception -> Lc0
            r4 = 2
            r5 = 87
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 3
            r5 = 97
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 4
            r5 = 115(0x73, float:1.61E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 5
            r5 = 104(0x68, float:1.46E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            android.serialport.SerialPort r4 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lc0
            java.io.OutputStream r4 = r4.getOutputStream()     // Catch: java.lang.Exception -> Lc0
            r4.write(r3)     // Catch: java.lang.Exception -> Lc0
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lc0
            r4.<init>()     // Catch: java.lang.Exception -> Lc0
            java.lang.String r5 = ">>"
            r4.append(r5)     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = com.oysb.utils.ObjectHelper.hex2String(r3)     // Catch: java.lang.Exception -> Lc0
            r4.append(r3)     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = r4.toString()     // Catch: java.lang.Exception -> Lc0
            com.oysb.utils.Loger.writeLog(r0, r3)     // Catch: java.lang.Exception -> Lc0
            long r3 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Lc0
            r5 = 1000(0x3e8, double:4.94E-321)
            java.lang.Thread.sleep(r5)     // Catch: java.lang.Exception -> L60
        L60:
            r5 = 0
        L61:
            long r6 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Lbe
            long r6 = r6 - r3
            r8 = 3000(0xbb8, double:1.482E-320)
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 >= 0) goto Lc5
            android.serialport.SerialPort r6 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lbe
            java.io.InputStream r6 = r6.getInputStream()     // Catch: java.lang.Exception -> Lbe
            int r6 = r6.available()     // Catch: java.lang.Exception -> Lbe
            if (r6 != 0) goto L79
            goto L61
        L79:
            byte[] r7 = new byte[r6]     // Catch: java.lang.Exception -> Lbe
            android.serialport.SerialPort r8 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lbe
            java.io.InputStream r8 = r8.getInputStream()     // Catch: java.lang.Exception -> Lbe
            r8.read(r7)     // Catch: java.lang.Exception -> Lbe
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lbe
            r8.<init>()     // Catch: java.lang.Exception -> Lbe
            java.lang.String r9 = "<<"
            r8.append(r9)     // Catch: java.lang.Exception -> Lbe
            java.lang.String r9 = com.oysb.utils.ObjectHelper.hex2String(r7)     // Catch: java.lang.Exception -> Lbe
            r8.append(r9)     // Catch: java.lang.Exception -> Lbe
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Exception -> Lbe
            com.oysb.utils.Loger.writeLog(r0, r8)     // Catch: java.lang.Exception -> Lbe
            r8 = 0
        L9d:
            if (r8 >= r6) goto Lbb
            r9 = r7[r8]     // Catch: java.lang.Exception -> Lbe
            r9 = r9 & 255(0xff, float:3.57E-43)
            r10 = 79
            if (r9 != r10) goto Lb8
            int r9 = r8 + 1
            r9 = r7[r9]     // Catch: java.lang.Exception -> Lbe
            r9 = r9 & 255(0xff, float:3.57E-43)
            r10 = 75
            if (r9 != r10) goto Lb8
            java.lang.String r5 = "发送指令成功"
            com.shj.Shj.onDeviceMessage(r0, r5)     // Catch: java.lang.Exception -> Lc1
            r5 = 1
            goto Lbb
        Lb8:
            int r8 = r8 + 1
            goto L9d
        Lbb:
            if (r5 == 0) goto L61
            goto Lc5
        Lbe:
            r2 = r5
            goto Lc1
        Lc0:
            r2 = 0
        Lc1:
            r0 = 0
            r11.serialPortSTK920 = r0
            r5 = r2
        Lc5:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.app.VmdHelper.bql_qingxi():boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x00b1, code lost:
    
        com.shj.Shj.onDeviceMessage("SHJ", "发送指令成功");
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00b6, code lost:
    
        r5 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    boolean bql_guoye() {
        /*
            r11 = this;
            java.lang.String r0 = "SHJ"
            com.hanlu.toolsdk.BqlManager r1 = com.hanlu.toolsdk.BqlManager.get()
            boolean r1 = r1.isDebug()
            r2 = 1
            if (r1 == 0) goto Le
            return r2
        Le:
            r1 = 0
            r11.bql_connect()     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = "发送指令，过夜"
            com.shj.Shj.onDeviceMessage(r0, r3)     // Catch: java.lang.Exception -> Lc0
            r3 = 6
            byte[] r3 = new byte[r3]     // Catch: java.lang.Exception -> Lc0
            r4 = 67
            r3[r1] = r4     // Catch: java.lang.Exception -> Lc0
            r4 = 110(0x6e, float:1.54E-43)
            r3[r2] = r4     // Catch: java.lang.Exception -> Lc0
            r4 = 2
            r5 = 105(0x69, float:1.47E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 3
            r5 = 103(0x67, float:1.44E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 4
            r5 = 104(0x68, float:1.46E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 5
            r5 = 116(0x74, float:1.63E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            android.serialport.SerialPort r4 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lc0
            java.io.OutputStream r4 = r4.getOutputStream()     // Catch: java.lang.Exception -> Lc0
            r4.write(r3)     // Catch: java.lang.Exception -> Lc0
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lc0
            r4.<init>()     // Catch: java.lang.Exception -> Lc0
            java.lang.String r5 = ">>"
            r4.append(r5)     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = com.oysb.utils.ObjectHelper.hex2String(r3)     // Catch: java.lang.Exception -> Lc0
            r4.append(r3)     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = r4.toString()     // Catch: java.lang.Exception -> Lc0
            com.oysb.utils.Loger.writeLog(r0, r3)     // Catch: java.lang.Exception -> Lc0
            long r3 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Lc0
            r5 = 1000(0x3e8, double:4.94E-321)
            java.lang.Thread.sleep(r5)     // Catch: java.lang.Exception -> L60
        L60:
            r5 = 0
        L61:
            long r6 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Lbe
            long r6 = r6 - r3
            r8 = 3000(0xbb8, double:1.482E-320)
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 >= 0) goto Lc5
            android.serialport.SerialPort r6 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lbe
            java.io.InputStream r6 = r6.getInputStream()     // Catch: java.lang.Exception -> Lbe
            int r6 = r6.available()     // Catch: java.lang.Exception -> Lbe
            if (r6 != 0) goto L79
            goto L61
        L79:
            byte[] r7 = new byte[r6]     // Catch: java.lang.Exception -> Lbe
            android.serialport.SerialPort r8 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lbe
            java.io.InputStream r8 = r8.getInputStream()     // Catch: java.lang.Exception -> Lbe
            r8.read(r7)     // Catch: java.lang.Exception -> Lbe
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lbe
            r8.<init>()     // Catch: java.lang.Exception -> Lbe
            java.lang.String r9 = "<<"
            r8.append(r9)     // Catch: java.lang.Exception -> Lbe
            java.lang.String r9 = com.oysb.utils.ObjectHelper.hex2String(r7)     // Catch: java.lang.Exception -> Lbe
            r8.append(r9)     // Catch: java.lang.Exception -> Lbe
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Exception -> Lbe
            com.oysb.utils.Loger.writeLog(r0, r8)     // Catch: java.lang.Exception -> Lbe
            r8 = 0
        L9d:
            if (r8 >= r6) goto Lbb
            r9 = r7[r8]     // Catch: java.lang.Exception -> Lbe
            r9 = r9 & 255(0xff, float:3.57E-43)
            r10 = 79
            if (r9 != r10) goto Lb8
            int r9 = r8 + 1
            r9 = r7[r9]     // Catch: java.lang.Exception -> Lbe
            r9 = r9 & 255(0xff, float:3.57E-43)
            r10 = 75
            if (r9 != r10) goto Lb8
            java.lang.String r5 = "发送指令成功"
            com.shj.Shj.onDeviceMessage(r0, r5)     // Catch: java.lang.Exception -> Lc1
            r5 = 1
            goto Lbb
        Lb8:
            int r8 = r8 + 1
            goto L9d
        Lbb:
            if (r5 == 0) goto L61
            goto Lc5
        Lbe:
            r2 = r5
            goto Lc1
        Lc0:
            r2 = 0
        Lc1:
            r0 = 0
            r11.serialPortSTK920 = r0
            r5 = r2
        Lc5:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.app.VmdHelper.bql_guoye():boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x00b1, code lost:
    
        com.shj.Shj.onDeviceMessage("SHJ", "发送指令成功");
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00b6, code lost:
    
        r5 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean bql_stop() {
        /*
            r11 = this;
            java.lang.String r0 = "SHJ"
            com.hanlu.toolsdk.BqlManager r1 = com.hanlu.toolsdk.BqlManager.get()
            boolean r1 = r1.isDebug()
            r2 = 1
            if (r1 == 0) goto Le
            return r2
        Le:
            r1 = 0
            r11.bql_connect()     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = "发送指令，停止"
            com.shj.Shj.onDeviceMessage(r0, r3)     // Catch: java.lang.Exception -> Lc0
            r3 = 6
            byte[] r3 = new byte[r3]     // Catch: java.lang.Exception -> Lc0
            r4 = 67
            r3[r1] = r4     // Catch: java.lang.Exception -> Lc0
            r4 = 109(0x6d, float:1.53E-43)
            r3[r2] = r4     // Catch: java.lang.Exception -> Lc0
            r4 = 2
            r5 = 83
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 3
            r5 = 116(0x74, float:1.63E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 4
            r5 = 111(0x6f, float:1.56E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            r4 = 5
            r5 = 112(0x70, float:1.57E-43)
            r3[r4] = r5     // Catch: java.lang.Exception -> Lc0
            android.serialport.SerialPort r4 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lc0
            java.io.OutputStream r4 = r4.getOutputStream()     // Catch: java.lang.Exception -> Lc0
            r4.write(r3)     // Catch: java.lang.Exception -> Lc0
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lc0
            r4.<init>()     // Catch: java.lang.Exception -> Lc0
            java.lang.String r5 = ">>"
            r4.append(r5)     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = com.oysb.utils.ObjectHelper.hex2String(r3)     // Catch: java.lang.Exception -> Lc0
            r4.append(r3)     // Catch: java.lang.Exception -> Lc0
            java.lang.String r3 = r4.toString()     // Catch: java.lang.Exception -> Lc0
            com.oysb.utils.Loger.writeLog(r0, r3)     // Catch: java.lang.Exception -> Lc0
            long r3 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Lc0
            r5 = 1000(0x3e8, double:4.94E-321)
            java.lang.Thread.sleep(r5)     // Catch: java.lang.Exception -> L60
        L60:
            r5 = 0
        L61:
            long r6 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Lbe
            long r6 = r6 - r3
            r8 = 3000(0xbb8, double:1.482E-320)
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 >= 0) goto Lc5
            android.serialport.SerialPort r6 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lbe
            java.io.InputStream r6 = r6.getInputStream()     // Catch: java.lang.Exception -> Lbe
            int r6 = r6.available()     // Catch: java.lang.Exception -> Lbe
            if (r6 != 0) goto L79
            goto L61
        L79:
            byte[] r7 = new byte[r6]     // Catch: java.lang.Exception -> Lbe
            android.serialport.SerialPort r8 = r11.serialPortSTK920     // Catch: java.lang.Exception -> Lbe
            java.io.InputStream r8 = r8.getInputStream()     // Catch: java.lang.Exception -> Lbe
            r8.read(r7)     // Catch: java.lang.Exception -> Lbe
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lbe
            r8.<init>()     // Catch: java.lang.Exception -> Lbe
            java.lang.String r9 = "<<"
            r8.append(r9)     // Catch: java.lang.Exception -> Lbe
            java.lang.String r9 = com.oysb.utils.ObjectHelper.hex2String(r7)     // Catch: java.lang.Exception -> Lbe
            r8.append(r9)     // Catch: java.lang.Exception -> Lbe
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Exception -> Lbe
            com.oysb.utils.Loger.writeLog(r0, r8)     // Catch: java.lang.Exception -> Lbe
            r8 = 0
        L9d:
            if (r8 >= r6) goto Lbb
            r9 = r7[r8]     // Catch: java.lang.Exception -> Lbe
            r9 = r9 & 255(0xff, float:3.57E-43)
            r10 = 79
            if (r9 != r10) goto Lb8
            int r9 = r8 + 1
            r9 = r7[r9]     // Catch: java.lang.Exception -> Lbe
            r9 = r9 & 255(0xff, float:3.57E-43)
            r10 = 75
            if (r9 != r10) goto Lb8
            java.lang.String r5 = "发送指令成功"
            com.shj.Shj.onDeviceMessage(r0, r5)     // Catch: java.lang.Exception -> Lc1
            r5 = 1
            goto Lbb
        Lb8:
            int r8 = r8 + 1
            goto L9d
        Lbb:
            if (r5 == 0) goto L61
            goto Lc5
        Lbe:
            r2 = r5
            goto Lc1
        Lc0:
            r2 = 0
        Lc1:
            r0 = 0
            r11.serialPortSTK920 = r0
            r5 = r2
        Lc5:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.machine.app.VmdHelper.bql_stop():boolean");
    }

    boolean bql_set_yingdu(int i) {
        Shj.onDeviceMessage("SHJ", "直接返回，上位机不设置硬度");
        return true;
    }

    void bql_connect() {
        try {
            if (this.serialPortSTK920 == null) {
                this.serialPortSTK920 = new SerialPort("/dev/ttyS4", 9600, 0);
            }
        } catch (Exception unused) {
        }
    }

    boolean bql_wait_chugao_finish() {
        sleep(((getChugao_time() / 10) + 7) * 1000);
        return true;
    }

    public void doBqlChugao() {
        Loger.writeLog("SHJ", "设置=> 冰淇淋机 出糕");
        if (this.threadService == null) {
            this.threadService = Executors.newFixedThreadPool(1);
        }
        this.threadService.execute(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.5
            AnonymousClass5() {
            }

            @Override // java.lang.Runnable
            public void run() {
                VmdHelper.this.bql_chugao();
            }
        });
    }

    /* renamed from: com.xyshj.machine.app.VmdHelper$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements Runnable {
        AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            VmdHelper.this.bql_chugao();
        }
    }

    public boolean isBqlStoped() {
        return this._isBqlStoped;
    }

    public boolean isBqlGuoye() {
        return this._isBqlGuoye;
    }

    public boolean isBqlQingxi() {
        return this._isBqlQingxi;
    }

    public void doBqlZhigao() {
        Loger.writeLog("SHJ", "设置=> 冰淇淋机 制糕");
        if (this.threadService == null) {
            this.threadService = Executors.newFixedThreadPool(1);
        }
        this.threadService.execute(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.6
            AnonymousClass6() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (VmdHelper.this._isBqlGuoye) {
                    VmdHelper.this._isRestarting = true;
                    VmdHelper.this.bql_stop();
                    VmdHelper.this.sleep(1000);
                    VmdHelper.this.bql_qingxi();
                    VmdHelper.this.sleep(30000);
                    VmdHelper.this.bql_stop();
                    VmdHelper.this.sleep(2000);
                    VmdHelper.this._isRestarting = false;
                } else if (VmdHelper.this._isBqlStoped) {
                    VmdHelper.this.bql_stop();
                    VmdHelper.this.sleep(2000);
                }
                VmdHelper.this.bql_zhigao();
                VmdHelper.this.sleep(2000);
                VmdHelper.this.bql_checkStatus(false);
            }
        });
    }

    /* renamed from: com.xyshj.machine.app.VmdHelper$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements Runnable {
        AnonymousClass6() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (VmdHelper.this._isBqlGuoye) {
                VmdHelper.this._isRestarting = true;
                VmdHelper.this.bql_stop();
                VmdHelper.this.sleep(1000);
                VmdHelper.this.bql_qingxi();
                VmdHelper.this.sleep(30000);
                VmdHelper.this.bql_stop();
                VmdHelper.this.sleep(2000);
                VmdHelper.this._isRestarting = false;
            } else if (VmdHelper.this._isBqlStoped) {
                VmdHelper.this.bql_stop();
                VmdHelper.this.sleep(2000);
            }
            VmdHelper.this.bql_zhigao();
            VmdHelper.this.sleep(2000);
            VmdHelper.this.bql_checkStatus(false);
        }
    }

    public void doBqlClean() {
        Loger.writeLog("SHJ", "设置=>清洗冰淇淋机");
        if (this.threadService == null) {
            this.threadService = Executors.newFixedThreadPool(1);
        }
        this.threadService.execute(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.7
            AnonymousClass7() {
            }

            @Override // java.lang.Runnable
            public void run() {
                VmdHelper.this.bql_qingxi();
                VmdHelper.this.sleep(2000);
                VmdHelper.this.bql_checkStatus(false);
            }
        });
    }

    /* renamed from: com.xyshj.machine.app.VmdHelper$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements Runnable {
        AnonymousClass7() {
        }

        @Override // java.lang.Runnable
        public void run() {
            VmdHelper.this.bql_qingxi();
            VmdHelper.this.sleep(2000);
            VmdHelper.this.bql_checkStatus(false);
        }
    }

    public void doBqlGuoye() {
        boolean z = isBqlGuoye() || this.isRunningGuoye;
        StringBuilder sb = new StringBuilder();
        sb.append("是否过夜状态:");
        sb.append(isBqlGuoye());
        sb.append(" 是否正在执行过夜指令:");
        sb.append(this.isRunningGuoye);
        sb.append(" 是否需要下发过夜:");
        sb.append(!z);
        Loger.writeLog("SHJ", sb.toString());
        if (z) {
            return;
        }
        Loger.writeLog("SHJ", "设置=>冰淇淋机过夜");
        this.isRunningGuoye = true;
        if (this.threadService == null) {
            this.threadService = Executors.newFixedThreadPool(1);
        }
        this.threadService.execute(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.8
            AnonymousClass8() {
            }

            @Override // java.lang.Runnable
            public void run() {
                VmdHelper.this.isRunningGuoye = true;
                VmdHelper.this.bql_stop();
                VmdHelper.this.sleep(2000);
                VmdHelper.this.bql_guoye();
                VmdHelper.this.sleep(2000);
                VmdHelper.this.bql_checkStatus(false);
                VmdHelper.this.isRunningGuoye = false;
            }
        });
    }

    /* renamed from: com.xyshj.machine.app.VmdHelper$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements Runnable {
        AnonymousClass8() {
        }

        @Override // java.lang.Runnable
        public void run() {
            VmdHelper.this.isRunningGuoye = true;
            VmdHelper.this.bql_stop();
            VmdHelper.this.sleep(2000);
            VmdHelper.this.bql_guoye();
            VmdHelper.this.sleep(2000);
            VmdHelper.this.bql_checkStatus(false);
            VmdHelper.this.isRunningGuoye = false;
        }
    }

    public void doBqlYingdo(int i) {
        Loger.writeLog("SHJ", "设置=>冰淇淋机，设置硬度 " + i);
        if (this.threadService == null) {
            this.threadService = Executors.newFixedThreadPool(1);
        }
        this.threadService.execute(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.9
            final /* synthetic */ int val$n;

            AnonymousClass9(int i2) {
                i = i2;
            }

            @Override // java.lang.Runnable
            public void run() {
                VmdHelper.this.bql_set_yingdu(i);
                VmdHelper.this.sleep(2000);
                VmdHelper.this.bql_checkStatus(false);
            }
        });
    }

    /* renamed from: com.xyshj.machine.app.VmdHelper$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements Runnable {
        final /* synthetic */ int val$n;

        AnonymousClass9(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public void run() {
            VmdHelper.this.bql_set_yingdu(i);
            VmdHelper.this.sleep(2000);
            VmdHelper.this.bql_checkStatus(false);
        }
    }

    public void CheckBqlPlcStatus() {
        Loger.writeLog("SHJ", "canCheckingBqlPlcStatus:" + this.canCheckingBqlPlcStatus + " time:" + (System.currentTimeMillis() - this.lastCheckBqlPlcStatusTime));
        if (!this.canCheckingBqlPlcStatus || System.currentTimeMillis() - this.lastCheckBqlPlcStatusTime < 2000) {
            return;
        }
        this.lastCheckBqlPlcStatusTime = System.currentTimeMillis();
        Loger.writeLog("SHJ", "查询PLC 1004 是否就绪");
        if (this.threadService == null) {
            this.threadService = Executors.newFixedThreadPool(1);
        }
        this.threadService.execute(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.10
            AnonymousClass10() {
            }

            @Override // java.lang.Runnable
            public void run() {
                VmdHelper.this.canCheckingBqlPlcStatus = false;
                if (BqlManager.get().isReady()) {
                    Shj.setOfferGoodsDiviceState(0);
                }
                VmdHelper.this.canCheckingBqlPlcStatus = true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.app.VmdHelper$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 implements Runnable {
        AnonymousClass10() {
        }

        @Override // java.lang.Runnable
        public void run() {
            VmdHelper.this.canCheckingBqlPlcStatus = false;
            if (BqlManager.get().isReady()) {
                Shj.setOfferGoodsDiviceState(0);
            }
            VmdHelper.this.canCheckingBqlPlcStatus = true;
        }
    }

    public int getMaxCountAfterQianliao() {
        try {
            return Integer.parseInt(CacheHelper.getFileCache().getAsString("maxCountAfterQianliao")) + 1;
        } catch (Exception unused) {
            return 11;
        }
    }

    public int getBqlVersion() {
        try {
            return Integer.parseInt(CacheHelper.getFileCache().getAsString("bqlversion"));
        } catch (Exception unused) {
            return 2;
        }
    }

    public void updateQianliaoOnCheck(boolean z) {
        Loger.writeLog("SHJ", "qianliao:" + z + " r:" + getQianliao());
        if (z) {
            if (getQianliao() == 0) {
                CacheHelper.getFileCache().put("countAfterQianliao", "" + getMaxCountAfterQianliao());
                Shj.onUpdateGoodsCount(201, Integer.valueOf(getMaxCountAfterQianliao() + (-1)));
                return;
            }
            return;
        }
        CacheHelper.getFileCache().put("countAfterQianliao", "0");
        if (Shj.getShelfInfo(201).getGoodsCount().intValue() <= 0) {
            Shj.onUpdateGoodsCount(201, 100);
        }
    }

    public void updateQianliaoOnOffer(int i) {
        if (i <= 0) {
            i = -1;
        }
        Loger.writeLog("SHJ", "updateQianliaoOnOffer:" + i);
        CacheHelper.getFileCache().put("countAfterQianliao", "" + i);
    }

    public int getQianliao() {
        try {
            return Integer.parseInt(CacheHelper.getFileCache().getAsString("countAfterQianliao"));
        } catch (Exception unused) {
            return 0;
        }
    }

    public void doCheckBqlStatus(int i, CheckBqlStatusListener checkBqlStatusListener) {
        Loger.writeLog("SHJ", "查询冰淇淋状态");
        if (isStoped_byTempretrue()) {
            checkBqlStatusListener.onCheckBqlStatusResult(false, ShjAppHelper.getString(R.string.bql_status_stoped_overheat));
            return;
        }
        if (this.threadService == null) {
            this.threadService = Executors.newFixedThreadPool(1);
        }
        this.threadService.execute(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.11
            final /* synthetic */ CheckBqlStatusListener val$l;
            final /* synthetic */ int val$waittime;

            AnonymousClass11(CheckBqlStatusListener checkBqlStatusListener2, int i2) {
                checkBqlStatusListener = checkBqlStatusListener2;
                i = i2;
            }

            @Override // java.lang.Runnable
            public void run() {
                boolean bql_checkStatus = VmdHelper.this.bql_checkStatus(false);
                if (VmdHelper.this._isBqlStoped || VmdHelper.this._isBqlGuoye || VmdHelper.this._isBqlQingxi) {
                    checkBqlStatusListener.onCheckBqlStatusResult(bql_checkStatus, VmdHelper.this.bqlError);
                    return;
                }
                if (!bql_checkStatus && i > 0) {
                    checkBqlStatusListener.onStartWait2StatusOk(VmdHelper.this.bqlError);
                    long currentTimeMillis = System.currentTimeMillis();
                    while (!bql_checkStatus && System.currentTimeMillis() - currentTimeMillis < i) {
                        bql_checkStatus = VmdHelper.this.bql_checkStatus(false);
                        VmdHelper.this.sleep(1000);
                    }
                }
                if (BqlManager.get().isQiangliao()) {
                    VmdHelper.this.bqlError = ShjAppHelper.getString(R.string.bql_stork_no);
                    VmdHelper.this.updateQianliaoOnCheck(true);
                    checkBqlStatusListener.onCheckBqlStatusResult(Shj.getShelfInfo(201).getGoodsCount().intValue() > 0, VmdHelper.this.bqlError);
                } else {
                    VmdHelper.this.updateQianliaoOnCheck(false);
                    checkBqlStatusListener.onCheckBqlStatusResult(bql_checkStatus, VmdHelper.this.bqlError);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.app.VmdHelper$11 */
    /* loaded from: classes2.dex */
    public class AnonymousClass11 implements Runnable {
        final /* synthetic */ CheckBqlStatusListener val$l;
        final /* synthetic */ int val$waittime;

        AnonymousClass11(CheckBqlStatusListener checkBqlStatusListener2, int i2) {
            checkBqlStatusListener = checkBqlStatusListener2;
            i = i2;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean bql_checkStatus = VmdHelper.this.bql_checkStatus(false);
            if (VmdHelper.this._isBqlStoped || VmdHelper.this._isBqlGuoye || VmdHelper.this._isBqlQingxi) {
                checkBqlStatusListener.onCheckBqlStatusResult(bql_checkStatus, VmdHelper.this.bqlError);
                return;
            }
            if (!bql_checkStatus && i > 0) {
                checkBqlStatusListener.onStartWait2StatusOk(VmdHelper.this.bqlError);
                long currentTimeMillis = System.currentTimeMillis();
                while (!bql_checkStatus && System.currentTimeMillis() - currentTimeMillis < i) {
                    bql_checkStatus = VmdHelper.this.bql_checkStatus(false);
                    VmdHelper.this.sleep(1000);
                }
            }
            if (BqlManager.get().isQiangliao()) {
                VmdHelper.this.bqlError = ShjAppHelper.getString(R.string.bql_stork_no);
                VmdHelper.this.updateQianliaoOnCheck(true);
                checkBqlStatusListener.onCheckBqlStatusResult(Shj.getShelfInfo(201).getGoodsCount().intValue() > 0, VmdHelper.this.bqlError);
            } else {
                VmdHelper.this.updateQianliaoOnCheck(false);
                checkBqlStatusListener.onCheckBqlStatusResult(bql_checkStatus, VmdHelper.this.bqlError);
            }
        }
    }

    public void doCheckBqlStatusEx() {
        Loger.writeLog("SHJ", "查询冰淇淋状态");
        if (this.threadService == null) {
            this.threadService = Executors.newFixedThreadPool(1);
        }
        this.threadService.execute(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.12
            AnonymousClass12() {
            }

            @Override // java.lang.Runnable
            public void run() {
                VmdHelper.this.bql_checkStatus(false);
                VmdHelper.this.tryStopBqlPowAfterDongan();
                VmdHelper.this.sleep(1000);
                if (BqlManager.get().isQiangliao()) {
                    VmdHelper.this.updateQianliaoOnCheck(true);
                } else {
                    VmdHelper.this.updateQianliaoOnCheck(false);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.app.VmdHelper$12 */
    /* loaded from: classes2.dex */
    public class AnonymousClass12 implements Runnable {
        AnonymousClass12() {
        }

        @Override // java.lang.Runnable
        public void run() {
            VmdHelper.this.bql_checkStatus(false);
            VmdHelper.this.tryStopBqlPowAfterDongan();
            VmdHelper.this.sleep(1000);
            if (BqlManager.get().isQiangliao()) {
                VmdHelper.this.updateQianliaoOnCheck(true);
            } else {
                VmdHelper.this.updateQianliaoOnCheck(false);
            }
        }
    }

    public void tryStopBqlPowAfterDongan() {
        if (getBqlVersion() == 1) {
            return;
        }
        Loger.writeLog("SHJ", "tryStopBqlAfterDongan isStopedBqlAfterDongan:" + this.isBqlPowOffAfterDongan + " 冻缸故障:" + this._isdonggan + " 变频故障:" + this._isbianpingError + " 高压故障:" + this._isgaoyaError + " 低压故障:" + this._isqianyaError);
        if (this.isBqlPowOffAfterDongan) {
            return;
        }
        if (this._isdonggan || this._isbianpingError || this._isgaoyaError || this._isqianyaError) {
            BqlManager.get().setBqlStatus(false);
            this.isBqlPowOffAfterDongan = true;
        }
    }

    public boolean isBqlPowOn() {
        if (getBqlVersion() == 1) {
            return true;
        }
        boolean isBqlPowon = BqlManager.get().isBqlPowon();
        if (isBqlPowon) {
            this.isBqlPowOffAfterDongan = false;
        }
        return isBqlPowon;
    }

    public boolean checkVmdConnected() {
        String uuid = UUID.randomUUID().toString();
        if (this.isVmdConnected) {
            return true;
        }
        PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ("[error]" + ShjAppHelper.getString(R.string.bql_unconnect))).put("time_out", (Object) 3000).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("showTime", (Object) false));
        return false;
    }

    public boolean checkBqlZhibeiCount(int i) {
        String uuid = UUID.randomUUID().toString();
        Loger.writeLog("SHJ", "201库存:" + Shj.getShelfInfo(201).getGoodsCount() + "");
        if (Shj.getShelfInfo(201).getGoodsCount().intValue() < 1) {
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ShjAppHelper.getString(R.string.bql_leek)).put("time_out", (Object) 2000).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("showTime", (Object) false));
            return false;
        }
        if (Shj.getShelfInfo(202).getGoodsCount().intValue() >= 1) {
            return true;
        }
        PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) ("" + uuid)).put("title", (Object) ShjAppHelper.getString(R.string.notice)).put("info", (Object) ShjAppHelper.getString(R.string.bql_no_cup)).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("time_out", (Object) 2000).put("showTime", (Object) false));
        return false;
    }

    org.json.JSONObject getShelfStatusItem(int i) throws Exception {
        ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
        org.json.JSONObject jSONObject = new org.json.JSONObject();
        jSONObject.put("hdbh", i);
        jSONObject.put("count", shelfInfo.getGoodsCount());
        jSONObject.put("code", shelfInfo.getGoodsCode());
        jSONObject.put("name", shelfInfo.getGoodsName());
        if (i == 101) {
            jSONObject.put(IjkMediaMeta.IJKM_KEY_TYPE, 1);
        } else if (i < 10) {
            jSONObject.put(IjkMediaMeta.IJKM_KEY_TYPE, 2);
        } else if (i > 200) {
            jSONObject.put(IjkMediaMeta.IJKM_KEY_TYPE, 3);
        }
        return jSONObject;
    }

    public void reportBqlStauts(boolean z) {
        if (!(NetAddress.getiAddress() instanceof DefaultAMEAddrss) && this.shelvesUpdated) {
            if (this.threadService2 == null) {
                this.threadService2 = Executors.newFixedThreadPool(1);
            }
            this.threadService2.execute(new Runnable() { // from class: com.xyshj.machine.app.VmdHelper.13
                final /* synthetic */ boolean val$queryQianliao;

                AnonymousClass13(boolean z2) {
                    z = z2;
                }

                /* JADX WARN: Multi-variable type inference failed */
                /* JADX WARN: Type inference failed for: r0v0, types: [org.json.JSONObject] */
                /* JADX WARN: Type inference failed for: r2v30 */
                /* JADX WARN: Type inference failed for: r2v31 */
                /* JADX WARN: Type inference failed for: r2v5 */
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ?? jSONObject = new org.json.JSONObject();
                        jSONObject.put("jqbh", Shj.getMachineId());
                        jSONObject.put("temperature", VmdHelper.this.tempretrue);
                        int i = 0;
                        ?? r2 = VmdHelper.this._isBqlStoped;
                        if (VmdHelper.this._isBqlGuoye) {
                            r2 = 2;
                        }
                        int i2 = r2;
                        if (VmdHelper.this._isBqlQingxi) {
                            i2 = 3;
                        }
                        int i3 = 4;
                        int i4 = i2;
                        if (z) {
                            i4 = i2;
                            if (BqlManager.get().isQiangliao()) {
                                i = 1;
                                i4 = 4;
                            }
                        }
                        int i5 = i4;
                        if (VmdHelper.this._isdonggan) {
                            i = 2;
                            i5 = 4;
                        }
                        int i6 = i5;
                        if (VmdHelper.this._isbianpingError) {
                            i = 3;
                            i6 = 4;
                        }
                        int i7 = i6;
                        if (VmdHelper.this._isqianyaError) {
                            i = 4;
                            i7 = 4;
                        }
                        int i8 = i7;
                        if (VmdHelper.this._isGaoWenError) {
                            i = 5;
                            i8 = 4;
                        }
                        if (VmdHelper.this._isgaoyaError) {
                            i = 6;
                        } else {
                            i3 = i8;
                        }
                        jSONObject.put("status", i3);
                        jSONObject.put("errorCode", i);
                        org.json.JSONArray jSONArray = new org.json.JSONArray();
                        jSONObject.put(ShjDbHelper.TABLE_NAME, jSONArray);
                        for (Integer num : Shj.getShelves()) {
                            if (num.intValue() < 10 || num.intValue() > 200) {
                                ShelfInfo shelfInfo = Shj.getShelfInfo(num);
                                org.json.JSONObject jSONObject2 = new org.json.JSONObject();
                                jSONObject2.put("hdbh", num);
                                jSONObject2.put("count", shelfInfo.getGoodsCount());
                                jSONObject2.put("code", shelfInfo.getGoodsCode());
                                jSONObject2.put("name", shelfInfo.getGoodsName());
                                if (num.intValue() == 101) {
                                    jSONObject2.put(IjkMediaMeta.IJKM_KEY_TYPE, 1);
                                } else if (num.intValue() < 10) {
                                    jSONObject2.put(IjkMediaMeta.IJKM_KEY_TYPE, 2);
                                } else if (num.intValue() > 200) {
                                    jSONObject2.put(IjkMediaMeta.IJKM_KEY_TYPE, 3);
                                }
                                jSONArray.put(jSONObject2);
                            }
                        }
                        String jSONObject3 = jSONObject.toString();
                        if (jSONObject3.equals(VmdHelper.this.lastReportV)) {
                            return;
                        }
                        VmdHelper.this.lastReportV = jSONObject3;
                        jSONObject.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        RequestHelper.request(new RequestItem(NetAddress.reportMachineStatusUrl(), (org.json.JSONObject) jSONObject, "POST"));
                    } catch (Exception unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.xyshj.machine.app.VmdHelper$13 */
    /* loaded from: classes2.dex */
    public class AnonymousClass13 implements Runnable {
        final /* synthetic */ boolean val$queryQianliao;

        AnonymousClass13(boolean z2) {
            z = z2;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [org.json.JSONObject] */
        /* JADX WARN: Type inference failed for: r2v30 */
        /* JADX WARN: Type inference failed for: r2v31 */
        /* JADX WARN: Type inference failed for: r2v5 */
        @Override // java.lang.Runnable
        public void run() {
            try {
                ?? jSONObject = new org.json.JSONObject();
                jSONObject.put("jqbh", Shj.getMachineId());
                jSONObject.put("temperature", VmdHelper.this.tempretrue);
                int i = 0;
                ?? r2 = VmdHelper.this._isBqlStoped;
                if (VmdHelper.this._isBqlGuoye) {
                    r2 = 2;
                }
                int i2 = r2;
                if (VmdHelper.this._isBqlQingxi) {
                    i2 = 3;
                }
                int i3 = 4;
                int i4 = i2;
                if (z) {
                    i4 = i2;
                    if (BqlManager.get().isQiangliao()) {
                        i = 1;
                        i4 = 4;
                    }
                }
                int i5 = i4;
                if (VmdHelper.this._isdonggan) {
                    i = 2;
                    i5 = 4;
                }
                int i6 = i5;
                if (VmdHelper.this._isbianpingError) {
                    i = 3;
                    i6 = 4;
                }
                int i7 = i6;
                if (VmdHelper.this._isqianyaError) {
                    i = 4;
                    i7 = 4;
                }
                int i8 = i7;
                if (VmdHelper.this._isGaoWenError) {
                    i = 5;
                    i8 = 4;
                }
                if (VmdHelper.this._isgaoyaError) {
                    i = 6;
                } else {
                    i3 = i8;
                }
                jSONObject.put("status", i3);
                jSONObject.put("errorCode", i);
                org.json.JSONArray jSONArray = new org.json.JSONArray();
                jSONObject.put(ShjDbHelper.TABLE_NAME, jSONArray);
                for (Integer num : Shj.getShelves()) {
                    if (num.intValue() < 10 || num.intValue() > 200) {
                        ShelfInfo shelfInfo = Shj.getShelfInfo(num);
                        org.json.JSONObject jSONObject2 = new org.json.JSONObject();
                        jSONObject2.put("hdbh", num);
                        jSONObject2.put("count", shelfInfo.getGoodsCount());
                        jSONObject2.put("code", shelfInfo.getGoodsCode());
                        jSONObject2.put("name", shelfInfo.getGoodsName());
                        if (num.intValue() == 101) {
                            jSONObject2.put(IjkMediaMeta.IJKM_KEY_TYPE, 1);
                        } else if (num.intValue() < 10) {
                            jSONObject2.put(IjkMediaMeta.IJKM_KEY_TYPE, 2);
                        } else if (num.intValue() > 200) {
                            jSONObject2.put(IjkMediaMeta.IJKM_KEY_TYPE, 3);
                        }
                        jSONArray.put(jSONObject2);
                    }
                }
                String jSONObject3 = jSONObject.toString();
                if (jSONObject3.equals(VmdHelper.this.lastReportV)) {
                    return;
                }
                VmdHelper.this.lastReportV = jSONObject3;
                jSONObject.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                RequestHelper.request(new RequestItem(NetAddress.reportMachineStatusUrl(), (org.json.JSONObject) jSONObject, "POST"));
            } catch (Exception unused) {
            }
        }
    }

    public void reportOfferGoodsResult() {
        if (NetAddress.getiAddress() instanceof DefaultAMEAddrss) {
            return;
        }
        try {
            int i = 2;
            Order resentOrder = ShjManager.getOrderManager().getResentOrder(2, null);
            BqlOrder bqlOrder = get().getBqlOrder(resentOrder.getArgs().getArg("bqlOrderSn"));
            org.json.JSONObject jSONObject = new org.json.JSONObject();
            jSONObject.put("jqbh", Shj.getMachineId());
            jSONObject.put("amount", bqlOrder.getOrderPrice_noYhje());
            jSONObject.put("payamount", bqlOrder.getOrderPrice());
            jSONObject.put("promoAmount", bqlOrder.getTotalYhje());
            jSONObject.put("promoCode", bqlOrder.getYhm());
            jSONObject.put("discount", bqlOrder.getOrderDiscount());
            jSONObject.put("count", bqlOrder.getTotalBqlCount());
            jSONObject.put("success", bqlOrder.getSuccessCount());
            if (resentOrder != null) {
                if (resentOrder.getPayType() == OrderPayType.CASH) {
                    i = 0;
                } else if (resentOrder.getPayType() == OrderPayType.ICCard) {
                    i = 1;
                } else if (resentOrder.getPayType() != OrderPayType.WEIXIN && resentOrder.getPayType() != OrderPayType.ZFB && resentOrder.getPayType() != OrderPayType.YLJH) {
                    i = 3;
                }
                jSONObject.put("payType", i);
                jSONObject.put("payid", resentOrder.getPayId());
                jSONObject.put(YGDBHelper.COLUM_ORDERID, resentOrder.getUid());
                org.json.JSONArray jSONArray = new org.json.JSONObject(resentOrder.getArgs().getArg("detail")).getJSONArray("goods");
                int i2 = 0;
                for (int i3 = 0; i3 < jSONArray.length(); i3++) {
                    org.json.JSONObject jSONObject2 = jSONArray.getJSONObject(i3);
                    Integer valueOf = Integer.valueOf(Integer.parseInt(jSONObject2.getString("hdbh")));
                    if (valueOf.intValue() != 101 || (i2 = i2 + 1) <= bqlOrder.getTotalBqlCount()) {
                        jSONObject2.put("name", Shj.getShelfInfo(valueOf).getGoodsName());
                    }
                }
                jSONObject.put(SpeechEvent.KEY_EVENT_RECORD_DATA, jSONArray);
            }
            RequestItem requestItem = new RequestItem(NetAddress.reportOfferResultUrl(), jSONObject, "POST");
            requestItem.setRepeatDelay(20000);
            requestItem.setRequestMaxCount(Integer.MAX_VALUE);
            RequestHelper.request(requestItem);
        } catch (Exception unused) {
        }
    }

    public void synServer() {
        if (NetAddress.getiAddress() instanceof DefaultAMEAddrss) {
            return;
        }
        try {
            org.json.JSONObject jSONObject = new org.json.JSONObject();
            jSONObject.put("jqbh", Shj.getMachineId());
            jSONObject.put("temperature", (int) this.tempretrue);
            jSONObject.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            RequestItem requestItem = new RequestItem(NetAddress.synServerUrl(), jSONObject, "POST");
            requestItem.setRepeatDelay(30000);
            requestItem.setRequestMaxCount(Integer.MAX_VALUE);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.app.VmdHelper.14
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                }

                AnonymousClass14() {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                    try {
                        requestItem2.getJSONParams().put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    } catch (Exception unused) {
                    }
                    VmdHelper.this.isServerSynOk = true;
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                    try {
                        requestItem2.getJSONParams().put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        requestItem2.getJSONParams().put("temperature", (int) VmdHelper.this.tempretrue);
                        VmdHelper.this.isServerSynOk = true;
                        PopView_Info.closeInfo("synServerrcheck");
                        org.json.JSONObject jSONObject2 = new org.json.JSONObject(str).getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONObject("task");
                        if (jSONObject2.has(SpeechConstant.ISV_CMD)) {
                            if (jSONObject2.getString(SpeechConstant.ISV_CMD).equalsIgnoreCase("reboot")) {
                                AndroidSystem.setNeedRestartApp(true, "sever cmd reboot");
                            } else if (jSONObject2.getString(SpeechConstant.ISV_CMD).equalsIgnoreCase("countCoins")) {
                                MdbReader_BDT.get().queryCoins(new MdbReader_BDT.QueryCoinsResultListener() { // from class: com.xyshj.machine.app.VmdHelper.14.1
                                    @Override // com.shj.device.cardreader.MdbReader_BDT.QueryCoinsResultListener
                                    public void onResult(HashMap<Double, Integer> hashMap) {
                                    }

                                    AnonymousClass1() {
                                    }
                                });
                            }
                        }
                        return false;
                    } catch (Exception unused) {
                        return false;
                    }
                }

                /* renamed from: com.xyshj.machine.app.VmdHelper$14$1 */
                /* loaded from: classes2.dex */
                class AnonymousClass1 implements MdbReader_BDT.QueryCoinsResultListener {
                    @Override // com.shj.device.cardreader.MdbReader_BDT.QueryCoinsResultListener
                    public void onResult(HashMap<Double, Integer> hashMap) {
                    }

                    AnonymousClass1() {
                    }
                }
            });
            RequestHelper.request(requestItem);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.app.VmdHelper$14 */
    /* loaded from: classes2.dex */
    public class AnonymousClass14 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass14() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            try {
                requestItem2.getJSONParams().put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            } catch (Exception unused) {
            }
            VmdHelper.this.isServerSynOk = true;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            try {
                requestItem2.getJSONParams().put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                requestItem2.getJSONParams().put("temperature", (int) VmdHelper.this.tempretrue);
                VmdHelper.this.isServerSynOk = true;
                PopView_Info.closeInfo("synServerrcheck");
                org.json.JSONObject jSONObject2 = new org.json.JSONObject(str).getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONObject("task");
                if (jSONObject2.has(SpeechConstant.ISV_CMD)) {
                    if (jSONObject2.getString(SpeechConstant.ISV_CMD).equalsIgnoreCase("reboot")) {
                        AndroidSystem.setNeedRestartApp(true, "sever cmd reboot");
                    } else if (jSONObject2.getString(SpeechConstant.ISV_CMD).equalsIgnoreCase("countCoins")) {
                        MdbReader_BDT.get().queryCoins(new MdbReader_BDT.QueryCoinsResultListener() { // from class: com.xyshj.machine.app.VmdHelper.14.1
                            @Override // com.shj.device.cardreader.MdbReader_BDT.QueryCoinsResultListener
                            public void onResult(HashMap<Double, Integer> hashMap) {
                            }

                            AnonymousClass1() {
                            }
                        });
                    }
                }
                return false;
            } catch (Exception unused) {
                return false;
            }
        }

        /* renamed from: com.xyshj.machine.app.VmdHelper$14$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements MdbReader_BDT.QueryCoinsResultListener {
            @Override // com.shj.device.cardreader.MdbReader_BDT.QueryCoinsResultListener
            public void onResult(HashMap<Double, Integer> hashMap) {
            }

            AnonymousClass1() {
            }
        }
    }

    public boolean checkServerSynOk() {
        if (!this.isServerSynOk) {
            Loger.writeLog("SHJ", "调用synServerr失败,将不能售卖");
            if (System.currentTimeMillis() - this.lastcheckServerSyn > 60000) {
                this.lastcheckServerSyn = System.currentTimeMillis();
                PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) "synServerrcheck").put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("info", (Object) ShjAppHelper.getString(R.string.st_bql_clean_check_stoped)).put("time_out", (Object) 60000).put("showTime", (Object) false));
            }
        }
        return this.isServerSynOk;
    }

    public void checkAndReportError() {
        String str;
        if (!(NetAddress.getiAddress() instanceof DefaultAMEAddrss) && System.currentTimeMillis() - this.lastCheckErrorTime >= 300000) {
            this.lastCheckErrorTime = System.currentTimeMillis();
            boolean z = Shj.getShelfInfo(202).getGoodsCount().intValue() < 1;
            boolean z2 = Shj.getShelfInfo(201).getGoodsCount().intValue() < 1;
            String str2 = "";
            if (MdbReader_BDT.get().getCoinCount() < 10) {
                str2 = "010;";
                str = "Coin lack;";
            } else {
                str = "";
            }
            if (z) {
                str2 = str2 + "020;";
                str = str + "Cup lack;";
            }
            if (z2) {
                str2 = str2 + "021;";
                str = str + "Ice cream lack;";
            }
            if (str2.length() == 0) {
                return;
            }
            try {
                org.json.JSONObject jSONObject = new org.json.JSONObject();
                jSONObject.put("jqbh", Shj.getMachineId());
                jSONObject.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                jSONObject.put("errorCode", str2);
                jSONObject.put("errorMessage", str);
                RequestItem requestItem = new RequestItem(NetAddress.getReportErrorUrl(), jSONObject, "POST");
                org.json.JSONArray jSONArray = new org.json.JSONArray();
                jSONObject.put(SpeechEvent.KEY_EVENT_RECORD_DATA, jSONArray);
                Iterator<Integer> it = Shj.getShelves().iterator();
                while (it.hasNext()) {
                    int intValue = it.next().intValue();
                    ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
                    if (shelfInfo.getGoodsCount().intValue() <= 0) {
                        org.json.JSONObject jSONObject2 = new org.json.JSONObject();
                        jSONObject2.put("hdbh", intValue);
                        jSONObject2.put("count", shelfInfo.getGoodsCount());
                        jSONObject2.put("code", shelfInfo.getGoodsCode());
                        jSONObject2.put("name", shelfInfo.getGoodsName());
                        jSONArray.put(jSONObject2);
                    }
                }
                requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.app.VmdHelper.15
                    @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                    public void onFailure(RequestItem requestItem2, int i, String str3, Throwable th) {
                    }

                    @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                    public void onRequestFinished(RequestItem requestItem2, boolean z3) {
                    }

                    @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                    public boolean onSuccess(RequestItem requestItem2, int i, String str3) {
                        return true;
                    }

                    AnonymousClass15() {
                    }
                });
                RequestHelper.request(requestItem);
            } catch (Exception unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.app.VmdHelper$15 */
    /* loaded from: classes2.dex */
    public class AnonymousClass15 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str3, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z3) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str3) {
            return true;
        }

        AnonymousClass15() {
        }
    }

    public void reportCashBalance() {
        int intValue;
        if ((NetAddress.getiAddress() instanceof DefaultAMEAddrss) || (intValue = Shj.getWallet().getCoinBalance().intValue() + Shj.getWallet().getPaperBalance().intValue()) == this.lastBalance) {
            return;
        }
        this.lastBalance = intValue;
        try {
            org.json.JSONObject jSONObject = new org.json.JSONObject();
            jSONObject.put("jqbh", Shj.getMachineId());
            jSONObject.put("cash", this.lastBalance);
            jSONObject.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            RequestItem requestItem = new RequestItem(NetAddress.getReportCashUrl(), jSONObject, "POST");
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.app.VmdHelper.16
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

                AnonymousClass16() {
                }
            });
            RequestHelper.request(requestItem);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.app.VmdHelper$16 */
    /* loaded from: classes2.dex */
    public class AnonymousClass16 implements RequestItem.OnRequestResultListener {
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

        AnonymousClass16() {
        }
    }
}
