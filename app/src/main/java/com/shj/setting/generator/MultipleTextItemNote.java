package com.shj.setting.generator;

import android.content.Context;
import android.view.View;
import com.shj.setting.R;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.MutipleTextItemView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MultipleTextItemNote extends SettingNote {
    private MutipleTextItemView mutipleTextItemView;

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
    }

    public MultipleTextItemNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    private List<String> getNameList() {
        ArrayList arrayList = new ArrayList();
        if (this.settingType == 148) {
            arrayList.add(this.context.getResources().getString(R.string.time_over));
            arrayList.add(this.context.getResources().getString(R.string.time_stop));
        }
        return arrayList;
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        List<String> nameList = getNameList();
        MutipleTextItemView mutipleTextItemView = new MutipleTextItemView(this.context, nameList);
        this.mutipleTextItemView = mutipleTextItemView;
        mutipleTextItemView.setTitle(getSettingName());
        this.mutipleTextItemView.setEventListener(this.eventListener);
        if (nameList.size() > 1) {
            this.mutipleTextItemView.setTitleVisibility(0);
        }
        return this.mutipleTextItemView;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.mutipleTextItemView;
    }
}
