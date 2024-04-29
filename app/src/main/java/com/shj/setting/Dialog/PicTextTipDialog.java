package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.oysb.utils.Loger;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;

/* loaded from: classes2.dex */
public class PicTextTipDialog extends Dialog {
    private Button button_ok;
    private Button button_openes;
    private Context context;
    private ImageView iv_pic;
    private int resourceId;
    private int textId;
    private TextView title;

    public PicTextTipDialog(Context context, int i, int i2) {
        super(context, R.style.ad_style);
        this.context = context;
        this.resourceId = i;
        this.textId = i2;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.layout_pictext_tipdialog);
        this.title = (TextView) findViewById(R.id.title);
        this.button_ok = (Button) findViewById(R.id.button_ok);
        this.button_openes = (Button) findViewById(R.id.button_openes);
        ImageView imageView = (ImageView) findViewById(R.id.iv_pic);
        this.iv_pic = imageView;
        int i = this.resourceId;
        if (i > 0) {
            imageView.setImageResource(i);
        }
        this.title.setText(this.textId);
        setCanceledOnTouchOutside(true);
        setListener();
    }

    /* renamed from: com.shj.setting.Dialog.PicTextTipDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PicTextTipDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.button_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.PicTextTipDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PicTextTipDialog.this.dismiss();
            }
        });
        this.button_openes.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.PicTextTipDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PicTextTipDialog.this.enterFileManagement();
            }
        });
        this.iv_pic.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.PicTextTipDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PicTextTipDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.PicTextTipDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PicTextTipDialog.this.enterFileManagement();
        }
    }

    /* renamed from: com.shj.setting.Dialog.PicTextTipDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PicTextTipDialog.this.dismiss();
        }
    }

    public void enterFileManagement() {
        PackageManager packageManager = this.context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.estrongs.android.pop", 1);
            if (packageInfo != null) {
                this.context.startActivity(packageManager.getLaunchIntentForPackage(packageInfo.packageName));
            } else {
                this.context.startActivity(packageManager.getLaunchIntentForPackage(packageManager.getPackageInfo("com.fb.FileBrower", 1).packageName));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Loger.safe_inner_exception_catch(e);
            ToastUitl.showShort(this.context, this.context.getString(R.string.not_installed));
        }
    }
}
