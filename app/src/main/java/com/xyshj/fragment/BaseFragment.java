package com.xyshj.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.Loger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public abstract class BaseFragment extends Fragment {
    private View contentView;
    private LayoutInflater inflater;
    private boolean isFragmentVisible = false;
    private boolean isFirstVisible = true;
    private boolean isReuseView = true;
    MyBroadcastReceiver broadcastReceiver = null;
    private String name = "";
    protected Handler handler = new Handler() { // from class: com.xyshj.fragment.BaseFragment.1


        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                if (message.what == 9998) {
                    Intent intent = (Intent) message.obj;
                    BaseFragment.this.onAction(intent.getAction(), intent.getBundleExtra(SpeechEvent.KEY_EVENT_RECORD_DATA));
                } else {
                    BaseFragment.this.onMessage(message);
                }
            } catch (Exception unused) {
            }
            super.handleMessage(message);
        }
    };

    public abstract View createView(LayoutInflater layoutInflater);

    public abstract void initViews(Context context);

    protected abstract void onAction(String str, Bundle bundle);

    protected void onFragmentFirstVisible() {
    }

    protected void onFragmentVisibleChange(boolean z) {
    }

    protected abstract void onMessage(Message message);

    protected abstract void registActions(List<String> list);

    public void updateFragment() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xyshj.fragment.BaseFragment$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                if (message.what == 9998) {
                    Intent intent = (Intent) message.obj;
                    BaseFragment.this.onAction(intent.getAction(), intent.getBundleExtra(SpeechEvent.KEY_EVENT_RECORD_DATA));
                } else {
                    BaseFragment.this.onMessage(message);
                }
            } catch (Exception unused) {
            }
            super.handleMessage(message);
        }
    }

    public String getName() {
        String str = this.name;
        if (str == null || str.length() == 0) {
            this.name = getClass().getSimpleName();
        }
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    /* loaded from: classes2.dex */
    public class MyBroadcastReceiver extends BroadcastReceiver {
        MyBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Message obtain = Message.obtain();
            obtain.obj = intent;
            obtain.what = 9998;
            BaseFragment.this.handler.sendMessage(obtain);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (this.contentView == null) {
            return;
        }
        if (this.isFirstVisible && z) {
            onFragmentFirstVisible();
            this.isFirstVisible = false;
        }
        if (z) {
            onFragmentVisibleChange(true);
            this.isFragmentVisible = true;
        } else if (this.isFragmentVisible) {
            this.isFragmentVisible = false;
            onFragmentVisibleChange(false);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        initVariable();
    }

    private void initVariable() {
        this.isFirstVisible = true;
        this.isFragmentVisible = false;
        this.contentView = null;
        this.isReuseView = true;
        try {
            synchronized (this) {
                if (this.broadcastReceiver != null) {
                    Loger.writeLog("OBJ", "fragement:" + getName() + " unregisterReceiver");
                    getActivity().unregisterReceiver(this.broadcastReceiver);
                    this.broadcastReceiver = null;
                }
            }
        } catch (Exception unused) {
        }
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.inflater = layoutInflater;
        if (this.contentView == null) {
            this.contentView = createView(layoutInflater);
            initViews(getActivity());
        }
        if (this.contentView.getParent() != null) {
            ((ViewGroup) this.contentView.getParent()).removeView(this.contentView);
        }
        return this.contentView;
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        if (getUserVisibleHint()) {
            if (this.isFirstVisible) {
                onFragmentFirstVisible();
                this.isFirstVisible = false;
            }
            onFragmentVisibleChange(true);
            this.isFragmentVisible = true;
        }
        super.onViewCreated(view, bundle);
    }

    protected void reuseView(boolean z) {
        this.isReuseView = z;
    }

    protected boolean isFragmentVisible() {
        return this.isFragmentVisible;
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ArrayList arrayList = new ArrayList();
        registActions(arrayList);
        registAction(arrayList);
    }

    public View getContentView() {
        return this.contentView;
    }

    public LayoutInflater getLayoutInfater() {
        return this.inflater;
    }

    public View findViewById(int i) {
        return this.contentView.findViewById(i);
    }

    private void registAction(List<String> list) {
        IntentFilter intentFilter = new IntentFilter();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            intentFilter.addAction(it.next());
        }
        synchronized (this) {
            if (this.broadcastReceiver != null) {
                getActivity().unregisterReceiver(this.broadcastReceiver);
                this.broadcastReceiver = null;
            }
            this.broadcastReceiver = new MyBroadcastReceiver();
            getActivity().registerReceiver(this.broadcastReceiver, intentFilter);
        }
    }
}
