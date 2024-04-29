package com.oysb.utils.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
//import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.RunnableEx;
import java.io.Serializable;
import java.util.HashMap;

/* loaded from: classes2.dex */
public abstract class BFPopView {
    public static final String ACTION_CLOSE_POPVIEW = "ACTION_BF_CLOSE_POPVIEW";
    public static final String ACTION_SHOW_POPVIEW = "ACTION_BF_SHOW_POPVIEW";
    public static final String POP_VIEW = "popViewKey";
    private Object args;
    private View contentView;
    private View contentView2;
    private RelativeLayout parent;
    String parentKey;
    String popviewKey;
    static HashMap<String, PopViewItem> mapPopViews = new HashMap<>();
    static HashMap<String, RelativeLayout> mapPopViewParents = new HashMap<>();
    public static int zindex = 0;
    static int showingPopViewCount = 0;
    protected boolean updateOnReShow = false;
    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            try {
                if (message.what == 9999) {
                    Intent intent = (Intent) message.obj;
//                    BFPopView.this.onAction(intent.getAction(), intent.getBundleExtra(SpeechEvent.KEY_EVENT_RECORD_DATA));
                } else {
                    BFPopView.this.onMessage(message);
                }
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
            }
        }
    };

    int color0 = Color.parseColor("#177194");
    int color1 = Color.parseColor("#00000000");
    int showAnimId = -1;
    int closeAnimId = -1;
    boolean closeOnClick = true;
    boolean canClickUnderView = false;
    boolean removeOnClose = true;

    protected abstract void onAction(String str, Bundle bundle);

    protected abstract void onMessage(Message message);

    public void onViewDidClose() {
    }

    public void onViewDidShow() {
    }

    public void onViewWillClose() {
    }

    public void onViewWillShow() {
    }

    public void sendMessage(HashMap<String, String> hashMap) {
        Intent intent = new Intent(ACTION_CLOSE_POPVIEW);
        Bundle bundle = new Bundle();
        for (String str : hashMap.keySet()) {
            bundle.putSerializable(str, hashMap.get(str));
        }
        bundle.putSerializable(POP_VIEW, this.popviewKey);
//        intent.putExtra(SpeechEvent.KEY_EVENT_RECORD_DATA, bundle);
        getParent().getContext().sendBroadcast(intent);
    }

    public String getParentKey() {
        return this.parentKey;
    }

    public BFPopView getParentBFPopView() {
        return getPopView(this.parentKey);
    }

    public String getSimpleClassName() {
        return getClass().getSimpleName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.view.BFPopView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                if (message.what == 9999) {
                    Intent intent = (Intent) message.obj;
                    BFPopView.this.onAction(intent.getAction(), intent.getBundleExtra(SpeechEvent.KEY_EVENT_RECORD_DATA));
                } else {
                    BFPopView.this.onMessage(message);
                }
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
            }
        }
    }

    public void runOnUiThread(int i, RunnableEx runnableEx) {
        this.handler.postDelayed(runnableEx, i);
    }

    public RelativeLayout getParent() {
        return this.parent;
    }

    public void setParent(RelativeLayout relativeLayout) {
        this.parent = relativeLayout;
    }

    public Object getArgs() {
        return this.args;
    }

    public void setArgs(Object obj) {
        this.args = obj;
    }

    /* loaded from: classes2.dex */
    public static class PopViewItem {
        Object args;
        String className;
        String key;
        String parentKey;
        BFPopView popView;

        PopViewItem() {
        }
    }

    public static void registerPopViewParent(Class cls, RelativeLayout relativeLayout) {
        mapPopViewParents.put(cls.getSimpleName(), relativeLayout);
    }

    public static void registerPopViewParent(String str, RelativeLayout relativeLayout) {
        mapPopViewParents.put(str, relativeLayout);
    }

    public static void closeAllSubPopView(Class cls) {
        closeAllSubPopView(cls.getSimpleName());
    }

    public static void closeAllSubPopView(String str) {
        Loger.writeLog("UI", "closeAllSubPopView:" + str);
        for (PopViewItem popViewItem : mapPopViews.values()) {
            if (popViewItem.parentKey.equals(str)) {
                Loger.writeLog("UI", "closeAllSubPopView sub popview:" + popViewItem.key);
                popViewItem.popView.close();
            }
        }
    }

    public static BFPopView findPopView(String str, String str2) {
        try {
            if (mapPopViews.containsKey(str2)) {
                return mapPopViews.get(str2).popView;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static void showPopView(Class cls, Class cls2, Serializable serializable) {
        showPopView(cls.getSimpleName(), cls2.getSimpleName(), cls2.getName(), serializable, 0);
    }

    public static void showPopView(Class cls, Class cls2, Serializable serializable, int i) {
        showPopView(cls.getSimpleName(), cls2.getSimpleName(), cls2.getName(), serializable, i);
    }

    public static void showPopView(String str, Class cls, Serializable serializable) {
        showPopView(str, cls.getSimpleName(), cls.getName(), serializable, 0);
    }

    public static void showPopView(String str, Class cls, Serializable serializable, int i) {
        showPopView(str, cls.getSimpleName(), cls.getName(), serializable, i);
    }

    public static void showPopView(String str, String str2, String str3, Serializable serializable, int i) {
        if (!mapPopViews.containsKey(str2)) {
            PopViewItem popViewItem = new PopViewItem();
            popViewItem.key = str2;
            popViewItem.parentKey = str;
            popViewItem.className = str3;
            popViewItem.args = serializable;
            mapPopViews.put(str2, popViewItem);
        }
        PopViewItem popViewItem2 = mapPopViews.get(str2);
        popViewItem2.parentKey = str;
        try {
            if (popViewItem2.popView == null) {
                popViewItem2.popView = (BFPopView) Class.forName(popViewItem2.className).newInstance();
                popViewItem2.popView.parentKey = str;
                popViewItem2.popView.popviewKey = str2;
            }
            if (popViewItem2.popView.getParent() == null) {
                popViewItem2.popView.setParent(mapPopViewParents.get(popViewItem2.parentKey));
            } else if (popViewItem2.popView.getParent() != mapPopViewParents.get(popViewItem2.parentKey)) {
                popViewItem2.popView.getParent().removeView(popViewItem2.popView.contentView);
                popViewItem2.popView.setParent(mapPopViewParents.get(popViewItem2.parentKey));
            }
            BFPopView bFPopView = popViewItem2.popView;
            zindex = i;
            popViewItem2.popView.setArgs(serializable);
            if (popViewItem2.popView.updateOnReShow || !popViewItem2.popView.isShowing()) {
                popViewItem2.popView.show();
            }
        } catch (Exception e) {
            Loger.writeException("SHJ", e);
            e.printStackTrace();
        }
    }

    public static synchronized void closePopView(Class cls) {
        synchronized (BFPopView.class) {
            try {
                getPopView(cls).close();
            } catch (Exception unused) {
            }
        }
    }

    public static synchronized void closePopView(String str) {
        synchronized (BFPopView.class) {
            if (mapPopViews.containsKey(str)) {
                PopViewItem popViewItem = mapPopViews.get(str);
                if (popViewItem.popView != null && popViewItem.popView.isShowing()) {
                    popViewItem.popView.close();
                }
            }
        }
    }

    public static BFPopView getPopView(Class cls) {
        String simpleName = cls.getSimpleName();
        if (mapPopViews.containsKey(simpleName)) {
            return mapPopViews.get(simpleName).popView;
        }
        return null;
    }

    public static BFPopView getPopView(String str) {
        if (mapPopViews.containsKey(str)) {
            return mapPopViews.get(str).popView;
        }
        return null;
    }

    public static int getShowingPopViewCount() {
        return showingPopViewCount;
    }

    public void setCloseOnClick(boolean z) {
        this.closeOnClick = z;
    }

    public void setCanClickUnderView(boolean z) {
        this.canClickUnderView = z;
    }

    public void setRemoveOnClose(boolean z) {
        this.removeOnClose = z;
    }

    public void setAnimations(int i, int i2) {
        this.showAnimId = i;
        this.closeAnimId = i2;
    }

    public View getContentView() {
        return this.contentView2;
    }

    public void setContentView(View view) {
        if (view == null || this.contentView != null) {
            return;
        }
        this.contentView2 = view;
        RelativeLayout relativeLayout = new RelativeLayout(view.getContext());
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, CommonTool.dip2px(view.getContext(), 5000.0f)));
        try {
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            }
            ((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
        View backgroundView = new View(view.getContext());
        backgroundView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        backgroundView.setBackgroundColor(this.color1);
        relativeLayout.addView(backgroundView);
        relativeLayout.addView(view);
        if (this.closeOnClick) {
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BFPopView.this.close();
                }
            });
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        } else {
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return !BFPopView.this.canClickUnderView;
                }
            });
        }
        this.contentView = relativeLayout;
        relativeLayout.setVisibility(View.INVISIBLE);
    }


    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.view.BFPopView$2 */
    /* loaded from: classes2.dex */
    public class CloseButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            BFPopView.this.close();
        }
    }


    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.view.BFPopView$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnTouchListener {
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view3, MotionEvent motionEvent) {
            return false;
        }


    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.view.BFPopView$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnTouchListener {

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view3, MotionEvent motionEvent) {
            return !BFPopView.this.canClickUnderView;
        }
    }

    public void show() {
        boolean z = !isShowing();
        if (this.contentView.getParent() == null) {
            int i = zindex;
            if (i != 0) {
                this.parent.addView(this.contentView, i);
            } else {
                this.parent.addView(this.contentView);
            }
        }
        this.parent.requestLayout();
        if (zindex == 0) {
            this.parent.bringChildToFront(this.contentView);
        }
        zindex = this.parent.getChildCount();
        this.contentView.setVisibility(View.VISIBLE);
        if (z && this.showAnimId != -1) {
            Animation loadAnimation = AnimationUtils.loadAnimation(this.contentView.getContext(), this.showAnimId);
            loadAnimation.setDuration(200L);
            loadAnimation.setFillAfter(true);
            loadAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.oysb.utils.view.BFPopView.5
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }


                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                    try {
                        BFPopView.this.onViewWillShow();
                    } catch (Exception unused) {
                    }
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    BFPopView.this.onViewDidShow();
                    BFPopView.showingPopViewCount++;
                    Loger.writeLog("UI", "showingPopViewCount:" + BFPopView.showingPopViewCount);
                }
            });
            this.contentView2.startAnimation(loadAnimation);
            return;
        }
        onViewWillShow();
        onViewDidShow();
        if (z) {
            showingPopViewCount++;
        }
        Loger.writeLog("UI", "showingPopViewCount:" + showingPopViewCount);
    }

    /* renamed from: com.oysb.utils.view.BFPopView$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements Animation.AnimationListener {
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }



        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            try {
                BFPopView.this.onViewWillShow();
            } catch (Exception unused) {
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            BFPopView.this.onViewDidShow();
            BFPopView.showingPopViewCount++;
            Loger.writeLog("UI", "showingPopViewCount:" + BFPopView.showingPopViewCount);
        }
    }

    public void close() {
        if (isShowing()) {
            if (this.closeAnimId != -1) {
                Loger.writeLog("UI", "closeAnimId:" + this.closeAnimId);
                try {
                    this.contentView2.clearAnimation();
                    Animation loadAnimation = AnimationUtils.loadAnimation(this.contentView2.getContext(), this.closeAnimId);
                    loadAnimation.setDuration(200L);
                    loadAnimation.setFillAfter(true);
                    loadAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.oysb.utils.view.BFPopView.6
                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationRepeat(Animation animation) {
                        }


                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationStart(Animation animation) {
                            BFPopView.this.onViewWillClose();
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationEnd(Animation animation) {
                            Loger.writeLog("UI", "onAnimationEnd1");
                            if (BFPopView.this.removeOnClose) {
                                ((RelativeLayout) BFPopView.this.contentView.getParent()).removeView(BFPopView.this.contentView);
                            }
                            BFPopView.this.contentView.setVisibility(View.GONE);
                            Loger.writeLog("UI", "onAnimationEnd2");
                            BFPopView.this.onViewDidClose();
                            BFPopView.showingPopViewCount--;
                            Loger.writeLog("UI", "showingPopViewCount:" + BFPopView.showingPopViewCount);
                        }
                    });
                    this.contentView2.startAnimation(loadAnimation);
                    return;
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                    return;
                }
            }
            Loger.writeLog("UI", "closeAnimId" + this.closeAnimId + " 1");
            try {
                if (this.removeOnClose) {
                    ((RelativeLayout) this.contentView.getParent()).removeView(this.contentView);
                }
                this.contentView.setVisibility(View.GONE);
                onViewWillClose();
                onViewDidClose();
                showingPopViewCount--;
                Loger.writeLog("UI", "showingPopViewCount:" + showingPopViewCount);
            } catch (Exception e2) {
                Loger.safe_inner_exception_catch(e2);
            }
        }
    }

    /* renamed from: com.oysb.utils.view.BFPopView$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements Animation.AnimationListener {
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        AnonymousClass6() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            BFPopView.this.onViewWillClose();
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            Loger.writeLog("UI", "onAnimationEnd1");
            if (BFPopView.this.removeOnClose) {
                ((RelativeLayout) BFPopView.this.contentView.getParent()).removeView(BFPopView.this.contentView);
            }
            BFPopView.this.contentView.setVisibility(View.GONE);
            Loger.writeLog("UI", "onAnimationEnd2");
            BFPopView.this.onViewDidClose();
            BFPopView.showingPopViewCount--;
            Loger.writeLog("UI", "showingPopViewCount:" + BFPopView.showingPopViewCount);
        }
    }

    public boolean isShowing() {
        View view = this.contentView;
        return (view == null || view.getParent() == null || this.contentView.getVisibility() != View.VISIBLE) ? false : true;
    }
}
