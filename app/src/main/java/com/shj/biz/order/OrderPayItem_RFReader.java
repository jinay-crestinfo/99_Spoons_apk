package com.shj.biz.order;

import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.MoneyType;
import com.shj.biz.ShjManager;
import com.shj.biz.order.OrderPayManager;
import com.shj.device.cardreader.ArmRfReader;
import com.shj.device.lfpos.LfPos;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class OrderPayItem_RFReader extends OrderPayItem_ICCard {
    OrderPayManager.OrderPayResultListener payResultListener;
    static OrderPayItem_RFReader posPayInstance = new OrderPayItem_RFReader();
    private static String lastPayCardSn = "";
    private static long lastPayMoney = 0;
    byte[] card = new byte[4];
    byte[] sn = new byte[4];
    boolean isCardFind = false;
    String strCard = "";
    String strSn = "";
    String optType = "pay";
    long money = 0;
    long count = -1;
    long startTime = 0;
    long timeOut = 0;
    byte findCardResult = 0;
    boolean readMoneySuccess = false;
    boolean paySuccess = false;
    Thread thread = null;

    @Override // com.shj.biz.order.OrderPayItem
    public void buildQrImage(OrderPayManager.QrImageBuildListener qrImageBuildListener) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void onTimer(int i) {
    }

    @Override // com.shj.biz.order.OrderPayItem
    public boolean shuldBuildQrImage() {
        return false;
    }

    public static String getLastPayCardSn() {
        return lastPayCardSn;
    }

    public static void setLastPayCardSn(String str) {
        lastPayCardSn = str;
    }

    void reset() {
        int i = 0;
        while (true) {
            byte[] bArr = this.sn;
            if (i >= bArr.length) {
                break;
            }
            bArr[i] = 0;
            i++;
        }
        int i2 = 0;
        while (true) {
            byte[] bArr2 = this.card;
            if (i2 >= bArr2.length) {
                this.paySuccess = false;
                this.readMoneySuccess = false;
                this.findCardResult = (byte) 0;
                this.timeOut = 0L;
                this.money = 0L;
                this.count = -1L;
                this.strCard = "";
                return;
            }
            bArr2[i2] = 0;
            i2++;
        }
    }

    public static OrderPayItem getInstance() {
        if (posPayInstance == null) {
            posPayInstance = new OrderPayItem_RFReader();
        }
        return posPayInstance;
    }

    public void setPayType(String str) {
        LfPos.setPayType(str + VoiceWakeuperAidl.PARAMS_SEPARATE);
    }

    @Override // com.shj.biz.order.OrderPayItem
    public void queryOrderPayResult(OrderPayManager.OrderPayResultListener orderPayResultListener) {
        this.payResultListener = orderPayResultListener;
        this.paySuccess = false;
        this.isCanceled = false;
        this.optType = "pay";
        reset();
        this.timeOut = ShjManager.getOrderTimeOut() * 1000;
        Thread thread = this.thread;
        if (thread != null) {
            try {
                thread.interrupt();
            } catch (Exception unused) {
            }
        }
        Thread thread2 = new Thread(new Runnable() { // from class: com.shj.biz.order.OrderPayItem_RFReader.1
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                try {
                    OrderPayItem_RFReader.this.isCanceled = false;
                    OrderPayItem_RFReader.this.paySuccess = false;
                    OrderPayItem_RFReader.this.startTime = System.currentTimeMillis();
                    while (!OrderPayItem_RFReader.this.isCanceled && !OrderPayItem_RFReader.this.paySuccess && OrderPayItem_RFReader.this.timeOut - (System.currentTimeMillis() - OrderPayItem_RFReader.this.startTime) > 0) {
                        OrderPayItem_RFReader.this.isCardFind = false;
                        OrderPayItem_RFReader.this.readMoneySuccess = false;
                        ArmRfReader.findCard((byte) 1, (byte) 1, (byte) 1, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.1.1
                            C00491() {
                            }

                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                                if (OrderPayItem_RFReader.this.findCardResult == 0) {
                                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                                    byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                                    OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                                    OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                                    Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                                    OrderPayItem_RFReader.this.isCardFind = true;
                                }
                            }

                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onError(Exception exc) {
                                Loger.writeException("Card", exc);
                            }
                        });
                        if (OrderPayItem_RFReader.this.isCardFind) {
                            ShjManager.getOrderListener().onOrderMessage("读卡成功");
                            ArmRfReader.getMoney(OrderPayItem_RFReader.this.sn, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.1.2
                                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                public void onError(Exception exc) {
                                }

                                AnonymousClass2() {
                                }

                                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                    if (bArr2[4] == 0) {
                                        OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                                        if (bArr2.length >= 15) {
                                            OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                                        }
                                        OrderPayItem_RFReader.this.readMoneySuccess = true;
                                        Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                                    }
                                }
                            });
                            if (!OrderPayItem_RFReader.this.readMoneySuccess) {
                                sleep();
                            } else {
                                long _onCheckICCard = ShjManager.getBizShjListener()._onCheckICCard(OrderPayItem_RFReader.this.strSn, OrderPayItem_RFReader.this.money, OrderPayItem_RFReader.this.optType + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.count);
                                if (_onCheckICCard < 0) {
                                    ShjManager.getOrderListener().onOrderMessage("卡受限");
                                    ShjManager.getStatusListener().onUpdateICCardMessage(OrderPayItem_RFReader.this.strSn, "卡受限");
                                } else if (_onCheckICCard < OrderPayItem_RFReader.this.order.getPrice()) {
                                    ShjManager.getOrderListener().onOrderMessage("可用余额不足");
                                    ShjManager.getStatusListener().onUpdateICCardMessage(OrderPayItem_RFReader.this.strSn, "可用余额不足");
                                } else {
                                    long price = OrderPayItem_RFReader.this.money - OrderPayItem_RFReader.this.order.getPrice();
                                    if (price > 1000) {
                                        price = 1000;
                                    }
                                    if (price > 0) {
                                        ShjManager.getOrderListener().onOrderMessage("正在支付...");
                                        ArmRfReader.setMoney(OrderPayItem_RFReader.this.sn, price, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.1.3
                                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                            public void onError(Exception exc) {
                                            }

                                            AnonymousClass3() {
                                            }

                                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                                if (bArr2[4] == 0) {
                                                    OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 5, 4);
                                                    Loger.writeLog("Card", "count:" + OrderPayItem_RFReader.this.count);
                                                    OrderPayItem_RFReader.this.paySuccess = true;
                                                }
                                            }
                                        });
                                        if (OrderPayItem_RFReader.this.paySuccess) {
                                            ShjManager.getOrderListener().onOrderMessage("支付成功，即将出货...");
                                            OrderPayItem_RFReader.this.order.getArgs().putArg("CardId", OrderPayItem_RFReader.this.strCard);
                                            OrderPayItem_RFReader.this.order.getArgs().putArg("CardSn", OrderPayItem_RFReader.this.strSn);
                                            OrderPayItem_RFReader.this.order.getArgs().putArg("CardBalance", "" + price);
                                            OrderPayItem_RFReader.this.order.getArgs().putArg("CardPayCount", "" + OrderPayItem_RFReader.this.count);
                                            OrderPayItem_RFReader.this.order.getArgs().putArg("CardPayMoney", "" + OrderPayItem_RFReader.this.order.getPrice());
                                            OrderPayItem_RFReader.this.order.setPayId(OrderPayItem_RFReader.this.order.getArgs().getArg("CardId"));
                                            ShjManager.setMoney(MoneyType.ICCard, OrderPayItem_RFReader.this.order.getPrice(), OrderPayItem_RFReader.this.order.getPayId());
                                            OrderPayItem_RFReader.this.payResultListener.onOrderPaySuccess(OrderPayItem_RFReader.this);
                                            return;
                                        }
                                        ShjManager.getOrderListener().onOrderMessage("支付失败，请重新刷卡");
                                        sleep();
                                    } else {
                                        ShjManager.getOrderListener().onOrderMessage("余额不足，请充值!");
                                        sleep();
                                    }
                                }
                            }
                        } else {
                            if (OrderPayItem_RFReader.this.findCardResult == 1) {
                                ShjManager.getOrderListener().onOrderMessage("无效卡");
                            } else {
                                byte b = OrderPayItem_RFReader.this.findCardResult;
                            }
                            sleep();
                        }
                    }
                } catch (Exception unused2) {
                }
            }

            /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$1$1 */
            /* loaded from: classes2.dex */
            class C00491 implements ArmRfReader.CmdResultListener {
                C00491() {
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                    OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                    if (OrderPayItem_RFReader.this.findCardResult == 0) {
                        ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                        ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                        byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                        OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                        OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                        Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                        OrderPayItem_RFReader.this.isCardFind = true;
                    }
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onError(Exception exc) {
                    Loger.writeException("Card", exc);
                }
            }

            /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$1$2 */
            /* loaded from: classes2.dex */
            class AnonymousClass2 implements ArmRfReader.CmdResultListener {
                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onError(Exception exc) {
                }

                AnonymousClass2() {
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                    if (bArr2[4] == 0) {
                        OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                        if (bArr2.length >= 15) {
                            OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                        }
                        OrderPayItem_RFReader.this.readMoneySuccess = true;
                        Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                    }
                }
            }

            /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$1$3 */
            /* loaded from: classes2.dex */
            class AnonymousClass3 implements ArmRfReader.CmdResultListener {
                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onError(Exception exc) {
                }

                AnonymousClass3() {
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                    if (bArr2[4] == 0) {
                        OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 5, 4);
                        Loger.writeLog("Card", "count:" + OrderPayItem_RFReader.this.count);
                        OrderPayItem_RFReader.this.paySuccess = true;
                    }
                }
            }

            void sleep() {
                try {
                    Thread.sleep(1000L);
                } catch (Exception unused2) {
                }
            }
        });
        this.thread = thread2;
        thread2.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                OrderPayItem_RFReader.this.isCanceled = false;
                OrderPayItem_RFReader.this.paySuccess = false;
                OrderPayItem_RFReader.this.startTime = System.currentTimeMillis();
                while (!OrderPayItem_RFReader.this.isCanceled && !OrderPayItem_RFReader.this.paySuccess && OrderPayItem_RFReader.this.timeOut - (System.currentTimeMillis() - OrderPayItem_RFReader.this.startTime) > 0) {
                    OrderPayItem_RFReader.this.isCardFind = false;
                    OrderPayItem_RFReader.this.readMoneySuccess = false;
                    ArmRfReader.findCard((byte) 1, (byte) 1, (byte) 1, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.1.1
                        C00491() {
                        }

                        @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                        public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                            Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                            OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                            if (OrderPayItem_RFReader.this.findCardResult == 0) {
                                ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                                ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                                byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                                OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                                OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                                Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                                OrderPayItem_RFReader.this.isCardFind = true;
                            }
                        }

                        @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                        public void onError(Exception exc) {
                            Loger.writeException("Card", exc);
                        }
                    });
                    if (OrderPayItem_RFReader.this.isCardFind) {
                        ShjManager.getOrderListener().onOrderMessage("读卡成功");
                        ArmRfReader.getMoney(OrderPayItem_RFReader.this.sn, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.1.2
                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onError(Exception exc) {
                            }

                            AnonymousClass2() {
                            }

                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                if (bArr2[4] == 0) {
                                    OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                                    if (bArr2.length >= 15) {
                                        OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                                    }
                                    OrderPayItem_RFReader.this.readMoneySuccess = true;
                                    Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                                }
                            }
                        });
                        if (!OrderPayItem_RFReader.this.readMoneySuccess) {
                            sleep();
                        } else {
                            long _onCheckICCard = ShjManager.getBizShjListener()._onCheckICCard(OrderPayItem_RFReader.this.strSn, OrderPayItem_RFReader.this.money, OrderPayItem_RFReader.this.optType + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.count);
                            if (_onCheckICCard < 0) {
                                ShjManager.getOrderListener().onOrderMessage("卡受限");
                                ShjManager.getStatusListener().onUpdateICCardMessage(OrderPayItem_RFReader.this.strSn, "卡受限");
                            } else if (_onCheckICCard < OrderPayItem_RFReader.this.order.getPrice()) {
                                ShjManager.getOrderListener().onOrderMessage("可用余额不足");
                                ShjManager.getStatusListener().onUpdateICCardMessage(OrderPayItem_RFReader.this.strSn, "可用余额不足");
                            } else {
                                long price = OrderPayItem_RFReader.this.money - OrderPayItem_RFReader.this.order.getPrice();
                                if (price > 1000) {
                                    price = 1000;
                                }
                                if (price > 0) {
                                    ShjManager.getOrderListener().onOrderMessage("正在支付...");
                                    ArmRfReader.setMoney(OrderPayItem_RFReader.this.sn, price, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.1.3
                                        @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                        public void onError(Exception exc) {
                                        }

                                        AnonymousClass3() {
                                        }

                                        @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                        public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                            Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                            if (bArr2[4] == 0) {
                                                OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 5, 4);
                                                Loger.writeLog("Card", "count:" + OrderPayItem_RFReader.this.count);
                                                OrderPayItem_RFReader.this.paySuccess = true;
                                            }
                                        }
                                    });
                                    if (OrderPayItem_RFReader.this.paySuccess) {
                                        ShjManager.getOrderListener().onOrderMessage("支付成功，即将出货...");
                                        OrderPayItem_RFReader.this.order.getArgs().putArg("CardId", OrderPayItem_RFReader.this.strCard);
                                        OrderPayItem_RFReader.this.order.getArgs().putArg("CardSn", OrderPayItem_RFReader.this.strSn);
                                        OrderPayItem_RFReader.this.order.getArgs().putArg("CardBalance", "" + price);
                                        OrderPayItem_RFReader.this.order.getArgs().putArg("CardPayCount", "" + OrderPayItem_RFReader.this.count);
                                        OrderPayItem_RFReader.this.order.getArgs().putArg("CardPayMoney", "" + OrderPayItem_RFReader.this.order.getPrice());
                                        OrderPayItem_RFReader.this.order.setPayId(OrderPayItem_RFReader.this.order.getArgs().getArg("CardId"));
                                        ShjManager.setMoney(MoneyType.ICCard, OrderPayItem_RFReader.this.order.getPrice(), OrderPayItem_RFReader.this.order.getPayId());
                                        OrderPayItem_RFReader.this.payResultListener.onOrderPaySuccess(OrderPayItem_RFReader.this);
                                        return;
                                    }
                                    ShjManager.getOrderListener().onOrderMessage("支付失败，请重新刷卡");
                                    sleep();
                                } else {
                                    ShjManager.getOrderListener().onOrderMessage("余额不足，请充值!");
                                    sleep();
                                }
                            }
                        }
                    } else {
                        if (OrderPayItem_RFReader.this.findCardResult == 1) {
                            ShjManager.getOrderListener().onOrderMessage("无效卡");
                        } else {
                            byte b = OrderPayItem_RFReader.this.findCardResult;
                        }
                        sleep();
                    }
                }
            } catch (Exception unused2) {
            }
        }

        /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$1$1 */
        /* loaded from: classes2.dex */
        class C00491 implements ArmRfReader.CmdResultListener {
            C00491() {
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                if (OrderPayItem_RFReader.this.findCardResult == 0) {
                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                    byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                    OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                    OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                    Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                    OrderPayItem_RFReader.this.isCardFind = true;
                }
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onError(Exception exc) {
                Loger.writeException("Card", exc);
            }
        }

        /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$1$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements ArmRfReader.CmdResultListener {
            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onError(Exception exc) {
            }

            AnonymousClass2() {
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                if (bArr2[4] == 0) {
                    OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                    if (bArr2.length >= 15) {
                        OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                    }
                    OrderPayItem_RFReader.this.readMoneySuccess = true;
                    Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                }
            }
        }

        /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$1$3 */
        /* loaded from: classes2.dex */
        class AnonymousClass3 implements ArmRfReader.CmdResultListener {
            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onError(Exception exc) {
            }

            AnonymousClass3() {
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                if (bArr2[4] == 0) {
                    OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 5, 4);
                    Loger.writeLog("Card", "count:" + OrderPayItem_RFReader.this.count);
                    OrderPayItem_RFReader.this.paySuccess = true;
                }
            }
        }

        void sleep() {
            try {
                Thread.sleep(1000L);
            } catch (Exception unused2) {
            }
        }
    }

    @Override // com.shj.biz.order.OrderPayItem_ICCard
    public void queryCardBanlance(int i) {
        this.optType = "balance";
        reset();
        Thread thread = this.thread;
        if (thread != null) {
            try {
                thread.interrupt();
            } catch (Exception unused) {
            }
        }
        Thread thread2 = new Thread(new Runnable() { // from class: com.shj.biz.order.OrderPayItem_RFReader.2
            final /* synthetic */ int val$timeOut;

            AnonymousClass2(int i2) {
                i = i2;
            }

            @Override // java.lang.Runnable
            public void run() {
                try {
                    OrderPayItem_RFReader.this.isCanceled = false;
                    OrderPayItem_RFReader.this.startTime = System.currentTimeMillis();
                    while (!OrderPayItem_RFReader.this.isCanceled && i - (System.currentTimeMillis() - OrderPayItem_RFReader.this.startTime) > 0) {
                        OrderPayItem_RFReader.this.isCardFind = false;
                        OrderPayItem_RFReader.this.readMoneySuccess = false;
                        ArmRfReader.findCard((byte) 1, (byte) 1, (byte) 1, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.2.1
                            AnonymousClass1() {
                            }

                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                                if (OrderPayItem_RFReader.this.findCardResult == 0) {
                                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                                    byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                                    OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                                    OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                                    Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                                    OrderPayItem_RFReader.this.isCardFind = true;
                                }
                            }

                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onError(Exception exc) {
                                Loger.writeException("Card", exc);
                            }
                        });
                        if (OrderPayItem_RFReader.this.isCardFind) {
                            ShjManager.getOrderListener().onOrderMessage("读卡成功");
                            ArmRfReader.getMoney(OrderPayItem_RFReader.this.sn, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.2.2
                                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                public void onError(Exception exc) {
                                }

                                C00502() {
                                }

                                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                    if (bArr2[4] == 0) {
                                        OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                                        if (bArr2.length >= 15) {
                                            OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                                        }
                                        OrderPayItem_RFReader.this.readMoneySuccess = true;
                                        Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                                    }
                                }
                            });
                            if (!OrderPayItem_RFReader.this.readMoneySuccess) {
                                sleep();
                            } else {
                                long _onCheckICCard = ShjManager.getBizShjListener()._onCheckICCard(OrderPayItem_RFReader.this.strSn, OrderPayItem_RFReader.this.money, OrderPayItem_RFReader.this.optType);
                                if (_onCheckICCard < 0) {
                                    ShjManager.getOrderListener().onOrderMessage("卡受限");
                                    ShjManager.getStatusListener().onUpdateICCardMessage(OrderPayItem_RFReader.this.strSn, "卡受限");
                                    return;
                                }
                                ShjManager.getBizShjListener()._onUpdateICCardMoney(OrderPayItem_RFReader.this.money, "CardBalance;" + OrderPayItem_RFReader.this.strSn + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.strCard + VoiceWakeuperAidl.PARAMS_SEPARATE + _onCheckICCard + ";0;" + OrderPayItem_RFReader.this.count);
                                sleep();
                            }
                        } else {
                            if (OrderPayItem_RFReader.this.findCardResult == 1) {
                                ShjManager.getOrderListener().onOrderMessage("无效卡");
                            } else {
                                byte b = OrderPayItem_RFReader.this.findCardResult;
                            }
                            sleep();
                        }
                    }
                } catch (Exception unused2) {
                }
            }

            /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements ArmRfReader.CmdResultListener {
                AnonymousClass1() {
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                    OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                    if (OrderPayItem_RFReader.this.findCardResult == 0) {
                        ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                        ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                        byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                        OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                        OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                        Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                        OrderPayItem_RFReader.this.isCardFind = true;
                    }
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onError(Exception exc) {
                    Loger.writeException("Card", exc);
                }
            }

            /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$2$2 */
            /* loaded from: classes2.dex */
            class C00502 implements ArmRfReader.CmdResultListener {
                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onError(Exception exc) {
                }

                C00502() {
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                    if (bArr2[4] == 0) {
                        OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                        if (bArr2.length >= 15) {
                            OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                        }
                        OrderPayItem_RFReader.this.readMoneySuccess = true;
                        Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                    }
                }
            }

            void sleep() {
                try {
                    Thread.sleep(1000L);
                } catch (Exception unused2) {
                }
            }
        });
        this.thread = thread2;
        thread2.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements Runnable {
        final /* synthetic */ int val$timeOut;

        AnonymousClass2(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                OrderPayItem_RFReader.this.isCanceled = false;
                OrderPayItem_RFReader.this.startTime = System.currentTimeMillis();
                while (!OrderPayItem_RFReader.this.isCanceled && i - (System.currentTimeMillis() - OrderPayItem_RFReader.this.startTime) > 0) {
                    OrderPayItem_RFReader.this.isCardFind = false;
                    OrderPayItem_RFReader.this.readMoneySuccess = false;
                    ArmRfReader.findCard((byte) 1, (byte) 1, (byte) 1, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.2.1
                        AnonymousClass1() {
                        }

                        @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                        public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                            Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                            OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                            if (OrderPayItem_RFReader.this.findCardResult == 0) {
                                ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                                ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                                byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                                OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                                OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                                Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                                OrderPayItem_RFReader.this.isCardFind = true;
                            }
                        }

                        @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                        public void onError(Exception exc) {
                            Loger.writeException("Card", exc);
                        }
                    });
                    if (OrderPayItem_RFReader.this.isCardFind) {
                        ShjManager.getOrderListener().onOrderMessage("读卡成功");
                        ArmRfReader.getMoney(OrderPayItem_RFReader.this.sn, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.2.2
                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onError(Exception exc) {
                            }

                            C00502() {
                            }

                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                if (bArr2[4] == 0) {
                                    OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                                    if (bArr2.length >= 15) {
                                        OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                                    }
                                    OrderPayItem_RFReader.this.readMoneySuccess = true;
                                    Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                                }
                            }
                        });
                        if (!OrderPayItem_RFReader.this.readMoneySuccess) {
                            sleep();
                        } else {
                            long _onCheckICCard = ShjManager.getBizShjListener()._onCheckICCard(OrderPayItem_RFReader.this.strSn, OrderPayItem_RFReader.this.money, OrderPayItem_RFReader.this.optType);
                            if (_onCheckICCard < 0) {
                                ShjManager.getOrderListener().onOrderMessage("卡受限");
                                ShjManager.getStatusListener().onUpdateICCardMessage(OrderPayItem_RFReader.this.strSn, "卡受限");
                                return;
                            }
                            ShjManager.getBizShjListener()._onUpdateICCardMoney(OrderPayItem_RFReader.this.money, "CardBalance;" + OrderPayItem_RFReader.this.strSn + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.strCard + VoiceWakeuperAidl.PARAMS_SEPARATE + _onCheckICCard + ";0;" + OrderPayItem_RFReader.this.count);
                            sleep();
                        }
                    } else {
                        if (OrderPayItem_RFReader.this.findCardResult == 1) {
                            ShjManager.getOrderListener().onOrderMessage("无效卡");
                        } else {
                            byte b = OrderPayItem_RFReader.this.findCardResult;
                        }
                        sleep();
                    }
                }
            } catch (Exception unused2) {
            }
        }

        /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$2$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements ArmRfReader.CmdResultListener {
            AnonymousClass1() {
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                if (OrderPayItem_RFReader.this.findCardResult == 0) {
                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                    byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                    OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                    OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                    Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                    OrderPayItem_RFReader.this.isCardFind = true;
                }
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onError(Exception exc) {
                Loger.writeException("Card", exc);
            }
        }

        /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$2$2 */
        /* loaded from: classes2.dex */
        class C00502 implements ArmRfReader.CmdResultListener {
            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onError(Exception exc) {
            }

            C00502() {
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                if (bArr2[4] == 0) {
                    OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                    if (bArr2.length >= 15) {
                        OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                    }
                    OrderPayItem_RFReader.this.readMoneySuccess = true;
                    Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                }
            }
        }

        void sleep() {
            try {
                Thread.sleep(1000L);
            } catch (Exception unused2) {
            }
        }
    }

    @Override // com.shj.biz.order.OrderPayItem_ICCard
    public void refund(long j, int i) {
        this.optType = "refund";
        updateCardMoney(j, i);
    }

    @Override // com.shj.biz.order.OrderPayItem_ICCard
    public void topUp(long j, int i) {
        this.optType = "topup";
        updateCardMoney(j, i);
    }

    void updateCardMoney(long j, int i) {
        reset();
        Thread thread = this.thread;
        if (thread != null) {
            try {
                thread.interrupt();
            } catch (Exception unused) {
            }
        }
        Thread thread2 = new Thread(new Runnable() { // from class: com.shj.biz.order.OrderPayItem_RFReader.3
            final /* synthetic */ int val$timeOut;
            final /* synthetic */ long val$updateMoney;

            AnonymousClass3(int i2, long j2) {
                i = i2;
                j = j2;
            }

            @Override // java.lang.Runnable
            public void run() {
                try {
                    OrderPayItem_RFReader.this.isCanceled = false;
                    OrderPayItem_RFReader.this.paySuccess = false;
                    OrderPayItem_RFReader.this.startTime = System.currentTimeMillis();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    while (!OrderPayItem_RFReader.this.isCanceled && !OrderPayItem_RFReader.this.paySuccess && i - (System.currentTimeMillis() - OrderPayItem_RFReader.this.startTime) > 0) {
                        OrderPayItem_RFReader.this.isCardFind = false;
                        OrderPayItem_RFReader.this.readMoneySuccess = false;
                        JSONArray jSONArray = new JSONArray();
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("time", simpleDateFormat.format(new Date()));
                        jSONObject.put("message", "检查卡片");
                        jSONArray.put(jSONObject);
                        ArmRfReader.findCard((byte) 1, (byte) 1, (byte) 1, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.3.1
                            AnonymousClass1() {
                            }

                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                                if (OrderPayItem_RFReader.this.findCardResult == 0) {
                                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                                    byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                                    OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                                    OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                                    Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                                    OrderPayItem_RFReader.this.isCardFind = true;
                                }
                            }

                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onError(Exception exc) {
                                Loger.writeException("Card", exc);
                            }
                        });
                        if (OrderPayItem_RFReader.this.isCardFind) {
                            ShjManager.getOrderListener().onOrderMessage("读卡成功");
                            ArmRfReader.getMoney(OrderPayItem_RFReader.this.sn, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.3.2
                                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                public void onError(Exception exc) {
                                }

                                AnonymousClass2() {
                                }

                                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                    if (bArr2[4] == 0) {
                                        OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                                        if (bArr2.length >= 15) {
                                            OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                                        }
                                        OrderPayItem_RFReader.this.readMoneySuccess = true;
                                        Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                                    }
                                }
                            });
                            if (!OrderPayItem_RFReader.this.readMoneySuccess) {
                                sleep();
                            } else {
                                JSONObject jSONObject2 = new JSONObject();
                                jSONObject2.put("time", simpleDateFormat.format(new Date()));
                                jSONObject2.put("message", "卡内余额查询完成，金额为 " + OrderPayItem_RFReader.this.money);
                                jSONArray.put(jSONObject2);
                                long _onCheckICCard = ShjManager.getBizShjListener()._onCheckICCard(OrderPayItem_RFReader.this.strSn, OrderPayItem_RFReader.this.money, OrderPayItem_RFReader.this.optType + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.count + VoiceWakeuperAidl.PARAMS_SEPARATE + j + VoiceWakeuperAidl.PARAMS_SEPARATE + jSONArray.toString());
                                if (_onCheckICCard < 0) {
                                    ShjManager.getOrderListener().onOrderMessage("卡受限");
                                    ShjManager.getStatusListener().onUpdateICCardMessage(OrderPayItem_RFReader.this.strSn, "卡受限");
                                    return;
                                }
                                ShjManager.setMoney(MoneyType.EAT, 0, "TopUpSN:" + OrderPayItem_RFReader.this.strSn);
                                ShjManager.getOrderListener().onOrderMessage("正在支付...");
                                ArmRfReader.setMoney(OrderPayItem_RFReader.this.sn, j + OrderPayItem_RFReader.this.money, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.3.3
                                    @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                    public void onError(Exception exc) {
                                    }

                                    C00513() {
                                    }

                                    @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                    public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                        Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                        if (bArr2[4] == 0) {
                                            OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 5, 4);
                                            Loger.writeLog("Card", "count:" + OrderPayItem_RFReader.this.count);
                                            OrderPayItem_RFReader.this.paySuccess = true;
                                        }
                                    }
                                });
                                if (OrderPayItem_RFReader.this.paySuccess) {
                                    OrderPayItem_RFReader.this.readMoneySuccess = false;
                                    ArmRfReader.getMoney(OrderPayItem_RFReader.this.sn, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.3.4
                                        @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                        public void onError(Exception exc) {
                                        }

                                        AnonymousClass4() {
                                        }

                                        @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                        public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                            Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                            if (bArr2[4] == 0) {
                                                OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                                                if (bArr2.length >= 15) {
                                                    OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                                                }
                                                OrderPayItem_RFReader.this.readMoneySuccess = true;
                                                Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                                            }
                                        }
                                    });
                                    if (OrderPayItem_RFReader.this.readMoneySuccess) {
                                        ShjManager.getBizShjListener()._onUpdateICCardMoney(OrderPayItem_RFReader.this.money, OrderPayItem_RFReader.this.optType + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.strSn + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.strCard + VoiceWakeuperAidl.PARAMS_SEPARATE + (_onCheckICCard + j) + VoiceWakeuperAidl.PARAMS_SEPARATE + j + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.count);
                                        return;
                                    }
                                }
                                sleep();
                            }
                        } else {
                            if (OrderPayItem_RFReader.this.findCardResult == 1) {
                                ShjManager.getOrderListener().onOrderMessage("无效卡");
                            } else {
                                byte b = OrderPayItem_RFReader.this.findCardResult;
                            }
                            sleep();
                        }
                    }
                } catch (Exception unused2) {
                }
            }

            /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$3$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements ArmRfReader.CmdResultListener {
                AnonymousClass1() {
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                    OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                    if (OrderPayItem_RFReader.this.findCardResult == 0) {
                        ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                        ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                        byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                        OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                        OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                        Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                        OrderPayItem_RFReader.this.isCardFind = true;
                    }
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onError(Exception exc) {
                    Loger.writeException("Card", exc);
                }
            }

            /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$3$2 */
            /* loaded from: classes2.dex */
            class AnonymousClass2 implements ArmRfReader.CmdResultListener {
                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onError(Exception exc) {
                }

                AnonymousClass2() {
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                    if (bArr2[4] == 0) {
                        OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                        if (bArr2.length >= 15) {
                            OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                        }
                        OrderPayItem_RFReader.this.readMoneySuccess = true;
                        Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                    }
                }
            }

            /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$3$3 */
            /* loaded from: classes2.dex */
            class C00513 implements ArmRfReader.CmdResultListener {
                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onError(Exception exc) {
                }

                C00513() {
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                    if (bArr2[4] == 0) {
                        OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 5, 4);
                        Loger.writeLog("Card", "count:" + OrderPayItem_RFReader.this.count);
                        OrderPayItem_RFReader.this.paySuccess = true;
                    }
                }
            }

            /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$3$4 */
            /* loaded from: classes2.dex */
            class AnonymousClass4 implements ArmRfReader.CmdResultListener {
                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onError(Exception exc) {
                }

                AnonymousClass4() {
                }

                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                    if (bArr2[4] == 0) {
                        OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                        if (bArr2.length >= 15) {
                            OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                        }
                        OrderPayItem_RFReader.this.readMoneySuccess = true;
                        Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                    }
                }
            }

            void sleep() {
                try {
                    Thread.sleep(1000L);
                } catch (Exception unused2) {
                }
            }
        });
        this.thread = thread2;
        thread2.start();
    }

    /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Runnable {
        final /* synthetic */ int val$timeOut;
        final /* synthetic */ long val$updateMoney;

        AnonymousClass3(int i2, long j2) {
            i = i2;
            j = j2;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                OrderPayItem_RFReader.this.isCanceled = false;
                OrderPayItem_RFReader.this.paySuccess = false;
                OrderPayItem_RFReader.this.startTime = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                while (!OrderPayItem_RFReader.this.isCanceled && !OrderPayItem_RFReader.this.paySuccess && i - (System.currentTimeMillis() - OrderPayItem_RFReader.this.startTime) > 0) {
                    OrderPayItem_RFReader.this.isCardFind = false;
                    OrderPayItem_RFReader.this.readMoneySuccess = false;
                    JSONArray jSONArray = new JSONArray();
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("time", simpleDateFormat.format(new Date()));
                    jSONObject.put("message", "检查卡片");
                    jSONArray.put(jSONObject);
                    ArmRfReader.findCard((byte) 1, (byte) 1, (byte) 1, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.3.1
                        AnonymousClass1() {
                        }

                        @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                        public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                            Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                            OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                            if (OrderPayItem_RFReader.this.findCardResult == 0) {
                                ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                                ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                                byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                                OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                                OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                                Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                                OrderPayItem_RFReader.this.isCardFind = true;
                            }
                        }

                        @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                        public void onError(Exception exc) {
                            Loger.writeException("Card", exc);
                        }
                    });
                    if (OrderPayItem_RFReader.this.isCardFind) {
                        ShjManager.getOrderListener().onOrderMessage("读卡成功");
                        ArmRfReader.getMoney(OrderPayItem_RFReader.this.sn, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.3.2
                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onError(Exception exc) {
                            }

                            AnonymousClass2() {
                            }

                            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                if (bArr2[4] == 0) {
                                    OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                                    if (bArr2.length >= 15) {
                                        OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                                    }
                                    OrderPayItem_RFReader.this.readMoneySuccess = true;
                                    Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                                }
                            }
                        });
                        if (!OrderPayItem_RFReader.this.readMoneySuccess) {
                            sleep();
                        } else {
                            JSONObject jSONObject2 = new JSONObject();
                            jSONObject2.put("time", simpleDateFormat.format(new Date()));
                            jSONObject2.put("message", "卡内余额查询完成，金额为 " + OrderPayItem_RFReader.this.money);
                            jSONArray.put(jSONObject2);
                            long _onCheckICCard = ShjManager.getBizShjListener()._onCheckICCard(OrderPayItem_RFReader.this.strSn, OrderPayItem_RFReader.this.money, OrderPayItem_RFReader.this.optType + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.count + VoiceWakeuperAidl.PARAMS_SEPARATE + j + VoiceWakeuperAidl.PARAMS_SEPARATE + jSONArray.toString());
                            if (_onCheckICCard < 0) {
                                ShjManager.getOrderListener().onOrderMessage("卡受限");
                                ShjManager.getStatusListener().onUpdateICCardMessage(OrderPayItem_RFReader.this.strSn, "卡受限");
                                return;
                            }
                            ShjManager.setMoney(MoneyType.EAT, 0, "TopUpSN:" + OrderPayItem_RFReader.this.strSn);
                            ShjManager.getOrderListener().onOrderMessage("正在支付...");
                            ArmRfReader.setMoney(OrderPayItem_RFReader.this.sn, j + OrderPayItem_RFReader.this.money, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.3.3
                                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                public void onError(Exception exc) {
                                }

                                C00513() {
                                }

                                @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                    Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                    if (bArr2[4] == 0) {
                                        OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 5, 4);
                                        Loger.writeLog("Card", "count:" + OrderPayItem_RFReader.this.count);
                                        OrderPayItem_RFReader.this.paySuccess = true;
                                    }
                                }
                            });
                            if (OrderPayItem_RFReader.this.paySuccess) {
                                OrderPayItem_RFReader.this.readMoneySuccess = false;
                                ArmRfReader.getMoney(OrderPayItem_RFReader.this.sn, new ArmRfReader.CmdResultListener() { // from class: com.shj.biz.order.OrderPayItem_RFReader.3.4
                                    @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                    public void onError(Exception exc) {
                                    }

                                    AnonymousClass4() {
                                    }

                                    @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
                                    public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                                        Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                                        if (bArr2[4] == 0) {
                                            OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                                            if (bArr2.length >= 15) {
                                                OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                                            }
                                            OrderPayItem_RFReader.this.readMoneySuccess = true;
                                            Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                                        }
                                    }
                                });
                                if (OrderPayItem_RFReader.this.readMoneySuccess) {
                                    ShjManager.getBizShjListener()._onUpdateICCardMoney(OrderPayItem_RFReader.this.money, OrderPayItem_RFReader.this.optType + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.strSn + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.strCard + VoiceWakeuperAidl.PARAMS_SEPARATE + (_onCheckICCard + j) + VoiceWakeuperAidl.PARAMS_SEPARATE + j + VoiceWakeuperAidl.PARAMS_SEPARATE + OrderPayItem_RFReader.this.count);
                                    return;
                                }
                            }
                            sleep();
                        }
                    } else {
                        if (OrderPayItem_RFReader.this.findCardResult == 1) {
                            ShjManager.getOrderListener().onOrderMessage("无效卡");
                        } else {
                            byte b = OrderPayItem_RFReader.this.findCardResult;
                        }
                        sleep();
                    }
                }
            } catch (Exception unused2) {
            }
        }

        /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$3$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements ArmRfReader.CmdResultListener {
            AnonymousClass1() {
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                OrderPayItem_RFReader.this.findCardResult = bArr2[4];
                if (OrderPayItem_RFReader.this.findCardResult == 0) {
                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.sn, bArr2, 5, 0, 4);
                    ObjectHelper.updateBytes(OrderPayItem_RFReader.this.card, bArr2, 6, 0, 4);
                    byte[] reverseBytes = ObjectHelper.reverseBytes(OrderPayItem_RFReader.this.card);
                    OrderPayItem_RFReader.this.strCard = String.format("%08d", Long.valueOf(ObjectHelper.longFromBytes(reverseBytes, 0, reverseBytes.length)));
                    OrderPayItem_RFReader.this.strSn = ObjectHelper.longFromBytes(OrderPayItem_RFReader.this.sn, 0, OrderPayItem_RFReader.this.sn.length) + "";
                    Loger.writeLog("Card", "SN:" + OrderPayItem_RFReader.this.strSn + " Card:" + OrderPayItem_RFReader.this.strCard);
                    OrderPayItem_RFReader.this.isCardFind = true;
                }
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onError(Exception exc) {
                Loger.writeException("Card", exc);
            }
        }

        /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$3$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements ArmRfReader.CmdResultListener {
            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onError(Exception exc) {
            }

            AnonymousClass2() {
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                if (bArr2[4] == 0) {
                    OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                    if (bArr2.length >= 15) {
                        OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                    }
                    OrderPayItem_RFReader.this.readMoneySuccess = true;
                    Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                }
            }
        }

        /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$3$3 */
        /* loaded from: classes2.dex */
        class C00513 implements ArmRfReader.CmdResultListener {
            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onError(Exception exc) {
            }

            C00513() {
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                if (bArr2[4] == 0) {
                    OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 5, 4);
                    Loger.writeLog("Card", "count:" + OrderPayItem_RFReader.this.count);
                    OrderPayItem_RFReader.this.paySuccess = true;
                }
            }
        }

        /* renamed from: com.shj.biz.order.OrderPayItem_RFReader$3$4 */
        /* loaded from: classes2.dex */
        class AnonymousClass4 implements ArmRfReader.CmdResultListener {
            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onError(Exception exc) {
            }

            AnonymousClass4() {
            }

            @Override // com.shj.device.cardreader.ArmRfReader.CmdResultListener
            public void onAcceptResult(byte[] bArr, byte[] bArr2) {
                Loger.writeLog("Card", ObjectHelper.hex2String(bArr, bArr.length) + " > " + ObjectHelper.hex2String(bArr2, bArr2.length));
                if (bArr2[4] == 0) {
                    OrderPayItem_RFReader.this.money = ObjectHelper.longFromBytes(bArr2, 5, 4);
                    if (bArr2.length >= 15) {
                        OrderPayItem_RFReader.this.count = ObjectHelper.longFromBytes_reverse(bArr2, 9, 4);
                    }
                    OrderPayItem_RFReader.this.readMoneySuccess = true;
                    Loger.writeLog("Card", "money:" + OrderPayItem_RFReader.this.money + " count:" + OrderPayItem_RFReader.this.count);
                }
            }
        }

        void sleep() {
            try {
                Thread.sleep(1000L);
            } catch (Exception unused2) {
            }
        }
    }

    @Override // com.shj.biz.order.OrderPayItem
    public boolean cancel() {
        this.payResultListener = null;
        if (this.paySuccess) {
            return false;
        }
        this.isCanceled = true;
        reset();
        Thread thread = this.thread;
        if (thread != null) {
            try {
                thread.interrupt();
            } catch (Exception unused) {
            }
        }
        ArmRfReader.cancel();
        return true;
    }
}
