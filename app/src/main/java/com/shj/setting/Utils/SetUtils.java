package com.shj.setting.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.serialport.SerialPort;
import android.text.TextUtils;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.setting.Dialog.SelectEnabledDialog;
import com.shj.setting.Dialog.TipDialog;
import com.shj.setting.R;
import com.shj.setting.mainSettingItem.AbsMainSettingItem;
import com.shj.setting.mainSettingItem.SettingTypeName;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSettingDao;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes2.dex */
public class SetUtils {
    public static void forceStopWxSmile(Context context) {
    }

    public static String getShelfState(Context context, int i, int i2) {
        if (i == 0) {
            return i2 + context.getString(R.string.cargo_way_normal);
        }
        if (i == 1) {
            return i2 + context.getString(R.string.lab_shelf) + context.getString(R.string.the_motor_not_stop_properly);
        }
        if (i == 2) {
            return i2 + context.getString(R.string.do_not_exist);
        }
        if (i == 3) {
            return i2 + context.getString(R.string.lab_shelf) + context.getString(R.string.communication_exception);
        }
        if (i != 4) {
            return "";
        }
        return i2 + context.getString(R.string.lab_shelf) + context.getString(R.string.motor_short_circuit);
    }

    public static String getShelfState(Context context, int i) {
        if (i == 0) {
            return context.getString(R.string.cargo_way_normal);
        }
        if (i == 1) {
            return context.getString(R.string.the_motor_not_stop_properly);
        }
        if (i == 2) {
            return context.getString(R.string.do_not_exist);
        }
        if (i == 3) {
            return context.getString(R.string.communication_exception);
        }
        return i == 4 ? context.getString(R.string.motor_short_circuit) : "";
    }

