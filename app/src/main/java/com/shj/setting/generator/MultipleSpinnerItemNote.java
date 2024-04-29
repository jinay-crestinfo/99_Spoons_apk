package com.shj.setting.generator;

import android.content.Context;
import android.serialport.SerialPortFinder;
import android.view.View;
import android.widget.AdapterView;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.http.RequestHelper;
import com.oysb.utils.http.RequestItem;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Dialog.LightInspectionDialog;
import com.shj.setting.Dialog.MutilTextTipDialog;
import com.shj.setting.NetAddress.NetAddress;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.MultipleSpinnerItemView;
import com.shj.setting.widget.SpinnerItemView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.spi.Configurator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class MultipleSpinnerItemNote extends SettingNote {
    private int cabinetNumber;
    private SpinnerItemView.SpinnerItemData deviceScanYRSpinnerItemData;
    private MultipleSpinnerItemView multipleSpinnerItemView;
    private MutilTextTipDialog mutilTextTipDialog;

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
    }

    public MultipleSpinnerItemNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    public void shipmentTest(int i, int i2) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.TestOfferGoodsCheck(true, i, i2);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.1
            final /* synthetic */ int val$mainMachineId;
            final /* synthetic */ int val$type;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass1(int i22, int i3) {
                i2 = i22;
                i = i3;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr.toString().equals("0")) {
                    MultipleSpinnerItemNote.this.mutilTextTipDialog.addTextShow(MultipleSpinnerItemNote.this.context.getString(R.string.test_fail));
                    return;
                }
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 1, 1);
                Loger.writeLog("SET", "截取的下位机的返回的值状态是:" + intFromBytes + ",测试状态:" + intFromBytes2);
                if (i2 != 0) {
                    MultipleSpinnerItemNote.this.mutilTextTipDialog.addTextShow(MultipleSpinnerItemNote.this.context.getString(R.string.manual_testing) + MultipleSpinnerItemNote.this.context.getString(R.string.start));
                } else {
                    MultipleSpinnerItemNote.this.mutilTextTipDialog = new MutilTextTipDialog(MultipleSpinnerItemNote.this.context);
                    MultipleSpinnerItemNote.this.mutilTextTipDialog.addTextShow(MultipleSpinnerItemNote.this.context.getString(R.string.automatic_testing) + MultipleSpinnerItemNote.this.context.getString(R.string.start));
                    MultipleSpinnerItemNote.this.mutilTextTipDialog.show();
                }
                if (intFromBytes != 0) {
                    if (intFromBytes == 1) {
                        ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.communication_error);
                        return;
                    }
                    return;
                }
                if (intFromBytes2 == 0) {
                    MultipleSpinnerItemNote.this.mutilTextTipDialog.addTextShow(MultipleSpinnerItemNote.this.context.getString(R.string.test_success));
                } else if (intFromBytes2 == 1) {
                    MultipleSpinnerItemNote.this.mutilTextTipDialog.addTextShow(MultipleSpinnerItemNote.this.context.getString(R.string.test_fail));
                }
                if (i2 == 0) {
                    MultipleSpinnerItemNote.this.shipmentTest(i, 1);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnCommandAnswerListener {
        final /* synthetic */ int val$mainMachineId;
        final /* synthetic */ int val$type;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass1(int i22, int i3) {
            i2 = i22;
            i = i3;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr.toString().equals("0")) {
                MultipleSpinnerItemNote.this.mutilTextTipDialog.addTextShow(MultipleSpinnerItemNote.this.context.getString(R.string.test_fail));
                return;
            }
            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
            int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 1, 1);
            Loger.writeLog("SET", "截取的下位机的返回的值状态是:" + intFromBytes + ",测试状态:" + intFromBytes2);
            if (i2 != 0) {
                MultipleSpinnerItemNote.this.mutilTextTipDialog.addTextShow(MultipleSpinnerItemNote.this.context.getString(R.string.manual_testing) + MultipleSpinnerItemNote.this.context.getString(R.string.start));
            } else {
                MultipleSpinnerItemNote.this.mutilTextTipDialog = new MutilTextTipDialog(MultipleSpinnerItemNote.this.context);
                MultipleSpinnerItemNote.this.mutilTextTipDialog.addTextShow(MultipleSpinnerItemNote.this.context.getString(R.string.automatic_testing) + MultipleSpinnerItemNote.this.context.getString(R.string.start));
                MultipleSpinnerItemNote.this.mutilTextTipDialog.show();
            }
            if (intFromBytes != 0) {
                if (intFromBytes == 1) {
                    ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.communication_error);
                    return;
                }
                return;
            }
            if (intFromBytes2 == 0) {
                MultipleSpinnerItemNote.this.mutilTextTipDialog.addTextShow(MultipleSpinnerItemNote.this.context.getString(R.string.test_success));
            } else if (intFromBytes2 == 1) {
                MultipleSpinnerItemNote.this.mutilTextTipDialog.addTextShow(MultipleSpinnerItemNote.this.context.getString(R.string.test_fail));
            }
            if (i2 == 0) {
                MultipleSpinnerItemNote.this.shipmentTest(i, 1);
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        SpinnerItemView.SpinnerItemData spinnerItemData;
        int i = this.settingType;
        if (i == 156) {
            int i2 = this.cabinetNumber;
            if (i2 != -1) {
                shipmentTest(i2, 0);
                return;
            } else {
                shipmentTest(this.multipleSpinnerItemView.getSelect(0), 0);
                return;
            }
        }
        if (i == 226) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setPrecision(true, this.multipleSpinnerItemView.getSelect(0));
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.2
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass2() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                    ToastUitl.showSetCompeleteTip(MultipleSpinnerItemNote.this.context, z2, MultipleSpinnerItemNote.this.getSettingName());
                }
            });
            return;
        }
        if (i == 229) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand2.setAutoAdjust_OfferGoodsCheck_frequency(true, this.multipleSpinnerItemView.getSelect(0));
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.3
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass3() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                    ToastUitl.showCompeleteTip(MultipleSpinnerItemNote.this.context, z2, MultipleSpinnerItemNote.this.getSettingName());
                }
            });
            return;
        }
        if (i == 247) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand3 = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand3.TestHFJ_JG(this.multipleSpinnerItemView.getDataIndex(1), 1, this.multipleSpinnerItemView.getSelect(0));
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand3, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.4
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass4() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                    if (z2) {
                        ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.success);
                    } else {
                        ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.fail);
                    }
                }
            });
            return;
        }
        if (i == 258) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand4 = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand4.testHfjOfferGoods(this.multipleSpinnerItemView.getSelect(1), this.multipleSpinnerItemView.getSelect(0));
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand4, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.5
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass5() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                    ToastUitl.showCompeleteTip(MultipleSpinnerItemNote.this.context, z2, MultipleSpinnerItemNote.this.getSettingName());
                }
            });
            return;
        }
        if (i == 303) {
            int select = this.multipleSpinnerItemView.getSelect(0);
            if (select == 0 || (spinnerItemData = this.deviceScanYRSpinnerItemData) == null || spinnerItemData.dataList == null || select >= this.deviceScanYRSpinnerItemData.dataList.size()) {
                return;
            }
            AppSetting.saveDeviceScanPortAddersYR(this.context, this.deviceScanYRSpinnerItemData.dataList.get(select), this.mUserSettingDao);
            ToastUitl.showShort(this.context, R.string.save_success);
            return;
        }
        if (i == 307) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand5 = new CommandV2_Up_SetCommand();
            int select2 = this.multipleSpinnerItemView.getSelect(0);
            commandV2_Up_SetCommand5.setOpenGridMachineGrid(select2, -1);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand5, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.6
                final /* synthetic */ int val$jgh;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass6(int select22) {
                    select2 = select22;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                    List<Integer> list;
                    if (z2) {
                        ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.open_success);
                        return;
                    }
                    HashMap<Integer, List<Integer>> hashMap = SettingActivity.getBasicMachineInfo().shelvesMap;
                    if (hashMap == null || (list = hashMap.get(Integer.valueOf(select2))) == null) {
                        return;
                    }
                    Iterator<Integer> it = list.iterator();
                    while (it.hasNext()) {
                        int intValue = it.next().intValue();
                        CommandV2_Up_SetCommand commandV2_Up_SetCommand6 = new CommandV2_Up_SetCommand();
                        commandV2_Up_SetCommand6.TestShelf(false, 1, intValue);
                        Shj.getInstance(MultipleSpinnerItemNote.this.context);
                        Shj.postSetCommand(commandV2_Up_SetCommand6, null);
                    }
                }
            });
            return;
        }
        if (i == 309) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand6 = new CommandV2_Up_SetCommand();
            String selectValue = this.multipleSpinnerItemView.getSelectValue(0);
            if (selectValue != null) {
                commandV2_Up_SetCommand6.readLightInspectionStatus(Integer.valueOf(selectValue).intValue());
                Shj.getInstance(this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand6, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.7
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z2) {
                    }

                    AnonymousClass7() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        if (bArr != null && bArr.length > 0) {
                            if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                                int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                                int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 1);
                                byte[] bytesFromBytes = ObjectHelper.bytesFromBytes(bArr, 3, 33);
                                ArrayList arrayList = new ArrayList();
                                HashMap<Integer, List<Integer>> hashMap = SettingActivity.getBasicMachineInfo().layerNumberMap;
                                Iterator<Integer> it = hashMap.keySet().iterator();
                                while (it.hasNext()) {
                                    Iterator<Integer> it2 = hashMap.get(it.next()).iterator();
                                    while (it2.hasNext()) {
                                        arrayList.add(String.valueOf(it2.next().intValue()));
                                    }
                                }
                                Collections.sort(arrayList);
                                new LightInspectionDialog(MultipleSpinnerItemNote.this.context, intFromBytes, arrayList, intFromBytes2, bytesFromBytes).show();
                                return;
                            }
                            ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.qurey_fail);
                            return;
                        }
                        ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.qurey_fail);
                    }
                });
                return;
            }
            return;
        }
        if (i != 323) {
            if (i != 336) {
                return;
            }
            AppSetting.saveWokMode(this.context, this.multipleSpinnerItemView.getSelect(0), this.mUserSettingDao);
            ToastUitl.showShort(this.context, R.string.save_success);
            return;
        }
        CommandV2_Up_SetCommand commandV2_Up_SetCommand7 = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand7.TestHFJ_JG(16, 1, this.multipleSpinnerItemView.getSelect(0));
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand7, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.8
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass8() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z2) {
                if (z2) {
                    ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.success);
                } else {
                    ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.fail);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass2() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(MultipleSpinnerItemNote.this.context, z2, MultipleSpinnerItemNote.this.getSettingName());
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass3() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showCompeleteTip(MultipleSpinnerItemNote.this.context, z2, MultipleSpinnerItemNote.this.getSettingName());
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass4() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            if (z2) {
                ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.success);
            } else {
                ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.fail);
            }
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$5 */
    /* loaded from: classes2.dex */
    class AnonymousClass5 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass5() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showCompeleteTip(MultipleSpinnerItemNote.this.context, z2, MultipleSpinnerItemNote.this.getSettingName());
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$6 */
    /* loaded from: classes2.dex */
    class AnonymousClass6 implements OnCommandAnswerListener {
        final /* synthetic */ int val$jgh;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass6(int select22) {
            select2 = select22;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            List<Integer> list;
            if (z2) {
                ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.open_success);
                return;
            }
            HashMap<Integer, List<Integer>> hashMap = SettingActivity.getBasicMachineInfo().shelvesMap;
            if (hashMap == null || (list = hashMap.get(Integer.valueOf(select2))) == null) {
                return;
            }
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                CommandV2_Up_SetCommand commandV2_Up_SetCommand6 = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand6.TestShelf(false, 1, intValue);
                Shj.getInstance(MultipleSpinnerItemNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand6, null);
            }
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$7 */
    /* loaded from: classes2.dex */
    class AnonymousClass7 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass7() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length > 0) {
                if (ObjectHelper.intFromBytes(bArr, 0, 1) == 0) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 1, 1);
                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 2, 1);
                    byte[] bytesFromBytes = ObjectHelper.bytesFromBytes(bArr, 3, 33);
                    ArrayList arrayList = new ArrayList();
                    HashMap<Integer, List<Integer>> hashMap = SettingActivity.getBasicMachineInfo().layerNumberMap;
                    Iterator<Integer> it = hashMap.keySet().iterator();
                    while (it.hasNext()) {
                        Iterator<Integer> it2 = hashMap.get(it.next()).iterator();
                        while (it2.hasNext()) {
                            arrayList.add(String.valueOf(it2.next().intValue()));
                        }
                    }
                    Collections.sort(arrayList);
                    new LightInspectionDialog(MultipleSpinnerItemNote.this.context, intFromBytes, arrayList, intFromBytes2, bytesFromBytes).show();
                    return;
                }
                ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.qurey_fail);
                return;
            }
            ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.qurey_fail);
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$8 */
    /* loaded from: classes2.dex */
    class AnonymousClass8 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass8() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            if (z2) {
                ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.success);
            } else {
                ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.fail);
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void clearData() {
        super.clearData();
        if (this.settingType != 247) {
            return;
        }
        Loger.writeLog("UI", "上报门已正常");
        String str = NetAddress.getAppBaseUrl() + "/service-member/jwapi/doorFaultClearing";
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("jqbh", AppSetting.getMachineId(this.context, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestItem requestItem = new RequestItem(str, jSONObject, "POST");
        requestItem.setRepeatDelay(4000);
        requestItem.setRequestMaxCount(3);
        requestItem.setOnRequestResultListener(new RequestItem.OnRequestResultListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.9
            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public void onRequestFinished(RequestItem requestItem2, boolean z) {
            }

            AnonymousClass9() {
            }

            @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
            public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
                Loger.writeLog("UI", "上报门已正常 上报成功");
                return true;
            }
        });
        RequestHelper.request(requestItem);
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$9 */
    /* loaded from: classes2.dex */
    class AnonymousClass9 implements RequestItem.OnRequestResultListener {
        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onFailure(RequestItem requestItem2, int i, String str2, Throwable th) {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public void onRequestFinished(RequestItem requestItem2, boolean z) {
        }

        AnonymousClass9() {
        }

        @Override // com.oysb.utils.http.RequestItem.OnRequestResultListener
        public boolean onSuccess(RequestItem requestItem2, int i, String str2) {
            Loger.writeLog("UI", "上报门已正常 上报成功");
            return true;
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        queryData();
    }

    private void queryData() {
        SpinnerItemView.SpinnerItemData spinnerItemData;
        int i = this.settingType;
        if (i == 226) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setPrecision(false, 0);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.10
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass10() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr == null || bArr.length <= 0) {
                        return;
                    }
                    MultipleSpinnerItemNote.this.multipleSpinnerItemView.setSelect(0, ObjectHelper.intFromBytes(bArr, 0, 1));
                }
            });
            return;
        }
        if (i != 303) {
            if (i != 336) {
                return;
            }
            this.multipleSpinnerItemView.setSelect(0, AppSetting.getWorkMode(this.context, this.mUserSettingDao));
            return;
        }
        String deviceScanPortAddersYR = AppSetting.getDeviceScanPortAddersYR(this.context, this.mUserSettingDao);
        if (deviceScanPortAddersYR != null && (spinnerItemData = this.deviceScanYRSpinnerItemData) != null && spinnerItemData.dataList != null && this.deviceScanYRSpinnerItemData.dataList.size() > 0) {
            Iterator<String> it = this.deviceScanYRSpinnerItemData.dataList.iterator();
            int i2 = 0;
            while (it.hasNext() && !it.next().equals(deviceScanPortAddersYR)) {
                i2++;
            }
            this.multipleSpinnerItemView.setSelect(0, i2);
            return;
        }
        this.multipleSpinnerItemView.setSelect(0, 0);
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass10() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr == null || bArr.length <= 0) {
                return;
            }
            MultipleSpinnerItemNote.this.multipleSpinnerItemView.setSelect(0, ObjectHelper.intFromBytes(bArr, 0, 1));
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        int i = this.settingType;
        if (i != 247) {
            if (i != 323) {
                return;
            }
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.TestHFJ_JG(17, 1, this.multipleSpinnerItemView.getSelect(0));
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.12
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass12() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                    if (z) {
                        ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.success);
                    } else {
                        ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.fail);
                    }
                }
            });
            return;
        }
        CommandV2_Up_SetCommand commandV2_Up_SetCommand2 = new CommandV2_Up_SetCommand();
        int select = this.multipleSpinnerItemView.getSelect(0);
        int dataIndex = this.multipleSpinnerItemView.getDataIndex(1);
        commandV2_Up_SetCommand2.TestHFJ_JG(dataIndex, dataIndex == 0 ? 3 : 2, select);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand2, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.11
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass11() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (z) {
                    ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.success);
                } else {
                    ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.fail);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$11 */
    /* loaded from: classes2.dex */
    class AnonymousClass11 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass11() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (z) {
                ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.success);
            } else {
                ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.fail);
            }
        }
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$12 */
    /* loaded from: classes2.dex */
    class AnonymousClass12 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass12() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (z) {
                ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.success);
            } else {
                ToastUitl.showShort(MultipleSpinnerItemNote.this.context, R.string.fail);
            }
        }
    }

    private List<SpinnerItemView.SpinnerItemData> getSpinnerDataList() {
        ArrayList arrayList = new ArrayList();
        int i = this.settingType;
        if (i != 156) {
            if (i == 226) {
                arrayList.add(getDecimalPointDigit());
            } else {
                if (i != 229) {
                    if (i != 247) {
                        int i2 = 0;
                        if (i == 258) {
                            arrayList.add(getMachineCabinetNumber());
                            SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
                            spinnerItemData.name = this.context.getString(R.string.mode);
                            spinnerItemData.dataList = new ArrayList();
                            String[] stringArray = this.context.getResources().getStringArray(R.array.box_out_mode);
                            int length = stringArray.length;
                            while (i2 < length) {
                                spinnerItemData.dataList.add(stringArray[i2]);
                                i2++;
                            }
                            arrayList.add(spinnerItemData);
                        } else if (i == 303) {
                            SpinnerItemView.SpinnerItemData spinnerItemData2 = new SpinnerItemView.SpinnerItemData();
                            this.deviceScanYRSpinnerItemData = spinnerItemData2;
                            spinnerItemData2.name = this.context.getString(R.string.serial_port_address);
                            this.deviceScanYRSpinnerItemData.dataList = new ArrayList();
                            String[] allDevicesPath = new SerialPortFinder().getAllDevicesPath();
                            if (allDevicesPath.length == 0) {
                                allDevicesPath = new String[]{this.context.getString(R.string.no_serial_device)};
                            }
                            this.deviceScanYRSpinnerItemData.dataList.add(Configurator.NULL);
                            int length2 = allDevicesPath.length;
                            while (i2 < length2) {
                                this.deviceScanYRSpinnerItemData.dataList.add(allDevicesPath[i2]);
                                i2++;
                            }
                            arrayList.add(this.deviceScanYRSpinnerItemData);
                        } else if (i != 307) {
                            if (i == 309) {
                                arrayList.add(getMachineLayer());
                            } else if (i != 323) {
                                if (i == 336) {
                                    arrayList.add(getWorkMode());
                                }
                            }
                        }
                    } else {
                        arrayList.add(getMachineCabinetNumber());
                        arrayList.add(getInstitutionalFunction());
                    }
                }
                arrayList.add(getMachineCabinetNumber());
            }
        } else if (this.cabinetNumber == -1) {
            arrayList.add(getMachineCabinetNumber());
        }
        return arrayList;
    }

    private SpinnerItemView.SpinnerItemData getFunction(int i) {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        spinnerItemData.name = this.context.getString(R.string.action);
        spinnerItemData.dataList = new ArrayList();
        String string = this.context.getString(R.string.forward);
        String string2 = this.context.getString(R.string.backward);
        String string3 = this.context.getString(R.string.stop);
        String string4 = this.context.getString(R.string.upward);
        String string5 = this.context.getString(R.string.down);
        String string6 = this.context.getString(R.string.open_over);
        if (i == 0 || i == 1) {
            spinnerItemData.dataList.add(string);
            spinnerItemData.dataList.add(string2);
            spinnerItemData.dataList.add(string3);
        } else if (i == 2 || i == 3) {
            spinnerItemData.dataList.add(string4);
            spinnerItemData.dataList.add(string5);
            spinnerItemData.dataList.add(string3);
        } else if (i == 4 || i == 5) {
            spinnerItemData.dataList.add(string6);
            spinnerItemData.dataList.add(string3);
        }
        return spinnerItemData;
    }

    private SpinnerItemView.SpinnerItemData getInstitutionalFunction() {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        spinnerItemData.name = this.context.getString(R.string.institutional);
        spinnerItemData.dataList = new ArrayList();
        spinnerItemData.indexList = new ArrayList();
        String[] stringArray = this.context.getResources().getStringArray(R.array.institutional_function);
        if ("sy".equalsIgnoreCase(CacheHelper.getFileCache().getAsString("institutiona_function_type"))) {
            stringArray = this.context.getResources().getStringArray(R.array.institutional_function_sy);
        }
        for (String str : stringArray) {
            String[] split = str.split(VoiceWakeuperAidl.PARAMS_SEPARATE);
            spinnerItemData.dataList.add(split[0]);
            spinnerItemData.indexList.add(Integer.valueOf(split[1]));
        }
        return spinnerItemData;
    }

    private SpinnerItemView.SpinnerItemData getWorkMode() {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        spinnerItemData.name = this.context.getString(R.string.mode);
        spinnerItemData.dataList = new ArrayList();
        spinnerItemData.indexList = new ArrayList();
        String[] stringArray = this.context.getResources().getStringArray(R.array.work_mode);
        int length = stringArray.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            spinnerItemData.dataList.add(stringArray[i]);
            spinnerItemData.indexList.add(Integer.valueOf(i2));
            i++;
            i2++;
        }
        return spinnerItemData;
    }

    private SpinnerItemView.SpinnerItemData getDecimalPointDigit() {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        spinnerItemData.name = this.context.getResources().getString(R.string.decimal_point_digit);
        spinnerItemData.dataList = new ArrayList();
        String string = this.context.getResources().getString(R.string.position);
        for (int i = 0; i < 3; i++) {
            spinnerItemData.dataList.add(i + string);
        }
        return spinnerItemData;
    }

    private SpinnerItemView.SpinnerItemData getMachineCabinetNumber() {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        spinnerItemData.name = this.context.getResources().getString(R.string.cabinet_number);
        spinnerItemData.dataList = new ArrayList();
        Iterator<String> it = SettingActivity.getBasicMachineInfo().cabinetNumberList.iterator();
        while (it.hasNext()) {
            spinnerItemData.dataList.add(it.next());
        }
        return spinnerItemData;
    }

    private SpinnerItemView.SpinnerItemData getMachineLayer() {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        spinnerItemData.name = this.context.getResources().getString(R.string.layer_number);
        spinnerItemData.dataList = new ArrayList();
        HashMap<Integer, List<Integer>> hashMap = SettingActivity.getBasicMachineInfo().layerNumberMap;
        Iterator<Integer> it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            Iterator<Integer> it2 = hashMap.get(it.next()).iterator();
            while (it2.hasNext()) {
                spinnerItemData.dataList.add(String.valueOf(it2.next().intValue()));
            }
        }
        return spinnerItemData;
    }

    private SpinnerItemView.SpinnerItemData getTestType() {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        spinnerItemData.name = this.context.getResources().getString(R.string.test_type);
        spinnerItemData.dataList = new ArrayList();
        spinnerItemData.dataList.add(this.context.getResources().getString(R.string.automatic_testing));
        spinnerItemData.dataList.add(this.context.getResources().getString(R.string.manual_testing));
        return spinnerItemData;
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        this.cabinetNumber = i;
        List<SpinnerItemView.SpinnerItemData> spinnerDataList = getSpinnerDataList();
        MultipleSpinnerItemView multipleSpinnerItemView = new MultipleSpinnerItemView(this.context, spinnerDataList);
        this.multipleSpinnerItemView = multipleSpinnerItemView;
        multipleSpinnerItemView.setEventListener(this.eventListener);
        if (this.settingType == 229 || this.settingType == 156 || this.settingType == 303 || this.settingType == 307 || this.settingType == 309 || this.settingType == 323 || this.settingType == 336) {
            this.multipleSpinnerItemView.setTitleVisibility(0);
            this.multipleSpinnerItemView.setTitle(getSettingName());
        } else if (spinnerDataList.size() > 1) {
            this.multipleSpinnerItemView.setTitleVisibility(0);
            this.multipleSpinnerItemView.setTitle(getSettingName());
        }
        setSaveSettingText();
        this.multipleSpinnerItemView.setOnItemSelectedListener(1, new AdapterView.OnItemSelectedListener() { // from class: com.shj.setting.generator.MultipleSpinnerItemNote.13
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            AnonymousClass13() {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
                int dataIndex = MultipleSpinnerItemNote.this.multipleSpinnerItemView.getDataIndex(1);
                if (dataIndex == 16 || dataIndex == 17) {
                    MultipleSpinnerItemNote.this.multipleSpinnerItemView.setQueryButtonVIsibility(4);
                } else {
                    MultipleSpinnerItemNote.this.multipleSpinnerItemView.setQueryButtonVIsibility(0);
                }
            }
        });
        return this.multipleSpinnerItemView;
    }

    /* renamed from: com.shj.setting.generator.MultipleSpinnerItemNote$13 */
    /* loaded from: classes2.dex */
    class AnonymousClass13 implements AdapterView.OnItemSelectedListener {
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        AnonymousClass13() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
            int dataIndex = MultipleSpinnerItemNote.this.multipleSpinnerItemView.getDataIndex(1);
            if (dataIndex == 16 || dataIndex == 17) {
                MultipleSpinnerItemNote.this.multipleSpinnerItemView.setQueryButtonVIsibility(4);
            } else {
                MultipleSpinnerItemNote.this.multipleSpinnerItemView.setQueryButtonVIsibility(0);
            }
        }
    }

    private void setSaveSettingText() {
        int i = this.settingType;
        if (i != 156) {
            if (i == 229) {
                this.multipleSpinnerItemView.setSaveSettingText(this.context.getResources().getString(R.string.adjustment));
                return;
            }
            if (i == 247) {
                this.multipleSpinnerItemView.setSaveSettingText(this.context.getResources().getString(R.string.open_over));
                this.multipleSpinnerItemView.setQueryButtonText(this.context.getResources().getString(R.string.lab_disable));
                this.multipleSpinnerItemView.setQueryButtonVIsibility(0);
                this.multipleSpinnerItemView.setClearSettingText(this.context.getResources().getString(R.string.door_state_normal_to_jinwu));
                this.multipleSpinnerItemView.setClearButtonVisibility(0);
                return;
            }
            if (i != 258) {
                if (i == 307) {
                    this.multipleSpinnerItemView.setSaveSettingText(this.context.getResources().getString(R.string.open));
                    return;
                } else if (i == 309) {
                    this.multipleSpinnerItemView.setSaveSettingText(this.context.getResources().getString(R.string.query));
                    return;
                } else {
                    if (i != 323) {
                        return;
                    }
                    this.multipleSpinnerItemView.setSaveSettingText(this.context.getResources().getString(R.string.roll_up));
                    this.multipleSpinnerItemView.setQueryButtonVIsibility(0);
                    this.multipleSpinnerItemView.setQueryButtonText(this.context.getResources().getString(R.string.roll_down));
                    return;
                }
            }
        }
        this.multipleSpinnerItemView.setSaveSettingText(this.context.getResources().getString(R.string.exec_test));
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        MultipleSpinnerItemView multipleSpinnerItemView = this.multipleSpinnerItemView;
        if (multipleSpinnerItemView != null) {
            return multipleSpinnerItemView;
        }
        return null;
    }
}
