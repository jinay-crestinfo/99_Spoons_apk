package com.shj.setting.generator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Event.BaseEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.event.SelectMainContolFileEvent;
import com.shj.setting.event.SelectShelfDrivingFileEvent;
import com.shj.setting.helper.LowerMachineProgramUpdate;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.QueryItemView;
import com.shj.setting.widget.SpinnerItemView;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSettingDao;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* loaded from: classes.dex */
public class QueryItemNote extends SettingNote {
    private QueryItemView queryItemView;

    public QueryItemNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        int i = this.settingType;
        if (i == 311) {
            String valueText = this.queryItemView.getValueText();
            if (TextUtils.isEmpty(valueText)) {
                return;
            }
            LowerMachineProgramUpdate.programUpdateMainControl(this.context, valueText, 255, 255);
            return;
        }
        if (i != 312) {
            switch (i) {
                case 160:
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                    commandV2_Up_SetCommand.setClearBlocks(true);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.1
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass1() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z2) {
                            if (z2) {
                                QueryItemNote.this.queryItemView.setValueText(("" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes) + 0 + StringUtils.LF) + QueryItemNote.this.context.getString(R.string.number_of_failure_channels) + 0);
                                ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_success);
                                return;
                            }
                            ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_fail);
                        }
                    });
                    return;
                case 161:
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                    commandV2_Up_SetCommand2.setClearEnginError(true);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.2
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass2() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z2) {
                            if (z2) {
                                QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.gear_box_fault_cargo) + 0);
                                ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_success);
                                return;
                            }
                            ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_fail);
                        }
                    });
                    return;
                case 162:
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
                    commandV2_Up_SetCommand3.setClearLiftError(true);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand3, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.3
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass3() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z2) {
                            if (z2) {
                                QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_lift_failures) + 0);
                                ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_success);
                                return;
                            }
                            ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_fail);
                        }
                    });
                    return;
                case 163:
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand4 = new CommandV2_Up_SetCommand();
                    commandV2_Up_SetCommand4.setClearBlock41s(true);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand4, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.4
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass4() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z2) {
                            if (z2) {
                                QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes) + 0);
                                ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_success);
                                return;
                            }
                            ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_fail);
                        }
                    });
                    return;
                default:
                    return;
            }
        }
        String valueText2 = this.queryItemView.getValueText();
        if (TextUtils.isEmpty(valueText2)) {
            return;
        }
        LowerMachineProgramUpdate.programUpdateCargoDrive(this.context, valueText2, 255, 255);
    }

    /* renamed from: com.shj.setting.generator.QueryItemNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            if (z2) {
                QueryItemNote.this.queryItemView.setValueText(("" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes) + 0 + StringUtils.LF) + QueryItemNote.this.context.getString(R.string.number_of_failure_channels) + 0);
                ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_success);
                return;
            }
            ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_fail);
        }
    }

    /* renamed from: com.shj.setting.generator.QueryItemNote$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass2() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            if (z2) {
                QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.gear_box_fault_cargo) + 0);
                ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_success);
                return;
            }
            ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_fail);
        }
    }

    /* renamed from: com.shj.setting.generator.QueryItemNote$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass3() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            if (z2) {
                QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_lift_failures) + 0);
                ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_success);
                return;
            }
            ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_fail);
        }
    }

    /* renamed from: com.shj.setting.generator.QueryItemNote$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass4() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            if (z2) {
                QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes) + 0);
                ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_success);
                return;
            }
            ToastUitl.showShort(QueryItemNote.this.context, R.string.clear_fail);
        }
    }

    private String getButtonName() {
        int i = this.settingType;
        if (i != 311 && i != 312) {
            switch (i) {
                case 160:
                    return this.context.getResources().getString(R.string.card_cargo_clearance_tip);
                case 161:
                    return this.context.getResources().getString(R.string.search_for_faulty_freight_tracks);
                case 162:
                    return this.context.getResources().getString(R.string.lift_fault_clearance_tip);
                case 163:
                    return this.context.getResources().getString(R.string.card_cargo_turn_to_4_circle_clearance_tip);
                default:
                    return getSettingName();
            }
        }
        return this.context.getResources().getString(R.string.select_file);
    }

    private void setTitleVisibility() {
        int i = this.settingType;
        if (i != 167 && i != 188) {
            if (i != 311 && i != 312) {
                switch (i) {
                    case 160:
                    case 161:
                    case 162:
                    case 163:
                        break;
                    default:
                        return;
                }
            }
            this.queryItemView.setTitleVisibility(0);
            return;
        }
        this.queryItemView.setTitleVisibility(8);
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        QueryItemView queryItemView = new QueryItemView(this.context, getButtonName(), getNewAddSpinnerItemData());
        this.queryItemView = queryItemView;
        queryItemView.setTitle(getSettingName());
        this.queryItemView.setEventListener(this.eventListener);
        setAlwaysNotDisplaySaveButton();
        setTitleVisibility();
        setSaveSettingText();
        setQueryListener();
        return this.queryItemView;
    }

    private SpinnerItemView.SpinnerItemData getNewAddSpinnerItemData() {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        if (this.settingType != 234) {
            return null;
        }
        spinnerItemData.name = this.context.getString(R.string.cabinet_number);
        spinnerItemData.dataList = SettingActivity.getBasicMachineInfo().cabinetNumberList;
        return spinnerItemData;
    }

    /* renamed from: com.shj.setting.generator.QueryItemNote$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
            int i = QueryItemNote.this.settingType;
            if (i == 167) {
                LoadingDialog loadingDialog = new LoadingDialog(QueryItemNote.this.context, R.string.on_querying);
                loadingDialog.show();
                CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand.setShelfCount();
                Shj.getInstance(QueryItemNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.5
                    final /* synthetic */ LoadingDialog val$loadingDialog;

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z) {
                    }

                    C00655(LoadingDialog loadingDialog2) {
                        loadingDialog = loadingDialog2;
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        int i2;
                        int i3;
                        if (bArr != null && bArr.length > 0) {
                            String string = QueryItemNote.this.context.getString(R.string.normal_number_of_cargo_paths);
                            String string2 = QueryItemNote.this.context.getString(R.string.short_circuit_number_of_motor);
                            int i4 = 0;
                            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                            int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 1, 1);
                            if (intFromBytes == 255 && intFromBytes2 == 255) {
                                int length = (bArr.length - 2) / 5;
                                i2 = 0;
                                i3 = 0;
                                while (i4 < length) {
                                    int i5 = (i4 * 5) + 2;
                                    i2 += ObjectHelper.intFromBytes(bArr, i5 + 1, 2);
                                    i3 += ObjectHelper.intFromBytes(bArr, i5 + 3, 2);
                                    i4++;
                                }
                            } else {
                                int length2 = (bArr.length - 1) / 3;
                                int i6 = 0;
                                int i7 = 0;
                                while (i4 < length2) {
                                    int i8 = (i4 * 3) + 1;
                                    i6 += ObjectHelper.intFromBytes(bArr, i8 + 1, 1);
                                    i7 += ObjectHelper.intFromBytes(bArr, i8 + 2, 1);
                                    i4++;
                                }
                                i2 = i6;
                                i3 = i7;
                            }
                            QueryItemNote.this.queryItemView.setValueText(string + i2 + StringUtils.LF + string2 + i3);
                        } else {
                            ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                        }
                        loadingDialog.dismiss();
                    }
                });
                return;
            }
            if (i == 188) {
                CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand2.queryCoinBalance();
                Shj.getInstance(QueryItemNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.6
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z) {
                    }

                    AnonymousClass6() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        if (!bArr.toString().equals("0")) {
                            if (ObjectHelper.intFromBytes(bArr, 0, 1) != 0) {
                                QueryItemNote.this.queryItemView.setValueText(QueryItemNote.this.context.getString(R.string.coin_changer_error));
                                return;
                            } else {
                                QueryItemNote.this.queryItemView.setValueText(String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 4)));
                                return;
                            }
                        }
                        ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                    }
                });
                return;
            }
            if (i != 234) {
                switch (i) {
                    case 160:
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand3.setClearBlocks(false);
                        Shj.getInstance(QueryItemNote.this.context);
                        Shj.postSetCommand(commandV2_Up_SetCommand3, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.1
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z) {
                            }

                            AnonymousClass1() {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                                String str;
                                if (!bArr.toString().equals("0")) {
                                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                                    String str2 = "" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes);
                                    if (intFromBytes == 0) {
                                        str2 = str2 + QueryItemNote.this.context.getString(R.string.no_have);
                                    } else {
                                        for (int i2 = 0; i2 < intFromBytes; i2++) {
                                            str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i2 * 2) + 1, 2));
                                            if (i2 != intFromBytes - 1) {
                                                str2 = str2 + ",";
                                            }
                                        }
                                    }
                                    String str3 = str2 + StringUtils.LF;
                                    int i3 = intFromBytes * 2;
                                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, i3 + 1, 1);
                                    String str4 = str3 + QueryItemNote.this.context.getString(R.string.number_of_failure_channels);
                                    if (intFromBytes2 == 0) {
                                        str = str4 + QueryItemNote.this.context.getString(R.string.no_have);
                                    } else {
                                        for (int i4 = 0; i4 < intFromBytes2; i4++) {
                                            str4 = str4 + String.valueOf(ObjectHelper.intFromBytes(bArr, i3 + 2 + (i4 * 2), 2));
                                            if (i4 != intFromBytes - 1) {
                                                str4 = str4 + ",";
                                            }
                                        }
                                        str = str4;
                                    }
                                    QueryItemNote.this.queryItemView.setValueText(str);
                                    return;
                                }
                                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                            }
                        });
                        return;
                    case 161:
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand4 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand4.setClearEnginError(false);
                        Shj.getInstance(QueryItemNote.this.context);
                        Shj.postSetCommand(commandV2_Up_SetCommand4, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.2
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z) {
                            }

                            AnonymousClass2() {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                                String str;
                                if (!bArr.toString().equals("0")) {
                                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                                    String str2 = "" + QueryItemNote.this.context.getString(R.string.gear_box_fault_cargo);
                                    if (intFromBytes == 0) {
                                        str = str2 + QueryItemNote.this.context.getString(R.string.no_have);
                                    } else {
                                        for (int i2 = 0; i2 < intFromBytes; i2++) {
                                            str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i2 * 2) + 1, 2));
                                            if (i2 != intFromBytes - 1) {
                                                str2 = str2 + ",";
                                            }
                                        }
                                        str = str2;
                                    }
                                    QueryItemNote.this.queryItemView.setValueText(str);
                                    return;
                                }
                                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                            }
                        });
                        return;
                    case 162:
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand5 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand5.setClearLiftError(false);
                        Shj.getInstance(QueryItemNote.this.context);
                        Shj.postSetCommand(commandV2_Up_SetCommand5, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.3
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z) {
                            }

                            AnonymousClass3() {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                                if (!bArr.toString().equals("0")) {
                                    QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_lift_failures) + ObjectHelper.intFromBytes(bArr, 0, 1));
                                    return;
                                }
                                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                            }
                        });
                        return;
                    case 163:
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand6 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand6.setClearBlock41s(false);
                        Shj.getInstance(QueryItemNote.this.context);
                        Shj.postSetCommand(commandV2_Up_SetCommand6, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.4
                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandSetAnswer(boolean z) {
                            }

                            AnonymousClass4() {
                            }

                            @Override // com.shj.OnCommandAnswerListener
                            public void onCommandReadAnswer(byte[] bArr) {
                                if (!bArr.toString().equals("0")) {
                                    QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes) + ObjectHelper.intFromBytes(bArr, 0, 1));
                                    return;
                                }
                                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                            }
                        });
                        return;
                    default:
                        switch (i) {
                            case SettingType.MAINCONTOL_PROGRAM_UPDATE /* 311 */:
                            case SettingType.SHELF_DRIVING_PROGRAM_UPDATE /* 312 */:
                                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                                intent.setType("*/*");
                                intent.addCategory("android.intent.category.OPENABLE");
                                if (QueryItemNote.this.context instanceof Activity) {
                                    ((Activity) QueryItemNote.this.context).startActivityForResult(intent, QueryItemNote.this.settingType);
                                    return;
                                }
                                return;
                            case SettingType.SHELF_DRIVING_VERSION_QUERY /* 313 */:
                                QueryItemNote.this.querShelfDrivingVersion(SettingActivity.getBasicMachineInfo().cabinetList, 0, "");
                                return;
                            default:
                                return;
                        }
                }
            }
            CommandV2_Up_SetCommand commandV2_Up_SetCommand7 = new CommandV2_Up_SetCommand();
            int newAddSelectIndex = QueryItemNote.this.queryItemView.getNewAddSelectIndex();
            commandV2_Up_SetCommand7.queryQgqStateNew(newAddSelectIndex);
            Shj.getInstance(QueryItemNote.this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand7, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.7
                final /* synthetic */ int val$jgh;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass7(int newAddSelectIndex2) {
                    newAddSelectIndex = newAddSelectIndex2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (bArr.length == 1) {
                            if (ObjectHelper.intFromBytes(bArr, 0, 1) == 1) {
                                QueryItemNote.this.queryQgqStateOld(newAddSelectIndex);
                                return;
                            }
                            return;
                        } else {
                            try {
                                QueryItemNote.this.queryItemView.setValueText(new String(ObjectHelper.bytesFromBytes(bArr, 0, bArr.length), "gbk").replace(VoiceWakeuperAidl.PARAMS_SEPARATE, StringUtils.LF));
                                return;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                }
            });
        }

        /* renamed from: com.shj.setting.generator.QueryItemNote$5$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                String str;
                if (!bArr.toString().equals("0")) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                    String str2 = "" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes);
                    if (intFromBytes == 0) {
                        str2 = str2 + QueryItemNote.this.context.getString(R.string.no_have);
                    } else {
                        for (int i2 = 0; i2 < intFromBytes; i2++) {
                            str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i2 * 2) + 1, 2));
                            if (i2 != intFromBytes - 1) {
                                str2 = str2 + ",";
                            }
                        }
                    }
                    String str3 = str2 + StringUtils.LF;
                    int i3 = intFromBytes * 2;
                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, i3 + 1, 1);
                    String str4 = str3 + QueryItemNote.this.context.getString(R.string.number_of_failure_channels);
                    if (intFromBytes2 == 0) {
                        str = str4 + QueryItemNote.this.context.getString(R.string.no_have);
                    } else {
                        for (int i4 = 0; i4 < intFromBytes2; i4++) {
                            str4 = str4 + String.valueOf(ObjectHelper.intFromBytes(bArr, i3 + 2 + (i4 * 2), 2));
                            if (i4 != intFromBytes - 1) {
                                str4 = str4 + ",";
                            }
                        }
                        str = str4;
                    }
                    QueryItemNote.this.queryItemView.setValueText(str);
                    return;
                }
                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
            }
        }

        /* renamed from: com.shj.setting.generator.QueryItemNote$5$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass2() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                String str;
                if (!bArr.toString().equals("0")) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                    String str2 = "" + QueryItemNote.this.context.getString(R.string.gear_box_fault_cargo);
                    if (intFromBytes == 0) {
                        str = str2 + QueryItemNote.this.context.getString(R.string.no_have);
                    } else {
                        for (int i2 = 0; i2 < intFromBytes; i2++) {
                            str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i2 * 2) + 1, 2));
                            if (i2 != intFromBytes - 1) {
                                str2 = str2 + ",";
                            }
                        }
                        str = str2;
                    }
                    QueryItemNote.this.queryItemView.setValueText(str);
                    return;
                }
                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
            }
        }

        /* renamed from: com.shj.setting.generator.QueryItemNote$5$3 */
        /* loaded from: classes2.dex */
        class AnonymousClass3 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass3() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (!bArr.toString().equals("0")) {
                    QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_lift_failures) + ObjectHelper.intFromBytes(bArr, 0, 1));
                    return;
                }
                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
            }
        }

        /* renamed from: com.shj.setting.generator.QueryItemNote$5$4 */
        /* loaded from: classes2.dex */
        class AnonymousClass4 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass4() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (!bArr.toString().equals("0")) {
                    QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes) + ObjectHelper.intFromBytes(bArr, 0, 1));
                    return;
                }
                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
            }
        }

        /* renamed from: com.shj.setting.generator.QueryItemNote$5$5 */
        /* loaded from: classes2.dex */
        class C00655 implements OnCommandAnswerListener {
            final /* synthetic */ LoadingDialog val$loadingDialog;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            C00655(LoadingDialog loadingDialog2) {
                loadingDialog = loadingDialog2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                int i2;
                int i3;
                if (bArr != null && bArr.length > 0) {
                    String string = QueryItemNote.this.context.getString(R.string.normal_number_of_cargo_paths);
                    String string2 = QueryItemNote.this.context.getString(R.string.short_circuit_number_of_motor);
                    int i4 = 0;
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 1, 1);
                    if (intFromBytes == 255 && intFromBytes2 == 255) {
                        int length = (bArr.length - 2) / 5;
                        i2 = 0;
                        i3 = 0;
                        while (i4 < length) {
                            int i5 = (i4 * 5) + 2;
                            i2 += ObjectHelper.intFromBytes(bArr, i5 + 1, 2);
                            i3 += ObjectHelper.intFromBytes(bArr, i5 + 3, 2);
                            i4++;
                        }
                    } else {
                        int length2 = (bArr.length - 1) / 3;
                        int i6 = 0;
                        int i7 = 0;
                        while (i4 < length2) {
                            int i8 = (i4 * 3) + 1;
                            i6 += ObjectHelper.intFromBytes(bArr, i8 + 1, 1);
                            i7 += ObjectHelper.intFromBytes(bArr, i8 + 2, 1);
                            i4++;
                        }
                        i2 = i6;
                        i3 = i7;
                    }
                    QueryItemNote.this.queryItemView.setValueText(string + i2 + StringUtils.LF + string2 + i3);
                } else {
                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                }
                loadingDialog.dismiss();
            }
        }

        /* renamed from: com.shj.setting.generator.QueryItemNote$5$6 */
        /* loaded from: classes2.dex */
        class AnonymousClass6 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass6() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (!bArr.toString().equals("0")) {
                    if (ObjectHelper.intFromBytes(bArr, 0, 1) != 0) {
                        QueryItemNote.this.queryItemView.setValueText(QueryItemNote.this.context.getString(R.string.coin_changer_error));
                        return;
                    } else {
                        QueryItemNote.this.queryItemView.setValueText(String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 4)));
                        return;
                    }
                }
                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
            }
        }

        /* renamed from: com.shj.setting.generator.QueryItemNote$5$7 */
        /* loaded from: classes2.dex */
        class AnonymousClass7 implements OnCommandAnswerListener {
            final /* synthetic */ int val$jgh;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass7(int newAddSelectIndex2) {
                newAddSelectIndex = newAddSelectIndex2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length > 0) {
                    if (bArr.length == 1) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 1) {
                            QueryItemNote.this.queryQgqStateOld(newAddSelectIndex);
                            return;
                        }
                        return;
                    } else {
                        try {
                            QueryItemNote.this.queryItemView.setValueText(new String(ObjectHelper.bytesFromBytes(bArr, 0, bArr.length), "gbk").replace(VoiceWakeuperAidl.PARAMS_SEPARATE, StringUtils.LF));
                            return;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
            }
        }
    }

    private void setQueryListener() {
        this.queryItemView.setQueryListener(new View.OnClickListener() { // from class: com.shj.setting.generator.QueryItemNote.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
                int i = QueryItemNote.this.settingType;
                if (i == 167) {
                    LoadingDialog loadingDialog2 = new LoadingDialog(QueryItemNote.this.context, R.string.on_querying);
                    loadingDialog2.show();
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                    commandV2_Up_SetCommand.setShelfCount();
                    Shj.getInstance(QueryItemNote.this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.5
                        final /* synthetic */ LoadingDialog val$loadingDialog;

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z) {
                        }

                        C00655(LoadingDialog loadingDialog22) {
                            loadingDialog = loadingDialog22;
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                            int i2;
                            int i3;
                            if (bArr != null && bArr.length > 0) {
                                String string = QueryItemNote.this.context.getString(R.string.normal_number_of_cargo_paths);
                                String string2 = QueryItemNote.this.context.getString(R.string.short_circuit_number_of_motor);
                                int i4 = 0;
                                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                                int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 1, 1);
                                if (intFromBytes == 255 && intFromBytes2 == 255) {
                                    int length = (bArr.length - 2) / 5;
                                    i2 = 0;
                                    i3 = 0;
                                    while (i4 < length) {
                                        int i5 = (i4 * 5) + 2;
                                        i2 += ObjectHelper.intFromBytes(bArr, i5 + 1, 2);
                                        i3 += ObjectHelper.intFromBytes(bArr, i5 + 3, 2);
                                        i4++;
                                    }
                                } else {
                                    int length2 = (bArr.length - 1) / 3;
                                    int i6 = 0;
                                    int i7 = 0;
                                    while (i4 < length2) {
                                        int i8 = (i4 * 3) + 1;
                                        i6 += ObjectHelper.intFromBytes(bArr, i8 + 1, 1);
                                        i7 += ObjectHelper.intFromBytes(bArr, i8 + 2, 1);
                                        i4++;
                                    }
                                    i2 = i6;
                                    i3 = i7;
                                }
                                QueryItemNote.this.queryItemView.setValueText(string + i2 + StringUtils.LF + string2 + i3);
                            } else {
                                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                            }
                            loadingDialog.dismiss();
                        }
                    });
                    return;
                }
                if (i == 188) {
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                    commandV2_Up_SetCommand2.queryCoinBalance();
                    Shj.getInstance(QueryItemNote.this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.6
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z) {
                        }

                        AnonymousClass6() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                            if (!bArr.toString().equals("0")) {
                                if (ObjectHelper.intFromBytes(bArr, 0, 1) != 0) {
                                    QueryItemNote.this.queryItemView.setValueText(QueryItemNote.this.context.getString(R.string.coin_changer_error));
                                    return;
                                } else {
                                    QueryItemNote.this.queryItemView.setValueText(String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 4)));
                                    return;
                                }
                            }
                            ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                        }
                    });
                    return;
                }
                if (i != 234) {
                    switch (i) {
                        case 160:
                            CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
                            commandV2_Up_SetCommand3.setClearBlocks(false);
                            Shj.getInstance(QueryItemNote.this.context);
                            Shj.postSetCommand(commandV2_Up_SetCommand3, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.1
                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandSetAnswer(boolean z) {
                                }

                                AnonymousClass1() {
                                }

                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandReadAnswer(byte[] bArr) {
                                    String str;
                                    if (!bArr.toString().equals("0")) {
                                        int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                                        String str2 = "" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes);
                                        if (intFromBytes == 0) {
                                            str2 = str2 + QueryItemNote.this.context.getString(R.string.no_have);
                                        } else {
                                            for (int i2 = 0; i2 < intFromBytes; i2++) {
                                                str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i2 * 2) + 1, 2));
                                                if (i2 != intFromBytes - 1) {
                                                    str2 = str2 + ",";
                                                }
                                            }
                                        }
                                        String str3 = str2 + StringUtils.LF;
                                        int i3 = intFromBytes * 2;
                                        int intFromBytes2 = ObjectHelper.intFromBytes(bArr, i3 + 1, 1);
                                        String str4 = str3 + QueryItemNote.this.context.getString(R.string.number_of_failure_channels);
                                        if (intFromBytes2 == 0) {
                                            str = str4 + QueryItemNote.this.context.getString(R.string.no_have);
                                        } else {
                                            for (int i4 = 0; i4 < intFromBytes2; i4++) {
                                                str4 = str4 + String.valueOf(ObjectHelper.intFromBytes(bArr, i3 + 2 + (i4 * 2), 2));
                                                if (i4 != intFromBytes - 1) {
                                                    str4 = str4 + ",";
                                                }
                                            }
                                            str = str4;
                                        }
                                        QueryItemNote.this.queryItemView.setValueText(str);
                                        return;
                                    }
                                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                                }
                            });
                            return;
                        case 161:
                            CommandV2_Up_SetCommand commandV2_Up_SetCommand4 = new CommandV2_Up_SetCommand();
                            commandV2_Up_SetCommand4.setClearEnginError(false);
                            Shj.getInstance(QueryItemNote.this.context);
                            Shj.postSetCommand(commandV2_Up_SetCommand4, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.2
                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandSetAnswer(boolean z) {
                                }

                                AnonymousClass2() {
                                }

                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandReadAnswer(byte[] bArr) {
                                    String str;
                                    if (!bArr.toString().equals("0")) {
                                        int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                                        String str2 = "" + QueryItemNote.this.context.getString(R.string.gear_box_fault_cargo);
                                        if (intFromBytes == 0) {
                                            str = str2 + QueryItemNote.this.context.getString(R.string.no_have);
                                        } else {
                                            for (int i2 = 0; i2 < intFromBytes; i2++) {
                                                str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i2 * 2) + 1, 2));
                                                if (i2 != intFromBytes - 1) {
                                                    str2 = str2 + ",";
                                                }
                                            }
                                            str = str2;
                                        }
                                        QueryItemNote.this.queryItemView.setValueText(str);
                                        return;
                                    }
                                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                                }
                            });
                            return;
                        case 162:
                            CommandV2_Up_SetCommand commandV2_Up_SetCommand5 = new CommandV2_Up_SetCommand();
                            commandV2_Up_SetCommand5.setClearLiftError(false);
                            Shj.getInstance(QueryItemNote.this.context);
                            Shj.postSetCommand(commandV2_Up_SetCommand5, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.3
                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandSetAnswer(boolean z) {
                                }

                                AnonymousClass3() {
                                }

                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandReadAnswer(byte[] bArr) {
                                    if (!bArr.toString().equals("0")) {
                                        QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_lift_failures) + ObjectHelper.intFromBytes(bArr, 0, 1));
                                        return;
                                    }
                                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                                }
                            });
                            return;
                        case 163:
                            CommandV2_Up_SetCommand commandV2_Up_SetCommand6 = new CommandV2_Up_SetCommand();
                            commandV2_Up_SetCommand6.setClearBlock41s(false);
                            Shj.getInstance(QueryItemNote.this.context);
                            Shj.postSetCommand(commandV2_Up_SetCommand6, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.4
                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandSetAnswer(boolean z) {
                                }

                                AnonymousClass4() {
                                }

                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandReadAnswer(byte[] bArr) {
                                    if (!bArr.toString().equals("0")) {
                                        QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes) + ObjectHelper.intFromBytes(bArr, 0, 1));
                                        return;
                                    }
                                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                                }
                            });
                            return;
                        default:
                            switch (i) {
                                case SettingType.MAINCONTOL_PROGRAM_UPDATE /* 311 */:
                                case SettingType.SHELF_DRIVING_PROGRAM_UPDATE /* 312 */:
                                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                                    intent.setType("*/*");
                                    intent.addCategory("android.intent.category.OPENABLE");
                                    if (QueryItemNote.this.context instanceof Activity) {
                                        ((Activity) QueryItemNote.this.context).startActivityForResult(intent, QueryItemNote.this.settingType);
                                        return;
                                    }
                                    return;
                                case SettingType.SHELF_DRIVING_VERSION_QUERY /* 313 */:
                                    QueryItemNote.this.querShelfDrivingVersion(SettingActivity.getBasicMachineInfo().cabinetList, 0, "");
                                    return;
                                default:
                                    return;
                            }
                    }
                }
                CommandV2_Up_SetCommand commandV2_Up_SetCommand7 = new CommandV2_Up_SetCommand();
                int newAddSelectIndex2 = QueryItemNote.this.queryItemView.getNewAddSelectIndex();
                commandV2_Up_SetCommand7.queryQgqStateNew(newAddSelectIndex2);
                Shj.getInstance(QueryItemNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand7, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.5.7
                    final /* synthetic */ int val$jgh;

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z) {
                    }

                    AnonymousClass7(int newAddSelectIndex22) {
                        newAddSelectIndex = newAddSelectIndex22;
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        if (bArr != null && bArr.length > 0) {
                            if (bArr.length == 1) {
                                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 1) {
                                    QueryItemNote.this.queryQgqStateOld(newAddSelectIndex);
                                    return;
                                }
                                return;
                            } else {
                                try {
                                    QueryItemNote.this.queryItemView.setValueText(new String(ObjectHelper.bytesFromBytes(bArr, 0, bArr.length), "gbk").replace(VoiceWakeuperAidl.PARAMS_SEPARATE, StringUtils.LF));
                                    return;
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                        }
                        ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                    }
                });
            }

            /* renamed from: com.shj.setting.generator.QueryItemNote$5$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    String str;
                    if (!bArr.toString().equals("0")) {
                        int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                        String str2 = "" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes);
                        if (intFromBytes == 0) {
                            str2 = str2 + QueryItemNote.this.context.getString(R.string.no_have);
                        } else {
                            for (int i2 = 0; i2 < intFromBytes; i2++) {
                                str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i2 * 2) + 1, 2));
                                if (i2 != intFromBytes - 1) {
                                    str2 = str2 + ",";
                                }
                            }
                        }
                        String str3 = str2 + StringUtils.LF;
                        int i3 = intFromBytes * 2;
                        int intFromBytes2 = ObjectHelper.intFromBytes(bArr, i3 + 1, 1);
                        String str4 = str3 + QueryItemNote.this.context.getString(R.string.number_of_failure_channels);
                        if (intFromBytes2 == 0) {
                            str = str4 + QueryItemNote.this.context.getString(R.string.no_have);
                        } else {
                            for (int i4 = 0; i4 < intFromBytes2; i4++) {
                                str4 = str4 + String.valueOf(ObjectHelper.intFromBytes(bArr, i3 + 2 + (i4 * 2), 2));
                                if (i4 != intFromBytes - 1) {
                                    str4 = str4 + ",";
                                }
                            }
                            str = str4;
                        }
                        QueryItemNote.this.queryItemView.setValueText(str);
                        return;
                    }
                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                }
            }

            /* renamed from: com.shj.setting.generator.QueryItemNote$5$2 */
            /* loaded from: classes2.dex */
            class AnonymousClass2 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass2() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    String str;
                    if (!bArr.toString().equals("0")) {
                        int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                        String str2 = "" + QueryItemNote.this.context.getString(R.string.gear_box_fault_cargo);
                        if (intFromBytes == 0) {
                            str = str2 + QueryItemNote.this.context.getString(R.string.no_have);
                        } else {
                            for (int i2 = 0; i2 < intFromBytes; i2++) {
                                str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i2 * 2) + 1, 2));
                                if (i2 != intFromBytes - 1) {
                                    str2 = str2 + ",";
                                }
                            }
                            str = str2;
                        }
                        QueryItemNote.this.queryItemView.setValueText(str);
                        return;
                    }
                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                }
            }

            /* renamed from: com.shj.setting.generator.QueryItemNote$5$3 */
            /* loaded from: classes2.dex */
            class AnonymousClass3 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass3() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (!bArr.toString().equals("0")) {
                        QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_lift_failures) + ObjectHelper.intFromBytes(bArr, 0, 1));
                        return;
                    }
                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                }
            }

            /* renamed from: com.shj.setting.generator.QueryItemNote$5$4 */
            /* loaded from: classes2.dex */
            class AnonymousClass4 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass4() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (!bArr.toString().equals("0")) {
                        QueryItemNote.this.queryItemView.setValueText("" + QueryItemNote.this.context.getString(R.string.number_of_cargo_lanes) + ObjectHelper.intFromBytes(bArr, 0, 1));
                        return;
                    }
                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                }
            }

            /* renamed from: com.shj.setting.generator.QueryItemNote$5$5 */
            /* loaded from: classes2.dex */
            class C00655 implements OnCommandAnswerListener {
                final /* synthetic */ LoadingDialog val$loadingDialog;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                C00655(LoadingDialog loadingDialog22) {
                    loadingDialog = loadingDialog22;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    int i2;
                    int i3;
                    if (bArr != null && bArr.length > 0) {
                        String string = QueryItemNote.this.context.getString(R.string.normal_number_of_cargo_paths);
                        String string2 = QueryItemNote.this.context.getString(R.string.short_circuit_number_of_motor);
                        int i4 = 0;
                        int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                        int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 1, 1);
                        if (intFromBytes == 255 && intFromBytes2 == 255) {
                            int length = (bArr.length - 2) / 5;
                            i2 = 0;
                            i3 = 0;
                            while (i4 < length) {
                                int i5 = (i4 * 5) + 2;
                                i2 += ObjectHelper.intFromBytes(bArr, i5 + 1, 2);
                                i3 += ObjectHelper.intFromBytes(bArr, i5 + 3, 2);
                                i4++;
                            }
                        } else {
                            int length2 = (bArr.length - 1) / 3;
                            int i6 = 0;
                            int i7 = 0;
                            while (i4 < length2) {
                                int i8 = (i4 * 3) + 1;
                                i6 += ObjectHelper.intFromBytes(bArr, i8 + 1, 1);
                                i7 += ObjectHelper.intFromBytes(bArr, i8 + 2, 1);
                                i4++;
                            }
                            i2 = i6;
                            i3 = i7;
                        }
                        QueryItemNote.this.queryItemView.setValueText(string + i2 + StringUtils.LF + string2 + i3);
                    } else {
                        ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                    }
                    loadingDialog.dismiss();
                }
            }

            /* renamed from: com.shj.setting.generator.QueryItemNote$5$6 */
            /* loaded from: classes2.dex */
            class AnonymousClass6 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass6() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (!bArr.toString().equals("0")) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) != 0) {
                            QueryItemNote.this.queryItemView.setValueText(QueryItemNote.this.context.getString(R.string.coin_changer_error));
                            return;
                        } else {
                            QueryItemNote.this.queryItemView.setValueText(String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 4)));
                            return;
                        }
                    }
                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                }
            }

            /* renamed from: com.shj.setting.generator.QueryItemNote$5$7 */
            /* loaded from: classes2.dex */
            class AnonymousClass7 implements OnCommandAnswerListener {
                final /* synthetic */ int val$jgh;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass7(int newAddSelectIndex22) {
                    newAddSelectIndex = newAddSelectIndex22;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (bArr.length == 1) {
                            if (ObjectHelper.intFromBytes(bArr, 0, 1) == 1) {
                                QueryItemNote.this.queryQgqStateOld(newAddSelectIndex);
                                return;
                            }
                            return;
                        } else {
                            try {
                                QueryItemNote.this.queryItemView.setValueText(new String(ObjectHelper.bytesFromBytes(bArr, 0, bArr.length), "gbk").replace(VoiceWakeuperAidl.PARAMS_SEPARATE, StringUtils.LF));
                                return;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                }
            }
        });
    }

    public void querShelfDrivingVersion(List<Integer> list, int i, String str) {
        if (i >= list.size()) {
            this.queryItemView.setValueText(str.replaceAll(VoiceWakeuperAidl.PARAMS_SEPARATE, StringUtils.LF));
            this.queryItemView.setValueTextSize(this.context.getResources().getDimensionPixelSize(R.dimen.text_xnormal));
        } else {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.shelfDrvingVersionQuery(list.get(i).intValue());
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.6
                final /* synthetic */ List val$cabinetList;
                final /* synthetic */ int val$index;
                final /* synthetic */ String val$text;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass6(int i2, String str2, List list2) {
                    i = i2;
                    str = str2;
                    list = list2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 1) {
                        try {
                            String str2 = (SettingActivity.getBasicMachineInfo().cabinetNumberList.get(i) + ":") + new String(ObjectHelper.bytesFromBytes(bArr, 0, bArr.length), "gbk");
                            if (!TextUtils.isEmpty(str)) {
                                str2 = str + StringUtils.LF + str2;
                            }
                            QueryItemNote.this.querShelfDrivingVersion(list, i + 1, str2);
                            return;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
                }
            });
        }
    }

    /* renamed from: com.shj.setting.generator.QueryItemNote$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements OnCommandAnswerListener {
        final /* synthetic */ List val$cabinetList;
        final /* synthetic */ int val$index;
        final /* synthetic */ String val$text;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass6(int i2, String str2, List list2) {
            i = i2;
            str = str2;
            list = list2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 1) {
                try {
                    String str2 = (SettingActivity.getBasicMachineInfo().cabinetNumberList.get(i) + ":") + new String(ObjectHelper.bytesFromBytes(bArr, 0, bArr.length), "gbk");
                    if (!TextUtils.isEmpty(str)) {
                        str2 = str + StringUtils.LF + str2;
                    }
                    QueryItemNote.this.querShelfDrivingVersion(list, i + 1, str2);
                    return;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return;
                }
            }
            ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
        }
    }

    public void queryQgqStateOld(int i) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.queryQgqState(i);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.QueryItemNote.7
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass7() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length > 0) {
                    String string = QueryItemNote.this.getString(R.string.at_down);
                    String string2 = QueryItemNote.this.getString(R.string.be_not_in);
                    String string3 = QueryItemNote.this.getString(R.string.at_top);
                    String string4 = QueryItemNote.this.getString(R.string.at_left);
                    String string5 = QueryItemNote.this.getString(R.string.at_right);
                    String string6 = QueryItemNote.this.getString(R.string.shortening_state);
                    String string7 = QueryItemNote.this.getString(R.string.elongation_state);
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                    String str = QueryItemNote.this.getString(R.string.number_of_sensor_data) + intFromBytes + StringUtils.LF;
                    if (intFromBytes > 0) {
                        str = str + QueryItemNote.this.getString(R.string.ad_value_of_elevator_testing_tube) + ObjectHelper.intFromBytes(bArr, 1, 1) + StringUtils.LF;
                    }
                    if (intFromBytes > 1) {
                        str = str + QueryItemNote.this.getString(R.string.ad_value_of_microwave_oven_test_tube) + ObjectHelper.intFromBytes(bArr, 2, 1) + StringUtils.LF;
                    }
                    if (intFromBytes > 2) {
                        str = str + QueryItemNote.this.getString(R.string.tube_3ad_value) + ObjectHelper.intFromBytes(bArr, 3, 1) + StringUtils.LF;
                    }
                    if (intFromBytes > 3) {
                        str = str + QueryItemNote.this.getString(R.string.tube_4ad_value) + ObjectHelper.intFromBytes(bArr, 4, 1) + StringUtils.LF;
                    }
                    if (intFromBytes > 4) {
                        str = str + QueryItemNote.this.getString(R.string.tube_5ad_value) + ObjectHelper.intFromBytes(bArr, 5, 1) + StringUtils.LF;
                    }
                    if (intFromBytes > 5) {
                        str = str + QueryItemNote.this.getString(R.string.tube_6ad_value) + ObjectHelper.intFromBytes(bArr, 6, 1) + StringUtils.LF;
                    }
                    if (intFromBytes > 6) {
                        str = str + QueryItemNote.this.getString(R.string.io_port_1_state) + ObjectHelper.intFromBytes(bArr, 7, 1) + StringUtils.LF;
                    }
                    if (intFromBytes > 7) {
                        str = str + QueryItemNote.this.getString(R.string.io_port_2_state) + ObjectHelper.intFromBytes(bArr, 8, 1) + StringUtils.LF;
                    }
                    if (intFromBytes > 8) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(QueryItemNote.this.getString(R.string.whether_the_elevator_is_in_the_bottom_state));
                        sb.append(ObjectHelper.intFromBytes(bArr, 9, 1) == 1 ? string : string2);
                        sb.append(StringUtils.LF);
                        str = sb.toString();
                    }
                    if (intFromBytes > 9) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(QueryItemNote.this.getString(R.string.whether_the_translation_motor_is_in_the_left_state));
                        if (ObjectHelper.intFromBytes(bArr, 10, 1) != 1) {
                            string4 = string2;
                        }
                        sb2.append(string4);
                        sb2.append(StringUtils.LF);
                        str = sb2.toString();
                    }
                    if (intFromBytes > 10) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str);
                        sb3.append(QueryItemNote.this.getString(R.string.whether_the_translation_motor_is_in_the_right_state));
                        if (ObjectHelper.intFromBytes(bArr, 11, 1) != 1) {
                            string5 = string2;
                        }
                        sb3.append(string5);
                        sb3.append(StringUtils.LF);
                        str = sb3.toString();
                    }
                    if (intFromBytes > 11) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(str);
                        sb4.append(QueryItemNote.this.getString(R.string.is_the_front_door_at_the_bottom));
                        sb4.append(ObjectHelper.intFromBytes(bArr, 12, 1) == 1 ? string : string2);
                        sb4.append(StringUtils.LF);
                        str = sb4.toString();
                    }
                    if (intFromBytes > 12) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str);
                        sb5.append(QueryItemNote.this.getString(R.string.is_the_front_door_at_the_top));
                        sb5.append(ObjectHelper.intFromBytes(bArr, 13, 1) == 1 ? string3 : string2);
                        sb5.append(StringUtils.LF);
                        str = sb5.toString();
                    }
                    if (intFromBytes > 13) {
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(str);
                        sb6.append(QueryItemNote.this.getString(R.string.is_the_back_door_at_the_bottom));
                        if (ObjectHelper.intFromBytes(bArr, 14, 1) != 1) {
                            string = string2;
                        }
                        sb6.append(string);
                        sb6.append(StringUtils.LF);
                        str = sb6.toString();
                    }
                    if (intFromBytes > 14) {
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(str);
                        sb7.append(QueryItemNote.this.getString(R.string.is_the_back_door_at_the_top));
                        if (ObjectHelper.intFromBytes(bArr, 15, 1) == 1) {
                            string2 = string3;
                        }
                        sb7.append(string2);
                        sb7.append(StringUtils.LF);
                        str = sb7.toString();
                    }
                    if (intFromBytes > 15) {
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(str);
                        sb8.append(QueryItemNote.this.getString(R.string.stage_of_pole_motor));
                        if (ObjectHelper.intFromBytes(bArr, 16, 1) != 1) {
                            string6 = string7;
                        }
                        sb8.append(string6);
                        str = sb8.toString();
                    }
                    QueryItemNote.this.queryItemView.setValueText(str);
                    return;
                }
                ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
            }
        });
    }

    /* renamed from: com.shj.setting.generator.QueryItemNote$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass7() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                String string = QueryItemNote.this.getString(R.string.at_down);
                String string2 = QueryItemNote.this.getString(R.string.be_not_in);
                String string3 = QueryItemNote.this.getString(R.string.at_top);
                String string4 = QueryItemNote.this.getString(R.string.at_left);
                String string5 = QueryItemNote.this.getString(R.string.at_right);
                String string6 = QueryItemNote.this.getString(R.string.shortening_state);
                String string7 = QueryItemNote.this.getString(R.string.elongation_state);
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                String str = QueryItemNote.this.getString(R.string.number_of_sensor_data) + intFromBytes + StringUtils.LF;
                if (intFromBytes > 0) {
                    str = str + QueryItemNote.this.getString(R.string.ad_value_of_elevator_testing_tube) + ObjectHelper.intFromBytes(bArr, 1, 1) + StringUtils.LF;
                }
                if (intFromBytes > 1) {
                    str = str + QueryItemNote.this.getString(R.string.ad_value_of_microwave_oven_test_tube) + ObjectHelper.intFromBytes(bArr, 2, 1) + StringUtils.LF;
                }
                if (intFromBytes > 2) {
                    str = str + QueryItemNote.this.getString(R.string.tube_3ad_value) + ObjectHelper.intFromBytes(bArr, 3, 1) + StringUtils.LF;
                }
                if (intFromBytes > 3) {
                    str = str + QueryItemNote.this.getString(R.string.tube_4ad_value) + ObjectHelper.intFromBytes(bArr, 4, 1) + StringUtils.LF;
                }
                if (intFromBytes > 4) {
                    str = str + QueryItemNote.this.getString(R.string.tube_5ad_value) + ObjectHelper.intFromBytes(bArr, 5, 1) + StringUtils.LF;
                }
                if (intFromBytes > 5) {
                    str = str + QueryItemNote.this.getString(R.string.tube_6ad_value) + ObjectHelper.intFromBytes(bArr, 6, 1) + StringUtils.LF;
                }
                if (intFromBytes > 6) {
                    str = str + QueryItemNote.this.getString(R.string.io_port_1_state) + ObjectHelper.intFromBytes(bArr, 7, 1) + StringUtils.LF;
                }
                if (intFromBytes > 7) {
                    str = str + QueryItemNote.this.getString(R.string.io_port_2_state) + ObjectHelper.intFromBytes(bArr, 8, 1) + StringUtils.LF;
                }
                if (intFromBytes > 8) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(QueryItemNote.this.getString(R.string.whether_the_elevator_is_in_the_bottom_state));
                    sb.append(ObjectHelper.intFromBytes(bArr, 9, 1) == 1 ? string : string2);
                    sb.append(StringUtils.LF);
                    str = sb.toString();
                }
                if (intFromBytes > 9) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(QueryItemNote.this.getString(R.string.whether_the_translation_motor_is_in_the_left_state));
                    if (ObjectHelper.intFromBytes(bArr, 10, 1) != 1) {
                        string4 = string2;
                    }
                    sb2.append(string4);
                    sb2.append(StringUtils.LF);
                    str = sb2.toString();
                }
                if (intFromBytes > 10) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(QueryItemNote.this.getString(R.string.whether_the_translation_motor_is_in_the_right_state));
                    if (ObjectHelper.intFromBytes(bArr, 11, 1) != 1) {
                        string5 = string2;
                    }
                    sb3.append(string5);
                    sb3.append(StringUtils.LF);
                    str = sb3.toString();
                }
                if (intFromBytes > 11) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str);
                    sb4.append(QueryItemNote.this.getString(R.string.is_the_front_door_at_the_bottom));
                    sb4.append(ObjectHelper.intFromBytes(bArr, 12, 1) == 1 ? string : string2);
                    sb4.append(StringUtils.LF);
                    str = sb4.toString();
                }
                if (intFromBytes > 12) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str);
                    sb5.append(QueryItemNote.this.getString(R.string.is_the_front_door_at_the_top));
                    sb5.append(ObjectHelper.intFromBytes(bArr, 13, 1) == 1 ? string3 : string2);
                    sb5.append(StringUtils.LF);
                    str = sb5.toString();
                }
                if (intFromBytes > 13) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(str);
                    sb6.append(QueryItemNote.this.getString(R.string.is_the_back_door_at_the_bottom));
                    if (ObjectHelper.intFromBytes(bArr, 14, 1) != 1) {
                        string = string2;
                    }
                    sb6.append(string);
                    sb6.append(StringUtils.LF);
                    str = sb6.toString();
                }
                if (intFromBytes > 14) {
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str);
                    sb7.append(QueryItemNote.this.getString(R.string.is_the_back_door_at_the_top));
                    if (ObjectHelper.intFromBytes(bArr, 15, 1) == 1) {
                        string2 = string3;
                    }
                    sb7.append(string2);
                    sb7.append(StringUtils.LF);
                    str = sb7.toString();
                }
                if (intFromBytes > 15) {
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(str);
                    sb8.append(QueryItemNote.this.getString(R.string.stage_of_pole_motor));
                    if (ObjectHelper.intFromBytes(bArr, 16, 1) != 1) {
                        string6 = string7;
                    }
                    sb8.append(string6);
                    str = sb8.toString();
                }
                QueryItemNote.this.queryItemView.setValueText(str);
                return;
            }
            ToastUitl.showShort(QueryItemNote.this.context, R.string.qurey_fail);
        }
    }

    public String getString(int i) {
        return this.context.getString(i);
    }

    private void setAlwaysNotDisplaySaveButton() {
        int i = this.settingType;
        if (i == 311 || i == 312) {
            return;
        }
        switch (i) {
            case 160:
            case 161:
            case 162:
            case 163:
                return;
            default:
                this.queryItemView.setAlwaysNotDisplaySaveButton();
                return;
        }
    }

    private void setSaveSettingText() {
        int i = this.settingType;
        if (i != 311 && i != 312) {
            switch (i) {
                case 160:
                case 161:
                case 162:
                case 163:
                    this.queryItemView.setSaveSettingText(this.context.getResources().getString(R.string.cargo_clearance));
                    return;
                default:
                    return;
            }
        }
        this.queryItemView.setSaveSettingText(this.context.getResources().getString(R.string.program_update));
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        super.onAttached();
        EventBus.getDefault().register(this);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
        super.onDetached();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof SelectMainContolFileEvent) {
            if (this.settingType == 311) {
                this.queryItemView.setValueText(((SelectMainContolFileEvent) baseEvent).getPath());
            }
        } else if ((baseEvent instanceof SelectShelfDrivingFileEvent) && this.settingType == 312) {
            this.queryItemView.setValueText(((SelectShelfDrivingFileEvent) baseEvent).getPath());
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.queryItemView;
    }
}
