package com.shj.biz;

import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Loger;
import com.oysb.utils.video.TTSManager;
import com.shj.MoneyType;
import com.shj.OfferState;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.biz.goods.BatchOfferGoodsInfo;
import com.shj.biz.yg.YGDBHelper;
import com.shj.command.CommandManager;
import com.shj.commandV2.CommandV2_Up_queryShelfDoorStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class YGOrderHelper {
    static int YG_OrderTask_OptType;
    static Timer YG_OrderTimer;
    static YGDBHelper ygdbHelper;
    static ArrayList<Integer> openningShelves = new ArrayList<>();
    static HashMap<String, String> curYG_OrderInfo = null;
    static long YG_OrderTimer_startTime = 0;

    public static void YG_OrderTask_UpdatePickCode(String str, String str2) {
        if (ygdbHelper == null) {
            ygdbHelper = new YGDBHelper(ShjManager.getAppContext());
        }
        ygdbHelper.updateOrderByPickCode(str, str2);
    }

    public static HashMap<String, String> YG_OrderTask_GetOrderInfo(String str) {
        if (ygdbHelper == null) {
            ygdbHelper = new YGDBHelper(ShjManager.getAppContext());
        }
        return ygdbHelper.getOrderInfo(str);
    }

    public static void YG_OrderTask_OfferOrder(String str, String str2, String str3, String str4, String str5) {
        String str6 = "";
        try {
            BatchOfferGoodsInfo batchOfferGoodsInfo = new BatchOfferGoodsInfo();
            batchOfferGoodsInfo.getOrderArgs().putArg("orderId", "" + str2);
            batchOfferGoodsInfo.getOrderArgs().putArg("payId", "" + str2);
            batchOfferGoodsInfo.getOrderArgs().putArg("操作类型", "取货");
            batchOfferGoodsInfo.getOrderArgs().putArg("动作", "开柜");
            batchOfferGoodsInfo.getOrderArgs().putArg("姓名", str3);
            batchOfferGoodsInfo.getOrderArgs().putArg("身份证", str4);
            batchOfferGoodsInfo.getOrderArgs().putArg("柜号", str5);
            batchOfferGoodsInfo.getOrderArgs().putArg("订单号", str2);
            batchOfferGoodsInfo.getOrderArgs().putArg("提货码", str);
            batchOfferGoodsInfo.setRemark(str2);
            batchOfferGoodsInfo.setShouldSetMoney(false);
            batchOfferGoodsInfo.setCheckShelfStatue(false);
            batchOfferGoodsInfo.setCheckGoodsCount(false);
            ArrayList arrayList = new ArrayList();
            String[] split = str5.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
            for (String str7 : split) {
                if (str7.length() != 0) {
                    arrayList.add(Shj.getShelfInfo(Integer.valueOf(Integer.parseInt(str7))));
                    str6 = str6 + str7 + ":1" + VoiceWakeuperAidl.PARAMS_SEPARATE;
                    batchOfferGoodsInfo.addShelfItem(Integer.parseInt(str7));
                }
            }
            str6.substring(0, str6.length() - 1);
            ShjManager.putData("lastOfferCmd_OrderId", str2);
            curYG_OrderInfo = ygdbHelper.getOrderInfo(str2);
            YG_OrderTask_OptType = 1;
            if (!ShjManager.startBatchOfferGoods(batchOfferGoodsInfo, MoneyType.Weixin)) {
                ShjManager.getGoodsStatusListener().onOfferingGoods_State(-1, OfferState.BusinessStoped.getIndex(), ShjManager.getData(ShjManager.STR_offerGoods_failed).toString(), 1);
                TTSManager.addText(ShjManager.getData(ShjManager.STR_offerGoods_failed).toString());
                ShjManager.tryOfferNextYCCmdGoods();
            } else {
                ShjManager.putData("YG_ORDER_STATE", "NOT_FINISH");
                curYG_OrderInfo = ygdbHelper.getOrderInfo(str2);
                YG_OrderTask_OptType = 1;
            }
        } catch (Exception unused) {
        }
    }

    public static void YG_OrderTask_ReOpenShelf(String str, String str2, String str3, String str4, int i) {
        String str5 = "";
        try {
            BatchOfferGoodsInfo batchOfferGoodsInfo = new BatchOfferGoodsInfo();
            batchOfferGoodsInfo.setCheckGoodsCount(false);
            batchOfferGoodsInfo.setCheckShelfStatue(false);
            batchOfferGoodsInfo.getOrderArgs().putArg("orderId", "" + str);
            batchOfferGoodsInfo.getOrderArgs().putArg("payId", "" + str);
            batchOfferGoodsInfo.getOrderArgs().putArg("操作类型", "寄存");
            batchOfferGoodsInfo.getOrderArgs().putArg("动作", "二次开柜");
            batchOfferGoodsInfo.getOrderArgs().putArg("姓名", str2);
            batchOfferGoodsInfo.getOrderArgs().putArg("身份证", str3);
            batchOfferGoodsInfo.getOrderArgs().putArg("柜号", "" + i);
            batchOfferGoodsInfo.getOrderArgs().putArg("订单号", str);
            batchOfferGoodsInfo.getOrderArgs().putArg("提货码", str4);
            batchOfferGoodsInfo.getOrderArgs().putArg("二次开关柜", i + "");
            batchOfferGoodsInfo.setRemark(str);
            batchOfferGoodsInfo.setShouldSetMoney(false);
            ArrayList arrayList = new ArrayList();
            arrayList.add(Shj.getShelfInfo(Integer.valueOf(i)));
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                str5 = str5 + arrayList.get(i2) + ":1" + VoiceWakeuperAidl.PARAMS_SEPARATE;
                batchOfferGoodsInfo.addShelfItem(((ShelfInfo) arrayList.get(i2)).getShelf().intValue());
            }
            str5.substring(0, str5.length() - 1);
            if (!ShjManager.startBatchOfferGoods(batchOfferGoodsInfo, MoneyType.Weixin)) {
                ShjManager.getGoodsStatusListener().onOfferingGoods_State(-1, OfferState.BusinessStoped.getIndex(), ShjManager.getData(ShjManager.STR_offerGoods_failed).toString(), 1);
                TTSManager.addText(ShjManager.getData(ShjManager.STR_offerGoods_failed).toString());
                ShjManager.tryOfferNextYCCmdGoods();
            } else {
                ShjManager.putData("YG_ORDER_STATE", "NOT_FINISH");
                curYG_OrderInfo = ygdbHelper.getOrderInfo(str);
                YG_OrderTask_OptType = 2;
            }
        } catch (Exception unused) {
        }
    }

    public static void YG_OrderTask_FinishOrder() {
        ShjManager.putData("YG_ORDER_STATE", "FINISH");
        Loger.writeLog("NET", "云柜订单结已结束");
    }

    public static void YG_OrderTask_Test() {
        ShjManager.getBizShjListener().onServerOfferGoodsCmd(0, "1*0000*33*" + UUID.randomUUID().toString() + ";1;yg;1;1;abcdefg;张三;4321578974124474474;123456");
    }

    public static void YG_OrderTask_NewOrder() {
        String str = "";
        try {
        } catch (Exception unused) {
        } catch (Throwable th) {
            ShjManager.tryOfferNextYCCmdGoods();
            throw th;
        }
        if (ShjManager.getData("YG_ORDER_STATE") != null && ShjManager.getData("YG_ORDER_STATE").toString().equalsIgnoreCase("NOT_FINISH")) {
            ShjManager.tryOfferNextYCCmdGoods();
            return;
        }
        String obj = ShjManager.getData("lastOfferCmd_OrderId").toString();
        ShjManager.getData("lastOfferCmd_payType").toString();
        String obj2 = ShjManager.getData("lastOfferCmd_shelf").toString();
        String obj3 = ShjManager.getData("lastOfferCmd_goodsid").toString();
        if (obj3.endsWith(VoiceWakeuperAidl.PARAMS_SEPARATE)) {
            obj3.substring(0, obj3.length() - 1);
        }
        ShjManager.getData("lastOfferCmd_ly").toString();
        String[] split = ShjManager.getData("lastOfferCmd_key").toString().split(VoiceWakeuperAidl.PARAMS_SEPARATE);
        String str2 = split[6];
        String str3 = split[7];
        String str4 = split[8];
        int parseInt = Integer.parseInt(obj2);
        ArrayList arrayList = new ArrayList();
        String str5 = "";
        for (ShelfInfo shelfInfo : Shj.getShelfInfos("")) {
            if (shelfInfo.getGoodsCount().intValue() == 1) {
                str5 = str5 + shelfInfo.getShelf() + VoiceWakeuperAidl.PARAMS_SEPARATE;
                arrayList.add(shelfInfo);
            }
            if (arrayList.size() >= parseInt) {
                break;
            }
        }
        String str6 = str5;
        if (arrayList.size() < parseInt) {
            ShjManager.getStatusListener().onMessage("SHJ", "可用货柜不足;" + parseInt + VoiceWakeuperAidl.PARAMS_SEPARATE + arrayList.size());
            ShjManager.tryOfferNextYCCmdGoods();
            return;
        }
        if (ygdbHelper == null) {
            ygdbHelper = new YGDBHelper(ShjManager.getAppContext());
        }
        ArrayList arrayList2 = arrayList;
        ygdbHelper.addOrder(obj, str4, str2, str3, str6);
        BatchOfferGoodsInfo batchOfferGoodsInfo = new BatchOfferGoodsInfo();
        batchOfferGoodsInfo.setCheckGoodsCount(false);
        batchOfferGoodsInfo.setCheckShelfStatue(false);
        batchOfferGoodsInfo.getOrderArgs().putArg("orderId", "" + obj);
        batchOfferGoodsInfo.getOrderArgs().putArg("payId", "" + obj);
        batchOfferGoodsInfo.getOrderArgs().putArg("操作类型", "寄存");
        batchOfferGoodsInfo.getOrderArgs().putArg("动作", "开柜");
        batchOfferGoodsInfo.getOrderArgs().putArg("姓名", str2);
        batchOfferGoodsInfo.getOrderArgs().putArg("身份证", str3);
        batchOfferGoodsInfo.getOrderArgs().putArg("柜号", str6);
        batchOfferGoodsInfo.getOrderArgs().putArg("订单号", obj);
        batchOfferGoodsInfo.getOrderArgs().putArg("提货码", str4);
        batchOfferGoodsInfo.setRemark(obj);
        batchOfferGoodsInfo.setShouldSetMoney(false);
        int i = 0;
        while (i < arrayList2.size()) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            ArrayList arrayList3 = arrayList2;
            sb.append(arrayList3.get(i));
            sb.append(":");
            sb.append(1);
            sb.append(VoiceWakeuperAidl.PARAMS_SEPARATE);
            String sb2 = sb.toString();
            batchOfferGoodsInfo.addShelfItem(((ShelfInfo) arrayList3.get(i)).getShelf().intValue());
            i++;
            arrayList2 = arrayList3;
            str = sb2;
        }
        str.substring(0, str.length() - 1);
        if (ShjManager.startBatchOfferGoods(batchOfferGoodsInfo, MoneyType.Weixin)) {
            ShjManager.putData("YG_ORDER_STATE", "NOT_FINISH");
            curYG_OrderInfo = ygdbHelper.getOrderInfo(obj);
            YG_OrderTask_OptType = 0;
        } else {
            ShjManager.getGoodsStatusListener().onOfferingGoods_State(-1, OfferState.BusinessStoped.getIndex(), ShjManager.getData(ShjManager.STR_offerGoods_failed).toString(), 1);
            TTSManager.addText(ShjManager.getData(ShjManager.STR_offerGoods_failed).toString());
            ShjManager.tryOfferNextYCCmdGoods();
        }
        curYG_OrderInfo = ygdbHelper.getOrderInfo(obj);
        ShjManager.tryOfferNextYCCmdGoods();
    }

    static void YG_OrderTask_UpdateOpeningShelves() {
        String[] split = curYG_OrderInfo.get(YGDBHelper.COLUM_GH).split(VoiceWakeuperAidl.PARAMS_SEPARATE);
        try {
            openningShelves.clear();
            for (String str : split) {
                if (split.length != 0) {
                    openningShelves.add(Integer.valueOf(Integer.parseInt(str)));
                }
            }
        } catch (Exception unused) {
        }
    }

    static void YG_OrderTask_Timer() {
        Timer timer = YG_OrderTimer;
        if (timer != null) {
            timer.cancel();
            YG_OrderTimer = null;
        }
        Loger.writeLog("SALES", "quer door state ...." + openningShelves.size());
        YG_OrderTimer_startTime = System.currentTimeMillis();
        Timer timer2 = new Timer();
        YG_OrderTimer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.shj.biz.YGOrderHelper.1
            AnonymousClass1() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                int i = 0;
                for (int i2 = 0; i2 < YGOrderHelper.openningShelves.size(); i2++) {
                    Integer num = YGOrderHelper.openningShelves.get(i2);
                    Integer doorStatus = Shj.getShelfInfo(num).getDoorStatus();
                    Loger.writeLog("SALES", "quer door state: " + doorStatus + "...." + YGOrderHelper.openningShelves.size());
                    if (doorStatus.intValue() != 2) {
                        i++;
                        CommandV2_Up_queryShelfDoorStatus commandV2_Up_queryShelfDoorStatus = new CommandV2_Up_queryShelfDoorStatus();
                        commandV2_Up_queryShelfDoorStatus.setParams(num.intValue());
                        CommandManager.appendSendCommand(commandV2_Up_queryShelfDoorStatus);
                    }
                }
                if (i == 0) {
                    Loger.writeLog("SALES", "quer door state cancel");
                    try {
                        YGOrderHelper.YG_OrderTimer.cancel();
                        YGOrderHelper.YG_OrderTimer = null;
                    } catch (Exception unused) {
                    }
                }
            }
        }, 1000L, 1000L);
    }

    /* renamed from: com.shj.biz.YGOrderHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            int i = 0;
            for (int i2 = 0; i2 < YGOrderHelper.openningShelves.size(); i2++) {
                Integer num = YGOrderHelper.openningShelves.get(i2);
                Integer doorStatus = Shj.getShelfInfo(num).getDoorStatus();
                Loger.writeLog("SALES", "quer door state: " + doorStatus + "...." + YGOrderHelper.openningShelves.size());
                if (doorStatus.intValue() != 2) {
                    i++;
                    CommandV2_Up_queryShelfDoorStatus commandV2_Up_queryShelfDoorStatus = new CommandV2_Up_queryShelfDoorStatus();
                    commandV2_Up_queryShelfDoorStatus.setParams(num.intValue());
                    CommandManager.appendSendCommand(commandV2_Up_queryShelfDoorStatus);
                }
            }
            if (i == 0) {
                Loger.writeLog("SALES", "quer door state cancel");
                try {
                    YGOrderHelper.YG_OrderTimer.cancel();
                    YGOrderHelper.YG_OrderTimer = null;
                } catch (Exception unused) {
                }
            }
        }
    }

    public static void updateBatchOfferGoodsResult() {
        try {
            Loger.writeLog("SALES", "open door finished:" + curYG_OrderInfo);
            if (curYG_OrderInfo != null) {
                YG_OrderTask_UpdateOpeningShelves();
                int i = YG_OrderTask_OptType;
                if (i == 0 || i == 2) {
                    Iterator<Integer> it = openningShelves.iterator();
                    while (it.hasNext()) {
                        int intValue = it.next().intValue();
                        ShjManager.setShelfGoodsCount(intValue, 2);
                        Loger.writeLog("SALES", "shelf:" + intValue + " goodsCount:" + Shj.getShelfInfo(Integer.valueOf(intValue)).getGoodsCount() + " state:" + Shj.getShelfInfo(Integer.valueOf(intValue)).getStatus() + StringUtils.SPACE + Shj.getShelfInfo(Integer.valueOf(intValue)).getDoorStatus());
                    }
                } else if (i == 1) {
                    Iterator<Integer> it2 = openningShelves.iterator();
                    while (it2.hasNext()) {
                        int intValue2 = it2.next().intValue();
                        ShjManager.setShelfGoodsCount(intValue2, 1);
                        Loger.writeLog("SALES", "shelf:" + intValue2 + "  goodsCount:" + Shj.getShelfInfo(Integer.valueOf(intValue2)).getGoodsCount() + " state:" + Shj.getShelfInfo(Integer.valueOf(intValue2)).getStatus() + StringUtils.SPACE + Shj.getShelfInfo(Integer.valueOf(intValue2)).getDoorStatus());
                    }
                }
            }
            YG_OrderTask_Timer();
        } catch (Exception unused) {
        }
    }
}
