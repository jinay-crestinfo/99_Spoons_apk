package com.oysb.utils.cache;

import android.graphics.Bitmap;
import android.util.LruCache;
import java.util.ArrayList;
import java.util.List;

public class BitmapCache {
    private static final int DEFAULT_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);
    private static final List<String> keys = new ArrayList<>();
    private static LruCache<String, Bitmap> mMemoryCache;

    static {
        init();
    }

    private static void init() {
        mMemoryCache = new LruCache<String, Bitmap>(DEFAULT_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (mMemoryCache == null) {
            init();
        }
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
            keys.add(key);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        if (mMemoryCache == null) {
            init();
        }
        Bitmap bitmap = mMemoryCache.get(key);
        if (bitmap == null || bitmap.isRecycled()) {
            mMemoryCache.remove(key);
            return null;
        }
        return bitmap;
    }

    public static void clearCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
            keys.clear();
        }
    }

    public static void clearCacheStartWithKey(String prefix) {
        if (mMemoryCache != null) {
            for (String key : keys) {
                if (key.startsWith(prefix)) {
                    mMemoryCache.remove(key);
                }
            }
        }
    }
}
