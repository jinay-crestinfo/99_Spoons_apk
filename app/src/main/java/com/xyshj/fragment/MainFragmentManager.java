package com.xyshj.fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.oysb.utils.Stack;

/* loaded from: classes.dex */
public class MainFragmentManager {
    private int body;
    private Stack<BaseFragment> fragments;
    private FragmentManager manager;

    public void init(FragmentActivity fragmentActivity, int i) {
        this.body = i;
        this.manager = fragmentActivity.getSupportFragmentManager();
        this.fragments = new Stack<>();
    }

    public void showFragment(BaseFragment baseFragment) {
        if (baseFragment == this.fragments.peek()) {
            return;
        }
        this.fragments.push(baseFragment);
        FragmentTransaction beginTransaction = this.manager.beginTransaction();
        beginTransaction.add(this.body, baseFragment);
        beginTransaction.addToBackStack(null);
        beginTransaction.commit();
    }

    public void popFragment() {
        if (this.fragments.size() <= 1) {
            return;
        }
        BaseFragment poll = this.fragments.poll();
        FragmentTransaction beginTransaction = this.manager.beginTransaction();
        beginTransaction.remove(poll);
        beginTransaction.commit();
    }
}
