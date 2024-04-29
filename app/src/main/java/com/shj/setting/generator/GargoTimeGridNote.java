package com.shj.setting.generator;

import android.content.Context;
import android.view.View;
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
import com.shj.setting.widget.GargoTimeGridView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* loaded from: classes.dex */
public class GargoTimeGridNote extends SettingNote {
    private int cabinetNumber;
    private GargoTimeGridView gargoTimeGridView;
    private GargoTimeGridView.GridTimeItemData itemData;
    private LoadingDialog loadingDialog;
    private MutilTextTipDialog mutilTextTipDialog;

    public GargoTimeGridNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    public void beltSetting(List<GargoTimeGridView.TimeItemData> list, int i, boolean z) {
        if (i < list.size()) {
            GargoTimeGridView.TimeItemData timeItemData = list.get(i);
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            if (timeItemData.star != -1 && timeItemData.end != -1) {
                if (z) {
                    commandV2_Up_SetCommand.setBeltTime(true, timeItemData.identifier, timeItemData.star, timeItemData.end);
                } else {
                    commandV2_Up_SetCommand.setBeltTime(true, timeItemData.identifier + 2000, timeItemData.star, timeItemData.end);
                }
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.GargoTimeGridNote.1
                    final /* synthetic */ GargoTimeGridView.TimeItemData val$data;
                    final /* synthetic */ int val$index;
                    final /* synthetic */ boolean val$isSingle;
                    final /* synthetic */ List val$siglelist;

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass1(boolean z2, GargoTimeGridView.TimeItemData timeItemData2, int i2, List list2) {
                        z = z2;
                        timeItemData = timeItemData2;
                        i = i2;
                        list = list2;
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        String str;
                        String str2;
                        if (GargoTimeGridNote.this.mutilTextTipDialog != null) {
                            GargoTimeGridNote.this.mutilTextTipDialog.show();
                        } else {
                            GargoTimeGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(GargoTimeGridNote.this.context);
                            GargoTimeGridNote.this.mutilTextTipDialog.show();
                        }
                        if (z) {
                            str = ("" + GargoTimeGridNote.this.context.getString(R.string.lab_shelf)) + "  " + timeItemData.textIdentifier;
                        } else {
                            str = ("" + GargoTimeGridNote.this.context.getString(R.string.layer_number)) + "  " + timeItemData.identifier;
                        }
                        String str3 = (str + StringUtils.SPACE + GargoTimeGridNote.this.context.getString(R.string.time_over) + StringUtils.SPACE + timeItemData.star) + StringUtils.SPACE + GargoTimeGridNote.this.context.getString(R.string.time_stop) + StringUtils.SPACE + timeItemData.end;
                        if (z2) {
                            str2 = str3 + StringUtils.SPACE + GargoTimeGridNote.this.context.getString(R.string.setting_success);
                            GargoTimeGridNote.this.gargoTimeGridView.setTimeItemData(timeItemData.identifier, String.valueOf(timeItemData.star), String.valueOf(timeItemData.end));
                        } else {
                            str2 = str3 + StringUtils.SPACE + GargoTimeGridNote.this.context.getString(R.string.setting_fail);
                        }
                        GargoTimeGridNote.this.mutilTextTipDialog.addTextShow(str2);
                        if (i + 1 < list.size()) {
                            GargoTimeGridNote.this.beltSetting(list, i + 1, z);
                        } else {
                            GargoTimeGridNote.this.mutilTextTipDialog.addTextShow(GargoTimeGridNote.this.context.getString(R.string.setting_compelete));
                        }
                        if (z || !z2) {
                            return;
                        }
                        EventBus.getDefault().post(new SettingTypeEvent(GargoTimeGridNote.this.settingType, timeItemData));
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
            beltSetting(list2, i2, z2);
        }
    }

    /* renamed from: com.shj.setting.generator.GargoTimeGridNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnCommandAnswerListener {
        final /* synthetic */ GargoTimeGridView.TimeItemData val$data;
        final /* synthetic */ int val$index;
        final /* synthetic */ boolean val$isSingle;
        final /* synthetic */ List val$siglelist;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1(boolean z2, GargoTimeGridView.TimeItemData timeItemData2, int i2, List list2) {
            z = z2;
            timeItemData = timeItemData2;
            i = i2;
            list = list2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            String str;
            String str2;
            if (GargoTimeGridNote.this.mutilTextTipDialog != null) {
                GargoTimeGridNote.this.mutilTextTipDialog.show();
            } else {
                GargoTimeGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(GargoTimeGridNote.this.context);
                GargoTimeGridNote.this.mutilTextTipDialog.show();
            }
            if (z) {
                str = ("" + GargoTimeGridNote.this.context.getString(R.string.lab_shelf)) + "  " + timeItemData.textIdentifier;
            } else {
                str = ("" + GargoTimeGridNote.this.context.getString(R.string.layer_number)) + "  " + timeItemData.identifier;
            }
            String str3 = (str + StringUtils.SPACE + GargoTimeGridNote.this.context.getString(R.string.time_over) + StringUtils.SPACE + timeItemData.star) + StringUtils.SPACE + GargoTimeGridNote.this.context.getString(R.string.time_stop) + StringUtils.SPACE + timeItemData.end;
            if (z2) {
                str2 = str3 + StringUtils.SPACE + GargoTimeGridNote.this.context.getString(R.string.setting_success);
                GargoTimeGridNote.this.gargoTimeGridView.setTimeItemData(timeItemData.identifier, String.valueOf(timeItemData.star), String.valueOf(timeItemData.end));
            } else {
                str2 = str3 + StringUtils.SPACE + GargoTimeGridNote.this.context.getString(R.string.setting_fail);
            }
            GargoTimeGridNote.this.mutilTextTipDialog.addTextShow(str2);
            if (i + 1 < list.size()) {
                GargoTimeGridNote.this.beltSetting(list, i + 1, z);
            } else {
                GargoTimeGridNote.this.mutilTextTipDialog.addTextShow(GargoTimeGridNote.this.context.getString(R.string.setting_compelete));
            }
            if (z || !z2) {
                return;
            }
            EventBus.getDefault().post(new SettingTypeEvent(GargoTimeGridNote.this.settingType, timeItemData));
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        new CommandV2_Up_SetCommand();
        int i = this.settingType;
        if (i == 146) {
            List<GargoTimeGridView.TimeItemData> timeItemDataList = this.gargoTimeGridView.getTimeItemDataList();
            if (timeItemDataList != null && timeItemDataList.size() > 0) {
                MutilTextTipDialog mutilTextTipDialog = this.mutilTextTipDialog;
                if (mutilTextTipDialog != null) {
                    mutilTextTipDialog.clearText();
                }
                beltSetting(timeItemDataList, 0, true);
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.setting));
            return;
        }
        if (i != 147) {
            return;
        }
        List<GargoTimeGridView.TimeItemData> timeItemDataList2 = this.gargoTimeGridView.getTimeItemDataList();
        if (timeItemDataList2 != null && timeItemDataList2.size() > 0) {
            MutilTextTipDialog mutilTextTipDialog2 = this.mutilTextTipDialog;
            if (mutilTextTipDialog2 != null) {
                mutilTextTipDialog2.clearText();
            }
            beltSetting(timeItemDataList2, 0, false);
            return;
        }
        ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.setting));
    }

    private GargoTimeGridView.GridTimeItemData getGridTimeItemData() {
        this.itemData = new GargoTimeGridView.GridTimeItemData();
        int i = this.settingType;
        if (i == 146) {
            this.itemData.timeItemDataList = new ArrayList();
            List<Integer> list = SettingActivity.getBasicMachineInfo().shelvesMap.get(Integer.valueOf(this.cabinetNumber));
            if (list != null) {
                for (int i2 = 0; i2 < list.size(); i2++) {
                    GargoTimeGridView.TimeItemData timeItemData = new GargoTimeGridView.TimeItemData();
                    timeItemData.identifier = list.get(i2).intValue();
                    timeItemData.textIdentifier = String.format("%03d", list.get(i2));
                    timeItemData.upperLevelNumber = Shj.getLayerByShelf(timeItemData.identifier);
                    this.itemData.timeItemDataList.add(timeItemData);
                }
                GargoTimeGridView.GridTimeItemData gridTimeItemData = this.itemData;
                gridTimeItemData.totalCount = gridTimeItemData.timeItemDataList.size();
                this.itemData.index_name = this.context.getString(R.string.lab_shelf);
                this.itemData.time_start = this.context.getString(R.string.time_over);
                this.itemData.time_end = this.context.getString(R.string.time_stop);
            }
        } else if (i == 147) {
            this.itemData.timeItemDataList = new ArrayList();
            List<Integer> list2 = SettingActivity.getBasicMachineInfo().layerNumberMap.get(Integer.valueOf(this.cabinetNumber));
            if (list2 != null) {
                for (int i3 = 0; i3 < list2.size(); i3++) {
                    GargoTimeGridView.TimeItemData timeItemData2 = new GargoTimeGridView.TimeItemData();
                    timeItemData2.identifier = list2.get(i3).intValue();
                    this.itemData.timeItemDataList.add(timeItemData2);
                }
                GargoTimeGridView.GridTimeItemData gridTimeItemData2 = this.itemData;
                gridTimeItemData2.totalCount = gridTimeItemData2.timeItemDataList.size();
                this.itemData.index_name = this.context.getString(R.string.layer_number);
                this.itemData.time_start = this.context.getString(R.string.time_over);
                this.itemData.time_end = this.context.getString(R.string.time_stop);
            }
        }
        return this.itemData;
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        EventBus.getDefault().register(this);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        int i = this.settingType;
        if (i == 146) {
            LoadingDialog loadingDialog = new LoadingDialog(this.context);
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            readBeltState(0, true);
            return;
        }
        if (i != 147) {
            return;
        }
        LoadingDialog loadingDialog2 = new LoadingDialog(this.context);
        this.loadingDialog = loadingDialog2;
        loadingDialog2.show();
        readBeltState(0, false);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        Object data;
        GargoTimeGridView.GridTimeItemData gridTimeItemData;
        Object data2;
        if (baseEvent instanceof SettingTypeEvent) {
            SettingTypeEvent settingTypeEvent = (SettingTypeEvent) baseEvent;
            int i = this.settingType;
            if (i != 146) {
                if (i == 147 && settingTypeEvent.getSettingType() == 148 && (data2 = settingTypeEvent.getData()) != null) {
                    if (data2 instanceof String) {
                        if (Constant.READ_BELT_WHOLE_COMPELTE.equals((String) data2)) {
                            LoadingDialog loadingDialog = new LoadingDialog(this.context);
                            this.loadingDialog = loadingDialog;
                            loadingDialog.show();
                            readBeltState(0, false);
                            return;
                        }
                        return;
                    }
                    if (data2 instanceof GargoTimeGridView.TimeItemData) {
                        GargoTimeGridView.TimeItemData timeItemData = (GargoTimeGridView.TimeItemData) data2;
                        this.gargoTimeGridView.setTimeData(String.valueOf(timeItemData.star), String.valueOf(timeItemData.end));
                        return;
                    }
                    return;
                }
                return;
            }
            if (settingTypeEvent.getSettingType() == 147) {
                Object data3 = settingTypeEvent.getData();
                if (data3 != null) {
                    if (data3 instanceof String) {
                        if (!Constant.READ_BELT_WHOLE_LAYER_COMPELTE.equals((String) data3) || (gridTimeItemData = this.itemData) == null || gridTimeItemData.timeItemDataList == null) {
                            return;
                        }
                        LoadingDialog loadingDialog2 = new LoadingDialog(this.context);
                        this.loadingDialog = loadingDialog2;
                        loadingDialog2.show();
                        readBeltState(0, true);
                        return;
                    }
                    if (data3 instanceof GargoTimeGridView.TimeItemData) {
                        GargoTimeGridView.TimeItemData timeItemData2 = (GargoTimeGridView.TimeItemData) data3;
                        this.gargoTimeGridView.setTimeLayerData(timeItemData2.identifier, String.valueOf(timeItemData2.star), String.valueOf(timeItemData2.end));
                        return;
                    }
                    return;
                }
                return;
            }
            if (settingTypeEvent.getSettingType() == 148 && (data = settingTypeEvent.getData()) != null && (data instanceof GargoTimeGridView.TimeItemData)) {
                GargoTimeGridView.TimeItemData timeItemData3 = (GargoTimeGridView.TimeItemData) data;
                this.gargoTimeGridView.setTimeData(String.valueOf(timeItemData3.star), String.valueOf(timeItemData3.end));
            }
        }
    }

    public void readBeltState(int i, boolean z) {
        if (i < this.itemData.timeItemDataList.size()) {
            GargoTimeGridView.TimeItemData timeItemData = this.itemData.timeItemDataList.get(i);
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            if (z) {
                commandV2_Up_SetCommand.setBeltTime(false, timeItemData.identifier, 0, 0);
            } else {
                commandV2_Up_SetCommand.setBeltTime(false, timeItemData.identifier + 2000, 0, 0);
            }
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.GargoTimeGridNote.2
                final /* synthetic */ int val$index;
                final /* synthetic */ boolean val$isSigle;
                final /* synthetic */ GargoTimeGridView.TimeItemData val$timeItemData;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                }

                AnonymousClass2(GargoTimeGridView.TimeItemData timeItemData2, int i2, boolean z2) {
                    timeItemData = timeItemData2;
                    i = i2;
                    z = z2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr == null || bArr.length <= 0) {
                        GargoTimeGridNote.this.readBeltState(i + 1, z);
                        return;
                    }
                    if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                        GargoTimeGridNote.this.gargoTimeGridView.setTimeItemData(timeItemData.identifier, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)), String.valueOf(ObjectHelper.intFromBytes(bArr, 3, 2)));
                        GargoTimeGridNote.this.readBeltState(i + 1, z);
                        return;
                    }
                    ToastUitl.showShort(GargoTimeGridNote.this.context, R.string.communication_error);
                }
            });
            return;
        }
        LoadingDialog loadingDialog = this.loadingDialog;
        if (loadingDialog != null && loadingDialog.isShowing()) {
            this.loadingDialog.dismiss();
        }
        if (z2) {
            return;
        }
        EventBus.getDefault().post(new SettingTypeEvent(this.settingType, Constant.READ_BELT_WHOLE_LAYER_COMPELTE));
    }

    /* renamed from: com.shj.setting.generator.GargoTimeGridNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnCommandAnswerListener {
        final /* synthetic */ int val$index;
        final /* synthetic */ boolean val$isSigle;
        final /* synthetic */ GargoTimeGridView.TimeItemData val$timeItemData;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass2(GargoTimeGridView.TimeItemData timeItemData2, int i2, boolean z2) {
            timeItemData = timeItemData2;
            i = i2;
            z = z2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr == null || bArr.length <= 0) {
                GargoTimeGridNote.this.readBeltState(i + 1, z);
                return;
            }
            if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                GargoTimeGridNote.this.gargoTimeGridView.setTimeItemData(timeItemData.identifier, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)), String.valueOf(ObjectHelper.intFromBytes(bArr, 3, 2)));
                GargoTimeGridNote.this.readBeltState(i + 1, z);
                return;
            }
            ToastUitl.showShort(GargoTimeGridNote.this.context, R.string.communication_error);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        GargoTimeGridView gargoTimeGridView = new GargoTimeGridView(this.context, getGridTimeItemData());
        this.gargoTimeGridView = gargoTimeGridView;
        gargoTimeGridView.setTitle(getSettingName());
        this.gargoTimeGridView.setTitleVisibility(0);
        this.gargoTimeGridView.setEventListener(this.eventListener);
        showQueryButton();
        return this.gargoTimeGridView;
    }

    private void showQueryButton() {
        if (this.settingType == 147 || this.settingType == 146) {
            this.gargoTimeGridView.setQueryButtonVIsibility(0);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.gargoTimeGridView;
    }
}
