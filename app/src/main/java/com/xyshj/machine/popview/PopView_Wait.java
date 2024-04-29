package com.xyshj.machine.popview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.io.file.SDFileUtils;
import com.oysb.utils.video.VideoHelper;
import com.oysb.utils.view.BasePopView;
import com.shj.biz.ShjManager;
import com.xyshj.app.ShjAppBase;
import com.xyshj.machine.R;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class PopView_Wait extends BasePopView implements VideoHelper.VideoListener {
    public static final String ACTION_FULL_ADVIEW_2CLOSE = "ACTION_FULL_ADVIEW_2CLOSE";
    public static final String ACTION_FULL_ADVIEW_CLOSED = "ACTION_FULL_ADVIEW_CLOSED";
    public static final int EVENT_CLOSE_VIEW = 2000;
    ImageView imageView;
    public Timer timer;
    public long timeShow = 0;
    public long timeAdShow = 0;
    int adFileIdx = 0;
    ProgressBar progressBar = null;
    boolean isStartShow = true;

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.popview_wait, (ViewGroup) null);
        ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progressBar);
        this.progressBar = progressBar;
        progressBar.setMax(100);
        this.imageView = (ImageView) inflate.findViewById(R.id.imageView);
        new Thread(new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Wait.1
            AnonymousClass1(Object obj) {
                super(obj);
            }

            /* renamed from: com.xyshj.machine.popview.PopView_Wait$1$1 */
            /* loaded from: classes2.dex */
            class C00851 extends RunnableEx {
                C00851(Object obj) {
                    super(obj);
                }

                @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                public void run() {
                    PopView_Wait.this.progressBar.setProgress(PopView_Wait.this.progressBar.getProgress() + 1);
                }
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                while (PopView_Wait.this.progressBar.getProgress() != PopView_Wait.this.progressBar.getMax()) {
                    PopView_Wait.this.runOnUiThread(0, new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Wait.1.1
                        C00851(Object obj) {
                            super(obj);
                        }

                        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                        public void run() {
                            PopView_Wait.this.progressBar.setProgress(PopView_Wait.this.progressBar.getProgress() + 1);
                        }
                    });
                    try {
                        Thread.sleep(100L);
                    } catch (Exception unused) {
                    }
                }
                PopView_Wait.this.runOnUiThread(1000, new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Wait.1.2
                    AnonymousClass2(Object obj) {
                        super(obj);
                    }

                    @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                    public void run() {
                        PopView_Wait.this.progressBar.setVisibility(4);
                        PopView_Wait.this.close();
                    }
                });
            }

            /* renamed from: com.xyshj.machine.popview.PopView_Wait$1$2 */
            /* loaded from: classes2.dex */
            class AnonymousClass2 extends RunnableEx {
                AnonymousClass2(Object obj) {
                    super(obj);
                }

                @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                public void run() {
                    PopView_Wait.this.progressBar.setVisibility(4);
                    PopView_Wait.this.close();
                }
            }
        }).start();
        inflate.findViewById(R.id.mask).setOnTouchListener(new View.OnTouchListener() { // from class: com.xyshj.machine.popview.PopView_Wait.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (PopView_Wait.this.progressBar.getVisibility() != 4) {
                    return false;
                }
                PopView_Wait.this.close();
                return false;
            }
        });
        return inflate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.popview.PopView_Wait$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends RunnableEx {
        AnonymousClass1(Object obj) {
            super(obj);
        }

        /* renamed from: com.xyshj.machine.popview.PopView_Wait$1$1 */
        /* loaded from: classes2.dex */
        class C00851 extends RunnableEx {
            C00851(Object obj) {
                super(obj);
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                PopView_Wait.this.progressBar.setProgress(PopView_Wait.this.progressBar.getProgress() + 1);
            }
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            while (PopView_Wait.this.progressBar.getProgress() != PopView_Wait.this.progressBar.getMax()) {
                PopView_Wait.this.runOnUiThread(0, new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Wait.1.1
                    C00851(Object obj) {
                        super(obj);
                    }

                    @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                    public void run() {
                        PopView_Wait.this.progressBar.setProgress(PopView_Wait.this.progressBar.getProgress() + 1);
                    }
                });
                try {
                    Thread.sleep(100L);
                } catch (Exception unused) {
                }
            }
            PopView_Wait.this.runOnUiThread(1000, new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Wait.1.2
                AnonymousClass2(Object obj) {
                    super(obj);
                }

                @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                public void run() {
                    PopView_Wait.this.progressBar.setVisibility(4);
                    PopView_Wait.this.close();
                }
            });
        }

        /* renamed from: com.xyshj.machine.popview.PopView_Wait$1$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 extends RunnableEx {
            AnonymousClass2(Object obj) {
                super(obj);
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                PopView_Wait.this.progressBar.setVisibility(4);
                PopView_Wait.this.close();
            }
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Wait$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements View.OnTouchListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (PopView_Wait.this.progressBar.getVisibility() != 4) {
                return false;
            }
            PopView_Wait.this.close();
            return false;
        }
    }

    @Override // com.oysb.utils.view.BasePopView
    protected void registActions(List<String> list) {
        list.add("ACTION_FULL_ADVIEW_2CLOSE");
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onAction(String str, Bundle bundle) {
        if (isShowing()) {
            str.hashCode();
            if (str.equals("ACTION_FULL_ADVIEW_2CLOSE")) {
                this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Wait.3
                    AnonymousClass3() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        PopView_Wait.this.close();
                    }
                });
            }
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Wait$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_Wait.this.close();
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onMessage(Message message) {
        if (message.what != 2000) {
            return;
        }
        close();
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidShow() {
        try {
            if (VideoHelper.getVideoFiles(SDFileUtils.SDCardRoot + "xyShj/AvFiles/FullScreen", null).size() > 0) {
                if (!this.isStartShow) {
                    findViewById(R.id.textView1).setVisibility(0);
                    String str = SDFileUtils.SDCardRoot + "xyShj/AvFiles/FullScreen";
                    this.timeShow = System.currentTimeMillis();
                    this.timeAdShow = System.currentTimeMillis();
                    this.handler.removeMessages(2000);
                    ShjManager.getVideoHelper().stop();
                    ShjManager.getVideoHelper().setFullScreen(true);
                    ShjManager.getVideoHelper().setVideoFolder(str);
                    ShjManager.getVideoHelper().setVideoListener(new VideoHelper.VideoListener() { // from class: com.xyshj.machine.popview.PopView_Wait.4
                        @Override // com.oysb.utils.video.VideoHelper.VideoListener
                        public boolean shuldPlayNextLoop() {
                            return true;
                        }

                        AnonymousClass4() {
                        }

                        @Override // com.oysb.utils.video.VideoHelper.VideoListener
                        public void onPlayItemChanged(String str2, int i, int i2) {
                            PopView_Wait.this.adFileIdx = i;
                        }
                    });
                    ShjManager.getVideoHelper().startPlay(0, this.adFileIdx, (FrameLayout) findViewById(R.id.video), R.layout.empty_control_video, this.imageView);
                    ShjManager.getVideoHelper().setVideoListener(this);
                    if (VideoHelper.hasSupportFiles(str, null, true)) {
                        this.imageView.setBackground(null);
                    }
                    resetTimer();
                }
            } else {
                ShjManager.getVideoHelper().stop();
                findViewById(R.id.textView1).setVisibility(4);
            }
        } catch (Exception unused) {
        }
        this.isStartShow = false;
        super.onViewDidShow();
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Wait$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 implements VideoHelper.VideoListener {
        @Override // com.oysb.utils.video.VideoHelper.VideoListener
        public boolean shuldPlayNextLoop() {
            return true;
        }

        AnonymousClass4() {
        }

        @Override // com.oysb.utils.video.VideoHelper.VideoListener
        public void onPlayItemChanged(String str2, int i, int i2) {
            PopView_Wait.this.adFileIdx = i;
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidClose() {
        try {
            cancelTimer();
            ShjManager.getVideoHelper().stop();
            ShjAppBase.sysApp.sendBroadcast(new Intent("ACTION_FULL_ADVIEW_CLOSED"));
        } catch (Exception unused) {
        }
        super.onViewDidClose();
    }

    void cancelTimer() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
    }

    void resetTimer() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        Timer timer2 = new Timer();
        this.timer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.xyshj.machine.popview.PopView_Wait.5
            AnonymousClass5() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    if (System.currentTimeMillis() - PopView_Wait.this.timeAdShow > 900000) {
                        PopView_Wait.this.handler.sendEmptyMessage(2000);
                    }
                } catch (Exception unused) {
                }
            }
        }, 1000L, 30000L);
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Wait$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 extends TimerTask {
        AnonymousClass5() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                if (System.currentTimeMillis() - PopView_Wait.this.timeAdShow > 900000) {
                    PopView_Wait.this.handler.sendEmptyMessage(2000);
                }
            } catch (Exception unused) {
            }
        }
    }

    @Override // com.oysb.utils.video.VideoHelper.VideoListener
    public void onPlayItemChanged(String str, int i, int i2) {
        this.timeAdShow = System.currentTimeMillis();
    }

    @Override // com.oysb.utils.video.VideoHelper.VideoListener
    public boolean shuldPlayNextLoop() {
        long currentTimeMillis = System.currentTimeMillis();
        this.timeAdShow = currentTimeMillis;
        if (currentTimeMillis - this.timeShow <= 600000) {
            return true;
        }
        this.handler.sendEmptyMessage(2000);
        return false;
    }
}
