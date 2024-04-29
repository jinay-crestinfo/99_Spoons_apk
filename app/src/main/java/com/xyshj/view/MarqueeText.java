package com.xyshj.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewDebug;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.app.R;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class MarqueeText extends FrameLayout {
    private static final int EVN_SHOW_NEXT_FLIP_TEXT = 2000;
    private static final int EVN_SHOW_NORMAL_TEXT = 2001;
    private ArrayList<String> aryTexts;
    private int flipTime;
    Handler handler;
    private int idxText;
    private Integer idxTextView;
    private Animation inAnimation;
    private Animation outAnimation;
    private TextView textView1;
    private TextView textView2;
    private Timer timer;

    @Override // android.view.View
    @ViewDebug.ExportedProperty(category = "focus")
    public boolean isFocused() {
        return true;
    }

    static /* synthetic */ int access$308(MarqueeText marqueeText) {
        int i = marqueeText.idxText;
        marqueeText.idxText = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.view.MarqueeText$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                int i = message.what;
                if (i == 2000) {
                    synchronized (MarqueeText.this.idxTextView) {
                        TextView textView = MarqueeText.this.idxTextView.intValue() == 0 ? MarqueeText.this.textView1 : MarqueeText.this.textView2;
                        TextView textView2 = MarqueeText.this.idxTextView.intValue() == 1 ? MarqueeText.this.textView1 : MarqueeText.this.textView2;
                        textView.clearAnimation();
                        textView2.clearAnimation();
                        MarqueeText.this.removeView(textView);
                        MarqueeText.access$308(MarqueeText.this);
                        if (MarqueeText.this.idxText >= MarqueeText.this.aryTexts.size()) {
                            MarqueeText.this.idxText = 0;
                        }
                        textView2.setText((CharSequence) MarqueeText.this.aryTexts.get(MarqueeText.this.idxText));
                        MarqueeText marqueeText = MarqueeText.this;
                        marqueeText.addView(marqueeText.idxTextView.intValue() == 0 ? MarqueeText.this.textView2 : MarqueeText.this.textView1);
                        textView2.startAnimation(MarqueeText.this.inAnimation);
                        MarqueeText.this.handler.postDelayed(new Runnable() { // from class: com.xyshj.view.MarqueeText.1.1
                            final /* synthetic */ TextView val$toAdd;

                            RunnableC00871(TextView textView22) {
                                textView2 = textView22;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                textView2.clearAnimation();
                            }
                        }, 1000L);
                        Integer unused = MarqueeText.this.idxTextView;
                        MarqueeText marqueeText2 = MarqueeText.this;
                        marqueeText2.idxTextView = Integer.valueOf(marqueeText2.idxTextView.intValue() + 1);
                        MarqueeText marqueeText3 = MarqueeText.this;
                        marqueeText3.idxTextView = Integer.valueOf(marqueeText3.idxTextView.intValue() % 2);
                    }
                } else if (i == 2001) {
                    MarqueeText.this.removeAllViews();
                    MarqueeText marqueeText4 = MarqueeText.this;
                    marqueeText4.addView(marqueeText4.textView1);
                    MarqueeText.this.textView1.setText(message.obj.toString());
                }
            } catch (Exception unused2) {
            }
            super.handleMessage(message);
        }

        /* renamed from: com.xyshj.view.MarqueeText$1$1 */
        /* loaded from: classes2.dex */
        class RunnableC00871 implements Runnable {
            final /* synthetic */ TextView val$toAdd;

            RunnableC00871(TextView textView22) {
                textView2 = textView22;
            }

            @Override // java.lang.Runnable
            public void run() {
                textView2.clearAnimation();
            }
        }
    }

    public MarqueeText(Context context) {
        super(context);
        this.aryTexts = new ArrayList<>();
        this.idxTextView = 0;
        this.idxText = 0;
        this.timer = null;
        this.flipTime = 8000;
        this.handler = new Handler() { // from class: com.xyshj.view.MarqueeText.1
            AnonymousClass1() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                try {
                    int i = message.what;
                    if (i == 2000) {
                        synchronized (MarqueeText.this.idxTextView) {
                            TextView textView = MarqueeText.this.idxTextView.intValue() == 0 ? MarqueeText.this.textView1 : MarqueeText.this.textView2;
                            TextView textView22 = MarqueeText.this.idxTextView.intValue() == 1 ? MarqueeText.this.textView1 : MarqueeText.this.textView2;
                            textView.clearAnimation();
                            textView22.clearAnimation();
                            MarqueeText.this.removeView(textView);
                            MarqueeText.access$308(MarqueeText.this);
                            if (MarqueeText.this.idxText >= MarqueeText.this.aryTexts.size()) {
                                MarqueeText.this.idxText = 0;
                            }
                            textView22.setText((CharSequence) MarqueeText.this.aryTexts.get(MarqueeText.this.idxText));
                            MarqueeText marqueeText = MarqueeText.this;
                            marqueeText.addView(marqueeText.idxTextView.intValue() == 0 ? MarqueeText.this.textView2 : MarqueeText.this.textView1);
                            textView22.startAnimation(MarqueeText.this.inAnimation);
                            MarqueeText.this.handler.postDelayed(new Runnable() { // from class: com.xyshj.view.MarqueeText.1.1
                                final /* synthetic */ TextView val$toAdd;

                                RunnableC00871(TextView textView222) {
                                    textView2 = textView222;
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    textView2.clearAnimation();
                                }
                            }, 1000L);
                            Integer unused = MarqueeText.this.idxTextView;
                            MarqueeText marqueeText2 = MarqueeText.this;
                            marqueeText2.idxTextView = Integer.valueOf(marqueeText2.idxTextView.intValue() + 1);
                            MarqueeText marqueeText3 = MarqueeText.this;
                            marqueeText3.idxTextView = Integer.valueOf(marqueeText3.idxTextView.intValue() % 2);
                        }
                    } else if (i == 2001) {
                        MarqueeText.this.removeAllViews();
                        MarqueeText marqueeText4 = MarqueeText.this;
                        marqueeText4.addView(marqueeText4.textView1);
                        MarqueeText.this.textView1.setText(message.obj.toString());
                    }
                } catch (Exception unused2) {
                }
                super.handleMessage(message);
            }

            /* renamed from: com.xyshj.view.MarqueeText$1$1 */
            /* loaded from: classes2.dex */
            class RunnableC00871 implements Runnable {
                final /* synthetic */ TextView val$toAdd;

                RunnableC00871(TextView textView222) {
                    textView2 = textView222;
                }

                @Override // java.lang.Runnable
                public void run() {
                    textView2.clearAnimation();
                }
            }
        };
        initTextView();
    }

    public MarqueeText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.aryTexts = new ArrayList<>();
        this.idxTextView = 0;
        this.idxText = 0;
        this.timer = null;
        this.flipTime = 8000;
        this.handler = new Handler() { // from class: com.xyshj.view.MarqueeText.1
            AnonymousClass1() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                try {
                    int i = message.what;
                    if (i == 2000) {
                        synchronized (MarqueeText.this.idxTextView) {
                            TextView textView = MarqueeText.this.idxTextView.intValue() == 0 ? MarqueeText.this.textView1 : MarqueeText.this.textView2;
                            TextView textView222 = MarqueeText.this.idxTextView.intValue() == 1 ? MarqueeText.this.textView1 : MarqueeText.this.textView2;
                            textView.clearAnimation();
                            textView222.clearAnimation();
                            MarqueeText.this.removeView(textView);
                            MarqueeText.access$308(MarqueeText.this);
                            if (MarqueeText.this.idxText >= MarqueeText.this.aryTexts.size()) {
                                MarqueeText.this.idxText = 0;
                            }
                            textView222.setText((CharSequence) MarqueeText.this.aryTexts.get(MarqueeText.this.idxText));
                            MarqueeText marqueeText = MarqueeText.this;
                            marqueeText.addView(marqueeText.idxTextView.intValue() == 0 ? MarqueeText.this.textView2 : MarqueeText.this.textView1);
                            textView222.startAnimation(MarqueeText.this.inAnimation);
                            MarqueeText.this.handler.postDelayed(new Runnable() { // from class: com.xyshj.view.MarqueeText.1.1
                                final /* synthetic */ TextView val$toAdd;

                                RunnableC00871(TextView textView2222) {
                                    textView2 = textView2222;
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    textView2.clearAnimation();
                                }
                            }, 1000L);
                            Integer unused = MarqueeText.this.idxTextView;
                            MarqueeText marqueeText2 = MarqueeText.this;
                            marqueeText2.idxTextView = Integer.valueOf(marqueeText2.idxTextView.intValue() + 1);
                            MarqueeText marqueeText3 = MarqueeText.this;
                            marqueeText3.idxTextView = Integer.valueOf(marqueeText3.idxTextView.intValue() % 2);
                        }
                    } else if (i == 2001) {
                        MarqueeText.this.removeAllViews();
                        MarqueeText marqueeText4 = MarqueeText.this;
                        marqueeText4.addView(marqueeText4.textView1);
                        MarqueeText.this.textView1.setText(message.obj.toString());
                    }
                } catch (Exception unused2) {
                }
                super.handleMessage(message);
            }

            /* renamed from: com.xyshj.view.MarqueeText$1$1 */
            /* loaded from: classes2.dex */
            class RunnableC00871 implements Runnable {
                final /* synthetic */ TextView val$toAdd;

                RunnableC00871(TextView textView2222) {
                    textView2 = textView2222;
                }

                @Override // java.lang.Runnable
                public void run() {
                    textView2.clearAnimation();
                }
            }
        };
        initTextView();
    }

    public MarqueeText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.aryTexts = new ArrayList<>();
        this.idxTextView = 0;
        this.idxText = 0;
        this.timer = null;
        this.flipTime = 8000;
        this.handler = new Handler() { // from class: com.xyshj.view.MarqueeText.1
            AnonymousClass1() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                try {
                    int i2 = message.what;
                    if (i2 == 2000) {
                        synchronized (MarqueeText.this.idxTextView) {
                            TextView textView = MarqueeText.this.idxTextView.intValue() == 0 ? MarqueeText.this.textView1 : MarqueeText.this.textView2;
                            TextView textView2222 = MarqueeText.this.idxTextView.intValue() == 1 ? MarqueeText.this.textView1 : MarqueeText.this.textView2;
                            textView.clearAnimation();
                            textView2222.clearAnimation();
                            MarqueeText.this.removeView(textView);
                            MarqueeText.access$308(MarqueeText.this);
                            if (MarqueeText.this.idxText >= MarqueeText.this.aryTexts.size()) {
                                MarqueeText.this.idxText = 0;
                            }
                            textView2222.setText((CharSequence) MarqueeText.this.aryTexts.get(MarqueeText.this.idxText));
                            MarqueeText marqueeText = MarqueeText.this;
                            marqueeText.addView(marqueeText.idxTextView.intValue() == 0 ? MarqueeText.this.textView2 : MarqueeText.this.textView1);
                            textView2222.startAnimation(MarqueeText.this.inAnimation);
                            MarqueeText.this.handler.postDelayed(new Runnable() { // from class: com.xyshj.view.MarqueeText.1.1
                                final /* synthetic */ TextView val$toAdd;

                                RunnableC00871(TextView textView22222) {
                                    textView2 = textView22222;
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    textView2.clearAnimation();
                                }
                            }, 1000L);
                            Integer unused = MarqueeText.this.idxTextView;
                            MarqueeText marqueeText2 = MarqueeText.this;
                            marqueeText2.idxTextView = Integer.valueOf(marqueeText2.idxTextView.intValue() + 1);
                            MarqueeText marqueeText3 = MarqueeText.this;
                            marqueeText3.idxTextView = Integer.valueOf(marqueeText3.idxTextView.intValue() % 2);
                        }
                    } else if (i2 == 2001) {
                        MarqueeText.this.removeAllViews();
                        MarqueeText marqueeText4 = MarqueeText.this;
                        marqueeText4.addView(marqueeText4.textView1);
                        MarqueeText.this.textView1.setText(message.obj.toString());
                    }
                } catch (Exception unused2) {
                }
                super.handleMessage(message);
            }

            /* renamed from: com.xyshj.view.MarqueeText$1$1 */
            /* loaded from: classes2.dex */
            class RunnableC00871 implements Runnable {
                final /* synthetic */ TextView val$toAdd;

                RunnableC00871(TextView textView22222) {
                    textView2 = textView22222;
                }

                @Override // java.lang.Runnable
                public void run() {
                    textView2.clearAnimation();
                }
            }
        };
        initTextView();
    }

    void initTextView() {
        this.textView1 = new TextView(getContext());
        this.textView2 = new TextView(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        this.textView1.setLayoutParams(layoutParams);
        this.textView1.setLayoutParams(layoutParams);
        this.inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.push_bottom_in);
        this.outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.push_up_out);
        this.idxTextView = 0;
        this.idxText = 0;
        addView(this.textView1);
        if (this.aryTexts.size() > 0) {
            this.textView1.setText(this.aryTexts.get(0));
        }
    }

    public void setStyle(int i, int i2, int i3) {
        float f = i;
        this.textView1.setTextSize(0, f);
        this.textView1.setTextColor(i2);
        this.textView1.setGravity(i3);
        this.textView2.setTextSize(0, f);
        this.textView2.setTextColor(i2);
        this.textView2.setGravity(i3);
    }

    public void addText(String str) {
        String trim = str.trim();
        if (trim.endsWith(VoiceWakeuperAidl.PARAMS_SEPARATE)) {
            trim = trim.substring(0, trim.length() - 1);
        }
        if (trim == null || trim.trim().length() == 0 || this.aryTexts.contains(trim)) {
            return;
        }
        this.aryTexts.add(trim);
    }

    public void startFlipText() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        this.idxTextView = 0;
        this.idxText = 0;
        removeAllViews();
        addView(this.textView1);
        if (this.aryTexts.size() > 0) {
            this.textView1.setText(this.aryTexts.get(0));
        }
        Timer timer2 = new Timer();
        this.timer = timer2;
        AnonymousClass2 anonymousClass2 = new TimerTask() { // from class: com.xyshj.view.MarqueeText.2
            AnonymousClass2() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (MarqueeText.this.aryTexts.size() <= 1) {
                    return;
                }
                MarqueeText.this.handler.sendEmptyMessage(2000);
            }
        };
        int i = this.flipTime;
        timer2.schedule(anonymousClass2, i, i);
    }

    /* renamed from: com.xyshj.view.MarqueeText$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 extends TimerTask {
        AnonymousClass2() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (MarqueeText.this.aryTexts.size() <= 1) {
                return;
            }
            MarqueeText.this.handler.sendEmptyMessage(2000);
        }
    }

    public void stopFlipText() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        this.idxText = 0;
        this.idxTextView = 0;
        removeAllViews();
    }

    public void showNormalText(String str) {
        stopFlipText();
        Message obtain = Message.obtain();
        obtain.what = 2001;
        obtain.obj = str;
        this.handler.sendMessageDelayed(obtain, 1000L);
    }
}
