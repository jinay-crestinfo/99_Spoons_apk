package com.xyshj.database.setting.basedao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import com.xyshj.database.setting.anno.TbField;
import com.xyshj.database.setting.anno.TbName;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public abstract class BaseDao<M> implements IBaseDao<M> {
    private String TAG = getClass().getSimpleName();
    private SQLiteDatabase mDatabase;
    private Class<M> mEntityClass;
    private Map<String, Field> mFieldMap;
    private String mTbName;

    public boolean init(SQLiteDatabase sQLiteDatabase, Class<M> cls) {
        this.mDatabase = sQLiteDatabase;
        this.mEntityClass = cls;
        if (!sQLiteDatabase.isOpen()) {
            return false;
        }
        TbName tbName = (TbName) cls.getAnnotation(TbName.class);
        this.mTbName = tbName == null ? cls.getSimpleName() : tbName.value();
        return genFieldMap() && createTable(sQLiteDatabase);
    }

    protected boolean createTable(SQLiteDatabase sQLiteDatabase) {
        String str;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Field> entry : this.mFieldMap.entrySet()) {
            String key = entry.getKey();
            Field value = entry.getValue();
            TbField tbField = (TbField) value.getAnnotation(TbField.class);
            int length = tbField == null ? 255 : tbField.length();
            Class<?> type = value.getType();
            if (type == String.class) {
                str = "varchar";
            } else if (type == Integer.TYPE || type == Integer.class) {
                str = "int";
            } else if (type == Double.TYPE || type == Double.class) {
                str = "double";
            } else {
                str = (type == Float.TYPE || type == Float.class) ? "float" : "";
            }
            if (TextUtils.isEmpty(str)) {
                Log.e(this.TAG, str.getClass().getName() + "是不支持的字段");
            } else {
                sb.append(key + StringUtils.SPACE + str + "(" + length + "),");
            }
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        String sb2 = sb.toString();
        if (TextUtils.isEmpty(sb2)) {
            Log.e(this.TAG, "获取不到表字段信息");
            return false;
        }
        String str2 = "create table if not exists " + this.mTbName + " (" + sb2 + ") ";
        Log.e(this.TAG, str2);
        sQLiteDatabase.execSQL(str2);
        return true;
    }

    private boolean genFieldMap() {
        this.mFieldMap = new HashMap();
        Field[] fields = this.mEntityClass.getFields();
        if (fields == null || fields.length == 0) {
            Log.e(this.TAG, "获取不到类中字段");
            return false;
        }
        for (Field field : fields) {
            field.setAccessible(true);
            TbField tbField = (TbField) field.getAnnotation(TbField.class);
            this.mFieldMap.put(tbField == null ? field.getName() : tbField.value(), field);
        }
        return true;
    }

    @Override // com.xyshj.database.setting.basedao.IBaseDao
    public Long insert(M m) {
        try {
            return Long.valueOf(this.mDatabase.insert(this.mTbName, null, getContentValues(getValues(m))));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override // com.xyshj.database.setting.basedao.IBaseDao
    public Integer delete(M m) {
        try {
            Condition condition = new Condition(getValues(m));
            return Integer.valueOf(this.mDatabase.delete(this.mTbName, condition.whereClause, condition.whereArgs));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override // com.xyshj.database.setting.basedao.IBaseDao
    public Integer update(M m, M m2) {
        try {
            ContentValues contentValues = getContentValues(getValues(m));
            Condition condition = new Condition(getValues(m2));
            return Integer.valueOf(this.mDatabase.update(this.mTbName, contentValues, condition.whereClause, condition.whereArgs));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override // com.xyshj.database.setting.basedao.IBaseDao
    public List<M> query(M m) {
        return query(m, null);
    }

    @Override // com.xyshj.database.setting.basedao.IBaseDao
    public List<M> query(M m, String str) {
        return query(m, str, null, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x007e, code lost:
    
        if (r13 != null) goto L79;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0088  */
    /* JADX WARN: Type inference failed for: r12v11, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r12v5, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r12v6, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r13v13 */
    /* JADX WARN: Type inference failed for: r13v14 */
    /* JADX WARN: Type inference failed for: r13v4, types: [android.database.Cursor] */
    @Override // com.xyshj.database.setting.basedao.IBaseDao
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.List<M> query(M r12, java.lang.String r13, java.lang.Integer r14, java.lang.Integer r15) {
        /*
            r11 = this;
            r0 = 0
            if (r14 == 0) goto L2c
            if (r15 == 0) goto L2c
            int r14 = r14.intValue()     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            int r14 = r14 + (-1)
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            int r14 = r14.intValue()     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            r1.<init>()     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            if (r14 >= 0) goto L1b
            r14 = 0
        L1b:
            r1.append(r14)     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            java.lang.String r14 = ","
            r1.append(r14)     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            r1.append(r15)     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            java.lang.String r14 = r1.toString()     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            r9 = r14
            goto L2d
        L2c:
            r9 = r0
        L2d:
            if (r12 == 0) goto L49
            java.util.Map r12 = r11.getValues(r12)     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            com.xyshj.database.setting.basedao.BaseDao$Condition r14 = new com.xyshj.database.setting.basedao.BaseDao$Condition     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            r14.<init>(r12)     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            android.database.sqlite.SQLiteDatabase r1 = r11.mDatabase     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            java.lang.String r2 = r11.mTbName     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            r3 = 0
            java.lang.String r4 = r14.whereClause     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            java.lang.String[] r5 = r14.whereArgs     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            r6 = 0
            r7 = 0
            r8 = r13
            android.database.Cursor r12 = r1.query(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            goto L57
        L49:
            android.database.sqlite.SQLiteDatabase r1 = r11.mDatabase     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            java.lang.String r2 = r11.mTbName     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = r13
            android.database.Cursor r12 = r1.query(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L6f java.lang.InstantiationException -> L71 java.lang.IllegalAccessException -> L79
        L57:
            java.util.List r0 = r11.getDataList(r12)     // Catch: java.lang.Throwable -> L61 java.lang.InstantiationException -> L65 java.lang.IllegalAccessException -> L6a
            if (r12 == 0) goto L83
            r12.close()
            goto L83
        L61:
            r13 = move-exception
            r0 = r12
            r12 = r13
            goto L86
        L65:
            r13 = move-exception
            r10 = r13
            r13 = r12
            r12 = r10
            goto L73
        L6a:
            r13 = move-exception
            r10 = r13
            r13 = r12
            r12 = r10
            goto L7b
        L6f:
            r12 = move-exception
            goto L86
        L71:
            r12 = move-exception
            r13 = r0
        L73:
            r12.printStackTrace()     // Catch: java.lang.Throwable -> L84
            if (r13 == 0) goto L83
            goto L80
        L79:
            r12 = move-exception
            r13 = r0
        L7b:
            r12.printStackTrace()     // Catch: java.lang.Throwable -> L84
            if (r13 == 0) goto L83
        L80:
            r13.close()
        L83:
            return r0
        L84:
            r12 = move-exception
            r0 = r13
        L86:
            if (r0 == 0) goto L8b
            r0.close()
        L8b:
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xyshj.database.setting.basedao.BaseDao.query(java.lang.Object, java.lang.String, java.lang.Integer, java.lang.Integer):java.util.List");
    }

    private Map<String, String> getValues(M m) throws IllegalAccessException {
        HashMap hashMap = new HashMap();
        for (Map.Entry<String, Field> entry : this.mFieldMap.entrySet()) {
            Object obj = entry.getValue().get(m);
            hashMap.put(entry.getKey(), obj == null ? "" : obj.toString());
        }
        return hashMap;
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            contentValues.put(entry.getKey(), entry.getValue());
        }
        return contentValues;
    }

    private List<M> getDataList(Cursor cursor) throws IllegalAccessException, InstantiationException {
        Object valueOf;
        if (cursor == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext()) {
            Object newInstance = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
            for (String str : this.mFieldMap.keySet()) {
                Field field = this.mFieldMap.get(str);
                Class<?> type = field.getType();
                if (type == String.class) {
                    valueOf = cursor.getString(cursor.getColumnIndex(str));
                } else if (type == Integer.TYPE || type == Integer.class) {
                    valueOf = Integer.valueOf(cursor.getInt(cursor.getColumnIndex(str)));
                } else if (type == Double.TYPE || type == Double.class) {
                    valueOf = Double.valueOf(cursor.getDouble(cursor.getColumnIndex(str)));
                } else {
                    valueOf = (type == Float.TYPE || type == Float.class) ? Float.valueOf(cursor.getFloat(cursor.getColumnIndex(str))) : null;
                }
                field.set(newInstance, valueOf);
            }
            arrayList.add(newInstance);
        }
        return arrayList;
    }

    /* loaded from: classes2.dex */
    public class Condition {
        String[] whereArgs;
        String whereClause;

        public Condition(Map<String, String> map) {
            StringBuilder sb = new StringBuilder();
            ArrayList arrayList = new ArrayList();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!TextUtils.isEmpty(entry.getValue())) {
                    sb.append("and " + entry.getKey() + "=? ");
                    arrayList.add(entry.getValue());
                }
            }
            this.whereClause = sb.delete(0, 4).toString();
            this.whereArgs = (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
    }
}
