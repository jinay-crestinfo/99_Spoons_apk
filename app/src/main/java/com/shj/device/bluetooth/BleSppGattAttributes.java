package com.shj.device.bluetooth;

import java.util.HashMap;

/* loaded from: classes2.dex */
public class BleSppGattAttributes {
    public static String BLE_SPP_AT_Characteristic;
    public static String BLE_SPP_Notify_Characteristic;
    public static String BLE_SPP_Service;
    public static String BLE_SPP_Write_Characteristic;
    public static String CLIENT_CHARACTERISTIC_CONFIG;
    private static HashMap<String, String> attributes;

    static {
        HashMap<String, String> hashMap = new HashMap<>();
        attributes = hashMap;
        CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
        BLE_SPP_Service = "0000fee0-0000-1000-8000-00805f9b34fb";
        BLE_SPP_Notify_Characteristic = "0000fee1-0000-1000-8000-00805f9b34fb";
        BLE_SPP_Write_Characteristic = "0000fee2-0000-1000-8000-00805f9b34fb";
        BLE_SPP_AT_Characteristic = "0000fee3-0000-1000-8000-00805f9b34fb";
        hashMap.put("0000fee0-0000-1000-8000-00805f9b34fb", "BLE SPP Service");
        attributes.put(BLE_SPP_Notify_Characteristic, "BLE SPP Notify Characteristic");
        attributes.put(BLE_SPP_Write_Characteristic, "BLE SPP Write Characteristic");
        attributes.put(BLE_SPP_AT_Characteristic, "BLE SPP AT Characteristic");
    }

    public static String lookup(String str, String str2) {
        String str3 = attributes.get(str);
        return str3 == null ? str2 : str3;
    }
}
