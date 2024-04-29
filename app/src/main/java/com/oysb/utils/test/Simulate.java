package com.oysb.utils.test;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

/* loaded from: classes2.dex */
public class Simulate {
    static View view;

    public static void attachView(View view2) {
        view = view2;
    }

    public static void click(float f, float f2) {
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, f, f2, 0);
        long j = uptimeMillis + 1000;
        MotionEvent obtain2 = MotionEvent.obtain(j, j, 1, f, f2, 0);
        view.onTouchEvent(obtain);
        view.onTouchEvent(obtain2);
        obtain.recycle();
        obtain2.recycle();
    }
}
