package com.shj.setting.widget;

import android.content.Context;
import android.content.DialogInterface;
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

/* loaded from: classes2.dex */
public class GridItemView extends LinearLayout {
    public String beforeSettingText;
    private long clickTime;
    private Context context;
    private EditText et_input;
    private int index;
    private String initText;
    private boolean isShowAddAndReduce;
    private String textIdentifier;
    private TextView tv_index;
    private int upperLevelNumber;

    public GridItemView(Context context, int i, String str, String str2, int i2) {
        super(context);
        this.context = context;
        this.index = i;
        this.textIdentifier = str;
        this.initText = str2;
        this.upperLevelNumber = i2;
        initView();
    }

    public void setShowAddAndReduce(boolean z) {
        this.isShowAddAndReduce = z;
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_grid_item, this);
        this.tv_index = (TextView) findViewById(R.id.tv_index);
        EditText editText = (EditText) findViewById(R.id.et_input);
        this.et_input = editText;
        editText.setInputType(8194);
        String str = this.textIdentifier;
        if (str != null) {
            this.tv_index.setText(str);
        } else {
            this.tv_index.setText(String.valueOf(this.index));
        }
        this.et_input.setText(this.initText);
        this.beforeSettingText = this.initText;
        this.et_input.setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: com.shj.setting.widget.GridItemView.1
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
        this.et_input.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shj.setting.widget.GridItemView.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                GridItemView gridItemView = GridItemView.this;
                gridItemView.showInputDialog(gridItemView.isShowAddAndReduce);
                return false;
            }
        });
    }

    /* renamed from: com.shj.setting.widget.GridItemView$1 */
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

    /* renamed from: com.shj.setting.widget.GridItemView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnLongClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            GridItemView gridItemView = GridItemView.this;
            gridItemView.showInputDialog(gridItemView.isShowAddAndReduce);
            return false;
        }
    }

    public void showInputDialog(boolean z) {
        EditTextDialog editTextDialog = new EditTextDialog(this.context, this.et_input.getText().toString(), z);
        editTextDialog.show();
        editTextDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.shj.setting.widget.GridItemView.3
            final /* synthetic */ EditTextDialog val$editTextDialog;

            AnonymousClass3(EditTextDialog editTextDialog2) {
                editTextDialog = editTextDialog2;
            }

            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                GridItemView.this.et_input.setText(editTextDialog.getInput());
                GridItemView.this.et_input.setSelection(GridItemView.this.et_input.getText().length());
            }
        });
    }

    /* renamed from: com.shj.setting.widget.GridItemView$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements DialogInterface.OnDismissListener {
        final /* synthetic */ EditTextDialog val$editTextDialog;

        AnonymousClass3(EditTextDialog editTextDialog2) {
            editTextDialog = editTextDialog2;
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            GridItemView.this.et_input.setText(editTextDialog.getInput());
            GridItemView.this.et_input.setSelection(GridItemView.this.et_input.getText().length());
        }
    }

    public void setInputText(String str, boolean z) {
        this.et_input.setText(str);
        if (z) {
            this.beforeSettingText = str;
        }
    }

    public String getInputText() {
        return this.et_input.getText().toString();
    }

    public int getIndex() {
        return this.index;
    }

    public int getUpperLevelNumber() {
        return this.upperLevelNumber;
    }

    public String getTextIdentifier() {
        return this.textIdentifier;
    }

    public void setBeforeSettingText(String str) {
        this.beforeSettingText = str;
    }

    public String getBeforeSettingText() {
        return this.beforeSettingText;
    }
}
