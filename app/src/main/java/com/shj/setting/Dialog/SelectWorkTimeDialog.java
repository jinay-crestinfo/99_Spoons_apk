package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class SelectWorkTimeDialog extends Dialog {
    private Button bt_cancel;
    private Button bt_end_add;
    private Button bt_end_reduce;
    private Button bt_ok;
    private Button bt_start_add;
    private Button bt_start_reduce;
    private ButtonListener buttonListener;
    private Context context;
    private String end;
    private String start;
    private TextView tv_end;
    private TextView tv_start;

    /* loaded from: classes2.dex */
    public interface ButtonListener {
        void buttonClick_OK(String str, String str2);
    }

    public SelectWorkTimeDialog(Context context, String str, String str2) {
        super(context, R.style.loading_style);
        this.context = context;
        this.start = str;
        this.end = str2;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_select_work_time);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        this.bt_cancel = (Button) findViewById(R.id.bt_cancel);
        this.bt_start_reduce = (Button) findViewById(R.id.bt_start_reduce);
        this.tv_start = (TextView) findViewById(R.id.tv_start);
        this.bt_start_add = (Button) findViewById(R.id.bt_start_add);
        this.bt_end_reduce = (Button) findViewById(R.id.bt_end_reduce);
        this.tv_end = (TextView) findViewById(R.id.tv_end);
        this.bt_end_add = (Button) findViewById(R.id.bt_end_add);
        this.tv_start.setText(this.start);
        this.tv_end.setText(this.end);
        setCanceledOnTouchOutside(false);
        setListener();
    }

    /* renamed from: com.shj.setting.Dialog.SelectWorkTimeDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
            int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_start);
            if (textValue > 0) {
                SelectWorkTimeDialog.this.tv_start.setText(String.valueOf(textValue - 1));
            }
        }
    }

    private void setListener() {
        this.bt_start_reduce.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectWorkTimeDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
                int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_start);
                if (textValue > 0) {
                    SelectWorkTimeDialog.this.tv_start.setText(String.valueOf(textValue - 1));
                }
            }
        });
        this.bt_start_reduce.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shj.setting.Dialog.SelectWorkTimeDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
                int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_start);
                if (textValue > 0) {
                    int i = textValue - 8;
                    if (i < 0) {
                        i = 0;
                    }
                    SelectWorkTimeDialog.this.tv_start.setText(String.valueOf(i));
                }
                return false;
            }
        });
        this.bt_start_add.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectWorkTimeDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
                int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_start);
                if (textValue < 24) {
                    SelectWorkTimeDialog.this.tv_start.setText(String.valueOf(textValue + 1));
                }
            }
        });
        this.bt_start_add.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shj.setting.Dialog.SelectWorkTimeDialog.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
                int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_start);
                if (textValue >= 24) {
                    return false;
                }
                int i = textValue + 8;
                SelectWorkTimeDialog.this.tv_start.setText(String.valueOf(i <= 24 ? i : 24));
                return false;
            }
        });
        this.bt_end_reduce.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectWorkTimeDialog.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
                int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_end);
                if (textValue > 0) {
                    SelectWorkTimeDialog.this.tv_end.setText(String.valueOf(textValue - 1));
                }
            }
        });
        this.bt_end_reduce.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shj.setting.Dialog.SelectWorkTimeDialog.6
            AnonymousClass6() {
            }

            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
                int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_end);
                if (textValue > 0) {
                    int i = textValue - 8;
                    if (i < 0) {
                        i = 0;
                    }
                    SelectWorkTimeDialog.this.tv_end.setText(String.valueOf(i));
                }
                return false;
            }
        });
        this.bt_end_add.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectWorkTimeDialog.7
            AnonymousClass7() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
                int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_end);
                if (textValue < 24) {
                    SelectWorkTimeDialog.this.tv_end.setText(String.valueOf(textValue + 1));
                }
            }
        });
        this.bt_end_add.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shj.setting.Dialog.SelectWorkTimeDialog.8
            AnonymousClass8() {
            }

            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
                int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_end);
                if (textValue >= 24) {
                    return false;
                }
                int i = textValue + 8;
                SelectWorkTimeDialog.this.tv_end.setText(String.valueOf(i <= 24 ? i : 24));
                return false;
            }
        });
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectWorkTimeDialog.9
            AnonymousClass9() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SelectWorkTimeDialog.this.buttonListener != null) {
                    SelectWorkTimeDialog.this.buttonListener.buttonClick_OK(SelectWorkTimeDialog.this.tv_start.getText().toString(), SelectWorkTimeDialog.this.tv_end.getText().toString());
                }
                SelectWorkTimeDialog.this.dismiss();
            }
        });
        this.bt_cancel.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.SelectWorkTimeDialog.10
            AnonymousClass10() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectWorkTimeDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.SelectWorkTimeDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnLongClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
            int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_start);
            if (textValue > 0) {
                int i = textValue - 8;
                if (i < 0) {
                    i = 0;
                }
                SelectWorkTimeDialog.this.tv_start.setText(String.valueOf(i));
            }
            return false;
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectWorkTimeDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
            int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_start);
            if (textValue < 24) {
                SelectWorkTimeDialog.this.tv_start.setText(String.valueOf(textValue + 1));
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectWorkTimeDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnLongClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
            int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_start);
            if (textValue >= 24) {
                return false;
            }
            int i = textValue + 8;
            SelectWorkTimeDialog.this.tv_start.setText(String.valueOf(i <= 24 ? i : 24));
            return false;
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectWorkTimeDialog$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
            int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_end);
            if (textValue > 0) {
                SelectWorkTimeDialog.this.tv_end.setText(String.valueOf(textValue - 1));
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectWorkTimeDialog$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements View.OnLongClickListener {
        AnonymousClass6() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
            int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_end);
            if (textValue > 0) {
                int i = textValue - 8;
                if (i < 0) {
                    i = 0;
                }
                SelectWorkTimeDialog.this.tv_end.setText(String.valueOf(i));
            }
            return false;
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectWorkTimeDialog$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements View.OnClickListener {
        AnonymousClass7() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
            int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_end);
            if (textValue < 24) {
                SelectWorkTimeDialog.this.tv_end.setText(String.valueOf(textValue + 1));
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectWorkTimeDialog$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements View.OnLongClickListener {
        AnonymousClass8() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            SelectWorkTimeDialog selectWorkTimeDialog = SelectWorkTimeDialog.this;
            int textValue = selectWorkTimeDialog.getTextValue(selectWorkTimeDialog.tv_end);
            if (textValue >= 24) {
                return false;
            }
            int i = textValue + 8;
            SelectWorkTimeDialog.this.tv_end.setText(String.valueOf(i <= 24 ? i : 24));
            return false;
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectWorkTimeDialog$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements View.OnClickListener {
        AnonymousClass9() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SelectWorkTimeDialog.this.buttonListener != null) {
                SelectWorkTimeDialog.this.buttonListener.buttonClick_OK(SelectWorkTimeDialog.this.tv_start.getText().toString(), SelectWorkTimeDialog.this.tv_end.getText().toString());
            }
            SelectWorkTimeDialog.this.dismiss();
        }
    }

    /* renamed from: com.shj.setting.Dialog.SelectWorkTimeDialog$10 */
    /* loaded from: classes2.dex */
    public class AnonymousClass10 implements View.OnClickListener {
        AnonymousClass10() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SelectWorkTimeDialog.this.dismiss();
        }
    }

    public int getTextValue(TextView textView) {
        String charSequence = textView.getText().toString();
        if (TextUtils.isEmpty(charSequence)) {
            return 0;
        }
        return Integer.valueOf(charSequence).intValue();
    }

    public void setButtonListener(ButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }
}
