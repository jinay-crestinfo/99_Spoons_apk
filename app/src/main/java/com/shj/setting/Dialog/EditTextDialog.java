package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class EditTextDialog extends Dialog {
    private Button bt_add;
    private Button bt_ok;
    private Button bt_reduce;
    private Context context;
    private EditText et_input;
    private String input;
    private boolean isShowAddAndReduce;
    int[] listViewLocation;
    Handler mHandler;

    public EditTextDialog(Context context, String str, boolean z) {
        super(context, R.style.loading_style);
        this.mHandler = new Handler() { // from class: com.shj.setting.Dialog.EditTextDialog.6
            AnonymousClass6() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what != 0) {
                    return;
                }
                EditText editText = (EditText) message.obj;
                editText.setSelection(editText.getText().length());
                EditTextDialog.this.showKeyboard(editText);
            }
        };
        this.context = context;
        this.input = str;
        this.isShowAddAndReduce = z;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_edittext_dialog);
        this.et_input = (EditText) findViewById(R.id.et_input);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        this.bt_add = (Button) findViewById(R.id.bt_add);
        this.bt_reduce = (Button) findViewById(R.id.bt_reduce);
        if (this.isShowAddAndReduce) {
            this.bt_add.setVisibility(0);
            this.bt_reduce.setVisibility(0);
        }
        this.et_input.setText(this.input);
        String str = this.input;
        if (str != null) {
            this.et_input.setSelection(str.length());
        }
        this.et_input.setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: com.shj.setting.Dialog.EditTextDialog.1
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
        setCanceledOnTouchOutside(false);
        setListener();
        setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.shj.setting.Dialog.EditTextDialog.2
            AnonymousClass2() {
            }

            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                EditTextDialog.this.listViewLocation = new int[2];
                EditTextDialog.this.et_input.getLocationOnScreen(EditTextDialog.this.listViewLocation);
                new Thread(new Runnable() { // from class: com.shj.setting.Dialog.EditTextDialog.2.1
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        int i = 0;
                        while (i < 20) {
                            i++;
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (EditTextDialog.this.isSoftShowing()) {
                                return;
                            }
                            Message obtain = Message.obtain();
                            obtain.obj = EditTextDialog.this.et_input;
                            obtain.what = 0;
                            EditTextDialog.this.mHandler.sendMessage(obtain);
                            try {
                                Thread.sleep(200L);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                            if (EditTextDialog.this.isSoftShowing()) {
                                return;
                            }
                        }
                    }
                }).start();
            }

            /* renamed from: com.shj.setting.Dialog.EditTextDialog$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements Runnable {
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    int i = 0;
                    while (i < 20) {
                        i++;
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (EditTextDialog.this.isSoftShowing()) {
                            return;
                        }
                        Message obtain = Message.obtain();
                        obtain.obj = EditTextDialog.this.et_input;
                        obtain.what = 0;
                        EditTextDialog.this.mHandler.sendMessage(obtain);
                        try {
                            Thread.sleep(200L);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        if (EditTextDialog.this.isSoftShowing()) {
                            return;
                        }
                    }
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.EditTextDialog$1 */
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

    /* renamed from: com.shj.setting.Dialog.EditTextDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements DialogInterface.OnShowListener {
        AnonymousClass2() {
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            EditTextDialog.this.listViewLocation = new int[2];
            EditTextDialog.this.et_input.getLocationOnScreen(EditTextDialog.this.listViewLocation);
            new Thread(new Runnable() { // from class: com.shj.setting.Dialog.EditTextDialog.2.1
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    int i = 0;
                    while (i < 20) {
                        i++;
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (EditTextDialog.this.isSoftShowing()) {
                            return;
                        }
                        Message obtain = Message.obtain();
                        obtain.obj = EditTextDialog.this.et_input;
                        obtain.what = 0;
                        EditTextDialog.this.mHandler.sendMessage(obtain);
                        try {
                            Thread.sleep(200L);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        if (EditTextDialog.this.isSoftShowing()) {
                            return;
                        }
                    }
                }
            }).start();
        }

        /* renamed from: com.shj.setting.Dialog.EditTextDialog$2$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                int i = 0;
                while (i < 20) {
                    i++;
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (EditTextDialog.this.isSoftShowing()) {
                        return;
                    }
                    Message obtain = Message.obtain();
                    obtain.obj = EditTextDialog.this.et_input;
                    obtain.what = 0;
                    EditTextDialog.this.mHandler.sendMessage(obtain);
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    if (EditTextDialog.this.isSoftShowing()) {
                        return;
                    }
                }
            }
        }
    }

    public String getInput() {
        return this.et_input.getText().toString();
    }

    public boolean isSoftShowing() {
        int[] iArr = new int[2];
        this.et_input.getLocationOnScreen(iArr);
        return this.listViewLocation[1] != iArr[1];
    }

    public void showKeyboard(EditText editText) {
        if (editText != null) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            ((InputMethodManager) this.context.getSystemService("input_method")).showSoftInput(editText, 0);
        }
    }

    /* renamed from: com.shj.setting.Dialog.EditTextDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            EditTextDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.EditTextDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                EditTextDialog.this.dismiss();
            }
        });
        this.bt_add.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.EditTextDialog.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    EditTextDialog.this.et_input.setText(String.valueOf(Integer.valueOf(EditTextDialog.this.et_input.getText().toString()).intValue() + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.bt_reduce.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.EditTextDialog.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    int intValue = Integer.valueOf(EditTextDialog.this.et_input.getText().toString()).intValue();
                    if (intValue >= 1) {
                        EditTextDialog.this.et_input.setText(String.valueOf(intValue - 1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.EditTextDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            try {
                EditTextDialog.this.et_input.setText(String.valueOf(Integer.valueOf(EditTextDialog.this.et_input.getText().toString()).intValue() + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.EditTextDialog$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            try {
                int intValue = Integer.valueOf(EditTextDialog.this.et_input.getText().toString()).intValue();
                if (intValue >= 1) {
                    EditTextDialog.this.et_input.setText(String.valueOf(intValue - 1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.Dialog.EditTextDialog$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 extends Handler {
        AnonymousClass6() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 0) {
                return;
            }
            EditText editText = (EditText) message.obj;
            editText.setSelection(editText.getText().length());
            EditTextDialog.this.showKeyboard(editText);
        }
    }
}
