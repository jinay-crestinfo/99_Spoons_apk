package com.shj.device;

import android.serialport.SerialDevice;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.serotonin.modbus4j.code.ExceptionCode;
import java.util.LinkedList;
import java.util.Queue;
import kotlin.UByte;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class Gzj485VMC extends SerialDevice {
    Gzj485VMCListener listener;
    private static Gzj485VMC cardReader = new Gzj485VMC();
    private static boolean enabled = false;
    static Queue<CmdItem> cmdQueue = new LinkedList();
    private String cmd = "";
    private boolean inited = false;
    Thread thread = null;
    boolean isIdle = false;
    long lastWriteDataTime = -1;

    /* loaded from: classes2.dex */
    public interface Gzj485VMCListener {
        void onResult(String str, int i, String str2, String str3);
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean z) {
        enabled = z;
    }

    /* loaded from: classes2.dex */
    public static class CmdItem {
        byte[] cmd;
        String cmdName;
        long time;
        int xn;
        int yn;

        public CmdItem(String str, int i, int i2, byte[] bArr) {
            this.cmdName = str;
            this.cmd = bArr;
            this.yn = i;
            this.xn = i2;
        }

        public int getShelf() {
            return ((this.yn - 1) * 6) + this.xn;
        }
    }

    public static Gzj485VMC get() {
        Gzj485VMC gzj485VMC = cardReader;
        if (!gzj485VMC.inited) {
            gzj485VMC.setBaudrate(9600);
            cardReader.setDevPath("/dev/ttyS0");
            Gzj485VMC gzj485VMC2 = cardReader;
            gzj485VMC2.inited = true;
            gzj485VMC2.isIdle = true;
            gzj485VMC2.thread = new Thread(new Runnable() { // from class: com.shj.device.Gzj485VMC.1
                int idx = 1;

                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (Gzj485VMC.cardReader.isConnected()) {
                        if (System.currentTimeMillis() - Gzj485VMC.cardReader.lastWriteDataTime > 500) {
                            Gzj485VMC.cardReader.isIdle = true;
                        }
                        if (Gzj485VMC.cardReader.isIdle) {
                            if (Gzj485VMC.cmdQueue.size() == 0) {
                                Gzj485VMC gzj485VMC3 = Gzj485VMC.cardReader;
                                int i = this.idx;
                                this.idx = i + 1;
                                gzj485VMC3.queryShelfGoodsCount(i + 24);
                                int i2 = this.idx;
                                if (i2 > 24) {
                                    this.idx = i2 - 24;
                                }
                            } else {
                                CmdItem poll = Gzj485VMC.cmdQueue.poll();
                                Gzj485VMC.cardReader.lastWriteDataTime = System.currentTimeMillis();
                                if (Gzj485VMC.cardReader.lastWriteDataTime - poll.time < 1000) {
                                    Gzj485VMC.cardReader.isIdle = false;
                                    Gzj485VMC.cardReader.writeData(poll.cmd);
                                    Loger.writeLog(Gzj485VMC.cardReader.getLogTag(), ">>" + poll.cmdName + StringUtils.SPACE + ObjectHelper.hex2String(poll.cmd) + " 请求开门 格口号:" + poll.getShelf() + " yn=" + poll.yn + " xn=" + poll.xn);
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            cardReader.thread.start();
        }
        return cardReader;
    }

    /* renamed from: com.shj.device.Gzj485VMC$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements Runnable {
        int idx = 1;

        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (Gzj485VMC.cardReader.isConnected()) {
                if (System.currentTimeMillis() - Gzj485VMC.cardReader.lastWriteDataTime > 500) {
                    Gzj485VMC.cardReader.isIdle = true;
                }
                if (Gzj485VMC.cardReader.isIdle) {
                    if (Gzj485VMC.cmdQueue.size() == 0) {
                        Gzj485VMC gzj485VMC3 = Gzj485VMC.cardReader;
                        int i = this.idx;
                        this.idx = i + 1;
                        gzj485VMC3.queryShelfGoodsCount(i + 24);
                        int i2 = this.idx;
                        if (i2 > 24) {
                            this.idx = i2 - 24;
                        }
                    } else {
                        CmdItem poll = Gzj485VMC.cmdQueue.poll();
                        Gzj485VMC.cardReader.lastWriteDataTime = System.currentTimeMillis();
                        if (Gzj485VMC.cardReader.lastWriteDataTime - poll.time < 1000) {
                            Gzj485VMC.cardReader.isIdle = false;
                            Gzj485VMC.cardReader.writeData(poll.cmd);
                            Loger.writeLog(Gzj485VMC.cardReader.getLogTag(), ">>" + poll.cmdName + StringUtils.SPACE + ObjectHelper.hex2String(poll.cmd) + " 请求开门 格口号:" + poll.getShelf() + " yn=" + poll.yn + " xn=" + poll.xn);
                        }
                    }
                }
            }
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
            if ((bArr[i] & UByte.MAX_VALUE) == 255) {
                break;
            }
            int i2 = i + 1;
            if ((bArr[i2] & UByte.MAX_VALUE) == 11) {
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
            if ((bArr[i] & UByte.MAX_VALUE) == 254) {
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

    /* JADX WARN: Removed duplicated region for block: B:14:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    @Override // android.serialport.SerialDevice
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onMessage(byte[] r8) {
        /*
            r7 = this;
            java.lang.String r0 = r7.getLogTag()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "<< "
            r1.append(r2)
            java.lang.String r2 = com.oysb.utils.ObjectHelper.hex2String(r8)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.oysb.utils.Loger.writeLog(r0, r1)
            r0 = 1
            r7.isIdle = r0
            r1 = 3
            r2 = 0
            int r1 = com.oysb.utils.ObjectHelper.intFromBytes(r8, r1, r0)     // Catch: java.lang.Exception -> L64
            r3 = 5
            int r3 = com.oysb.utils.ObjectHelper.intFromBytes(r8, r3, r0)     // Catch: java.lang.Exception -> L61
            r4 = 6
            int r5 = com.oysb.utils.ObjectHelper.intFromBytes(r8, r4, r0)     // Catch: java.lang.Exception -> L61
            r6 = 7
            int r8 = com.oysb.utils.ObjectHelper.intFromBytes(r8, r6, r0)     // Catch: java.lang.Exception -> L61
            int r3 = r3 - r0
            int r3 = r3 * 6
            int r3 = r3 + r5
            r4 = r1 & 255(0xff, float:3.57E-43)
            r5 = 160(0xa0, float:2.24E-43)
            r6 = 162(0xa2, float:2.27E-43)
            if (r6 != r4) goto L4d
            r8 = r8 & 255(0xff, float:3.57E-43)
            if (r8 != r5) goto L47
            com.shj.OfferState r8 = com.shj.OfferState.OfferSuccess     // Catch: java.lang.Exception -> L61
            goto L49
        L47:
            com.shj.OfferState r8 = com.shj.OfferState.Blocked     // Catch: java.lang.Exception -> L61
        L49:
            com.shj.Shj.onOfferGoods(r3, r8, r2)     // Catch: java.lang.Exception -> L61
            goto L69
        L4d:
            if (r6 != r4) goto L69
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch: java.lang.Exception -> L61
            r8 = r8 & 255(0xff, float:3.57E-43)
            if (r8 != r5) goto L58
            goto L59
        L58:
            r0 = 0
        L59:
            java.lang.Integer r8 = java.lang.Integer.valueOf(r0)     // Catch: java.lang.Exception -> L61
            com.shj.Shj.onUpdateGoodsCount(r3, r8)     // Catch: java.lang.Exception -> L61
            goto L69
        L61:
            r8 = move-exception
            r2 = r1
            goto L65
        L64:
            r8 = move-exception
        L65:
            com.oysb.utils.Loger.safe_inner_exception_catch(r8)
            r1 = r2
        L69:
            com.shj.device.Gzj485VMC$Gzj485VMCListener r8 = r7.listener
            if (r8 == 0) goto L72
            java.lang.String r0 = ""
            r8.onResult(r0, r1, r0, r0)
        L72:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.device.Gzj485VMC.onMessage(byte[]):void");
    }

    @Override // android.serialport.SerialDevice
    public void onConnectStateChanged(boolean z) {
        if (z) {
            this.isIdle = true;
            Loger.writeLog(getLogTag(), "<< 控制板已连接");
        }
    }

    public static int crc16(byte[] bArr, int i) {
        if (i <= 0) {
            return 0;
        }
        int i2 = 65535;
        for (int i3 = 0; i3 < i; i3++) {
            i2 ^= bArr[i3] & UByte.MAX_VALUE;
            for (int i4 = 0; i4 < 8; i4++) {
                i2 = (i2 & 1) != 0 ? (i2 >> 1) ^ 40961 : i2 >> 1;
            }
        }
        return i2;
    }

    public byte[] updateBytesWithCRC16(byte[] bArr) {
        int crc16 = crc16(bArr, bArr.length - 3);
        ObjectHelper.updateBytes(bArr, crc16 & 255, bArr.length - 3, 1);
        ObjectHelper.updateBytes(bArr, crc16 >> 8, bArr.length - 2, 1);
        return bArr;
    }

    public void openShelf(int i) {
        if (isConnected()) {
            this.cmd = "openShelf";
            int i2 = (i / 6) + 1;
            int i3 = i % 6;
            cmdQueue.add(new CmdItem(this.cmd, i2, i3, updateBytesWithCRC16(new byte[]{-1, 10, 0, -94, ExceptionCode.GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND, (byte) i2, (byte) i3, 0, 0, -2})));
        }
    }

    public void queryShelfGoodsCount(int i) {
        if (isConnected()) {
            this.cmd = "queryShelfGoodsCount";
            int i2 = (i / 6) + 1;
            int i3 = i % 6;
            cmdQueue.add(new CmdItem(this.cmd, i2, i3, updateBytesWithCRC16(new byte[]{-1, 10, 0, -78, ExceptionCode.GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND, (byte) i2, (byte) i3, 0, 0, -2})));
        }
    }
}
