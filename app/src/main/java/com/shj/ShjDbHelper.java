package com.shj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.NotificationManagerCompat;
import com.oysb.utils.Loger;
import com.oysb.utils.date.DateUtil;
import com.oysb.utils.io.file.SDFileUtils;
import com.tencent.wxpayface.WxfacePayCommonCode;
import java.util.Date;

/* loaded from: classes2.dex */
public class ShjDbHelper extends SQLiteOpenHelper {
    public static final String COLUM_ID = "_id";
    public static final String COLUM_cur_count = "cur_count";
    public static final String COLUM_goodcount_settime = "goodcount_settime";
    public static final String COLUM_goodprice_settime = "goodprice_settime";
    public static final String COLUM_goods_image = "goods_image";
    public static final String COLUM_goods_name = "goods_name";
    public static final String COLUM_goods_setcount = "setcount";
    public static final String COLUM_goodsbatchnumber = "goodsbatchnumber";
    public static final String COLUM_goodscode = "goodscode";
    public static final String COLUM_groupname = "shelf_group";
    public static final String COLUM_layer = "layer";
    public static final String COLUM_max_count = "max_count";
    public static final String COLUM_pickonly = "pickonly";
    public static final String COLUM_price = "price";
    public static final String COLUM_shelf = "shelf";
    public static final String COLUM_shelf_type = "shelf_type";
    public static final String COLUM_status = "status";
    public static final String COLUM_stopsale = "stopsale_byserver";
    public static final String COLUM_update_time = "update_time";
    private static final String DATABASE_NAME = "xyshj/shj_v1.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "shelves";
    private static ShjDbHelper dbHelper;
    private static SQLiteDatabase innerDb;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public static void init(Context context) {
        dbHelper = new ShjDbHelper(context);
    }

