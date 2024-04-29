package com.shj.device.cardreader;

import android.serialport.SerialDevice;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.Shj;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class WinCarcdReader extends SerialDevice {
    private static WinCarcdReader cardReader = new WinCarcdReader();
    private static boolean enabled = false;
    private WinCarcdReaderListener listener;
    private String cmd = "";
    private String sn = "";
    private boolean inited = false;
    private boolean isLastPayFinished = true;
    private int lastPayMoney = 0;
    private int serialNo = 1;
    private Timer timer = null;
    private long lastPayTime = 0;

    /* loaded from: classes2.dex */
    public interface WinCarcdReaderListener {
        void onResult(String str, int i, String str2, String str3);
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean z) {
        enabled = z;
    }

    public static WinCarcdReader get() {
        WinCarcdReader winCarcdReader = cardReader;
        if (!winCarcdReader.inited) {
            winCarcdReader.setBaudrate(9600);
            cardReader.addTestBytes("#SO111#EO".getBytes());
            cardReader.setTestWaitTimeAndMinData(1000, 12);
            cardReader.setStringSplit("#EO");
            cardReader.addExDevPath(Shj.getComPath());
            cardReader.setDevPath("/dev/ttyS0");
            cardReader.inited = true;
        }
        return cardReader;
    }

    @Override // android.serialport.SerialDevice
    public void onMessage(String str) {
        Loger.writeLog(getLogTag(), "<< " + str);
        if (str.length() == 0 || str.equalsIgnoreCase("#SOAccpet#EO")) {
            return;
        }
        try {
            if (str.startsWith("#SOPrint")) {
                str = str.substring(3, str.length() - 3);
                Shj.onDeviceMessage(getLogTag(), str);
                return;
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
        try {
            String[] split = str.substring(3, str.length() - 3).split("\\*");
            if (split[1].equalsIgnoreCase(this.cmd)) {
                try {
                    if (split[0].equalsIgnoreCase(this.sn)) {
                        this.listener.onResult(split[1], Integer.parseInt(split[2]), split[3], split[4]);
                        Loger.writeLog(getLogTag(), "通知售货机 已收款 ");
                    }
                } catch (Exception unused) {
                }
                this.isLastPayFinished = true;
            }
        } catch (Exception e2) {
            Loger.safe_inner_exception_catch(e2);
        }
    }

    @Override // android.serialport.SerialDevice
    public boolean checkTestResult(byte[] bArr) {
        String str = new String(bArr);
        Loger.writeLog(getLogTag(), "recive:" + str);
        return str.contains("#SOAccept#EO");
    }

    @Override // android.serialport.SerialDevice
    public void onConnectStateChanged(boolean z) {
        if (z) {
            writeData("#SOVendMachine connected.#EO".getBytes());
            Loger.writeLog(getLogTag(), ">> #SOVendMachine connected.#EO");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0033  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int getNextSerialNo() {
        /*
            r5 = this;
            java.lang.String r0 = "wincardreader_g_serial_1"
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
            int r3 = r1.intValue()
            r4 = 255(0xff, float:3.57E-43)
            if (r3 <= r4) goto L33
            goto L34
        L33:
            r2 = r1
        L34:
            com.oysb.utils.cache.ACache r1 = com.oysb.utils.cache.CacheHelper.getFileCache()
            r1.put(r0, r2)
            int r0 = r2.intValue()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.device.cardreader.WinCarcdReader.getNextSerialNo():int");
    }

    public void pay(int i, WinCarcdReaderListener winCarcdReaderListener) {
        if (isConnected()) {
            this.isLastPayFinished = true;
            this.lastPayMoney = i;
            this.listener = winCarcdReaderListener;
            this.cmd = "pay";
            this.sn = "" + getNextSerialNo();
            String str = "#SO" + this.sn + Marker.ANY_MARKER + this.cmd + Marker.ANY_MARKER + i + "#EO";
            writeData(str.getBytes());
            this.isLastPayFinished = false;
            this.lastPayTime = System.currentTimeMillis();
            Loger.writeLog(getLogTag(), ">> " + str);
        }
    }

    public void cancel(WinCarcdReaderListener winCarcdReaderListener) {
        if (isConnected()) {
            this.isLastPayFinished = true;
            this.listener = winCarcdReaderListener;
            this.cmd = "cancellation";
            this.sn = "" + getNextSerialNo();
            String str = "#SO" + this.sn + Marker.ANY_MARKER + this.cmd + Marker.ANY_MARKER + this.lastPayMoney + "#EO";
            writeData(str.getBytes());
            Loger.writeLog(getLogTag(), ">> " + str);
        }
    }

    public void refund(int i, WinCarcdReaderListener winCarcdReaderListener) {
        if (isConnected()) {
            this.isLastPayFinished = true;
            this.listener = winCarcdReaderListener;
            this.cmd = "refund";
            this.sn = "" + getNextSerialNo();
            String str = "#SO" + this.sn + Marker.ANY_MARKER + this.cmd + Marker.ANY_MARKER + i + "#EO";
            writeData(str.getBytes());
            Loger.writeLog(getLogTag(), ">> " + str);
        }
    }

    public void readCard(WinCarcdReaderListener winCarcdReaderListener) {
        if (isConnected()) {
            this.listener = winCarcdReaderListener;
            this.cmd = "readcard";
            this.sn = "" + getNextSerialNo();
            String str = "#SO" + this.sn + Marker.ANY_MARKER + this.cmd + "#EO";
            writeData(str.getBytes());
            Loger.writeLog(getLogTag(), ">> " + str);
        }
    }

    public boolean isLastPayFinished() {
        return this.isLastPayFinished || System.currentTimeMillis() - this.lastPayTime > 180000;
    }

    public int getLastPayMoney() {
        return this.lastPayMoney;
    }

    public void restHeartbeat() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        Timer timer2 = new Timer();
        this.timer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.shj.device.cardreader.WinCarcdReader.1
            AnonymousClass1() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    Loger.writeLog(WinCarcdReader.this.getLogTag(), ">> " + ObjectHelper.hex2String("#SO11112222333#EO".getBytes()));
                    WinCarcdReader.this.writeData("#SO11112222333#EO".getBytes());
                } catch (Exception unused) {
                }
            }
        }, 1000L, 1000L);
    }

    /* renamed from: com.shj.device.cardreader.WinCarcdReader$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                Loger.writeLog(WinCarcdReader.this.getLogTag(), ">> " + ObjectHelper.hex2String("#SO11112222333#EO".getBytes()));
                WinCarcdReader.this.writeData("#SO11112222333#EO".getBytes());
            } catch (Exception unused) {
            }
        }
    }
}
