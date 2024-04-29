package com.shj.biz;

import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Loger;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.biz.order.OrderArgs;
import com.shj.biz.order.OrderPayType;
import com.shj.command.CommandManager;
import com.shj.commandV2.CommandV2_Up_queryShelfDoorStatus;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/* loaded from: classes2.dex */
public class HdgOrderHelper {
    static Timer HDG_OrderTimer = null;
    static long YG_OrderTimer_startTime = 0;
    static int openingShelf = -1;
    static int orderStep = 0;
    static int orderType = -1;
    static int toOpenDcShelf = -1;
    static int toOpenEmptyShelf = -1;

    public static int getCurrentOrderType() {
        return orderType;
    }

    public static int getCurrentOrderStep() {
        return orderStep;
    }

    public static void HDG_OrderTask_Test(int i) {
        ShjManager.getBizShjListener().onServerOfferGoodsCmd(0, "1*0000*33*" + UUID.randomUUID().toString() + ";1;hdg;1;1;abcdefg;" + i + ";4321578974124474474;123456");
    }

    public static void HDG_OrderTask() {
        String obj;
        ShelfInfo shelfInfo;
        try {
            obj = ShjManager.getData("lastOfferCmd_OrderId").toString();
            ShjManager.getData("lastOfferCmd_payType").toString();
            ShjManager.getData("lastOfferCmd_shelf").toString();
            String obj2 = ShjManager.getData("lastOfferCmd_goodsid").toString();
            if (obj2.endsWith(VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                obj2.substring(0, obj2.length() - 1);
            }
            ShjManager.getData("lastOfferCmd_ly").toString();
            int parseInt = Integer.parseInt(ShjManager.getData("lastOfferCmd_key").toString().split(VoiceWakeuperAidl.PARAMS_SEPARATE)[6]);
            orderType = parseInt;
            orderStep = 0;
            shelfInfo = null;
            if (parseInt == 0) {
                Iterator<ShelfInfo> it = Shj.getShelfInfos("").iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ShelfInfo next = it.next();
                    if (next.getGoodsCount().intValue() == 1) {
                        toOpenEmptyShelf = next.getShelf().intValue();
                        shelfInfo = next;
                        break;
                    }
                }
                Iterator<ShelfInfo> it2 = Shj.getShelfInfos("").iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    ShelfInfo next2 = it2.next();
                    if (next2.getGoodsCount().intValue() >= 2) {
                        toOpenDcShelf = next2.getShelf().intValue();
                        break;
                    }
                }
            } else if (parseInt == 1) {
                Iterator<ShelfInfo> it3 = Shj.getShelfInfos("").iterator();
                while (true) {
                    if (!it3.hasNext()) {
                        break;
                    }
                    ShelfInfo next3 = it3.next();
                    if (next3.getGoodsCount().intValue() == 1) {
                        toOpenEmptyShelf = next3.getShelf().intValue();
                        shelfInfo = next3;
                        break;
                    }
                }
            } else if (parseInt == 2) {
                Iterator<ShelfInfo> it4 = Shj.getShelfInfos("").iterator();
                while (true) {
                    if (!it4.hasNext()) {
                        break;
                    }
                    ShelfInfo next4 = it4.next();
                    if (next4.getGoodsCount().intValue() == 2) {
                        toOpenDcShelf = next4.getShelf().intValue();
                        shelfInfo = next4;
                        break;
                    }
                }
            }
        } catch (Exception unused) {
        } catch (Throwable th) {
            ShjManager.tryOfferNextYCCmdGoods();
            throw th;
        }
        if (shelfInfo == null) {
            ShjManager.getStatusListener().onMessage("SHJ", "可用电池不足;");
            ShjManager.tryOfferNextYCCmdGoods();
            return;
        }
        System.currentTimeMillis();
        OrderArgs orderArgs = new OrderArgs();
        orderArgs.autoSelect(false);
        orderArgs.selectByGoodsCode(false);
        orderArgs.setShelf(shelfInfo.getShelf().intValue());
        ShjManager.getOrderManager().submitDriverShelfOrder(OrderPayType.WEIXIN, orderArgs, null, shelfInfo.getPrice().intValue(), obj);
        openingShelf = shelfInfo.getShelf().intValue();
        ShjManager.tryOfferNextYCCmdGoods();
    }

    public static void HDG_OrderTask_shelfDoor_StateChanged(int i, int i2) {
        int i3 = orderStep;
        if (i3 == -1) {
            return;
        }
        if (i2 == 1) {
            int i4 = orderType;
            if (i4 == 0) {
                if (i3 == 0) {
                    ShjManager.setShelfGoodsCount(i, 2);
                } else {
                    ShjManager.setShelfGoodsCount(i, 1);
                }
            } else if (1 == i4) {
                ShjManager.setShelfGoodsCount(i, 2);
            } else if (2 == i3) {
                ShjManager.setShelfGoodsCount(i, 1);
            }
            YG_OrderTask_Timer();
            return;
        }
        if (2 == i2) {
            int i5 = orderType;
            if (i5 == 0) {
                if (i3 == 0) {
                    ShjManager.setShelfGoodsCount(i, 2);
                } else {
                    ShjManager.setShelfGoodsCount(i, 1);
                }
            } else if (1 == i5) {
                ShjManager.setShelfGoodsCount(i, 2);
            } else if (2 == i3) {
                ShjManager.setShelfGoodsCount(i, 1);
            }
            if (orderType == 0 && orderStep == 0) {
                new Timer().schedule(new TimerTask() { // from class: com.shj.biz.HdgOrderHelper.1
                    AnonymousClass1() {
                    }

                    @Override // java.util.TimerTask, java.lang.Runnable
                    public void run() {
                        HdgOrderHelper.orderStep = 1;
                        String obj = ShjManager.getData("lastOfferCmd_OrderId").toString();
                        ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(HdgOrderHelper.toOpenDcShelf));
                        OrderArgs orderArgs = new OrderArgs();
                        orderArgs.autoSelect(false);
                        orderArgs.selectByGoodsCode(false);
                        orderArgs.setShelf(shelfInfo.getShelf().intValue());
                        ShjManager.getOrderManager().submitDriverShelfOrder(OrderPayType.WEIXIN, orderArgs, null, shelfInfo.getPrice().intValue(), obj);
                        HdgOrderHelper.openingShelf = shelfInfo.getShelf().intValue();
                    }
                }, 10000L);
            }
        }
    }

    /* renamed from: com.shj.biz.HdgOrderHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            HdgOrderHelper.orderStep = 1;
            String obj = ShjManager.getData("lastOfferCmd_OrderId").toString();
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(HdgOrderHelper.toOpenDcShelf));
            OrderArgs orderArgs = new OrderArgs();
            orderArgs.autoSelect(false);
            orderArgs.selectByGoodsCode(false);
            orderArgs.setShelf(shelfInfo.getShelf().intValue());
            ShjManager.getOrderManager().submitDriverShelfOrder(OrderPayType.WEIXIN, orderArgs, null, shelfInfo.getPrice().intValue(), obj);
            HdgOrderHelper.openingShelf = shelfInfo.getShelf().intValue();
        }
    }

    static void YG_OrderTask_Timer() {
        if (openingShelf == -1) {
            return;
        }
        Timer timer = HDG_OrderTimer;
        if (timer != null) {
            timer.cancel();
            HDG_OrderTimer = null;
        }
        YG_OrderTimer_startTime = System.currentTimeMillis();
        Timer timer2 = new Timer();
        HDG_OrderTimer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.shj.biz.HdgOrderHelper.2
            AnonymousClass2() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                Integer doorStatus = Shj.getShelfInfo(Integer.valueOf(HdgOrderHelper.openingShelf)).getDoorStatus();
                boolean z = true;
                if (doorStatus.intValue() == 1) {
                    Loger.writeLog("TEST", "查询门状态");
                    CommandV2_Up_queryShelfDoorStatus commandV2_Up_queryShelfDoorStatus = new CommandV2_Up_queryShelfDoorStatus();
                    commandV2_Up_queryShelfDoorStatus.setParams(HdgOrderHelper.openingShelf);
                    CommandManager.appendSendCommand(commandV2_Up_queryShelfDoorStatus);
                } else {
                    z = false;
                }
                if (!z || 2 == doorStatus.intValue()) {
                    try {
                        HdgOrderHelper.HDG_OrderTimer.cancel();
                        HdgOrderHelper.HDG_OrderTimer = null;
                    } catch (Exception unused) {
                    }
                }
            }
        }, 1000L, 1000L);
    }

    /* renamed from: com.shj.biz.HdgOrderHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends TimerTask {
        AnonymousClass2() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Integer doorStatus = Shj.getShelfInfo(Integer.valueOf(HdgOrderHelper.openingShelf)).getDoorStatus();
            boolean z = true;
            if (doorStatus.intValue() == 1) {
                Loger.writeLog("TEST", "查询门状态");
                CommandV2_Up_queryShelfDoorStatus commandV2_Up_queryShelfDoorStatus = new CommandV2_Up_queryShelfDoorStatus();
                commandV2_Up_queryShelfDoorStatus.setParams(HdgOrderHelper.openingShelf);
                CommandManager.appendSendCommand(commandV2_Up_queryShelfDoorStatus);
            } else {
                z = false;
            }
            if (!z || 2 == doorStatus.intValue()) {
                try {
                    HdgOrderHelper.HDG_OrderTimer.cancel();
                    HdgOrderHelper.HDG_OrderTimer = null;
                } catch (Exception unused) {
                }
            }
        }
    }
}
