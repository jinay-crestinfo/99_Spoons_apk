package com.shj.device.lfpos.service;

import android.content.Context;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.io.XyStreamParser;
import com.shj.device.lfpos.command.Command;
import com.shj.device.lfpos.command.CommandManager;
import com.shj.device.lfpos.service.PosSerialClientManager;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class PosSerialProcessor {
    static final int EVENT_ACK_TIMEOUT = 2001;
    static final int EVENT_NOTICE_SOCDISCONNECT = 3000;
    static final int EVENT_SEND_REPORT = 2000;
    static byte[] ackBytes = {-6, -5, 66, 0, 67};
    static PosSerialProcessor processor;
    static PosSerialClientManager socProcessor;
    Timer ackCheckTimer;
    Queue<Command> queue;
    MyStreamParserListener parseListener = new MyStreamParserListener();
    MySerialHandler serialHandler = new MySerialHandler();

    public void stop() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class MySerialHandler implements PosSerialClientManager.SerialEventListener {
        int lastSn = -1;

        public void onAckTimeOut() {
        }

        @Override // com.shj.device.lfpos.service.PosSerialClientManager.SerialEventListener
        public void onError(String str) {
        }

        @Override // com.shj.device.lfpos.service.PosSerialClientManager.SerialEventListener
        public void onInit() {
        }

        MySerialHandler() {
        }

        public boolean isMessage(byte[] bArr) {
            return (ObjectHelper.intFromBytes(bArr, 10, 1) == 6 || ObjectHelper.intFromBytes(bArr, 11, 1) == 28) ? false : true;
        }

        public boolean isAck(byte[] bArr) {
            return ObjectHelper.intFromBytes(bArr, 10, 1) == 6 && ObjectHelper.intFromBytes(bArr, 11, 1) == 28;
        }

        public boolean isNak(byte[] bArr) {
            return ObjectHelper.intFromBytes(bArr, 10, 1) == 21 && ObjectHelper.intFromBytes(bArr, 11, 1) == 28;
        }

        @Override // com.shj.device.lfpos.service.PosSerialClientManager.SerialEventListener
        public void onConnected() {
            Loger.writeLog("LFPOS", "POS connected");
        }

        @Override // com.shj.device.lfpos.service.PosSerialClientManager.SerialEventListener
        public void onDisconnect() {
            Loger.writeLog("LFPOS", "POS disconnected");
        }

        @Override // com.shj.device.lfpos.service.PosSerialClientManager.SerialEventListener
        public void onMessage(byte[] bArr, boolean z) {
            Loger.writeLog("LFPOS", "上报:" + ObjectHelper.hex2String(bArr, bArr.length));
            CommandManager.appendReceivedRawCommand(bArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class MyStreamParserListener implements XyStreamParser.XyStreamParserListener {
        short head = 2;
        short end = 3;
        int maxLen = 255;
        int minLen = 13;
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
            byte[] bArr2 = null;
            if (bArr.length < this.minLen) {
                return null;
            }
            this.offset = 0;
            while (true) {
                int i = this.offset;
                if (i > bArr.length - this.minLen) {
                    break;
                }
                try {
                    if (this.head == ((short) (bArr[i] & UByte.MAX_VALUE))) {
                        int intFromBytes = ObjectHelper.intFromBytes(bArr, i + 1, 2);
                        int i2 = this.offset;
                        if (this.end == ((short) (bArr[i2 + intFromBytes + 3] & UByte.MAX_VALUE)) && ((short) (bArr[i2 + intFromBytes + 4] & UByte.MAX_VALUE)) == ObjectHelper.computerXor(bArr, i2 + 1, intFromBytes + 3)) {
                            int i3 = intFromBytes + 5;
                            bArr2 = new byte[i3];
                            System.arraycopy(bArr, this.offset, bArr2, 0, i3);
                            this.offset += i3;
                            break;
                        }
                    }
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                }
                this.offset++;
            }
            return bArr2;
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyStreamParserListener
        public int paseOffset() {
            return this.offset;
        }
    }

    public void resetTimer() {
        Timer timer = this.ackCheckTimer;
        if (timer != null) {
            timer.cancel();
            this.ackCheckTimer = null;
        }
        Timer timer2 = new Timer();
        this.ackCheckTimer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.shj.device.lfpos.service.PosSerialProcessor.1
            AnonymousClass1() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                PosSerialProcessor.this.serialHandler.onAckTimeOut();
            }
        }, HlsChunkSource.DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.device.lfpos.service.PosSerialProcessor$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            PosSerialProcessor.this.serialHandler.onAckTimeOut();
        }
    }

    public void cancelTimer() {
        Timer timer = this.ackCheckTimer;
        if (timer != null) {
            timer.cancel();
            this.ackCheckTimer = null;
        }
    }

    public static PosSerialProcessor getProcessor() {
        if (processor == null) {
            processor = new PosSerialProcessor();
        }
        return processor;
    }

    public void start(Context context) {
        PosSerialClientManager processor2 = PosSerialClientManager.getProcessor();
        socProcessor = processor2;
        if (processor2.isRunning()) {
            return;
        }
        socProcessor.setEventHandler(this.serialHandler);
        socProcessor.setParseListener(this.parseListener);
        socProcessor.start(context);
        new Thread(new ProcessRunnable()).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ProcessRunnable implements Runnable {
        boolean run;

        private ProcessRunnable() {
            this.run = true;
        }

        /* synthetic */ ProcessRunnable(PosSerialProcessor posSerialProcessor, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void stop() {
            this.run = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            while (this.run) {
                try {
                    if (PosSerialProcessor.this.queue == null) {
                        PosSerialProcessor.this.queue = CommandManager.getSendCommandQueue();
                    }
                    if (PosSerialProcessor.this.queue.isEmpty()) {
                        Thread.sleep(50L);
                    } else {
                        Command poll = PosSerialProcessor.this.queue.poll();
                        CommandManager.setCurrentCommand(poll);
                        PosSerialClientManager.getProcessor().send(poll.getRawCommand());
                        Thread.sleep(50L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
