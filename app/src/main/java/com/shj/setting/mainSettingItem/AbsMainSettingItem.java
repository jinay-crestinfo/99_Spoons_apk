package com.shj.setting.mainSettingItem;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.shj.setting.R;
import com.shj.setting.generator.Generator;
import com.shj.setting.generator.SettingNote;
import com.shj.setting.generator.TwoLevelMenuNote;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.OutItemView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class AbsMainSettingItem {
    public Button bt_save;
    public Button bt_save_signle;
    protected Context context;
    protected boolean isSelect;
    public boolean isShowCommandItem;
    public boolean isShowSignleSaveButton;
    protected UserSettingDao mUserSettingDao;
    public List<SettingNote> settingNoteList;

    /* loaded from: classes2.dex */
    public static class MainSettingView {
        public View contentView;
        public View menuView;
    }

    public abstract int getImageId();

    public abstract int getMainSettingType();

    public abstract String getName();

    public abstract boolean isShowAllSaveButton();

    public AbsMainSettingItem(Context context, UserSettingDao userSettingDao, boolean z) {
        this.context = context;
        this.mUserSettingDao = userSettingDao;
        this.isShowCommandItem = z;
    }

    public List<Integer> getChildTypeList() {
        return Generator.getSettingTypeList(this.context, getMainSettingType(), this.mUserSettingDao);
    }

    public MainSettingView getView() {
        MainSettingView mainSettingView = new MainSettingView();
        OutItemView outItemView = new OutItemView(this.context);
        this.bt_save = outItemView.getSaveButton();
        this.bt_save_signle = outItemView.getSaveSignleButton();
        setListener();
        LinearLayout centerContentView = outItemView.getCenterContentView();
        this.settingNoteList = Generator.getSettingNoteList(this.context, getMainSettingType(), this.mUserSettingDao, this.isShowCommandItem);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.height = 1;
        Iterator<SettingNote> it = this.settingNoteList.iterator();
        while (it.hasNext()) {
            View view = it.next().getView(-1);
            if (view != null) {
                centerContentView.addView(view);
                View view2 = new View(this.context);
                view2.setBackgroundColor(this.context.getResources().getColor(R.color.color_setting_tip));
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
        if (this.settingNoteList.size() > 0) {
            Iterator<SettingNote> it2 = this.settingNoteList.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                SettingNote next = it2.next();
                if (next instanceof TwoLevelMenuNote) {
                    mainSettingView.menuView = ((TwoLevelMenuNote) next).getMenuView();
                    break;
                }
            }
        }
        return mainSettingView;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }

    /* renamed from: com.shj.setting.mainSettingItem.AbsMainSettingItem$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (AbsMainSettingItem.this.settingNoteList != null) {
                Iterator<SettingNote> it = AbsMainSettingItem.this.settingNoteList.iterator();
                while (it.hasNext()) {
                    it.next().saveSetting(true);
                }
            }
        }
    }

    public void setListener() {
        this.bt_save.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.mainSettingItem.AbsMainSettingItem.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (AbsMainSettingItem.this.settingNoteList != null) {
                    Iterator<SettingNote> it = AbsMainSettingItem.this.settingNoteList.iterator();
                    while (it.hasNext()) {
                        it.next().saveSetting(true);
                    }
                }
            }
        });
        this.bt_save_signle.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.mainSettingItem.AbsMainSettingItem.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (AbsMainSettingItem.this.settingNoteList != null) {
                    int i = 0;
                    if (AbsMainSettingItem.this.isShowSignleSaveButton) {
                        AbsMainSettingItem.this.isShowSignleSaveButton = false;
                        AbsMainSettingItem.this.bt_save_signle.setText(R.string.save_setting_single_show);
                        i = 8;
                    } else {
                        AbsMainSettingItem.this.isShowSignleSaveButton = true;
                        AbsMainSettingItem.this.bt_save_signle.setText(R.string.save_setting_single_hide);
                    }
                    Iterator<SettingNote> it = AbsMainSettingItem.this.settingNoteList.iterator();
                    while (it.hasNext()) {
                        AbsItemView absItemView = it.next().getAbsItemView();
                        if (absItemView != null) {
                            absItemView.setSaveButtonVisibility(i);
                        }
                    }
                }
            }
        });
    }

    /* renamed from: com.shj.setting.mainSettingItem.AbsMainSettingItem$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (AbsMainSettingItem.this.settingNoteList != null) {
                int i = 0;
                if (AbsMainSettingItem.this.isShowSignleSaveButton) {
                    AbsMainSettingItem.this.isShowSignleSaveButton = false;
                    AbsMainSettingItem.this.bt_save_signle.setText(R.string.save_setting_single_show);
                    i = 8;
                } else {
                    AbsMainSettingItem.this.isShowSignleSaveButton = true;
                    AbsMainSettingItem.this.bt_save_signle.setText(R.string.save_setting_single_hide);
                }
                Iterator<SettingNote> it = AbsMainSettingItem.this.settingNoteList.iterator();
                while (it.hasNext()) {
                    AbsItemView absItemView = it.next().getAbsItemView();
                    if (absItemView != null) {
                        absItemView.setSaveButtonVisibility(i);
                    }
                }
            }
        }
    }

    public void setAllSaveInvisiblity() {
        this.bt_save_signle.performClick();
        this.bt_save.setVisibility(8);
        this.bt_save_signle.setVisibility(8);
    }
}
