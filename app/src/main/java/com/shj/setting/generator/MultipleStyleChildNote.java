package com.shj.setting.generator;

import android.content.Context;
import android.view.View;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.MultipleStyleChildItemView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class MultipleStyleChildNote extends SettingNote {
    private boolean isShowCommandItem;
    private MultipleStyleChildItemView multipleStyleChildItemView;
    private List<SettingNote> settingNoteList;

    public MultipleStyleChildNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    public void setIsShowCommandItem(boolean z) {
        this.isShowCommandItem = z;
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        List<SettingNote> list = this.settingNoteList;
        if (list != null) {
            Iterator<SettingNote> it = list.iterator();
            while (it.hasNext()) {
                it.next().saveSetting(true);
            }
        }
        if (z) {
            return;
        }
        int i = this.settingType;
        if (i == 106 || i == 109 || i == 112) {
            ToastUitl.showSaveSuccessTip(this.context);
        }
    }

    public void showChild(int i) {
        this.settingNoteList = Generator.getTwoLevelNoteList(this.context, this.mainSettingType, this.settingType, this.mUserSettingDao, this.isShowCommandItem);
        this.multipleStyleChildItemView.clearContentView();
        Iterator<SettingNote> it = this.settingNoteList.iterator();
        while (it.hasNext()) {
            this.multipleStyleChildItemView.addContentView(it.next().getView(i));
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        MultipleStyleChildItemView multipleStyleChildItemView = new MultipleStyleChildItemView(this.context);
        this.multipleStyleChildItemView = multipleStyleChildItemView;
        multipleStyleChildItemView.setTitle(getSettingName());
        this.multipleStyleChildItemView.setTitleVisibility(0);
        this.multipleStyleChildItemView.setEventListener(this.eventListener);
        showChild(i);
        setSaveButtonVisibility();
        return this.multipleStyleChildItemView;
    }

    private void setSaveButtonVisibility() {
        int i = this.settingType;
        if (i == 112 || i == 164 || i == 182 || i == 187 || i == 300 || i == 346) {
            this.multipleStyleChildItemView.setAlwaysNotDisplaySaveButton();
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.multipleStyleChildItemView;
    }
}
