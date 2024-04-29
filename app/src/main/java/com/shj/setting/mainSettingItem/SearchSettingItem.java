package com.shj.setting.mainSettingItem;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.shj.setting.R;
import com.shj.setting.generator.Generator;
import com.shj.setting.generator.SettingNote;
import com.shj.setting.generator.TwoLevelMenuNote;
import com.shj.setting.mainSettingItem.AbsMainSettingItem;
import com.shj.setting.widget.OutItemView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class SearchSettingItem extends AbsMainSettingItem {
    private List<Integer> settingTypeList;

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public int getMainSettingType() {
        return 17;
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public boolean isShowAllSaveButton() {
        return false;
    }

    public SearchSettingItem(Context context, UserSettingDao userSettingDao, boolean z) {
        super(context, userSettingDao, z);
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public String getName() {
        return this.context.getResources().getString(R.string.search);
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public int getImageId() {
        return R.drawable.other_setting;
    }

    public void setSettingTypeList(List<Integer> list) {
        this.settingTypeList = list;
    }

    public List<Integer> gettingTypeList() {
        return this.settingTypeList;
    }

    @Override // com.shj.setting.mainSettingItem.AbsMainSettingItem
    public AbsMainSettingItem.MainSettingView getView() {
        AbsMainSettingItem.MainSettingView mainSettingView = new AbsMainSettingItem.MainSettingView();
        OutItemView outItemView = new OutItemView(this.context);
        this.bt_save = outItemView.getSaveButton();
        this.bt_save_signle = outItemView.getSaveSignleButton();
        setListener();
        LinearLayout centerContentView = outItemView.getCenterContentView();
        this.settingNoteList = Generator.getNoteList(this.context, this.settingTypeList, getMainSettingType(), this.mUserSettingDao, this.isShowCommandItem);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.height = 2;
        Iterator<SettingNote> it = this.settingNoteList.iterator();
        while (it.hasNext()) {
            View view = it.next().getView(-1);
            if (view != null) {
                centerContentView.addView(view);
                View view2 = new View(this.context);
                view2.setBackgroundColor(this.context.getResources().getColor(R.color.color_line));
                centerContentView.addView(view2, layoutParams);
            }
        }
        if (!isShowAllSaveButton()) {
            setAllSaveInvisiblity();
        } else {
            this.bt_save_signle.performClick();
        }
        this.isShowSignleSaveButton = false;
        mainSettingView.contentView = outItemView;
        if (this.settingNoteList.size() > 0 && (this.settingNoteList.get(0) instanceof TwoLevelMenuNote)) {
            mainSettingView.menuView = ((TwoLevelMenuNote) this.settingNoteList.get(0)).getMenuView();
        }
        return mainSettingView;
    }
}
