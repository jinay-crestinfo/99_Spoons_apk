package com.shj.biz.tradrecord;

import android.text.TextUtils;
import com.github.mjdev.libaums.fs.UsbFile;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.oysb.utils.date.DateUtil;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.biz.order.OrderPayType;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.net.SocketClient;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class TradRecordManager {
    public static int TYPE_EAT_MONNEY = 999;
    public static RecordData dateRecordData;
    public static RecordData machineRecordData;
    public static RecordData monthRecordData;
    private static String saveAllPath = SDFileUtils.SDCardRoot + "TransactionRecords";
    private static String savePath = "TransactionRecords";
    public static HashMap<String, ShelfTradRecordData> shelfTradRecordDataMap;
    public static RecordData stageRecordData;
    public static RecordData yearRecordData;

    public static void saveTradRecord(boolean z, int i, String str, String str2, String str3, String str4, int i2, OrderPayType orderPayType, String str5) {
        Loger.writeLog("UI", "saveTradRecord, isSuccess:" + z + ",tradSn:" + i + ",remark:" + str + ", shelf:" + str2 + ", goodsCode" + str3 + ",goodsName:" + str4 + ", money:" + i2 + ", payType:" + orderPayType + ",date:" + str5);
        String str6 = str5 + Marker.ANY_MARKER + z + Marker.ANY_MARKER + i + Marker.ANY_MARKER + str + Marker.ANY_MARKER + str2 + Marker.ANY_MARKER + str3 + Marker.ANY_MARKER + str4 + Marker.ANY_MARKER + i2 + Marker.ANY_MARKER + orderPayType.getIndex() + SocketClient.NETASCII_EOL;
        SDFileUtils.writeToSDFromInput(savePath, str5.substring(0, 8) + "_log", str6, true);
        if (z) {
            saveRecord(dateRecordData, str2, orderPayType.getIndex(), orderPayType.getName(), "_date", 8, i2, str5);
            saveRecord(monthRecordData, str2, orderPayType.getIndex(), orderPayType.getName(), "_month", 6, i2, str5);
            saveRecord(yearRecordData, str2, orderPayType.getIndex(), orderPayType.getName(), "_year", 4, i2, str5);
            saveRecord(machineRecordData, str2, orderPayType.getIndex(), orderPayType.getName(), "_all", 0, i2, str5);
            saveRecord(stageRecordData, str2, orderPayType.getIndex(), orderPayType.getName(), "_stage", 0, i2, str5);
            saveShelfRecordData(str2, i2, str5);
        }
    }

    public static void saveEatMoneyRecord(int i) {
        String format = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        Loger.writeLog("UI", "saveEatMoneyRecord, money:" + i);
        saveRecord(dateRecordData, null, TYPE_EAT_MONNEY, "", "_date", 8, i, format);
        saveRecord(monthRecordData, null, TYPE_EAT_MONNEY, "", "_month", 6, i, format);
        saveRecord(yearRecordData, null, TYPE_EAT_MONNEY, "", "_year", 4, i, format);
        saveRecord(machineRecordData, null, TYPE_EAT_MONNEY, "", "_all", 0, i, format);
        saveRecord(stageRecordData, null, TYPE_EAT_MONNEY, "", "_stage", 0, i, format);
    }

    private static void saveShelfRecordData(String str, int i, String str2) {
        String str3 = saveAllPath + "/_shelf";
        File file = new File(str3);
        if (file.exists()) {
            HashMap<String, ShelfTradRecordData> hashMap = (HashMap) ObjectHelper.loadObject(str3);
            shelfTradRecordDataMap = hashMap;
            if (hashMap == null) {
                file.delete();
                shelfTradRecordDataMap = new HashMap<>();
            }
        } else {
            shelfTradRecordDataMap = new HashMap<>();
        }
        ShelfTradRecordData shelfTradRecordData = shelfTradRecordDataMap.get(str);
        if (shelfTradRecordData == null) {
            shelfTradRecordData = new ShelfTradRecordData();
            shelfTradRecordData.shelf = str;
            shelfTradRecordData.stageStartDate = str2;
            shelfTradRecordDataMap.put(str, shelfTradRecordData);
        }
        shelfTradRecordData.frequency++;
        long j = i;
        shelfTradRecordData.money += j;
        shelfTradRecordData.stageFrequency++;
        shelfTradRecordData.stateMoney += j;
        ObjectHelper.saveObject(shelfTradRecordDataMap, str3);
    }

    private static void saveRecord(RecordData recordData, String str, int i, String str2, String str3, int i2, int i3, String str4) {
        if (recordData == null || i2 == 0 || !str4.startsWith(recordData.date)) {
            recordData = crateRecordData(i2, str3, str4);
        }
        if (i != TYPE_EAT_MONNEY) {
            recordData.frequency++;
            recordData.money += i3;
        }
        TypeRecordData typeRecordData = recordData.TypeRecordDataMap.get(Integer.valueOf(i));
        if (typeRecordData == null) {
            typeRecordData = new TypeRecordData();
            typeRecordData.type = i;
            typeRecordData.name = str2;
            recordData.TypeRecordDataMap.put(Integer.valueOf(i), typeRecordData);
        }
        typeRecordData.frequency++;
        typeRecordData.money += i3;
        ObjectHelper.saveObject(recordData, saveAllPath + UsbFile.separator + (i2 > 0 ? str4.substring(0, i2) : "") + str3);
    }

    private static RecordData crateRecordData(int i, String str, String str2) {
        String substring = i > 0 ? str2.substring(0, i) : "";
        String str3 = saveAllPath + UsbFile.separator + substring + str;
        if (new File(str3).exists()) {
            return (RecordData) ObjectHelper.loadObject(str3);
        }
        RecordData recordData = new RecordData();
        recordData.date = substring;
        if (TextUtils.isEmpty(recordData.date)) {
            recordData.date = str2;
        }
        recordData.TypeRecordDataMap = new HashMap<>();
        return recordData;
    }

    public static RecordData getDateRecordData(String str) {
        String str2 = "";
        for (String str3 : str.split("-")) {
            if (str3.length() == 1) {
                str3 = "0" + str3;
            }
            str2 = str2 + str3;
        }
        String str4 = saveAllPath + UsbFile.separator + str2 + "_date";
        Loger.writeLog("UI", "getDateRecordData path:" + str4);
        if (new File(str4).exists()) {
            dateRecordData = (RecordData) ObjectHelper.loadObject(str4);
        }
        return dateRecordData;
    }

    public static RecordData getMonthRecordData(String str) {
        String str2 = "";
        for (String str3 : str.split("-")) {
            if (str3.length() == 1) {
                str3 = "0" + str3;
            }
            str2 = str2 + str3;
        }
        String str4 = saveAllPath + UsbFile.separator + str2 + "_month";
        if (new File(str4).exists()) {
            monthRecordData = (RecordData) ObjectHelper.loadObject(str4);
        }
        return monthRecordData;
    }

    public static RecordData getYearRecordData(String str) {
        String str2 = saveAllPath + UsbFile.separator + str.replaceAll("-", "") + "_year";
        if (new File(str2).exists()) {
            yearRecordData = (RecordData) ObjectHelper.loadObject(str2);
        }
        return yearRecordData;
    }

    public static RecordData getMachineRecordData() {
        if (machineRecordData == null) {
            String str = saveAllPath + "/_all";
            if (new File(str).exists()) {
                machineRecordData = (RecordData) ObjectHelper.loadObject(str);
            }
        }
        return machineRecordData;
    }

    public static RecordData getStageRecordData() {
        if (stageRecordData == null) {
            String str = saveAllPath + "/_stage";
            if (new File(str).exists()) {
                stageRecordData = (RecordData) ObjectHelper.loadObject(str);
            }
        }
        return stageRecordData;
    }

    public static HashMap<String, ShelfTradRecordData> getShelfTradRecordDataMap() {
        if (shelfTradRecordDataMap == null) {
            String str = saveAllPath + "/_shelf";
            if (new File(str).exists()) {
                shelfTradRecordDataMap = (HashMap) ObjectHelper.loadObject(str);
            }
        }
        return shelfTradRecordDataMap;
    }

    public static void deleteStageRecordData() {
        String str = saveAllPath + "/_stage";
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        RecordData recordData = new RecordData();
        recordData.money = 0L;
        recordData.frequency = 0;
        recordData.TypeRecordDataMap = new HashMap<>();
        recordData.date = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        ObjectHelper.saveObject(recordData, str);
        stageRecordData = recordData;
    }

    public static void deleteShelfStageRecordData(int i, int i2) {
        HashMap<String, ShelfTradRecordData> hashMap = shelfTradRecordDataMap;
        if (hashMap != null) {
            Iterator<Map.Entry<String, ShelfTradRecordData>> it = hashMap.entrySet().iterator();
            String format = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            while (it.hasNext()) {
                ShelfTradRecordData value = it.next().getValue();
                int intValue = Integer.valueOf(value.shelf).intValue();
                if (intValue >= i && intValue <= i2) {
                    value.stateMoney = 0L;
                    value.stageFrequency = 0;
                    value.stageStartDate = format;
                }
            }
            ObjectHelper.saveObject(shelfTradRecordDataMap, saveAllPath + "/_shelf");
        }
    }
}
