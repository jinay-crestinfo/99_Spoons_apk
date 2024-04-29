package com.shj.biz;

import android.graphics.Bitmap;
import android.support.media.ExifInterface;
import android.support.v4.app.NotificationCompat;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Loger;
import com.oysb.utils.zxing.encoding.EncodingHandler;
import com.oysb.xy.net.thirdparty.OnRequestListenter;
import com.oysb.xy.net.thirdparty.ThirdPartyPay_PwApply;
import com.oysb.xy.net.thirdparty.ThirdPartyPay_PwApply2;
import com.oysb.xy.net.thirdparty.ThirdPartyPay_PwFeedback;
import com.oysb.xy.net.thirdparty.ThirdPartyPay_QRcodeApply;
import com.oysb.xy.net.thirdparty.ThirdPartyPay_QRcodeFeedback;
import com.oysb.xy.net.thirdparty.ThirdPartyPay_QRcodeQuery;
import com.oysb.xy.net.thirdparty.ThirdPartyPay_VipApply;
import com.oysb.xy.net.thirdparty.ThirdPartyPay_VipFeedback;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_ChangeMoney;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_DeviceStatus;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_DoorStatus;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_Heartbeat;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_MachineSignal;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_MachineStatus;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_MoneyBalances;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_OfferGoodsRecord;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_SetShelDropCheck;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_SetShelfCapacity;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_SetShelfCount;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_SetShelfFull;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_SetShelfGoodsCode;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_SetShelfPrice;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_SetTemperature;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_ShelfStatus;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_Shelves;
import com.oysb.xy.net.thirdparty.ThirdPartyReport_Temperature;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.biz.order.Order;
import com.shj.biz.order.OrderListener;
import com.shj.biz.order.OrderPayType;
import com.shj.biz.tools.RemoteUpdateHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ReportManager {
    private static int PackNo = 0;
    static boolean isRequest = false;
    static String machineId;
    private static SendMessageListener sendMessageListener;
    static Timer timer;

    /* loaded from: classes2.dex */
    public interface SendMessageListener {
        void getMessage(String str);
    }

    static /* synthetic */ String access$000() {
        return getPackNo();
    }

    public static void init(String str) {
        machineId = Shj.getMachineId();
        isRequest = false;
        if (str.equals("XY-MALA") || str.equals("XY-Third")) {
            isRequest = true;
        }
    }

    public static void initPayType() {
        new RemoteUpdateHelper(getPackNo()).doUpdatePayTypes();
    }

    /* renamed from: com.shj.biz.ReportManager$27 */
    /* loaded from: classes2.dex */
    static /* synthetic */ class AnonymousClass27 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$biz$order$OrderPayType;

        static {
            int[] iArr = new int[OrderPayType.values().length];
            $SwitchMap$com$shj$biz$order$OrderPayType = iArr;
            try {
                iArr[OrderPayType.ZFB.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$biz$order$OrderPayType[OrderPayType.WEIXIN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static int getToPayType(OrderPayType orderPayType) {
        int i = AnonymousClass27.$SwitchMap$com$shj$biz$order$OrderPayType[orderPayType.ordinal()];
        if (i != 1) {
            return i != 2 ? 0 : 2;
        }
        return 1;
    }

    private static String getPackNo() {
        if (PackNo == 9999) {
            PackNo = 0;
        }
        int i = PackNo + 1;
        PackNo = i;
        return String.format("%04d", Integer.valueOf(i));
    }

    private static void setPackNo(int i) {
        PackNo = i;
    }

    public static void sendHeartBeat() {
        if (isRequest && timer == null) {
            Timer timer2 = new Timer();
            timer = timer2;
            timer2.schedule(new TimerTask() { // from class: com.shj.biz.ReportManager.1
                AnonymousClass1() {
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    ThirdPartyReport_Heartbeat thirdPartyReport_Heartbeat = new ThirdPartyReport_Heartbeat();
                    thirdPartyReport_Heartbeat.setParam(ReportManager.machineId, ReportManager.access$000());
                    thirdPartyReport_Heartbeat.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.1.1
                        C00481() {
                        }

                        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                        public void onFail(String str) {
                            Loger.writeLog("APP", "心跳失败" + str);
                        }

                        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                        public void onSucces(String str) {
                            try {
                                Loger.writeLog("APP", "心跳成功" + str);
                                String string = new JSONObject(str).getString("cmdInfo");
                                if (!string.startsWith("1")) {
                                    if (string.startsWith(ExifInterface.GPS_MEASUREMENT_2D)) {
                                        if (ReportManager.timer != null) {
                                            ReportManager.timer.cancel();
                                            ReportManager.timer = null;
                                            new RemoteUpdateHelper(ReportManager.access$000()).doUpdateShelfPrice();
                                        }
                                    } else if (string.startsWith(ExifInterface.GPS_MEASUREMENT_3D)) {
                                        if (ReportManager.timer != null) {
                                            ReportManager.timer.cancel();
                                            ReportManager.timer = null;
                                            new RemoteUpdateHelper(ReportManager.access$000()).doUpdateAdImage();
                                        }
                                    } else if (string.startsWith("4") && ReportManager.timer != null) {
                                        ReportManager.timer.cancel();
                                        ReportManager.timer = null;
                                        new RemoteUpdateHelper(ReportManager.access$000()).doUpdatePayTypes();
                                    }
                                }
                            } catch (JSONException unused) {
                                ReportManager.sendHeartBeat();
                            }
                        }
                    });
                }

                /* renamed from: com.shj.biz.ReportManager$1$1 */
                /* loaded from: classes2.dex */
                class C00481 implements OnRequestListenter {
                    C00481() {
                    }

                    @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                    public void onFail(String str) {
                        Loger.writeLog("APP", "心跳失败" + str);
                    }

                    @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                    public void onSucces(String str) {
                        try {
                            Loger.writeLog("APP", "心跳成功" + str);
                            String string = new JSONObject(str).getString("cmdInfo");
                            if (!string.startsWith("1")) {
                                if (string.startsWith(ExifInterface.GPS_MEASUREMENT_2D)) {
                                    if (ReportManager.timer != null) {
                                        ReportManager.timer.cancel();
                                        ReportManager.timer = null;
                                        new RemoteUpdateHelper(ReportManager.access$000()).doUpdateShelfPrice();
                                    }
                                } else if (string.startsWith(ExifInterface.GPS_MEASUREMENT_3D)) {
                                    if (ReportManager.timer != null) {
                                        ReportManager.timer.cancel();
                                        ReportManager.timer = null;
                                        new RemoteUpdateHelper(ReportManager.access$000()).doUpdateAdImage();
                                    }
                                } else if (string.startsWith("4") && ReportManager.timer != null) {
                                    ReportManager.timer.cancel();
                                    ReportManager.timer = null;
                                    new RemoteUpdateHelper(ReportManager.access$000()).doUpdatePayTypes();
                                }
                            }
                        } catch (JSONException unused) {
                            ReportManager.sendHeartBeat();
                        }
                    }
                }
            }, 1000L, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
        }
    }

    /* renamed from: com.shj.biz.ReportManager$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ThirdPartyReport_Heartbeat thirdPartyReport_Heartbeat = new ThirdPartyReport_Heartbeat();
            thirdPartyReport_Heartbeat.setParam(ReportManager.machineId, ReportManager.access$000());
            thirdPartyReport_Heartbeat.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.1.1
                C00481() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "心跳失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "心跳成功" + str);
                        String string = new JSONObject(str).getString("cmdInfo");
                        if (!string.startsWith("1")) {
                            if (string.startsWith(ExifInterface.GPS_MEASUREMENT_2D)) {
                                if (ReportManager.timer != null) {
                                    ReportManager.timer.cancel();
                                    ReportManager.timer = null;
                                    new RemoteUpdateHelper(ReportManager.access$000()).doUpdateShelfPrice();
                                }
                            } else if (string.startsWith(ExifInterface.GPS_MEASUREMENT_3D)) {
                                if (ReportManager.timer != null) {
                                    ReportManager.timer.cancel();
                                    ReportManager.timer = null;
                                    new RemoteUpdateHelper(ReportManager.access$000()).doUpdateAdImage();
                                }
                            } else if (string.startsWith("4") && ReportManager.timer != null) {
                                ReportManager.timer.cancel();
                                ReportManager.timer = null;
                                new RemoteUpdateHelper(ReportManager.access$000()).doUpdatePayTypes();
                            }
                        }
                    } catch (JSONException unused) {
                        ReportManager.sendHeartBeat();
                    }
                }
            });
        }

        /* renamed from: com.shj.biz.ReportManager$1$1 */
        /* loaded from: classes2.dex */
        class C00481 implements OnRequestListenter {
            C00481() {
            }

            @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
            public void onFail(String str) {
                Loger.writeLog("APP", "心跳失败" + str);
            }

            @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
            public void onSucces(String str) {
                try {
                    Loger.writeLog("APP", "心跳成功" + str);
                    String string = new JSONObject(str).getString("cmdInfo");
                    if (!string.startsWith("1")) {
                        if (string.startsWith(ExifInterface.GPS_MEASUREMENT_2D)) {
                            if (ReportManager.timer != null) {
                                ReportManager.timer.cancel();
                                ReportManager.timer = null;
                                new RemoteUpdateHelper(ReportManager.access$000()).doUpdateShelfPrice();
                            }
                        } else if (string.startsWith(ExifInterface.GPS_MEASUREMENT_3D)) {
                            if (ReportManager.timer != null) {
                                ReportManager.timer.cancel();
                                ReportManager.timer = null;
                                new RemoteUpdateHelper(ReportManager.access$000()).doUpdateAdImage();
                            }
                        } else if (string.startsWith("4") && ReportManager.timer != null) {
                            ReportManager.timer.cancel();
                            ReportManager.timer = null;
                            new RemoteUpdateHelper(ReportManager.access$000()).doUpdatePayTypes();
                        }
                    }
                } catch (JSONException unused) {
                    ReportManager.sendHeartBeat();
                }
            }
        }
    }

    public static void reportShelfInfo(String str, List<HashMap<String, Object>> list) {
        if (isRequest) {
            ThirdPartyReport_Shelves thirdPartyReport_Shelves = new ThirdPartyReport_Shelves();
            thirdPartyReport_Shelves.setParams(getPackNo(), machineId, str, list);
            thirdPartyReport_Shelves.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.2
                AnonymousClass2() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str2) {
                    Loger.writeLog("APP", "上报货道失败" + str2);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str2) {
                    try {
                        Loger.writeLog("APP", "上报货道成功" + str2);
                        new JSONObject(str2).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnRequestListenter {
        AnonymousClass2() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str2) {
            Loger.writeLog("APP", "上报货道失败" + str2);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str2) {
            try {
                Loger.writeLog("APP", "上报货道成功" + str2);
                new JSONObject(str2).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportMachineStatus(String str, Map<Integer, Integer> map) {
        if (isRequest) {
            ThirdPartyReport_MachineStatus thirdPartyReport_MachineStatus = new ThirdPartyReport_MachineStatus();
            thirdPartyReport_MachineStatus.setParams(getPackNo(), machineId, str, map);
            thirdPartyReport_MachineStatus.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.3
                AnonymousClass3() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str2) {
                    Loger.writeLog("APP", "上报机器状态失败" + str2);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str2) {
                    try {
                        Loger.writeLog("APP", "上报机器状态成功" + str2);
                        new JSONObject(str2).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnRequestListenter {
        AnonymousClass3() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str2) {
            Loger.writeLog("APP", "上报机器状态失败" + str2);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str2) {
            try {
                Loger.writeLog("APP", "上报机器状态成功" + str2);
                new JSONObject(str2).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportMachineSignal(String str) {
        if (isRequest) {
            ThirdPartyReport_MachineSignal thirdPartyReport_MachineSignal = new ThirdPartyReport_MachineSignal();
            thirdPartyReport_MachineSignal.setParams(getPackNo(), machineId, str);
            thirdPartyReport_MachineSignal.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.4
                AnonymousClass4() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str2) {
                    Loger.writeLog("APP", "上报机器信号位置失败" + str2);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str2) {
                    try {
                        Loger.writeLog("APP", "上报机器信号位置成功" + str2);
                        new JSONObject(str2).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements OnRequestListenter {
        AnonymousClass4() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str2) {
            Loger.writeLog("APP", "上报机器信号位置失败" + str2);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str2) {
            try {
                Loger.writeLog("APP", "上报机器信号位置成功" + str2);
                new JSONObject(str2).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportSetPrice(int i, int i2, int i3) {
        if (isRequest) {
            ThirdPartyReport_SetShelfPrice thirdPartyReport_SetShelfPrice = new ThirdPartyReport_SetShelfPrice();
            thirdPartyReport_SetShelfPrice.setParams(getPackNo(), machineId, i, i2, i3);
            thirdPartyReport_SetShelfPrice.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.5
                AnonymousClass5() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "上报价格设置失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "上报价格设置成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements OnRequestListenter {
        AnonymousClass5() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "上报价格设置失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "上报价格设置成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportSetCount(int i, int i2, int i3) {
        if (isRequest) {
            ThirdPartyReport_SetShelfCount thirdPartyReport_SetShelfCount = new ThirdPartyReport_SetShelfCount();
            thirdPartyReport_SetShelfCount.setParams(getPackNo(), machineId, i, i2, i3);
            thirdPartyReport_SetShelfCount.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.6
                AnonymousClass6() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "上报库存设置失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "上报库存设置成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements OnRequestListenter {
        AnonymousClass6() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "上报库存设置失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "上报库存设置成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportSetCapacity(int i, int i2, int i3) {
        if (isRequest) {
            ThirdPartyReport_SetShelfCapacity thirdPartyReport_SetShelfCapacity = new ThirdPartyReport_SetShelfCapacity();
            thirdPartyReport_SetShelfCapacity.setParams(getPackNo(), machineId, i, i2, i3);
            thirdPartyReport_SetShelfCapacity.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.7
                AnonymousClass7() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "上报容量设置失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "上报容量设置成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements OnRequestListenter {
        AnonymousClass7() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "上报容量设置失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "上报容量设置成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportSetGoodsCode(int i, int i2, String str) {
        if (isRequest) {
            ThirdPartyReport_SetShelfGoodsCode thirdPartyReport_SetShelfGoodsCode = new ThirdPartyReport_SetShelfGoodsCode();
            thirdPartyReport_SetShelfGoodsCode.setParams(getPackNo(), machineId, i, i2, str);
            thirdPartyReport_SetShelfGoodsCode.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.8
                AnonymousClass8() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str2) {
                    Loger.writeLog("APP", "上报商品编码设置失败" + str2);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str2) {
                    try {
                        Loger.writeLog("APP", "上报商品编码设置成功" + str2);
                        new JSONObject(str2).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements OnRequestListenter {
        AnonymousClass8() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str2) {
            Loger.writeLog("APP", "上报商品编码设置失败" + str2);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str2) {
            try {
                Loger.writeLog("APP", "上报商品编码设置成功" + str2);
                new JSONObject(str2).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportSetFull() {
        if (isRequest) {
            ThirdPartyReport_SetShelfFull thirdPartyReport_SetShelfFull = new ThirdPartyReport_SetShelfFull();
            thirdPartyReport_SetShelfFull.setParams(getPackNo(), machineId);
            thirdPartyReport_SetShelfFull.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.9
                AnonymousClass9() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "上报满货设置失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "上报满货设置成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.biz.ReportManager$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements OnRequestListenter {
        AnonymousClass9() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "上报满货设置失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "上报满货设置成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportSetDropCheck(int i, int i2, int i3) {
        if (isRequest) {
            ThirdPartyReport_SetShelDropCheck thirdPartyReport_SetShelDropCheck = new ThirdPartyReport_SetShelDropCheck();
            thirdPartyReport_SetShelDropCheck.setParams(getPackNo(), machineId, i, i2, i3);
            thirdPartyReport_SetShelDropCheck.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.10
                AnonymousClass10() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "上报掉货检测设置失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "上报掉货检测设置成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.biz.ReportManager$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 implements OnRequestListenter {
        AnonymousClass10() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "上报掉货检测设置失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "上报掉货检测设置成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportSetTemperature(int i, int i2) {
        if (isRequest) {
            ThirdPartyReport_SetTemperature thirdPartyReport_SetTemperature = new ThirdPartyReport_SetTemperature();
            thirdPartyReport_SetTemperature.setParams(getPackNo(), machineId, i, i2);
            thirdPartyReport_SetTemperature.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.11
                AnonymousClass11() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "上报温度设置失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "上报温度设置成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.biz.ReportManager$11 */
    /* loaded from: classes2.dex */
    public class AnonymousClass11 implements OnRequestListenter {
        AnonymousClass11() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "上报温度设置失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "上报温度设置成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportDoorStatus(int i, int i2) {
        if (isRequest) {
            ThirdPartyReport_DoorStatus thirdPartyReport_DoorStatus = new ThirdPartyReport_DoorStatus();
            thirdPartyReport_DoorStatus.setParams(getPackNo(), machineId, i, i2);
            thirdPartyReport_DoorStatus.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.12
                AnonymousClass12() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "上报门状态失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "上报门状态成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$12 */
    /* loaded from: classes2.dex */
    public class AnonymousClass12 implements OnRequestListenter {
        AnonymousClass12() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "上报门状态失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "上报门状态成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportShelfStatus(int i, int i2) {
        if (isRequest) {
            ThirdPartyReport_ShelfStatus thirdPartyReport_ShelfStatus = new ThirdPartyReport_ShelfStatus();
            thirdPartyReport_ShelfStatus.setParams(getPackNo(), machineId, i, i2);
            thirdPartyReport_ShelfStatus.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.13
                AnonymousClass13() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "上报货道状态失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "上报货道状态成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$13 */
    /* loaded from: classes2.dex */
    public class AnonymousClass13 implements OnRequestListenter {
        AnonymousClass13() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "上报货道状态失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "上报货道状态成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportTemperature(int i, int i2, int i3) {
        if (isRequest) {
            ThirdPartyReport_Temperature thirdPartyReport_Temperature = new ThirdPartyReport_Temperature();
            thirdPartyReport_Temperature.setParams(getPackNo(), machineId, i, i2, i3);
            thirdPartyReport_Temperature.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.14
                AnonymousClass14() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "上报温度信息失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "上报温度信息成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$14 */
    /* loaded from: classes2.dex */
    public class AnonymousClass14 implements OnRequestListenter {
        AnonymousClass14() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "上报温度信息失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "上报温度信息成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportDeviceStatus(int i, int i2) {
        if (isRequest) {
            ThirdPartyReport_DeviceStatus thirdPartyReport_DeviceStatus = new ThirdPartyReport_DeviceStatus();
            thirdPartyReport_DeviceStatus.setParams(getPackNo(), machineId, i, i2);
            thirdPartyReport_DeviceStatus.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.15
                AnonymousClass15() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "上报设备状态失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "上报设备状态成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$15 */
    /* loaded from: classes2.dex */
    public class AnonymousClass15 implements OnRequestListenter {
        AnonymousClass15() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "上报设备状态失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "上报设备状态成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportChangeMoney(int i, int i2) {
        if (isRequest) {
            ThirdPartyReport_ChangeMoney thirdPartyReport_ChangeMoney = new ThirdPartyReport_ChangeMoney();
            thirdPartyReport_ChangeMoney.setParams(getPackNo(), machineId, i, i2);
            thirdPartyReport_ChangeMoney.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.16
                AnonymousClass16() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "上报可用零钱失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "上报可用零钱成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$16 */
    /* loaded from: classes2.dex */
    public class AnonymousClass16 implements OnRequestListenter {
        AnonymousClass16() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "上报可用零钱失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "上报可用零钱成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportMoneyBalances(int i, int i2, int i3, int i4, int i5) {
        if (isRequest) {
            ThirdPartyReport_MoneyBalances thirdPartyReport_MoneyBalances = new ThirdPartyReport_MoneyBalances();
            thirdPartyReport_MoneyBalances.setParam(machineId, getPackNo(), i, i2, i3, i4, i5);
            thirdPartyReport_MoneyBalances.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.17
                AnonymousClass17() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "钱币收支失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "钱币收支成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$17 */
    /* loaded from: classes2.dex */
    public class AnonymousClass17 implements OnRequestListenter {
        AnonymousClass17() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "钱币收支失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "钱币收支成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void reportOfferGoodsRecord(int i, int i2, int i3, String str, int i4, int i5, int i6, String str2) {
        if (isRequest) {
            ThirdPartyReport_OfferGoodsRecord thirdPartyReport_OfferGoodsRecord = new ThirdPartyReport_OfferGoodsRecord();
            thirdPartyReport_OfferGoodsRecord.setParam(machineId, getPackNo(), i, i2, i3, str, i4, i5, i6, str2);
            thirdPartyReport_OfferGoodsRecord.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.18
                AnonymousClass18() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str3) {
                    Loger.writeLog("APP", "出货记录失败" + str3);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str3) {
                    try {
                        Loger.writeLog("APP", "出货记录成功" + str3);
                        new JSONObject(str3).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.biz.ReportManager$18 */
    /* loaded from: classes2.dex */
    public class AnonymousClass18 implements OnRequestListenter {
        AnonymousClass18() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str3) {
            Loger.writeLog("APP", "出货记录失败" + str3);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str3) {
            try {
                Loger.writeLog("APP", "出货记录成功" + str3);
                new JSONObject(str3).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void requestQrcode(Order order, SendMessageListener sendMessageListener2) {
        if (isRequest) {
            sendMessageListener = sendMessageListener2;
            String packNo = getPackNo();
            ThirdPartyOrderManager.setCurrentKey(packNo);
            ThirdPartyOrderManager.packNumToOrder(packNo, order);
            ThirdPartyPay_QRcodeApply thirdPartyPay_QRcodeApply = new ThirdPartyPay_QRcodeApply();
            thirdPartyPay_QRcodeApply.setParam(machineId, packNo, order.getPayType().getIndex(), order.getShelf(), order.getGoodsCode(), order.getPrice());
            thirdPartyPay_QRcodeApply.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.19
                AnonymousClass19() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    try {
                        Loger.writeLog("APP", "二维码申请失败" + str);
                        JSONObject jSONObject = new JSONObject(str);
                        if (ReportManager.sendMessageListener != null) {
                            ReportManager.sendMessageListener.getMessage(jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "二维码申请成功" + str);
                        JSONObject jSONObject = new JSONObject(str);
                        if (!jSONObject.getString("status").equals("0")) {
                            if (ReportManager.sendMessageListener != null) {
                                ReportManager.sendMessageListener.getMessage(jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE));
                                return;
                            }
                            return;
                        }
                        Order packNumFromOrder = ThirdPartyOrderManager.packNumFromOrder(jSONObject.getString("PackNo"));
                        ThirdPartyOrderManager.removeOrder(jSONObject.getString("PackNo"));
                        if (packNumFromOrder == null) {
                            return;
                        }
                        packNumFromOrder.setPayId(jSONObject.getString("serialno"));
                        Bitmap createQRCode = EncodingHandler.createQRCode(jSONObject.getString("qrcodeurl"), 300);
                        OrderListener orderListener = ShjManager.getOrderListener();
                        if (orderListener != null) {
                            orderListener.onQrCodeImageCreated(packNumFromOrder, packNumFromOrder.getPayType(), createQRCode);
                        }
                        ThirdPartyOrderManager.setCurrentKey("");
                        ReportManager.requestQrcodeQuery(packNumFromOrder);
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.biz.ReportManager$19 */
    /* loaded from: classes2.dex */
    public class AnonymousClass19 implements OnRequestListenter {
        AnonymousClass19() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            try {
                Loger.writeLog("APP", "二维码申请失败" + str);
                JSONObject jSONObject = new JSONObject(str);
                if (ReportManager.sendMessageListener != null) {
                    ReportManager.sendMessageListener.getMessage(jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "二维码申请成功" + str);
                JSONObject jSONObject = new JSONObject(str);
                if (!jSONObject.getString("status").equals("0")) {
                    if (ReportManager.sendMessageListener != null) {
                        ReportManager.sendMessageListener.getMessage(jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE));
                        return;
                    }
                    return;
                }
                Order packNumFromOrder = ThirdPartyOrderManager.packNumFromOrder(jSONObject.getString("PackNo"));
                ThirdPartyOrderManager.removeOrder(jSONObject.getString("PackNo"));
                if (packNumFromOrder == null) {
                    return;
                }
                packNumFromOrder.setPayId(jSONObject.getString("serialno"));
                Bitmap createQRCode = EncodingHandler.createQRCode(jSONObject.getString("qrcodeurl"), 300);
                OrderListener orderListener = ShjManager.getOrderListener();
                if (orderListener != null) {
                    orderListener.onQrCodeImageCreated(packNumFromOrder, packNumFromOrder.getPayType(), createQRCode);
                }
                ThirdPartyOrderManager.setCurrentKey("");
                ReportManager.requestQrcodeQuery(packNumFromOrder);
            } catch (JSONException unused) {
            }
        }
    }

    public static void requestQrcodeQuery(Order order) {
        if (isRequest) {
            String packNo = getPackNo();
            ThirdPartyOrderManager.setCurrentKey(packNo);
            ThirdPartyOrderManager.packNumToOrder(packNo, order);
            ThirdPartyPay_QRcodeQuery thirdPartyPay_QRcodeQuery = new ThirdPartyPay_QRcodeQuery();
            thirdPartyPay_QRcodeQuery.setParam(machineId, packNo, order.getPayId());
            thirdPartyPay_QRcodeQuery.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.20
                AnonymousClass20() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "二维码查询失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    Order packNumFromOrder;
                    try {
                        Loger.writeLog("APP", "二维码查询成功" + str);
                        JSONObject jSONObject = new JSONObject(str);
                        if (jSONObject.getString("status").equals("0") && jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("1") && (packNumFromOrder = ThirdPartyOrderManager.packNumFromOrder(jSONObject.getString("PackNo"))) != null) {
                            OrderListener orderListener = ShjManager.getOrderListener();
                            if (orderListener != null) {
                                orderListener.onPaySuccess(packNumFromOrder, packNumFromOrder.getPayType());
                            }
                            ShjManager.getOrderManager().driverThirdPayOrder(packNumFromOrder.getPayId(), packNumFromOrder.getPrice(), packNumFromOrder.getPayType(), packNumFromOrder.getPayId(), packNumFromOrder.getShelf() + "");
                        }
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.biz.ReportManager$20 */
    /* loaded from: classes2.dex */
    public class AnonymousClass20 implements OnRequestListenter {
        AnonymousClass20() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "二维码查询失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            Order packNumFromOrder;
            try {
                Loger.writeLog("APP", "二维码查询成功" + str);
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.getString("status").equals("0") && jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("1") && (packNumFromOrder = ThirdPartyOrderManager.packNumFromOrder(jSONObject.getString("PackNo"))) != null) {
                    OrderListener orderListener = ShjManager.getOrderListener();
                    if (orderListener != null) {
                        orderListener.onPaySuccess(packNumFromOrder, packNumFromOrder.getPayType());
                    }
                    ShjManager.getOrderManager().driverThirdPayOrder(packNumFromOrder.getPayId(), packNumFromOrder.getPrice(), packNumFromOrder.getPayType(), packNumFromOrder.getPayId(), packNumFromOrder.getShelf() + "");
                }
            } catch (JSONException unused) {
            }
        }
    }

    public static void requestQrcodeFeedBack(Order order, int i) {
        if (isRequest) {
            ThirdPartyPay_QRcodeFeedback thirdPartyPay_QRcodeFeedback = new ThirdPartyPay_QRcodeFeedback();
            thirdPartyPay_QRcodeFeedback.setParam(machineId, getPackNo(), order.getPayId(), order.getShelf(), order.getGoodsCode(), i);
            thirdPartyPay_QRcodeFeedback.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.21
                AnonymousClass21() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "二维码回馈失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "二维码回馈成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.biz.ReportManager$21 */
    /* loaded from: classes2.dex */
    public class AnonymousClass21 implements OnRequestListenter {
        AnonymousClass21() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "二维码回馈失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "二维码回馈成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void requestPwApply(String str, Order order, SendMessageListener sendMessageListener2) {
        if (isRequest) {
            sendMessageListener = sendMessageListener2;
            String packNo = getPackNo();
            ThirdPartyOrderManager.packNumToOrder(packNo, order);
            ThirdPartyPay_PwApply thirdPartyPay_PwApply = new ThirdPartyPay_PwApply();
            thirdPartyPay_PwApply.setParam(machineId, packNo, str, order.getShelf(), order.getGoodsCode(), order.getPrice());
            thirdPartyPay_PwApply.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.22
                AnonymousClass22() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str2) {
                    try {
                        Loger.writeLog("APP", "密码支付申请失败" + str2);
                        JSONObject jSONObject = new JSONObject(str2);
                        if (ReportManager.sendMessageListener != null) {
                            ReportManager.sendMessageListener.getMessage(jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str2) {
                    try {
                        Loger.writeLog("APP", "密码支付申请成功" + str2);
                        JSONObject jSONObject = new JSONObject(str2);
                        if (jSONObject.getString("status").equals("0")) {
                            if (!jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("0")) {
                                if (ReportManager.sendMessageListener != null) {
                                    ReportManager.sendMessageListener.getMessage(jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("1") ? "密码错误" : "未知错误");
                                }
                            } else {
                                Order packNumFromOrder = ThirdPartyOrderManager.packNumFromOrder(jSONObject.getString("PackNo"));
                                if (packNumFromOrder == null) {
                                    return;
                                }
                                packNumFromOrder.setPayId(jSONObject.getString("serialno"));
                                ShjManager.getOrderManager().driverThirdPayOrder(packNumFromOrder.getPayId(), packNumFromOrder.getPrice(), packNumFromOrder.getPayType(), packNumFromOrder.getPayId(), packNumFromOrder.getShelf() + "");
                            }
                        }
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.biz.ReportManager$22 */
    /* loaded from: classes2.dex */
    public class AnonymousClass22 implements OnRequestListenter {
        AnonymousClass22() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str2) {
            try {
                Loger.writeLog("APP", "密码支付申请失败" + str2);
                JSONObject jSONObject = new JSONObject(str2);
                if (ReportManager.sendMessageListener != null) {
                    ReportManager.sendMessageListener.getMessage(jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str2) {
            try {
                Loger.writeLog("APP", "密码支付申请成功" + str2);
                JSONObject jSONObject = new JSONObject(str2);
                if (jSONObject.getString("status").equals("0")) {
                    if (!jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("0")) {
                        if (ReportManager.sendMessageListener != null) {
                            ReportManager.sendMessageListener.getMessage(jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("1") ? "密码错误" : "未知错误");
                        }
                    } else {
                        Order packNumFromOrder = ThirdPartyOrderManager.packNumFromOrder(jSONObject.getString("PackNo"));
                        if (packNumFromOrder == null) {
                            return;
                        }
                        packNumFromOrder.setPayId(jSONObject.getString("serialno"));
                        ShjManager.getOrderManager().driverThirdPayOrder(packNumFromOrder.getPayId(), packNumFromOrder.getPrice(), packNumFromOrder.getPayType(), packNumFromOrder.getPayId(), packNumFromOrder.getShelf() + "");
                    }
                }
            } catch (JSONException unused) {
            }
        }
    }

    public static void requestPwApply2(String str, Order order, SendMessageListener sendMessageListener2) {
        if (isRequest) {
            sendMessageListener = sendMessageListener2;
            String packNo = getPackNo();
            ThirdPartyOrderManager.packNumToOrder(packNo, order);
            ThirdPartyPay_PwApply2 thirdPartyPay_PwApply2 = new ThirdPartyPay_PwApply2();
            thirdPartyPay_PwApply2.setParam(machineId, packNo, str);
            thirdPartyPay_PwApply2.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.23
                AnonymousClass23() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str2) {
                    try {
                        Loger.writeLog("APP", "密码支付2申请失败" + str2);
                        JSONObject jSONObject = new JSONObject(str2);
                        if (ReportManager.sendMessageListener != null) {
                            ReportManager.sendMessageListener.getMessage(jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str2) {
                    try {
                        Loger.writeLog("APP", "密码支付2申请成功" + str2);
                        JSONObject jSONObject = new JSONObject(str2);
                        if (jSONObject.getString("status").equals("0")) {
                            if (!jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("0")) {
                                if (ReportManager.sendMessageListener != null) {
                                    ReportManager.sendMessageListener.getMessage(jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("1") ? "密码错误" : "未知错误");
                                    return;
                                }
                                return;
                            }
                            Order packNumFromOrder = ThirdPartyOrderManager.packNumFromOrder(jSONObject.getString("PackNo"));
                            if (packNumFromOrder == null) {
                                return;
                            }
                            String string = jSONObject.getString("serialno");
                            int parseInt = Integer.parseInt(jSONObject.getString("slct"));
                            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(parseInt));
                            if (shelfInfo == null) {
                                if (ReportManager.sendMessageListener != null) {
                                    ReportManager.sendMessageListener.getMessage("机器无此货道");
                                }
                                ReportManager.requestPwFeedBack(packNumFromOrder, 1);
                                return;
                            }
                            if (shelfInfo.getGoodsCount().intValue() <= 0) {
                                if (ReportManager.sendMessageListener != null) {
                                    ReportManager.sendMessageListener.getMessage("此货道库存不足");
                                }
                                ReportManager.requestPwFeedBack(packNumFromOrder, 1);
                                return;
                            }
                            packNumFromOrder.setPayId(string);
                            packNumFromOrder.setShelf(parseInt);
                            ShjManager.getOrderManager().driverThirdPayOrder(packNumFromOrder.getPayId(), packNumFromOrder.getPrice(), packNumFromOrder.getPayType(), packNumFromOrder.getPayId(), parseInt + "");
                        }
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.biz.ReportManager$23 */
    /* loaded from: classes2.dex */
    class AnonymousClass23 implements OnRequestListenter {
        AnonymousClass23() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str2) {
            try {
                Loger.writeLog("APP", "密码支付2申请失败" + str2);
                JSONObject jSONObject = new JSONObject(str2);
                if (ReportManager.sendMessageListener != null) {
                    ReportManager.sendMessageListener.getMessage(jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str2) {
            try {
                Loger.writeLog("APP", "密码支付2申请成功" + str2);
                JSONObject jSONObject = new JSONObject(str2);
                if (jSONObject.getString("status").equals("0")) {
                    if (!jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("0")) {
                        if (ReportManager.sendMessageListener != null) {
                            ReportManager.sendMessageListener.getMessage(jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("1") ? "密码错误" : "未知错误");
                            return;
                        }
                        return;
                    }
                    Order packNumFromOrder = ThirdPartyOrderManager.packNumFromOrder(jSONObject.getString("PackNo"));
                    if (packNumFromOrder == null) {
                        return;
                    }
                    String string = jSONObject.getString("serialno");
                    int parseInt = Integer.parseInt(jSONObject.getString("slct"));
                    ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(parseInt));
                    if (shelfInfo == null) {
                        if (ReportManager.sendMessageListener != null) {
                            ReportManager.sendMessageListener.getMessage("机器无此货道");
                        }
                        ReportManager.requestPwFeedBack(packNumFromOrder, 1);
                        return;
                    }
                    if (shelfInfo.getGoodsCount().intValue() <= 0) {
                        if (ReportManager.sendMessageListener != null) {
                            ReportManager.sendMessageListener.getMessage("此货道库存不足");
                        }
                        ReportManager.requestPwFeedBack(packNumFromOrder, 1);
                        return;
                    }
                    packNumFromOrder.setPayId(string);
                    packNumFromOrder.setShelf(parseInt);
                    ShjManager.getOrderManager().driverThirdPayOrder(packNumFromOrder.getPayId(), packNumFromOrder.getPrice(), packNumFromOrder.getPayType(), packNumFromOrder.getPayId(), parseInt + "");
                }
            } catch (JSONException unused) {
            }
        }
    }

    public static void requestPwFeedBack(Order order, int i) {
        if (isRequest) {
            ThirdPartyPay_PwFeedback thirdPartyPay_PwFeedback = new ThirdPartyPay_PwFeedback();
            thirdPartyPay_PwFeedback.setParam(machineId, getPackNo(), order.getPayId(), order.getShelf(), order.getGoodsCode(), i);
            thirdPartyPay_PwFeedback.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.24
                AnonymousClass24() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "密码支付回馈失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "密码支付回馈成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.biz.ReportManager$24 */
    /* loaded from: classes2.dex */
    public class AnonymousClass24 implements OnRequestListenter {
        AnonymousClass24() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "密码支付回馈失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "密码支付回馈成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }

    public static void requestVipApply(String str, Order order, SendMessageListener sendMessageListener2) {
        if (isRequest) {
            sendMessageListener = sendMessageListener2;
            String packNo = getPackNo();
            order.setPayId(str);
            ThirdPartyOrderManager.packNumToOrder(packNo, order);
            ThirdPartyPay_VipApply thirdPartyPay_VipApply = new ThirdPartyPay_VipApply();
            thirdPartyPay_VipApply.setParam(machineId, packNo, str, order.getShelf(), order.getGoodsCode(), order.getPrice());
            thirdPartyPay_VipApply.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.25
                AnonymousClass25() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str2) {
                    try {
                        Loger.writeLog("APP", "会员卡支付申请失败" + str2);
                        JSONObject jSONObject = new JSONObject(str2);
                        if (ReportManager.sendMessageListener != null) {
                            ReportManager.sendMessageListener.getMessage(jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str2) {
                    try {
                        Loger.writeLog("APP", "会员卡支付申请成功" + str2);
                        JSONObject jSONObject = new JSONObject(str2);
                        if (jSONObject.getString("status").equals("0")) {
                            if (!jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("1")) {
                                if (ReportManager.sendMessageListener != null) {
                                    ReportManager.sendMessageListener.getMessage(jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("1") ? "扣款失败" : "未知错误");
                                }
                            } else {
                                Order packNumFromOrder = ThirdPartyOrderManager.packNumFromOrder(jSONObject.getString("PackNo"));
                                if (packNumFromOrder == null) {
                                    return;
                                }
                                packNumFromOrder.setPayId(jSONObject.getString("serialno") + VoiceWakeuperAidl.PARAMS_SEPARATE + packNumFromOrder.getPayId());
                                ShjManager.getOrderManager().driverThirdPayOrder(packNumFromOrder.getPayId(), packNumFromOrder.getPrice(), packNumFromOrder.getPayType(), packNumFromOrder.getPayId(), packNumFromOrder.getShelf() + "");
                            }
                        }
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.biz.ReportManager$25 */
    /* loaded from: classes2.dex */
    public class AnonymousClass25 implements OnRequestListenter {
        AnonymousClass25() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str2) {
            try {
                Loger.writeLog("APP", "会员卡支付申请失败" + str2);
                JSONObject jSONObject = new JSONObject(str2);
                if (ReportManager.sendMessageListener != null) {
                    ReportManager.sendMessageListener.getMessage(jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str2) {
            try {
                Loger.writeLog("APP", "会员卡支付申请成功" + str2);
                JSONObject jSONObject = new JSONObject(str2);
                if (jSONObject.getString("status").equals("0")) {
                    if (!jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("1")) {
                        if (ReportManager.sendMessageListener != null) {
                            ReportManager.sendMessageListener.getMessage(jSONObject.getString(SpeechUtility.TAG_RESOURCE_RESULT).equals("1") ? "扣款失败" : "未知错误");
                        }
                    } else {
                        Order packNumFromOrder = ThirdPartyOrderManager.packNumFromOrder(jSONObject.getString("PackNo"));
                        if (packNumFromOrder == null) {
                            return;
                        }
                        packNumFromOrder.setPayId(jSONObject.getString("serialno") + VoiceWakeuperAidl.PARAMS_SEPARATE + packNumFromOrder.getPayId());
                        ShjManager.getOrderManager().driverThirdPayOrder(packNumFromOrder.getPayId(), packNumFromOrder.getPrice(), packNumFromOrder.getPayType(), packNumFromOrder.getPayId(), packNumFromOrder.getShelf() + "");
                    }
                }
            } catch (JSONException unused) {
            }
        }
    }

    public static void requestVipFeedBack(Order order, int i) {
        if (isRequest) {
            String[] split = order.getPayId().split(VoiceWakeuperAidl.PARAMS_SEPARATE);
            ThirdPartyPay_VipFeedback thirdPartyPay_VipFeedback = new ThirdPartyPay_VipFeedback();
            thirdPartyPay_VipFeedback.setParam(machineId, getPackNo(), split[0], split[1], order.getShelf(), order.getGoodsCode(), i);
            thirdPartyPay_VipFeedback.request(new OnRequestListenter() { // from class: com.shj.biz.ReportManager.26
                AnonymousClass26() {
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onFail(String str) {
                    Loger.writeLog("APP", "会员卡支付回馈失败" + str);
                }

                @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
                public void onSucces(String str) {
                    try {
                        Loger.writeLog("APP", "会员卡支付回馈成功" + str);
                        new JSONObject(str).getString("status").equals("0");
                    } catch (JSONException unused) {
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.biz.ReportManager$26 */
    /* loaded from: classes2.dex */
    public class AnonymousClass26 implements OnRequestListenter {
        AnonymousClass26() {
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onFail(String str) {
            Loger.writeLog("APP", "会员卡支付回馈失败" + str);
        }

        @Override // com.oysb.xy.net.thirdparty.OnRequestListenter
        public void onSucces(String str) {
            try {
                Loger.writeLog("APP", "会员卡支付回馈成功" + str);
                new JSONObject(str).getString("status").equals("0");
            } catch (JSONException unused) {
            }
        }
    }
}
