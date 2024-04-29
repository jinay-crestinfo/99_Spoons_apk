package com.xyshj.database.setting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String name = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xyShj/setting.db";
    private static final int version = 3;

    public MySQLiteOpenHelper(Context context) {
        super(context, name, (SQLiteDatabase.CursorFactory) null, 3);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        UserSettingDao.T.createTable(sQLiteDatabase);
        UserSettingDao.sEnable.createTable(sQLiteDatabase);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (i == 1) {
            sQLiteDatabase.execSQL("drop table t_setting");
        }
        UserSettingDao.T.createTable(sQLiteDatabase);
        UserSettingDao.sEnable.createTable(sQLiteDatabase);
    }
}
