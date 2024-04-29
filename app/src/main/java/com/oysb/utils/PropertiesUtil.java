package com.oysb.utils;

import android.content.Context;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/* loaded from: classes2.dex */
public class PropertiesUtil {
    public static String getProperties(Context context, String str, String str2) {
        Properties properties = new Properties();
        try {
            properties.load(context.getAssets().open(str));
            return properties.getProperty(str2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, String> getPropertiesEx(Context context, String str, String str2) {
        HashMap<String, String> hashMap = new HashMap<>();
        Properties properties = new Properties();
        try {
            properties.load(context.getAssets().open(str));
            Enumeration<?> propertyNames = properties.propertyNames();
            while (true) {
                Object nextElement = propertyNames.nextElement();
                if (nextElement == null) {
                    break;
                }
                String obj = nextElement.toString();
                if (obj.startsWith(str2)) {
                    hashMap.put(obj, properties.getProperty(obj));
                }
            }
        } catch (Exception unused) {
        }
        return hashMap;
    }
}
