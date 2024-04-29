package com.shj.setting.generator;

import android.content.Context;
import android.view.View;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.ConnectLiftView;
import com.shj.setting.widget.RadioGroupItemView;
import com.shj.setting.widget.SpinnerItemView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class ConnectLiftNote extends SettingNote {
    private ConnectLiftView connectLiftView;

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
    }

    public ConnectLiftNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        int cabinet = this.connectLiftView.getCabinet();
        int lift = this.connectLiftView.getLift();
        int protect = this.connectLiftView.getProtect();
        if (lift == -1 || protect == -1) {
            ToastUitl.showNotInputTip(this.context);
            return;
        }
        boolean z2 = lift == 0;
        commandV2_Up_SetCommand.setConnectLift(true, cabinet, z2, protect == 0);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.ConnectLiftNote.1
            final /* synthetic */ boolean val$lift;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass1(boolean z22) {
                z2 = z22;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z3) {
                ToastUitl.showSetCompeleteTip(ConnectLiftNote.this.context, z3, ConnectLiftNote.this.getSettingName());
                if (z2) {
                    AppSetting.saveEquipmentType(ConnectLiftNote.this.context, 1, ConnectLiftNote.this.mUserSettingDao);
                } else {
                    AppSetting.saveEquipmentType(ConnectLiftNote.this.context, 0, ConnectLiftNote.this.mUserSettingDao);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.ConnectLiftNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements OnCommandAnswerListener {
        final /* synthetic */ boolean val$lift;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1(boolean z22) {
            z2 = z22;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showSetCompeleteTip(ConnectLiftNote.this.context, z3, ConnectLiftNote.this.getSettingName());
            if (z2) {
                AppSetting.saveEquipmentType(ConnectLiftNote.this.context, 1, ConnectLiftNote.this.mUserSettingDao);
            } else {
                AppSetting.saveEquipmentType(ConnectLiftNote.this.context, 0, ConnectLiftNote.this.mUserSettingDao);
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.on_querying);
        loadingDialog.show();
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setConnectLift(false, this.connectLiftView.getCabinet(), false, false);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.ConnectLiftNote.2
            final /* synthetic */ LoadingDialog val$loadingDialog;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass2(LoadingDialog loadingDialog2) {
                loadingDialog = loadingDialog2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length > 0) {
                    if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                        int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                        int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 1);
                        ConnectLiftNote.this.connectLiftView.setLift(intFromBytes - 1);
                        ConnectLiftNote.this.connectLiftView.setProtect(intFromBytes2);
                        if (intFromBytes == 1) {
                            AppSetting.saveEquipmentType(ConnectLiftNote.this.context, 1, ConnectLiftNote.this.mUserSettingDao);
                        } else {
                            AppSetting.saveEquipmentType(ConnectLiftNote.this.context, 0, ConnectLiftNote.this.mUserSettingDao);
                        }
                    } else {
                        ToastUitl.showShort(ConnectLiftNote.this.context, R.string.communication_error);
                    }
                }
                loadingDialog.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.generator.ConnectLiftNote$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements OnCommandAnswerListener {
        final /* synthetic */ LoadingDialog val$loadingDialog;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass2(LoadingDialog loadingDialog2) {
            loadingDialog = loadingDialog2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 1);
                    ConnectLiftNote.this.connectLiftView.setLift(intFromBytes - 1);
                    ConnectLiftNote.this.connectLiftView.setProtect(intFromBytes2);
                    if (intFromBytes == 1) {
                        AppSetting.saveEquipmentType(ConnectLiftNote.this.context, 1, ConnectLiftNote.this.mUserSettingDao);
                    } else {
                        AppSetting.saveEquipmentType(ConnectLiftNote.this.context, 0, ConnectLiftNote.this.mUserSettingDao);
                    }
                } else {
                    ToastUitl.showShort(ConnectLiftNote.this.context, R.string.communication_error);
                }
            }
            loadingDialog.dismiss();
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        spinnerItemData.name = this.context.getString(R.string.cabinet_number);
        spinnerItemData.dataList = SettingActivity.getBasicMachineInfo().cabinetNumberList;
        RadioGroupItemView.RadioGroupData radioGroupData = new RadioGroupItemView.RadioGroupData();
        radioGroupData.title = this.context.getString(R.string.over_current_protection);
        radioGroupData.isVertical = false;
        radioGroupData.nameList = new ArrayList();
        radioGroupData.nameList.add(this.context.getString(R.string.enable_yes));
        radioGroupData.nameList.add(this.context.getString(R.string.enable_no));
        RadioGroupItemView.RadioGroupData radioGroupData2 = new RadioGroupItemView.RadioGroupData();
        radioGroupData2.title = this.context.getString(R.string.enabling_lift);
        radioGroupData2.isVertical = false;
        radioGroupData2.nameList = new ArrayList();
        radioGroupData2.nameList.add(this.context.getString(R.string.enable_yes));
        radioGroupData2.nameList.add(this.context.getString(R.string.enable_no));
        ConnectLiftView connectLiftView = new ConnectLiftView(this.context, spinnerItemData, radioGroupData, radioGroupData2);
        this.connectLiftView = connectLiftView;
        connectLiftView.setEventListener(this.eventListener);
        this.connectLiftView.setTitleVisibility(0);
        this.connectLiftView.setTitle(getSettingName());
        this.connectLiftView.setQueryButtonVIsibility(0);
        return this.connectLiftView;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.connectLiftView;
    }
}
