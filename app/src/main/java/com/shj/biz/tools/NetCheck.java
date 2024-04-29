package com.shj.biz.tools;

import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.oysb.utils.AndroidSystem;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.NetLoger;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.shj.biz.ShjManager;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class NetCheck {
    private static Timer testTimer;

    public static void testBaiduUrl() {
        Loger.writeLog("CUTNET", "testBaiduUrl");
        RequestItem requestItem = new RequestItem("https://www.baidu.com/", new RequestParams(), HttpGet.METHOD_NAME);
        requestItem.setRepeatDelay(4000);
        requestItem.setRequestMaxCount(1);
        requestItem.setLostAble(true);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.biz.tools.NetCheck.1
            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            AnonymousClass1() {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                Loger.writeLog("CUTNET", "访问百度失败");
                NetLoger.addVisitBaiduRecordLogs("访问百度失败", false);
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                Loger.writeLog("CUTNET", "访问百度成功返回值=：" + str);
                NetLoger.addVisitBaiduRecordLogs("访问百度成功：" + str, true);
                return false;
            }
        });
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.biz.tools.NetCheck$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass1() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            Loger.writeLog("CUTNET", "访问百度失败");
            NetLoger.addVisitBaiduRecordLogs("访问百度失败", false);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            Loger.writeLog("CUTNET", "访问百度成功返回值=：" + str);
            NetLoger.addVisitBaiduRecordLogs("访问百度成功：" + str, true);
            return false;
        }
    }

    public static void timingTestBaidu() {
        if (testTimer == null) {
            Timer timer = new Timer();
            testTimer = timer;
            timer.schedule(new TimerTask() { // from class: com.shj.biz.tools.NetCheck.2
                AnonymousClass2() {
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    CommonTool.isNetworkAvailable(ShjManager.getAppContext());
                    NetLoger.addLogs(NetLoger.LOG_SIGNAL_RECORD, "信号强度：" + AndroidSystem.getMobileDbm(ShjManager.getAppContext()));
                    NetCheck.testBaiduUrl();
                }
            }, 2000L, 300000L);
        }
    }

    /* renamed from: com.shj.biz.tools.NetCheck$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 extends TimerTask {
        AnonymousClass2() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            CommonTool.isNetworkAvailable(ShjManager.getAppContext());
            NetLoger.addLogs(NetLoger.LOG_SIGNAL_RECORD, "信号强度：" + AndroidSystem.getMobileDbm(ShjManager.getAppContext()));
            NetCheck.testBaiduUrl();
        }
    }

    public static void stopTestBaidu() {
        Timer timer = testTimer;
        if (timer != null) {
            timer.cancel();
            testTimer = null;
        }
    }
}
