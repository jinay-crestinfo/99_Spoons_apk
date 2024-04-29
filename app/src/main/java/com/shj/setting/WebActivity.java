package com.shj.setting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.shj.setting.Dialog.TipDialog;
import org.apache.commons.lang3.StringUtils;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes2.dex */
public class WebActivity extends Activity {
    private Button bt_close;
    private ProgressBar help_center_loading_prgbar;
    private WebView help_webview;
    private boolean netAvailable = true;
    private TipDialog tipDialog;
    private TextView tv_success;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_web);
        this.help_center_loading_prgbar = (ProgressBar) findViewById(R.id.help_center_loading_prgbar);
        this.help_webview = (WebView) findViewById(R.id.help_webview);
        this.bt_close = (Button) findViewById(R.id.bt_close);
        this.tv_success = (TextView) findViewById(R.id.tv_success);
        this.help_webview.getSettings().setCacheMode(2);
        this.help_webview.getSettings().setJavaScriptEnabled(true);
        this.help_webview.getSettings().setUseWideViewPort(true);
        this.help_webview.getSettings().setLoadWithOverviewMode(true);
        this.help_webview.getSettings().setBuiltInZoomControls(true);
        this.help_webview.getSettings().setSupportZoom(true);
        this.help_webview.getSettings().setLoadsImagesAutomatically(true);
        this.help_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.help_webview.getSettings().setAppCacheEnabled(true);
        this.help_webview.getSettings().setDomStorageEnabled(true);
        this.help_webview.getSettings().supportMultipleWindows();
        this.help_webview.getSettings().setAllowContentAccess(true);
        this.help_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        this.help_webview.getSettings().setUseWideViewPort(true);
        this.help_webview.getSettings().setLoadWithOverviewMode(true);
        this.help_webview.getSettings().setSavePassword(true);
        this.help_webview.getSettings().setSaveFormData(true);
        this.help_webview.setWebChromeClient(new WebChromeClient());
        this.help_webview.setWebViewClient(new WebViewClient() { // from class: com.shj.setting.WebActivity.1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                return true;
            }

            AnonymousClass1() {
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                WebActivity.this.help_center_loading_prgbar.setVisibility(0);
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                WebActivity.this.help_center_loading_prgbar.setVisibility(4);
                if (WebActivity.this.netAvailable) {
                    if (WebActivity.this.tipDialog == null || !WebActivity.this.tipDialog.isShowing()) {
                        WebActivity webActivity = WebActivity.this;
                        WebActivity webActivity2 = WebActivity.this;
                        webActivity.tipDialog = new TipDialog(webActivity2, webActivity2.getString(R.string.net_available), WebActivity.this.getString(R.string.button_ok));
                        WebActivity.this.tipDialog.show();
                    }
                }
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, int i, String str, String str2) {
                super.onReceivedError(webView, i, str, str2);
                WebActivity.this.help_center_loading_prgbar.setVisibility(4);
                WebActivity.this.netAvailable = false;
                if (WebActivity.this.tipDialog != null && WebActivity.this.tipDialog.isShowing()) {
                    WebActivity.this.tipDialog.dismiss();
                }
                String string = WebActivity.this.getString(R.string.setting_net_available_tip);
                String string2 = WebActivity.this.getString(R.string.net_not_available);
                String replaceAll = string.replaceAll(StringUtils.LF, "<br>");
                TextView textView = WebActivity.this.tv_success;
                textView.setText(Html.fromHtml((string2 + "<br>") + replaceAll));
                WebActivity.this.tv_success.setVisibility(0);
                WebActivity.this.tv_success.bringToFront();
            }
        });
        this.bt_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.WebActivity.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                WebActivity.this.finish();
            }
        });
        this.help_webview.loadUrl(getIntent().getExtras().getString(IjkMediaPlayer.OnNativeInvokeListener.ARG_URL));
    }

    /* renamed from: com.shj.setting.WebActivity$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends WebViewClient {
        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            return true;
        }

        AnonymousClass1() {
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
            WebActivity.this.help_center_loading_prgbar.setVisibility(0);
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            WebActivity.this.help_center_loading_prgbar.setVisibility(4);
            if (WebActivity.this.netAvailable) {
                if (WebActivity.this.tipDialog == null || !WebActivity.this.tipDialog.isShowing()) {
                    WebActivity webActivity = WebActivity.this;
                    WebActivity webActivity2 = WebActivity.this;
                    webActivity.tipDialog = new TipDialog(webActivity2, webActivity2.getString(R.string.net_available), WebActivity.this.getString(R.string.button_ok));
                    WebActivity.this.tipDialog.show();
                }
            }
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
            WebActivity.this.help_center_loading_prgbar.setVisibility(4);
            WebActivity.this.netAvailable = false;
            if (WebActivity.this.tipDialog != null && WebActivity.this.tipDialog.isShowing()) {
                WebActivity.this.tipDialog.dismiss();
            }
            String string = WebActivity.this.getString(R.string.setting_net_available_tip);
            String string2 = WebActivity.this.getString(R.string.net_not_available);
            String replaceAll = string.replaceAll(StringUtils.LF, "<br>");
            TextView textView = WebActivity.this.tv_success;
            textView.setText(Html.fromHtml((string2 + "<br>") + replaceAll));
            WebActivity.this.tv_success.setVisibility(0);
            WebActivity.this.tv_success.bringToFront();
        }
    }

    /* renamed from: com.shj.setting.WebActivity$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            WebActivity.this.finish();
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4 && this.help_webview.canGoBack()) {
            this.help_webview.goBack();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }
}
