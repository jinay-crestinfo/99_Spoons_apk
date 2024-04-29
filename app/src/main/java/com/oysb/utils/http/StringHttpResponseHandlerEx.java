package com.oysb.utils.http;

import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;

/* loaded from: classes2.dex */
public abstract class StringHttpResponseHandlerEx extends TextHttpResponseHandler {
    public abstract void onFailureEx(int statusCode, String responseBody, Throwable error);

    public abstract void onStartEx();

    public abstract void onSuccessEx(int statusCode, String responseBody);

    @Override
    public void onFinish() {
        super.onFinish();
    }

    @Override
    public void onStart() {
        onStartEx();
    }
}
