package com.shj.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.serialport.SerialClient;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.Loger;
import com.oysb.utils.io.XyStreamParser;
import com.shj.Shj;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public class ShjVMCSerialClientManager {
    public static final int HANDLE_ERROR_BYTE_MESSAGE = 1014;
    public static final int HANDLE_SERIAL_INIT = 1013;
    public static final int HANDLE_SERIAL_ON_BYTE_MESSAGE = 1010;
    public static final int HANDLE_SERIAL_ON_CONNECTION = 1011;
    public static final int HANDLE_SERIAL_ON_DISCONNECT = 1012;
    static ShjVMCSerialClientManager serveice;
    SerialClient client;
    AtomicBoolean isConnectedAtomicBoolean;
    XyStreamParser.XyStreamParserListener parseListener;
    SerialEventListener serialEventHandler;
    String host = "/dev/ttyS1";
    int port = 57600;
    boolean FLAG_RUNNING = false;
    boolean debugRunning = false;
    Handler handler = new Handler() { // from class: com.shj.service.ShjVMCSerialClientManager.1
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1000) {
                ShjVMCSerialClientManager.this.startSerialClient();
            }
            super.handleMessage(message);
        }
    };
    MySerialHandler serialHandler = new MySerialHandler();

    /* loaded from: classes2.dex */
    public interface SerialEventListener {
        void onConnected();

        void onDisconnect();

        void onError(String str);

        void onInit();

        void onMessage(byte[] bArr, boolean z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.service.ShjVMCSerialClientManager$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1000) {
                ShjVMCSerialClientManager.this.startSerialClient();
            }
            super.handleMessage(message);
        }
    }

    /* loaded from: classes2.dex */
    public class MySerialHandler implements XyStreamParser.XyHandler {
        MySerialHandler() {
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyHandler
        public void onConnect() {
            ShjVMCSerialClientManager.this.FLAG_RUNNING = true;
            ShjVMCSerialClientManager.this.serialEventHandler.onConnected();
            ShjVMCSerialClientManager.this.isConnectedAtomicBoolean.set(true);
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyHandler
        public void onMessage(byte[] bArr, boolean z) {
            ShjVMCSerialClientManager.this.serialEventHandler.onMessage(bArr, z);
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyHandler
        public void onDisconnect(int i, String str) {
            ShjVMCSerialClientManager.this.FLAG_RUNNING = false;
            ShjVMCSerialClientManager.this.isConnectedAtomicBoolean.set(false);
            if (ShjVMCSerialClientManager.serveice != null && ShjVMCSerialClientManager.this.serialEventHandler != null) {
                ShjVMCSerialClientManager.this.serialEventHandler.onDisconnect();
                Loger.writeLog("SHJ", "Application:" + Shj.getContext());
                ShjVMCSerialClientManager.this.handler.sendEmptyMessageDelayed(1000, 30000L);
            }
            AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_Serial, "09000002", "下位机已断开");
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyHandler
        public void onError(String str) {
            if (ShjVMCSerialClientManager.serveice != null && ShjVMCSerialClientManager.this.serialEventHandler != null) {
                ShjVMCSerialClientManager.this.serialEventHandler.onError(str);
            }
            AppStatusLoger.addAppStatus(null, "SHJ", AppStatusLoger.Type_Serial, "09000003", "下位机通讯故障：" + str);
        }
    }

    public static ShjVMCSerialClientManager getProcessor() throws NullPointerException {
        if (serveice == null) {
            serveice = new ShjVMCSerialClientManager();
        }
        return serveice;
    }

    public void setHost(String str, int i) {
        this.host = str;
        this.port = i;
    }

    public boolean isRunning() {
        if (Shj.isDebug()) {
            return this.debugRunning;
        }
        SerialClient serialClient = this.client;
        return serialClient != null && serialClient.isThreadRunning();
    }

    public void start(Context context) {
        SerialClient serialClient = this.client;
        if (serialClient == null || !serialClient.isThreadRunning()) {
            if (this.isConnectedAtomicBoolean == null) {
                this.isConnectedAtomicBoolean = new AtomicBoolean(false);
            }
            this.debugRunning = true;
            if (Shj.isDebug()) {
                return;
            }
            startSerialClient();
        }
    }

    public void stop() {
        try {
            this.FLAG_RUNNING = false;
            this.client.unBindSerialPort();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startSerialClient() {
        Loger.writeLog("SHJ", "Application:" + Shj.getContext());
        this.FLAG_RUNNING = false;
        this.serialEventHandler.onInit();
        if (this.client == null) {
            Loger.writeLog("SHJ", "下位机接入串口 dev:" + this.host + " baudrate:" + this.port + " 协议类型代码:" + Shj.getVersion());
            SerialClient serialClient = new SerialClient(this.host, this.port, this.serialHandler, this.parseListener, Shj.shouldLogSerialDetail());
            this.client = serialClient;
            serialClient.setName("COMMAND");
        }
        this.client.bindSerialPort();
    }

    public void setEventHandler(SerialEventListener serialEventListener) {
        this.serialEventHandler = serialEventListener;
    }

    public XyStreamParser.XyStreamParserListener getParseListener() {
        return this.parseListener;
    }

    public void setParseListener(XyStreamParser.XyStreamParserListener xyStreamParserListener) {
        this.parseListener = xyStreamParserListener;
    }

    public void send(byte[] bArr) {
        this.client.send(bArr);
    }
}
