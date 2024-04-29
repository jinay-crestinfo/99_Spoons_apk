package com.shj.device.scanor;

import android.content.Context;
import android.serialport.SerialClient;
import android.serialport.SerialPort;
import android.serialport.SerialPortFinder;
import android.util.Log;
import com.oysb.utils.Loger;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.io.XyStreamParser;
import com.oysb.utils.io.file.SDFileUtils;
import com.printsdk.cmd.PrintCmd;
import com.shj.Shj;
import com.shj.setting.Utils.UsbFileUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class ScanorSerialManager {
    Timer checkTimer;
    SerialClient client;
    MyStreamParserListener parseListener = new MyStreamParserListener();
    MySerialHandler serialHandler = new MySerialHandler();
    ArrayList<ComPathItem> comPaths = null;
    boolean isFinding = false;
    long connectTime = Long.MAX_VALUE;
    ExecutorService threadPool = null;

    /* loaded from: classes2.dex */
    public class ComPathItem {
        int baudrate;
        String dev;

        public ComPathItem(String str, int i) {
            this.dev = str;
            this.baudrate = i;
        }
    }

    public boolean isRunning() {
        SerialClient serialClient = this.client;
        return serialClient != null && serialClient.isThreadRunning();
    }

    /* loaded from: classes2.dex */
    public class MySerialHandler implements XyStreamParser.XyHandler {
        public void onAckTimeOut() {
        }

        MySerialHandler() {
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyHandler
        public void onMessage(byte[] bArr, boolean z) {
            String str = new String(bArr);
            Loger.writeLog("SCANOR", "上报:" + str);
            Scanor.onMessage(str);
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyHandler
        public void onError(String str) {
            Loger.writeLog("SCANOR", "读取串口数据出错");
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyHandler
        public void onConnect() {
            Scanor.connectState(true);
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyHandler
        public void onDisconnect(int i, String str) {
            Loger.writeLog("SCANOR", "串口已断开");
            Scanor.connectState(false);
        }
    }

    public void startCheckTimer() {
        if (this.checkTimer != null) {
            return;
        }
        Timer timer = new Timer();
        this.checkTimer = timer;
        timer.schedule(new TimerTask() { // from class: com.shj.device.scanor.ScanorSerialManager.1
            AnonymousClass1() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                boolean z;
                boolean z2 = false;
                if (ScanorSerialManager.this.isFinding) {
                    Loger.writeLog("SCANOR", "正在查找串口");
                    z = false;
                } else {
                    z = true;
                }
                long currentTimeMillis = System.currentTimeMillis() - ScanorSerialManager.this.connectTime;
                if (currentTimeMillis > 0 && currentTimeMillis < 10000) {
                    Loger.writeLog("SCANOR", "刚找到串口，并正在连接");
                    z = false;
                }
                if (ScanorSerialManager.this.client == null || !ScanorSerialManager.this.client.isThreadRunning()) {
                    z2 = z;
                } else {
                    Loger.writeLog("SCANOR", "串口运行正常");
                    Scanor.connectState(true);
                }
                if (z2) {
                    ScanorSerialManager.this.start(Scanor.getContext());
                }
            }
        }, 60000L, 30000L);
    }

    /* renamed from: com.shj.device.scanor.ScanorSerialManager$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            boolean z;
            boolean z2 = false;
            if (ScanorSerialManager.this.isFinding) {
                Loger.writeLog("SCANOR", "正在查找串口");
                z = false;
            } else {
                z = true;
            }
            long currentTimeMillis = System.currentTimeMillis() - ScanorSerialManager.this.connectTime;
            if (currentTimeMillis > 0 && currentTimeMillis < 10000) {
                Loger.writeLog("SCANOR", "刚找到串口，并正在连接");
                z = false;
            }
            if (ScanorSerialManager.this.client == null || !ScanorSerialManager.this.client.isThreadRunning()) {
                z2 = z;
            } else {
                Loger.writeLog("SCANOR", "串口运行正常");
                Scanor.connectState(true);
            }
            if (z2) {
                ScanorSerialManager.this.start(Scanor.getContext());
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class MyStreamParserListener implements XyStreamParser.XyStreamParserListener {
        int offset = 0;

        @Override // com.oysb.utils.io.XyStreamParser.XyStreamParserListener
        public int maxPaseBuffer() {
            return 2048;
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyStreamParserListener
        public byte[] paseState(int i, XyStreamParser.XyDataInputStream xyDataInputStream) throws Exception {
            return null;
        }

        MyStreamParserListener() {
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyStreamParserListener
        public synchronized byte[] paseBytes(byte[] bArr) {
            byte[] bArr2;
            this.offset = 0;
            int length = bArr.length;
            bArr2 = new byte[length];
            System.arraycopy(bArr, 0, bArr2, 0, length);
            this.offset += bArr.length;
            return bArr2;
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyStreamParserListener
        public int paseOffset() {
            return this.offset;
        }
    }

    public void start(Context context) {
        start(context, true);
    }

    public void start(Context context, boolean z) {
        if (this.threadPool == null) {
            this.threadPool = Executors.newSingleThreadExecutor();
        }
        this.threadPool.submit(new Runnable() { // from class: com.shj.device.scanor.ScanorSerialManager.2
            final /* synthetic */ Context val$context;
            final /* synthetic */ boolean val$isStartCheckTimer;

            AnonymousClass2(Context context2, boolean z2) {
                context = context2;
                z = z2;
            }

            @Override // java.lang.Runnable
            public void run() {
                ScanorSerialManager.this.searchPort(context, z);
            }
        });
    }

    /* renamed from: com.shj.device.scanor.ScanorSerialManager$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements Runnable {
        final /* synthetic */ Context val$context;
        final /* synthetic */ boolean val$isStartCheckTimer;

        AnonymousClass2(Context context2, boolean z2) {
            context = context2;
            z = z2;
        }

        @Override // java.lang.Runnable
        public void run() {
            ScanorSerialManager.this.searchPort(context, z);
        }
    }

    public void searchPort(Context context, boolean z) {
        ComPathItem comPathItem;
        Loger.writeLog("SCANOR", "开始扫描扫码头...");
        this.isFinding = true;
        this.connectTime = Long.MAX_VALUE;
        if (z) {
            startCheckTimer();
        }
        try {
            if (this.comPaths == null && context != null) {
                Loger.writeLog("SCANOR", "comPaths");
                this.comPaths = new ArrayList<>();
                String asString = CacheHelper.getFileCache().getAsString("SCANOR_LAST_DEV");
                String asString2 = CacheHelper.getFileCache().getAsString("SCANOR_LAST_BAT");
                if (asString == null) {
                    asString = "";
                }
                String str = asString;
                String str2 = "115200";
                if (asString2 == null) {
                    asString2 = "115200";
                }
                if (isInteger(asString2)) {
                    str2 = asString2;
                }
                Loger.writeLog("SCANOR", "lastBaudrate=" + str2);
                if (str.length() > 0 && !Scanor.isExDevPath(str)) {
                    this.comPaths.add(new ComPathItem(str, Integer.parseInt(str2)));
                }
                String[] allDevicesPath = new SerialPortFinder().getAllDevicesPath();
                Loger.writeLog("SCANOR", "devs length =" + allDevicesPath.length);
                for (String str3 : allDevicesPath) {
                    try {
                        if (!str3.contains(UsbFileUtil.DEFAULT_BIN_DIR) && !str3.contains("USB") && !Scanor.isExDevPath(str3)) {
                            String comPath = Shj.getComPath();
                            String linkPath = SDFileUtils.getLinkPath(comPath);
                            if (!str3.equalsIgnoreCase(comPath) && !str3.equalsIgnoreCase(linkPath) && !str3.equalsIgnoreCase(str)) {
                                Loger.writeLog("SCANOR", "add devs" + str3);
                                this.comPaths.add(new ComPathItem(str3, 115200));
                            }
                        }
                    } catch (Exception e) {
                        Loger.safe_inner_exception_catch(e);
                        e.printStackTrace();
                    }
                }
            }
            Loger.writeLog("SCANOR", "comPaths size =" + this.comPaths.size());
            Iterator<ComPathItem> it = this.comPaths.iterator();
            SerialPort serialPort = null;
            while (it.hasNext()) {
                ComPathItem next = it.next();
                Loger.writeLog("SCANOR", "正在测试:" + next.dev + StringUtils.SPACE + next.baudrate);
                try {
                    try {
                        Log.i("Serialport", next.dev);
                        SerialPort serialPort2 = new SerialPort(next.dev, next.baudrate, 0);
                        try {
                            serialPort2.getOutputStream().write(new byte[]{126, 85, 7, 69, 53, 48, 48, 50, 48, 48, 48, 59, 111});
                            serialPort2.getOutputStream().flush();
                            Loger.writeLog("SCANOR", "devicepath =" + serialPort2.gettDdevicePath());
                            long currentTimeMillis = System.currentTimeMillis();
                            while (System.currentTimeMillis() - currentTimeMillis < 500 && serialPort2.getInputStream().available() <= 0) {
                                Thread.sleep(10L);
                            }
                            if (serialPort2.getInputStream().available() > 0) {
                                Loger.writeLog("SCANOR", "content =" + serialPort2.getInputStream().available());
                                byte[] bArr = new byte[serialPort2.getInputStream().available()];
                                serialPort2.getInputStream().read(bArr);
                                String str4 = new String(bArr);
                                Loger.writeLog("SCANOR", "msg =" + str4 + "dev=" + next.dev);
                                if (str4.contains("END")) {
                                    if (str4.contains("DEVICEINFO")) {
                                        serialPort2.close();
                                        comPathItem = next;
                                        break;
                                    }
                                }
                                if (str4.equalsIgnoreCase("!")) {
                                    serialPort2.close();
                                    comPathItem = next;
                                    break;
                                }
                            }
                            serialPort2.getOutputStream().write("?".getBytes());
                            serialPort2.getOutputStream().flush();
                            long currentTimeMillis2 = System.currentTimeMillis();
                            while (System.currentTimeMillis() - currentTimeMillis2 < 500 && serialPort2.getInputStream().available() <= 0) {
                                Thread.sleep(10L);
                            }
                            if (serialPort2.getInputStream().available() > 0) {
                                Loger.writeLog("SCANOR", "content =" + serialPort2.getInputStream().available());
                                byte[] bArr2 = new byte[serialPort2.getInputStream().available()];
                                serialPort2.getInputStream().read(bArr2);
                                String str5 = new String(bArr2);
                                Loger.writeLog("SCANOR", "msg =" + str5 + "dev =" + next.dev);
                                if (str5.contains("END")) {
                                    if (str5.contains("DEVICEINFO")) {
                                        serialPort2.close();
                                        comPathItem = next;
                                        break;
                                    }
                                }
                                if (str5.equalsIgnoreCase("!")) {
                                    serialPort2.close();
                                    comPathItem = next;
                                    break;
                                }
                            }
                            serialPort2.getOutputStream().write(new byte[]{6, -57, 4, 0, -14, 1, -2, 60});
                            serialPort2.getOutputStream().flush();
                            long currentTimeMillis3 = System.currentTimeMillis();
                            while (System.currentTimeMillis() - currentTimeMillis3 < 500 && serialPort2.getInputStream().available() <= 0) {
                                Thread.sleep(10L);
                            }
                            if (serialPort2.getInputStream().available() > 0) {
                                Loger.writeLog("SCANOR", "content =" + serialPort2.getInputStream().available());
                                byte[] bArr3 = new byte[serialPort2.getInputStream().available()];
                                serialPort2.getInputStream().read(bArr3);
                                if (Arrays.equals(bArr3, new byte[]{8, -58, 0, 0, -1, -14, 1, 2, -3, 62})) {
                                    serialPort2.close();
                                    comPathItem = next;
                                    break;
                                }
                            }
                            serialPort2.getOutputStream().write(PrintCmd.SetClean());
                            serialPort2.getOutputStream().flush();
                            serialPort2.close();
                            serialPort = serialPort2;
                        } catch (Exception e2) {
                            e = e2;
                            serialPort = serialPort2;
                            e.printStackTrace();
                            if (serialPort != null) {
                                serialPort.close();
                            }
                        } catch (Throwable th) {
                            th = th;
                            serialPort = serialPort2;
                            if (serialPort != null) {
                                serialPort.close();
                            }
                            throw th;
                        }
                    } catch (Exception e3) {
                        e = e3;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            comPathItem = null;
            if (comPathItem != null) {
                Scanor.onMessage("Notice:已找到扫码头");
                CacheHelper.getFileCache().put("SCANOR_LAST_DEV", comPathItem.dev);
                CacheHelper.getFileCache().put("SCANOR_LAST_BAT", Integer.valueOf(comPathItem.baudrate));
                SerialClient serialClient = new SerialClient(comPathItem.dev, comPathItem.baudrate, this.serialHandler, this.parseListener);
                this.client = serialClient;
                serialClient.bindSerialPort();
                this.connectTime = System.currentTimeMillis();
            } else {
                Scanor.onMessage("Notice:一轮扫描完毕，没有找到扫码头");
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        this.isFinding = false;
    }

    public static boolean isInteger(String str) {
        return Pattern.compile("^[-\\+]?[\\d]*$").matcher(str).matches();
    }
}
