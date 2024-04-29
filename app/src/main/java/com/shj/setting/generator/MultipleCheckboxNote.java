package com.shj.setting.generator;

import android.content.Context;
import android.view.View;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.MultipleCheckBoxView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MultipleCheckboxNote extends SettingNote {
    private boolean isShowCommandItem;
    private MultipleCheckBoxView multipleCheckBoxView;

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
    }

    public MultipleCheckboxNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    public void setIsShowCommandItem(boolean z) {
        this.isShowCommandItem = z;
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        AppSetting.savePaymentMethodCash(this.context, this.multipleCheckBoxView.isChecked(0), null);
        AppSetting.savePaymentAggregateCode(this.context, this.multipleCheckBoxView.isChecked(1), null);
        AppSetting.savePaymentMethodWeixin(this.context, this.multipleCheckBoxView.isChecked(2), null);
        AppSetting.savePaymentMethodZfb(this.context, this.multipleCheckBoxView.isChecked(3), null);
        AppSetting.savePaymentMethodYl(this.context, this.multipleCheckBoxView.isChecked(4), null);
        AppSetting.savePaymentMethodYlx(this.context, this.multipleCheckBoxView.isChecked(5), null);
        AppSetting.savePaymentMethodJd(this.context, this.multipleCheckBoxView.isChecked(6), null);
        AppSetting.savePaymentMethodYlsf(this.context, this.multipleCheckBoxView.isChecked(7), null);
        AppSetting.savePaymentMethodIc(this.context, this.multipleCheckBoxView.isChecked(8), null);
        AppSetting.savePaymentMethodJF(this.context, this.multipleCheckBoxView.isChecked(9), null);
        AppSetting.savePaymentMethodScan(this.context, this.multipleCheckBoxView.isChecked(10), null);
        if (this.isShowCommandItem) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setYLICCard(true, this.multipleCheckBoxView.isChecked(7) || this.multipleCheckBoxView.isChecked(8));
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleCheckboxNote.1
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                    ToastUitl.showSetCompeleteTip(MultipleCheckboxNote.this.context, z2, MultipleCheckboxNote.this.getSettingName());
                }
            });
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleCheckboxNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(MultipleCheckboxNote.this.context, z2, MultipleCheckboxNote.this.getSettingName());
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        this.multipleCheckBoxView.setChecked(0, AppSetting.getPaymentMethodCash(this.context, null));
        this.multipleCheckBoxView.setChecked(1, AppSetting.getPaymentMethodAggregateCode(this.context, null));
        this.multipleCheckBoxView.setChecked(2, AppSetting.getPaymentMethodWeixin(this.context, null));
        this.multipleCheckBoxView.setChecked(3, AppSetting.getPaymentMethodZfb(this.context, null));
        this.multipleCheckBoxView.setChecked(4, AppSetting.getPaymentMethodYl(this.context, null));
        this.multipleCheckBoxView.setChecked(5, AppSetting.getPaymentMethodYlx(this.context, null));
        this.multipleCheckBoxView.setChecked(6, AppSetting.getPaymentMethodJd(this.context, null));
        this.multipleCheckBoxView.setChecked(7, AppSetting.getPaymentMethodYlsf(this.context, null));
        this.multipleCheckBoxView.setChecked(8, AppSetting.getPaymentMethodIc(this.context, null));
        this.multipleCheckBoxView.setChecked(9, AppSetting.getPaymentMethodJF(this.context, null));
        this.multipleCheckBoxView.setChecked(10, AppSetting.getPaymentMethodScan(this.context, null));
        if (this.isShowCommandItem) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setYLICCard(false, false);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleCheckboxNote.2
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
                        MultipleCheckboxNote.this.multipleCheckBoxView.setChecked(7, true);
                        MultipleCheckboxNote.this.multipleCheckBoxView.setChecked(8, true);
                        AppSetting.savePaymentMethodYlsf(MultipleCheckboxNote.this.context, true, null);
                        AppSetting.savePaymentMethodIc(MultipleCheckboxNote.this.context, true, null);
                        return;
                    }
                    MultipleCheckboxNote.this.multipleCheckBoxView.setChecked(7, false);
                    MultipleCheckboxNote.this.multipleCheckBoxView.setChecked(8, false);
                    AppSetting.savePaymentMethodYlsf(MultipleCheckboxNote.this.context, false, null);
                    AppSetting.savePaymentMethodIc(MultipleCheckboxNote.this.context, false, null);
                }
            });
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleCheckboxNote$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements OnCommandAnswerListener {
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
                MultipleCheckboxNote.this.multipleCheckBoxView.setChecked(7, true);
                MultipleCheckboxNote.this.multipleCheckBoxView.setChecked(8, true);
                AppSetting.savePaymentMethodYlsf(MultipleCheckboxNote.this.context, true, null);
                AppSetting.savePaymentMethodIc(MultipleCheckboxNote.this.context, true, null);
                return;
            }
            MultipleCheckboxNote.this.multipleCheckBoxView.setChecked(7, false);
            MultipleCheckboxNote.this.multipleCheckBoxView.setChecked(8, false);
            AppSetting.savePaymentMethodYlsf(MultipleCheckboxNote.this.context, false, null);
            AppSetting.savePaymentMethodIc(MultipleCheckboxNote.this.context, false, null);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        MultipleCheckBoxView multipleCheckBoxView = new MultipleCheckBoxView(this.context, getNameList());
        this.multipleCheckBoxView = multipleCheckBoxView;
        multipleCheckBoxView.setTitle(getSettingName());
        this.multipleCheckBoxView.setEventListener(this.eventListener);
        this.multipleCheckBoxView.setTitleVisibility(0);
        return this.multipleCheckBoxView;
    }

    public List<MultipleCheckBoxView.CheckBoxData> getNameList() {
        ArrayList arrayList = new ArrayList();
        if (this.settingType == 105) {
            MultipleCheckBoxView.CheckBoxData checkBoxData = new MultipleCheckBoxView.CheckBoxData();
            checkBoxData.name = this.context.getResources().getString(R.string.lab_paytype_cash);
            checkBoxData.isHide = !AppSetting.isSettingEnabled(this.context, SettingType.PAYMENT_METHOD_CASH, this.mUserSettingDao);
            arrayList.add(checkBoxData);
            MultipleCheckBoxView.CheckBoxData checkBoxData2 = new MultipleCheckBoxView.CheckBoxData();
            checkBoxData2.name = this.context.getResources().getString(R.string.payment_method_aggregate_code);
            checkBoxData2.isHide = !AppSetting.isSettingEnabled(this.context, SettingType.PAYMENT_METHOD_AGGREGATE_CODE, this.mUserSettingDao);
            arrayList.add(checkBoxData2);
            MultipleCheckBoxView.CheckBoxData checkBoxData3 = new MultipleCheckBoxView.CheckBoxData();
            checkBoxData3.name = this.context.getResources().getString(R.string.lab_paytype_weixin);
            checkBoxData3.isHide = !AppSetting.isSettingEnabled(this.context, SettingType.PAYMENT_METHOD_WEIXIN, this.mUserSettingDao);
            arrayList.add(checkBoxData3);
            MultipleCheckBoxView.CheckBoxData checkBoxData4 = new MultipleCheckBoxView.CheckBoxData();
            checkBoxData4.name = this.context.getResources().getString(R.string.lab_paytype_zfb);
            checkBoxData4.isHide = !AppSetting.isSettingEnabled(this.context, SettingType.PAYMENT_METHOD_ZFB, this.mUserSettingDao);
            arrayList.add(checkBoxData4);
            MultipleCheckBoxView.CheckBoxData checkBoxData5 = new MultipleCheckBoxView.CheckBoxData();
            checkBoxData5.name = this.context.getResources().getString(R.string.lab_paytype_ylsm);
            checkBoxData5.isHide = !AppSetting.isSettingEnabled(this.context, SettingType.PAYMENT_METHOD_YL, this.mUserSettingDao);
            arrayList.add(checkBoxData5);
            MultipleCheckBoxView.CheckBoxData checkBoxData6 = new MultipleCheckBoxView.CheckBoxData();
            checkBoxData6.name = this.context.getResources().getString(R.string.lab_paytype_ylsmx);
            checkBoxData6.isHide = !AppSetting.isSettingEnabled(this.context, SettingType.PAYMENT_METHOD_YLX, this.mUserSettingDao);
            arrayList.add(checkBoxData6);
            MultipleCheckBoxView.CheckBoxData checkBoxData7 = new MultipleCheckBoxView.CheckBoxData();
            checkBoxData7.name = this.context.getResources().getString(R.string.lab_paytype_jdzf);
            checkBoxData7.isHide = !AppSetting.isSettingEnabled(this.context, 213, this.mUserSettingDao);
            arrayList.add(checkBoxData7);
            MultipleCheckBoxView.CheckBoxData checkBoxData8 = new MultipleCheckBoxView.CheckBoxData();
            checkBoxData8.name = this.context.getResources().getString(R.string.lab_paytype_ylsf);
            checkBoxData8.isHide = !AppSetting.isSettingEnabled(this.context, 211, this.mUserSettingDao);
            arrayList.add(checkBoxData8);
            MultipleCheckBoxView.CheckBoxData checkBoxData9 = new MultipleCheckBoxView.CheckBoxData();
            checkBoxData9.name = this.context.getResources().getString(R.string.lab_paytype_iccard);
            checkBoxData9.isHide = !AppSetting.isSettingEnabled(this.context, 212, this.mUserSettingDao);
            arrayList.add(checkBoxData9);
            MultipleCheckBoxView.CheckBoxData checkBoxData10 = new MultipleCheckBoxView.CheckBoxData();
            checkBoxData10.name = this.context.getResources().getString(R.string.payment_method_jf);
            checkBoxData10.isHide = !AppSetting.isSettingEnabled(this.context, SettingType.PAYMENT_METHOD_JF, this.mUserSettingDao);
            arrayList.add(checkBoxData10);
            MultipleCheckBoxView.CheckBoxData checkBoxData11 = new MultipleCheckBoxView.CheckBoxData();
            checkBoxData11.name = this.context.getResources().getString(R.string.lab_paytype_scan);
            checkBoxData11.isHide = !AppSetting.isSettingEnabled(this.context, 212, this.mUserSettingDao);
            arrayList.add(checkBoxData11);
        }
        return arrayList;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        MultipleCheckBoxView multipleCheckBoxView = this.multipleCheckBoxView;
        if (multipleCheckBoxView != null) {
            return multipleCheckBoxView;
        }
        return null;
    }
}
