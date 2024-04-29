package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.event.SettingTypeEvent;
import com.xyshj.database.setting.SettingType;
import org.greenrobot.eventbus.EventBus;

/* loaded from: classes2.dex */
public class SyncBackstageDialog extends Dialog {
    private Context context;
    private TextView tv_download_detail;
    private TextView tv_download_goods;
    private TextView tv_onekeyset;

    public SyncBackstageDialog(Context context) {
        super(context, R.style.translucent_dialog_style);
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(85);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.x = 20;
        attributes.y = this.context.getResources().getDimensionPixelOffset(R.dimen.y140);
        setContentView(R.layout.layout_sync_backstage_dialog);
        this.tv_download_detail = (TextView) findViewById(R.id.tv_download_detail);
        this.tv_onekeyset = (TextView) findViewById(R.id.tv_onekeyset);
        this.tv_download_goods = (TextView) findViewById(R.id.tv_download_goods);
        setCanceledOnTouchOutside(true);
        setListener();
    }

    /* renamed from: com.shj.setting.Dialog.SyncBackstageDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            EventBus.getDefault().post(new SettingTypeEvent(127, null));
            SyncBackstageDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.tv_download_goods.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SyncBackstageDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                EventBus.getDefault().post(new SettingTypeEvent(127, null));
                SyncBackstageDialog.this.dismiss();
            }
        });
        this.tv_download_detail.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SyncBackstageDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                EventBus.getDefault().post(new SettingTypeEvent(SettingType.DOWNLOAD_MERCHANDISE_DETAILPICTURES, null));
                SyncBackstageDialog.this.dismiss();
            }
        });
        this.tv_onekeyset.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SyncBackstageDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                EventBus.getDefault().post(new SettingTypeEvent(SettingType.COMMODITY_ONE_BUTTON_SETUP, null));
                SyncBackstageDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.SyncBackstageDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            EventBus.getDefault().post(new SettingTypeEvent(SettingType.DOWNLOAD_MERCHANDISE_DETAILPICTURES, null));
            SyncBackstageDialog.this.dismiss();
        }
    }

    /* renamed from: com.shj.setting.Dialog.SyncBackstageDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            EventBus.getDefault().post(new SettingTypeEvent(SettingType.COMMODITY_ONE_BUTTON_SETUP, null));
            SyncBackstageDialog.this.dismiss();
        }
    }
}
