package com.shj.setting.mainSettingItem;

import android.content.Context;
import com.shj.setting.R;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public class LogLookSettingItem extends AbsMainSettingItem {
    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public int getMainSettingType() {
        return 26;
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public boolean isShowAllSaveButton() {
        return false;
    }

    public LogLookSettingItem(Context context, UserSettingDao userSettingDao, boolean z) {
        super(context, userSettingDao, z);
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public String getName() {
        return this.context.getResources().getString(R.string.log_look);
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public int getImageId() {
        return R.drawable.basics;
    }
}
