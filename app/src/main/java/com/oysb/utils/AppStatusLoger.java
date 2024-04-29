package com.oysb.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.oysb.utils.date.DateUtil;
import com.oysb.utils.io.file.SDFileUtils;
import com.tencent.wxpayface.WxfacePayCommonCode;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;

/* loaded from: classes2.dex */
public class AppStatusLoger extends SQLiteOpenHelper {
    public static final String COLUM_Code = "state_code";
    public static final String COLUM_Count = "state_count";
    public static final String COLUM_ID = "_id";
    public static final String COLUM_Info = "state_info";
    public static final String COLUM_Module = "state_module";
    public static final String COLUM_SentState = "reportstatus";
    public static final String COLUM_Sn = "addtime";
    public static final String COLUM_Type = "state_type";
    private static final String DATABASE_NAME = "xyshj/appstatus.db";
    private static final int DATABASE_VERSION = 1;
    private static final long DAY_MILLS = 86400000;
    public static final String TABLE_NAME = "appstatus";
    private static final String TEMP_STORE_FILE_NAME = "appstatus.tmp";
    private static final String TEMP_STORE_NOREPEAT_FILE_NAME = "appstatus_norepeat.tmp";
    public static final String Type_AppCrash = "AppCrash";
    public static final String Type_AppSetting = "AppSetting";
    public static final String Type_AppStartUp = "AppStartUp";
    public static final String Type_AppStatus = "AppStatus";
    public static final String Type_BatchOfferGoods = "BatchOfferGoods";
    public static final String Type_Command = "Command";
    public static final String Type_ConnectAuthority = "ConnectAuthority";
    public static final String Type_DeviceError = "DeviceError";
    public static final String Type_ExitApp = "ExitApp";
    public static final String Type_HttpRequest = "HttpRequest";
    public static final String Type_Network = "Network";
    public static final String Type_OfferGoods = "OfferGoods";
    public static final String Type_Order = "Order";
    public static final String Type_QueryQrCode = "QueryQrCode";
    public static final String Type_RebootSystem = "RebootSystem";
    public static final String Type_RestartApp = "RestartApp";
    public static final String Type_Serial = "Serial";
    public static final String Type_SocketDisconnect = "SocketDisconnect";
    public static final String Type_TimerTask = "TimerTask";
    private static AppStatusLoger dbHelper;
    static JSONArray jsonArray;
    private static AppStatusLogerListener listener;
    static HashMap<String, AppStatus> mapStatus_count = new HashMap<>();
    static List<String> listStatus_no_repeat = null;

    /* loaded from: classes2.dex */
    public interface AppStatusLogerListener {
        void onAppStatusItemAdded(AppStatus appStatus);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public static void init(Context context, AppStatusLogerListener appStatusLogerListener) {
        dbHelper = new AppStatusLoger(context);
        listener = appStatusLogerListener;
        if (jsonArray == null) {
            restoreDataFromSdcard();
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                addAppStatus(AppStatus.fromString(jsonArray.get(i).toString()));
            } catch (Exception unused) {
            }
        }
        jsonArray = new JSONArray();
        saveDate2Sdcard();
    }

