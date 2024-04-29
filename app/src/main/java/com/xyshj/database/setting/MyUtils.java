package com.xyshj.database.setting;

import com.oysb.utils.date.DateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes2.dex */
public class MyUtils {
    public static boolean compare_date(String str, String str2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.HM_COLON);
        try {
            Date parse = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date parse2 = simpleDateFormat.parse(str);
            Date parse3 = simpleDateFormat.parse(str2);
            if (parse.getTime() >= parse2.getTime()) {
                return parse.getTime() <= parse3.getTime();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
