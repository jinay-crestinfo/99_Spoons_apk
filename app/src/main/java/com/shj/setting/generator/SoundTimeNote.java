package com.shj.setting.generator;

import android.content.Context;
import android.view.View;
import com.shj.setting.Dialog.TimeRangePickerDialog;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.SoundTimeView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.SoundTimeData;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public class SoundTimeNote extends SettingNote {
    private int cabinetNumber;
    private SoundTimeData soundTimeData;
    private SoundTimeView soundTimeView;

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
    }

    public SoundTimeNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        super.onAttached();
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        this.soundTimeData = getSoundTimeData();
        SoundTimeView soundTimeView = new SoundTimeView(this.context, getSettingName(), this.soundTimeData);
        this.soundTimeView = soundTimeView;
        soundTimeView.setEventListener(this.eventListener);
        this.soundTimeView.setAlwaysNotDisplaySaveButton();
        setListener();
        return this.soundTimeView;
    }

    /* renamed from: com.shj.setting.generator.SoundTimeNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String str;
            int i;
            SoundTimeData soundTimeData = SoundTimeNote.this.getSoundTimeData();
            if (soundTimeData != null) {
                str = soundTimeData.startTime + "-" + soundTimeData.endTime;
                i = soundTimeData.soundValue;
            } else {
                str = "00:00-00:00";
                i = 0;
            }
            new TimeRangePickerDialog(SoundTimeNote.this.context, str, i, new TimeRangePickerDialog.ConfirmAction() { // from class: com.shj.setting.generator.SoundTimeNote.1.1
                @Override // com.shj.setting.Dialog.TimeRangePickerDialog.ConfirmAction
                public void onLeftClick() {
                }

                C00661() {
                }

                @Override // com.shj.setting.Dialog.TimeRangePickerDialog.ConfirmAction
                public void onRightClick(String str2, String str3, int i2) {
                    SoundTimeData soundTimeData2 = new SoundTimeData();
                    soundTimeData2.startTime = str2;
                    soundTimeData2.endTime = str3;
                    soundTimeData2.soundValue = i2;
                    SoundTimeNote.this.saveSoundTimeData(soundTimeData2);
                    SoundTimeNote.this.soundTimeView.updataValue(soundTimeData2);
                    ToastUitl.showSaveSuccessTip(SoundTimeNote.this.context);
                }
            }).show();
        }

        /* renamed from: com.shj.setting.generator.SoundTimeNote$1$1 */
        /* loaded from: classes2.dex */
        class C00661 implements TimeRangePickerDialog.ConfirmAction {
            @Override // com.shj.setting.Dialog.TimeRangePickerDialog.ConfirmAction
            public void onLeftClick() {
            }

            C00661() {
            }

            @Override // com.shj.setting.Dialog.TimeRangePickerDialog.ConfirmAction
            public void onRightClick(String str2, String str3, int i2) {
                SoundTimeData soundTimeData2 = new SoundTimeData();
                soundTimeData2.startTime = str2;
                soundTimeData2.endTime = str3;
                soundTimeData2.soundValue = i2;
                SoundTimeNote.this.saveSoundTimeData(soundTimeData2);
                SoundTimeNote.this.soundTimeView.updataValue(soundTimeData2);
                ToastUitl.showSaveSuccessTip(SoundTimeNote.this.context);
            }
        }
    }

    private void setListener() {
        this.soundTimeView.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.generator.SoundTimeNote.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String str;
                int i;
                SoundTimeData soundTimeData = SoundTimeNote.this.getSoundTimeData();
                if (soundTimeData != null) {
                    str = soundTimeData.startTime + "-" + soundTimeData.endTime;
                    i = soundTimeData.soundValue;
                } else {
                    str = "00:00-00:00";
                    i = 0;
                }
                new TimeRangePickerDialog(SoundTimeNote.this.context, str, i, new TimeRangePickerDialog.ConfirmAction() { // from class: com.shj.setting.generator.SoundTimeNote.1.1
                    @Override // com.shj.setting.Dialog.TimeRangePickerDialog.ConfirmAction
                    public void onLeftClick() {
                    }

                    C00661() {
                    }

                    @Override // com.shj.setting.Dialog.TimeRangePickerDialog.ConfirmAction
                    public void onRightClick(String str2, String str3, int i2) {
                        SoundTimeData soundTimeData2 = new SoundTimeData();
                        soundTimeData2.startTime = str2;
                        soundTimeData2.endTime = str3;
                        soundTimeData2.soundValue = i2;
                        SoundTimeNote.this.saveSoundTimeData(soundTimeData2);
                        SoundTimeNote.this.soundTimeView.updataValue(soundTimeData2);
                        ToastUitl.showSaveSuccessTip(SoundTimeNote.this.context);
                    }
                }).show();
            }

            /* renamed from: com.shj.setting.generator.SoundTimeNote$1$1 */
            /* loaded from: classes2.dex */
            class C00661 implements TimeRangePickerDialog.ConfirmAction {
                @Override // com.shj.setting.Dialog.TimeRangePickerDialog.ConfirmAction
                public void onLeftClick() {
                }

                C00661() {
                }

                @Override // com.shj.setting.Dialog.TimeRangePickerDialog.ConfirmAction
                public void onRightClick(String str2, String str3, int i2) {
                    SoundTimeData soundTimeData2 = new SoundTimeData();
                    soundTimeData2.startTime = str2;
                    soundTimeData2.endTime = str3;
                    soundTimeData2.soundValue = i2;
                    SoundTimeNote.this.saveSoundTimeData(soundTimeData2);
                    SoundTimeNote.this.soundTimeView.updataValue(soundTimeData2);
                    ToastUitl.showSaveSuccessTip(SoundTimeNote.this.context);
                }
            }
        });
    }

    public SoundTimeData getSoundTimeData() {
        SoundTimeData soundTimeData = null;
        switch (this.settingType) {
            case SettingType.SOUND_SETTING_ADVERTISEMENT_TIME1 /* 284 */:
                soundTimeData = AppSetting.getSoundSettingAdvertisementTime1(this.context, null);
                break;
            case SettingType.SOUND_SETTING_ADVERTISEMENT_TIME2 /* 285 */:
                soundTimeData = AppSetting.getSoundSettingAdvertisementTime2(this.context, null);
                break;
            case SettingType.SOUND_SETTING_VOICE_TIME1 /* 286 */:
                soundTimeData = AppSetting.getSoundSettingVoiceTime1(this.context, null);
                break;
            case SettingType.SOUND_SETTING_VOICE_TIME2 /* 287 */:
                soundTimeData = AppSetting.getSoundSettingVoiceTime2(this.context, null);
                break;
        }
        if (soundTimeData != null) {
            return soundTimeData;
        }
        SoundTimeData soundTimeData2 = new SoundTimeData();
        soundTimeData2.startTime = "00:00";
        soundTimeData2.endTime = "00:00";
        soundTimeData2.soundValue = 0;
        return soundTimeData2;
    }

    public void saveSoundTimeData(SoundTimeData soundTimeData) {
        switch (this.settingType) {
            case SettingType.SOUND_SETTING_ADVERTISEMENT_TIME1 /* 284 */:
                AppSetting.saveSoundSettingAdvertisementTime1(this.context, soundTimeData, null);
                return;
            case SettingType.SOUND_SETTING_ADVERTISEMENT_TIME2 /* 285 */:
                AppSetting.saveSoundSettingAdvertisementTime2(this.context, soundTimeData, null);
                return;
            case SettingType.SOUND_SETTING_VOICE_TIME1 /* 286 */:
                AppSetting.saveSoundSettingVoiceTime1(this.context, soundTimeData, null);
                return;
            case SettingType.SOUND_SETTING_VOICE_TIME2 /* 287 */:
                AppSetting.saveSoundSettingVoiceTime2(this.context, soundTimeData, null);
                return;
            default:
                return;
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.soundTimeView;
    }
}
