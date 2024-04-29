package com.shj.biz;

import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import com.iflytek.cloud.SpeechUtility;
import com.oysb.utils.AndroidSystem;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.Constant;
import com.oysb.utils.Loger;
import com.oysb.utils.PhoneHelper;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.date.DateUtil;
import com.oysb.utils.video.VideoHelper;
import com.oysb.xy.i.ReportListener;
import com.oysb.xy.net.NetManager;
import com.oysb.xy.net.report.Report;
import com.oysb.xy.net.report.ReportState;
import com.oysb.xy.net.report.Report_Con_ClientVersion;
import com.oysb.xy.net.report.Report_Con_Paytypes;
import com.oysb.xy.net.report.Report_Con_Shelves;
import com.oysb.xy.net.report.Report_Con_androidinfo;
import com.oysb.xy.net.report.Report_Con_apkinfo;
import com.oysb.xy.net.report.Report_Error;
import com.oysb.xy.net.report.Report_OnlinePay_OnlineCard;
import com.oysb.xy.net.report.Report_OnlinePay_OnlineCard_v2;
import com.oysb.xy.net.report.Report_ServerTime;
import com.oysb.xy.net.report.Report_Status_ChargMoneyBalance;
import com.oysb.xy.net.report.Report_Status_CoinMachine;
import com.oysb.xy.net.report.Report_Status_Door;
import com.oysb.xy.net.report.Report_Status_Driverboard;
import com.oysb.xy.net.report.Report_Status_Humidity_v2;
import com.oysb.xy.net.report.Report_Status_PaperMoneyMachine;
import com.oysb.xy.net.report.Report_Status_PosMachine;
import com.oysb.xy.net.report.Report_Status_Shelf;
import com.oysb.xy.net.report.Report_Status_Temperature;
import com.oysb.xy.net.report.Report_Status_Temperature_v2;
import com.oysb.xy.net.report.Report_Status_phone;
import com.oysb.xy.net.report.Report_Trad_CoinMoney;
import com.oysb.xy.net.report.Report_Trad_NoCash;
import com.oysb.xy.net.report.Report_Trad_OfflineOrder_v2;
import com.oysb.xy.net.report.Report_Trad_PaperMoney;
import com.oysb.xy.net.report.Report_Transf_OfferCmd2Server;
import com.shj.MoneyType;
import com.shj.OfferState;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.biz.goods.BatchOfferGoodsInfo;
import com.shj.biz.goods.Goods;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderManager;
import com.shj.biz.order.OrderPayType;
import com.shj.biz.service.ShjBizServiceEx;
import com.shj.device.Machine;
import com.tencent.wxpayface.WxfacePayCommonCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class DataSynchronous {
    static boolean firstTimer = true;
    static boolean isReady = false;
    static long lastShelfInfoUpdateTime;
    static int lockStep;
    static Timer lockTimer;
    static Vector<Integer> shevleslist = new Vector<>();

    public static void report_applayFor_blackList() {
    }

    public static void report_applayFor_lock() {
    }

    public static void report_con_machineStatus() {
    }

    public static void report_set_machineUploadGoodsConfirm() {
    }

    public static void report_set_maxPaperMoneyType() {
    }

    public static void report_set_offerGoodsCheck() {
    }

    public static void report_set_payType() {
    }

    public static void report_status_goodsDropCheck() {
    }

    public static void report_status_warnning() {
    }

    public static void report_topup_apply() {
    }

    public static void report_topup_result() {
    }

    public static void report_trad_cardError() {
    }

    public static void report_trad_offlineCard() {
    }

    public static void report_trad_onecartoonCard() {
    }

    public static void report_trad_onlineCard() {
    }

    public static void report_trad_pos() {
    }

    public static void report_trad_saleSumary() {
    }

    public static void setReady(boolean z) {
        isReady = z;
    }

    public static void startLockTimer() {
        Timer timer = lockTimer;
        if (timer != null) {
            timer.cancel();
            lockTimer = null;
        }
        Timer timer2 = new Timer();
        lockTimer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.shj.biz.DataSynchronous.1
            AnonymousClass1() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    ShjBizServiceEx.startServiceCheckor();
                } catch (Exception unused) {
                }
                try {
                    DataSynchronous.lockStep++;
                    if (DataSynchronous.lockStep == 3) {
                        DataSynchronous.lockStep = 0;
                        DataSynchronous.report_applayFor_lock();
                        DataSynchronous.report_status_coinMachine();
                        DataSynchronous.report_status_paperMachine();
                        DataSynchronous.report_status_posMachine();
                        DataSynchronous.report_status_temperature();
                        DataSynchronous.report_status_humidity();
                        DataSynchronous.report_appstauts();
                    }
                    if (DataSynchronous.firstTimer) {
                        DataSynchronous.firstTimer = false;
                        DataSynchronous.report_machine_status();
                        DataSynchronous.report_status_coinMachine();
                        DataSynchronous.report_status_paperMachine();
                        DataSynchronous.report_status_posMachine();
                        DataSynchronous.report_appstauts();
                    }
                    DataSynchronous.report_status_phone();
                    DataSynchronous.checkOrderErrors();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 60000L, Shj.isDebug() ? 60000L : 600000L);
        new Thread(new RunnableEx(null) { // from class: com.shj.biz.DataSynchronous.2
            AnonymousClass2(Object obj) {
                super(obj);
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                while (true) {
                    try {
                        if (DataSynchronous.shevleslist.size() == 0) {
                            Thread.sleep(500L);
                        }
                        if (System.currentTimeMillis() - DataSynchronous.lastShelfInfoUpdateTime >= 1000) {
                            Enumeration<Integer> elements = DataSynchronous.shevleslist.elements();
                            while (elements.hasMoreElements()) {
                                Integer nextElement = elements.nextElement();
                                DataSynchronous.shevleslist.removeElement(nextElement);
                                ShelfInfo shelfInfo = Shj.getShelfInfo(nextElement);
                                Report_Con_Shelves report_Con_Shelves = new Report_Con_Shelves();
                                report_Con_Shelves.setLostAble(true);
                                report_Con_Shelves.setParams("0", "0", "0", String.format("%03d", nextElement), shelfInfo.getStatus2Server().intValue(), shelfInfo.getGoodsCode(), shelfInfo.getPrice().intValue(), shelfInfo.getGoodsCount().intValue(), shelfInfo.getCapacity().intValue(), 0);
                                NetManager.appendReport(report_Con_Shelves);
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
            }
        }).start();
    }

    /* renamed from: com.shj.biz.DataSynchronous$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                ShjBizServiceEx.startServiceCheckor();
            } catch (Exception unused) {
            }
            try {
                DataSynchronous.lockStep++;
                if (DataSynchronous.lockStep == 3) {
                    DataSynchronous.lockStep = 0;
                    DataSynchronous.report_applayFor_lock();
                    DataSynchronous.report_status_coinMachine();
                    DataSynchronous.report_status_paperMachine();
                    DataSynchronous.report_status_posMachine();
                    DataSynchronous.report_status_temperature();
                    DataSynchronous.report_status_humidity();
                    DataSynchronous.report_appstauts();
                }
                if (DataSynchronous.firstTimer) {
                    DataSynchronous.firstTimer = false;
                    DataSynchronous.report_machine_status();
                    DataSynchronous.report_status_coinMachine();
                    DataSynchronous.report_status_paperMachine();
                    DataSynchronous.report_status_posMachine();
                    DataSynchronous.report_appstauts();
                }
                DataSynchronous.report_status_phone();
                DataSynchronous.checkOrderErrors();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shj.biz.DataSynchronous$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends RunnableEx {
        AnonymousClass2(Object obj) {
            super(obj);
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    if (DataSynchronous.shevleslist.size() == 0) {
                        Thread.sleep(500L);
                    }
                    if (System.currentTimeMillis() - DataSynchronous.lastShelfInfoUpdateTime >= 1000) {
                        Enumeration<Integer> elements = DataSynchronous.shevleslist.elements();
                        while (elements.hasMoreElements()) {
                            Integer nextElement = elements.nextElement();
                            DataSynchronous.shevleslist.removeElement(nextElement);
                            ShelfInfo shelfInfo = Shj.getShelfInfo(nextElement);
                            Report_Con_Shelves report_Con_Shelves = new Report_Con_Shelves();
                            report_Con_Shelves.setLostAble(true);
                            report_Con_Shelves.setParams("0", "0", "0", String.format("%03d", nextElement), shelfInfo.getStatus2Server().intValue(), shelfInfo.getGoodsCode(), shelfInfo.getPrice().intValue(), shelfInfo.getGoodsCount().intValue(), shelfInfo.getCapacity().intValue(), 0);
                            NetManager.appendReport(report_Con_Shelves);
                        }
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    public static void checkOrderErrors() {
        List<Order> resentOrders = ShjManager.getOrderManager().getResentOrders();
        if (resentOrders == null) {
            return;
        }
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        for (Order order : resentOrders) {
            if (order.getStatus() > 0 && order.getStatus() < 6 && valueOf.longValue() - order.getLastOfferTime() > 300000) {
                AppStatusLoger.addAppStatus(null, "BIZ", AppStatusLoger.Type_Order, "", "订单 payId：" + order.getPayId() + " status:" + order.getStatus() + " 交易结果(17 70 3)未上报");
                order.setStatus(7);
            }
        }
    }

    public static void addUpdatedShelf(Integer num) {
        if (shevleslist.contains(num)) {
            return;
        }
        shevleslist.add(num);
    }

    public static void report_machine_status() {
        String apkVersionCode = AndroidSystem.getApkVersionCode(ShjManager.getAppContext(), ShjManager.getAppContext().getPackageName());
        HashMap hashMap = new HashMap();
        hashMap.put(1, 0);
        Machine machine = Shj.getMachine(0, false);
        hashMap.put(2, Integer.valueOf(1 ^ (machine.isDoorIsOpen() ? 1 : 0)));
        hashMap.put(3, Integer.valueOf(machine.getCoinMachineStatus()));
        hashMap.put(4, Integer.valueOf(machine.getPaperMachineStatus()));
        hashMap.put(5, Integer.valueOf(machine.getPosMachineStatus()));
        ReportManager.reportMachineStatus(apkVersionCode, hashMap);
    }

    public static void report_con_shelves() {
        List list;
        if (isReady) {
            List<Integer> shelves = Shj.getShelves();
            HashMap hashMap = new HashMap();
            Iterator<Integer> it = shelves.iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                int layerByShelf = Shj.getLayerByShelf(intValue);
                if (hashMap.containsKey(Integer.valueOf(layerByShelf))) {
                    list = (List) hashMap.get(Integer.valueOf(layerByShelf));
                } else {
                    ArrayList arrayList = new ArrayList();
                    hashMap.put(Integer.valueOf(layerByShelf), arrayList);
                    list = arrayList;
                }
                list.add(Integer.valueOf(intValue));
            }
            ArrayList arrayList2 = new ArrayList();
            Iterator it2 = hashMap.keySet().iterator();
            while (it2.hasNext()) {
                arrayList2.add(Integer.valueOf(((Integer) it2.next()).intValue()));
            }
            Collections.sort(arrayList2);
            Report_Con_Shelves report_Con_Shelves = new Report_Con_Shelves();
            report_Con_Shelves.setParams("0", "0", "1", "-1", 0, "0000", 0, 0, 0, 0);
            NetManager.appendReport(report_Con_Shelves);
            ArrayList arrayList3 = new ArrayList();
            Iterator it3 = arrayList2.iterator();
            int i = 0;
            while (it3.hasNext()) {
                int intValue2 = ((Integer) it3.next()).intValue();
                try {
                    List<Integer> list2 = (List) hashMap.get(Integer.valueOf(intValue2));
                    Collections.sort(list2);
                    i = intValue2 / 10;
                    int i2 = intValue2 % 10;
                    ArrayList arrayList4 = new ArrayList();
                    for (Integer num : list2) {
                        ShelfInfo shelfInfo = Shj.getShelfInfo(num);
                        HashMap hashMap2 = new HashMap();
                        Loger.writeLog("SHJ", "shelf:" + num + " zt:" + shelfInfo.getStatus() + " spbm:" + shelfInfo.getGoodsCode() + " hdjg:" + shelfInfo.getPrice() + " hdcl:" + shelfInfo.getGoodsCount() + " hdrl:" + shelfInfo.getCapacity());
                        hashMap2.put("hdh", String.format("%03d", num));
                        hashMap2.put("zt", shelfInfo.getStatus2Server());
                        hashMap2.put("spbm", shelfInfo.getGoodsCode());
                        hashMap2.put("hdjg", shelfInfo.getPrice());
                        hashMap2.put("hdcl", shelfInfo.getGoodsCount());
                        hashMap2.put("hdrl", shelfInfo.getCapacity());
                        hashMap2.put("gdjc", shelfInfo.getGdjc());
                        hashMap2.put("lay", Integer.valueOf(intValue2));
                        arrayList4.add(hashMap2);
                        arrayList3.add(hashMap2);
                    }
                    Report_Con_Shelves report_Con_Shelves2 = new Report_Con_Shelves();
                    report_Con_Shelves2.setParamsEx("" + i, "" + i2, arrayList4);
                    if (report_Con_Shelves2.getDataSize() < 200) {
                        NetManager.appendReport(report_Con_Shelves2);
                    } else {
                        int dataSize = (report_Con_Shelves2.getDataSize() / 200) + 1;
                        int size = (arrayList4.size() / dataSize) + 1;
                        int i3 = 0;
                        while (i3 < dataSize) {
                            int i4 = size * i3;
                            int i5 = i3 + 1;
                            int i6 = size * i5;
                            if (i3 == dataSize - 1) {
                                i6 = arrayList4.size();
                            }
                            Report_Con_Shelves report_Con_Shelves3 = new Report_Con_Shelves();
                            report_Con_Shelves3.setParamsEx("" + i, "" + i2, arrayList4.subList(i4, i6));
                            NetManager.appendReport(report_Con_Shelves3);
                            i3 = i5;
                        }
                    }
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
            }
            ReportManager.reportShelfInfo("" + i, arrayList3);
        }
    }

    public static void report_con_clientVersion() {
        if (isReady) {
            try {
                String machineBoardVersion = Shj.getMachineBoardVersion();
                String obj = ShjManager.getSetting(ShjManager.CLINET_VERSION).toString();
                Report_Con_ClientVersion report_Con_ClientVersion = new Report_Con_ClientVersion();
                report_Con_ClientVersion.setParams(machineBoardVersion, obj, ShjManager.getAppType());
                NetManager.appendReport(report_Con_ClientVersion);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void report_con_paytypes() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (OrderPayType orderPayType : ShjManager.getOrderPayTypes()) {
            arrayList.add(orderPayType.getName() + "(" + OrderManager.orderPayType2ServerType(orderPayType) + ")");
        }
        Report_Con_Paytypes report_Con_Paytypes = new Report_Con_Paytypes();
        report_Con_Paytypes.setParams(arrayList);
        NetManager.appendReport(report_Con_Paytypes);
    }

    public static void report_con_apk_info() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("0:0:0");
        arrayList.add("售货机串口(" + Shj.getComPath() + "." + Shj.getComBaudrate() + ".StoreDataInVMC:" + Shj.isStoreGoodsInfoInVMC() + "):1.0.0:已安装");
        if (arrayList.size() == 2) {
            Report_Con_apkinfo report_Con_apkinfo = new Report_Con_apkinfo();
            report_Con_apkinfo.setParams(arrayList);
            NetManager.appendReport(report_Con_apkinfo);
            arrayList.clear();
        }
        for (Constant.ConfigurePackageInfo configurePackageInfo : Constant.getNeedPackageNames()) {
            String apkStateInfo = AndroidSystem.getApkStateInfo(ShjManager.getAppContext(), configurePackageInfo.packageName, configurePackageInfo.appName);
            if (!apkStateInfo.contains(":-:未安装")) {
                arrayList.add(apkStateInfo);
                if (arrayList.size() == 2) {
                    Report_Con_apkinfo report_Con_apkinfo2 = new Report_Con_apkinfo();
                    report_Con_apkinfo2.setParams(arrayList);
                    NetManager.appendReport(report_Con_apkinfo2);
                    arrayList.clear();
                }
            }
        }
        if (arrayList.size() > 0) {
            Report_Con_apkinfo report_Con_apkinfo3 = new Report_Con_apkinfo();
            report_Con_apkinfo3.setParams(arrayList);
            NetManager.appendReport(report_Con_apkinfo3);
        }
    }

    public static void report_con_android_info() {
        Report_Con_androidinfo report_Con_androidinfo = new Report_Con_androidinfo();
        StringBuilder sb = new StringBuilder();
        sb.append(Marker.ANY_MARKER);
        sb.append(AndroidSystem.getSystemVersion());
        sb.append(Marker.ANY_MARKER);
        sb.append(AndroidSystem.getSystemModelType());
        sb.append(Marker.ANY_MARKER);
        sb.append(AndroidSystem.getSystemHadVersion());
        sb.append(Marker.ANY_MARKER);
        sb.append(AndroidSystem.GetNetworkType(ShjManager.getAppContext()));
        sb.append(Marker.ANY_MARKER);
        sb.append(ShjManager.getPhoneHelper().getSimName());
        sb.append(Marker.ANY_MARKER);
        sb.append(ShjManager.getPhoneHelper().getPhone());
        sb.append(Marker.ANY_MARKER);
        sb.append(ShjManager.getPhoneHelper().getImei());
        sb.append(Marker.ANY_MARKER);
        sb.append(ShjManager.getPhoneHelper().getImsi());
        sb.append(Marker.ANY_MARKER);
        sb.append(ShjManager.getPhoneHelper().getIccid());
        sb.append(Marker.ANY_MARKER);
        sb.append(String.format("%.02f/%.02f", Double.valueOf(AndroidSystem.getFreeMemery(ShjManager.getAppContext())), Double.valueOf(AndroidSystem.getTotalMemery(ShjManager.getAppContext()))));
        sb.append(Marker.ANY_MARKER);
        sb.append(String.format("%.02f/%.02f", Double.valueOf(AndroidSystem.getFreeSpace()), Double.valueOf(AndroidSystem.getTotalSpace())));
        sb.append(Marker.ANY_MARKER);
        sb.append(AndroidSystem.screenWidth(ShjManager.getActivityContext()));
        sb.append(":");
        sb.append(AndroidSystem.screenHeight(ShjManager.getActivityContext()));
        sb.append(Marker.ANY_MARKER);
        sb.append(AndroidSystem.getVirtualBarHeigh(ShjManager.getActivityContext()) > 0 ? "TRUE" : "FALSE");
        sb.append(Marker.ANY_MARKER);
        sb.append(AndroidSystem.getLanguage(ShjManager.getActivityContext()));
        sb.append(Marker.ANY_MARKER);
        sb.append(AndroidSystem.isTimeZoneAuto(ShjManager.getAppContext()) ? "TRUE" : "FALSE");
        sb.append(Marker.ANY_MARKER);
        sb.append(AndroidSystem.isDateTimeAuto(ShjManager.getAppContext()) ? "TRUE" : "FALSE");
        sb.append(Marker.ANY_MARKER);
        sb.append(AndroidSystem.isRoot() ? "TRUE" : "FALSE");
        sb.append(Marker.ANY_MARKER);
        sb.append(VideoHelper.getPlayerMode());
        sb.append(Marker.ANY_MARKER);
        sb.append(AndroidSystem.getSystemSerial());
        report_Con_androidinfo.setParams(sb.toString());
        NetManager.appendReport(report_Con_androidinfo);
        if (AndroidSystem.getFreeSpace() < 0.3d) {
            AppStatusLoger.addAppStatus_no_repeat(null, "BIZ", AppStatusLoger.Type_AppStatus, "", "磁盘空间已不足100M");
        }
    }

    public static void report_set_temperature(int i) {
        if (isReady) {
            Machine machine = Shj.getMachine(0, false);
            Report_Status_Temperature report_Status_Temperature = new Report_Status_Temperature();
            report_Status_Temperature.setParams(i, 0, machine.getTemperature().intValue());
            NetManager.appendReport(report_Status_Temperature);
        }
    }

    public static void report_set_shelfGoodsCode(int i) {
        if (isReady) {
            addUpdatedShelf(Integer.valueOf(i));
            ReportManager.reportSetGoodsCode(1, i, Shj.getShelfInfo(Integer.valueOf(i)).getGoodsCode());
        }
    }

    public static void report_set_shelfPrice(int i) {
        if (isReady) {
            addUpdatedShelf(Integer.valueOf(i));
            ReportManager.reportSetPrice(1, i, Shj.getShelfInfo(Integer.valueOf(i)).getPrice().intValue());
        }
    }

    public static void report_set_shelfCapacity(int i) {
        if (isReady) {
            addUpdatedShelf(Integer.valueOf(i));
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
            int i2 = 1;
            if (i == 0) {
                i2 = 3;
            } else if (i > 1000) {
                i2 = 2;
                i += NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
            }
            ReportManager.reportSetCapacity(i2, i, shelfInfo.getCapacity().intValue());
        }
    }

    public static void report_set_shelfGoodsCount(int i) {
        if (isReady) {
            addUpdatedShelf(Integer.valueOf(i));
        }
    }

    public static void report_status_door(int i) {
        if (isReady) {
            Machine machine = Shj.getMachine(i, false);
            Report_Status_Door report_Status_Door = new Report_Status_Door();
            report_Status_Door.setParams(i, machine.isDoorIsOpen() ? 2 : 1);
            NetManager.appendReport(report_Status_Door);
            ReportManager.reportDoorStatus(i, machine.isDoorIsOpen() ? 2 : 1);
        }
    }

    public static void report_status_humidity() {
        int intValue;
        if (isReady) {
            List<Machine> allMachines = Shj.getAllMachines();
            ArrayList arrayList = new ArrayList();
            for (Machine machine : allMachines) {
                if (machine.hasHumidity() && (intValue = machine.getHumidity().intValue()) != 170 && intValue != 161 && intValue != 162) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("jgh", machine.getJgh());
                    hashMap.put("status", 0);
                    hashMap.put("temperature", machine.getHumidity());
                    arrayList.add(hashMap);
                }
            }
            Report_Status_Humidity_v2 report_Status_Humidity_v2 = new Report_Status_Humidity_v2();
            report_Status_Humidity_v2.setParams(arrayList);
            NetManager.appendReport(report_Status_Humidity_v2);
        }
    }

    public static void report_status_temperature() {
        int intValue;
        if (isReady) {
            List<Machine> allMachines = Shj.getAllMachines();
            if (allMachines.size() == 0) {
                Machine machine = Shj.getMachine(0, false);
                Report_Status_Temperature report_Status_Temperature = new Report_Status_Temperature();
                report_Status_Temperature.setParams(0, machine.getTemperatureState().intValue(), machine.getTemperature().intValue());
                NetManager.appendReport(report_Status_Temperature);
                ReportManager.reportTemperature(0, machine.getTemperatureState().intValue(), machine.getTemperature().intValue());
                if (machine.getTemperature().intValue() > 10) {
                    AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_DeviceError, "", "温度大于10度");
                }
                if (machine.getTemperatureState().intValue() == 1) {
                    AppStatusLoger.addAppStatus_Count(null, "BIZ", AppStatusLoger.Type_DeviceError, "", "温控器故障");
                    return;
                }
                return;
            }
            ArrayList arrayList = new ArrayList();
            for (Machine machine2 : allMachines) {
                if (machine2.hasTemperature() && (intValue = machine2.getTemperature().intValue()) != 170 && intValue != 161 && intValue != 162) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("jgh", machine2.getJgh());
                    hashMap.put("status", machine2.getTemperatureState());
                    hashMap.put("temperature", Integer.valueOf(intValue));
                    arrayList.add(hashMap);
                    ReportManager.reportTemperature(machine2.getJgh().intValue(), machine2.getTemperatureState().intValue(), intValue);
                }
            }
            Report_Status_Temperature_v2 report_Status_Temperature_v2 = new Report_Status_Temperature_v2();
            report_Status_Temperature_v2.setParams(arrayList);
            NetManager.appendReport(report_Status_Temperature_v2);
        }
    }

    public static void report_status_phone() {
        if (isReady) {
            PhoneHelper phoneHelper = ShjManager.getPhoneHelper();
            phoneHelper.getPhone();
            Report_Status_phone report_Status_phone = new Report_Status_phone();
            phoneHelper.updateTrafficInfo();
            String asString = CacheHelper.getFileCache().getAsString("NET_TIMEOUT_INFO");
            String str = "0";
            try {
                String format = DateUtil.format(new Date(), "yyyy-MM-dd");
                String[] split = asString.split("&#&");
                str = split[1];
                if (split[0].equalsIgnoreCase(format)) {
                    str = split[1];
                }
            } catch (Exception unused) {
            }
            report_Status_phone.setParams(phoneHelper.getIccid(), phoneHelper.getCurMonthTraffic(), phoneHelper.getCurDayTraffic(), AndroidSystem.getMobileDbm(ShjManager.getAppContext()), NetManager.getNetSpeed(), str, AndroidSystem.GetNetworkType(ShjManager.getAppContext()));
            NetManager.appendReport(report_Status_phone);
            ReportManager.reportMachineSignal(NetManager.getNetSpeed() + Marker.ANY_MARKER + ShjManager.getPhoneHelper().getPhone() + "*-*-");
            if (phoneHelper.getCurDayTraffic() > ShjManager.getNetWorkWaringAmount()) {
                AppStatusLoger.addAppStatus_no_repeat(null, "BIZ", AppStatusLoger.Type_Network, "", "日流量使用超" + (ShjManager.getNetWorkWaringAmount() / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) + "M");
            }
        }
    }

    public static void report_status_shelf(int i) {
        if (isReady) {
            addUpdatedShelf(Integer.valueOf(i));
            Shj.getShelfInfo(Integer.valueOf(i));
            ReportManager.reportSetCount(1, i, Shj.getShelfInfo(Integer.valueOf(i)).getGoodsCount().intValue());
            ReportManager.reportShelfStatus(i, Shj.getShelfInfo(Integer.valueOf(i)).getStatus().intValue());
        }
    }

    public static void report_status_shelf(HashMap<String, Integer> hashMap, HashMap<String, Integer> hashMap2) {
        if (isReady) {
            Report_Status_Shelf report_Status_Shelf = new Report_Status_Shelf();
            report_Status_Shelf.setParams(hashMap, hashMap2);
            NetManager.appendReport(report_Status_Shelf);
            for (String str : hashMap.keySet()) {
                ReportManager.reportShelfStatus(Integer.parseInt(str), hashMap.get(str).intValue());
            }
        }
    }

    public static void report_status_coinMachine() {
        if (isReady) {
            Report_Status_CoinMachine report_Status_CoinMachine = new Report_Status_CoinMachine();
            report_Status_CoinMachine.setParams(Shj.getMachine(0, false).getCoinMachineStatus());
            NetManager.appendReport(report_Status_CoinMachine);
            ReportManager.reportDeviceStatus(1, Shj.getMachine(0, false).getCoinMachineStatus());
        }
    }

    public static void report_status_paperMachine() {
        if (isReady) {
            Report_Status_PaperMoneyMachine report_Status_PaperMoneyMachine = new Report_Status_PaperMoneyMachine();
            report_Status_PaperMoneyMachine.setParams(Shj.getMachine(0, false).getPaperMachineStatus());
            NetManager.appendReport(report_Status_PaperMoneyMachine);
            ReportManager.reportDeviceStatus(2, Shj.getMachine(0, false).getPaperMachineStatus());
        }
    }

    public static void report_status_posMachine() {
        if (isReady) {
            Report_Status_PosMachine report_Status_PosMachine = new Report_Status_PosMachine();
            report_Status_PosMachine.setParams(Shj.getMachine(0, false).getPosMachineStatus());
            NetManager.appendReport(report_Status_PosMachine);
            ReportManager.reportDeviceStatus(3, Shj.getMachine(0, false).getPosMachineStatus());
        }
    }

    public static void report_status_driverboard() {
        if (isReady) {
            Report_Status_Driverboard report_Status_Driverboard = new Report_Status_Driverboard();
            report_Status_Driverboard.setParams(0, 0);
            NetManager.appendReport(report_Status_Driverboard);
        }
    }

    public static void report_status_chargMoneyBalance() {
        if (isReady) {
            Report_Status_ChargMoneyBalance report_Status_ChargMoneyBalance = new Report_Status_ChargMoneyBalance();
            report_Status_ChargMoneyBalance.setParams(true, Shj.getWallet().getCoinBalance().intValue(), true, Shj.getWallet().getPaperBalance().intValue());
            NetManager.appendReport(report_Status_ChargMoneyBalance);
            ReportManager.reportChangeMoney(Shj.getWallet().getCoinBalance().intValue(), Shj.getWallet().getPaperBalance().intValue());
        }
    }

    static int updateOrderRealPayPrice(Order order, ShelfInfo shelfInfo) {
        if (ShjManager.isBatchOfferingGoods()) {
            return shelfInfo.getPrice().intValue();
        }
        int price = order.getPrice();
        try {
            String arg = order.getArgs().getArg(ShjManager.getSetting(order.getPayType() + "ZK").toString());
            return arg.length() > 0 ? (int) (Double.parseDouble(arg) * 100.0d) : price;
        } catch (Exception unused) {
            return price;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Can't wrap try/catch for region: R(34:10|11|(1:13)(1:131)|14|(1:18)|19|(1:21)|22|23|26|(1:28)|29|(1:31)|32|(2:34|(1:100)(2:37|(2:39|(1:41)(1:98))(1:99)))(1:101)|42|(1:97)(15:44|(1:46)|96|55|(1:59)|60|61|(3:90|91|92)|63|(1:65)|(5:67|(1:69)(1:87)|70|(1:72)(1:86)|73)(1:88)|74|75|76|(2:81|82)(1:80))|47|(2:51|(1:53)(15:54|55|(2:57|59)|60|61|(0)|63|(0)|(0)(0)|74|75|76|(1:78)|81|82))|96|55|(0)|60|61|(0)|63|(0)|(0)(0)|74|75|76|(0)|81|82) */
    /* JADX WARN: Removed duplicated region for block: B:57:0x022f A[Catch: Exception -> 0x0329, TRY_LEAVE, TryCatch #2 {Exception -> 0x0329, blocks: (B:11:0x0026, B:14:0x0039, B:16:0x0047, B:18:0x004d, B:19:0x004f, B:22:0x005f, B:23:0x0069, B:24:0x006c, B:29:0x00ed, B:32:0x00ff, B:42:0x0157, B:47:0x01be, B:49:0x01ca, B:51:0x01d6, B:53:0x01de, B:55:0x0229, B:57:0x022f, B:67:0x0275, B:69:0x0282, B:70:0x028a, B:73:0x02af, B:76:0x02fc, B:78:0x0302, B:81:0x030c), top: B:10:0x0026 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0270  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0275 A[Catch: Exception -> 0x0329, TRY_ENTER, TryCatch #2 {Exception -> 0x0329, blocks: (B:11:0x0026, B:14:0x0039, B:16:0x0047, B:18:0x004d, B:19:0x004f, B:22:0x005f, B:23:0x0069, B:24:0x006c, B:29:0x00ed, B:32:0x00ff, B:42:0x0157, B:47:0x01be, B:49:0x01ca, B:51:0x01d6, B:53:0x01de, B:55:0x0229, B:57:0x022f, B:67:0x0275, B:69:0x0282, B:70:0x028a, B:73:0x02af, B:76:0x02fc, B:78:0x0302, B:81:0x030c), top: B:10:0x0026 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0302 A[Catch: Exception -> 0x0329, TryCatch #2 {Exception -> 0x0329, blocks: (B:11:0x0026, B:14:0x0039, B:16:0x0047, B:18:0x004d, B:19:0x004f, B:22:0x005f, B:23:0x0069, B:24:0x006c, B:29:0x00ed, B:32:0x00ff, B:42:0x0157, B:47:0x01be, B:49:0x01ca, B:51:0x01d6, B:53:0x01de, B:55:0x0229, B:57:0x022f, B:67:0x0275, B:69:0x0282, B:70:0x028a, B:73:0x02af, B:76:0x02fc, B:78:0x0302, B:81:0x030c), top: B:10:0x0026 }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x02ce  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x024a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void report_trad_offerGoods(com.shj.biz.order.Order r31, boolean r32) {
        /*
            Method dump skipped, instructions count: 836
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.DataSynchronous.report_trad_offerGoods(com.shj.biz.order.Order, boolean):void");
    }

    /* renamed from: com.shj.biz.DataSynchronous$4 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$MoneyType;

        static {
            int[] iArr = new int[MoneyType.values().length];
            $SwitchMap$com$shj$MoneyType = iArr;
            try {
                iArr[MoneyType.Paper.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Coin.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.ICCard.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.ECard.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Zfb.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Weixin.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.PickCode.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.EAT.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.YL.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.JD.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Weixin_Share.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    public static void report_trad_money_accept() {
        if (isReady) {
            int tradSn = ShjManager.getOrderManager().getTradSn();
            int i = AnonymousClass4.$SwitchMap$com$shj$MoneyType[Shj.getWallet().getLastAddType().ordinal()];
            if (i == 1) {
                int intValue = Shj.getWallet().getLastAdd().intValue();
                if (intValue > 0) {
                    report_trad_paperMoney(tradSn, 1, intValue, 1, true);
                    return;
                }
                return;
            }
            if (i == 2) {
                int intValue2 = Shj.getWallet().getLastAdd().intValue();
                if (intValue2 > 0) {
                    report_trad_coinMoney(tradSn, 1, intValue2, 1);
                    return;
                }
                return;
            }
            if (i == 3 || i == 4) {
                try {
                    int intValue3 = Shj.getWallet().getLastAddMoneyBeforOfferGoods().intValue();
                    if (intValue3 > 0) {
                        ShelfInfo lastSelectedShelf = Shj.getLastSelectedShelf();
                        report_trad_noCash(4, lastSelectedShelf.getShelf().intValue(), lastSelectedShelf.getPrice().intValue(), intValue3, 0, true, Shj.getWallet().getLastAddMoneyInfoBeforOfferGoods());
                    }
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
            }
        }
    }

    public static void report_trad_money_change() {
        int tradSn = ShjManager.getOrderManager().getTradSn();
        if (Shj.getWallet().getLateastChange().intValue() > 0) {
            int intValue = Shj.getWallet().getLastCoinChange().intValue();
            int intValue2 = Shj.getWallet().getLastPaperChange().intValue();
            if (intValue > 0) {
                report_trad_coinMoney(tradSn, 0, intValue, 1);
                ReportManager.reportMoneyBalances(tradSn, 1, 2, intValue, 1);
            }
            if (intValue2 > 0) {
                report_trad_paperMoney(tradSn, 0, intValue2, 1, false);
                ReportManager.reportMoneyBalances(tradSn, 2, 2, intValue2, 1);
            }
            Loger.writeLog("SALES", "正在上报找零信息 sn:" + tradSn + " coinChange:" + intValue + " paperChange:" + intValue2);
        }
    }

    public static void report_trad_paperMoney(int i, int i2, int i3, int i4, boolean z) {
        if (isReady) {
            Report_Trad_PaperMoney report_Trad_PaperMoney = new Report_Trad_PaperMoney();
            report_Trad_PaperMoney.setParams(i, i2, i3, i4, z);
            NetManager.appendReport(report_Trad_PaperMoney);
            Loger.writeLog("SALES", "正在上报收纸币信息 sn:" + i + " type:" + i2 + " money:" + i3 + " count:" + i4);
            ReportManager.reportMoneyBalances(i, 2, 1, i3, i4);
        }
    }

    public static void report_trad_coinMoney(int i, int i2, int i3, int i4) {
        if (isReady) {
            Report_Trad_CoinMoney report_Trad_CoinMoney = new Report_Trad_CoinMoney();
            report_Trad_CoinMoney.setParams(i, i2, i3, i4);
            NetManager.appendReport(report_Trad_CoinMoney);
            Loger.writeLog("SALES", "正在上报投币信息  sn:" + i + " type:" + i2 + " money:" + i3 + " count:" + i4);
            if (i2 == 1) {
                ReportManager.reportMoneyBalances(i, 1, 1, i3, i4);
            }
        }
    }

    public static void report_trad_noCash(int i, int i2, int i3, int i4, int i5, boolean z, String str) {
        if (isReady) {
            int tradSn = ShjManager.getOrderManager().getTradSn();
            Report_Trad_NoCash report_Trad_NoCash = new Report_Trad_NoCash();
            report_Trad_NoCash.setParams(tradSn, i, String.format("%03d", Integer.valueOf(i2)), i3, i4, i5, z, str);
            NetManager.appendReport(report_Trad_NoCash);
            Loger.writeLog("SALES", "正在上报非现金交易信息  sn:" + tradSn + " type:" + i + " shelf:" + i2 + " price:" + i3 + " realPay:" + i4 + " charge:" + i5 + " sucess:" + z + " card:" + str);
        }
    }

    public static void report_status_resetMoney(int i) {
        if (isReady && i != 0) {
            int tradSn = ShjManager.getOrderManager().getTradSn();
            report_trad_coinMoney(tradSn, 2, i, 1);
            Loger.writeLog("SALES", "正在上报吞币信息 sn:" + tradSn + " money:" + i);
            ReportManager.reportMoneyBalances(tradSn, 2, 3, i, 1);
        }
    }

    public static void report_appstauts() {
        AppStatusLoger.clearAppStatuss(7);
        for (AppStatusLoger.AppStatus appStatus : AppStatusLoger.getAppStatus2Report()) {
            report_appstauts(appStatus.getSn(), appStatus.getModule(), appStatus.getType(), appStatus.getCode(), appStatus.getInfo(), appStatus.getCount(), appStatus.getCreateTime());
        }
        List<AppStatusLoger.AppStatus> appStatusCountList = AppStatusLoger.getAppStatusCountList();
        AppStatusLoger.clearStatusCountList();
        for (AppStatusLoger.AppStatus appStatus2 : appStatusCountList) {
            report_appstauts(appStatus2.getSn(), appStatus2.getModule(), appStatus2.getType(), appStatus2.getCode(), appStatus2.getInfo(), appStatus2.getCount(), appStatus2.getCreateTime());
        }
    }

    public static void report_appstauts(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        if (isReady) {
            Report_Error report_Error = new Report_Error();
            report_Error.setParams(str2, str3, str4, str5, str6, str7);
            report_Error.setObject(str);
            report_Error.setReportListener(new ReportListener() { // from class: com.shj.biz.DataSynchronous.3
                AnonymousClass3() {
                }

                @Override // com.oysb.xy.i.ReportListener
                public void onReportResult(Report report) {
                    if (report.getState() == ReportState.Finished) {
                        AppStatusLoger.markAppStatusReported(report.getObject().toString());
                    }
                }
            });
            NetManager.appendReport(report_Error);
        }
    }

    /* renamed from: com.shj.biz.DataSynchronous$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements ReportListener {
        AnonymousClass3() {
        }

        @Override // com.oysb.xy.i.ReportListener
        public void onReportResult(Report report) {
            if (report.getState() == ReportState.Finished) {
                AppStatusLoger.markAppStatusReported(report.getObject().toString());
            }
        }
    }

    public static void report_transf_iccardpay_v2(int i, String str, String str2, String str3, Order order) {
        String str4;
        String str5;
        if (order != null) {
            String arg = order.getArgs().getArg("detail");
            if (arg.length() == 0) {
                if (Shj.getSelectedShelf() != null && Shj.getSelectedShelf().getGoodsCode().equals(order.getGoodsCode())) {
                    arg = "{\"goods\":[{\"spbh\":null,\"hdbh\":\"" + String.format("%03d", Shj.getSelectedShelf().getShelf()) + "\",\"price\":" + order.getPrice() + "}]}";
                } else {
                    arg = "{\"goods\":[{\"spbh\":\"" + String.format("%04d", order.getGoodsCode()) + "\",\"hdbh\":" + ((Object) null) + ",\"price\":" + order.getPrice() + "}]}";
                }
            }
            str4 = order.getArgs().getRemark();
            str5 = arg;
        } else {
            str4 = "0";
            str5 = str4;
        }
        String str6 = str4.length() == 0 ? "0" : str4;
        Report_OnlinePay_OnlineCard_v2 report_OnlinePay_OnlineCard_v2 = new Report_OnlinePay_OnlineCard_v2();
        report_OnlinePay_OnlineCard_v2.setParams(ShjManager.getOrderManager().getTradSn(), Order.nextUid(), i, Shj.getMachineId(), str, str2, order != null ? order.getSumPrice() : 0, str6, str5);
        NetManager.appendReport(report_OnlinePay_OnlineCard_v2);
    }

    public static void report_transf_iccardpay(int i, String str) {
        if (isReady) {
            int tradSn = ShjManager.getOrderManager().getTradSn();
            ShelfInfo selectedShelf = Shj.getSelectedShelf();
            int intValue = selectedShelf == null ? 0 : selectedShelf.getShelf().intValue();
            Report_OnlinePay_OnlineCard report_OnlinePay_OnlineCard = new Report_OnlinePay_OnlineCard();
            report_OnlinePay_OnlineCard.setParams("" + i, "" + tradSn, str, String.format("%03d", Integer.valueOf(intValue)), selectedShelf == null ? 0 : selectedShelf.getPrice().intValue());
            NetManager.appendReport(report_OnlinePay_OnlineCard);
            StringBuilder sb = new StringBuilder();
            sb.append("正在上报非现金交易信息  sn:");
            sb.append(tradSn);
            sb.append(" type:");
            sb.append(i);
            sb.append(" shelf:");
            sb.append(intValue);
            sb.append(" price:");
            sb.append(selectedShelf != null ? selectedShelf.getPrice().intValue() : 0);
            Loger.writeLog("SALES", sb.toString());
        }
    }

    public static void report_transf_offerGoodsCmd(String str, int i, String str2, String str3) {
        if (isReady) {
            Report_Transf_OfferCmd2Server report_Transf_OfferCmd2Server = new Report_Transf_OfferCmd2Server();
            report_Transf_OfferCmd2Server.setParams(str, i, str2, str3);
            NetManager.appendReport(report_Transf_OfferCmd2Server);
            if (i == 3) {
                Loger.writeLog("SALES", "出货结果：" + new String(report_Transf_OfferCmd2Server.getRawData()));
            }
        }
    }

    public static void report_trad_offlineOrder_v2(int i, BatchOfferGoodsInfo batchOfferGoodsInfo) {
        Report_Trad_OfflineOrder_v2 report_Trad_OfflineOrder_v2 = new Report_Trad_OfflineOrder_v2();
        ArrayList arrayList = new ArrayList();
        Iterator<HashMap<String, Integer>> it = batchOfferGoodsInfo.getShelfs().iterator();
        while (it.hasNext()) {
            HashMap<String, Integer> next = it.next();
            ShelfInfo shelfInfo = Shj.getShelfInfo(next.get(ShjDbHelper.COLUM_shelf));
            HashMap hashMap = new HashMap();
            int i2 = 0;
            hashMap.put(ShjDbHelper.COLUM_shelf, String.format("%03d", shelfInfo.getShelf()));
            hashMap.put("goodsCode", shelfInfo.getGoodsCode());
            int intValue = shelfInfo.getPrice().intValue();
            Goods goodsByCode = ShjManager.getGoodsManager().getGoodsByCode(shelfInfo.getGoodsCode());
            if (goodsByCode.getPrice() != goodsByCode.getDiscountPrice() && goodsByCode.getMarkImage().length() > 0) {
                intValue = goodsByCode.getDiscountPrice();
            } else if (goodsByCode.getMarkImage().equalsIgnoreCase("满减") && goodsByCode.getFullcut_cutPrice() > 0 && goodsByCode.getFullcut_fullPrice() > 0) {
                int fullcut_fullPrice = goodsByCode.getFullcut_fullPrice();
                int fullcut_cutPrice = goodsByCode.getFullcut_cutPrice();
                if (intValue >= fullcut_fullPrice && intValue > fullcut_cutPrice) {
                    intValue -= fullcut_cutPrice;
                }
            } else {
                int discountPrice = goodsByCode.getDiscountPrice();
                if (discountPrice != 0 && discountPrice != intValue) {
                    intValue = discountPrice;
                }
            }
            hashMap.put(ShjDbHelper.COLUM_price, Integer.valueOf(intValue));
            if (next.get(SpeechUtility.TAG_RESOURCE_RESULT).intValue() != 2) {
                i2 = ShjManager.offerDeviceErrorState2ServerState(OfferState.int2Enum(next.get(SpeechUtility.TAG_RESOURCE_RESULT).intValue()));
            }
            hashMap.put("success", Integer.valueOf(i2));
            arrayList.add(hashMap);
        }
        report_Trad_OfflineOrder_v2.setParams(ShjManager.getOrderManager().getTradSn(), Shj.getMachineId(), Order.nextUid(), i, arrayList, "0", DateUtil.format(new Date(), "yyyyMMddHHmmss"), "");
        NetManager.appendReport(report_Trad_OfflineOrder_v2);
        Loger.writeLog("SALES", "非线上支付补报订单出货结果：" + new String(report_Trad_OfflineOrder_v2.getRawData()));
    }

    public static void report_queryServerTime() {
        if (isReady) {
            NetManager.appendReport(new Report_ServerTime());
        }
    }
}
