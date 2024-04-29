package com.shj.setting.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/* loaded from: classes2.dex */
public class HomeListener {
    public static final String TAG = "HomeListener";
    public Context mContext;
    public IntentFilter mHomeBtnIntentFilter;
    public HomeBtnReceiver mHomeBtnReceiver;
    public KeyFun mKeyFun;

    /* loaded from: classes2.dex */
    public interface KeyFun {
        void home();

        void longHome();

        void recent();
    }

    public HomeListener(Context context) {
        this.mHomeBtnIntentFilter = null;
        this.mHomeBtnReceiver = null;
        this.mContext = context;
        this.mHomeBtnIntentFilter = new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        this.mHomeBtnReceiver = new HomeBtnReceiver();
    }

    public void startListen() {
        Context context = this.mContext;
        if (context != null) {
            context.registerReceiver(this.mHomeBtnReceiver, this.mHomeBtnIntentFilter);
        } else {
            Log.e(TAG, "mContext is null and startListen fail");
        }
    }

    public void stopListen() {
        Context context = this.mContext;
        if (context != null) {
            context.unregisterReceiver(this.mHomeBtnReceiver);
        } else {
            Log.e(TAG, "mContext is null and stopListen fail");
        }
    }

    public void setInterface(KeyFun keyFun) {
        this.mKeyFun = keyFun;
    }

    /* loaded from: classes2.dex */
    public class HomeBtnReceiver extends BroadcastReceiver {
        HomeBtnReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String stringExtra;
            if (!intent.getAction().equals("android.intent.action.CLOSE_SYSTEM_DIALOGS") || (stringExtra = intent.getStringExtra("reason")) == null || HomeListener.this.mKeyFun == null) {
                return;
            }
            if (stringExtra.equals("homekey")) {
                HomeListener.this.mKeyFun.home();
            } else if (stringExtra.equals("recentapps")) {
                HomeListener.this.mKeyFun.recent();
            } else if (stringExtra.equals("assist")) {
                HomeListener.this.mKeyFun.longHome();
            }
        }
    }
}
