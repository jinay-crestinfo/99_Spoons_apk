package com.shj.setting.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.shj.ShjDbHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class SharedPreferenceUtils {
    public static SharedPreferences readSharedPreferences(Context context) {
        return context.getSharedPreferences("shelves_list", 0);
    }

    public static void saveShelves(Context context, List<Integer> list) {
        String str = "";
        if (list != null) {
            int i = 0;
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                if (i == 0) {
                    str = str + intValue;
                } else {
                    str = str + VoiceWakeuperAidl.PARAMS_SEPARATE + intValue;
                }
                i++;
            }
            readSharedPreferences(context).edit().putString(ShjDbHelper.TABLE_NAME, str).commit();
            return;
        }
        readSharedPreferences(context).edit().putString(ShjDbHelper.TABLE_NAME, "").commit();
    }

    public static List<Integer> getShelves(Context context) {
        ArrayList arrayList = new ArrayList();
        String string = readSharedPreferences(context).getString(ShjDbHelper.TABLE_NAME, "");
        if (!TextUtils.isEmpty(string)) {
            for (String str : string.split(VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                arrayList.add(Integer.valueOf(str));
            }
        }
        return arrayList;
    }
}
