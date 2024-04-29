package com.shj.setting.generator;

import android.content.Context;
import android.text.TextUtils;
import com.github.mjdev.libaums.fs.UsbFile;
import com.oysb.utils.cache.CacheHelper;
import com.shj.setting.R;
import com.shj.setting.mainSettingItem.SettingTypeName;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class Generator {
    public static HashMap<Integer, String> parentNameMap = new HashMap<>();

    public static void init(Context context, UserSettingDao userSettingDao) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(18);
        arrayList.add(19);
        arrayList.add(21);
        arrayList.add(20);
        arrayList.add(24);
        arrayList.add(23);
        arrayList.add(22);
        arrayList.add(25);
        arrayList.add(26);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            getSettingTypeList(context, ((Integer) it.next()).intValue(), userSettingDao);
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(122);
        arrayList2.add(Integer.valueOf(SettingType.CARGO_CODE));
        arrayList2.add(Integer.valueOf(SettingType.INVENTORY));
        arrayList2.add(Integer.valueOf(SettingType.CARGO_CAPACITY));
        arrayList2.add(Integer.valueOf(SettingType.DROP_INSPECTION));
        arrayList2.add(106);
        arrayList2.add(109);
        arrayList2.add(145);
        arrayList2.add(112);
        arrayList2.add(126);
        arrayList2.add(149);
        arrayList2.add(153);
        arrayList2.add(164);
        arrayList2.add(Integer.valueOf(SettingType.REPLENISHMENT_OTHER));
        arrayList2.add(182);
        arrayList2.add(187);
        arrayList2.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME));
        arrayList2.add(Integer.valueOf(SettingType.CARGO_MANAGEMENT_OTHER));
        arrayList2.add(300);
        arrayList2.add(Integer.valueOf(SettingType.BANK_MACHINE));
        arrayList2.add(Integer.valueOf(SettingType.NEW_LINKAGE_SYNCHRONIZATION_TIME));
        arrayList2.add(Integer.valueOf(SettingType.QRCODE_FLOAT_VIEW));
        Iterator it2 = arrayList2.iterator();
        while (it2.hasNext()) {
            getTwoLevelMenuSettingTypeList(context, ((Integer) it2.next()).intValue());
        }
    }

    public static List<SettingNote> getSettingNoteList(Context context, int i, UserSettingDao userSettingDao, boolean z) {
        return getNoteList(context, getSettingTypeList(context, i, userSettingDao), i, userSettingDao, z);
    }

    public static List<SettingNote> getNoteList(Context context, List<Integer> list, int i, UserSettingDao userSettingDao, boolean z) {
        ArrayList arrayList = new ArrayList();
        Iterator<Integer> it = list.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            SettingNote createSettingNote = createSettingNote(context, i, it.next().intValue(), userSettingDao, z);
            if (createSettingNote != null) {
                if (createSettingNote instanceof TwoLevelMenuNote) {
                    if (!z2) {
                        z2 = true;
                    }
                }
                arrayList.add(createSettingNote);
            }
        }
        return arrayList;
    }

    public static List<SettingNote> getTwoLevelNoteList(Context context, int i, int i2, UserSettingDao userSettingDao, boolean z) {
        ArrayList arrayList = new ArrayList();
        Iterator<Integer> it = getTwoLevelMenuSettingTypeList(context, i2).iterator();
        while (it.hasNext()) {
            SettingNote createSettingNote = createSettingNote(context, i, it.next().intValue(), userSettingDao, z);
            if (createSettingNote != null) {
                arrayList.add(createSettingNote);
            }
        }
        return arrayList;
    }

    public static List<Integer> getSettingTypeList(Context context, int i, UserSettingDao userSettingDao) {
        String str;
        ArrayList arrayList = new ArrayList();
        if (i == 8) {
            arrayList.add(105);
        } else if (i == 15) {
            arrayList.add(242);
            arrayList.add(244);
            arrayList.add(245);
            arrayList.add(246);
        } else {
            if (i != 16) {
                switch (i) {
                    case 0:
                        str = context.getString(R.string.vendor_setting);
                        arrayList.add(102);
                        arrayList.add(Integer.valueOf(SettingType.MONETARY_SYMBOL));
                        arrayList.add(103);
                        arrayList.add(104);
                        arrayList.add(259);
                        arrayList.add(Integer.valueOf(SettingType.SHOW_BALANCE));
                        arrayList.add(Integer.valueOf(SettingType.CALL_PHONE));
                        arrayList.add(Integer.valueOf(SettingType.GOODWAY_SELECTION));
                        arrayList.add(222);
                        arrayList.add(Integer.valueOf(SettingType.AD_PLAYER));
                        arrayList.add(223);
                        arrayList.add(Integer.valueOf(SettingType.NETWORKING_TIMEOUT));
                        arrayList.add(115);
                        arrayList.add(109);
                        arrayList.add(112);
                        arrayList.add(Integer.valueOf(SettingType.KEFU_PHONE));
                        arrayList.add(117);
                        arrayList.add(118);
                        break;
                    case 1:
                        arrayList.add(126);
                        arrayList.add(122);
                        arrayList.add(Integer.valueOf(SettingType.INVENTORY));
                        arrayList.add(Integer.valueOf(SettingType.CARGO_CAPACITY));
                        arrayList.add(Integer.valueOf(SettingType.CARGO_CODE));
                        arrayList.add(Integer.valueOf(SettingType.DROP_INSPECTION));
                        arrayList.add(145);
                        arrayList.add(149);
                        arrayList.add(153);
                        arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME));
                        break;
                    case 2:
                        arrayList.add(167);
                        arrayList.add(160);
                        arrayList.add(161);
                        arrayList.add(162);
                        arrayList.add(163);
                        arrayList.add(164);
                        arrayList.add(158);
                        arrayList.add(159);
                        break;
                    case 3:
                        arrayList.add(Integer.valueOf(SettingType.DAILY_SALES));
                        arrayList.add(Integer.valueOf(SettingType.MONTHLY_SALES));
                        arrayList.add(192);
                        arrayList.add(193);
                        arrayList.add(Integer.valueOf(SettingType.GOODS_SALES_INFORMATION));
                        arrayList.add(Integer.valueOf(SettingType.CLEAR_LOCAL_SALES_RECORDS));
                        break;
                    case 4:
                        arrayList.add(180);
                        arrayList.add(Integer.valueOf(SettingType.PRINTER));
                        arrayList.add(Integer.valueOf(SettingType.SCAVENGING_WHARF));
                        arrayList.add(177);
                        arrayList.add(178);
                        arrayList.add(236);
                        arrayList.add(179);
                        arrayList.add(181);
                        arrayList.add(182);
                        arrayList.add(187);
                        break;
                    case 5:
                        arrayList.add(248);
                        arrayList.add(172);
                        arrayList.add(Integer.valueOf(SettingType.RESTORE_FACTORY_SETTINGS));
                        arrayList.add(224);
                        arrayList.add(225);
                        arrayList.add(249);
                        arrayList.add(255);
                        arrayList.add(250);
                        arrayList.add(251);
                        arrayList.add(252);
                        arrayList.add(253);
                        arrayList.add(Integer.valueOf(SettingType.SETTING_SYSTEM_TIME));
                        arrayList.add(226);
                        arrayList.add(227);
                        arrayList.add(Integer.valueOf(SettingType.CONNECTING_ELEVATOR));
                        arrayList.add(229);
                        arrayList.add(230);
                        break;
                    case 6:
                        arrayList.add(231);
                        arrayList.add(Integer.valueOf(SettingType.MICROWAVE_OVEN_POSITIONING));
                        arrayList.add(Integer.valueOf(SettingType.MANUAL_POSITIONING_OF_BOXES));
                        arrayList.add(256);
                        arrayList.add(257);
                        arrayList.add(234);
                        arrayList.add(235);
                        arrayList.add(237);
                        arrayList.add(238);
                        arrayList.add(239);
                        arrayList.add(240);
                        arrayList.add(241);
                        arrayList.add(Integer.valueOf(SettingType.SETTING_UP_HUMIDIFIER));
                        arrayList.add(Integer.valueOf(SettingType.FAULT_TEMPERATURE_PROBE));
                        break;
                    default:
                        switch (i) {
                            case 18:
                                String string = context.getString(R.string.replenrishment_setting);
                                arrayList.add(Integer.valueOf(SettingType.REPLENISHMENT_OTHER));
                                if (CacheHelper.getFileCache().getAsString("remove_price_setting") == null) {
                                    arrayList.add(122);
                                }
                                arrayList.add(Integer.valueOf(SettingType.INVENTORY));
                                arrayList.add(Integer.valueOf(SettingType.CARGO_CODE));
                                Iterator<Integer> it = getTwoLevelMenuSettingTypeList(context, SettingType.BANK_MACHINE).iterator();
                                while (true) {
                                    if (it.hasNext()) {
                                        if (AppSetting.isSettingEnabled(context, it.next().intValue(), userSettingDao)) {
                                            arrayList.add(Integer.valueOf(SettingType.BANK_MACHINE));
                                        }
                                    }
                                }
                                str = string;
                                break;
                            case 19:
                                str = context.getString(R.string.goodway_management_setting);
                                arrayList.add(Integer.valueOf(SettingType.CARGO_MANAGEMENT_OTHER));
                                arrayList.add(Integer.valueOf(SettingType.CARGO_CAPACITY));
                                arrayList.add(Integer.valueOf(SettingType.DROP_INSPECTION));
                                arrayList.add(145);
                                arrayList.add(149);
                                arrayList.add(153);
                                arrayList.add(Integer.valueOf(SettingType.NEW_LINKAGE_SYNCHRONIZATION_TIME));
                                arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME));
                                break;
                            case 20:
                                str = context.getString(R.string.system_setup);
                                arrayList.add(Integer.valueOf(SettingType.SOFT_MANAGE));
                                arrayList.add(225);
                                arrayList.add(224);
                                arrayList.add(Integer.valueOf(SettingType.RESTORE_FACTORY_SETTINGS));
                                arrayList.add(Integer.valueOf(SettingType.MAIN_BOARD_SEQUENCE_NUMBER));
                                arrayList.add(Integer.valueOf(SettingType.REBOOT_ANDROID_SYSTEM));
                                arrayList.add(Integer.valueOf(SettingType.MICROPHONE_TEST));
                                arrayList.add(Integer.valueOf(SettingType.ID_CARD_READER_TEST));
                                arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_TEST));
                                arrayList.add(Integer.valueOf(SettingType.LANCHER_SETTINGS));
                                arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_TEST));
                                arrayList.add(Integer.valueOf(SettingType.HIGH_TIME_METER_PID_VID));
                                arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_SENSITIVITY));
                                arrayList.add(Integer.valueOf(SettingType.FIND_PEOPER_DISTANCE));
                                arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_VERSION_QUERY));
                                arrayList.add(Integer.valueOf(SettingType.MAINCONTOL_PROGRAM_UPDATE));
                                arrayList.add(Integer.valueOf(SettingType.SHELF_DRIVING_PROGRAM_UPDATE));
                                arrayList.add(180);
                                arrayList.add(Integer.valueOf(SettingType.SETTING_UP_HUMIDIFIER));
                                arrayList.add(Integer.valueOf(SettingType.FAULT_TEMPERATURE_PROBE));
                                arrayList.add(Integer.valueOf(SettingType.SETTING_SYSTEM_TIME));
                                arrayList.add(181);
                                arrayList.add(Integer.valueOf(SettingType.LIGHTBOX_CANVAS));
                                arrayList.add(Integer.valueOf(SettingType.TOP_LIGHT_CONTROL));
                                arrayList.add(Integer.valueOf(SettingType.LIGHT_BOX_ROLLING_INTERVAL));
                                arrayList.add(226);
                                arrayList.add(Integer.valueOf(SettingType.PRINTER));
                                arrayList.add(Integer.valueOf(SettingType.SCAVENGING_WHARF));
                                arrayList.add(Integer.valueOf(SettingType.DEVICE_SCAN_PORT_ADDERS_YR));
                                arrayList.add(Integer.valueOf(SettingType.LIGHTING_CONTROL_TR));
                                break;
                            case 21:
                                str = context.getString(R.string.pay_system_setting);
                                arrayList.add(Integer.valueOf(SettingType.MONETARY_SYMBOL));
                                arrayList.add(223);
                                arrayList.add(105);
                                arrayList.add(117);
                                arrayList.add(118);
                                arrayList.add(182);
                                arrayList.add(187);
                                arrayList.add(189);
                                break;
                            case 22:
                                str = context.getString(R.string.calibration_heating_setting);
                                arrayList.add(178);
                                arrayList.add(179);
                                arrayList.add(235);
                                arrayList.add(236);
                                break;
                            case 23:
                                str = context.getString(R.string.fault_diagnosis);
                                arrayList.add(Integer.valueOf(SettingType.NUMBER_CABINETS));
                                arrayList.add(158);
                                arrayList.add(159);
                                arrayList.add(160);
                                arrayList.add(161);
                                arrayList.add(163);
                                arrayList.add(162);
                                arrayList.add(234);
                                arrayList.add(164);
                                arrayList.add(Integer.valueOf(SettingType.LIGHT_INSPECTION_STATUS_QUERY));
                                break;
                            case 24:
                                str = context.getString(R.string.lift_system);
                                arrayList.add(Integer.valueOf(SettingType.CONNECTING_ELEVATOR));
                                arrayList.add(227);
                                arrayList.add(245);
                                arrayList.add(229);
                                arrayList.add(230);
                                arrayList.add(244);
                                arrayList.add(242);
                                arrayList.add(256);
                                arrayList.add(231);
                                arrayList.add(Integer.valueOf(SettingType.MANUAL_POSITIONING_OF_BOXES));
                                arrayList.add(Integer.valueOf(SettingType.MICROWAVE_OVEN_POSITIONING));
                                arrayList.add(243);
                                arrayList.add(254);
                                arrayList.add(247);
                                break;
                            case 25:
                                str = context.getString(R.string.app_setting);
                                arrayList.add(102);
                                arrayList.add(Integer.valueOf(SettingType.DEVICE_FACE_SN));
                                arrayList.add(332);
                                arrayList.add(172);
                                arrayList.add(248);
                                arrayList.add(249);
                                arrayList.add(250);
                                arrayList.add(251);
                                arrayList.add(252);
                                arrayList.add(253);
                                arrayList.add(255);
                                arrayList.add(103);
                                arrayList.add(104);
                                arrayList.add(Integer.valueOf(SettingType.BOX_RICE_MACHINE_CABINET_SETTING));
                                arrayList.add(Integer.valueOf(SettingType.PAY_QRCODE_LEVEL));
                                arrayList.add(Integer.valueOf(SettingType.ALWAYS_HEATING));
                                arrayList.add(Integer.valueOf(SettingType.HEART_DIALOG));
                                arrayList.add(222);
                                arrayList.add(Integer.valueOf(SettingType.GOODWAY_SELECTION));
                                arrayList.add(Integer.valueOf(SettingType.CALL_PHONE));
                                arrayList.add(Integer.valueOf(SettingType.AD_PLAYER));
                                arrayList.add(Integer.valueOf(SettingType.SHOW_MARKETING));
                                arrayList.add(Integer.valueOf(SettingType.SHOW_SHOPPING_BUTTON));
                                arrayList.add(Integer.valueOf(SettingType.NETWORKING_TIMEOUT));
                                arrayList.add(220);
                                arrayList.add(Integer.valueOf(SettingType.DRUG_BOX_MENU_NAME));
                                arrayList.add(340);
                                arrayList.add(116);
                                arrayList.add(Integer.valueOf(SettingType.SURVEILLANCE_CAMERA_PID_VID));
                                arrayList.add(300);
                                arrayList.add(109);
                                arrayList.add(112);
                                arrayList.add(Integer.valueOf(SettingType.KEFU_PHONE));
                                arrayList.add(106);
                                arrayList.add(Integer.valueOf(SettingType.QRCODE_FLOAT_VIEW));
                                break;
                            case 26:
                                str = context.getString(R.string.log_look);
                                arrayList.add(Integer.valueOf(SettingType.LOG_SETTING));
                                break;
                            case 27:
                                str = context.getString(R.string.mechanism_parameters);
                                arrayList.add(350);
                                arrayList.add(Integer.valueOf(SettingType.WATER_INLET_SOLENOID_VALVE_CONTROL));
                                arrayList.add(Integer.valueOf(SettingType.MIXING_TANK_CONTROL));
                                break;
                            case 28:
                                str = context.getString(R.string.setting);
                                arrayList.add(251);
                                arrayList.add(248);
                                arrayList.add(172);
                                arrayList.add(224);
                                arrayList.add(225);
                                arrayList.add(Integer.valueOf(SettingType.LANCHER_SETTINGS));
                                arrayList.add(Integer.valueOf(SettingType.AD_PLAYER));
                                arrayList.add(164);
                                break;
                            case 29:
                                str = context.getString(R.string.setting);
                                arrayList.add(102);
                                arrayList.add(180);
                                arrayList.add(112);
                                break;
                            case 30:
                                str = context.getString(R.string.app_setting);
                                arrayList.add(102);
                                arrayList.add(172);
                                arrayList.add(248);
                                arrayList.add(249);
                                arrayList.add(250);
                                arrayList.add(251);
                                arrayList.add(252);
                                arrayList.add(253);
                                arrayList.add(255);
                                arrayList.add(Integer.valueOf(SettingType.NETWORKING_TIMEOUT));
                                arrayList.add(112);
                                break;
                        }
                }
                putRootParentName(str, arrayList);
                return arrayList;
            }
            arrayList.add(168);
            arrayList.add(169);
            arrayList.add(254);
            arrayList.add(156);
            arrayList.add(243);
            arrayList.add(247);
            arrayList.add(258);
        }
        str = "";
        putRootParentName(str, arrayList);
        return arrayList;
    }

    private static void putRootParentName(String str, List<Integer> list) {
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            parentNameMap.put(Integer.valueOf(it.next().intValue()), str);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:16:0x0025. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:17:0x0028. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:18:0x002b. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:19:0x002e. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:20:0x0031. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:21:0x0034. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:22:0x0037. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:23:0x003a. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x00e7  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0126  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.shj.setting.generator.SettingNote createSettingNote(android.content.Context r3, int r4, int r5, com.xyshj.database.setting.UserSettingDao r6, boolean r7) {
        /*
            Method dump skipped, instructions count: 786
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shj.setting.generator.Generator.createSettingNote(android.content.Context, int, int, com.xyshj.database.setting.UserSettingDao, boolean):com.shj.setting.generator.SettingNote");
    }

    public static List<Integer> getTwoLevelMenuSettingTypeList(Context context, int i) {
        ArrayList arrayList = new ArrayList();
        Integer valueOf = Integer.valueOf(SettingType.FULL_DELIVERY);
        Integer valueOf2 = Integer.valueOf(SettingType.COMMODITY_ONE_BUTTON_SETUP);
        switch (i) {
            case 106:
                arrayList.add(107);
                break;
            case 109:
                arrayList.add(110);
                arrayList.add(111);
                break;
            case 112:
                arrayList.add(113);
                arrayList.add(114);
                arrayList.add(Integer.valueOf(SettingType.SOUND_SETTING_ADVERTISEMENT_TIME1));
                arrayList.add(Integer.valueOf(SettingType.SOUND_SETTING_ADVERTISEMENT_TIME2));
                arrayList.add(Integer.valueOf(SettingType.SOUND_SETTING_VOICE_TIME1));
                arrayList.add(Integer.valueOf(SettingType.SOUND_SETTING_VOICE_TIME2));
                break;
            case 122:
                arrayList.add(123);
                arrayList.add(Integer.valueOf(SettingType.PRICE_SETTING_WHOLE_LAYER));
                arrayList.add(125);
                break;
            case 126:
                arrayList.add(127);
                arrayList.add(128);
                arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_MATERIAL_FILE));
                arrayList.add(valueOf2);
                break;
            case SettingType.INVENTORY /* 130 */:
                arrayList.add(Integer.valueOf(SettingType.INVENTORY_SETTING_SINGLE));
                arrayList.add(132);
                arrayList.add(Integer.valueOf(SettingType.INVENTORY_WHOLE_MACHINE));
                arrayList.add(valueOf);
                break;
            case SettingType.CARGO_CAPACITY /* 135 */:
                arrayList.add(Integer.valueOf(SettingType.CARGO_CAPACITY_SETTING));
                arrayList.add(Integer.valueOf(SettingType.CARGO_CAPACITY_WHOLE_LAYER));
                arrayList.add(Integer.valueOf(SettingType.CARGO_CAPACITY_WHOLE_MACHINE));
                break;
            case SettingType.CARGO_CODE /* 139 */:
                arrayList.add(Integer.valueOf(SettingType.CARGO_CODE_SETTING));
                arrayList.add(Integer.valueOf(SettingType.CARGO_CODE_WHOLE_LAYER));
                arrayList.add(Integer.valueOf(SettingType.CARGO_CODE_WHOLE_MACHINE));
                break;
            case SettingType.DROP_INSPECTION /* 142 */:
                arrayList.add(143);
                arrayList.add(144);
                break;
            case 145:
                arrayList.add(146);
                arrayList.add(147);
                arrayList.add(148);
                break;
            case 149:
                arrayList.add(150);
                arrayList.add(151);
                arrayList.add(152);
                break;
            case 153:
                arrayList.add(154);
                arrayList.add(155);
                break;
            case 164:
                arrayList.add(165);
                break;
            case 182:
                arrayList.add(183);
                arrayList.add(185);
                arrayList.add(184);
                arrayList.add(Integer.valueOf(SettingType.BANKNOTE_CHANGE_OF_MONEY));
                break;
            case 187:
                arrayList.add(119);
                arrayList.add(120);
                arrayList.add(Integer.valueOf(SettingType.COINS_ONE_YUAN));
                arrayList.add(Integer.valueOf(SettingType.COIN_QUERY));
                break;
            case SettingType.ELECTROMAGNETIC_LOCK_ON_TIME /* 273 */:
                arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME_SIGLE));
                arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME_WHOLE_LAYER));
                arrayList.add(Integer.valueOf(SettingType.ELECTROMAGNETIC_LOCK_ON_TIME_WHOLE_MACHINE));
                break;
            case SettingType.REPLENISHMENT_OTHER /* 278 */:
                arrayList.add(Integer.valueOf(SettingType.GOODS_INFO_LIST));
                arrayList.add(127);
                arrayList.add(128);
                arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_MERCHANDISE_DETAILPICTURES));
                arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_INSTRUCTIONS_PICTURES));
                arrayList.add(valueOf2);
                arrayList.add(115);
                arrayList.add(valueOf);
                arrayList.add(Integer.valueOf(SettingType.CALCULATED_INVENTORY));
                arrayList.add(Integer.valueOf(SettingType.SCAN_BARCODE_REPLENISHMENT));
                arrayList.add(Integer.valueOf(SettingType.DOWNLOAD_SIGNAL_GOODS_PIC));
                break;
            case SettingType.CARGO_MANAGEMENT_OTHER /* 279 */:
                arrayList.add(156);
                arrayList.add(167);
                arrayList.add(168);
                arrayList.add(169);
                arrayList.add(237);
                arrayList.add(Integer.valueOf(SettingType.WORK_MODE));
                arrayList.add(238);
                arrayList.add(241);
                break;
            case 300:
                arrayList.add(Integer.valueOf(SettingType.CAMERA_TEST));
                arrayList.add(Integer.valueOf(SettingType.CAMERA_AUTO_TAKE));
                break;
            case SettingType.NEW_LINKAGE_SYNCHRONIZATION_TIME /* 328 */:
                arrayList.add(Integer.valueOf(SettingType.NEW_LINKAGE_SYNCHRONIZATION_SIGLE));
                arrayList.add(Integer.valueOf(SettingType.NEW_LINKAGE_SYNCHRONIZATION_LAYER));
                arrayList.add(331);
                break;
            case SettingType.QRCODE_FLOAT_VIEW /* 346 */:
                arrayList.add(Integer.valueOf(SettingType.QRCODE_FLOAT_VIEW_ENABLE));
                arrayList.add(Integer.valueOf(SettingType.QRCODE_FLOAT_VIEW_IMAGE));
                break;
            case SettingType.BANK_MACHINE /* 349 */:
                arrayList.add(Integer.valueOf(SettingType.GRID_MACHINE_FULL_LOAD));
                arrayList.add(Integer.valueOf(SettingType.OPEN_GRID_MACHINE_GRID_ALL));
                arrayList.add(307);
                arrayList.add(308);
                arrayList.add(354);
                break;
        }
        putParentName(context, i, arrayList);
        return arrayList;
    }

    private static void putParentName(Context context, int i, List<Integer> list) {
        String str = parentNameMap.get(Integer.valueOf(i));
        String settingName = SettingTypeName.getSettingName(context, i);
        if (!TextUtils.isEmpty(str)) {
            settingName = str + UsbFile.separator + settingName;
        }
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            parentNameMap.put(Integer.valueOf(it.next().intValue()), settingName);
        }
    }

    public static String getParentName(int i) {
        return parentNameMap.get(Integer.valueOf(i));
    }
}
