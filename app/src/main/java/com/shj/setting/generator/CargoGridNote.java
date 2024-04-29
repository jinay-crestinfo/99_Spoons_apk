package com.shj.setting.generator;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import com.oysb.utils.Event.BaseEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.OnShjGoodsSetResultListener;
import com.shj.Shj;
import com.shj.ShjGoodsSetting;
import com.shj.biz.ShjManager;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Dialog.GoodsInfoInputDialog;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.MutilTextTipDialog;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.event.SettingTypeEvent;
import com.shj.setting.event.ShowShelfErrorTipEvent;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.CargoGridView;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* loaded from: classes.dex */
public class CargoGridNote extends SettingNote {
    private int cabinetNumber;
    private CargoGridView cargoGridView;
    private CargoGridView.GridItemData gridItemData;
    private boolean isShowAddAndReduce;
    private LoadingDialog loadingDialog;
    private MutilTextTipDialog mutilTextTipDialog;

    public CargoGridNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        boolean z2;
        boolean z3;
        List<CargoGridView.IndexData> inputData = this.cargoGridView.getInputData();
        int i = this.settingType;
        int i2 = 0;
        if (i == 123) {
            ArrayList<CargoGridView.IndexData> arrayList = new ArrayList();
            int i3 = 0;
            for (CargoGridView.IndexData indexData : inputData) {
                int i4 = indexData.identifier;
                if (!TextUtils.isEmpty(indexData.inputText)) {
                    float parseFloat = Float.parseFloat(indexData.inputText);
                    if (TextUtils.isEmpty(indexData.beforeSettingText)) {
                        i3++;
                        Loger.writeLog("SET", "设置 货道号=" + i4 + "价格=" + (parseFloat * 100.0f));
                        arrayList.add(indexData);
                    } else if (parseFloat != Float.parseFloat(indexData.beforeSettingText)) {
                        i3++;
                        Loger.writeLog("SET", "设置 货道号=" + i4 + "价格=" + (parseFloat * 100.0f));
                        arrayList.add(indexData);
                    }
                }
            }
            if (i3 > 0) {
                MutilTextTipDialog mutilTextTipDialog = new MutilTextTipDialog(this.context);
                this.mutilTextTipDialog = mutilTextTipDialog;
                mutilTextTipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.shj.setting.generator.CargoGridNote.1
                    AnonymousClass1() {
                    }

                    @Override // android.content.DialogInterface.OnDismissListener
                    public void onDismiss(DialogInterface dialogInterface) {
                        EventBus.getDefault().post(new ShowShelfErrorTipEvent());
                    }
                });
                this.mutilTextTipDialog.show();
                ShjGoodsSetting shjGoodsSetting = new ShjGoodsSetting();
                shjGoodsSetting.count = i3;
                shjGoodsSetting.onShjGoodsSetResultListener = new OnShjGoodsSetResultListener() { // from class: com.shj.setting.generator.CargoGridNote.2
                    AnonymousClass2() {
                    }

                    @Override // com.shj.OnShjGoodsSetResultListener
                    public void result(int i5, Object obj, boolean z4) {
                        String string = CargoGridNote.this.context.getString(R.string.lab_shelf);
                        String string2 = CargoGridNote.this.context.getString(R.string.lab_price);
                        float intValue = ((Integer) obj).intValue() / 100.0f;
                        CargoGridNote.this.mutilTextTipDialog.addTextShow(string + ":" + String.format("%03d", Integer.valueOf(i5)) + "  " + string2 + ":" + intValue);
                        CargoGridNote.this.cargoGridView.setBeforeSettingText(i5, String.valueOf(intValue));
                        if (z4) {
                            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                        }
                    }
                };
                Shj.addGoodsSetCommand(18, shjGoodsSetting);
                for (CargoGridView.IndexData indexData2 : arrayList) {
                    ShjManager.setShelfGoodsPrice(indexData2.identifier, Math.round(Float.parseFloat(indexData2.inputText) * 100.0f));
                }
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.lab_price));
            return;
        }
        if (i == 124) {
            ArrayList<CargoGridView.IndexData> arrayList2 = new ArrayList();
            int i5 = 0;
            for (CargoGridView.IndexData indexData3 : inputData) {
                int i6 = indexData3.identifier;
                if (!TextUtils.isEmpty(indexData3.inputText)) {
                    float parseFloat2 = Float.parseFloat(indexData3.inputText);
                    if (TextUtils.isEmpty(indexData3.beforeSettingText)) {
                        indexData3.needUpdate = true;
                        i5++;
                        Loger.writeLog("SET", "设置 货道号=" + (i6 + 1000) + "价格=" + (parseFloat2 * 100.0f));
                        arrayList2.add(indexData3);
                    } else if (parseFloat2 != Float.parseFloat(indexData3.beforeSettingText)) {
                        indexData3.needUpdate = true;
                        i5++;
                        Loger.writeLog("SET", "设置 货道号=" + (i6 + 1000) + "价格=" + (parseFloat2 * 100.0f));
                        arrayList2.add(indexData3);
                    }
                }
            }
            if (i5 > 0) {
                MutilTextTipDialog mutilTextTipDialog2 = new MutilTextTipDialog(this.context);
                this.mutilTextTipDialog = mutilTextTipDialog2;
                mutilTextTipDialog2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.shj.setting.generator.CargoGridNote.3
                    AnonymousClass3() {
                    }

                    @Override // android.content.DialogInterface.OnDismissListener
                    public void onDismiss(DialogInterface dialogInterface) {
                        EventBus.getDefault().post(new ShowShelfErrorTipEvent());
                    }
                });
                this.mutilTextTipDialog.show();
                ShjGoodsSetting shjGoodsSetting2 = new ShjGoodsSetting();
                shjGoodsSetting2.count = i5;
                shjGoodsSetting2.onShjGoodsSetResultListener = new OnShjGoodsSetResultListener() { // from class: com.shj.setting.generator.CargoGridNote.4
                    final /* synthetic */ List val$inputData;

                    AnonymousClass4(List inputData2) {
                        inputData = inputData2;
                    }

                    @Override // com.shj.OnShjGoodsSetResultListener
                    public void result(int i7, Object obj, boolean z4) {
                        float intValue = ((Integer) obj).intValue() / 100.0f;
                        int i8 = i7 % 1000;
                        CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.layer_number) + ":" + i8 + "  " + CargoGridNote.this.context.getString(R.string.lab_price) + ":" + intValue);
                        CargoGridView cargoGridView = CargoGridNote.this.cargoGridView;
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(intValue);
                        cargoGridView.setBeforeSettingText(i8, sb.toString());
                        if (z4) {
                            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                            EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, inputData));
                        }
                    }
                };
                Shj.addGoodsSetCommand(18, shjGoodsSetting2);
                for (CargoGridView.IndexData indexData4 : arrayList2) {
                    ShjManager.setShelfGoodsPrice(indexData4.identifier + 1000, Math.round(Float.parseFloat(indexData4.inputText) * 100.0f));
                }
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.lab_price));
            return;
        }
        if (i == 131) {
            ArrayList<CargoGridView.IndexData> arrayList3 = new ArrayList();
            Iterator<CargoGridView.IndexData> it = inputData2.iterator();
            int i7 = 0;
            while (true) {
                if (!it.hasNext()) {
                    z2 = false;
                    break;
                }
                CargoGridView.IndexData next = it.next();
                int i8 = next.identifier;
                if (!TextUtils.isEmpty(next.inputText)) {
                    int intValue = Integer.valueOf(next.inputText).intValue();
                    if (intValue > 255) {
                        z2 = true;
                        break;
                    }
                    if (!TextUtils.isEmpty(next.beforeSettingText)) {
                        if (intValue != Integer.valueOf(next.beforeSettingText).intValue()) {
                            i7++;
                            Loger.writeLog("SET", "设置 货道号=" + i8 + "库存=" + intValue);
                            arrayList3.add(next);
                        }
                    } else {
                        i7++;
                        Loger.writeLog("SET", "设置 货道号=" + i8 + "库存=" + intValue);
                        arrayList3.add(next);
                    }
                }
            }
            if (z2) {
                ToastUitl.showShort(this.context, this.context.getString(R.string.input_value_should_not_greater_255));
                return;
            }
            if (i7 > 0) {
                MutilTextTipDialog mutilTextTipDialog3 = new MutilTextTipDialog(this.context);
                this.mutilTextTipDialog = mutilTextTipDialog3;
                mutilTextTipDialog3.show();
                ShjGoodsSetting shjGoodsSetting3 = new ShjGoodsSetting();
                shjGoodsSetting3.count = i7;
                shjGoodsSetting3.onShjGoodsSetResultListener = new OnShjGoodsSetResultListener() { // from class: com.shj.setting.generator.CargoGridNote.5
                    AnonymousClass5() {
                    }

                    @Override // com.shj.OnShjGoodsSetResultListener
                    public void result(int i9, Object obj, boolean z4) {
                        String string = CargoGridNote.this.context.getString(R.string.lab_shelf);
                        String string2 = CargoGridNote.this.context.getString(R.string.lab_stock);
                        CargoGridNote.this.mutilTextTipDialog.addTextShow(string + ":" + String.format("%03d", Integer.valueOf(i9)) + "  " + string2 + ":" + obj);
                        CargoGridNote.this.cargoGridView.setBeforeSettingText(i9, String.valueOf(obj));
                        if (z4) {
                            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                        }
                    }
                };
                Shj.addGoodsSetCommand(19, shjGoodsSetting3);
                for (CargoGridView.IndexData indexData5 : arrayList3) {
                    ShjManager.setShelfGoodsCount(indexData5.identifier, Integer.valueOf(indexData5.inputText).intValue());
                }
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.lab_stock));
            return;
        }
        if (i == 132) {
            ArrayList<CargoGridView.IndexData> arrayList4 = new ArrayList();
            Iterator<CargoGridView.IndexData> it2 = inputData2.iterator();
            int i9 = 0;
            while (true) {
                if (!it2.hasNext()) {
                    z3 = false;
                    break;
                }
                CargoGridView.IndexData next2 = it2.next();
                int i10 = next2.identifier;
                if (!TextUtils.isEmpty(next2.inputText)) {
                    int intValue2 = Integer.valueOf(next2.inputText).intValue();
                    if (intValue2 > 255) {
                        z3 = true;
                        break;
                    }
                    if (TextUtils.isEmpty(next2.beforeSettingText)) {
                        next2.needUpdate = true;
                        i9++;
                        Loger.writeLog("SET", "设置 货道号=" + (i10 + 1000) + "库存=" + intValue2);
                        arrayList4.add(next2);
                    } else if (intValue2 != Integer.valueOf(next2.beforeSettingText).intValue()) {
                        next2.needUpdate = true;
                        i9++;
                        Loger.writeLog("SET", "设置 货道号=" + (i10 + 1000) + "库存=" + intValue2);
                        arrayList4.add(next2);
                    }
                }
            }
            if (z3) {
                ToastUitl.showShort(this.context, this.context.getString(R.string.input_value_should_not_greater_255));
                return;
            }
            if (i9 > 0) {
                MutilTextTipDialog mutilTextTipDialog4 = new MutilTextTipDialog(this.context);
                this.mutilTextTipDialog = mutilTextTipDialog4;
                mutilTextTipDialog4.show();
                ShjGoodsSetting shjGoodsSetting4 = new ShjGoodsSetting();
                shjGoodsSetting4.count = i9;
                shjGoodsSetting4.onShjGoodsSetResultListener = new OnShjGoodsSetResultListener() { // from class: com.shj.setting.generator.CargoGridNote.6
                    final /* synthetic */ List val$inputData;

                    AnonymousClass6(List inputData2) {
                        inputData = inputData2;
                    }

                    @Override // com.shj.OnShjGoodsSetResultListener
                    public void result(int i11, Object obj, boolean z4) {
                        String string = CargoGridNote.this.context.getString(R.string.layer_number);
                        String string2 = CargoGridNote.this.context.getString(R.string.lab_stock);
                        StringBuilder sb = new StringBuilder();
                        sb.append(string);
                        sb.append(":");
                        int i12 = i11 % 1000;
                        sb.append(i12);
                        sb.append("  ");
                        sb.append(string2);
                        sb.append(":");
                        sb.append(obj);
                        CargoGridNote.this.mutilTextTipDialog.addTextShow(sb.toString());
                        CargoGridNote.this.cargoGridView.setBeforeSettingText(i12, String.valueOf(obj));
                        if (z4) {
                            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                            EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, inputData));
                        }
                    }
                };
                Shj.addGoodsSetCommand(19, shjGoodsSetting4);
                for (CargoGridView.IndexData indexData6 : arrayList4) {
                    ShjManager.setShelfGoodsCount(indexData6.identifier + 1000, Integer.valueOf(indexData6.inputText).intValue());
                }
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.lab_stock));
            return;
        }
        if (i == 136) {
            ArrayList<CargoGridView.IndexData> arrayList5 = new ArrayList();
            Iterator<CargoGridView.IndexData> it3 = inputData2.iterator();
            int i11 = 0;
            while (true) {
                if (!it3.hasNext()) {
                    break;
                }
                CargoGridView.IndexData next3 = it3.next();
                int i12 = next3.identifier;
                if (!TextUtils.isEmpty(next3.inputText)) {
                    int intValue3 = Integer.valueOf(next3.inputText).intValue();
                    if (intValue3 > 255) {
                        i2 = 1;
                        break;
                    } else if (!TextUtils.isEmpty(next3.beforeSettingText)) {
                        if (intValue3 != Integer.valueOf(next3.beforeSettingText).intValue()) {
                            i11++;
                            arrayList5.add(next3);
                        }
                    } else {
                        i11++;
                        arrayList5.add(next3);
                    }
                }
            }
            if (i2 != 0) {
                ToastUitl.showShort(this.context, this.context.getString(R.string.input_value_should_not_greater_255));
                return;
            }
            if (i11 > 0) {
                MutilTextTipDialog mutilTextTipDialog5 = new MutilTextTipDialog(this.context);
                this.mutilTextTipDialog = mutilTextTipDialog5;
                mutilTextTipDialog5.show();
                ShjGoodsSetting shjGoodsSetting5 = new ShjGoodsSetting();
                shjGoodsSetting5.count = i11;
                shjGoodsSetting5.onShjGoodsSetResultListener = new OnShjGoodsSetResultListener() { // from class: com.shj.setting.generator.CargoGridNote.7
                    AnonymousClass7() {
                    }

                    @Override // com.shj.OnShjGoodsSetResultListener
                    public void result(int i13, Object obj, boolean z4) {
                        String string = CargoGridNote.this.context.getString(R.string.lab_shelf);
                        String string2 = CargoGridNote.this.context.getString(R.string.lab_capacity);
                        CargoGridNote.this.mutilTextTipDialog.addTextShow(string + ":" + String.format("%03d", Integer.valueOf(i13)) + "  " + string2 + ":" + obj);
                        CargoGridNote.this.cargoGridView.setBeforeSettingText(i13, String.valueOf(obj));
                        if (z4) {
                            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                        }
                    }
                };
                Shj.addGoodsSetCommand(20, shjGoodsSetting5);
                for (CargoGridView.IndexData indexData7 : arrayList5) {
                    ShjManager.setShelfInventory(indexData7.identifier, Integer.valueOf(indexData7.inputText).intValue());
                }
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.lab_capacity));
            return;
        }
        if (i == 137) {
            ArrayList<CargoGridView.IndexData> arrayList6 = new ArrayList();
            int i13 = 0;
            for (CargoGridView.IndexData indexData8 : inputData2) {
                int i14 = indexData8.identifier;
                if (!TextUtils.isEmpty(indexData8.inputText)) {
                    int intValue4 = Integer.valueOf(indexData8.inputText).intValue();
                    if (intValue4 > 255) {
                        i2 = 1;
                    } else if (TextUtils.isEmpty(indexData8.beforeSettingText)) {
                        indexData8.needUpdate = true;
                        i13++;
                        arrayList6.add(indexData8);
                    } else if (intValue4 != Integer.valueOf(indexData8.beforeSettingText).intValue()) {
                        indexData8.needUpdate = true;
                        i13++;
                        arrayList6.add(indexData8);
                    }
                }
            }
            if (i2 != 0) {
                ToastUitl.showShort(this.context, this.context.getString(R.string.input_value_should_not_greater_255));
                return;
            }
            if (i13 > 0) {
                MutilTextTipDialog mutilTextTipDialog6 = new MutilTextTipDialog(this.context);
                this.mutilTextTipDialog = mutilTextTipDialog6;
                mutilTextTipDialog6.show();
                ShjGoodsSetting shjGoodsSetting6 = new ShjGoodsSetting();
                shjGoodsSetting6.count = i13;
                shjGoodsSetting6.onShjGoodsSetResultListener = new OnShjGoodsSetResultListener() { // from class: com.shj.setting.generator.CargoGridNote.8
                    final /* synthetic */ List val$inputData;

                    AnonymousClass8(List inputData2) {
                        inputData = inputData2;
                    }

                    @Override // com.shj.OnShjGoodsSetResultListener
                    public void result(int i15, Object obj, boolean z4) {
                        String string = CargoGridNote.this.context.getString(R.string.layer_number);
                        String string2 = CargoGridNote.this.context.getString(R.string.lab_capacity);
                        StringBuilder sb = new StringBuilder();
                        sb.append(string);
                        sb.append(":");
                        int i16 = i15 % 1000;
                        sb.append(i16);
                        sb.append("  ");
                        sb.append(string2);
                        sb.append(":");
                        sb.append(obj);
                        CargoGridNote.this.mutilTextTipDialog.addTextShow(sb.toString());
                        CargoGridNote.this.cargoGridView.setBeforeSettingText(i16, String.valueOf(obj));
                        if (z4) {
                            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                            EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, inputData));
                        }
                    }
                };
                Shj.addGoodsSetCommand(20, shjGoodsSetting6);
                for (CargoGridView.IndexData indexData9 : arrayList6) {
                    ShjManager.setShelfInventory(indexData9.identifier + 1000, Integer.valueOf(indexData9.inputText).intValue());
                }
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.lab_capacity));
            return;
        }
        if (i == 140) {
            ArrayList<CargoGridView.IndexData> arrayList7 = new ArrayList();
            for (CargoGridView.IndexData indexData10 : inputData2) {
                int i15 = indexData10.identifier;
                if (!TextUtils.isEmpty(indexData10.inputText)) {
                    int intValue5 = Integer.valueOf(indexData10.inputText).intValue();
                    if (TextUtils.isEmpty(indexData10.beforeSettingText)) {
                        i2++;
                        Loger.writeLog("SET", "设置 货道号=" + i15 + "编码=" + intValue5);
                        arrayList7.add(indexData10);
                    } else if (intValue5 != Integer.valueOf(indexData10.beforeSettingText).intValue()) {
                        i2++;
                        Loger.writeLog("SET", "设置 货道号=" + i15 + "编码=" + intValue5);
                        arrayList7.add(indexData10);
                    }
                }
            }
            if (i2 > 0) {
                MutilTextTipDialog mutilTextTipDialog7 = new MutilTextTipDialog(this.context);
                this.mutilTextTipDialog = mutilTextTipDialog7;
                mutilTextTipDialog7.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.shj.setting.generator.CargoGridNote.9
                    AnonymousClass9() {
                    }

                    @Override // android.content.DialogInterface.OnDismissListener
                    public void onDismiss(DialogInterface dialogInterface) {
                        EventBus.getDefault().post(new ShowShelfErrorTipEvent());
                    }
                });
                this.mutilTextTipDialog.show();
                ShjGoodsSetting shjGoodsSetting7 = new ShjGoodsSetting();
                shjGoodsSetting7.count = i2;
                shjGoodsSetting7.onShjGoodsSetResultListener = new OnShjGoodsSetResultListener() { // from class: com.shj.setting.generator.CargoGridNote.10
                    AnonymousClass10() {
                    }

                    @Override // com.shj.OnShjGoodsSetResultListener
                    public void result(int i16, Object obj, boolean z4) {
                        CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.lab_shelf) + ":" + i16 + "  " + CargoGridNote.this.context.getString(R.string.code) + ":" + obj);
                        CargoGridNote.this.cargoGridView.setBeforeSettingText(i16, String.valueOf(obj));
                        if (z4) {
                            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                        }
                    }
                };
                Shj.addGoodsSetCommand(21, shjGoodsSetting7);
                for (CargoGridView.IndexData indexData11 : arrayList7) {
                    ShjManager.setShelfGoodsCode(indexData11.identifier, indexData11.inputText);
                }
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.cargo_code));
            return;
        }
        if (i == 268) {
            ArrayList<CargoGridView.IndexData> arrayList8 = new ArrayList();
            for (CargoGridView.IndexData indexData12 : inputData2) {
                int i16 = indexData12.identifier;
                if (!TextUtils.isEmpty(indexData12.inputText)) {
                    int intValue6 = Integer.valueOf(indexData12.inputText).intValue();
                    if (TextUtils.isEmpty(indexData12.beforeSettingText)) {
                        indexData12.needUpdate = true;
                        i2++;
                        Loger.writeLog("SET", "设置 货道号=" + (i16 + 1000) + "编码=" + intValue6);
                        arrayList8.add(indexData12);
                    } else if (intValue6 != Integer.valueOf(indexData12.beforeSettingText).intValue()) {
                        indexData12.needUpdate = true;
                        i2++;
                        Loger.writeLog("SET", "设置 货道号=" + (i16 + 1000) + "编码=" + intValue6);
                        arrayList8.add(indexData12);
                    }
                }
            }
            if (i2 > 0) {
                MutilTextTipDialog mutilTextTipDialog8 = new MutilTextTipDialog(this.context);
                this.mutilTextTipDialog = mutilTextTipDialog8;
                mutilTextTipDialog8.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.shj.setting.generator.CargoGridNote.11
                    AnonymousClass11() {
                    }

                    @Override // android.content.DialogInterface.OnDismissListener
                    public void onDismiss(DialogInterface dialogInterface) {
                        EventBus.getDefault().post(new ShowShelfErrorTipEvent());
                    }
                });
                this.mutilTextTipDialog.show();
                ShjGoodsSetting shjGoodsSetting8 = new ShjGoodsSetting();
                shjGoodsSetting8.count = i2;
                shjGoodsSetting8.onShjGoodsSetResultListener = new OnShjGoodsSetResultListener() { // from class: com.shj.setting.generator.CargoGridNote.12
                    final /* synthetic */ List val$inputData;

                    AnonymousClass12(List inputData2) {
                        inputData = inputData2;
                    }

                    @Override // com.shj.OnShjGoodsSetResultListener
                    public void result(int i17, Object obj, boolean z4) {
                        String string = CargoGridNote.this.context.getString(R.string.layer_number);
                        String string2 = CargoGridNote.this.context.getString(R.string.code);
                        StringBuilder sb = new StringBuilder();
                        sb.append(string);
                        sb.append(":");
                        int i18 = i17 % 1000;
                        sb.append(i18);
                        sb.append("  ");
                        sb.append(string2);
                        sb.append(":");
                        sb.append(obj);
                        CargoGridNote.this.mutilTextTipDialog.addTextShow(sb.toString());
                        CargoGridNote.this.cargoGridView.setBeforeSettingText(i18, String.valueOf(obj));
                        if (z4) {
                            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                            EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, inputData));
                        }
                    }
                };
                Shj.addGoodsSetCommand(21, shjGoodsSetting8);
                for (CargoGridView.IndexData indexData13 : arrayList8) {
                    ShjManager.setShelfGoodsCode(indexData13.identifier + 1000, indexData13.inputText);
                }
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.cargo_code));
            return;
        }
        if (i == 274) {
            ArrayList arrayList9 = new ArrayList();
            for (CargoGridView.IndexData indexData14 : inputData2) {
                int i17 = indexData14.identifier;
                if (!TextUtils.isEmpty(indexData14.inputText)) {
                    int intValue7 = Integer.valueOf(indexData14.inputText).intValue();
                    if (!TextUtils.isEmpty(indexData14.beforeSettingText)) {
                        if (intValue7 != Integer.valueOf(indexData14.beforeSettingText).intValue()) {
                            arrayList9.add(indexData14);
                        }
                    } else {
                        arrayList9.add(indexData14);
                    }
                }
            }
            if (arrayList9.size() > 0) {
                setLockTime(arrayList9, 0, true);
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.time));
            return;
        }
        if (i == 275) {
            ArrayList arrayList10 = new ArrayList();
            for (CargoGridView.IndexData indexData15 : inputData2) {
                int i18 = indexData15.identifier;
                if (!TextUtils.isEmpty(indexData15.inputText)) {
                    int intValue8 = Integer.valueOf(indexData15.inputText).intValue();
                    if (!TextUtils.isEmpty(indexData15.beforeSettingText)) {
                        if (intValue8 != Integer.valueOf(indexData15.beforeSettingText).intValue()) {
                            arrayList10.add(indexData15);
                        }
                    } else {
                        arrayList10.add(indexData15);
                    }
                }
            }
            if (arrayList10.size() > 0) {
                setLockTime(arrayList10, 0, false);
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.time));
            return;
        }
        if (i == 329) {
            ArrayList arrayList11 = new ArrayList();
            for (CargoGridView.IndexData indexData16 : inputData2) {
                int i19 = indexData16.identifier;
                if (!TextUtils.isEmpty(indexData16.inputText)) {
                    int intValue9 = Integer.valueOf(indexData16.inputText).intValue();
                    if (!TextUtils.isEmpty(indexData16.beforeSettingText)) {
                        if (intValue9 != Integer.valueOf(indexData16.beforeSettingText).intValue()) {
                            arrayList11.add(indexData16);
                        }
                    } else {
                        arrayList11.add(indexData16);
                    }
                }
            }
            if (arrayList11.size() > 0) {
                setLinkageSynTime(arrayList11, 0, true);
                return;
            }
            ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.time));
            return;
        }
        if (i != 330) {
            return;
        }
        ArrayList arrayList12 = new ArrayList();
        for (CargoGridView.IndexData indexData17 : inputData2) {
            int i20 = indexData17.identifier;
            if (!TextUtils.isEmpty(indexData17.inputText)) {
                int intValue10 = Integer.valueOf(indexData17.inputText).intValue();
                if (!TextUtils.isEmpty(indexData17.beforeSettingText)) {
                    if (intValue10 != Integer.valueOf(indexData17.beforeSettingText).intValue()) {
                        arrayList12.add(indexData17);
                    }
                } else {
                    arrayList12.add(indexData17);
                }
            }
        }
        if (arrayList12.size() > 0) {
            setLinkageSynTime(arrayList12, 0, false);
            return;
        }
        ToastUitl.showShort(this.context, this.context.getString(R.string.please_amend) + this.context.getString(R.string.time));
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements DialogInterface.OnDismissListener {
        AnonymousClass1() {
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            EventBus.getDefault().post(new ShowShelfErrorTipEvent());
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements OnShjGoodsSetResultListener {
        AnonymousClass2() {
        }

        @Override // com.shj.OnShjGoodsSetResultListener
        public void result(int i5, Object obj, boolean z4) {
            String string = CargoGridNote.this.context.getString(R.string.lab_shelf);
            String string2 = CargoGridNote.this.context.getString(R.string.lab_price);
            float intValue = ((Integer) obj).intValue() / 100.0f;
            CargoGridNote.this.mutilTextTipDialog.addTextShow(string + ":" + String.format("%03d", Integer.valueOf(i5)) + "  " + string2 + ":" + intValue);
            CargoGridNote.this.cargoGridView.setBeforeSettingText(i5, String.valueOf(intValue));
            if (z4) {
                CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
            }
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 implements DialogInterface.OnDismissListener {
        AnonymousClass3() {
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            EventBus.getDefault().post(new ShowShelfErrorTipEvent());
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 implements OnShjGoodsSetResultListener {
        final /* synthetic */ List val$inputData;

        AnonymousClass4(List inputData2) {
            inputData = inputData2;
        }

        @Override // com.shj.OnShjGoodsSetResultListener
        public void result(int i7, Object obj, boolean z4) {
            float intValue = ((Integer) obj).intValue() / 100.0f;
            int i8 = i7 % 1000;
            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.layer_number) + ":" + i8 + "  " + CargoGridNote.this.context.getString(R.string.lab_price) + ":" + intValue);
            CargoGridView cargoGridView = CargoGridNote.this.cargoGridView;
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(intValue);
            cargoGridView.setBeforeSettingText(i8, sb.toString());
            if (z4) {
                CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, inputData));
            }
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$5 */
    /* loaded from: classes2.dex */
    class AnonymousClass5 implements OnShjGoodsSetResultListener {
        AnonymousClass5() {
        }

        @Override // com.shj.OnShjGoodsSetResultListener
        public void result(int i9, Object obj, boolean z4) {
            String string = CargoGridNote.this.context.getString(R.string.lab_shelf);
            String string2 = CargoGridNote.this.context.getString(R.string.lab_stock);
            CargoGridNote.this.mutilTextTipDialog.addTextShow(string + ":" + String.format("%03d", Integer.valueOf(i9)) + "  " + string2 + ":" + obj);
            CargoGridNote.this.cargoGridView.setBeforeSettingText(i9, String.valueOf(obj));
            if (z4) {
                CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
            }
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$6 */
    /* loaded from: classes2.dex */
    class AnonymousClass6 implements OnShjGoodsSetResultListener {
        final /* synthetic */ List val$inputData;

        AnonymousClass6(List inputData2) {
            inputData = inputData2;
        }

        @Override // com.shj.OnShjGoodsSetResultListener
        public void result(int i11, Object obj, boolean z4) {
            String string = CargoGridNote.this.context.getString(R.string.layer_number);
            String string2 = CargoGridNote.this.context.getString(R.string.lab_stock);
            StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(":");
            int i12 = i11 % 1000;
            sb.append(i12);
            sb.append("  ");
            sb.append(string2);
            sb.append(":");
            sb.append(obj);
            CargoGridNote.this.mutilTextTipDialog.addTextShow(sb.toString());
            CargoGridNote.this.cargoGridView.setBeforeSettingText(i12, String.valueOf(obj));
            if (z4) {
                CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, inputData));
            }
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$7 */
    /* loaded from: classes2.dex */
    class AnonymousClass7 implements OnShjGoodsSetResultListener {
        AnonymousClass7() {
        }

        @Override // com.shj.OnShjGoodsSetResultListener
        public void result(int i13, Object obj, boolean z4) {
            String string = CargoGridNote.this.context.getString(R.string.lab_shelf);
            String string2 = CargoGridNote.this.context.getString(R.string.lab_capacity);
            CargoGridNote.this.mutilTextTipDialog.addTextShow(string + ":" + String.format("%03d", Integer.valueOf(i13)) + "  " + string2 + ":" + obj);
            CargoGridNote.this.cargoGridView.setBeforeSettingText(i13, String.valueOf(obj));
            if (z4) {
                CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
            }
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$8 */
    /* loaded from: classes2.dex */
    class AnonymousClass8 implements OnShjGoodsSetResultListener {
        final /* synthetic */ List val$inputData;

        AnonymousClass8(List inputData2) {
            inputData = inputData2;
        }

        @Override // com.shj.OnShjGoodsSetResultListener
        public void result(int i15, Object obj, boolean z4) {
            String string = CargoGridNote.this.context.getString(R.string.layer_number);
            String string2 = CargoGridNote.this.context.getString(R.string.lab_capacity);
            StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(":");
            int i16 = i15 % 1000;
            sb.append(i16);
            sb.append("  ");
            sb.append(string2);
            sb.append(":");
            sb.append(obj);
            CargoGridNote.this.mutilTextTipDialog.addTextShow(sb.toString());
            CargoGridNote.this.cargoGridView.setBeforeSettingText(i16, String.valueOf(obj));
            if (z4) {
                CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, inputData));
            }
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$9 */
    /* loaded from: classes2.dex */
    class AnonymousClass9 implements DialogInterface.OnDismissListener {
        AnonymousClass9() {
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            EventBus.getDefault().post(new ShowShelfErrorTipEvent());
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$10 */
    /* loaded from: classes2.dex */
    class AnonymousClass10 implements OnShjGoodsSetResultListener {
        AnonymousClass10() {
        }

        @Override // com.shj.OnShjGoodsSetResultListener
        public void result(int i16, Object obj, boolean z4) {
            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.lab_shelf) + ":" + i16 + "  " + CargoGridNote.this.context.getString(R.string.code) + ":" + obj);
            CargoGridNote.this.cargoGridView.setBeforeSettingText(i16, String.valueOf(obj));
            if (z4) {
                CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
            }
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$11 */
    /* loaded from: classes2.dex */
    class AnonymousClass11 implements DialogInterface.OnDismissListener {
        AnonymousClass11() {
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            EventBus.getDefault().post(new ShowShelfErrorTipEvent());
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$12 */
    /* loaded from: classes2.dex */
    class AnonymousClass12 implements OnShjGoodsSetResultListener {
        final /* synthetic */ List val$inputData;

        AnonymousClass12(List inputData2) {
            inputData = inputData2;
        }

        @Override // com.shj.OnShjGoodsSetResultListener
        public void result(int i17, Object obj, boolean z4) {
            String string = CargoGridNote.this.context.getString(R.string.layer_number);
            String string2 = CargoGridNote.this.context.getString(R.string.code);
            StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(":");
            int i18 = i17 % 1000;
            sb.append(i18);
            sb.append("  ");
            sb.append(string2);
            sb.append(":");
            sb.append(obj);
            CargoGridNote.this.mutilTextTipDialog.addTextShow(sb.toString());
            CargoGridNote.this.cargoGridView.setBeforeSettingText(i18, String.valueOf(obj));
            if (z4) {
                CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.save_success));
                EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, inputData));
            }
        }
    }

    public void setLockTime(List<CargoGridView.IndexData> list, int i, boolean z) {
        if (i < list.size()) {
            CargoGridView.IndexData indexData = list.get(i);
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            if (!TextUtils.isEmpty(indexData.inputText)) {
                this.cargoGridView.setBeforeSettingText(indexData.identifier, indexData.inputText);
                if (z) {
                    commandV2_Up_SetCommand.setElectromagneticLockTime(true, indexData.identifier, Integer.valueOf(indexData.inputText).intValue());
                } else {
                    commandV2_Up_SetCommand.setElectromagneticLockTime(true, indexData.identifier + 2000, Integer.valueOf(indexData.inputText).intValue());
                }
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.CargoGridNote.13
                    final /* synthetic */ CargoGridView.IndexData val$data;
                    final /* synthetic */ int val$index;
                    final /* synthetic */ boolean val$isSingle;
                    final /* synthetic */ List val$lockTimeList;

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass13(boolean z2, CargoGridView.IndexData indexData2, int i2, List list2) {
                        z = z2;
                        indexData = indexData2;
                        i = i2;
                        list = list2;
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        String str;
                        String str2;
                        if (CargoGridNote.this.mutilTextTipDialog == null) {
                            CargoGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(CargoGridNote.this.context);
                            CargoGridNote.this.mutilTextTipDialog.show();
                        } else {
                            CargoGridNote.this.mutilTextTipDialog.show();
                        }
                        if (z) {
                            str = ("" + CargoGridNote.this.context.getString(R.string.lab_shelf)) + StringUtils.SPACE + String.format("%03d", Integer.valueOf(indexData.identifier));
                        } else {
                            str = ("" + CargoGridNote.this.context.getString(R.string.layer_number)) + StringUtils.SPACE + indexData.identifier;
                        }
                        String str3 = str + "  " + CargoGridNote.this.context.getString(R.string.electromagnetic_lock_on_time) + StringUtils.SPACE + indexData.inputText;
                        if (z2) {
                            str2 = str3 + StringUtils.SPACE + CargoGridNote.this.context.getString(R.string.setting_success);
                        } else {
                            str2 = str3 + StringUtils.SPACE + CargoGridNote.this.context.getString(R.string.setting_fail);
                        }
                        CargoGridNote.this.mutilTextTipDialog.addTextShow(str2);
                        if (i + 1 < list.size()) {
                            CargoGridNote.this.setLockTime(list, i + 1, z);
                        } else {
                            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.setting_compelete));
                        }
                        if (z || !z2) {
                            return;
                        }
                        indexData.needUpdate = true;
                        EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, indexData));
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
            setLockTime(list2, i2, z2);
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$13 */
    /* loaded from: classes2.dex */
    public class AnonymousClass13 implements OnCommandAnswerListener {
        final /* synthetic */ CargoGridView.IndexData val$data;
        final /* synthetic */ int val$index;
        final /* synthetic */ boolean val$isSingle;
        final /* synthetic */ List val$lockTimeList;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass13(boolean z2, CargoGridView.IndexData indexData2, int i2, List list2) {
            z = z2;
            indexData = indexData2;
            i = i2;
            list = list2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            String str;
            String str2;
            if (CargoGridNote.this.mutilTextTipDialog == null) {
                CargoGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(CargoGridNote.this.context);
                CargoGridNote.this.mutilTextTipDialog.show();
            } else {
                CargoGridNote.this.mutilTextTipDialog.show();
            }
            if (z) {
                str = ("" + CargoGridNote.this.context.getString(R.string.lab_shelf)) + StringUtils.SPACE + String.format("%03d", Integer.valueOf(indexData.identifier));
            } else {
                str = ("" + CargoGridNote.this.context.getString(R.string.layer_number)) + StringUtils.SPACE + indexData.identifier;
            }
            String str3 = str + "  " + CargoGridNote.this.context.getString(R.string.electromagnetic_lock_on_time) + StringUtils.SPACE + indexData.inputText;
            if (z2) {
                str2 = str3 + StringUtils.SPACE + CargoGridNote.this.context.getString(R.string.setting_success);
            } else {
                str2 = str3 + StringUtils.SPACE + CargoGridNote.this.context.getString(R.string.setting_fail);
            }
            CargoGridNote.this.mutilTextTipDialog.addTextShow(str2);
            if (i + 1 < list.size()) {
                CargoGridNote.this.setLockTime(list, i + 1, z);
            } else {
                CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.setting_compelete));
            }
            if (z || !z2) {
                return;
            }
            indexData.needUpdate = true;
            EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, indexData));
        }
    }

    public void setLinkageSynTime(List<CargoGridView.IndexData> list, int i, boolean z) {
        if (i < list.size()) {
            CargoGridView.IndexData indexData = list.get(i);
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            if (!TextUtils.isEmpty(indexData.inputText)) {
                this.cargoGridView.setBeforeSettingText(indexData.identifier, indexData.inputText);
                if (z) {
                    commandV2_Up_SetCommand.setNewMergeShelSynRunTime(true, indexData.identifier, Integer.valueOf(indexData.inputText).intValue());
                } else {
                    commandV2_Up_SetCommand.setNewMergeShelSynRunTime(true, indexData.identifier + 2000, Integer.valueOf(indexData.inputText).intValue());
                }
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.CargoGridNote.14
                    final /* synthetic */ CargoGridView.IndexData val$data;
                    final /* synthetic */ int val$index;
                    final /* synthetic */ boolean val$isSingle;
                    final /* synthetic */ List val$lockTimeList;

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    AnonymousClass14(boolean z2, CargoGridView.IndexData indexData2, int i2, List list2) {
                        z = z2;
                        indexData = indexData2;
                        i = i2;
                        list = list2;
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                        String str;
                        String str2;
                        if (CargoGridNote.this.mutilTextTipDialog == null) {
                            CargoGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(CargoGridNote.this.context);
                            CargoGridNote.this.mutilTextTipDialog.show();
                        } else {
                            CargoGridNote.this.mutilTextTipDialog.show();
                        }
                        if (z) {
                            str = ("" + CargoGridNote.this.context.getString(R.string.lab_shelf)) + StringUtils.SPACE + String.format("%03d", Integer.valueOf(indexData.identifier));
                        } else {
                            str = ("" + CargoGridNote.this.context.getString(R.string.layer_number)) + StringUtils.SPACE + indexData.identifier;
                        }
                        String str3 = str + "  " + CargoGridNote.this.context.getString(R.string.linkage_synchronization_time) + StringUtils.SPACE + indexData.inputText;
                        if (z2) {
                            str2 = str3 + StringUtils.SPACE + CargoGridNote.this.context.getString(R.string.setting_success);
                        } else {
                            str2 = str3 + StringUtils.SPACE + CargoGridNote.this.context.getString(R.string.setting_fail);
                        }
                        CargoGridNote.this.mutilTextTipDialog.addTextShow(str2);
                        if (i + 1 < list.size()) {
                            CargoGridNote.this.setLinkageSynTime(list, i + 1, z);
                        } else {
                            CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.setting_compelete));
                        }
                        if (z || !z2) {
                            return;
                        }
                        indexData.needUpdate = true;
                        EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, indexData));
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
            setLinkageSynTime(list2, i2, z2);
        }
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$14 */
    /* loaded from: classes2.dex */
    public class AnonymousClass14 implements OnCommandAnswerListener {
        final /* synthetic */ CargoGridView.IndexData val$data;
        final /* synthetic */ int val$index;
        final /* synthetic */ boolean val$isSingle;
        final /* synthetic */ List val$lockTimeList;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass14(boolean z2, CargoGridView.IndexData indexData2, int i2, List list2) {
            z = z2;
            indexData = indexData2;
            i = i2;
            list = list2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            String str;
            String str2;
            if (CargoGridNote.this.mutilTextTipDialog == null) {
                CargoGridNote.this.mutilTextTipDialog = new MutilTextTipDialog(CargoGridNote.this.context);
                CargoGridNote.this.mutilTextTipDialog.show();
            } else {
                CargoGridNote.this.mutilTextTipDialog.show();
            }
            if (z) {
                str = ("" + CargoGridNote.this.context.getString(R.string.lab_shelf)) + StringUtils.SPACE + String.format("%03d", Integer.valueOf(indexData.identifier));
            } else {
                str = ("" + CargoGridNote.this.context.getString(R.string.layer_number)) + StringUtils.SPACE + indexData.identifier;
            }
            String str3 = str + "  " + CargoGridNote.this.context.getString(R.string.linkage_synchronization_time) + StringUtils.SPACE + indexData.inputText;
            if (z2) {
                str2 = str3 + StringUtils.SPACE + CargoGridNote.this.context.getString(R.string.setting_success);
            } else {
                str2 = str3 + StringUtils.SPACE + CargoGridNote.this.context.getString(R.string.setting_fail);
            }
            CargoGridNote.this.mutilTextTipDialog.addTextShow(str2);
            if (i + 1 < list.size()) {
                CargoGridNote.this.setLinkageSynTime(list, i + 1, z);
            } else {
                CargoGridNote.this.mutilTextTipDialog.addTextShow(CargoGridNote.this.context.getString(R.string.setting_compelete));
            }
            if (z || !z2) {
                return;
            }
            indexData.needUpdate = true;
            EventBus.getDefault().post(new SettingTypeEvent(CargoGridNote.this.settingType, indexData));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x003d, code lost:
    
        if (r0 != 330) goto L226;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.shj.setting.widget.CargoGridView.GridItemData getGridItemData() {
        /*
            Method dump skipped, instructions count: 1355
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.setting.generator.CargoGridNote.getGridItemData():com.shj.setting.widget.CargoGridView$GridItemData");
    }

    @Override // com.shj.setting.generator.SettingNote
    public void clearData() {
        int i = this.settingType;
        if (i == 274) {
            LoadingDialog loadingDialog = new LoadingDialog(this.context);
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            readLockTime(0, true);
            return;
        }
        if (i == 275) {
            LoadingDialog loadingDialog2 = new LoadingDialog(this.context);
            this.loadingDialog = loadingDialog2;
            loadingDialog2.show();
            readLockTime(0, false);
            return;
        }
        if (i == 329) {
            LoadingDialog loadingDialog3 = new LoadingDialog(this.context);
            this.loadingDialog = loadingDialog3;
            loadingDialog3.show();
            readLinkageTime(0, true);
            return;
        }
        if (i != 330) {
            return;
        }
        LoadingDialog loadingDialog4 = new LoadingDialog(this.context);
        this.loadingDialog = loadingDialog4;
        loadingDialog4.show();
        readLinkageTime(0, false);
    }

    public void readLockTime(int i, boolean z) {
        CargoGridView.GridItemData gridItemData = this.gridItemData;
        if (gridItemData == null) {
            return;
        }
        if (i < gridItemData.indexDataList.size()) {
            CargoGridView.IndexData indexData = this.gridItemData.indexDataList.get(i);
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            if (z) {
                commandV2_Up_SetCommand.setElectromagneticLockTime(false, indexData.identifier, 0);
            } else {
                commandV2_Up_SetCommand.setElectromagneticLockTime(false, indexData.identifier + 2000, 0);
            }
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.CargoGridNote.15
                final /* synthetic */ int val$index;
                final /* synthetic */ boolean val$isSigle;
                final /* synthetic */ CargoGridView.IndexData val$timeItemData;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                }

                AnonymousClass15(CargoGridView.IndexData indexData2, int i2, boolean z2) {
                    indexData = indexData2;
                    i = i2;
                    z = z2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                            CargoGridNote.this.cargoGridView.setText(indexData.identifier, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                            CargoGridNote.this.readLockTime(i + 1, z);
                            return;
                        } else {
                            ToastUitl.showShort(CargoGridNote.this.context, R.string.communication_error);
                            if (CargoGridNote.this.loadingDialog == null || !CargoGridNote.this.loadingDialog.isShowing()) {
                                return;
                            }
                            CargoGridNote.this.loadingDialog.dismiss();
                            return;
                        }
                    }
                    ToastUitl.showShort(CargoGridNote.this.context, R.string.qurey_fail);
                    if (CargoGridNote.this.loadingDialog == null || !CargoGridNote.this.loadingDialog.isShowing()) {
                        return;
                    }
                    CargoGridNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        LoadingDialog loadingDialog = this.loadingDialog;
        if (loadingDialog == null || !loadingDialog.isShowing()) {
            return;
        }
        this.loadingDialog.dismiss();
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$15 */
    /* loaded from: classes2.dex */
    public class AnonymousClass15 implements OnCommandAnswerListener {
        final /* synthetic */ int val$index;
        final /* synthetic */ boolean val$isSigle;
        final /* synthetic */ CargoGridView.IndexData val$timeItemData;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass15(CargoGridView.IndexData indexData2, int i2, boolean z2) {
            indexData = indexData2;
            i = i2;
            z = z2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    CargoGridNote.this.cargoGridView.setText(indexData.identifier, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                    CargoGridNote.this.readLockTime(i + 1, z);
                    return;
                } else {
                    ToastUitl.showShort(CargoGridNote.this.context, R.string.communication_error);
                    if (CargoGridNote.this.loadingDialog == null || !CargoGridNote.this.loadingDialog.isShowing()) {
                        return;
                    }
                    CargoGridNote.this.loadingDialog.dismiss();
                    return;
                }
            }
            ToastUitl.showShort(CargoGridNote.this.context, R.string.qurey_fail);
            if (CargoGridNote.this.loadingDialog == null || !CargoGridNote.this.loadingDialog.isShowing()) {
                return;
            }
            CargoGridNote.this.loadingDialog.dismiss();
        }
    }

    public void readLinkageTime(int i, boolean z) {
        CargoGridView.GridItemData gridItemData = this.gridItemData;
        if (gridItemData == null) {
            return;
        }
        if (i < gridItemData.indexDataList.size()) {
            CargoGridView.IndexData indexData = this.gridItemData.indexDataList.get(i);
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            if (z) {
                commandV2_Up_SetCommand.setNewMergeShelSynRunTime(false, indexData.identifier, 0);
            } else {
                commandV2_Up_SetCommand.setNewMergeShelSynRunTime(false, indexData.identifier + 2000, 0);
            }
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.CargoGridNote.16
                final /* synthetic */ int val$index;
                final /* synthetic */ boolean val$isSigle;
                final /* synthetic */ CargoGridView.IndexData val$timeItemData;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                }

                AnonymousClass16(CargoGridView.IndexData indexData2, int i2, boolean z2) {
                    indexData = indexData2;
                    i = i2;
                    z = z2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr != null && bArr.length > 0) {
                        if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                            CargoGridNote.this.cargoGridView.setText(indexData.identifier, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                            CargoGridNote.this.readLinkageTime(i + 1, z);
                            return;
                        } else {
                            ToastUitl.showShort(CargoGridNote.this.context, R.string.news_report_error);
                            if (CargoGridNote.this.loadingDialog == null || !CargoGridNote.this.loadingDialog.isShowing()) {
                                return;
                            }
                            CargoGridNote.this.loadingDialog.dismiss();
                            return;
                        }
                    }
                    ToastUitl.showShort(CargoGridNote.this.context, R.string.qurey_fail);
                    if (CargoGridNote.this.loadingDialog == null || !CargoGridNote.this.loadingDialog.isShowing()) {
                        return;
                    }
                    CargoGridNote.this.loadingDialog.dismiss();
                }
            });
            return;
        }
        LoadingDialog loadingDialog = this.loadingDialog;
        if (loadingDialog == null || !loadingDialog.isShowing()) {
            return;
        }
        this.loadingDialog.dismiss();
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$16 */
    /* loaded from: classes2.dex */
    public class AnonymousClass16 implements OnCommandAnswerListener {
        final /* synthetic */ int val$index;
        final /* synthetic */ boolean val$isSigle;
        final /* synthetic */ CargoGridView.IndexData val$timeItemData;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass16(CargoGridView.IndexData indexData2, int i2, boolean z2) {
            indexData = indexData2;
            i = i2;
            z = z2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    CargoGridNote.this.cargoGridView.setText(indexData.identifier, String.valueOf(ObjectHelper.intFromBytes(bArr, 1, 2)));
                    CargoGridNote.this.readLinkageTime(i + 1, z);
                    return;
                } else {
                    ToastUitl.showShort(CargoGridNote.this.context, R.string.news_report_error);
                    if (CargoGridNote.this.loadingDialog == null || !CargoGridNote.this.loadingDialog.isShowing()) {
                        return;
                    }
                    CargoGridNote.this.loadingDialog.dismiss();
                    return;
                }
            }
            ToastUitl.showShort(CargoGridNote.this.context, R.string.qurey_fail);
            if (CargoGridNote.this.loadingDialog == null || !CargoGridNote.this.loadingDialog.isShowing()) {
                return;
            }
            CargoGridNote.this.loadingDialog.dismiss();
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        int i = this.settingType;
        if (i == 123) {
            showInputDialog(this.context.getString(R.string.lab_price) + "-" + getSettingName(), this.context.getString(R.string.lab_shelf), false);
            return;
        }
        if (i == 124) {
            showInputDialog(this.context.getString(R.string.lab_price) + "-" + getSettingName(), this.context.getString(R.string.layer_number), false);
            return;
        }
        if (i == 131) {
            showInputDialog(this.context.getString(R.string.lab_stock) + "-" + getSettingName(), this.context.getString(R.string.lab_shelf), true);
            return;
        }
        if (i == 132) {
            showInputDialog(this.context.getString(R.string.lab_stock) + "-" + getSettingName(), this.context.getString(R.string.layer_number), true);
            return;
        }
        if (i == 136) {
            showInputDialog(this.context.getString(R.string.lab_capacity) + "-" + getSettingName(), this.context.getString(R.string.lab_shelf), false);
            return;
        }
        if (i == 137) {
            showInputDialog(this.context.getString(R.string.lab_capacity) + "-" + getSettingName(), this.context.getString(R.string.layer_number), false);
            return;
        }
        if (i == 140) {
            showInputDialog(this.context.getString(R.string.code) + "-" + getSettingName(), this.context.getString(R.string.lab_shelf), false);
            return;
        }
        if (i == 268) {
            showInputDialog(this.context.getString(R.string.code) + "-" + getSettingName(), this.context.getString(R.string.layer_number), false);
            return;
        }
        if (i == 274) {
            showInputDialog(this.context.getString(R.string.electromagnetic_lock_on_time) + "-" + getSettingName(), this.context.getString(R.string.lab_shelf), false);
            return;
        }
        if (i == 275) {
            showInputDialog(this.context.getString(R.string.electromagnetic_lock_on_time) + "-" + getSettingName(), this.context.getString(R.string.layer_number), false);
            return;
        }
        if (i == 329) {
            showInputDialog(this.context.getString(R.string.linkage_synchronization_time) + "-" + getSettingName(), this.context.getString(R.string.lab_shelf), false);
            return;
        }
        if (i != 330) {
            return;
        }
        showInputDialog(this.context.getString(R.string.linkage_synchronization_time) + "-" + getSettingName(), this.context.getString(R.string.layer_number), false);
    }

    private void showInputDialog(String str, String str2, boolean z) {
        GoodsInfoInputDialog goodsInfoInputDialog = new GoodsInfoInputDialog(this.context, str, str2, this.cargoGridView.getInputData());
        if (z) {
            goodsInfoInputDialog.setAddAndReduceButtonVisible();
        }
        goodsInfoInputDialog.show();
        goodsInfoInputDialog.setButtonClickListener(new GoodsInfoInputDialog.ButtonClickListener() { // from class: com.shj.setting.generator.CargoGridNote.17
            final /* synthetic */ GoodsInfoInputDialog val$goodsInfoInputDialog;

            AnonymousClass17(GoodsInfoInputDialog goodsInfoInputDialog2) {
                goodsInfoInputDialog = goodsInfoInputDialog2;
            }

            @Override // com.shj.setting.Dialog.GoodsInfoInputDialog.ButtonClickListener
            public void okClick() {
                CargoGridNote.this.cargoGridView.setInputData(goodsInfoInputDialog.getDataList());
            }
        });
    }

    /* renamed from: com.shj.setting.generator.CargoGridNote$17 */
    /* loaded from: classes2.dex */
    public class AnonymousClass17 implements GoodsInfoInputDialog.ButtonClickListener {
        final /* synthetic */ GoodsInfoInputDialog val$goodsInfoInputDialog;

        AnonymousClass17(GoodsInfoInputDialog goodsInfoInputDialog2) {
            goodsInfoInputDialog = goodsInfoInputDialog2;
        }

        @Override // com.shj.setting.Dialog.GoodsInfoInputDialog.ButtonClickListener
        public void okClick() {
            CargoGridNote.this.cargoGridView.setInputData(goodsInfoInputDialog.getDataList());
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        setShowAddAndReduce();
        this.cargoGridView = new CargoGridView(this.context, getGridItemData(), this.isShowAddAndReduce);
        setQueryButtonVisibility();
        setClearButton();
        this.cargoGridView.setTitle(getSettingName());
        this.cargoGridView.setTitleVisibility(0);
        this.cargoGridView.setEventListener(this.eventListener);
        return this.cargoGridView;
    }

    private void setShowAddAndReduce() {
        int i = this.settingType;
        if (i == 131 || i == 132) {
            this.isShowAddAndReduce = true;
        }
    }

    private void setQueryButtonVisibility() {
        int i = this.settingType;
        if (i == 123 || i == 124 || i == 131 || i == 132 || i == 136 || i == 137 || i == 140 || i == 268 || i == 274 || i == 275 || i == 329 || i == 330) {
            this.cargoGridView.setQueryButtonVIsibility(0);
            this.cargoGridView.setQueryButtonText(this.context.getString(R.string.dialog_input));
        }
    }

    private void setClearButton() {
        int i = this.settingType;
        if (i == 274 || i == 275 || i == 329 || i == 330) {
            this.cargoGridView.setClearButtonVisibility(0);
            this.cargoGridView.setClearSettingText(this.context.getString(R.string.query));
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        CargoGridView cargoGridView = this.cargoGridView;
        if (cargoGridView != null) {
            return cargoGridView;
        }
        return null;
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        super.onAttached();
        EventBus.getDefault().register(this);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
        super.onDetached();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof SettingTypeEvent) {
            SettingTypeEvent settingTypeEvent = (SettingTypeEvent) baseEvent;
            int settingType = settingTypeEvent.getSettingType();
            if (settingType == 124) {
                if (this.settingType == 123) {
                    this.cargoGridView.setLayerData((List) settingTypeEvent.getData());
                    return;
                }
                return;
            }
            if (settingType == 125) {
                if (this.settingType == 123 || this.settingType == 124) {
                    this.cargoGridView.setWholeData((String) settingTypeEvent.getData());
                    return;
                }
                return;
            }
            if (settingType == 137) {
                if (this.settingType == 136) {
                    this.cargoGridView.setLayerData((List) settingTypeEvent.getData());
                    return;
                }
                return;
            }
            if (settingType == 138) {
                if (this.settingType == 136 || this.settingType == 137) {
                    this.cargoGridView.setWholeData((String) settingTypeEvent.getData());
                    return;
                }
                return;
            }
            if (settingType == 141) {
                if (this.settingType == 140 || this.settingType == 268) {
                    this.cargoGridView.setWholeData((String) settingTypeEvent.getData());
                    return;
                }
                return;
            }
            if (settingType == 268) {
                if (this.settingType == 140) {
                    this.cargoGridView.setLayerData((List) settingTypeEvent.getData());
                    return;
                }
                return;
            }
            if (settingType == 275) {
                if (this.settingType == 274) {
                    CargoGridView.IndexData indexData = (CargoGridView.IndexData) settingTypeEvent.getData();
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(indexData);
                    this.cargoGridView.setLayerData(arrayList);
                    return;
                }
                return;
            }
            if (settingType == 276) {
                if (this.settingType == 274 || this.settingType == 275) {
                    this.cargoGridView.setWholeData((String) settingTypeEvent.getData());
                    return;
                }
                return;
            }
            if (settingType == 330) {
                if (this.settingType == 329) {
                    CargoGridView.IndexData indexData2 = (CargoGridView.IndexData) settingTypeEvent.getData();
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.add(indexData2);
                    this.cargoGridView.setLayerData(arrayList2);
                    return;
                }
                return;
            }
            if (settingType != 331) {
                switch (settingType) {
                    case 132:
                        if (this.settingType == 131) {
                            this.cargoGridView.setLayerData((List) settingTypeEvent.getData());
                            return;
                        }
                        return;
                    case SettingType.INVENTORY_WHOLE_MACHINE /* 133 */:
                        if (this.settingType == 131 || this.settingType == 132) {
                            this.cargoGridView.setWholeData((String) settingTypeEvent.getData());
                            return;
                        }
                        return;
                    case SettingType.FULL_DELIVERY /* 134 */:
                        if (this.settingType == 131) {
                            this.cargoGridView.updateData(getGridItemData());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
            if (this.settingType == 329 || this.settingType == 330) {
                this.cargoGridView.setWholeData((String) settingTypeEvent.getData());
            }
        }
    }
}
