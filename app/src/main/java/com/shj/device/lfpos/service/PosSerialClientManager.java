package com.shj.device.lfpos.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.serialport.SerialClient;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.io.XyStreamParser;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public class PosSerialClientManager {
    public static final int HANDLE_ERROR_BYTE_MESSAGE = 1014;
    public static final int HANDLE_SERIAL_INIT = 1013;
    public static final int HANDLE_SERIAL_ON_BYTE_MESSAGE = 1010;
    public static final int HANDLE_SERIAL_ON_CONNECTION = 1011;
    public static final int HANDLE_SERIAL_ON_DISCONNECT = 1012;
    static PosSerialClientManager serveice;
    SerialClient client;
    AtomicBoolean isConnectedAtomicBoolean;
    XyStreamParser.XyStreamParserListener parseListener;
    SerialEventListener serialEventHandler;
    String host = "/dev/ttyS2";
    int port = 115200;
    boolean FLAG_RUNNING = false;
    Handler handler = new Handler() { // from class: com.shj.device.lfpos.service.PosSerialClientManager.1
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1000) {
                PosSerialClientManager.this.startSerialClient();
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
    /* renamed from: com.shj.device.lfpos.service.PosSerialClientManager$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1000) {
                PosSerialClientManager.this.startSerialClient();
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
            PosSerialClientManager.this.FLAG_RUNNING = true;
            PosSerialClientManager.this.serialEventHandler.onConnected();
            PosSerialClientManager.this.isConnectedAtomicBoolean.set(true);
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyHandler
        public void onMessage(byte[] bArr, boolean z) {
            PosSerialClientManager.this.serialEventHandler.onMessage(bArr, z);
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyHandler
        public void onDisconnect(int i, String str) {
            PosSerialClientManager.this.FLAG_RUNNING = false;
            PosSerialClientManager.this.isConnectedAtomicBoolean.set(false);
            if (PosSerialClientManager.serveice == null || PosSerialClientManager.this.serialEventHandler == null) {
                return;
            }
            PosSerialClientManager.this.serialEventHandler.onDisconnect();
            PosSerialClientManager.this.handler.sendEmptyMessageDelayed(1000, 30000L);
        }

        @Override // com.oysb.utils.io.XyStreamParser.XyHandler
        public void onError(String str) {
            if (PosSerialClientManager.serveice == null || PosSerialClientManager.this.serialEventHandler == null) {
                return;
            }
            PosSerialClientManager.this.serialEventHandler.onError(str);
        }
    }

    public static PosSerialClientManager getProcessor() throws NullPointerException {
        if (serveice == null) {
            serveice = new PosSerialClientManager();
        }
        return serveice;
    }

    public void setHost(String str, int i) {
        this.host = str;
        this.port = i;
    }

    public boolean isRunning() {
        SerialClient serialClient = this.client;
        return serialClient != null && serialClient.isThreadRunning();
    }

    public void start(Context context) {
        SerialClient serialClient = this.client;
        if (serialClient == null || !serialClient.isThreadRunning()) {
            if (this.isConnectedAtomicBoolean == null) {
                this.isConnectedAtomicBoolean = new AtomicBoolean(false);
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
        this.FLAG_RUNNING = false;
        this.serialEventHandler.onInit();
        if (this.client == null) {
            this.client = new SerialClient(this.host, this.port, this.serialHandler, this.parseListener);
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
        Loger.writeLog("LFPOS", "下发 " + ObjectHelper.hex2String(bArr, bArr.length));
        this.client.send(bArr);
    }
}
