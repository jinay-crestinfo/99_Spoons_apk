package com.shj.setting.generator;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.oysb.utils.Event.BaseEvent;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.biz.ReportManager;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.MutilTextTipDialog;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.Constant;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.event.SettingTypeEvent;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.GargoCheckGridView;
import com.shj.setting.widget.GridCheckItemView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* loaded from: classes.dex */
public class CargoCheckGridNote extends SettingNote {
    private int cabinetNumber;
    private GargoCheckGridView cargoCheckGridView;
    private GargoCheckGridView.GridCheckItemData gridItemData;
    private Handler handler;
    private int index;
    private LoadingDialog loadingDialog;
    private MutilTextTipDialog mutilTextTipDialog;

    public void queryData() {
    }

    static /* synthetic */ int access$708(CargoCheckGridNote cargoCheckGridNote) {
        int i = cargoCheckGridNote.index;
        cargoCheckGridNote.index = i + 1;
        return i;
    }

    public CargoCheckGridNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
        this.handler = new Handler() { // from class: com.shj.setting.generator.CargoCheckGridNote.5
            AnonymousClass5() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                CargoCheckGridNote.access$708(CargoCheckGridNote.this);
                CargoCheckGridNote.this.nextDropLayerState();
            }
        };
    }

    public void setDropInspection(List<GargoCheckGridView.CheckData> list, int i) {
        GargoCheckGridView.CheckData checkData = list.get(i);
        ReportManager.reportSetDropCheck(checkData.identifier == 100 ? 3 : 2, checkData.identifier, checkData.selectState ? 1 : 0);
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setGoodsDroopCheck(true, checkData.identifier, checkData.selectState);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.CargoCheckGridNote.1
            final /* synthetic */ GargoCheckGridView.CheckData val$checkData;
            final /* synthetic */ List val$dropList;
            final /* synthetic */ int val$index;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass1(GargoCheckGridView.CheckData checkData2, int i2, List list2) {
                checkData = checkData2;
                i = i2;
                list = list2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                String str;
                if (CargoCheckGridNote.this.mutilTextTipDialog != null) {
                    CargoCheckGridNote.this.mutilTextTipDialog.show();
                } else {
                    CargoCheckGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(CargoCheckGridNote.this.context);
                    CargoCheckGridNote.this.mutilTextTipDialog.show();
                }
                String str2 = "" + CargoCheckGridNote.this.context.getString(R.string.layer_number) + ":" + checkData.identifier + "  ";
                if (checkData.selectState) {
                    str = str2 + CargoCheckGridNote.this.context.getString(R.string.lab_enable) + "  ";
                } else {
                    str = str2 + CargoCheckGridNote.this.context.getString(R.string.lab_disable) + "  ";
                }
                if (z) {
                    CargoCheckGridNote.this.mutilTextTipDialog.addTextShow(str + CargoCheckGridNote.this.context.getString(R.string.setting_success));
                    CargoCheckGridNote.this.cargoCheckGridView.upDataView(checkData.identifier, checkData.selectState);
                } else {
                    CargoCheckGridNote.this.mutilTextTipDialog.addTextShow(str + CargoCheckGridNote.this.context.getString(R.string.setting_fail));
                }
                if (i + 1 < list.size()) {
                    CargoCheckGridNote.this.setDropInspection(list, i + 1);
                } else {
                    CargoCheckGridNote.this.mutilTextTipDialog.addTextShow(CargoCheckGridNote.this.context.getString(R.string.setting_compelete));
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.CargoCheckGridNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnCommandAnswerListener {
        final /* synthetic */ GargoCheckGridView.CheckData val$checkData;
        final /* synthetic */ List val$dropList;
        final /* synthetic */ int val$index;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1(GargoCheckGridView.CheckData checkData2, int i2, List list2) {
            checkData = checkData2;
            i = i2;
            list = list2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            String str;
            if (CargoCheckGridNote.this.mutilTextTipDialog != null) {
                CargoCheckGridNote.this.mutilTextTipDialog.show();
            } else {
                CargoCheckGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(CargoCheckGridNote.this.context);
                CargoCheckGridNote.this.mutilTextTipDialog.show();
            }
            String str2 = "" + CargoCheckGridNote.this.context.getString(R.string.layer_number) + ":" + checkData.identifier + "  ";
            if (checkData.selectState) {
                str = str2 + CargoCheckGridNote.this.context.getString(R.string.lab_enable) + "  ";
            } else {
                str = str2 + CargoCheckGridNote.this.context.getString(R.string.lab_disable) + "  ";
            }
            if (z) {
                CargoCheckGridNote.this.mutilTextTipDialog.addTextShow(str + CargoCheckGridNote.this.context.getString(R.string.setting_success));
                CargoCheckGridNote.this.cargoCheckGridView.upDataView(checkData.identifier, checkData.selectState);
            } else {
                CargoCheckGridNote.this.mutilTextTipDialog.addTextShow(str + CargoCheckGridNote.this.context.getString(R.string.setting_fail));
            }
            if (i + 1 < list.size()) {
                CargoCheckGridNote.this.setDropInspection(list, i + 1);
            } else {
                CargoCheckGridNote.this.mutilTextTipDialog.addTextShow(CargoCheckGridNote.this.context.getString(R.string.setting_compelete));
            }
        }
    }

    public void circleSetting(List<GargoCheckGridView.CheckData> list, int i, boolean z) {
        if (i < list.size()) {
            GargoCheckGridView.CheckData checkData = list.get(i);
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            if (z) {
                if (this.cabinetNumber == 0) {
                    commandV2_Up_SetCommand.setBlock41(true, checkData.identifier, !checkData.selectState);
                } else {
                    commandV2_Up_SetCommand.setBlock41(true, checkData.identifier, checkData.selectState);
                }
            } else if (this.cabinetNumber == 0) {
                commandV2_Up_SetCommand.setBlock41(true, checkData.identifier + 2000, !checkData.selectState);
            } else {
                commandV2_Up_SetCommand.setBlock41(true, checkData.identifier + 2000, checkData.selectState);
            }
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.CargoCheckGridNote.2
                final /* synthetic */ GargoCheckGridView.CheckData val$checkData;
                final /* synthetic */ int val$index;
                final /* synthetic */ boolean val$isSingle;
                final /* synthetic */ List val$siglelist;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass2(boolean z2, GargoCheckGridView.CheckData checkData2, int i2, List list2) {
                    z = z2;
                    checkData = checkData2;
                    i = i2;
                    list = list2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                    String str;
                    String str2;
                    String str3;
                    if (CargoCheckGridNote.this.mutilTextTipDialog != null) {
                        CargoCheckGridNote.this.mutilTextTipDialog.show();
                    } else {
                        CargoCheckGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(CargoCheckGridNote.this.context);
                        CargoCheckGridNote.this.mutilTextTipDialog.show();
                    }
                    if (z) {
                        str = ("" + CargoCheckGridNote.this.context.getString(R.string.lab_shelf)) + "  " + checkData.textIdentifier;
                    } else {
                        str = ("" + CargoCheckGridNote.this.context.getString(R.string.layer_number)) + "  " + checkData.identifier;
                    }
                    if (checkData.selectState) {
                        str2 = str + "  " + CargoCheckGridNote.this.context.getString(R.string.lab_enable);
                    } else {
                        str2 = str + "  " + CargoCheckGridNote.this.context.getString(R.string.lab_disable);
                    }
                    if (z2) {
                        str3 = str2 + StringUtils.SPACE + CargoCheckGridNote.this.context.getString(R.string.setting_success);
                        CargoCheckGridNote.this.cargoCheckGridView.upDataView(checkData.identifier, checkData.selectState);
                    } else {
                        str3 = str2 + StringUtils.SPACE + CargoCheckGridNote.this.context.getString(R.string.setting_fail);
                    }
                    CargoCheckGridNote.this.mutilTextTipDialog.addTextShow(str3);
                    if (i + 1 < list.size()) {
                        CargoCheckGridNote.this.circleSetting(list, i + 1, z);
                    } else {
                        CargoCheckGridNote.this.mutilTextTipDialog.addTextShow(CargoCheckGridNote.this.context.getString(R.string.setting_compelete));
                    }
                    if (z || !z2) {
                        return;
                    }
                    EventBus.getDefault().post(new SettingTypeEvent(CargoCheckGridNote.this.settingType, checkData));
                }
            });
        }
    }

    /* renamed from: com.shj.setting.generator.CargoCheckGridNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnCommandAnswerListener {
        final /* synthetic */ GargoCheckGridView.CheckData val$checkData;
        final /* synthetic */ int val$index;
        final /* synthetic */ boolean val$isSingle;
        final /* synthetic */ List val$siglelist;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass2(boolean z2, GargoCheckGridView.CheckData checkData2, int i2, List list2) {
            z = z2;
            checkData = checkData2;
            i = i2;
            list = list2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            String str;
            String str2;
            String str3;
            if (CargoCheckGridNote.this.mutilTextTipDialog != null) {
                CargoCheckGridNote.this.mutilTextTipDialog.show();
            } else {
                CargoCheckGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(CargoCheckGridNote.this.context);
                CargoCheckGridNote.this.mutilTextTipDialog.show();
            }
            if (z) {
                str = ("" + CargoCheckGridNote.this.context.getString(R.string.lab_shelf)) + "  " + checkData.textIdentifier;
            } else {
                str = ("" + CargoCheckGridNote.this.context.getString(R.string.layer_number)) + "  " + checkData.identifier;
            }
            if (checkData.selectState) {
                str2 = str + "  " + CargoCheckGridNote.this.context.getString(R.string.lab_enable);
            } else {
                str2 = str + "  " + CargoCheckGridNote.this.context.getString(R.string.lab_disable);
            }
            if (z2) {
                str3 = str2 + StringUtils.SPACE + CargoCheckGridNote.this.context.getString(R.string.setting_success);
                CargoCheckGridNote.this.cargoCheckGridView.upDataView(checkData.identifier, checkData.selectState);
            } else {
                str3 = str2 + StringUtils.SPACE + CargoCheckGridNote.this.context.getString(R.string.setting_fail);
            }
            CargoCheckGridNote.this.mutilTextTipDialog.addTextShow(str3);
            if (i + 1 < list.size()) {
                CargoCheckGridNote.this.circleSetting(list, i + 1, z);
            } else {
                CargoCheckGridNote.this.mutilTextTipDialog.addTextShow(CargoCheckGridNote.this.context.getString(R.string.setting_compelete));
            }
            if (z || !z2) {
                return;
            }
            EventBus.getDefault().post(new SettingTypeEvent(CargoCheckGridNote.this.settingType, checkData));
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        List<GargoCheckGridView.CheckBoxData> checkBoxDataList = this.cargoCheckGridView.getCheckBoxDataList();
        int i = this.settingType;
        if (i == 143) {
            ArrayList arrayList = new ArrayList();
            Iterator<GargoCheckGridView.CheckBoxData> it = checkBoxDataList.iterator();
            while (it.hasNext()) {
                Iterator<GridCheckItemView> it2 = it.next().itemViewList.iterator();
                while (it2.hasNext()) {
                    GargoCheckGridView.CheckData checkData = it2.next().getCheckData();
                    if (checkData.selectState != checkData.beforeSettingState) {
                        arrayList.add(checkData);
                    }
                }
            }
            if (arrayList.size() > 0) {
                MutilTextTipDialog mutilTextTipDialog = this.mutilTextTipDialog;
                if (mutilTextTipDialog != null) {
                    mutilTextTipDialog.clearText();
                }
                setDropInspection(arrayList, 0);
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.setting));
            return;
        }
        if (i == 150) {
            ArrayList arrayList2 = new ArrayList();
            Iterator<GargoCheckGridView.CheckBoxData> it3 = checkBoxDataList.iterator();
            while (it3.hasNext()) {
                Iterator<GridCheckItemView> it4 = it3.next().itemViewList.iterator();
                while (it4.hasNext()) {
                    GargoCheckGridView.CheckData checkData2 = it4.next().getCheckData();
                    if (checkData2.selectState != checkData2.beforeSettingState) {
                        arrayList2.add(checkData2);
                    }
                }
            }
            if (arrayList2.size() > 0) {
                MutilTextTipDialog mutilTextTipDialog2 = this.mutilTextTipDialog;
                if (mutilTextTipDialog2 != null) {
                    mutilTextTipDialog2.clearText();
                }
                circleSetting(arrayList2, 0, true);
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.setting));
            return;
        }
        if (i != 151) {
            return;
        }
        ArrayList arrayList3 = new ArrayList();
        Iterator<GargoCheckGridView.CheckBoxData> it5 = checkBoxDataList.iterator();
        while (it5.hasNext()) {
            for (GridCheckItemView gridCheckItemView : it5.next().itemViewList) {
                new CommandV2_Up_SetCommand();
                GargoCheckGridView.CheckData checkData3 = gridCheckItemView.getCheckData();
                if (checkData3.selectState != checkData3.beforeSettingState) {
                    arrayList3.add(checkData3);
                }
            }
        }
        if (arrayList3.size() > 0) {
            MutilTextTipDialog mutilTextTipDialog3 = this.mutilTextTipDialog;
            if (mutilTextTipDialog3 != null) {
                mutilTextTipDialog3.clearText();
            }
            circleSetting(arrayList3, 0, false);
            return;
        }
        ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.setting));
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        EventBus.getDefault().register(this);
        queryData();
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        GargoCheckGridView.GridCheckItemData gridCheckItemData;
        int i = this.settingType;
        if (i == 143) {
            readDropWholeLayerState();
            return;
        }
        if (i != 150) {
            if (i != 151 || (gridCheckItemData = this.gridItemData) == null || gridCheckItemData.checkDataList == null) {
                return;
            }
            LoadingDialog loadingDialog = new LoadingDialog(this.context);
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            readCircleWholeLayerState(0, false);
            return;
        }
        GargoCheckGridView.GridCheckItemData gridCheckItemData2 = this.gridItemData;
        if (gridCheckItemData2 == null || gridCheckItemData2.checkDataList == null) {
            return;
        }
        LoadingDialog loadingDialog2 = new LoadingDialog(this.context);
        this.loadingDialog = loadingDialog2;
        loadingDialog2.show();
        readCircleWholeLayerState(0, true);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        Object data;
        Object data2;
        GargoCheckGridView.GridCheckItemData gridCheckItemData;
        Object data3;
        GargoCheckGridView.GridCheckItemData gridCheckItemData2;
        if (baseEvent instanceof SettingTypeEvent) {
            SettingTypeEvent settingTypeEvent = (SettingTypeEvent) baseEvent;
            int i = this.settingType;
            if (i == 143) {
                if (settingTypeEvent.getSettingType() != 144 || (data = settingTypeEvent.getData()) == null) {
                    return;
                }
                if (data instanceof String) {
                    if (Constant.READ_DROP_WHOLE_COMPELTE.equals((String) data)) {
                        readDropWholeLayerState();
                        return;
                    }
                    return;
                } else {
                    if (data instanceof Boolean) {
                        this.cargoCheckGridView.upDateView(((Boolean) data).booleanValue());
                        return;
                    }
                    return;
                }
            }
            if (i != 150) {
                if (i == 151 && settingTypeEvent.getSettingType() == 152 && (data3 = settingTypeEvent.getData()) != null) {
                    if (data3 instanceof String) {
                        if (!Constant.READ_CIRCLE_WHOLE_COMPELTE.equals((String) data3) || (gridCheckItemData2 = this.gridItemData) == null || gridCheckItemData2.checkDataList == null) {
                            return;
                        }
                        LoadingDialog loadingDialog = new LoadingDialog(this.context);
                        this.loadingDialog = loadingDialog;
                        loadingDialog.show();
                        readCircleWholeLayerState(0, false);
                        return;
                    }
                    if (data3 instanceof Boolean) {
                        this.cargoCheckGridView.upDateView(((Boolean) data3).booleanValue());
                        return;
                    }
                    return;
                }
                return;
            }
            if (settingTypeEvent.getSettingType() == 151) {
                Object data4 = settingTypeEvent.getData();
                if (data4 != null) {
                    if (data4 instanceof String) {
                        if (!Constant.READ_CIRCLE_WHOLE_LAYER_COMPELTE.equals((String) data4) || (gridCheckItemData = this.gridItemData) == null || gridCheckItemData.checkDataList == null) {
                            return;
                        }
                        LoadingDialog loadingDialog2 = new LoadingDialog(this.context);
                        this.loadingDialog = loadingDialog2;
                        loadingDialog2.show();
                        readCircleWholeLayerState(0, true);
                        return;
                    }
                    if (data4 instanceof GargoCheckGridView.CheckData) {
                        GargoCheckGridView.CheckData checkData = (GargoCheckGridView.CheckData) data4;
                        this.cargoCheckGridView.upDataLayerView(checkData.identifier, checkData.selectState);
                        return;
                    }
                    return;
                }
                return;
            }
            if (settingTypeEvent.getSettingType() == 152 && (data2 = settingTypeEvent.getData()) != null && (data2 instanceof Boolean)) {
                this.cargoCheckGridView.upDateView(((Boolean) data2).booleanValue());
            }
        }
    }

    public void readCircleWholeLayerState(int i, boolean z) {
        if (i < this.gridItemData.checkDataList.size()) {
            GargoCheckGridView.CheckData checkData = this.gridItemData.checkDataList.get(i);
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            if (z) {
                commandV2_Up_SetCommand.setBlock41(false, checkData.identifier, false);
            } else {
                commandV2_Up_SetCommand.setBlock41(false, checkData.identifier + 2000, false);
            }
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.CargoCheckGridNote.3
                final /* synthetic */ GargoCheckGridView.CheckData val$checkData;
                final /* synthetic */ int val$index;
                final /* synthetic */ boolean val$isSignle;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                }

                AnonymousClass3(GargoCheckGridView.CheckData checkData2, int i2, boolean z2) {
                    checkData = checkData2;
                    i = i2;
                    z = z2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                        boolean z2 = ObjectHelper.intFromBytes(bArr, 1, 1) == 0;
                        if (CargoCheckGridNote.this.cabinetNumber == 0) {
                            CargoCheckGridNote.this.cargoCheckGridView.upDataView(checkData.identifier, !z2);
                        } else {
                            CargoCheckGridNote.this.cargoCheckGridView.upDataView(checkData.identifier, z2);
                        }
                        CargoCheckGridNote.this.readCircleWholeLayerState(i + 1, z);
                        return;
                    }
                    ToastUitl.showShort(CargoCheckGridNote.this.context, R.string.communication_error);
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
        EventBus.getDefault().post(new SettingTypeEvent(this.settingType, Constant.READ_CIRCLE_WHOLE_LAYER_COMPELTE));
    }

    /* renamed from: com.shj.setting.generator.CargoCheckGridNote$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnCommandAnswerListener {
        final /* synthetic */ GargoCheckGridView.CheckData val$checkData;
        final /* synthetic */ int val$index;
        final /* synthetic */ boolean val$isSignle;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass3(GargoCheckGridView.CheckData checkData2, int i2, boolean z2) {
            checkData = checkData2;
            i = i2;
            z = z2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                boolean z2 = ObjectHelper.intFromBytes(bArr, 1, 1) == 0;
                if (CargoCheckGridNote.this.cabinetNumber == 0) {
                    CargoCheckGridNote.this.cargoCheckGridView.upDataView(checkData.identifier, !z2);
                } else {
                    CargoCheckGridNote.this.cargoCheckGridView.upDataView(checkData.identifier, z2);
                }
                CargoCheckGridNote.this.readCircleWholeLayerState(i + 1, z);
                return;
            }
            ToastUitl.showShort(CargoCheckGridNote.this.context, R.string.communication_error);
        }
    }

    private void readDropWholeLayerState() {
        this.index = -1;
        GargoCheckGridView.GridCheckItemData gridCheckItemData = this.gridItemData;
        if (gridCheckItemData == null || gridCheckItemData.checkDataList == null) {
            return;
        }
        LoadingDialog loadingDialog = new LoadingDialog(this.context);
        this.loadingDialog = loadingDialog;
        loadingDialog.show();
        this.handler.sendEmptyMessageDelayed(0, 10L);
    }

    public void nextDropLayerState() {
        if (this.index < this.gridItemData.checkDataList.size()) {
            int i = this.gridItemData.checkDataList.get(this.index).identifier;
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setGoodsDroopCheck(false, i, false);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.CargoCheckGridNote.4
                final /* synthetic */ int val$identifier;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass4(int i2) {
                    i = i2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    CargoCheckGridNote.this.cargoCheckGridView.upDataView(i, ObjectHelper.intFromBytes(bArr, 0, 1) == 1);
                    CargoCheckGridNote.this.handler.sendEmptyMessageDelayed(0, 10L);
                }
            });
            return;
        }
        LoadingDialog loadingDialog = this.loadingDialog;
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.CargoCheckGridNote$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements OnCommandAnswerListener {
        final /* synthetic */ int val$identifier;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass4(int i2) {
            i = i2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            CargoCheckGridNote.this.cargoCheckGridView.upDataView(i, ObjectHelper.intFromBytes(bArr, 0, 1) == 1);
            CargoCheckGridNote.this.handler.sendEmptyMessageDelayed(0, 10L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.generator.CargoCheckGridNote$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 extends Handler {
        AnonymousClass5() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            CargoCheckGridNote.access$708(CargoCheckGridNote.this);
            CargoCheckGridNote.this.nextDropLayerState();
        }
    }

    private GargoCheckGridView.GridCheckItemData getGridItemData() {
        this.gridItemData = new GargoCheckGridView.GridCheckItemData();
        int i = this.settingType;
        int i2 = 0;
        if (i == 143) {
            List<Integer> list = SettingActivity.getBasicMachineInfo().layerNumberMap.get(Integer.valueOf(this.cabinetNumber));
            this.gridItemData.checkDataList = new ArrayList();
            if (list != null) {
                while (i2 < list.size()) {
                    GargoCheckGridView.CheckData checkData = new GargoCheckGridView.CheckData();
                    checkData.identifier = list.get(i2).intValue();
                    this.gridItemData.checkDataList.add(checkData);
                    i2++;
                }
                GargoCheckGridView.GridCheckItemData gridCheckItemData = this.gridItemData;
                gridCheckItemData.totalCount = gridCheckItemData.checkDataList.size();
                this.gridItemData.index_name = this.context.getResources().getString(R.string.layer_number);
            }
        } else if (i == 150) {
            List<Integer> list2 = SettingActivity.getBasicMachineInfo().shelvesMap.get(Integer.valueOf(this.cabinetNumber));
            this.gridItemData.checkDataList = new ArrayList();
            if (list2 != null) {
                for (int i3 = 0; i3 < list2.size(); i3++) {
                    GargoCheckGridView.CheckData checkData2 = new GargoCheckGridView.CheckData();
                    checkData2.identifier = list2.get(i3).intValue();
                    checkData2.upperLevelNumber = Shj.getLayerByShelf(checkData2.identifier);
                    checkData2.textIdentifier = String.format("%03d", list2.get(i3));
                    this.gridItemData.checkDataList.add(checkData2);
                }
                GargoCheckGridView.GridCheckItemData gridCheckItemData2 = this.gridItemData;
                gridCheckItemData2.totalCount = gridCheckItemData2.checkDataList.size();
                this.gridItemData.index_name = this.context.getResources().getString(R.string.lab_shelf);
            }
        } else if (i == 151) {
            List<Integer> list3 = SettingActivity.getBasicMachineInfo().layerNumberMap.get(Integer.valueOf(this.cabinetNumber));
            this.gridItemData.checkDataList = new ArrayList();
            if (list3 != null) {
                while (i2 < list3.size()) {
                    GargoCheckGridView.CheckData checkData3 = new GargoCheckGridView.CheckData();
                    checkData3.identifier = list3.get(i2).intValue();
                    this.gridItemData.checkDataList.add(checkData3);
                    i2++;
                }
                GargoCheckGridView.GridCheckItemData gridCheckItemData3 = this.gridItemData;
                gridCheckItemData3.totalCount = gridCheckItemData3.checkDataList.size();
                this.gridItemData.index_name = this.context.getResources().getString(R.string.layer_number);
            }
        }
        return this.gridItemData;
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        GargoCheckGridView gargoCheckGridView = new GargoCheckGridView(this.context, getGridItemData());
        this.cargoCheckGridView = gargoCheckGridView;
        gargoCheckGridView.setTitle(getSettingName());
        this.cargoCheckGridView.setTitleVisibility(0);
        this.cargoCheckGridView.setEventListener(this.eventListener);
        showQueryButton();
        return this.cargoCheckGridView;
    }

    public void showQueryButton() {
        int i = this.settingType;
        if (i == 143 || i == 150 || i == 151) {
            this.cargoCheckGridView.setQueryButtonVIsibility(0);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        GargoCheckGridView gargoCheckGridView = this.cargoCheckGridView;
        if (gargoCheckGridView != null) {
            return gargoCheckGridView;
        }
        return null;
    }
}
