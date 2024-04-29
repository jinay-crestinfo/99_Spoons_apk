package com.shj.biz.order;

import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Loger;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.video.TTSManager;
import com.shj.MoneyType;
import com.shj.OfferState;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.shj.biz.goods.BatchOfferGoodsInfo;
import com.shj.biz.goods.Goods;
import com.shj.device.cardreader.JB_CardReader;
import com.shj.device.cardreader.MdbReader_BDT;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class OrderManager {
    public static final int ORDER_LAST_OFFER = 2;
    public static final int ORDER_TOP = 1;
    public static final int ORDER_UUIDORKEY = 3;
    private static double netPayHl = 1.0d;
    public static final String stradSNKey = "xyShj_tradSN";
    OrderArgs curOrderArgs;
    Order currentOrder;
    List<OrderPayType> payTypes;
    Order tempOrder;
    private boolean hasUnCreatesubmitOrder = false;
    private boolean singlePaytypeModel = true;
    int qrCodeSize = 150;
    OrderPayManager orderPayManager = new OrderPayManager();
    List<Order> resentOrders = new ArrayList();
    int resendOrdersMaxSize = 10;
    private boolean _canCancelOrderByApp = true;

    public void setCanCancelOrderByApp(boolean z) {
        this._canCancelOrderByApp = z;
    }

    public boolean canCancelOrderByApp() {
        return this._canCancelOrderByApp;
    }

    public static OrderPayType money2OrderPayType(MoneyType moneyType) {
        OrderPayType orderPayType = OrderPayType.CASH;
        switch (AnonymousClass1.$SwitchMap$com$shj$MoneyType[moneyType.ordinal()]) {
            case 1:
            case 2:
                return OrderPayType.CASH;
            case 3:
                return OrderPayType.ICCard;
            case 4:
                return OrderPayType.ECard;
            case 5:
                return OrderPayType.WEIXIN;
            case 6:
                return OrderPayType.ZFB;
            case 7:
                return OrderPayType.JD;
            case 8:
                return OrderPayType.YL;
            case 9:
                return OrderPayType.PickCode;
            default:
                return orderPayType;
        }
    }

    /* renamed from: com.shj.biz.order.OrderManager$1 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$MoneyType;

        static {
            int[] iArr = new int[OrderPayType.values().length];
            $SwitchMap$com$shj$biz$order$OrderPayType = iArr;
            try {
                iArr[OrderPayType.CASH.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.ICCard.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.ECard.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.WEIXIN.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.WxFace.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.Face.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.ZFB.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.JD.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YL.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YL6.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.YLJH.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.PickCode.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.ThridPay.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.ThridPayScanQrcode.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.MemberPay.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.WEIXIN_Share.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            int[] iArr2 = new int[MoneyType.values().length];
            $SwitchMap$com$shj$MoneyType = iArr2;
            try {
                iArr2[MoneyType.Paper.ordinal()] = 1;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Coin.ordinal()] = 2;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.ICCard.ordinal()] = 3;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.ECard.ordinal()] = 4;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Weixin.ordinal()] = 5;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Zfb.ordinal()] = 6;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.JD.ordinal()] = 7;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.YL.ordinal()] = 8;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.PickCode.ordinal()] = 9;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.EAT.ordinal()] = 10;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$com$shj$MoneyType[MoneyType.Weixin_Share.ordinal()] = 11;
            } catch (NoSuchFieldError unused27) {
            }
        }
    }

    public static MoneyType payType2MoneyType(OrderPayType orderPayType) {
        MoneyType moneyType = MoneyType.Paper;
        switch (AnonymousClass1.$SwitchMap$com$shj$biz$order$OrderPayType[orderPayType.ordinal()]) {
            case 1:
                return MoneyType.Paper;
            case 2:
                return MoneyType.ICCard;
            case 3:
                return MoneyType.ECard;
            case 4:
            case 5:
                return MoneyType.Weixin;
            case 6:
            case 7:
                return MoneyType.Zfb;
            case 8:
                return MoneyType.JD;
            case 9:
            case 10:
            case 11:
                return MoneyType.YL;
            case 12:
                return MoneyType.PickCode;
            default:
                return moneyType;
        }
    }

    public static int moneyType2ServerType(MoneyType moneyType) {
        switch (AnonymousClass1.$SwitchMap$com$shj$MoneyType[moneyType.ordinal()]) {
            case 1:
            case 2:
            default:
                return 1;
            case 3:
            case 10:
                return 8;
            case 4:
                return 4;
            case 5:
                return 3;
            case 6:
                return 2;
            case 7:
                return 7;
            case 8:
                return 9;
            case 9:
            case 11:
                return 6;
        }
    }

    public static int orderPayType2ServerType(OrderPayType orderPayType) {
        switch (orderPayType) {
            case ICCard:
                return 8;
            case ECard:
                return 4;
            case WEIXIN:
                return 3;
            case WxFace:
                return 27;
            case Face:
                return 16;
            case ZFB:
                return 2;
            case JD:
            default:
                return 1;
            case YL:
            case YL6:
                return 9;
            case YLJH:
                return 19;
            case PickCode:
                return 6;
            case ThridPay:
                return 10;
            case ThridPayScanQrcode:
                return 11;
            case MemberPay:
                return 15;
        }
    }

    public static OrderPayType serverType2OrderPayType(int i) {
        OrderPayType orderPayType = OrderPayType.WEIXIN;
        if (i == 1) {
            return OrderPayType.CASH;
        }
        if (i == 2) {
            return OrderPayType.ZFB;
        }
        if (i == 3) {
            return OrderPayType.WEIXIN;
        }
        if (i == 4) {
            return OrderPayType.ECard;
        }
        if (i == 6) {
            return OrderPayType.PickCode;
        }
        if (i == 19) {
            return OrderPayType.YLJH;
        }
        if (i == 27) {
            return OrderPayType.WxFace;
        }
        if (i == 15) {
            return OrderPayType.MemberPay;
        }
        if (i != 16) {
            switch (i) {
                case 8:
                    return OrderPayType.ICCard;
                case 9:
                    return OrderPayType.YL6;
                case 10:
                    return OrderPayType.ThridPay;
                case 11:
                    return OrderPayType.ThridPayScanQrcode;
                default:
                    return orderPayType;
            }
        }
        return OrderPayType.Face;
    }

    public static double getNetPayHl() {
        if (netPayHl == 0.0d) {
            netPayHl = 1.0d;
        }
        return netPayHl;
    }

    public void addTradSn() {
        CacheHelper.getFileCache().put(stradSNKey, Integer.valueOf(getTradSn() + 1));
        Loger.writeLog("SHJ", "交易号:" + getTradSn());
    }

    public int getTradSn() {
        try {
            return ((Integer) CacheHelper.getFileCache().getAsObject(stradSNKey)).intValue();
        } catch (Exception unused) {
            CacheHelper.getFileCache().put(stradSNKey, (Serializable) 1);
            return 1;
        }
    }

    public void setQrCodeSize(int i) {
        this.qrCodeSize = i;
    }

    public boolean hasOrder() {
        return this.currentOrder != null || this.hasUnCreatesubmitOrder;
    }

    public boolean orderIsReady() {
        Order order = this.currentOrder;
        return order != null && order.getGoodsCount() > 0;
    }

    public void cancelPay() {
        this.orderPayManager.cancelPay();
    }

    public void cancelOrder() {
        ShjManager.putData("YHM", "");
        if (hasOrder()) {
            try {
                if (!isSinglePaytypeModel()) {
                    Order order = this.currentOrder;
                    if (order != null && order.getStatus() == 0) {
                        this.currentOrder.setStatus(3);
                    }
                    cancelPay();
                }
            } catch (Exception unused) {
            }
            this.hasUnCreatesubmitOrder = false;
            this.currentOrder = null;
            this.tempOrder = null;
            ShjManager.unSelectGoodsOnShelf();
            if (MdbReader_BDT.isEnabled()) {
                MdbReader_BDT.get().cancel();
            }
            if (JB_CardReader.isEnabled()) {
                JB_CardReader.get().cancel();
            }
            Loger.writeLog("SALES", "取消订单成功");
        }
    }

    private synchronized boolean submitOrderV2(List<OrderPayType> list, OrderArgs orderArgs) {
        this.curOrderArgs = orderArgs;
        return _submitOrder(list, orderArgs);
    }

    private synchronized boolean submitOrderV1(List<OrderPayType> list, OrderArgs orderArgs) {
        this.curOrderArgs = orderArgs;
        this.hasUnCreatesubmitOrder = true;
        this.tempOrder = null;
        this.currentOrder = null;
        return _submitOrder(list, orderArgs);
    }

    void addResentOrder(Order order) {
        if (order == null || this.resentOrders.contains(order)) {
            return;
        }
        this.resentOrders.add(0, order);
        int size = this.resentOrders.size();
        while (true) {
            size--;
            if (size > this.resendOrdersMaxSize) {
                this.resentOrders.remove(size);
            } else {
                Loger.writeLog("SALES", "order:" + order);
                return;
            }
        }
    }

    public List<Order> getResentOrders() {
        return this.resentOrders;
    }

    public Order getResentOrder(int i, String str) {
        if (this.resentOrders == null) {
            this.resentOrders = new ArrayList();
        }
        Loger.writeLog("SALES", "ordersize:" + this.resentOrders.size());
        if (this.resentOrders.size() == 0) {
            return null;
        }
        if (i == 1) {
            return this.resentOrders.get(0);
        }
        if ((str == null || str.length() == 0) && i == 2) {
            String lastAddMoneyInfo = Shj.getWallet().getLastAddMoneyInfo();
            if (lastAddMoneyInfo == null) {
                lastAddMoneyInfo = "";
            }
            Object data = ShjManager.getData("lastOfferCmd_OrderId");
            Object obj = data != null ? data : "";
            if (obj.toString().equalsIgnoreCase(lastAddMoneyInfo)) {
                str = obj.toString();
            }
            Loger.writeLog("SALES", "uuidOrKey:" + str);
            if (str == null || str.length() == 0) {
                return this.resentOrders.get(0);
            }
        }
        if (i == 3 && (str == null || str.length() == 0)) {
            return null;
        }
        Iterator<Order> it = this.resentOrders.iterator();
        while (it.hasNext()) {
            Order next = it.next();
            if (str.contains(next.getUid()) || str.contains(next.getPayId())) {
                return next;
            }
        }
        return null;
    }

    public synchronized boolean submitOrder(List<OrderPayType> list, OrderArgs orderArgs) {
        if (Shj.isOfferingGoods()) {
            return false;
        }
        if (!this.singlePaytypeModel) {
            return submitOrderV1(list, orderArgs);
        }
        return submitOrderV2(list, orderArgs);
    }

    public synchronized boolean submitOrderEx(List<OrderPayType> list, List<Goods> list2, int i, OrderArgs orderArgs) {
        if (Shj.isOfferingGoods()) {
            return false;
        }
        this.hasUnCreatesubmitOrder = true;
        Loger.writeLog("SALES", "取消上一次支付监听,确保启动一次新的监听");
        this.curOrderArgs = orderArgs;
        return _submitOrderEx(list, list2, i, orderArgs);
    }

    public synchronized boolean submitOrder(List<OrderPayType> list, List<Goods> list2, int i, OrderArgs orderArgs) {
        if (Shj.isOfferingGoods()) {
            return false;
        }
        this.hasUnCreatesubmitOrder = true;
        Loger.writeLog("SALES", "取消上一次支付监听,确保启动一次新的监听");
        this.curOrderArgs = orderArgs;
        return _submitOrderEx(list, list2, i, orderArgs);
    }

    private synchronized boolean _submitOrder(List<OrderPayType> list, OrderArgs orderArgs) {
        setCanCancelOrderByApp(true);
        Loger.writeLog("SALES", "取消上一次支付监听,确保启动一次新的监听");
        ShjManager.setYCCHOfferingGoods(false);
        ShjManager.getOfferCmdQueue().clear();
        if (ShjManager.getData("YHM") != null && ShjManager.getData("YHM").toString().length() > 0) {
            orderArgs.addRremark("YHM:" + ShjManager.getData("YHM").toString());
        }
        this.orderPayManager.cancelPay();
        this.payTypes = list;
        Order order = this.currentOrder;
        if (order != null) {
            order.setPayTypes(list);
        }
        this.orderPayManager.setTimeOut(ShjManager.getOrderTimeOut());
        Iterator<OrderPayType> it = this.payTypes.iterator();
        while (it.hasNext()) {
            OrderPayType next = it.next();
            int i = AnonymousClass1.$SwitchMap$com$shj$biz$order$OrderPayType[next.ordinal()];
            if (i != 14) {
                switch (i) {
                    case 1:
                        this.orderPayManager.addOrderPayItem(new OrderPayItem_Cash());
                        continue;
                    case 2:
                    case 3:
                        if (ShjManager.getIcCardOrderPayItem() instanceof OrderPayItem_LFPos) {
                            OrderPayItem_LFPos orderPayItem_LFPos = (OrderPayItem_LFPos) ShjManager.getIcCardOrderPayItem();
                            if (!this.orderPayManager.hasLFPosPayItem()) {
                                orderPayItem_LFPos.setPayType(next == OrderPayType.ICCard ? "03" : "01;02;");
                                this.orderPayManager.addOrderPayItem(orderPayItem_LFPos);
                                break;
                            } else {
                                orderPayItem_LFPos.addPayType(next == OrderPayType.ICCard ? "03" : "01;02;");
                                break;
                            }
                        } else {
                            try {
                                this.orderPayManager.addOrderPayItem(ShjManager.getIcCardOrderPayItem());
                                orderArgs.autoSelect(true);
                                ShelfInfo shelfInfo = null;
                                if (orderArgs.isSelectByGoodsCode()) {
                                    int firstGoodsShelf = ShjManager.getGoodsManager().getFirstGoodsShelf(orderArgs.getGoodsCode());
                                    if (firstGoodsShelf != -1) {
                                        shelfInfo = Shj.getShelfInfo(Integer.valueOf(firstGoodsShelf));
                                    }
                                } else if (orderArgs.getShelf() != -1) {
                                    shelfInfo = Shj.getShelfInfo(Integer.valueOf(orderArgs.getShelf()));
                                }
                                if (shelfInfo != null) {
                                    ShjManager.shopCartPay(shelfInfo.getPrice().intValue());
                                    break;
                                } else {
                                    continue;
                                }
                            } catch (Exception unused) {
                                break;
                            }
                        }
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        break;
                    default:
                        continue;
                }
            }
            this.orderPayManager.addOrderPayItem(ShjManager.getYljhOrderPayItem(next));
        }
        if (orderArgs.isSelectByGoodsCode()) {
            Order order2 = this.currentOrder;
            if (order2 != null) {
                order2.setArgs(orderArgs);
            }
            int nextGoodsShelf = ShjManager.getGoodsManager().getNextGoodsShelf(orderArgs.getGoodsCode());
            orderArgs.setShelf(nextGoodsShelf);
            ShelfInfo selectedShelf = Shj.getSelectedShelf();
            if (this.currentOrder != null && selectedShelf != null && selectedShelf.getShelf().intValue() == nextGoodsShelf) {
                Loger.writeLog("SALES", "已选货shelf:" + nextGoodsShelf);
                updatePayListener();
                return true;
            }
        } else {
            try {
                Order order3 = this.currentOrder;
                if (order3 != null) {
                    order3.setArgs(orderArgs);
                }
                if (Shj.getSelectedShelf() != null) {
                    orderArgs.setShelf(Shj.getSelectedShelf().getShelf().intValue());
                }
            } catch (Exception unused2) {
            }
        }
        if (orderArgs.getShelf() == -1) {
            return false;
        }
        Order order4 = this.currentOrder;
        if (order4 == null) {
            createOrderEx(orderArgs.getShelf(), true);
        } else {
            order4.setPayTypes(this.payTypes);
        }
        if (Shj.getSelectedShelf() == null && orderArgs.autoSelect()) {
            ShjManager.selectGoodsOnShelf(orderArgs.getShelf());
        }
        orderArgs.putArg("offerShelves", "" + orderArgs.getShelf());
        this.orderPayManager.startPay(this.currentOrder);
        Order order5 = this.tempOrder;
        if (order5 != null) {
            order5.setArgs(orderArgs);
        }
        Order order6 = this.currentOrder;
        if (order6 != null) {
            order6.setArgs(orderArgs);
        }
        return true;
    }

    public synchronized boolean _submitOrderEx(List<OrderPayType> list, List<Goods> list2, int i, OrderArgs orderArgs) {
        ShjManager.setYCCHOfferingGoods(false);
        ShjManager.getOfferCmdQueue().clear();
        if (ShjManager.getData("YHM") != null && ShjManager.getData("YHM").toString().length() > 0) {
            orderArgs.addRremark("YHM:" + ShjManager.getData("YHM").toString());
        }
        this.orderPayManager.cancelPay();
        this.payTypes = list;
        this.orderPayManager.setTimeOut(ShjManager.getOrderTimeOut());
        Iterator<OrderPayType> it = this.payTypes.iterator();
        while (it.hasNext()) {
            OrderPayType next = it.next();
            switch (AnonymousClass1.$SwitchMap$com$shj$biz$order$OrderPayType[next.ordinal()]) {
                case 1:
                    this.orderPayManager.addOrderPayItem(new OrderPayItem_Cash());
                    break;
                case 2:
                case 3:
                    if (ShjManager.getIcCardOrderPayItem() instanceof OrderPayItem_LFPos) {
                        OrderPayItem_LFPos orderPayItem_LFPos = (OrderPayItem_LFPos) ShjManager.getIcCardOrderPayItem();
                        if (!this.orderPayManager.hasLFPosPayItem()) {
                            orderPayItem_LFPos.setPayType(next == OrderPayType.ICCard ? "03" : "01;02;");
                            this.orderPayManager.addOrderPayItem(orderPayItem_LFPos);
                            break;
                        } else {
                            orderPayItem_LFPos.addPayType(next == OrderPayType.ICCard ? "03" : "01;02;");
                            break;
                        }
                    } else {
                        try {
                            this.orderPayManager.addOrderPayItem(ShjManager.getIcCardOrderPayItem());
                            orderArgs.autoSelect(true);
                            ShjManager.shopCartPay(i);
                            break;
                        } catch (Exception unused) {
                            break;
                        }
                    }
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                    this.orderPayManager.addOrderPayItem(ShjManager.getYljhOrderPayItem(next));
                    break;
            }
        }
        String str = "";
        String str2 = "{\"goods\":[";
        orderArgs.restGoodsBatchnumberLockcount();
        String str3 = "";
        Goods goods = null;
        int i2 = -1;
        for (Goods goods2 : list2) {
            str = str + goods2.getName() + VoiceWakeuperAidl.PARAMS_SEPARATE;
            if (goods != goods2) {
                goods = goods2;
                i2 = 0;
            } else {
                i2++;
            }
            int nextGoodsShelf = ShjManager.getGoodsManager().getNextGoodsShelf(goods2.getCode(), orderArgs.getNextGoodsbatchnumber("" + goods2.getCode()), i2);
            if (nextGoodsShelf != -1) {
                str2 = str2 + "{\"spbh\":\"" + goods2.getCode() + "\",\"hdbh\":\"" + String.format("%03d", Integer.valueOf(nextGoodsShelf)) + "\",\"price\":\"" + goods2.getPrice() + "\"},";
                str3 = str3 + nextGoodsShelf + VoiceWakeuperAidl.PARAMS_SEPARATE;
            }
        }
        if (str3 != null && str3.length() != 0) {
            orderArgs.putArg("offerShelves", new String(str3.substring(0, str3.length() - 1)));
            orderArgs.restGoodsBatchnumberLockcount();
            Loger.writeLog("SALES", str2.toString());
            String substring = str.substring(0, str.length() - 1);
            orderArgs.putArg("detail", str2.substring(0, str2.length() - 1) + "]}");
            this.tempOrder = null;
            this.curOrderArgs = orderArgs;
            this.currentOrder = null;
            Order order = new Order();
            this.currentOrder = order;
            order.setArgs(this.curOrderArgs);
            this.currentOrder.setPayTypes(this.payTypes);
            if (this.curOrderArgs.getArg("PayId").length() > 0) {
                this.currentOrder.setPayId(this.curOrderArgs.getArg("PayId"));
            }
            this.currentOrder.setTradId(getTradSn());
            this.currentOrder.setCreatTime(new Date());
            this.currentOrder.setGoodsCode("-1");
            this.currentOrder.setGoodsName(substring);
            this.currentOrder.setGoodsCount(list2.size());
            this.currentOrder.setPrice(i);
            this.currentOrder.setSumPrice(i);
            this.currentOrder.setStatus(0);
            this.currentOrder.setShelf(-1);
            Order order2 = this.currentOrder;
            this.tempOrder = order2;
            order2.getArgs().putArg("IsExOrder", "true");
            if (Shj.getWallet().getCatchMoney().intValue() > 0) {
                this.currentOrder.setPayType(money2OrderPayType(Shj.getWallet().getLastAddType()));
                this.currentOrder.setPayId(Shj.getWallet().getLastAddMoneyInfo());
            }
            addResentOrder(this.currentOrder);
            this.orderPayManager.startPay(this.currentOrder);
            return true;
        }
        return false;
    }

    public void submitDriverShelfOrder(OrderPayType orderPayType, OrderArgs orderArgs, Order order, int i, String str) {
        MoneyType payType2MoneyType = payType2MoneyType(orderPayType);
        if (str == null) {
            str = "";
        }
        Shj.onAcceptMoney(0, payType2MoneyType, str);
        if (order == null) {
            createOrderEx(orderArgs.getShelf(), true);
        } else {
            this.tempOrder = order;
            this.currentOrder = order;
        }
        this.currentOrder.setPayType(orderPayType);
        this.currentOrder.setArgs(orderArgs);
        this.currentOrder.setPayId(orderArgs.getArg("tradeNo"));
        if (orderArgs.getArg("PICKDOOR").length() > 0) {
            ShjManager.driverShelf(orderArgs.getShelf(), Integer.parseInt(orderArgs.getArg("PICKDOOR")));
        } else {
            ShjManager.driverShelf(orderArgs.getShelf());
        }
    }

    private void updatePayListener() {
        Order order = this.currentOrder;
        if (order == null) {
            return;
        }
        this.orderPayManager.startPay(order);
    }

    public void _onSelectGoodsOnShelf(Integer num) {
        Loger.writeLog("SHJ", "currentOrder:" + this.currentOrder);
        if (!ShjManager.isBatchOfferingGoods() || this.currentOrder == null) {
            if (ShjManager.isBatchOfferingGoods() && this.currentOrder == null) {
                this.curOrderArgs = ShjManager.getBatchOfferGoodsInfo().getOrderArgs();
            }
            Loger.writeLog("SHJ", "createOrderEx******");
            createOrderEx(num.intValue(), false);
            if (ShjManager.isBatchOfferingGoods()) {
                this.currentOrder.setPayType(money2OrderPayType(ShjManager.getBatchOfferGoodsInfo().getMoneyType()));
            }
        }
    }

    public void _onDeselectGoodsOnShelf(Integer num) {
        this.hasUnCreatesubmitOrder = false;
        this.currentOrder = null;
        this.tempOrder = null;
        this.orderPayManager.cancelPay();
    }

    public void _onOrderFinished() {
        try {
            ShjManager.putData("YHM", "");
            this.curOrderArgs = null;
            this.tempOrder = null;
            cancelOrder();
        } catch (Exception unused) {
        }
    }

    public void createOrderEx(int i, boolean z) {
        ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
        Loger.writeLog("SHJ", "createOrderEx shelf:" + i + " goodsCode:" + shelfInfo.getGoodsCode());
        Goods goodsByCode = ShjManager.getGoodsManager().getGoodsByCode(shelfInfo.getGoodsCode());
        if (!z) {
            Order order = this.tempOrder;
            if (order == null || (order.getShelf() != i && this.tempOrder.getArgs().getArg("IsExOrder").length() == 0)) {
                this.tempOrder = null;
                Order order2 = new Order();
                order2.setTradId(getTradSn());
                order2.setCreatTime(new Date());
                order2.setGoodsCode(shelfInfo.getGoodsCode());
                order2.setGoodsName(goodsByCode.getName());
                order2.setGoodsCount(1);
                order2.setPrice(goodsByCode.getPrice());
                order2.setSumPrice(goodsByCode.getPrice() * 1);
                order2.setStatus(0);
                order2.setShelf(i);
                this.tempOrder = order2;
                if (this.curOrderArgs == null) {
                    this.curOrderArgs = new OrderArgs();
                }
                this.currentOrder = order2;
            } else {
                this.currentOrder = this.tempOrder;
            }
        } else {
            Order order3 = new Order();
            order3.setTradId(getTradSn());
            order3.setCreatTime(new Date());
            order3.setGoodsCode(shelfInfo.getGoodsCode());
            order3.setGoodsName(goodsByCode.getName());
            order3.setGoodsCount(1);
            order3.setPrice(goodsByCode.getPrice());
            order3.setSumPrice(goodsByCode.getPrice() * 1);
            order3.setStatus(0);
            order3.setShelf(i);
            this.tempOrder = order3;
            this.currentOrder = order3;
        }
        this.currentOrder.setPayTypes(this.payTypes);
        this.currentOrder.setArgs(this.curOrderArgs);
        addResentOrder(this.currentOrder);
        if (Shj.getWallet().getCatchMoney().intValue() > 0) {
            this.currentOrder.setPayType(money2OrderPayType(Shj.getWallet().getLastAddType()));
            this.currentOrder.setPayId(Shj.getWallet().getLastAddMoneyInfo());
            return;
        }
        OrderArgs orderArgs = this.curOrderArgs;
        if (orderArgs == null || orderArgs.getArg("payId") == null) {
            return;
        }
        this.currentOrder.setPayId(this.curOrderArgs.getArg("payId"));
    }

    public void onDpjModelNetPayResult(int i, int i2, String str) {
        Loger.writeLog("SALES", "onDpjModelNetPayResult:" + i + ":" + i2 + "=" + str);
        this.orderPayManager.onDpjModelNetPayResult(i, i2, str);
    }

    public void onNewPlateformNetPayResult(int i, boolean z) {
        Loger.writeLog("SALES", "payType:" + i + "=true");
        this.orderPayManager.onNewPlateformNetPayResult(i, true);
    }

    public boolean isSinglePaytypeModel() {
        return this.singlePaytypeModel;
    }

    public void setSinglePaytypeModel(boolean z) {
        this.singlePaytypeModel = z;
    }

    public void resetWaitTimer(int i) {
        this.orderPayManager.setTimeOut(i);
        this.orderPayManager.resetWaitTimer();
    }

    public void cancelWaitTimer() {
        this.orderPayManager.cancelWaitTimer();
    }

    public boolean driverThirdPayOrder(String str, int i, OrderPayType orderPayType, String str2, String str3) {
        ShjManager.putData("lastOfferCmd_OrderId", str2);
        ShjManager.putData("lastOfferCmd_payType", "" + orderPayType.getIndex());
        ShjManager.putData("lastOfferCmd_ly", "ThirdPay");
        ShjManager.putData("lastOfferCmd_key", str);
        ShjManager.putData("offering_Order_key", str2);
        new ArrayList();
        Shj.getWallet().setLastAddMoneyInfo(str2);
        Order order = null;
        Order resentOrder = ShjManager.getOrderManager().getResentOrder(1, null);
        if (resentOrder != null && resentOrder.getArgs().getArg("ThirdOrderKey").equalsIgnoreCase(str)) {
            Loger.writeLog("SALES;NET", "order:" + resentOrder);
            resentOrder.setPayId(str2);
            resentOrder.setPrice(i);
            order = resentOrder;
        }
        MoneyType payType2MoneyType = payType2MoneyType(orderPayType);
        Loger.writeLog("SALES;NET", "moneyType:" + payType2MoneyType);
        OrderArgs orderArgs = order == null ? new OrderArgs() : order.getArgs();
        orderArgs.putArg("tradeNo", str2);
        orderArgs.putArg("YC_CMD_OfferGoods", "FALSE");
        orderArgs.putArg("isOfflineOrder", "TRUE");
        orderArgs.putArg("thirdPayName", orderPayType.getName());
        Loger.writeLog("SALES;NET", "shelf:" + str3);
        String[] split = str3.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
        if (split.length == 1) {
            int parseInt = Integer.parseInt(split[0]);
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(parseInt));
            if (shelfInfo != null && shelfInfo.isStatusOK() && shelfInfo.getGoodsCount().intValue() > 0) {
                Loger.writeLog("SALES;NET", "updateArgs");
                orderArgs.autoSelect(false);
                orderArgs.selectByGoodsCode(false);
                orderArgs.setShelf(parseInt);
                ShjManager.getGoodsStatusListener().onOfferingGoods_State(parseInt, OfferState.Offering.getIndex(), ShjManager.getData(ShjManager.STR_offerGoods_n).toString(), 1);
                ShjManager.getOrderManager().submitDriverShelfOrder(orderPayType, orderArgs, order, 0, str2);
                Loger.writeLog("SALES;NET", "货道号:" + parseInt + "已驱动出货");
            } else {
                Loger.writeLog("SALES;NET", "report error");
                ShjManager.getGoodsStatusListener().onOfferingGoods_State(-1, OfferState.BusinessStoped.getIndex(), ShjManager.getData(ShjManager.STR_offerGoods_failed).toString(), 1);
                TTSManager.addText(ShjManager.getData(ShjManager.STR_offerGoods_failed).toString());
                ShjManager.tryOfferNextYCCmdGoods();
                return false;
            }
        } else {
            Loger.writeLog("SALES;NET", "update offerGoodsInfo");
            BatchOfferGoodsInfo batchOfferGoodsInfo = new BatchOfferGoodsInfo();
            if (orderArgs != null) {
                batchOfferGoodsInfo.setOrderArgs(orderArgs);
            }
            batchOfferGoodsInfo.getOrderArgs().putArg("orderId", "" + str2);
            batchOfferGoodsInfo.setRemark(order != null ? order.getPayId() : str2);
            batchOfferGoodsInfo.setMoneyType(payType2MoneyType);
            batchOfferGoodsInfo.setShouldSetMoney(true);
            for (String str4 : split) {
                batchOfferGoodsInfo.addShelfItem(Integer.parseInt(str4));
            }
            Loger.writeLog("SALES;NET", "startBatchOfferGoods");
            if (!ShjManager.startBatchOfferGoods(batchOfferGoodsInfo, payType2MoneyType)) {
                Loger.writeLog("SALES;NET", "report error");
                ShjManager.getGoodsStatusListener().onOfferingGoods_State(-1, OfferState.BusinessStoped.getIndex(), ShjManager.getData(ShjManager.STR_offerGoods_failed).toString(), 1);
                TTSManager.addText(ShjManager.getData(ShjManager.STR_offerGoods_failed).toString());
                ShjManager.tryOfferNextYCCmdGoods();
                return false;
            }
        }
        return true;
    }

    public boolean driverOfferLineOrder(OrderPayType orderPayType, String str, String str2) {
        ShjManager.putData("lastOfferCmd_OrderId", str2);
        ShjManager.putData("lastOfferCmd_payType", "" + orderPayType.getIndex());
        ShjManager.putData("lastOfferCmd_ly", "ThirdPay");
        ShjManager.putData("lastOfferCmd_key", str2);
        ShjManager.putData("offering_Order_key", str2);
        new ArrayList();
        Shj.getWallet().setLastAddMoneyInfo(str2);
        Order resentOrder = ShjManager.getOrderManager().getResentOrder(3, str);
        if (resentOrder == null) {
            return false;
        }
        resentOrder.setPayId(str2);
        Loger.writeLog("SALES;NET", "order:" + resentOrder);
        MoneyType payType2MoneyType = payType2MoneyType(orderPayType);
        Loger.writeLog("SALES;NET", "moneyType:" + payType2MoneyType);
        OrderArgs orderArgs = resentOrder == null ? new OrderArgs() : resentOrder.getArgs();
        orderArgs.putArg("tradeNo", str2);
        orderArgs.putArg("YC_CMD_OfferGoods", "FALSE");
        orderArgs.putArg("isOfflineOrder", "TRUE");
        orderArgs.putArg("thirdPayName", orderPayType.getName());
        String arg = orderArgs.getArg("offerShelves");
        Loger.writeLog("SALES;NET", "shelf:" + arg);
        String[] split = arg.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
        Loger.writeLog("SALES;NET", "update offerGoodsInfo");
        BatchOfferGoodsInfo batchOfferGoodsInfo = new BatchOfferGoodsInfo();
        if (orderArgs != null) {
            batchOfferGoodsInfo.setOrderArgs(orderArgs);
        }
        batchOfferGoodsInfo.getOrderArgs().putArg("orderId", "" + str2);
        if (resentOrder != null) {
            str2 = resentOrder.getPayId();
        }
        batchOfferGoodsInfo.setRemark(str2);
        batchOfferGoodsInfo.setMoneyType(payType2MoneyType);
        batchOfferGoodsInfo.setShouldSetMoney(true);
        for (String str3 : split) {
            batchOfferGoodsInfo.addShelfItem(Integer.parseInt(str3));
        }
        Loger.writeLog("SALES;NET", "startBatchOfferGoods");
        if (ShjManager.startBatchOfferGoods(batchOfferGoodsInfo, payType2MoneyType)) {
            return true;
        }
        Loger.writeLog("SALES;NET", "report error");
        ShjManager.getGoodsStatusListener().onOfferingGoods_State(-1, OfferState.BusinessStoped.getIndex(), ShjManager.getData(ShjManager.STR_offerGoods_failed).toString(), 1);
        TTSManager.addText(ShjManager.getData(ShjManager.STR_offerGoods_failed).toString());
        ShjManager.tryOfferNextYCCmdGoods();
        return false;
    }
}
