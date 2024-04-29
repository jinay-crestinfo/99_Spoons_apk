package com.xyshj.machine.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/* loaded from: classes2.dex */
public class TextView_add_text_space extends TextView {
    private CharSequence originalText;
    private float spacing;

    public TextView_add_text_space(Context context) {
        super(context);
        this.spacing = 0.0f;
        this.originalText = "";
    }

    public TextView_add_text_space(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.spacing = 0.0f;
        this.originalText = "";
    }

    public TextView_add_text_space(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.spacing = 0.0f;
        this.originalText = "";
    }

    public float getSpacing() {
        return this.spacing;
    }

    public void setSpacing(float f) {
        this.spacing = f;
        applySpacing();
    }

    @Override // android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        this.originalText = charSequence;
        applySpacing();
    }

    @Override // android.widget.TextView
    public CharSequence getText() {
        return this.originalText;
    }

    private void applySpacing() {
        if (this.originalText == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < this.originalText.length()) {
            sb.append(this.originalText.charAt(i));
            int i2 = i + 1;
            if (i2 < this.originalText.length()) {
                if (isEnglish(this.originalText.charAt(i) + "")) {
                    if (isEnglish(this.originalText.charAt(i2) + "")) {
                    }
                }
                sb.append(" ");
            }
            i = i2;
        }
        SpannableString spannableString = new SpannableString(sb.toString());
        if (sb.toString().length() > 1) {
            for (int i3 = 1; i3 < sb.toString().length(); i3 += 2) {
                spannableString.setSpan(new ScaleXSpan((this.spacing + 1.0f) / 10.0f), i3, i3 + 1, 33);
            }
        }
        super.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    /* loaded from: classes2.dex */
    public class Spacing {
        public static final float NORMAL = 0.0f;

        public Spacing() {
        }
    }

    public static boolean isEnglish(String str) {
        return str.matches("^[a-zA-Z]*");
    }
}
