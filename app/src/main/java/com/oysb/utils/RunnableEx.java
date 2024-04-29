package com.oysb.utils;

/* loaded from: classes2.dex */
public class RunnableEx implements Runnable {
    private Object obj;

    @Override // java.lang.Runnable
    public void run() {
    }

    public RunnableEx(Object obj) {
        if (obj != null) {
            this.obj = obj;
        }
    }

    public Object getObj() {
        Object obj = this.obj;
        if (obj == null) {
            return null;
        }
        return obj;
    }
}
