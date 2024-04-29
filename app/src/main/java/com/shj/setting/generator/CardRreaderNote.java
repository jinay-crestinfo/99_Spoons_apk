package com.shj.setting.generator;

import android.content.Context;
import com.shj.setting.R;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class CardRreaderNote extends BaseDeviceNote {
    @Override // com.shj.setting.generator.BaseDeviceNote
    protected List<String> getAccessLocationNameList() {
        return null;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected String getBaudRate() {
        return "111";
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected List<String> getProtocolVersionNumberList() {
        return null;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected String getVersionNumber() {
        return null;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected boolean isHaveEnableItem() {
        return true;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected boolean isHaveSerialPortSearch() {
        return true;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected boolean isHaveWorkingTime() {
        return false;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected boolean isShow() {
        return true;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected void queryData() {
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected void serchSerialPortClick() {
    }

    public CardRreaderNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected List<String> getManufacturerList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("厂家一");
        arrayList.add("厂家二");
        return arrayList;
    }

    @Override // com.shj.setting.generator.BaseDeviceNote
    protected String getTestButtonName() {
        return this.context.getResources().getString(R.string.reading_card_test);
    }
}
