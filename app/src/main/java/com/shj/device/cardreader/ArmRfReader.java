package com.shj.device.cardreader;

import android.serialport.SerialPort;
import android.serialport.SerialPortFinder;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.io.file.SDFileUtils;
import com.printsdk.cmd.PrintCmd;
import com.shj.Shj;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class ArmRfReader {
    static byte[] currentCmd = null;
    static CmdResultListener currentResultListener = null;
    public static final long maxWaitTime = 500;
    static SerialPort serialPort;
    private static ByteArrayOutputStream mBuffer = new ByteArrayOutputStream();
    static long postCmdStartTime = 0;
    static Object obj = new Object();
    static boolean isCanced = false;

    /* loaded from: classes2.dex */
    public interface CmdResultListener {
        void onAcceptResult(byte[] bArr, byte[] bArr2);

        void onError(Exception exc);
    }

    /* loaded from: classes2.dex */
    public static class ComPathItem {
        int baudrate;
        String dev;
        boolean hasConnected = false;

        public ComPathItem(String str, int i) {
            this.dev = str;
            this.baudrate = i;
        }
    }

    public static boolean findCom() {
        if (serialPort != null) {
            return true;
        }
        try {
            ArrayList arrayList = new ArrayList();
            String asString = CacheHelper.getFileCache().getAsString("ArmRfReader_LAST_DEV");
            String asString2 = CacheHelper.getFileCache().getAsString("ArmRfReader_LAST_BAT");
            if (asString == null) {
                asString = "";
            }
            if (asString2 == null) {
                asString2 = "115200";
            }
            if (asString.length() > 0) {
                arrayList.add(new ComPathItem(asString, Integer.parseInt(asString2)));
            }
            for (String str : new SerialPortFinder().getAllDevicesPath()) {
                try {
                    String comPath = Shj.getComPath();
                    String linkPath = SDFileUtils.getLinkPath(comPath);
                    if (!str.equalsIgnoreCase(comPath) && !str.equalsIgnoreCase(linkPath) && !str.equalsIgnoreCase(asString)) {
                        arrayList.add(new ComPathItem(str, 115200));
                    }
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                }
            }
            for (int i = 0; i < arrayList.size(); i++) {
                ComPathItem comPathItem = (ComPathItem) arrayList.get(i);
                try {
                    SerialPort serialPort2 = new SerialPort(comPathItem.dev, comPathItem.baudrate, 0);
                    serialPort = serialPort2;
                    serialPort2.getOutputStream().write(new byte[]{64, 5, -1, 65, 1, -6, 13});
                    serialPort.getOutputStream().flush();
                    Thread.sleep(500L);
                    if (serialPort.getInputStream().available() > 0) {
                        int available = serialPort.getInputStream().available();
                        byte[] bArr = new byte[available];
                        serialPort.getInputStream().read(bArr);
                        if ((bArr[2] & UByte.MAX_VALUE) == 255) {
                            Loger.writeLog("Card", "已找到设备" + comPathItem.dev);
                            Loger.writeLog("Card", ObjectHelper.hex2String(bArr, available));
                            comPathItem.hasConnected = true;
                            CacheHelper.getFileCache().put("ArmRfReader_LAST_DEV", comPathItem.dev);
                            CacheHelper.getFileCache().put("ArmRfReader_LAST_BAT", "" + comPathItem.baudrate);
                            break;
                        }
                        serialPort.getOutputStream().write(PrintCmd.SetClean());
                        serialPort.getOutputStream().flush();
                        serialPort.close();
                        serialPort = null;
                    } else {
                        serialPort.close();
                        serialPort = null;
                    }
                } catch (Exception unused) {
                    serialPort = null;
                }
            }
        } catch (Exception unused2) {
        }
        return serialPort != null;
    }

    public static void findCard(byte b, byte b2, byte b3, CmdResultListener cmdResultListener) {
        byte[] bArr = {64, 7, -1, 97, b, b2, b3, 0, 13};
        bArr[7] = xorByte(bArr, 0, 7);
        postCmd(bArr, cmdResultListener);
    }

    public static void getMoney(byte[] bArr, CmdResultListener cmdResultListener) {
        byte[] bArr2 = {64, 8, -1, 99, bArr[0], bArr[1], bArr[2], bArr[3], 0, 13};
        bArr2[8] = xorByte(bArr2, 0, 8);
        postCmd(bArr2, cmdResultListener);
    }

    public static void setMoney(byte[] bArr, long j, CmdResultListener cmdResultListener) {
        byte[] bArr2 = {64, 12, -1, 100, bArr[0], bArr[1], bArr[2], bArr[3], 0, 0, 0, 0, 0, 13};
        ObjectHelper.updateBytes(bArr2, j, 8, 4);
        bArr2[12] = xorByte(bArr2, 0, 12);
        postCmd(bArr2, cmdResultListener);
    }

    public static byte xorByte(byte[] bArr, int i, int i2) {
        byte b = 0;
        if (i2 > 1) {
            b = bArr[0];
            for (int i3 = 1; i3 < i2; i3++) {
                b = (byte) (b ^ bArr[i3]);
            }
        }
        return b;
    }

    public static boolean close() {
        cancel();
        try {
            SerialPort serialPort2 = serialPort;
            if (serialPort2 == null) {
                return true;
            }
            serialPort2.close();
            serialPort = null;
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static void cancel() {
        isCanced = true;
    }

    public static void postCmd(byte[] bArr, CmdResultListener cmdResultListener) {
        currentResultListener = cmdResultListener;
        currentCmd = bArr;
        postCmdStartTime = System.currentTimeMillis();
        isCanced = false;
        if (findCom()) {
            Loger.writeLog("Card", ".....");
            if (isCanced) {
                return;
            }
            try {
                try {
                    try {
                        if (serialPort.getInputStream().available() > 0) {
                            int available = serialPort.getInputStream().available();
                            byte[] bArr2 = new byte[available];
                            serialPort.getInputStream().read(bArr2);
                            Loger.writeLog("Card", "写卡前：" + ObjectHelper.hex2String(bArr2, available));
                        }
                        OutputStream outputStream = serialPort.getOutputStream();
                        if (outputStream == null) {
                            throw new Exception("serialPort is closed");
                        }
                        byte[] bArr3 = currentCmd;
                        outputStream.write(bArr3, 0, bArr3.length);
                        outputStream.flush();
                        mBuffer.reset();
                        long currentTimeMillis = System.currentTimeMillis();
                        InputStream inputStream = serialPort.getInputStream();
                        boolean z = false;
                        while (!isCanced) {
                            int available2 = inputStream.available();
                            if (available2 > 0) {
                                z = true;
                            }
                            long currentTimeMillis2 = System.currentTimeMillis();
                            if (z) {
                                if (available2 == 0) {
                                    currentResultListener.onAcceptResult(currentCmd, mBuffer.toByteArray());
                                    return;
                                }
                                byte[] bArr4 = new byte[available2];
                                inputStream.read(bArr4, 0, available2);
                                mBuffer.write(bArr4);
                                mBuffer.flush();
                            } else if (currentTimeMillis2 - currentTimeMillis > 500) {
                                return;
                            }
                            try {
                                Thread.sleep(10L);
                            } catch (Exception unused) {
                            }
                        }
                    } catch (Exception e) {
                        currentResultListener.onError(e);
                    }
                } catch (IOException unused2) {
                    SerialPort serialPort2 = serialPort;
                    if (serialPort2 != null) {
                        serialPort2.close();
                    }
                    serialPort = null;
                }
            } catch (Exception unused3) {
                serialPort = null;
            }
        }
    }
}
