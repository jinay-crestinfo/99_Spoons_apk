package com.shj.setting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.shj.setting.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class RadioGroupItemView extends AbsItemView {
    private ChangeListener changeListener;
    private Context context;
    private boolean isListering;
    private List<RadioButton> radioButtonList;
    private RadioGroupData radioGroupData;
    private TextView tv_name;

    /* loaded from: classes2.dex */
    public interface ChangeListener {
        void change();
    }

    /* loaded from: classes2.dex */
    public static class RadioGroupData {
        public boolean isVertical;
        public List<String> nameList;
        public String title;
    }

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public RadioGroupItemView(Context context, RadioGroupData radioGroupData) {
        super(context);
        this.isListering = true;
        this.context = context;
        this.radioGroupData = radioGroupData;
        initView();
    }

    private void initView() {
        int i;
        if (this.radioGroupData.nameList != null) {
            if (this.radioGroupData.isVertical) {
                i = R.layout.layout_radio_group_vertical;
            } else {
                i = R.layout.layout_radio_group;
            }
            View inflate = LayoutInflater.from(this.context).inflate(i, (ViewGroup) null);
            TextView textView = (TextView) inflate.findViewById(R.id.tv_name);
            this.tv_name = textView;
            textView.setText(this.radioGroupData.title);
            RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.radio_group);
            addContentView(inflate);
            int childCount = radioGroup.getChildCount();
            this.radioButtonList = new ArrayList();
            int i2 = 0;
            Iterator<String> it = this.radioGroupData.nameList.iterator();
            while (it.hasNext()) {
                addRadioButton(i2, childCount, it.next(), radioGroup);
                i2++;
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.shj.setting.widget.RadioGroupItemView.1
                AnonymousClass1() {
                }

                @Override // android.widget.RadioGroup.OnCheckedChangeListener
                public void onCheckedChanged(RadioGroup radioGroup2, int i3) {
                    if (!RadioGroupItemView.this.isListering || RadioGroupItemView.this.changeListener == null) {
                        return;
                    }
                    RadioGroupItemView.this.changeListener.change();
                }
            });
        }
    }

    /* renamed from: com.shj.setting.widget.RadioGroupItemView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RadioGroup.OnCheckedChangeListener {
        AnonymousClass1() {
        }

        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup2, int i3) {
            if (!RadioGroupItemView.this.isListering || RadioGroupItemView.this.changeListener == null) {
                return;
            }
            RadioGroupItemView.this.changeListener.change();
        }
    }

    private void addRadioButton(int i, int i2, String str, RadioGroup radioGroup) {
        if (i < i2) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setVisibility(0);
            radioButton.setText(str);
            this.radioButtonList.add(radioButton);
        }
    }

    public int getRadioButtonCheckIndex() {
        for (int i = 0; i < this.radioButtonList.size(); i++) {
            if (this.radioButtonList.get(i).isChecked()) {
                return i;
            }
        }
        return -1;
    }

    public void setRadioButtonCheck(int i) {
        this.isListering = false;
        if (i < this.radioButtonList.size()) {
            this.radioButtonList.get(i).setChecked(true);
        }
        this.isListering = true;
    }

    public void setRadioButtonHide(int i) {
        if (i < this.radioButtonList.size()) {
            this.radioButtonList.get(i).setVisibility(8);
        }
    }

    public void setRadioButtonClickListener(int i, View.OnClickListener onClickListener) {
        if (i < this.radioButtonList.size()) {
            this.radioButtonList.get(i).setOnClickListener(onClickListener);
        }
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }
}
