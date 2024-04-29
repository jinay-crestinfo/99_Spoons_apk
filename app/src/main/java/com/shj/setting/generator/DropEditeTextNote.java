package com.shj.setting.generator;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.event.SettingTypeEvent;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.DropEditeTextView;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSetting;
import com.xyshj.database.setting.UserSettingDao;
import org.greenrobot.eventbus.EventBus;

/* loaded from: classes2.dex */
public class DropEditeTextNote extends SettingNote {
    private DropEditeTextView dropEditeTextView;

    public DropEditeTextNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        if (this.settingType != 216) {
            return;
        }
        String settingKey = SettingType.getSettingKey(this.settingType);
        if (!TextUtils.isEmpty(settingKey)) {
            String editeText = this.dropEditeTextView.getEditeText();
            if (!TextUtils.isEmpty(editeText)) {
                this.mUserSettingDao.insert(new UserSetting(this.settingType, SettingType.getParentId(this.settingType), settingKey, editeText));
            } else {
                ToastUitl.showNotInputTip(this.context);
            }
        }
        EventBus.getDefault().post(new SettingTypeEvent(this.settingType, null));
        if (z) {
            return;
        }
        ToastUitl.showSaveSuccessTip(this.context);
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        DropEditeTextView dropEditeTextView = new DropEditeTextView(this.context, getDropEditTextData());
        this.dropEditeTextView = dropEditeTextView;
        dropEditeTextView.setEventListener(this.eventListener);
        this.dropEditeTextView.setTitleVisibility(8);
        setEditText();
        hideSaveButton();
        return this.dropEditeTextView;
    }

    private void setEditText() {
        UserSetting userSettingFormKey;
        String value;
        String settingKey = SettingType.getSettingKey(this.settingType);
        if (this.settingType != 216 || TextUtils.isEmpty(settingKey) || (userSettingFormKey = this.mUserSettingDao.getUserSettingFormKey(settingKey)) == null || (value = userSettingFormKey.getValue()) == null) {
            return;
        }
        this.dropEditeTextView.setEditeText(value);
    }

    private void hideSaveButton() {
        if (this.settingType != 216) {
            return;
        }
        this.dropEditeTextView.setAlwaysNotDisplaySaveButton();
        this.dropEditeTextView.setChangeListener(new DropEditeTextView.ChangeListener() { // from class: com.shj.setting.generator.DropEditeTextNote.1
            AnonymousClass1() {
            }

            @Override // com.shj.setting.widget.DropEditeTextView.ChangeListener
            public void change() {
                DropEditeTextNote.this.saveSetting(false);
            }
        });
    }

    /* renamed from: com.shj.setting.generator.DropEditeTextNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements DropEditeTextView.ChangeListener {
        AnonymousClass1() {
        }

        @Override // com.shj.setting.widget.DropEditeTextView.ChangeListener
        public void change() {
            DropEditeTextNote.this.saveSetting(false);
        }
    }

    private DropEditeTextView.DropEditTextData getDropEditTextData() {
        DropEditeTextView.DropEditTextData dropEditTextData = new DropEditeTextView.DropEditTextData();
        if (this.settingType == 216) {
            dropEditTextData.name = getSettingName();
            dropEditTextData.tipInfo = "";
            dropEditTextData.diopList = this.context.getResources().getStringArray(R.array.currency_symbol);
        }
        return dropEditTextData;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.dropEditeTextView;
    }
}
