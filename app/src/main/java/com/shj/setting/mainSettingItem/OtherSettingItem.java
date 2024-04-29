package com.shj.setting.mainSettingItem;

import android.content.Context;
import com.shj.setting.R;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public class OtherSettingItem extends AbsMainSettingItem {
    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public int getMainSettingType() {
        return 6;
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public boolean isShowAllSaveButton() {
        return false;
    }

    public OtherSettingItem(Context context, UserSettingDao userSettingDao, boolean z) {
        super(context, userSettingDao, z);
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public String getName() {
        return this.context.getResources().getString(R.string.other_setting);
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public int getImageId() {
        return R.drawable.other_setting;
    }
}
