package com.shj.device.lfpos;

import android.support.media.ExifInterface;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.oysb.utils.Loger;
import com.shj.device.lfpos.command.CommandManager;
import com.shj.device.lfpos.command.Command_Up_ClearLogs;
import com.shj.device.lfpos.command.Command_Up_ClearRecords;
import com.shj.device.lfpos.command.Command_Up_Nak;
import com.shj.device.lfpos.command.Command_Up_Pay;
import com.shj.device.lfpos.command.Command_Up_Pay_Bank;
import com.shj.device.lfpos.command.Command_Up_Pay_Pos;
import com.shj.device.lfpos.command.Command_Up_Properties;
import com.shj.device.lfpos.command.Command_Up_Search;
import com.shj.device.lfpos.command.Command_Up_Server;
import com.shj.device.lfpos.command.Command_Up_Sign;
import com.shj.device.lfpos.command.Command_Up_Test;
import java.util.HashMap;
import java.util.Timer;

/* loaded from: classes2.dex */
public class LfPos {
    private static String childVersion = null;
    private static long comBaudrate = 57600;
    private static String comPath = "/dev/ttyS2";
    private static LfPosPayInfo currentPayInfo = null;
    private static LfPosSignInfo currentSignInfo = null;
    private static LfPosListener lfPosListener = null;
    private static LfPosServerListener lfPosServerListener = null;
    private static String masterVersion = null;
    private static String name_en = null;
    private static String name_zh = null;
    private static String posCompnay = null;
    private static String posSerial = null;
    private static String posType = null;
    private static String posid = "12340026";
    private static byte[] serverMsgCode = null;
    private static String shjLab = "123456";
    private static String user = "";
    private static Integer currentID = 0;
    private static boolean signed = false;
    private static String payType = "01;02;03;";
    private static int onLineCardPrice = 0;
    private static int onLineCardShelf = 0;
    private static int onLineCardSn = 0;
    private static Timer timer = null;
    static long singTime = 0;

    public static void cmd_Records(int i) {
    }

