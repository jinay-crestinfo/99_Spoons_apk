package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.oysb.utils.cache.CacheHelper;
import com.shj.setting.R;
import com.shj.setting.SearchSettingItemAdapter;
import com.shj.setting.Utils.PinYinSearch;
import com.shj.setting.mainSettingItem.SettingTypeName;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class SelectEnabledDialog extends Dialog {
    private Button bt_bank_machine;
    private Button bt_box;
    private Button bt_cancel;
    private Button bt_hide_input;
    private Button bt_marking_lift;
    private Button bt_marking_no_lift;
    private Button bt_ok;
    private Button bt_side;
    private Button bt_side_long_belt;
    private Button bt_syj;
    private CheckBox cb_bank;
    private CheckBox cb_cash;
    private Context context;
    private List<EnabledData> enabledDataList;
    private EditText et_search;
    private ListView listView;
    private ListView lv_search;
    private UserSettingDao mUserSettingDao;
    private MyAdapter myAdapter;
    private List<SearchSettingItemAdapter.SearchAdapterData> searchDataList;
    private SearchSettingItemAdapter searchSettingItemAdapter;
    private SelectEnabledDialogListener selectEnabledDialogListener;
    private TextView tv_bank;
    private TextView tv_cash;
    private int type;

    /* loaded from: classes2.dex */
    public static class EnabledData {
        public boolean isEnable;
        public String name;
        public boolean oldIsEnable;
        public int type;
    }

    /* loaded from: classes2.dex */
    public interface SelectEnabledDialogListener {
        void buttonClick_OK();
    }

    public SelectEnabledDialog(Context context, UserSettingDao userSettingDao) {
        super(context, R.style.loading_style);
        this.searchDataList = new ArrayList();
        this.context = context;
        this.mUserSettingDao = userSettingDao;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setSoftInputMode(32);
        setContentView(R.layout.layout_setting_enable);
        this.listView = (ListView) findViewById(R.id.listView);
        this.bt_marking_lift = (Button) findViewById(R.id.bt_marking_lift);
        this.bt_marking_no_lift = (Button) findViewById(R.id.bt_marking_no_lift);
        this.bt_box = (Button) findViewById(R.id.bt_box);
        this.tv_bank = (TextView) findViewById(R.id.tv_bank);
        this.cb_bank = (CheckBox) findViewById(R.id.cb_bank);
        this.bt_side = (Button) findViewById(R.id.bt_side);
        this.bt_bank_machine = (Button) findViewById(R.id.bt_bank_machine);
        this.bt_side_long_belt = (Button) findViewById(R.id.bt_side_long_belt);
        this.bt_syj = (Button) findViewById(R.id.bt_syj);
        this.cb_cash = (CheckBox) findViewById(R.id.cb_cash);
        this.tv_cash = (TextView) findViewById(R.id.tv_cash);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        this.bt_cancel = (Button) findViewById(R.id.bt_cancel);
        EditText editText = (EditText) findViewById(R.id.et_search);
        this.et_search = editText;
        editText.clearFocus();
        this.lv_search = (ListView) findViewById(R.id.lv_search);
        this.bt_hide_input = (Button) findViewById(R.id.bt_hide_input);
        setCanceledOnTouchOutside(false);
        SearchSettingItemAdapter searchSettingItemAdapter = new SearchSettingItemAdapter(this.context, this.searchDataList);
        this.searchSettingItemAdapter = searchSettingItemAdapter;
        this.lv_search.setAdapter((ListAdapter) searchSettingItemAdapter);
        setListener();
        showList();
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            for (EnabledData enabledData : SelectEnabledDialog.this.enabledDataList) {
                if (enabledData.isEnable != enabledData.oldIsEnable) {
                    AppSetting.saveSettingEnabled(SelectEnabledDialog.this.context, enabledData.type, enabledData.isEnable, SelectEnabledDialog.this.mUserSettingDao);
                }
            }
            SelectEnabledDialog.this.dismiss();
            if (SelectEnabledDialog.this.selectEnabledDialogListener != null) {
                SelectEnabledDialog.this.selectEnabledDialogListener.buttonClick_OK();
            }
            if (SelectEnabledDialog.this.type == 4) {
                CacheHelper.getFileCache().put("institutiona_function_type", "sy");
            } else {
                CacheHelper.getFileCache().remove("institutiona_function_type");
            }
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                for (EnabledData enabledData : SelectEnabledDialog.this.enabledDataList) {
                    if (enabledData.isEnable != enabledData.oldIsEnable) {
                        AppSetting.saveSettingEnabled(SelectEnabledDialog.this.context, enabledData.type, enabledData.isEnable, SelectEnabledDialog.this.mUserSettingDao);
                    }
                }
                SelectEnabledDialog.this.dismiss();
                if (SelectEnabledDialog.this.selectEnabledDialogListener != null) {
                    SelectEnabledDialog.this.selectEnabledDialogListener.buttonClick_OK();
                }
                if (SelectEnabledDialog.this.type == 4) {
                    CacheHelper.getFileCache().put("institutiona_function_type", "sy");
                } else {
                    CacheHelper.getFileCache().remove("institutiona_function_type");
                }
            }
        });
        this.bt_cancel.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectEnabledDialog.this.dismiss();
            }
        });
        this.bt_marking_lift.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectEnabledDialog.this.type = 1;
                SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
                selectEnabledDialog.selectList(selectEnabledDialog.getMarkingLiftSettingType());
            }
        });
        this.bt_marking_no_lift.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectEnabledDialog.this.type = 2;
                SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
                selectEnabledDialog.selectList(selectEnabledDialog.getMarkingNoLiftSettingType());
            }
        });
        this.bt_box.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectEnabledDialog.this.type = 3;
                SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
                selectEnabledDialog.selectList(selectEnabledDialog.getBoxSettingType());
            }
        });
        this.bt_syj.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.6
            AnonymousClass6() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectEnabledDialog.this.type = 4;
                SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
                selectEnabledDialog.selectList(selectEnabledDialog.getSYJSettingType());
            }
        });
        this.bt_side.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.7
            AnonymousClass7() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectEnabledDialog.this.type = 5;
                SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
                selectEnabledDialog.selectList(selectEnabledDialog.getSideSettingType());
            }
        });
        this.bt_side_long_belt.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.8
            AnonymousClass8() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectEnabledDialog.this.type = 6;
                SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
                selectEnabledDialog.selectList(selectEnabledDialog.getSideLongBeltSettingType());
            }
        });
        this.bt_bank_machine.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.9
            AnonymousClass9() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectEnabledDialog.this.type = 7;
                SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
                selectEnabledDialog.selectList(selectEnabledDialog.getBankSettingType());
            }
        });
        this.tv_bank.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.10
            AnonymousClass10() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SelectEnabledDialog.this.cb_bank.isChecked()) {
                    SelectEnabledDialog.this.cb_bank.setChecked(false);
                } else {
                    SelectEnabledDialog.this.cb_bank.setChecked(true);
                }
            }
        });
        this.cb_bank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.11
            AnonymousClass11() {
            }

            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
                selectEnabledDialog.setCheckList(selectEnabledDialog.getBankSelectType(), z);
            }
        });
        this.tv_cash.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.12
            AnonymousClass12() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SelectEnabledDialog.this.cb_cash.isChecked()) {
                    SelectEnabledDialog.this.cb_cash.setChecked(false);
                } else {
                    SelectEnabledDialog.this.cb_cash.setChecked(true);
                }
            }
        });
        this.cb_cash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.13
            AnonymousClass13() {
            }

            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
                selectEnabledDialog.setCheckList(selectEnabledDialog.getCashSettingType(), z);
            }
        });
        this.et_search.addTextChangedListener(new TextWatcher() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.14
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass14() {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                PinYinSearch.getSearchSettingItemData(SelectEnabledDialog.this.context, charSequence.toString(), SelectEnabledDialog.this.searchDataList, SelectEnabledDialog.this.mUserSettingDao, true);
                if (SelectEnabledDialog.this.searchDataList.size() > 0) {
                    SelectEnabledDialog.this.lv_search.setVisibility(0);
                    SelectEnabledDialog.this.searchSettingItemAdapter.notifyDataSetChanged();
                } else {
                    SelectEnabledDialog.this.lv_search.setVisibility(8);
                }
            }
        });
        this.bt_hide_input.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.15
            AnonymousClass15() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectEnabledDialog.this.hideInput();
            }
        });
        this.lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.16
            AnonymousClass16() {
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                SearchSettingItemAdapter.SearchAdapterData searchAdapterData = (SearchSettingItemAdapter.SearchAdapterData) SelectEnabledDialog.this.searchDataList.get(i);
                EnabledData enabledData = new EnabledData();
                enabledData.name = searchAdapterData.name;
                enabledData.type = searchAdapterData.settingType;
                enabledData.isEnable = AppSetting.isSettingEnabled(SelectEnabledDialog.this.context, enabledData.type, SelectEnabledDialog.this.mUserSettingDao);
                enabledData.oldIsEnable = enabledData.isEnable;
                SelectEnabledDialog.this.enabledDataList.clear();
                SelectEnabledDialog.this.enabledDataList.add(enabledData);
                SelectEnabledDialog.this.myAdapter.notifyDataSetChanged();
                SelectEnabledDialog.this.searchDataList.clear();
                SelectEnabledDialog.this.lv_search.setVisibility(8);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectEnabledDialog.this.dismiss();
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectEnabledDialog.this.type = 1;
            SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
            selectEnabledDialog.selectList(selectEnabledDialog.getMarkingLiftSettingType());
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectEnabledDialog.this.type = 2;
            SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
            selectEnabledDialog.selectList(selectEnabledDialog.getMarkingNoLiftSettingType());
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectEnabledDialog.this.type = 3;
            SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
            selectEnabledDialog.selectList(selectEnabledDialog.getBoxSettingType());
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements View.OnClickListener {
        AnonymousClass6() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectEnabledDialog.this.type = 4;
            SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
            selectEnabledDialog.selectList(selectEnabledDialog.getSYJSettingType());
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements View.OnClickListener {
        AnonymousClass7() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectEnabledDialog.this.type = 5;
            SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
            selectEnabledDialog.selectList(selectEnabledDialog.getSideSettingType());
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements View.OnClickListener {
        AnonymousClass8() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectEnabledDialog.this.type = 6;
            SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
            selectEnabledDialog.selectList(selectEnabledDialog.getSideLongBeltSettingType());
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements View.OnClickListener {
        AnonymousClass9() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectEnabledDialog.this.type = 7;
            SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
            selectEnabledDialog.selectList(selectEnabledDialog.getBankSettingType());
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 implements View.OnClickListener {
        AnonymousClass10() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SelectEnabledDialog.this.cb_bank.isChecked()) {
                SelectEnabledDialog.this.cb_bank.setChecked(false);
            } else {
                SelectEnabledDialog.this.cb_bank.setChecked(true);
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$11 */
    /* loaded from: classes2.dex */
    public class AnonymousClass11 implements CompoundButton.OnCheckedChangeListener {
        AnonymousClass11() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
            selectEnabledDialog.setCheckList(selectEnabledDialog.getBankSelectType(), z);
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$12 */
    /* loaded from: classes2.dex */
    public class AnonymousClass12 implements View.OnClickListener {
        AnonymousClass12() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SelectEnabledDialog.this.cb_cash.isChecked()) {
                SelectEnabledDialog.this.cb_cash.setChecked(false);
            } else {
                SelectEnabledDialog.this.cb_cash.setChecked(true);
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$13 */
    /* loaded from: classes2.dex */
    public class AnonymousClass13 implements CompoundButton.OnCheckedChangeListener {
        AnonymousClass13() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            SelectEnabledDialog selectEnabledDialog = SelectEnabledDialog.this;
            selectEnabledDialog.setCheckList(selectEnabledDialog.getCashSettingType(), z);
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$14 */
    /* loaded from: classes2.dex */
    public class AnonymousClass14 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass14() {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            PinYinSearch.getSearchSettingItemData(SelectEnabledDialog.this.context, charSequence.toString(), SelectEnabledDialog.this.searchDataList, SelectEnabledDialog.this.mUserSettingDao, true);
            if (SelectEnabledDialog.this.searchDataList.size() > 0) {
                SelectEnabledDialog.this.lv_search.setVisibility(0);
                SelectEnabledDialog.this.searchSettingItemAdapter.notifyDataSetChanged();
            } else {
                SelectEnabledDialog.this.lv_search.setVisibility(8);
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$15 */
    /* loaded from: classes2.dex */
    public class AnonymousClass15 implements View.OnClickListener {
        AnonymousClass15() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectEnabledDialog.this.hideInput();
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$16 */
    /* loaded from: classes2.dex */
    public class AnonymousClass16 implements AdapterView.OnItemClickListener {
        AnonymousClass16() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            SearchSettingItemAdapter.SearchAdapterData searchAdapterData = (SearchSettingItemAdapter.SearchAdapterData) SelectEnabledDialog.this.searchDataList.get(i);
            EnabledData enabledData = new EnabledData();
            enabledData.name = searchAdapterData.name;
            enabledData.type = searchAdapterData.settingType;
            enabledData.isEnable = AppSetting.isSettingEnabled(SelectEnabledDialog.this.context, enabledData.type, SelectEnabledDialog.this.mUserSettingDao);
            enabledData.oldIsEnable = enabledData.isEnable;
            SelectEnabledDialog.this.enabledDataList.clear();
            SelectEnabledDialog.this.enabledDataList.add(enabledData);
            SelectEnabledDialog.this.myAdapter.notifyDataSetChanged();
            SelectEnabledDialog.this.searchDataList.clear();
            SelectEnabledDialog.this.lv_search.setVisibility(8);
        }
    }

    public void hideInput() {
        ((InputMethodManager) this.context.getSystemService("input_method")).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
    }

    public void selectList(List<Integer> list) {
        boolean z;
        for (EnabledData enabledData : this.enabledDataList) {
            Iterator<Integer> it = list.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().intValue() == enabledData.type) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (z) {
                enabledData.isEnable = false;
            } else {
                enabledData.isEnable = true;
            }
        }
        this.myAdapter.notifyDataSetChanged();
    }

    public void setCheckList(List<Integer> list, boolean z) {
        for (EnabledData enabledData : this.enabledDataList) {
            Iterator<Integer> it = list.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().intValue() == enabledData.type) {
                        enabledData.isEnable = z;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        this.myAdapter.notifyDataSetChanged();
    }

    public List<Integer> getMarkingLiftSettingType() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(117);
        arrayList.add(118);
        arrayList.add(119);
        arrayList.add(120);
        arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
        arrayList.add(149);
        arrayList.add(163);
        arrayList.add(158);
        arrayList.add(159);
        arrayList.add(166);
        arrayList.add(254);
        arrayList.add(Integer.valueOf(SettingType.PRINTER));
        arrayList.add(Integer.valueOf(SettingType.SCAVENGING_WHARF));
        arrayList.add(177);
        arrayList.add(182);
        arrayList.add(183);
        arrayList.add(184);
        arrayList.add(185);
        arrayList.add(Integer.valueOf(SettingType.BANKNOTE_CHANGE_OF_MONEY));
        arrayList.add(187);
        arrayList.add(Integer.valueOf(SettingType.COIN_QUERY));
        arrayList.add(189);
        arrayList.add(Integer.valueOf(SettingType.SHOW_BALANCE));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YL));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YLX));
        arrayList.add(211);
        arrayList.add(213);
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_JF));
        arrayList.add(Integer.valueOf(SettingType.MONETARY_SYMBOL));
        arrayList.add(Integer.valueOf(SettingType.CALL_PHONE));
        arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_MATERIAL_FILE));
        arrayList.add(256);
        arrayList.add(257);
        arrayList.add(258);
        arrayList.add(Integer.valueOf(SettingType.MICROWAVE_OVEN_POSITIONING));
        arrayList.add(Integer.valueOf(SettingType.MANUAL_POSITIONING_OF_BOXES));
        arrayList.add(234);
        arrayList.add(235);
        arrayList.add(246);
        arrayList.add(247);
        arrayList.add(Integer.valueOf(SettingType.SETTING_UP_HUMIDIFIER));
        arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME));
        arrayList.add(Integer.valueOf(SettingType.BOX_RICE_MACHINE_CABINET_SETTING));
        arrayList.add(Integer.valueOf(SettingType.DEVICE_SCAN_PORT_ADDERS_YR));
        arrayList.add(Integer.valueOf(SettingType.ALWAYS_HEATING));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID_ALL));
        arrayList.add(307);
        arrayList.add(308);
        arrayList.add(Integer.valueOf(SettingType.LIGHT_INSPECTION_STATUS_QUERY));
        arrayList.add(Integer.valueOf(SettingType.CALCULATED_INVENTORY));
        arrayList.add(Integer.valueOf(SettingType.MAINCONTOL_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_VERSION_QUERY));
        arrayList.add(Integer.valueOf(SettingType.SCAN_BARCODE_REPLENISHMENT));
        arrayList.add(Integer.valueOf(SettingType.HEART_DIALOG));
        arrayList.add(Integer.valueOf(SettingType.TOP_LIGHT_CONTROL));
        arrayList.add(Integer.valueOf(SettingType.LIGHT_BOX_ROLLING_INTERVAL));
        arrayList.add(332);
        arrayList.add(Integer.valueOf(SettingType.WORK_MODE));
        arrayList.add(Integer.valueOf(SettingType.DRUG_BOX_MENU_NAME));
        arrayList.add(Integer.valueOf(SettingType.PRACTICE_MODE));
        arrayList.add(Integer.valueOf(SettingType.MICROPHONE_TEST));
        arrayList.add(Integer.valueOf(SettingType.LIGHTBOX_CANVAS));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_TEST));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_PID_VID));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_TEST));
        arrayList.add(Integer.valueOf(SettingType.CAMERA_AUTO_TAKE));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_SENSITIVITY));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_DISTANCE));
        arrayList.add(Integer.valueOf(SettingType.MAIN_BOARD_SEQUENCE_NUMBER));
        arrayList.add(Integer.valueOf(SettingType.LOG_SETTING));
        arrayList.add(Integer.valueOf(SettingType.ID_CARD_READER_TEST));
        arrayList.add(Integer.valueOf(SettingType.GRID_MACHINE_FULL_LOAD));
        arrayList.add(354);
        return arrayList;
    }

    public List<Integer> getMarkingNoLiftSettingType() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(117);
        arrayList.add(118);
        arrayList.add(119);
        arrayList.add(120);
        arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
        arrayList.add(149);
        arrayList.add(158);
        arrayList.add(159);
        arrayList.add(166);
        arrayList.add(254);
        arrayList.add(Integer.valueOf(SettingType.PRINTER));
        arrayList.add(Integer.valueOf(SettingType.SCAVENGING_WHARF));
        arrayList.add(177);
        arrayList.add(182);
        arrayList.add(183);
        arrayList.add(184);
        arrayList.add(185);
        arrayList.add(Integer.valueOf(SettingType.BANKNOTE_CHANGE_OF_MONEY));
        arrayList.add(187);
        arrayList.add(Integer.valueOf(SettingType.COIN_QUERY));
        arrayList.add(189);
        arrayList.add(Integer.valueOf(SettingType.SHOW_BALANCE));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YL));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YLX));
        arrayList.add(211);
        arrayList.add(213);
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_JF));
        arrayList.add(Integer.valueOf(SettingType.MONETARY_SYMBOL));
        arrayList.add(Integer.valueOf(SettingType.CALL_PHONE));
        arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_MATERIAL_FILE));
        arrayList.add(256);
        arrayList.add(257);
        arrayList.add(162);
        arrayList.add(227);
        arrayList.add(Integer.valueOf(SettingType.CONNECTING_ELEVATOR));
        arrayList.add(229);
        arrayList.add(230);
        arrayList.add(231);
        arrayList.add(Integer.valueOf(SettingType.MICROWAVE_OVEN_POSITIONING));
        arrayList.add(Integer.valueOf(SettingType.MANUAL_POSITIONING_OF_BOXES));
        arrayList.add(234);
        arrayList.add(235);
        arrayList.add(242);
        arrayList.add(243);
        arrayList.add(244);
        arrayList.add(245);
        arrayList.add(246);
        arrayList.add(247);
        arrayList.add(258);
        arrayList.add(Integer.valueOf(SettingType.SETTING_UP_HUMIDIFIER));
        arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME));
        arrayList.add(Integer.valueOf(SettingType.BOX_RICE_MACHINE_CABINET_SETTING));
        arrayList.add(Integer.valueOf(SettingType.DEVICE_SCAN_PORT_ADDERS_YR));
        arrayList.add(Integer.valueOf(SettingType.ALWAYS_HEATING));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID_ALL));
        arrayList.add(307);
        arrayList.add(308);
        arrayList.add(Integer.valueOf(SettingType.LIGHT_INSPECTION_STATUS_QUERY));
        arrayList.add(Integer.valueOf(SettingType.CALCULATED_INVENTORY));
        arrayList.add(Integer.valueOf(SettingType.MAINCONTOL_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_VERSION_QUERY));
        arrayList.add(Integer.valueOf(SettingType.SCAN_BARCODE_REPLENISHMENT));
        arrayList.add(Integer.valueOf(SettingType.HEART_DIALOG));
        arrayList.add(Integer.valueOf(SettingType.TOP_LIGHT_CONTROL));
        arrayList.add(Integer.valueOf(SettingType.LIGHT_BOX_ROLLING_INTERVAL));
        arrayList.add(Integer.valueOf(SettingType.LIGHTBOX_CANVAS));
        arrayList.add(332);
        arrayList.add(Integer.valueOf(SettingType.WORK_MODE));
        arrayList.add(Integer.valueOf(SettingType.DRUG_BOX_MENU_NAME));
        arrayList.add(Integer.valueOf(SettingType.PRACTICE_MODE));
        arrayList.add(Integer.valueOf(SettingType.MICROPHONE_TEST));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_TEST));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_PID_VID));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_TEST));
        arrayList.add(Integer.valueOf(SettingType.CAMERA_AUTO_TAKE));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_SENSITIVITY));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_DISTANCE));
        arrayList.add(Integer.valueOf(SettingType.MAIN_BOARD_SEQUENCE_NUMBER));
        arrayList.add(Integer.valueOf(SettingType.LOG_SETTING));
        arrayList.add(Integer.valueOf(SettingType.ID_CARD_READER_TEST));
        arrayList.add(Integer.valueOf(SettingType.GRID_MACHINE_FULL_LOAD));
        arrayList.add(354);
        return arrayList;
    }

    public List<Integer> getBoxSettingType() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(117);
        arrayList.add(118);
        arrayList.add(119);
        arrayList.add(120);
        arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
        arrayList.add(149);
        arrayList.add(163);
        arrayList.add(158);
        arrayList.add(159);
        arrayList.add(166);
        arrayList.add(Integer.valueOf(SettingType.PRINTER));
        arrayList.add(Integer.valueOf(SettingType.SCAVENGING_WHARF));
        arrayList.add(182);
        arrayList.add(183);
        arrayList.add(184);
        arrayList.add(185);
        arrayList.add(Integer.valueOf(SettingType.BANKNOTE_CHANGE_OF_MONEY));
        arrayList.add(187);
        arrayList.add(Integer.valueOf(SettingType.COIN_QUERY));
        arrayList.add(189);
        arrayList.add(Integer.valueOf(SettingType.SHOW_BALANCE));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YL));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YLX));
        arrayList.add(211);
        arrayList.add(213);
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_JF));
        arrayList.add(Integer.valueOf(SettingType.MONETARY_SYMBOL));
        arrayList.add(Integer.valueOf(SettingType.CALL_PHONE));
        arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_MATERIAL_FILE));
        arrayList.add(227);
        arrayList.add(Integer.valueOf(SettingType.KEFU_PHONE));
        arrayList.add(Integer.valueOf(SettingType.SETTING_UP_HUMIDIFIER));
        arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME));
        arrayList.add(Integer.valueOf(SettingType.DEVICE_SCAN_PORT_ADDERS_YR));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID_ALL));
        arrayList.add(307);
        arrayList.add(308);
        arrayList.add(Integer.valueOf(SettingType.LIGHT_INSPECTION_STATUS_QUERY));
        arrayList.add(Integer.valueOf(SettingType.CALCULATED_INVENTORY));
        arrayList.add(Integer.valueOf(SettingType.MAINCONTOL_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_VERSION_QUERY));
        arrayList.add(Integer.valueOf(SettingType.SCAN_BARCODE_REPLENISHMENT));
        arrayList.add(238);
        arrayList.add(239);
        arrayList.add(240);
        arrayList.add(245);
        arrayList.add(246);
        arrayList.add(Integer.valueOf(SettingType.DEVICE_FACE_SN));
        arrayList.add(Integer.valueOf(SettingType.TOP_LIGHT_CONTROL));
        arrayList.add(Integer.valueOf(SettingType.LIGHT_BOX_ROLLING_INTERVAL));
        arrayList.add(Integer.valueOf(SettingType.LIGHTBOX_CANVAS));
        arrayList.add(332);
        arrayList.add(Integer.valueOf(SettingType.WORK_MODE));
        arrayList.add(Integer.valueOf(SettingType.DRUG_BOX_MENU_NAME));
        arrayList.add(Integer.valueOf(SettingType.PRACTICE_MODE));
        arrayList.add(Integer.valueOf(SettingType.MICROPHONE_TEST));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_TEST));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_PID_VID));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_TEST));
        arrayList.add(Integer.valueOf(SettingType.CAMERA_AUTO_TAKE));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_SENSITIVITY));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_DISTANCE));
        arrayList.add(Integer.valueOf(SettingType.MAIN_BOARD_SEQUENCE_NUMBER));
        arrayList.add(Integer.valueOf(SettingType.LOG_SETTING));
        arrayList.add(Integer.valueOf(SettingType.ID_CARD_READER_TEST));
        arrayList.add(Integer.valueOf(SettingType.GRID_MACHINE_FULL_LOAD));
        arrayList.add(354);
        return arrayList;
    }

    public List<Integer> getBankSelectType() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID_ALL));
        arrayList.add(307);
        arrayList.add(308);
        arrayList.add(Integer.valueOf(SettingType.GRID_MACHINE_FULL_LOAD));
        arrayList.add(354);
        return arrayList;
    }

    public List<Integer> getBankSettingType() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(166);
        arrayList.add(254);
        arrayList.add(177);
        arrayList.add(Integer.valueOf(SettingType.CALL_PHONE));
        arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_MATERIAL_FILE));
        arrayList.add(259);
        arrayList.add(Integer.valueOf(SettingType.SHOW_BALANCE));
        arrayList.add(117);
        arrayList.add(118);
        arrayList.add(119);
        arrayList.add(120);
        arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
        arrayList.add(Integer.valueOf(SettingType.DROP_INSPECTION));
        arrayList.add(145);
        arrayList.add(149);
        arrayList.add(158);
        arrayList.add(159);
        arrayList.add(Integer.valueOf(SettingType.PRINTER));
        arrayList.add(Integer.valueOf(SettingType.SCAVENGING_WHARF));
        arrayList.add(182);
        arrayList.add(183);
        arrayList.add(184);
        arrayList.add(185);
        arrayList.add(Integer.valueOf(SettingType.BANKNOTE_CHANGE_OF_MONEY));
        arrayList.add(187);
        arrayList.add(Integer.valueOf(SettingType.COIN_QUERY));
        arrayList.add(189);
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YL));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YLX));
        arrayList.add(211);
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_JF));
        arrayList.add(227);
        arrayList.add(Integer.valueOf(SettingType.CONNECTING_ELEVATOR));
        arrayList.add(229);
        arrayList.add(230);
        arrayList.add(231);
        arrayList.add(Integer.valueOf(SettingType.MICROWAVE_OVEN_POSITIONING));
        arrayList.add(Integer.valueOf(SettingType.MANUAL_POSITIONING_OF_BOXES));
        arrayList.add(234);
        arrayList.add(235);
        arrayList.add(238);
        arrayList.add(239);
        arrayList.add(240);
        arrayList.add(241);
        arrayList.add(242);
        arrayList.add(243);
        arrayList.add(244);
        arrayList.add(245);
        arrayList.add(246);
        arrayList.add(247);
        arrayList.add(256);
        arrayList.add(257);
        arrayList.add(258);
        arrayList.add(Integer.valueOf(SettingType.KEFU_PHONE));
        arrayList.add(Integer.valueOf(SettingType.SETTING_UP_HUMIDIFIER));
        arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME));
        arrayList.add(Integer.valueOf(SettingType.BOX_RICE_MACHINE_CABINET_SETTING));
        arrayList.add(Integer.valueOf(SettingType.LIGHT_INSPECTION_STATUS_QUERY));
        arrayList.add(Integer.valueOf(SettingType.CALCULATED_INVENTORY));
        arrayList.add(Integer.valueOf(SettingType.MAINCONTOL_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_VERSION_QUERY));
        arrayList.add(Integer.valueOf(SettingType.SCAN_BARCODE_REPLENISHMENT));
        arrayList.add(Integer.valueOf(SettingType.HEART_DIALOG));
        arrayList.add(Integer.valueOf(SettingType.DEVICE_FACE_SN));
        arrayList.add(Integer.valueOf(SettingType.LIGHTBOX_CANVAS));
        arrayList.add(332);
        arrayList.add(Integer.valueOf(SettingType.WORK_MODE));
        arrayList.add(Integer.valueOf(SettingType.DRUG_BOX_MENU_NAME));
        arrayList.add(Integer.valueOf(SettingType.PRACTICE_MODE));
        arrayList.add(Integer.valueOf(SettingType.MICROPHONE_TEST));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_TEST));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_PID_VID));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_TEST));
        arrayList.add(Integer.valueOf(SettingType.CAMERA_AUTO_TAKE));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_SENSITIVITY));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_DISTANCE));
        arrayList.add(Integer.valueOf(SettingType.MAIN_BOARD_SEQUENCE_NUMBER));
        arrayList.add(Integer.valueOf(SettingType.LOG_SETTING));
        arrayList.add(Integer.valueOf(SettingType.ID_CARD_READER_TEST));
        arrayList.add(Integer.valueOf(SettingType.GRID_MACHINE_FULL_LOAD));
        arrayList.add(354);
        return arrayList;
    }

    public List<Integer> getSideSettingType() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(117);
        arrayList.add(118);
        arrayList.add(119);
        arrayList.add(120);
        arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
        arrayList.add(149);
        arrayList.add(163);
        arrayList.add(158);
        arrayList.add(159);
        arrayList.add(166);
        arrayList.add(Integer.valueOf(SettingType.PRINTER));
        arrayList.add(Integer.valueOf(SettingType.SCAVENGING_WHARF));
        arrayList.add(177);
        arrayList.add(182);
        arrayList.add(183);
        arrayList.add(184);
        arrayList.add(185);
        arrayList.add(Integer.valueOf(SettingType.BANKNOTE_CHANGE_OF_MONEY));
        arrayList.add(187);
        arrayList.add(Integer.valueOf(SettingType.COIN_QUERY));
        arrayList.add(189);
        arrayList.add(Integer.valueOf(SettingType.SHOW_BALANCE));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YL));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YLX));
        arrayList.add(211);
        arrayList.add(213);
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_JF));
        arrayList.add(Integer.valueOf(SettingType.MONETARY_SYMBOL));
        arrayList.add(Integer.valueOf(SettingType.CALL_PHONE));
        arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_MATERIAL_FILE));
        arrayList.add(235);
        arrayList.add(Integer.valueOf(SettingType.KEFU_PHONE));
        arrayList.add(Integer.valueOf(SettingType.SETTING_UP_HUMIDIFIER));
        arrayList.add(238);
        arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME));
        arrayList.add(240);
        arrayList.add(Integer.valueOf(SettingType.DROP_INSPECTION));
        arrayList.add(Integer.valueOf(SettingType.DEVICE_SCAN_PORT_ADDERS_YR));
        arrayList.add(Integer.valueOf(SettingType.ALWAYS_HEATING));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID_ALL));
        arrayList.add(307);
        arrayList.add(308);
        arrayList.add(Integer.valueOf(SettingType.LIGHT_INSPECTION_STATUS_QUERY));
        arrayList.add(Integer.valueOf(SettingType.CALCULATED_INVENTORY));
        arrayList.add(Integer.valueOf(SettingType.MAINCONTOL_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_VERSION_QUERY));
        arrayList.add(Integer.valueOf(SettingType.SCAN_BARCODE_REPLENISHMENT));
        arrayList.add(245);
        arrayList.add(246);
        arrayList.add(Integer.valueOf(SettingType.HEART_DIALOG));
        arrayList.add(Integer.valueOf(SettingType.TOP_LIGHT_CONTROL));
        arrayList.add(Integer.valueOf(SettingType.LIGHT_BOX_ROLLING_INTERVAL));
        arrayList.add(Integer.valueOf(SettingType.LIGHTBOX_CANVAS));
        arrayList.add(332);
        arrayList.add(Integer.valueOf(SettingType.WORK_MODE));
        arrayList.add(Integer.valueOf(SettingType.DRUG_BOX_MENU_NAME));
        arrayList.add(Integer.valueOf(SettingType.PRACTICE_MODE));
        arrayList.add(Integer.valueOf(SettingType.MICROPHONE_TEST));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_TEST));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_PID_VID));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_TEST));
        arrayList.add(Integer.valueOf(SettingType.CAMERA_AUTO_TAKE));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_SENSITIVITY));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_DISTANCE));
        arrayList.add(Integer.valueOf(SettingType.MAIN_BOARD_SEQUENCE_NUMBER));
        arrayList.add(Integer.valueOf(SettingType.LOG_SETTING));
        arrayList.add(Integer.valueOf(SettingType.ID_CARD_READER_TEST));
        arrayList.add(Integer.valueOf(SettingType.GRID_MACHINE_FULL_LOAD));
        arrayList.add(354);
        return arrayList;
    }

    public List<Integer> getSideLongBeltSettingType() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(117);
        arrayList.add(118);
        arrayList.add(119);
        arrayList.add(120);
        arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
        arrayList.add(163);
        arrayList.add(158);
        arrayList.add(159);
        arrayList.add(166);
        arrayList.add(Integer.valueOf(SettingType.PRINTER));
        arrayList.add(Integer.valueOf(SettingType.SCAVENGING_WHARF));
        arrayList.add(177);
        arrayList.add(182);
        arrayList.add(183);
        arrayList.add(184);
        arrayList.add(185);
        arrayList.add(Integer.valueOf(SettingType.BANKNOTE_CHANGE_OF_MONEY));
        arrayList.add(187);
        arrayList.add(Integer.valueOf(SettingType.COIN_QUERY));
        arrayList.add(189);
        arrayList.add(Integer.valueOf(SettingType.SHOW_BALANCE));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YL));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YLX));
        arrayList.add(211);
        arrayList.add(213);
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_JF));
        arrayList.add(Integer.valueOf(SettingType.MONETARY_SYMBOL));
        arrayList.add(Integer.valueOf(SettingType.CALL_PHONE));
        arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_MATERIAL_FILE));
        arrayList.add(235);
        arrayList.add(Integer.valueOf(SettingType.KEFU_PHONE));
        arrayList.add(Integer.valueOf(SettingType.SETTING_UP_HUMIDIFIER));
        arrayList.add(238);
        arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME));
        arrayList.add(Integer.valueOf(SettingType.MANUAL_POSITIONING_OF_BOXES));
        arrayList.add(256);
        arrayList.add(257);
        arrayList.add(Integer.valueOf(SettingType.DEVICE_SCAN_PORT_ADDERS_YR));
        arrayList.add(Integer.valueOf(SettingType.ALWAYS_HEATING));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID_ALL));
        arrayList.add(307);
        arrayList.add(308);
        arrayList.add(Integer.valueOf(SettingType.LIGHT_INSPECTION_STATUS_QUERY));
        arrayList.add(Integer.valueOf(SettingType.CALCULATED_INVENTORY));
        arrayList.add(Integer.valueOf(SettingType.MAINCONTOL_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_VERSION_QUERY));
        arrayList.add(Integer.valueOf(SettingType.SCAN_BARCODE_REPLENISHMENT));
        arrayList.add(245);
        arrayList.add(246);
        arrayList.add(Integer.valueOf(SettingType.HEART_DIALOG));
        arrayList.add(Integer.valueOf(SettingType.TOP_LIGHT_CONTROL));
        arrayList.add(Integer.valueOf(SettingType.LIGHT_BOX_ROLLING_INTERVAL));
        arrayList.add(Integer.valueOf(SettingType.LIGHTBOX_CANVAS));
        arrayList.add(332);
        arrayList.add(Integer.valueOf(SettingType.WORK_MODE));
        arrayList.add(Integer.valueOf(SettingType.DRUG_BOX_MENU_NAME));
        arrayList.add(Integer.valueOf(SettingType.PRACTICE_MODE));
        arrayList.add(Integer.valueOf(SettingType.MICROPHONE_TEST));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_TEST));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_PID_VID));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_TEST));
        arrayList.add(Integer.valueOf(SettingType.CAMERA_AUTO_TAKE));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_SENSITIVITY));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_DISTANCE));
        arrayList.add(Integer.valueOf(SettingType.MAIN_BOARD_SEQUENCE_NUMBER));
        arrayList.add(Integer.valueOf(SettingType.LOG_SETTING));
        arrayList.add(Integer.valueOf(SettingType.ID_CARD_READER_TEST));
        arrayList.add(Integer.valueOf(SettingType.GRID_MACHINE_FULL_LOAD));
        arrayList.add(354);
        return arrayList;
    }

    public List<Integer> getSYJSettingType() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(106);
        arrayList.add(109);
        arrayList.add(110);
        arrayList.add(111);
        arrayList.add(117);
        arrayList.add(118);
        arrayList.add(119);
        arrayList.add(120);
        arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
        arrayList.add(158);
        arrayList.add(159);
        arrayList.add(162);
        arrayList.add(254);
        arrayList.add(235);
        arrayList.add(182);
        arrayList.add(183);
        arrayList.add(184);
        arrayList.add(185);
        arrayList.add(Integer.valueOf(SettingType.BANKNOTE_CHANGE_OF_MONEY));
        arrayList.add(187);
        arrayList.add(Integer.valueOf(SettingType.COIN_QUERY));
        arrayList.add(189);
        arrayList.add(Integer.valueOf(SettingType.SHOW_BALANCE));
        arrayList.add(227);
        arrayList.add(Integer.valueOf(SettingType.CONNECTING_ELEVATOR));
        arrayList.add(229);
        arrayList.add(230);
        arrayList.add(231);
        arrayList.add(Integer.valueOf(SettingType.MICROWAVE_OVEN_POSITIONING));
        arrayList.add(Integer.valueOf(SettingType.MANUAL_POSITIONING_OF_BOXES));
        arrayList.add(258);
        arrayList.add(238);
        arrayList.add(239);
        arrayList.add(240);
        arrayList.add(242);
        arrayList.add(243);
        arrayList.add(244);
        arrayList.add(245);
        arrayList.add(246);
        arrayList.add(Integer.valueOf(SettingType.GOODWAY_SELECTION));
        arrayList.add(Integer.valueOf(SettingType.BOX_RICE_MACHINE_CABINET_SETTING));
        arrayList.add(Integer.valueOf(SettingType.ALWAYS_HEATING));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID_ALL));
        arrayList.add(307);
        arrayList.add(308);
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YL));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YLX));
        arrayList.add(211);
        arrayList.add(212);
        arrayList.add(213);
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_JF));
        arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_MATERIAL_FILE));
        arrayList.add(Integer.valueOf(SettingType.HEART_DIALOG));
        arrayList.add(256);
        arrayList.add(Integer.valueOf(SettingType.DEVICE_FACE_SN));
        arrayList.add(Integer.valueOf(SettingType.DEVICE_SCAN_PORT_ADDERS_YR));
        arrayList.add(Integer.valueOf(SettingType.NEW_LINKAGE_SYNCHRONIZATION_TIME));
        arrayList.add(332);
        arrayList.add(Integer.valueOf(SettingType.WORK_MODE));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_SENSITIVITY));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_DISTANCE));
        arrayList.add(Integer.valueOf(SettingType.DRUG_BOX_MENU_NAME));
        arrayList.add(Integer.valueOf(SettingType.PRACTICE_MODE));
        arrayList.add(Integer.valueOf(SettingType.QRCODE_FLOAT_VIEW));
        arrayList.add(Integer.valueOf(SettingType.GRID_MACHINE_FULL_LOAD));
        arrayList.add(354);
        return arrayList;
    }

    public List<Integer> getCashSettingType() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(117);
        arrayList.add(118);
        arrayList.add(119);
        arrayList.add(120);
        arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
        arrayList.add(158);
        arrayList.add(159);
        arrayList.add(182);
        arrayList.add(183);
        arrayList.add(184);
        arrayList.add(185);
        arrayList.add(Integer.valueOf(SettingType.BANKNOTE_CHANGE_OF_MONEY));
        arrayList.add(187);
        arrayList.add(Integer.valueOf(SettingType.COIN_QUERY));
        arrayList.add(189);
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_CASH));
        return arrayList;
    }

    private void showList() {
        this.enabledDataList = new ArrayList();
        Iterator<Integer> it = getNeedShowSettingItem().iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            EnabledData enabledData = new EnabledData();
            enabledData.type = intValue;
            enabledData.name = SettingTypeName.getSettingName(this.context, intValue);
            enabledData.isEnable = AppSetting.isSettingEnabled(this.context, intValue, this.mUserSettingDao);
            enabledData.oldIsEnable = enabledData.isEnable;
            this.enabledDataList.add(enabledData);
        }
        MyAdapter myAdapter = new MyAdapter(this.context);
        this.myAdapter = myAdapter;
        this.listView.setAdapter((ListAdapter) myAdapter);
    }

    /* loaded from: classes2.dex */
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        public MyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return SelectEnabledDialog.this.enabledDataList.size();
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.inflater.inflate(R.layout.layout_setting_enable_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.check_box = (CheckBox) view.findViewById(R.id.check_box);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_name.setText(((EnabledData) SelectEnabledDialog.this.enabledDataList.get(i)).name);
            viewHolder.check_box.setOnCheckedChangeListener(null);
            viewHolder.check_box.setChecked(((EnabledData) SelectEnabledDialog.this.enabledDataList.get(i)).isEnable);
            viewHolder.check_box.setTag(Integer.valueOf(i));
            viewHolder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shj.setting.Dialog.SelectEnabledDialog.MyAdapter.1
                AnonymousClass1() {
                }

                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    ((EnabledData) SelectEnabledDialog.this.enabledDataList.get(((Integer) compoundButton.getTag()).intValue())).isEnable = z;
                }
            });
            return view;
        }

        /* renamed from: com.shj.setting.Dialog.SelectEnabledDialog$MyAdapter$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements CompoundButton.OnCheckedChangeListener {
            AnonymousClass1() {
            }

            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                ((EnabledData) SelectEnabledDialog.this.enabledDataList.get(((Integer) compoundButton.getTag()).intValue())).isEnable = z;
            }
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public CheckBox check_box;
            public TextView tv_name;

            public ViewHolder() {
            }
        }
    }

    public void setSelectEnabledDialogListener(SelectEnabledDialogListener selectEnabledDialogListener) {
        this.selectEnabledDialogListener = selectEnabledDialogListener;
    }

    public static List<Integer> getNeedShowSettingItem() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(102);
        arrayList.add(103);
        arrayList.add(104);
        arrayList.add(106);
        arrayList.add(109);
        arrayList.add(110);
        arrayList.add(111);
        arrayList.add(113);
        arrayList.add(114);
        arrayList.add(Integer.valueOf(SettingType.SOUND_SETTING_ADVERTISEMENT_TIME1));
        arrayList.add(Integer.valueOf(SettingType.SOUND_SETTING_ADVERTISEMENT_TIME2));
        arrayList.add(Integer.valueOf(SettingType.SOUND_SETTING_VOICE_TIME1));
        arrayList.add(Integer.valueOf(SettingType.SOUND_SETTING_VOICE_TIME2));
        arrayList.add(115);
        arrayList.add(116);
        arrayList.add(117);
        arrayList.add(118);
        arrayList.add(119);
        arrayList.add(120);
        arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
        arrayList.add(122);
        arrayList.add(127);
        arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_INSTRUCTIONS_PICTURES));
        arrayList.add(128);
        arrayList.add(Integer.valueOf(SettingType.COMMODITY_ONE_BUTTON_SETUP));
        arrayList.add(Integer.valueOf(SettingType.INVENTORY));
        arrayList.add(Integer.valueOf(SettingType.FULL_DELIVERY));
        arrayList.add(Integer.valueOf(SettingType.CARGO_CAPACITY));
        arrayList.add(Integer.valueOf(SettingType.CARGO_CODE));
        arrayList.add(Integer.valueOf(SettingType.DROP_INSPECTION));
        arrayList.add(145);
        arrayList.add(149);
        arrayList.add(153);
        arrayList.add(156);
        arrayList.add(158);
        arrayList.add(159);
        arrayList.add(160);
        arrayList.add(161);
        arrayList.add(162);
        arrayList.add(163);
        arrayList.add(164);
        arrayList.add(165);
        arrayList.add(166);
        arrayList.add(167);
        arrayList.add(168);
        arrayList.add(169);
        arrayList.add(254);
        arrayList.add(Integer.valueOf(SettingType.RESTORE_FACTORY_SETTINGS));
        arrayList.add(172);
        arrayList.add(Integer.valueOf(SettingType.PRINTER));
        arrayList.add(Integer.valueOf(SettingType.SCAVENGING_WHARF));
        arrayList.add(177);
        arrayList.add(178);
        arrayList.add(179);
        arrayList.add(180);
        arrayList.add(181);
        arrayList.add(182);
        arrayList.add(183);
        arrayList.add(184);
        arrayList.add(185);
        arrayList.add(Integer.valueOf(SettingType.BANKNOTE_CHANGE_OF_MONEY));
        arrayList.add(187);
        arrayList.add(Integer.valueOf(SettingType.COIN_QUERY));
        arrayList.add(189);
        arrayList.add(259);
        arrayList.add(Integer.valueOf(SettingType.SHOW_BALANCE));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_CASH));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_AGGREGATE_CODE));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_WEIXIN));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_ZFB));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YL));
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YLX));
        arrayList.add(211);
        arrayList.add(212);
        arrayList.add(213);
        arrayList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_JF));
        arrayList.add(Integer.valueOf(SettingType.MONETARY_SYMBOL));
        arrayList.add(222);
        arrayList.add(223);
        arrayList.add(224);
        arrayList.add(225);
        arrayList.add(226);
        arrayList.add(227);
        arrayList.add(Integer.valueOf(SettingType.CONNECTING_ELEVATOR));
        arrayList.add(229);
        arrayList.add(230);
        arrayList.add(231);
        arrayList.add(Integer.valueOf(SettingType.MICROWAVE_OVEN_POSITIONING));
        arrayList.add(Integer.valueOf(SettingType.MANUAL_POSITIONING_OF_BOXES));
        arrayList.add(256);
        arrayList.add(257);
        arrayList.add(258);
        arrayList.add(234);
        arrayList.add(235);
        arrayList.add(236);
        arrayList.add(237);
        arrayList.add(238);
        arrayList.add(239);
        arrayList.add(242);
        arrayList.add(243);
        arrayList.add(244);
        arrayList.add(245);
        arrayList.add(246);
        arrayList.add(247);
        arrayList.add(248);
        arrayList.add(249);
        arrayList.add(250);
        arrayList.add(251);
        arrayList.add(252);
        arrayList.add(253);
        arrayList.add(255);
        arrayList.add(Integer.valueOf(SettingType.AD_PLAYER));
        arrayList.add(Integer.valueOf(SettingType.CALL_PHONE));
        arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_MATERIAL_FILE));
        arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_MERCHANDISE_DETAILPICTURES));
        arrayList.add(Integer.valueOf(SettingType.NETWORKING_TIMEOUT));
        arrayList.add(Integer.valueOf(SettingType.GOODWAY_SELECTION));
        arrayList.add(Integer.valueOf(SettingType.KEFU_PHONE));
        arrayList.add(Integer.valueOf(SettingType.SETTING_UP_HUMIDIFIER));
        arrayList.add(Integer.valueOf(SettingType.FAULT_TEMPERATURE_PROBE));
        arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME));
        arrayList.add(Integer.valueOf(SettingType.BOX_RICE_MACHINE_CABINET_SETTING));
        arrayList.add(Integer.valueOf(SettingType.SOFT_MANAGE));
        arrayList.add(Integer.valueOf(SettingType.DEVICE_FACE_SN));
        arrayList.add(Integer.valueOf(SettingType.DEVICE_SCAN_PORT_ADDERS_YR));
        arrayList.add(Integer.valueOf(SettingType.ALWAYS_HEATING));
        arrayList.add(Integer.valueOf(SettingType.HEART_DIALOG));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID));
        arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID_ALL));
        arrayList.add(307);
        arrayList.add(308);
        arrayList.add(Integer.valueOf(SettingType.LIGHT_INSPECTION_STATUS_QUERY));
        arrayList.add(Integer.valueOf(SettingType.CALCULATED_INVENTORY));
        arrayList.add(Integer.valueOf(SettingType.MAINCONTOL_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_VERSION_QUERY));
        arrayList.add(Integer.valueOf(SettingType.SCAN_BARCODE_REPLENISHMENT));
        arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_SIGNAL_GOODS_PIC));
        arrayList.add(Integer.valueOf(SettingType.TOP_LIGHT_CONTROL));
        Integer valueOf = Integer.valueOf(SettingType.LIGHT_BOX_ROLLING_INTERVAL);
        arrayList.add(valueOf);
        arrayList.add(Integer.valueOf(SettingType.ID_CARD_READER_TEST));
        arrayList.add(valueOf);
        arrayList.add(Integer.valueOf(SettingType.MICROPHONE_TEST));
        arrayList.add(Integer.valueOf(SettingType.LIGHTBOX_CANVAS));
        arrayList.add(Integer.valueOf(SettingType.NEW_LINKAGE_SYNCHRONIZATION_TIME));
        arrayList.add(332);
        arrayList.add(Integer.valueOf(SettingType.WORK_MODE));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_TEST));
        arrayList.add(Integer.valueOf(SettingType.SURVEILLANCE_CAMERA_PID_VID));
        arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_PID_VID));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_TEST));
        arrayList.add(300);
        arrayList.add(Integer.valueOf(SettingType.CAMERA_TEST));
        arrayList.add(Integer.valueOf(SettingType.CAMERA_AUTO_TAKE));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_SENSITIVITY));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_DISTANCE));
        arrayList.add(Integer.valueOf(SettingType.DRUG_BOX_MENU_NAME));
        arrayList.add(Integer.valueOf(SettingType.MAIN_BOARD_SEQUENCE_NUMBER));
        arrayList.add(Integer.valueOf(SettingType.LOG_SETTING));
        arrayList.add(Integer.valueOf(SettingType.PRACTICE_MODE));
        arrayList.add(Integer.valueOf(SettingType.QRCODE_FLOAT_VIEW));
        arrayList.add(Integer.valueOf(SettingType.GRID_MACHINE_FULL_LOAD));
        arrayList.add(354);
        return arrayList;
    }

    public static List<Integer> getCommandItem() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(117);
        arrayList.add(118);
        arrayList.add(119);
        arrayList.add(120);
        arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
        arrayList.add(Integer.valueOf(SettingType.FULL_DELIVERY));
        arrayList.add(Integer.valueOf(SettingType.DROP_INSPECTION));
        arrayList.add(145);
        arrayList.add(149);
        arrayList.add(153);
        arrayList.add(156);
        arrayList.add(158);
        arrayList.add(159);
        arrayList.add(160);
        arrayList.add(161);
        arrayList.add(162);
        arrayList.add(163);
        arrayList.add(167);
        arrayList.add(168);
        arrayList.add(169);
        arrayList.add(254);
        arrayList.add(Integer.valueOf(SettingType.RESTORE_FACTORY_SETTINGS));
        arrayList.add(177);
        arrayList.add(178);
        arrayList.add(179);
        arrayList.add(181);
        arrayList.add(182);
        arrayList.add(183);
        arrayList.add(184);
        arrayList.add(185);
        arrayList.add(Integer.valueOf(SettingType.BANKNOTE_CHANGE_OF_MONEY));
        arrayList.add(187);
        arrayList.add(Integer.valueOf(SettingType.COIN_QUERY));
        arrayList.add(189);
        arrayList.add(224);
        arrayList.add(225);
        arrayList.add(226);
        arrayList.add(227);
        arrayList.add(Integer.valueOf(SettingType.CONNECTING_ELEVATOR));
        arrayList.add(229);
        arrayList.add(230);
        arrayList.add(231);
        arrayList.add(Integer.valueOf(SettingType.MICROWAVE_OVEN_POSITIONING));
        arrayList.add(Integer.valueOf(SettingType.MANUAL_POSITIONING_OF_BOXES));
        arrayList.add(256);
        arrayList.add(257);
        arrayList.add(258);
        arrayList.add(234);
        arrayList.add(235);
        arrayList.add(236);
        arrayList.add(237);
        arrayList.add(238);
        arrayList.add(239);
        arrayList.add(240);
        arrayList.add(241);
        arrayList.add(242);
        arrayList.add(243);
        arrayList.add(244);
        arrayList.add(245);
        arrayList.add(246);
        arrayList.add(247);
        arrayList.add(Integer.valueOf(SettingType.SETTING_UP_HUMIDIFIER));
        arrayList.add(Integer.valueOf(SettingType.FAULT_TEMPERATURE_PROBE));
        arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME));
        arrayList.add(Integer.valueOf(SettingType.SETTING_SYSTEM_TIME));
        arrayList.add(Integer.valueOf(SettingType.CARGO_MANAGEMENT_OTHER));
        arrayList.add(Integer.valueOf(SettingType.LIGHT_INSPECTION_STATUS_QUERY));
        arrayList.add(Integer.valueOf(SettingType.CALCULATED_INVENTORY));
        arrayList.add(Integer.valueOf(SettingType.MAINCONTOL_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_PROGRAM_UPDATE));
        arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_VERSION_QUERY));
        arrayList.add(Integer.valueOf(SettingType.SCAN_BARCODE_REPLENISHMENT));
        arrayList.add(Integer.valueOf(SettingType.TOP_LIGHT_CONTROL));
        arrayList.add(Integer.valueOf(SettingType.LIGHT_BOX_ROLLING_INTERVAL));
        arrayList.add(Integer.valueOf(SettingType.LIGHTBOX_CANVAS));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_SENSITIVITY));
        arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_DISTANCE));
        return arrayList;
    }
}
