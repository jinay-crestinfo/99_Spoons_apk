package com.xyshj.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/* loaded from: classes2.dex */
public class AutoScrollViewPager extends ViewPager {
    public static final int DEFAULT_INTERVAL = 1500;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int SCROLL_WAITE = 1000;
    public static final int SCROLL_WHAT = 0;
    public static final int SLIDE_BORDER_MODE_CYCLE = 1;
    public static final int SLIDE_BORDER_MODE_NONE = 0;
    public static final int SLIDE_BORDER_MODE_TO_PARENT = 2;
    private double autoScrollFactor;
    private int direction;
    private float downX;
    private Handler handler;
    private long interval;
    private boolean isAutoScroll;
    private boolean isBorderAnimation;
    private boolean isCycle;
    private boolean isStopByTouch;
    private CustomDurationScroller scroller;
    private int slideBorderMode;
    private boolean stopScrollWhenTouch;
    private double swipeScrollFactor;
    private float touchX;

    public AutoScrollViewPager(Context context) {
        super(context);
        this.interval = 1500L;
        this.direction = 1;
        this.isCycle = true;
        this.stopScrollWhenTouch = true;
        this.slideBorderMode = 0;
        this.isBorderAnimation = true;
        this.autoScrollFactor = 1.0d;
        this.swipeScrollFactor = 1.0d;
        this.isAutoScroll = false;
        this.isStopByTouch = false;
        this.touchX = 0.0f;
        this.downX = 0.0f;
        this.scroller = null;
        init();
    }

    public AutoScrollViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.interval = 1500L;
        this.direction = 1;
        this.isCycle = true;
        this.stopScrollWhenTouch = true;
        this.slideBorderMode = 0;
        this.isBorderAnimation = true;
        this.autoScrollFactor = 1.0d;
        this.swipeScrollFactor = 1.0d;
        this.isAutoScroll = false;
        this.isStopByTouch = false;
        this.touchX = 0.0f;
        this.downX = 0.0f;
        this.scroller = null;
        init();
    }

    private void init() {
        this.handler = new MyHandler(this);
        setViewPagerScroller();
    }

    public void startAutoScroll() {
        this.isAutoScroll = true;
        double d = this.interval;
        double duration = this.scroller.getDuration();
        double d2 = this.autoScrollFactor;
        Double.isNaN(duration);
        double d3 = (duration / d2) * this.swipeScrollFactor;
        Double.isNaN(d);
        sendScrollMessage((long) (d + d3));
    }

    public void startAutoScroll(int i) {
        this.isAutoScroll = true;
        sendScrollMessage(i);
    }

    public void stopAutoScroll() {
        this.isAutoScroll = false;
        this.handler.removeMessages(0);
    }

    public void setSwipeScrollDurationFactor(double d) {
        this.swipeScrollFactor = d;
    }

    public void setAutoScrollDurationFactor(double d) {
        this.autoScrollFactor = d;
    }

    public void sendScrollMessage(long j) {
        this.handler.removeMessages(0);
        this.handler.sendEmptyMessageDelayed(0, j);
    }

    private void setViewPagerScroller() {
        try {
            Field declaredField = ViewPager.class.getDeclaredField("mScroller");
            declaredField.setAccessible(true);
            Field declaredField2 = ViewPager.class.getDeclaredField("sInterpolator");
            declaredField2.setAccessible(true);
            CustomDurationScroller customDurationScroller = new CustomDurationScroller(getContext(), (Interpolator) declaredField2.get(null));
            this.scroller = customDurationScroller;
            declaredField.set(this, customDurationScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scrollOnce() {
        int count;
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        if (adapter == null || (count = adapter.getCount()) <= 1) {
            return;
        }
        int i = this.direction == 0 ? currentItem - 1 : currentItem + 1;
        if (i < 0) {
            if (this.isCycle) {
                setCurrentItem(count - 1, this.isBorderAnimation);
            }
        } else {
            if (i == count) {
                if (this.isCycle) {
                    setCurrentItem(0, this.isBorderAnimation);
                    return;
                }
                return;
            }
            setCurrentItem(i, true);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        if (this.stopScrollWhenTouch) {
            if (actionMasked == 0 && this.isAutoScroll) {
                this.isStopByTouch = true;
                this.handler.sendEmptyMessageDelayed(1000, 30000L);
                stopAutoScroll();
            } else {
                motionEvent.getAction();
            }
        }
        int i = this.slideBorderMode;
        if (i == 2 || i == 1) {
            this.touchX = motionEvent.getX();
            if (motionEvent.getAction() == 0) {
                this.downX = this.touchX;
            }
            int currentItem = getCurrentItem();
            PagerAdapter adapter = getAdapter();
            int count = adapter == null ? 0 : adapter.getCount();
            if ((currentItem == 0 && this.downX <= this.touchX) || (currentItem == count - 1 && this.downX >= this.touchX)) {
                if (this.slideBorderMode == 2) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    if (count > 1) {
                        setCurrentItem((count - currentItem) - 1, this.isBorderAnimation);
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.dispatchTouchEvent(motionEvent);
            }
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(motionEvent);
    }

    /* loaded from: classes2.dex */
    public static class MyHandler extends Handler {
        private final WeakReference<AutoScrollViewPager> autoScrollViewPager;

        public MyHandler(AutoScrollViewPager autoScrollViewPager) {
            this.autoScrollViewPager = new WeakReference<>(autoScrollViewPager);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AutoScrollViewPager autoScrollViewPager;
            super.handleMessage(message);
            int i = message.what;
            if (i != 0) {
                if (i == 1000 && (autoScrollViewPager = this.autoScrollViewPager.get()) != null) {
                    autoScrollViewPager.startAutoScroll();
                    return;
                }
                return;
            }
            AutoScrollViewPager autoScrollViewPager2 = this.autoScrollViewPager.get();
            if (autoScrollViewPager2 != null) {
                autoScrollViewPager2.scroller.setScrollDurationFactor(autoScrollViewPager2.autoScrollFactor);
                autoScrollViewPager2.scrollOnce();
                autoScrollViewPager2.scroller.setScrollDurationFactor(autoScrollViewPager2.swipeScrollFactor);
                autoScrollViewPager2.sendScrollMessage(autoScrollViewPager2.interval + autoScrollViewPager2.scroller.getDuration());
            }
        }
    }

    public long getInterval() {
        return this.interval;
    }

    public void setInterval(long j) {
        this.interval = j;
    }

    public int getDirection() {
        return this.direction == 0 ? 0 : 1;
    }

    public void setDirection(int i) {
        this.direction = i;
    }

    public boolean isCycle() {
        return this.isCycle;
    }

    public void setCycle(boolean z) {
        this.isCycle = z;
    }

    public boolean isStopScrollWhenTouch() {
        return this.stopScrollWhenTouch;
    }

    public void setStopScrollWhenTouch(boolean z) {
        this.stopScrollWhenTouch = z;
    }

    public int getSlideBorderMode() {
        return this.slideBorderMode;
    }

    public void setSlideBorderMode(int i) {
        this.slideBorderMode = i;
    }

    public boolean isBorderAnimation() {
        return this.isBorderAnimation;
    }

    public void setBorderAnimation(boolean z) {
        this.isBorderAnimation = z;
    }
}