    public static void cmd_Sum(int i) {
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.Integer getNextID() {
        /*
            java.lang.String r0 = "lfPosV1_g_serial_1"
            r1 = 1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r1)
            com.oysb.utils.cache.ACache r3 = com.oysb.utils.cache.CacheHelper.getFileCache()     // Catch: java.lang.Exception -> L24
            java.lang.Object r3 = r3.getAsObject(r0)     // Catch: java.lang.Exception -> L24
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch: java.lang.Exception -> L24
            if (r3 != 0) goto L18
            r4 = 0
            java.lang.Integer r3 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Exception -> L22
        L18:
            int r4 = r3.intValue()     // Catch: java.lang.Exception -> L22
            int r4 = r4 + r1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Exception -> L22
            goto L2a
        L22:
            r1 = move-exception
            goto L26
        L24:
            r1 = move-exception
            r3 = r2
        L26:
            com.oysb.utils.Loger.safe_inner_exception_catch(r1)
            r1 = r3
        L2a:
            int r1 = r1.intValue()
            int r1 = r1 % 1000
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            int r3 = r1.intValue()
            if (r3 != 0) goto L3b
            goto L3c
        L3b:
            r2 = r1
        L3c:
            com.oysb.utils.cache.ACache r1 = com.oysb.utils.cache.CacheHelper.getFileCache()
            r1.put(r0, r2)
            long r0 = java.lang.System.currentTimeMillis()
            r3 = 1000(0x3e8, double:4.94E-321)
            long r0 = r0 / r3
            int r2 = r2.intValue()
            long r2 = (long) r2
            long r0 = r0 + r2
            int r1 = (int) r0
            java.lang.Integer r0 = java.lang.Integer.valueOf(r1)
            com.shj.device.lfpos.LfPos.currentID = r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.device.lfpos.LfPos.getNextID():java.lang.Integer");
    }

    public static Integer getCurrentID() {
        return currentID;
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

    public static String getUser() {
        return user;
    }

    public static void setUser(String str) {
        user = str;
    }

    public static String getposid() {
        return posid;
    }

    public static void setposid(String str) {
        posid = str;
    }

    public static String getShjLab() {
        return shjLab;
    }

    public static void setShjLab(String str) {
        shjLab = str;
    }

    public static String getPosCompnay() {
        return posCompnay;
    }

    public static String getPosType() {
        return posType;
    }

    public static String getPosSerial() {
        return posSerial;
    }

    public static String getMasterVersion() {
        return masterVersion;
    }

    public static String getChildVersion() {
        return childVersion;
    }

    public static void Sign() {
        singTime = System.currentTimeMillis();
        LfPosSignInfo lfPosSignInfo = new LfPosSignInfo();
        currentSignInfo = lfPosSignInfo;
        lfPosSignInfo.setId(getNextID());
        currentSignInfo.setStep(LfPosSignStep.TEST);
        cmd_Test(currentSignInfo.getId().intValue());
    }

    public static boolean isPaying() {
        return currentPayInfo != null;
    }

    public static void startPay(int i) {
        if (currentPayInfo != null) {
            cancelPay();
        }
        LfPosPayInfo lfPosPayInfo = new LfPosPayInfo();
        currentPayInfo = lfPosPayInfo;
        lfPosPayInfo.setMoney(Integer.valueOf(i));
        currentPayInfo.setId(getNextID());
        currentPayInfo.setStep(LfPosPayStep.TEST);
        cmd_Test(currentPayInfo.getId().intValue());
    }

    public static void cancelPay() {
        try {
            cmd_Nak(currentPayInfo.getId().intValue());
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }

    public static void qureyCardBalance() {
        if (currentPayInfo != null) {
            cancelPay();
        }
        LfPosPayInfo lfPosPayInfo = new LfPosPayInfo();
        currentPayInfo = lfPosPayInfo;
        lfPosPayInfo.setMoney(0);
        currentPayInfo.setQueryBalance(true);
        currentPayInfo.setId(getNextID());
        currentPayInfo.setStep(LfPosPayStep.TEST);
        cmd_Test(currentPayInfo.getId().intValue());
    }

    public static void onQueryCardBalanceFinished(boolean z, String str) {
        Loger.writeLog("LFPOS", z ? "查询成功" : "查询失败");
        LfPosListener lfPosListener2 = lfPosListener;
        if (lfPosListener2 != null) {
            lfPosListener2.onQeryCardBalanceResult(z, currentPayInfo.getPayType(), currentPayInfo.getCard(), currentPayInfo.getId().intValue(), str);
        }
        currentPayInfo = null;
    }

    public static void cmd_Test(int i) {
        Command_Up_Test command_Up_Test = new Command_Up_Test();
        command_Up_Test.setID(i);
        CommandManager.appendSendCommand(command_Up_Test);
    }

    public static void cmd_Sign(int i, String str) {
        Command_Up_Sign command_Up_Sign = new Command_Up_Sign();
        command_Up_Sign.setID(i);
        command_Up_Sign.setParams(str, user, posid);
        CommandManager.appendSendCommand(command_Up_Sign);
    }

    public static void cmd_Search(int i, String str, String str2, String str3, String str4, String str5) {
        Command_Up_Search command_Up_Search = new Command_Up_Search();
        command_Up_Search.setID(i);
        command_Up_Search.setParams(str, str2, str3, str4, str5);
        CommandManager.appendSendCommand(command_Up_Search);
    }

    public static void cmd_Properties(int i) {
        Command_Up_Properties command_Up_Properties = new Command_Up_Properties();
        command_Up_Properties.setID(i);
        command_Up_Properties.setParams(user, posid);
        CommandManager.appendSendCommand(command_Up_Properties);
    }

    public static void cmd_Pay(int i, int i2, String str) {
        Command_Up_Pay command_Up_Pay = new Command_Up_Pay();
        command_Up_Pay.setID(i);
        command_Up_Pay.setParams(i2, str);
        CommandManager.appendSendCommand(command_Up_Pay);
    }

    public static void cmd_Pay_Bank(int i, String str) {
        Command_Up_Pay_Bank command_Up_Pay_Bank = new Command_Up_Pay_Bank();
        command_Up_Pay_Bank.setID(i);
        command_Up_Pay_Bank.setParams(user, posid, str);
        CommandManager.appendSendCommand(command_Up_Pay_Bank);
    }

    public static void cmd_Pay_pos(int i, String str) {
        Command_Up_Pay_Pos command_Up_Pay_Pos = new Command_Up_Pay_Pos();
        command_Up_Pay_Pos.setID(i);
        command_Up_Pay_Pos.setParams(user, posid, str);
        CommandManager.appendSendCommand(command_Up_Pay_Pos);
    }

    public static void cmd_Nak(int i) {
        Command_Up_Nak command_Up_Nak = new Command_Up_Nak();
        command_Up_Nak.setID(i);
        CommandManager.appendSendCommand(command_Up_Nak);
    }

    public static void cmd_ClearRecords(int i) {
        Command_Up_ClearRecords command_Up_ClearRecords = new Command_Up_ClearRecords();
        command_Up_ClearRecords.setParams(user, posid, name_zh, name_en);
        CommandManager.appendSendCommand(command_Up_ClearRecords);
    }

    public static void cmd_ClearLogs(int i) {
        Command_Up_ClearLogs command_Up_ClearLogs = new Command_Up_ClearLogs();
        command_Up_ClearLogs.setID(i);
        command_Up_ClearLogs.setParams(user, posid, name_zh, name_en);
        CommandManager.appendSendCommand(command_Up_ClearLogs);
    }

    private static void onSign(boolean z, HashMap<String, Object> hashMap) {
        if (z) {
            Loger.writeLog("LFPOS", currentSignInfo.getStep().getName() + " 成功");
            switch (AnonymousClass1.$SwitchMap$com$shj$device$lfpos$LfPosSignStep[currentSignInfo.getStep().ordinal()]) {
                case 1:
                    currentSignInfo.setStep(LfPosSignStep.PSIGN);
                    serverMsgCode = "01".getBytes();
                    cmd_Sign(currentSignInfo.getId().intValue(), "01");
                    return;
                case 2:
                    currentSignInfo.setStep(LfPosSignStep.CLEAR_RECORDS);
                    cmd_ClearRecords(currentSignInfo.getId().intValue());
                    return;
                case 3:
                    currentSignInfo.setStep(LfPosSignStep.SIGN1);
                    serverMsgCode = "1".getBytes();
                    cmd_Sign(currentSignInfo.getId().intValue(), "01");
                    return;
                case 4:
                    currentSignInfo.setStep(LfPosSignStep.SIGN2);
                    serverMsgCode = ExifInterface.GPS_MEASUREMENT_2D.getBytes();
                    cmd_Sign(currentSignInfo.getId().intValue(), "02");
                    return;
                case 5:
                    serverMsgCode = null;
                    onSingFinished(true);
                    return;
                case 6:
                    serverMsgCode = null;
                    onSingFinished(false);
                    return;
                default:
                    return;
            }
        }
        Loger.writeLog("LFPOS", currentSignInfo.getStep().getName() + " 失败");
        onSingFinished(false);
    }

    public static void onPayResult03(boolean z, String str) {
        String sb;
        if (currentPayInfo.isQueryBalance()) {
            sb = "会员卡查余额";
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("会员卡支付:");
            sb2.append(z ? "成功" : "失败");
            sb = sb2.toString();
        }
        Loger.writeLog("LFPOS", sb);
        if (currentPayInfo.isQueryBalance()) {
            onQueryCardBalanceFinished(z, str);
        } else {
            onPayFinished(z, str);
        }
    }

    /* renamed from: com.shj.device.lfpos.LfPos$1 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$shj$device$lfpos$LfPosPayStep;
        static final /* synthetic */ int[] $SwitchMap$com$shj$device$lfpos$LfPosSignStep;

        static {
            int[] iArr = new int[LfPosPayStep.values().length];
            $SwitchMap$com$shj$device$lfpos$LfPosPayStep = iArr;
            try {
                iArr[LfPosPayStep.TEST.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shj$device$lfpos$LfPosPayStep[LfPosPayStep.PAYTYPE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shj$device$lfpos$LfPosPayStep[LfPosPayStep.PAY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shj$device$lfpos$LfPosPayStep[LfPosPayStep.SEARCH.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shj$device$lfpos$LfPosPayStep[LfPosPayStep.NAK.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            int[] iArr2 = new int[LfPosSignStep.values().length];
            $SwitchMap$com$shj$device$lfpos$LfPosSignStep = iArr2;
            try {
                iArr2[LfPosSignStep.TEST.ordinal()] = 1;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$shj$device$lfpos$LfPosSignStep[LfPosSignStep.PSIGN.ordinal()] = 2;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$shj$device$lfpos$LfPosSignStep[LfPosSignStep.CLEAR_RECORDS.ordinal()] = 3;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$shj$device$lfpos$LfPosSignStep[LfPosSignStep.SIGN1.ordinal()] = 4;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$shj$device$lfpos$LfPosSignStep[LfPosSignStep.SIGN2.ordinal()] = 5;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$shj$device$lfpos$LfPosSignStep[LfPosSignStep.NAK.ordinal()] = 6;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0116, code lost:
    
        if (r11.equals("01") == false) goto L84;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void onPay(boolean r11, java.util.HashMap<java.lang.String, java.lang.Object> r12) {
        /*
            Method dump skipped, instructions count: 502
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.device.lfpos.LfPos.onPay(boolean, java.util.HashMap):void");
    }

    public static void cmd_onAck(HashMap<String, Object> hashMap) {
        if (currentSignInfo != null) {
            onSign(true, hashMap);
        } else if (currentPayInfo != null) {
            onPay(true, hashMap);
        }
    }

    public static void cmd_onNak(int i, String str, String str2, String str3) {
        HashMap hashMap = new HashMap();
        hashMap.put("ID", Integer.valueOf(i));
        hashMap.put("CODE", str);
        hashMap.put("MESSAGE", str2);
        hashMap.put("REMAIN", str3);
        LfPosSignInfo lfPosSignInfo = currentSignInfo;
        if (lfPosSignInfo != null) {
            lfPosSignInfo.setStep(LfPosSignStep.NAK);
            onSign(true, hashMap);
        } else {
            LfPosPayInfo lfPosPayInfo = currentPayInfo;
            if (lfPosPayInfo != null) {
                lfPosPayInfo.setStep(LfPosPayStep.NAK);
                onPay(true, hashMap);
            }
        }
        currentSignInfo = null;
        currentPayInfo = null;
    }

    public static void cmd_onUpdatePosProperties(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, Object obj) {
        name_en = str5;
        name_zh = str4;
    }

    public static void cmd_onServerMessage(int i, byte[] bArr) {
        lfPosServerListener.sendMesssage2Server(serverMsgCode, bArr);
    }

    public static LfPosServerListener getLfPosServerListener() {
        return lfPosServerListener;
    }

    public static void setLfPosServerListener(LfPosServerListener lfPosServerListener2) {
        lfPosServerListener = lfPosServerListener2;
    }

    public static void onPayFinished(boolean z, String str) {
        Loger.writeLog("LFPOS", z ? "支付成功" : "支付失败");
        LfPosListener lfPosListener2 = lfPosListener;
        if (lfPosListener2 != null) {
            lfPosListener2.onPayResult(z, currentPayInfo.getPayType(), currentPayInfo.getCard(), currentPayInfo.getId().intValue(), str);
        }
        currentPayInfo = null;
    }

    public static void onSingFinished(boolean z) {
        setSigned(z);
        Loger.writeLog("LFPOS", z ? "签到成功" : "签到失败");
        currentSignInfo = null;
    }

    public static void onServerResult(byte[] bArr) {
        LfPosSignInfo lfPosSignInfo = currentSignInfo;
        if (lfPosSignInfo != null && !lfPosSignInfo.isServerTested()) {
            currentSignInfo.setServerTested(true);
        }
        Command_Up_Server command_Up_Server = new Command_Up_Server();
        command_Up_Server.setCONT(bArr);
        CommandManager.appendSendCommand(command_Up_Server);
    }

    public static String getPayType() {
        return payType;
    }

    public static void setPayType(String str) {
        payType = str;
    }

    public static void setOnLineCardPayInfo(int i, int i2, int i3) {
        onLineCardPrice = i3;
        onLineCardShelf = i2;
        onLineCardSn = i;
    }

    public static LfPosListener getLfPosListener() {
        return lfPosListener;
    }

    public static void setLfPosListener(LfPosListener lfPosListener2) {
        lfPosListener = lfPosListener2;
    }

    public static boolean isSigning() {
        if (currentSignInfo == null) {
            return false;
        }
        if (System.currentTimeMillis() - singTime > HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS && !currentSignInfo.isServerTested()) {
            currentSignInfo = null;
        }
        if (System.currentTimeMillis() - singTime > 30000 && !signed) {
            currentSignInfo = null;
        }
        return currentSignInfo != null;
    }

    public static boolean isSigned() {
        return signed;
    }

    public static void setSigned(boolean z) {
        signed = z;
    }

    public static void cmd_onSearchedTradInfo(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("CARD", str);
        onPay(true, hashMap);
    }
}
