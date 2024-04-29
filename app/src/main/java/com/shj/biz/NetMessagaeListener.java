package com.shj.biz;

import com.oysb.utils.AndroidSystem;
import com.oysb.utils.AppStatusLoger;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.xy.net.NetManager;
import com.oysb.xy.net.report.Report;
import com.oysb.xy.net.report.Report_Transf_OfferCmd2Server;
import com.shj.Shj;
import com.tencent.wxpayface.WxfacePayCommonCode;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class NetMessagaeListener implements NetManager.OnMessageListener {
    private static boolean disConnectNoticed = false;
    private static long lastDisconnectTime = Long.MAX_VALUE;
    private static boolean netConnected = true;
    private static long restAndroidSystemCheckTime = Long.MAX_VALUE;
    private static Timer restartTimer;
    private static Timer stopMacTimer;

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onAppendReport(Report report) {
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onError(byte[] bArr) {
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onLogin() {
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onSendFailed(byte[] bArr, Report report, String str) {
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onTcpInit() {
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onTcpConnected() {
        if (!netConnected) {
            ShjManager.getStatusListener().onMessage("LOCAL", "*notice*close*");
        }
        netConnected = true;
        disConnectNoticed = false;
        restAndroidSystemCheckTime = Long.MAX_VALUE;
        lastDisconnectTime = Long.MAX_VALUE;
        Timer timer = stopMacTimer;
        if (timer != null) {
            timer.cancel();
            stopMacTimer = null;
        }
        Timer timer2 = restartTimer;
        if (timer2 != null) {
            timer2.cancel();
            restartTimer = null;
        }
        if (Shj.isStoped()) {
            Shj.setStoped(false);
        }
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onTcpDisConnected(String str) {
        synchronized (this) {
            if (netConnected) {
                Loger.writeLog("UI", "onTcpDisConnected");
                int i = -1;
                String str2 = "未知";
                try {
                    i = AndroidSystem.getMobileDbm(ShjManager.getActivityContext());
                    str2 = AndroidSystem.GetNetworkType(ShjManager.getActivityContext());
                } catch (Exception unused) {
                }
                AppStatusLoger.addAppStatus(null, "NET", AppStatusLoger.Type_SocketDisconnect, "", "onTcpDisConnected(reason:" + str + ") 联网类型：" + str2 + " dbm:" + i + " netvailable:" + CommonTool.isNetworkAvailable(ShjManager.getActivityContext()));
                netConnected = false;
                disConnectNoticed = false;
                lastDisconnectTime = System.currentTimeMillis();
                try {
                    if (stopMacTimer == null) {
                        Loger.writeLog("UI", "start stopMacTimer");
                        Timer timer = new Timer();
                        stopMacTimer = timer;
                        timer.schedule(new TimerTask() { // from class: com.shj.biz.NetMessagaeListener.1
                            AnonymousClass1() {
                            }

                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                try {
                                    if (NetMessagaeListener.lastDisconnectTime > System.currentTimeMillis() - 3000) {
                                        return;
                                    }
                                    ShjManager.requestJqConnectPwd(false);
                                    Timer unused2 = NetMessagaeListener.stopMacTimer = null;
                                } catch (Exception e) {
                                    Loger.writeException("NET", e);
                                }
                            }
                        }, 60000L);
                    }
                } catch (Exception e) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                }
                try {
                    if (restartTimer == null) {
                        Loger.writeLog("UI", "start restartTimer");
                        Timer timer2 = new Timer();
                        restartTimer = timer2;
                        timer2.schedule(new TimerTask() { // from class: com.shj.biz.NetMessagaeListener.2
                            AnonymousClass2() {
                            }

                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                if (ShjManager.needStopShjWhenOfferLineTooLong() && !NetMessagaeListener.disConnectNoticed) {
                                    AppStatusLoger.addAppStatus(null, "NET", AppStatusLoger.Type_SocketDisconnect, "", "needStopShjWhenOfferLineTooLong");
                                    Loger.writeLog("UI", "to long to reconnect server, set need re power shj");
                                    ShjManager.getStatusListener().onMessage("LOCAL", "*notice*offelineTimeout*");
                                    Shj.setStoped(true);
                                    long unused2 = NetMessagaeListener.restAndroidSystemCheckTime = System.currentTimeMillis() + 720000;
                                    boolean unused3 = NetMessagaeListener.disConnectNoticed = true;
                                    return;
                                }
                                if (!ShjManager.needRestartAppWhenOfferLineTooLong() || NetMessagaeListener.disConnectNoticed) {
                                    return;
                                }
                                AppStatusLoger.addAppStatus(null, "NET", AppStatusLoger.Type_SocketDisconnect, "", "needRestartAppWhenOfferLineTooLong");
                                Loger.writeLog("UI", "to long to reconnect server, set need Restart");
                                ShjManager.getStatusListener().onMessage("LOCAL", "*notice*offelineTimeout*");
                                boolean unused4 = NetMessagaeListener.disConnectNoticed = true;
                            }
                        }, 60000L);
                    }
                } catch (Exception e2) {
                    Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
                }
                ShjManager.getBizShjListener()._onUpdateNetStatus(false, null);
                StringBuilder sb = new StringBuilder();
                sb.append("System.currentTimeMillis():");
                sb.append(System.currentTimeMillis());
                sb.append(" restAndroidSystemCheckTime:");
                sb.append(restAndroidSystemCheckTime);
                sb.append(" ? ");
                sb.append(System.currentTimeMillis() > restAndroidSystemCheckTime);
                Loger.writeLog("UI", sb.toString());
                if (System.currentTimeMillis() > restAndroidSystemCheckTime) {
                    AppStatusLoger.addAppStatus(null, "NET", AppStatusLoger.Type_SocketDisconnect, "", "联网超时，未安装重启控制线，改重启安卓系统");
                    AndroidSystem.rebootSystem("联网超时，未安装重启控制线，改重启安卓系统", 0);
                }
            }
        }
    }

    /* renamed from: com.shj.biz.NetMessagaeListener$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                if (NetMessagaeListener.lastDisconnectTime > System.currentTimeMillis() - 3000) {
                    return;
                }
                ShjManager.requestJqConnectPwd(false);
                Timer unused2 = NetMessagaeListener.stopMacTimer = null;
            } catch (Exception e) {
                Loger.writeException("NET", e);
            }
        }
    }

    /* renamed from: com.shj.biz.NetMessagaeListener$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 extends TimerTask {
        AnonymousClass2() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (ShjManager.needStopShjWhenOfferLineTooLong() && !NetMessagaeListener.disConnectNoticed) {
                AppStatusLoger.addAppStatus(null, "NET", AppStatusLoger.Type_SocketDisconnect, "", "needStopShjWhenOfferLineTooLong");
                Loger.writeLog("UI", "to long to reconnect server, set need re power shj");
                ShjManager.getStatusListener().onMessage("LOCAL", "*notice*offelineTimeout*");
                Shj.setStoped(true);
                long unused2 = NetMessagaeListener.restAndroidSystemCheckTime = System.currentTimeMillis() + 720000;
                boolean unused3 = NetMessagaeListener.disConnectNoticed = true;
                return;
            }
            if (!ShjManager.needRestartAppWhenOfferLineTooLong() || NetMessagaeListener.disConnectNoticed) {
                return;
            }
            AppStatusLoger.addAppStatus(null, "NET", AppStatusLoger.Type_SocketDisconnect, "", "needRestartAppWhenOfferLineTooLong");
            Loger.writeLog("UI", "to long to reconnect server, set need Restart");
            ShjManager.getStatusListener().onMessage("LOCAL", "*notice*offelineTimeout*");
            boolean unused4 = NetMessagaeListener.disConnectNoticed = true;
        }
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onAck(byte[] bArr, Report report, boolean z, int i) {
        Loger.writeLog("NET", "ack:" + report.getResult());
        if (i == 0) {
            ShjManager.requestNextBinUpdatePackge();
        }
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onMessage(byte[] bArr) {
        try {
            int length = bArr.length - 5;
            byte[] bArr2 = new byte[length];
            ObjectHelper.updateBytes(bArr2, bArr, 4, 0, length);
            String str = new String(bArr2, "UTF-8");
            if ((bArr[0] & UByte.MAX_VALUE) == 48 && bArr[3] == 49) {
                Loger.writeLog("NET", "server message:" + str);
                ShjManager.getBizShjListener().onServerOfferGoodsCmd(0, str);
            } else if ((bArr[0] & UByte.MAX_VALUE) == 33 && bArr[3] == 112) {
                Loger.writeLog("NET", "server message:" + str);
                ShjManager.onServerCmd(str);
            } else if ((bArr[0] & UByte.MAX_VALUE) == 35 && (bArr[4] & UByte.MAX_VALUE) == 113) {
                ShjManager.onAcceptBinUpdatePacke(bArr);
            } else {
                ShjManager.getBizShjListener()._onServerMessage(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onSendReport(Report report) {
        if ((report.getDataType() & UByte.MAX_VALUE) == 23) {
            ShjManager.onRequestNextBinUpdatePackge_needPause();
        }
        Loger.writeLog("NET", "reportData:" + report.toString());
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onAckTimeOut(Report report) {
        if ((report.getDataType() & UByte.MAX_VALUE) == 34 && (report.getBizType() & UByte.MAX_VALUE) == 113) {
            ShjManager.onRequestNextBinUpdatePackge_needPause();
        }
        if (report instanceof Report_Transf_OfferCmd2Server) {
            ShjManager.getBizShjListener().onServerOfferGoodsCmdStateTimeOut(((Report_Transf_OfferCmd2Server) report).getStepState());
        }
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onLoginFailed(String str) {
        ShjManager.getBizShjListener()._onUpdateNetStatus(false, null);
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public void onLoginOk(Date date) {
        try {
            ShjManager.getBizShjListener()._onUpdateNetStatus(true, date);
        } catch (Exception unused) {
        }
        AndroidSystem.setSystemTime(ShjManager.getAppContext(), date);
    }

    @Override // com.oysb.xy.net.NetManager.OnMessageListener
    public boolean shuldSendHeartBeatOnTcpFree() {
        return !ShjManager.requestNextBinUpdatePackge();
    }
}