    private ShjDbHelper(Context context) {
        super(context, SDFileUtils.SDCardRoot + DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        createDB(sQLiteDatabase);
    }

    private synchronized void createDB(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS shelves(_id INTEGER PRIMARY KEY AUTOINCREMENT,shelf INTEGER,shelf_type VARCHAR(16),layer INTEGER,status INTEGER,stopsale_byserver VARCHAR(16),pickonly VARCHAR(16),price INTEGER,goodscode VARCHAR(48),goodsbatchnumber VARCHAR(48),max_count INTEGER,cur_count INTEGER,goodcount_settime VARCHAR(48),setcount VARCHAR(48),shelf_group VARCHAR(48),goods_name VARCHAR(128),goods_image VARCHAR(256),update_time VARCHAR(32),goodprice_settime VARCHAR(32))");
            innerDb = sQLiteDatabase;
        } catch (Exception e) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
        }
    }

    private static SQLiteDatabase getInnerDb() {
        SQLiteDatabase sQLiteDatabase = innerDb;
        if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
            innerDb = dbHelper.getWritableDatabase();
        }
        return innerDb;
    }

    public synchronized void upgrade(int i, int i2) {
        SQLiteDatabase innerDb2 = getInnerDb();
        try {
            innerDb2.execSQL("DROP TABLE IF EXISTS shelves");
            createDB(innerDb2);
        } catch (Exception e) {
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
        }
    }

    public static synchronized void clear() {
        synchronized (ShjDbHelper.class) {
            if (dbHelper == null) {
                return;
            }
            try {
                getInnerDb().execSQL("DELETE FROM shelves");
            } catch (Exception e) {
                Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
            }
        }
    }

    public static boolean reset() {
        SQLiteDatabase innerDb2;
        if (dbHelper == null || (innerDb2 = getInnerDb()) == null) {
            return false;
        }
        try {
            innerDb2.execSQL("UPDATE shelves SET status= -1");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean saveShelfInfo(int i, int i2, int i3, int i4, int i5, int i6, String str, String str2, String str3, String str4) {
        String str5;
        String[] strArr;
        String str6;
        if (dbHelper == null) {
            return false;
        }
        String[] strArr2 = {"" + i};
        if (i == 0) {
            str6 = "status > -1";
            strArr = null;
        } else {
            if (i > 999) {
                strArr2 = new String[]{"" + (i + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED)};
                str5 = "layer = ";
            } else {
                str5 = "shelf = ?";
            }
            String str7 = str5;
            strArr = strArr2;
            str6 = str7;
        }
        SQLiteDatabase innerDb2 = getInnerDb();
        if (innerDb2 == null) {
            return false;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUM_shelf, Integer.valueOf(i));
            if (i2 != -1) {
                contentValues.put(COLUM_layer, Integer.valueOf(i2));
            }
            if (i3 != -1) {
                contentValues.put("status", Integer.valueOf(i3));
            }
            if (i4 != -1) {
                contentValues.put(COLUM_price, Integer.valueOf(i4));
                contentValues.put(COLUM_goodprice_settime, DateUtil.format(new Date(), "yyyy-MM-dd HH:ss:mm:SSS"));
            }
            if (i5 != -1) {
                contentValues.put(COLUM_max_count, Integer.valueOf(i5));
            }
            if (i6 != -1) {
                contentValues.put(COLUM_cur_count, Integer.valueOf(i6));
            }
            if (str != null && str.length() > 0) {
                contentValues.put(COLUM_groupname, str);
            }
            if (str2 != null && str2.length() > 0) {
                contentValues.put(COLUM_goodscode, str2);
            }
            if (str3 != null && str3.length() > 0) {
                contentValues.put(COLUM_goods_name, str3);
            }
            if (str4 != null && str4.length() > 0) {
                contentValues.put(COLUM_goods_image, str4);
            }
            contentValues.put(COLUM_update_time, DateUtil.format(new Date(), "yyyy-MM-dd HH:ss:mm:SSS"));
            if (innerDb2.update(TABLE_NAME, contentValues, str6, strArr) != 0) {
                return true;
            }
            if (!contentValues.containsKey("status")) {
                contentValues.put("status", (Integer) 0);
            }
            if (!contentValues.containsKey(COLUM_max_count)) {
                contentValues.put(COLUM_max_count, (Integer) 10);
            }
            if (!contentValues.containsKey(COLUM_cur_count)) {
                contentValues.put(COLUM_cur_count, (Integer) 0);
            }
            if (!contentValues.containsKey(COLUM_price)) {
                contentValues.put(COLUM_price, (Integer) 99900);
            }
            if (!contentValues.containsKey(COLUM_goodscode)) {
                contentValues.put(COLUM_goodscode, Integer.valueOf(i));
            }
            return innerDb2.insert(TABLE_NAME, null, contentValues) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hashShelf(int i) {
        SQLiteDatabase innerDb2;
        if (dbHelper == null || (innerDb2 = getInnerDb()) == null) {
            return false;
        }
        try {
            Cursor rawQuery = innerDb2.rawQuery("select count(1) from shelves where shelf=" + i, null);
            rawQuery.moveToNext();
            if (rawQuery.isNull(0)) {
                return false;
            }
            return rawQuery.getInt(0) != 0;
        } catch (Exception unused) {
            return false;
        }
    }

    public static ShelfInfo getShelfInfo(int i) {
        SQLiteDatabase innerDb2;
        ShelfInfo shelfInfo = null;
        if (dbHelper == null || (innerDb2 = getInnerDb()) == null) {
            return null;
        }
        try {
            ShelfInfo _getShelfInfo = Shj._getShelfInfo(Integer.valueOf(i));
            try {
                Cursor rawQuery = innerDb2.rawQuery("select  _id, shelf, layer, status, price, max_count, cur_count, shelf_group, goodscode, goods_name, goods_image, shelf_type, pickonly, goodsbatchnumber, goodcount_settime, setcount, goodprice_settime from shelves where shelf=" + i, null);
                if (!rawQuery.moveToNext()) {
                    return _getShelfInfo;
                }
                if (!rawQuery.isNull(2)) {
                    _getShelfInfo.setLayer(Integer.valueOf(rawQuery.getInt(2)));
                }
                if (!rawQuery.isNull(3)) {
                    _getShelfInfo.setStatus(Integer.valueOf(rawQuery.getInt(3)));
                }
                if (!rawQuery.isNull(4)) {
                    _getShelfInfo.setPrice(Integer.valueOf(rawQuery.getInt(4)));
                }
                if (!rawQuery.isNull(5)) {
                    _getShelfInfo.setCapacity(Integer.valueOf(rawQuery.getInt(5)));
                }
                if (!rawQuery.isNull(6)) {
                    _getShelfInfo.setGoodsCount(Integer.valueOf(rawQuery.getInt(6)));
                }
                if (!rawQuery.isNull(7)) {
                    _getShelfInfo.setGroupName(rawQuery.getString(7));
                }
                if (!rawQuery.isNull(8)) {
                    _getShelfInfo.setGoodsCode(rawQuery.getString(8));
                }
                if (!rawQuery.isNull(9)) {
                    _getShelfInfo.setGoodsName(rawQuery.getString(9));
                }
                if (!rawQuery.isNull(10)) {
                    _getShelfInfo.setGoodsImage(rawQuery.getString(10));
                }
                if (!rawQuery.isNull(11)) {
                    _getShelfInfo.setShelfType(ShelfType.int2Enum(Integer.parseInt(rawQuery.getString(11))));
                }
                if (!rawQuery.isNull(12)) {
                    _getShelfInfo.setPickOnly(rawQuery.getString(12).equalsIgnoreCase("TRUE"));
                }
                if (!rawQuery.isNull(13)) {
                    _getShelfInfo.setGoodsbatchnumber(rawQuery.getString(13));
                }
                if (!rawQuery.isNull(14)) {
                    _getShelfInfo.setGoodcount_settime(rawQuery.getString(14));
                }
                if (!rawQuery.isNull(15)) {
                    _getShelfInfo.setLastGoodsSetCount(Integer.parseInt(rawQuery.getString(15)));
                }
                if (rawQuery.isNull(16)) {
                    return _getShelfInfo;
                }
                _getShelfInfo.setGoodprice_settime(rawQuery.getString(16));
                return _getShelfInfo;
            } catch (Exception unused) {
                shelfInfo = _getShelfInfo;
                return shelfInfo;
            }
        } catch (Exception unused2) {
        }
    }

    public static boolean updateShelfInfo(int i, String str, String str2) {
        if (dbHelper == null) {
            return false;
        }
        SQLiteDatabase innerDb2 = getInnerDb();
        if (innerDb2 == null) {
            return false;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUM_shelf, Integer.valueOf(i));
            contentValues.put(str, str2);
            if (str.equals(COLUM_goods_setcount)) {
                contentValues.put(COLUM_goodcount_settime, DateUtil.format(new Date(), "yyyy-MM-dd HH:ss:mm:SSS"));
            }
            contentValues.put(COLUM_update_time, DateUtil.format(new Date(), "yyyy-MM-dd HH:ss:mm:SSS"));
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(i);
            return innerDb2.update(TABLE_NAME, contentValues, "shelf = ?", new String[]{sb.toString()}) != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
