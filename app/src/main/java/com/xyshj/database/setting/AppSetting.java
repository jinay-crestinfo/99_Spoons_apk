package com.xyshj.database.setting;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class AppSetting {
    public static final String TAG = "AppSetting";

    public static void saveInitFlag(Context context, UserSettingDao userSettingDao) {
        saveAppSetting(context, 101, "init", userSettingDao);
    }

    public static boolean isInitConfigData(Context context, UserSettingDao userSettingDao) {
        String value = getValue(context, 101, userSettingDao);
        return value != null && value.equals("init");
    }

    public static String getMachineId(Context context, UserSettingDao userSettingDao) {
        return getValue(context, 102, userSettingDao);
    }

    public static void saveMachineId(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, 102, str, userSettingDao);
    }

    public static String getMerchantNumber(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.MERCHANT_NUMBER, userSettingDao);
    }

    public static void saveMerchantNumber(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.MERCHANT_NUMBER, str, userSettingDao);
    }

    public static String getDeviceFaceSn(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.DEVICE_FACE_SN, userSettingDao);
    }

    public static String getKeFuPhone(Context context, UserSettingDao userSettingDao) {
        String value = getValue(context, SettingType.KEFU_PHONE, userSettingDao);
        return TextUtils.isEmpty(value) ? "400-100-2229-2" : value;
    }

    public static void saveKeFuPhone(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.KEFU_PHONE, str, userSettingDao);
    }

    public static int getEquipmentType(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 103, userSettingDao);
    }

    public static void saveEquipmentType(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, 103, i, userSettingDao);
    }

    public static int getDeviceDeploymentLocation(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.DEVICE_DEPLOYMENT_LOCATION, userSettingDao);
    }

    public static void saveDeviceDeploymentLocation(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.DEVICE_DEPLOYMENT_LOCATION, i, userSettingDao);
    }

    public static int getPickUpPortCount(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 334, userSettingDao);
    }

    public static void savePickUpPortCount(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, 334, i, userSettingDao);
    }

    public static String getControlTemperatureSet(Context context, UserSettingDao userSettingDao) {
        return getValue(context, 340, userSettingDao);
    }

    public static String getDrugBoxMenuNameLeft(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.DRUG_BOX_MENU_NAME_LEFT, userSettingDao);
    }

    public static void saveDrugBoxMenuNameLeft(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.DRUG_BOX_MENU_NAME_LEFT, str, userSettingDao);
    }

    public static String getDrugBoxMenuNameRight(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.DRUG_BOX_MENU_NAME_RIGHT, userSettingDao);
    }

    public static void saveDrugBoxMenuNameRight(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.DRUG_BOX_MENU_NAME_RIGHT, str, userSettingDao);
    }

    public static int getWorkMode(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.WORK_MODE, userSettingDao);
    }

    public static void saveWokMode(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.WORK_MODE, i, userSettingDao);
    }

    public static int getDataSaveLocation(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 335, userSettingDao);
    }

    public static void saveDataSaveLocation(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, 335, i, userSettingDao);
    }

    public static int getPayQrcodeLevel(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.PAY_QRCODE_LEVEL, userSettingDao);
    }

    public static void savePayQrcodeLevel(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.PAY_QRCODE_LEVEL, i, userSettingDao);
    }

    public static boolean getHeartDialog(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.HEART_DIALOG, userSettingDao) == 0;
    }

    public static void saveHeartDialog(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.HEART_DIALOG, i, userSettingDao);
    }

    public static boolean getAlwaysHeating(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.ALWAYS_HEATING, 1, userSettingDao) == 0;
    }

    public static void saveAlwaysHeating(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.ALWAYS_HEATING, i, userSettingDao);
    }

    public static boolean getCameraTest(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.CAMERA_TEST, 1, userSettingDao) == 0;
    }

    public static void saveCameraTest(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.CAMERA_TEST, i, userSettingDao);
    }

    public static Boolean getCameraAutoTake(Context context, UserSettingDao userSettingDao) {
        if (getIntValue(context, SettingType.CAMERA_AUTO_TAKE, 1, userSettingDao) == 0) {
            return true;
        }
        return false;
    }

    public static void saveCameraAutoTake(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.CAMERA_AUTO_TAKE, i, userSettingDao);
    }

    public static Boolean getPracticeMode(Context context, UserSettingDao userSettingDao) {
        if (getIntValue(context, SettingType.PRACTICE_MODE, 1, userSettingDao) == 0) {
            return true;
        }
        return false;
    }

    public static void savePracticeMode(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.PRACTICE_MODE, i, userSettingDao);
    }

    public static int getMachineType(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 104, userSettingDao);
    }

    public static void saveMachineType(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, 104, i, userSettingDao);
    }

    public static String getAppType(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.APPTYPE, userSettingDao);
    }

    public static void saveAppType(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.APPTYPE, str, userSettingDao);
    }

    public static String getHomeFont(Context context, UserSettingDao userSettingDao) {
        return getValue(context, 198, userSettingDao);
    }

    public static void saveHomeFont(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, 198, str, userSettingDao);
    }

    public static boolean getLightingControlTR(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.LIGHTING_CONTROL_TR, true, userSettingDao);
    }

    public static boolean getAutoUpgrade(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, 199, true, userSettingDao);
    }

    public static void saveAutoUpgrade(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 199, z, userSettingDao);
    }

    public static boolean getCallPhone(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.CALL_PHONE, false, userSettingDao);
    }

    public static void saveCallPhone(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.CALL_PHONE, z, userSettingDao);
    }

    public static boolean getShowCodeZero(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.SHOW_CODE_ZERO, false, userSettingDao);
    }

    public static void saveShowCodeZero(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.SHOW_CODE_ZERO, z, userSettingDao);
    }

    public static boolean getShowTemperature(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, 259, false, userSettingDao);
    }

    public static void saveShowTemperature(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 259, z, userSettingDao);
    }

    public static boolean getShowBalance(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.SHOW_BALANCE, false, userSettingDao);
    }

    public static void saveShowBalance(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.SHOW_BALANCE, z, userSettingDao);
    }

    public static boolean getGoodwaySelection(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.GOODWAY_SELECTION, true, userSettingDao);
    }

    public static void saveGoodwaySelection(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.GOODWAY_SELECTION, z, userSettingDao);
    }

    public static boolean getFreeSweepCode(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, 200, false, userSettingDao);
    }

    public static void saveFreeSweepCode(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 200, z, userSettingDao);
    }

    public static String getQuickPassAddress(Context context, UserSettingDao userSettingDao) {
        return getValue(context, 201, userSettingDao);
    }

    public static void saveQuickPassAddress(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, 201, str, userSettingDao);
    }

    public static int getQuickPassPort(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 202, userSettingDao);
    }

    public static void saveQuickPassPort(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, 202, i, userSettingDao);
    }

    public static String getQuickPassArea(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.QUICK_PASS_AREA, userSettingDao);
    }

    public static void saveQuickPassArea(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.QUICK_PASS_AREA, str, userSettingDao);
    }

    public static String getQuickPassSerialPort(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.QUICK_PASS_SERIAL_PORT, userSettingDao);
    }

    public static void saveQuickPassSerialPort(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.QUICK_PASS_SERIAL_PORT, str, userSettingDao);
    }

    public static String getDeviceScanPortAddersYR(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.DEVICE_SCAN_PORT_ADDERS_YR, userSettingDao);
    }

    public static void saveDeviceScanPortAddersYR(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.DEVICE_SCAN_PORT_ADDERS_YR, str, userSettingDao);
    }

    public static long getQuickPassBaudRate(Context context, UserSettingDao userSettingDao) {
        return getLongValue(context, 205, userSettingDao);
    }

    public static void saveQuickPassBaudRate(Context context, long j, UserSettingDao userSettingDao) {
        saveAppSetting(context, 205, j, userSettingDao);
    }

    public static boolean getPaymentMethodCash(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.PAYMENT_METHOD_CASH, false, userSettingDao);
    }

    public static void savePaymentMethodCash(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.PAYMENT_METHOD_CASH, z, userSettingDao);
    }

    public static boolean getPaymentMethodAggregateCode(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.PAYMENT_METHOD_AGGREGATE_CODE, false, userSettingDao);
    }

    public static void savePaymentAggregateCode(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.PAYMENT_METHOD_AGGREGATE_CODE, z, userSettingDao);
    }

    public static boolean getPaymentMethodWeixin(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.PAYMENT_METHOD_WEIXIN, false, userSettingDao);
    }

    public static void savePaymentMethodWeixin(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.PAYMENT_METHOD_WEIXIN, z, userSettingDao);
    }

    public static boolean getPaymentMethodZfb(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.PAYMENT_METHOD_ZFB, false, userSettingDao);
    }

    public static void savePaymentMethodZfb(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.PAYMENT_METHOD_ZFB, z, userSettingDao);
    }

    public static boolean getPaymentMethodYl(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.PAYMENT_METHOD_YL, false, userSettingDao);
    }

    public static void savePaymentMethodYl(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.PAYMENT_METHOD_YL, z, userSettingDao);
    }

    public static boolean getPaymentMethodYlx(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.PAYMENT_METHOD_YLX, false, userSettingDao);
    }

    public static void savePaymentMethodYlx(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.PAYMENT_METHOD_YLX, z, userSettingDao);
    }

    public static boolean getPaymentMethodYlsf(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, 211, false, userSettingDao);
    }

    public static void savePaymentMethodYlsf(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 211, z, userSettingDao);
    }

    public static boolean getPaymentMethodIc(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, 212, false, userSettingDao);
    }

    public static void savePaymentMethodIc(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 212, z, userSettingDao);
    }

    public static boolean getPaymentMethodJd(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, 213, false, userSettingDao);
    }

    public static void savePaymentMethodJd(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 213, z, userSettingDao);
    }

    public static boolean getPaymentMethodJF(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.PAYMENT_METHOD_JF, false, userSettingDao);
    }

    public static void savePaymentMethodJF(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.PAYMENT_METHOD_JF, z, userSettingDao);
    }

    public static boolean getPaymentMethodScan(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.PAYMENT_METHOD_SCAN, false, userSettingDao);
    }

    public static void savePaymentMethodScan(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.PAYMENT_METHOD_SCAN, z, userSettingDao);
    }

    public static boolean getPaymentMethodSales(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, 214, false, userSettingDao);
    }

    public static void savePaymentMethodSales(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 214, z, userSettingDao);
    }

    public static boolean getHideHelpQrcode(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.HIDE_HELP_QRCODE, false, userSettingDao);
    }

    public static void saveHideHelpQrcode(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.HIDE_HELP_QRCODE, z, userSettingDao);
    }

    public static SerialDeviceData getMainControl(Context context, UserSettingDao userSettingDao) {
        String value = getValue(context, 180, userSettingDao);
        if (value == null) {
            return null;
        }
        SerialDeviceData serialDeviceData = new SerialDeviceData();
        serialDeviceData.formJson(value);
        return serialDeviceData;
    }

    public static void saveMainControl(Context context, SerialDeviceData serialDeviceData, UserSettingDao userSettingDao) {
        saveAppSetting(context, 180, serialDeviceData.toJson(), userSettingDao);
    }

    public static SerialDeviceData getPrinter(Context context, UserSettingDao userSettingDao) {
        String value = getValue(context, SettingType.PRINTER, userSettingDao);
        if (value == null) {
            return null;
        }
        SerialDeviceData serialDeviceData = new SerialDeviceData();
        serialDeviceData.formJson(value);
        return serialDeviceData;
    }

    public static void savePrinter(Context context, SerialDeviceData serialDeviceData, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.PRINTER, serialDeviceData.toJson(), userSettingDao);
    }

    public static SerialDeviceData getScavenging(Context context, UserSettingDao userSettingDao) {
        String value = getValue(context, SettingType.SCAVENGING_WHARF, userSettingDao);
        if (value == null) {
            return null;
        }
        SerialDeviceData serialDeviceData = new SerialDeviceData();
        serialDeviceData.formJson(value);
        return serialDeviceData;
    }

    public static void saveScavenging(Context context, SerialDeviceData serialDeviceData, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.SCAVENGING_WHARF, serialDeviceData.toJson(), userSettingDao);
    }

    public static int getSoundSettingAdvertisement(Context context, UserSettingDao userSettingDao) {
        SoundTimeData soundSettingAdvertisementTime1 = getSoundSettingAdvertisementTime1(context, userSettingDao);
        if (soundSettingAdvertisementTime1 != null && soundSettingAdvertisementTime1.startTime != null && soundSettingAdvertisementTime1.endTime != null && MyUtils.compare_date(soundSettingAdvertisementTime1.startTime, soundSettingAdvertisementTime1.endTime)) {
            return soundSettingAdvertisementTime1.soundValue;
        }
        SoundTimeData soundSettingAdvertisementTime2 = getSoundSettingAdvertisementTime2(context, userSettingDao);
        if (soundSettingAdvertisementTime2 != null && soundSettingAdvertisementTime2.startTime != null && soundSettingAdvertisementTime2.endTime != null && MyUtils.compare_date(soundSettingAdvertisementTime2.startTime, soundSettingAdvertisementTime2.endTime)) {
            return soundSettingAdvertisementTime2.soundValue;
        }
        return getIntValue(context, 113, userSettingDao);
    }

    public static void saveSoundSettingAdvertisement(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, 113, i, userSettingDao);
    }

    public static int getSoundSettingVoice(Context context, UserSettingDao userSettingDao) {
        SoundTimeData soundSettingVoiceTime1 = getSoundSettingVoiceTime1(context, userSettingDao);
        if (soundSettingVoiceTime1 != null && soundSettingVoiceTime1.startTime != null && soundSettingVoiceTime1.endTime != null && MyUtils.compare_date(soundSettingVoiceTime1.startTime, soundSettingVoiceTime1.endTime)) {
            return soundSettingVoiceTime1.soundValue;
        }
        SoundTimeData soundSettingVoiceTime2 = getSoundSettingVoiceTime2(context, userSettingDao);
        if (soundSettingVoiceTime2 != null && soundSettingVoiceTime2.startTime != null && soundSettingVoiceTime2.endTime != null && MyUtils.compare_date(soundSettingVoiceTime2.startTime, soundSettingVoiceTime2.endTime)) {
            return soundSettingVoiceTime2.soundValue;
        }
        return getIntValue(context, 114, userSettingDao);
    }

    public static void saveSoundSettingVoice(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, 114, i, userSettingDao);
    }

    public static SoundTimeData getSoundSettingAdvertisementTime1(Context context, UserSettingDao userSettingDao) {
        return getSoundTime(context, SettingType.SOUND_SETTING_ADVERTISEMENT_TIME1, userSettingDao);
    }

    public static void saveSoundSettingAdvertisementTime1(Context context, SoundTimeData soundTimeData, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.SOUND_SETTING_ADVERTISEMENT_TIME1, soundTimeData.toJson(), userSettingDao);
    }

    public static SoundTimeData getSoundSettingAdvertisementTime2(Context context, UserSettingDao userSettingDao) {
        return getSoundTime(context, SettingType.SOUND_SETTING_ADVERTISEMENT_TIME2, userSettingDao);
    }

    public static void saveSoundSettingAdvertisementTime2(Context context, SoundTimeData soundTimeData, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.SOUND_SETTING_ADVERTISEMENT_TIME2, soundTimeData.toJson(), userSettingDao);
    }

    public static SoundTimeData getSoundSettingVoiceTime1(Context context, UserSettingDao userSettingDao) {
        return getSoundTime(context, SettingType.SOUND_SETTING_VOICE_TIME1, userSettingDao);
    }

    public static void saveSoundSettingVoiceTime1(Context context, SoundTimeData soundTimeData, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.SOUND_SETTING_VOICE_TIME1, soundTimeData.toJson(), userSettingDao);
    }

    public static SoundTimeData getSoundSettingVoiceTime2(Context context, UserSettingDao userSettingDao) {
        return getSoundTime(context, SettingType.SOUND_SETTING_VOICE_TIME2, userSettingDao);
    }

    public static void saveSoundSettingVoiceTime2(Context context, SoundTimeData soundTimeData, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.SOUND_SETTING_VOICE_TIME2, soundTimeData.toJson(), userSettingDao);
    }

    public static SoundTimeData getSoundTime(Context context, int i, UserSettingDao userSettingDao) {
        String value = getValue(context, i, userSettingDao);
        if (value == null) {
            return null;
        }
        SoundTimeData soundTimeData = new SoundTimeData();
        soundTimeData.formJson(value);
        return soundTimeData;
    }

    public static boolean getEnableCommodityPriceCheck(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 115, userSettingDao) == 0;
    }

    public static void saveEnableCommodityPriceCheck(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 115, !z ? 1 : 0, userSettingDao);
    }

    public static boolean getCommodityClassification(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 215, userSettingDao) == 0;
    }

    public static void saveCommodityClassification(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 215, !z ? 1 : 0, userSettingDao);
    }

    public static void saveSwallowingMoneyTime(Context context, long j, UserSettingDao userSettingDao) {
        saveAppSetting(context, 117, j, userSettingDao);
    }

    public static long getSwallowingMoneyTime(Context context, UserSettingDao userSettingDao) {
        return getLongValue(context, 117, userSettingDao);
    }

    public static void saveQuantityOfMerchandisePerPage(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, 110, i, userSettingDao);
    }

    public static int getQuantityOfMerchandisePerPage(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 110, userSettingDao);
    }

    public static String getMonetarySymbol(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.MONETARY_SYMBOL, userSettingDao);
    }

    public static void saveMonetarySymbol(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.MONETARY_SYMBOL, str, userSettingDao);
    }

    public static String getSurveillanceCameraPidVid(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.SURVEILLANCE_CAMERA_PID_VID, userSettingDao);
    }

    public static void saveSurveillanceCameraPidVid(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.SURVEILLANCE_CAMERA_PID_VID, str, userSettingDao);
    }

    public static String getHighTimeMeterPidVid(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.HIGH_TIME_METER_PID_VID, userSettingDao);
    }

    public static void saveHighTimeMeterPidVid(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.HIGH_TIME_METER_PID_VID, str, userSettingDao);
    }

    public static String getReminder(Context context, UserSettingDao userSettingDao) {
        return getValue(context, 116, userSettingDao);
    }

    public static void saveReminder(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, 116, str, userSettingDao);
    }

    public static String getActivityWebsite(Context context, UserSettingDao userSettingDao) {
        return getValue(context, 107, userSettingDao);
    }

    public static void saveActivityWebsite(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, 107, str, userSettingDao);
    }

    public static boolean getCivilianService(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.CIVILIAN_SERVICE, false, userSettingDao);
    }

    public static void saveCivilianService(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.CIVILIAN_SERVICE, z, userSettingDao);
    }

    public static boolean getActivityBulletin(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, SettingType.ACTIVITY_BULLETIN, false, userSettingDao);
    }

    public static void saveActivityBulletin(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.ACTIVITY_BULLETIN, z, userSettingDao);
    }

    public static boolean getAutoToActivity(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, 220, false, userSettingDao);
    }

    public static void saveAutoToActivity(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 220, z, userSettingDao);
    }

    public static void saveShoppingInterfaceStyle(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, 111, i, userSettingDao);
    }

    public static int getShoppingInterfaceStyle(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 111, userSettingDao);
    }

    public static boolean getAccessToNewPlatform(Context context, UserSettingDao userSettingDao) {
        return getBooleanValue(context, 221, true, userSettingDao);
    }

    public static void saveAccessToNewPlatform(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 221, z, userSettingDao);
    }

    public static boolean getEnableShoppingCart(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 222, userSettingDao) == 0;
    }

    public static boolean getEnableShoppingCart(Context context, Boolean bool, UserSettingDao userSettingDao) {
        return getIntValue(context, 222, !bool.booleanValue() ? 1 : 0, userSettingDao) == 0;
    }

    public static void saveEnableShoppingCart(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 222, !z ? 1 : 0, userSettingDao);
    }

    public static boolean getFaceEnableShoppingCart(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.ENABLE_SHOPPING_CART_FACE, 1, userSettingDao) == 0;
    }

    public static void saveFaceEnableShoppingCart(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.ENABLE_SHOPPING_CART_FACE, !z ? 1 : 0, userSettingDao);
    }

    public static boolean getYYTAd(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 281, 1, userSettingDao) == 0;
    }

    public static void saveYYTAd(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 281, !z ? 1 : 0, userSettingDao);
    }

    public static boolean getShowMarketing(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.SHOW_MARKETING, 1, userSettingDao) == 0;
    }

    public static void saveShowMarketing(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.SHOW_MARKETING, !z ? 1 : 0, userSettingDao);
    }

    public static boolean getShowShoppingButton(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.SHOW_SHOPPING_BUTTON, 1, userSettingDao) == 0;
    }

    public static void saveShowShoppingButton(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.SHOW_SHOPPING_BUTTON, !z ? 1 : 0, userSettingDao);
    }

    public static boolean getQrcodeFloatViewEnable(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.QRCODE_FLOAT_VIEW_ENABLE, 1, userSettingDao) == 0;
    }

    public static String getQrcodeFloatViewImage(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.QRCODE_FLOAT_VIEW_IMAGE, userSettingDao);
    }

    public static boolean getEnableFacePay(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, 223, userSettingDao) == 0;
    }

    public static void saveEnableFacePay(Context context, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, 223, !z ? 1 : 0, userSettingDao);
    }

    public static int getFacepayType(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.FACEPAY_TYPE, userSettingDao);
    }

    public static void saveFacepayType(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.FACEPAY_TYPE, i, userSettingDao);
    }

    public static int getNetworkingTimeout(Context context, UserSettingDao userSettingDao) {
        return getIntValue(context, SettingType.NETWORKING_TIMEOUT, 2, userSettingDao);
    }

    public static void saveNetworkingTimeout(Context context, int i, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.NETWORKING_TIMEOUT, i, userSettingDao);
    }

    public static String getAdPlayer(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.AD_PLAYER, userSettingDao);
    }

    public static void saveAdPlayer(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.AD_PLAYER, str, userSettingDao);
    }

    public static String getBoxRiceMachineCabinetSetting(Context context, UserSettingDao userSettingDao) {
        return getValue(context, SettingType.BOX_RICE_MACHINE_CABINET_SETTING, userSettingDao);
    }

    public static void saveBoxRiceMachineCabinetSetting(Context context, String str, UserSettingDao userSettingDao) {
        saveAppSetting(context, SettingType.BOX_RICE_MACHINE_CABINET_SETTING, str, userSettingDao);
    }

    public static void saveAppSetting(Context context, int i, boolean z, UserSettingDao userSettingDao) {
        saveAppSetting(context, i, String.valueOf(z), userSettingDao);
    }

    public static void saveAppSetting(Context context, int i, int i2, UserSettingDao userSettingDao) {
        saveAppSetting(context, i, String.valueOf(i2), userSettingDao);
    }

    public static void saveAppSetting(Context context, int i, long j, UserSettingDao userSettingDao) {
        saveAppSetting(context, i, String.valueOf(j), userSettingDao);
    }

    public static void saveAppSetting(Context context, int i, String str, UserSettingDao userSettingDao) {
        Log.i(TAG, "saveAppSetting" + str + " settingType=" + i);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String settingKey = SettingType.getSettingKey(i);
        boolean z = false;
        if (userSettingDao == null) {
            userSettingDao = new UserSettingDao(context);
            z = true;
        }
        userSettingDao.insert(new UserSetting(i, SettingType.getParentId(i), settingKey, str));
        if (z) {
            userSettingDao.close();
        }
    }

    public static boolean getBooleanValue(Context context, int i, boolean z, UserSettingDao userSettingDao) {
        String value = getValue(context, i, userSettingDao);
        Log.i(TAG, "getBooleanValue" + value + " settingType=" + i);
        return value != null ? Boolean.valueOf(value).booleanValue() : z;
    }

    public static int getIntValue(Context context, int i, UserSettingDao userSettingDao) {
        String value = getValue(context, i, userSettingDao);
        if (value != null) {
            return Integer.valueOf(value).intValue();
        }
        return 0;
    }

    public static int getIntValue(Context context, int i, int i2, UserSettingDao userSettingDao) {
        String value = getValue(context, i, userSettingDao);
        return value != null ? Integer.valueOf(value).intValue() : i2;
    }

    public static long getLongValue(Context context, int i, UserSettingDao userSettingDao) {
        String value = getValue(context, i, userSettingDao);
        if (value != null) {
            return Long.parseLong(value);
        }
        return 0L;
    }

    public static String getValue(Context context, int i, UserSettingDao userSettingDao) {
        boolean z;
        String settingKey = SettingType.getSettingKey(i);
        if (userSettingDao == null) {
            userSettingDao = new UserSettingDao(context);
            z = true;
        } else {
            z = false;
        }
        UserSetting userSettingFormKey = userSettingDao.getUserSettingFormKey(settingKey);
        String value = userSettingFormKey != null ? userSettingFormKey.getValue() : null;
        if (z) {
            userSettingDao.close();
        }
        return value;
    }

    public static void saveSettingEnabled(Context context, int i, boolean z, UserSettingDao userSettingDao) {
        boolean z2;
        String settingKey = SettingType.getSettingKey(i);
        if (userSettingDao == null) {
            userSettingDao = new UserSettingDao(context);
            z2 = true;
        } else {
            z2 = false;
        }
        userSettingDao.insertEnabled(new SettingsEnabled(settingKey, String.valueOf(z)));
        if (z2) {
            userSettingDao.close();
        }
    }

    public static boolean isSettingEnabled(Context context, int i, UserSettingDao userSettingDao) {
        boolean z;
        String settingKey = SettingType.getSettingKey(i);
        if (userSettingDao == null) {
            userSettingDao = new UserSettingDao(context);
            z = true;
        } else {
            z = false;
        }
        SettingsEnabled settingEnabledFormKey = userSettingDao.getSettingEnabledFormKey(settingKey);
        boolean booleanValue = settingEnabledFormKey != null ? Boolean.valueOf(settingEnabledFormKey.getValue()).booleanValue() : true;
        if (z) {
            userSettingDao.close();
        }
        return booleanValue;
    }

    public static void hideSetting(Context context, int i) {
        List<Integer> typeListTwo;
        boolean z;
        if (i == 0) {
            typeListTwo = getTypeListOne();
        } else {
            typeListTwo = i == 1 ? getTypeListTwo() : null;
        }
        if (typeListTwo != null) {
            UserSettingDao userSettingDao = new UserSettingDao(context);
            for (int i2 = 102; i2 < 362; i2++) {
                Iterator<Integer> it = typeListTwo.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (i2 == it.next().intValue()) {
                            z = true;
                            break;
                        }
                    } else {
                        z = false;
                        break;
                    }
                }
                if (z) {
                    userSettingDao.insertEnabled(new SettingsEnabled(SettingType.getSettingKey(i2), String.valueOf(false)));
                } else {
                    userSettingDao.insertEnabled(new SettingsEnabled(SettingType.getSettingKey(i2), String.valueOf(true)));
                }
            }
            userSettingDao.close();
        }
    }

    private static List<Integer> getTypeListOne() {
        return new ArrayList();
    }

    private static List<Integer> getTypeListTwo() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(102);
        arrayList.add(103);
        arrayList.add(104);
        arrayList.add(105);
        arrayList.add(106);
        arrayList.add(107);
        arrayList.add(108);
        arrayList.add(109);
        arrayList.add(110);
        arrayList.add(111);
        arrayList.add(112);
        arrayList.add(113);
        arrayList.add(114);
        arrayList.add(115);
        arrayList.add(116);
        arrayList.add(117);
        arrayList.add(118);
        arrayList.add(119);
        arrayList.add(120);
        arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
        return arrayList;
    }
}
