package com.shj.setting.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.shj.setting.R;

/* loaded from: classes2.dex */
public class ToastUitl {
    public static Toast showToastWithImg(Context context, String str, int i) {
        return null;
    }

    private static Toast initToast(Context context, CharSequence charSequence, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_toast, (ViewGroup) null, true);
        ((TextView) inflate.findViewById(R.id.text)).setText(charSequence);
        Toast toast = new Toast(context);
        toast.setGravity(17, 0, 0);
        toast.setDuration(i);
        toast.setView(inflate);
        return toast;
    }

    public static void showShort(Context context, CharSequence charSequence) {
        initToast(context, charSequence, 0).show();
    }

    public static void showShort(Context context, int i) {
        initToast(context, context.getResources().getText(i), 0).show();
    }

    public static void showLong(Context context, CharSequence charSequence) {
        initToast(context, charSequence, 1).show();
    }

    public static void showLong(Context context, int i) {
        initToast(context, context.getResources().getText(i), 1).show();
    }

    public static void show(Context context, CharSequence charSequence, int i) {
        initToast(context, charSequence, i).show();
    }

    public static void show(Context context, int i, int i2) {
        initToast(context, context.getResources().getText(i), i2).show();
    }

    public static void showCompeleteTip(Context context, boolean z, String str) {
        showTextTip(context, z, "", str);
    }

    public static void showSetCompeleteTip(Context context, boolean z, String str) {
        showTextTip(context, z, context.getString(R.string.setting), str);
    }

    public static void showQueryCompeleteTip(Context context, boolean z, String str) {
        showTextTip(context, z, context.getString(R.string.query), str);
    }

    public static void showTextTip(Context context, boolean z, String str, String str2) {
        String str3;
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        if (z) {
            str3 = str2 + context.getString(R.string.success);
        } else {
            str3 = str2 + context.getString(R.string.fail);
        }
        if (!str3.startsWith(str)) {
            str3 = str + str3;
        }
        showLong(context, str3);
    }

    public static void showNotInputTip(Context context) {
        showShort(context, R.string.not_input_tip);
    }

    public static void showPasswordTip(Context context) {
        showShort(context, R.string.input_error_password);
    }

    public static void showErrorInputTip(Context context, String str) {
        showShort(context, context.getResources().getString(R.string.error_input_tip) + str);
    }

    public static void showSaveSuccessTip(Context context) {
        showShort(context, R.string.save_success);
    }
}
