package com.shj.setting.generator;

import android.content.Context;
import android.view.View;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.LogLookNoteView;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public class LogLookNote extends SettingNote {
    private int cabinetNumber;
    private LogLookNoteView logLookNoteView;

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
    }

    public LogLookNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        LogLookNoteView logLookNoteView = new LogLookNoteView(this.context);
        this.logLookNoteView = logLookNoteView;
        logLookNoteView.setEventListener(this.eventListener);
        this.logLookNoteView.setAlwaysNotDisplaySaveButton();
        return this.logLookNoteView;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.logLookNoteView;
    }
}
