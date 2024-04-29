package com.shj.device.cardreader;

//import android.serialport.SerialDevice;
//import com.digi.android.serial.SerialPortManager;
//import com.ftdi.j2xx.D2xxManager;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
//import com.serotonin.modbus4j.code.ExceptionCode;
import com.shj.MoneyType;
import com.shj.Shj;
import com.shj.command.CommandManager;
import com.shj.command.Command_Up_SetMoney;
import com.shj.commandV2.CommandV2_Up_SetMoney;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
//public class MdbReader_BDT extends SerialDevice {
public class MdbReader_BDT  {
    private static MdbReader_BDT cardReader = new MdbReader_BDT();
    static byte[] resetBytes = {1, 6, 0, 8, 0, 1, -55, -56};
    private static boolean enabled = false;
    private static boolean isBusy = false;
    private static double ln = -1.0d;
    static int needPayAmount = Integer.MAX_VALUE;
    static boolean isPaysuccess = false;
    private String cmd = "";
    private boolean inited = false;
    Thread thread = null;
    boolean isIdle = false;
    long lastWriteCmdTime = 0;
    int count = 0;
    int coinCount = 1000;
    Timer timer = null;
    String lastError = "";
    boolean autoStopPaperMoney = true;
    long lastPayTime = 0;
    long lastpay = 0;
    QueryCoinsResultListener listener = null;

    /* loaded from: classes2.dex */
    public interface QueryCoinsResultListener {
        void onResult(HashMap<Double, Integer> hashMap);
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean z) {
        enabled = z;
    }

    public static synchronized boolean isCardReaderBusy() {
        boolean z;
        synchronized (MdbReader_BDT.class) {
            z = isBusy;
        }
        return z;
    }

    public void setAutoStopPaperMoney(boolean z) {
        this.autoStopPaperMoney = z;
    }

    public boolean isAutoStopPaperMoney() {
        return this.autoStopPaperMoney;
    }

    public static MdbReader_BDT get() {
        MdbReader_BDT mdbReader_BDT = cardReader;
        if (!mdbReader_BDT.inited) {
//            mdbReader_BDT.setLogTag("MDB-BDT");
//            Loger.setLogFile(cardReader.getLogTag(), "xyshj/log", "MDB.log", 604800000L, 0);
//            cardReader.setBaudrate(9600);
//            cardReader.setTestWaitTimeAndMinData(1000, 8);
//            cardReader.addExDevPath(Shj.getComPath());
//            cardReader.addTestBytes(resetBytes);
            cardReader.thread = new Thread(new Runnable() { // from class: com.shj.device.cardreader.MdbReader_BDT.1


                @Override // java.lang.Runnable
                public void run() {
//                    while (!MdbReader_BDT.cardReader.isConnected()) {
//                        try {
//                            Thread.sleep(1000L);
//                        } catch (Exception unused) {
//                        }
//                    }
                    MdbReader_BDT.cardReader.isIdle = false;
//                    MdbReader_BDT.cardReader.writeData(MdbFuncUtil.ModbusSend(6, 7, 0));
//                    Loger.writeLog(MdbReader_BDT.cardReader.getLogTag() + ";SHJ", ">> 纸币金额不受限制");
                    try {
                        Thread.sleep(600L);
                    } catch (Exception unused2) {
                    }
//                    MdbReader_BDT.cardReader.writeData(MdbFuncUtil.ModbusSend(6, 250, 1));
//                    Loger.writeLog(MdbReader_BDT.cardReader.getLogTag() + ";SHJ", ">> 启用刷卡器，纸币器等等");
                    try {
                        Thread.sleep(600L);
                    } catch (Exception unused3) {
                    }
                    while (-1.0d == MdbReader_BDT.ln) {
                        MdbReader_BDT.cardReader.cmd = "queryfbl";
//                        MdbReader_BDT.cardReader.writeData(new byte[]{1, 3, 0, -9, 0, 1, 53, -8});
//                        Loger.writeLog(MdbReader_BDT.cardReader.getLogTag() + ";SHJ", ">> 读取分辨率");
                        try {
                            Thread.sleep(1000L);
                        } catch (Exception unused4) {
                        }
//                        if (MdbReader_BDT.cardReader.isDebug) {
//                            MdbReader_BDT.cardReader.isIdle = true;
//                            double unused5 = MdbReader_BDT.ln = 0.01d;
//                        }
                    }
                    MdbReader_BDT.cardReader.lastWriteCmdTime = System.currentTimeMillis();
                    MdbReader_BDT.cardReader.queryCoins(null);
                    long j = 0;
                    while (true) {
                        try {
                            if (System.currentTimeMillis() - j <= 2000) {
                                if (MdbReader_BDT.cardReader.isIdle && System.currentTimeMillis() - MdbReader_BDT.cardReader.lastWriteCmdTime > 500) {
//                                    Loger.writeLog(MdbReader_BDT.cardReader.getLogTag(), "轮询查币");
                                    MdbReader_BDT.cardReader.queryPayMoney();
                                }
                                Thread.sleep(500L);
                            } else {
                                j = System.currentTimeMillis();
                                MdbReader_BDT.cardReader.queryCoins(null);
                                Thread.sleep(700L);
                            }
                        } catch (Exception unused6) {
                        }
                    }
                }
            });
            cardReader.thread.start();
            cardReader.inited = true;
        }
        return cardReader;
    }

