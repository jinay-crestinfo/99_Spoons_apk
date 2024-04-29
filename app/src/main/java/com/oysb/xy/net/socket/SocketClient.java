package com.oysb.xy.net.socket;

import com.oysb.utils.Loger;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.io.XyStreamParser;
//import com.tencent.wxpayface.WxfacePayCommonCode;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

/* loaded from: classes2.dex */
public class SocketClient {
    XyStreamParser.XyHandler handler;
    String host;
    Socket mSocket;
    private Thread mThread;
    XyStreamParser parser;
    int port;
    BufferedOutputStream streamWriter = null;
    String name = "";
    private Boolean isThreadRunning = false;
    private volatile Boolean isThreadIniting = false;
    private final Object mSendLock = new Object();

    public Boolean isThreadRunning() {
        Boolean bool;
        synchronized (this.isThreadRunning) {
            bool = this.isThreadRunning;
        }
        return bool;
    }

    public void setName(String str) {
        this.name = str;
        XyStreamParser xyStreamParser = this.parser;
        if (xyStreamParser != null) {
            xyStreamParser.setName(str);
        }
    }

    public SocketClient(String str, int i, XyStreamParser.XyHandler xyHandler, XyStreamParser.XyStreamParserListener xyStreamParserListener) {
        this.host = str;
        this.port = i;
        this.handler = xyHandler;
        XyStreamParser xyStreamParser = new XyStreamParser();
        this.parser = xyStreamParser;
        xyStreamParser.setSleep(50);
        this.parser.setWaitModel(false);
        this.parser.setHandler(this.handler);
        this.parser.setParseListener(xyStreamParserListener);
    }

    public SocketClient(String str, int i, XyStreamParser.XyHandler xyHandler, XyStreamParser.XyStreamParserListener xyStreamParserListener, boolean z) {
        this.host = str;
        this.port = i;
        this.handler = xyHandler;
        XyStreamParser xyStreamParser = new XyStreamParser();
        this.parser = xyStreamParser;
        xyStreamParser.setLogDetail(z);
        this.parser.setSleep(50);
        this.parser.setWaitModel(false);
        this.parser.setHandler(this.handler);
        this.parser.setParseListener(xyStreamParserListener);
    }

