package com.xyshj.machine.popview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.iflytek.cloud.SpeechEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.view.BasePopView;
import com.shj.Shj;
import com.shj.ShjDbHelper;
import com.shj.setting.NetAddress.NetAddress;
import com.xyshj.app.ShjAppBase;
import com.xyshj.app.ShjAppHelper;
import com.xyshj.machine.R;
import com.xyshj.machine.app.VmdHelper;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class PopView_EnterCode extends BasePopView implements View.OnClickListener {
    TextView code = null;

    @Override // com.oysb.utils.view.BasePopView
    protected void registActions(List<String> list) {
    }

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        int dimensionPixelSize = getParent().getContext().getResources().getDimensionPixelSize(R.dimen.px80);
        RelativeLayout relativeLayout = new RelativeLayout(getParent().getContext());
        relativeLayout.setBackgroundColor(Color.parseColor("#88888888"));
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        relativeLayout.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        View inflate = layoutInflater.inflate(R.layout.enter_code_dlg, (ViewGroup) null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(13);
        inflate.setLayoutParams(layoutParams);
        relativeLayout.addView(inflate);
        this.code = (TextView) inflate.findViewById(R.id.code);
        inflate.findViewById(R.id.cancel).setOnClickListener(this);
        inflate.findViewById(R.id.ok).setOnClickListener(this);
        inflate.findViewById(R.id.bt_clear).setOnClickListener(this);
        inflate.findViewById(R.id.bt_delete).setOnClickListener(this);
        inflate.findViewById(R.id.bt_0).setOnClickListener(this);
        inflate.findViewById(R.id.bt_1).setOnClickListener(this);
        inflate.findViewById(R.id.bt_2).setOnClickListener(this);
        inflate.findViewById(R.id.bt_3).setOnClickListener(this);
        inflate.findViewById(R.id.bt_4).setOnClickListener(this);
        inflate.findViewById(R.id.bt_5).setOnClickListener(this);
        inflate.findViewById(R.id.bt_6).setOnClickListener(this);
        inflate.findViewById(R.id.bt_7).setOnClickListener(this);
        inflate.findViewById(R.id.bt_8).setOnClickListener(this);
        inflate.findViewById(R.id.bt_9).setOnClickListener(this);
        return relativeLayout;
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onAction(String str, Bundle bundle) {
        Loger.writeLog("SHJ", "ACIONT : " + str + " isShowing:" + isShowing());
        if (isShowing() && str.hashCode() == 0) {
            str.equals("");
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onMessage(Message message) {
        int i = message.what;
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidShow() {
        try {
            this.code.setText("");
        } catch (Exception unused) {
        }
        super.onViewDidShow();
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidClose() {
        try {
            this.code.setText("");
        } catch (Exception unused) {
        }
        super.onViewDidClose();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_0 /* 2131230775 */:
            case R.id.bt_1 /* 2131230776 */:
            case R.id.bt_2 /* 2131230777 */:
            case R.id.bt_3 /* 2131230778 */:
            case R.id.bt_4 /* 2131230779 */:
            case R.id.bt_5 /* 2131230780 */:
            case R.id.bt_6 /* 2131230781 */:
            case R.id.bt_7 /* 2131230782 */:
            case R.id.bt_8 /* 2131230783 */:
            case R.id.bt_9 /* 2131230784 */:
                if (this.code.getText().toString().equalsIgnoreCase(ShjAppHelper.getString(R.string.yhm_error))) {
                    this.code.setText("");
                }
                this.code.setText(this.code.getText().toString() + view.getTag());
                return;
            default:
                switch (id) {
                    case R.id.bt_clear /* 2131230807 */:
                        this.code.setText("");
                        return;
                    case R.id.bt_delete /* 2131230818 */:
                        if (this.code.getText().length() > 0) {
                            TextView textView = this.code;
                            textView.setText(textView.getText().toString().substring(0, this.code.getText().length() - 1));
                            return;
                        }
                        return;
                    case R.id.cancel /* 2131230915 */:
                        close();
                        return;
                    case R.id.ok /* 2131231159 */:
                        verifyCoupon();
                        return;
                    default:
                        return;
                }
        }
    }

    private void verifyCoupon() {
        try {
            JSONObject jSONObject = new JSONObject(getArgs().toString());
            String queryVoucherUsableUrl = NetAddress.queryVoucherUsableUrl();
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("jqbh", Shj.getMachineId());
            JSONArray jSONArray = new JSONArray();
            jSONArray.put(jSONObject.optString("goodsCode"));
            int optInt = jSONObject.optInt("goodsCount");
            for (int i = 0; i < optInt; i++) {
                jSONArray.put(jSONObject.optString("goodsCode"));
            }
            jSONObject2.put("splist", jSONArray);
            double optInt2 = jSONObject.optInt(ShjDbHelper.COLUM_price);
            Double.isNaN(optInt2);
            jSONObject2.put("ddzj", optInt2 / 100.0d);
            jSONObject2.put("djqbh", this.code.getText().toString());
            jSONObject2.put("qqf", 1);
            RequestItem requestItem = new RequestItem(queryVoucherUsableUrl, jSONObject2, "POST");
            requestItem.setRepeatDelay(5000);
            requestItem.setRequestMaxCount(1);
            requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.xyshj.machine.popview.PopView_EnterCode.1
                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onFailure(RequestItem requestItem2, int i2, String str, Throwable th) {
                }

                AnonymousClass1() {
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public boolean onSuccess(RequestItem requestItem2, int i2, String str) {
                    try {
                        JSONObject jSONObject3 = new JSONObject(str);
                        if (!jSONObject3.getString("code").equals("H0000")) {
                            PopView_EnterCode.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.xyshj.machine.popview.PopView_EnterCode.1.3
                                AnonymousClass3() {
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    PopView_EnterCode.this.code.setText(ShjAppHelper.getString(R.string.yhm_error));
                                }
                            });
                            return false;
                        }
                        JSONObject jSONObject4 = jSONObject3.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
                        int optInt3 = jSONObject4.optInt("nfsy");
                        int optInt4 = jSONObject4.optInt("yhje");
                        if (jSONObject4.optString(NotificationCompat.CATEGORY_MESSAGE) == null) {
                            jSONObject3.optString(NotificationCompat.CATEGORY_MESSAGE);
                        }
                        if (optInt3 != 1) {
                            PopView_EnterCode.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.xyshj.machine.popview.PopView_EnterCode.1.2
                                AnonymousClass2() {
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    PopView_EnterCode.this.code.setText(ShjAppHelper.getString(R.string.yhm_error));
                                }
                            });
                            return false;
                        }
                        VmdHelper.get().getBqlOrder().setYhje(optInt4);
                        VmdHelper.get().getBqlOrder().setYhm(requestItem2.getJSONParams().getString("djqbh"));
                        ShjAppBase.sysApp.sendBroadcast(new Intent(VmdHelper.ACTION_UPDATE_YYJE));
                        PopView_EnterCode.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.xyshj.machine.popview.PopView_EnterCode.1.1
                            RunnableC00841() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                PopView_EnterCode.this.close();
                            }
                        });
                        return false;
                    } catch (Exception e) {
                        Loger.safe_inner_exception_catch(e);
                        Loger.writeException("SALES", e);
                        return false;
                    }
                }

                /* renamed from: com.xyshj.machine.popview.PopView_EnterCode$1$1 */
                /* loaded from: classes2.dex */
                class RunnableC00841 implements Runnable {
                    RunnableC00841() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        PopView_EnterCode.this.close();
                    }
                }

                /* renamed from: com.xyshj.machine.popview.PopView_EnterCode$1$2 */
                /* loaded from: classes2.dex */
                class AnonymousClass2 implements Runnable {
                    AnonymousClass2() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        PopView_EnterCode.this.code.setText(ShjAppHelper.getString(R.string.yhm_error));
                    }
                }

                /* renamed from: com.xyshj.machine.popview.PopView_EnterCode$1$3 */
                /* loaded from: classes2.dex */
                class AnonymousClass3 implements Runnable {
                    AnonymousClass3() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        PopView_EnterCode.this.code.setText(ShjAppHelper.getString(R.string.yhm_error));
                    }
                }

                @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                public void onRequestFinished(RequestItem requestItem2, boolean z) {
                    if (z) {
                        PopView_EnterCode.this.close();
                    }
                }
            });
            RequestHelper.request(requestItem);
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }

    /* renamed from: com.xyshj.machine.popview.PopView_EnterCode$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i2, String str, Throwable th) {
        }

        AnonymousClass1() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i2, String str) {
            try {
                JSONObject jSONObject3 = new JSONObject(str);
                if (!jSONObject3.getString("code").equals("H0000")) {
                    PopView_EnterCode.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.xyshj.machine.popview.PopView_EnterCode.1.3
                        AnonymousClass3() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_EnterCode.this.code.setText(ShjAppHelper.getString(R.string.yhm_error));
                        }
                    });
                    return false;
                }
                JSONObject jSONObject4 = jSONObject3.getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
                int optInt3 = jSONObject4.optInt("nfsy");
                int optInt4 = jSONObject4.optInt("yhje");
                if (jSONObject4.optString(NotificationCompat.CATEGORY_MESSAGE) == null) {
                    jSONObject3.optString(NotificationCompat.CATEGORY_MESSAGE);
                }
                if (optInt3 != 1) {
                    PopView_EnterCode.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.xyshj.machine.popview.PopView_EnterCode.1.2
                        AnonymousClass2() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PopView_EnterCode.this.code.setText(ShjAppHelper.getString(R.string.yhm_error));
                        }
                    });
                    return false;
                }
                VmdHelper.get().getBqlOrder().setYhje(optInt4);
                VmdHelper.get().getBqlOrder().setYhm(requestItem2.getJSONParams().getString("djqbh"));
                ShjAppBase.sysApp.sendBroadcast(new Intent(VmdHelper.ACTION_UPDATE_YYJE));
                PopView_EnterCode.this.handler.postAtFrontOfQueue(new Runnable() { // from class: com.xyshj.machine.popview.PopView_EnterCode.1.1
                    RunnableC00841() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        PopView_EnterCode.this.close();
                    }
                });
                return false;
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                Loger.writeException("SALES", e);
                return false;
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_EnterCode$1$1 */
        /* loaded from: classes2.dex */
        class RunnableC00841 implements Runnable {
            RunnableC00841() {
            }

            @Override // java.lang.Runnable
            public void run() {
                PopView_EnterCode.this.close();
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_EnterCode$1$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements Runnable {
            AnonymousClass2() {
            }

            @Override // java.lang.Runnable
            public void run() {
                PopView_EnterCode.this.code.setText(ShjAppHelper.getString(R.string.yhm_error));
            }
        }

        /* renamed from: com.xyshj.machine.popview.PopView_EnterCode$1$3 */
        /* loaded from: classes2.dex */
        class AnonymousClass3 implements Runnable {
            AnonymousClass3() {
            }

            @Override // java.lang.Runnable
            public void run() {
                PopView_EnterCode.this.code.setText(ShjAppHelper.getString(R.string.yhm_error));
            }
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
            if (z) {
                PopView_EnterCode.this.close();
            }
        }
    }
}
