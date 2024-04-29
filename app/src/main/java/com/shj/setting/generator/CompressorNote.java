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
import com.shj.setting.widget.CompressorView;
import com.shj.setting.widget.MultipleEditItemView;
import com.shj.setting.widget.SpinnerItemView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class CompressorNote extends SettingNote {
    private CompressorView compressorView;
    private LoadingDialog loadingDialog;

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
    }

    public CompressorNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int selectCabinet = this.compressorView.getSelectCabinet();
        List<String> inputTextList = this.compressorView.getInputTextList();
        boolean z2 = true;
        if (inputTextList.size() >= 3) {
            String[] split = inputTextList.get(0).split("-");
            String[] split2 = inputTextList.get(1).split("-");
            String[] split3 = inputTextList.get(2).split("-");
            if (isRightInput(inputTextList.get(0)) && isRightInput(inputTextList.get(1)) && isRightInput(inputTextList.get(2))) {
                if (split.length >= 2) {
                    i = Integer.valueOf(split[0]).intValue();
                    i2 = Integer.valueOf(split[1]).intValue();
                } else {
                    i = 0;
                    i2 = 0;
                }
                if (split2.length >= 2) {
                    i3 = Integer.valueOf(split2[0]).intValue();
                    i4 = Integer.valueOf(split2[1]).intValue();
                } else {
                    i3 = 0;
                    i4 = 0;
                }
                if (split3.length >= 2) {
                    i5 = Integer.valueOf(split3[0]).intValue();
                    i6 = Integer.valueOf(split3[1]).intValue();
                } else {
                    i5 = 0;
                    i6 = 0;
                }
                LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.saveing);
                this.loadingDialog = loadingDialog;
                loadingDialog.show();
                CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand.setYSJ(true, selectCabinet, i, i2, i3, i4, i5, i6);
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.CompressorNote.1
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass1() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z3) {
                        ToastUitl.showCompeleteTip(CompressorNote.this.context, z3, CompressorNote.this.getSettingName());
                        CompressorNote.this.loadingDialog.dismiss();
                    }
                });
                z2 = false;
            }
        }
        if (z2) {
            ToastUitl.showErrorInputTip(this.context, "(8-10)");
        }
    }

    /* renamed from: com.shj.setting.generator.CompressorNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z3) {
            ToastUitl.showCompeleteTip(CompressorNote.this.context, z3, CompressorNote.this.getSettingName());
            CompressorNote.this.loadingDialog.dismiss();
        }
    }

    public boolean isRightInput(String str) {
        return "0".equals(str) || Pattern.compile("[0-9]{1,2}-[0-9]{1,2}").matcher(str).matches();
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        queryData();
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        CompressorView.CompressorData compressorData = new CompressorView.CompressorData();
        compressorData.spinnerItemData = new SpinnerItemView.SpinnerItemData();
        compressorData.spinnerItemData.name = this.context.getString(R.string.cabinet_number);
        compressorData.spinnerItemData.dataList = SettingActivity.getBasicMachineInfo().cabinetNumberList;
        compressorData.editTextDataList = new ArrayList();
        String string = this.context.getResources().getString(R.string.working_time);
        int i2 = 0;
        while (i2 < 3) {
            MultipleEditItemView.EditTextDataInfo editTextDataInfo = new MultipleEditItemView.EditTextDataInfo();
            i2++;
            editTextDataInfo.name = String.format(string, Integer.valueOf(i2));
            editTextDataInfo.tipInfo = "";
            compressorData.editTextDataList.add(editTextDataInfo);
        }
        CompressorView compressorView = new CompressorView(this.context, compressorData);
        this.compressorView = compressorView;
        compressorView.setTitle(getSettingName());
        this.compressorView.setTitleVisibility(0);
        this.compressorView.setQueryButtonVIsibility(0);
        this.compressorView.setEventListener(this.eventListener);
        return this.compressorView;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.compressorView;
    }

    private void queryData() {
        LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.on_querying);
        this.loadingDialog = loadingDialog;
        loadingDialog.show();
        int selectCabinet = this.compressorView.getSelectCabinet();
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setYSJ(false, selectCabinet, 0, 0, 0, 0, 0, 0);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.CompressorNote.2
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass2() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr == null || bArr.length <= 0) {
                    return;
                }
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 1);
                    int intFromBytes3 = ObjectHelper.intFromBytes(bArr, 3, 1);
                    int intFromBytes4 = ObjectHelper.intFromBytes(bArr, 4, 1);
                    int intFromBytes5 = ObjectHelper.intFromBytes(bArr, 5, 1);
                    int intFromBytes6 = ObjectHelper.intFromBytes(bArr, 6, 1);
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(intFromBytes + "-" + intFromBytes2);
                    arrayList.add(intFromBytes3 + "-" + intFromBytes4);
                    arrayList.add(intFromBytes5 + "-" + intFromBytes6);
                    CompressorNote.this.compressorView.setEditText(arrayList);
                } else {
                    ToastUitl.showShort(CompressorNote.this.context, R.string.communication_error);
                }
                CompressorNote.this.loadingDialog.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.generator.CompressorNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass2() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr == null || bArr.length <= 0) {
                return;
            }
            if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 1);
                int intFromBytes3 = ObjectHelper.intFromBytes(bArr, 3, 1);
                int intFromBytes4 = ObjectHelper.intFromBytes(bArr, 4, 1);
                int intFromBytes5 = ObjectHelper.intFromBytes(bArr, 5, 1);
                int intFromBytes6 = ObjectHelper.intFromBytes(bArr, 6, 1);
                ArrayList arrayList = new ArrayList();
                arrayList.add(intFromBytes + "-" + intFromBytes2);
                arrayList.add(intFromBytes3 + "-" + intFromBytes4);
                arrayList.add(intFromBytes5 + "-" + intFromBytes6);
                CompressorNote.this.compressorView.setEditText(arrayList);
            } else {
                ToastUitl.showShort(CompressorNote.this.context, R.string.communication_error);
            }
            CompressorNote.this.loadingDialog.dismiss();
        }
    }
}
