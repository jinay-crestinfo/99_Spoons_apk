package com.oysb.utils.cache;

import android.content.Context;
import java.io.File;

/* loaded from: classes2.dex */
public class CacheHelper {
    private static File g_file;

    public static void setCacheFile(File file) {
        g_file = file;
    }

    public static ACache getFileCache() {
        return ACache.get(g_file);
    }

    public static ACache getContextCache(Context context) {
        return ACache.get(context);
    }
}
