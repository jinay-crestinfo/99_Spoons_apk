package com.shj.setting.generator;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.support.media.ExifInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.MutilTextTipDialog;
import com.shj.setting.Dialog.TipDialog;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.SetUtils;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.Utils.UsbFileUtil;
import com.shj.setting.bean.BoxLaunchTestData;
import com.shj.setting.event.SettingTypeEvent;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.MultipleEditItemView;
import com.shj.setting.widget.RadioGroupItemView;
import com.shj.setting.widget.SpinnerItemView;
import com.shj.setting.widget.SpinnerMulEditTextView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* loaded from: classes2.dex */
public class SpinnerMulEditTextNote extends SettingNote {
    private int cabinetNumber;
    private int cargoLinkageCount;
    private ArrayList<String> cargoLinkageList;
    private Handler handler;
    private boolean isQueryCargoLinkageing;
    private LoadingDialog loadingDialog;
    private int loopTimes;
    private MutilTextTipDialog mutilTextTipDialog;
    private SpinnerMulEditTextView spinnerMulEditTextView;

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
    }

    static /* synthetic */ int access$310(SpinnerMulEditTextNote spinnerMulEditTextNote) {
        int i = spinnerMulEditTextNote.loopTimes;
        spinnerMulEditTextNote.loopTimes = i - 1;
        return i;
    }

    static /* synthetic */ int access$708(SpinnerMulEditTextNote spinnerMulEditTextNote) {
        int i = spinnerMulEditTextNote.cargoLinkageCount;
        spinnerMulEditTextNote.cargoLinkageCount = i + 1;
        return i;
    }

    public SpinnerMulEditTextNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
        this.cargoLinkageList = new ArrayList<>();
        this.isQueryCargoLinkageing = false;
        this.handler = new Handler() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.21
            AnonymousClass21() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 0) {
                    SpinnerMulEditTextNote.this.liftLoopTest(message.arg1, message.arg2);
                }
            }
        };
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        int i = this.settingType;
        if (i == 119) {
            LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.saveing);
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            commandV2_Up_SetCommand.setCoinSystem(true, this.spinnerMulEditTextView.getSelectIndex() + 1);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.4
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass4() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                    ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z2, SpinnerMulEditTextNote.this.getSettingName());
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        int i2 = 0;
        if (i == 254) {
            int selectIndex = this.spinnerMulEditTextView.getSelectIndex();
            String editeText = this.spinnerMulEditTextView.getEditeText(0);
            String editeText2 = this.spinnerMulEditTextView.getEditeText(1);
            String editeText3 = this.spinnerMulEditTextView.getEditeText(2);
            if (TextUtils.isEmpty(editeText) || TextUtils.isEmpty(editeText2) || TextUtils.isEmpty(editeText3)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            int intValue = Integer.valueOf(editeText).intValue();
            int intValue2 = Integer.valueOf(editeText2).intValue();
            int intValue3 = Integer.valueOf(editeText3).intValue();
            boolean z2 = selectIndex == 0;
            BoxLaunchTestData boxLaunchTestData = new BoxLaunchTestData();
            boxLaunchTestData.start = intValue;
            boxLaunchTestData.end = intValue2;
            boxLaunchTestData.times = intValue3;
            boxLaunchTestData.isOutGoods = z2;
            EventBus.getDefault().post(new SettingTypeEvent(this.settingType, boxLaunchTestData));
            return;
        }
        if (i == 271) {
            String editeText4 = this.spinnerMulEditTextView.getEditeText(1);
            String editeText5 = this.spinnerMulEditTextView.getEditeText(2);
            if (TextUtils.isEmpty(editeText4) || TextUtils.isEmpty(editeText5)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            int intValue4 = Integer.valueOf(editeText4).intValue();
            int intValue5 = Integer.valueOf(editeText5).intValue();
            if (intValue4 < 30 || intValue4 > 90 || intValue5 < 30 || intValue5 > 90 || intValue4 > intValue5) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            int selectIndex2 = this.spinnerMulEditTextView.getSelectIndex();
            LoadingDialog loadingDialog2 = new LoadingDialog(this.context, R.string.saveing);
            this.loadingDialog = loadingDialog2;
            loadingDialog2.show();
            commandV2_Up_SetCommand.setHumidifier(true, intValue4, intValue5, selectIndex2);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.15
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass15() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z3) {
                    ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 308) {
            int selectIndex3 = this.spinnerMulEditTextView.getSelectIndex();
            String editeText6 = this.spinnerMulEditTextView.getEditeText(0);
            if (TextUtils.isEmpty(editeText6)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            int intValue6 = Integer.valueOf(editeText6).intValue();
            CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand2.setOpenGridMachineGrid(selectIndex3, intValue6);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.16
                final /* synthetic */ int val$layerValue;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass16(int intValue62) {
                    intValue6 = intValue62;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z3) {
                    List<Integer> list;
                    if (z3) {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.open_success);
                        return;
                    }
                    HashMap<Integer, List<Integer>> hashMap = SettingActivity.getBasicMachineInfo().shelvesLayerMap;
                    if (hashMap == null || (list = hashMap.get(Integer.valueOf(intValue6))) == null) {
                        return;
                    }
                    Iterator<Integer> it = list.iterator();
                    while (it.hasNext()) {
                        int intValue7 = it.next().intValue();
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand3.TestShelf(false, 1, intValue7);
                        Shj.getInstance(SpinnerMulEditTextNote.this.context);
                        Shj.postSetCommand(commandV2_Up_SetCommand3, null);
                    }
                }
            });
            return;
        }
        if (i == 352) {
            int selectIndex4 = this.spinnerMulEditTextView.getSelectIndex();
            if (selectIndex4 == 0 || selectIndex4 == 1) {
                String editeText7 = this.spinnerMulEditTextView.getEditeText(0);
                if (TextUtils.isEmpty(editeText7)) {
                    ToastUitl.showNotInputTip(this.context);
                    return;
                }
                i2 = Integer.valueOf(editeText7).intValue();
            }
            LoadingDialog loadingDialog3 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog3;
            loadingDialog3.show();
            CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand3.setMixingTankControl(true, selectIndex4 + 1, i2);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand3, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.19
                AnonymousClass19() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                        if (intFromBytes == 0) {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_success);
                        } else if (intFromBytes == 1) {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.warter_cup_take);
                        } else if (intFromBytes == 2) {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.warter_not_enough);
                        } else if (intFromBytes == 3) {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.not_warter);
                        } else if (intFromBytes == 4) {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.not_disinfectant);
                        } else if (intFromBytes == 5) {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.liquid_filling_timeout);
                        } else {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_fail);
                        }
                    } else {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_fail);
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z3) {
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 231) {
            int selectIndex5 = this.spinnerMulEditTextView.getSelectIndex();
            String editeText8 = this.spinnerMulEditTextView.getEditeText(0);
            String editeText9 = this.spinnerMulEditTextView.getEditeText(1);
            if (TextUtils.isEmpty(editeText8) || TextUtils.isEmpty(editeText9)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            int intValue7 = Integer.valueOf(editeText8).intValue();
            int intValue8 = Integer.valueOf(editeText9).intValue();
            if (intValue7 < 0 || intValue7 > 9 || intValue8 < 0 || intValue8 > 8000) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            LoadingDialog loadingDialog4 = new LoadingDialog(this.context, R.string.saveing);
            this.loadingDialog = loadingDialog4;
            loadingDialog4.show();
            commandV2_Up_SetCommand.setSetLiftPos(true, selectIndex5, intValue7, intValue8);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.1
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z3) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                            int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                            String[] stringArray = SpinnerMulEditTextNote.this.context.getResources().getStringArray(R.array.lift_layer_location_status);
                            if (intFromBytes < stringArray.length) {
                                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, stringArray[intFromBytes]);
                            }
                        } else {
                            ToastUitl.showSetCompeleteTip(SpinnerMulEditTextNote.this.context, false, SpinnerMulEditTextNote.this.getSettingName());
                        }
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 232) {
            int selectIndex6 = this.spinnerMulEditTextView.getSelectIndex();
            String editeText10 = this.spinnerMulEditTextView.getEditeText(0);
            String editeText11 = this.spinnerMulEditTextView.getEditeText(1);
            if (TextUtils.isEmpty(editeText10) || TextUtils.isEmpty(editeText11)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            LoadingDialog loadingDialog5 = new LoadingDialog(this.context, R.string.saveing);
            this.loadingDialog = loadingDialog5;
            loadingDialog5.show();
            commandV2_Up_SetCommand.setWBLPos(true, Integer.valueOf(editeText10).intValue(), Integer.valueOf(editeText11).intValue(), selectIndex6 + 1, this.spinnerMulEditTextView.getNewAddSelectIndex());
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.2
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass2() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z3) {
                    ToastUitl.showSetCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 256) {
            String editeText12 = this.spinnerMulEditTextView.getEditeText(0);
            if (TextUtils.isEmpty(editeText12)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            int intValue9 = Integer.valueOf(editeText12).intValue();
            int selectIndex7 = this.spinnerMulEditTextView.getSelectIndex();
            LoadingDialog loadingDialog6 = new LoadingDialog(this.context, R.string.saveing);
            this.loadingDialog = loadingDialog6;
            loadingDialog6.show();
            commandV2_Up_SetCommand.setBoxSpeed(true, intValue9, selectIndex7);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.13
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass13() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z3) {
                    ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 257) {
            String editeText13 = this.spinnerMulEditTextView.getEditeText(0);
            if (TextUtils.isEmpty(editeText13)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            int intValue10 = Integer.valueOf(editeText13).intValue();
            int selectIndex8 = this.spinnerMulEditTextView.getSelectIndex();
            LoadingDialog loadingDialog7 = new LoadingDialog(this.context, R.string.saveing);
            this.loadingDialog = loadingDialog7;
            loadingDialog7.show();
            commandV2_Up_SetCommand.setBoxLmd(true, intValue10, selectIndex8);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.14
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass14() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z3) {
                    ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 326) {
            String editeText14 = this.spinnerMulEditTextView.getEditeText(0);
            if (TextUtils.isEmpty(editeText14)) {
                return;
            }
            AppSetting.saveSurveillanceCameraPidVid(this.context, editeText14, this.mUserSettingDao);
            SDFileUtils.writeToSDFromInput("xyShj", "video_camara_pid_vid.txt", editeText14, false);
            ToastUitl.showShort(this.context, R.string.save_success);
            return;
        }
        if (i == 327) {
            String editeText15 = this.spinnerMulEditTextView.getEditeText(0);
            if (TextUtils.isEmpty(editeText15)) {
                return;
            }
            AppSetting.saveHighTimeMeterPidVid(this.context, editeText15, this.mUserSettingDao);
            ToastUitl.showShort(this.context, R.string.save_success);
            return;
        }
        if (i == 338) {
            int selectIndex9 = this.spinnerMulEditTextView.getSelectIndex();
            String editeText16 = this.spinnerMulEditTextView.getEditeText(0);
            if (TextUtils.isEmpty(editeText16)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            int intValue11 = Integer.valueOf(editeText16).intValue();
            if (intValue11 < 4 || intValue11 > 15) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            CommandV2_Up_SetCommand commandV2_Up_SetCommand4 = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand4.setFindPeoperSensitivity(true, selectIndex9, intValue11);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand4, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.17
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass17() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z3) {
                    if (z3) {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_success);
                    } else {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_fail);
                    }
                }
            });
            return;
        }
        if (i != 339) {
            switch (i) {
                case 236:
                    int selectIndex10 = this.spinnerMulEditTextView.getSelectIndex();
                    ArrayList arrayList = new ArrayList();
                    for (int i3 = 0; i3 < 7; i3++) {
                        String editeText17 = this.spinnerMulEditTextView.getEditeText(i3);
                        if (TextUtils.isEmpty(editeText17)) {
                            ToastUitl.showNotInputTip(this.context);
                            return;
                        }
                        arrayList.add(editeText17);
                    }
                    int radioGroupCheck = this.spinnerMulEditTextView.getRadioGroupCheck();
                    if (radioGroupCheck == -1) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    ArrayList arrayList2 = new ArrayList();
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        arrayList2.add(Integer.valueOf((String) it.next()));
                    }
                    if (((Integer) arrayList2.get(0)).intValue() < -28 || ((Integer) arrayList2.get(0)).intValue() > 60 || ((Integer) arrayList2.get(1)).intValue() < 0 || ((Integer) arrayList2.get(1)).intValue() > 60 || ((Integer) arrayList2.get(2)).intValue() < 2 || ((Integer) arrayList2.get(2)).intValue() > 8 || ((Integer) arrayList2.get(3)).intValue() < 0 || ((Integer) arrayList2.get(3)).intValue() > 8 || ((Integer) arrayList2.get(4)).intValue() < -10 || ((Integer) arrayList2.get(4)).intValue() > 10 || ((Integer) arrayList2.get(5)).intValue() < 0 || ((Integer) arrayList2.get(5)).intValue() > 24 || ((Integer) arrayList2.get(6)).intValue() < 1 || ((Integer) arrayList2.get(6)).intValue() > 40) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    LoadingDialog loadingDialog8 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog8;
                    loadingDialog8.show();
                    commandV2_Up_SetCommand.setWKYSets(true, selectIndex10, ((Integer) arrayList2.get(0)).intValue(), ((Integer) arrayList2.get(1)).intValue(), ((Integer) arrayList2.get(2)).intValue(), ((Integer) arrayList2.get(3)).intValue(), ((Integer) arrayList2.get(4)).intValue(), ((Integer) arrayList2.get(5)).intValue(), ((Integer) arrayList2.get(6)).intValue(), radioGroupCheck == 0 ? 1 : 0);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.3
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass3() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z3) {
                            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 237:
                    String editeText18 = this.spinnerMulEditTextView.getEditeText(0);
                    int selectIndex11 = this.spinnerMulEditTextView.getSelectIndex();
                    if (TextUtils.isEmpty(editeText18)) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    LoadingDialog loadingDialog9 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog9;
                    loadingDialog9.show();
                    commandV2_Up_SetCommand.setShelfModel(true, Integer.valueOf(editeText18).intValue(), selectIndex11 + 1);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.5
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass5() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z3) {
                            ToastUitl.showSetCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 238:
                    int i4 = this.cabinetNumber;
                    if (i4 == -1) {
                        i4 = this.spinnerMulEditTextView.getSelectIndex();
                    }
                    String editeText19 = this.spinnerMulEditTextView.getEditeText(0);
                    if (TextUtils.isEmpty(editeText19)) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    int intValue12 = Integer.valueOf(editeText19).intValue();
                    if (intValue12 < 20 || intValue12 > 250) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    LoadingDialog loadingDialog10 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog10;
                    loadingDialog10.show();
                    commandV2_Up_SetCommand.setEnginStopYZ(true, i4, intValue12);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.6
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass6() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z3) {
                            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 239:
                    int selectIndex12 = this.spinnerMulEditTextView.getSelectIndex();
                    String editeText20 = this.spinnerMulEditTextView.getEditeText(0);
                    if (TextUtils.isEmpty(editeText20)) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    int intValue13 = Integer.valueOf(editeText20).intValue();
                    if (intValue13 < 700 || intValue13 > 900) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    LoadingDialog loadingDialog11 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog11;
                    loadingDialog11.show();
                    commandV2_Up_SetCommand.setEnginDLYZ(true, selectIndex12, intValue13);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.7
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass7() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z3) {
                            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 240:
                    int i5 = this.cabinetNumber;
                    if (i5 == -1) {
                        i5 = this.spinnerMulEditTextView.getSelectIndex();
                    }
                    String editeText21 = this.spinnerMulEditTextView.getEditeText(0);
                    if (TextUtils.isEmpty(editeText21)) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    int intValue14 = Integer.valueOf(editeText21).intValue();
                    if (intValue14 < 1200 || intValue14 > 2200) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    LoadingDialog loadingDialog12 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog12;
                    loadingDialog12.show();
                    commandV2_Up_SetCommand.setMergeShelSynRunTime(true, i5, intValue14);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.8
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass8() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z3) {
                            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 241:
                    String editeText22 = this.spinnerMulEditTextView.getEditeText(0);
                    if (TextUtils.isEmpty(editeText22)) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    LoadingDialog loadingDialog13 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog13;
                    loadingDialog13.show();
                    int intValue15 = Integer.valueOf(editeText22).intValue();
                    int selectIndex13 = this.spinnerMulEditTextView.getSelectIndex();
                    commandV2_Up_SetCommand.setMergeShelf(true, intValue15, selectIndex13 + 1);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.9
                        final /* synthetic */ int val$cargoNumber;
                        final /* synthetic */ int val$linkageState;

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass9(int selectIndex132, int intValue152) {
                            selectIndex13 = selectIndex132;
                            intValue15 = intValue152;
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z3) {
                            ToastUitl.showSetCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                            if (z3) {
                                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(intValue15 + selectIndex13 + 1));
                                SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                            }
                        }
                    });
                    return;
                case 242:
                    int selectIndex14 = this.spinnerMulEditTextView.getSelectIndex();
                    String editeText23 = this.spinnerMulEditTextView.getEditeText(0);
                    if (TextUtils.isEmpty(editeText23)) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    int intValue16 = Integer.valueOf(editeText23).intValue();
                    if (intValue16 < 15 || intValue16 > 100) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    LoadingDialog loadingDialog14 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog14;
                    loadingDialog14.show();
                    commandV2_Up_SetCommand.setSetLiftSpeed(true, selectIndex14, intValue16);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.10
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass10() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z3) {
                            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 243:
                    String editeText24 = this.spinnerMulEditTextView.getEditeText(0);
                    if (TextUtils.isEmpty(editeText24)) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    int intValue17 = Integer.valueOf(editeText24).intValue();
                    if (intValue17 < 0 || intValue17 > 9) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    String editeText25 = this.spinnerMulEditTextView.getEditeText(1);
                    if (TextUtils.isEmpty(editeText25)) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    int intValue18 = Integer.valueOf(editeText25).intValue();
                    this.loopTimes = intValue18;
                    if (intValue18 < 0 || intValue18 > 10000) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    MutilTextTipDialog mutilTextTipDialog = new MutilTextTipDialog(this.context);
                    this.mutilTextTipDialog = mutilTextTipDialog;
                    mutilTextTipDialog.show();
                    this.mutilTextTipDialog.addTextShow(this.context.getString(R.string.test_start));
                    liftLoopTest(intValue17, 1);
                    return;
                case 244:
                    int selectIndex15 = this.spinnerMulEditTextView.getSelectIndex();
                    String editeText26 = this.spinnerMulEditTextView.getEditeText(0);
                    if (TextUtils.isEmpty(editeText26)) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    int intValue19 = Integer.valueOf(editeText26).intValue();
                    if (intValue19 < 0 || intValue19 > 50) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    LoadingDialog loadingDialog15 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog15;
                    loadingDialog15.show();
                    commandV2_Up_SetCommand.setSetLiftSensitivity(true, selectIndex15, intValue19);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.11
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass11() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z3) {
                            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 245:
                    int selectIndex16 = this.spinnerMulEditTextView.getSelectIndex();
                    String editeText27 = this.spinnerMulEditTextView.getEditeText(0);
                    if (TextUtils.isEmpty(editeText27)) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    int intValue20 = Integer.valueOf(editeText27).intValue();
                    if (intValue20 < 20 || intValue20 > 8000) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    LoadingDialog loadingDialog16 = new LoadingDialog(this.context, R.string.saveing);
                    this.loadingDialog = loadingDialog16;
                    loadingDialog16.show();
                    commandV2_Up_SetCommand.setGuardDoorCloseTime(true, selectIndex16, intValue20);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.12
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        AnonymousClass12() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z3) {
                            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                default:
                    return;
            }
        }
        int selectIndex17 = this.spinnerMulEditTextView.getSelectIndex();
        String editeText28 = this.spinnerMulEditTextView.getEditeText(0);
        if (TextUtils.isEmpty(editeText28)) {
            ToastUitl.showNotInputTip(this.context);
            return;
        }
        int intValue21 = Integer.valueOf(editeText28).intValue();
        if (intValue21 < 0 || intValue21 > 255) {
            ToastUitl.showNotInputTip(this.context);
            return;
        }
        CommandV2_Up_SetCommand commandV2_Up_SetCommand5 = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand5.setFindPeoperDistance(true, selectIndex17, intValue21);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand5, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.18
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass18() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z3) {
                if (z3) {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_success);
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_fail);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                    String[] stringArray = SpinnerMulEditTextNote.this.context.getResources().getStringArray(R.array.lift_layer_location_status);
                    if (intFromBytes < stringArray.length) {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, stringArray[intFromBytes]);
                    }
                } else {
                    ToastUitl.showSetCompeleteTip(SpinnerMulEditTextNote.this.context, false, SpinnerMulEditTextNote.this.getSettingName());
                }
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass2() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showSetCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass3() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass4() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z2, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass5() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showSetCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass6() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass7() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass8() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements OnCommandAnswerListener {
        final /* synthetic */ int val$cargoNumber;
        final /* synthetic */ int val$linkageState;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass9(int selectIndex132, int intValue152) {
            selectIndex13 = selectIndex132;
            intValue15 = intValue152;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showSetCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            if (z3) {
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(intValue15 + selectIndex13 + 1));
                SpinnerMulEditTextNote.this.loadingDialog.dismiss();
            }
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass10() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$11 */
    /* loaded from: classes2.dex */
    public class AnonymousClass11 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass11() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$12 */
    /* loaded from: classes2.dex */
    public class AnonymousClass12 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass12() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$13 */
    /* loaded from: classes2.dex */
    public class AnonymousClass13 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass13() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$14 */
    /* loaded from: classes2.dex */
    public class AnonymousClass14 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass14() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$15 */
    /* loaded from: classes2.dex */
    public class AnonymousClass15 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass15() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z3, SpinnerMulEditTextNote.this.getSettingName());
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$16 */
    /* loaded from: classes2.dex */
    public class AnonymousClass16 implements OnCommandAnswerListener {
        final /* synthetic */ int val$layerValue;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass16(int intValue62) {
            intValue6 = intValue62;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            List<Integer> list;
            if (z3) {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.open_success);
                return;
            }
            HashMap<Integer, List<Integer>> hashMap = SettingActivity.getBasicMachineInfo().shelvesLayerMap;
            if (hashMap == null || (list = hashMap.get(Integer.valueOf(intValue6))) == null) {
                return;
            }
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                int intValue7 = it.next().intValue();
                CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand3.TestShelf(false, 1, intValue7);
                Shj.getInstance(SpinnerMulEditTextNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand3, null);
            }
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$17 */
    /* loaded from: classes2.dex */
    public class AnonymousClass17 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass17() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            if (z3) {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_success);
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_fail);
            }
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$18 */
    /* loaded from: classes2.dex */
    public class AnonymousClass18 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass18() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            if (z3) {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_success);
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_fail);
            }
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$19 */
    /* loaded from: classes2.dex */
    public class AnonymousClass19 implements OnCommandAnswerListener {
        AnonymousClass19() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                if (intFromBytes == 0) {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_success);
                } else if (intFromBytes == 1) {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.warter_cup_take);
                } else if (intFromBytes == 2) {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.warter_not_enough);
                } else if (intFromBytes == 3) {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.not_warter);
                } else if (intFromBytes == 4) {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.not_disinfectant);
                } else if (intFromBytes == 5) {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.liquid_filling_timeout);
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_fail);
                }
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.setting_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    public void liftLoopTest(int i, int i2) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.TestLift(this.spinnerMulEditTextView.getSelectIndex(), i);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.20
            final /* synthetic */ int val$layer;
            final /* synthetic */ int val$times;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass20(int i22, int i3) {
                i2 = i22;
                i = i3;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr == null || bArr.length <= 0) {
                    return;
                }
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                    String[] stringArray = SpinnerMulEditTextNote.this.context.getResources().getStringArray(R.array.lift_test_result);
                    if (intFromBytes >= 0 && intFromBytes < stringArray.length) {
                        String format = String.format(SpinnerMulEditTextNote.this.context.getString(R.string.test_count), "" + i2);
                        SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(format + stringArray[intFromBytes]);
                        SpinnerMulEditTextNote.this.mutilTextTipDialog.addStatisticalInfo(intFromBytes, stringArray[intFromBytes]);
                    }
                    if (SpinnerMulEditTextNote.this.loopTimes <= 1) {
                        SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(SpinnerMulEditTextNote.this.context.getString(R.string.test_complete));
                        return;
                    }
                    SpinnerMulEditTextNote.access$310(SpinnerMulEditTextNote.this);
                    Message message = new Message();
                    message.what = 0;
                    message.arg1 = i;
                    message.arg2 = i2 + 1;
                    SpinnerMulEditTextNote.this.handler.sendMessageDelayed(message, 100L);
                    return;
                }
                String format2 = String.format(SpinnerMulEditTextNote.this.context.getString(R.string.test_count), "" + i2);
                SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(format2 + SpinnerMulEditTextNote.this.context.getString(R.string.communication_error));
            }
        });
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$20 */
    /* loaded from: classes2.dex */
    public class AnonymousClass20 implements OnCommandAnswerListener {
        final /* synthetic */ int val$layer;
        final /* synthetic */ int val$times;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass20(int i22, int i3) {
            i2 = i22;
            i = i3;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr == null || bArr.length <= 0) {
                return;
            }
            if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                String[] stringArray = SpinnerMulEditTextNote.this.context.getResources().getStringArray(R.array.lift_test_result);
                if (intFromBytes >= 0 && intFromBytes < stringArray.length) {
                    String format = String.format(SpinnerMulEditTextNote.this.context.getString(R.string.test_count), "" + i2);
                    SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(format + stringArray[intFromBytes]);
                    SpinnerMulEditTextNote.this.mutilTextTipDialog.addStatisticalInfo(intFromBytes, stringArray[intFromBytes]);
                }
                if (SpinnerMulEditTextNote.this.loopTimes <= 1) {
                    SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(SpinnerMulEditTextNote.this.context.getString(R.string.test_complete));
                    return;
                }
                SpinnerMulEditTextNote.access$310(SpinnerMulEditTextNote.this);
                Message message = new Message();
                message.what = 0;
                message.arg1 = i;
                message.arg2 = i2 + 1;
                SpinnerMulEditTextNote.this.handler.sendMessageDelayed(message, 100L);
                return;
            }
            String format2 = String.format(SpinnerMulEditTextNote.this.context.getString(R.string.test_count), "" + i2);
            SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(format2 + SpinnerMulEditTextNote.this.context.getString(R.string.communication_error));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$21 */
    /* loaded from: classes2.dex */
    public class AnonymousClass21 extends Handler {
        AnonymousClass21() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 0) {
                SpinnerMulEditTextNote.this.liftLoopTest(message.arg1, message.arg2);
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        if (this.settingType == 119 || this.settingType == 326 || this.settingType == 327) {
            querySettingData();
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void clearData() {
        if (this.settingType != 241) {
            return;
        }
        clearCargoLinkage();
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$22 */
    /* loaded from: classes2.dex */
    public class AnonymousClass22 implements TipDialog.TipDialogListener {
        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_01() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void timeEnd() {
        }

        AnonymousClass22() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
        public void buttonClick_02() {
            SpinnerMulEditTextNote.this.loadingDialog = new LoadingDialog(SpinnerMulEditTextNote.this.context, R.string.on_clearing);
            SpinnerMulEditTextNote.this.loadingDialog.show();
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.ClearMergeShelf();
            Shj.getInstance(SpinnerMulEditTextNote.this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.22.1
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                    ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z, SpinnerMulEditTextNote.this.context.getString(R.string.clear_cargo_linkage));
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
        }

        /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$22$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z, SpinnerMulEditTextNote.this.context.getString(R.string.clear_cargo_linkage));
                SpinnerMulEditTextNote.this.loadingDialog.dismiss();
            }
        }
    }

    private void clearCargoLinkage() {
        TipDialog tipDialog = new TipDialog(this.context, 0, R.string.clear_cargo_linkage_tip, R.string.button_cancel, R.string.button_ok);
        tipDialog.setTipDialogListener(new TipDialog.TipDialogListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.22
            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_01() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void timeEnd() {
            }

            AnonymousClass22() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListener
            public void buttonClick_02() {
                SpinnerMulEditTextNote.this.loadingDialog = new LoadingDialog(SpinnerMulEditTextNote.this.context, R.string.on_clearing);
                SpinnerMulEditTextNote.this.loadingDialog.show();
                CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand.ClearMergeShelf();
                Shj.getInstance(SpinnerMulEditTextNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.22.1
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass1() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z) {
                        ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z, SpinnerMulEditTextNote.this.context.getString(R.string.clear_cargo_linkage));
                        SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                    }
                });
            }

            /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$22$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                    ToastUitl.showCompeleteTip(SpinnerMulEditTextNote.this.context, z, SpinnerMulEditTextNote.this.context.getString(R.string.clear_cargo_linkage));
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            }
        });
        tipDialog.show();
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        int i = this.settingType;
        if (i == 119) {
            LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            commandV2_Up_SetCommand.setCoinSystem(false, this.spinnerMulEditTextView.getSelectIndex() + 1);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.26
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass26() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setSelectSpinnerIndex(ObjectHelper.intFromBytes(bArr, 0, 1) - 1);
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 271) {
            int selectIndex = this.spinnerMulEditTextView.getSelectIndex();
            LoadingDialog loadingDialog2 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog2;
            loadingDialog2.show();
            commandV2_Up_SetCommand.setHumidifier(false, 0, 0, selectIndex);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.38
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass38() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                            int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                            int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 1);
                            int intFromBytes3 = ObjectHelper.intFromBytes(bArr, 3, 1);
                            SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(intFromBytes));
                            SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(1, String.valueOf(intFromBytes2));
                            SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(2, String.valueOf(intFromBytes3));
                        } else {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                        }
                    } else {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 352) {
            LoadingDialog loadingDialog3 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog3;
            loadingDialog3.show();
            int selectIndex2 = this.spinnerMulEditTextView.getSelectIndex();
            CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand2.setMixingTankControl(false, selectIndex2 + 1, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.41
                AnonymousClass41() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length >= 2) {
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 2)));
                    } else {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 231) {
            int selectIndex3 = this.spinnerMulEditTextView.getSelectIndex();
            String editeText = this.spinnerMulEditTextView.getEditeText(0);
            if (TextUtils.isEmpty(editeText)) {
                ToastUitl.showNotInputTip(this.context);
                return;
            }
            int intValue = Integer.valueOf(editeText).intValue();
            if (intValue < 0 || intValue > 9) {
                ToastUitl.showErrorInputTip(this.context, ExifInterface.GPS_MEASUREMENT_2D);
                return;
            }
            LoadingDialog loadingDialog4 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog4;
            loadingDialog4.show();
            commandV2_Up_SetCommand.setSetLiftPos(false, selectIndex3, intValue, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.23
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass23() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                            SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(1, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                        } else {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                        }
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 232) {
            LoadingDialog loadingDialog5 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog5;
            loadingDialog5.show();
            commandV2_Up_SetCommand.setWBLPos(false, 0, 0, this.spinnerMulEditTextView.getSelectIndex() + 1, this.spinnerMulEditTextView.getNewAddSelectIndex());
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.24
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass24() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length >= 4) {
                        int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 2);
                        int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 2);
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(intFromBytes));
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(1, String.valueOf(intFromBytes2));
                    } else {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 244) {
            LoadingDialog loadingDialog6 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog6;
            loadingDialog6.show();
            commandV2_Up_SetCommand.setSetLiftSensitivity(false, this.spinnerMulEditTextView.getSelectIndex(), 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.34
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass34() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                            SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                        } else {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                        }
                    } else {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 245) {
            LoadingDialog loadingDialog7 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog7;
            loadingDialog7.show();
            commandV2_Up_SetCommand.setGuardDoorCloseTime(false, this.spinnerMulEditTextView.getSelectIndex(), 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.35
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass35() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                            SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                        } else {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                        }
                    } else {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 256) {
            int selectIndex4 = this.spinnerMulEditTextView.getSelectIndex();
            LoadingDialog loadingDialog8 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog8;
            loadingDialog8.show();
            commandV2_Up_SetCommand.setBoxSpeed(false, 0, selectIndex4);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.36
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass36() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 1)));
                    } else {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 257) {
            int selectIndex5 = this.spinnerMulEditTextView.getSelectIndex();
            LoadingDialog loadingDialog9 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog9;
            loadingDialog9.show();
            commandV2_Up_SetCommand.setBoxLmd(false, 0, selectIndex5);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.37
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass37() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 1)));
                    } else {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i == 326) {
            String surveillanceCameraPidVid = AppSetting.getSurveillanceCameraPidVid(this.context, this.mUserSettingDao);
            if (surveillanceCameraPidVid != null) {
                this.spinnerMulEditTextView.setEditeText(0, surveillanceCameraPidVid);
                return;
            }
            return;
        }
        if (i == 327) {
            String highTimeMeterPidVid = AppSetting.getHighTimeMeterPidVid(this.context, this.mUserSettingDao);
            if (highTimeMeterPidVid != null) {
                this.spinnerMulEditTextView.setEditeText(0, highTimeMeterPidVid);
                return;
            }
            return;
        }
        if (i == 338) {
            LoadingDialog loadingDialog10 = new LoadingDialog(this.context, R.string.on_querying);
            this.loadingDialog = loadingDialog10;
            loadingDialog10.show();
            int selectIndex6 = this.spinnerMulEditTextView.getSelectIndex();
            CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand3.setFindPeoperSensitivity(false, selectIndex6, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand3, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.39
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass39() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                            SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                        } else {
                            ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                        }
                    } else {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                    }
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        if (i != 339) {
            switch (i) {
                case 236:
                    LoadingDialog loadingDialog11 = new LoadingDialog(this.context, R.string.on_querying);
                    this.loadingDialog = loadingDialog11;
                    loadingDialog11.show();
                    commandV2_Up_SetCommand.setWKYSets(false, this.spinnerMulEditTextView.getSelectIndex(), 0, 0, 0, 0, 0, 0, 0, 0);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.25
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z) {
                        }

                        AnonymousClass25() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                            if (bArr != null && bArr.length > 0) {
                                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                                    ArrayList arrayList = new ArrayList();
                                    int i2 = 0;
                                    while (i2 < 8) {
                                        i2++;
                                        arrayList.add(Integer.valueOf(ObjectHelper.signedIntFromByte(bArr[i2])));
                                    }
                                    for (int i3 = 0; i3 < 7; i3++) {
                                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(i3, String.valueOf(arrayList.get(i3)));
                                    }
                                    if (((Integer) arrayList.get(7)).intValue() == 1) {
                                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setRadioGroupCheck(0);
                                    } else {
                                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setRadioGroupCheck(1);
                                    }
                                } else {
                                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                                }
                            }
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 237:
                    String editeText2 = this.spinnerMulEditTextView.getEditeText(0);
                    if (TextUtils.isEmpty(editeText2)) {
                        ToastUitl.showNotInputTip(this.context);
                        return;
                    }
                    LoadingDialog loadingDialog12 = new LoadingDialog(this.context, R.string.on_querying);
                    this.loadingDialog = loadingDialog12;
                    loadingDialog12.show();
                    commandV2_Up_SetCommand.setShelfModel(false, Integer.valueOf(editeText2).intValue(), 0);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.27
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z) {
                        }

                        AnonymousClass27() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                            if (bArr != null && bArr.length > 0) {
                                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setSelectSpinnerIndex(ObjectHelper.intFromBytes(bArr, 0, 1) - 1);
                            } else {
                                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                            }
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 238:
                    LoadingDialog loadingDialog13 = new LoadingDialog(this.context, R.string.on_querying);
                    this.loadingDialog = loadingDialog13;
                    loadingDialog13.show();
                    int i2 = this.cabinetNumber;
                    if (i2 == -1) {
                        i2 = this.spinnerMulEditTextView.getSelectIndex();
                    }
                    commandV2_Up_SetCommand.setEnginStopYZ(false, i2, 0);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.28
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z) {
                        }

                        AnonymousClass28() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                            if (bArr != null && bArr.length > 0) {
                                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                                } else {
                                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                                }
                            } else {
                                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                            }
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 239:
                    LoadingDialog loadingDialog14 = new LoadingDialog(this.context, R.string.on_querying);
                    this.loadingDialog = loadingDialog14;
                    loadingDialog14.show();
                    commandV2_Up_SetCommand.setEnginDLYZ(false, this.spinnerMulEditTextView.getSelectIndex(), 0);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.29
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z) {
                        }

                        AnonymousClass29() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                            if (bArr != null && bArr.length > 0) {
                                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                                } else {
                                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                                }
                            } else {
                                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                            }
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 240:
                    LoadingDialog loadingDialog15 = new LoadingDialog(this.context, R.string.on_querying);
                    this.loadingDialog = loadingDialog15;
                    loadingDialog15.show();
                    int i3 = this.cabinetNumber;
                    if (i3 == -1) {
                        i3 = this.spinnerMulEditTextView.getSelectIndex();
                    }
                    commandV2_Up_SetCommand.setMergeShelSynRunTime(false, i3, 0);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.30
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z) {
                        }

                        AnonymousClass30() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                            if (bArr != null && bArr.length > 0) {
                                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                                } else {
                                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                                }
                            } else {
                                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                            }
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                case 241:
                    List<Integer> list = SettingActivity.getBasicMachineInfo().shelvesMap.get(Integer.valueOf(this.cabinetNumber));
                    if (list == null || list.size() <= 0) {
                        return;
                    }
                    MutilTextTipDialog mutilTextTipDialog = new MutilTextTipDialog(this.context);
                    this.mutilTextTipDialog = mutilTextTipDialog;
                    mutilTextTipDialog.addTextShow(this.context.getString(R.string.start));
                    this.mutilTextTipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.31
                        AnonymousClass31() {
                        }

                        @Override // android.content.DialogInterface.OnDismissListener
                        public void onDismiss(DialogInterface dialogInterface) {
                            SpinnerMulEditTextNote.this.isQueryCargoLinkageing = false;
                            if (SpinnerMulEditTextNote.this.loadingDialog != null) {
                                SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                            }
                        }
                    });
                    this.mutilTextTipDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.32
                        AnonymousClass32() {
                        }

                        @Override // android.content.DialogInterface.OnShowListener
                        public void onShow(DialogInterface dialogInterface) {
                            SpinnerMulEditTextNote.this.loadingDialog = new LoadingDialog(SpinnerMulEditTextNote.this.context, R.string.on_querying);
                            SpinnerMulEditTextNote.this.loadingDialog.show();
                        }
                    });
                    this.mutilTextTipDialog.show();
                    this.isQueryCargoLinkageing = true;
                    this.cargoLinkageCount = 0;
                    queryAllCargoLinkage(list, list.get(0).intValue(), list.get(list.size() - 1).intValue());
                    return;
                case 242:
                    LoadingDialog loadingDialog16 = new LoadingDialog(this.context, R.string.on_querying);
                    this.loadingDialog = loadingDialog16;
                    loadingDialog16.show();
                    commandV2_Up_SetCommand.setSetLiftSpeed(false, this.spinnerMulEditTextView.getSelectIndex(), 0);
                    Shj.getInstance(this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.33
                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z) {
                        }

                        AnonymousClass33() {
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                            if (bArr != null && bArr.length > 0) {
                                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                                } else {
                                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                                }
                            } else {
                                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                            }
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                        }
                    });
                    return;
                default:
                    return;
            }
        }
        LoadingDialog loadingDialog17 = new LoadingDialog(this.context, R.string.on_querying);
        this.loadingDialog = loadingDialog17;
        loadingDialog17.show();
        int selectIndex7 = this.spinnerMulEditTextView.getSelectIndex();
        CommandV2_Up_SetCommand commandV2_Up_SetCommand4 = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand4.setFindPeoperDistance(false, selectIndex7, 0);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand4, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.40
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass40() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length > 0) {
                    if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                    } else {
                        ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                    }
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                }
                SpinnerMulEditTextNote.this.loadingDialog.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$23 */
    /* loaded from: classes2.dex */
    public class AnonymousClass23 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass23() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(1, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                }
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$24 */
    /* loaded from: classes2.dex */
    public class AnonymousClass24 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass24() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length >= 4) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 2);
                int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 2);
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(intFromBytes));
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(1, String.valueOf(intFromBytes2));
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$25 */
    /* loaded from: classes2.dex */
    public class AnonymousClass25 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass25() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    ArrayList arrayList = new ArrayList();
                    int i2 = 0;
                    while (i2 < 8) {
                        i2++;
                        arrayList.add(Integer.valueOf(ObjectHelper.signedIntFromByte(bArr[i2])));
                    }
                    for (int i3 = 0; i3 < 7; i3++) {
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(i3, String.valueOf(arrayList.get(i3)));
                    }
                    if (((Integer) arrayList.get(7)).intValue() == 1) {
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setRadioGroupCheck(0);
                    } else {
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setRadioGroupCheck(1);
                    }
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                }
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$26 */
    /* loaded from: classes2.dex */
    public class AnonymousClass26 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass26() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setSelectSpinnerIndex(ObjectHelper.intFromBytes(bArr, 0, 1) - 1);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$27 */
    /* loaded from: classes2.dex */
    public class AnonymousClass27 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass27() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setSelectSpinnerIndex(ObjectHelper.intFromBytes(bArr, 0, 1) - 1);
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$28 */
    /* loaded from: classes2.dex */
    public class AnonymousClass28 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass28() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                }
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$29 */
    /* loaded from: classes2.dex */
    public class AnonymousClass29 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass29() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                }
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$30 */
    /* loaded from: classes2.dex */
    public class AnonymousClass30 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass30() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                }
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$31 */
    /* loaded from: classes2.dex */
    public class AnonymousClass31 implements DialogInterface.OnDismissListener {
        AnonymousClass31() {
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            SpinnerMulEditTextNote.this.isQueryCargoLinkageing = false;
            if (SpinnerMulEditTextNote.this.loadingDialog != null) {
                SpinnerMulEditTextNote.this.loadingDialog.dismiss();
            }
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$32 */
    /* loaded from: classes2.dex */
    public class AnonymousClass32 implements DialogInterface.OnShowListener {
        AnonymousClass32() {
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            SpinnerMulEditTextNote.this.loadingDialog = new LoadingDialog(SpinnerMulEditTextNote.this.context, R.string.on_querying);
            SpinnerMulEditTextNote.this.loadingDialog.show();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$33 */
    /* loaded from: classes2.dex */
    public class AnonymousClass33 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass33() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                }
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$34 */
    /* loaded from: classes2.dex */
    public class AnonymousClass34 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass34() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                }
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$35 */
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
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                }
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$36 */
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
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 1)));
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$37 */
    /* loaded from: classes2.dex */
    public class AnonymousClass37 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass37() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 1)));
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$38 */
    /* loaded from: classes2.dex */
    public class AnonymousClass38 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass38() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 1);
                    int intFromBytes3 = ObjectHelper.intFromBytes(bArr, 3, 1);
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(intFromBytes));
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(1, String.valueOf(intFromBytes2));
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(2, String.valueOf(intFromBytes3));
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.communication_error);
                }
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$39 */
    /* loaded from: classes2.dex */
    public class AnonymousClass39 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass39() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                }
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$40 */
    /* loaded from: classes2.dex */
    public class AnonymousClass40 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass40() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 1)));
                } else {
                    ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
                }
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$41 */
    /* loaded from: classes2.dex */
    public class AnonymousClass41 implements OnCommandAnswerListener {
        AnonymousClass41() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length >= 2) {
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, String.valueOf(ObjectHelper.intFromBytes(bArr, 0, 2)));
            } else {
                ToastUitl.showShort(SpinnerMulEditTextNote.this.context, R.string.qurey_fail);
            }
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
        }
    }

    public void queryAllCargoLinkage(List<Integer> list, int i, int i2) {
        if (this.isQueryCargoLinkageing) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setMergeShelf(false, i, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.42
                final /* synthetic */ int val$end;
                final /* synthetic */ List val$shelvesList;
                final /* synthetic */ int val$start;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass42(int i3, int i22, List list2) {
                    i = i3;
                    i2 = i22;
                    list = list2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr == null || bArr.length <= 0) {
                        SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(i + SpinnerMulEditTextNote.this.context.getString(R.string.lab_shelf) + SpinnerMulEditTextNote.this.context.getString(R.string.qurey_fail));
                        int i3 = i;
                        int i4 = i3 + 1;
                        int i5 = i2;
                        if (i4 < i5) {
                            SpinnerMulEditTextNote.this.queryAllCargoLinkage(list, i3 + 1, i5);
                            return;
                        }
                        SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(SpinnerMulEditTextNote.this.context.getString(R.string.complete));
                        if (SpinnerMulEditTextNote.this.loadingDialog != null) {
                            SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                            return;
                        }
                        return;
                    }
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                    if (intFromBytes > 1) {
                        String str = "(" + i + ",";
                        for (int i6 = 1; i6 < intFromBytes; i6++) {
                            str = i6 != intFromBytes - 1 ? str + (i + i6) + "," : str + (i + i6);
                        }
                        SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(str + ")" + SpinnerMulEditTextNote.this.context.getString(R.string.cargo_linkage));
                        SpinnerMulEditTextNote.access$708(SpinnerMulEditTextNote.this);
                    }
                    int i7 = i;
                    int i8 = i7 + intFromBytes;
                    int i9 = i2;
                    if (i8 <= i9) {
                        SpinnerMulEditTextNote.this.queryAllCargoLinkage(list, i7 + intFromBytes, i9);
                        return;
                    }
                    if (SpinnerMulEditTextNote.this.cargoLinkageCount == 0) {
                        SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(SpinnerMulEditTextNote.this.context.getString(R.string.not_setting_cargo_link));
                    }
                    SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(SpinnerMulEditTextNote.this.context.getString(R.string.complete));
                    if (SpinnerMulEditTextNote.this.loadingDialog != null) {
                        SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$42 */
    /* loaded from: classes2.dex */
    public class AnonymousClass42 implements OnCommandAnswerListener {
        final /* synthetic */ int val$end;
        final /* synthetic */ List val$shelvesList;
        final /* synthetic */ int val$start;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass42(int i3, int i22, List list2) {
            i = i3;
            i2 = i22;
            list = list2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr == null || bArr.length <= 0) {
                SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(i + SpinnerMulEditTextNote.this.context.getString(R.string.lab_shelf) + SpinnerMulEditTextNote.this.context.getString(R.string.qurey_fail));
                int i3 = i;
                int i4 = i3 + 1;
                int i5 = i2;
                if (i4 < i5) {
                    SpinnerMulEditTextNote.this.queryAllCargoLinkage(list, i3 + 1, i5);
                    return;
                }
                SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(SpinnerMulEditTextNote.this.context.getString(R.string.complete));
                if (SpinnerMulEditTextNote.this.loadingDialog != null) {
                    SpinnerMulEditTextNote.this.loadingDialog.dismiss();
                    return;
                }
                return;
            }
            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
            if (intFromBytes > 1) {
                String str = "(" + i + ",";
                for (int i6 = 1; i6 < intFromBytes; i6++) {
                    str = i6 != intFromBytes - 1 ? str + (i + i6) + "," : str + (i + i6);
                }
                SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(str + ")" + SpinnerMulEditTextNote.this.context.getString(R.string.cargo_linkage));
                SpinnerMulEditTextNote.access$708(SpinnerMulEditTextNote.this);
            }
            int i7 = i;
            int i8 = i7 + intFromBytes;
            int i9 = i2;
            if (i8 <= i9) {
                SpinnerMulEditTextNote.this.queryAllCargoLinkage(list, i7 + intFromBytes, i9);
                return;
            }
            if (SpinnerMulEditTextNote.this.cargoLinkageCount == 0) {
                SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(SpinnerMulEditTextNote.this.context.getString(R.string.not_setting_cargo_link));
            }
            SpinnerMulEditTextNote.this.mutilTextTipDialog.addTextShow(SpinnerMulEditTextNote.this.context.getString(R.string.complete));
            if (SpinnerMulEditTextNote.this.loadingDialog != null) {
                SpinnerMulEditTextNote.this.loadingDialog.dismiss();
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        SpinnerMulEditTextView spinnerMulEditTextView = new SpinnerMulEditTextView(this.context, getNewAddSpinnerItemData(), getSpinnerItemData(), getEditeDataList(), getRadioGroupData());
        this.spinnerMulEditTextView = spinnerMulEditTextView;
        spinnerMulEditTextView.setTitle(getSettingName());
        setSaveSettingText();
        setTitleVisibility();
        setEditeTypeInterger();
        setButtonVisibility();
        setSpinnerListener();
        this.spinnerMulEditTextView.setEventListener(this.eventListener);
        return this.spinnerMulEditTextView;
    }

    private void setSpinnerListener() {
        SpinnerItemView spinnerItemView;
        int i = this.settingType;
        if (i == 326 || i == 327) {
            SpinnerItemView spinnerItemView2 = this.spinnerMulEditTextView.getSpinnerItemView();
            if (spinnerItemView2 != null) {
                spinnerItemView2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.43
                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }

                    AnonymousClass43() {
                    }

                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
                        if (i2 != 0) {
                            SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, SpinnerMulEditTextNote.this.spinnerMulEditTextView.getSelectValue());
                        }
                    }
                });
                return;
            }
            return;
        }
        if (i == 352 && (spinnerItemView = this.spinnerMulEditTextView.getSpinnerItemView()) != null) {
            spinnerItemView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.44
                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

                AnonymousClass44() {
                }

                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
                    SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, "");
                    if (i2 == 0 || i2 == 1) {
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeVisiable(0, 0);
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setQueryButtonVIsibility(0);
                    } else {
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeVisiable(0, 8);
                        SpinnerMulEditTextNote.this.spinnerMulEditTextView.setQueryButtonVIsibility(4);
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$43 */
    /* loaded from: classes2.dex */
    public class AnonymousClass43 implements AdapterView.OnItemSelectedListener {
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        AnonymousClass43() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
            if (i2 != 0) {
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, SpinnerMulEditTextNote.this.spinnerMulEditTextView.getSelectValue());
            }
        }
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$44 */
    /* loaded from: classes2.dex */
    public class AnonymousClass44 implements AdapterView.OnItemSelectedListener {
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        AnonymousClass44() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
            SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeText(0, "");
            if (i2 == 0 || i2 == 1) {
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeVisiable(0, 0);
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setQueryButtonVIsibility(0);
            } else {
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setEditeVisiable(0, 8);
                SpinnerMulEditTextNote.this.spinnerMulEditTextView.setQueryButtonVIsibility(4);
            }
        }
    }

    private void setSaveSettingText() {
        int i = this.settingType;
        if (i == 243 || i == 254) {
            this.spinnerMulEditTextView.setSaveSettingText(this.context.getString(R.string.exec_test));
        } else {
            if (i != 308) {
                return;
            }
            this.spinnerMulEditTextView.setSaveSettingText(this.context.getString(R.string.open));
        }
    }

    private void setTitleVisibility() {
        int i = this.settingType;
        if (i != 231 && i != 232 && i != 254 && i != 271 && i != 308 && i != 352 && i != 256 && i != 257) {
            if (i == 326 || i == 327) {
                this.spinnerMulEditTextView.setEditeTextNotInput(0);
                this.spinnerMulEditTextView.setTitleVisibility(0);
                return;
            } else if (i != 338 && i != 339) {
                switch (i) {
                    case 236:
                    case 237:
                    case 238:
                    case 239:
                    case 240:
                    case 241:
                    case 242:
                    case 243:
                    case 244:
                    case 245:
                        break;
                    default:
                        return;
                }
            }
        }
        this.spinnerMulEditTextView.setTitleVisibility(0);
    }

    private void setEditeTypeInterger() {
        int i = this.settingType;
        if (i == 231 || i == 232) {
            this.spinnerMulEditTextView.setEditeTypeInterger(0);
            this.spinnerMulEditTextView.setEditeTypeInterger(1);
            return;
        }
        if (i == 254) {
            this.spinnerMulEditTextView.setEditeTypeInterger(0);
            this.spinnerMulEditTextView.setEditeTypeInterger(1);
            this.spinnerMulEditTextView.setEditeTypeInterger(2);
            return;
        }
        if (i != 271) {
            if (i != 308 && i != 352 && i != 256 && i != 257 && i != 338 && i != 339) {
                switch (i) {
                    case 236:
                        for (int i2 = 0; i2 < 7; i2++) {
                            if (i2 != 0 && i2 != 4) {
                                this.spinnerMulEditTextView.setEditeTypeInterger(i2);
                            }
                        }
                        this.spinnerMulEditTextView.setEditeTypeNumberSigned(0);
                        this.spinnerMulEditTextView.setEditeTypeNumberSigned(4);
                        return;
                    case 237:
                    case 238:
                    case 239:
                    case 240:
                    case 241:
                    case 242:
                    case 244:
                    case 245:
                        break;
                    case 243:
                        this.spinnerMulEditTextView.setEditeTypeInterger(0);
                        this.spinnerMulEditTextView.setEditeTypeInterger(1);
                        return;
                    default:
                        return;
                }
            }
            this.spinnerMulEditTextView.setEditeTypeInterger(0);
            return;
        }
        this.spinnerMulEditTextView.setEditeTypeInterger(0);
        this.spinnerMulEditTextView.setEditeTypeInterger(1);
        this.spinnerMulEditTextView.setEditeTypeInterger(2);
        this.spinnerMulEditTextView.setEditeEnable(0, false);
    }

    private void setButtonVisibility() {
        int i = this.settingType;
        if (i != 119) {
            if (i != 271 && i != 352 && i != 231 && i != 232 && i != 244 && i != 245 && i != 256 && i != 257 && i != 338 && i != 339) {
                switch (i) {
                    case 236:
                    case 237:
                    case 238:
                    case 239:
                    case 240:
                    case 242:
                        break;
                    case 241:
                        this.spinnerMulEditTextView.setQueryButtonVIsibility(0);
                        this.spinnerMulEditTextView.setClearButtonVisibility(0);
                        this.spinnerMulEditTextView.setClearSettingText(this.context.getString(R.string.clear_cargo_linkage));
                        return;
                    default:
                        return;
                }
            }
            this.spinnerMulEditTextView.setQueryButtonVIsibility(0);
            return;
        }
        this.spinnerMulEditTextView.setAlwaysNotDisplaySaveButton();
        this.spinnerMulEditTextView.setChangeListener(new SpinnerMulEditTextView.ChangeListener() { // from class: com.shj.setting.generator.SpinnerMulEditTextNote.45
            AnonymousClass45() {
            }

            @Override // com.shj.setting.widget.SpinnerMulEditTextView.ChangeListener
            public void change() {
                SpinnerMulEditTextNote.this.saveSetting(false);
            }
        });
    }

    /* renamed from: com.shj.setting.generator.SpinnerMulEditTextNote$45 */
    /* loaded from: classes2.dex */
    public class AnonymousClass45 implements SpinnerMulEditTextView.ChangeListener {
        AnonymousClass45() {
        }

        @Override // com.shj.setting.widget.SpinnerMulEditTextView.ChangeListener
        public void change() {
            SpinnerMulEditTextNote.this.saveSetting(false);
        }
    }

    private SpinnerItemView.SpinnerItemData getNewAddSpinnerItemData() {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        if (this.settingType != 232) {
            return null;
        }
        spinnerItemData.name = this.context.getString(R.string.cabinet_number);
        spinnerItemData.dataList = SettingActivity.getBasicMachineInfo().cabinetNumberList;
        return spinnerItemData;
    }

    private SpinnerItemView.SpinnerItemData getSpinnerItemData() {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        int i = this.settingType;
        if (i == 119) {
            spinnerItemData.name = getSettingName();
            spinnerItemData.dataList = new ArrayList();
            spinnerItemData.dataList.add(this.context.getString(R.string.coin_changer));
            spinnerItemData.dataList.add("HOPPER");
            return spinnerItemData;
        }
        if (i != 254) {
            if (i != 271 && i != 308) {
                if (i == 352) {
                    spinnerItemData.name = this.context.getString(R.string.lab_capacity);
                    spinnerItemData.dataList = new ArrayList();
                    spinnerItemData.dataList.add(this.context.getString(R.string.ml_500));
                    spinnerItemData.dataList.add(this.context.getString(R.string.ml_1000));
                    spinnerItemData.dataList.add(this.context.getString(R.string.fill_the_mixing_tank));
                    spinnerItemData.dataList.add(this.context.getString(R.string.empty_the_mixing_tank));
                    return spinnerItemData;
                }
                if (i != 231) {
                    if (i == 232) {
                        spinnerItemData.name = this.context.getString(R.string.microwave_oven_number);
                        spinnerItemData.dataList = new ArrayList();
                        spinnerItemData.dataList.add("1");
                        spinnerItemData.dataList.add(ExifInterface.GPS_MEASUREMENT_2D);
                        return spinnerItemData;
                    }
                    if (i != 256 && i != 257) {
                        if (i == 326 || i == 327) {
                            spinnerItemData.name = this.context.getString(R.string.camera_pid_vid);
                            spinnerItemData.dataList = new ArrayList();
                            spinnerItemData.dataList.add("请点击选择PID与VID");
                            try {
                                int numberOfCameras = Camera.getNumberOfCameras();
                                Loger.writeLog("SET", "cameraCount=" + numberOfCameras);
                                for (int i2 = 0; i2 < numberOfCameras; i2++) {
                                    String execCmdGetResult = SetUtils.execCmdGetResult("cat /sys/class/video4linux/video" + i2 + "/device/modalias");
                                    if (execCmdGetResult != null && execCmdGetResult.startsWith(UsbFileUtil.DEFAULT_BIN_DIR)) {
                                        String substring = execCmdGetResult.substring(0, 14);
                                        if (!spinnerItemData.dataList.contains(substring)) {
                                            spinnerItemData.dataList.add(substring);
                                        }
                                    }
                                }
                            } catch (Exception unused) {
                            }
                            if (spinnerItemData.dataList.size() == 1) {
                                spinnerItemData.dataList.clear();
                                spinnerItemData.dataList.add("未找到摄像头");
                            }
                            return spinnerItemData;
                        }
                        if (i != 338 && i != 339) {
                            switch (i) {
                                case 236:
                                case 239:
                                case 242:
                                case 243:
                                case 244:
                                case 245:
                                    break;
                                case 237:
                                    spinnerItemData.name = this.context.getString(R.string.mode);
                                    spinnerItemData.dataList = new ArrayList();
                                    spinnerItemData.dataList.add(this.context.getString(R.string.spring_mode));
                                    spinnerItemData.dataList.add(this.context.getString(R.string.belt_mode));
                                    spinnerItemData.dataList.add(this.context.getString(R.string.hook_mode));
                                    return spinnerItemData;
                                case 238:
                                case 240:
                                    if (this.cabinetNumber != -1) {
                                        return null;
                                    }
                                    spinnerItemData.name = this.context.getString(R.string.cabinet_number);
                                    spinnerItemData.dataList = SettingActivity.getBasicMachineInfo().cabinetNumberList;
                                    return spinnerItemData;
                                case 241:
                                    spinnerItemData.name = this.context.getString(R.string.state_linkage);
                                    spinnerItemData.dataList = new ArrayList();
                                    spinnerItemData.dataList.add(this.context.getString(R.string.no_cargo_linkage));
                                    spinnerItemData.dataList.add(this.context.getString(R.string.two_cargo_linkage));
                                    spinnerItemData.dataList.add(this.context.getString(R.string.three_cargo_linkage));
                                    spinnerItemData.dataList.add(this.context.getString(R.string.four_cargo_linkage));
                                    spinnerItemData.dataList.add(this.context.getString(R.string.five_cargo_linkage));
                                    spinnerItemData.dataList.add(this.context.getString(R.string.six_cargo_linkage));
                                    spinnerItemData.dataList.add(this.context.getString(R.string.seven_cargo_linkage));
                                    spinnerItemData.dataList.add(this.context.getString(R.string.eight_cargo_linkage));
                                    spinnerItemData.dataList.add(this.context.getString(R.string.nine_cargo_linkage));
                                    spinnerItemData.dataList.add(this.context.getString(R.string.ten_cargo_linkage));
                                    return spinnerItemData;
                                default:
                                    return null;
                            }
                        }
                    }
                }
            }
            spinnerItemData.name = this.context.getString(R.string.cabinet_number);
            spinnerItemData.dataList = SettingActivity.getBasicMachineInfo().cabinetNumberList;
            return spinnerItemData;
        }
        spinnerItemData.name = this.context.getString(R.string.is_out_goods);
        spinnerItemData.dataList = new ArrayList();
        spinnerItemData.dataList.add(this.context.getString(R.string.enable_yes));
        spinnerItemData.dataList.add(this.context.getString(R.string.enable_no));
        return spinnerItemData;
    }

    private List<MultipleEditItemView.EditTextDataInfo> getEditeDataList() {
        ArrayList arrayList = new ArrayList();
        int i = this.settingType;
        if (i == 231) {
            MultipleEditItemView.EditTextDataInfo editTextDataInfo = new MultipleEditItemView.EditTextDataInfo();
            String string = this.context.getString(R.string.location_layer_number);
            editTextDataInfo.name = string + "(0-9)";
            editTextDataInfo.tipInfo = this.context.getString(R.string.please_input) + string;
            arrayList.add(editTextDataInfo);
            MultipleEditItemView.EditTextDataInfo editTextDataInfo2 = new MultipleEditItemView.EditTextDataInfo();
            String string2 = this.context.getString(R.string.number_positioning_pulses);
            editTextDataInfo2.name = string2 + "(0-8000)";
            editTextDataInfo2.tipInfo = this.context.getString(R.string.please_input) + string2;
            arrayList.add(editTextDataInfo2);
            return arrayList;
        }
        if (i == 232) {
            MultipleEditItemView.EditTextDataInfo editTextDataInfo3 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo3.name = this.context.getString(R.string.layer_pulse_number);
            editTextDataInfo3.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo3.name;
            arrayList.add(editTextDataInfo3);
            MultipleEditItemView.EditTextDataInfo editTextDataInfo4 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo4.name = this.context.getString(R.string.box_pulse_number);
            editTextDataInfo4.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo4.name;
            arrayList.add(editTextDataInfo4);
            return arrayList;
        }
        if (i == 254) {
            MultipleEditItemView.EditTextDataInfo editTextDataInfo5 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo5.name = this.context.getString(R.string.initial_cargo_number);
            editTextDataInfo5.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo5.name;
            arrayList.add(editTextDataInfo5);
            MultipleEditItemView.EditTextDataInfo editTextDataInfo6 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo6.name = this.context.getString(R.string.stop_shipment_no);
            editTextDataInfo6.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo6.name;
            arrayList.add(editTextDataInfo6);
            MultipleEditItemView.EditTextDataInfo editTextDataInfo7 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo7.name = this.context.getString(R.string.cycle_times);
            editTextDataInfo7.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo7.name;
            arrayList.add(editTextDataInfo7);
            return arrayList;
        }
        if (i == 271) {
            MultipleEditItemView.EditTextDataInfo editTextDataInfo8 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo8.name = this.context.getString(R.string.humidity);
            arrayList.add(editTextDataInfo8);
            MultipleEditItemView.EditTextDataInfo editTextDataInfo9 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo9.name = this.context.getString(R.string.humidity_lower);
            arrayList.add(editTextDataInfo9);
            MultipleEditItemView.EditTextDataInfo editTextDataInfo10 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo10.name = this.context.getString(R.string.humidity_upper);
            arrayList.add(editTextDataInfo10);
            return arrayList;
        }
        if (i == 308) {
            MultipleEditItemView.EditTextDataInfo editTextDataInfo11 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo11.name = this.context.getString(R.string.layer_number);
            arrayList.add(editTextDataInfo11);
            return arrayList;
        }
        if (i == 352) {
            MultipleEditItemView.EditTextDataInfo editTextDataInfo12 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo12.name = this.context.getString(R.string.pulse_count);
            arrayList.add(editTextDataInfo12);
            return arrayList;
        }
        if (i == 256) {
            MultipleEditItemView.EditTextDataInfo editTextDataInfo13 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo13.name = this.context.getString(R.string.speed);
            editTextDataInfo13.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo13.name;
            arrayList.add(editTextDataInfo13);
            return arrayList;
        }
        if (i == 257) {
            MultipleEditItemView.EditTextDataInfo editTextDataInfo14 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo14.name = this.context.getString(R.string.sensitivity);
            editTextDataInfo14.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo14.name;
            arrayList.add(editTextDataInfo14);
            return arrayList;
        }
        if (i == 326 || i == 327) {
            MultipleEditItemView.EditTextDataInfo editTextDataInfo15 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo15.name = getSettingName();
            editTextDataInfo15.tipInfo = "请选择PID与VID";
            arrayList.add(editTextDataInfo15);
            return arrayList;
        }
        if (i == 338) {
            MultipleEditItemView.EditTextDataInfo editTextDataInfo16 = new MultipleEditItemView.EditTextDataInfo();
            editTextDataInfo16.name = this.context.getString(R.string.sensitivity) + "(4-15)";
            editTextDataInfo16.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo16.name;
            arrayList.add(editTextDataInfo16);
            return arrayList;
        }
        if (i != 339) {
            switch (i) {
                case 236:
                    ArrayList<String> arrayList2 = new ArrayList();
                    arrayList2.add(this.context.getString(R.string.control_lower_temperature_limit));
                    arrayList2.add(this.context.getString(R.string.control_temperature_upper_limit));
                    arrayList2.add(this.context.getString(R.string.return_temperature));
                    arrayList2.add(this.context.getString(R.string.delayed_startup_time));
                    arrayList2.add(this.context.getString(R.string.probe_temperature_correction));
                    arrayList2.add(this.context.getString(R.string.downtime_defrosting_cycle));
                    arrayList2.add(this.context.getString(R.string.downtime_defrosting_time));
                    for (String str : arrayList2) {
                        MultipleEditItemView.EditTextDataInfo editTextDataInfo17 = new MultipleEditItemView.EditTextDataInfo();
                        editTextDataInfo17.name = str;
                        arrayList.add(editTextDataInfo17);
                    }
                    return arrayList;
                case 237:
                    MultipleEditItemView.EditTextDataInfo editTextDataInfo18 = new MultipleEditItemView.EditTextDataInfo();
                    editTextDataInfo18.name = this.context.getString(R.string.layer_number);
                    editTextDataInfo18.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo18.name;
                    arrayList.add(editTextDataInfo18);
                    return arrayList;
                case 238:
                    MultipleEditItemView.EditTextDataInfo editTextDataInfo19 = new MultipleEditItemView.EditTextDataInfo();
                    editTextDataInfo19.name = this.context.getString(R.string.stop_threshold) + "(20-250)";
                    arrayList.add(editTextDataInfo19);
                    return arrayList;
                case 239:
                    MultipleEditItemView.EditTextDataInfo editTextDataInfo20 = new MultipleEditItemView.EditTextDataInfo();
                    editTextDataInfo20.name = this.context.getString(R.string.stop_threshold) + "(700-900)";
                    arrayList.add(editTextDataInfo20);
                    return arrayList;
                case 240:
                    MultipleEditItemView.EditTextDataInfo editTextDataInfo21 = new MultipleEditItemView.EditTextDataInfo();
                    editTextDataInfo21.name = this.context.getString(R.string.sync_time);
                    editTextDataInfo21.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo21.name;
                    arrayList.add(editTextDataInfo21);
                    return arrayList;
                case 241:
                    MultipleEditItemView.EditTextDataInfo editTextDataInfo22 = new MultipleEditItemView.EditTextDataInfo();
                    editTextDataInfo22.name = this.context.getString(R.string.cargo_number);
                    editTextDataInfo22.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo22.name;
                    arrayList.add(editTextDataInfo22);
                    return arrayList;
                case 242:
                    MultipleEditItemView.EditTextDataInfo editTextDataInfo23 = new MultipleEditItemView.EditTextDataInfo();
                    editTextDataInfo23.name = this.context.getString(R.string.lift_speed);
                    editTextDataInfo23.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo23.name;
                    arrayList.add(editTextDataInfo23);
                    return arrayList;
                case 243:
                    MultipleEditItemView.EditTextDataInfo editTextDataInfo24 = new MultipleEditItemView.EditTextDataInfo();
                    String string3 = this.context.getString(R.string.layer_number);
                    editTextDataInfo24.name = string3 + "(0-9)";
                    editTextDataInfo24.tipInfo = this.context.getString(R.string.please_input) + string3;
                    arrayList.add(editTextDataInfo24);
                    MultipleEditItemView.EditTextDataInfo editTextDataInfo25 = new MultipleEditItemView.EditTextDataInfo();
                    String string4 = this.context.getString(R.string.cycle_times);
                    editTextDataInfo25.name = string4 + "(0-10000)";
                    editTextDataInfo25.tipInfo = this.context.getString(R.string.please_input) + string4;
                    arrayList.add(editTextDataInfo25);
                    return arrayList;
                case 244:
                    MultipleEditItemView.EditTextDataInfo editTextDataInfo26 = new MultipleEditItemView.EditTextDataInfo();
                    String string5 = this.context.getString(R.string.sensitivity);
                    editTextDataInfo26.name = string5 + "(0-50)";
                    editTextDataInfo26.tipInfo = this.context.getString(R.string.please_input) + string5;
                    arrayList.add(editTextDataInfo26);
                    return arrayList;
                case 245:
                    MultipleEditItemView.EditTextDataInfo editTextDataInfo27 = new MultipleEditItemView.EditTextDataInfo();
                    String string6 = this.context.getString(R.string.closing_time);
                    editTextDataInfo27.name = string6 + "(20-8000ms)";
                    editTextDataInfo27.tipInfo = this.context.getString(R.string.please_input) + string6;
                    arrayList.add(editTextDataInfo27);
                    return arrayList;
                default:
                    return null;
            }
        }
        MultipleEditItemView.EditTextDataInfo editTextDataInfo28 = new MultipleEditItemView.EditTextDataInfo();
        editTextDataInfo28.name = this.context.getString(R.string.distance) + "(0-255cm)";
        editTextDataInfo28.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo28.name;
        arrayList.add(editTextDataInfo28);
        return arrayList;
    }

    private RadioGroupItemView.RadioGroupData getRadioGroupData() {
        if (this.settingType != 236) {
            return null;
        }
        RadioGroupItemView.RadioGroupData radioGroupData = new RadioGroupItemView.RadioGroupData();
        radioGroupData.isVertical = false;
        radioGroupData.title = this.context.getString(R.string.current_protection_settings);
        radioGroupData.nameList = new ArrayList();
        radioGroupData.nameList.add(this.context.getString(R.string.lab_enable));
        radioGroupData.nameList.add(this.context.getString(R.string.lab_disable));
        return radioGroupData;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.spinnerMulEditTextView;
    }
}
