package com.shj.service;

import android.content.Context;
import android.serialport.SerialPort;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.Shj;
import com.shj.command.Command;
import com.shj.command.CommandManager;
import java.io.File;
import kotlin.UByte;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.SocketClient;

/* loaded from: classes2.dex */
public class ShjVMCSerialProcessorV1 {
    public static int MAX_LENGTH = 64;
    public static int PROTOCOL_END = 255;
    public static int PROTOCOL_HEAD = 250;
    public static int PROTOCOL_LENGTH = 9;
    public static int PROTOCOL_POST_FLAG = 254;
    public static int PROTOCOL_POST_LENGTH = 21;
    static byte[] ackBytes = {-6, 0, 0, 0, 0, 0, 0, 0, -1};
    static ShjVMCSerialProcessorV1 processor = null;
    public static int readSleepTime = 50;
    SerialPort serialPort = null;
    private SerialReadRunnable serialReadThread;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class SerialReadRunnable implements Runnable {
        boolean run;

        private SerialReadRunnable() {
            this.run = true;
        }

        /* synthetic */ SerialReadRunnable(ShjVMCSerialProcessorV1 shjVMCSerialProcessorV1, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void stop() {
            this.run = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            Exception e;
            try {
                byte[] bArr = new byte[ShjVMCSerialProcessorV1.MAX_LENGTH];
                byte[] bArr2 = new byte[ShjVMCSerialProcessorV1.MAX_LENGTH];
                int i = 0;
                int i2 = 0;
                while (this.run) {
                    while (this.run && !Thread.interrupted()) {
                        try {
                            try {
                                i = ShjVMCSerialProcessorV1.this.serialPort.getInputStream().available();
                            } catch (InterruptedException e2) {
                                e = e2;
                                Loger.safe_inner_exception_catch(e);
                            }
                        } catch (Exception e3) {
                            Loger.safe_inner_exception_catch(e3);
                            Loger.writeException("SHJ", e3);
                        }
                        if (i == 0) {
                            try {
                                Thread.sleep(ShjVMCSerialProcessorV1.readSleepTime);
                            } catch (Exception e4) {
                                e = e4;
                                Loger.safe_inner_exception_catch(e);
                                Loger.writeException("SHJ", e);
                            }
                        } else {
                            if (i > ShjVMCSerialProcessorV1.MAX_LENGTH - i2) {
                                i = ShjVMCSerialProcessorV1.MAX_LENGTH - i2;
                            }
                            try {
                                int read = ShjVMCSerialProcessorV1.this.serialPort.getInputStream().read(bArr, 0, i);
                                for (int i3 = 0; i3 < read; i3++) {
                                    bArr2[i2 + i3] = bArr[i3];
                                }
                                int i4 = i2 + read;
                                int i5 = i4;
                                int i6 = 0;
                                while (i5 >= ShjVMCSerialProcessorV1.PROTOCOL_LENGTH) {
                                    try {
                                        try {
                                            if ((bArr2[i6] & UByte.MAX_VALUE) != ShjVMCSerialProcessorV1.PROTOCOL_HEAD) {
                                                i6++;
                                                i5--;
                                            } else {
                                                int i7 = ShjVMCSerialProcessorV1.PROTOCOL_LENGTH;
                                                if ((bArr2[i6 + 1] & UByte.MAX_VALUE) == ShjVMCSerialProcessorV1.PROTOCOL_POST_FLAG) {
                                                    i7 = ShjVMCSerialProcessorV1.PROTOCOL_POST_LENGTH;
                                                    if (i5 < ShjVMCSerialProcessorV1.PROTOCOL_POST_LENGTH) {
                                                        break;
                                                    }
                                                }
                                                int i8 = i6 + i7;
                                                if ((bArr2[i8 - 1] & UByte.MAX_VALUE) != ShjVMCSerialProcessorV1.PROTOCOL_END) {
                                                    Loger.writeLog("SHJ", "数据包不正确");
                                                    i5 -= i7;
                                                } else {
                                                    byte[] bArr3 = new byte[i7];
                                                    System.arraycopy(bArr2, i6, bArr3, 0, i7);
                                                    i5 -= i7;
                                                    int intFromBytes = ObjectHelper.intFromBytes(bArr3, 1, 1);
                                                    if (intFromBytes != 0) {
                                                        if ((intFromBytes & 255) == 10) {
                                                            Command poll = CommandManager.getSendCommandQueue().poll();
                                                            if (poll == null) {
                                                                ShjVMCSerialProcessorV1.this.writeSerialPort(ShjVMCSerialProcessorV1.ackBytes, 0, ShjVMCSerialProcessorV1.ackBytes.length);
                                                            } else {
                                                                CommandManager.setCurrentCommand(poll);
                                                                try {
                                                                    poll.doCommand();
                                                                    if (poll.getType() != Command.CommandType.VCMD) {
                                                                        byte[] rawCommand = poll.getRawCommand();
                                                                        Loger.writeLog("SHJ;COMMAND", "下发：" + poll.toString() + StringUtils.SPACE + ObjectHelper.hex2String(rawCommand, rawCommand.length) + " sendCount:" + poll.getSendRepeatCount());
                                                                        ShjVMCSerialProcessorV1.this.writeSerialPort(rawCommand, 0, rawCommand.length);
                                                                    }
                                                                } catch (Exception e5) {
                                                                    Loger.safe_inner_exception_catch(e5);
                                                                    Loger.writeException("SHJ", e5);
                                                                }
                                                            }
                                                        } else {
                                                            Loger.writeLog("COMMAND", "上报:" + ObjectHelper.hex2String(bArr3, i7));
                                                            CommandManager.appendReceivedRawCommand(bArr3);
                                                            Loger.writeLog("COMMAND", "下发：ACP");
                                                            ShjVMCSerialProcessorV1.this.writeSerialPort(ShjVMCSerialProcessorV1.ackBytes, 0, ShjVMCSerialProcessorV1.ackBytes.length);
                                                        }
                                                    }
                                                }
                                                i6 = i8;
                                            }
                                        } catch (InterruptedException e6) {
                                            int i9 = i5;
                                            e = e6;
                                            i2 = i9;
                                            Loger.safe_inner_exception_catch(e);
                                        }
                                    } catch (Exception e7) {
                                        int i10 = i5;
                                        e = e7;
                                        i2 = i10;
                                        Loger.safe_inner_exception_catch(e);
                                        Loger.writeException("SHJ", e);
                                    }
                                }
                                if (i5 > 0 && i6 > 0) {
                                    System.arraycopy(bArr2, i6, bArr2, 0, i4 - i6);
                                }
                                Thread.sleep(ShjVMCSerialProcessorV1.readSleepTime);
                                i2 = i5;
                            } catch (Exception e8) {
                                Loger.writeException("SHJ", e8);
                            }
                        }
                    }
                }
            } catch (Exception e9) {
                Loger.safe_inner_exception_catch(e9);
                Loger.writeException("SHJ", e9);
            }
        }
    }

    public boolean bindSerialPort(String str, int i) {
        File file = new File(str);
        if (!file.exists() || i == -1) {
            Loger.writeLog("SHJ", "串口参数错误{devPath:" + str + ",baudrate:" + i + "}");
            return false;
        }
        try {
            this.serialPort = new SerialPort(file, i, 0);
            return true;
        } catch (Exception e) {
            Loger.writeLog("SHJ", "串口读写初始化错误:" + e.getMessage() + SocketClient.NETASCII_EOL + e.getStackTrace());
            e.printStackTrace();
            return true;
        }
    }

    void writeSerialPort(byte[] bArr, int i, int i2) {
        SerialPort serialPort = this.serialPort;
        if (serialPort == null) {
            return;
        }
        try {
            serialPort.getOutputStream().write(bArr, i, i2);
            this.serialPort.getOutputStream().flush();
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
    }

    public static ShjVMCSerialProcessorV1 getProcessor() {
        if (processor == null) {
            processor = new ShjVMCSerialProcessorV1();
        }
        return processor;
    }

    public void start(Context context) {
        try {
            SerialPort serialPort = this.serialPort;
            if (serialPort == null || serialPort.getInputStream().available() == -1) {
                bindSerialPort(Shj.getComPath(), (int) Shj.getComBaudrate());
            }
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
        }
        SerialReadRunnable serialReadRunnable = this.serialReadThread;
        if (serialReadRunnable == null || !serialReadRunnable.run) {
            this.serialReadThread = new SerialReadRunnable();
            new Thread(this.serialReadThread).start();
        }
    }

    private void stop() {
        try {
            this.serialReadThread.stop();
            this.serialReadThread = null;
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("SHJ", e);
        }
    }
}
