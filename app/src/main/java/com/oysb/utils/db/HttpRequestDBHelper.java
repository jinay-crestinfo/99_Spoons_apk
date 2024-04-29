package com.oysb.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import com.iflytek.cloud.SpeechConstant;
import com.oysb.utils.Loger;
import com.oysb.utils.http.RequestItem;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.biz.yg.YGDBHelper;
//import com.tencent.wxpayface.WxfacePayCommonCode;
import java.text.SimpleDateFormat;
import java.util.Date;
//import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes2.dex */
public class HttpRequestDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final long DAY_MILLS = 86400000;
    public final String COLUM_ADD_TIME;
    public final String COLUM_COUNT;
    public final String COLUM_FINISH_TIME;
    public final String COLUM_ID;
    public final String COLUM_KEY;
    public final String COLUM_MOTHED;
//    public final String COLUM_PARAMS;
    public final String COLUM_SIZE;
    public final String COLUM_STATE;
    public final String COLUM_SUCCESS_STR;
//    public final String COLUM_URL;
    public final String TABLE_NAME_REQUEST;
    public final String TABLE_NAME_STATIC;
    private static String DATABASE_NAME = SDFileUtils.SDCardRoot + "xyshj/httpRequestV5.db";
    static SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public void updateRequestStaticInfo(String str, int i, int i2) {
    }

    public static void setDatabaseFile(String str) {
        DATABASE_NAME = str;
    }

    public HttpRequestDBHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        this.TABLE_NAME_REQUEST = YGDBHelper.TABLE_NAME_ORDERS;
        this.COLUM_ID = "_id";
        this.COLUM_KEY = "rquest";