    /* renamed from: com.shj.device.cardreader.MdbReader_BDT$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
//            while (!MdbReader_BDT.cardReader.isConnected()) {
//                try {
//                    Thread.sleep(1000L);
//                } catch (Exception unused) {
//                }
//            }
            MdbReader_BDT.cardReader.isIdle = false;
//            MdbReader_BDT.cardReader.writeData(MdbFuncUtil.ModbusSend(6, 7, 0));
//            Loger.writeLog(MdbReader_BDT.cardReader.getLogTag() + ";SHJ", ">> 纸币金额不受限制");
            try {
                Thread.sleep(600L);
            } catch (Exception unused2) {
            }
//            MdbReader_BDT.cardReader.writeData(MdbFuncUtil.ModbusSend(6, 250, 1));
//            Loger.writeLog(MdbReader_BDT.cardReader.getLogTag() + ";SHJ", ">> 启用刷卡器，纸币器等等");
            try {
                Thread.sleep(600L);
            } catch (Exception unused3) {
            }
            while (-1.0d == MdbReader_BDT.ln) {
                MdbReader_BDT.cardReader.cmd = "queryfbl";
//                MdbReader_BDT.cardReader.writeData(new byte[]{1, 3, 0, -9, 0, 1, 53, -8});
//                Loger.writeLog(MdbReader_BDT.cardReader.getLogTag() + ";SHJ", ">> 读取分辨率");
                try {
                    Thread.sleep(1000L);
                } catch (Exception unused4) {
                }
//                if (MdbReader_BDT.cardReader.isDebug) {
//                    MdbReader_BDT.cardReader.isIdle = true;
//                    double unused5 = MdbReader_BDT.ln = 0.01d;
//                }
            }
            MdbReader_BDT.cardReader.lastWriteCmdTime = System.currentTimeMillis();
            MdbReader_BDT.cardReader.queryCoins(null);
            long j = 0;
            while (true) {
                try {
                    if (System.currentTimeMillis() - j <= 2000) {
                        if (MdbReader_BDT.cardReader.isIdle && System.currentTimeMillis() - MdbReader_BDT.cardReader.lastWriteCmdTime > 500) {
//                            Loger.writeLog(MdbReader_BDT.cardReader.getLogTag(), "轮询查币");
                            MdbReader_BDT.cardReader.queryPayMoney();
                        }
                        Thread.sleep(500L);
                    } else {
                        j = System.currentTimeMillis();
                        MdbReader_BDT.cardReader.queryCoins(null);
                        Thread.sleep(700L);
                    }
                } catch (Exception unused6) {
                }
            }
        }
    }

    void checkDeviceStatus(byte b) {
        String str = "";
        if ((b & 1) == 1) {
            str = "硬币器异常;";
        }
        if ((b & 16) == 16) {
            str = str + "纸币器异常;";
        }
        if ((b & 256) == 256) {
            str = str + "刷卡器异常;";
        }
        if ((b & 4096) == 4096) {
            str = str + "传感器故障/硬币量不足等;";
        }
        if (str.equalsIgnoreCase(this.lastError)) {
            return;
        }
        this.lastError = str;
        if (str.length() > 0) {
            Loger.writeLog("SHJ", ">> MDB 故障:" + str);
            return;
        }
        Loger.writeLog("SHJ", ">> MDB 故障已恢复");
    }

    public int getCoinCount() {
        return this.coinCount;
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x015d A[Catch: Exception -> 0x0256, TRY_ENTER, TryCatch #1 {Exception -> 0x0256, blocks: (B:3:0x0020, B:5:0x002a, B:7:0x002e, B:9:0x0034, B:11:0x003e, B:13:0x004a, B:25:0x00a2, B:27:0x00a9, B:30:0x00b1, B:36:0x00b6, B:38:0x00bc, B:40:0x00c0, B:42:0x00c5, B:44:0x00c9, B:46:0x00d7, B:47:0x00f9, B:51:0x00fd, B:55:0x015d, B:57:0x0161, B:61:0x0184, B:67:0x011b, B:68:0x011c, B:74:0x0124, B:76:0x012a, B:79:0x0132, B:81:0x0136, B:82:0x013c, B:84:0x0140, B:85:0x014c, B:93:0x01b0, B:94:0x01b1, B:96:0x01b5, B:98:0x01b9, B:100:0x01bd, B:102:0x01c7, B:104:0x01cd, B:105:0x01e7, B:108:0x01d7, B:110:0x01de, B:112:0x01e5, B:113:0x0213, B:115:0x021d, B:117:0x0223, B:118:0x022a, B:49:0x00fa, B:50:0x00fc, B:70:0x011d, B:71:0x011f), top: B:2:0x0020, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0182  */

    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onMessage(byte[] r15) {
        /*
            Method dump skipped, instructions count: 603
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.device.cardreader.MdbReader_BDT.onMessage(byte[]):void");
    }

//    @Override // android.serialport.SerialDevice
//    public boolean checkTestResult(byte[] bArr) {
//        if (bArr.length != resetBytes.length) {
//            return false;
//        }
//        int i = 0;
//        while (true) {
//            byte[] bArr2 = resetBytes;
//            if (i >= bArr2.length) {
//                return true;
//            }
//            if (bArr[i] != bArr2[i]) {
//                return false;
//            }
//            i++;
//        }
//    }

//    @Override // android.serialport.SerialDevice
//    public void onConnectStateChanged(boolean z) {
//        if (z) {
//            this.isIdle = true;
//            Loger.writeLog(getLogTag(), "<< MDB刷卡器已连接");
//            byte[] bArr = {1, 3, 0, 0, 0, 2, -60, ExceptionCode.GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND};
//            writeData(bArr);
//            Loger.writeLog(getLogTag(), ">> " + ObjectHelper.hex2String(bArr) + " 查询查询钱币状态和查询入币金额");
//        }
//    }

//    @Override // android.serialport.SerialDevice
//    protected void onDebug_writeData(byte[] bArr) {
//        Loger.writeLog(getLogTag(), "last cmd:" + this.cmd + " 如果是pay 则 模拟投币开始...3秒后投币");
//        Timer timer = this.timer;
//        if (timer != null) {
//            timer.cancel();
//            this.timer = null;
//        }
//        Timer timer2 = new Timer();
//        this.timer = timer2;
//        timer2.schedule(new TimerTask() { // from class: com.shj.device.cardreader.MdbReader_BDT.2
//            AnonymousClass2() {
//            }
//
//            @Override // java.util.TimerTask, java.lang.Runnable
//            public void run() {
//                if (!MdbReader_BDT.this.cmd.equalsIgnoreCase("pay")) {
//                    if (MdbReader_BDT.this.cmd.equalsIgnoreCase("charge")) {
//                        MdbReader_BDT.this.onMessage(new byte[]{1, 3, 6, 13, 0, 0, 0, 0, 0, 32, 104});
//                        MdbReader_BDT.this.updateMdbMoney(0, 0);
//                        MdbReader_BDT.this.success(true);
//                        return;
//                    }
//                    return;
//                }
//                MdbReader_BDT.this.onMessage(new byte[]{1, 3, 6, 13, 0, 0, 0, 0, 0, 32, 104});
//                MdbReader_BDT.this.updateMdbMoney(MdbReader_BDT.needPayAmount, 0);
//                MdbReader_BDT.this.success(true);
//                Loger.writeLog(MdbReader_BDT.this.getLogTag(), "模拟投币结束...投币" + MdbReader_BDT.needPayAmount);
//            }
//        }, 3000L);
//    }

    /* renamed from: com.shj.device.cardreader.MdbReader_BDT$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 extends TimerTask {
        AnonymousClass2() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (!MdbReader_BDT.this.cmd.equalsIgnoreCase("pay")) {
                if (MdbReader_BDT.this.cmd.equalsIgnoreCase("charge")) {
                    MdbReader_BDT.this.onMessage(new byte[]{1, 3, 6, 13, 0, 0, 0, 0, 0, 32, 104});
                    MdbReader_BDT.this.updateMdbMoney(0, 0);
                    MdbReader_BDT.this.success(true);
                    return;
                }
                return;
            }
            MdbReader_BDT.this.onMessage(new byte[]{1, 3, 6, 13, 0, 0, 0, 0, 0, 32, 104});
            MdbReader_BDT.this.updateMdbMoney(MdbReader_BDT.needPayAmount, 0);
            MdbReader_BDT.this.success(true);
//            Loger.writeLog(MdbReader_BDT.this.getLogTag(), "模拟投币结束...投币" + MdbReader_BDT.needPayAmount);
        }
    }

    public void updateMdbMoney(int i, int i2) {
        if (Shj.isDebug()) {
            Shj.debugAcceptMoney(i, i2 == 0 ? MoneyType.Paper : MoneyType.ICCard, "mdb_accept_money");
            return;
        }
        if (Shj.isResetFinished()) {
            if (Shj.getVersion() == 1) {
                Command_Up_SetMoney command_Up_SetMoney = new Command_Up_SetMoney();
                command_Up_SetMoney.setParams(i2 == 0 ? MoneyType.Paper : MoneyType.ICCard, i, "mdb_accept_money");
                CommandManager.appendSendCommand(command_Up_SetMoney);
            } else {
                CommandV2_Up_SetMoney commandV2_Up_SetMoney = new CommandV2_Up_SetMoney();
                commandV2_Up_SetMoney.setParams(i2 == 0 ? MoneyType.Paper : MoneyType.ICCard, i, "mdb_accept_money");
                CommandManager.appendSendCommand(commandV2_Up_SetMoney);
            }
        }
    }

    public void pay(int i, int i2) {
//        if (isConnected()) {
//            long j = (i * 1000000000) + i2;
//            if (j == this.lastpay && System.currentTimeMillis() - this.lastPayTime < 1000) {
//                Loger.writeLog(getLogTag() + ";SHJ", ">>与上一次下发收款指令，间隔时间太短");
//                return;
//            }
//            this.lastpay = j;
//            this.lastPayTime = System.currentTimeMillis();
//            needPayAmount = i2;
//            this.isIdle = false;
//            isPaysuccess = false;
//            this.lastWriteCmdTime = System.currentTimeMillis();
//            this.cmd = "pay";
//            byte[] ModbusWrite = MdbFuncUtil.ModbusWrite(3, i, i2);
//            writeData(ModbusWrite);
//            Loger.writeLog(getLogTag() + ";SHJ", ">>" + this.cmd + StringUtils.SPACE + ObjectHelper.hex2String(ModbusWrite) + " 信用卡刷卡请求扣款----" + needPayAmount);
//        }
    }

    public void queryPayMoney() {
//        if (isConnected()) {
//            this.isIdle = false;
//            this.lastWriteCmdTime = System.currentTimeMillis();
//            this.cmd = "queryPayMoney";
//            byte[] bArr = {1, 3, 0, 0, 0, 3, 5, -53};
//            writeData(bArr);
//            Loger.writeLog(getLogTag(), ">>" + this.cmd + StringUtils.SPACE + ObjectHelper.hex2String(bArr) + " 查询刷卡状态和入币金额");
//        }
    }

    private void queryStatus() {
//        if (isConnected()) {
//            this.isIdle = false;
//            this.lastWriteCmdTime = System.currentTimeMillis();
//            this.cmd = "queryStatus";
//            byte[] bArr = {1, 3, 0, 0, 0, 1, -124, 10};
//            writeData(bArr);
//            Loger.writeLog(getLogTag(), ">>" + this.cmd + StringUtils.SPACE + ObjectHelper.hex2String(bArr) + " 查询钱币状态");
//        }
    }

    public void charge(int i) {
//        if (isConnected()) {
//            double d = ln;
//            Double.isNaN(r0);
//            this.isIdle = false;
//            this.lastWriteCmdTime = System.currentTimeMillis();
//            this.cmd = "charge";
//            byte[] ModbusSend = MdbFuncUtil.ModbusSend(6, 6, (int) ((((long) (r0 / d)) / 100) / 1000));
//            writeData(ModbusSend);
//            Shj.debugAcceptMoney(0, MoneyType.Reset, "mdb_charge_money");
//            Loger.writeLog(getLogTag() + ";SHJ", ">>" + this.cmd + StringUtils.SPACE + ObjectHelper.hex2String(ModbusSend) + " 刷卡器找零");
//        }
    }

    public void success(boolean z) {
//        if (isConnected()) {
//            isPaysuccess = z;
//            this.isIdle = false;
//            this.lastWriteCmdTime = System.currentTimeMillis();
//            this.cmd = "success";
//            byte[] bArr = {1, 6, 0, 5, 0, 0, -103, -53};
//            if (z) {
//                // fill-array-data instruction
//                bArr[0] = 1;
//                bArr[1] = 6;
//                bArr[2] = 0;
//                bArr[3] = 5;
//                bArr[4] = 0;
//                bArr[5] = 1;
//                bArr[6] = 88;
//                bArr[7] = 11;
//            }
//            writeData(bArr);
//            Loger.writeLog(getLogTag() + ";SHJ", ">>" + this.cmd + StringUtils.SPACE + ObjectHelper.hex2String(bArr) + " 出货：" + z);
//            needPayAmount = Integer.MAX_VALUE;
//        }
    }

    public void cancel() {
//        if (!isConnected() || isPaysuccess) {
//            return;
//        }
//        this.lastpay = 0L;
//        this.lastPayTime = 0L;
//        this.isIdle = false;
//        this.lastWriteCmdTime = System.currentTimeMillis();
//        this.cmd = "cancellation";
//        byte[] bArr = {1, 6, 0, 5, 0, 0, -103, -53};
//        writeData(bArr);
//        Loger.writeLog(getLogTag() + ";SHJ", ">>" + this.cmd + StringUtils.SPACE + ObjectHelper.hex2String(bArr) + " 取消刷卡请求");
//        needPayAmount = Integer.MAX_VALUE;
    }

    public void updatePayType(short s) {
//        if (isConnected()) {
//            this.isIdle = false;
//            this.lastWriteCmdTime = System.currentTimeMillis();
//            this.cmd = "updatepaytype";
//            byte[] ModbusSend = MdbFuncUtil.ModbusSend(6, 7, s);
//            writeData(ModbusSend);
//            Loger.writeLog(getLogTag() + ";SHJ", ">>" + this.cmd + StringUtils.SPACE + ObjectHelper.hex2String(ModbusSend) + " 更新支付设置：");
//            StringBuilder sb = new StringBuilder();
//            sb.append(getLogTag());
//            sb.append(";SHJ");
//            String sb2 = sb.toString();
//            StringBuilder sb3 = new StringBuilder();
//            sb3.append(">>");
//            sb3.append(this.cmd);
//            sb3.append(StringUtils.SPACE);
//            sb3.append(ObjectHelper.hex2String(ModbusSend));
//            sb3.append(" 硬币器禁用：");
//            sb3.append((s & 1) == 1);
//            Loger.writeLog(sb2, sb3.toString());
//            String str = getLogTag() + ";SHJ";
//            StringBuilder sb4 = new StringBuilder();
//            sb4.append(">>");
//            sb4.append(this.cmd);
//            sb4.append(StringUtils.SPACE);
//            sb4.append(ObjectHelper.hex2String(ModbusSend));
//            sb4.append(" 纸币器禁用：");
//            sb4.append((s & 16) == 16);
//            Loger.writeLog(str, sb4.toString());
//            String str2 = getLogTag() + ";SHJ";
//            StringBuilder sb5 = new StringBuilder();
//            sb5.append(">>");
//            sb5.append(this.cmd);
//            sb5.append(StringUtils.SPACE);
//            sb5.append(ObjectHelper.hex2String(ModbusSend));
//            sb5.append(" 刷卡器禁用：");
//            sb5.append((s & D2xxManager.FT_FLOW_RTS_CTS) == 256);
//            Loger.writeLog(str2, sb5.toString());
//            String str3 = getLogTag() + ";SHJ";
//            StringBuilder sb6 = new StringBuilder();
//            sb6.append(">>");
//            sb6.append(this.cmd);
//            sb6.append(StringUtils.SPACE);
//            sb6.append(ObjectHelper.hex2String(ModbusSend));
//            sb6.append(" 脉冲入币器禁用：");
//            sb6.append((s & 4096) == 4096);
//            Loger.writeLog(str3, sb6.toString());
//        }
    }

    public void queryCoins(QueryCoinsResultListener queryCoinsResultListener) {
//        if (isConnected()) {
//            this.listener = queryCoinsResultListener;
//            this.isIdle = false;
//            this.lastWriteCmdTime = System.currentTimeMillis();
//            this.cmd = "queryCoins";
//            byte[] ModbusSend = MdbFuncUtil.ModbusSend(3, 33, 8);
//            writeData(ModbusSend);
//            Loger.writeLog(getLogTag() + ";SHJ", ">>" + this.cmd + StringUtils.SPACE + ObjectHelper.hex2String(ModbusSend) + " 查询硬币存量");
//        }
    }

    public void checkPapers(boolean z) {
//        if (isConnected()) {
//            this.isIdle = false;
//            this.lastWriteCmdTime = System.currentTimeMillis();
//            this.cmd = "checkPapers";
//            byte[] ModbusSend = MdbFuncUtil.ModbusSend(6, 7, z ? 0 : 2);
//            writeData(ModbusSend);
//            String str = getLogTag() + ";SHJ";
//            StringBuilder sb = new StringBuilder();
//            sb.append(">>");
//            sb.append(this.cmd);
//            sb.append(StringUtils.SPACE);
//            sb.append(ObjectHelper.hex2String(ModbusSend));
//            sb.append(z ? " 启用纸币器" : "禁用纸币器");
//            Loger.writeLog(str, sb.toString());
//        }
    }
}
