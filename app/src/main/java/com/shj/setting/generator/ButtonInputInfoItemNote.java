package com.shj.setting.generator;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.event.GetMenuDateEvent;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.ButtonInputInfoItemView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes2.dex */
public class ButtonInputInfoItemNote extends SettingNote {
    private ButtonInputInfoItemView buttonInputInfoItemView;

    public ButtonInputInfoItemNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        if (this.settingType != 107) {
            return;
        }
        String inputString = this.buttonInputInfoItemView.getInputString();
        if (!TextUtils.isEmpty(inputString)) {
            AppSetting.saveActivityWebsite(this.context, inputString, null);
        }
        if (z) {
            return;
        }
        ToastUitl.showSaveSuccessTip(this.context);
    }

    private String getTipInfo() {
        if (this.settingType != 194) {
            return "";
        }
        String string = this.context.getResources().getString(R.string.please_input);
        if (CommonTool.getLanguage(this.context).equals("en") || CommonTool.getLanguage(this.context).equals("fr")) {
            string = string + StringUtils.SPACE;
        }
        return string + this.context.getResources().getString(R.string.cargo_number);
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        ButtonInputInfoItemView.ButtonInputInfoData buttonInputInfoData = new ButtonInputInfoItemView.ButtonInputInfoData();
        buttonInputInfoData.name = getSettingName();
        buttonInputInfoData.tipInfo = getTipInfo();
        ButtonInputInfoItemView buttonInputInfoItemView = new ButtonInputInfoItemView(this.context, buttonInputInfoData);
        this.buttonInputInfoItemView = buttonInputInfoItemView;
        buttonInputInfoItemView.setTitle(getSettingName());
        this.buttonInputInfoItemView.setEventListener(this.eventListener);
        this.buttonInputInfoItemView.setAlwaysNotDisplaySaveButton();
        setEditeTypeInterger();
        setEditText();
        setListener();
        return this.buttonInputInfoItemView;
    }

    private void setEditeTypeInterger() {
        if (this.settingType != 194) {
            return;
        }
        this.buttonInputInfoItemView.setEditeTypeInterger();
    }

    private void setEditText() {
        if (this.settingType != 107) {
            return;
        }
        String activityWebsite = AppSetting.getActivityWebsite(this.context, null);
        if (TextUtils.isEmpty(activityWebsite)) {
            this.buttonInputInfoItemView.setEditText("http://120.24.83.75:20090/");
        } else {
            this.buttonInputInfoItemView.setEditText(activityWebsite);
        }
    }

    /* renamed from: com.shj.setting.generator.ButtonInputInfoItemNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
            int i = ButtonInputInfoItemNote.this.settingType;
            if (i == 107) {
                String inputString = ButtonInputInfoItemNote.this.buttonInputInfoItemView.getInputString();
                if (!ButtonInputInfoItemNote.this.isUrl(inputString)) {
                    ToastUitl.showNotInputTip(ButtonInputInfoItemNote.this.context);
                    return;
                }
                Intent intent = new Intent("android.intent.action.web");
                intent.putExtra(IjkMediaPlayer.OnNativeInvokeListener.ARG_URL, inputString);
                ButtonInputInfoItemNote.this.context.startActivity(intent);
                return;
            }
            if (i != 194) {
                return;
            }
            String inputString2 = ButtonInputInfoItemNote.this.buttonInputInfoItemView.getInputString();
            if (!TextUtils.isEmpty(inputString2)) {
                int intValue = Integer.valueOf(inputString2).intValue();
                if (Shj.getShelfInfo(Integer.valueOf(intValue)) == null) {
                    ToastUitl.showShort(ButtonInputInfoItemNote.this.context, R.string.not_exist_shelf);
                    return;
                }
                CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand.setSales_Shelf(false, intValue);
                Shj.getInstance(ButtonInputInfoItemNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.ButtonInputInfoItemNote.1.1
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z) {
                    }

                    C00631() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        if (!bArr.toString().equals("0")) {
                            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 4);
                            int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 4, 4);
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(String.valueOf(intFromBytes));
                            arrayList.add(String.valueOf(intFromBytes2 / 100.0f));
                            EventBus.getDefault().post(new GetMenuDateEvent(71, arrayList));
                            return;
                        }
                        ToastUitl.showQueryCompeleteTip(ButtonInputInfoItemNote.this.context, false, ButtonInputInfoItemNote.this.getSettingName());
                    }
                });
                return;
            }
            ToastUitl.showNotInputTip(ButtonInputInfoItemNote.this.context);
        }

        /* renamed from: com.shj.setting.generator.ButtonInputInfoItemNote$1$1 */
        /* loaded from: classes2.dex */
        class C00631 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            C00631() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (!bArr.toString().equals("0")) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 4);
                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 4, 4);
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(String.valueOf(intFromBytes));
                    arrayList.add(String.valueOf(intFromBytes2 / 100.0f));
                    EventBus.getDefault().post(new GetMenuDateEvent(71, arrayList));
                    return;
                }
                ToastUitl.showQueryCompeleteTip(ButtonInputInfoItemNote.this.context, false, ButtonInputInfoItemNote.this.getSettingName());
            }
        }
    }

    private void setListener() {
        this.buttonInputInfoItemView.setButtonOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.generator.ButtonInputInfoItemNote.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
                int i = ButtonInputInfoItemNote.this.settingType;
                if (i == 107) {
                    String inputString = ButtonInputInfoItemNote.this.buttonInputInfoItemView.getInputString();
                    if (!ButtonInputInfoItemNote.this.isUrl(inputString)) {
                        ToastUitl.showNotInputTip(ButtonInputInfoItemNote.this.context);
                        return;
                    }
                    Intent intent = new Intent("android.intent.action.web");
                    intent.putExtra(IjkMediaPlayer.OnNativeInvokeListener.ARG_URL, inputString);
                    ButtonInputInfoItemNote.this.context.startActivity(intent);
                    return;
                }
                if (i != 194) {
                    return;
                }
                String inputString2 = ButtonInputInfoItemNote.this.buttonInputInfoItemView.getInputString();
                if (!TextUtils.isEmpty(inputString2)) {
                    int intValue = Integer.valueOf(inputString2).intValue();
                    if (Shj.getShelfInfo(Integer.valueOf(intValue)) == null) {
                        ToastUitl.showShort(ButtonInputInfoItemNote.this.context, R.string.not_exist_shelf);
                        return;
                    }
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                    commandV2_Up_SetCommand.setSales_Shelf(false, intValue);
                    Shj.getInstance(ButtonInputInfoItemNote.this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.ButtonInputInfoItemNote.1.1
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z) {
                        }

                        C00631() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                            if (!bArr.toString().equals("0")) {
                                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 4);
                                int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 4, 4);
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(String.valueOf(intFromBytes));
                                arrayList.add(String.valueOf(intFromBytes2 / 100.0f));
                                EventBus.getDefault().post(new GetMenuDateEvent(71, arrayList));
                                return;
                            }
                            ToastUitl.showQueryCompeleteTip(ButtonInputInfoItemNote.this.context, false, ButtonInputInfoItemNote.this.getSettingName());
                        }
                    });
                    return;
                }
                ToastUitl.showNotInputTip(ButtonInputInfoItemNote.this.context);
            }

            /* renamed from: com.shj.setting.generator.ButtonInputInfoItemNote$1$1 */
            /* loaded from: classes2.dex */
            class C00631 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                C00631() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (!bArr.toString().equals("0")) {
                        int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 4);
                        int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 4, 4);
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(String.valueOf(intFromBytes));
                        arrayList.add(String.valueOf(intFromBytes2 / 100.0f));
                        EventBus.getDefault().post(new GetMenuDateEvent(71, arrayList));
                        return;
                    }
                    ToastUitl.showQueryCompeleteTip(ButtonInputInfoItemNote.this.context, false, ButtonInputInfoItemNote.this.getSettingName());
                }
            }
        });
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.buttonInputInfoItemView;
    }

    public boolean isUrl(String str) {
        return Pattern.compile("((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,6})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?", 2).matcher(str).matches();
    }
}
