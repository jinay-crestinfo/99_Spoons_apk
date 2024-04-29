package com.oysb.xy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.oysb.utils.Loger;
import com.oysb.utils.io.file.SDFileUtils;
import com.oysb.xy.net.report.Report;
import com.oysb.xy.net.report.ReportState;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* loaded from: classes2.dex */
public class ReportDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final long DAY_MILLS = 86400000;
    public final String COLUM_CONTENT;
    public final String COLUM_ID;
    public final String COLUM_RECTCODE;
    public final String COLUM_RECTMSG;
    public final String COLUM_REPORT;
    public final String COLUM_REPORT_NAME;
    public final String COLUM_SEND_TIME;
    public final String COLUM_SERIAL;
    public final String COLUM_SN;
    public final String COLUM_STATE;
    public final String COLUM_UPDATE_TIME;
    public final String TABLE_NAME;
    private static String DATABASE_NAME = SDFileUtils.SDCardRoot + "xyshj/report.db";
    static SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public static void setDatabaseFile(String str) {
        DATABASE_NAME = str;
    }

    public ReportDBHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        this.TABLE_NAME = "report";
        this.COLUM_ID = "_id";
        this.COLUM_REPORT_NAME = "reprotclass";
        this.COLUM_CONTENT = "BLOB";
        this.COLUM_SEND_TIME = "sendtime";
        this.COLUM_STATE = "state";
        this.COLUM_REPORT = "report";
        this.COLUM_SERIAL = "serial";
        this.COLUM_SN = "SN";
        this.COLUM_RECTMSG = "retmsg";
        this.COLUM_RECTCODE = "retcode";
        this.COLUM_UPDATE_TIME = "updatetime";
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        createDB(sQLiteDatabase);
    }

    private synchronized void createDB(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS report(_id INTEGER PRIMARY KEY AUTOINCREMENT,reprotclass VARCHAR(128),BLOB VARCHAR(1024),sendtime VARCHAR(32),report VARCHAR(2048),state INTEGER,serial INTEGER,SN VARCHAR(256),retmsg VARCHAR(128),retcode VARCHAR(60),updatetime VARCHAR(32))");
        } catch (Exception e) {
            Loger.writeException("REPORT_DB", e);
        }
    }

    public synchronized void upgrade(int i, int i2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        try {
            writableDatabase.execSQL("DROP TABLE IF EXISTS report");
            createDB(writableDatabase);
        } catch (Exception e) {
            Loger.writeException("REPORT_DB", e);
        }
        writableDatabase.close();
    }

    public synchronized void clearDB() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        try {
            writableDatabase.execSQL("DELETE FROM report");
        } catch (Exception e) {
            Loger.writeException("REPORT_DB", e);
        }
        writableDatabase.close();
    }

    public synchronized void closeDB() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase != null && writableDatabase.isOpen()) {
            writableDatabase.close();
        }
    }

    public synchronized void addReport(Report report) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        try {
            report.setUpdateTime(new Date());
            Object[] objArr = new Object[11];
            objArr[0] = null;
            objArr[1] = report.getClass().getName();
            objArr[2] = report.getRawData();
            objArr[3] = report.getSendTime() == null ? "" : f.format(report.getSendTime());
            objArr[4] = "";
            objArr[5] = report.getState();
            objArr[6] = Integer.valueOf(report.getSerialNumber());
            objArr[7] = report.getSN();
            objArr[8] = report.getRetMsg();
            objArr[9] = report.getRetCode();
            objArr[10] = f.format(report.getUpdateTime());
            writableDatabase.execSQL("INSERT INTO report VALUES (?,?,?,?,?,?,?,?,?,?,?)", objArr);
        } catch (Exception e) {
            Loger.writeException("REPORT_DB", e);
        }
        writableDatabase.close();
    }

    public synchronized void updateReportState(Report report) {
        if (!report.lostAble() && !report.doNotStoreInDb()) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (writableDatabase == null) {
                return;
            }
            try {
                report.setUpdateTime(new Date());
                ContentValues contentValues = new ContentValues();
                contentValues.put("state", Integer.valueOf(report.getState().getIndex()));
                contentValues.put("sendtime", f.format(report.getSendTime()));
                contentValues.put("retmsg", report.getRetMsg());
                contentValues.put("retcode", report.getRetCode());
                contentValues.put("updatetime", f.format(report.getUpdateTime()));
                writableDatabase.update("report", contentValues, "SN = ?", new String[]{report.getSN()});
            } catch (Exception e) {
                Loger.writeException("REPORT_DB", e);
            }
            writableDatabase.close();
        }
    }

    public synchronized void deleteReport(Report report) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        try {
            writableDatabase.delete("report", "SN = ?", new String[]{report.getSN()});
        } catch (Exception e) {
            Loger.writeException("REPORT_DB", e);
        }
        writableDatabase.close();
    }

    public synchronized void deleteReportsOutDate(int i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        try {
            Date date = new Date();
            date.setTime(new Date().getTime() - (i * 86400000));
            writableDatabase.delete("report", "updatetime < ?", new String[]{f.format(date)});
        } catch (Exception e) {
            Loger.writeException("REPORT_DB", e);
        }
        writableDatabase.close();
    }

    public synchronized List<Report> getUnSendReports() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return arrayList;
        }
        try {
            Cursor rawQuery = writableDatabase.rawQuery("select  reprotclass, BLOB, state, SN from report where state  <> " + ReportState.Finished.getIndex() + " order by _id", null);
            while (rawQuery.moveToNext()) {
                try {
                    String string = rawQuery.getString(0);
                    byte[] blob = rawQuery.getBlob(1);
                    int i = rawQuery.getInt(2);
                    String string2 = rawQuery.getString(3);
                    Report report = (Report) Class.forName(string).newInstance();
                    report.setState(ReportState.int2Enum(i));
                    report.setSN(string2);
                    report.updateByRawData(blob);
                    report.setLostAble(false);
                    arrayList.add(report);
                } catch (Exception unused) {
                }
            }
            rawQuery.close();
        } catch (Exception e) {
            Loger.writeException("REPORT_DB", e);
        }
        writableDatabase.close();
        return arrayList;
    }
}