    public void connect() {
        this.parser.reset();
        synchronized (this.isThreadRunning) {
            if (!this.isThreadRunning.booleanValue() && !this.isThreadIniting.booleanValue()) {
                Thread thread = new Thread(new RunnableEx(null) { // from class: com.oysb.xy.net.socket.SocketClient.1


                    @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                    public void run() {
                        String str = "connect off";
                        try {
                            try {
                                synchronized (SocketClient.this.isThreadRunning) {
                                    SocketClient.this.isThreadIniting = true;
                                    SocketClient.this.isThreadRunning = false;
                                }
                                SocketClient.this.mSocket = new Socket();
                                SocketClient.this.mSocket.connect(new InetSocketAddress(SocketClient.this.host, SocketClient.this.port), 20000);
                                SocketClient.this.streamWriter = new BufferedOutputStream(SocketClient.this.mSocket.getOutputStream());
                                XyStreamParser xyStreamParser = SocketClient.this.parser;
                                Objects.requireNonNull(xyStreamParser);
                                XyStreamParser.XyDataInputStream xyDataInputStream = new XyStreamParser.XyDataInputStream(SocketClient.this.mSocket.getInputStream());
                                synchronized (SocketClient.this.isThreadRunning) {
                                    SocketClient.this.isThreadIniting = false;
                                    SocketClient.this.isThreadRunning = true;
                                }
                                SocketClient.this.parser.clearBuffer(xyDataInputStream);
                                SocketClient.this.handler.onConnect();
                                SocketClient.this.parser.start(xyDataInputStream);
                                SocketClient.this.isThreadIniting = false;
                                synchronized (SocketClient.this.isThreadRunning) {
                                    SocketClient.this.isThreadRunning = false;
                                }
                            } catch (IOException e) {
                                Loger.writeLog("WxFacePayError", "connect Cause:" + e.getCause());
                                Loger.writeLog("WxFacePayError", "connect message:" + e.getMessage());
                                str = "io error connect failed";
                                SocketClient.this.isThreadIniting = false;
                                synchronized (SocketClient.this.isThreadRunning) {
                                    SocketClient.this.isThreadRunning = false;
                                }
                            } catch (Exception unused) {
                                SocketClient.this.isThreadIniting = false;
                                synchronized (SocketClient.this.isThreadRunning) {
                                    SocketClient.this.isThreadRunning = false;
                                }
                            }
                            SocketClient.this.handler.onDisconnect(0, str);
                        } catch (Throwable th) {
                            SocketClient.this.isThreadIniting = false;
                            synchronized (SocketClient.this.isThreadRunning) {
                                SocketClient.this.isThreadRunning = false;
                                SocketClient.this.handler.onDisconnect(0, "connect off");
                                throw th;
                            }
                        }
                    }
                });
                this.mThread = thread;
                thread.start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.xy.net.socket.SocketClient$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends RunnableEx {
        AnonymousClass1(Object obj) {
            super(obj);
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            String str = "connect off";
            try {
                try {
                    synchronized (SocketClient.this.isThreadRunning) {
                        SocketClient.this.isThreadIniting = true;
                        SocketClient.this.isThreadRunning = false;
                    }
                    SocketClient.this.mSocket = new Socket();
                    SocketClient.this.mSocket.connect(new InetSocketAddress(SocketClient.this.host, SocketClient.this.port), 20000);
                    SocketClient.this.streamWriter = new BufferedOutputStream(SocketClient.this.mSocket.getOutputStream());
                    XyStreamParser xyStreamParser = SocketClient.this.parser;
                    Objects.requireNonNull(xyStreamParser);
                    XyStreamParser.XyDataInputStream xyDataInputStream = new XyStreamParser.XyDataInputStream(SocketClient.this.mSocket.getInputStream());
                    synchronized (SocketClient.this.isThreadRunning) {
                        SocketClient.this.isThreadIniting = false;
                        SocketClient.this.isThreadRunning = true;
                    }
                    SocketClient.this.parser.clearBuffer(xyDataInputStream);
                    SocketClient.this.handler.onConnect();
                    SocketClient.this.parser.start(xyDataInputStream);
                    SocketClient.this.isThreadIniting = false;
                    synchronized (SocketClient.this.isThreadRunning) {
                        SocketClient.this.isThreadRunning = false;
                    }
                } catch (IOException e) {
                    Loger.writeLog("WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR", "connect Cause:" + e.getCause());
                    Loger.writeLog("WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR", "connect message:" + e.getMessage());
                    str = "io error connect failed";
                    SocketClient.this.isThreadIniting = false;
                    synchronized (SocketClient.this.isThreadRunning) {
                        SocketClient.this.isThreadRunning = false;
                    }
                } catch (Exception unused) {
                    SocketClient.this.isThreadIniting = false;
                    synchronized (SocketClient.this.isThreadRunning) {
                        SocketClient.this.isThreadRunning = false;
                    }
                }
                SocketClient.this.handler.onDisconnect(0, str);
            } catch (Throwable th) {
                SocketClient.this.isThreadIniting = false;
                synchronized (SocketClient.this.isThreadRunning) {
                    SocketClient.this.isThreadRunning = false;
                    SocketClient.this.handler.onDisconnect(0, "connect off");
                    throw th;
                }
            }
        }
    }

    private String readLine(XyStreamParser.XyDataInputStream xyDataInputStream) throws IOException {
        int read = xyDataInputStream.read();
        if (read == -1) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        while (read != 10) {
            if (read != 13) {
                sb.append((char) read);
            }
            read = xyDataInputStream.read();
            if (read == -1) {
                return null;
            }
        }
        return sb.toString();
    }

    public void disconnectAll() throws IOException {
        if (this.isThreadIniting.booleanValue()) {
            return;
        }
        if (this.mSocket == null) {
            synchronized (this.isThreadRunning) {
                this.isThreadRunning = false;
            }
            return;
        }
        try {
            try {
                try {
                    this.streamWriter.close();
                } catch (Exception unused) {
                } catch (Throwable th) {
                    this.streamWriter = null;
                    throw th;
                }
                this.streamWriter = null;
                try {
                    this.mThread.interrupt();
                } catch (Exception unused2) {
                }
                try {
                    this.parser.stop();
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                }
                Socket socket = this.mSocket;
                if (socket != null) {
                    try {
                        socket.shutdownInput();
                    } catch (Exception e2) {
                        Loger.safe_inner_exception_catch(e2);
                    }
                    try {
                        this.mSocket.shutdownOutput();
                    } catch (Exception e3) {
                        Loger.safe_inner_exception_catch(e3);
                    }
                    try {
                        this.mSocket.close();
                    } catch (Exception e4) {
                        Loger.safe_inner_exception_catch(e4);
                    }
                    this.mSocket = null;
                }
                synchronized (this.isThreadRunning) {
                    this.isThreadRunning = false;
                }
            } catch (Exception e5) {
                Loger.safe_inner_exception_catch(e5);
                synchronized (this.isThreadRunning) {
                    this.isThreadRunning = false;
                }
            }
            this.handler.onDisconnect(0, "unConnect");
        } catch (Throwable th2) {
            synchronized (this.isThreadRunning) {
                this.isThreadRunning = false;
                this.handler.onDisconnect(0, "unConnect");
                throw th2;
            }
        }
    }

    void sendFrame(byte[] bArr) {
        if (bArr == null) {
            this.handler.onError("send data is null");
            return;
        }
        if (this.mSocket == null || this.streamWriter == null || !this.isThreadRunning.booleanValue()) {
            this.handler.onError("socket is not running");
            return;
        }
        try {
            synchronized (this.mSendLock) {
                this.parser.appendLog(bArr, true);
                this.streamWriter.write(bArr);
                this.streamWriter.flush();
            }
        } catch (Exception unused) {
            this.handler.onError("write to outputStream failed");
        }
    }

    public byte[] getBuffer_log() {
        return this.parser.getBuffer_log();
    }

    public void send(byte[] bArr) {
        if (this.parser.isWaitModel()) {
            this.parser.notifyWaiter();
        }
        sendFrame(bArr);
    }
}
