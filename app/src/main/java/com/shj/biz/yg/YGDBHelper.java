package com.shj.biz.yg;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.oysb.utils.Loger;
import com.oysb.utils.io.file.SDFileUtils;
import com.tencent.wxpayface.WxfacePayCommonCode;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes2.dex */
public class YGDBHelper extends SQLiteOpenHelper {
    public static final String COLUM_ADD_TIME = "addtime";
    public static final String COLUM_GH = "gh";
    public static final String COLUM_ID = "_id";
    public static final String COLUM_ORDERID = "orderid";
    public static final String COLUM_PICKCODE = "pickcode";
    public static final String COLUM_PICK_TIME = "picktime";
    public static final String COLUM_PICK_USERNAME = "pickusername";
    public static final String COLUM_SALT = "salt";
    public static final String COLUM_SFZ = "sfz";
    public static final String COLUM_USERNAME = "username";
    private static final int DATABASE_VERSION = 1;
    private static final long DAY_MILLS = 86400000;
    public static final String TABLE_NAME_ORDERS = "request";
    private static String DATABASE_NAME = SDFileUtils.SDCardRoot + "xyshj/ygv1.db";
    static SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public static void setDatabaseFile(String str) {
        DATABASE_NAME = str;
    }

    public YGDBHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        createDB(sQLiteDatabase);
    }

    private synchronized void createDB(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS request(_id INTEGER PRIMARY KEY AUTOINCREMENT,orderid VARCHAR(100),username VARCHAR(32),sfz VARCHAR(32),pickcode VARCHAR(32),gh VARCHAR(100),addtime VARCHAR(32),picktime VARCHAR(32),pickusername VARCHAR(32))");
        } catch (Exception e) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0024, code lost:
    
        if (r0.isOpen() != false) goto L41;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void clearDB() {
        /*
            r3 = this;
            monitor-enter(r3)
            android.database.sqlite.SQLiteDatabase r0 = r3.getWritableDatabase()     // Catch: java.lang.Throwable -> L35
            java.lang.String r1 = "DELETE FROM request"
            r0.execSQL(r1)     // Catch: java.lang.Throwable -> L16 java.lang.Exception -> L18
            if (r0 == 0) goto L27
            boolean r1 = r0.isOpen()     // Catch: java.lang.Throwable -> L35
            if (r1 == 0) goto L27
        L12:
            r0.close()     // Catch: java.lang.Throwable -> L35
            goto L27
        L16:
            r1 = move-exception
            goto L29
        L18:
            r1 = move-exception
            java.lang.String r2 = "ERROR"
            com.oysb.utils.Loger.writeException(r2, r1)     // Catch: java.lang.Throwable -> L16
            if (r0 == 0) goto L27
            boolean r1 = r0.isOpen()     // Catch: java.lang.Throwable -> L35
            if (r1 == 0) goto L27
            goto L12
        L27:
            monitor-exit(r3)
            return
        L29:
            if (r0 == 0) goto L34
            boolean r2 = r0.isOpen()     // Catch: java.lang.Throwable -> L35
            if (r2 == 0) goto L34
            r0.close()     // Catch: java.lang.Throwable -> L35
        L34:
            throw r1     // Catch: java.lang.Throwable -> L35
        L35:
            r0 = move-exception
            monitor-exit(r3)
            goto L39
        L38:
            throw r0
        L39:
            goto L38
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.yg.YGDBHelper.clearDB():void");
    }

    public synchronized void closeDB() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase != null && writableDatabase.isOpen()) {
            writableDatabase.close();
        }
    }

    public void addOrder(String str, String str2, String str3, String str4, String str5) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            if (writableDatabase == null || !writableDatabase.isOpen()) {
                return;
            }
            writableDatabase.close();
            return;
        }
        try {
            try {
                new Date();
                writableDatabase.execSQL("INSERT INTO request VALUES (?,?,?,?,?,?,?,?,?)", new Object[]{null, str, str3, str4, str2, str5, "" + f.format(new Date()), null, null});
                Loger.writeLog("YG_ORDER", "order added");
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
            } catch (Exception e) {
                Loger.writeException("YG_ORDER", e);
                e.printStackTrace();
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
            }
            writableDatabase.close();
        } catch (Throwable th) {
            if (writableDatabase != null && writableDatabase.isOpen()) {
                writableDatabase.close();
            }
            throw th;
        }
    }

    public void updateOrderByPickCode(String str, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUM_PICKCODE, str2);
                contentValues.put(COLUM_PICK_TIME, f.format(new Date()));
                writableDatabase.update(TABLE_NAME_ORDERS, contentValues, "orderid = ?", new String[]{"" + str});
                Loger.writeLog("YG_ORDER", "order updated ");
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
            }
            writableDatabase.close();
        } catch (Throwable th) {
            if (writableDatabase != null && writableDatabase.isOpen()) {
                writableDatabase.close();
            }
            throw th;
        }
    }

    public void updateOrderByPickUserV1(String str, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUM_PICK_USERNAME, str2);
                contentValues.put(COLUM_PICK_TIME, f.format(new Date()));
                writableDatabase.update(TABLE_NAME_ORDERS, contentValues, "orderid = ?", new String[]{"" + str});
                Loger.writeLog("YG_ORDER", "order updated ");
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
            }
            writableDatabase.close();
        } catch (Throwable th) {
            if (writableDatabase != null && writableDatabase.isOpen()) {
                writableDatabase.close();
            }
            throw th;
        }
    }

    public void updateOrderByPickUserV2(String str, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUM_PICK_USERNAME, str2);
                contentValues.put(COLUM_PICK_TIME, f.format(new Date()));
                writableDatabase.update(TABLE_NAME_ORDERS, contentValues, "pickcode = ?", new String[]{"" + str});
                Loger.writeLog("YG_ORDER", "order updated ");
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
            }
            writableDatabase.close();
        } catch (Throwable th) {
            if (writableDatabase != null && writableDatabase.isOpen()) {
                writableDatabase.close();
            }
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x004a, code lost:
    
        if (r0.isOpen() != false) goto L42;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void deleteOrdersOutDate(int r9) {
        /*
            r8 = this;
            monitor-enter(r8)
            android.database.sqlite.SQLiteDatabase r0 = r8.getWritableDatabase()     // Catch: java.lang.Throwable -> L5b
            java.util.Date r1 = new java.util.Date     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            r1.<init>()     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            java.util.Date r2 = new java.util.Date     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            r2.<init>()     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            long r2 = r2.getTime()     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            long r4 = (long) r9     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            r6 = 86400000(0x5265c00, double:4.2687272E-316)
            long r4 = r4 * r6
            long r2 = r2 - r4
            r1.setTime(r2)     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            java.lang.String r9 = "request"
            java.lang.String r2 = "addtime < ?"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            r4 = 0
            java.text.SimpleDateFormat r5 = com.shj.biz.yg.YGDBHelper.f     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            java.lang.String r1 = r5.format(r1)     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            r3[r4] = r1     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            r0.delete(r9, r2, r3)     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            if (r0 == 0) goto L4d
            boolean r9 = r0.isOpen()     // Catch: java.lang.Throwable -> L5b
            if (r9 == 0) goto L4d
        L38:
            r0.close()     // Catch: java.lang.Throwable -> L5b
            goto L4d
        L3c:
            r9 = move-exception
            goto L4f
        L3e:
            r9 = move-exception
            java.lang.String r1 = "ERROR"
            com.oysb.utils.Loger.writeException(r1, r9)     // Catch: java.lang.Throwable -> L3c
            if (r0 == 0) goto L4d
            boolean r9 = r0.isOpen()     // Catch: java.lang.Throwable -> L5b
            if (r9 == 0) goto L4d
            goto L38
        L4d:
            monitor-exit(r8)
            return
        L4f:
            if (r0 == 0) goto L5a
            boolean r1 = r0.isOpen()     // Catch: java.lang.Throwable -> L5b
            if (r1 == 0) goto L5a
            r0.close()     // Catch: java.lang.Throwable -> L5b
        L5a:
            throw r9     // Catch: java.lang.Throwable -> L5b
        L5b:
            r9 = move-exception
            monitor-exit(r8)
            goto L5f
        L5e:
            throw r9
        L5f:
            goto L5e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.yg.YGDBHelper.deleteOrdersOutDate(int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x00c2 A[Catch: all -> 0x00d1, TRY_ENTER, TryCatch #0 {, blocks: (B:3:0x0001, B:35:0x00ae, B:37:0x00b3, B:39:0x00b9, B:19:0x008a, B:21:0x008f, B:23:0x0095, B:50:0x00c2, B:52:0x00c7, B:54:0x00cd, B:55:0x00d0), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized java.util.HashMap<java.lang.String, java.lang.String> getOrderInfo(java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 214
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.biz.yg.YGDBHelper.getOrderInfo(java.lang.String):java.util.HashMap");
    }
}
