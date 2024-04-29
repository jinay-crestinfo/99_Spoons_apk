package com.xyshj.database.setting;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/* loaded from: classes2.dex */
public class UserSettingDao {
    private SQLiteDatabase mDatabase;
    private MySQLiteOpenHelper mOpenHelper;

    /* loaded from: classes2.dex */
    public static class T {
        public static final String NAME = "t_setting";

        /* loaded from: classes2.dex */
        public static class Column {
            public static final String EID = "eid";
            public static final String KEY = "key";
            public static final String PARENTID = "parentId";
            public static final String VALUE = "value";
        }

        public static void createTable(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("create table if not exists t_setting(key text,eid integer,parentId integer,value  text)");
        }
    }

    public UserSettingDao(Context context) {
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        this.mOpenHelper = mySQLiteOpenHelper;
        this.mDatabase = mySQLiteOpenHelper.getWritableDatabase();
    }

    public long insert(UserSetting userSetting) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(T.Column.EID, Integer.valueOf(userSetting.getEid()));
        contentValues.put(T.Column.PARENTID, Integer.valueOf(userSetting.getParentId()));
        contentValues.put("key", userSetting.getKey());
        contentValues.put("value", userSetting.getValue());
        return isHaveThisUser(userSetting) ? this.mDatabase.update(T.NAME, contentValues, "key = ? ", new String[]{userSetting.getKey()}) : this.mDatabase.insert(T.NAME, null, contentValues);
    }

    public int remove(String str) {
        return this.mDatabase.delete(T.NAME, "key = ? ", new String[]{str});
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0028, code lost:
    
        if (r1 != null) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0036, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0033, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0031, code lost:
    
        if (r1 == null) goto L43;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isHaveThisUser(com.xyshj.database.setting.UserSetting r13) {
        /*
            r12 = this;
            r0 = 0
            r1 = 0
            android.database.sqlite.SQLiteDatabase r2 = r12.mDatabase     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            java.lang.String r3 = "t_setting"
            r4 = 0
            java.lang.String r5 = "key = ? "
            r11 = 1
            java.lang.String[] r6 = new java.lang.String[r11]     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            java.lang.String r13 = r13.getKey()     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            r6[r0] = r13     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            android.database.Cursor r1 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            if (r1 == 0) goto L28
            int r13 = r1.getCount()     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            if (r13 <= 0) goto L28
            if (r1 == 0) goto L27
            r1.close()
        L27:
            return r11
        L28:
            if (r1 == 0) goto L36
            goto L33
        L2b:
            r13 = move-exception
            goto L37
        L2d:
            r13 = move-exception
            r13.printStackTrace()     // Catch: java.lang.Throwable -> L2b
            if (r1 == 0) goto L36
        L33:
            r1.close()
        L36:
            return r0
        L37:
            if (r1 == 0) goto L3c
            r1.close()
        L3c:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.database.setting.UserSettingDao.isHaveThisUser(com.xyshj.database.setting.UserSetting):boolean");
    }

    public int update(UserSetting userSetting) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(T.Column.EID, Integer.valueOf(userSetting.getEid()));
        contentValues.put(T.Column.PARENTID, Integer.valueOf(userSetting.getParentId()));
        contentValues.put("key", userSetting.getKey());
        contentValues.put("value", userSetting.getValue());
        return this.mDatabase.update(T.NAME, contentValues, "key = ? ", new String[]{userSetting.getKey()});
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0062, code lost:
    
        if (r12 == null) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0052, code lost:
    
        if (r12 != null) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0054, code lost:
    
        r12.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0065, code lost:
    
        return r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0069  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.xyshj.database.setting.UserSetting getUserSettingFormKey(java.lang.String r12) {
        /*
            r11 = this;
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r11.mDatabase     // Catch: java.lang.Throwable -> L58 java.lang.Exception -> L5d
            java.lang.String r2 = "t_setting"
            r3 = 0
            java.lang.String r4 = "key = ? "
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch: java.lang.Throwable -> L58 java.lang.Exception -> L5d
            r6 = 0
            r5[r6] = r12     // Catch: java.lang.Throwable -> L58 java.lang.Exception -> L5d
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            android.database.Cursor r12 = r1.query(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L58 java.lang.Exception -> L5d
            if (r12 == 0) goto L52
            int r1 = r12.getCount()     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            if (r1 <= 0) goto L52
            r12.moveToFirst()     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            java.lang.String r1 = "eid"
            int r1 = r12.getColumnIndex(r1)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            int r1 = r12.getInt(r1)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            java.lang.String r2 = "parentId"
            int r2 = r12.getColumnIndex(r2)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            int r2 = r12.getInt(r2)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            java.lang.String r3 = "key"
            int r3 = r12.getColumnIndex(r3)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            java.lang.String r3 = r12.getString(r3)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            java.lang.String r4 = "value"
            int r4 = r12.getColumnIndex(r4)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            java.lang.String r4 = r12.getString(r4)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            com.xyshj.database.setting.UserSetting r5 = new com.xyshj.database.setting.UserSetting     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            r5.<init>(r1, r2, r3, r4)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L66
            r0 = r5
            goto L52
        L50:
            r1 = move-exception
            goto L5f
        L52:
            if (r12 == 0) goto L65
        L54:
            r12.close()
            goto L65
        L58:
            r12 = move-exception
            r10 = r0
            r0 = r12
            r12 = r10
            goto L67
        L5d:
            r1 = move-exception
            r12 = r0
        L5f:
            r1.printStackTrace()     // Catch: java.lang.Throwable -> L66
            if (r12 == 0) goto L65
            goto L54
        L65:
            return r0
        L66:
            r0 = move-exception
        L67:
            if (r12 == 0) goto L6c
            r12.close()
        L6c:
            goto L6e
        L6d:
            throw r0
        L6e:
            goto L6d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.database.setting.UserSettingDao.getUserSettingFormKey(java.lang.String):com.xyshj.database.setting.UserSetting");
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x004c, code lost:
    
        if (r1 != null) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x004e, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0068, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0065, code lost:
    
        if (r1 == null) goto L58;
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.List<com.xyshj.database.setting.UserSetting> select() {
        /*
            r11 = this;
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r11.mDatabase     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5e
            java.lang.String r2 = "t_setting"
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5e
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L69
            r2.<init>()     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L69
        L15:
            boolean r0 = r1.moveToNext()     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            if (r0 == 0) goto L4c
            java.lang.String r0 = "eid"
            int r0 = r1.getColumnIndex(r0)     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            int r0 = r1.getInt(r0)     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            java.lang.String r3 = "parentId"
            int r3 = r1.getColumnIndex(r3)     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            int r3 = r1.getInt(r3)     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            java.lang.String r4 = "key"
            int r4 = r1.getColumnIndex(r4)     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            java.lang.String r4 = r1.getString(r4)     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            java.lang.String r5 = "value"
            int r5 = r1.getColumnIndex(r5)     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            java.lang.String r5 = r1.getString(r5)     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            com.xyshj.database.setting.UserSetting r6 = new com.xyshj.database.setting.UserSetting     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            r6.<init>(r0, r3, r4, r5)     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            r2.add(r6)     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L69
            goto L15
        L4c:
            if (r1 == 0) goto L68
        L4e:
            r1.close()
            goto L68
        L52:
            r0 = move-exception
            goto L62
        L54:
            r2 = move-exception
            r10 = r2
            r2 = r0
            r0 = r10
            goto L62
        L59:
            r1 = move-exception
            r10 = r1
            r1 = r0
            r0 = r10
            goto L6a
        L5e:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L62:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L69
            if (r1 == 0) goto L68
            goto L4e
        L68:
            return r2
        L69:
            r0 = move-exception
        L6a:
            if (r1 == 0) goto L6f
            r1.close()
        L6f:
            goto L71
        L70:
            throw r0
        L71:
            goto L70
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.database.setting.UserSettingDao.select():java.util.List");
    }

    public void close() {
        SQLiteDatabase sQLiteDatabase = this.mDatabase;
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
            this.mDatabase = null;
        }
        MySQLiteOpenHelper mySQLiteOpenHelper = this.mOpenHelper;
        if (mySQLiteOpenHelper != null) {
            mySQLiteOpenHelper.close();
            this.mOpenHelper = null;
        }
    }

    /* loaded from: classes2.dex */
    public static class sEnable {
        public static final String NAME = "t_setting_enabled";

        /* loaded from: classes2.dex */
        public static class Column {
            public static final String KEY = "key";
            public static final String VALUE = "value";
        }

        public static void createTable(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("create table if not exists t_setting_enabled(key text,value  text)");
        }
    }

    public long insertEnabled(SettingsEnabled settingsEnabled) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("key", settingsEnabled.getKey());
        contentValues.put("value", settingsEnabled.getValue());
        return isHaveSettingEnabled(settingsEnabled) ? this.mDatabase.update(sEnable.NAME, contentValues, "key = ? ", new String[]{settingsEnabled.getKey()}) : this.mDatabase.insert(sEnable.NAME, null, contentValues);
    }

    public int removeEnabled(String str) {
        return this.mDatabase.delete(sEnable.NAME, "key = ? ", new String[]{str});
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0028, code lost:
    
        if (r1 != null) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0036, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0033, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0031, code lost:
    
        if (r1 == null) goto L43;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isHaveSettingEnabled(com.xyshj.database.setting.SettingsEnabled r13) {
        /*
            r12 = this;
            r0 = 0
            r1 = 0
            android.database.sqlite.SQLiteDatabase r2 = r12.mDatabase     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            java.lang.String r3 = "t_setting_enabled"
            r4 = 0
            java.lang.String r5 = "key = ? "
            r11 = 1
            java.lang.String[] r6 = new java.lang.String[r11]     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            java.lang.String r13 = r13.getKey()     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            r6[r0] = r13     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            android.database.Cursor r1 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            if (r1 == 0) goto L28
            int r13 = r1.getCount()     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            if (r13 <= 0) goto L28
            if (r1 == 0) goto L27
            r1.close()
        L27:
            return r11
        L28:
            if (r1 == 0) goto L36
            goto L33
        L2b:
            r13 = move-exception
            goto L37
        L2d:
            r13 = move-exception
            r13.printStackTrace()     // Catch: java.lang.Throwable -> L2b
            if (r1 == 0) goto L36
        L33:
            r1.close()
        L36:
            return r0
        L37:
            if (r1 == 0) goto L3c
            r1.close()
        L3c:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.database.setting.UserSettingDao.isHaveSettingEnabled(com.xyshj.database.setting.SettingsEnabled):boolean");
    }

    public int updateEnabled(SettingsEnabled settingsEnabled) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("key", settingsEnabled.getKey());
        contentValues.put("value", settingsEnabled.getValue());
        return this.mDatabase.update(sEnable.NAME, contentValues, "key = ? ", new String[]{settingsEnabled.getKey()});
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x004e, code lost:
    
        if (r12 == null) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x003e, code lost:
    
        if (r12 != null) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0040, code lost:
    
        r12.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0051, code lost:
    
        return r0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0055  */
    /* JADX WARN: Type inference failed for: r12v2 */
    /* JADX WARN: Type inference failed for: r12v4, types: [android.database.Cursor] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.xyshj.database.setting.SettingsEnabled getSettingEnabledFormKey(java.lang.String r12) {
        /*
            r11 = this;
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r11.mDatabase     // Catch: java.lang.Throwable -> L44 java.lang.Exception -> L49
            java.lang.String r2 = "t_setting_enabled"
            r3 = 0
            java.lang.String r4 = "key = ? "
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch: java.lang.Throwable -> L44 java.lang.Exception -> L49
            r6 = 0
            r5[r6] = r12     // Catch: java.lang.Throwable -> L44 java.lang.Exception -> L49
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            android.database.Cursor r12 = r1.query(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L44 java.lang.Exception -> L49
            if (r12 == 0) goto L3e
            int r1 = r12.getCount()     // Catch: java.lang.Exception -> L3c java.lang.Throwable -> L52
            if (r1 <= 0) goto L3e
            r12.moveToFirst()     // Catch: java.lang.Exception -> L3c java.lang.Throwable -> L52
            java.lang.String r1 = "key"
            int r1 = r12.getColumnIndex(r1)     // Catch: java.lang.Exception -> L3c java.lang.Throwable -> L52
            java.lang.String r1 = r12.getString(r1)     // Catch: java.lang.Exception -> L3c java.lang.Throwable -> L52
            java.lang.String r2 = "value"
            int r2 = r12.getColumnIndex(r2)     // Catch: java.lang.Exception -> L3c java.lang.Throwable -> L52
            java.lang.String r2 = r12.getString(r2)     // Catch: java.lang.Exception -> L3c java.lang.Throwable -> L52
            com.xyshj.database.setting.SettingsEnabled r3 = new com.xyshj.database.setting.SettingsEnabled     // Catch: java.lang.Exception -> L3c java.lang.Throwable -> L52
            r3.<init>(r1, r2)     // Catch: java.lang.Exception -> L3c java.lang.Throwable -> L52
            r0 = r3
            goto L3e
        L3c:
            r1 = move-exception
            goto L4b
        L3e:
            if (r12 == 0) goto L51
        L40:
            r12.close()
            goto L51
        L44:
            r12 = move-exception
            r10 = r0
            r0 = r12
            r12 = r10
            goto L53
        L49:
            r1 = move-exception
            r12 = r0
        L4b:
            r1.printStackTrace()     // Catch: java.lang.Throwable -> L52
            if (r12 == 0) goto L51
            goto L40
        L51:
            return r0
        L52:
            r0 = move-exception
        L53:
            if (r12 == 0) goto L58
            r12.close()
        L58:
            goto L5a
        L59:
            throw r0
        L5a:
            goto L59
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.database.setting.UserSettingDao.getSettingEnabledFormKey(java.lang.String):com.xyshj.database.setting.SettingsEnabled");
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0038, code lost:
    
        if (r1 != null) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x003a, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0054, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0051, code lost:
    
        if (r1 == null) goto L59;
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0058  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.List<com.xyshj.database.setting.SettingsEnabled> selectEnabled() {
        /*
            r11 = this;
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r11.mDatabase     // Catch: java.lang.Throwable -> L45 java.lang.Exception -> L4a
            java.lang.String r2 = "t_setting_enabled"
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L45 java.lang.Exception -> L4a
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch: java.lang.Exception -> L40 java.lang.Throwable -> L55
            r2.<init>()     // Catch: java.lang.Exception -> L40 java.lang.Throwable -> L55
        L15:
            boolean r0 = r1.moveToNext()     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L55
            if (r0 == 0) goto L38
            java.lang.String r0 = "key"
            int r0 = r1.getColumnIndex(r0)     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L55
            java.lang.String r0 = r1.getString(r0)     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L55
            java.lang.String r3 = "value"
            int r3 = r1.getColumnIndex(r3)     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L55
            java.lang.String r3 = r1.getString(r3)     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L55
            com.xyshj.database.setting.SettingsEnabled r4 = new com.xyshj.database.setting.SettingsEnabled     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L55
            r4.<init>(r0, r3)     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L55
            r2.add(r4)     // Catch: java.lang.Exception -> L3e java.lang.Throwable -> L55
            goto L15
        L38:
            if (r1 == 0) goto L54
        L3a:
            r1.close()
            goto L54
        L3e:
            r0 = move-exception
            goto L4e
        L40:
            r2 = move-exception
            r10 = r2
            r2 = r0
            r0 = r10
            goto L4e
        L45:
            r1 = move-exception
            r10 = r1
            r1 = r0
            r0 = r10
            goto L56
        L4a:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L4e:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L55
            if (r1 == 0) goto L54
            goto L3a
        L54:
            return r2
        L55:
            r0 = move-exception
        L56:
            if (r1 == 0) goto L5b
            r1.close()
        L5b:
            goto L5d
        L5c:
            throw r0
        L5d:
            goto L5c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.database.setting.UserSettingDao.selectEnabled():java.util.List");
    }
}
