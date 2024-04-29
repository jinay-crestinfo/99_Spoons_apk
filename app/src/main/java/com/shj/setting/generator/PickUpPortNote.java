package com.shj.setting.generator;

import android.content.Context;
import android.text.TextUtils;
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
import com.shj.setting.widget.MultipleEditItemView;
import com.shj.setting.widget.PickUpPortView;
import com.shj.setting.widget.SpinnerItemView;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public class PickUpPortNote extends SettingNote {
    private PickUpPortView pickUpPortView;

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
    }

    public PickUpPortNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        int cabinet = this.pickUpPortView.getCabinet();
        String delayTime = this.pickUpPortView.getDelayTime();
        int i = this.settingType;
        if (i != 227) {
            if (i != 230) {
                return;
            }
            if (TextUtils.isEmpty(delayTime)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.saveing);
            loadingDialog.show();
            commandV2_Up_SetCommand.setOfferGoodsCheck_Sensitivity(true, cabinet, Integer.valueOf(delayTime).intValue());
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.PickUpPortNote.2
                final /* synthetic */ LoadingDialog val$loadingDialog;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass2(LoadingDialog loadingDialog2) {
                    loadingDialog = loadingDialog2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                    ToastUitl.showSetCompeleteTip(PickUpPortNote.this.context, z2, PickUpPortNote.this.getSettingName());
                    loadingDialog.dismiss();
                }
            });
            return;
        }
        if (TextUtils.isEmpty(delayTime)) {
            ToastUitl.showNotInputTip(this.context);
            return;
        }
        int intValue = Integer.valueOf(delayTime).intValue();
        if (intValue < 3 || intValue > 250) {
            ToastUitl.showErrorInputTip(this.context, "100");
            return;
        }
        commandV2_Up_SetCommand.setPickDoorCloseTime(true, cabinet, intValue);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.PickUpPortNote.1
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z2) {
                ToastUitl.showSetCompeleteTip(PickUpPortNote.this.context, z2, PickUpPortNote.this.getSettingName());
            }
        });
    }

    /* renamed from: com.shj.setting.generator.PickUpPortNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(PickUpPortNote.this.context, z2, PickUpPortNote.this.getSettingName());
        }
    }

    /* renamed from: com.shj.setting.generator.PickUpPortNote$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements OnCommandAnswerListener {
        final /* synthetic */ LoadingDialog val$loadingDialog;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass2(LoadingDialog loadingDialog2) {
            loadingDialog = loadingDialog2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(PickUpPortNote.this.context, z2, PickUpPortNote.this.getSettingName());
            loadingDialog.dismiss();
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        int cabinet = this.pickUpPortView.getCabinet();
        int i = this.settingType;
        if (i == 227) {
            LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.on_querying);
            loadingDialog.show();
            commandV2_Up_SetCommand.setPickDoorCloseTime(false, cabinet, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.PickUpPortNote.3
                final /* synthetic */ LoadingDialog val$loadingDialog;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass3(LoadingDialog loadingDialog2) {
                    loadingDialog = loadingDialog2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                            PickUpPortNote.this.pickUpPortView.setDelayTime(String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                        } else {
                            ToastUitl.showShort(PickUpPortNote.this.context, R.string.communication_error);
                        }
                    }
                    loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i != 230) {
            return;
        }
        LoadingDialog loadingDialog2 = new LoadingDialog(this.context, R.string.on_querying);
        loadingDialog2.show();
        commandV2_Up_SetCommand.setOfferGoodsCheck_Sensitivity(false, cabinet, 0);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.PickUpPortNote.4
            final /* synthetic */ LoadingDialog val$loadingDialog;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass4(LoadingDialog loadingDialog22) {
                loadingDialog2 = loadingDialog22;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length > 0) {
                    if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                        PickUpPortNote.this.pickUpPortView.setDelayTime(String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                    } else {
                        ToastUitl.showShort(PickUpPortNote.this.context, R.string.communication_error);
                    }
                }
                loadingDialog2.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.generator.PickUpPortNote$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 implements OnCommandAnswerListener {
        final /* synthetic */ LoadingDialog val$loadingDialog;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass3(LoadingDialog loadingDialog2) {
            loadingDialog = loadingDialog2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    PickUpPortNote.this.pickUpPortView.setDelayTime(String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                } else {
                    ToastUitl.showShort(PickUpPortNote.this.context, R.string.communication_error);
                }
            }
            loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.PickUpPortNote$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 implements OnCommandAnswerListener {
        final /* synthetic */ LoadingDialog val$loadingDialog;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass4(LoadingDialog loadingDialog22) {
            loadingDialog2 = loadingDialog22;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    PickUpPortNote.this.pickUpPortView.setDelayTime(String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                } else {
                    ToastUitl.showShort(PickUpPortNote.this.context, R.string.communication_error);
                }
            }
            loadingDialog2.dismiss();
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        spinnerItemData.name = this.context.getString(R.string.cabinet_number);
        spinnerItemData.dataList = SettingActivity.getBasicMachineInfo().cabinetNumberList;
        MultipleEditItemView.EditTextDataInfo editTextDataInfo = new MultipleEditItemView.EditTextDataInfo();
        editTextDataInfo.name = getEditeName();
        PickUpPortView pickUpPortView = new PickUpPortView(this.context, spinnerItemData, editTextDataInfo);
        this.pickUpPortView = pickUpPortView;
        pickUpPortView.setTitle(getSettingName());
        this.pickUpPortView.setEventListener(this.eventListener);
        this.pickUpPortView.setTitleVisibility(0);
        setQueryButtonVisibility();
        return this.pickUpPortView;
    }

    private void setQueryButtonVisibility() {
        int i = this.settingType;
        if (i == 227 || i == 230) {
            this.pickUpPortView.setQueryButtonVIsibility(0);
        }
    }

    private String getEditeName() {
        int i = this.settingType;
        if (i != 227) {
            return i != 230 ? "" : this.context.getString(R.string.sensitivity);
        }
        return this.context.getString(R.string.delay_time);
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.pickUpPortView;
    }
}
