package com.oysb.utils.device;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.serialport.SerialPort;
import android.serialport.SerialPortFinder;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.cache.CacheHelper;
//import com.printsdk.cmd.PrintCmd;
import com.shj.setting.Utils.UsbFileUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class MsPrintor {
    static byte[] currentCmd = null;
    static PrinterListener currentResultListener = null;
    public static final long maxWaitTime = 500;
    static SerialPort serialPort;
    private static SerialPortSearchListener serialPortSearchListener;
    private static ByteArrayOutputStream mBuffer = new ByteArrayOutputStream();
    static long postCmdStartTime = 0;
    static Object obj = new Object();
    static boolean isCanced = false;
    static Thread readThread = null;
    static String defDev = "/dev/ttyS2";
    static boolean autoFindDev = true;

    /* loaded from: classes2.dex */
    public interface PrinterListener {
        void onAcceptMessage(byte[] bArr);

        void onError(Exception exc);
    }

    /* loaded from: classes2.dex */
    public interface SerialPortSearchListener {
        void getAddress(String str);

        void notFound();
    }

    public static void init(String str, boolean z) {
        defDev = str;
        autoFindDev = z;
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

//    public static void moveLine(int i) {
//        postCmd(PrintCmd.PrintFeedline(i));
//    }

    public static void setSerialPortSearchListener(SerialPortSearchListener serialPortSearchListener2) {
        serialPortSearchListener = serialPortSearchListener2;
    }

    public static boolean findPrinter() {
        return findPrinter(null);
    }

    public static boolean findPrinter(List<String> list) {
        SerialPortSearchListener serialPortSearchListener2;
        ArrayList arrayList;
        int i;
        int available;
        byte[] bArr;
        boolean z;
        SerialPort serialPort2 = serialPort;
        if (serialPort2 != null) {
            SerialPortSearchListener serialPortSearchListener3 = serialPortSearchListener;
            if (serialPortSearchListener3 != null) {
//                serialPortSearchListener3.getAddress(serialPort2.gettDdevicePath());
            }
            return true;
        }
        Loger.writeLog("Printor", "在正查找打印机...");
        try {
            arrayList = new ArrayList();
            if (autoFindDev) {
                String asString = CacheHelper.getFileCache().getAsString("MS_Printor_LAST_DEV");
                String asString2 = CacheHelper.getFileCache().getAsString("MS_Printor_LAST_BAT");
                if (asString == null) {
                    asString = "";
                }
                if (asString2 == null) {
                    asString2 = "9600";
                }
                if (asString.length() > 0) {
                    arrayList.add(new ComPathItem(asString, Integer.parseInt(asString2)));
                }
                Loger.writeLog("Printor", "正在扫描串口...");
                for (String str : new SerialPortFinder().getAllDevicesPath()) {
                    if (list != null) {
                        try {
                            Iterator<String> it = list.iterator();
                            while (it.hasNext()) {
                                if (str.equals(it.next())) {
                                    z = false;
                                    break;
                                }
                            }
                        } catch (Exception unused) {
                        }
                    }
                    z = true;
                    if (z && !str.contains("USB") && !str.contains(UsbFileUtil.DEFAULT_BIN_DIR)) {
                        arrayList.add(new ComPathItem(str, 9600));
                    }
                }
                Loger.writeLog("Printor", "共发现" + arrayList.size() + "个串口");
            } else {
                arrayList.add(new ComPathItem(defDev, 9600));
            }
        } catch (Exception unused2) {
            serialPort = null;
        }
        for (i = 0; i < arrayList.size(); i++) {
            ComPathItem comPathItem = (ComPathItem) arrayList.get(i);
            if (!autoFindDev) {
                try {
                    serialPort = SerialPort.newBuilder(comPathItem.dev, comPathItem.baudrate).parity(0).dataBits(8).stopBits(1).build();
                } catch (Exception unused3) {
                    serialPort = null;
                }
            } else {
                Loger.writeLog("Printor", "正在测试第" + i + "个串口" + comPathItem.dev);
                try {
                    SerialPort serialPort3 = new SerialPort(comPathItem.dev, comPathItem.baudrate, 0);
                    serialPort = serialPort3;
//                    serialPort3.getOutputStream().write(PrintCmd.SetClean());
                    serialPort.getOutputStream().flush();
//                    serialPort.getOutputStream().write(PrintCmd.GetStatus());
                    serialPort.getOutputStream().flush();
                    Thread.sleep(500L);
                    Loger.writeLog("Printor", "已向串口发送测试指令");
                    available = serialPort.getInputStream().available();
                    bArr = new byte[available];
                    serialPort.getInputStream().read(bArr);
                } catch (Exception unused4) {
                    serialPort = null;
                }
                if (bArr[0] == 22 && bArr[1] == 18 && bArr[2] == 18 && bArr[3] == 16) {
                    Loger.writeLog("Printor", "已找到设备" + comPathItem.dev);
//                    Loger.writeLog("Printor", ObjectHelper.hex2String(bArr, available));
                    comPathItem.hasConnected = true;
                    CacheHelper.getFileCache().put("MS_Printor_LAST_DEV", comPathItem.dev);
                    CacheHelper.getFileCache().put("MS_Printor_LAST_BAT", "" + comPathItem.baudrate);
                    SerialPortSearchListener serialPortSearchListener4 = serialPortSearchListener;
                    if (serialPortSearchListener4 != null) {
                        serialPortSearchListener4.getAddress(comPathItem.dev);
                    }
                    Thread thread = new Thread(new ReadRunnable());
                    readThread = thread;
                    thread.start();
                    break;
                }
                Loger.writeLog("Printor", "测试失败");
                serialPort.close();
                serialPort = null;
            }
            serialPort = null;
        }
        if (serialPort == null && (serialPortSearchListener2 = serialPortSearchListener) != null) {
            serialPortSearchListener2.notFound();
        }
        Loger.writeLog("Printor", "查找打印机结果：" + serialPort);
        if (serialPort == null) {
            AppStatusLoger.addAppStatus_Count(null, "Utils", AppStatusLoger.Type_DeviceError, "", "查找打印机失败");
        }
        return serialPort != null;
    }

    public static void cutpaper(int i) {
//        postCmd(PrintCmd.PrintFeedline(i));
//        postCmd(PrintCmd.PrintCutpaper(1));
//        postCmd(PrintCmd.SetClean());
    }

    public static Bitmap string2Bmp(String str, int i, boolean z) {
        Paint paint = new Paint();
        paint.setTextSize(i * 2);
        paint.setFakeBoldText(z);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        paint.setColor(-12303292);
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        double width = rect.width();
        Double.isNaN(width);
        int i2 = (int) (width * 1.2d);
        double height = rect.height();
        Double.isNaN(height);
        Bitmap createBitmap = Bitmap.createBitmap(i2, (int) (height * 1.2d), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Double.isNaN(rect.height());
        canvas.drawText(str, 0.0f, (int) (r2 * 1.1d), paint);
        return createBitmap;
    }

    public static void clean() {
        postCmd(PrintCmd.SetClean());
    }

    public static void setData(byte[] bArr) {
        postCmd(bArr);
    }

    public static void printQRCode(String str, int i, int i2, int i3) {
        postCmd(PrintCmd.PrintQrcode(str, i, i2, i3));
    }

    private static int[] getBitmapParamsData(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[width * height];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        return iArr;
    }

    public static void printBitmap(Bitmap bitmap, boolean z) {
        postCmd(PrintCmd.PrintDiskImagefile(getBitmapParamsData(bitmap), bitmap.getWidth(), bitmap.getHeight()));
        postCmd(PrintCmd.PrintFeedDot(5));
    }

    public static void printBitmap(Bitmap bitmap) {
        postCmd(PrintCmd.PrintDiskImagefile(getBitmapParamsData(bitmap), bitmap.getWidth(), bitmap.getHeight()));
    }

    public static void printString(String str, boolean z, int i, boolean z2, int i2, int i3, int i4) {
        postCmd(PrintCmd.SetReadZKmode(!z ? 1 : 0));
        postCmd(PrintCmd.SetBold(z2 ? 1 : 0));
        if (i2 > 0) {
            postCmd(PrintCmd.SetLeftmargin(i2));
            postCmd(PrintCmd.SetRightmargin(i2));
        }
        postCmd(PrintCmd.SetSizetext(i, i));
        if (i3 <= 0) {
            i3 = 1;
        }
        postCmd(PrintCmd.SetSpacechar(i3));
        if (i4 <= 0) {
            i4 = 1;
        }
        postCmd(PrintCmd.SetLinespace(i4));
        postCmd(PrintCmd.PrintString(str, !str.endsWith(StringUtils.LF) ? 1 : 0));
    }

    public static boolean close() {
        cancel();
        try {
            SerialPort serialPort2 = serialPort;
            if (serialPort2 == null) {
                return true;
            }
            try {
                serialPort2.getInputStream().close();
            } catch (Exception unused) {
            }
            try {
                serialPort.getOutputStream().close();
            } catch (Exception unused2) {
            }
            try {
                serialPort.close();
            } catch (Exception unused3) {
            }
            serialPort = null;
            return true;
        } catch (Exception unused4) {
            return false;
        }
    }

    public static void cancel() {
        isCanced = true;
    }

    public static void postCmd(byte[] bArr) {
        currentCmd = bArr;
        postCmdStartTime = System.currentTimeMillis();
        isCanced = false;
        if (findPrinter()) {
            Loger.writeLog("Card", ".....");
            if (isCanced) {
                return;
            }
            try {
                try {
                    OutputStream outputStream = serialPort.getOutputStream();
                    if (outputStream == null) {
                        throw new Exception("serialPort is closed");
                    }
                    byte[] bArr2 = currentCmd;
                    outputStream.write(bArr2, 0, bArr2.length);
                    outputStream.flush();
                } catch (IOException unused) {
                    SerialPort serialPort2 = serialPort;
                    if (serialPort2 != null) {
                        serialPort2.close();
                    }
                    serialPort = null;
                } catch (Exception e) {
                    currentResultListener.onError(e);
                }
            } catch (Exception unused2) {
                serialPort = null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class ReadRunnable implements Runnable {
        ReadRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                MsPrintor.mBuffer.reset();
                Loger.writeLog("Printor", "-------start--------");
                long currentTimeMillis = System.currentTimeMillis();
                InputStream inputStream = MsPrintor.serialPort.getInputStream();
                loop0: while (true) {
                    boolean z = false;
                    while (!MsPrintor.isCanced) {
                        int available = inputStream.available();
                        if (available > 0) {
                            z = true;
                        }
                        long currentTimeMillis2 = System.currentTimeMillis();
                        if (z) {
                            if (available == 0) {
                                byte[] byteArray = MsPrintor.mBuffer.toByteArray();
                                if (MsPrintor.currentResultListener != null) {
                                    MsPrintor.currentResultListener.onAcceptMessage(byteArray);
                                }
                                Loger.writeLog("Printor", "msg:" + ObjectHelper.hex2String(byteArray, byteArray.length));
                                MsPrintor.mBuffer.reset();
                            } else {
                                byte[] bArr = new byte[available];
                                inputStream.read(bArr, 0, available);
                                MsPrintor.mBuffer.write(bArr);
                                MsPrintor.mBuffer.flush();
                                try {
                                    Thread.sleep(100L);
                                } catch (Exception unused) {
                                }
                            }
                        } else if (currentTimeMillis2 - currentTimeMillis > 500) {
                            MsPrintor.mBuffer.reset();
                        }
                    }
                    break loop0;
                }
            } catch (Exception e) {
                Loger.writeException("Printor", e);
            }
            Loger.writeLog("Printor", "-------close--------");
        }
    }
}
