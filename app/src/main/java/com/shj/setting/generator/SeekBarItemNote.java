package com.shj.setting.generator;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.oysb.utils.Loger;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.SeekBarItemView;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSetting;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public class SeekBarItemNote extends SettingNote {
    private SeekBarItemView seekBarItemView;

    public SeekBarItemNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        int i = this.settingType;
        if (i == 113 || i == 114) {
            String settingKey = SettingType.getSettingKey(this.settingType);
            if (TextUtils.isEmpty(settingKey)) {
                return;
            }
            String valueOf = String.valueOf(this.seekBarItemView.getProgress());
            Loger.writeLog("UI", getSettingName() + ":" + valueOf);
            this.mUserSettingDao.insert(new UserSetting(this.settingType, SettingType.getParentId(this.settingType), settingKey, valueOf));
            if (z) {
                return;
            }
            ToastUitl.showSaveSuccessTip(this.context);
        }
    }

    private void setProgress() {
        UserSetting userSettingFormKey;
        int i = this.settingType;
        if (i == 113 || i == 114) {
            String settingKey = SettingType.getSettingKey(this.settingType);
            if (TextUtils.isEmpty(settingKey) || (userSettingFormKey = this.mUserSettingDao.getUserSettingFormKey(settingKey)) == null) {
                return;
            }
            String value = userSettingFormKey.getValue();
            if (TextUtils.isEmpty(value)) {
                return;
            }
            this.seekBarItemView.setProgress(Integer.valueOf(value).intValue());
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        SeekBarItemView seekBarItemView = new SeekBarItemView(this.context, getSettingName());
        this.seekBarItemView = seekBarItemView;
        seekBarItemView.setEventListener(this.eventListener);
        setProgress();
        this.seekBarItemView.setChangeListener(new SeekBarItemView.ChangeListener() { // from class: com.shj.setting.generator.SeekBarItemNote.1
            AnonymousClass1() {
            }

            @Override // com.shj.setting.widget.SeekBarItemView.ChangeListener
            public void change() {
                SeekBarItemNote.this.saveSetting(false);
            }
        });
        return this.seekBarItemView;
    }

    /* renamed from: com.shj.setting.generator.SeekBarItemNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements SeekBarItemView.ChangeListener {
        AnonymousClass1() {
        }

        @Override // com.shj.setting.widget.SeekBarItemView.ChangeListener
        public void change() {
            SeekBarItemNote.this.saveSetting(false);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.seekBarItemView;
    }
}
