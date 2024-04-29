package com.shj.setting.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.internal.view.SupportMenu;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shj.setting.Dialog.EditTextDialog;
import com.shj.setting.R;
import com.shj.setting.widget.CalculateInventoryView;

/* loaded from: classes2.dex */
public class CalculateInventoryItemView extends LinearLayout {
    private CalculateInventoryView.CIIndexData cIIndexData;
    private Context context;
    private EditText et_input;
    private boolean isShowAddAndReduce;
    private EditText tv_calculate_count;
    private TextView tv_index;
    private TextView tv_local_count;

    public CalculateInventoryItemView(Context context, CalculateInventoryView.CIIndexData cIIndexData) {
        super(context);
        this.context = context;
        this.cIIndexData = cIIndexData;
        initView();
    }

    public void setShowAddAndReduce(boolean z) {
        this.isShowAddAndReduce = z;
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_calculate_inventory_item, this);
        this.tv_index = (TextView) findViewById(R.id.tv_index);
        this.et_input = (EditText) findViewById(R.id.et_input);
        this.tv_local_count = (TextView) findViewById(R.id.tv_local_count);
        this.tv_calculate_count = (EditText) findViewById(R.id.tv_calculate_count);
        this.et_input.setInputType(8194);
        if (this.cIIndexData.textIdentifier != null) {
            this.tv_index.setText(this.cIIndexData.textIdentifier);
        } else {
            this.tv_index.setText(String.valueOf(this.cIIndexData.identifier));
        }
        this.tv_local_count.setText(String.valueOf(this.cIIndexData.localCount));
        this.et_input.setText(this.cIIndexData.initText);
        this.et_input.setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: com.shj.setting.widget.CalculateInventoryItemView.1
            @Override // android.view.ActionMode.Callback
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override // android.view.ActionMode.Callback
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override // android.view.ActionMode.Callback
            public void onDestroyActionMode(ActionMode actionMode) {
            }

            @Override // android.view.ActionMode.Callback
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            AnonymousClass1() {
            }
        });
        this.et_input.setImeOptions(268435456);
        this.et_input.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shj.setting.widget.CalculateInventoryItemView.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                CalculateInventoryItemView calculateInventoryItemView = CalculateInventoryItemView.this;
                calculateInventoryItemView.showInputDialog(calculateInventoryItemView.isShowAddAndReduce);
                return false;
            }
        });
    }

    /* renamed from: com.shj.setting.widget.CalculateInventoryItemView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements ActionMode.Callback {
        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        AnonymousClass1() {
        }
    }

    /* renamed from: com.shj.setting.widget.CalculateInventoryItemView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnLongClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            CalculateInventoryItemView calculateInventoryItemView = CalculateInventoryItemView.this;
            calculateInventoryItemView.showInputDialog(calculateInventoryItemView.isShowAddAndReduce);
            return false;
        }
    }

    public void showInputDialog(boolean z) {
        EditTextDialog editTextDialog = new EditTextDialog(this.context, this.et_input.getText().toString(), z);
        editTextDialog.show();
        editTextDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.shj.setting.widget.CalculateInventoryItemView.3
            final /* synthetic */ EditTextDialog val$editTextDialog;

            AnonymousClass3(EditTextDialog editTextDialog2) {
                editTextDialog = editTextDialog2;
            }

            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                CalculateInventoryItemView.this.et_input.setText(editTextDialog.getInput());
                CalculateInventoryItemView.this.et_input.setSelection(CalculateInventoryItemView.this.et_input.getText().length());
            }
        });
    }

    /* renamed from: com.shj.setting.widget.CalculateInventoryItemView$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements DialogInterface.OnDismissListener {
        final /* synthetic */ EditTextDialog val$editTextDialog;

        AnonymousClass3(EditTextDialog editTextDialog2) {
            editTextDialog = editTextDialog2;
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            CalculateInventoryItemView.this.et_input.setText(editTextDialog.getInput());
            CalculateInventoryItemView.this.et_input.setSelection(CalculateInventoryItemView.this.et_input.getText().length());
        }
    }

    public void setInputText(String str) {
        this.et_input.setText(str);
    }

    public void setCalculateCount(int i) {
        this.tv_calculate_count.setText(String.valueOf(i));
        if (i != this.cIIndexData.localCount) {
            this.tv_calculate_count.setTextColor(SupportMenu.CATEGORY_MASK);
            this.tv_local_count.setTextColor(SupportMenu.CATEGORY_MASK);
        } else {
            this.tv_calculate_count.setTextColor(this.context.getResources().getColor(R.color.color_text));
            this.tv_local_count.setTextColor(this.context.getResources().getColor(R.color.color_text));
        }
    }

    public void setLengthData(String str) {
        this.et_input.setText(str);
    }

    public String getInputText() {
        return this.et_input.getText().toString();
    }

    public int getCalculateCount() {
        String obj = this.tv_calculate_count.getText().toString();
        if (TextUtils.isEmpty(obj)) {
            return -5;
        }
        return Integer.valueOf(obj).intValue();
    }

    public int getIndex() {
        return this.cIIndexData.identifier;
    }

    public String getTextIdentifier() {
        return this.cIIndexData.textIdentifier;
    }
}