//        this.COLUM_PARAMS = SpeechConstant.PARAMS;
//        this.COLUM_URL = IjkMediaPlayer.OnNativeInvokeListener.ARG_URL;
        this.COLUM_MOTHED = "mothed";
        this.COLUM_STATE = "state";
        this.COLUM_ADD_TIME = "addtime";
        this.COLUM_FINISH_TIME = "finishtime";
        this.COLUM_SUCCESS_STR = "successstr";
        this.TABLE_NAME_STATIC = "request_static";
        this.COLUM_COUNT = "request_count";
        this.COLUM_SIZE = "request_size";
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        createDB(sQLiteDatabase);
    }

    private synchronized void createDB(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS request_static(_id INTEGER PRIMARY KEY AUTOINCREMENT,url VARCHAR(512),request_count INTEGER,request_size INTEGER)");
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR", e);
        }
        try {
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS request(_id INTEGER PRIMARY KEY AUTOINCREMENT,rquest VARCHAR(128),params VARCHAR(1024),url VARCHAR(512),mothed VARCHAR(16),state INTEGER,addtime VARCHAR(32),finishtime VARCHAR(32),successstr VARCHAR(32))");
        } catch (Exception e2) {
            Loger.safe_inner_exception_catch(e2);
            Loger.writeException("WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR", e2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x002a, code lost:
    
        if (r2.isOpen() != false) goto L42;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void upgrade(int r2, int r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            android.database.sqlite.SQLiteDatabase r2 = r1.getWritableDatabase()     // Catch: java.lang.Throwable -> L3b
            java.lang.String r3 = "DROP TABLE IF EXISTS request"
            r2.execSQL(r3)     // Catch: java.lang.Throwable -> L19 java.lang.Exception -> L1b
            r1.createDB(r2)     // Catch: java.lang.Throwable -> L19 java.lang.Exception -> L1b
            if (r2 == 0) goto L2d
            boolean r3 = r2.isOpen()     // Catch: java.lang.Throwable -> L3b
            if (r3 == 0) goto L2d
        L15:
            r2.close()     // Catch: java.lang.Throwable -> L3b
            goto L2d
        L19:
            r3 = move-exception
            goto L2f
        L1b:
            r3 = move-exception
            com.oysb.utils.Loger.safe_inner_exception_catch(r3)     // Catch: java.lang.Throwable -> L19
            java.lang.String r0 = "ERROR"
            com.oysb.utils.Loger.writeException(r0, r3)     // Catch: java.lang.Throwable -> L19
            if (r2 == 0) goto L2d
            boolean r3 = r2.isOpen()     // Catch: java.lang.Throwable -> L3b
            if (r3 == 0) goto L2d
            goto L15
        L2d:
            monitor-exit(r1)
            return
        L2f:
            if (r2 == 0) goto L3a
            boolean r0 = r2.isOpen()     // Catch: java.lang.Throwable -> L3b
            if (r0 == 0) goto L3a
            r2.close()     // Catch: java.lang.Throwable -> L3b
        L3a:
            throw r3     // Catch: java.lang.Throwable -> L3b
        L3b:
            r2 = move-exception
            monitor-exit(r1)
            goto L3f
        L3e:
            throw r2
        L3f:
            goto L3e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.db.HttpRequestDBHelper.upgrade(int, int):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0027, code lost:
    
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
            android.database.sqlite.SQLiteDatabase r0 = r3.getWritableDatabase()     // Catch: java.lang.Throwable -> L38
            java.lang.String r1 = "DELETE FROM request"
            r0.execSQL(r1)     // Catch: java.lang.Throwable -> L16 java.lang.Exception -> L18
            if (r0 == 0) goto L2a
            boolean r1 = r0.isOpen()     // Catch: java.lang.Throwable -> L38
            if (r1 == 0) goto L2a
        L12:
            r0.close()     // Catch: java.lang.Throwable -> L38
            goto L2a
        L16:
            r1 = move-exception
            goto L2c
        L18:
            r1 = move-exception
            com.oysb.utils.Loger.safe_inner_exception_catch(r1)     // Catch: java.lang.Throwable -> L16
            java.lang.String r2 = "ERROR"
            com.oysb.utils.Loger.writeException(r2, r1)     // Catch: java.lang.Throwable -> L16
            if (r0 == 0) goto L2a
            boolean r1 = r0.isOpen()     // Catch: java.lang.Throwable -> L38
            if (r1 == 0) goto L2a
            goto L12
        L2a:
            monitor-exit(r3)
            return
        L2c:
            if (r0 == 0) goto L37
            boolean r2 = r0.isOpen()     // Catch: java.lang.Throwable -> L38
            if (r2 == 0) goto L37
            r0.close()     // Catch: java.lang.Throwable -> L38
        L37:
            throw r1     // Catch: java.lang.Throwable -> L38
        L38:
            r0 = move-exception
            monitor-exit(r3)
            goto L3c
        L3b:
            throw r0
        L3c:
            goto L3b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.db.HttpRequestDBHelper.clearDB():void");
    }

    public synchronized void closeDB() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase != null && writableDatabase.isOpen()) {
            writableDatabase.close();
        }
    }

    public String getHttpNetCostInfo() {
        String str = "noInfo";
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            Cursor rawQuery = writableDatabase.rawQuery("select sum(request_count),sum(request_size) from request_static", null);
            while (rawQuery.moveToNext()) {
                try {
                    str = "count:" + rawQuery.getInt(0) + " size:" + rawQuery.getLong(1);
                } catch (Exception unused) {
                }
            }
            if (writableDatabase != null && writableDatabase.isOpen()) {
                writableDatabase.close();
            }
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
        return str;
    }

    public void addRequest(RequestItem requestItem) {
        String str;
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            try {
                Date date = new Date();
                if (requestItem.getAppendTime() == null) {
                    requestItem.setAppendTime(date);
                }
                requestItem.setUpdateTime(date);
            } catch (Exception e) {
                Loger.writeException("REQUEST", e);
                e.printStackTrace();
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
            }
            if (writableDatabase == null) {
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
                writableDatabase.close();
                return;
            }
            if (requestItem.isJSONParams()) {
                str = "JSON:" + requestItem.getJSONParams().toString();
            } else {
                str = "" + requestItem.getParams().toString();
            }
            writableDatabase.execSQL("INSERT INTO request VALUES (?,?,?,?,?,?,?,?,?)", new Object[]{null, requestItem.getKey(), str, requestItem.getUrl(), requestItem.getMothed(), Integer.valueOf(requestItem.getState()), "" + requestItem.getAppendTime().getTime(), null, requestItem.getSuccessStr()});
            Loger.writeLog("REQUEST", "report added");
            if (writableDatabase == null || !writableDatabase.isOpen()) {
                return;
            }
            writableDatabase.close();
        } catch (Throwable th) {
            if (writableDatabase != null && writableDatabase.isOpen()) {
                writableDatabase.close();
            }
            throw th;
        }
    }

    public void updateRequestState(RequestItem requestItem) {
        requestItem.setUpdateTime(new Date());
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("state", Integer.valueOf(requestItem.getState()));
                contentValues.put("finishtime", f.format(requestItem.getFinishTime()));
                writableDatabase.update(YGDBHelper.TABLE_NAME_ORDERS, contentValues, "addtime = ? and url = ?", new String[]{"" + requestItem.getAppendTime().getTime(), requestItem.getUrl()});
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
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

    public void deleteRequest(RequestItem requestItem) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            try {
                writableDatabase.delete(YGDBHelper.TABLE_NAME_ORDERS, "rquest = ?", new String[]{requestItem.getKey()});
                Loger.writeLog("REQUEST", "report deleted");
                if (writableDatabase == null || !writableDatabase.isOpen()) {
                    return;
                }
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
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

    /* JADX WARN: Code restructure failed: missing block: B:18:0x004d, code lost:
    
        if (r0.isOpen() != false) goto L42;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void deleteReportsOutDate(int r9) {
        /*
            r8 = this;
            monitor-enter(r8)
            android.database.sqlite.SQLiteDatabase r0 = r8.getWritableDatabase()     // Catch: java.lang.Throwable -> L5e
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
            java.lang.String r2 = "finishtime < ?"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            r4 = 0
            java.text.SimpleDateFormat r5 = com.oysb.utils.db.HttpRequestDBHelper.f     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            java.lang.String r1 = r5.format(r1)     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            r3[r4] = r1     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            r0.delete(r9, r2, r3)     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L3e
            if (r0 == 0) goto L50
            boolean r9 = r0.isOpen()     // Catch: java.lang.Throwable -> L5e
            if (r9 == 0) goto L50
        L38:
            r0.close()     // Catch: java.lang.Throwable -> L5e
            goto L50
        L3c:
            r9 = move-exception
            goto L52
        L3e:
            r9 = move-exception
            com.oysb.utils.Loger.safe_inner_exception_catch(r9)     // Catch: java.lang.Throwable -> L3c
            java.lang.String r1 = "ERROR"
            com.oysb.utils.Loger.writeException(r1, r9)     // Catch: java.lang.Throwable -> L3c
            if (r0 == 0) goto L50
            boolean r9 = r0.isOpen()     // Catch: java.lang.Throwable -> L5e
            if (r9 == 0) goto L50
            goto L38
        L50:
            monitor-exit(r8)
            return
        L52:
            if (r0 == 0) goto L5d
            boolean r1 = r0.isOpen()     // Catch: java.lang.Throwable -> L5e
            if (r1 == 0) goto L5d
            r0.close()     // Catch: java.lang.Throwable -> L5e
        L5d:
            throw r9     // Catch: java.lang.Throwable -> L5e
        L5e:
            r9 = move-exception
            monitor-exit(r8)
            goto L62
        L61:
            throw r9
        L62:
            goto L61
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.db.HttpRequestDBHelper.deleteReportsOutDate(int):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x0107, code lost:
    
        if (r1.isOpen() != false) goto L111;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized java.util.List<com.oysb.utils.http.RequestItem> getUnSendReports(java.lang.String r14, java.util.Date r15) {
        /*
            Method dump skipped, instructions count: 290
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.db.HttpRequestDBHelper.getUnSendReports(java.lang.String, java.util.Date):java.util.List");
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x00f9, code lost:
    
        if (r1.isOpen() != false) goto L111;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized java.util.List<com.oysb.utils.http.RequestItem> getUnSendReports(java.util.Date r14) {
        /*
            Method dump skipped, instructions count: 276
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.db.HttpRequestDBHelper.getUnSendReports(java.util.Date):java.util.List");
    }
}
