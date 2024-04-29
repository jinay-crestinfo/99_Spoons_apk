package com.shj.setting.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.shj.setting.Dialog.SelectWorkTimeDialog;
import com.shj.setting.widget.MultipleEditItemView;
import com.shj.setting.widget.SpinnerItemView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class CompressorView extends AbsItemView {
    private static final int LINE_COUNT = 3;
    private CompressorData compressorData;
    private MultipleEditItemView multipleEditItemView;
    private SpinnerItemView spinnerItemView;

    /* loaded from: classes2.dex */
    public static class CompressorData {
        public List<MultipleEditItemView.EditTextDataInfo> editTextDataList;
        public SpinnerItemView.SpinnerItemData spinnerItemData;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public CompressorView(Context context, CompressorData compressorData) {
        super(context);
        this.compressorData = compressorData;
        initView();
    }

    private void initView() {
        SpinnerItemView spinnerItemView = new SpinnerItemView(this.context, this.compressorData.spinnerItemData);
        this.spinnerItemView = spinnerItemView;
        addContentView(spinnerItemView);
        MultipleEditItemView multipleEditItemView = new MultipleEditItemView(this.context, this.compressorData.editTextDataList);
        this.multipleEditItemView = multipleEditItemView;
        addContentView(multipleEditItemView);
        for (EditeItemView editeItemView : this.multipleEditItemView.getEditeItemViewList()) {
            editeItemView.getEditText().setFocusable(false);
            editeItemView.getEditText().setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.CompressorView.1
                final /* synthetic */ EditeItemView val$item;

                AnonymousClass1(EditeItemView editeItemView2) {
                    editeItemView = editeItemView2;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int i;
                    String obj = editeItemView.getEditText().getText().toString();
                    int i2 = 0;
                    if (TextUtils.isEmpty(obj)) {
                        i = 0;
                    } else {
                        String[] split = obj.split("-");
                        i2 = Integer.valueOf(split[0]).intValue();
                        i = Integer.valueOf(split[1]).intValue();
                    }
                    SelectWorkTimeDialog selectWorkTimeDialog = new SelectWorkTimeDialog(CompressorView.this.context, String.valueOf(i2), String.valueOf(i));
                    selectWorkTimeDialog.setButtonListener(new SelectWorkTimeDialog.ButtonListener() { // from class: com.shj.setting.widget.CompressorView.1.1
                        C00751() {
                        }

                        @Override // com.shj.setting.Dialog.SelectWorkTimeDialog.ButtonListener
                        public void buttonClick_OK(String str, String str2) {
                            editeItemView.getEditText().setText(str + "-" + str2);
                        }
                    });
                    selectWorkTimeDialog.show();
                }

                /* renamed from: com.shj.setting.widget.CompressorView$1$1 */
                /* loaded from: classes2.dex */
                class C00751 implements SelectWorkTimeDialog.ButtonListener {
                    C00751() {
                    }

                    @Override // com.shj.setting.Dialog.SelectWorkTimeDialog.ButtonListener
                    public void buttonClick_OK(String str, String str2) {
                        editeItemView.getEditText().setText(str + "-" + str2);
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.setting.widget.CompressorView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        final /* synthetic */ EditeItemView val$item;

        AnonymousClass1(EditeItemView editeItemView2) {
            editeItemView = editeItemView2;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int i;
            String obj = editeItemView.getEditText().getText().toString();
            int i2 = 0;
            if (TextUtils.isEmpty(obj)) {
                i = 0;
            } else {
                String[] split = obj.split("-");
                i2 = Integer.valueOf(split[0]).intValue();
                i = Integer.valueOf(split[1]).intValue();
            }
            SelectWorkTimeDialog selectWorkTimeDialog = new SelectWorkTimeDialog(CompressorView.this.context, String.valueOf(i2), String.valueOf(i));
            selectWorkTimeDialog.setButtonListener(new SelectWorkTimeDialog.ButtonListener() { // from class: com.shj.setting.widget.CompressorView.1.1
                C00751() {
                }

                @Override // com.shj.setting.Dialog.SelectWorkTimeDialog.ButtonListener
                public void buttonClick_OK(String str, String str2) {
                    editeItemView.getEditText().setText(str + "-" + str2);
                }
            });
            selectWorkTimeDialog.show();
        }

        /* renamed from: com.shj.setting.widget.CompressorView$1$1 */
        /* loaded from: classes2.dex */
        class C00751 implements SelectWorkTimeDialog.ButtonListener {
            C00751() {
            }

            @Override // com.shj.setting.Dialog.SelectWorkTimeDialog.ButtonListener
            public void buttonClick_OK(String str, String str2) {
                editeItemView.getEditText().setText(str + "-" + str2);
            }
        }
    }

    public int getSelectCabinet() {
        return this.spinnerItemView.getSelectIndex();
    }

    public void setEditText(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            this.multipleEditItemView.setEditeText(i, list.get(i));
        }
    }

    public List<String> getInputTextList() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 3; i++) {
            String editeText = this.multipleEditItemView.getEditeText(i);
            if (!TextUtils.isEmpty(editeText)) {
                arrayList.add(editeText);
            } else {
                arrayList.add("0");
            }
        }
        return arrayList;
    }
}
