package com.oysb.xy.net.report;

import com.oysb.utils.Loger;
import com.oysb.xy.i.EmptyCmdListener;
import com.oysb.xy.net.NetManager;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class Report_EmptyCmd extends Report {
    private static final long serialVersionUID = 1;
    EmptyCmdListener l;
    private String key = "";
    private long waitTimeOut = 6000;
    private boolean finishNoticed = false;
    Timer checkTimer = null;

    @Override // com.oysb.xy.net.report.Report
    public boolean acceptAck(byte[] bArr) {
        return true;
    }

    @Override // com.oysb.xy.net.report.Report
    public byte[] getRawData() {
        return null;
    }

    public void setKey(String str, EmptyCmdListener emptyCmdListener) {
        this.key = str;
        this.l = emptyCmdListener;
        this.lostAble = true;
        if (emptyCmdListener != null) {
            waitTimeOut();
        }
    }

    /* renamed from: com.oysb.xy.net.report.Report_EmptyCmd$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (NetManager.isLogined()) {
                return;
            }
            Loger.writeLog("SHJ;NET", "发送数据失败，设备未登录，空指令等10后直接结束了");
            Report_EmptyCmd.this.postCmdFinished();
        }
    }

    private void waitTimeOut() {
        Timer timer = new Timer();
        this.checkTimer = timer;
        timer.schedule(new TimerTask() { // from class: com.oysb.xy.net.report.Report_EmptyCmd.1


            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (NetManager.isLogined()) {
                    return;
                }
                Loger.writeLog("SHJ;NET", "发送数据失败，设备未登录，空指令等10后直接结束了");
                Report_EmptyCmd.this.postCmdFinished();
            }
        }, 10000L, 1000L);
    }

    public String getKey() {
        return this.key;
    }

    public void postCmdFinished() {
        Timer timer = this.checkTimer;
        if (timer != null) {
            timer.cancel();
            this.checkTimer = null;
        }
        try {
            if (this.finishNoticed) {
                return;
            }
            this.finishNoticed = true;
            this.l.onEmptyCmdFinished(this.key);
        } catch (Exception unused) {
        }
    }
}
