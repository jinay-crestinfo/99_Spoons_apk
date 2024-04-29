package com.oysb.utils.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.oysb.utils.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BasePopView extends BFPopView {
    View contentView;
    Timer timer = null;
    int timeCount = 0;
    int closeTimeCount = 0;
    int timeCountOnTuch = 0;

    protected abstract View createView(LayoutInflater layoutInflater);

    protected abstract void registActions(List<String> list);

    public static int getShowingPopViewCount() {
        return showingPopViewCount;
    }

    public BasePopView() {
        setCloseOnClick(false);
        setRemoveOnClose(false);
        setAnimations(R.anim.change_left_in, R.anim.change_right_out);
    }

    @Override
    public void setParent(RelativeLayout relativeLayout) {
        super.setParent(relativeLayout);
        if (contentView == null) {
            List<String> actions = createAndRegisterActions();
            View createView = createView(((Activity) relativeLayout.getContext()).getLayoutInflater());
            contentView = createView;
            setContentView(createView);
        }
    }

    public View findViewById(int i) {
        return contentView != null ? contentView.findViewById(i) : null;
    }

    private List<String> createAndRegisterActions() {
        List<String> actions = new ArrayList<>();
        registActions(actions);
        registAction(actions);
        return actions;
    }

    private void registAction(List<String> actions) {
        IntentFilter intentFilter = new IntentFilter();
        for (String action : actions) {
            intentFilter.addAction(action);
        }
        getParent().getContext().registerReceiver(new MyBroadcastReceiver(), intentFilter);
    }

    public void resetTimeCount() {
        timeCount = 0;
    }

    public void setCloseTimeCount(int count) {
        closeTimeCount = count;
    }

    public void setTimeCountOnTuch(int count) {
        timeCountOnTuch = count;
    }

    public void updateTimeCountOnTuch() {
        if (timeCount <= timeCountOnTuch) {
            timeCount = timeCountOnTuch;
        }
    }

    @Override
    public void show() {
        if (contentView == null) {
            View createView = createView(((Activity) getParent().getContext()).getLayoutInflater());
            contentView = createView;
            setContentView(createView);
        }
        try {
            ((BasePopView) BFPopView.getPopView(getParentKey())).resetTimeCount();
        } catch (Exception ignored) {}
        cancelTimer();
        if (closeTimeCount > 0) {
            timeCount = 0;
            startCloseTimer();
        }
        super.show();
    }

    private void startCloseTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeCount++;
                if (timeCount >= closeTimeCount) {
                    closeOnUiThread();
                }
            }
        }, 1000L, 1000L);
    }

    private void closeOnUiThread() {
        new Handler(getParent().getContext().getMainLooper()).post(() -> close());
    }

    @Override
    public void close() {
        cancelTimer();
        super.close();
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message message = Message.obtain();
            message.obj = intent;
            message.what = 9999;
            handler.sendMessage(message);
        }
    }
}
