package com.xyshj.view;

import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.oysb.utils.cache.CacheHelper;
import com.shj.Shj;
import com.xyshj.app.ShjAppBase;

/* loaded from: classes2.dex */
public class WebViewHelper {
    WebView webView;

    /* renamed from: com.xyshj.view.WebViewHelper$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends WebChromeClient {
        AnonymousClass1() {
        }

        @Override // android.webkit.WebChromeClient
        public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
            callback.invoke(str, true, false);
            super.onGeolocationPermissionsShowPrompt(str, callback);
        }
    }

    public void attach(WebView webView) {
        this.webView = webView;
        webView.setWebChromeClient(new WebChromeClient() { // from class: com.xyshj.view.WebViewHelper.1
            AnonymousClass1() {
            }

            @Override // android.webkit.WebChromeClient
            public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
                callback.invoke(str, true, false);
                super.onGeolocationPermissionsShowPrompt(str, callback);
            }
        });
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.supportMultipleWindows();
        settings.setDatabaseEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationDatabasePath(ShjAppBase.getInstance().getDir("database", 0).getPath());
        this.webView.setWebViewClient(new WebViewClient() { // from class: com.xyshj.view.WebViewHelper.2
            AnonymousClass2() {
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView2, String str) {
                String str2;
                Object asObject = CacheHelper.getFileCache().getAsObject("lat");
                Object asObject2 = CacheHelper.getFileCache().getAsObject("lng");
                String str3 = "jqbh=" + Shj.getMachineId() + "&lat=" + (asObject == null ? 0.0d : ((Double) asObject).doubleValue()) + "&lng=" + (asObject != null ? ((Double) asObject2).doubleValue() : 0.0d);
                if (str.contains("?")) {
                    str2 = str + "&" + str3;
                } else {
                    str2 = str + "?" + str3;
                }
                webView2.loadUrl(str2);
                return true;
            }
        });
    }

    /* renamed from: com.xyshj.view.WebViewHelper$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 extends WebViewClient {
        AnonymousClass2() {
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView2, String str) {
            String str2;
            Object asObject = CacheHelper.getFileCache().getAsObject("lat");
            Object asObject2 = CacheHelper.getFileCache().getAsObject("lng");
            String str3 = "jqbh=" + Shj.getMachineId() + "&lat=" + (asObject == null ? 0.0d : ((Double) asObject).doubleValue()) + "&lng=" + (asObject != null ? ((Double) asObject2).doubleValue() : 0.0d);
            if (str.contains("?")) {
                str2 = str + "&" + str3;
            } else {
                str2 = str + "?" + str3;
            }
            webView2.loadUrl(str2);
            return true;
        }
    }

    public void onPause() {
        WebView webView = this.webView;
        if (webView != null) {
            webView.pauseTimers();
        }
    }

    public void onResume() {
        WebView webView = this.webView;
        if (webView != null) {
            webView.resumeTimers();
        }
    }
}
