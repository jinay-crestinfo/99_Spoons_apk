package com.shj.setting.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.oysb.utils.Loger;
import com.shj.Shj;
import com.shj.device.Machine;
import com.shj.setting.R;
import com.shj.setting.view.CommonProblemView;
import com.shj.setting.view.FaultClearView;
import com.shj.setting.view.HelpQrcodeView;
import com.shj.setting.view.NetCheckView;
import com.shj.setting.view.SingleTextTipView;
import com.shj.setting.view.TrafficStatsView;
import com.xyshj.database.setting.AppSetting;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class FaultHintDialog extends Dialog {
    private Button button_close;
    private Button button_set;
    private Context context;
    private int delayTime;
    private Handler handler;
    private int initDelayTime;
    private LinearLayout ll_content;

    static /* synthetic */ int access$110(FaultHintDialog faultHintDialog) {
        int i = faultHintDialog.delayTime;
        faultHintDialog.delayTime = i - 1;
        return i;
    }

    public FaultHintDialog(Context context) {
        super(context, R.style.loading_style);
        this.initDelayTime = 60;
        AnonymousClass3 anonymousClass3 = new Handler() { // from class: com.shj.setting.Dialog.FaultHintDialog.3
            AnonymousClass3() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                try {
                    FaultHintDialog.access$110(FaultHintDialog.this);
                    if (FaultHintDialog.this.delayTime > 0) {
                        FaultHintDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                    } else {
                        FaultHintDialog.this.dismiss();
                    }
                } catch (Exception unused) {
                }
            }
        };
        this.handler = anonymousClass3;
        this.context = context;
        this.delayTime = this.initDelayTime;
        anonymousClass3.sendEmptyMessageDelayed(0, 1000L);
        initView();
        addCommonProblem();
        addLine();
        if (!AppSetting.getHideHelpQrcode(context, null)) {
            addHelpQrcode();
            addLine();
        }
        addNetCheck();
        addLine();
        addFaultItem();
        addLine();
        addTrafficStatsView();
        addLine();
        if (addTemperatureTrobeProblem()) {
            addLine();
        }
        if (addTimeProblem()) {
            addLine();
        }
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_dialog_fault_hint);
        this.ll_content = (LinearLayout) findViewById(R.id.ll_content);
        this.button_close = (Button) findViewById(R.id.button_close);
        this.button_set = (Button) findViewById(R.id.button_set);
        setCanceledOnTouchOutside(true);
        setListener();
    }

    private void addHelpQrcode() {
        this.ll_content.addView(new HelpQrcodeView(this.context));
    }

    private void addCommonProblem() {
        this.ll_content.addView(new CommonProblemView(this.context));
    }

    private void addFaultItem() {
        this.ll_content.addView(new FaultClearView(this.context));
    }

    private void addNetCheck() {
        this.ll_content.addView(new NetCheckView(this.context));
    }

    private void addTrafficStatsView() {
        this.ll_content.addView(new TrafficStatsView(this.context));
    }

    private boolean addTemperatureTrobeProblem() {
        String str = null;
        int i = 0;
        for (int i2 = 0; i2 < 10; i2++) {
            Machine machine = Shj.getMachine(i2, false);
            if (machine != null && machine.getTemperatureFault().intValue() == 162) {
                if (i != 0) {
                    str = str + StringUtils.LF + this.context.getString(R.string.auxiliary_engine) + i2 + this.context.getString(R.string.temperature_trobe_problem);
                } else if (i2 == 0) {
                    str = this.context.getString(R.string.host) + this.context.getString(R.string.temperature_trobe_problem);
                } else {
                    str = this.context.getString(R.string.auxiliary_engine) + i2 + this.context.getString(R.string.temperature_trobe_problem);
                }
                i++;
            }
        }
        if (str == null) {
            return false;
        }
        this.ll_content.addView(new SingleTextTipView(this.context, str));
        return true;
    }

    private boolean addTimeProblem() {
        if (!Shj.getAndroidSystemTimeError()) {
            return false;
        }
        this.ll_content.addView(new SingleTextTipView(this.context, this.context.getString(R.string.time_error_tip)));
        return true;
    }

    private void addLine() {
        View view = new View(this.context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.height = 1;
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.ll_content.addView(view);
    }

    /* renamed from: com.shj.setting.Dialog.FaultHintDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            FaultHintDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.button_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.FaultHintDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FaultHintDialog.this.dismiss();
            }
        });
        this.button_set.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.FaultHintDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FaultHintDialog.enterSetting((Activity) FaultHintDialog.this.context, true);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.FaultHintDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            FaultHintDialog.enterSetting((Activity) FaultHintDialog.this.context, true);
        }
    }

    public static void enterSetting(Activity activity, boolean z) {
        if (!z || Shj.getMachine(0, false).isDoorIsOpen()) {
            Loger.writeLog("UI", "调设置" + Log.getStackTraceString(new Throwable()));
            Intent intent = new Intent("android.intent.action.xinyuan.setting");
            intent.putExtra("showLoginDialog", z);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            activity.finish();
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        this.handler.removeMessages(0);
    }

    /* renamed from: com.shj.setting.Dialog.FaultHintDialog$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 extends Handler {
        AnonymousClass3() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                FaultHintDialog.access$110(FaultHintDialog.this);
                if (FaultHintDialog.this.delayTime > 0) {
                    FaultHintDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                } else {
                    FaultHintDialog.this.dismiss();
                }
            } catch (Exception unused) {
            }
        }
    }
}
