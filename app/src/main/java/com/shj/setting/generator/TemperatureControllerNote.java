package com.shj.setting.generator;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.oysb.utils.CommonTool;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.biz.ReportManager;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.MultipleEditItemView;
import com.shj.setting.widget.SpinnerItemView;
import com.shj.setting.widget.TemperatureControllerView;
import com.shj.setting.widget.TextItemView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class TemperatureControllerNote extends SettingNote {
    private TemperatureControllerView temperatureControllerView;

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
    }

    public TemperatureControllerNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        int selectCabinet = this.temperatureControllerView.getSelectCabinet();
        int wordingMode = this.temperatureControllerView.getWordingMode() + 1;
        String controlTemperature = this.temperatureControllerView.getControlTemperature();
        if (wordingMode == 4) {
            controlTemperature = "0";
        } else if (TextUtils.isEmpty(controlTemperature)) {
            ToastUitl.showNotInputTip(this.context);
            return;
        }
        int intValue = Integer.valueOf(controlTemperature).intValue();
        if (Shj.getPicker(0) != null) {
            if (wordingMode == 1) {
                wordingMode = 2;
            } else if (wordingMode == 2) {
                wordingMode = 1;
            }
        }
        LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.saveing);
        loadingDialog.show();
        commandV2_Up_SetCommand.setWkY(true, selectCabinet, wordingMode, intValue);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.TemperatureControllerNote.1
            final /* synthetic */ LoadingDialog val$loadingDialog;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass1(LoadingDialog loadingDialog2) {
                loadingDialog = loadingDialog2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z2) {
                ToastUitl.showCompeleteTip(TemperatureControllerNote.this.context, z2, TemperatureControllerNote.this.getSettingName());
                loadingDialog.dismiss();
            }
        });
        ReportManager.reportSetTemperature(intValue, wordingMode);
    }

    /* renamed from: com.shj.setting.generator.TemperatureControllerNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements OnCommandAnswerListener {
        final /* synthetic */ LoadingDialog val$loadingDialog;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1(LoadingDialog loadingDialog2) {
            loadingDialog = loadingDialog2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showCompeleteTip(TemperatureControllerNote.this.context, z2, TemperatureControllerNote.this.getSettingName());
            loadingDialog.dismiss();
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        queryData(false);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        queryData(true);
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        TemperatureControllerView temperatureControllerView = new TemperatureControllerView(this.context, getTemperatureControllerData());
        this.temperatureControllerView = temperatureControllerView;
        temperatureControllerView.setTitle(getSettingName());
        this.temperatureControllerView.setTitleVisibility(0);
        this.temperatureControllerView.setEventListener(this.eventListener);
        this.temperatureControllerView.setQueryButtonVIsibility(0);
        return this.temperatureControllerView;
    }

    private TemperatureControllerView.TemperatureControllerData getTemperatureControllerData() {
        TemperatureControllerView.TemperatureControllerData temperatureControllerData = new TemperatureControllerView.TemperatureControllerData();
        temperatureControllerData.cabinetNumberData = new SpinnerItemView.SpinnerItemData();
        temperatureControllerData.cabinetNumberData.name = this.context.getResources().getString(R.string.cabinet_number);
        temperatureControllerData.cabinetNumberData.dataList = SettingActivity.getBasicMachineInfo().cabinetNumberList;
        temperatureControllerData.workingModeData = new SpinnerItemView.SpinnerItemData();
        temperatureControllerData.workingModeData.name = this.context.getResources().getString(R.string.working_mode);
        temperatureControllerData.workingModeData.dataList = new ArrayList();
        temperatureControllerData.workingModeData.dataList.add(this.context.getResources().getString(R.string.temperature_controller_01));
        temperatureControllerData.workingModeData.dataList.add(this.context.getResources().getString(R.string.temperature_controller_02));
        temperatureControllerData.workingModeData.dataList.add(this.context.getResources().getString(R.string.temperature_controller_03));
        temperatureControllerData.workingModeData.dataList.add(this.context.getResources().getString(R.string.temperature_controller_04));
        temperatureControllerData.temperatureData = new MultipleEditItemView.EditTextDataInfo();
        temperatureControllerData.temperatureData.name = this.context.getResources().getString(R.string.control_temperature);
        if (CommonTool.getLanguage(this.context).equals("en") || CommonTool.getLanguage(this.context).equals("fr")) {
            temperatureControllerData.temperatureData.tipInfo = this.context.getResources().getString(R.string.please_input) + StringUtils.SPACE + temperatureControllerData.temperatureData.name;
        } else {
            temperatureControllerData.temperatureData.tipInfo = this.context.getResources().getString(R.string.please_input) + temperatureControllerData.temperatureData.name;
        }
        TextItemView.TextItemData textItemData = new TextItemView.TextItemData();
        textItemData.name = this.context.getResources().getString(R.string.temperature_controller_status);
        textItemData.value = "";
        temperatureControllerData.stateData = textItemData;
        return temperatureControllerData;
    }

    private void queryData(boolean z) {
        int selectCabinet = this.temperatureControllerView.getSelectCabinet();
        queryMode(selectCabinet, z);
        queryState(selectCabinet, z);
    }

    private void queryMode(int i, boolean z) {
        LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.on_querying);
        if (z) {
            loadingDialog.show();
        }
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setWkY(false, i, 0, 0);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.TemperatureControllerNote.2
            final /* synthetic */ int val$cabinet;
            final /* synthetic */ boolean val$isShowDialog;
            final /* synthetic */ LoadingDialog val$loadingDialog;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z2) {
            }

            AnonymousClass2(boolean z2, int i2, LoadingDialog loadingDialog2) {
                z = z2;
                i = i2;
                loadingDialog = loadingDialog2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr == null || bArr.length <= 0) {
                    return;
                }
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                    int signedIntFromByte = ObjectHelper.signedIntFromByte(bArr[2]);
                    ObjectHelper.signedIntFromByte(bArr[3]);
                    ObjectHelper.signedIntFromByte(bArr[4]);
                    TemperatureControllerNote.this.temperatureControllerView.setWorkingMode(intFromBytes - 1);
                    TemperatureControllerNote.this.temperatureControllerView.setControlTemperature(String.valueOf(signedIntFromByte));
                } else if (z) {
                    if (i == 0) {
                        ToastUitl.showShort(TemperatureControllerNote.this.context, R.string.main_communication_error);
                    } else {
                        ToastUitl.showShort(TemperatureControllerNote.this.context, R.string.communication_error);
                    }
                }
                if (z) {
                    loadingDialog.dismiss();
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.TemperatureControllerNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnCommandAnswerListener {
        final /* synthetic */ int val$cabinet;
        final /* synthetic */ boolean val$isShowDialog;
        final /* synthetic */ LoadingDialog val$loadingDialog;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass2(boolean z2, int i2, LoadingDialog loadingDialog2) {
            z = z2;
            i = i2;
            loadingDialog = loadingDialog2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr == null || bArr.length <= 0) {
                return;
            }
            if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                int signedIntFromByte = ObjectHelper.signedIntFromByte(bArr[2]);
                ObjectHelper.signedIntFromByte(bArr[3]);
                ObjectHelper.signedIntFromByte(bArr[4]);
                TemperatureControllerNote.this.temperatureControllerView.setWorkingMode(intFromBytes - 1);
                TemperatureControllerNote.this.temperatureControllerView.setControlTemperature(String.valueOf(signedIntFromByte));
            } else if (z) {
                if (i == 0) {
                    ToastUitl.showShort(TemperatureControllerNote.this.context, R.string.main_communication_error);
                } else {
                    ToastUitl.showShort(TemperatureControllerNote.this.context, R.string.communication_error);
                }
            }
            if (z) {
                loadingDialog.dismiss();
            }
        }
    }

    private void queryState(int i, boolean z) {
        LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.on_querying);
        if (z) {
            loadingDialog.show();
        }
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setWKYState(false, i);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.TemperatureControllerNote.3
            final /* synthetic */ int val$cabinet;
            final /* synthetic */ boolean val$isShowDialog;
            final /* synthetic */ LoadingDialog val$loadingDialog;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z2) {
            }

            AnonymousClass3(boolean z2, int i2, LoadingDialog loadingDialog2) {
                z = z2;
                i = i2;
                loadingDialog = loadingDialog2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length > 0) {
                    if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                        int signedIntFromByte = ObjectHelper.signedIntFromByte(bArr[1]);
                        TemperatureControllerNote.this.temperatureControllerView.setTemperatureControllerState(TemperatureControllerNote.this.getTemperatureControllerStatus(ObjectHelper.intFromBytes(bArr, 2, 1)) + "  " + signedIntFromByte + "℃");
                    } else if (z) {
                        if (i == 0) {
                            ToastUitl.showShort(TemperatureControllerNote.this.context, R.string.main_communication_error);
                        } else {
                            ToastUitl.showShort(TemperatureControllerNote.this.context, R.string.communication_error);
                        }
                    }
                }
                if (z) {
                    loadingDialog.dismiss();
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.TemperatureControllerNote$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnCommandAnswerListener {
        final /* synthetic */ int val$cabinet;
        final /* synthetic */ boolean val$isShowDialog;
        final /* synthetic */ LoadingDialog val$loadingDialog;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass3(boolean z2, int i2, LoadingDialog loadingDialog2) {
            z = z2;
            i = i2;
            loadingDialog = loadingDialog2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    int signedIntFromByte = ObjectHelper.signedIntFromByte(bArr[1]);
                    TemperatureControllerNote.this.temperatureControllerView.setTemperatureControllerState(TemperatureControllerNote.this.getTemperatureControllerStatus(ObjectHelper.intFromBytes(bArr, 2, 1)) + "  " + signedIntFromByte + "℃");
                } else if (z) {
                    if (i == 0) {
                        ToastUitl.showShort(TemperatureControllerNote.this.context, R.string.main_communication_error);
                    } else {
                        ToastUitl.showShort(TemperatureControllerNote.this.context, R.string.communication_error);
                    }
                }
            }
            if (z) {
                loadingDialog.dismiss();
            }
        }
    }

    public String getTemperatureControllerStatus(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.context.getString(R.string.temperature_controller_status0));
        arrayList.add(this.context.getString(R.string.temperature_controller_status1));
        arrayList.add(this.context.getString(R.string.temperature_controller_status2));
        arrayList.add(this.context.getString(R.string.temperature_controller_status3));
        arrayList.add(this.context.getString(R.string.temperature_controller_status4));
        arrayList.add(this.context.getString(R.string.temperature_controller_status5));
        arrayList.add(this.context.getString(R.string.temperature_controller_status6));
        arrayList.add(this.context.getString(R.string.temperature_controller_status7));
        return (i < 0 || i >= arrayList.size()) ? "" : (String) arrayList.get(i);
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.temperatureControllerView;
    }
}
