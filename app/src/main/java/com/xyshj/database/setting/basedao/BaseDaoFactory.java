package com.xyshj.database.setting.basedao;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/* loaded from: classes2.dex */
public class BaseDaoFactory {
    private static String mDbPath;
    private SQLiteDatabase mDatabase;


    /* loaded from: classes2.dex */
    private static class Instance {
        public static BaseDaoFactory INSTANCE = new BaseDaoFactory();

        private Instance() {
        }
    }

    public static BaseDaoFactory getInstance() {
        return Instance.INSTANCE;
    }

    public static void init(String str) {
        mDbPath = str;
    }

    private BaseDaoFactory() {
        if (TextUtils.isEmpty(mDbPath)) {
            throw new RuntimeException("在使用BaseDaoFactory之前，请调用BaseDaoFactory.init()初始化好数据库路径。");
        }
        this.mDatabase = SQLiteDatabase.openOrCreateDatabase(mDbPath, (SQLiteDatabase.CursorFactory) null);
    }

    public <T extends BaseDao<M>, M> T getDataHelper(Class<T> cls, Class<M> cls2) {
        T t = null;
        try {
            T newInstance = cls.newInstance();
            newInstance.init(this.mDatabase, cls2);
            return newInstance;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return t;
        } catch (InstantiationException e4) {
            e4.printStackTrace();
            return t;
        }
    }
}
