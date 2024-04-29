package com.shj;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.PropertiesUtil;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.anno.XYClass;
import com.oysb.utils.cache.CacheHelper;
import com.shj.command.Command;
import com.shj.command.CommandError;
import com.shj.command.CommandManager;
import com.shj.command.CommandStatusListener;
import com.shj.command.Command_Down_ReportAcceptMoney;
import com.shj.command.Command_Down_ReportCoinBalance;
import com.shj.command.Command_Down_ReportGoodsCode;
import com.shj.command.Command_Down_ReportPaperMoneyBalance;
import com.shj.command.Command_Down_ReportPosInfo;
import com.shj.command.Command_Down_ReportReseted;
import com.shj.command.Command_Down_ReportSelGoodsStatus;
import com.shj.command.Command_Down_ReportSettingFinished;
import com.shj.command.Command_Down_ReportShelfGoodsCount;
import com.shj.command.Command_Down_ReportShelfGoodsPrice;
import com.shj.command.Command_Down_ReportStatus;
import com.shj.command.Command_Down_ReportTemperature;
import com.shj.command.Command_Down_WaitCommand;
import com.shj.command.Command_Up_UnSelectGoods;
import com.shj.commandV2.CommandV2;
import com.shj.commandV2.CommandV2_Down_AcceptMoney;
import com.shj.commandV2.CommandV2_Down_Change;
import com.shj.commandV2.CommandV2_Down_CommandAnswer;
import com.shj.commandV2.CommandV2_Down_CurrentMoney;
import com.shj.commandV2.CommandV2_Down_ICCardInfo;
import com.shj.commandV2.CommandV2_Down_ICCardPay;
import com.shj.commandV2.CommandV2_Down_Notice;
import com.shj.commandV2.CommandV2_Down_OfferGoods;
import com.shj.commandV2.CommandV2_Down_OneKeyFull;
import com.shj.commandV2.CommandV2_Down_PickerState;
import com.shj.commandV2.CommandV2_Down_PosInfo;
import com.shj.commandV2.CommandV2_Down_SelectGoods;
import com.shj.commandV2.CommandV2_Down_Setting;
import com.shj.commandV2.CommandV2_Down_ShelfDoorStatus;
import com.shj.commandV2.CommandV2_Down_ShelfInfo;
import com.shj.commandV2.CommandV2_Down_ShelfStatus;
import com.shj.commandV2.CommandV2_Down_ShjStatus;
import com.shj.commandV2.CommandV2_Down_Syn;
import com.shj.commandV2.CommandV2_Down_UpdateShelfCapacity;
import com.shj.commandV2.CommandV2_Down_UpdateShelfGoodsCode;
import com.shj.commandV2.CommandV2_Down_UpdateShelfGoodsCount;
import com.shj.commandV2.CommandV2_Down_UpdateShelfPrice;
import com.shj.commandV2.CommandV2_Down_VMCStatus;
import com.shj.commandV2.CommandV2_Down_Version;
import com.shj.commandV2.CommandV2_Up_Empty;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.commandV2.CommandV2_Up_Test;
import com.shj.commandV2.CommandV2_Up_UnSelectGoods;
import com.shj.device.Machine;
import com.shj.device.Picker;
import com.shj.device.VMCStatus;
import com.shj.device.Wallet;
import com.shj.device.bluetooth.Bluetooth;
import com.shj.service.ShjVMCSerialProcessorV1;
import com.shj.service.ShjVMCServiceEx;
import com.xyshj.database.setting.SettingType;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class Shj {
    private static final long OFFER_GOODS_TIME_OUT = 50000;
    private static boolean androidSystemTimeError = false;
    private static Bluetooth bluetooth = null;
    public static int devicesType = 0;
    public static int machineMode = 0;
    public static final String propertyFile = "shj.properties";
    private static Shj shj;
    private static ShjListener shjListener;
    private static WeakReference<Context> wkContext;
    private static Wallet wallet = new Wallet();
    private static HashMap<Integer, Machine> mapMachines = new HashMap<>();
    private static List<Machine> machinesList = new ArrayList();
    private static HashMap<Integer, ShelfInfo> shelfGoodsMap = new HashMap<>();
    private static HashMap<String, HashMap<Integer, ShelfInfo>> codeGoodsMap = new HashMap<>();
    private static HashMap<String, ShelfGroupInfo> groupsMap = new HashMap<>();
    private static List<Picker> aryPickers = new ArrayList();
    private static int selectedShelf = -1;
    private static int lastDrivedShelf = -1;
    private static int selectShelfTmp = -1;
    private static int lastSelectShelf = -1;
    private static int lastOfferedShelf = -1;
    private static int resetStatus = 0;
    private static String machineId = "";
    private static String testMachineId = "";
    private static String machineBoardVersion = "";
    private static int version = 2;
    private static String comPath = "/dev/ttyS1";
    private static long comBaudrate = 57600;
    private static boolean isDebug = false;
    private static long lastOfferGoodsTime = 0;
    private static Boolean isOfferingGoods = false;
    private static Boolean isHfj = false;
    private static boolean isBatchJobRunning = false;
    private static int goodsPickerDoorState = 0;
    private static long lastGoodsPickerDoorStateUpdateTime = 0;
    private static int machineType = 0;
    private static long lastDownStateTime = Long.MAX_VALUE;
    private static long lastFindPopleTime = 0;
    static Timer resetTimer = null;
    private static Timer stateCheckTimer = null;
    static Timer debugTimer = null;
    static int debugStep = 0;
    static int debugMachineType = 0;
    static int debugShelf = -1;
    static int offerShelf = -1;
    private static boolean resetFinished = false;
    private static int debugOfferDiviceError = 0;
    private static boolean debugOfferGoodsError = false;
    private static boolean debugShelfStopSale = false;
    private static boolean debugBlockOfferGoods = false;
    private static boolean debugNoGoodsOnShelf = false;
    private static boolean hasPeopleFindDevice = false;
    private static boolean stoped = false;
    private static boolean logSerialDetail = false;
    private static boolean debugOfferShelfByThirdApi = false;
    private static VMCStatus shjState = VMCStatus.Normal;
    private static long shjStateUpdateTime = 0;
    private static int lastOfferGoodsPicker = 0;
    private static boolean storeGoodsInfoInVMC = true;
    static boolean isDebugOfferringGoods = false;
    static boolean offerSuccessTmp = false;
    static boolean offerErrorReported = false;
    static int latestOfferErroCode = 0;
    static boolean needCheckOfferStatus = true;
    static int checkOfferStatusLong = 6000;
    static long lastCheckTime = 0;
    private static HashMap<Integer, OnCommandAnswerListener> commandMap = new HashMap<>();
    private static HashMap<Integer, ShjGoodsSetting> goodsSetMap = new HashMap<>();

    public static void onUpdatePosInfo(String str) {
    }

    private Shj() {
    }

    public static void setStoreGoodsInfoInVMC(boolean z) {
        storeGoodsInfoInVMC = z;
    }

    public static boolean isStoreGoodsInfoInVMC() {
        return storeGoodsInfoInVMC;
    }

    public static Shj getInstance() {
        if (shj == null) {
            synchronized (Shj.class) {
                if (shj == null) {
                    shj = new Shj();
                }
            }
        }
        return shj;
    }

    public static Shj getInstance(Context context) {
        if (shj == null) {
            synchronized (Shj.class) {
                if (shj == null) {
                    shj = new Shj();
                    wkContext = new WeakReference<>(context);
                }
            }
        }
        return shj;
    }

    public static void debugOfferGoodsError(boolean z) {
        debugOfferGoodsError = z;
    }

    public static void setDebugShelfStopSale(boolean z) {
        debugShelfStopSale = z;
    }

    public static boolean isDebugShelfStopSale() {
        return debugShelfStopSale;
    }

    public static void setDebugBlockOfferGoods(boolean z) {
        debugBlockOfferGoods = z;
    }

    public static boolean isDebugBlockOfferGoods() {
        return debugBlockOfferGoods;
    }

    public static void setDebugNoGoodsOnShelf(boolean z) {
        debugNoGoodsOnShelf = z;
    }

    public static boolean isDebugNoGoodsOnShelf() {
        return debugNoGoodsOnShelf;
    }

    public static void setLogSerialDetail(boolean z) {
        logSerialDetail = z;
    }

    public static boolean shouldLogSerialDetail() {
        return logSerialDetail;
    }

    public static boolean isOfferingGoods() {
        boolean z;
        synchronized (isOfferingGoods) {
            z = isOfferingGoods.booleanValue() && System.currentTimeMillis() - lastOfferGoodsTime < OFFER_GOODS_TIME_OUT;
        }
        return z;
    }

    private static void setIsOfferingGoods(boolean z) {
        synchronized (isOfferingGoods) {
            isOfferingGoods = Boolean.valueOf(z);
            if (!z) {
                isDebugOfferringGoods = false;
            }
        }
    }

    public static void debugOfferDivicError(int i) {
        debugOfferDiviceError = i;
    }

    public static void setDebugMachineType(int i) {
        debugMachineType = i;
    }

    public static boolean isVMCConnected() {
        long currentTimeMillis = System.currentTimeMillis() - lastDownStateTime;
        return currentTimeMillis >= 0 && currentTimeMillis < 300000;
    }

    public static void setDebugShelf(int i) {
        debugShelf = i;
    }

    public static void setAndroidSystemTimeError(boolean z) {
        androidSystemTimeError = z;
    }

    public static boolean getAndroidSystemTimeError() {
        return androidSystemTimeError;
    }

    public static void setDebugOfferShelfByThirdApi(boolean z) {
        debugOfferShelfByThirdApi = z;
    }

    public static void debugOfferGoods(int i, boolean z) {
        if (debugOfferShelfByThirdApi) {
            isDebugOfferringGoods = true;
            debugShelf = i;
            offerShelf = i;
            shjListener._onNeedDriverShelf(i);
            return;
        }
        if (isDebug()) {
            isDebugOfferringGoods = true;
            debugShelf = i;
            offerShelf = i;
            debugStep = 0;
            Timer timer = debugTimer;
            if (timer != null) {
                timer.cancel();
                debugTimer = null;
            }
            Timer timer2 = new Timer();
            debugTimer = timer2;
            timer2.schedule(new TimerTask() { // from class: com.shj.Shj.1
                boolean isLooping = false;

                AnonymousClass1() {
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (this.isLooping) {
                        return;
                    }
                    this.isLooping = true;
                    try {
                        if (Shj.debugMachineType == 0) {
                            try {
                                if (Shj.debugStep == 0) {
                                    Shj.onOfferGoods(Shj.offerShelf, OfferState.Offering, 0);
                                    Loger.writeLog("SHJ", "" + OfferState.Offering + ":" + Shj.offerShelf + "------");
                                } else if (Shj.debugStep == 4) {
                                    ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(Shj.offerShelf));
                                    int intValue = Shj.getWallet().getCatchMoney().intValue();
                                    if (shelfInfo.getStatus().intValue() == 0 && Math.random() > 0.75d) {
                                        OfferState offerState = OfferState.OfferSuccess;
                                    } else if (Math.random() > 0.25d) {
                                        OfferState offerState2 = OfferState.Blocked;
                                    } else {
                                        OfferState offerState3 = OfferState.LiftUpError;
                                    }
                                    OfferState offerState4 = OfferState.Unkonw;
                                    offerState4.setIndex((int) ((Math.random() * 100.0d) + 500.0d));
                                    try {
                                        int intValue2 = intValue - Shj.getShelfInfo(Integer.valueOf(Shj.offerShelf)).getPrice().intValue();
                                        if (intValue2 < 0) {
                                            intValue2 = 0;
                                        }
                                        Shj.onResetCurrentMoney(intValue2, false);
                                        Shj.onOfferGoods(Shj.offerShelf, offerState4, 0);
                                        Loger.writeLog("SHJ", "" + OfferState.Offering + ":" + Shj.offerShelf + ":" + offerState4 + "------");
                                        Shj.onDeselectGoodsOnShelf(Integer.valueOf(Shj.offerShelf));
                                        if (offerState4 == OfferState.OfferSuccess) {
                                            Shj.onUpdateGoodsCount(Integer.valueOf(Shj.offerShelf), Integer.valueOf(shelfInfo.getGoodsCount().intValue() - 1));
                                        }
                                        if (offerState4 == OfferState.BeltCheckError || offerState4 == OfferState.LiftUpError) {
                                            Shj.onOfferGoods(Shj.offerShelf, OfferState.BusinessStoped, 0);
                                        }
                                    } catch (Exception e) {
                                        Loger.writeException("SHJ", e);
                                    }
                                    if (Shj.getWallet().getLastAddType() != MoneyType.Coin && Shj.getWallet().getLastAddType() != MoneyType.Paper) {
                                        Shj.onUpdateCoinBalance(0);
                                        Shj.onUpdatePaperMoneyBalance(0);
                                    } else {
                                        Shj.onResetCurrentMoney(intValue, false);
                                    }
                                    Shj.offerShelf = -1;
                                    Shj.debugShelf = -1;
                                    Shj.isDebugOfferringGoods = false;
                                    Shj.debugTimer.cancel();
                                    Shj.debugTimer = null;
                                }
                            } catch (Exception unused) {
                            }
                        } else if (Shj.debugMachineType == 1) {
                            if (Shj.debugStep == 0) {
                                Shj.onOfferGoods(Shj.offerShelf, OfferState.LiftUping, 0);
                            } else if (Shj.debugStep == 2) {
                                Shj.onOfferGoods(Shj.offerShelf, OfferState.Offering, 0);
                            } else if (Shj.debugStep == 4) {
                                Shj.onOfferGoods(Shj.offerShelf, OfferState.LiftDowning, 0);
                            } else if (Shj.debugStep == 5) {
                                Shj.onOfferGoods(0, OfferState.WBLClosingFrontDoor, 0);
                            } else if (Shj.debugStep == 6) {
                                Shj.onOfferGoods(0, OfferState.Push2WBL, 0);
                            } else if (Shj.debugStep == 7) {
                                Shj.onOfferGoods(0, OfferState.WBLClosingBackDoor, 0);
                            } else if (Shj.debugStep == 8) {
                                Shj.onOfferGoods(0, OfferState.WBLRunning, 0);
                            } else if (Shj.debugStep == 9) {
                                Shj.onOfferGoods(30, OfferState.WBLTimer, 0);
                            } else if (Shj.debugStep == 10) {
                                Shj.onOfferGoods(27, OfferState.WBLTimer, 0);
                            } else if (Shj.debugStep == 11) {
                                Shj.onOfferGoods(24, OfferState.WBLTimer, 0);
                            } else if (Shj.debugStep == 12) {
                                Shj.onOfferGoods(21, OfferState.WBLTimer, 0);
                            } else if (Shj.debugStep == 13) {
                                Shj.onOfferGoods(18, OfferState.WBLTimer, 0);
                            } else if (Shj.debugStep == 14) {
                                Shj.onOfferGoods(15, OfferState.WBLTimer, 0);
                            } else if (Shj.debugStep == 15) {
                                Shj.onOfferGoods(12, OfferState.WBLTimer, 0);
                            } else if (Shj.debugStep == 16) {
                                Shj.onOfferGoods(9, OfferState.WBLTimer, 0);
                            } else if (Shj.debugStep == 17) {
                                Shj.onOfferGoods(6, OfferState.WBLTimer, 0);
                            } else if (Shj.debugStep == 18) {
                                Shj.onOfferGoods(3, OfferState.WBLTimer, 0);
                            } else if (Shj.debugStep == 19) {
                                Shj.onOfferGoods(1, OfferState.WBLTimer, 0);
                            } else if (Shj.debugStep == 20) {
                                Shj.onOfferGoods(Shj.offerShelf, OfferState.WBLOpeningFrontDoor, 0);
                            } else if (Shj.debugStep == 21) {
                                Shj.onOfferGoods(Shj.offerShelf, OfferState.WBLWaitting2PickGoods, 0);
                                Shj.onUpdateGoodsCount(Integer.valueOf(Shj.offerShelf), Integer.valueOf(Shj.getShelfInfo(Integer.valueOf(Shj.offerShelf)).getGoodsCount().intValue() - 1));
                                int intValue3 = Shj.getWallet().getCatchMoney().intValue() - Shj.getShelfInfo(Integer.valueOf(Shj.offerShelf)).getPrice().intValue();
                                if (intValue3 < 0) {
                                    intValue3 = 0;
                                }
                                Shj.onResetCurrentMoney(intValue3, false);
                                Shj.onUpdateCoinBalance(0);
                                Shj.onUpdatePaperMoneyBalance(0);
                                Shj.offerShelf = -1;
                                Shj.debugShelf = -1;
                                Shj.debugTimer.cancel();
                                Shj.debugTimer = null;
                            }
                        }
                        Shj.debugStep++;
                    } catch (Exception unused2) {
                    }
                    this.isLooping = false;
                }
            }, 0L, 1000L);
        }
    }

    /* renamed from: com.shj.Shj$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        boolean isLooping = false;

        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (this.isLooping) {
                return;
            }
            this.isLooping = true;
            try {
                if (Shj.debugMachineType == 0) {
                    try {
                        if (Shj.debugStep == 0) {
                            Shj.onOfferGoods(Shj.offerShelf, OfferState.Offering, 0);
                            Loger.writeLog("SHJ", "" + OfferState.Offering + ":" + Shj.offerShelf + "------");
                        } else if (Shj.debugStep == 4) {
                            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(Shj.offerShelf));
                            int intValue = Shj.getWallet().getCatchMoney().intValue();
                            if (shelfInfo.getStatus().intValue() == 0 && Math.random() > 0.75d) {
                                OfferState offerState = OfferState.OfferSuccess;
                            } else if (Math.random() > 0.25d) {
                                OfferState offerState2 = OfferState.Blocked;
                            } else {
                                OfferState offerState3 = OfferState.LiftUpError;
                            }
                            OfferState offerState4 = OfferState.Unkonw;
                            offerState4.setIndex((int) ((Math.random() * 100.0d) + 500.0d));
                            try {
                                int intValue2 = intValue - Shj.getShelfInfo(Integer.valueOf(Shj.offerShelf)).getPrice().intValue();
                                if (intValue2 < 0) {
                                    intValue2 = 0;
                                }
                                Shj.onResetCurrentMoney(intValue2, false);
                                Shj.onOfferGoods(Shj.offerShelf, offerState4, 0);
                                Loger.writeLog("SHJ", "" + OfferState.Offering + ":" + Shj.offerShelf + ":" + offerState4 + "------");
                                Shj.onDeselectGoodsOnShelf(Integer.valueOf(Shj.offerShelf));
                                if (offerState4 == OfferState.OfferSuccess) {
                                    Shj.onUpdateGoodsCount(Integer.valueOf(Shj.offerShelf), Integer.valueOf(shelfInfo.getGoodsCount().intValue() - 1));
                                }
                                if (offerState4 == OfferState.BeltCheckError || offerState4 == OfferState.LiftUpError) {
                                    Shj.onOfferGoods(Shj.offerShelf, OfferState.BusinessStoped, 0);
                                }
                            } catch (Exception e) {
                                Loger.writeException("SHJ", e);
                            }
                            if (Shj.getWallet().getLastAddType() != MoneyType.Coin && Shj.getWallet().getLastAddType() != MoneyType.Paper) {
                                Shj.onUpdateCoinBalance(0);
                                Shj.onUpdatePaperMoneyBalance(0);
                            } else {
                                Shj.onResetCurrentMoney(intValue, false);
                            }
                            Shj.offerShelf = -1;
                            Shj.debugShelf = -1;
                            Shj.isDebugOfferringGoods = false;
                            Shj.debugTimer.cancel();
                            Shj.debugTimer = null;
                        }
                    } catch (Exception unused) {
                    }
                } else if (Shj.debugMachineType == 1) {
                    if (Shj.debugStep == 0) {
                        Shj.onOfferGoods(Shj.offerShelf, OfferState.LiftUping, 0);
                    } else if (Shj.debugStep == 2) {
                        Shj.onOfferGoods(Shj.offerShelf, OfferState.Offering, 0);
                    } else if (Shj.debugStep == 4) {
                        Shj.onOfferGoods(Shj.offerShelf, OfferState.LiftDowning, 0);
                    } else if (Shj.debugStep == 5) {
                        Shj.onOfferGoods(0, OfferState.WBLClosingFrontDoor, 0);
                    } else if (Shj.debugStep == 6) {
                        Shj.onOfferGoods(0, OfferState.Push2WBL, 0);
                    } else if (Shj.debugStep == 7) {
                        Shj.onOfferGoods(0, OfferState.WBLClosingBackDoor, 0);
                    } else if (Shj.debugStep == 8) {
                        Shj.onOfferGoods(0, OfferState.WBLRunning, 0);
                    } else if (Shj.debugStep == 9) {
                        Shj.onOfferGoods(30, OfferState.WBLTimer, 0);
                    } else if (Shj.debugStep == 10) {
                        Shj.onOfferGoods(27, OfferState.WBLTimer, 0);
                    } else if (Shj.debugStep == 11) {
                        Shj.onOfferGoods(24, OfferState.WBLTimer, 0);
                    } else if (Shj.debugStep == 12) {
                        Shj.onOfferGoods(21, OfferState.WBLTimer, 0);
                    } else if (Shj.debugStep == 13) {
                        Shj.onOfferGoods(18, OfferState.WBLTimer, 0);
                    } else if (Shj.debugStep == 14) {
                        Shj.onOfferGoods(15, OfferState.WBLTimer, 0);
                    } else if (Shj.debugStep == 15) {
                        Shj.onOfferGoods(12, OfferState.WBLTimer, 0);
                    } else if (Shj.debugStep == 16) {
                        Shj.onOfferGoods(9, OfferState.WBLTimer, 0);
                    } else if (Shj.debugStep == 17) {
                        Shj.onOfferGoods(6, OfferState.WBLTimer, 0);
                    } else if (Shj.debugStep == 18) {
                        Shj.onOfferGoods(3, OfferState.WBLTimer, 0);
                    } else if (Shj.debugStep == 19) {
                        Shj.onOfferGoods(1, OfferState.WBLTimer, 0);
                    } else if (Shj.debugStep == 20) {
                        Shj.onOfferGoods(Shj.offerShelf, OfferState.WBLOpeningFrontDoor, 0);
                    } else if (Shj.debugStep == 21) {
                        Shj.onOfferGoods(Shj.offerShelf, OfferState.WBLWaitting2PickGoods, 0);
                        Shj.onUpdateGoodsCount(Integer.valueOf(Shj.offerShelf), Integer.valueOf(Shj.getShelfInfo(Integer.valueOf(Shj.offerShelf)).getGoodsCount().intValue() - 1));
                        int intValue3 = Shj.getWallet().getCatchMoney().intValue() - Shj.getShelfInfo(Integer.valueOf(Shj.offerShelf)).getPrice().intValue();
                        if (intValue3 < 0) {
                            intValue3 = 0;
                        }
                        Shj.onResetCurrentMoney(intValue3, false);
                        Shj.onUpdateCoinBalance(0);
                        Shj.onUpdatePaperMoneyBalance(0);
                        Shj.offerShelf = -1;
                        Shj.debugShelf = -1;
                        Shj.debugTimer.cancel();
                        Shj.debugTimer = null;
                    }
                }
                Shj.debugStep++;
            } catch (Exception unused2) {
            }
            this.isLooping = false;
        }
    }

    public static void debugSelectShelf(int i) {
        if (isDebugOfferringGoods) {
            return;
        }
        onSelectGoodsOnShelf(Integer.valueOf(i));
        if (getWallet().getCatchMoney().intValue() >= getShelfInfo(Integer.valueOf(i)).getPrice().intValue()) {
            debugOfferGoods(i, true);
        }
    }

    public static void debugUnslectShelf(int i) {
        debugShelf = -1;
        onDeselectGoodsOnShelf(Integer.valueOf(i));
    }

    public static void debugAcceptMoney(int i, MoneyType moneyType, String str) {
        onAcceptMoney(i, moneyType, str);
        onResetCurrentMoney(getWallet().getCatchMoney().intValue() + i, false);
    }

    public static List<String> getAllGoodeCode() {
        ArrayList arrayList = new ArrayList();
        Iterator<ShelfInfo> it = shelfGoodsMap.values().iterator();
        while (it.hasNext()) {
            String goodsCode = it.next().getGoodsCode();
            if (!goodsCode.equals("-1") && !arrayList.contains(goodsCode)) {
                arrayList.add(goodsCode);
            }
        }
        return arrayList;
    }

    public static List<ShelfInfo> getShelfInfos(String str) {
        ArrayList arrayList = new ArrayList();
        for (Integer num : shelfGoodsMap.keySet()) {
            ShelfInfo shelfInfo = shelfGoodsMap.get(num);
            if (str.length() == 0 && shelfInfo.getGroupName().length() == 0) {
                arrayList.add(getShelfInfo(num));
            } else if (shelfInfo.getGroupName().length() > 0) {
                if (str.contains(shelfInfo.getGroupName() + VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                    arrayList.add(getShelfInfo(num));
                }
            }
        }
        return arrayList;
    }

    public static void initShelfInfo(int i, String str, Object obj) {
        if (!groupsMap.containsKey(str)) {
            groupsMap.put(str, new ShelfGroupInfo(str));
        } else {
            groupsMap.get(str);
        }
        ShelfInfo _getShelfInfo = _getShelfInfo(Integer.valueOf(i));
        _getShelfInfo.setGroupName(str);
        _getShelfInfo.setDatas(SpeechEvent.KEY_EVENT_RECORD_DATA, obj);
    }

    public static String adjustShelfGoodsCode(int i, String str) {
        ShelfInfo _getShelfInfo = _getShelfInfo(Integer.valueOf(i));
        if (_getShelfInfo == null || !groupsMap.containsKey(_getShelfInfo.getGroupName())) {
            return str;
        }
        return str + groupsMap.get(_getShelfInfo.getGroupName()).getGroupId();
    }

    public static Context getContext() {
        return wkContext.get();
    }

    public static void setVersion(int i) {
        version = i;
    }

    public static void updateVMCConnectStateTime() {
        lastDownStateTime = System.currentTimeMillis();
    }

    public static void resetStateCheckTimer() {
        if (isDebug) {
            return;
        }
        lastDownStateTime = System.currentTimeMillis();
        Timer timer = stateCheckTimer;
        if (timer != null) {
            timer.cancel();
            stateCheckTimer = null;
        }
        Timer timer2 = new Timer();
        stateCheckTimer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.shj.Shj.2
            AnonymousClass2() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    if (System.currentTimeMillis() - Shj.lastDownStateTime > 300000) {
                        Shj.shjListener._onDownMachineDisconnect();
                    }
                } catch (Exception unused) {
                }
            }
        }, 180000L, 180000L);
    }

    /* renamed from: com.shj.Shj$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends TimerTask {
        AnonymousClass2() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                if (System.currentTimeMillis() - Shj.lastDownStateTime > 300000) {
                    Shj.shjListener._onDownMachineDisconnect();
                }
            } catch (Exception unused) {
            }
        }
    }

    public static boolean initShj(Context context) {
        Loger.writeLog("SHJ", "售货机初始化...");
        if (shj == null) {
            Loger.writeLog("SHJ", "生成售货机实例...");
            shj = new Shj();
        }
        if (wkContext == null) {
            wkContext = new WeakReference<>(context);
        }
        if (!isStoreGoodsInfoInVMC()) {
            ShjDbHelper.init(context);
        }
        resetStateCheckTimer();
        registerReportCommands();
        if (!isDebug()) {
            ShjVMCServiceEx.start(context);
        }
        Loger.writeLog("SHJ", "售货机初始化完成...");
        return true;
    }

    public static boolean initShj(Context context, String str) {
        Loger.writeLog("SHJ", "售货机初始化...");
        if (shj == null) {
            Loger.writeLog("SHJ", "生成售货机实例...");
            shj = new Shj();
        }
        if (wkContext == null) {
            wkContext = new WeakReference<>(context);
        }
        registerReportCommands();
        Bluetooth bluetooth2 = new Bluetooth();
        bluetooth = bluetooth2;
        bluetooth2.attachBluetooth(str);
        Loger.writeLog("SHJ", "售货机初始化完成...");
        return true;
    }

    private static void registerReportCommands() {
        Loger.writeLog("SHJ", "开始注册命令...");
        ArrayList arrayList = new ArrayList();
        if (version == 2) {
            arrayList.add(CommandV2_Down_AcceptMoney.class.getName());
            arrayList.add(CommandV2_Down_Change.class.getName());
            arrayList.add(CommandV2_Down_CurrentMoney.class.getName());
            arrayList.add(CommandV2_Down_OfferGoods.class.getName());
            arrayList.add(CommandV2_Down_PosInfo.class.getName());
            arrayList.add(CommandV2_Down_SelectGoods.class.getName());
            arrayList.add(CommandV2_Down_ShelfInfo.class.getName());
            arrayList.add(CommandV2_Down_ShelfStatus.class.getName());
            arrayList.add(CommandV2_Down_ShjStatus.class.getName());
            arrayList.add(CommandV2_Down_UpdateShelfCapacity.class.getName());
            arrayList.add(CommandV2_Down_UpdateShelfGoodsCode.class.getName());
            arrayList.add(CommandV2_Down_UpdateShelfGoodsCount.class.getName());
            arrayList.add(CommandV2_Down_UpdateShelfPrice.class.getName());
            arrayList.add(CommandV2_Down_Syn.class.getName());
            arrayList.add(CommandV2_Down_OneKeyFull.class.getName());
            arrayList.add(CommandV2_Down_ICCardInfo.class.getName());
            arrayList.add(CommandV2_Down_Setting.class.getName());
            arrayList.add(CommandV2_Down_ICCardPay.class.getName());
            arrayList.add(CommandV2_Down_CommandAnswer.class.getName());
            arrayList.add(CommandV2_Down_Version.class.getName());
            arrayList.add(CommandV2_Down_Notice.class.getName());
            arrayList.add(CommandV2_Down_PickerState.class.getName());
            arrayList.add(CommandV2_Down_ShelfDoorStatus.class.getName());
            arrayList.add(CommandV2_Down_VMCStatus.class.getName());
        } else {
            arrayList.add(Command_Down_ReportAcceptMoney.class.getName());
            arrayList.add(Command_Down_ReportCoinBalance.class.getName());
            arrayList.add(Command_Down_ReportGoodsCode.class.getName());
            arrayList.add(Command_Down_ReportShelfGoodsCount.class.getName());
            arrayList.add(Command_Down_ReportShelfGoodsPrice.class.getName());
            arrayList.add(Command_Down_ReportPaperMoneyBalance.class.getName());
            arrayList.add(Command_Down_ReportPosInfo.class.getName());
            arrayList.add(Command_Down_ReportReseted.class.getName());
            arrayList.add(Command_Down_ReportSelGoodsStatus.class.getName());
            arrayList.add(Command_Down_ReportSettingFinished.class.getName());
            arrayList.add(Command_Down_ReportStatus.class.getName());
            arrayList.add(Command_Down_ReportTemperature.class.getName());
            arrayList.add(Command_Down_ReportStatus.class.getName());
            arrayList.add(Command_Down_WaitCommand.class.getName());
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            try {
                XYClass xYClass = (XYClass) Class.forName(str).getAnnotation(XYClass.class);
                if (xYClass.KEY().equals("HEAD")) {
                    CommandManager.registerCommand(Short.valueOf((short) Integer.parseInt(xYClass.VALUE().substring(2), 16)), str);
                }
            } catch (Exception unused) {
            }
        }
        Loger.writeLog("SHJ", "注册命令结束...");
    }

    public static void setShjListener(ShjListener shjListener2) {
        shjListener = shjListener2;
    }

    public static Wallet getWallet() {
        return wallet;
    }

    public static ShelfInfo getSelectedShelf() {
        return _getShelfInfo(Integer.valueOf(isDebug ? debugShelf : selectedShelf));
    }

    public static ShelfInfo getLastSelectedShelf() {
        int i = lastSelectShelf;
        if (i == -1) {
            return null;
        }
        return _getShelfInfo(Integer.valueOf(i));
    }

    public static ShelfInfo getLastOfferedShelf() {
        int i = lastOfferedShelf;
        if (i == -1) {
            return null;
        }
        return _getShelfInfo(Integer.valueOf(i));
    }

    public static HashMap<Integer, ShelfInfo> getShelfInfosByGoodsCode(String str) {
        if (codeGoodsMap.containsKey(str)) {
            return codeGoodsMap.get(str);
        }
        HashMap<Integer, ShelfInfo> hashMap = new HashMap<>();
        codeGoodsMap.put(str, hashMap);
        return hashMap;
    }

    public static List<String> getShelfGoodsBatchNumbers(String str) {
        ArrayList arrayList = new ArrayList();
        if (codeGoodsMap.containsKey(str)) {
            HashMap<Integer, ShelfInfo> hashMap = codeGoodsMap.get(str);
            Iterator<Integer> it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                ShelfInfo shelfInfo = hashMap.get(it.next());
                if (!arrayList.contains(shelfInfo.getGoodsbatchnumber())) {
                    arrayList.add(shelfInfo.getGoodsbatchnumber());
                }
            }
        }
        return arrayList;
    }

    public static HashMap<Integer, ShelfInfo> getShelfInfosByGoodsCode(String str, String str2) {
        if (codeGoodsMap.containsKey(str)) {
            if (str2 != null && !str2.isEmpty()) {
                HashMap<Integer, ShelfInfo> hashMap = new HashMap<>();
                HashMap<Integer, ShelfInfo> hashMap2 = codeGoodsMap.get(str);
                for (Integer num : hashMap2.keySet()) {
                    ShelfInfo shelfInfo = hashMap2.get(num);
                    if (hashMap2.get(num).getGoodsbatchnumber().equals(str2)) {
                        hashMap.put(num, shelfInfo);
                    }
                }
                return hashMap;
            }
            return codeGoodsMap.get(str);
        }
        HashMap<Integer, ShelfInfo> hashMap3 = new HashMap<>();
        codeGoodsMap.put(str, hashMap3);
        return hashMap3;
    }

    public static ShelfInfo _getShelfInfo(Integer num) {
        if (num.intValue() <= 0 || num.intValue() > 900) {
            return null;
        }
        if (shelfGoodsMap.containsKey(num)) {
            return shelfGoodsMap.get(num);
        }
        ShelfInfo shelfInfo = new ShelfInfo();
        shelfInfo.setGoodsCode("-1");
        shelfInfo.setShelf(num);
        shelfInfo.setGoodsCount(0);
        shelfGoodsMap.put(num, shelfInfo);
        return shelfInfo;
    }

    public static ShelfInfo getShelfInfo(Integer num) {
        if (shelfGoodsMap.containsKey(num)) {
            return shelfGoodsMap.get(num);
        }
        return null;
    }

    public static void onUpdateShelfGoodsCode(Integer num, String str) {
        String adjustShelfGoodsCode = adjustShelfGoodsCode(num.intValue(), str);
        try {
            if (num.intValue() == 999) {
                resetStatus = 1;
                return;
            }
            if (num.intValue() == 0) {
                Iterator<ShelfInfo> it = shelfGoodsMap.values().iterator();
                while (it.hasNext()) {
                    updateShelfGoodsCode(it.next().getShelf(), adjustShelfGoodsCode);
                }
            } else {
                if (num.intValue() >= 1000) {
                    int intValue = num.intValue() - 1000;
                    for (ShelfInfo shelfInfo : shelfGoodsMap.values()) {
                        if (intValue == getLayerByShelf(shelfInfo.getShelf().intValue())) {
                            updateShelfGoodsCode(shelfInfo.getShelf(), adjustShelfGoodsCode);
                        }
                    }
                    return;
                }
                updateShelfGoodsCode(num, adjustShelfGoodsCode);
            }
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    private static void updateShelfGoodsCode(Integer num, String str) {
        try {
            ShelfInfo _getShelfInfo = _getShelfInfo(num);
            if (_getShelfInfo != null && !_getShelfInfo.getGoodsCode().equals(str)) {
                HashMap<Integer, ShelfInfo> shelfInfosByGoodsCode = getShelfInfosByGoodsCode(str);
                if (!_getShelfInfo.getGoodsCode().equals("-1")) {
                    HashMap<Integer, ShelfInfo> shelfInfosByGoodsCode2 = getShelfInfosByGoodsCode(_getShelfInfo.getGoodsCode());
                    if (shelfInfosByGoodsCode2.size() > 0 && shelfInfosByGoodsCode2.containsKey(num)) {
                        shelfInfosByGoodsCode2.remove(num);
                    }
                }
                String goodsCode = _getShelfInfo.getGoodsCode();
                boolean equals = goodsCode.equals("-1");
                _getShelfInfo.setGoodsCode(str);
                if (!shelfInfosByGoodsCode.containsKey(num)) {
                    shelfInfosByGoodsCode.put(num, _getShelfInfo);
                }
                shjListener._onUpdateShelfGoods(num, str, goodsCode, _getShelfInfo.getGroupName());
                if (equals) {
                    if (_getShelfInfo.getPrice() != null) {
                        onUpdateShelfPrice(num, _getShelfInfo.getPrice());
                    }
                    if (_getShelfInfo.getGoodsCount() != null) {
                        onUpdateGoodsCount(num, _getShelfInfo.getGoodsCount());
                    }
                }
            }
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    public static Machine getMachine(int i, boolean z) {
        if (i < 0) {
            return null;
        }
        if (mapMachines.containsKey(Integer.valueOf(i))) {
            return mapMachines.get(Integer.valueOf(i));
        }
        if (!z) {
            return null;
        }
        Machine machine = new Machine();
        machine.setJgh(Integer.valueOf(i));
        mapMachines.put(Integer.valueOf(i), machine);
        machinesList.add(machine);
        return machine;
    }

    public static List<Machine> getAllMachines() {
        return machinesList;
    }

    public static int getLayerByShelf(int i) {
        ShelfInfo shelfInfo = getShelfInfo(Integer.valueOf(i));
        if (shelfInfo != null && -1 != shelfInfo.getLayer().intValue()) {
            return shelfInfo.getLayer().intValue();
        }
        if (i <= 9) {
            return 0;
        }
        int i2 = i / 10;
        return i % 10 == 0 ? i2 - 1 : i2;
    }

    public static int getLayerNumberByShelf(int i) {
        return getLayerByShelf(i) / 10;
    }

    public static int getJghByShelf(int i) {
        ShelfInfo shelfInfo = getShelfInfo(Integer.valueOf(i));
        if (shelfInfo != null && -1 != shelfInfo.getJgh().intValue()) {
            return shelfInfo.getJgh().intValue();
        }
        return getLayerByShelf(i) / 10;
    }

    public static void setTemperature(int i, int i2) {
        if (170 == i2) {
            return;
        }
        boolean z = true;
        try {
            Machine machine = getMachine(i, true);
            if (162 != i2 && 162 != i2) {
                if (machine.getTemperature().intValue() == i2) {
                    z = false;
                }
                machine.setTemperature(Integer.valueOf(i2));
                if (z) {
                    shjListener._onUpdateTemperature(i, i2);
                }
            }
            setTemperatureFault(i, i2);
        } catch (Exception unused) {
        }
    }

    public static void setTemperatureFault(int i, int i2) {
        try {
            AppStatusLoger.addAppStatus_no_repeat(null, "SHJ", AppStatusLoger.Type_DeviceError, "", "机柜号:" + i + " 温控仪故障:" + i2);
            boolean z = true;
            Machine machine = getMachine(i, true);
            if (machine.getTemperatureFault().intValue() == i2) {
                z = false;
            }
            if (z) {
                machine.setTemperatureFault(i2);
            }
        } catch (Exception unused) {
        }
    }

    public static void setHumidity(int i, int i2) {
        if (170 == i2) {
            return;
        }
        boolean z = true;
        try {
            Machine machine = getMachine(i, true);
            if (162 != i2 && 162 != i2) {
                if (machine.getHumidity().intValue() == i2) {
                    z = false;
                }
                machine.setHumidity(Integer.valueOf(i2));
                if (z) {
                    shjListener._onUpdateHumidity(i, i2);
                }
            }
            AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_DeviceError, "", "机柜号:" + i + " 湿控仪故障:" + i2);
        } catch (Exception unused) {
        }
    }

    public static void setDoorState(int i, int i2) {
        if (170 == i2) {
            return;
        }
        boolean z = true;
        try {
            Machine machine = getMachine(i, true);
            if (162 != i2 && 162 != i2) {
                boolean z2 = machine.isDoorIsOpen() != (i2 == 1);
                if (i2 != 1) {
                    z = false;
                }
                machine.setDoorIsOpen(z);
                if (z2) {
                    shjListener._onDoorStatusChanged(i, machine.isDoorIsOpen());
                }
            }
            AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_DeviceError, "16000003", "机柜号:" + i + " 门状态故障:" + i2);
        } catch (Exception unused) {
        }
    }

    public static int getShelfCount() {
        return shelfGoodsMap.keySet().size();
    }

    public static int getShelfCapacity(int i) {
        if (!shelfGoodsMap.containsKey(Integer.valueOf(i))) {
            return 0;
        }
        if (shelfGoodsMap.get(Integer.valueOf(i)).getCapacity().intValue() > 0) {
            return shelfGoodsMap.get(Integer.valueOf(i)).getCapacity().intValue();
        }
        shelfGoodsMap.get(Integer.valueOf(i)).setCapacity(20);
        return 20;
    }

    public static HashMap<String, HashMap<Integer, ShelfInfo>> getErrorShelves() {
        HashMap<String, HashMap<Integer, ShelfInfo>> hashMap = new HashMap<>();
        for (String str : codeGoodsMap.keySet()) {
            HashMap<Integer, ShelfInfo> hashMap2 = codeGoodsMap.get(str);
            if (hashMap2.size() > 0) {
                Iterator<ShelfInfo> it = hashMap2.values().iterator();
                int i = -1;
                while (true) {
                    if (it.hasNext()) {
                        ShelfInfo next = it.next();
                        if (i == -1) {
                            try {
                                i = next.getPrice().intValue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (i != next.getPrice().intValue()) {
                            hashMap.put(str, hashMap2);
                            break;
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    public static HashMap<String, HashMap<Integer, ShelfInfo>> getLowPriceShelves() {
        HashMap<String, HashMap<Integer, ShelfInfo>> hashMap = new HashMap<>();
        for (String str : codeGoodsMap.keySet()) {
            HashMap<Integer, ShelfInfo> hashMap2 = codeGoodsMap.get(str);
            for (ShelfInfo shelfInfo : hashMap2.values()) {
                if (str.equals("0") || shelfInfo.getPrice().intValue() <= 100) {
                    hashMap.put(str, hashMap2);
                    break;
                }
            }
        }
        return hashMap;
    }

    public static List<Integer> getShelves() {
        ArrayList arrayList = new ArrayList();
        Iterator<Integer> it = shelfGoodsMap.keySet().iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    public static boolean isHaveShelvesInfo() {
        return shelfGoodsMap.size() > 0;
    }

    public static int getGoodsCount(String str) {
        try {
            int i = 0;
            Iterator<ShelfInfo> it = codeGoodsMap.get(str).values().iterator();
            while (it.hasNext()) {
                i += it.next().getGoodsCount().intValue();
            }
            return i;
        } catch (Exception unused) {
            return -1;
        }
    }

    private static void updateGoodsCount(Integer num, Integer num2) {
        try {
            ShelfInfo _getShelfInfo = _getShelfInfo(num);
            if (_getShelfInfo.getGoodsCount() == num2) {
                return;
            }
            _getShelfInfo.setGoodsCount(num2);
            shjListener._onUpdateShelfGoodsCount(num, num2.intValue());
            if (_getShelfInfo.getGoodsCode().equals("-1")) {
                return;
            }
            int i = 0;
            Iterator<ShelfInfo> it = codeGoodsMap.get(_getShelfInfo.getGoodsCode()).values().iterator();
            while (it.hasNext()) {
                i += it.next().getGoodsCount().intValue();
            }
            shjListener._onUpdateGoodsCount(_getShelfInfo.getGoodsCode(), i);
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    public static void onFullGoodsCount() {
        for (ShelfInfo shelfInfo : shelfGoodsMap.values()) {
            updateGoodsCount(shelfInfo.getShelf(), shelfInfo.getCapacity());
        }
    }

    public static void onUpdateGoodsCount(Integer num, Integer num2) {
        if (num2.intValue() < 0) {
            num2 = 0;
        }
        Loger.writeLog("SHJ", "更新货道" + num + "商品数量" + num2);
        if (getVersion() == 1) {
            updateGoodsCount(num, num2);
            return;
        }
        if (num.intValue() == 0) {
            Iterator<ShelfInfo> it = shelfGoodsMap.values().iterator();
            while (it.hasNext()) {
                updateGoodsCount(it.next().getShelf(), num2);
            }
        } else {
            if (num.intValue() >= 1000) {
                int intValue = num.intValue() - 1000;
                for (ShelfInfo shelfInfo : shelfGoodsMap.values()) {
                    if (intValue == getLayerByShelf(shelfInfo.getShelf().intValue())) {
                        updateGoodsCount(shelfInfo.getShelf(), num2);
                    }
                }
                return;
            }
            updateGoodsCount(num, num2);
        }
    }

    private static void updateShelfPrice(Integer num, Integer num2) {
        try {
            ShelfInfo _getShelfInfo = _getShelfInfo(num);
            if (_getShelfInfo.getPrice() == num2) {
                return;
            }
            _getShelfInfo.setPrice(num2);
            if (_getShelfInfo.getGoodsCode().equals("-1")) {
                return;
            }
            shjListener._onUpdateGoodsPrice(num, _getShelfInfo.getGoodsCode(), num2);
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    public static void onUpdateShelfPrice(Integer num, Integer num2) {
        int i = resetStatus;
        if (i == 1) {
            resetStatus = i + 1;
        }
        Loger.writeLog("SHJ", "更新货道" + num + "商品价格" + num2);
        if (getVersion() == 1) {
            updateShelfPrice(num, num2);
            return;
        }
        if (num.intValue() == 0) {
            Iterator<ShelfInfo> it = shelfGoodsMap.values().iterator();
            while (it.hasNext()) {
                updateShelfPrice(it.next().getShelf(), num2);
            }
        } else {
            if (num.intValue() >= 1000) {
                int intValue = num.intValue() - 1000;
                for (ShelfInfo shelfInfo : shelfGoodsMap.values()) {
                    if (intValue == getLayerByShelf(shelfInfo.getShelf().intValue())) {
                        updateShelfPrice(shelfInfo.getShelf(), num2);
                    }
                }
                return;
            }
            updateShelfPrice(num, num2);
        }
    }

    public static void onSelectGoodsOnShelf(Integer num) {
        try {
            if (getShelfInfo(num).getGoodsCode().equals("0") || debugOfferDiviceError > 0) {
                if (getVersion() == 1) {
                    CommandManager.appendSendCommand(new Command_Up_UnSelectGoods());
                    return;
                } else {
                    CommandManager.appendSendCommand(new CommandV2_Up_UnSelectGoods());
                    return;
                }
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeLog("SHJ", "下位机选择货道的商品编码为0000，已自动取消选择货道");
        }
        setIsOfferingGoods(false);
        goodsPickerDoorState = 0;
        lastSelectShelf = num.intValue();
        if (selectedShelf == num.intValue()) {
            return;
        }
        debugShelf = num.intValue();
        selectShelfTmp = -1;
        selectedShelf = num.intValue();
        shjListener._onSelectGoodsOnShelf(num);
    }

    public static void onDeselectGoodsOnShelf(Integer num) {
        try {
            setIsOfferingGoods(false);
            int i = selectedShelf;
            selectedShelf = -1;
            shjListener._onDeselectGoodsOnShelf(Integer.valueOf(i));
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    public static void onWriteCommand(Command command) {
        try {
            shjListener._onWriteCommand(command);
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    public static void onUpdateShjStatus(VMCStatus vMCStatus, int i) {
        lastDownStateTime = System.currentTimeMillis();
        Loger.writeLog("SHJ", "更新状态" + vMCStatus + " selectShelf:" + selectedShelf + " tmp:" + selectShelfTmp);
        try {
            int i2 = AnonymousClass7.$SwitchMap$com$shj$device$VMCStatus[vMCStatus.ordinal()];
            switch (i2) {
                case 1:
                    onOfferGoods(selectedShelf, OfferState.Offering, 1);
                    break;
                case 2:
                    wallet.setCatchMoney(Integer.valueOf(i));
                    wallet.markLastAddTypeBeforOfferGoods();
                    onOfferGoods(selectedShelf, OfferState.OfferSuccess, 1);
                    shjListener._onMoneyChanged();
                    int i3 = lastDrivedShelf;
                    if (i3 != -1 && selectedShelf == i3) {
                        onUpdateGoodsCount(Integer.valueOf(i3), Integer.valueOf(getShelfInfo(Integer.valueOf(lastDrivedShelf)).getGoodsCount().intValue() - 1));
                    }
                    lastDrivedShelf = -1;
                    break;
                case 3:
                case 4:
                    int intValue = wallet.getCatchMoney().intValue() - i;
                    if (intValue > 0) {
                        Wallet wallet2 = wallet;
                        wallet2.setLastCoinChange(Integer.valueOf(wallet2.getCatchMoney().intValue() - i));
                    }
                    wallet.setLateastChange(Integer.valueOf(intValue));
                    wallet.setCatchMoney(Integer.valueOf(i));
                    if (wallet.getLateastChange().intValue() != 0) {
                        shjListener._onMoneyChanged();
                    }
                    shjListener._onChangeFinished(wallet.getLateastChange().intValue(), 0);
                    break;
                case 5:
                    onOfferGoods(selectedShelf, OfferState.ShelfNoGoods, 1);
                    break;
                case 6:
                    onOfferGoods(selectedShelf, OfferState.CargoLaneInvalid, 1);
                    break;
                case 7:
                    onOfferGoods(selectedShelf, OfferState.Blocked, 1);
                    break;
                case 8:
                    int i4 = selectShelfTmp;
                    if (i4 != -1) {
                        onSelectGoodsOnShelf(Integer.valueOf(i4));
                    }
                    onUpdateShelfState(selectedShelf, 0);
                    break;
                default:
                    switch (i2) {
                        case 17:
                            getMachine(0, true).setDoorIsOpen(true);
                            shjListener._onDoorStatusChanged(0, true);
                            break;
                        case 18:
                            getMachine(0, true).setDoorIsOpen(false);
                            shjListener._onDoorStatusChanged(0, false);
                            break;
                        case 19:
                            shjListener._onCharging();
                            shjListener._onCharging();
                            break;
                        case 20:
                            shjListener._onShelfBlockCleared(-1);
                            break;
                    }
            }
            onUpdateShjStatus(vMCStatus);
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    public static void onUpdateShjStatus(VMCStatus vMCStatus) {
        VMCStatus vMCStatus2 = shjState;
        boolean z = vMCStatus2 != vMCStatus;
        shjState = vMCStatus;
        shjStateUpdateTime = System.currentTimeMillis();
        if (z) {
            shjListener._onVMCStatusChanged(vMCStatus2, shjState);
        }
    }

    public static VMCStatus getShjState() {
        if (shjState != VMCStatus.Normal && System.currentTimeMillis() - shjStateUpdateTime > 60000) {
            return VMCStatus.Normal;
        }
        return shjState;
    }

    public static void onUpdateCoinBalance(int i) {
        if (i == wallet.getCoinBalance().intValue()) {
            return;
        }
        wallet.setCoinBalance(Integer.valueOf(i));
        shjListener._onUpdateCoinBalance(i);
    }

    public static void onUpdatePaperMoneyBalance(int i) {
        if (i == wallet.getPaperBalance().intValue()) {
            return;
        }
        wallet.setPaperBalance(Integer.valueOf(i));
        shjListener._onUpdatePaperMoneyBalance(i);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x005a A[Catch: Exception -> 0x0085, TryCatch #0 {Exception -> 0x0085, blocks: (B:5:0x0022, B:7:0x0028, B:10:0x002d, B:12:0x0033, B:13:0x0045, B:14:0x004e, B:16:0x005a, B:17:0x005f, B:19:0x007f, B:24:0x004b), top: B:4:0x0022 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x007f A[Catch: Exception -> 0x0085, TRY_LEAVE, TryCatch #0 {Exception -> 0x0085, blocks: (B:5:0x0022, B:7:0x0028, B:10:0x002d, B:12:0x0033, B:13:0x0045, B:14:0x004e, B:16:0x005a, B:17:0x005f, B:19:0x007f, B:24:0x004b), top: B:4:0x0022 }] */
    /* JADX WARN: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void onAcceptMoney(int r5, com.shj.MoneyType r6, java.lang.String r7) {
        /*
            java.lang.String r0 = "SHJ"
            com.shj.device.Wallet r1 = com.shj.Shj.wallet
            java.lang.Integer r1 = r1.getCatchMoney()
            int r1 = r1.intValue()
            com.shj.device.Wallet r2 = com.shj.Shj.wallet
            java.lang.Integer r3 = java.lang.Integer.valueOf(r5)
            r2.setLastAdd(r3, r6)
            java.lang.String r2 = "NOINFO"
            boolean r2 = r7.equals(r2)
            if (r2 != 0) goto L22
            com.shj.device.Wallet r2 = com.shj.Shj.wallet
            r2.setLastAddMoneyInfo(r7)
        L22:
            com.shj.MoneyType r7 = com.shj.MoneyType.Reset     // Catch: java.lang.Exception -> L85
            r2 = 1
            r3 = 0
            if (r6 == r7) goto L4b
            com.shj.MoneyType r7 = com.shj.MoneyType.EAT     // Catch: java.lang.Exception -> L85
            if (r6 != r7) goto L2d
            goto L4b
        L2d:
            int r7 = getVersion()     // Catch: java.lang.Exception -> L85
            if (r7 != r2) goto L45
            com.shj.device.Wallet r7 = com.shj.Shj.wallet     // Catch: java.lang.Exception -> L85
            java.lang.Integer r4 = r7.getCatchMoney()     // Catch: java.lang.Exception -> L85
            int r4 = r4.intValue()     // Catch: java.lang.Exception -> L85
            int r4 = r4 + r5
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Exception -> L85
            r7.setCatchMoney(r4)     // Catch: java.lang.Exception -> L85
        L45:
            com.shj.ShjListener r7 = com.shj.Shj.shjListener     // Catch: java.lang.Exception -> L85
            r7._onReciveMoney(r6, r5)     // Catch: java.lang.Exception -> L85
            goto L4e
        L4b:
            onResetCurrentMoney(r3, r2)     // Catch: java.lang.Exception -> L85
        L4e:
            com.shj.device.Wallet r7 = com.shj.Shj.wallet     // Catch: java.lang.Exception -> L85
            java.lang.Integer r7 = r7.getCatchMoney()     // Catch: java.lang.Exception -> L85
            int r7 = r7.intValue()     // Catch: java.lang.Exception -> L85
            if (r1 == r7) goto L5f
            com.shj.ShjListener r7 = com.shj.Shj.shjListener     // Catch: java.lang.Exception -> L85
            r7._onMoneyChanged()     // Catch: java.lang.Exception -> L85
        L5f:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L85
            r7.<init>()     // Catch: java.lang.Exception -> L85
            java.lang.String r1 = "更新余额 "
            r7.append(r1)     // Catch: java.lang.Exception -> L85
            r7.append(r5)     // Catch: java.lang.Exception -> L85
            java.lang.String r5 = " "
            r7.append(r5)     // Catch: java.lang.Exception -> L85
            r7.append(r6)     // Catch: java.lang.Exception -> L85
            java.lang.String r5 = r7.toString()     // Catch: java.lang.Exception -> L85
            com.oysb.utils.Loger.writeLog(r0, r5)     // Catch: java.lang.Exception -> L85
            int r5 = com.shj.Shj.resetStatus     // Catch: java.lang.Exception -> L85
            if (r5 != r2) goto L89
            com.shj.Shj.resetStatus = r3     // Catch: java.lang.Exception -> L85
            onResetFinished(r3)     // Catch: java.lang.Exception -> L85
            goto L89
        L85:
            r5 = move-exception
            com.oysb.utils.Loger.writeException(r0, r5)
        L89:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.Shj.onAcceptMoney(int, com.shj.MoneyType, java.lang.String):void");
    }

    public static String getProperty(Context context, String str) {
        return PropertiesUtil.getProperties(context.getApplicationContext(), propertyFile, str);
    }

    public static boolean isReseting() {
        return resetStatus != 0;
    }

    public static void onReset() {
        try {
            ShjDbHelper.reset();
            resetFinished = false;
            mapMachines.clear();
            machinesList.clear();
            codeGoodsMap.clear();
            resetStatus = 1;
            Loger.writeLog("SHJ", "开始复位 ....");
            wallet.reset();
            shjListener._onReset();
        } catch (Exception unused) {
        }
        Timer timer = resetTimer;
        if (timer != null) {
            timer.cancel();
            resetTimer = null;
        }
        Timer timer2 = new Timer();
        resetTimer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.shj.Shj.3
            AnonymousClass3() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (Shj.resetStatus != 0) {
                    Shj.onResetFinished(true);
                }
            }
        }, 120000L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.Shj$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends TimerTask {
        AnonymousClass3() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (Shj.resetStatus != 0) {
                Shj.onResetFinished(true);
            }
        }
    }

    public static void onResetFinished(boolean z) {
        resetStatus = 0;
        resetFinished = true;
        Timer timer = resetTimer;
        if (timer != null) {
            timer.cancel();
            resetTimer = null;
        }
        try {
            shjListener._onResetFinished(z);
            ShjVMCSerialProcessorV1.readSleepTime = 50;
            Loger.writeLog("SHJ", "复位完成 ....");
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    public static void onSettingFinished() {
        Loger.writeLog("SHJ", "设置完成");
    }

    public static void onResume(Context context, String str) {
        Loger.writeLog("SHJ", "售货机正在恢复...");
        initShj(context);
    }

    public static void set2SelectShelf(int i) {
        selectShelfTmp = i;
    }

    public static void onChangeFinish(int i, int i2) {
        wallet.setLastCoinChange(Integer.valueOf(i));
        wallet.setLastPaperChange(Integer.valueOf(i2));
        wallet.setLateastChange(Integer.valueOf(i + i2));
        shjListener._onChangeFinished(i, i2);
    }

    public static void onResetCurrentMoney(int i, boolean z) {
        boolean z2 = i != wallet.getCatchMoney().intValue();
        int intValue = wallet.getCatchMoney().intValue() - i;
        Loger.writeLog("SHJ", "onResetCurrentMoney new:" + i + " :change:" + intValue + " changed:" + z2);
        if (z2) {
            wallet.setCatchMoney(Integer.valueOf(i));
            if (z) {
                shjListener._onResetMoneyInCatch(intValue);
            }
            shjListener._onMoneyChanged();
        }
    }

    public static void onDriverShelf(int i, ShelfDriverState shelfDriverState) {
        getWallet().markLastAddTypeBeforOfferGoods();
        shelfGoodsMap.get(Integer.valueOf(i)).setStatus(0);
        int i2 = AnonymousClass7.$SwitchMap$com$shj$ShelfDriverState[shelfDriverState.ordinal()];
        if (i2 == 1) {
            shjListener._onDriverShelfSuccess(i);
            return;
        }
        if (i2 == 3) {
            shjListener._onDriverShelfBlocked(i);
            return;
        }
        if (i2 == 4) {
            AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_DeviceError, "", "EngineError shelf:" + i);
            shjListener._onDriverShelfError(i);
            return;
        }
        if (i2 != 5) {
            return;
        }
        AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_DeviceError, "", "ShelfCheckFailed shelf:" + i);
        shjListener._onDriverShelfError(i);
    }

    public static int getLatestOfferErroCode() {
        return latestOfferErroCode;
    }

    public static void onOfferGoods(int i, OfferState offerState, int i2) {
        String name;
        String name2;
        String name3;
        if (offerState == OfferState.OfferSuccess) {
            if (debugBlockOfferGoods) {
                offerState = OfferState.Blocked;
            } else if (debugNoGoodsOnShelf) {
                offerState = OfferState.ShelfNoGoods;
            }
        }
        try {
            Loger.writeLog("SHJ;SALES", "shelf doorstate:1 shelf:" + i);
            onShelfDoorStatusUpdated(i, 1);
        } catch (Exception unused) {
        }
        if (i2 > -1) {
            if (i > 0) {
                try {
                    aryPickers.get(i2 - 1).setLastOfferShelf(i);
                    lastOfferGoodsPicker = i;
                } catch (Exception unused2) {
                }
            }
            int i3 = AnonymousClass7.$SwitchMap$com$shj$OfferState[offerState.ordinal()];
            if (i3 == 1 || i3 == 2 || i3 == 3 || i3 == 4) {
                aryPickers.get(i2 - 1).setStatus(1);
            } else if (i3 == 5) {
                aryPickers.get(i2 - 1).setWaitTimer(0);
            } else {
                aryPickers.get(i2 - 1).setStatus(0);
            }
        }
        getWallet().markLastAddTypeBeforOfferGoods();
        getWallet().markLastAddMoneyInfoBeforOfferGoods();
        getWallet().markLastAddMoneyBeforOfferGoods();
        if (shelfGoodsMap.containsKey(Integer.valueOf(i))) {
            shelfGoodsMap.get(Integer.valueOf(i)).setStatus(0);
        }
        switch (offerState) {
            case Offering:
                offerErrorReported = false;
                latestOfferErroCode = 0;
                lastOfferGoodsTime = System.currentTimeMillis();
                offerSuccessTmp = false;
                setIsOfferingGoods(true);
                if (selectedShelf == -1) {
                    selectedShelf = i;
                }
                if (i == 0) {
                    i = selectedShelf;
                }
                lastOfferedShelf = i;
                try {
                    shjListener._onOfferingGoods(i, i2);
                    aryPickers.get(i2 - 1).setWaitTimer(0);
                    return;
                } catch (Exception unused3) {
                    return;
                }
            case OfferSuccess:
                offerSuccessTmp = true;
                if (selectedShelf == -1) {
                    selectedShelf = i;
                }
                if (i == 0) {
                    i = selectedShelf;
                }
                lastOfferedShelf = i;
                try {
                    shjListener._onOfferGoodsSuccess(i, i2);
                    aryPickers.get(i2 - 1).setWaitTimer(0);
                } catch (Exception unused4) {
                }
                selectedShelf = -1;
                debugShelf = -1;
                setIsOfferingGoods(false);
                return;
            case Blocked:
                if (selectedShelf == -1) {
                    selectedShelf = i;
                }
                if (i == 0) {
                    i = selectedShelf;
                }
                lastOfferedShelf = i;
                try {
                    onUpdateShelfState(i, 3);
                    int index = OfferState.Blocked.getIndex();
                    latestOfferErroCode = index;
                    shjListener._onOfferGoodsBlocked(i, index, i2);
                    aryPickers.get(i2 - 1).setWaitTimer(0);
                } catch (Exception unused5) {
                }
                selectedShelf = -1;
                setIsOfferingGoods(false);
                return;
            case Unkonw:
                if (selectedShelf == -1) {
                    selectedShelf = i;
                }
                if (i == 0) {
                    i = selectedShelf;
                }
                lastOfferedShelf = i;
                try {
                    onUpdateShelfState(i, 3);
                    int index2 = OfferState.Unkonw.getIndex();
                    latestOfferErroCode = index2;
                    shjListener._onOfferGoodsBlocked(i, index2, i2);
                    aryPickers.get(i2 - 1).setWaitTimer(0);
                } catch (Exception unused6) {
                }
                selectedShelf = -1;
                setIsOfferingGoods(false);
                return;
            case EngineError:
                if (selectedShelf == -1) {
                    selectedShelf = i;
                }
                try {
                    ShjListener shjListener2 = shjListener;
                    int index3 = offerState.getIndex();
                    if (offerState.getIndex() == 35) {
                        name2 = "" + i;
                    } else {
                        name2 = offerState.getName();
                    }
                    shjListener2._onOfferGoodsState(index3, name2, i2);
                } catch (Exception unused7) {
                }
                try {
                    onUpdateShelfState(i, 1);
                    latestOfferErroCode = OfferState.EngineError.getIndex();
                    shjListener._onOfferGoodsBlocked(i, OfferState.EngineError.getIndex(), i2);
                    aryPickers.get(i2 - 1).setWaitTimer(0);
                } catch (Exception unused8) {
                }
                selectedShelf = -1;
                setIsOfferingGoods(false);
                AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_DeviceError, "", "EngineError shelf:" + i);
                return;
            case ShelfNoGoods:
                if (selectedShelf == -1) {
                    selectedShelf = i;
                }
                try {
                    ShjListener shjListener3 = shjListener;
                    int index4 = offerState.getIndex();
                    if (offerState.getIndex() == 35) {
                        name3 = "" + i;
                    } else {
                        name3 = offerState.getName();
                    }
                    shjListener3._onOfferGoodsState(index4, name3, i2);
                } catch (Exception unused9) {
                }
                try {
                    onUpdateShelfState(i, 2);
                    latestOfferErroCode = OfferState.ShelfNoGoods.getIndex();
                    shjListener._onOfferGoodsBlocked(i, OfferState.ShelfNoGoods.getIndex(), i2);
                    aryPickers.get(i2 - 1).setWaitTimer(0);
                } catch (Exception unused10) {
                }
                selectedShelf = -1;
                setIsOfferingGoods(false);
                AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_DeviceError, "", "EngineError shelf:" + i);
                return;
            default:
                try {
                    ShjListener shjListener4 = shjListener;
                    int index5 = offerState.getIndex();
                    if (offerState.getIndex() == 35) {
                        name = "" + i;
                    } else {
                        name = offerState.getName();
                    }
                    shjListener4._onOfferGoodsState(index5, name, i2);
                } catch (Exception unused11) {
                }
                try {
                    if (offerState.getIndex() == 35 && i2 > -1) {
                        aryPickers.get(i2 - 1).setWaitTimer(i);
                        if (!offerSuccessTmp) {
                            offerSuccessTmp = true;
                        }
                    }
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                }
                if (offerState == OfferState.Push2WBL) {
                    isHfj = true;
                }
                if (offerState == OfferState.CGResetError || offerState == OfferState.LiftCGPushError || offerState == OfferState.LiftDownError || offerState == OfferState.LiftUpError || offerState == OfferState.LiftInWBLError || offerState == OfferState.LiftOutWBLError || offerState == OfferState.OfferGoodsPaused || offerState == OfferState.NoWBLCloseBackDoorError || offerState == OfferState.NoWBLCloseFrontDoorError || offerState == OfferState.NoWBLOpenBackDoorError || offerState == OfferState.WBLOpenFrontDoorError || offerState == OfferState.WBLInnerTGBackError || offerState == OfferState.WBLInnerTGPushError || offerState == OfferState.WBLNoGoods || offerState == OfferState.CargoLaneInvalid || offerState == OfferState.NoEngine || offerState == OfferState.BeltCheckError || offerState == OfferState.ShelfNoGoods) {
                    offerErrorReported = false;
                    AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_OfferGoods, "08000001", "OfferState:" + offerState.toString());
                }
                latestOfferErroCode = offerState.getIndex();
                if (offerState == OfferState.BusinessStoped || offerState == OfferState.WBLWaitting2PickGoods || offerState == OfferState.BeltCheckError || offerState == OfferState.CargoLaneInvalid) {
                    if ((OfferState.BusinessStoped == offerState || latestOfferErroCode > 4) && OfferState.WBLWaitting2PickGoods != offerState && !offerErrorReported) {
                        if (selectedShelf == -1 && i != -1) {
                            selectedShelf = i;
                        }
                        offerErrorReported = true;
                        if (latestOfferErroCode == 0) {
                            latestOfferErroCode = OfferState.BusinessStoped.getIndex();
                        }
                        shjListener._onOfferGoodsBlocked(selectedShelf, latestOfferErroCode, i2);
                    }
                    int i4 = i2 - 1;
                    if (i4 >= 0 && i4 < aryPickers.size()) {
                        aryPickers.get(i4).setWaitTimer(0);
                    }
                    Loger.writeLog("SHJ", "offergoods state =" + offerState.getName() + "selectedShelf=" + selectedShelf);
                    selectedShelf = -1;
                }
                setIsOfferingGoods(false);
                return;
        }
    }

    /* renamed from: com.shj.Shj$7 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$ShelfDriverState;
        static final /* synthetic */ int[] $SwitchMap$com$shj$device$VMCStatus;

        static {
            int[] iArr = new int[OfferState.values().length];
            $SwitchMap$com$shj$OfferState = iArr;
            try {
                iArr[OfferState.NoWBLCloseFrontDoorError.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.NoWBLOpenBackDoorError.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.LiftInWBLError.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.WBLInnerTGBackError.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.WBLWaitting2PickGoods.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.Offering.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.OfferSuccess.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.Blocked.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.Unkonw.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.EngineError.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$shj$OfferState[OfferState.ShelfNoGoods.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            int[] iArr2 = new int[ShelfDriverState.values().length];
            $SwitchMap$com$shj$ShelfDriverState = iArr2;
            try {
                iArr2[ShelfDriverState.OfferSuccess.ordinal()] = 1;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$shj$ShelfDriverState[ShelfDriverState.NoEngine.ordinal()] = 2;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$shj$ShelfDriverState[ShelfDriverState.Blocked.ordinal()] = 3;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$shj$ShelfDriverState[ShelfDriverState.EngineError.ordinal()] = 4;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$shj$ShelfDriverState[ShelfDriverState.ShelfCheckFailed.ordinal()] = 5;
            } catch (NoSuchFieldError unused16) {
            }
            int[] iArr3 = new int[VMCStatus.values().length];
            $SwitchMap$com$shj$device$VMCStatus = iArr3;
            try {
                iArr3[VMCStatus.OfferingGoods.ordinal()] = 1;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.OfferGoodsSuccess.ordinal()] = 2;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.DoChargeSuccess.ordinal()] = 3;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.DoChargeFailed.ordinal()] = 4;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.GoodsLack.ordinal()] = 5;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.GoodsInvalidate.ordinal()] = 6;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.GoodsBlocked.ordinal()] = 7;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.GoodsValidate.ordinal()] = 8;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.NoEnging.ordinal()] = 9;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.GearStopError.ordinal()] = 10;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.EngingRunning.ordinal()] = 11;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.CommunicationsError.ordinal()] = 12;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.CardPaySuccess.ordinal()] = 13;
            } catch (NoSuchFieldError unused29) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.CardPayFailed.ordinal()] = 14;
            } catch (NoSuchFieldError unused30) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.PosSinging.ordinal()] = 15;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.PosSingSuccess.ordinal()] = 16;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.DoorIsOpen.ordinal()] = 17;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.DoorIsClose.ordinal()] = 18;
            } catch (NoSuchFieldError unused34) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.Charging.ordinal()] = 19;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.ClearGoodsBlockSuccess.ordinal()] = 20;
            } catch (NoSuchFieldError unused36) {
            }
            try {
                $SwitchMap$com$shj$device$VMCStatus[VMCStatus.ClearGoodsBLockFailed.ordinal()] = 21;
            } catch (NoSuchFieldError unused37) {
            }
        }
    }

    public static void onUpdateShelfCapacity(int i, int i2) {
        if (i == 0) {
            Iterator<ShelfInfo> it = shelfGoodsMap.values().iterator();
            while (it.hasNext()) {
                it.next().setCapacity(Integer.valueOf(i2));
            }
        } else if (i >= 1000) {
            int i3 = i + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
            for (ShelfInfo shelfInfo : shelfGoodsMap.values()) {
                if (i3 == getLayerByShelf(shelfInfo.getShelf().intValue())) {
                    shelfInfo.setCapacity(Integer.valueOf(i2));
                }
            }
        } else {
            ShelfInfo _getShelfInfo = _getShelfInfo(Integer.valueOf(i));
            if (_getShelfInfo != null) {
                _getShelfInfo.setCapacity(Integer.valueOf(i2));
            }
        }
        shjListener._onUpdateShelfInventory(Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static void onUpdateShelfState(int i, int i2) {
        if (debugShelfStopSale) {
            i2 = 4;
        }
        ShelfInfo _getShelfInfo = _getShelfInfo(Integer.valueOf(i));
        if (_getShelfInfo.getStatus().intValue() == i2) {
            return;
        }
        if (!isStoreGoodsInfoInVMC()) {
            ShjDbHelper.saveShelfInfo(i, -1, i2, -1, -1, -1, "", "", "", "");
        }
        _getShelfInfo.setStatus(Integer.valueOf(i2));
        shjListener._onUpdateShelfStatus(i, i2);
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0131  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0158  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0196  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01a9  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01bc  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01cf  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void updateShelfInfo(int r19, int r20, int r21, int r22, java.lang.String r23, int r24, int r25, int r26, int r27) {
        /*
            Method dump skipped, instructions count: 538
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.Shj.updateShelfInfo(int, int, int, int, java.lang.String, int, int, int, int):void");
    }

    public static void onUpdateShelfInfo(int i, int i2, int i3, int i4, String str, int i5, int i6, int i7, int i8) {
        int i9;
        int layerByShelf;
        String adjustShelfGoodsCode = adjustShelfGoodsCode(i, str);
        int i10 = i7;
        if (-1 == i10 && i < 1000) {
            i10 = getJghByShelf(i);
        }
        int i11 = i10;
        Machine machine = getMachine(i11, true);
        if (-1 == i8) {
            if (i >= 1000) {
                layerByShelf = i + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
            } else {
                layerByShelf = getLayerByShelf(i);
                if (machine != null) {
                    machine.addLayer(layerByShelf);
                    machine.addShelf(i);
                }
            }
            i9 = layerByShelf;
        } else {
            i9 = i8;
        }
        if (i == 0) {
            Iterator<ShelfInfo> it = shelfGoodsMap.values().iterator();
            while (it.hasNext()) {
                updateShelfInfo(it.next().getShelf().intValue(), i2, i3, i4, adjustShelfGoodsCode, -1, i6, i11, i9);
            }
        } else {
            if (i >= 1000) {
                for (ShelfInfo shelfInfo : shelfGoodsMap.values()) {
                    if (i9 == getLayerByShelf(shelfInfo.getShelf().intValue())) {
                        updateShelfInfo(shelfInfo.getShelf().intValue(), i2, i3, i4, adjustShelfGoodsCode, -1, i6, i11, i9);
                    }
                }
                return;
            }
            updateShelfInfo(i, i2, i3, i4, adjustShelfGoodsCode, i5, i6, i11, i9);
        }
    }

    public static void onUpdateShjStatusV2(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27, int i28, int i29, int i30, int i31, int i32, int i33) {
        lastDownStateTime = System.currentTimeMillis();
        if (resetStatus == 2) {
            resetStatus = 0;
            onResetFinished(false);
        }
        Machine machine = getMachine(0, true);
        machine.setPaperMachineStatus(i);
        machine.setCoinMachineStatus(i2);
        boolean z = machine.isDoorIsOpen() != (i6 == 1);
        machine.setDoorIsOpen(i6 == 1);
        machine.setPosMachineStatus(i3);
        setTemperature(0, i5);
        onUpdateCoinBalance(i8);
        onUpdatePaperMoneyBalance(i7);
        if (z) {
            shjListener._onDoorStatusChanged(0, machine.isDoorIsOpen());
        }
        setTemperature(0, i10);
        setTemperature(1, i11);
        setTemperature(2, i12);
        setTemperature(3, i13);
        setTemperature(4, i14);
        setTemperature(5, i15);
        setTemperature(6, i16);
        setTemperature(7, i17);
        setHumidity(0, i18);
        setHumidity(1, i19);
        setHumidity(2, i20);
        setHumidity(3, i21);
        setHumidity(4, i22);
        setHumidity(5, i23);
        setHumidity(6, i24);
        setHumidity(7, i25);
        setDoorState(0, i26);
        setDoorState(1, i27);
        setDoorState(2, i28);
        setDoorState(3, i29);
        setDoorState(4, i30);
        setDoorState(5, i31);
        setDoorState(6, i32);
        setDoorState(7, i33);
    }

    public static int getVersion() {
        return version;
    }

    public static boolean needCheckOfferStatus() {
        return needCheckOfferStatus;
    }

    public static void setCheckOfferStatusLong(int i) {
        checkOfferStatusLong = i;
    }

    public static int getCheckOfferStatusTimeOut() {
        return checkOfferStatusLong;
    }

    public static void setNeedCheckOfferStatus(boolean z, int i) {
        needCheckOfferStatus = z;
        checkOfferStatusLong = i;
    }

    public static String getComPath() {
        return comPath;
    }

    public static void setComPath(String str) {
        comPath = str;
    }

    public static long getComBaudrate() {
        return comBaudrate;
    }

    public static void setComBaudrate(long j) {
        comBaudrate = j;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean z) {
        isDebug = z;
        Loger.setSendBroadcast(z);
    }

    public static void clearDebugShelfInfos(boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        for (int i = 1; i < 100; i++) {
            if (z) {
                CacheHelper.getFileCache().remove("shelfGoodsCount:" + i);
            }
            if (z3) {
                CacheHelper.getFileCache().remove("shelfGoodsCode:" + i);
                CacheHelper.getFileCache().remove("shelfGoodsName:" + i);
            }
            if (z4) {
                CacheHelper.getFileCache().remove("shelfGoodsPrice:" + i);
            }
            if (z2) {
                CacheHelper.getFileCache().remove("shelfInventory:" + i);
            }
        }
        if (z5) {
            CacheHelper.getFileCache().remove("Debug:Blocks");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.Shj$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 extends RunnableEx {
        final /* synthetic */ ArrayList val$shelfInfos;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Object obj, ArrayList arrayList) {
            super(obj);
            arrayList = arrayList;
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            String str;
            String str2;
            String str3;
            int parseInt;
            int parseInt2;
            int parseInt3;
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ShelfInfo shelfInfo = (ShelfInfo) it.next();
                String goodsName = shelfInfo.getGoodsName();
                String str4 = null;
                if (Shj.isStoreGoodsInfoInVMC()) {
                    String asString = CacheHelper.getFileCache().getAsString("shelfGoodsCount:" + shelfInfo.getShelf());
                    String asString2 = CacheHelper.getFileCache().getAsString("shelfGoodsCode:" + shelfInfo.getShelf());
                    String asString3 = CacheHelper.getFileCache().getAsString("shelfGoodsName:" + shelfInfo.getShelf());
                    String asString4 = CacheHelper.getFileCache().getAsString("shelfGoodsPrice:" + shelfInfo.getShelf());
                    str3 = CacheHelper.getFileCache().getAsString("shelfInventory:" + shelfInfo.getShelf());
                    str2 = asString2;
                    goodsName = asString3;
                    str = asString;
                    str4 = asString4;
                } else {
                    str = null;
                    str2 = null;
                    str3 = null;
                }
                int intValue = shelfInfo.getShelf().intValue();
                if (str4 == null) {
                    parseInt = shelfInfo.getPrice().intValue();
                } else {
                    parseInt = Integer.parseInt(str4);
                }
                int i = parseInt;
                if (str == null) {
                    parseInt2 = shelfInfo.getGoodsCount().intValue();
                } else {
                    parseInt2 = Integer.parseInt(str);
                }
                int i2 = parseInt2;
                if (str3 == null) {
                    parseInt3 = shelfInfo.getCapacity().intValue();
                } else {
                    parseInt3 = Integer.parseInt(str3);
                }
                int i3 = parseInt3;
                if (str2 == null) {
                    str2 = shelfInfo.getGoodsCode();
                }
                Shj.onUpdateShelfInfo(intValue, i, i2, i3, str2, 0, 0, -1, -1);
                if (Shj.isStoreGoodsInfoInVMC()) {
                    Shj.getShelfInfo(shelfInfo.getShelf()).setGoodsName(goodsName);
                }
            }
            Shj.onUpdateShjStatusV2(0, 0, 0, 0, 14, 1, 0, 0, 144758855, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS);
        }
    }

    public static void createDebugShelfInfos(ArrayList<ShelfInfo> arrayList) {
        new Thread(new RunnableEx(null) { // from class: com.shj.Shj.4
            final /* synthetic */ ArrayList val$shelfInfos;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass4(Object obj, ArrayList arrayList2) {
                super(obj);
                arrayList = arrayList2;
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                String str;
                String str2;
                String str3;
                int parseInt;
                int parseInt2;
                int parseInt3;
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    ShelfInfo shelfInfo = (ShelfInfo) it.next();
                    String goodsName = shelfInfo.getGoodsName();
                    String str4 = null;
                    if (Shj.isStoreGoodsInfoInVMC()) {
                        String asString = CacheHelper.getFileCache().getAsString("shelfGoodsCount:" + shelfInfo.getShelf());
                        String asString2 = CacheHelper.getFileCache().getAsString("shelfGoodsCode:" + shelfInfo.getShelf());
                        String asString3 = CacheHelper.getFileCache().getAsString("shelfGoodsName:" + shelfInfo.getShelf());
                        String asString4 = CacheHelper.getFileCache().getAsString("shelfGoodsPrice:" + shelfInfo.getShelf());
                        str3 = CacheHelper.getFileCache().getAsString("shelfInventory:" + shelfInfo.getShelf());
                        str2 = asString2;
                        goodsName = asString3;
                        str = asString;
                        str4 = asString4;
                    } else {
                        str = null;
                        str2 = null;
                        str3 = null;
                    }
                    int intValue = shelfInfo.getShelf().intValue();
                    if (str4 == null) {
                        parseInt = shelfInfo.getPrice().intValue();
                    } else {
                        parseInt = Integer.parseInt(str4);
                    }
                    int i = parseInt;
                    if (str == null) {
                        parseInt2 = shelfInfo.getGoodsCount().intValue();
                    } else {
                        parseInt2 = Integer.parseInt(str);
                    }
                    int i2 = parseInt2;
                    if (str3 == null) {
                        parseInt3 = shelfInfo.getCapacity().intValue();
                    } else {
                        parseInt3 = Integer.parseInt(str3);
                    }
                    int i3 = parseInt3;
                    if (str2 == null) {
                        str2 = shelfInfo.getGoodsCode();
                    }
                    Shj.onUpdateShelfInfo(intValue, i, i2, i3, str2, 0, 0, -1, -1);
                    if (Shj.isStoreGoodsInfoInVMC()) {
                        Shj.getShelfInfo(shelfInfo.getShelf()).setGoodsName(goodsName);
                    }
                }
                Shj.onUpdateShjStatusV2(0, 0, 0, 0, 14, 1, 0, 0, 144758855, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS);
            }
        }).start();
    }

    /* renamed from: com.shj.Shj$5 */
    /* loaded from: classes2.dex */
    class AnonymousClass5 extends RunnableEx {
        final /* synthetic */ int val$count;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Object obj, int i) {
            super(obj);
            i = i;
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            ArrayList arrayList = new ArrayList();
            arrayList.add(155);
            arrayList.add(Integer.valueOf(SettingType.PRICE_SETTING_WHOLE_LAYER));
            for (int i = 0; i < i; i++) {
                if (i < 8) {
                    int i2 = i + 1;
                    Shj.onUpdateShelfInfo(i2, 1, 255, 10, "" + i2, 4, 0, -1, -1);
                } else {
                    int i3 = i + 1;
                    Shj.onUpdateShelfInfo(i3, 1, 255, 10, "" + i3, 0, 0, -1, -1);
                }
            }
            Shj.onUpdateShelfInfo(104, 1, 255, 10, "104", 0, 0, -1, -1);
            Shj.onUpdateShjStatusV2(0, 0, 0, 0, 14, 1, 100, 100, 144758855, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS);
            Shj.onResetFinished(false);
        }
    }

    public static void createDebugShelfInfos(int i) {
        new Thread(new RunnableEx(null) { // from class: com.shj.Shj.5
            final /* synthetic */ int val$count;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass5(Object obj, int i2) {
                super(obj);
                i = i2;
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                ArrayList arrayList = new ArrayList();
                arrayList.add(155);
                arrayList.add(Integer.valueOf(SettingType.PRICE_SETTING_WHOLE_LAYER));
                for (int i2 = 0; i2 < i; i2++) {
                    if (i2 < 8) {
                        int i22 = i2 + 1;
                        Shj.onUpdateShelfInfo(i22, 1, 255, 10, "" + i22, 4, 0, -1, -1);
                    } else {
                        int i3 = i2 + 1;
                        Shj.onUpdateShelfInfo(i3, 1, 255, 10, "" + i3, 0, 0, -1, -1);
                    }
                }
                Shj.onUpdateShelfInfo(104, 1, 255, 10, "104", 0, 0, -1, -1);
                Shj.onUpdateShjStatusV2(0, 0, 0, 0, 14, 1, 100, 100, 144758855, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS, SettingType.RESTORE_FACTORY_SETTINGS);
                Shj.onResetFinished(false);
            }
        }).start();
    }

    public static String getMachineId() {
        String str = testMachineId;
        if (str != "" && str.length() > 0) {
            return testMachineId;
        }
        if (machineId == null) {
            machineId = "0000000000";
        }
        return machineId;
    }

    public static void setTestMachineId(String str) {
        if (str != null && str.length() > 0) {
            testMachineId = str;
        } else {
            testMachineId = "";
        }
    }

    public static void setMachineId(String str) {
        machineId = str;
        if (str.equalsIgnoreCase("0000000000")) {
            AppStatusLoger.addAppStatus(null, "SHJ", "AppSetting", "04000003", "机器号变为：0000000000");
            return;
        }
        String asString = CacheHelper.getFileCache().getAsString("machineId_last");
        if (asString != null && asString.length() > 0 && !asString.equals(str)) {
            AppStatusLoger.addAppStatus(null, "SHJ", "AppSetting", "04000002", "机器号变更：" + asString + "=>" + str);
        }
        CacheHelper.getFileCache().put("machineId_last", str);
    }

    public static String getMachineBoardVersion() {
        return machineBoardVersion;
    }

    public static void setMachineBoardVersion(String str) {
        machineBoardVersion = str;
    }

    public static int getOfferGoodsDiviceState() {
        int i = debugOfferDiviceError;
        return i > 0 ? i : goodsPickerDoorState;
    }

    public static void setOfferGoodsDiviceState(int i) {
        boolean z = goodsPickerDoorState != i;
        goodsPickerDoorState = i;
        lastGoodsPickerDoorStateUpdateTime = System.currentTimeMillis();
        if (!z || i <= 2) {
            return;
        }
        AppStatusLoger.addAppStatus_Count(null, "SHJ", AppStatusLoger.Type_OfferGoods, "08000002", "OfferGoodsDiviceState:" + getOfferGoodsDiviceStateInfo() + "(" + i + ")");
    }

    public static long getOfferGoodsDiviceStateUpdateTime() {
        try {
            if (System.currentTimeMillis() - lastCheckTime > 1000) {
                lastCheckTime = System.currentTimeMillis();
                shjListener._onNeedCheckOfferDeviceStatus();
            }
        } catch (Exception unused) {
        }
        return lastGoodsPickerDoorStateUpdateTime;
    }

    public static String getOfferGoodsDiviceStateInfo() {
        String str = "";
        try {
            String language = CommonTool.getLanguage(getContext());
            int offerGoodsDiviceState = getOfferGoodsDiviceState();
            String str2 = "błąd autokontrolny windy";
            String str3 = "Elevator self-check error";
            switch (offerGoodsDiviceState) {
                case 0:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "货道正常";
                                        break;
                                    } else {
                                        str = "컬럼 정상";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "W Normie";
                                break;
                            }
                        } else {
                            str = "Sélection disponible";
                            break;
                        }
                    } else {
                        str = "Selection normal";
                        break;
                    }
                case 1:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "货道故障";
                                        break;
                                    } else {
                                        str = "컬럼 오류";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Błąd wyboru";
                                break;
                            }
                        } else {
                            str = "Erreur de sélection";
                            break;
                        }
                    } else {
                        str = "selection error";
                        break;
                    }
                case 2:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "货道缺货";
                                        break;
                                    } else {
                                        str = "컬럼 재고부족";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Brak produktu";
                                break;
                            }
                        } else {
                            str = "Sélection en ruputre de stock";
                            break;
                        }
                    } else {
                        str = "Selection out of stock";
                        break;
                    }
                case 3:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "货道卡货";
                                        break;
                                    } else {
                                        str = "컬럼 끼임";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Wydanie Zablokowane";
                                break;
                            }
                        } else {
                            str = "Sélection coincée";
                            break;
                        }
                    } else {
                        str = "Selection jamed";
                        break;
                    }
                case 4:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "货道暂停使用";
                                        break;
                                    } else {
                                        str = "컬럼 사용 일시정지 ";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Wybór zawieszony";
                                break;
                            }
                        } else {
                            str = "Produits non encore récupérés";
                            break;
                        }
                    } else {
                        str = "Selection pause to use";
                        break;
                    }
                case 5:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "取货口商品未取出";
                                        break;
                                    } else {
                                        str = "상품이 아직 인출하지 않았습니다.";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Odbierz zakup";
                                break;
                            }
                        } else {
                            str = "Trappe de récupération ouverte";
                            break;
                        }
                    } else {
                        str = "Goods in pick up";
                        break;
                    }
                case 6:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "取货口门没关上";
                                        break;
                                    } else {
                                        str = "문이 안 닫혔습니다.";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "drzwi odbioru nie zamknięte";
                                break;
                            }
                        } else {
                            str = "Erreur de fermeture porte réceptacle";
                            break;
                        }
                    } else {
                        str = "Pick up door didn't close";
                        break;
                    }
                case 7:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "升降机故障";
                                        break;
                                    } else {
                                        str = "엘리베이터 오류";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = str2;
                                break;
                            }
                        } else {
                            str = "Erreur ascenseur";
                            break;
                        }
                    } else {
                        str = str3;
                        break;
                    }
                case 8:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "升降机自检错误";
                                        break;
                                    } else {
                                        str = "엘리베이터 자기진단- 오류";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = str2;
                                break;
                            }
                        } else {
                            str = "Erreur Auto-test ascenseur";
                            break;
                        }
                    } else {
                        str = str3;
                        break;
                    }
                case 9:
                    if (!language.equalsIgnoreCase("en")) {
                        if (!language.equalsIgnoreCase("fr")) {
                            if (!language.equalsIgnoreCase("it")) {
                                if (!language.equalsIgnoreCase("pl")) {
                                    if (!language.equalsIgnoreCase("ko")) {
                                        str = "前门关闭错误";
                                        break;
                                    } else {
                                        str = "앞 도어 닫힘 오류";
                                        break;
                                    }
                                } else {
                                    str = "Normal";
                                    break;
                                }
                            } else {
                                str = "Błąd zamknięcia drzwi przednich";
                                break;
                            }
                        } else {
                            str = "Erreur fermeture de porte";
                            break;
                        }
                    } else {
                        str = "Front door close error";
                        break;
                    }
                default:
                    str2 = "뒤 문 닫힘 오류";
                    str3 = "Błąd zamknięcia drzwi tylnych";
                    switch (offerGoodsDiviceState) {
                        case 16:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "后门打开错误";
                                                break;
                                            } else {
                                                str = str2;
                                                break;
                                            }
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    } else {
                                        str = str3;
                                        break;
                                    }
                                } else {
                                    str = "Erreur ouverture porte sas intérieur";
                                    break;
                                }
                            } else {
                                str = "Backdoor close error";
                                break;
                            }
                        case 17:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "后门关闭错误";
                                                break;
                                            }
                                            str = str2;
                                            break;
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    }
                                    str = str3;
                                    break;
                                } else {
                                    str = "Erreur fermeture porte sas intérieur";
                                    break;
                                }
                            }
                            str = "Backdoor close error";
                            break;
                        case 18:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "没检测到盒饭";
                                                break;
                                            } else {
                                                str = "음식 감지 안됨 ";
                                                break;
                                            }
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    } else {
                                        str = "Nie wykryto posiłku";
                                        break;
                                    }
                                } else {
                                    str = "Aucun plat détecté";
                                    break;
                                }
                            } else {
                                str = "No meal detecting";
                                break;
                            }
                        case 19:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "盒饭正在加热";
                                                break;
                                            } else {
                                                str = "음식 가열 중";
                                                break;
                                            }
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    } else {
                                        str = "Podgrzewam Posiłek";
                                        break;
                                    }
                                } else {
                                    str = "Réchauffement en cours";
                                    break;
                                }
                            } else {
                                str = "Heating the meal";
                                break;
                            }
                        case 20:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "前门打开错误";
                                                break;
                                            } else {
                                                str = "앞 문 열림 오류";
                                                break;
                                            }
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    } else {
                                        str = "Błąd otwarcia drzwi przednich";
                                        break;
                                    }
                                } else {
                                    str = "Erreur ouverture de porte";
                                    break;
                                }
                            } else {
                                str = "Front Door Open error";
                                break;
                            }
                        case 21:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "请取出微波炉内盒饭";
                                                break;
                                            } else {
                                                str = "전자레인지에서  음식 반출";
                                                break;
                                            }
                                        } else {
                                            str = "Normal";
                                            break;
                                        }
                                    } else {
                                        str = "Wyciągnij wszelkie metalowe objekty z mikrofalówki";
                                        break;
                                    }
                                } else {
                                    str = "Sortir le plat du micro onde";
                                    break;
                                }
                            } else {
                                str = "Take out meal from Microwave Oven";
                                break;
                            }
                        case 22:
                            if (!language.equalsIgnoreCase("en")) {
                                if (!language.equalsIgnoreCase("fr")) {
                                    if (!language.equalsIgnoreCase("it")) {
                                        if (!language.equalsIgnoreCase("pl")) {
                                            if (!language.equalsIgnoreCase("ko")) {
                                                str = "撑杆回位错误";
                                                break;
                                            } else {
                                                str = "버팀대 위치 오류";
                                                break;
                                            }
                                        }
                                        str = "Normal";
                                        break;
                                    } else {
                                        str = "Błąd pozycji";
                                        break;
                                    }
                                } else {
                                    str = "Erreur  électro piston";
                                    break;
                                }
                            } else {
                                str = "Brace position error";
                                break;
                            }
                        case 23:
                            if (!language.equalsIgnoreCase("en")) {
                                str = "主电机故障";
                                break;
                            } else {
                                str = "Lift motor error";
                                break;
                            }
                        case 24:
                            if (!language.equalsIgnoreCase("en")) {
                                str = "平移电机故障";
                                break;
                            }
                            str = "Lift motor error";
                            break;
                        case 25:
                            if (!language.equalsIgnoreCase("en")) {
                                str = "撑杆推出错误";
                                break;
                            } else {
                                str = "Lift motor push out error";
                                break;
                            }
                        default:
                            switch (offerGoodsDiviceState) {
                                case 32:
                                    if (!language.equalsIgnoreCase("en")) {
                                        str = "升降台进去微波炉一点点错误";
                                        break;
                                    } else {
                                        str = "There is a little mistake when the elevator goes into the microwave oven";
                                        break;
                                    }
                                case 33:
                                    if (!language.equalsIgnoreCase("en")) {
                                        str = "升降台出去微波炉一点点错误";
                                        break;
                                    } else {
                                        str = "There is a little mistake when the elevator goes out the microwave oven";
                                        break;
                                    }
                                case 34:
                                    if (!language.equalsIgnoreCase("en")) {
                                        str = "微波炉内推杆推出错误";
                                        break;
                                    } else {
                                        str = "Microwave pusher push out error";
                                        break;
                                    }
                                case 35:
                                    if (!language.equalsIgnoreCase("en")) {
                                        str = "微波炉内推杆收回错误";
                                        break;
                                    } else {
                                        str = "Microwave Pusher back in error";
                                        break;
                                    }
                            }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static int getMachineType() {
        return machineType;
    }

    public static void setMachineType(int i) {
        machineType = i;
    }

    public static void onDownSyn() {
        shjListener._onDownSyn();
    }

    public static void onUpdateICCardMoney(long j, String str) {
        shjListener._onUpdateICCardMoney(j, str);
    }

    public static void onNeedICCardPay(int i, String str) {
        shjListener._onNeedICCardPay(i, str, "");
    }

    public static boolean isBatchJobRunning() {
        return isBatchJobRunning;
    }

    public static void onBatchStart() {
        isBatchJobRunning = true;
        shjListener._onBatchStart();
    }

    public static void onBatchEnd() {
        shjListener._onBatchEnd();
        isBatchJobRunning = false;
    }

    public static void onDownReportSetCmd() {
        shjListener._onDownReportSetCmd();
    }

    public static boolean isResetFinished() {
        return resetFinished;
    }

    public static boolean isStoped() {
        return stoped;
    }

    public static void setStoped(boolean z) {
        stoped = z;
        AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_Serial, "09000001", "Shj.setStoped(" + z + ")");
    }

    public static void addGoodsSetCommand(int i, ShjGoodsSetting shjGoodsSetting) {
        goodsSetMap.put(Integer.valueOf(i), shjGoodsSetting);
    }

    public static void OnGoodsSetCommandResult(CommandV2 commandV2, int i, Object obj) {
        XYClass xYClass = (XYClass) commandV2.getClass().getAnnotation(XYClass.class);
        if (xYClass.KEY().equals("HEAD")) {
            int parseInt = Integer.parseInt(xYClass.VALUE().substring(2), 16);
            ShjGoodsSetting shjGoodsSetting = goodsSetMap.get(Integer.valueOf(parseInt));
            if (shjGoodsSetting != null) {
                if (shjGoodsSetting.count == 1) {
                    shjGoodsSetting.onShjGoodsSetResultListener.result(i, obj, true);
                    goodsSetMap.remove(Integer.valueOf(parseInt));
                } else {
                    shjGoodsSetting.count--;
                    shjGoodsSetting.onShjGoodsSetResultListener.result(i, obj, false);
                }
            }
        }
    }

    public static void onFrontCommandFinished(Object obj, FrontCommandFinishedListener frontCommandFinishedListener) {
        if (frontCommandFinishedListener == null) {
            return;
        }
        CommandV2_Up_Empty commandV2_Up_Empty = new CommandV2_Up_Empty();
        commandV2_Up_Empty.setParams(new Object[]{obj, frontCommandFinishedListener});
        commandV2_Up_Empty.setCommandStatusListener(new CommandStatusListener() { // from class: com.shj.Shj.6
            AnonymousClass6() {
            }

            @Override // com.shj.command.CommandStatusListener
            public void onCommandFinished(Command command) {
                Object[] objArr = (Object[]) ((CommandV2_Up_Empty) command).getObj();
                ((FrontCommandFinishedListener) objArr[1]).onFrontCommandFinished(objArr[0]);
            }

            @Override // com.shj.command.CommandStatusListener
            public void onCommandError(Command command, CommandError commandError) {
                Object[] objArr = (Object[]) ((CommandV2_Up_Empty) command).getObj();
                ((FrontCommandFinishedListener) objArr[1]).onFrontCommandFinished(objArr[0]);
            }
        });
        CommandManager.appendSendCommand(commandV2_Up_Empty);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.Shj$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements CommandStatusListener {
        AnonymousClass6() {
        }

        @Override // com.shj.command.CommandStatusListener
        public void onCommandFinished(Command command) {
            Object[] objArr = (Object[]) ((CommandV2_Up_Empty) command).getObj();
            ((FrontCommandFinishedListener) objArr[1]).onFrontCommandFinished(objArr[0]);
        }

        @Override // com.shj.command.CommandStatusListener
        public void onCommandError(Command command, CommandError commandError) {
            Object[] objArr = (Object[]) ((CommandV2_Up_Empty) command).getObj();
            ((FrontCommandFinishedListener) objArr[1]).onFrontCommandFinished(objArr[0]);
        }
    }

    public static void postSetCommand(CommandV2_Up_SetCommand commandV2_Up_SetCommand, OnCommandAnswerListener onCommandAnswerListener) {
        if (isDebug()) {
            return;
        }
        commandMap.put(Integer.valueOf(commandV2_Up_SetCommand.getCmdType()), onCommandAnswerListener);
        Loger.writeLog("SHJ", "排队命令:" + commandV2_Up_SetCommand.getClass().getName() + StringUtils.SPACE + ((int) commandV2_Up_SetCommand.getHead()));
        if (bluetooth == null) {
            CommandManager.appendSendCommand(commandV2_Up_SetCommand);
        } else {
            CommandManager.setCurrentCommand(commandV2_Up_SetCommand);
            bluetooth.writeData(commandV2_Up_SetCommand.getRawCommand());
        }
    }

    public static void onSetCommandAnswer(CommandV2_Down_CommandAnswer commandV2_Down_CommandAnswer) {
        OnCommandAnswerListener onCommandAnswerListener = commandMap.get(Integer.valueOf(commandV2_Down_CommandAnswer.getCmdType()));
        if (onCommandAnswerListener != null) {
            commandMap.remove(Integer.valueOf(commandV2_Down_CommandAnswer.getCmdType()));
            int cmdType = commandV2_Down_CommandAnswer.getCmdType();
            if (cmdType == 56 || cmdType == 80 || cmdType == 81 || cmdType == 84 || cmdType == 86 || cmdType == 88 || cmdType == 160 || cmdType == 161 || cmdType == 162) {
                onCommandAnswerListener.onCommandReadAnswer(commandV2_Down_CommandAnswer.getAnswers());
            } else if (commandV2_Down_CommandAnswer.isSetCommand()) {
                onCommandAnswerListener.onCommandSetAnswer(commandV2_Down_CommandAnswer.isSetOk());
                shjListener._onPostSetCommandAnswer(commandV2_Down_CommandAnswer);
            } else {
                onCommandAnswerListener.onCommandReadAnswer(commandV2_Down_CommandAnswer.getAnswers());
            }
        }
    }

    public static void clearCommandMap() {
        OnCommandAnswerListener onCommandAnswerListener = commandMap.get(88);
        commandMap.clear();
        if (onCommandAnswerListener != null) {
            commandMap.put(88, onCommandAnswerListener);
        }
        goodsSetMap.clear();
    }

    public static boolean hasPeopleFindDevice() {
        return hasPeopleFindDevice;
    }

    public static void onFindPeopleIn() {
        hasPeopleFindDevice = true;
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastFindPopleTime > 1500) {
            shjListener._onFindPeopleIn();
            lastFindPopleTime = currentTimeMillis;
        }
    }

    public static void onAlarm(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (i == 1) {
            shjListener._onShjAlarm(0, i);
        }
        if (i2 == 1) {
            shjListener._onShjAlarm(1, i2);
        }
        if (i3 == 1) {
            shjListener._onShjAlarm(2, i3);
        }
        if (i4 == 1) {
            shjListener._onShjAlarm(3, i4);
        }
        if (i5 == 1) {
            shjListener._onShjAlarm(4, i5);
        }
        if (i6 == 1) {
            shjListener._onShjAlarm(5, i6);
        }
        if (i7 == 1) {
            shjListener._onShjAlarm(6, i7);
        }
        if (i8 == 1) {
            shjListener._onShjAlarm(7, i8);
        }
    }

    public static void onVmcNotice(byte[] bArr) {
        CommandV2_Up_Test commandV2_Up_Test = new CommandV2_Up_Test();
        commandV2_Up_Test.setParams(bArr);
        CommandManager.appendSendCommand(commandV2_Up_Test);
    }

    public static void setPickerStatus(List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            if (aryPickers.size() <= i) {
                aryPickers.add(new Picker(i));
            }
            aryPickers.get(i).setStatus(list.get(i).intValue());
            aryPickers.get(i).setWaitTimer(0);
        }
    }

    public static boolean hasFreePicker() {
        boolean z = false;
        for (Picker picker : aryPickers) {
            Loger.writeLog("SHJ", "picker:" + picker.getId() + " status:" + picker.getStatus() + " timer:" + picker.getWaitTimer());
            z = picker.getStatus() == 0 && picker.getWaitTimer() == 0;
            if (z) {
                break;
            }
        }
        return z;
    }

    public static Picker getPicker(int i) {
        try {
            for (Picker picker : aryPickers) {
                if (picker.getId() == i) {
                    return picker;
                }
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static Picker getLastOfferGoodsPicker() {
        try {
            return aryPickers.get(lastOfferGoodsPicker - 1);
        } catch (Exception unused) {
            return null;
        }
    }

    public static void onShelfDoorStatusUpdated(int i, int i2) {
        try {
            getShelfInfo(Integer.valueOf(i));
            getShelfInfo(Integer.valueOf(i)).setDoorStatus(Integer.valueOf(i2));
            shjListener._onShelfBoorStateChanged(i, i2);
        } catch (Exception unused) {
        }
    }

    public static void setDrivedShelf(int i) {
        offerErrorReported = false;
        selectedShelf = i;
        lastDrivedShelf = i;
        lastOfferGoodsTime = System.currentTimeMillis();
        setIsOfferingGoods(true);
        lastSelectShelf = selectedShelf;
    }

    public static void onDeviceMessage(String str, String str2) {
        try {
            shjListener._onDeviceMessage(str, str2);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }
}
