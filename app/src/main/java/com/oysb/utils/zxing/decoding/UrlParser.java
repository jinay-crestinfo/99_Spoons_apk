package com.oysb.utils.zxing.decoding;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class UrlParser {
    public static String UrlPage(String str) {
        String lowerCase = str.trim().toLowerCase();
        String[] split = lowerCase.split("[?]");
        if (lowerCase.length() <= 0 || split.length <= 1 || split[0] == null) {
            return null;
        }
        return split[0];
    }

    private static String TruncateUrlPage(String str) {
        String lowerCase = str.trim().toLowerCase();
        String[] split = lowerCase.split("[?]");
        if (lowerCase.length() <= 1 || split.length <= 1 || split[1] == null) {
            return null;
        }
        return split[1];
    }

    public static Map<String, String> parseUrlParams(String str) {
        HashMap hashMap = new HashMap();
        String TruncateUrlPage = TruncateUrlPage(str);
        if (TruncateUrlPage == null) {
            return hashMap;
        }
        for (String str2 : TruncateUrlPage.split("[&]")) {
            String[] split = str2.split("[=]");
            if (split.length > 1) {
                hashMap.put(split[0], split[1]);
            } else if (split[0] != "") {
                hashMap.put(split[0], "");
            }
        }
        return hashMap;
    }
}
