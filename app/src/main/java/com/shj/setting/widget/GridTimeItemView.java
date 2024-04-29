package com.shj.setting.widget;

import android.content.Context;
import android.content.DialogInterface;
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
import com.shj.setting.widget.GargoTimeGridView;

/* loaded from: classes2.dex */
public class GridTimeItemView extends LinearLayout {
    private Context context;
    private EditText ev_time_end;
    private EditText ev_time_start;
    private GargoTimeGridView.TimeItemData itemData;
    private TextView tv_index;

    public GridTimeItemView(Context context, GargoTimeGridView.TimeItemData timeItemData) {
        super(context);
        this.context = context;
        this.itemData = timeItemData;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(R.layout.layout_grid_time_item, this);
        this.tv_index = (TextView) findViewById(R.id.tv_index);
        this.ev_time_start = (EditText) findViewById(R.id.ev_time_start);
        this.ev_time_end = (EditText) findViewById(R.id.ev_time_end);
        this.ev_time_start.setInputType(2);
        this.ev_time_end.setInputType(2);
        GargoTimeGridView.TimeItemData timeItemData = this.itemData;
        if (timeItemData != null) {
            if (timeItemData.textIdentifier != null) {
                this.tv_index.setText(this.itemData.textIdentifier);
            } else {
                this.tv_index.setText(String.valueOf(this.itemData.identifier));
            }
        }
        if (this.itemData.star != -1) {
            this.ev_time_start.setText(String.valueOf(this.itemData.star));
        }
        if (this.itemData.end != -1) {
            this.ev_time_end.setText(String.valueOf(this.itemData.end));
        }
        this.ev_time_start.setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: com.shj.setting.widget.GridTimeItemView.1
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
        this.ev_time_start.setImeOptions(268435456);
        this.ev_time_end.setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: com.shj.setting.widget.GridTimeItemView.2
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

            AnonymousClass2() {
            }
        });
        this.ev_time_end.setImeOptions(268435456);
        this.ev_time_start.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shj.setting.widget.GridTimeItemView.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                GridTimeItemView gridTimeItemView = GridTimeItemView.this;
                gridTimeItemView.showInputDialog(gridTimeItemView.ev_time_start.getText().toString(), true);
                return false;
            }
        });
        this.ev_time_end.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shj.setting.widget.GridTimeItemView.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                GridTimeItemView gridTimeItemView = GridTimeItemView.this;
                gridTimeItemView.showInputDialog(gridTimeItemView.ev_time_end.getText().toString(), false);
                return false;
            }
        });
    }

    /* renamed from: com.shj.setting.widget.GridTimeItemView$1 */
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

    /* renamed from: com.shj.setting.widget.GridTimeItemView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements ActionMode.Callback {
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

        AnonymousClass2() {
        }
    }

    /* renamed from: com.shj.setting.widget.GridTimeItemView$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnLongClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            GridTimeItemView gridTimeItemView = GridTimeItemView.this;
            gridTimeItemView.showInputDialog(gridTimeItemView.ev_time_start.getText().toString(), true);
            return false;
        }
    }

    /* renamed from: com.shj.setting.widget.GridTimeItemView$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnLongClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            GridTimeItemView gridTimeItemView = GridTimeItemView.this;
            gridTimeItemView.showInputDialog(gridTimeItemView.ev_time_end.getText().toString(), false);
            return false;
        }
    }

    public void showInputDialog(String str, boolean z) {
        EditTextDialog editTextDialog = new EditTextDialog(this.context, str, false);
        editTextDialog.show();
        editTextDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.shj.setting.widget.GridTimeItemView.5
            final /* synthetic */ EditTextDialog val$editTextDialog;
            final /* synthetic */ boolean val$isStart;

            AnonymousClass5(boolean z2, EditTextDialog editTextDialog2) {
                z = z2;
                editTextDialog = editTextDialog2;
            }

            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                if (z) {
                    GridTimeItemView.this.ev_time_start.setText(editTextDialog.getInput());
                    GridTimeItemView.this.ev_time_start.setSelection(GridTimeItemView.this.ev_time_start.getText().length());
                } else {
                    GridTimeItemView.this.ev_time_end.setText(editTextDialog.getInput());
                    GridTimeItemView.this.ev_time_end.setSelection(GridTimeItemView.this.ev_time_end.getText().length());
                }
            }
        });
    }

    /* renamed from: com.shj.setting.widget.GridTimeItemView$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements DialogInterface.OnDismissListener {
        final /* synthetic */ EditTextDialog val$editTextDialog;
        final /* synthetic */ boolean val$isStart;

        AnonymousClass5(boolean z2, EditTextDialog editTextDialog2) {
            z = z2;
            editTextDialog = editTextDialog2;
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            if (z) {
                GridTimeItemView.this.ev_time_start.setText(editTextDialog.getInput());
                GridTimeItemView.this.ev_time_start.setSelection(GridTimeItemView.this.ev_time_start.getText().length());
            } else {
                GridTimeItemView.this.ev_time_end.setText(editTextDialog.getInput());
                GridTimeItemView.this.ev_time_end.setSelection(GridTimeItemView.this.ev_time_end.getText().length());
            }
        }
    }

    public GargoTimeGridView.TimeItemData getItemData() {
        String obj = this.ev_time_start.getText().toString();
        String obj2 = this.ev_time_end.getText().toString();
        if (!TextUtils.isEmpty(obj)) {
            this.itemData.star = Integer.valueOf(obj).intValue();
        }
        if (!TextUtils.isEmpty(obj2)) {
            this.itemData.end = Integer.valueOf(obj2).intValue();
        }
        return this.itemData;
    }

    public void setItemData(String str, String str2) {
        this.ev_time_start.setText(str);
        this.ev_time_end.setText(str2);
    }

    public void setBeforeSettingData(String str, String str2) {
        this.itemData.beforeSettingStar = Integer.valueOf(str).intValue();
        this.itemData.beforeSettingEnd = Integer.valueOf(str2).intValue();
    }

    public int getIdentifier() {
        return this.itemData.identifier;
    }

    public int getUpperLevelNumber() {
        return this.itemData.upperLevelNumber;
    }
}
