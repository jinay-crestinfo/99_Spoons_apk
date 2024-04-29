package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.SetUtils;
import com.shj.setting.Utils.ToastUitl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class ShelfTestDialog extends Dialog {
    private Button bt_close;
    private Button bt_test;
    private Context context;
    private EditText et_input01;
    private EditText et_input02;
    private LinearLayout ll_01;
    private LinearLayout ll_02;
    private LinearLayout ll_spinner_type;
    private int mode;
    private MutilTextTipDialog mutilTextTipDialog;
    private Spinner spinner_mode;
    private Spinner spinner_type;
    private TextView tv_name01;
    private TextView tv_name02;
    private TextView tv_name_spinner;

    public ShelfTestDialog(Context context) {
        super(context, R.style.translucent_dialog_style);
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.layout_shelf_test_dialog);
        findView();
        setListener();
        showContent();
    }

    private void findView() {
        this.ll_spinner_type = (LinearLayout) findViewById(R.id.ll_spinner_type);
        this.spinner_mode = (Spinner) findViewById(R.id.spinner_mode);
        this.spinner_type = (Spinner) findViewById(R.id.spinner_type);
        this.ll_01 = (LinearLayout) findViewById(R.id.ll_01);
        this.ll_02 = (LinearLayout) findViewById(R.id.ll_02);
        this.tv_name_spinner = (TextView) findViewById(R.id.tv_name_spinner);
        this.tv_name01 = (TextView) findViewById(R.id.tv_name01);
        this.tv_name02 = (TextView) findViewById(R.id.tv_name02);
        this.et_input01 = (EditText) findViewById(R.id.et_input01);
        this.et_input02 = (EditText) findViewById(R.id.et_input02);
        this.bt_close = (Button) findViewById(R.id.bt_close);
        this.bt_test = (Button) findViewById(R.id.bt_test);
    }

    /* renamed from: com.shj.setting.Dialog.ShelfTestDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ShelfTestDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ShelfTestDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ShelfTestDialog.this.dismiss();
            }
        });
        this.bt_test.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.ShelfTestDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ShelfTestDialog.this.mode == 0) {
                    String obj = ShelfTestDialog.this.et_input01.getText().toString();
                    if (TextUtils.isEmpty(obj)) {
                        ToastUitl.showNotInputTip(ShelfTestDialog.this.context);
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(Integer.valueOf(obj));
                    ShelfTestDialog.this.testShelf(arrayList, 0, false);
                    return;
                }
                if (ShelfTestDialog.this.mode != 1) {
                    if (ShelfTestDialog.this.mode != 2) {
                        if (ShelfTestDialog.this.mode == 3) {
                            String obj2 = ShelfTestDialog.this.et_input01.getText().toString();
                            String obj3 = ShelfTestDialog.this.et_input02.getText().toString();
                            if (TextUtils.isEmpty(obj2) || TextUtils.isEmpty(obj3)) {
                                ToastUitl.showNotInputTip(ShelfTestDialog.this.context);
                                return;
                            }
                            int intValue = Integer.valueOf(obj2).intValue();
                            int intValue2 = Integer.valueOf(obj3).intValue();
                            if (intValue >= intValue2) {
                                ToastUitl.showNotInputTip(ShelfTestDialog.this.context);
                                return;
                            }
                            ShelfTestDialog.this.mutilTextTipDialog = new MutilTextTipDialog(ShelfTestDialog.this.context);
                            ShelfTestDialog.this.mutilTextTipDialog.show();
                            ShelfTestDialog.this.mutilTextTipDialog.addTextShow(ShelfTestDialog.this.context.getString(R.string.test_start));
                            ArrayList arrayList2 = new ArrayList();
                            while (intValue <= intValue2) {
                                arrayList2.add(Integer.valueOf(intValue));
                                intValue++;
                            }
                            ShelfTestDialog.this.testShelf(arrayList2, 0, true);
                            return;
                        }
                        return;
                    }
                    ShelfTestDialog.this.mutilTextTipDialog = new MutilTextTipDialog(ShelfTestDialog.this.context);
                    ShelfTestDialog.this.mutilTextTipDialog.show();
                    ShelfTestDialog.this.mutilTextTipDialog.addTextShow(ShelfTestDialog.this.context.getString(R.string.test_start));
                    ShelfTestDialog.this.testShelf(SettingActivity.getBasicMachineInfo().shelvesMap.get(Integer.valueOf(SettingActivity.getBasicMachineInfo().cabinetList.get(ShelfTestDialog.this.spinner_type.getSelectedItemPosition()).intValue())), 0, true);
                    return;
                }
                ShelfTestDialog.this.mutilTextTipDialog = new MutilTextTipDialog(ShelfTestDialog.this.context);
                ShelfTestDialog.this.mutilTextTipDialog.show();
                ShelfTestDialog.this.mutilTextTipDialog.addTextShow(ShelfTestDialog.this.context.getString(R.string.test_start));
                ShelfTestDialog.this.testShelf(SettingActivity.getBasicMachineInfo().shelvesLayerMap.get(Integer.valueOf(SettingActivity.getBasicMachineInfo().layerNumberList.get(ShelfTestDialog.this.spinner_type.getSelectedItemPosition()).intValue())), 0, true);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.ShelfTestDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ShelfTestDialog.this.mode == 0) {
                String obj = ShelfTestDialog.this.et_input01.getText().toString();
                if (TextUtils.isEmpty(obj)) {
                    ToastUitl.showNotInputTip(ShelfTestDialog.this.context);
                    return;
                }
                ArrayList arrayList = new ArrayList();
                arrayList.add(Integer.valueOf(obj));
                ShelfTestDialog.this.testShelf(arrayList, 0, false);
                return;
            }
            if (ShelfTestDialog.this.mode != 1) {
                if (ShelfTestDialog.this.mode != 2) {
                    if (ShelfTestDialog.this.mode == 3) {
                        String obj2 = ShelfTestDialog.this.et_input01.getText().toString();
                        String obj3 = ShelfTestDialog.this.et_input02.getText().toString();
                        if (TextUtils.isEmpty(obj2) || TextUtils.isEmpty(obj3)) {
                            ToastUitl.showNotInputTip(ShelfTestDialog.this.context);
                            return;
                        }
                        int intValue = Integer.valueOf(obj2).intValue();
                        int intValue2 = Integer.valueOf(obj3).intValue();
                        if (intValue >= intValue2) {
                            ToastUitl.showNotInputTip(ShelfTestDialog.this.context);
                            return;
                        }
                        ShelfTestDialog.this.mutilTextTipDialog = new MutilTextTipDialog(ShelfTestDialog.this.context);
                        ShelfTestDialog.this.mutilTextTipDialog.show();
                        ShelfTestDialog.this.mutilTextTipDialog.addTextShow(ShelfTestDialog.this.context.getString(R.string.test_start));
                        ArrayList arrayList2 = new ArrayList();
                        while (intValue <= intValue2) {
                            arrayList2.add(Integer.valueOf(intValue));
                            intValue++;
                        }
                        ShelfTestDialog.this.testShelf(arrayList2, 0, true);
                        return;
                    }
                    return;
                }
                ShelfTestDialog.this.mutilTextTipDialog = new MutilTextTipDialog(ShelfTestDialog.this.context);
                ShelfTestDialog.this.mutilTextTipDialog.show();
                ShelfTestDialog.this.mutilTextTipDialog.addTextShow(ShelfTestDialog.this.context.getString(R.string.test_start));
                ShelfTestDialog.this.testShelf(SettingActivity.getBasicMachineInfo().shelvesMap.get(Integer.valueOf(SettingActivity.getBasicMachineInfo().cabinetList.get(ShelfTestDialog.this.spinner_type.getSelectedItemPosition()).intValue())), 0, true);
                return;
            }
            ShelfTestDialog.this.mutilTextTipDialog = new MutilTextTipDialog(ShelfTestDialog.this.context);
            ShelfTestDialog.this.mutilTextTipDialog.show();
            ShelfTestDialog.this.mutilTextTipDialog.addTextShow(ShelfTestDialog.this.context.getString(R.string.test_start));
            ShelfTestDialog.this.testShelf(SettingActivity.getBasicMachineInfo().shelvesLayerMap.get(Integer.valueOf(SettingActivity.getBasicMachineInfo().layerNumberList.get(ShelfTestDialog.this.spinner_type.getSelectedItemPosition()).intValue())), 0, true);
        }
    }

    private void showContent() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.context, R.layout.layout_spinner_item_simple, this.context.getResources().getStringArray(R.array.shelf_test_type));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner_mode.setAdapter((SpinnerAdapter) arrayAdapter);
        this.spinner_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.shj.setting.Dialog.ShelfTestDialog.3
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            AnonymousClass3() {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ShelfTestDialog.this.mode = i;
                ShelfTestDialog.this.AdapteMode(i);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.ShelfTestDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements AdapterView.OnItemSelectedListener {
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        AnonymousClass3() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            ShelfTestDialog.this.mode = i;
            ShelfTestDialog.this.AdapteMode(i);
        }
    }

    public void AdapteMode(int i) {
        if (i == 0) {
            this.ll_01.setVisibility(0);
            this.ll_02.setVisibility(8);
            this.ll_spinner_type.setVisibility(8);
            this.tv_name01.setText(R.string.lab_shelf);
            return;
        }
        if (i == 1) {
            this.ll_01.setVisibility(8);
            this.ll_02.setVisibility(8);
            this.ll_spinner_type.setVisibility(0);
            this.tv_name_spinner.setText(R.string.layer_number);
            List<Integer> list = SettingActivity.getBasicMachineInfo().layerNumberList;
            ArrayList arrayList = new ArrayList();
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(String.valueOf(it.next().intValue()));
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this.context, R.layout.layout_spinner_item_simple, arrayList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner_type.setAdapter((SpinnerAdapter) arrayAdapter);
            return;
        }
        if (i != 2) {
            if (i == 3) {
                this.ll_01.setVisibility(0);
                this.ll_02.setVisibility(0);
                this.ll_spinner_type.setVisibility(8);
                this.tv_name01.setText(R.string.initial_cargo_number);
                this.tv_name02.setText(R.string.stop_shipment_no);
                return;
            }
            return;
        }
        this.ll_01.setVisibility(8);
        this.ll_02.setVisibility(8);
        this.ll_spinner_type.setVisibility(0);
        this.tv_name_spinner.setText(R.string.cabinet_number);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this.context, R.layout.layout_spinner_item_simple, SettingActivity.getBasicMachineInfo().cabinetNumberList);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner_type.setAdapter((SpinnerAdapter) arrayAdapter2);
    }

    public void testShelf(List<Integer> list, int i, boolean z) {
        if (list == null || i >= list.size()) {
            return;
        }
        int intValue = list.get(i).intValue();
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.TestShelf(false, 1, intValue);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.ShelfTestDialog.4
            final /* synthetic */ int val$index;
            final /* synthetic */ boolean val$isNeedTestNext;
            final /* synthetic */ int val$shelf;
            final /* synthetic */ List val$shelfList;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z2) {
            }

            AnonymousClass4(int intValue2, boolean z2, int i2, List list2) {
                intValue = intValue2;
                z = z2;
                i = i2;
                list = list2;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr.toString().equals("0")) {
                    return;
                }
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                Loger.writeLog("SET", "截取的下位机的返回的值是:" + intFromBytes);
                String shelfState = SetUtils.getShelfState(ShelfTestDialog.this.context, intFromBytes, intValue);
                if (z) {
                    if (ShelfTestDialog.this.mutilTextTipDialog != null) {
                        ShelfTestDialog.this.mutilTextTipDialog.addTextShow(shelfState);
                        ShelfTestDialog.this.mutilTextTipDialog.addStatisticalInfo(intFromBytes, SetUtils.getShelfState(ShelfTestDialog.this.context, intFromBytes));
                    } else {
                        ToastUitl.showShort(ShelfTestDialog.this.context, SetUtils.getShelfState(ShelfTestDialog.this.context, intFromBytes));
                    }
                    if (i + 1 < list.size()) {
                        ShelfTestDialog.this.testShelf(list, i + 1, z);
                        return;
                    } else {
                        ShelfTestDialog.this.mutilTextTipDialog.addTextShow(ShelfTestDialog.this.context.getResources().getString(R.string.test_complete));
                        return;
                    }
                }
                ToastUitl.showShort(ShelfTestDialog.this.context, SetUtils.getShelfState(ShelfTestDialog.this.context, intFromBytes));
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.ShelfTestDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements OnCommandAnswerListener {
        final /* synthetic */ int val$index;
        final /* synthetic */ boolean val$isNeedTestNext;
        final /* synthetic */ int val$shelf;
        final /* synthetic */ List val$shelfList;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
        }

        AnonymousClass4(int intValue2, boolean z2, int i2, List list2) {
            intValue = intValue2;
            z = z2;
            i = i2;
            list = list2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr.toString().equals("0")) {
                return;
            }
            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
            Loger.writeLog("SET", "截取的下位机的返回的值是:" + intFromBytes);
            String shelfState = SetUtils.getShelfState(ShelfTestDialog.this.context, intFromBytes, intValue);
            if (z) {
                if (ShelfTestDialog.this.mutilTextTipDialog != null) {
                    ShelfTestDialog.this.mutilTextTipDialog.addTextShow(shelfState);
                    ShelfTestDialog.this.mutilTextTipDialog.addStatisticalInfo(intFromBytes, SetUtils.getShelfState(ShelfTestDialog.this.context, intFromBytes));
                } else {
                    ToastUitl.showShort(ShelfTestDialog.this.context, SetUtils.getShelfState(ShelfTestDialog.this.context, intFromBytes));
                }
                if (i + 1 < list.size()) {
                    ShelfTestDialog.this.testShelf(list, i + 1, z);
                    return;
                } else {
                    ShelfTestDialog.this.mutilTextTipDialog.addTextShow(ShelfTestDialog.this.context.getResources().getString(R.string.test_complete));
                    return;
                }
            }
            ToastUitl.showShort(ShelfTestDialog.this.context, SetUtils.getShelfState(ShelfTestDialog.this.context, intFromBytes));
        }
    }
}
