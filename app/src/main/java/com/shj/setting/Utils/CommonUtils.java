package com.shj.setting.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class CommonUtils {
    public static boolean isNull(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNull(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static List<String> getRegEx(String str, String str2) {
        ArrayList arrayList = new ArrayList();
        Matcher matcher = Pattern.compile(str2).matcher(str);
        while (matcher.find()) {
            arrayList.add(matcher.group());
        }
        return arrayList;
    }
}
