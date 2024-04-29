package com.shj.setting.generator;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Event.BaseEvent;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.video.VideoHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.biz.ReportManager;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.commandV2.MenuCommandType;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.RbootModelTipDialog;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.event.GetMenuDateEvent;
import com.shj.setting.event.SettingTypeEvent;
import com.shj.setting.view.CameraFloatView;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.RadioGroupItemView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSetting;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* loaded from: classes.dex */
public class RadioGroupItemNote extends SettingNote {
    private int cabinetNumber;
    private LoadingDialog loadingDialog;
    private RadioGroupItemView radioGroupItemView;

    public RadioGroupItemNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r4v4 */
    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        int radioButtonCheckIndex = this.radioGroupItemView.getRadioButtonCheckIndex();
        String settingKey = SettingType.getSettingKey(this.settingType);
        if (radioButtonCheckIndex == -1 || TextUtils.isEmpty(settingKey)) {
            return;
        }
        int i = this.settingType;
        if (i != 103 && i != 104 && i != 222 && i != 223 && i != 266) {
            if (i == 267) {
                if (radioButtonCheckIndex == 0) {
                    AppSetting.saveGoodwaySelection(this.context, true, null);
                } else {
                    AppSetting.saveGoodwaySelection(this.context, false, null);
                }
                if (z) {
                    return;
                }
                ToastUitl.showSaveSuccessTip(this.context);
                return;
            }
            if (i == 293) {
                if (radioButtonCheckIndex == 0) {
                    AppSetting.saveShowMarketing(this.context, true, null);
                } else {
                    AppSetting.saveShowMarketing(this.context, false, null);
                }
                if (z) {
                    return;
                }
                ToastUitl.showSaveSuccessTip(this.context);
                return;
            }
            if (i == 294) {
                if (radioButtonCheckIndex == 0) {
                    AppSetting.saveShowShoppingButton(this.context, true, null);
                } else {
                    AppSetting.saveShowShoppingButton(this.context, false, null);
                }
                if (z) {
                    return;
                }
                ToastUitl.showSaveSuccessTip(this.context);
                return;
            }
            switch (i) {
                case 111:
                case 115:
                case SettingType.ALWAYS_HEATING /* 304 */:
                case SettingType.FACEPAY_TYPE /* 325 */:
                case SettingType.PRACTICE_MODE /* 345 */:
                case SettingType.QRCODE_FLOAT_VIEW_ENABLE /* 347 */:
                    break;
                case 118:
                    LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog;
                    loadingDialog.show();
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                    commandV2_Up_SetCommand.setRemainMoneyPro(true, radioButtonCheckIndex + 1);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.1
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass1() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z2) {
                            ToastUitl.showSetCompeleteTip(RadioGroupItemNote.this.context, z2, RadioGroupItemNote.this.getSettingName());
                            RadioGroupItemNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 144:
                    LoadingDialog loadingDialog2 = new LoadingDialog(this.context, this.context.getString(R.string.saveing));
                    this.loadingDialog = loadingDialog2;
                    loadingDialog2.show();
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                    ?? r4 = radioButtonCheckIndex + 1 == 1 ? 1 : 0;
                    commandV2_Up_SetCommand2.setGoodsDroopCheck(true, 100, r4);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.3
                        final /* synthetic */ boolean val$open;

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass3(boolean r42) {
                            r4 = r42;
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z2) {
                            ToastUitl.showSetCompeleteTip(RadioGroupItemNote.this.context, z2, RadioGroupItemNote.this.context.getString(R.string.drop_inspection) + RadioGroupItemNote.this.getSettingName());
                            if (z2) {
                                EventBus.getDefault().post(new SettingTypeEvent(RadioGroupItemNote.this.settingType, Boolean.valueOf(r4)));
                            }
                            RadioGroupItemNote.this.loadingDialog.dismiss();
                        }
                    });
                    ReportManager.reportSetDropCheck(3, 100, r42);
                    return;
                case 152:
                    LoadingDialog loadingDialog3 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog3;
                    loadingDialog3.show();
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
                    boolean z2 = radioButtonCheckIndex == 0;
                    int i2 = this.cabinetNumber;
                    if (i2 == 0) {
                        commandV2_Up_SetCommand3.setBlock41(true, i2 + 3000, !z2);
                    } else {
                        commandV2_Up_SetCommand3.setBlock41(true, i2 + 3000, z2);
                    }
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand3, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.4
                        final /* synthetic */ boolean val$enable;

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass4(boolean z22) {
                            z2 = z22;
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z3) {
                            ToastUitl.showSetCompeleteTip(RadioGroupItemNote.this.context, z3, RadioGroupItemNote.this.context.getString(R.string.circle_setting) + RadioGroupItemNote.this.getSettingName());
                            if (z3) {
                                EventBus.getDefault().post(new SettingTypeEvent(RadioGroupItemNote.this.settingType, Boolean.valueOf(z2)));
                            }
                            RadioGroupItemNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 155:
                    LoadingDialog loadingDialog4 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog4;
                    loadingDialog4.show();
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand4 = new CommandV2_Up_SetCommand();
                    boolean z3 = radioButtonCheckIndex + 1 != 1;
                    commandV2_Up_SetCommand4.setcontinueSaleAfterBlock(true, 100, z3);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand4, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.5
                        final /* synthetic */ boolean val$enableCard;

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass5(boolean z32) {
                            z3 = z32;
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z4) {
                            ToastUitl.showSetCompeleteTip(RadioGroupItemNote.this.context, z4, RadioGroupItemNote.this.context.getString(R.string.cargo_card_setup) + RadioGroupItemNote.this.getSettingName());
                            if (z4) {
                                EventBus.getDefault().post(new SettingTypeEvent(RadioGroupItemNote.this.settingType, Boolean.valueOf(z3)));
                            }
                            RadioGroupItemNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 185:
                    LoadingDialog loadingDialog5 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog5;
                    loadingDialog5.show();
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand5 = new CommandV2_Up_SetCommand();
                    commandV2_Up_SetCommand5.setPaperModel(true, radioButtonCheckIndex + 1);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand5, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.2
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass2() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z4) {
                            ToastUitl.showSetCompeleteTip(RadioGroupItemNote.this.context, z4, RadioGroupItemNote.this.getSettingName());
                            RadioGroupItemNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 199:
                    if (radioButtonCheckIndex == 0) {
                        AppSetting.saveAutoUpgrade(this.context, true, null);
                    } else {
                        AppSetting.saveAutoUpgrade(this.context, false, null);
                    }
                    if (z) {
                        return;
                    }
                    ToastUitl.showSaveSuccessTip(this.context);
                    return;
                case 220:
                    if (radioButtonCheckIndex == 0) {
                        AppSetting.saveAutoToActivity(this.context, true, null);
                    } else {
                        AppSetting.saveAutoToActivity(this.context, false, null);
                    }
                    if (z) {
                        return;
                    }
                    ToastUitl.showSaveSuccessTip(this.context);
                    return;
                case 281:
                    if (radioButtonCheckIndex == 0) {
                        AppSetting.saveYYTAd(this.context, true, null);
                    } else {
                        AppSetting.saveYYTAd(this.context, false, null);
                    }
                    if (z) {
                        return;
                    }
                    ToastUitl.showSaveSuccessTip(this.context);
                    return;
                case SettingType.LIGHTING_CONTROL_TR /* 353 */:
                    this.mUserSettingDao.insert(new UserSetting(this.settingType, SettingType.getParentId(this.settingType), settingKey, String.valueOf(radioButtonCheckIndex)));
                    return;
                default:
                    switch (i) {
                        case 259:
                            if (radioButtonCheckIndex == 0) {
                                AppSetting.saveShowTemperature(this.context, true, null);
                            } else {
                                AppSetting.saveShowTemperature(this.context, false, null);
                            }
                            if (z) {
                                return;
                            }
                            ToastUitl.showSaveSuccessTip(this.context);
                            return;
                        case SettingType.SHOW_BALANCE /* 260 */:
                            if (radioButtonCheckIndex == 0) {
                                AppSetting.saveShowBalance(this.context, true, null);
                            } else {
                                AppSetting.saveShowBalance(this.context, false, null);
                            }
                            if (z) {
                                return;
                            }
                            ToastUitl.showSaveSuccessTip(this.context);
                            return;
                        case SettingType.AD_PLAYER /* 261 */:
                            if (radioButtonCheckIndex == 0) {
                                AppSetting.saveAdPlayer(this.context, VideoHelper.MODE_IJK, null);
                            } else {
                                AppSetting.saveAdPlayer(this.context, "system", null);
                            }
                            if (z) {
                                return;
                            }
                            ToastUitl.showSaveSuccessTip(this.context);
                            return;
                        case SettingType.SHOW_CODE_ZERO /* 262 */:
                            if (radioButtonCheckIndex == 0) {
                                AppSetting.saveShowCodeZero(this.context, true, null);
                            } else {
                                AppSetting.saveShowCodeZero(this.context, false, null);
                            }
                            if (z) {
                                return;
                            }
                            ToastUitl.showSaveSuccessTip(this.context);
                            return;
                        case SettingType.CALL_PHONE /* 263 */:
                            if (radioButtonCheckIndex == 0) {
                                AppSetting.saveCallPhone(this.context, true, null);
                            } else {
                                AppSetting.saveCallPhone(this.context, false, null);
                            }
                            if (z) {
                                return;
                            }
                            ToastUitl.showSaveSuccessTip(this.context);
                            return;
                        default:
                            switch (i) {
                                case SettingType.PAY_QRCODE_LEVEL /* 296 */:
                                case SettingType.HEART_DIALOG /* 297 */:
                                    break;
                                case SettingType.CAMERA_TEST /* 298 */:
                                    if (radioButtonCheckIndex == 0) {
                                        AppSetting.saveCameraTest(this.context, 0, null);
                                        CameraFloatView.getinstance().closeFloatBox();
                                        CameraFloatView.getinstance().showFloat(this.context);
                                    } else {
                                        AppSetting.saveCameraTest(this.context, 1, null);
                                        CameraFloatView.getinstance().closeFloatBox();
                                    }
                                    if (z) {
                                        return;
                                    }
                                    ToastUitl.showSaveSuccessTip(this.context);
                                    return;
                                case SettingType.CAMERA_AUTO_TAKE /* 299 */:
                                    if (radioButtonCheckIndex == 0) {
                                        AppSetting.saveCameraAutoTake(this.context, 0, null);
                                        if (AppSetting.getCameraTest(this.context, null)) {
                                            CameraFloatView.getinstance().closeFloatBox();
                                            CameraFloatView.getinstance().showFloat(this.context);
                                        } else {
                                            CameraFloatView.getinstance().closeFloatBox();
                                        }
                                    } else {
                                        AppSetting.saveCameraAutoTake(this.context, 1, null);
                                        if (AppSetting.getCameraTest(this.context, null)) {
                                            CameraFloatView.getinstance().closeFloatBox();
                                            CameraFloatView.getinstance().showFloat(this.context);
                                        } else {
                                            CameraFloatView.getinstance().closeFloatBox();
                                        }
                                    }
                                    if (z) {
                                        return;
                                    }
                                    ToastUitl.showSaveSuccessTip(this.context);
                                    return;
                                default:
                                    switch (i) {
                                        case SettingType.DEVICE_DEPLOYMENT_LOCATION /* 333 */:
                                        case 334:
                                        case 335:
                                            break;
                                        default:
                                            return;
                                    }
                            }
                    }
            }
        }
        this.mUserSettingDao.insert(new UserSetting(this.settingType, SettingType.getParentId(this.settingType), settingKey, String.valueOf(radioButtonCheckIndex)));
        if (z) {
            return;
        }
        ToastUitl.showSaveSuccessTip(this.context);
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(RadioGroupItemNote.this.context, z2, RadioGroupItemNote.this.getSettingName());
            RadioGroupItemNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass2() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z4) {
            ToastUitl.showSetCompeleteTip(RadioGroupItemNote.this.context, z4, RadioGroupItemNote.this.getSettingName());
            RadioGroupItemNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnCommandAnswerListener {
        final /* synthetic */ boolean val$open;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass3(boolean r42) {
            r4 = r42;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(RadioGroupItemNote.this.context, z2, RadioGroupItemNote.this.context.getString(R.string.drop_inspection) + RadioGroupItemNote.this.getSettingName());
            if (z2) {
                EventBus.getDefault().post(new SettingTypeEvent(RadioGroupItemNote.this.settingType, Boolean.valueOf(r4)));
            }
            RadioGroupItemNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements OnCommandAnswerListener {
        final /* synthetic */ boolean val$enable;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass4(boolean z22) {
            z2 = z22;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showSetCompeleteTip(RadioGroupItemNote.this.context, z3, RadioGroupItemNote.this.context.getString(R.string.circle_setting) + RadioGroupItemNote.this.getSettingName());
            if (z3) {
                EventBus.getDefault().post(new SettingTypeEvent(RadioGroupItemNote.this.settingType, Boolean.valueOf(z2)));
            }
            RadioGroupItemNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements OnCommandAnswerListener {
        final /* synthetic */ boolean val$enableCard;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass5(boolean z32) {
            z3 = z32;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z4) {
            ToastUitl.showSetCompeleteTip(RadioGroupItemNote.this.context, z4, RadioGroupItemNote.this.context.getString(R.string.cargo_card_setup) + RadioGroupItemNote.this.getSettingName());
            if (z4) {
                EventBus.getDefault().post(new SettingTypeEvent(RadioGroupItemNote.this.settingType, Boolean.valueOf(z3)));
            }
            RadioGroupItemNote.this.loadingDialog.dismiss();
        }
    }

    public void hideRadioButton() {
        if (this.settingType != 104) {
            return;
        }
        this.radioGroupItemView.setRadioButtonHide(1);
        this.radioGroupItemView.setRadioButtonHide(2);
        this.radioGroupItemView.setRadioButtonHide(3);
    }

    public void setRadioButton() {
        String settingKey = SettingType.getSettingKey(this.settingType);
        if (TextUtils.isEmpty(settingKey)) {
            return;
        }
        int i = this.settingType;
        if (i == 103 || i == 104 || i == 111 || i == 115) {
            UserSetting userSettingFormKey = this.mUserSettingDao.getUserSettingFormKey(settingKey);
            if (userSettingFormKey != null) {
                this.radioGroupItemView.setRadioButtonCheck(Integer.valueOf(userSettingFormKey.getValue()).intValue());
                return;
            }
            return;
        }
        if (i == 199) {
            if (AppSetting.getAutoUpgrade(this.context, null)) {
                this.radioGroupItemView.setRadioButtonCheck(0);
                return;
            } else {
                this.radioGroupItemView.setRadioButtonCheck(1);
                return;
            }
        }
        if (i == 220) {
            if (AppSetting.getAutoToActivity(this.context, this.mUserSettingDao)) {
                this.radioGroupItemView.setRadioButtonCheck(0);
                return;
            } else {
                this.radioGroupItemView.setRadioButtonCheck(1);
                return;
            }
        }
        if (i == 281) {
            if (AppSetting.getYYTAd(this.context, null)) {
                this.radioGroupItemView.setRadioButtonCheck(0);
                return;
            } else {
                this.radioGroupItemView.setRadioButtonCheck(1);
                return;
            }
        }
        if (i == 304) {
            this.radioGroupItemView.setRadioButtonCheck(!AppSetting.getAlwaysHeating(this.context, null) ? 1 : 0);
            return;
        }
        if (i == 325) {
            this.radioGroupItemView.setRadioButtonCheck(AppSetting.getFacepayType(this.context, this.mUserSettingDao) != 0 ? 1 : 0);
            return;
        }
        if (i == 345) {
            this.radioGroupItemView.setRadioButtonCheck(!AppSetting.getPracticeMode(this.context, null).booleanValue() ? 1 : 0);
            return;
        }
        if (i == 347) {
            if (AppSetting.getQrcodeFloatViewEnable(this.context, null)) {
                this.radioGroupItemView.setRadioButtonCheck(0);
                return;
            } else {
                this.radioGroupItemView.setRadioButtonCheck(1);
                return;
            }
        }
        if (i == 353) {
            if (AppSetting.getLightingControlTR(this.context, null)) {
                this.radioGroupItemView.setRadioButtonCheck(0);
                return;
            } else {
                this.radioGroupItemView.setRadioButtonCheck(1);
                return;
            }
        }
        if (i == 222) {
            this.radioGroupItemView.setRadioButtonCheck(!AppSetting.getEnableShoppingCart(this.context, this.mUserSettingDao) ? 1 : 0);
            return;
        }
        if (i == 223) {
            this.radioGroupItemView.setRadioButtonCheck(!AppSetting.getEnableFacePay(this.context, this.mUserSettingDao) ? 1 : 0);
            return;
        }
        if (i == 266) {
            this.radioGroupItemView.setRadioButtonCheck(AppSetting.getNetworkingTimeout(this.context, null));
            return;
        }
        if (i == 267) {
            this.radioGroupItemView.setRadioButtonCheck(!AppSetting.getGoodwaySelection(this.context, this.mUserSettingDao) ? 1 : 0);
            return;
        }
        if (i == 293) {
            if (AppSetting.getShowMarketing(this.context, null)) {
                this.radioGroupItemView.setRadioButtonCheck(0);
                return;
            } else {
                this.radioGroupItemView.setRadioButtonCheck(1);
                return;
            }
        }
        if (i != 294) {
            switch (i) {
                case 259:
                    if (AppSetting.getAutoUpgrade(this.context, null)) {
                        this.radioGroupItemView.setRadioButtonCheck(0);
                        return;
                    } else {
                        this.radioGroupItemView.setRadioButtonCheck(1);
                        return;
                    }
                case SettingType.SHOW_BALANCE /* 260 */:
                    if (AppSetting.getAutoUpgrade(this.context, null)) {
                        this.radioGroupItemView.setRadioButtonCheck(0);
                        return;
                    } else {
                        this.radioGroupItemView.setRadioButtonCheck(1);
                        return;
                    }
                case SettingType.AD_PLAYER /* 261 */:
                    String adPlayer = AppSetting.getAdPlayer(this.context, null);
                    if (VideoHelper.MODE_IJK.equals(adPlayer) || adPlayer == null) {
                        this.radioGroupItemView.setRadioButtonCheck(0);
                        return;
                    } else {
                        this.radioGroupItemView.setRadioButtonCheck(1);
                        return;
                    }
                case SettingType.SHOW_CODE_ZERO /* 262 */:
                    if (AppSetting.getShowCodeZero(this.context, null)) {
                        this.radioGroupItemView.setRadioButtonCheck(0);
                        return;
                    } else {
                        this.radioGroupItemView.setRadioButtonCheck(1);
                        return;
                    }
                case SettingType.CALL_PHONE /* 263 */:
                    if (AppSetting.getCallPhone(this.context, null)) {
                        this.radioGroupItemView.setRadioButtonCheck(0);
                        return;
                    } else {
                        this.radioGroupItemView.setRadioButtonCheck(1);
                        return;
                    }
                default:
                    switch (i) {
                        case SettingType.PAY_QRCODE_LEVEL /* 296 */:
                            this.radioGroupItemView.setRadioButtonCheck(AppSetting.getPayQrcodeLevel(this.context, null));
                            return;
                        case SettingType.HEART_DIALOG /* 297 */:
                            this.radioGroupItemView.setRadioButtonCheck(!AppSetting.getHeartDialog(this.context, null) ? 1 : 0);
                            return;
                        case SettingType.CAMERA_TEST /* 298 */:
                            this.radioGroupItemView.setRadioButtonCheck(!AppSetting.getCameraTest(this.context, null) ? 1 : 0);
                            return;
                        case SettingType.CAMERA_AUTO_TAKE /* 299 */:
                            this.radioGroupItemView.setRadioButtonCheck(!AppSetting.getCameraAutoTake(this.context, null).booleanValue() ? 1 : 0);
                            return;
                        default:
                            switch (i) {
                                case SettingType.DEVICE_DEPLOYMENT_LOCATION /* 333 */:
                                    if (AppSetting.getDeviceDeploymentLocation(this.context, this.mUserSettingDao) == 0) {
                                        this.radioGroupItemView.setRadioButtonCheck(0);
                                        return;
                                    } else {
                                        this.radioGroupItemView.setRadioButtonCheck(1);
                                        return;
                                    }
                                case 334:
                                    if (AppSetting.getPickUpPortCount(this.context, this.mUserSettingDao) == 0) {
                                        this.radioGroupItemView.setRadioButtonCheck(0);
                                        return;
                                    } else {
                                        this.radioGroupItemView.setRadioButtonCheck(1);
                                        return;
                                    }
                                case 335:
                                    if (AppSetting.getDataSaveLocation(this.context, this.mUserSettingDao) == 0) {
                                        this.radioGroupItemView.setRadioButtonCheck(0);
                                        return;
                                    } else {
                                        this.radioGroupItemView.setRadioButtonCheck(1);
                                        return;
                                    }
                                default:
                                    return;
                            }
                    }
            }
        }
        if (AppSetting.getShowShoppingButton(this.context, null)) {
            this.radioGroupItemView.setRadioButtonCheck(0);
        } else {
            this.radioGroupItemView.setRadioButtonCheck(1);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public List<String> getNameList() {
        ArrayList arrayList = new ArrayList();
        int i = this.settingType;
        if (i == 103) {
            arrayList.add(this.context.getResources().getString(R.string.lab_devicetype_normal));
            arrayList.add(this.context.getResources().getString(R.string.lab_devicetype_updown));
        } else if (i == 104) {
            arrayList.add(this.context.getResources().getString(R.string.lab_machinetype_normal));
            arrayList.add(this.context.getResources().getString(R.string.lab_machinetype_frute));
            arrayList.add(this.context.getResources().getString(R.string.lab_machinetype_present));
            arrayList.add(this.context.getResources().getString(R.string.lab_machinetype_tobacco));
            arrayList.add(this.context.getResources().getString(R.string.box_lunch_machine));
            arrayList.add(this.context.getResources().getString(R.string.mixer_machine));
        } else {
            if (i != 222 && i != 223) {
                if (i == 266) {
                    arrayList.add(this.context.getResources().getString(R.string.waiting_for_network_recovery));
                    arrayList.add(this.context.getResources().getString(R.string.restart_app));
                    arrayList.add(this.context.getResources().getString(R.string.restart_the_host_computer));
                } else if (i != 267 && i != 293) {
                    if (i != 294) {
                        switch (i) {
                            case 111:
                                arrayList.add(this.context.getResources().getString(R.string.lab_classic));
                                arrayList.add(this.context.getResources().getString(R.string.lab_grid));
                                break;
                            case 115:
                            case 144:
                            case 152:
                            case 199:
                            case 220:
                            case 281:
                            case SettingType.ALWAYS_HEATING /* 304 */:
                            case SettingType.PRACTICE_MODE /* 345 */:
                            case SettingType.LIGHTING_CONTROL_TR /* 353 */:
                                break;
                            case 118:
                                arrayList.add(this.context.getResources().getString(R.string.swallowing_money));
                                arrayList.add(this.context.getResources().getString(R.string.looking_for_money));
                                arrayList.add(this.context.getResources().getString(R.string.swallow_coins_after_finding_coins));
                                break;
                            case 155:
                                arrayList.add(this.context.getResources().getString(R.string.cannot_buy));
                                arrayList.add(this.context.getResources().getString(R.string.continue_to_buy));
                                break;
                            case 185:
                                arrayList.add(this.context.getResources().getString(R.string.collecting_mode01));
                                arrayList.add(this.context.getResources().getString(R.string.collecting_mode02));
                                arrayList.add(this.context.getResources().getString(R.string.collecting_mode03));
                                break;
                            case SettingType.FACEPAY_TYPE /* 325 */:
                                arrayList.add(this.context.getResources().getString(R.string.zfb));
                                arrayList.add(this.context.getResources().getString(R.string.weixin));
                                break;
                            case SettingType.QRCODE_FLOAT_VIEW_ENABLE /* 347 */:
                                break;
                            default:
                                switch (i) {
                                    case 259:
                                    case SettingType.SHOW_BALANCE /* 260 */:
                                    case SettingType.SHOW_CODE_ZERO /* 262 */:
                                        break;
                                    case SettingType.AD_PLAYER /* 261 */:
                                        arrayList.add(this.context.getResources().getString(R.string.ijk));
                                        arrayList.add(this.context.getResources().getString(R.string.system));
                                        break;
                                    default:
                                        switch (i) {
                                            case SettingType.PAY_QRCODE_LEVEL /* 296 */:
                                                arrayList.add(this.context.getResources().getString(R.string.weixin));
                                                arrayList.add(this.context.getResources().getString(R.string.zfb));
                                                break;
                                            case SettingType.HEART_DIALOG /* 297 */:
                                            case SettingType.CAMERA_TEST /* 298 */:
                                            case SettingType.CAMERA_AUTO_TAKE /* 299 */:
                                                break;
                                            default:
                                                switch (i) {
                                                    case SettingType.DEVICE_DEPLOYMENT_LOCATION /* 333 */:
                                                        arrayList.add(this.context.getResources().getString(R.string.routine));
                                                        arrayList.add(this.context.getResources().getString(R.string.airport));
                                                        break;
                                                    case 334:
                                                        arrayList.add(this.context.getResources().getString(R.string.pick_up_port_mul));
                                                        arrayList.add(this.context.getResources().getString(R.string.pick_up_port_singal));
                                                        break;
                                                    case 335:
                                                        arrayList.add(this.context.getResources().getString(R.string.singlechip));
                                                        arrayList.add(this.context.getResources().getString(R.string.f11android));
                                                        break;
                                                }
                                        }
                                    case SettingType.CALL_PHONE /* 263 */:
                                        arrayList.add(this.context.getResources().getString(R.string.lab_enable));
                                        arrayList.add(this.context.getResources().getString(R.string.lab_disable));
                                        break;
                                }
                        }
                    }
                    arrayList.add(this.context.getResources().getString(R.string.lab_show));
                    arrayList.add(this.context.getResources().getString(R.string.lab_disable));
                }
            }
            arrayList.add(this.context.getResources().getString(R.string.lab_enable));
            arrayList.add(this.context.getResources().getString(R.string.lab_disable));
        }
        return arrayList;
    }

    private boolean isVertical() {
        int i = this.settingType;
        return (i == 104 || i == 118) ? CommonTool.getLanguage(this.context).equals("en") || CommonTool.getLanguage(this.context).equals("fr") : i == 185 || i == 266;
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        EventBus.getDefault().register(this);
        queryData();
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        int i = this.settingType;
        if (i == 144) {
            LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setGoodsDroopCheck(false, 100, false);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.6
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass6() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(ObjectHelper.intFromBytes(bArr, 0, 1) - 1);
                    }
                    if (RadioGroupItemNote.this.loadingDialog != null) {
                        RadioGroupItemNote.this.loadingDialog.dismiss();
                    }
                }
            });
            return;
        }
        if (i == 152) {
            LoadingDialog loadingDialog2 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog2;
            loadingDialog2.show();
            CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand2.setBlock41(false, this.cabinetNumber + 3000, false);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.7
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass7() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                        int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                        if (RadioGroupItemNote.this.cabinetNumber == 0) {
                            RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(intFromBytes == 0 ? 1 : 0);
                        } else {
                            RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(intFromBytes);
                        }
                    } else {
                        ToastUitl.showShort(RadioGroupItemNote.this.context, R.string.communication_error);
                    }
                    if (RadioGroupItemNote.this.loadingDialog != null) {
                        RadioGroupItemNote.this.loadingDialog.dismiss();
                    }
                }
            });
            return;
        }
        if (i != 155) {
            return;
        }
        LoadingDialog loadingDialog3 = new LoadingDialog(this.context, R.string.on_querying);
        this.loadingDialog = loadingDialog3;
        loadingDialog3.show();
        CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand3.setcontinueSaleAfterBlock(false, 100, false);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand3, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.8
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass8() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length > 0) {
                    RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(ObjectHelper.intFromBytes(bArr, 0, 1) - 1);
                }
                if (RadioGroupItemNote.this.loadingDialog != null) {
                    RadioGroupItemNote.this.loadingDialog.dismiss();
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$6 */
    /* loaded from: classes2.dex */
    class AnonymousClass6 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass6() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(ObjectHelper.intFromBytes(bArr, 0, 1) - 1);
            }
            if (RadioGroupItemNote.this.loadingDialog != null) {
                RadioGroupItemNote.this.loadingDialog.dismiss();
            }
        }
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$7 */
    /* loaded from: classes2.dex */
    class AnonymousClass7 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass7() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                if (RadioGroupItemNote.this.cabinetNumber == 0) {
                    RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(intFromBytes == 0 ? 1 : 0);
                } else {
                    RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(intFromBytes);
                }
            } else {
                ToastUitl.showShort(RadioGroupItemNote.this.context, R.string.communication_error);
            }
            if (RadioGroupItemNote.this.loadingDialog != null) {
                RadioGroupItemNote.this.loadingDialog.dismiss();
            }
        }
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$8 */
    /* loaded from: classes2.dex */
    class AnonymousClass8 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass8() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(ObjectHelper.intFromBytes(bArr, 0, 1) - 1);
            }
            if (RadioGroupItemNote.this.loadingDialog != null) {
                RadioGroupItemNote.this.loadingDialog.dismiss();
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof GetMenuDateEvent) {
            GetMenuDateEvent getMenuDateEvent = (GetMenuDateEvent) baseEvent;
            if (this.settingType == 118 && getMenuDateEvent.menuCommandType == MenuCommandType.TYPE_DISPOSAL_OF_SURPLUS_AMOUNT) {
                this.radioGroupItemView.setRadioButtonCheck(((Integer) getMenuDateEvent.data).intValue() - 1);
            }
        }
    }

    public void queryData() {
        int i = this.settingType;
        if (i == 118) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setRemainMoneyPro(false, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.9
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass9() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(ObjectHelper.intFromBytes(bArr, 0, bArr.length) - 1);
                }
            });
            return;
        }
        if (i != 185) {
            return;
        }
        CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand2.setPaperModel(false, 0);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.10
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass10() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(ObjectHelper.intFromBytes(bArr, 0, 1) - 1);
            }
        });
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass9() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(ObjectHelper.intFromBytes(bArr, 0, bArr.length) - 1);
        }
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass10() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            RadioGroupItemNote.this.radioGroupItemView.setRadioButtonCheck(ObjectHelper.intFromBytes(bArr, 0, 1) - 1);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        RadioGroupItemView.RadioGroupData radioGroupData = new RadioGroupItemView.RadioGroupData();
        radioGroupData.title = getSettingName();
        radioGroupData.nameList = getNameList();
        radioGroupData.isVertical = isVertical();
        RadioGroupItemView radioGroupItemView = new RadioGroupItemView(this.context, radioGroupData);
        this.radioGroupItemView = radioGroupItemView;
        radioGroupItemView.setTitle(getSettingName());
        this.radioGroupItemView.setEventListener(this.eventListener);
        hideRadioButton();
        setRadioButton();
        hideSaveButton();
        showQueryButton();
        setRadioButtonClickListener();
        return this.radioGroupItemView;
    }

    public void setRadioButtonClickListener() {
        if (this.settingType != 266) {
            return;
        }
        this.radioGroupItemView.setRadioButtonClickListener(2, new View.OnClickListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.11
            AnonymousClass11() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                new RbootModelTipDialog(RadioGroupItemNote.this.context).show();
            }
        });
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$11 */
    /* loaded from: classes2.dex */
    public class AnonymousClass11 implements View.OnClickListener {
        AnonymousClass11() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            new RbootModelTipDialog(RadioGroupItemNote.this.context).show();
        }
    }

    private void hideSaveButton() {
        switch (this.settingType) {
            case 103:
            case 104:
            case 115:
            case 118:
            case 185:
            case 199:
            case 220:
            case 222:
            case 223:
            case 259:
            case SettingType.SHOW_BALANCE /* 260 */:
            case SettingType.AD_PLAYER /* 261 */:
            case SettingType.CALL_PHONE /* 263 */:
            case SettingType.NETWORKING_TIMEOUT /* 266 */:
            case SettingType.GOODWAY_SELECTION /* 267 */:
            case 281:
            case SettingType.SHOW_MARKETING /* 293 */:
            case SettingType.SHOW_SHOPPING_BUTTON /* 294 */:
            case SettingType.PAY_QRCODE_LEVEL /* 296 */:
            case SettingType.HEART_DIALOG /* 297 */:
            case SettingType.CAMERA_TEST /* 298 */:
            case SettingType.CAMERA_AUTO_TAKE /* 299 */:
            case SettingType.ALWAYS_HEATING /* 304 */:
            case SettingType.FACEPAY_TYPE /* 325 */:
            case SettingType.DEVICE_DEPLOYMENT_LOCATION /* 333 */:
            case 334:
            case 335:
            case SettingType.PRACTICE_MODE /* 345 */:
            case SettingType.QRCODE_FLOAT_VIEW_ENABLE /* 347 */:
            case SettingType.LIGHTING_CONTROL_TR /* 353 */:
                this.radioGroupItemView.setAlwaysNotDisplaySaveButton();
                this.radioGroupItemView.setChangeListener(new RadioGroupItemView.ChangeListener() { // from class: com.shj.setting.generator.RadioGroupItemNote.12
                    AnonymousClass12() {
                    }

                    @Override // com.shj.setting.widget.RadioGroupItemView.ChangeListener
                    public void change() {
                        RadioGroupItemNote.this.saveSetting(false);
                    }
                });
                return;
            default:
                return;
        }
    }

    /* renamed from: com.shj.setting.generator.RadioGroupItemNote$12 */
    /* loaded from: classes2.dex */
    public class AnonymousClass12 implements RadioGroupItemView.ChangeListener {
        AnonymousClass12() {
        }

        @Override // com.shj.setting.widget.RadioGroupItemView.ChangeListener
        public void change() {
            RadioGroupItemNote.this.saveSetting(false);
        }
    }

    public void showQueryButton() {
        int i = this.settingType;
        if (i == 144 || i == 152 || i == 155) {
            this.radioGroupItemView.setQueryButtonVIsibility(0);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        RadioGroupItemView radioGroupItemView = this.radioGroupItemView;
        if (radioGroupItemView != null) {
            return radioGroupItemView;
        }
        return null;
    }
}
