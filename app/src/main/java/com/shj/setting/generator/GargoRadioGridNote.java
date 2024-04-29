package com.shj.setting.generator;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Event.BaseEvent;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.MutilTextTipDialog;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.Constant;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.event.SettingTypeEvent;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.GargoRadioGridView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* loaded from: classes.dex */
public class GargoRadioGridNote extends SettingNote {
    private int cabinetNumber;
    private GargoRadioGridView gargoRadioGridView;
    private GargoRadioGridView.GridRadioItemData gridRadioItemData;
    private LoadingDialog loadingDialog;
    private MutilTextTipDialog mutilTextTipDialog;

    public GargoRadioGridNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    public void cargoSetup(List<GargoRadioGridView.RadioItemData> list, int i) {
        if (i < list.size()) {
            GargoRadioGridView.RadioItemData radioItemData = list.get(i);
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            if (radioItemData.selectState != 0) {
                boolean z = radioItemData.selectState != 1;
                commandV2_Up_SetCommand.setcontinueSaleAfterBlock(true, radioItemData.identifier, z);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.GargoRadioGridNote.1
                    final /* synthetic */ GargoRadioGridView.RadioItemData val$data;
                    final /* synthetic */ boolean val$enableCard;
                    final /* synthetic */ int val$index;
                    final /* synthetic */ List val$list;

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass1(GargoRadioGridView.RadioItemData radioItemData2, boolean z2, int i2, List list2) {
                        radioItemData = radioItemData2;
                        z = z2;
                        i = i2;
                        list = list2;
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        String str;
                        String str2;
                        if (GargoRadioGridNote.this.mutilTextTipDialog != null) {
                            GargoRadioGridNote.this.mutilTextTipDialog.show();
                        } else {
                            GargoRadioGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(GargoRadioGridNote.this.context);
                            GargoRadioGridNote.this.mutilTextTipDialog.show();
                        }
                        String str3 = ("" + GargoRadioGridNote.this.context.getString(R.string.layer_number)) + "  " + radioItemData.identifier;
                        if (z) {
                            str = str3 + StringUtils.SPACE + GargoRadioGridNote.this.context.getString(R.string.continue_to_buy);
                        } else {
                            str = str3 + StringUtils.SPACE + GargoRadioGridNote.this.context.getString(R.string.cannot_buy);
                        }
                        if (z2) {
                            str2 = str + StringUtils.SPACE + GargoRadioGridNote.this.context.getString(R.string.setting_success);
                            GargoRadioGridNote.this.gargoRadioGridView.upDateState(radioItemData.identifier, radioItemData.selectState);
                        } else {
                            str2 = str + StringUtils.SPACE + GargoRadioGridNote.this.context.getString(R.string.setting_fail);
                        }
                        GargoRadioGridNote.this.mutilTextTipDialog.addTextShow(str2);
                        if (i + 1 < list.size()) {
                            GargoRadioGridNote.this.cargoSetup(list, i + 1);
                        } else {
                            GargoRadioGridNote.this.mutilTextTipDialog.addTextShow(GargoRadioGridNote.this.context.getString(R.string.setting_compelete));
                        }
                    }
                });
                return;
            }
            int i2 = i2 + 1;
            if (i2 >= list2.size()) {
                MutilTextTipDialog mutilTextTipDialog = this.mutilTextTipDialog;
                if (mutilTextTipDialog != null) {
                    mutilTextTipDialog.addTextShow(this.context.getString(R.string.setting_compelete));
                    return;
                }
                return;
            }
            cargoSetup(list2, i2);
        }
    }

    /* renamed from: com.shj.setting.generator.GargoRadioGridNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnCommandAnswerListener {
        final /* synthetic */ GargoRadioGridView.RadioItemData val$data;
        final /* synthetic */ boolean val$enableCard;
        final /* synthetic */ int val$index;
        final /* synthetic */ List val$list;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1(GargoRadioGridView.RadioItemData radioItemData2, boolean z2, int i2, List list2) {
            radioItemData = radioItemData2;
            z = z2;
            i = i2;
            list = list2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            String str;
            String str2;
            if (GargoRadioGridNote.this.mutilTextTipDialog != null) {
                GargoRadioGridNote.this.mutilTextTipDialog.show();
            } else {
                GargoRadioGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(GargoRadioGridNote.this.context);
                GargoRadioGridNote.this.mutilTextTipDialog.show();
            }
            String str3 = ("" + GargoRadioGridNote.this.context.getString(R.string.layer_number)) + "  " + radioItemData.identifier;
            if (z) {
                str = str3 + StringUtils.SPACE + GargoRadioGridNote.this.context.getString(R.string.continue_to_buy);
            } else {
                str = str3 + StringUtils.SPACE + GargoRadioGridNote.this.context.getString(R.string.cannot_buy);
            }
            if (z2) {
                str2 = str + StringUtils.SPACE + GargoRadioGridNote.this.context.getString(R.string.setting_success);
                GargoRadioGridNote.this.gargoRadioGridView.upDateState(radioItemData.identifier, radioItemData.selectState);
            } else {
                str2 = str + StringUtils.SPACE + GargoRadioGridNote.this.context.getString(R.string.setting_fail);
            }
            GargoRadioGridNote.this.mutilTextTipDialog.addTextShow(str2);
            if (i + 1 < list.size()) {
                GargoRadioGridNote.this.cargoSetup(list, i + 1);
            } else {
                GargoRadioGridNote.this.mutilTextTipDialog.addTextShow(GargoRadioGridNote.this.context.getString(R.string.setting_compelete));
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        EventBus.getDefault().register(this);
        queryData();
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
        EventBus.getDefault().unregister(this);
    }

    public void queryData() {
        super.querySettingData();
        if (this.settingType != 292) {
            return;
        }
        String boxRiceMachineCabinetSetting = AppSetting.getBoxRiceMachineCabinetSetting(this.context, null);
        List<GargoRadioGridView.RadioItemData> list = this.gridRadioItemData.radioItemData;
        if (list != null) {
            Iterator<GargoRadioGridView.RadioItemData> it = list.iterator();
            while (it.hasNext()) {
                this.gargoRadioGridView.upDateState(it.next().identifier, 1);
            }
        }
        if (TextUtils.isEmpty(boxRiceMachineCabinetSetting)) {
            return;
        }
        for (String str : boxRiceMachineCabinetSetting.split(VoiceWakeuperAidl.PARAMS_SEPARATE)) {
            String[] split = str.split(":");
            if (split.length == 2) {
                this.gargoRadioGridView.upDateState(Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue());
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        super.querySettingData();
        if (this.settingType == 154 && this.gridRadioItemData.radioItemData != null) {
            LoadingDialog loadingDialog = new LoadingDialog(this.context, this.context.getString(R.string.loading));
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            readCargoCardWholeLayerState(0);
        }
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        Object data;
        if (baseEvent instanceof SettingTypeEvent) {
            SettingTypeEvent settingTypeEvent = (SettingTypeEvent) baseEvent;
            if (this.settingType == 154 && settingTypeEvent.getSettingType() == 155 && (data = settingTypeEvent.getData()) != null) {
                if (data instanceof String) {
                    if (!Constant.READ_CARGO_CARD_WHOLE_COMPELTE.equals((String) data) || this.gridRadioItemData.radioItemData == null) {
                        return;
                    }
                    LoadingDialog loadingDialog = new LoadingDialog(this.context, this.context.getString(R.string.loading));
                    this.loadingDialog = loadingDialog;
                    loadingDialog.show();
                    readCargoCardWholeLayerState(0);
                    return;
                }
                if (data instanceof Boolean) {
                    this.gargoRadioGridView.upDateAllState((((Boolean) data).booleanValue() ? 1 : 0) + 1);
                }
            }
        }
    }

    public void readCargoCardWholeLayerState(int i) {
        if (i < this.gridRadioItemData.radioItemData.size()) {
            GargoRadioGridView.RadioItemData radioItemData = this.gridRadioItemData.radioItemData.get(i);
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setcontinueSaleAfterBlock(false, radioItemData.identifier, false);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.GargoRadioGridNote.2
                final /* synthetic */ int val$index;
                final /* synthetic */ GargoRadioGridView.RadioItemData val$itemData;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass2(GargoRadioGridView.RadioItemData radioItemData2, int i2) {
                    radioItemData = radioItemData2;
                    i = i2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr == null || bArr.length <= 0) {
                        if (GargoRadioGridNote.this.loadingDialog == null || !GargoRadioGridNote.this.loadingDialog.isShowing()) {
                            return;
                        }
                        GargoRadioGridNote.this.loadingDialog.dismiss();
                        return;
                    }
                    GargoRadioGridNote.this.gargoRadioGridView.upDateState(radioItemData.identifier, ObjectHelper.intFromBytes(bArr, 0, 1));
                    GargoRadioGridNote.this.readCargoCardWholeLayerState(i + 1);
                }
            });
            return;
        }
        LoadingDialog loadingDialog = this.loadingDialog;
        if (loadingDialog == null || !loadingDialog.isShowing()) {
            return;
        }
        this.loadingDialog.dismiss();
    }

    /* renamed from: com.shj.setting.generator.GargoRadioGridNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnCommandAnswerListener {
        final /* synthetic */ int val$index;
        final /* synthetic */ GargoRadioGridView.RadioItemData val$itemData;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass2(GargoRadioGridView.RadioItemData radioItemData2, int i2) {
            radioItemData = radioItemData2;
            i = i2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr == null || bArr.length <= 0) {
                if (GargoRadioGridNote.this.loadingDialog == null || !GargoRadioGridNote.this.loadingDialog.isShowing()) {
                    return;
                }
                GargoRadioGridNote.this.loadingDialog.dismiss();
                return;
            }
            GargoRadioGridNote.this.gargoRadioGridView.upDateState(radioItemData.identifier, ObjectHelper.intFromBytes(bArr, 0, 1));
            GargoRadioGridNote.this.readCargoCardWholeLayerState(i + 1);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        int i = this.settingType;
        if (i == 154) {
            List<GargoRadioGridView.RadioItemData> selectData = this.gargoRadioGridView.getSelectData(true);
            if (selectData != null && selectData.size() > 0) {
                MutilTextTipDialog mutilTextTipDialog = this.mutilTextTipDialog;
                if (mutilTextTipDialog != null) {
                    mutilTextTipDialog.clearText();
                }
                cargoSetup(selectData, 0);
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.setting));
            return;
        }
        if (i != 292) {
            return;
        }
        List<GargoRadioGridView.RadioItemData> selectData2 = this.gargoRadioGridView.getSelectData(false);
        String str = "";
        if (selectData2 != null && selectData2.size() > 0) {
            for (int i2 = 0; i2 < selectData2.size(); i2++) {
                str = str + selectData2.get(i2).identifier + ":" + selectData2.get(i2).selectState;
                if (i2 != selectData2.size() - 1) {
                    str = str + VoiceWakeuperAidl.PARAMS_SEPARATE;
                }
            }
        }
        if (!TextUtils.isEmpty(str)) {
            AppSetting.saveBoxRiceMachineCabinetSetting(this.context, str, null);
        }
        ToastUitl.showShort(this.context, this.context.getString(R.string.save_success));
    }

    public GargoRadioGridView.GridRadioItemData getGridRadioItemData() {
        this.gridRadioItemData = new GargoRadioGridView.GridRadioItemData();
        int i = this.settingType;
        int i2 = 0;
        if (i == 154) {
            this.gridRadioItemData.radioItemData = new ArrayList();
            List<Integer> list = SettingActivity.getBasicMachineInfo().layerNumberMap.get(Integer.valueOf(this.cabinetNumber));
            if (list != null) {
                while (i2 < list.size()) {
                    GargoRadioGridView.RadioItemData radioItemData = new GargoRadioGridView.RadioItemData();
                    radioItemData.identifier = list.get(i2).intValue();
                    this.gridRadioItemData.radioItemData.add(radioItemData);
                    i2++;
                }
                GargoRadioGridView.GridRadioItemData gridRadioItemData = this.gridRadioItemData;
                gridRadioItemData.totalCount = gridRadioItemData.radioItemData.size();
                this.gridRadioItemData.index_name = this.context.getResources().getString(R.string.layer_number);
                this.gridRadioItemData.unable_name = this.context.getResources().getString(R.string.cannot_buy);
                this.gridRadioItemData.enable_name = this.context.getResources().getString(R.string.continue_to_buy);
            }
        } else if (i == 292) {
            this.gridRadioItemData.radioItemData = new ArrayList();
            List<String> list2 = SettingActivity.getBasicMachineInfo().cabinetNumberList;
            List<Integer> list3 = SettingActivity.getBasicMachineInfo().cabinetList;
            if (list2 != null) {
                while (i2 < list2.size()) {
                    GargoRadioGridView.RadioItemData radioItemData2 = new GargoRadioGridView.RadioItemData();
                    radioItemData2.identifier = list3.get(i2).intValue();
                    radioItemData2.identifierName = list2.get(i2);
                    this.gridRadioItemData.radioItemData.add(radioItemData2);
                    i2++;
                }
                GargoRadioGridView.GridRadioItemData gridRadioItemData2 = this.gridRadioItemData;
                gridRadioItemData2.totalCount = gridRadioItemData2.radioItemData.size();
                this.gridRadioItemData.index_name = this.context.getResources().getString(R.string.cabinet_number);
                this.gridRadioItemData.unable_name = this.context.getResources().getString(R.string.lab_machinetype_normal);
                this.gridRadioItemData.enable_name = this.context.getResources().getString(R.string.box_lunch_machine);
            }
        }
        return this.gridRadioItemData;
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        GargoRadioGridView gargoRadioGridView = new GargoRadioGridView(this.context, getGridRadioItemData());
        this.gargoRadioGridView = gargoRadioGridView;
        gargoRadioGridView.setTitle(getSettingName());
        this.gargoRadioGridView.setTitleVisibility(0);
        this.gargoRadioGridView.setEventListener(this.eventListener);
        showQueryButton();
        return this.gargoRadioGridView;
    }

    private void showQueryButton() {
        if (this.settingType != 154) {
            return;
        }
        this.gargoRadioGridView.setQueryButtonVIsibility(0);
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.gargoRadioGridView;
    }
}
