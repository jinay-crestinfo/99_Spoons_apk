package com.oysb.utils;

import android.util.Log;
import com.oysb.utils.io.file.SDFileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.reflect.Field;

/* loaded from: classes2.dex */
public class Config {
    private static final boolean DEBUG_CONFIG = false;
    public static final String TAG = "Config";

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:50:0x006c -> B:16:0x007b). Please report as a decompilation issue!!! */
    private static void readConfig(Class<?> cls, String fileName) {
        File file = new File(SDFileUtils.SDCardRoot, fileName);
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return;
        }
        LineNumberReader lineNumberReader = null;
        try {
            lineNumberReader = new LineNumberReader(new FileReader(file));
            String readLine;
            while ((readLine = lineNumberReader.readLine()) != null) {
                String[] split = readLine.split("=");
                if (split.length == 2) {
                    config(cls, split[0].trim(), split[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (lineNumberReader != null) {
                try {
                    lineNumberReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static void config(Class<?> cls, String str, String str2) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            Class<?> type = declaredField.getType();
            Log.i(TAG, String.format("SET %s=%s,%s", str, str2, type));
            if (String.class.equals(type)) {
                declaredField.set(type, str2);
            } else {
                declaredField.setBoolean(type, "true".equalsIgnoreCase(str2));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (NoSuchFieldException e3) {
            e3.printStackTrace();
        }
    }

    private static void printConfig(Class<?> cls) {
        try {
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                Log.i("fldskdfk", String.format("%s=%s", field.getName(), field.get(cls)));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        }
    }

    public static void initConfig(Class<?> cls, String str) {
        readConfig(cls, str);
    }
}