    public static boolean checkApkExist(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(str, 8192);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static PackageInfo getPackageInfo(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return context.getPackageManager().getPackageInfo(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static String findAdPic(String str) {
        File[] listFiles = new File(SDFileUtils.SDCardRoot + "xyShj/avFiles").listFiles();
        if (listFiles == null) {
            return null;
        }
        for (File file : listFiles) {
            if (file.isFile()) {
                String absolutePath = file.getAbsolutePath();
                if (!absolutePath.endsWith(str + ".png")) {
                    if (absolutePath.endsWith(str + ".jpg")) {
                    }
                }
                return absolutePath;
            }
        }
        return null;
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

    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void checkNetConnect(Context context) {
        if (isConnected(context)) {
            return;
        }
        TipDialog tipDialog = new TipDialog(context, 120, R.string.setting_net_available_tip, R.string.button_ok, R.string.open_the_web_page, R.string.system_setup, true);
        tipDialog.setButtonBackgroud(1, R.drawable.selector_dialog_button_yellow);
        tipDialog.setDialogWidth(context.getResources().getDimensionPixelSize(R.dimen.x600));
        tipDialog.setTitle(context.getString(R.string.net_available_tip));
        tipDialog.setTipDialogListenerEx(new TipDialog.TipDialogListenerEx() { // from class: com.shj.setting.Utils.SetUtils.1
            final /* synthetic */ Context val$context;

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
            public void buttonClick_01() {
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
            public void timeEnd() {
            }

            AnonymousClass1(Context context2) {
                context = context2;
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
            public void buttonClick_02() {
                String str = !"zh".equalsIgnoreCase(CommonTool.getLanguage(context)) ? "https://www.google.cn/" : "https://www.baidu.com/";
                Intent intent = new Intent("android.intent.action.web");
                intent.putExtra(IjkMediaPlayer.OnNativeInvokeListener.ARG_URL, str);
                context.startActivity(intent);
            }

            @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
            public void buttonClick_03() {
                context.startActivity(new Intent("android.settings.SETTINGS"));
            }
        });
        tipDialog.show();
    }

    /* renamed from: com.shj.setting.Utils.SetUtils$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements TipDialog.TipDialogListenerEx {
        final /* synthetic */ Context val$context;

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
        public void buttonClick_01() {
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
        public void timeEnd() {
        }

        AnonymousClass1(Context context2) {
            context = context2;
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
        public void buttonClick_02() {
            String str = !"zh".equalsIgnoreCase(CommonTool.getLanguage(context)) ? "https://www.google.cn/" : "https://www.baidu.com/";
            Intent intent = new Intent("android.intent.action.web");
            intent.putExtra(IjkMediaPlayer.OnNativeInvokeListener.ARG_URL, str);
            context.startActivity(intent);
        }

        @Override // com.shj.setting.Dialog.TipDialog.TipDialogListenerEx
        public void buttonClick_03() {
            context.startActivity(new Intent("android.settings.SETTINGS"));
        }
    }

    private static int getPid(Context context, String str) {
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (runningAppProcessInfo.processName.equals(str)) {
                return runningAppProcessInfo.pid;
            }
        }
        return -1;
    }

    private static void forceStopPackage(Context context, String str) {
        try {
            Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class).invoke((ActivityManager) context.getSystemService("activity"), str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String execCmdGetResult(String str) throws Exception {
        Process process;
        BufferedReader bufferedReader;
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader2 = null;
        try {
            process = Runtime.getRuntime().exec(str);
            try {
                process.waitFor();
                BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
                    while (true) {
                        try {
                            String readLine = bufferedReader3.readLine();
                            if (readLine == null) {
                                break;
                            }
                            sb.append(readLine);
                            sb.append('\n');
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader2 = bufferedReader3;
                            closeStream(bufferedReader2);
                            closeStream(bufferedReader);
                            if (process != null) {
                                process.destroy();
                            }
                            throw th;
                        }
                    }
                    while (true) {
                        String readLine2 = bufferedReader.readLine();
                        if (readLine2 == null) {
                            break;
                        }
                        sb.append(readLine2);
                        sb.append('\n');
                    }
                    closeStream(bufferedReader3);
                    closeStream(bufferedReader);
                    if (process != null) {
                        process.destroy();
                    }
                    return sb.toString();
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = null;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = null;
            }
        } catch (Throwable th4) {
            th = th4;
            process = null;
            bufferedReader = null;
        }
    }

    private static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }

    public static int getCameraIndex(String str) {
        try {
            int numberOfCameras = Camera.getNumberOfCameras();
            for (int i = 0; i < numberOfCameras; i++) {
                String readId = readId("cat /sys/class/video4linux/video" + i + "/device/modalias");
                if (readId != null && (readId.startsWith(str) || readId.startsWith(str.toUpperCase()))) {
                    Loger.writeLog("SET", "deviceId=" + readId);
                    return i;
                }
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static String readId(String str) {
        try {
            return execCmdGetResult(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])|(18[0-9])|(19[8,9]))\\d{8}$").matcher(str).matches();
    }

    public static boolean isRegularSn() {
        Loger.writeLog("UI", "Build.SERIAL=" + Build.SERIAL);
        return Build.SERIAL.contains("XUA") && Build.SERIAL.length() == 23;
    }

    public static void setDebug(Context context, UserSettingDao userSettingDao) {
        AppSetting.saveHideHelpQrcode(context, false, userSettingDao);
        AppSetting.saveSettingEnabled(context, 106, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, 111, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.BOX_RICE_MACHINE_CABINET_SETTING, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.SETTING_UP_HUMIDIFIER, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.FAULT_TEMPERATURE_PROBE, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.ELECTROMAGNETIC_LOCK_ON_TIME, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.SOUND_SETTING_ADVERTISEMENT_TIME1, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.SOUND_SETTING_ADVERTISEMENT_TIME2, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.SOUND_SETTING_VOICE_TIME1, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.SOUND_SETTING_VOICE_TIME2, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.DOWNLOAD_INSTRUCTIONS_PICTURES, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.SHOW_SHOPPING_BUTTON, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.SHOW_MARKETING, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.DEVICE_FACE_SN, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.MAIN_BOARD_SEQUENCE_NUMBER, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.DEVICE_SCAN_PORT_ADDERS_YR, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.LIGHT_INSPECTION_STATUS_QUERY, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.CALCULATED_INVENTORY, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.MAINCONTOL_PROGRAM_UPDATE, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.SHELF_DRIVING_PROGRAM_UPDATE, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.SHELF_DRIVING_VERSION_QUERY, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.SCAN_BARCODE_REPLENISHMENT, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, 332, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.DEVICE_DEPLOYMENT_LOCATION, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, 222, true, userSettingDao);
        AppSetting.saveSettingEnabled(context, SettingType.WORK_MODE, true, userSettingDao);
    }

    public static boolean isNeeShowMainSettingItem(Context context, AbsMainSettingItem absMainSettingItem, boolean z, UserSettingDao userSettingDao) {
        List<Integer> childTypeList = absMainSettingItem.getChildTypeList();
        if (childTypeList.size() <= 0) {
            return false;
        }
        if (childTypeList.contains(105)) {
            childTypeList.remove((Object) 105);
            childTypeList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_CASH));
            childTypeList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_WEIXIN));
            childTypeList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_ZFB));
            childTypeList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YL));
            childTypeList.add(Integer.valueOf(SettingType.PAYMENT_METHOD_YLX));
            childTypeList.add(211);
            childTypeList.add(212);
            childTypeList.add(213);
        }
        List<Integer> commandItem = SelectEnabledDialog.getCommandItem();
        Iterator<Integer> it = childTypeList.iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            if (z || !commandItem.contains(Integer.valueOf(intValue))) {
                if (AppSetting.isSettingEnabled(context, intValue, userSettingDao)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void writeShelfInfoLog() {
        List<Integer> shelves = Shj.getShelves();
        Collections.sort(shelves);
        Iterator<Integer> it = shelves.iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
            if (shelfInfo != null) {
                Loger.writeLog("SET", "货道=" + intValue + "价格=" + String.valueOf(shelfInfo.getPrice().intValue() / 100.0f) + "库存=" + shelfInfo.getGoodsCount() + "编码=" + shelfInfo.getGoodsCode());
            } else {
                Loger.writeLog("SET", "货道=" + intValue + "shelfInfo=null");
            }
        }
    }

    public static void getAllAppSetttingValue(Context context) {
        Loger.writeLog("SET", "打印售货机APP设置信息 --开始");
        UserSettingDao userSettingDao = new UserSettingDao(context);
        for (int i = 102; i < 362; i++) {
            String settingName = SettingTypeName.getSettingName(context, i);
            String value = AppSetting.getValue(context, i, userSettingDao);
            if (settingName != null) {
                if (value != null) {
                    Loger.writeLog("SET", settingName + ":" + value);
                } else {
                    Loger.writeLog("SET", settingName + ":null");
                }
            }
        }
        userSettingDao.close();
        Loger.writeLog("SET", "打印售货机APP设置信息 --结束");
    }
}
