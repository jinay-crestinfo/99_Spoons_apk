package com.shj.biz.goods;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.oysb.utils.Loger;
import com.oysb.utils.io.file.SDFileUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class SalesDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final long DAY_MILLS = 86400000;
    public final String COLUM_COUNT;
    public final String COLUM_GOODS;
    public final String COLUM_ID;
    public final String COLUM_MONEY;
    public final String COLUM_PAYTYPE;
    public final String COLUM_TYPE;
    public final String COLUM_UPDATE_TIME;
    public final String TABLE_NAME;
    private static String DATABASE_NAME = SDFileUtils.SDCardRoot + "xyshj/sales.db";
    static SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    /* loaded from: classes2.dex */
    public static class SaleItem {
        public static final String TYPE_GOODS = "GOODS";
        public static final String TYPE_PAY = "PAY";
        public static final String TYPE_REFUND = "REFUND";
        int count;
        String goods;
        int money;
        String payType;
        Date time;
        String type;

        public String getGoods() {
            return this.goods;
        }

        public double getMoney() {
            double d = this.money;
            Double.isNaN(d);
            return d / 100.0d;
        }

        public int count() {
            return this.count;
        }

        public String getType() {
            return this.type;
        }

        public String getPayType() {
            return this.payType;
        }

        public SaleItem(String str, String str2, int i, String str3, int i2, Date date) {
            this.type = str;
            this.payType = str2;
            this.money = i;
            this.goods = str3;
            this.count = i2;
            this.time = date;
        }
    }

    public static void setDatabaseFile(String str) {
        DATABASE_NAME = str;
    }

    public SalesDBHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        this.TABLE_NAME = "sales";
        this.COLUM_ID = "_id";
        this.COLUM_TYPE = IjkMediaMeta.IJKM_KEY_TYPE;
        this.COLUM_PAYTYPE = "payType";
        this.COLUM_MONEY = "money";
        this.COLUM_GOODS = "goods";
        this.COLUM_COUNT = "count";
        this.COLUM_UPDATE_TIME = "updatetime";
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        createDB(sQLiteDatabase);
    }

    private synchronized void createDB(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS sales(_id INTEGER PRIMARY KEY AUTOINCREMENT,type VARCHAR(8),payType VARCHAR(32),money INTEGER,goods VARCHAR(128),count INTEGER,updatetime VARCHAR(32))");
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
            writableDatabase.execSQL("DROP TABLE IF EXISTS sales");
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
            writableDatabase.execSQL("DELETE FROM sales");
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

    public synchronized void addData(SaleItem saleItem) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        try {
            writableDatabase.execSQL("INSERT INTO sales VALUES (?,?,?,?,?,?,?)", new Object[]{null, saleItem.type, saleItem.payType, Integer.valueOf(saleItem.money), saleItem.goods, Integer.valueOf(saleItem.count), f.format(saleItem.time)});
        } catch (Exception e) {
            Loger.writeException("REPORT_DB", e);
        }
        writableDatabase.close();
    }

    public synchronized List<SaleItem> querySalesByType(Date date, Date date2) {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return arrayList;
        }
        try {
            Cursor rawQuery = writableDatabase.rawQuery("select  type, payType, sum(money), sum(count) from sales where type <> 'GOODS' and updatetime  >= " + f.format(date) + " and updatetime  < " + f.format(date2) + " group by " + IjkMediaMeta.IJKM_KEY_TYPE + ", payType", null);
            while (rawQuery.moveToNext()) {
                try {
                    arrayList.add(new SaleItem(rawQuery.getString(0), rawQuery.getString(1), rawQuery.getInt(2), "", rawQuery.getInt(3), new Date()));
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

    public synchronized List<SaleItem> queryGoodsSales(Date date, Date date2) {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return arrayList;
        }
        try {
            Cursor rawQuery = writableDatabase.rawQuery("select  type,goods, sum(money), sum(count) scount from sales where type = 'GOODS' and updatetime  >= " + f.format(date) + " and updatetime  < " + f.format(date2) + " group by " + IjkMediaMeta.IJKM_KEY_TYPE + ", goods order by scount desc", null);
            while (rawQuery.moveToNext()) {
                try {
                    arrayList.add(new SaleItem(rawQuery.getString(0), "", rawQuery.getInt(2), rawQuery.getString(1), rawQuery.getInt(3), new Date()));
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
