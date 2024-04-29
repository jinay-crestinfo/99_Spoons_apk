package com.shj.setting.generator;

import android.content.Context;
import android.view.View;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.PureTextItemView;
import com.xyshj.database.setting.UserSettingDao;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class PureTextNote extends SettingNote {
    private PureTextItemView pureTextItemView;

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
    }

    public PureTextNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    private void setText() {
        if (this.settingType != 283) {
            return;
        }
        int size = SettingActivity.getBasicMachineInfo().cabinetNumberList.size();
        String string = this.context.getString(R.string.number_cabinets);
        this.pureTextItemView.setText(string + ":" + String.valueOf(size) + StringUtils.LF + this.context.getString(R.string.number_cabinets_error_tip));
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        PureTextItemView pureTextItemView = new PureTextItemView(this.context);
        this.pureTextItemView = pureTextItemView;
        pureTextItemView.setEventListener(this.eventListener);
        this.pureTextItemView.setAlwaysNotDisplaySaveButton();
        setText();
        return this.pureTextItemView;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.pureTextItemView;
    }
}
