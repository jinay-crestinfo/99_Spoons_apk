package com.shj.device.cardreader;

import android.serialport.SerialDevice;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.UByte;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class Micro_CardReader extends SerialDevice {
    private static boolean isCancled = true;
    JB_CardReaderListener listener;
    private static Integer lastPayMoney = 0;
    private static int lastShelf = 0;
    private static long lastStartPayTime = 0;
    private static Micro_CardReader cardReader = new Micro_CardReader();
    private static boolean enabled = false;
    static Timer timer = null;
    private String cmd = "";
    private boolean inited = false;
    Thread thread = null;
    boolean isIdle = false;
    long lastWriteCmdTime = 0;

    /* loaded from: classes2.dex */
    public static class JBCardInfo {
        String cardNo;
        int money;
    }

    /* loaded from: classes2.dex */
    public interface JB_CardReaderListener {
        void onResult(String str, int i, String str2, String str3);
    }

    public void queryCardInfo() {
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean z) {
        enabled = z;
    }

    public static Micro_CardReader get() {
        Micro_CardReader micro_CardReader = cardReader;
        if (!micro_CardReader.inited) {
            micro_CardReader.setBaudrate(115200);
            cardReader.setDevPath("/dev/ttyS2");
            cardReader.inited = true;
            Timer timer2 = new Timer();
            timer = timer2;
            timer2.schedule(new TimerTask() { // from class: com.shj.device.cardreader.Micro_CardReader.1
                AnonymousClass1() {
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (!Micro_CardReader.isCancled && System.currentTimeMillis() - Micro_CardReader.lastStartPayTime > 10000) {
                        Micro_CardReader.get().pay(Micro_CardReader.lastShelf, Micro_CardReader.lastPayMoney.intValue());
                    }
                }
            }, 2000L, 2000L);
        }
        return cardReader;
    }

    /* renamed from: com.shj.device.cardreader.Micro_CardReader$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (!Micro_CardReader.isCancled && System.currentTimeMillis() - Micro_CardReader.lastStartPayTime > 10000) {
                Micro_CardReader.get().pay(Micro_CardReader.lastShelf, Micro_CardReader.lastPayMoney.intValue());
            }
        }
    }

    public void setJB_CardReaderListener(JB_CardReaderListener jB_CardReaderListener) {
        this.listener = jB_CardReaderListener;
    }

    @Override // android.serialport.SerialDevice
    protected byte[] paseBytes(byte[] bArr) {
        this.offset = 0;
        int i = 0;
        while (true) {
            if (i >= bArr.length - 2) {
                i = -1;
                break;
            }
            if ((bArr[i] & UByte.MAX_VALUE) == 85) {
                break;
            }
            int i2 = i + 1;
            if ((bArr[i2] & UByte.MAX_VALUE) == 170) {
                break;
            }
            i = i2;
        }
        if (i == -1) {
            this.offset = bArr.length - 2;
            return null;
        }
        this.offset = i;
        while (true) {
            i++;
            if (i >= bArr.length) {
                i = -1;
                break;
            }
            if ((bArr[i] & UByte.MAX_VALUE) == 255) {
                break;
            }
        }
        if (i == -1) {
            return null;
        }
        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, this.offset, bArr2, 0, length);
        this.offset += bArr.length;
        return bArr2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:30:0x010e  */
    /* JADX WARN: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v14 */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v21, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v22 */
    /* JADX WARN: Type inference failed for: r3v25 */
    /* JADX WARN: Type inference failed for: r3v26 */
    /* JADX WARN: Type inference failed for: r3v27 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v9 */
    @Override // android.serialport.SerialDevice
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onMessage(byte[] r11) {
        /*
            Method dump skipped, instructions count: 300
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.device.cardreader.Micro_CardReader.onMessage(byte[]):void");
    }

    @Override // android.serialport.SerialDevice
    public void onConnectStateChanged(boolean z) {
        if (z) {
            this.isIdle = true;
            Loger.writeLog(getLogTag(), "<< JB刷卡器已连接");
        }
    }

    public void pay(int i, int i2) {
        if (isConnected()) {
            isCancled = false;
            lastPayMoney = Integer.valueOf(i2);
            lastShelf = i;
            lastStartPayTime = System.currentTimeMillis();
            int i3 = i2 / 10;
            this.isIdle = false;
            this.lastWriteCmdTime = System.currentTimeMillis();
            this.cmd = "pay";
            byte[] bArr = {85, -86, 0, 0, 0, 0, -1};
            ObjectHelper.updateBytes(bArr, i3, 2, 4);
            writeData(bArr);
            String logTag = getLogTag();
            StringBuilder sb = new StringBuilder();
            sb.append(">>");
            sb.append(this.cmd);
            sb.append(StringUtils.SPACE);
            sb.append(ObjectHelper.hex2String(bArr));
            sb.append(" 请求扣款:");
            double d = i3;
            Double.isNaN(d);
            sb.append(d / 10.0d);
            sb.append("元");
            Loger.writeLog(logTag, sb.toString());
        }
    }

    private void queryPayMoney() {
        if (isConnected()) {
            this.isIdle = false;
            this.lastWriteCmdTime = System.currentTimeMillis();
            this.cmd = "query";
            byte[] bArr = {1, 3, 0, 1, 0, 1, -43, -54};
            writeData(bArr);
            Loger.writeLog(getLogTag(), ">>" + this.cmd + StringUtils.SPACE + ObjectHelper.hex2String(bArr) + " 查询入币金额");
        }
    }

    public void charge(int i) {
        if (isConnected()) {
            this.isIdle = false;
            this.lastWriteCmdTime = System.currentTimeMillis();
            this.cmd = "charge";
            byte[] ModbusSend = MdbFuncUtil.ModbusSend(6, 6, i);
            writeData(ModbusSend);
            Loger.writeLog(getLogTag(), ">>" + this.cmd + StringUtils.SPACE + ObjectHelper.hex2String(ModbusSend) + " 刷卡器找零");
        }
    }

    public void cancel() {
        if (isCancled) {
            return;
        }
        isCancled = true;
        if (isConnected()) {
            this.isIdle = false;
            this.lastWriteCmdTime = System.currentTimeMillis();
            this.cmd = "pay";
            writeData(new byte[]{85, -86, 0, 0, 0, 0, -1});
            Loger.writeLog(getLogTag(), ">>" + this.cmd + "  取消刷卡请求");
        }
    }

    public void close_view_request() {
        if (isConnected()) {
            this.isIdle = false;
            this.lastWriteCmdTime = System.currentTimeMillis();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("action", (Object) "view.turn_off");
            jSONObject.put("kwarg", (Object) new JSONObject());
            jSONObject.put("arg", (Object) new JSONArray());
            byte[] bytes = jSONObject.toString().getBytes();
            writeData(bytes);
            Loger.writeLog(getLogTag(), ">>" + this.cmd + StringUtils.SPACE + ObjectHelper.hex2String(bytes) + " data:" + jSONObject.toString());
        }
    }
}
