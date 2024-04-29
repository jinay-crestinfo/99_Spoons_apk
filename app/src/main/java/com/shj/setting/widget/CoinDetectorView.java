package com.shj.setting.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import com.oysb.utils.CommonTool;
import com.shj.setting.R;
import com.shj.setting.widget.MultipleEditItemView;
import com.shj.setting.widget.SpinnerItemView;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class CoinDetectorView extends AbsItemView {
    private SpinnerItemView coinChangerItemView;
    private SpinnerItemView coinTypeItemView;
    private EditeItemView editeItemView;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public CoinDetectorView(Context context) {
        super(context);
        initView();
    }

    public void initView() {
        SpinnerItemView.SpinnerItemData spinnerItemData = new SpinnerItemView.SpinnerItemData();
        spinnerItemData.name = this.context.getString(R.string.coin_changer_type);
        spinnerItemData.dataList = new ArrayList();
        spinnerItemData.dataList.add(this.context.getString(R.string.coin_changer));
        spinnerItemData.dataList.add("HOPPER");
        SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, spinnerItemData);
        this.coinChangerItemView = spinnerItemView;
        addContentView(spinnerItemView);
        MultipleEditItemView.EditTextDataInfo editTextDataInfo = new MultipleEditItemView.EditTextDataInfo();
        editTextDataInfo.name = this.context.getString(R.string.looking_for_money_count);
        if (CommonTool.getLanguage(this.context).equals("en") || CommonTool.getLanguage(this.context).equals("fr")) {
            editTextDataInfo.tipInfo = this.context.getString(R.string.please_input) + StringUtils.SPACE + editTextDataInfo.name;
        } else {
            editTextDataInfo.tipInfo = this.context.getString(R.string.please_input) + editTextDataInfo.name;
        }
        EditeItemView editeItemView = new EditeItemView(this.context, editTextDataInfo);
        this.editeItemView = editeItemView;
        editeItemView.setInputTypeNumber();
        addContentView(this.editeItemView);
        SpinnerItemView.SpinnerItemData spinnerItemData2 = new SpinnerItemView.SpinnerItemData();
        spinnerItemData2.name = this.context.getString(R.string.coin_type);
        spinnerItemData2.dataList = new ArrayList();
        spinnerItemData2.dataList.add(this.context.getString(R.string.coin_pentagon));
        spinnerItemData2.dataList.add(this.context.getString(R.string.coin_one_yuan));
        SpinnerItemView spinnerItemView2 = new SpinnerItemView(this.context, spinnerItemData2);
        this.coinTypeItemView = spinnerItemView2;
        addContentView(spinnerItemView2);
        this.coinTypeItemView.setVisibility(8);
        this.coinChangerItemView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.shj.setting.widget.CoinDetectorView.1
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            AnonymousClass1() {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (i == 0) {
                    CoinDetectorView.this.coinTypeItemView.setVisibility(8);
                } else {
                    CoinDetectorView.this.coinTypeItemView.setVisibility(0);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.widget.CoinDetectorView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements AdapterView.OnItemSelectedListener {
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        AnonymousClass1() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (i == 0) {
                CoinDetectorView.this.coinTypeItemView.setVisibility(8);
            } else {
                CoinDetectorView.this.coinTypeItemView.setVisibility(0);
            }
        }
    }

    public int getCoinChangerType() {
        return this.coinChangerItemView.getSelectIndex() + 1;
    }

    public int getCoinCount() {
        String obj = this.editeItemView.getEditText().getText().toString();
        if (TextUtils.isEmpty(obj)) {
            return -1;
        }
        return Integer.valueOf(obj).intValue();
    }

    public int getCoinType() {
        return this.coinTypeItemView.getSelectIndex() + 1;
    }
}
