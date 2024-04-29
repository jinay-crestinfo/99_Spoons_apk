package com.shj.setting.mainSettingItem;

import android.content.Context;
import com.shj.setting.R;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public class SystemSetupSettingItem extends AbsMainSettingItem {
    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public int getMainSettingType() {
        return 5;
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public boolean isShowAllSaveButton() {
        return false;
    }

    public SystemSetupSettingItem(Context context, UserSettingDao userSettingDao, boolean z) {
        super(context, userSettingDao, z);
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public String getName() {
        return this.context.getResources().getString(R.string.system_setup);
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public int getImageId() {
        return R.drawable.system_setup;
    }
}
