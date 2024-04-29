package com.shj.setting.widget;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class DropEditeTextView extends AbsItemView {
    private ChangeListener changeListener;
    private Context context;
    private DropEditTextData data;
    private EditText et_input;
    private ListPopupWindow listPopupWindow;
    private TextView tv_name;

    /* loaded from: classes2.dex */
    public interface ChangeListener {
        void change();
    }

    /* loaded from: classes2.dex */
    public static class DropEditTextData {
        public String[] diopList;
        public String name;
        public String tipInfo;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public DropEditeTextView(Context context, DropEditTextData dropEditTextData) {
        super(context);
        this.context = context;
        this.data = dropEditTextData;
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_edite_item, (ViewGroup) null);
        addContentView(inflate);
        this.tv_name = (TextView) inflate.findViewById(R.id.tv_name);
        this.et_input = (EditText) inflate.findViewById(R.id.et_input);
        this.tv_name.setText(this.data.name);
        if (this.data.tipInfo != null) {
            SpannableString spannableString = new SpannableString(this.data.tipInfo);
            spannableString.setSpan(new AbsoluteSizeSpan(this.context.getResources().getDimensionPixelSize(R.dimen.hint_size), false), 0, spannableString.length(), 33);
            this.et_input.setHint(new SpannedString(spannableString));
        }
        this.et_input.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.shj.setting.widget.DropEditeTextView.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    DropEditeTextView.this.showListPopulWindow();
                }
            }
        });
        this.et_input.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.widget.DropEditeTextView.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DropEditeTextView.this.et_input.isFocused()) {
                    if (DropEditeTextView.this.listPopupWindow == null || !DropEditeTextView.this.listPopupWindow.isShowing()) {
                        DropEditeTextView.this.showListPopulWindow();
                    }
                }
            }
        });
        this.et_input.addTextChangedListener(new TextWatcher() { // from class: com.shj.setting.widget.DropEditeTextView.3
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass3() {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (DropEditeTextView.this.changeListener == null || TextUtils.isEmpty(charSequence)) {
                    return;
                }
                DropEditeTextView.this.changeListener.change();
            }
        });
    }

    /* renamed from: com.shj.setting.widget.DropEditeTextView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnFocusChangeListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z) {
            if (z) {
                DropEditeTextView.this.showListPopulWindow();
            }
        }
    }

    /* renamed from: com.shj.setting.widget.DropEditeTextView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (DropEditeTextView.this.et_input.isFocused()) {
                if (DropEditeTextView.this.listPopupWindow == null || !DropEditeTextView.this.listPopupWindow.isShowing()) {
                    DropEditeTextView.this.showListPopulWindow();
                }
            }
        }
    }

    /* renamed from: com.shj.setting.widget.DropEditeTextView$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass3() {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (DropEditeTextView.this.changeListener == null || TextUtils.isEmpty(charSequence)) {
                return;
            }
            DropEditeTextView.this.changeListener.change();
        }
    }

    public void showListPopulWindow() {
        ListPopupWindow listPopupWindow = new ListPopupWindow(this.context);
        this.listPopupWindow = listPopupWindow;
        listPopupWindow.setAdapter(new ArrayAdapter(this.context, android.R.layout.simple_list_item_1, this.data.diopList));
        this.listPopupWindow.setAnchorView(this.et_input);
        this.listPopupWindow.setModal(true);
        this.listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shj.setting.widget.DropEditeTextView.4
            AnonymousClass4() {
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                DropEditeTextView.this.et_input.setText(DropEditeTextView.this.data.diopList[i]);
                DropEditeTextView.this.et_input.setSelection(DropEditeTextView.this.data.diopList[i].length());
                DropEditeTextView.this.listPopupWindow.dismiss();
                if (DropEditeTextView.this.changeListener != null) {
                    DropEditeTextView.this.changeListener.change();
                }
            }
        });
        this.listPopupWindow.show();
    }

    /* renamed from: com.shj.setting.widget.DropEditeTextView$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements AdapterView.OnItemClickListener {
        AnonymousClass4() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            DropEditeTextView.this.et_input.setText(DropEditeTextView.this.data.diopList[i]);
            DropEditeTextView.this.et_input.setSelection(DropEditeTextView.this.data.diopList[i].length());
            DropEditeTextView.this.listPopupWindow.dismiss();
            if (DropEditeTextView.this.changeListener != null) {
                DropEditeTextView.this.changeListener.change();
            }
        }
    }

    public String getEditeText() {
        return this.et_input.getText().toString();
    }

    public void setEditeText(String str) {
        this.et_input.setText(str);
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }
}
