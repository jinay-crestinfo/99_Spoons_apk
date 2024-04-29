package com.oysb.xy.net;

import android.content.Context;
//import com.google.android.exoplayer.hls.HlsChunkSource;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.io.XyStreamParser;
import com.oysb.xy.net.report.Report;
import com.oysb.xy.net.report.Report_EmptyCmd;
import com.oysb.xy.net.report.Report_Heartbeat;
import com.oysb.xy.net.socket.SocketProcessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class NetProcessor implements SocketProcessor.MessageProcessor {
    static final int CHECK_TIME = 300000;
    static final int EVENT_ACK_TIMEOUT = 2001;
    static final int EVENT_NOTICE_SOCDISCONNECT = 3000;
    static final int EVENT_QUEUE_TIMEOUT = 2002;
    static final int EVENT_REPORT_TIMEOUT = 2003;
    static final int EVENT_SEND_REPORT = 2000;
    static long lastConnectTime = Long.MAX_VALUE;
    static NetProcessor processor;
    Timer ackCheckTimer;
    Object obj = new Object();
    MyStreamParserListener parseListener;
    ProcessRunnable processThread;
    Queue<Report> queue;
    Timer queueCheckTimer;
    Report report;
    Timer reportCheckTimer;

    /* loaded from: classes2.dex */
    static class MyStreamParserListener implements XyStreamParser.XyStreamParserListener {
        static Map<Short, String> heads;
        int maxLen = 2048;
        int minLen = 5;
        int heartAckLen = 3;
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

        static {
            HashMap hashMap = new HashMap();
            heads = hashMap;
            hashMap.put((short) 29, "");
            heads.put((short) 48, "");
            heads.put((short) 33, "");
            heads.put((short) 34, "");
            heads.put((short) 35, "");
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyStreamParserListener
        public byte[] paseBytes(byte[] bArr) {
            this.offset = 0;
            if ((bArr[0] & UByte.MAX_VALUE) == 35 && (bArr[4] & UByte.MAX_VALUE) == 113 && bArr.length < 1035) {
                return null;
            }
            while (true) {
                int i = this.offset;
                if (i > bArr.length - this.heartAckLen) {
                    return null;
                }
                if (heads.containsKey(Short.valueOf((short) (bArr[i] & UByte.MAX_VALUE)))) {
                    int isDataType1 = isDataType1(bArr, this.offset);
                    if (isDataType1 > 0) {
                        byte[] bArr2 = new byte[isDataType1];
                        System.arraycopy(bArr, this.offset, bArr2, 0, isDataType1);
                        this.offset += isDataType1;
                        return bArr2;
                    }
                    int isDataType2 = isDataType2(bArr, this.offset);
                    if (isDataType2 > 0) {
                        int i2 = isDataType2 + 6;
                        byte[] bArr3 = new byte[i2];
                        System.arraycopy(bArr, this.offset, bArr3, 0, i2);
                        this.offset += isDataType2;
                        return bArr3;
                    }
                    int isDataType3 = isDataType3(bArr, this.offset);
                    if (isDataType3 > 0) {
                        byte[] bArr4 = new byte[isDataType3];
                        System.arraycopy(bArr, this.offset, bArr4, 0, isDataType3);
                        this.offset += isDataType3;
                        return bArr4;
                    }
                    if (isHeartBeatAck(bArr, this.offset)) {
                        int i3 = this.heartAckLen;
                        byte[] bArr5 = new byte[i3];
                        System.arraycopy(bArr, this.offset, bArr5, 0, i3);
                        this.offset += this.heartAckLen;
                        return bArr5;
                    }
                }
                this.offset++;
            }
        }

        private boolean isHeartBeatAck(byte[] bArr, int i) {
            try {
                short s = (short) (bArr[(this.heartAckLen + i) - 1] & UByte.MAX_VALUE);
                short s2 = 0;
                for (int i2 = 0; i2 < this.heartAckLen - 1; i2++) {
                    s2 = (short) (s2 + ((short) (bArr[i + i2] & UByte.MAX_VALUE)));
                }
                return ((short) (s2 & 255)) == s;
            } catch (Exception unused) {
                return false;
            }
        }

        private int isServerCmd(byte[] bArr, int i) {
            if ((bArr[0] & UByte.MAX_VALUE) == 29) {
                return 0;
            }
            if (((short) (bArr[0] & UByte.MAX_VALUE)) != 48) {
                return -1;
            }
            short s = (short) (bArr[i + 2] & UByte.MAX_VALUE);
            short s2 = (short) (bArr[(i + s) - 1] & UByte.MAX_VALUE);
            short s3 = 0;
            for (int i2 = 0; i2 < s - 1; i2++) {
                s3 = (short) (s3 + ((short) (bArr[i + i2] & UByte.MAX_VALUE)));
            }
            if (s2 == ((short) (s3 & 255))) {
                return s;
            }
            return 0;
        }

        private int isDataType1(byte[] bArr, int i) {
            try {
                short s = (short) (bArr[i + 2] & UByte.MAX_VALUE);
                int i2 = (i + s) - 1;
                if (i2 < bArr.length) {
                    short s2 = (short) (bArr[i2] & UByte.MAX_VALUE);
                    short s3 = 0;
                    for (int i3 = 0; i3 < s - 1; i3++) {
                        s3 = (short) (s3 + ((short) (bArr[i + i3] & UByte.MAX_VALUE)));
                    }
                    if (s2 == ((short) (s3 & 255))) {
                        return s;
                    }
                }
            } catch (Exception unused) {
            }
            return 0;
        }

        private int isDataType2(byte[] bArr, int i) {
            try {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, i + 3, 2);
                int i2 = i + intFromBytes + 5;
                if (i2 < bArr.length) {
                    short s = (short) (bArr[i2] & UByte.MAX_VALUE);
                    short s2 = 0;
                    for (int i3 = 0; i3 < (intFromBytes + 6) - 1; i3++) {
                        s2 = (short) (s2 + ((short) (bArr[i + i3] & UByte.MAX_VALUE)));
                    }
                    if (s == ((short) (s2 & 255))) {
                        return intFromBytes;
                    }
                }
            } catch (Exception unused) {
            }
            return 0;
        }

        private int isDataType3(byte[] bArr, int i) {
            try {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, i + 2, 2);
                int i2 = (i + intFromBytes) - 1;
                if (i2 < bArr.length) {
                    short s = (short) (bArr[i2] & UByte.MAX_VALUE);
                    short s2 = 0;
                    for (int i3 = 0; i3 < intFromBytes - 1; i3++) {
                        s2 = (short) (s2 + ((short) (bArr[i + i3] & UByte.MAX_VALUE)));
                    }
                    if (s == ((short) (s2 & 255))) {
                        return intFromBytes;
                    }
                }
            } catch (Exception unused) {
            }
            return 0;
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyStreamParserListener
        public int paseOffset() {
            return this.offset;
        }

        public boolean isMessage(byte[] bArr) {
            short s = (short) (bArr[0] & UByte.MAX_VALUE);
            return s == 48 || s == 33 || s == 35;
        }

        public boolean isAck(byte[] bArr) {
            return ((short) (bArr[0] & UByte.MAX_VALUE)) == 29 || ((short) (bArr[0] & UByte.MAX_VALUE)) == 28;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ProcessRunnable implements Runnable {
        boolean run;

        private ProcessRunnable() {
            this.run = true;
        }



        public void stop() {
            this.run = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            while (this.run) {
                try {
                    if (!NetManager.shuldSendReport(null)) {
                        Thread.sleep(1000L);
                    } else {
                        if (NetProcessor.this.queue == null) {
                            NetProcessor.this.queue = NetManager.getReportQueue();
                        }
                        if (NetProcessor.this.queue.isEmpty()) {
                            Thread.sleep(50L);
                        } else {
                            Report peek = NetProcessor.this.queue.peek();
                            if (peek instanceof Report_EmptyCmd) {
                                ((Report_EmptyCmd) peek).postCmdFinished();
                                NetProcessor.this.queue.poll();
                            } else if (peek.lostAble() && peek.getAppendTime().getTime() < NetProcessor.lastConnectTime) {
                                NetProcessor.this.queue.poll();
                            } else {
                                synchronized (NetProcessor.this.obj) {
                                    if (NetProcessor.this.sendReport(peek)) {
                                        NetProcessor.this.obj.wait();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    public boolean sendReport(Report report) {
        if (report == this.report) {
            return true;
        }
        this.report = report;
        if (report != null && NetManager.shuldSendReport(report)) {
            try {
                SocketProcessor socketProcessor = NetManager.getSocketProcessor();
                this.report.setSendTime(new Date());
                socketProcessor.send(this.report.getRawData());
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public void startMessageSendProcessor(Context context) {
        ProcessRunnable processRunnable = this.processThread;
        if (processRunnable == null || !processRunnable.run) {
            this.processThread = new ProcessRunnable();
            new Thread(this.processThread).start();
        }
    }

    public void stopMessageSendProcessor() {
        try {
            this.processThread.stop();
        } catch (Exception unused) {
        }
    }

    @Override // com.oysb.xy.net.socket.SocketProcessor.MessageProcessor
    public XyStreamParser.XyStreamParserListener getXyStreamParserListener() {
        if (this.parseListener == null) {
            this.parseListener = new MyStreamParserListener();
        }
        return this.parseListener;
    }

    @Override // com.oysb.xy.net.socket.SocketProcessor.MessageProcessor
    public void onSendMessage(byte[] bArr) {
        NetManager.onSendReport(this.report);
    }

    @Override // com.oysb.xy.net.socket.SocketProcessor.MessageProcessor
    public void onAcceptMessage(byte[] bArr, boolean z) {
        if (this.parseListener.isAck(bArr)) {
            if (NetManager.onAck(bArr)) {
                synchronized (this.obj) {
                    this.obj.notifyAll();
                }
                return;
            }
            return;
        }
        if (this.parseListener.isMessage(bArr)) {
            NetManager.onMessage(bArr);
            Report peek = this.queue.peek();
            if (peek != null && (peek.getDataType() & UByte.MAX_VALUE) == 34 && (peek.getBizType() & UByte.MAX_VALUE) == 113) {
                this.queue.poll();
                synchronized (this.obj) {
                    this.obj.notifyAll();
                }
            }
        }
    }

    @Override // com.oysb.xy.net.socket.SocketProcessor.MessageProcessor
    public boolean onTimeOut() {
        Loger.writeLog("NET", "onTimeOut");
        try {
            this.report.addResendCount();
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
        if (this.report.needResend() && this.queue.size() == 1) {
            sendReport(this.report);
            return false;
        }
        NetManager.onAckTimeOut(this.report);
        if (this.report.lostAble() || this.report.doNotStoreInDb()) {
            if (this.queue.peek() == this.report) {
                this.queue.poll();
            }
            synchronized (this.obj) {
                this.obj.notifyAll();
            }
            if (this.report.shuldReConnectOnTimeOut()) {
                return true;
            }
            if (this.queue.size() == 0) {
                NetManager.onNeedHeartBeat();
            }
            return false;
        }
        return true;
    }

    public void cancelCurrentLostAbleReport() {
        Report report = this.report;
        if (report == null || report.getResult() != null) {
            return;
        }
        Loger.writeLog("NET", "cancelCurrentLostAbleReport:" + new String(this.report.getRawData()));
        if (this.report.lostAble() || (this.report instanceof Report_Heartbeat)) {
            if (this.queue.peek() == this.report) {
                this.queue.poll();
            }
            synchronized (this.obj) {
                this.obj.notifyAll();
            }
        }
    }

    @Override // com.oysb.xy.net.socket.SocketProcessor.MessageProcessor
    public void onConnect() {
        lastConnectTime = System.currentTimeMillis();
        NetManager.onConnected();
    }

    @Override // com.oysb.xy.net.socket.SocketProcessor.MessageProcessor
    public void onDisConnect() {
        synchronized (this.obj) {
            this.obj.notifyAll();
        }
        NetManager.onTcpDisConnect("");
    }

    @Override // com.oysb.xy.net.socket.SocketProcessor.MessageProcessor
    public void onError() {
        NetManager.onError(null);
    }

    @Override // com.oysb.xy.net.socket.SocketProcessor.MessageProcessor
    public void onNeedHeartBeat() {
        try {
            if (NetManager.getCurrentReport() != null && (NetManager.getCurrentReport() instanceof Report_Heartbeat)) {
                this.queue.poll();
                synchronized (this.obj) {
                    this.obj.notifyAll();
                }
                Report peek = this.queue.peek();
                if (peek != null && (peek instanceof Report_Heartbeat)) {
                    return;
                }
            }
            NetManager.onNeedHeartBeat();
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }
}
