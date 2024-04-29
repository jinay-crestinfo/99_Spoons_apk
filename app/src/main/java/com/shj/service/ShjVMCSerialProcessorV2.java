package com.shj.service;

import android.content.Context;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.io.XyStreamParser;
import com.shj.Shj;
import com.shj.command.Command;
import com.shj.command.CommandManager;
import com.shj.commandV2.CommandV2;
import com.shj.device.VMCStatus;
import com.shj.service.ShjVMCSerialClientManager;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.UByte;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class ShjVMCSerialProcessorV2 {
    static byte[] ackBytes = {-6, -5, 66, 0, 67};
    static int lastDataType;
    static ShjVMCSerialProcessorV2 processor;
    static ShjVMCSerialClientManager socProcessor;
    MyStreamParserListener parseListener = new MyStreamParserListener();
    MySerialHandler serialHandler = new MySerialHandler();

    public void stop() {
    }

    /* loaded from: classes2.dex */
    public class MySerialHandler implements ShjVMCSerialClientManager.SerialEventListener {
        int lastSn = -1;
        long lastStatusCmdTime = -2147483648L;

        public void onAckTimeOut() {
        }

        @Override // com.shj.service.ShjVMCSerialClientManager.SerialEventListener
        public void onConnected() {
        }

        @Override // com.shj.service.ShjVMCSerialClientManager.SerialEventListener
        public void onDisconnect() {
        }

        @Override // com.shj.service.ShjVMCSerialClientManager.SerialEventListener
        public void onError(String str) {
        }

        @Override // com.shj.service.ShjVMCSerialClientManager.SerialEventListener
        public void onInit() {
        }

        MySerialHandler() {
        }

        public boolean isMessage(byte[] bArr) {
            byte b = bArr[2];
            return (b == 65 || b == 66) ? false : true;
        }

        public boolean isAck(byte[] bArr) {
            return bArr[2] == 66;
        }

        public boolean isPoll(byte[] bArr) {
            return bArr[2] == 65;
        }

        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:51:0x0137 -> B:46:0x01a2). Please report as a decompilation issue!!! */
        @Override // com.shj.service.ShjVMCSerialClientManager.SerialEventListener
        public void onMessage(byte[] bArr, boolean z) {
            if (Shj.isStoped()) {
                return;
            }
            Shj.onUpdateShjStatus(VMCStatus.Normal);
            Shj.updateVMCConnectStateTime();
            if (isMessage(bArr)) {
                ShjVMCSerialProcessorV2.lastDataType = 0;
                if (Shj.isResetFinished() && ObjectHelper.intFromBytes(bArr, 2, 1) == 82) {
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastStatusCmdTime < 60000) {
                        ShjVMCSerialProcessorV2.socProcessor.client.send(ShjVMCSerialProcessorV2.ackBytes);
                    } else {
                        this.lastStatusCmdTime = currentTimeMillis;
                    }
                }
                Loger.writeLog("COMMAND", "上报:" + ObjectHelper.hex2String(bArr, bArr.length));
                if (!z) {
                    ShjVMCSerialProcessorV2.socProcessor.client.send(ShjVMCSerialProcessorV2.ackBytes);
                }
                Loger.writeLog("COMMAND", "ACK");
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 4, 1);
                if (intFromBytes == this.lastSn) {
                    Loger.writeLog("SHJ", "数据包重复");
                    AppStatusLoger.addAppStatus_Count(null, "SHJ", AppStatusLoger.Type_Serial, "09000004", "数据包重复");
                    return;
                } else {
                    this.lastSn = intFromBytes;
                    CommandManager.appendReceivedRawCommand(bArr);
                    return;
                }
            }
            if (isPoll(bArr)) {
                ShjVMCSerialProcessorV2.lastDataType = 1;
                Queue<Command> sendCommandQueue = CommandManager.getSendCommandQueue();
                Command peek = sendCommandQueue.peek();
                if (peek == null) {
                    if (z || Shj.isDebug()) {
                        return;
                    }
                    ShjVMCSerialProcessorV2.socProcessor.client.send(ShjVMCSerialProcessorV2.ackBytes);
                    return;
                }
                if (peek.getSendRepeatCount() > 3 || !peek.isValid()) {
                    if (!Shj.isDebug() && !z) {
                        ShjVMCSerialProcessorV2.socProcessor.client.send(ShjVMCSerialProcessorV2.ackBytes);
                    }
                    sendCommandQueue.poll();
                    Loger.writeLog("SHJ;COMMAND", "下发指令超时：" + peek.toString() + StringUtils.SPACE + ObjectHelper.hex2String(peek.getRawCommand()));
                    return;
                }
                if (peek.getType() != Command.CommandType.VCMD && !Shj.isDebug()) {
                    CommandManager.setCurrentCommand(peek);
                }
                try {
                    peek.addSendRepeatCount();
                    if (peek.getType() != Command.CommandType.VCMD) {
                        byte[] rawCommand = peek.getRawCommand();
                        Loger.writeLog("SHJ;COMMAND", "下发：" + peek.toString() + StringUtils.SPACE + ObjectHelper.hex2String(rawCommand, rawCommand.length) + " sendCount:" + peek.getSendRepeatCount());
                        if (!Shj.isDebug()) {
                            peek.doCommand();
                            ShjVMCSerialProcessorV2.socProcessor.client.send(rawCommand);
                        } else {
                            sendCommandQueue.poll();
                        }
                    } else {
                        peek.doCommand();
                        sendCommandQueue.poll();
                    }
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                    Loger.writeException("SHJ", e);
                }
                return;
            }
            if (ShjVMCSerialProcessorV2.lastDataType == 1 && !Shj.isDebug() && isAck(bArr)) {
                ShjVMCSerialProcessorV2.lastDataType = 2;
                try {
                    ((CommandV2) CommandManager.getSendCommandQueue().poll()).onAck();
                    Loger.writeLog("COMMAND", "ACP");
                } catch (Exception e2) {
                    Loger.safe_inner_exception_catch(e2);
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class MyStreamParserListener implements XyStreamParser.XyStreamParserListener {
        short head0 = 250;
        short head1 = 251;
        int maxLen = 255;
        int minLen = 5;
        int offset = 0;

        @Override // com.oysb.utils.io.XyStreamParser.XyStreamParserListener
        public int maxPaseBuffer() {
            return 1024;
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyStreamParserListener
        public byte[] paseState(int i, XyStreamParser.XyDataInputStream xyDataInputStream) throws Exception {
            return null;
        }

        MyStreamParserListener() {
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyStreamParserListener
        public synchronized byte[] paseBytes(byte[] bArr) {
            int i;
            if (bArr.length < 5) {
                return null;
            }
            this.offset = 0;
            byte[] bArr2 = null;
            int i2 = 0;
            while (true) {
                i = this.offset;
                if (i > bArr.length - this.minLen) {
                    break;
                }
                try {
                    short s = (short) (bArr[i] & UByte.MAX_VALUE);
                    short s2 = (short) (bArr[i + 1] & UByte.MAX_VALUE);
                    if (this.head0 == s && this.head1 == s2) {
                        short s3 = (short) (bArr[i + 3] & UByte.MAX_VALUE);
                        if (i + 4 + s3 > bArr.length - 1) {
                            break;
                        }
                        if (((short) (bArr[i + 4 + s3] & UByte.MAX_VALUE)) == computerXor(bArr, i, s3 + 4)) {
                            int i3 = s3 + 5;
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
                i2 = i;
            }
            i2 = i;
            if (i2 > 0) {
                try {
                    Loger.writeLog("SHJ", "------------");
                    Loger.writeLog("SHJ", "[dataSize:" + bArr.length + "] " + ObjectHelper.hex2String(bArr));
                    AppStatusLoger.addAppStatus_Count(null, "SHJ", AppStatusLoger.Type_Serial, "09000005", "数据包格式错误");
                } catch (Exception unused) {
                }
            }
            return bArr2;
        }

        private short computerXor(byte[] bArr, int i, int i2) {
            short s = 0;
            for (int i3 = i; i3 < i + i2; i3++) {
                s = (short) (s ^ bArr[i3]);
            }
            return (short) (s & 255);
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyStreamParserListener
        public int paseOffset() {
            return this.offset;
        }
    }

    public static ShjVMCSerialProcessorV2 getProcessor() {
        if (processor == null) {
            processor = new ShjVMCSerialProcessorV2();
        }
        return processor;
    }

    public void start(Context context) {
        Loger.writeLog("APP", "ShjVMCSerialProcessor isDebug:" + Shj.isDebug() + " isStoped:" + Shj.isStoped());
        ShjVMCSerialClientManager processor2 = ShjVMCSerialClientManager.getProcessor();
        socProcessor = processor2;
        if (processor2.isRunning()) {
            return;
        }
        Loger.writeLog("APP", "ShjVMCSerialProcessor start new one" + socProcessor);
        socProcessor.setHost(Shj.getComPath(), (int) Shj.getComBaudrate());
        socProcessor.setEventHandler(this.serialHandler);
        socProcessor.setParseListener(this.parseListener);
        socProcessor.start(context);
        if (Shj.isDebug()) {
            new Timer().schedule(new TimerTask() { // from class: com.shj.service.ShjVMCSerialProcessorV2.1
                AnonymousClass1() {
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    try {
                        if (ShjVMCSerialProcessorV2.this.serialHandler != null) {
                            ShjVMCSerialProcessorV2.this.serialHandler.onMessage(new byte[]{-6, -5, 65, 64}, false);
                        }
                    } catch (Exception e) {
                        Loger.safe_inner_exception_catch(e);
                        Loger.writeException("SHJ", e);
                    }
                }
            }, 1000L, 200L);
        }
    }

    /* renamed from: com.shj.service.ShjVMCSerialProcessorV2$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                if (ShjVMCSerialProcessorV2.this.serialHandler != null) {
                    ShjVMCSerialProcessorV2.this.serialHandler.onMessage(new byte[]{-6, -5, 65, 64}, false);
                }
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                Loger.writeException("SHJ", e);
            }
        }
    }
}
