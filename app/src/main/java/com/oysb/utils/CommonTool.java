package com.oysb.utils;

import static com.oysb.utils.http.RequestHelper.handler;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.serialport.SerialPort;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import com.github.mjdev.libaums.fs.UsbFile;
import com.oysb.utils.cache.ACache;
import com.oysb.utils.date.DateUtil;
import com.oysb.utils.io.file.SDFileUtils;
import com.xyshj.database.setting.SettingType;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class CommonTool {
    private static int ID_LENGTH = 17;
    private static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
    private static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
    static float scale = 1.0f;

    public static boolean isMobileNum(String str) {
        return Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$").matcher(str).matches();
    }

    public static boolean vEmail(String str) {
        return Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$").matcher(str).matches();
    }

    public static boolean vId(String str) {
        if (str.length() == 15) {
            try {
                str = getEighteenIDCard(str);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return str.length() == 18 && vIDNumByCode(str) && vIDNumByRegex(str);
    }

    private static boolean vIDNumByRegex(String str) {
        String str2 = "" + Calendar.getInstance().get(1);
        int intValue = Integer.valueOf(str2.substring(2, 3)).intValue();
        int intValue2 = Integer.valueOf(str2.substring(3, 4)).intValue();
        StringBuilder sb = new StringBuilder();
        sb.append("^(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|71|8[1-2])\\d{4}(19\\d{2}|20([0-");
        sb.append(intValue - 1);
        sb.append("][0-9]|");
        sb.append(intValue);
        sb.append("[0-");
        sb.append(intValue2);
        sb.append("]))(((0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])))\\d{3}([0-9]|x|X)$");
        return str.matches(sb.toString());
    }

    private static boolean vIDNumByCode(String str) {
        int[] iArr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] cArr = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        char[] charArray = str.toCharArray();
        int i = ID_LENGTH;
        char c = charArray[i];
        int[] iArr2 = new int[i];
        int i2 = 0;
        for (int i3 = 0; i3 < ID_LENGTH; i3++) {
            iArr2[i3] = charArray[i3] - '0';
            i2 += iArr2[i3] * iArr[i3];
        }
        return Character.toUpperCase(c) == cArr[i2 % 11];
    }

    private static String getEighteenIDCard(String str) throws Exception {
        if (str != null && str.length() == 15) {
            StringBuilder sb = new StringBuilder();
            sb.append(str.substring(0, 6));
            sb.append("19");
            sb.append(str.substring(6));
            sb.append(getVerifyCode(sb.toString()));
            return sb.toString();
        }
        throw new Exception("不是15位的身份证");
    }

    private static char getVerifyCode(String str) throws Exception {
        if (str == null || str.length() < 17) {
            throw new Exception("不合法的身份证号码");
        }
        char[] charArray = str.toCharArray();
        int[] iArr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] cArr = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int i = 0;
        for (int i2 = 0; i2 < 17; i2++) {
            i += (charArray[i2] - '0') * iArr[i2];
        }
        return cArr[i % 11];
    }

    public static void doDialAction(Context context, String str) {
        context.startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + str)));
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static boolean isBackground(Context context) {
        boolean z;
        if (context == null) {
            return true;
        }
        Iterator<ActivityManager.RunningAppProcessInfo> it = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ActivityManager.RunningAppProcessInfo next = it.next();
            if (next.processName.equals(context.getPackageName())) {
                Log.i(context.getPackageName(), "此appimportace =" + next.importance + ",context.getClass().getName()=" + context.getClass().getName());
                if (next.importance != 100) {
                    z = true;
                }
            }
        }
        z = false;
        return z;
    }

    public static int px2dip(Context context, float f) {
        return ((int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f)) - 15;
    }

    public static int px2sp(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int sp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static String getPath(Context context, Uri uri) {
        Uri uri2 = null;
        if ((Build.VERSION.SDK_INT >= 19) && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + UsbFile.separator + split[1];
                }
            } else {
                if (isDownloadsDocument(uri)) {
                    return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
                }
                if (isMediaDocument(uri)) {
                    String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                    String str = split2[0];
                    if ("image".equals(str)) {
                        uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(str)) {
                        uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(str)) {
                        uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
                }
            }
        } else {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                return getDataColumn(context, uri, null, null);
            }
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        String data = query.getString(query.getColumnIndexOrThrow("_data"));
                        if (query != null) {
                            query.close();
                        }
                        return data;
                    }
                } catch (Throwable th) {
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }


    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isJsonString(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("{") && str.endsWith("}");
    }

    public static boolean isCurActivityName(Context context) {
        String[] activePackagesCompat;
        if (Build.VERSION.SDK_INT > 19) {
            activePackagesCompat = getActivePackages(context);
        } else {
            activePackagesCompat = getActivePackagesCompat(context);
        }
        if (activePackagesCompat != null) {
            for (String str : activePackagesCompat) {
                if (str.equals(context.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String[] getActivePackagesCompat(Context context) {
        return new String[]{((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getPackageName()};
    }

    public static String[] getActivePackages(Context context) {
        HashSet hashSet = new HashSet();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
            if (runningAppProcessInfo.importance == 100) {
                hashSet.addAll(Arrays.asList(runningAppProcessInfo.pkgList));
            }
        }
        return (String[]) hashSet.toArray(new String[hashSet.size()]);
    }

    public static boolean isEmailAddress(String str) {
        return Pattern.compile("\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}").matcher(str).matches();
    }

    public static boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static boolean isAsciiPwd(String str, int i, int i2) {
        if (TextUtils.isEmpty(str) || str.length() > i || str.length() < i2 || str.length() != str.getBytes().length) {
            return false;
        }
        for (int i3 = 0; i3 < str.length(); i3++) {
            if (str.charAt(i3) <= ' ' || str.charAt(i3) >= 127) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPassword(String str) {
        return !TextUtils.isEmpty(str) && str.length() == str.getBytes().length;
    }

    public static void showAlertDialog(Context context, String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (str != null) {
            builder.setTitle(str);
        }
        if (str2 != null) {
            builder.setMessage(str2);
        }
        if (str3 != null && onClickListener != null) {
            builder.setPositiveButton(str3, onClickListener);
        }
        if (str4 != null && onClickListener2 != null) {
            builder.setNegativeButton(str4, onClickListener2);
        }
        AlertDialog create = builder.create();
        create.setOnDismissListener(onDismissListener);
        create.show();
    }

    public static void showAlertDialog(Context context, String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (str != null) {
            builder.setTitle(str);
        }
        if (str2 != null) {
            builder.setMessage(str2);
        }
        if (str3 != null && onClickListener != null) {
            builder.setPositiveButton(str3, onClickListener);
        } else if (str3 != null && onClickListener == null) {
            builder.setPositiveButton(str3, new DialogInterface.OnClickListener() { // from class: com.oysb.utils.CommonTool.1

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        if (str4 != null && onClickListener2 != null) {
            builder.setNegativeButton(str4, onClickListener2);
        }
        builder.create().show();
    }

    /* renamed from: com.oysb.utils.CommonTool$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements DialogInterface.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    public static Calendar getPreviusMonth(Calendar calendar) {
        calendar.add(2, -1);
        calendar.set(5, 1);
        return calendar;
    }

    public static String geIndicatetDay(int i) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(5, calendar.get(5));
        calendar.add(5, i);
        int i2 = calendar.get(2) + 1;
        if (i2 < 10) {
            return calendar.get(1) + "-0" + i2 + "-" + calendar.get(5) + "";
        }
        return calendar.get(1) + "-" + i2 + "-" + calendar.get(5) + "";
    }

    public static String calendar2String(Calendar calendar) {
        int i = calendar.get(2) + 1;
        if (i < 10) {
            return calendar.get(1) + "-0" + i;
        }
        return calendar.get(1) + "-" + i;
    }

    public static Calendar getNextMonth(Calendar calendar) {
        calendar.add(2, 1);
        calendar.set(5, 1);
        return calendar;
    }

    public static String getNextMonth(int i) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.add(2, i + 1);
        calendar.set(5, 1);
        calendar.add(2, 1);
        int i2 = calendar.get(2) + 1;
        if (i2 < 10) {
            return calendar.get(1) + "�?0" + i2;
        }
        return calendar.get(1) + "�?" + i2;
    }

    public static void sendMSMViaSys(Activity activity, String str, String str2) {
        if (str == null) {
            str = "";
        }
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + str));
        intent.putExtra("sms_body", str2);
        activity.startActivity(intent);
    }

    public static void soundGpsSignalAlarm(Context context) {
        try {
            Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (defaultUri == null) {
                defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (defaultUri == null) {
                    defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    if (defaultUri == null) {
                        defaultUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
                    }
                }
            }

            if (defaultUri != null) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(context, defaultUri);

                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (audioManager != null && audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Date convertStr2Date(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
        } catch (ParseException unused) {
            return null;
        }
    }

    public static String convertDate2StringByHour(Date date) {
        try {
            return new SimpleDateFormat(DateUtil.YMD_TIME).format(date);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String convertDate2String(Date date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String convertDateMonth2String(Date date) {
        try {
            return new SimpleDateFormat(DateUtil.YM_DASH).format(date);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String convertDateDay2String(Date date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String convertDatemontdayString(String str) {
        try {
            return new SimpleDateFormat("MM月dd日 HH:mm:ss").format(convertStr2Date(str));
        } catch (Exception unused) {
            return null;
        }
    }

    public static String convertDate2StringByCaractere(Date date) {
        try {
            return new SimpleDateFormat("yyyy年MM月dd日 HH时mm分钟").format(date);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String convertDate2StringByCaractere2(Date date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        } catch (Exception unused) {
            return null;
        }
    }

    public static Date getYestoday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, -1);
        return calendar.getTime();
    }

    public static String cutMiniseconde(String str) {
        return str.split("\\.")[0];
    }

    public static double gps2m(double d, double d2, double d3, double d4) {
        double d5 = (d * 3.141592653589793d) / 180.0d;
        double d6 = (d3 * 3.141592653589793d) / 180.0d;
        return Math.round(((Math.asin(Math.sqrt(Math.pow(Math.sin((d5 - d6) / 2.0d), 2.0d) + ((Math.cos(d5) * Math.cos(d6)) * Math.pow(Math.sin((((d2 - d4) * 3.141592653589793d) / 180.0d) / 2.0d), 2.0d)))) * 2.0d) * 6378.137d) * 10000.0d) / 10000;
    }

    public static int convertTmptoSeconde(String str) {
        int intValue = Integer.valueOf(Pattern.compile("[^0-9]").matcher(str).replaceAll("")).intValue();
        return str.endsWith("�?") ? intValue : str.endsWith("分钟") ? intValue * 60 : intValue * ACache.TIME_HOUR;
    }

    public static String convertsecondtoTmp(int i) {
        StringBuilder sb = new StringBuilder();
        int i2 = i / ACache.TIME_HOUR;
        int i3 = i - (i2 * ACache.TIME_HOUR);
        int i4 = i3 / 60;
        if (i4 < 1) {
            i4 = 0;
        }
        int i5 = i3 - (i4 * 60);
        if (i2 > 0) {
            sb.append(i2 + "小时");
        }
        if (i4 > 0) {
            sb.append(i2 + "分钟");
        }
        if (i5 > 0) {
            sb.append(i5 + "秒");
        }
        return sb.toString();
    }

    public static String degreeToDirection(double d) {
        double abs;
        double d2 = d % 360.0d;
        String[] strArr = {"�?", "�?", "�?", "�?"};
        try {
            double round = Math.round(d2 / 90.0d);
            double d3 = d2 % 90.0d;
            if (d3 == 0.0d) {
                StringBuilder sb = new StringBuilder();
                sb.append("�?");
                Double.isNaN(round);
                sb.append(strArr[(int) (round % 4.0d)]);
                return sb.toString();
            }
            if (d3 >= 45.0d) {
                d3 = 90.0d - d3;
                Double.isNaN(round);
                abs = Math.abs(round - 1.0d);
            } else {
                Double.isNaN(round);
                abs = Math.abs(1.0d + round);
            }
            return strArr[((int) round) % 4] + "�?" + strArr[((int) abs) % 4] + ((int) d3) + "�?";
        } catch (Exception unused) {
            return "东偏�?30�?";
        }
    }

//    public static void sendSmsResult(String str, String str2, Context context, Handler handler) {
//        SmsManager smsManager = SmsManager.getDefault();
//        try {
//            smsManager.sendTextMessage(str, null, str2, PendingIntent.getBroadcast(context.getApplicationContext(), 0, new Intent(SMS_SEND_ACTIOIN), 0), PendingIntent.getBroadcast(context.getApplicationContext(), 0, new Intent(SMS_DELIVERED_ACTION), PendingIntent.FLAG_IMMUTABLE));
//            context.registerReceiver(new BroadcastReceiver() { // from class: com.oysb.utils.CommonTool.2
//                final /* synthetic */ Handler val$handle;
//
//                AnonymousClass2(Handler handler2) {
//                    handler = handler2;
//                }
//
//                @Override // android.content.BroadcastReceiver
//                public void onReceive(Context context2, Intent intent) {
//                    Message obtain = Message.obtain();
//                    int resultCode = getResultCode();
//                    if (resultCode == -1) {
//                        if (handler != null) {
//                            obtain.what = -10;
//                            obtain.obj = "成功发送短信....";
//                            handler.sendMessage(obtain);
//                            return;
//                        }
//                        return;
//                    }
//                    if (resultCode == 1) {
//                        if (handler != null) {
//                            obtain.what = -11;
//                            obtain.obj = "短信发送失败....";
//                            handler.sendMessage(obtain);
//                            return;
//                        }
//                        return;
//                    }
//                    if (resultCode == 2) {
//                        if (handler != null) {
//                            obtain.what = -11;
//                            obtain.obj = "短信发送失败....";
//                            handler.sendMessage(obtain);
//                            return;
//                        }
//                        return;
//                    }
//                    if (resultCode == 3 && handler != null) {
//                        obtain.what = -11;
//                        obtain.obj = "短信发送失败....";
//                        handler.sendMessage(obtain);
//                    }
//                }
//            }, new IntentFilter(SMS_SEND_ACTIOIN));
//            context.registerReceiver(new BroadcastReceiver() { // from class: com.oysb.utils.CommonTool.3
//                final /* synthetic */ Handler val$handle;
//
//                AnonymousClass3(Handler handler2) {
//                    handler = handler2;
//                }
//
//                @Override // android.content.BroadcastReceiver
//                public void onReceive(Context context2, Intent intent) {
//                    Message obtain = Message.obtain();
//                    if (handler != null) {
//                        obtain.what = -12;
//                        obtain.obj = "设备已经成功收到指令....";
//                        handler.sendMessage(obtain);
//                    }
//                }
//            }, new IntentFilter(SMS_DELIVERED_ACTION));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /* renamed from: com.oysb.utils.CommonTool$2 */
    /* loaded from: classes2.dex */
//    class AnonymousClass2 extends BroadcastReceiver {
//        final /* synthetic */ Handler val$handle;
//
//        AnonymousClass2(Handler handler2) {
//            handler = handler2;
//        }
//
//        @Override // android.content.BroadcastReceiver
//        public void onReceive(Context context2, Intent intent) {
//            Message obtain = Message.obtain();
//            int resultCode = getResultCode();
//            if (resultCode == -1) {
//                if (handler != null) {
//                    obtain.what = -10;
//                    obtain.obj = "成功发送短信....";
//                    handler.sendMessage(obtain);
//                    return;
//                }
//                return;
//            }
//            if (resultCode == 1) {
//                if (handler != null) {
//                    obtain.what = -11;
//                    obtain.obj = "短信发送失败....";
//                    handler.sendMessage(obtain);
//                    return;
//                }
//                return;
//            }
//            if (resultCode == 2) {
//                if (handler != null) {
//                    obtain.what = -11;
//                    obtain.obj = "短信发送失败....";
//                    handler.sendMessage(obtain);
//                    return;
//                }
//                return;
//            }
//            if (resultCode == 3 && handler != null) {
//                obtain.what = -11;
//                obtain.obj = "短信发送失败....";
//                handler.sendMessage(obtain);
//            }
//        }
//    }

    /* renamed from: com.oysb.utils.CommonTool$3 */
    /* loaded from: classes2.dex */

    public static void setListViewHeightBasedOnChildren(ExpandableListView expandableListView) {
        BaseExpandableListAdapter baseExpandableListAdapter = (BaseExpandableListAdapter) expandableListView.getExpandableListAdapter();
        if (baseExpandableListAdapter == null) {
            return;
        }
        int groupCount = baseExpandableListAdapter.getGroupCount();
        int i = 0;
        for (int i2 = 0; i2 < groupCount; i2++) {
            int i3 = i;
            for (int i4 = 0; i4 < baseExpandableListAdapter.getChildrenCount(i2); i4++) {
                View childView = baseExpandableListAdapter.getChildView(i2, i4, false, null, expandableListView);
                childView.measure(0, 0);
                i3 += childView.getMeasuredHeight();
            }
            View groupView = baseExpandableListAdapter.getGroupView(i2, true, null, expandableListView);
            groupView.measure(0, 0);
            i = groupView.getMeasuredHeight() + i3;
        }
        ViewGroup.LayoutParams layoutParams = expandableListView.getLayoutParams();
        layoutParams.height = i + (expandableListView.getDividerHeight() * (baseExpandableListAdapter.getGroupCount() - 1));
        expandableListView.setLayoutParams(layoutParams);
    }

    public static void setListViewHeightBasedOnChildren1(ExpandableListView expandableListView) {
        ListAdapter adapter = expandableListView.getAdapter();
        if (adapter == null) {
            return;
        }
        int count = adapter.getCount();
        int i = 0;
        for (int i2 = 0; i2 < count; i2++) {
            View view = adapter.getView(i2, null, expandableListView);
            view.measure(0, 0);
            i += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = expandableListView.getLayoutParams();
        layoutParams.height = i + (expandableListView.getDividerHeight() * (adapter.getCount() - 1));
        expandableListView.setLayoutParams(layoutParams);
    }

    public static boolean sdCardIsAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean enoughSpaceOnSdCard(long j) {
        return Environment.getExternalStorageState().equals("mounted") && j < getRealSizeOnSdcard();
    }

    public static long getRealSizeOnSdcard() {
        StatFs statFs = new StatFs(new File(Environment.getExternalStorageDirectory().getAbsolutePath()).getPath());
        return statFs.getAvailableBlocks() * statFs.getBlockSize();
    }

    public static boolean enoughSpaceOnPhone(long j) {
        return getRealSizeOnPhone() > j;
    }

    public static long getRealSizeOnPhone() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return statFs.getBlockSize() * statFs.getAvailableBlocks();
    }

    public static int screenWidth(Context context) {
        return ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
    }

    public static int screenHeight(Context context) {
        return ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
    }

    public static String ToSBC(String str) {
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == ' ') {
                charArray[i] = 12288;
            } else if (charArray[i] < 127) {
                charArray[i] = (char) (charArray[i] + 65248);
            }
        }
        return new String(charArray);
    }

    public static String ToDBC(String str) {
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == 12288) {
                charArray[i] = ' ';
            } else if (charArray[i] > 65280 && charArray[i] < 65375) {
                charArray[i] = (char) (charArray[i] - 65248);
            }
        }
        return new String(charArray);
    }

    public static double getPresionNumber(double d) {
        return new BigDecimal(d).setScale(1, 4).doubleValue();
    }

    public static boolean Pingtest(Context context) {
        try {
            return Runtime.getRuntime().exec("ping -c 1 -i 0.2 -W 1 m.chinagps.cc:8010").waitFor() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean ExistSDCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean isEmail(String str) {
        return Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$").matcher(str).find();
    }

    public static Bitmap takeScreenShot(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap drawingCache = decorView.getDrawingCache();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int i = rect.top;
        System.out.println(i);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Bitmap createBitmap = Bitmap.createBitmap(drawingCache, 0, i, displayMetrics.widthPixels, displayMetrics.heightPixels - i);
        decorView.destroyDrawingCache();
        return createBitmap;
    }

    public static void saveCurrentImage(Activity activity) {
        Bitmap bitmap = Bitmap.createBitmap(activity.getWindow().getDecorView().getRootView().getWidth(), activity.getWindow().getDecorView().getRootView().getHeight(), Bitmap.Config.ARGB_8888);
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache();
        Bitmap drawingCache = rootView.getDrawingCache();
        File file = new File(SDFileUtils.SDCardRoot + UsbFile.separator + System.currentTimeMillis() + ".png");
        FileOutputStream fileOutputStream = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            drawingCache.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static float getScale() {
        return scale;
    }

    public static void setScale(float f) {
        scale = f;
    }

    public static int getDisplayRotation(Activity activity) {
        if (activity == null) {
            return 0;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == 1) {
            return 90;
        }
        if (rotation == 2) {
            return 180;
        }
        if (rotation != 3) {
            return 0;
        }
        return SettingType.HIDE_HELP_QRCODE;
    }



    public static String getLanguage(Context context) {
        String str;
        Locale locale = context.getResources().getConfiguration().locale;
        try {
            str = locale.getLanguage().split("_")[0];
        } catch (Exception unused) {
            str = "";
        }
        if (str.length() != 0) {
            return str;
        }
        try {
            return locale.getLanguage().split("-")[1];
        } catch (Exception unused2) {
            return str;
        }
    }

    public static String getLanguageEx(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        try {
            return locale.getLanguage() + "-" + locale.getCountry();
        } catch (Exception unused) {
            return "";
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && (allNetworkInfo = connectivityManager.getAllNetworkInfo()) != null && allNetworkInfo.length > 0) {
            for (int i = 0; i < allNetworkInfo.length; i++) {
                Loger.writeLog("NET", i + "===状态===" + allNetworkInfo[i].getState());
                Loger.writeLog("NET", i + "===类型===" + allNetworkInfo[i].getTypeName());
                if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isTopActivity(Activity activity) {
        return ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getClassName().equals(activity.getClass().getName());
    }

    public static void execShellCmd(String str) {
        String str2;
        File file = new File(SerialPort.DEFAULT_SU_PATH);
        File file2 = new File("/system/xbin/ubiot");
        if (file.exists()) {
            str2 = "su";
        } else if (!file2.exists()) {
            return;
        } else {
            str2 = "ubiot";
        }
        try {
            OutputStream outputStream = Runtime.getRuntime().exec(str2).getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(str);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
