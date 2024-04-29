package com.xyshj.machine.popview;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.oysb.utils.CommonTool;
import com.oysb.utils.RunnableEx;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.io.file.SDFileUtils;
import com.oysb.utils.video.VideoHelper;
import com.oysb.utils.view.BasePopView;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.shj.device.cardreader.MdbReader_BDT;
import com.squareup.picasso.Picasso;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.machine.R;
import com.xyshj.machine.app.SysApp;
import com.xyshj.machine.app.VmdHelper;
import com.xyshj.machine.listener.MyGoodsStatusListener;
import com.xyshj.machine.listener.MyMoneyListener;
import com.xyshj.machine.listener.MyShjStatusListener;
import com.xyshj.machine.popview.PopView_Info;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class PopView_Wait_st extends BasePopView implements VideoHelper.VideoListener {
    public static final String ACTION_FULL_ADVIEW_2CLOSE = "ACTION_FULL_ADVIEW_2CLOSE";
    public static final String ACTION_FULL_ADVIEW_CLOSED = "ACTION_FULL_ADVIEW_CLOSED";
    public static final int EVENT_CLOSE_VIEW = 2000;
    public static final int EVENT_SHOW_AD = 3000;
    ImageView adImageView;
    TextView balance;
    TextView price;
    TextView temperature;
    public Timer timer;
    public long timeShow = 0;
    public long timeAdShow = 0;
    int adFileIdx = 0;
    ProgressBar progressBar = null;
    boolean isStartShow = true;
    String lang = "";
    String langEx = "";
    long startTime = Long.MAX_VALUE;

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.st_popview_wait, (ViewGroup) null);
        this.lang = CommonTool.getLanguage(inflate.getContext());
        this.langEx = CommonTool.getLanguageEx(inflate.getContext());
        inflate.findViewById(R.id.satrtbuy).setVisibility(4);
        this.temperature = (TextView) inflate.findViewById(R.id.temperature);
        TextView textView = (TextView) inflate.findViewById(R.id.tvbalance);
        this.balance = textView;
        textView.setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Black.otf"));
        TextView textView2 = (TextView) inflate.findViewById(R.id.price);
        this.price = textView2;
        if (textView2 != null) {
            textView2.setTypeface(Typeface.createFromAsset(inflate.getContext().getAssets(), "Poppins-Black.otf"));
            try {
                int parseInt = Integer.parseInt(CacheHelper.getFileCache().getAsString("Plain Ice Cream Price"));
                TextView textView3 = this.price;
                StringBuilder sb = new StringBuilder();
                sb.append(SysApp.getPriceUnit());
                sb.append(StringUtils.SPACE);
                Object[] objArr = new Object[1];
                double d = parseInt;
                Double.isNaN(d);
                objArr[0] = Double.valueOf(d / 100.0d);
                sb.append(String.format("%.02f", objArr));
                textView3.setText(sb.toString());
            } catch (Exception unused) {
            }
        }
        inflate.findViewById(R.id.bt_charge).setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_Wait_st.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MdbReader_BDT.setEnabled(true);
                ShjManager.charge();
            }
        });
        inflate.findViewById(R.id.phone).setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_Wait_st.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String mainServicePhone = ShjAppHelper.getMainServicePhone();
                PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) "showphone").put("title", (Object) ShjAppHelper.getString(R.string.connect_us)).put("info", (Object) ("" + mainServicePhone)).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("small_left_image", (Object) Integer.valueOf(R.drawable.ico_phone)).put("time_out", (Object) 5000).put("closeOnClick", (Object) true).put("showTime", (Object) false));
            }
        });
        updateBalance();
        try {
            File file = new File(SDFileUtils.SDCardRoot + "xyShj/resource/st_icecream.png");
            if (file.exists()) {
                Picasso.get().load(file).into((ImageView) inflate.findViewById(R.id.icecream));
            }
        } catch (Exception unused2) {
        }
        File file2 = new File(SDFileUtils.SDCardRoot + "xyShj/resource/th_topbar.png");
        if (file2.exists()) {
            inflate.findViewById(R.id.toplogo).setVisibility(4);
            Picasso.get().load(file2).into((ImageView) inflate.findViewById(R.id.toplogimg));
        }
        ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progressBar);
        this.progressBar = progressBar;
        progressBar.setMax(100);
        this.adImageView = (ImageView) inflate.findViewById(R.id.adImageView);
        new Thread(new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Wait_st.3
            final /* synthetic */ View val$view;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass3(Object obj, View inflate2) {
                super(obj);
                inflate = inflate2;
            }

            /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$3$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 extends RunnableEx {
                AnonymousClass1(Object obj) {
                    super(obj);
                }

                @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                public void run() {
                    PopView_Wait_st.this.progressBar.setProgress(PopView_Wait_st.this.progressBar.getProgress() + 1);
                }
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                while (PopView_Wait_st.this.progressBar.getProgress() != PopView_Wait_st.this.progressBar.getMax()) {
                    PopView_Wait_st.this.runOnUiThread(0, new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Wait_st.3.1
                        AnonymousClass1(Object obj) {
                            super(obj);
                        }

                        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                        public void run() {
                            PopView_Wait_st.this.progressBar.setProgress(PopView_Wait_st.this.progressBar.getProgress() + 1);
                        }
                    });
                    try {
                        Thread.sleep(100L);
                    } catch (Exception unused3) {
                    }
                }
                PopView_Wait_st.this.runOnUiThread(1000, new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Wait_st.3.2
                    AnonymousClass2(Object obj) {
                        super(obj);
                    }

                    @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                    public void run() {
                        PopView_Wait_st.this.progressBar.setVisibility(4);
                        inflate.findViewById(R.id.bottombar).setVisibility(0);
                        File file3 = new File(SDFileUtils.SDCardRoot + "xyShj/resource/th_startbuy.png");
                        if (file3.exists()) {
                            inflate.findViewById(R.id.satrtbuy).setVisibility(8);
                            inflate.findViewById(R.id.satrtbuyimg).setVisibility(0);
                            Picasso.get().load(file3).into((ImageView) inflate.findViewById(R.id.satrtbuyimg));
                        } else {
                            inflate.findViewById(R.id.satrtbuyimg).setVisibility(8);
                            inflate.findViewById(R.id.satrtbuy).setVisibility(0);
                        }
                    }
                });
            }

            /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$3$2 */
            /* loaded from: classes2.dex */
            class AnonymousClass2 extends RunnableEx {
                AnonymousClass2(Object obj) {
                    super(obj);
                }

                @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                public void run() {
                    PopView_Wait_st.this.progressBar.setVisibility(4);
                    inflate.findViewById(R.id.bottombar).setVisibility(0);
                    File file3 = new File(SDFileUtils.SDCardRoot + "xyShj/resource/th_startbuy.png");
                    if (file3.exists()) {
                        inflate.findViewById(R.id.satrtbuy).setVisibility(8);
                        inflate.findViewById(R.id.satrtbuyimg).setVisibility(0);
                        Picasso.get().load(file3).into((ImageView) inflate.findViewById(R.id.satrtbuyimg));
                    } else {
                        inflate.findViewById(R.id.satrtbuyimg).setVisibility(8);
                        inflate.findViewById(R.id.satrtbuy).setVisibility(0);
                    }
                }
            }
        }).start();
        inflate2.findViewById(R.id.mask).setOnTouchListener(new View.OnTouchListener() { // from class: com.xyshj.machine.popview.PopView_Wait_st.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (PopView_Wait_st.this.progressBar.getVisibility() != 4 || !VmdHelper.get().checkServerSynOk() || !VmdHelper.get().checkVmdConnected() || !VmdHelper.get().checkBqlZhibeiCount(1) || PopView_Wait_st.this.lang.equalsIgnoreCase("es")) {
                    return false;
                }
                PopView_Wait_st.this.close();
                return false;
            }
        });
        inflate2.findViewById(R.id.bottombar).setOnClickListener(new View.OnClickListener() { // from class: com.xyshj.machine.popview.PopView_Wait_st.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (VmdHelper.get().checkServerSynOk() && VmdHelper.get().checkVmdConnected() && VmdHelper.get().checkBqlZhibeiCount(1)) {
                    PopView_Wait_st.this.close();
                }
            }
        });
        return inflate2;
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            MdbReader_BDT.setEnabled(true);
            ShjManager.charge();
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String mainServicePhone = ShjAppHelper.getMainServicePhone();
            PopView_Info.showInfo(new PopView_Info.BaseMap().put("uid", (Object) "showphone").put("title", (Object) ShjAppHelper.getString(R.string.connect_us)).put("info", (Object) ("" + mainServicePhone)).put("text_color", (Object) ShjAppHelper.getString(R.string.st_color)).put("line", (Object) false).put("small_left_image", (Object) Integer.valueOf(R.drawable.ico_phone)).put("time_out", (Object) 5000).put("closeOnClick", (Object) true).put("showTime", (Object) false));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends RunnableEx {
        final /* synthetic */ View val$view;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Object obj, View inflate2) {
            super(obj);
            inflate = inflate2;
        }

        /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$3$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 extends RunnableEx {
            AnonymousClass1(Object obj) {
                super(obj);
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                PopView_Wait_st.this.progressBar.setProgress(PopView_Wait_st.this.progressBar.getProgress() + 1);
            }
        }

        @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
        public void run() {
            while (PopView_Wait_st.this.progressBar.getProgress() != PopView_Wait_st.this.progressBar.getMax()) {
                PopView_Wait_st.this.runOnUiThread(0, new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Wait_st.3.1
                    AnonymousClass1(Object obj) {
                        super(obj);
                    }

                    @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                    public void run() {
                        PopView_Wait_st.this.progressBar.setProgress(PopView_Wait_st.this.progressBar.getProgress() + 1);
                    }
                });
                try {
                    Thread.sleep(100L);
                } catch (Exception unused3) {
                }
            }
            PopView_Wait_st.this.runOnUiThread(1000, new RunnableEx(null) { // from class: com.xyshj.machine.popview.PopView_Wait_st.3.2
                AnonymousClass2(Object obj) {
                    super(obj);
                }

                @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
                public void run() {
                    PopView_Wait_st.this.progressBar.setVisibility(4);
                    inflate.findViewById(R.id.bottombar).setVisibility(0);
                    File file3 = new File(SDFileUtils.SDCardRoot + "xyShj/resource/th_startbuy.png");
                    if (file3.exists()) {
                        inflate.findViewById(R.id.satrtbuy).setVisibility(8);
                        inflate.findViewById(R.id.satrtbuyimg).setVisibility(0);
                        Picasso.get().load(file3).into((ImageView) inflate.findViewById(R.id.satrtbuyimg));
                    } else {
                        inflate.findViewById(R.id.satrtbuyimg).setVisibility(8);
                        inflate.findViewById(R.id.satrtbuy).setVisibility(0);
                    }
                }
            });
        }

        /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$3$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 extends RunnableEx {
            AnonymousClass2(Object obj) {
                super(obj);
            }

            @Override // com.oysb.utils.RunnableEx, java.lang.Runnable
            public void run() {
                PopView_Wait_st.this.progressBar.setVisibility(4);
                inflate.findViewById(R.id.bottombar).setVisibility(0);
                File file3 = new File(SDFileUtils.SDCardRoot + "xyShj/resource/th_startbuy.png");
                if (file3.exists()) {
                    inflate.findViewById(R.id.satrtbuy).setVisibility(8);
                    inflate.findViewById(R.id.satrtbuyimg).setVisibility(0);
                    Picasso.get().load(file3).into((ImageView) inflate.findViewById(R.id.satrtbuyimg));
                } else {
                    inflate.findViewById(R.id.satrtbuyimg).setVisibility(8);
                    inflate.findViewById(R.id.satrtbuy).setVisibility(0);
                }
            }
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 implements View.OnTouchListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (PopView_Wait_st.this.progressBar.getVisibility() != 4 || !VmdHelper.get().checkServerSynOk() || !VmdHelper.get().checkVmdConnected() || !VmdHelper.get().checkBqlZhibeiCount(1) || PopView_Wait_st.this.lang.equalsIgnoreCase("es")) {
                return false;
            }
            PopView_Wait_st.this.close();
            return false;
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$5 */
    /* loaded from: classes2.dex */
    class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (VmdHelper.get().checkServerSynOk() && VmdHelper.get().checkVmdConnected() && VmdHelper.get().checkBqlZhibeiCount(1)) {
                PopView_Wait_st.this.close();
            }
        }
    }

    void updateBalance() {
        try {
            int intValue = Shj.getWallet().getCatchMoney().intValue();
            TextView textView = this.balance;
            StringBuilder sb = new StringBuilder();
            sb.append(SysApp.getPriceUnit());
            sb.append(StringUtils.SPACE);
            Object[] objArr = new Object[1];
            double d = intValue;
            Double.isNaN(d);
            objArr[0] = Double.valueOf(d / 100.0d);
            sb.append(String.format("%.02f", objArr));
            textView.setText(sb.toString());
        } catch (Exception unused) {
        }
    }

    @Override // com.oysb.utils.view.BasePopView
    protected void registActions(List<String> list) {
        list.add("ACTION_FULL_ADVIEW_2CLOSE");
        list.add(MyShjStatusListener.ACTION_STATUS_TEMPRATURE);
        list.add(MyMoneyListener.ACTION_MONEY_CHANGED);
        list.add(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_BLOCKED);
        list.add(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_SUCCESS);
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onAction(String str, Bundle bundle) {
        if (isShowing()) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -2036084042:
                    if (str.equals("ACTION_FULL_ADVIEW_2CLOSE")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1274066004:
                    if (str.equals(MyMoneyListener.ACTION_MONEY_CHANGED)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1205706597:
                    if (str.equals(MyShjStatusListener.ACTION_STATUS_TEMPRATURE)) {
                        c = 2;
                        break;
                    }
                    break;
                case 2203589:
                    if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_SUCCESS)) {
                        c = 3;
                        break;
                    }
                    break;
                case 1847935406:
                    if (str.equals(MyGoodsStatusListener.ACTION_GOODS_OFFER_GOODS_BLOCKED)) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    this.handler.post(new Runnable() { // from class: com.xyshj.machine.popview.PopView_Wait_st.6
                        AnonymousClass6() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_Wait_st.this.close();
                        }
                    });
                    return;
                case 1:
                    updateBalance();
                    return;
                case 2:
                    this.temperature.setText(Shj.getMachine(0, false).getTemperature() + "°C");
                    return;
                case 3:
                case 4:
                    closeAd();
                    resetTimer();
                    this.handler.sendEmptyMessageDelayed(3000, 30000L);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$6 */
    /* loaded from: classes2.dex */
    class AnonymousClass6 implements Runnable {
        AnonymousClass6() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PopView_Wait_st.this.close();
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onMessage(Message message) {
        int i = message.what;
        if (i == 2000) {
            close();
        } else {
            if (i != 3000) {
                return;
            }
            showAds();
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewWillShow() {
        try {
            this.adImageView.setVisibility(4);
            findViewById(R.id.video).setVisibility(4);
            if (!this.langEx.equalsIgnoreCase("en-US")) {
                findViewById(R.id.bt_charge).setVisibility(0);
            }
            findViewById(R.id.bg).setVisibility(0);
            updateBalance();
            if (!this.isStartShow) {
                findViewById(R.id.bottombar).setVisibility(0);
                if (new File(SDFileUtils.SDCardRoot + "xyShj/resource/th_startbuy.png").exists()) {
                    findViewById(R.id.satrtbuy).setVisibility(8);
                    findViewById(R.id.satrtbuyimg).setVisibility(0);
                } else {
                    findViewById(R.id.satrtbuyimg).setVisibility(8);
                    findViewById(R.id.satrtbuy).setVisibility(0);
                }
            }
        } catch (Exception unused) {
        }
        try {
            File file = new File(SDFileUtils.SDCardRoot + "xyShj/resource/st_icecream.png");
            if (file.exists()) {
                Picasso.get().load(file).into((ImageView) findViewById(R.id.icecream));
            }
        } catch (Exception unused2) {
        }
        super.onViewWillShow();
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidShow() {
        this.startTime = System.currentTimeMillis();
        this.handler.sendEmptyMessageDelayed(3000, 30000L);
        this.isStartShow = false;
        super.onViewDidShow();
    }

    void showAds() {
        try {
            String str = SDFileUtils.SDCardRoot + "xyShj/AvFiles/FullScreen";
            if (VideoHelper.hasSupportFiles(str, null, true)) {
                findViewById(R.id.bg).setVisibility(4);
                findViewById(R.id.video).setVisibility(0);
                if (!this.langEx.equalsIgnoreCase("en-US")) {
                    findViewById(R.id.bt_charge).setVisibility(4);
                }
                this.timeShow = System.currentTimeMillis();
                this.timeAdShow = System.currentTimeMillis();
                this.handler.removeMessages(2000);
                ShjManager.getVideoHelper().stop();
                ShjManager.getVideoHelper().setFullScreen(true);
                ShjManager.getVideoHelper().setVideoFolder(str);
                ShjManager.getVideoHelper().setVideoListener(new VideoHelper.VideoListener() { // from class: com.xyshj.machine.popview.PopView_Wait_st.7
                    @Override // com.oysb.utils.video.VideoHelper.VideoListener
                    public boolean shuldPlayNextLoop() {
                        return true;
                    }

                    AnonymousClass7() {
                    }

                    @Override // com.oysb.utils.video.VideoHelper.VideoListener
                    public void onPlayItemChanged(String str2, int i, int i2) {
                        PopView_Wait_st.this.adFileIdx = i;
                    }
                });
                ShjManager.getVideoHelper().startPlay(0, this.adFileIdx, (FrameLayout) findViewById(R.id.video), R.layout.empty_control_video, this.adImageView);
                ShjManager.getVideoHelper().setVideoListener(this);
                resetTimer();
            }
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements VideoHelper.VideoListener {
        @Override // com.oysb.utils.video.VideoHelper.VideoListener
        public boolean shuldPlayNextLoop() {
            return true;
        }

        AnonymousClass7() {
        }

        @Override // com.oysb.utils.video.VideoHelper.VideoListener
        public void onPlayItemChanged(String str2, int i, int i2) {
            PopView_Wait_st.this.adFileIdx = i;
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidClose() {
        closeAd();
        super.onViewDidClose();
    }

    void closeAd() {
        try {
            this.handler.removeMessages(3000);
            cancelTimer();
            ShjManager.getVideoHelper().stop();
            this.adImageView.setVisibility(4);
            findViewById(R.id.video).setVisibility(4);
            if (!this.langEx.equalsIgnoreCase("en-US")) {
                findViewById(R.id.bt_charge).setVisibility(0);
            }
            findViewById(R.id.bg).setVisibility(0);
            findViewById(R.id.bottombar).setVisibility(0);
            if (new File(SDFileUtils.SDCardRoot + "xyShj/resource/th_startbuy.png").exists()) {
                findViewById(R.id.satrtbuyimg).setVisibility(0);
                findViewById(R.id.satrtbuy).setVisibility(8);
            } else {
                findViewById(R.id.satrtbuyimg).setVisibility(8);
                findViewById(R.id.satrtbuy).setVisibility(0);
            }
            ShjAppBase.sysApp.sendBroadcast(new Intent("ACTION_FULL_ADVIEW_CLOSED"));
        } catch (Exception unused) {
        }
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
        timer2.schedule(new TimerTask() { // from class: com.xyshj.machine.popview.PopView_Wait_st.8
            AnonymousClass8() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    if (System.currentTimeMillis() - PopView_Wait_st.this.timeAdShow > 900000) {
                        if (VideoHelper.getVideoFiles(SDFileUtils.SDCardRoot + "xyShj/AvFiles/FullScreen", null).size() > 0) {
                            PopView_Wait_st.this.handler.sendEmptyMessage(2000);
                        }
                    }
                } catch (Exception unused) {
                }
            }
        }, 1000L, 30000L);
    }

    /* renamed from: com.xyshj.machine.popview.PopView_Wait_st$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 extends TimerTask {
        AnonymousClass8() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                if (System.currentTimeMillis() - PopView_Wait_st.this.timeAdShow > 900000) {
                    if (VideoHelper.getVideoFiles(SDFileUtils.SDCardRoot + "xyShj/AvFiles/FullScreen", null).size() > 0) {
                        PopView_Wait_st.this.handler.sendEmptyMessage(2000);
                    }
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

    @Override // com.oysb.utils.view.BFPopView
    public void onViewWillClose() {
        super.onViewWillClose();
        ShjAppBase.sysApp.sendBroadcast(new Intent("Action_refresh_view"));
    }
}
