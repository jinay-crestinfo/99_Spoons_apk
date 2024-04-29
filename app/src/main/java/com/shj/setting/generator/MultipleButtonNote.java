package com.shj.setting.generator;

import android.content.Context;
import android.view.View;
import com.shj.ShelfInfo;
import com.shj.ShelfType;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.MultipleButtonView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class MultipleButtonNote extends SettingNote {
    private int cabinetNumber;
    private MultipleButtonView multipleButtonView;

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
    }

    public MultipleButtonNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        List<Integer> shelves = Shj.getShelves();
        ArrayList arrayList = new ArrayList();
        Iterator<Integer> it = shelves.iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
            if (shelfInfo != null && shelfInfo.getShelfType() == ShelfType.Box) {
                arrayList.add(Integer.valueOf(intValue));
            }
        }
        if (arrayList.size() == 0) {
            return null;
        }
        MultipleButtonView multipleButtonView = new MultipleButtonView(this.context, arrayList);
        this.multipleButtonView = multipleButtonView;
        multipleButtonView.setEventListener(this.eventListener);
        this.multipleButtonView.setAlwaysNotDisplaySaveButton();
        this.multipleButtonView.setTitle(getSettingName());
        this.multipleButtonView.setTitleVisibility(0);
        setListener();
        return this.multipleButtonView;
    }

    /* renamed from: com.shj.setting.generator.MultipleButtonNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements MultipleButtonView.ButtonClickListenr {
        AnonymousClass1() {
        }

        @Override // com.shj.setting.widget.MultipleButtonView.ButtonClickListenr
        public void onClick(int i) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.TestShelf(false, 1, i);
            Shj.getInstance(MultipleButtonNote.this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, null);
        }
    }

    private void setListener() {
        this.multipleButtonView.setButtonClickListenr(new MultipleButtonView.ButtonClickListenr() { // from class: com.shj.setting.generator.MultipleButtonNote.1
            AnonymousClass1() {
            }

            @Override // com.shj.setting.widget.MultipleButtonView.ButtonClickListenr
            public void onClick(int i) {
                CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand.TestShelf(false, 1, i);
                Shj.getInstance(MultipleButtonNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, null);
            }
        });
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.multipleButtonView;
    }
}
