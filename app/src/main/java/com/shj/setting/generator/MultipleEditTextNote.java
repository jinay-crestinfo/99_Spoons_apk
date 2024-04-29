package com.shj.setting.generator;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Event.BaseEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.OnCommandAnswerListener;
import com.shj.OnShjGoodsSetResultListener;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.ShjGoodsSetting;
import com.shj.biz.ShjManager;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.commandV2.MenuCommandType;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.MutilTextTipDialog;
import com.shj.setting.Dialog.TipDialog;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.R;
import com.shj.setting.Utils.SetUtils;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.event.GetMenuDateEvent;
import com.shj.setting.event.SettingTypeEvent;
import com.shj.setting.event.ShowShelfErrorTipEvent;
import com.shj.setting.helper.GoodsImagesHelper;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.GargoTimeGridView;
import com.shj.setting.widget.MultipleEditItemView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSetting;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MultipleEditTextNote extends SettingNote {
    private static final String DELIMITER = "##";
    private static final String TAG = "MultipleEditTextNote";
    private int cabinetNumber;
    private int countFrequency;
    private int end;
    private int frequency;
    private Handler handler;
    private int index;
    private boolean isTestingShelf;
    private LoadingDialog loadingDialog;
    private MultipleEditItemView multipleEditItemView;
    private MutilTextTipDialog mutilTextTipDialog;
    private int start;

    public MultipleEditTextNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
        this.handler = new Handler() { // from class: com.shj.setting.generator.MultipleEditTextNote.21
            AnonymousClass21() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                MultipleEditTextNote.this.nextShelfTest();
            }
        };
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        switch (this.settingType) {
            case 102:
                String editeText = this.multipleEditItemView.getEditeText(0);
                String editeText2 = this.multipleEditItemView.getEditeText(1);
                if (!TextUtils.isEmpty(editeText) && editeText.length() == 10 && !TextUtils.isEmpty(editeText2)) {
                    long longValue = Long.valueOf(editeText).longValue();
                    String judgeMachineUrl = NetAddress.getJudgeMachineUrl();
                    if (TextUtils.isEmpty(judgeMachineUrl) || (longValue >= 1707600001 && longValue <= 1707600009)) {
                        AppSetting.saveMachineId(this.context, editeText, this.mUserSettingDao);
                        AppSetting.saveMerchantNumber(this.context, editeText2, this.mUserSettingDao);
                        EventBus.getDefault().post(new SettingTypeEvent(this.settingType, null));
                        if (!z) {
                            ToastUitl.showSaveSuccessTip(this.context);
                        }
                        SDFileUtils.writeToSDFromInput("xyShj", "machineid_and_merchant_number.txt", editeText + VoiceWakeuperAidl.PARAMS_SEPARATE + editeText2, false);
                        return;
                    }
                    if (!SetUtils.isConnected(this.context)) {
                        ToastUitl.showShort(this.context, this.context.getString(R.string.net_available_tip));
                        return;
                    }
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("jqbh", editeText);
                        jSONObject.put("shbh", editeText2);
                        RequestItem requestItem = new RequestItem(judgeMachineUrl, jSONObject, "POST");
                        requestItem.setRepeatDelay(5000);
                        requestItem.setRequestMaxCount(1);
                        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.1
                            final /* synthetic */ boolean val$isAllSaveButtonClick;
                            final /* synthetic */ String val$mathineId;
                            final /* synthetic */ String val$merchantNumber;

                            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                            public void onRequestFinished(RequestItem requestItem2, boolean z2) {
                            }

                            AnonymousClass1(String editeText3, String editeText22, boolean z2) {
                                editeText = editeText3;
                                editeText2 = editeText22;
                                z = z2;
                            }

                            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                            public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
                                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.save_fail);
                            }

                            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
                            public boolean onSuccess(RequestItem requestItem2, int i, String str) {
                                String optString;
                                try {
                                    JSONObject jSONObject2 = new JSONObject(str);
                                    if ("H0000".equalsIgnoreCase(jSONObject2.optString("code"))) {
                                        JSONObject optJSONObject = jSONObject2.optJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
                                        if (optJSONObject != null && (optString = optJSONObject.optString("wlzt")) != null && !"0".equals(optString)) {
                                            TipDialog tipDialog = new TipDialog(MultipleEditTextNote.this.context, MultipleEditTextNote.this.context.getString(R.string.update_machine_id_tip), MultipleEditTextNote.this.context.getString(R.string.button_cancel), MultipleEditTextNote.this.context.getString(R.string.button_ok));
                                            tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.1.1
                                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                                public void buttonClick_01() {
                                                }

                                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                                public void timeEnd() {
                                                }

                                                C00641() {
                                                }

                                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                                public void buttonClick_02() {
                                                    AppSetting.saveMachineId(MultipleEditTextNote.this.context, editeText, MultipleEditTextNote.this.mUserSettingDao);
                                                    AppSetting.saveMerchantNumber(MultipleEditTextNote.this.context, editeText2, MultipleEditTextNote.this.mUserSettingDao);
                                                    EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, null));
                                                    if (!z) {
                                                        ToastUitl.showSaveSuccessTip(MultipleEditTextNote.this.context);
                                                    }
                                                    SDFileUtils.writeToSDFromInput("xyShj", "machineid_and_merchant_number.txt", editeText + VoiceWakeuperAidl.PARAMS_SEPARATE + editeText2, false);
                                                }
                                            });
                                            tipDialog.show();
                                            return true;
                                        }
                                        AppSetting.saveMachineId(MultipleEditTextNote.this.context, editeText, MultipleEditTextNote.this.mUserSettingDao);
                                        AppSetting.saveMerchantNumber(MultipleEditTextNote.this.context, editeText2, MultipleEditTextNote.this.mUserSettingDao);
                                        EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, null));
                                        if (!z) {
                                            ToastUitl.showSaveSuccessTip(MultipleEditTextNote.this.context);
                                        }
                                        SDFileUtils.writeToSDFromInput("xyShj", "machineid_and_merchant_number.txt", editeText + VoiceWakeuperAidl.PARAMS_SEPARATE + editeText2, false);
                                    } else {
                                        ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.merchant_number_machineid_error_tip);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }

                            /* renamed from: com.shj.setting.generator.MultipleEditTextNote$1$1 */
                            /* loaded from: classes2.dex */
                            class C00641 implements TipDialog.TipDialogListener {
                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_01() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void timeEnd() {
                                }

                                C00641() {
                                }

                                @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                                public void buttonClick_02() {
                                    AppSetting.saveMachineId(MultipleEditTextNote.this.context, editeText, MultipleEditTextNote.this.mUserSettingDao);
                                    AppSetting.saveMerchantNumber(MultipleEditTextNote.this.context, editeText2, MultipleEditTextNote.this.mUserSettingDao);
                                    EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, null));
                                    if (!z) {
                                        ToastUitl.showSaveSuccessTip(MultipleEditTextNote.this.context);
                                    }
                                    SDFileUtils.writeToSDFromInput("xyShj", "machineid_and_merchant_number.txt", editeText + VoiceWakeuperAidl.PARAMS_SEPARATE + editeText2, false);
                                }
                            }
                        });
                        RequestHelper.request(requestItem);
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                ToastUitl.showNotInputTip(this.context);
                return;
            case 110:
            case 116:
            case SettingType.KEFU_PHONE /* 269 */:
            case SettingType.DEVICE_FACE_SN /* 302 */:
            case 340:
                String settingKey = SettingType.getSettingKey(this.settingType);
                if (!TextUtils.isEmpty(settingKey)) {
                    String editeText3 = this.multipleEditItemView.getEditeText(0);
                    if (!TextUtils.isEmpty(editeText3)) {
                        this.mUserSettingDao.insert(new UserSetting(this.settingType, SettingType.getParentId(this.settingType), settingKey, editeText3));
                    } else {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                }
                EventBus.getDefault().post(new SettingTypeEvent(this.settingType, null));
                if (z2) {
                    return;
                }
                ToastUitl.showSaveSuccessTip(this.context);
                return;
            case 117:
                String editeText4 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText4)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                int intValue = Integer.valueOf(editeText4).intValue();
                if (intValue < 300 || intValue > 999) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog;
                loadingDialog.show();
                commandV2_Up_SetCommand.setAutoEatMoneyTime(true, intValue);
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.3
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass3() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case 120:
                String editeText5 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText5)) {
                    return;
                }
                LoadingDialog loadingDialog2 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog2;
                loadingDialog2.show();
                commandV2_Up_SetCommand.setCoinCount_05(true, Integer.valueOf(editeText5).intValue());
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.4
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass4() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case SettingType.COINS_ONE_YUAN /* 121 */:
                String editeText6 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText6)) {
                    return;
                }
                LoadingDialog loadingDialog3 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog3;
                loadingDialog3.show();
                commandV2_Up_SetCommand.setCoinCount_10(true, Integer.valueOf(editeText6).intValue());
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.5
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass5() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case 125:
                String editeText7 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText7)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                float parseFloat = Float.parseFloat(editeText7);
                showSaveDialog(18, editeText7);
                ShjManager.setShelfGoodsPrice(0, Math.round(parseFloat * 100.0f));
                return;
            case SettingType.INVENTORY_WHOLE_MACHINE /* 133 */:
                String editeText8 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText8)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                int intValue2 = Integer.valueOf(editeText8).intValue();
                if (intValue2 > 255) {
                    ToastUitl.showShort(this.context, this.context.getString(R.string.input_value_should_not_greater_255));
                    return;
                } else {
                    showSaveDialog(19, editeText8);
                    ShjManager.setShelfGoodsCount(0, intValue2);
                    return;
                }
            case SettingType.CARGO_CAPACITY_WHOLE_MACHINE /* 138 */:
                String editeText9 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText9)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                int intValue3 = Integer.valueOf(editeText9).intValue();
                if (intValue3 > 255) {
                    ToastUitl.showShort(this.context, this.context.getString(R.string.input_value_should_not_greater_255));
                    return;
                } else {
                    showSaveDialog(20, editeText9);
                    ShjManager.setShelfInventory(0, intValue3);
                    return;
                }
            case SettingType.CARGO_CODE_WHOLE_MACHINE /* 141 */:
                String editeText10 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText10)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                } else {
                    showSaveDialog(21, editeText10);
                    ShjManager.setShelfGoodsCode(0, editeText10);
                    return;
                }
            case 148:
                String editeText11 = this.multipleEditItemView.getEditeText(0);
                String editeText12 = this.multipleEditItemView.getEditeText(1);
                if (TextUtils.isEmpty(editeText11) || TextUtils.isEmpty(editeText12)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                LoadingDialog loadingDialog4 = new LoadingDialog(this.context, this.context.getString(R.string.saveing));
                this.loadingDialog = loadingDialog4;
                loadingDialog4.show();
                int intValue4 = Integer.valueOf(editeText11).intValue();
                int intValue5 = Integer.valueOf(editeText12).intValue();
                commandV2_Up_SetCommand.setBeltTime(true, this.cabinetNumber + 3000, intValue4, intValue5);
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.2
                    final /* synthetic */ int val$over;
                    final /* synthetic */ int val$stop;

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass2(int intValue42, int intValue52) {
                        intValue4 = intValue42;
                        intValue5 = intValue52;
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.context.getString(R.string.whole_machine));
                        if (z2) {
                            GargoTimeGridView.TimeItemData timeItemData = new GargoTimeGridView.TimeItemData();
                            timeItemData.star = intValue4;
                            timeItemData.end = intValue5;
                            EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, timeItemData));
                        }
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case 157:
                String settingKey2 = SettingType.getSettingKey(this.settingType);
                if (TextUtils.isEmpty(settingKey2)) {
                    return;
                }
                this.mUserSettingDao.insert(new UserSetting(this.settingType, SettingType.getParentId(this.settingType), settingKey2, this.multipleEditItemView.getEditeText(0) + DELIMITER + this.multipleEditItemView.getEditeText(1)));
                if (z2) {
                    return;
                }
                ToastUitl.showSaveSuccessTip(this.context);
                return;
            case 168:
                String editeText13 = this.multipleEditItemView.getEditeText(0);
                if (!TextUtils.isEmpty(editeText13)) {
                    testShelf(Integer.valueOf(editeText13).intValue(), false);
                    return;
                } else {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
            case 169:
                if (this.isTestingShelf) {
                    return;
                }
                String editeText14 = this.multipleEditItemView.getEditeText(0);
                String editeText15 = this.multipleEditItemView.getEditeText(1);
                String editeText16 = this.multipleEditItemView.getEditeText(2);
                if (TextUtils.isEmpty(editeText14) || TextUtils.isEmpty(editeText15) || TextUtils.isEmpty(editeText16)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                this.start = Integer.valueOf(editeText14).intValue();
                this.end = Integer.valueOf(editeText15).intValue();
                int intValue6 = Integer.valueOf(editeText16).intValue();
                this.frequency = intValue6;
                int i = this.start;
                if (i < 0 || i > this.end || intValue6 <= 0) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                this.index = -1;
                this.countFrequency = 0;
                MutilTextTipDialog mutilTextTipDialog = new MutilTextTipDialog(this.context);
                this.mutilTextTipDialog = mutilTextTipDialog;
                mutilTextTipDialog.show();
                this.mutilTextTipDialog.addTextShow(this.context.getString(R.string.test_start));
                nextShelfTest();
                return;
            case SettingType.RESET_LOGIN_PASSWORD /* 171 */:
                String editeText17 = this.multipleEditItemView.getEditeText(0);
                String editeText18 = this.multipleEditItemView.getEditeText(1);
                String editeText19 = this.multipleEditItemView.getEditeText(2);
                if (TextUtils.isEmpty(editeText17) || TextUtils.isEmpty(editeText18) || TextUtils.isEmpty(editeText19)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                if (editeText18.length() != 6) {
                    ToastUitl.showShort(this.context, this.context.getString(R.string.correct_six_new_password));
                    return;
                }
                if (!editeText18.equals(editeText19)) {
                    ToastUitl.showShort(this.context, this.context.getString(R.string.password_not_equal));
                    return;
                }
                LoadingDialog loadingDialog5 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog5;
                loadingDialog5.show();
                commandV2_Up_SetCommand.setResetPassword(true, editeText17, editeText18);
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.14
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass14() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        if (z2) {
                            ToastUitl.showShort(MultipleEditTextNote.this.context, MultipleEditTextNote.this.context.getString(R.string.setting_success));
                        } else {
                            ToastUitl.showShort(MultipleEditTextNote.this.context, MultipleEditTextNote.this.context.getString(R.string.password_error));
                        }
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case 177:
                String editeText20 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText20)) {
                    ToastUitl.showShort(this.context, this.context.getString(R.string.error_input_tip));
                    return;
                }
                LoadingDialog loadingDialog6 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog6;
                loadingDialog6.show();
                commandV2_Up_SetCommand.setWBLHeatTime(true, 0, Integer.valueOf(editeText20).intValue());
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.10
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass10() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName() + MultipleEditTextNote.this.context.getString(R.string.heating_time));
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case 184:
                String editeText21 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText21)) {
                    ToastUitl.showShort(this.context, this.context.getString(R.string.error_input_tip) + "(10)");
                    return;
                }
                int intValue7 = Integer.valueOf(editeText21).intValue();
                if (intValue7 < 1 || intValue7 > 200) {
                    ToastUitl.showShort(this.context, this.context.getString(R.string.error_input_tip) + "(10)");
                    return;
                }
                LoadingDialog loadingDialog7 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog7;
                loadingDialog7.show();
                commandV2_Up_SetCommand.setPaperMoney(true, intValue7);
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.9
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass9() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case SettingType.BANKNOTE_CHANGE_OF_MONEY /* 186 */:
                String editeText22 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText22)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                LoadingDialog loadingDialog8 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog8;
                loadingDialog8.show();
                commandV2_Up_SetCommand.setAcceptPaperMinChargeMoney(true, Integer.valueOf(editeText22).intValue());
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.15
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass15() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case SettingType.CLEAR_LOCAL_SALES_RECORDS /* 195 */:
                TipDialog tipDialog = new TipDialog(this.context, 0, R.string.clear_local_sales_records_tip, R.string.clear, R.string.cancel);
                tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.8
                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_02() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void timeEnd() {
                    }

                    AnonymousClass8() {
                    }

                    @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                    public void buttonClick_01() {
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                        String editeText23 = MultipleEditTextNote.this.multipleEditItemView.getEditeText(0);
                        if (!TextUtils.isEmpty(editeText23) && editeText23.length() == 6) {
                            MultipleEditTextNote.this.loadingDialog = new LoadingDialog(MultipleEditTextNote.this.context, R.string.on_clearing);
                            MultipleEditTextNote.this.loadingDialog.show();
                            commandV2_Up_SetCommand2.setClearSales(true, editeText23);
                            Shj.getInstance(MultipleEditTextNote.this.context);
                            Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.8.1
                                AnonymousClass1() {
                                }

                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandReadAnswer(byte[] bArr) {
                                    MultipleEditTextNote.this.loadingDialog.dismiss();
                                }

                                @Override // com.shj.OnCommandAnswerListener
                                public void onCommandSetAnswer(boolean z2) {
                                    Loger.writeLog("SET", "返回的值是:" + z2);
                                    if (z2) {
                                        ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                                    } else {
                                        ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.error_password);
                                    }
                                    MultipleEditTextNote.this.loadingDialog.dismiss();
                                }
                            });
                            return;
                        }
                        ToastUitl.showPasswordTip(MultipleEditTextNote.this.context);
                    }

                    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$8$1 */
                    /* loaded from: classes2.dex */
                    class AnonymousClass1 implements OnCommandAnswerListener {
                        AnonymousClass1() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                            MultipleEditTextNote.this.loadingDialog.dismiss();
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z2) {
                            Loger.writeLog("SET", "返回的值是:" + z2);
                            if (z2) {
                                ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                            } else {
                                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.error_password);
                            }
                            MultipleEditTextNote.this.loadingDialog.dismiss();
                        }
                    }
                });
                tipDialog.show();
                return;
            case SettingType.MANUAL_POSITIONING_OF_BOXES /* 233 */:
                String editeText23 = this.multipleEditItemView.getEditeText(0);
                String editeText24 = this.multipleEditItemView.getEditeText(1);
                if (TextUtils.isEmpty(editeText23) || TextUtils.isEmpty(editeText24)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                LoadingDialog loadingDialog9 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog9;
                loadingDialog9.show();
                commandV2_Up_SetCommand.setBoxPosition(true, Integer.valueOf(editeText23).intValue(), Integer.valueOf(editeText24).intValue());
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.11
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass11() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case 235:
                String editeText25 = this.multipleEditItemView.getEditeText(0);
                String editeText26 = this.multipleEditItemView.getEditeText(1);
                if (TextUtils.isEmpty(editeText25) || TextUtils.isEmpty(editeText26)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                LoadingDialog loadingDialog10 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog10;
                loadingDialog10.show();
                commandV2_Up_SetCommand.setWBLHeatTime(true, Integer.valueOf(editeText25).intValue(), Integer.valueOf(editeText26).intValue());
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.12
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass12() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case 246:
                String editeText27 = this.multipleEditItemView.getEditeText(0);
                String editeText28 = this.multipleEditItemView.getEditeText(1);
                String editeText29 = this.multipleEditItemView.getEditeText(2);
                if (TextUtils.isEmpty(editeText27) || TextUtils.isEmpty(editeText28) || TextUtils.isEmpty(editeText29)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                int intValue8 = Integer.valueOf(editeText27).intValue();
                if (intValue8 < 0 || intValue8 > 9) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                int intValue9 = Integer.valueOf(editeText28).intValue();
                int intValue10 = Integer.valueOf(editeText29).intValue();
                LoadingDialog loadingDialog11 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog11;
                loadingDialog11.show();
                commandV2_Up_SetCommand.setHFJ_HZ_spacing(intValue8, intValue9, intValue10);
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.13
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                    }

                    AnonymousClass13() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        if (bArr != null && bArr.length > 0) {
                            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                            String[] stringArray = MultipleEditTextNote.this.context.getResources().getStringArray(R.array.box_spacing_result);
                            if (intFromBytes >= 0 && intFromBytes < stringArray.length) {
                                ToastUitl.showShort(MultipleEditTextNote.this.context, stringArray[intFromBytes]);
                            }
                        }
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case SettingType.FAULT_TEMPERATURE_PROBE /* 272 */:
                String editeText30 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText30)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                int intValue11 = Integer.valueOf(editeText30).intValue();
                if (intValue11 < 5 || intValue11 > 180) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                LoadingDialog loadingDialog12 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog12;
                loadingDialog12.show();
                commandV2_Up_SetCommand.setfaultTemperatureProbe(true, intValue11);
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.16
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass16() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case SettingType.ELECTROMAGNETIC_LOCK_ON_TIME_WHOLE_MACHINE /* 276 */:
                String editeText31 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText31)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                int intValue12 = Integer.valueOf(editeText31).intValue();
                CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand2.setElectromagneticLockTime(true, this.cabinetNumber + 3000, intValue12);
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.6
                    final /* synthetic */ String val$whole_lock_time;

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass6(String editeText312) {
                        editeText31 = editeText312;
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        Loger.writeLog("SET", "返回的值是:" + z2);
                        ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                        if (z2) {
                            EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, editeText31));
                        }
                    }
                });
                return;
            case SettingType.DOWNLOAD_SIGNAL_GOODS_PIC /* 317 */:
                String editeText32 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText32)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                } else {
                    downloadGoodsPic(Integer.valueOf(editeText32).intValue());
                    return;
                }
            case SettingType.LIGHT_BOX_ROLLING_INTERVAL /* 320 */:
                String editeText33 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText33)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                LoadingDialog loadingDialog13 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog13;
                loadingDialog13.show();
                commandV2_Up_SetCommand.setLightBoxRollingInterval(true, Integer.valueOf(editeText33).intValue());
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.17
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass17() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.setting_success);
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case 331:
                String editeText34 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText34)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                int intValue13 = Integer.valueOf(editeText34).intValue();
                CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand3.setNewMergeShelSynRunTime(true, this.cabinetNumber + 3000, intValue13);
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand3, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.7
                    final /* synthetic */ String val$whole_lock_time;

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass7(String editeText342) {
                        editeText34 = editeText342;
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        Loger.writeLog("SET", "返回的值是:" + z2);
                        ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                        if (z2) {
                            EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, editeText34));
                        }
                    }
                });
                return;
            case SettingType.DRUG_BOX_MENU_NAME /* 341 */:
                String editeText35 = this.multipleEditItemView.getEditeText(0);
                String editeText36 = this.multipleEditItemView.getEditeText(1);
                if (TextUtils.isEmpty(editeText35) || TextUtils.isEmpty(editeText36)) {
                    ToastUitl.showShort(this.context, this.context.getString(R.string.please_input) + this.context.getString(R.string.lab_name));
                    return;
                }
                AppSetting.saveDrugBoxMenuNameLeft(this.context, editeText35, this.mUserSettingDao);
                AppSetting.saveDrugBoxMenuNameRight(this.context, editeText36, this.mUserSettingDao);
                ToastUitl.showShort(this.context, R.string.save_success);
                return;
            case 350:
                String editeText37 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText37)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                LoadingDialog loadingDialog14 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog14;
                loadingDialog14.show();
                commandV2_Up_SetCommand.setDisinfectantTankControl(true, Integer.valueOf(editeText37).intValue());
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.18
                    AnonymousClass18() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        if (bArr != null && bArr.length > 0) {
                            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                            if (intFromBytes == 0) {
                                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.setting_success);
                            } else if (intFromBytes == 1) {
                                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.warter_cup_take);
                            } else if (intFromBytes == 2) {
                                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.warter_not_enough);
                            }
                        } else {
                            ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.setting_fail);
                        }
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            case SettingType.WATER_INLET_SOLENOID_VALVE_CONTROL /* 351 */:
                String editeText38 = this.multipleEditItemView.getEditeText(0);
                if (TextUtils.isEmpty(editeText38)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                LoadingDialog loadingDialog15 = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog15;
                loadingDialog15.show();
                commandV2_Up_SetCommand.setWaterInletSolenoidValveControl(true, Integer.valueOf(editeText38).intValue());
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.19
                    AnonymousClass19() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        if (bArr != null && bArr.length > 0) {
                            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                            if (intFromBytes == 0) {
                                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.setting_success);
                            } else if (intFromBytes == 1) {
                                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.warter_cup_take);
                            } else if (intFromBytes == 2) {
                                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.warter_not_enough);
                            }
                        } else {
                            ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.setting_fail);
                        }
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestItem.OnRequestResultListener {
        final /* synthetic */ boolean val$isAllSaveButtonClick;
        final /* synthetic */ String val$mathineId;
        final /* synthetic */ String val$merchantNumber;

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z2) {
        }

        AnonymousClass1(String editeText3, String editeText22, boolean z2) {
            editeText = editeText3;
            editeText2 = editeText22;
            z = z2;
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str, Throwable th) {
            ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.save_fail);
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str) {
            String optString;
            try {
                JSONObject jSONObject2 = new JSONObject(str);
                if ("H0000".equalsIgnoreCase(jSONObject2.optString("code"))) {
                    JSONObject optJSONObject = jSONObject2.optJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    if (optJSONObject != null && (optString = optJSONObject.optString("wlzt")) != null && !"0".equals(optString)) {
                        TipDialog tipDialog = new TipDialog(MultipleEditTextNote.this.context, MultipleEditTextNote.this.context.getString(R.string.update_machine_id_tip), MultipleEditTextNote.this.context.getString(R.string.button_cancel), MultipleEditTextNote.this.context.getString(R.string.button_ok));
                        tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.1.1
                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_01() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void timeEnd() {
                            }

                            C00641() {
                            }

                            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
                            public void buttonClick_02() {
                                AppSetting.saveMachineId(MultipleEditTextNote.this.context, editeText, MultipleEditTextNote.this.mUserSettingDao);
                                AppSetting.saveMerchantNumber(MultipleEditTextNote.this.context, editeText2, MultipleEditTextNote.this.mUserSettingDao);
                                EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, null));
                                if (!z) {
                                    ToastUitl.showSaveSuccessTip(MultipleEditTextNote.this.context);
                                }
                                SDFileUtils.writeToSDFromInput("xyShj", "machineid_and_merchant_number.txt", editeText + VoiceWakeuperAidl.PARAMS_SEPARATE + editeText2, false);
                            }
                        });
                        tipDialog.show();
                        return true;
                    }
                    AppSetting.saveMachineId(MultipleEditTextNote.this.context, editeText, MultipleEditTextNote.this.mUserSettingDao);
                    AppSetting.saveMerchantNumber(MultipleEditTextNote.this.context, editeText2, MultipleEditTextNote.this.mUserSettingDao);
                    EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, null));
                    if (!z) {
                        ToastUitl.showSaveSuccessTip(MultipleEditTextNote.this.context);
                    }
                    SDFileUtils.writeToSDFromInput("xyShj", "machineid_and_merchant_number.txt", editeText + VoiceWakeuperAidl.PARAMS_SEPARATE + editeText2, false);
                } else {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.merchant_number_machineid_error_tip);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        /* renamed from: com.shj.setting.generator.MultipleEditTextNote$1$1 */
        /* loaded from: classes2.dex */
        class C00641 implements TipDialog.TipDialogListener {
            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_01() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void timeEnd() {
            }

            C00641() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_02() {
                AppSetting.saveMachineId(MultipleEditTextNote.this.context, editeText, MultipleEditTextNote.this.mUserSettingDao);
                AppSetting.saveMerchantNumber(MultipleEditTextNote.this.context, editeText2, MultipleEditTextNote.this.mUserSettingDao);
                EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, null));
                if (!z) {
                    ToastUitl.showSaveSuccessTip(MultipleEditTextNote.this.context);
                }
                SDFileUtils.writeToSDFromInput("xyShj", "machineid_and_merchant_number.txt", editeText + VoiceWakeuperAidl.PARAMS_SEPARATE + editeText2, false);
            }
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements OnCommandAnswerListener {
        final /* synthetic */ int val$over;
        final /* synthetic */ int val$stop;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass2(int intValue42, int intValue52) {
            intValue4 = intValue42;
            intValue5 = intValue52;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.context.getString(R.string.whole_machine));
            if (z2) {
                GargoTimeGridView.TimeItemData timeItemData = new GargoTimeGridView.TimeItemData();
                timeItemData.star = intValue4;
                timeItemData.end = intValue5;
                EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, timeItemData));
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass3() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass4() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$5 */
    /* loaded from: classes2.dex */
    class AnonymousClass5 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass5() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$6 */
    /* loaded from: classes2.dex */
    class AnonymousClass6 implements OnCommandAnswerListener {
        final /* synthetic */ String val$whole_lock_time;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass6(String editeText312) {
            editeText31 = editeText312;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            Loger.writeLog("SET", "返回的值是:" + z2);
            ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
            if (z2) {
                EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, editeText31));
            }
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$7 */
    /* loaded from: classes2.dex */
    class AnonymousClass7 implements OnCommandAnswerListener {
        final /* synthetic */ String val$whole_lock_time;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass7(String editeText342) {
            editeText34 = editeText342;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            Loger.writeLog("SET", "返回的值是:" + z2);
            ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
            if (z2) {
                EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, editeText34));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements TipDialog.TipDialogListener {
        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_02() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void timeEnd() {
        }

        AnonymousClass8() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_01() {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
            String editeText23 = MultipleEditTextNote.this.multipleEditItemView.getEditeText(0);
            if (!TextUtils.isEmpty(editeText23) && editeText23.length() == 6) {
                MultipleEditTextNote.this.loadingDialog = new LoadingDialog(MultipleEditTextNote.this.context, R.string.on_clearing);
                MultipleEditTextNote.this.loadingDialog.show();
                commandV2_Up_SetCommand2.setClearSales(true, editeText23);
                Shj.getInstance(MultipleEditTextNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.8.1
                    AnonymousClass1() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        Loger.writeLog("SET", "返回的值是:" + z2);
                        if (z2) {
                            ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                        } else {
                            ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.error_password);
                        }
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                    }
                });
                return;
            }
            ToastUitl.showPasswordTip(MultipleEditTextNote.this.context);
        }

        /* renamed from: com.shj.setting.generator.MultipleEditTextNote$8$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements OnCommandAnswerListener {
            AnonymousClass1() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                MultipleEditTextNote.this.loadingDialog.dismiss();
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z2) {
                Loger.writeLog("SET", "返回的值是:" + z2);
                if (z2) {
                    ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
                } else {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.error_password);
                }
                MultipleEditTextNote.this.loadingDialog.dismiss();
            }
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$9 */
    /* loaded from: classes2.dex */
    class AnonymousClass9 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass9() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$10 */
    /* loaded from: classes2.dex */
    class AnonymousClass10 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass10() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName() + MultipleEditTextNote.this.context.getString(R.string.heating_time));
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$11 */
    /* loaded from: classes2.dex */
    class AnonymousClass11 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass11() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$12 */
    /* loaded from: classes2.dex */
    class AnonymousClass12 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass12() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$13 */
    /* loaded from: classes2.dex */
    class AnonymousClass13 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass13() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                String[] stringArray = MultipleEditTextNote.this.context.getResources().getStringArray(R.array.box_spacing_result);
                if (intFromBytes >= 0 && intFromBytes < stringArray.length) {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, stringArray[intFromBytes]);
                }
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$14 */
    /* loaded from: classes2.dex */
    class AnonymousClass14 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass14() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            if (z2) {
                ToastUitl.showShort(MultipleEditTextNote.this.context, MultipleEditTextNote.this.context.getString(R.string.setting_success));
            } else {
                ToastUitl.showShort(MultipleEditTextNote.this.context, MultipleEditTextNote.this.context.getString(R.string.password_error));
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$15 */
    /* loaded from: classes2.dex */
    class AnonymousClass15 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass15() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$16 */
    /* loaded from: classes2.dex */
    class AnonymousClass16 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass16() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, z2, MultipleEditTextNote.this.getSettingName());
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$17 */
    /* loaded from: classes2.dex */
    class AnonymousClass17 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass17() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.setting_success);
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$18 */
    /* loaded from: classes2.dex */
    class AnonymousClass18 implements OnCommandAnswerListener {
        AnonymousClass18() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                if (intFromBytes == 0) {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.setting_success);
                } else if (intFromBytes == 1) {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.warter_cup_take);
                } else if (intFromBytes == 2) {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.warter_not_enough);
                }
            } else {
                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.setting_fail);
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$19 */
    /* loaded from: classes2.dex */
    class AnonymousClass19 implements OnCommandAnswerListener {
        AnonymousClass19() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                if (intFromBytes == 0) {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.setting_success);
                } else if (intFromBytes == 1) {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.warter_cup_take);
                } else if (intFromBytes == 2) {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.warter_not_enough);
                }
            } else {
                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.setting_fail);
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    private void downloadGoodsPic(int i) {
        ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
        if (shelfInfo == null) {
            return;
        }
        new GoodsImagesHelper(this.context).downLoadGoodsImage(AppSetting.getMachineId(this.context, this.mUserSettingDao), String.format("%04d", shelfInfo.getGoodsCode()));
    }

    public void nextShelfTest() {
        this.isTestingShelf = true;
        int i = this.index;
        if (i == -1) {
            int i2 = this.start;
            this.index = i2;
            testShelf(i2, true);
            return;
        }
        int i3 = i + 1;
        this.index = i3;
        if (i3 <= this.end) {
            testShelf(i3, true);
            return;
        }
        int i4 = this.countFrequency + 1;
        this.countFrequency = i4;
        if (i4 < this.frequency) {
            int i5 = this.start;
            this.index = i5;
            testShelf(i5, true);
        } else {
            this.mutilTextTipDialog.addTextShow(this.context.getString(R.string.test_complete));
            this.isTestingShelf = false;
        }
    }

    private void testShelf(int i, boolean z) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.TestShelf(false, 1, i);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.20
            final /* synthetic */ boolean val$isNeedTestNext;
            final /* synthetic */ int val$shelf;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z2) {
            }

            AnonymousClass20(int i2, boolean z2) {
                i = i2;
                z = z2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (!bArr.toString().equals("0")) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                    Loger.writeLog("SET", "截取的下位机的返回的值是:" + intFromBytes);
                    String shelfState = SetUtils.getShelfState(MultipleEditTextNote.this.context, intFromBytes, i);
                    if (MultipleEditTextNote.this.mutilTextTipDialog != null) {
                        MultipleEditTextNote.this.mutilTextTipDialog.addTextShow(shelfState);
                        MultipleEditTextNote.this.mutilTextTipDialog.addStatisticalInfo(intFromBytes, SetUtils.getShelfState(MultipleEditTextNote.this.context, intFromBytes));
                    } else {
                        ToastUitl.showShort(MultipleEditTextNote.this.context, SetUtils.getShelfState(MultipleEditTextNote.this.context, intFromBytes));
                    }
                } else {
                    ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, false, MultipleEditTextNote.this.getSettingName());
                }
                if (z) {
                    MultipleEditTextNote.this.handler.sendEmptyMessageDelayed(0, 100L);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$20 */
    /* loaded from: classes2.dex */
    public class AnonymousClass20 implements OnCommandAnswerListener {
        final /* synthetic */ boolean val$isNeedTestNext;
        final /* synthetic */ int val$shelf;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass20(int i2, boolean z2) {
            i = i2;
            z = z2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (!bArr.toString().equals("0")) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                Loger.writeLog("SET", "截取的下位机的返回的值是:" + intFromBytes);
                String shelfState = SetUtils.getShelfState(MultipleEditTextNote.this.context, intFromBytes, i);
                if (MultipleEditTextNote.this.mutilTextTipDialog != null) {
                    MultipleEditTextNote.this.mutilTextTipDialog.addTextShow(shelfState);
                    MultipleEditTextNote.this.mutilTextTipDialog.addStatisticalInfo(intFromBytes, SetUtils.getShelfState(MultipleEditTextNote.this.context, intFromBytes));
                } else {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, SetUtils.getShelfState(MultipleEditTextNote.this.context, intFromBytes));
                }
            } else {
                ToastUitl.showCompeleteTip(MultipleEditTextNote.this.context, false, MultipleEditTextNote.this.getSettingName());
            }
            if (z) {
                MultipleEditTextNote.this.handler.sendEmptyMessageDelayed(0, 100L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$21 */
    /* loaded from: classes2.dex */
    public class AnonymousClass21 extends Handler {
        AnonymousClass21() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            MultipleEditTextNote.this.nextShelfTest();
        }
    }

    private void setEditText() {
        UserSetting userSettingFormKey;
        String value;
        UserSetting userSettingFormKey2;
        String value2;
        String settingKey = SettingType.getSettingKey(this.settingType);
        int i = this.settingType;
        if (i != 102) {
            if (i != 110 && i != 116) {
                if (i == 157) {
                    if (TextUtils.isEmpty(settingKey) || (userSettingFormKey2 = this.mUserSettingDao.getUserSettingFormKey(settingKey)) == null || (value2 = userSettingFormKey2.getValue()) == null) {
                        return;
                    }
                    String[] split = value2.split(DELIMITER);
                    int i2 = 0;
                    for (String str : split) {
                        this.multipleEditItemView.setEditeText(i2, split[i2]);
                        i2++;
                    }
                    return;
                }
                if (i == 269) {
                    this.multipleEditItemView.setEditeText(0, AppSetting.getKeFuPhone(this.context, null));
                    return;
                }
                if (i != 302 && i != 120 && i != 121 && i != 340) {
                    if (i != 341) {
                        return;
                    }
                    String drugBoxMenuNameLeft = AppSetting.getDrugBoxMenuNameLeft(this.context, this.mUserSettingDao);
                    String drugBoxMenuNameRight = AppSetting.getDrugBoxMenuNameRight(this.context, this.mUserSettingDao);
                    if (!TextUtils.isEmpty(drugBoxMenuNameLeft)) {
                        this.multipleEditItemView.setEditeText(0, drugBoxMenuNameLeft);
                    }
                    if (TextUtils.isEmpty(drugBoxMenuNameRight)) {
                        return;
                    }
                    this.multipleEditItemView.setEditeText(1, drugBoxMenuNameRight);
                    return;
                }
            }
            if (TextUtils.isEmpty(settingKey) || (userSettingFormKey = this.mUserSettingDao.getUserSettingFormKey(settingKey)) == null || (value = userSettingFormKey.getValue()) == null) {
                return;
            }
            this.multipleEditItemView.setEditeText(0, value);
            return;
        }
        String machineId = AppSetting.getMachineId(this.context, this.mUserSettingDao);
        if (!TextUtils.isEmpty(machineId)) {
            this.multipleEditItemView.setEditeText(0, machineId);
        }
        String merchantNumber = AppSetting.getMerchantNumber(this.context, this.mUserSettingDao);
        if (TextUtils.isEmpty(machineId)) {
            return;
        }
        this.multipleEditItemView.setEditeText(1, merchantNumber);
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

    @Override // com.shj.setting.generator.SettingNote
    public void clearData() {
        int i = this.settingType;
        if (i == 276) {
            LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setElectromagneticLockTime(false, this.cabinetNumber + 3000, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.23
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass23() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                            MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                            if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                                return;
                            }
                            MultipleEditTextNote.this.loadingDialog.dismiss();
                            return;
                        }
                        ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.communication_error);
                        if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                            return;
                        }
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                        return;
                    }
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
                    if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                        return;
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i != 331) {
            return;
        }
        LoadingDialog loadingDialog2 = new LoadingDialog(this.context, R.string.on_querying);
        this.loadingDialog = loadingDialog2;
        loadingDialog2.show();
        CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand2.setNewMergeShelSynRunTime(false, this.cabinetNumber + 3000, 0);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.22
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass22() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length > 0) {
                    if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                        MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                        if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                            return;
                        }
                        MultipleEditTextNote.this.loadingDialog.dismiss();
                        return;
                    }
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.news_report_error);
                    if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                        return;
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                    return;
                }
                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
                if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                    return;
                }
                MultipleEditTextNote.this.loadingDialog.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$22 */
    /* loaded from: classes2.dex */
    class AnonymousClass22 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass22() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                    if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                        return;
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                    return;
                }
                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.news_report_error);
                if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                    return;
                }
                MultipleEditTextNote.this.loadingDialog.dismiss();
                return;
            }
            ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
            if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                return;
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$23 */
    /* loaded from: classes2.dex */
    class AnonymousClass23 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass23() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                    if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                        return;
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                    return;
                }
                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.communication_error);
                if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                    return;
                }
                MultipleEditTextNote.this.loadingDialog.dismiss();
                return;
            }
            ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
            if (MultipleEditTextNote.this.loadingDialog == null || !MultipleEditTextNote.this.loadingDialog.isShowing()) {
                return;
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        int i = this.settingType;
        if (i == 117) {
            LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            commandV2_Up_SetCommand.setAutoEatMoneyTime(false, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.28
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass28() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, bArr.length));
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 148) {
            LoadingDialog loadingDialog2 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog2;
            loadingDialog2.show();
            commandV2_Up_SetCommand.setBeltTime(false, this.cabinetNumber + 3000, 0, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.32
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass32() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                            int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 2);
                            int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 3, 2);
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(String.valueOf(intFromBytes));
                            arrayList.add(String.valueOf(intFromBytes2));
                            MultipleEditTextNote.this.setData(arrayList);
                        } else {
                            ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.communication_error);
                        }
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 177) {
            LoadingDialog loadingDialog3 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog3;
            loadingDialog3.show();
            commandV2_Up_SetCommand.setWBLHeatTime(false, 0, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.27
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass27() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 1) {
                        MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, 2));
                    } else {
                        ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 233) {
            String editeText = this.multipleEditItemView.getEditeText(0);
            if (TextUtils.isEmpty(editeText)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            LoadingDialog loadingDialog4 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog4;
            loadingDialog4.show();
            commandV2_Up_SetCommand.setBoxPosition(false, Integer.valueOf(editeText).intValue(), 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.24
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass24() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        MultipleEditTextNote.this.multipleEditItemView.setEditeText(1, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, bArr.length)));
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 235) {
            String editeText2 = this.multipleEditItemView.getEditeText(0);
            if (TextUtils.isEmpty(editeText2)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            LoadingDialog loadingDialog5 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog5;
            loadingDialog5.show();
            commandV2_Up_SetCommand.setWBLHeatTime(false, Integer.valueOf(editeText2).intValue(), 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.25
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass25() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        MultipleEditTextNote.this.multipleEditItemView.setEditeText(1, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, bArr.length)));
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 272) {
            LoadingDialog loadingDialog6 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog6;
            loadingDialog6.show();
            commandV2_Up_SetCommand.setfaultTemperatureProbe(false, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.26
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass26() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 1)));
                    } else {
                        ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 317) {
            String editeText3 = this.multipleEditItemView.getEditeText(0);
            if (TextUtils.isEmpty(editeText3)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            } else {
                downloadGoodsDetailPic(Integer.valueOf(editeText3).intValue());
                return;
            }
        }
        if (i == 320) {
            LoadingDialog loadingDialog7 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog7;
            loadingDialog7.show();
            commandV2_Up_SetCommand.setLightBoxRollingInterval(false, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.31
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass31() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, bArr.length)));
                    } else {
                        ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 120) {
            LoadingDialog loadingDialog8 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog8;
            loadingDialog8.show();
            commandV2_Up_SetCommand.setCoinCount_05(false, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.29
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass29() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, bArr.length));
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 121) {
            LoadingDialog loadingDialog9 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog9;
            loadingDialog9.show();
            commandV2_Up_SetCommand.setCoinCount_10(false, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.30
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass30() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, bArr.length));
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 350) {
            LoadingDialog loadingDialog10 = new LoadingDialog(this.context, R.string.saveing);
            this.loadingDialog = loadingDialog10;
            loadingDialog10.show();
            commandV2_Up_SetCommand.setDisinfectantTankControl(false, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.33
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass33() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length >= 2) {
                        MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 2)));
                    } else {
                        ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i != 351) {
            return;
        }
        LoadingDialog loadingDialog11 = new LoadingDialog(this.context, R.string.saveing);
        this.loadingDialog = loadingDialog11;
        loadingDialog11.show();
        commandV2_Up_SetCommand.setWaterInletSolenoidValveControl(false, 0);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.34
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass34() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length >= 2) {
                    MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 2)));
                } else {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
                }
                MultipleEditTextNote.this.loadingDialog.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$24 */
    /* loaded from: classes2.dex */
    class AnonymousClass24 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass24() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                MultipleEditTextNote.this.multipleEditItemView.setEditeText(1, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, bArr.length)));
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$25 */
    /* loaded from: classes2.dex */
    class AnonymousClass25 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass25() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                MultipleEditTextNote.this.multipleEditItemView.setEditeText(1, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, bArr.length)));
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$26 */
    /* loaded from: classes2.dex */
    class AnonymousClass26 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass26() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 1)));
            } else {
                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$27 */
    /* loaded from: classes2.dex */
    class AnonymousClass27 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass27() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 1) {
                MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, 2));
            } else {
                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$28 */
    /* loaded from: classes2.dex */
    class AnonymousClass28 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass28() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, bArr.length));
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$29 */
    /* loaded from: classes2.dex */
    class AnonymousClass29 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass29() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, bArr.length));
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$30 */
    /* loaded from: classes2.dex */
    class AnonymousClass30 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass30() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, bArr.length));
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$31 */
    /* loaded from: classes2.dex */
    class AnonymousClass31 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass31() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, bArr.length)));
            } else {
                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$32 */
    /* loaded from: classes2.dex */
    class AnonymousClass32 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass32() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 2);
                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 3, 2);
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(String.valueOf(intFromBytes));
                    arrayList.add(String.valueOf(intFromBytes2));
                    MultipleEditTextNote.this.setData(arrayList);
                } else {
                    ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.communication_error);
                }
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$33 */
    /* loaded from: classes2.dex */
    class AnonymousClass33 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass33() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length >= 2) {
                MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 2)));
            } else {
                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$34 */
    /* loaded from: classes2.dex */
    class AnonymousClass34 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass34() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length >= 2) {
                MultipleEditTextNote.this.multipleEditItemView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 2)));
            } else {
                ToastUitl.showShort(MultipleEditTextNote.this.context, R.string.qurey_fail);
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    private void downloadGoodsDetailPic(int i) {
        ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(i));
        if (shelfInfo == null) {
            return;
        }
        new GoodsImagesHelper(this.context).requestGoodsXqImage(true, AppSetting.getMachineId(this.context, this.mUserSettingDao), String.format("%04d", shelfInfo.getGoodsCode()));
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof GetMenuDateEvent) {
            GetMenuDateEvent getMenuDateEvent = (GetMenuDateEvent) baseEvent;
            int i = this.settingType;
            if (i == 117) {
                if (getMenuDateEvent.menuCommandType == MenuCommandType.TYPE_SWALLOWING_MONEY_TIME) {
                    int intValue = ((Integer) getMenuDateEvent.data).intValue();
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(String.valueOf(intValue));
                    setData(arrayList);
                    return;
                }
                return;
            }
            if (i == 120) {
                if (getMenuDateEvent.menuCommandType == MenuCommandType.TYPE_5CENTS) {
                    int intValue2 = ((Integer) getMenuDateEvent.data).intValue();
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.add(String.valueOf(intValue2));
                    setData(arrayList2);
                    return;
                }
                return;
            }
            if (i == 121 && getMenuDateEvent.menuCommandType == MenuCommandType.TYPE_ONE_YUAN) {
                int intValue3 = ((Integer) getMenuDateEvent.data).intValue();
                ArrayList arrayList3 = new ArrayList();
                arrayList3.add(String.valueOf(intValue3));
                setData(arrayList3);
            }
        }
    }

    public void queryData() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        int i = this.settingType;
        if (i == 184) {
            LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            commandV2_Up_SetCommand.setPaperMoney(false, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.35
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass35() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, 1));
                    }
                    MultipleEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i != 186) {
            return;
        }
        LoadingDialog loadingDialog2 = new LoadingDialog(this.context, R.string.on_querying);
        this.loadingDialog = loadingDialog2;
        loadingDialog2.show();
        commandV2_Up_SetCommand.setAcceptPaperMinChargeMoney(false, 0);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.36
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass36() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length > 0) {
                    MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, 1));
                }
                MultipleEditTextNote.this.loadingDialog.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$35 */
    /* loaded from: classes2.dex */
    public class AnonymousClass35 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass35() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, 1));
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$36 */
    /* loaded from: classes2.dex */
    public class AnonymousClass36 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass36() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                MultipleEditTextNote.this.setData(ObjectHelper.intFromBytes(bArr, 0, 1));
            }
            MultipleEditTextNote.this.loadingDialog.dismiss();
        }
    }

    public void setData(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.valueOf(i));
        setData(arrayList);
    }

    public void setData(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            this.multipleEditItemView.setEditeText(i, list.get(i));
        }
    }

    private List<MultipleEditItemView.EditTextDataInfo> getEditTextDataInfoList() {
        ArrayList arrayList = new ArrayList();
        String string = this.context.getString(R.string.please_input);
        if (CommonTool.getLanguage(this.context).equals("en") || CommonTool.getLanguage(this.context).equals("fr")) {
            string = string + StringUtils.SPACE;
        }
        switch (this.settingType) {
            case 102:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo.name = getSettingName();
                editTextDataInfo.tipInfo = string + editTextDataInfo.name;
                arrayList.add(editTextDataInfo);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo2 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo2.name = this.context.getString(R.string.merchant_number);
                editTextDataInfo2.tipInfo = string + editTextDataInfo2.name;
                arrayList.add(editTextDataInfo2);
                return arrayList;
            case 117:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo3 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo3.name = getSettingName() + "(300-999s)";
                editTextDataInfo3.tipInfo = string + getSettingName();
                arrayList.add(editTextDataInfo3);
                return arrayList;
            case 120:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo4 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo4.name = getSettingName();
                editTextDataInfo4.tipInfo = string + this.context.getString(R.string.coins_finve_cents_tip);
                arrayList.add(editTextDataInfo4);
                return arrayList;
            case SettingType.COINS_ONE_YUAN /* 121 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo5 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo5.name = getSettingName();
                editTextDataInfo5.tipInfo = string + this.context.getString(R.string.coins_one_yuan_tip);
                arrayList.add(editTextDataInfo5);
                return arrayList;
            case 125:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo6 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo6.name = getSettingName();
                editTextDataInfo6.tipInfo = string + this.context.getString(R.string.lab_price);
                arrayList.add(editTextDataInfo6);
                return arrayList;
            case SettingType.INVENTORY_WHOLE_MACHINE /* 133 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo7 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo7.name = getSettingName();
                editTextDataInfo7.tipInfo = string + this.context.getString(R.string.lab_stock);
                arrayList.add(editTextDataInfo7);
                return arrayList;
            case SettingType.CARGO_CAPACITY_WHOLE_MACHINE /* 138 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo8 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo8.name = getSettingName();
                editTextDataInfo8.tipInfo = string + this.context.getString(R.string.lab_capacity);
                arrayList.add(editTextDataInfo8);
                return arrayList;
            case SettingType.CARGO_CODE_WHOLE_MACHINE /* 141 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo9 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo9.name = getSettingName();
                editTextDataInfo9.tipInfo = string + this.context.getString(R.string.cargo_code);
                arrayList.add(editTextDataInfo9);
                return arrayList;
            case 148:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo10 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo10.name = this.context.getString(R.string.time_over);
                editTextDataInfo10.tipInfo = string + editTextDataInfo10.name;
                arrayList.add(editTextDataInfo10);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo11 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo11.name = this.context.getString(R.string.time_stop);
                editTextDataInfo11.tipInfo = string + editTextDataInfo11.name;
                arrayList.add(editTextDataInfo11);
                return arrayList;
            case 157:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo12 = new MultipleEditItemView.EditTextDataInfo();
                String string2 = this.context.getString(R.string.random_code);
                editTextDataInfo12.name = string2;
                editTextDataInfo12.tipInfo = string + string2;
                arrayList.add(editTextDataInfo12);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo13 = new MultipleEditItemView.EditTextDataInfo();
                String string3 = this.context.getString(R.string.verification_code_password);
                editTextDataInfo13.name = string3;
                editTextDataInfo13.tipInfo = string + string3;
                arrayList.add(editTextDataInfo13);
                return arrayList;
            case 168:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo14 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo14.name = this.context.getString(R.string.cargo_number);
                editTextDataInfo14.tipInfo = string + editTextDataInfo14.name;
                arrayList.add(editTextDataInfo14);
                return arrayList;
            case 169:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo15 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo15.name = this.context.getString(R.string.initial_cargo_number);
                editTextDataInfo15.tipInfo = string + editTextDataInfo15.name;
                arrayList.add(editTextDataInfo15);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo16 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo16.name = this.context.getString(R.string.stop_shipment_no);
                editTextDataInfo16.tipInfo = string + editTextDataInfo16.name;
                arrayList.add(editTextDataInfo16);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo17 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo17.name = this.context.getString(R.string.cycle_times);
                editTextDataInfo17.tipInfo = string + editTextDataInfo17.name;
                arrayList.add(editTextDataInfo17);
                return arrayList;
            case SettingType.RESET_LOGIN_PASSWORD /* 171 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo18 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo18.name = this.context.getString(R.string.old_password);
                editTextDataInfo18.tipInfo = string + editTextDataInfo18.name;
                arrayList.add(editTextDataInfo18);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo19 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo19.name = this.context.getString(R.string.new_password);
                editTextDataInfo19.tipInfo = string + editTextDataInfo19.name;
                arrayList.add(editTextDataInfo19);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo20 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo20.name = this.context.getString(R.string.confirm_the_password);
                editTextDataInfo20.tipInfo = string + this.context.getString(R.string.new_password);
                arrayList.add(editTextDataInfo20);
                return arrayList;
            case 177:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo21 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo21.name = this.context.getString(R.string.heating_time) + "(s)";
                editTextDataInfo21.tipInfo = string + editTextDataInfo21.name;
                arrayList.add(editTextDataInfo21);
                return arrayList;
            case 184:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo22 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo22.name = this.context.getString(R.string.banknote_receive_face_value);
                editTextDataInfo22.tipInfo = this.context.getString(R.string.input_200_receive_all);
                arrayList.add(editTextDataInfo22);
                return arrayList;
            case SettingType.BANKNOTE_CHANGE_OF_MONEY /* 186 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo23 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo23.name = this.context.getString(R.string.banknote_change_of_money_tip);
                editTextDataInfo23.tipInfo = string + editTextDataInfo23.name;
                arrayList.add(editTextDataInfo23);
                return arrayList;
            case SettingType.CLEAR_LOCAL_SALES_RECORDS /* 195 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo24 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo24.name = this.context.getString(R.string.password);
                editTextDataInfo24.tipInfo = string + this.context.getString(R.string.six_password);
                arrayList.add(editTextDataInfo24);
                return arrayList;
            case SettingType.MANUAL_POSITIONING_OF_BOXES /* 233 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo25 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo25.name = this.context.getString(R.string.cargo_number);
                editTextDataInfo25.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo25.name;
                arrayList.add(editTextDataInfo25);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo26 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo26.name = this.context.getString(R.string.number_positioning_pulses);
                editTextDataInfo26.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo26.name;
                arrayList.add(editTextDataInfo26);
                return arrayList;
            case 235:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo27 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo27.name = this.context.getString(R.string.cargo_number);
                editTextDataInfo27.tipInfo = this.context.getString(R.string.please_input) + this.context.getString(R.string.cargo_number) + this.context.getString(R.string.heat_time_tip);
                arrayList.add(editTextDataInfo27);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo28 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo28.name = this.context.getString(R.string.heating_time) + "(s)";
                editTextDataInfo28.tipInfo = this.context.getString(R.string.please_input) + this.context.getString(R.string.heating_time);
                arrayList.add(editTextDataInfo28);
                return arrayList;
            case 246:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo29 = new MultipleEditItemView.EditTextDataInfo();
                String string4 = this.context.getString(R.string.layer_number);
                editTextDataInfo29.name = string4 + "(0-9)";
                editTextDataInfo29.tipInfo = string + string4;
                arrayList.add(editTextDataInfo29);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo30 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo30.name = this.context.getString(R.string.initial_position_pulse);
                editTextDataInfo30.tipInfo = string + editTextDataInfo30.name;
                arrayList.add(editTextDataInfo30);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo31 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo31.name = this.context.getString(R.string.interval_distance_pulse);
                editTextDataInfo31.tipInfo = string + editTextDataInfo31.name;
                arrayList.add(editTextDataInfo31);
                return arrayList;
            case SettingType.FAULT_TEMPERATURE_PROBE /* 272 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo32 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo32.name = this.context.getString(R.string.falut_temperature_probe_work_time);
                arrayList.add(editTextDataInfo32);
                return arrayList;
            case SettingType.ELECTROMAGNETIC_LOCK_ON_TIME_WHOLE_MACHINE /* 276 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo33 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo33.name = getSettingName() + "(5-2000ms)";
                editTextDataInfo33.tipInfo = string + this.context.getString(R.string.electromagnetic_lock_on_time);
                arrayList.add(editTextDataInfo33);
                return arrayList;
            case SettingType.DOWNLOAD_SIGNAL_GOODS_PIC /* 317 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo34 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo34.name = this.context.getString(R.string.cargo_number);
                editTextDataInfo34.tipInfo = string + this.context.getString(R.string.cargo_number);
                arrayList.add(editTextDataInfo34);
                return arrayList;
            case SettingType.LIGHT_BOX_ROLLING_INTERVAL /* 320 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo35 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo35.name = this.context.getString(R.string.rolling_interval);
                editTextDataInfo35.tipInfo = this.context.getString(R.string.rolling_interval_tip);
                arrayList.add(editTextDataInfo35);
                return arrayList;
            case 331:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo36 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo36.name = getSettingName() + "(800-2200ms)";
                editTextDataInfo36.tipInfo = string + this.context.getString(R.string.linkage_synchronization_time);
                arrayList.add(editTextDataInfo36);
                return arrayList;
            case 340:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo37 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo37.name = this.context.getString(R.string.temperature) + "(℃)";
                editTextDataInfo37.tipInfo = this.context.getString(R.string.please_input) + this.context.getString(R.string.temperature);
                arrayList.add(editTextDataInfo37);
                return arrayList;
            case SettingType.DRUG_BOX_MENU_NAME /* 341 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo38 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo38.name = this.context.getString(R.string.left_name);
                arrayList.add(editTextDataInfo38);
                MultipleEditItemView.EditTextDataInfo editTextDataInfo39 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo39.name = this.context.getString(R.string.right_name);
                arrayList.add(editTextDataInfo39);
                return arrayList;
            case 350:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo40 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo40.name = this.context.getString(R.string.out_disinfectant_pulse_10);
                editTextDataInfo40.tipInfo = string + this.context.getString(R.string.pulse_count);
                arrayList.add(editTextDataInfo40);
                return arrayList;
            case SettingType.WATER_INLET_SOLENOID_VALVE_CONTROL /* 351 */:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo41 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo41.name = this.context.getString(R.string.in_warter_pulse_1000);
                editTextDataInfo41.tipInfo = string + this.context.getString(R.string.pulse_count);
                arrayList.add(editTextDataInfo41);
                return arrayList;
            default:
                MultipleEditItemView.EditTextDataInfo editTextDataInfo42 = new MultipleEditItemView.EditTextDataInfo();
                editTextDataInfo42.name = getSettingName();
                editTextDataInfo42.tipInfo = string + editTextDataInfo42.name;
                arrayList.add(editTextDataInfo42);
                return arrayList;
        }
    }

    private void setTitleVisibility(List<MultipleEditItemView.EditTextDataInfo> list) {
        if (this.settingType == 168 || this.settingType == 177 || this.settingType == 195 || this.settingType == 272 || this.settingType == 317 || this.settingType == 320 || this.settingType == 340 || this.settingType == 350 || this.settingType == 351) {
            this.multipleEditItemView.setTitleVisibility(0);
        } else {
            if (list.size() <= 1 || this.settingType == 102) {
                return;
            }
            this.multipleEditItemView.setTitleVisibility(0);
        }
    }

    private void showSaveDialog(int i, String str) {
        LoadingDialog loadingDialog = new LoadingDialog(this.context, this.context.getString(R.string.saveing));
        loadingDialog.show();
        ShjGoodsSetting shjGoodsSetting = new ShjGoodsSetting();
        shjGoodsSetting.count = 1;
        shjGoodsSetting.onShjGoodsSetResultListener = new OnShjGoodsSetResultListener() { // from class: com.shj.setting.generator.MultipleEditTextNote.37
            final /* synthetic */ int val$cmdType;
            final /* synthetic */ String val$content;
            final /* synthetic */ LoadingDialog val$loadingDialog;

            AnonymousClass37(LoadingDialog loadingDialog2, String str2, int i2) {
                loadingDialog = loadingDialog2;
                str = str2;
                i = i2;
            }

            @Override // com.shj.OnShjGoodsSetResultListener
            public void result(int i2, Object obj, boolean z) {
                loadingDialog.dismiss();
                ToastUitl.showSaveSuccessTip(MultipleEditTextNote.this.context);
                EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, str));
                int i3 = i;
                if (i3 == 18 || i3 == 21) {
                    EventBus.getDefault().post(new ShowShelfErrorTipEvent());
                }
            }
        };
        Shj.addGoodsSetCommand(i2, shjGoodsSetting);
    }

    /* renamed from: com.shj.setting.generator.MultipleEditTextNote$37 */
    /* loaded from: classes2.dex */
    public class AnonymousClass37 implements OnShjGoodsSetResultListener {
        final /* synthetic */ int val$cmdType;
        final /* synthetic */ String val$content;
        final /* synthetic */ LoadingDialog val$loadingDialog;

        AnonymousClass37(LoadingDialog loadingDialog2, String str2, int i2) {
            loadingDialog = loadingDialog2;
            str = str2;
            i = i2;
        }

        @Override // com.shj.OnShjGoodsSetResultListener
        public void result(int i2, Object obj, boolean z) {
            loadingDialog.dismiss();
            ToastUitl.showSaveSuccessTip(MultipleEditTextNote.this.context);
            EventBus.getDefault().post(new SettingTypeEvent(MultipleEditTextNote.this.settingType, str));
            int i3 = i;
            if (i3 == 18 || i3 == 21) {
                EventBus.getDefault().post(new ShowShelfErrorTipEvent());
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        List<MultipleEditItemView.EditTextDataInfo> editTextDataInfoList = getEditTextDataInfoList();
        MultipleEditItemView multipleEditItemView = new MultipleEditItemView(this.context, editTextDataInfoList);
        this.multipleEditItemView = multipleEditItemView;
        multipleEditItemView.setTitle(getSettingName());
        this.multipleEditItemView.setEventListener(this.eventListener);
        setTitleVisibility(editTextDataInfoList);
        setQueryButtonVIsibility();
        setEditText();
        setEditeTypePassword();
        setEditeTypeInterger();
        setSaveSettingText();
        if (this.settingType == 186 || this.settingType == 184 || this.settingType == 120 || this.settingType == 121 || this.settingType == 320 || this.settingType == 340 || this.settingType == 350 || this.settingType == 351) {
            this.multipleEditItemView.setSaveButtonVisibility(0);
        }
        return this.multipleEditItemView;
    }

    private void setQueryButtonVIsibility() {
        int i = this.settingType;
        if (i != 117 && i != 148 && i != 177 && i != 233 && i != 235 && i != 272) {
            if (i != 276) {
                if (i == 317) {
                    this.multipleEditItemView.setQueryButtonText(this.context.getResources().getString(R.string.download_goods_detail_pic));
                    this.multipleEditItemView.setQueryButtonVIsibility(0);
                    return;
                } else if (i != 320) {
                    if (i != 331) {
                        if (i != 120 && i != 121 && i != 350 && i != 351) {
                            return;
                        }
                    }
                }
            }
            this.multipleEditItemView.setClearButtonVisibility(0);
            this.multipleEditItemView.setClearSettingText(this.context.getResources().getString(R.string.query));
            return;
        }
        this.multipleEditItemView.setQueryButtonVIsibility(0);
    }

    private void setSaveSettingText() {
        int i = this.settingType;
        if (i == 168 || i == 169) {
            this.multipleEditItemView.setSaveSettingText(this.context.getResources().getString(R.string.exec_test));
            return;
        }
        if (i == 171) {
            this.multipleEditItemView.setSaveSettingText(this.context.getResources().getString(R.string.reset_login_password_button));
        } else if (i == 195) {
            this.multipleEditItemView.setSaveSettingText(this.context.getResources().getString(R.string.clear));
        } else {
            if (i != 317) {
                return;
            }
            this.multipleEditItemView.setSaveSettingText(this.context.getResources().getString(R.string.download_goods_pic));
        }
    }

    private void setEditeTypePassword() {
        int i = this.settingType;
        if (i == 171) {
            this.multipleEditItemView.setEditeTypePassword(0);
            this.multipleEditItemView.setEditeTypePassword(1);
        } else {
            if (i != 195) {
                return;
            }
            this.multipleEditItemView.setEditeTypePassword(0);
        }
    }

    private void setEditeTypeInterger() {
        switch (this.settingType) {
            case 102:
                this.multipleEditItemView.setEditeTypeInterger(0);
                this.multipleEditItemView.setMaxInputCount(0, 10);
                this.multipleEditItemView.setEditeTypeInterger(1);
                return;
            case 110:
            case 117:
            case 120:
            case SettingType.COINS_ONE_YUAN /* 121 */:
            case SettingType.INVENTORY_WHOLE_MACHINE /* 133 */:
            case SettingType.CARGO_CAPACITY_WHOLE_MACHINE /* 138 */:
            case SettingType.CARGO_CODE_WHOLE_MACHINE /* 141 */:
            case 168:
            case 177:
            case 184:
            case SettingType.BANKNOTE_CHANGE_OF_MONEY /* 186 */:
            case SettingType.FAULT_TEMPERATURE_PROBE /* 272 */:
            case SettingType.ELECTROMAGNETIC_LOCK_ON_TIME_WHOLE_MACHINE /* 276 */:
            case SettingType.LIGHT_BOX_ROLLING_INTERVAL /* 320 */:
            case 331:
            case 350:
            case SettingType.WATER_INLET_SOLENOID_VALVE_CONTROL /* 351 */:
                this.multipleEditItemView.setEditeTypeInterger(0);
                return;
            case 125:
                this.multipleEditItemView.setEditeTypeDecimal(0);
                return;
            case 148:
            case SettingType.MANUAL_POSITIONING_OF_BOXES /* 233 */:
            case 235:
                this.multipleEditItemView.setEditeTypeInterger(0);
                this.multipleEditItemView.setEditeTypeInterger(1);
                return;
            case 169:
            case SettingType.RESET_LOGIN_PASSWORD /* 171 */:
            case 246:
                this.multipleEditItemView.setEditeTypeInterger(0);
                this.multipleEditItemView.setEditeTypeInterger(1);
                this.multipleEditItemView.setEditeTypeInterger(2);
                return;
            case 340:
                this.multipleEditItemView.setEditeTypeSignedInterger(0);
                return;
            default:
                return;
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        MultipleEditItemView multipleEditItemView = this.multipleEditItemView;
        if (multipleEditItemView != null) {
            return multipleEditItemView;
        }
        return null;
    }
}
