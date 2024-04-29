package com.xyshj.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/* loaded from: classes2.dex */
public class CustomDurationScroller extends Scroller {
    private double scrollFactor;

    public CustomDurationScroller(Context context) {
        super(context);
        this.scrollFactor = 1.0d;
    }

    public CustomDurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
        this.scrollFactor = 1.0d;
    }

    public void setScrollDurationFactor(double d) {
        this.scrollFactor = d;
    }

    @Override // android.widget.Scroller
    public void startScroll(int i, int i2, int i3, int i4, int i5) {
        double d = i5;
        double d2 = this.scrollFactor;
        Double.isNaN(d);
        super.startScroll(i, i2, i3, i4, (int) (d * d2));
    }
}
