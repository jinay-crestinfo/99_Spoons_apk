package com.oysb.xy.net.socket;

import android.content.Context;
//import com.google.android.exoplayer.hls.HlsChunkSource;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.io.XyStreamParser;
import com.oysb.xy.net.NetManager;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class SocketProcessor implements XyStreamParser.XyHandler {
    private SocketClient client;
    private String host;
    private MessageProcessor messageProcessor;
    private int port;
    private Timer timer = null;
    private long lastSendTime = Long.MAX_VALUE;
    private long timeOut = 40000;
//    private long restartTime = HlsChunkSource.DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS;
    private long restartTime = 20000;
    private long lastDisconnectTime = Long.MAX_VALUE;
    private long lastSendOrAcceptTime = 0;
    private long heartBeatTime = 45000;

    /* loaded from: classes2.dex */
    public interface MessageProcessor {
        XyStreamParser.XyStreamParserListener getXyStreamParserListener();

        void onAcceptMessage(byte[] bArr, boolean z);

        void onConnect();

        void onDisConnect();

        void onError();

        void onNeedHeartBeat();

        void onSendMessage(byte[] bArr);

        boolean onTimeOut();
    }

    @Override // com.oysb.utils.io.XyStreamParser.XyHandler
    public void onConnect() {
        this.messageProcessor.onConnect();
        Loger.writeLog("NET", "proce onConnect");
    }

    public void send(byte[] bArr) {
        long currentTimeMillis = System.currentTimeMillis();
        this.lastSendTime = currentTimeMillis;
        this.lastSendOrAcceptTime = currentTimeMillis;
        try {
            if (this.client.isThreadRunning().booleanValue()) {
                this.client.send(bArr);
                this.messageProcessor.onSendMessage(bArr);
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            try {
                this.client.disconnectAll();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override // com.oysb.utils.io.XyStreamParser.XyHandler
    public void onDisconnect(int i, String str) {
        this.lastDisconnectTime = System.currentTimeMillis();
        this.messageProcessor.onDisConnect();
        Loger.writeLog("NET", "proce onDisconnect:" + str);
    }

    @Override // com.oysb.utils.io.XyStreamParser.XyHandler
    public void onError(String str) {
        try {
            try {
                this.client.disconnectAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.messageProcessor.onError();
        } catch (Exception e2) {
            Loger.safe_inner_exception_catch(e2);
        }
        this.lastDisconnectTime = System.currentTimeMillis();
        Loger.writeLog("NET", "proce onError:" + str);
    }

    @Override // com.oysb.utils.io.XyStreamParser.XyHandler
    public void onMessage(byte[] bArr, boolean z) {
        if (NetManager.debugError()) {
            return;
        }
        try {
            this.messageProcessor.onAcceptMessage(bArr, z);
            this.lastSendTime += this.timeOut * 2;
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
        this.lastSendOrAcceptTime = System.currentTimeMillis();
    }

    public void setHost(String str, int i) {
        this.host = str;
        this.port = i;
    }

    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    public void start(Context context) {
        if (this.timer != null && System.currentTimeMillis() - this.lastSendOrAcceptTime > 120000 && System.currentTimeMillis() - this.lastDisconnectTime > 120000) {
            try {
                this.timer.cancel();
            } catch (Exception unused) {
            }
            this.timer = null;
        }
        if (this.timer == null) {
            Timer timer = new Timer();
            this.timer = timer;
            timer.schedule(new TimerTask() { // from class: com.oysb.xy.net.socket.SocketProcessor.1
                long lastLogTime = 0;


                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    try {
                        long currentTimeMillis = System.currentTimeMillis();
                        long j = this.lastLogTime;
                        boolean z = true;
                        boolean z2 = currentTimeMillis - j > 60000;
                        if (currentTimeMillis - j > 60000) {
                            CacheHelper.getFileCache().put("SHJ_NET_SERVICE_CHECK_TIME", "" + currentTimeMillis);
                            this.lastLogTime = currentTimeMillis;
                            if (currentTimeMillis - currentTimeMillis > 300000) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("soc check start:");
                                sb.append(SocketProcessor.this.client == null ? null : SocketProcessor.this.client.isThreadRunning());
                                sb.append(" cur:");
                                sb.append(currentTimeMillis);
                                sb.append(" timeOut:");
                                sb.append(currentTimeMillis - SocketProcessor.this.lastSendTime > SocketProcessor.this.timeOut);
                                sb.append(" needHeartBeat:");
                                sb.append(currentTimeMillis - SocketProcessor.this.lastSendOrAcceptTime > SocketProcessor.this.heartBeatTime);
                                sb.append(" shuldRestart:");
                                if (currentTimeMillis - SocketProcessor.this.lastDisconnectTime <= SocketProcessor.this.restartTime) {
                                    z = false;
                                }
                                sb.append(z);
                                Loger.writeLog("NET", sb.toString());
                            }
                        }
                        if (SocketProcessor.this.client != null && SocketProcessor.this.client.isThreadRunning().booleanValue()) {
                            if (z2) {
                                Loger.writeLog("NET", "soc running timeOut:" + (currentTimeMillis - SocketProcessor.this.lastSendTime) + " needH:" + (currentTimeMillis - SocketProcessor.this.lastSendOrAcceptTime));
                            }
                            if (currentTimeMillis - SocketProcessor.this.lastSendTime > SocketProcessor.this.timeOut) {
                                Loger.writeLog("NET", "---OnTimeOut start:" + (currentTimeMillis - SocketProcessor.this.lastSendTime));
                                Loger.writeLog("NET", ObjectHelper.hex2String_nospace(SocketProcessor.this.client.getBuffer_log()));
                                if (NetManager.getCurrentReport() != null) {
                                    byte[] rawData = NetManager.getCurrentReport().getRawData();
                                    if (rawData == null) {
                                        Loger.writeLog("NET", "超时的指令： getRawData is null");
                                    } else {
                                        Loger.writeLog("NET", "超时的指令：" + ObjectHelper.hex2String_nospace(rawData));
                                    }
                                }
                                if (SocketProcessor.this.messageProcessor.onTimeOut()) {
                                    SocketProcessor.this.client.disconnectAll();
                                    SocketProcessor.this.lastDisconnectTime = currentTimeMillis;
                                    SocketProcessor.this.lastSendTime = Long.MAX_VALUE;
                                }
                                Loger.writeLog("NET", "---OnTimeOut end");
                            } else if (currentTimeMillis - SocketProcessor.this.lastSendOrAcceptTime > SocketProcessor.this.heartBeatTime) {
                                SocketProcessor.this.lastSendOrAcceptTime = currentTimeMillis;
                                SocketProcessor.this.messageProcessor.onNeedHeartBeat();
                            }
                        } else if (currentTimeMillis - SocketProcessor.this.lastDisconnectTime > SocketProcessor.this.restartTime || currentTimeMillis - SocketProcessor.this.lastSendTime > SocketProcessor.this.timeOut * 2) {
                            if (SocketProcessor.this.client == null) {
                                SocketProcessor socketProcessor = SocketProcessor.this;
                                String str = socketProcessor.host;
                                int i = SocketProcessor.this.port;
                                SocketProcessor socketProcessor2 = SocketProcessor.this;
                                socketProcessor.client = new SocketClient(str, i, socketProcessor2, socketProcessor2.messageProcessor.getXyStreamParserListener());
                                SocketProcessor.this.client.setName("NET");
                            }
                            SocketProcessor.this.lastDisconnectTime = currentTimeMillis;
                            SocketProcessor.this.lastSendTime = Long.MAX_VALUE;
                            SocketProcessor.this.client.connect();
                            Loger.writeLog("NET", "proce new client host:" + SocketProcessor.this.host + " port:" + SocketProcessor.this.port);
                        }
                        if (z2) {
                            Loger.writeLog("NET", "soc check end");
                        }
                    } catch (Exception e) {
                        Loger.writeException("NET", e);
                    }
                }
            }, 1000L, 3000L);
        }
        String str = this.host;
        if (str == null || str.length() == 0) {
            this.host = NetManager.getServerIp();
        }
        if (this.port == 0) {
            this.port = Integer.parseInt(NetManager.getServerPort());
        }
        SocketClient socketClient = this.client;
        if (socketClient == null) {
            Loger.writeLog("NET", "proce new client host:" + this.host + " port:" + this.port);
            SocketClient socketClient2 = new SocketClient(this.host, this.port, this, this.messageProcessor.getXyStreamParserListener(), true);
            this.client = socketClient2;
            socketClient2.setName("NET");
            this.client.connect();
            return;
        }
        socketClient.host = this.host;
        this.client.port = this.port;
    }

    /* renamed from: com.oysb.xy.net.socket.SocketProcessor$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        long lastLogTime = 0;

        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                long j = this.lastLogTime;
                boolean z = true;
                boolean z2 = currentTimeMillis - j > 60000;
                if (currentTimeMillis - j > 60000) {
                    CacheHelper.getFileCache().put("SHJ_NET_SERVICE_CHECK_TIME", "" + currentTimeMillis);
                    this.lastLogTime = currentTimeMillis;
                    if (currentTimeMillis - currentTimeMillis > 300000) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("soc check start:");
                        sb.append(SocketProcessor.this.client == null ? null : SocketProcessor.this.client.isThreadRunning());
                        sb.append(" cur:");
                        sb.append(currentTimeMillis);
                        sb.append(" timeOut:");
                        sb.append(currentTimeMillis - SocketProcessor.this.lastSendTime > SocketProcessor.this.timeOut);
                        sb.append(" needHeartBeat:");
                        sb.append(currentTimeMillis - SocketProcessor.this.lastSendOrAcceptTime > SocketProcessor.this.heartBeatTime);
                        sb.append(" shuldRestart:");
                        if (currentTimeMillis - SocketProcessor.this.lastDisconnectTime <= SocketProcessor.this.restartTime) {
                            z = false;
                        }
                        sb.append(z);
                        Loger.writeLog("NET", sb.toString());
                    }
                }
                if (SocketProcessor.this.client != null && SocketProcessor.this.client.isThreadRunning().booleanValue()) {
                    if (z2) {
                        Loger.writeLog("NET", "soc running timeOut:" + (currentTimeMillis - SocketProcessor.this.lastSendTime) + " needH:" + (currentTimeMillis - SocketProcessor.this.lastSendOrAcceptTime));
                    }
                    if (currentTimeMillis - SocketProcessor.this.lastSendTime > SocketProcessor.this.timeOut) {
                        Loger.writeLog("NET", "---OnTimeOut start:" + (currentTimeMillis - SocketProcessor.this.lastSendTime));
                        Loger.writeLog("NET", ObjectHelper.hex2String_nospace(SocketProcessor.this.client.getBuffer_log()));
                        if (NetManager.getCurrentReport() != null) {
                            byte[] rawData = NetManager.getCurrentReport().getRawData();
                            if (rawData == null) {
                                Loger.writeLog("NET", "超时的指令： getRawData is null");
                            } else {
                                Loger.writeLog("NET", "超时的指令：" + ObjectHelper.hex2String_nospace(rawData));
                            }
                        }
                        if (SocketProcessor.this.messageProcessor.onTimeOut()) {
                            SocketProcessor.this.client.disconnectAll();
                            SocketProcessor.this.lastDisconnectTime = currentTimeMillis;
                            SocketProcessor.this.lastSendTime = Long.MAX_VALUE;
                        }
                        Loger.writeLog("NET", "---OnTimeOut end");
                    } else if (currentTimeMillis - SocketProcessor.this.lastSendOrAcceptTime > SocketProcessor.this.heartBeatTime) {
                        SocketProcessor.this.lastSendOrAcceptTime = currentTimeMillis;
                        SocketProcessor.this.messageProcessor.onNeedHeartBeat();
                    }
                } else if (currentTimeMillis - SocketProcessor.this.lastDisconnectTime > SocketProcessor.this.restartTime || currentTimeMillis - SocketProcessor.this.lastSendTime > SocketProcessor.this.timeOut * 2) {
                    if (SocketProcessor.this.client == null) {
                        SocketProcessor socketProcessor = SocketProcessor.this;
                        String str = socketProcessor.host;
                        int i = SocketProcessor.this.port;
                        SocketProcessor socketProcessor2 = SocketProcessor.this;
                        socketProcessor.client = new SocketClient(str, i, socketProcessor2, socketProcessor2.messageProcessor.getXyStreamParserListener());
                        SocketProcessor.this.client.setName("NET");
                    }
                    SocketProcessor.this.lastDisconnectTime = currentTimeMillis;
                    SocketProcessor.this.lastSendTime = Long.MAX_VALUE;
                    SocketProcessor.this.client.connect();
                    Loger.writeLog("NET", "proce new client host:" + SocketProcessor.this.host + " port:" + SocketProcessor.this.port);
                }
                if (z2) {
                    Loger.writeLog("NET", "soc check end");
                }
            } catch (Exception e) {
                Loger.writeException("NET", e);
            }
        }
    }

    public void stop() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        try {
            this.client.disconnectAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