    private AppStatusLoger(Context context) {
        super(context, SDFileUtils.SDCardRoot + DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        createDB(sQLiteDatabase);
    }

    private synchronized void createDB(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS appstatus(_id INTEGER PRIMARY KEY AUTOINCREMENT,addtime VARCHAR(32),state_module VARCHAR(64),state_type VARCHAR(64),state_code VARCHAR(32),state_info VARCHAR(32),state_count INTEGER,reportstatus INTEGER)");
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("WxFacePayError", e);
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
            java.lang.String r3 = "DROP TABLE IF EXISTS appstatus"
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
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.AppStatusLoger.upgrade(int, int):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x002f, code lost:
    
        if (r1.isOpen() != false) goto L52;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static synchronized void clear() {
        /*
            java.lang.Class<com.oysb.utils.AppStatusLoger> r0 = com.oysb.utils.AppStatusLoger.class
            monitor-enter(r0)
            com.oysb.utils.AppStatusLoger r1 = com.oysb.utils.AppStatusLoger.dbHelper     // Catch: java.lang.Throwable -> L40
            if (r1 != 0) goto L9
            monitor-exit(r0)
            return
        L9:
            android.database.sqlite.SQLiteDatabase r1 = r1.getWritableDatabase()     // Catch: java.lang.Throwable -> L40
            java.lang.String r2 = "DELETE FROM appstatus"
            r1.execSQL(r2)     // Catch: java.lang.Throwable -> L1e java.lang.Exception -> L20
            if (r1 == 0) goto L32
            boolean r2 = r1.isOpen()     // Catch: java.lang.Throwable -> L40
            if (r2 == 0) goto L32
        L1a:
            r1.close()     // Catch: java.lang.Throwable -> L40
            goto L32
        L1e:
            r2 = move-exception
            goto L34
        L20:
            r2 = move-exception
            com.oysb.utils.Loger.safe_inner_exception_catch(r2)     // Catch: java.lang.Throwable -> L1e
            java.lang.String r3 = "ERROR"
            com.oysb.utils.Loger.writeException(r3, r2)     // Catch: java.lang.Throwable -> L1e
            if (r1 == 0) goto L32
            boolean r2 = r1.isOpen()     // Catch: java.lang.Throwable -> L40
            if (r2 == 0) goto L32
            goto L1a
        L32:
            monitor-exit(r0)
            return
        L34:
            if (r1 == 0) goto L3f
            boolean r3 = r1.isOpen()     // Catch: java.lang.Throwable -> L40
            if (r3 == 0) goto L3f
            r1.close()     // Catch: java.lang.Throwable -> L40
        L3f:
            throw r2     // Catch: java.lang.Throwable -> L40
        L40:
            r1 = move-exception
            monitor-exit(r0)
            goto L44
        L43:
            throw r1
        L44:
            goto L43
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.AppStatusLoger.clear():void");
    }

    private static void restoreDataFromSdcard() {
        try {
            String readFile = SDFileUtils.readFile(SDFileUtils.SDCardRoot + "xyshj/" + TEMP_STORE_FILE_NAME);
            jsonArray = readFile.length() == 0 ? new JSONArray() : new JSONArray(readFile);
        } catch (Exception unused) {
            jsonArray = new JSONArray();
        }
    }

    private static void saveDate2Sdcard() {
        try {
            SDFileUtils.writeToSDFromInput(SDFileUtils.SDCardRoot + "xyshj", TEMP_STORE_FILE_NAME, jsonArray.toString(), false);
        } catch (Exception unused) {
        }
    }

    public static AppStatus addAppStatus(String str, String str2, String str3, String str4, String str5) {
        return addAppStatus(new AppStatus(str, str2, str3, str4, str5));
    }

    public static AppStatus addAppStatus_Count(String str, String str2, String str3, String str4, String str5) {
        if (mapStatus_count == null) {
            mapStatus_count = new HashMap<>();
        }
        String str6 = str3 + str4 + str5;
        if (mapStatus_count.containsKey(str6)) {
            AppStatus appStatus = mapStatus_count.get(str6);
            appStatus.addCount();
            return appStatus;
        }
        AppStatus appStatus2 = new AppStatus(str, str2, str3, str4, str5);
        mapStatus_count.put(str6, appStatus2);
        return appStatus2;
    }

    public static AppStatus addAppStatus_no_repeat(String str, String str2, String str3, String str4, String str5) {
        String format = DateUtil.format(new Date(), DateUtil.YMD);
        if (listStatus_no_repeat == null) {
            try {
                listStatus_no_repeat = (ArrayList) SDFileUtils.readObject(new File(SDFileUtils.SDCardRoot + "xyshj/" + TEMP_STORE_NOREPEAT_FILE_NAME));
            } catch (Exception unused) {
            }
            if (listStatus_no_repeat == null) {
                ArrayList arrayList = new ArrayList();
                listStatus_no_repeat = arrayList;
                arrayList.add(format);
            }
        }
        if (listStatus_no_repeat.size() > 0) {
            if (!listStatus_no_repeat.get(0).equals(format)) {
                listStatus_no_repeat.clear();
                listStatus_no_repeat.add(format);
            }
        } else {
            listStatus_no_repeat.add(format);
        }
        String str6 = str3 + str4 + str5;
        if (listStatus_no_repeat.contains(str6)) {
            return null;
        }
        AppStatus appStatus = new AppStatus(str, str2, str3, str4, str5);
        listStatus_no_repeat.add(str6);
        try {
            SDFileUtils.writeObject(new File(SDFileUtils.SDCardRoot + "xyshj/" + TEMP_STORE_NOREPEAT_FILE_NAME), listStatus_no_repeat);
        } catch (Exception unused2) {
        }
        Loger.writeLog("APPLOG", "add listStatus_no_repeat " + appStatus.toString());
        return addAppStatus(appStatus);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0098, code lost:
    
        if (r0.isOpen() != false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00a9, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00a7, code lost:
    
        if (r0.isOpen() != false) goto L77;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static com.oysb.utils.AppStatusLoger.AppStatus addAppStatus(com.oysb.utils.AppStatusLoger.AppStatus r6) {
        /*
            com.oysb.utils.AppStatusLoger r0 = com.oysb.utils.AppStatusLoger.dbHelper
            if (r0 != 0) goto L19
            org.json.JSONArray r0 = com.oysb.utils.AppStatusLoger.jsonArray
            if (r0 != 0) goto Lb
            restoreDataFromSdcard()
        Lb:
            org.json.JSONArray r0 = com.oysb.utils.AppStatusLoger.jsonArray
            java.lang.String r1 = com.oysb.utils.AppStatusLoger.AppStatus.access$100(r6)
            r0.put(r1)
            saveDate2Sdcard()
            goto Lb9
        L19:
            boolean r0 = r6.shuldAdd()
            if (r0 == 0) goto Lb9
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "add addAppStatus "
            r0.append(r1)
            java.lang.String r1 = r6.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "APPLOG"
            com.oysb.utils.Loger.writeLog(r1, r0)
            long r0 = java.lang.System.currentTimeMillis()
            r6.lastTime = r0
            com.oysb.utils.AppStatusLoger r0 = com.oysb.utils.AppStatusLoger.dbHelper
            android.database.sqlite.SQLiteDatabase r0 = r0.getWritableDatabase()
            if (r0 != 0) goto L53
            if (r0 == 0) goto L52
            boolean r1 = r0.isOpen()
            if (r1 == 0) goto L52
            r0.close()
        L52:
            return r6
        L53:
            java.lang.String r1 = "INSERT INTO appstatus VALUES (?,?,?,?,?,?,?,?)"
            r2 = 8
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r3 = 0
            r4 = 0
            r2[r4] = r3     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r3 = 1
            java.lang.String r5 = r6.sn     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r2[r3] = r5     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r3 = 2
            java.lang.String r5 = r6.module     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r2[r3] = r5     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r3 = 3
            java.lang.String r5 = r6.type     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r2[r3] = r5     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r3 = 4
            java.lang.String r5 = r6.code     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r2[r3] = r5     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r3 = 5
            java.lang.String r5 = r6.info     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r2[r3] = r5     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r3 = 6
            int r5 = r6.count     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r2[r3] = r5     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r3 = 7
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r2[r3] = r4     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            r0.execSQL(r1, r2)     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            com.oysb.utils.AppStatusLoger$AppStatusLogerListener r1 = com.oysb.utils.AppStatusLoger.listener     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> L9d
            if (r1 == 0) goto L92
            r1.onAppStatusItemAdded(r6)     // Catch: java.lang.Exception -> L91 java.lang.Throwable -> L9b
            goto L92
        L91:
        L92:
            if (r0 == 0) goto Lb9
            boolean r1 = r0.isOpen()
            if (r1 == 0) goto Lb9
            goto La9
        L9b:
            r6 = move-exception
            goto Lad
        L9d:
            r1 = move-exception
            r1.printStackTrace()     // Catch: java.lang.Throwable -> L9b
            if (r0 == 0) goto Lb9
            boolean r1 = r0.isOpen()
            if (r1 == 0) goto Lb9
        La9:
            r0.close()
            goto Lb9
        Lad:
            if (r0 == 0) goto Lb8
            boolean r1 = r0.isOpen()
            if (r1 == 0) goto Lb8
            r0.close()
        Lb8:
            throw r6
        Lb9:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.AppStatusLoger.addAppStatus(com.oysb.utils.AppStatusLoger$AppStatus):com.oysb.utils.AppStatusLoger$AppStatus");
    }

    public static void markAppStatusReported(String str) {
        AppStatusLoger appStatusLoger = dbHelper;
        if (appStatusLoger == null) {
            return;
        }
        SQLiteDatabase writableDatabase = appStatusLoger.getWritableDatabase();
        try {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUM_SentState, (Integer) 1);
                writableDatabase.update(TABLE_NAME, contentValues, "addtime = ?", new String[]{"" + str});
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

    public static void clearAppStatuss(int i) {
        AppStatusLoger appStatusLoger = dbHelper;
        if (appStatusLoger == null) {
            return;
        }
        SQLiteDatabase writableDatabase = appStatusLoger.getWritableDatabase();
        try {
            try {
                Date date = new Date();
                date.setTime(new Date().getTime() - (i * 86400000));
                writableDatabase.delete(TABLE_NAME, "addtime < ?", new String[]{DateUtil.format(date, "yyyy/MM/dd HHmmss:SSSS")});
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

    public static List<AppStatus> getAppStatusCountList() {
        ArrayList arrayList = new ArrayList();
        HashMap<String, AppStatus> hashMap = mapStatus_count;
        if (hashMap != null) {
            for (AppStatus appStatus : hashMap.values()) {
                if (appStatus.count > 1) {
                    arrayList.add(appStatus);
                }
            }
        }
        return arrayList;
    }

    public static void clearStatusCountList() {
        mapStatus_count.clear();
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0067, code lost:
    
        if (r2.isOpen() != false) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0082, code lost:
    
        r2.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0080, code lost:
    
        if (r2.isOpen() != false) goto L78;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List<com.oysb.utils.AppStatusLoger.AppStatus> getAppStatus2Report() {
        /*
            com.oysb.utils.AppStatusLoger r0 = com.oysb.utils.AppStatusLoger.dbHelper
            r1 = 0
            if (r0 != 0) goto L6
            return r1
        L6:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            com.oysb.utils.AppStatusLoger r2 = com.oysb.utils.AppStatusLoger.dbHelper
            android.database.sqlite.SQLiteDatabase r2 = r2.getWritableDatabase()
            java.lang.String r3 = "select  _id, addtime, state_module, state_type, state_code, state_info, state_count from appstatus where reportstatus < 1 order by _id"
            android.database.Cursor r1 = r2.rawQuery(r3, r1)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
        L17:
            boolean r3 = r1.moveToNext()     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            if (r3 == 0) goto L5c
            com.oysb.utils.AppStatusLoger$AppStatus r3 = new com.oysb.utils.AppStatusLoger$AppStatus     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            r4 = 1
            java.lang.String r5 = r1.getString(r4)     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            r4 = 2
            java.lang.String r6 = r1.getString(r4)     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            r4 = 3
            java.lang.String r7 = r1.getString(r4)     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            r4 = 4
            java.lang.String r8 = r1.getString(r4)     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            r10 = 5
            java.lang.String r9 = r1.getString(r10)     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            r4 = r3
            r4.<init>(r5, r6, r7, r8, r9)     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            r4 = 6
            int r4 = r1.getInt(r4)     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            r3.count = r4     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            java.lang.String r4 = r3.sn     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            r5 = 0
            java.lang.String r6 = r3.sn     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            int r6 = r6.length()     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            int r6 = r6 - r10
            java.lang.String r4 = r4.substring(r5, r6)     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            r3.createTime = r4     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            r0.add(r3)     // Catch: java.lang.Exception -> L57 java.lang.Throwable -> L6a
            goto L17
        L57:
            r3 = move-exception
            r3.printStackTrace()     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            goto L17
        L5c:
            if (r1 == 0) goto L61
            r1.close()
        L61:
            if (r2 == 0) goto L85
            boolean r1 = r2.isOpen()
            if (r1 == 0) goto L85
            goto L82
        L6a:
            r0 = move-exception
            goto L86
        L6c:
            r3 = move-exception
            com.oysb.utils.Loger.safe_inner_exception_catch(r3)     // Catch: java.lang.Throwable -> L6a
            java.lang.String r4 = "ERROR"
            com.oysb.utils.Loger.writeException(r4, r3)     // Catch: java.lang.Throwable -> L6a
            if (r1 == 0) goto L7a
            r1.close()
        L7a:
            if (r2 == 0) goto L85
            boolean r1 = r2.isOpen()
            if (r1 == 0) goto L85
        L82:
            r2.close()
        L85:
            return r0
        L86:
            if (r1 == 0) goto L8b
            r1.close()
        L8b:
            if (r2 == 0) goto L96
            boolean r1 = r2.isOpen()
            if (r1 == 0) goto L96
            r2.close()
        L96:
            goto L98
        L97:
            throw r0
        L98:
            goto L97
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.AppStatusLoger.getAppStatus2Report():java.util.List");
    }

    /* loaded from: classes2.dex */
    public static class AppStatus {
        String info;
        String module;
        String type;
        String code = "";
        int count = 1;
        long lastTime = 0;
        String createTime = DateUtil.format(new Date(), "yyyy/MM/dd HH:mm:ss");
        String sn = "";

        public String getCreateTime() {
            return this.createTime;
        }

        public boolean shuldAdd() {
            return this.count == 1;
        }

        public AppStatus(String str, String str2, String str3, String str4, String str5) {
            init(str, str2, str3, str4, str5);
        }

        private void init(String str, String str2, String str3, String str4, String str5) {
            update(str, str2, str3, str4, str5);
        }

        public void setType(String str) {
            this.type = str;
        }

        public void setCode(String str) {
            this.code = str;
        }

        public void setInfo(String str) {
            this.info = str;
        }

        public void update(String str, String str2, String str3, String str4, String str5) {
            this.module = str2;
            this.type = str3;
            this.code = str4;
            this.info = str5;
            if (str == null || str.length() == 0) {
                this.sn = DateUtil.format(new Date(), "yyyy/MM/dd HH:mm:ss:SSSS");
            } else {
                this.sn = str;
            }
        }

        public String getModule() {
            return this.module;
        }

        public String getType() {
            return this.type;
        }

        public String getCode() {
            return this.code;
        }

        public String getInfo() {
            return this.info;
        }

        public String getCount() {
            return "" + this.count;
        }

        public String getSn() {
            return this.sn;
        }

        public String toString() {
            if (this.count <= 1) {
                return this.info;
            }
            return "最近发生" + this.count + "次，" + this.info;
        }

        public String toSaveString() {
            return this.sn + "#:#" + this.module + "#:#" + this.type + "#:#" + this.code + "#:#" + this.info + "#:#" + this.count + "#:#" + this.lastTime + "#:#" + this.createTime;
        }

        public static AppStatus fromString(String str) {
            String[] split = str.split("#:#");
            AppStatus appStatus = new AppStatus(split[0], split[1], split[2], split[3], split[4]);
            appStatus.count = Integer.parseInt(split[5]);
            appStatus.lastTime = Long.parseLong(split[6]);
            appStatus.createTime = split[7];
            return appStatus;
        }

        public void addCount() {
            this.count++;
        }
    }
}
