package com.shj.setting.generator;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.SelectWorkTimeDialog;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.TextItemView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class LightingControlNote extends SettingNote {
    private LoadingDialog loadingDialog;
    private TextItemView textItemView;

    public LightingControlNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        String charSequence = this.textItemView.getTextValue().getText().toString();
        if (!TextUtils.isEmpty(charSequence)) {
            String[] split = charSequence.split("-");
            if (!isRightInput(charSequence)) {
                ToastUitl.showErrorInputTip(this.context, "(20-8)");
                return;
            }
            int intValue = Integer.valueOf(split[0]).intValue();
            int intValue2 = Integer.valueOf(split[1]).intValue();
            if (intValue < 0 || intValue > 24 || intValue2 < 0 || intValue2 > 24) {
                ToastUitl.showErrorInputTip(this.context, "(20-8)");
                return;
            }
            LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.saveing);
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            if (this.settingType == 318) {
                commandV2_Up_SetCommand.setTopLight(true, intValue, intValue2);
            } else {
                commandV2_Up_SetCommand.setLight(true, intValue, intValue2);
            }
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.LightingControlNote.1
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                    ToastUitl.showSetCompeleteTip(LightingControlNote.this.context, z2, String.format(LightingControlNote.this.context.getResources().getString(R.string.working_time), ""));
                    LightingControlNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        ToastUitl.showNotInputTip(this.context);
    }

    /* renamed from: com.shj.setting.generator.LightingControlNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(LightingControlNote.this.context, z2, String.format(LightingControlNote.this.context.getResources().getString(R.string.working_time), ""));
            LightingControlNote.this.loadingDialog.dismiss();
        }
    }

    public boolean isRightInput(String str) {
        return Pattern.compile("[0-9]{1,2}-[0-9]{1,2}").matcher(str).matches();
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        super.querySettingData();
        LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.on_querying);
        this.loadingDialog = loadingDialog;
        loadingDialog.show();
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        if (this.settingType == 318) {
            commandV2_Up_SetCommand.setTopLight(false, 0, 0);
        } else {
            commandV2_Up_SetCommand.setLight(false, 0, 0);
        }
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.LightingControlNote.2
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass2() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length > 1) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 1, 1);
                    LightingControlNote.this.setWorkingTime(intFromBytes + "-" + intFromBytes2);
                } else {
                    ToastUitl.showLong(LightingControlNote.this.context, R.string.qurey_fail);
                }
                LightingControlNote.this.loadingDialog.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.generator.LightingControlNote$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass2() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 1) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 1, 1);
                LightingControlNote.this.setWorkingTime(intFromBytes + "-" + intFromBytes2);
            } else {
                ToastUitl.showLong(LightingControlNote.this.context, R.string.qurey_fail);
            }
            LightingControlNote.this.loadingDialog.dismiss();
        }
    }

    public void setWorkingTime(String str) {
        this.textItemView.setTextValue(str);
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        TextItemView.TextItemData textItemData = new TextItemView.TextItemData();
        textItemData.name = String.format(this.context.getResources().getString(R.string.working_time), "(h)");
        textItemData.value = "";
        TextItemView textItemView = new TextItemView(this.context, textItemData);
        this.textItemView = textItemView;
        textItemView.setTitle(getSettingName());
        this.textItemView.setTitleVisibility(0);
        this.textItemView.setQueryButtonVIsibility(0);
        this.textItemView.setEventListener(this.eventListener);
        setListener();
        return this.textItemView;
    }

    /* renamed from: com.shj.setting.generator.LightingControlNote$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int i;
            String charSequence = LightingControlNote.this.textItemView.getTextValue().getText().toString();
            int i2 = 0;
            if (TextUtils.isEmpty(charSequence)) {
                i = 0;
            } else {
                String[] split = charSequence.split("-");
                i2 = Integer.valueOf(split[0]).intValue();
                i = Integer.valueOf(split[1]).intValue();
            }
            SelectWorkTimeDialog selectWorkTimeDialog = new SelectWorkTimeDialog(LightingControlNote.this.context, String.valueOf(i2), String.valueOf(i));
            selectWorkTimeDialog.setButtonListener(new SelectWorkTimeDialog.ButtonListener() { // from class: com.shj.setting.generator.LightingControlNote.3.1
                AnonymousClass1() {
                }

                @Override // com.shj.setting.Dialog.SelectWorkTimeDialog.ButtonListener
                public void buttonClick_OK(String str, String str2) {
                    LightingControlNote.this.setWorkingTime(str + "-" + str2);
                }
            });
            selectWorkTimeDialog.show();
        }

        /* renamed from: com.shj.setting.generator.LightingControlNote$3$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements SelectWorkTimeDialog.ButtonListener {
            AnonymousClass1() {
            }

            @Override // com.shj.setting.Dialog.SelectWorkTimeDialog.ButtonListener
            public void buttonClick_OK(String str, String str2) {
                LightingControlNote.this.setWorkingTime(str + "-" + str2);
            }
        }
    }

    private void setListener() {
        this.textItemView.getTextValue().setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.generator.LightingControlNote.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int i;
                String charSequence = LightingControlNote.this.textItemView.getTextValue().getText().toString();
                int i2 = 0;
                if (TextUtils.isEmpty(charSequence)) {
                    i = 0;
                } else {
                    String[] split = charSequence.split("-");
                    i2 = Integer.valueOf(split[0]).intValue();
                    i = Integer.valueOf(split[1]).intValue();
                }
                SelectWorkTimeDialog selectWorkTimeDialog = new SelectWorkTimeDialog(LightingControlNote.this.context, String.valueOf(i2), String.valueOf(i));
                selectWorkTimeDialog.setButtonListener(new SelectWorkTimeDialog.ButtonListener() { // from class: com.shj.setting.generator.LightingControlNote.3.1
                    AnonymousClass1() {
                    }

                    @Override // com.shj.setting.Dialog.SelectWorkTimeDialog.ButtonListener
                    public void buttonClick_OK(String str, String str2) {
                        LightingControlNote.this.setWorkingTime(str + "-" + str2);
                    }
                });
                selectWorkTimeDialog.show();
            }

            /* renamed from: com.shj.setting.generator.LightingControlNote$3$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements SelectWorkTimeDialog.ButtonListener {
                AnonymousClass1() {
                }

                @Override // com.shj.setting.Dialog.SelectWorkTimeDialog.ButtonListener
                public void buttonClick_OK(String str, String str2) {
                    LightingControlNote.this.setWorkingTime(str + "-" + str2);
                }
            }
        });
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.textItemView;
    }
}
