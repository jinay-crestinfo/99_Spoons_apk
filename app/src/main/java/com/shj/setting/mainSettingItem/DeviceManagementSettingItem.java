package com.shj.setting.mainSettingItem;

import android.content.Context;
import com.shj.setting.R;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public class DeviceManagementSettingItem extends AbsMainSettingItem {
    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public int getMainSettingType() {
        return 4;
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public boolean isShowAllSaveButton() {
        return false;
    }

    public DeviceManagementSettingItem(Context context, UserSettingDao userSettingDao, boolean z) {
        super(context, userSettingDao, z);
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public String getName() {
        return this.context.getResources().getString(R.string.device_management);
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public int getImageId() {
        return R.drawable.device_management;
    }
}
