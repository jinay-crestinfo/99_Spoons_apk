package com.oysb.utils.io;

import com.oysb.utils.ObjectHelper;
import com.oysb.utils.date.DateUtil;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes2.dex */
public class XyStreamParser {
    private static ArrayList<String> logDetailNames = new ArrayList<>();
    private XyHandler handler;
    private XyStreamParserListener parseListener;
    private int stageCount = 0;
    private ByteArrayOutputStream mBuffer = new ByteArrayOutputStream();
    private ByteArrayOutputStream mBuffer_log = new ByteArrayOutputStream();
    private Boolean waitModel = false;
    private Boolean isWaiting = false;
    Boolean needStop = false;
    boolean logDetail = false;
    private boolean read2End = false;
    private int sleep = 10;
    private String name = "";
    boolean isReady = false;

    /* loaded from: classes2.dex */
    public interface XyHandler {
        void onConnect();

        void onDisconnect(int i, String str);

        void onError(String str);

        void onMessage(byte[] bArr, boolean z);
    }

    /* loaded from: classes2.dex */
    public interface XyStreamParserListener {
        int maxPaseBuffer();

        byte[] paseBytes(byte[] bArr);

        int paseOffset();

        byte[] paseState(int i, XyDataInputStream xyDataInputStream) throws Exception;
    }

    public synchronized void appendLog(byte[] bArr, boolean z) {
        try {
            String format = DateUtil.format(new Date(), "HHmmss");
            this.mBuffer_log.write(bArr);
            if (z) {
                Calendar.getInstance();
                this.mBuffer_log.write(ObjectHelper.str2Bcd(format));
                this.mBuffer_log.write(new byte[]{0, 0});
            }
            if (this.mBuffer_log.size() > 500) {
                byte[] byteArray = this.mBuffer_log.toByteArray();
                int length = byteArray.length - 500;
                while (length < byteArray.length - 250 && (byteArray[length] != 0 || byteArray[length + 1] != 0)) {
                    length++;
                }
                int length2 = (byteArray.length - length) - 2;
                byte[] bArr2 = new byte[length2];
                System.arraycopy(byteArray, length + 2, bArr2, 0, length2);
                this.mBuffer_log.reset();
                this.mBuffer_log.write(bArr2);
            }
        } catch (Exception unused) {
        }
    }

    public synchronized byte[] getBuffer_log() {
        return this.mBuffer_log.toByteArray();
    }

    public static void clearLogDetailNames() {
        if (logDetailNames == null) {
            logDetailNames = new ArrayList<>();
        }
        logDetailNames.clear();
    }

    public static void addLogDetailNames(String str) {
        logDetailNames.add(str);
    }

    public ArrayList<String> getLogDetailNames() {
        return logDetailNames;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setLogDetail(boolean z) {
        this.logDetail = z;
    }

    public void stop() {
        synchronized (this.needStop) {
            this.needStop = true;
        }
    }

    public void reset() {
        synchronized (this.needStop) {
            this.needStop = false;
        }
    }

    public void notifyWaiter() {
        synchronized (this.waitModel) {
            this.waitModel.notifyAll();
            this.isWaiting = true;
        }
    }

    /* loaded from: classes2.dex */
    public static class XyDataInputStream extends DataInputStream {
        public XyDataInputStream(InputStream inputStream) {
            super(inputStream);
        }

        public byte[] readBytes(int i) throws IOException {
            byte[] bArr = new byte[i];
            int i2 = 0;
            while (i2 < i) {
                int read = read(bArr, i2, i - i2);
                if (read == -1) {
                    break;
                }
                i2 += read;
            }
            if (i2 == i) {
                return bArr;
            }
            throw new IOException(String.format("Read wrong number of bytes. Got: %s, Expected: %s.", Integer.valueOf(i2), Integer.valueOf(i)));
        }
    }

    public void clearBuffer(XyDataInputStream xyDataInputStream) {
        try {
            xyDataInputStream.readBytes(xyDataInputStream.available());
            this.isReady = true;
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:116:0x0168, code lost:
    
        throw new java.lang.Exception("called stop");
     */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0173 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void start(com.oysb.utils.io.XyStreamParser.XyDataInputStream r10) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 393
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.io.XyStreamParser.start(com.oysb.utils.io.XyStreamParser$XyDataInputStream):void");
    }

    public XyStreamParserListener getParseListener() {
        return this.parseListener;
    }

    public void setParseListener(XyStreamParserListener xyStreamParserListener) {
        this.parseListener = xyStreamParserListener;
    }

    public XyHandler getHandler() {
        return this.handler;
    }

    public void setHandler(XyHandler xyHandler) {
        this.handler = xyHandler;
    }

    public boolean isWaitModel() {
        return this.waitModel.booleanValue();
    }

    public void setWaitModel(boolean z) {
        this.waitModel = Boolean.valueOf(z);
    }

    public int getSleep() {
        return this.sleep;
    }

    public void setSleep(int i) {
        this.sleep = i;
    }

    public boolean isRead2End() {
        return this.read2End;
    }

    public void setRead2End(boolean z) {
        this.read2End = z;
    }
}
