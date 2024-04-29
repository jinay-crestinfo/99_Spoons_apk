package com.oysb.utils.date;

import com.xyshj.database.setting.SettingType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class DateUtil {
    public static final long DAY = 86400000;
    public static final String HM = "HHmm";
    public static final String HM_COLON = "HH:mm";
    public static final String YDM_DASH = "yyyy-MM-dd";
    public static final String YDM_SLASH = "yyyy/MM/dd";
    public static final String YMD = "yyyyMMdd";
    public static final String YMD_DASH = "yyyy-MM-dd";
    public static final String YMD_DASH_IMPORT = "yy-MM-dd";
    public static final String YMD_DASH_WITH_FULLTIME = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD_DASH_WITH_FULLTIME24 = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD_DASH_WITH_TIME = "yyyy-MM-dd H:m";
    public static final String YMD_SLASH = "yyyy/MM/dd";
    public static final String YMD_TIME = "HH:mm:ss";
    public static final String YM_DASH = "yyyy-MM";
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public static Calendar calendar = null;
    private static final Map<String, DateFormat> DFS = new HashMap();

    private DateUtil() {
    }

    public static DateFormat getFormat(String str) {
        Map<String, DateFormat> map = DFS;
        DateFormat dateFormat = map.get(str);
        if (dateFormat != null) {
            return dateFormat;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
        map.put(str, simpleDateFormat);
        return simpleDateFormat;
    }

    public static Date parse(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            return getFormat(str2).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date parseAddMonth(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            Date parse = getFormat(str2).parse(str);
            parse.setMonth(parse.getMonth() + 1);
            return parse;
        } catch (ParseException unused) {
            return null;
        }
    }

    public static String parseDateString(String str) {
        if (str == null) {
            return null;
        }
        return str.split(StringUtils.SPACE)[0];
    }

    public static String format(Date date, String str) {
        if (date == null) {
            return null;
        }
        return getFormat(str).format(date);
    }

    public static Date getEndDate(Date date, Integer num, Integer num2) {
        if (date == null) {
            return null;
        }
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        calendar2.add(2, num.intValue());
        calendar2.add(5, num2.intValue());
        return calendar2.getTime();
    }

    public static boolean isValid(int i, int i2, int i3) {
        if (i2 <= 0 || i2 >= 13 || i3 <= 0 || i3 >= 32) {
            return false;
        }
        int i4 = i2 - 1;
        GregorianCalendar gregorianCalendar = new GregorianCalendar(i, i4, i3);
        return gregorianCalendar.get(1) == i && gregorianCalendar.get(2) == i4 && gregorianCalendar.get(5) == i3;
    }

    public static Date getDate(Date date) {
        Calendar convert = convert(date);
        convert.set(11, 0);
        convert.set(12, 0);
        convert.set(13, 0);
        convert.set(14, 0);
        return convert.getTime();
    }

    private static Calendar convert(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return gregorianCalendar;
    }

    public static Date yearOffset(Date date, int i) {
        return offsetDate(date, 1, i);
    }

    public static Date monthOffset(Date date, int i) {
        return offsetDate(date, 2, i);
    }

    public static Date dayOffset(Date date, int i) {
        return offsetDate(date, 5, i);
    }

    public static Date offsetDate(Date date, int i, int i2) {
        Calendar convert = convert(date);
        convert.add(i, i2);
        return convert.getTime();
    }

    public static Date firstDay(Date date) {
        Calendar convert = convert(date);
        convert.set(5, 1);
        return convert.getTime();
    }

    public static Date firstDayOfMonth(Date date) {
        Calendar convert = convert(firstDay(date));
        convert.set(11, 0);
        convert.set(12, 0);
        convert.set(13, 0);
        convert.set(14, 0);
        return convert.getTime();
    }

    public static Date firstDayOfYear(Date date) {
        Calendar convert = convert(date);
        convert.set(2, convert.getActualMinimum(2));
        return firstDayOfMonth(convert.getTime());
    }

    public static Date lastDayOfYear(Date date) {
        Calendar convert = convert(date);
        convert.set(2, convert.getActualMaximum(2));
        return lastDayOfMonth(convert.getTime());
    }

    public static Date lastDay(Date date) {
        Calendar convert = convert(date);
        convert.set(5, convert.getActualMaximum(5));
        return convert.getTime();
    }

    public static Date lastDayOfMonth(Date date) {
        Calendar convert = convert(lastDay(date));
        convert.set(11, 0);
        convert.set(12, 0);
        convert.set(13, 0);
        convert.set(14, 0);
        return convert.getTime();
    }

    public static int dayDiff(Date date, Date date2) {
        return (int) ((date.getTime() - date2.getTime()) / 86400000);
    }

    public static long secondeDiff(Date date, Date date2) {
        return (date.getTime() - date2.getTime()) / 1000;
    }

    public static Date addDay(Date date, int i) {
        Calendar convert = convert(date);
        convert.add(5, i);
        return convert.getTime();
    }

    public static Date addMonth(Date date, int i) {
        Calendar convert = convert(date);
        convert.add(2, i);
        return convert.getTime();
    }

    public static Date addYear(Date date, int i) {
        Calendar convert = convert(date);
        convert.add(1, i);
        return convert.getTime();
    }

    public static Date addHour(Date date, int i) {
        Calendar convert = convert(date);
        convert.add(11, i);
        return convert.getTime();
    }

    public static Date addMinute(Date date, int i) {
        Calendar convert = convert(date);
        convert.add(12, i);
        return convert.getTime();
    }

    public static Date addSecond(Date date, int i) {
        Calendar convert = convert(date);
        convert.add(13, i);
        return convert.getTime();
    }

    public static String parseLog(String str) {
        return str.substring(0, str.indexOf("."));
    }

    public static String formatPreMonth() {
        Date date = new Date();
        date.setMonth(date.getMonth() - 1);
        return getFormat(YM_DASH).format(date);
    }

    public static String formatToday() {
        return getFormat("yyyy-MM-dd").format(new Date());
    }

    public static String formatTodayTime() {
        return getFormat(YMD_TIME).format(new Date());
    }

    public static String formatBeforeday() {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        calendar2.add(5, -1);
        return getFormat(YMD).format(calendar2.getTime());
    }

    public static String formatlastBeforeday() {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        calendar2.add(5, -1);
        return getFormat("yyyy-MM-dd").format(calendar2.getTime());
    }

    public static String getBeforeTenDay() {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        calendar2.add(5, -11);
        return getFormat("yyyy-MM-dd").format(calendar2.getTime());
    }

    public static Date getBeforeDay() {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        calendar2.add(5, -1);
        return calendar2.getTime();
    }

    public static String formatNowTime() {
        return getFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static void main(String[] strArr) {
        System.out.println(addDay(lastDayOfMonth(new Date()), SettingType.INVENTORY_SETTING_SINGLE));
        System.out.println(formatPreMonth());
    }

    public static int daysBetween(Date date, Date date2) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        long timeInMillis = calendar2.getTimeInMillis();
        calendar2.setTime(date2);
        return Integer.parseInt(String.valueOf((calendar2.getTimeInMillis() - timeInMillis) / 86400000));
    }

    public static boolean compare_date(String str, String str2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(HM_COLON);
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

    public static boolean isEffectiveDate(String str, String str2) {
        String format2 = new SimpleDateFormat(HM_COLON).format(new Date(System.currentTimeMillis()));
        System.out.println(format2);
        String[] split = str.split(":");
        int parseInt = Integer.parseInt(split[0]);
        int parseInt2 = Integer.parseInt(split[1]);
        String[] split2 = str2.split(":");
        int parseInt3 = Integer.parseInt(split2[0]);
        int parseInt4 = Integer.parseInt(split2[1]);
        String[] split3 = format2.split(":");
        int parseInt5 = Integer.parseInt(split3[0]);
        int parseInt6 = Integer.parseInt(split3[1]);
        if (parseInt > parseInt5 || parseInt5 > parseInt3) {
            System.out.println("在范围外");
            return false;
        }
        if (parseInt == parseInt5 && parseInt2 >= parseInt6) {
            System.out.println("在范围内");
            return true;
        }
        if (parseInt5 == parseInt3 && parseInt4 >= parseInt6) {
            System.out.println("在范围内");
            return true;
        }
        if (parseInt < parseInt5 && parseInt2 >= parseInt6) {
            System.out.println("在范围内");
            return true;
        }
        if (parseInt5 < parseInt3 && parseInt4 <= parseInt6) {
            System.out.println("在范围内");
            return true;
        }
        System.out.println("在范围外");
        return false;
    }
}
