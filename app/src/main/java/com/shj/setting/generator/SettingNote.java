package com.shj.setting.generator;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import com.oysb.utils.Loger;
import com.shj.Shj;
import com.shj.setting.mainSettingItem.SettingTypeName;
import com.shj.setting.widget.AbsItemView;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public abstract class SettingNote {
    protected CompeleteListener compeleteListener;
    protected Context context;
    protected AbsItemView.EventListener eventListener;
    protected UserSettingDao mUserSettingDao;
    protected int mainSettingType;
    protected int settingType;

    /* loaded from: classes2.dex */
    public interface CompeleteListener {
        void getComplete();

        void setComplete();
    }

    public void clearData() {
    }

    public abstract AbsItemView getAbsItemView();

    public abstract View getView(int i);

    public void onAttached() {
    }

    public void onDetached() {
    }

    public void querySettingData() {
    }

    public abstract void saveSetting(boolean z);

    public SettingNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        this.context = context;
        this.settingType = i2;
        this.mUserSettingDao = userSettingDao;
        this.mainSettingType = i;
        this.eventListener = new AbsItemView.EventListener() { // from class: com.shj.setting.generator.SettingNote.1
            final /* synthetic */ Context val$context;

            AnonymousClass1(Context context2) {
                context = context2;
            }

            @Override // com.shj.setting.widget.AbsItemView.EventListener
            public void saveButtonClick(Button button) {
                Loger.writeLog("SET", "点击了：" + SettingNote.this.getSettingName() + ":" + button.getText().toString());
                SettingNote.this.saveSetting(false);
            }

            @Override // com.shj.setting.widget.AbsItemView.EventListener
            public void clearButtonClick(Button button) {
                Loger.writeLog("SET", "点击了：" + SettingNote.this.getSettingName() + "：" + button.getText().toString());
                SettingNote.this.clearData();
            }

            @Override // com.shj.setting.widget.AbsItemView.EventListener
            public void queryButtonClick(Button button) {
                Loger.writeLog("SET", "点击了：" + SettingNote.this.getSettingName() + "：" + button.getText().toString());
                SettingNote.this.querySettingData();
            }

            @Override // com.shj.setting.widget.AbsItemView.EventListener
            public void onAttachedToWindow() {
                try {
                    SettingNote.this.onAttached();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.shj.setting.widget.AbsItemView.EventListener
            public void onDetachedFromWindow() {
                try {
                    Shj.getInstance(context);
                    Shj.clearCommandMap();
                    SettingNote.this.onDetached();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.generator.SettingNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements AbsItemView.EventListener {
        final /* synthetic */ Context val$context;

        AnonymousClass1(Context context2) {
            context = context2;
        }

        @Override // com.shj.setting.widget.AbsItemView.EventListener
        public void saveButtonClick(Button button) {
            Loger.writeLog("SET", "点击了：" + SettingNote.this.getSettingName() + ":" + button.getText().toString());
            SettingNote.this.saveSetting(false);
        }

        @Override // com.shj.setting.widget.AbsItemView.EventListener
        public void clearButtonClick(Button button) {
            Loger.writeLog("SET", "点击了：" + SettingNote.this.getSettingName() + "：" + button.getText().toString());
            SettingNote.this.clearData();
        }

        @Override // com.shj.setting.widget.AbsItemView.EventListener
        public void queryButtonClick(Button button) {
            Loger.writeLog("SET", "点击了：" + SettingNote.this.getSettingName() + "：" + button.getText().toString());
            SettingNote.this.querySettingData();
        }

        @Override // com.shj.setting.widget.AbsItemView.EventListener
        public void onAttachedToWindow() {
            try {
                SettingNote.this.onAttached();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.shj.setting.widget.AbsItemView.EventListener
        public void onDetachedFromWindow() {
            try {
                Shj.getInstance(context);
                Shj.clearCommandMap();
                SettingNote.this.onDetached();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getSettingName() {
        return SettingTypeName.getSettingName(this.context, this.settingType);
    }

    public void setCompeleteListener(CompeleteListener compeleteListener) {
        this.compeleteListener = compeleteListener;
    }
}
