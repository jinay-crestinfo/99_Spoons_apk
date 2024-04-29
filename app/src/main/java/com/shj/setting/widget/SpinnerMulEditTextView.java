package com.shj.setting.widget;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import com.shj.setting.widget.MultipleEditItemView;
import com.shj.setting.widget.RadioGroupItemView;
import com.shj.setting.widget.SpinnerItemView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class SpinnerMulEditTextView extends AbsItemView {
    private ChangeListener changeListener;
    private List<EditeItemView> editeItemViewList;
    private boolean isFirst;
    private boolean listenering;
    private SpinnerItemView newAddspinnerItemView;
    private RadioGroupItemView radioGroupItemView;
    private SpinnerItemView spinnerItemView;

    /* loaded from: classes2.dex */
    public interface ChangeListener {
        void change();
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public SpinnerMulEditTextView(Context context, SpinnerItemView.SpinnerItemData spinnerItemData, SpinnerItemView.SpinnerItemData spinnerItemData2, List<MultipleEditItemView.EditTextDataInfo> list, RadioGroupItemView.RadioGroupData radioGroupData) {
        super(context);
        this.listenering = true;
        this.isFirst = true;
        this.context = context;
        initView(spinnerItemData, spinnerItemData2, list, radioGroupData);
    }

    private void initView(SpinnerItemView.SpinnerItemData spinnerItemData, SpinnerItemView.SpinnerItemData spinnerItemData2, List<MultipleEditItemView.EditTextDataInfo> list, RadioGroupItemView.RadioGroupData radioGroupData) {
        if (spinnerItemData != null) {
            SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, spinnerItemData);
            this.newAddspinnerItemView = spinnerItemView;
            addContentView(spinnerItemView);
        }
        if (spinnerItemData2 != null) {
            SpinnerItemView spinnerItemView2 = new SpinnerItemView(this.context, spinnerItemData2);
            this.spinnerItemView = spinnerItemView2;
            spinnerItemView2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.shj.setting.widget.SpinnerMulEditTextView.1
                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

                AnonymousClass1() {
                }

                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                    if (!SpinnerMulEditTextView.this.isFirst && SpinnerMulEditTextView.this.listenering && SpinnerMulEditTextView.this.changeListener != null) {
                        SpinnerMulEditTextView.this.changeListener.change();
                    }
                    SpinnerMulEditTextView.this.isFirst = false;
                }
            });
            addContentView(this.spinnerItemView);
        }
        this.editeItemViewList = new ArrayList();
        if (list != null) {
            Iterator<MultipleEditItemView.EditTextDataInfo> it = list.iterator();
            while (it.hasNext()) {
                EditeItemView editeItemView = new EditeItemView(this.context, it.next());
                this.editeItemViewList.add(editeItemView);
                addContentView(editeItemView);
            }
        }
        if (radioGroupData != null) {
            RadioGroupItemView radioGroupItemView = new RadioGroupItemView(this.context, radioGroupData);
            this.radioGroupItemView = radioGroupItemView;
            addContentView(radioGroupItemView);
        }
    }

    /* renamed from: com.shj.setting.widget.SpinnerMulEditTextView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements AdapterView.OnItemSelectedListener {
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        AnonymousClass1() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (!SpinnerMulEditTextView.this.isFirst && SpinnerMulEditTextView.this.listenering && SpinnerMulEditTextView.this.changeListener != null) {
                SpinnerMulEditTextView.this.changeListener.change();
            }
            SpinnerMulEditTextView.this.isFirst = false;
        }
    }

    public int getNewAddSelectIndex() {
        return this.newAddspinnerItemView.getSelectIndex();
    }

    public int getSelectIndex() {
        return this.spinnerItemView.getSelectIndex();
    }

    public String getSelectValue() {
        return this.spinnerItemView.getSelectValue();
    }

    public void setSelectSpinnerIndex(int i) {
        this.listenering = false;
        this.spinnerItemView.setSelectIndex(i);
        new Handler().postDelayed(new Runnable() { // from class: com.shj.setting.widget.SpinnerMulEditTextView.2
            AnonymousClass2() {
            }

            @Override // java.lang.Runnable
            public void run() {
                SpinnerMulEditTextView.this.listenering = true;
            }
        }, 200L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.widget.SpinnerMulEditTextView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SpinnerMulEditTextView.this.listenering = true;
        }
    }

    public String getEditeText(int i) {
        if (i < this.editeItemViewList.size()) {
            return this.editeItemViewList.get(i).getEditText().getText().toString().trim();
        }
        return null;
    }

    public void setEditeText(int i, String str) {
        if (i < this.editeItemViewList.size()) {
            EditText editText = this.editeItemViewList.get(i).getEditText();
            editText.setText(str);
            editText.setSelection(str.length());
        }
    }

    public void setEditeTextNotInput(int i) {
        if (i < this.editeItemViewList.size()) {
            this.editeItemViewList.get(i).getEditText().setEnabled(false);
        }
    }

    public void setRadioGroupCheck(int i) {
        RadioGroupItemView radioGroupItemView = this.radioGroupItemView;
        if (radioGroupItemView != null) {
            radioGroupItemView.setRadioButtonCheck(i);
        }
    }

    public int getRadioGroupCheck() {
        return this.radioGroupItemView.getRadioButtonCheckIndex();
    }

    public void setEditeTypeInterger(int i) {
        if (i < this.editeItemViewList.size()) {
            this.editeItemViewList.get(i).getEditText().setInputType(2);
        }
    }

    public void setEditeEnable(int i, boolean z) {
        if (i < this.editeItemViewList.size()) {
            this.editeItemViewList.get(i).getEditText().setEnabled(z);
        }
    }

    public void setEditeVisiable(int i, int i2) {
        if (i < this.editeItemViewList.size()) {
            this.editeItemViewList.get(i).setVisibility(i2);
        }
    }

    public void setEditeTypeNumberSigned(int i) {
        if (i < this.editeItemViewList.size()) {
            this.editeItemViewList.get(i).getEditText().setInputType(4098);
        }
    }

    public SpinnerItemView getSpinnerItemView() {
        return this.spinnerItemView;
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }
}
