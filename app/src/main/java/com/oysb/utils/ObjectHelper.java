package com.oysb.utils;


import android.util.Base64;

//import com.iflytek.speech.VoiceWakeuperAidl;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.UByte;


/* loaded from: classes2.dex */
public class ObjectHelper {
    static HashMap<String, Short> hexMap = new HashMap<>();



    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:51:0x001b -> B:12:0x003e). Please report as a decompilation issue!!! */
    public static void saveObject(Object obj, String filePath) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(obj);
        } catch (IOException e) {
            Loger.safe_inner_exception_catch(e);
        }
    }


    /* JADX WARN: Removed duplicated region for block: B:41:0x0057 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:48:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x004d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static Object loadObject(String filePath) {
        Object object = null;
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            object = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }


    public static List<byte[]> splitBytes(byte[] bArr, byte b) {
        int indexOfByte;
        ArrayList arrayList = new ArrayList();
        int indexOfByte2 = indexOfByte(bArr, b, 0);
        int i = -1;
        while (indexOfByte2 != -1) {
            if (indexOfByte2 == 0) {
                indexOfByte = indexOfByte(bArr, b, indexOfByte2 + 1);
            } else {
                int i2 = (indexOfByte2 - i) - 1;
                byte[] bArr2 = new byte[i2];
                updateBytes(bArr2, bArr, i + 1, 0, i2);
                arrayList.add(bArr2);
                indexOfByte = indexOfByte(bArr, b, indexOfByte2 + 1);
            }
            int i3 = indexOfByte;
            i = indexOfByte2;
            indexOfByte2 = i3;
        }
        if (i == -1) {
            arrayList.add(bArr);
        } else if (bArr.length - 1 > i) {
            int length = (bArr.length - 1) - i;
            byte[] bArr3 = new byte[length];
            updateBytes(bArr3, bArr, i + 1, 0, length);
            arrayList.add(bArr3);
        }
        return arrayList;
    }

    public static byte[] reverseBytes(byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[(bArr.length - i) - 1] = bArr[i];
        }
        return bArr2;
    }

    public static int indexOfByte(byte[] bArr, byte b, int i) {
        while (i < bArr.length) {
            if (bArr[i] == b) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static short computerXor(byte[] bArr, int i, int i2) {
        short s = 0;
        for (int i3 = i; i3 < i + i2; i3++) {
            s = (short) (s ^ bArr[i3]);
        }
        return (short) (s & 255);
    }

    public static byte[] int2Bytes16_2(int i, int i2) {
        byte[] Int2Bytes16_2 = Int2Bytes16_2(i);
        if (Int2Bytes16_2.length == i2) {
            return Int2Bytes16_2;
        }
        byte[] bArr = new byte[i2];
        updateBytes(bArr, Int2Bytes16_2, i2 - Int2Bytes16_2.length, Int2Bytes16_2.length);
        return bArr;
    }

    public static byte[] Int2Bytes16_2(int i) {
        if (i == 0) {
            return new byte[]{0};
        }
        if (i < 0) {
            i = ((short) i) & 255;
        }
        byte[] bArr = new byte[50];
        int i2 = 0;
        while (i > 0) {
            bArr[i2] = (byte) (i % 256);
            i /= 256;
            i2++;
        }
        byte[] bArr2 = new byte[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            bArr2[i3] = bArr[(i2 - i3) - 1];
        }
        return bArr2;
    }

    public static byte getByte16(int i) {
        int i2 = i & 255;
        return (byte) (-(i2 + ((i2 / 10) * 6)));
    }

    public static short[] Int2Short16_2(int i) {
        if (i == 0) {
            return new short[]{0};
        }
        if (i < 0) {
            i = ((short) i) & 255;
        }
        short[] sArr = new short[50];
        int i2 = 0;
        while (i > 0) {
            sArr[i2] = (short) (i % 256);
            i /= 256;
            i2++;
        }
        short[] sArr2 = new short[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            sArr2[i3] = sArr[(i2 - i3) - 1];
        }
        return sArr2;
    }

    public static short[] Long2Short16_2(long j) {
        if (j == 0) {
            return new short[]{0};
        }
        if (j < 0) {
            j = ((short) j) & 255;
        }
        short[] sArr = new short[50];
        int i = 0;
        while (j > 0) {
            sArr[i] = (short) (j % 256);
            j /= 256;
            i++;
        }
        short[] sArr2 = new short[i];
        for (int i2 = 0; i2 < i; i2++) {
            sArr2[i2] = sArr[(i - i2) - 1];
        }
        return sArr2;
    }

    public static byte[] updateBytes(byte[] bArr, int i, int i2, int i3) {
        short[] Int2Short16_2 = Int2Short16_2(i);
        for (int i4 = 0; i4 < Int2Short16_2.length; i4++) {
            bArr[(i3 - Int2Short16_2.length) + i2 + i4] = (byte) Int2Short16_2[i4];
        }
        return bArr;
    }

    public static byte[] updateBytes(byte[] bArr, long j, int i, int i2) {
        short[] Long2Short16_2 = Long2Short16_2(j);
        for (int i3 = 0; i3 < Long2Short16_2.length; i3++) {
            bArr[(i2 - Long2Short16_2.length) + i + i3] = (byte) Long2Short16_2[i3];
        }
        return bArr;
    }

    public static byte[] updateBytes(byte[] bArr, byte[] bArr2, int i, int i2) {
        System.arraycopy(bArr2, 0, bArr, i, i2);
        return bArr;
    }

    public static byte[] updateBytes(byte[] bArr, byte[] bArr2, int i, int i2, int i3) {
        System.arraycopy(bArr2, i, bArr, i2, i3);
        return bArr;
    }

    public static short[] updateShorts(short[] sArr, int i, int i2, int i3) {
        short[] Int2Short16_2 = Int2Short16_2(i);
        for (int i4 = 0; i4 < Int2Short16_2.length; i4++) {
            sArr[(i3 - Int2Short16_2.length) + i2 + i4] = Int2Short16_2[i4];
        }
        return sArr;
    }

    public static short[] updateShorts(short[] sArr, short[] sArr2, int i, int i2) {
        System.arraycopy(sArr2, 0, sArr, i, i2);
        return sArr;
    }

    public static byte[] bytesFromBytes(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        return bArr2;
    }

    public static int intFromBytes(byte[] bArr, int i, int i2) {
        int i3 = 0;
        for (int i4 = i; i4 < i + i2; i4++) {
            i3 = (i3 * 256) + ((short) (bArr[i4] & UByte.MAX_VALUE));
        }
        return i3;
    }

    public static long longFromBytes(byte[] bArr, int i, int i2) {
        long j = 0;
        for (int i3 = i; i3 < i + i2; i3++) {
            j = (j * 256) + ((short) (bArr[i3] & UByte.MAX_VALUE));
        }
        return j;
    }

    public static long longFromBytes_reverse(byte[] bArr, int i, int i2) {
        long j = 0;
        for (int i3 = (i2 + i) - 1; i3 >= i; i3--) {
            j = (j * 256) + ((short) (bArr[i3] & UByte.MAX_VALUE));
        }
        return j;
    }

    public static int intFromShorts(short[] sArr, int i, int i2) {
        int i3 = 0;
        for (int i4 = i; i4 < i + i2; i4++) {
            i3 = (i3 * 256) + sArr[i4];
        }
        return i3;
    }

    public static long longFromShorts(short[] sArr, int i, int i2) {
        long j = 0;
        for (int i3 = i; i3 < i + i2; i3++) {
            j = (j * 256) + sArr[i3];
        }
        return j;
    }

    public static byte[] shots2bytes(short[] sArr) {
        byte[] bArr = new byte[sArr.length];
        for (int i = 0; i < sArr.length; i++) {
            bArr[i] = (byte) sArr[i];
        }
        return bArr;
    }

    public static short[] bytes2shorts(byte[] bArr) {
        short[] sArr = new short[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            sArr[i] = (short) (bArr[i] & UByte.MAX_VALUE);
        }
        return sArr;
    }

    public static String hex2String(byte[] bArr) {
        return hex2String(bArr, bArr.length);
    }

    public static String hex2String(byte[] byteArray, int length) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int value = byteArray[i] & 0xFF;
            sb.append(hexArray[value >> 4]);
            sb.append(hexArray[value & 0x0F]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }


    public static String hex2String_nospace(byte[] byteArray) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            int value = byteArray[i] & 0xFF;
            sb.append(hexArray[value >> 4]);
            sb.append(hexArray[value & 0x0F]);
        }
        return sb.toString();
    }


    public static byte[] str2Bcd(String str) {
        int i;
        int i2;
        int length = str.length();
        if (length % 2 != 0) {
            str = "0" + str;
            length = str.length();
        }
        byte[] bArr = new byte[length];
        if (length >= 2) {
            length /= 2;
        }
        byte[] bArr2 = new byte[length];
        byte[] bytes = str.getBytes();
        for (int i3 = 0; i3 < str.length() / 2; i3++) {
            int i4 = i3 * 2;
            if (bytes[i4] >= 48 && bytes[i4] <= 57) {
                i = bytes[i4] - 48;
            } else {
                i = ((bytes[i4] >= 97 && bytes[i4] <= 122) ? bytes[i4] - 97 : bytes[i4] - 65) + 10;
            }
            int i5 = i4 + 1;
            if (bytes[i5] >= 48 && bytes[i5] <= 57) {
                i2 = bytes[i5] - 48;
            } else {
                i2 = ((bytes[i5] >= 97 && bytes[i5] <= 122) ? bytes[i5] - 97 : bytes[i5] - 65) + 10;
            }
            bArr2[i3] = (byte) ((i << 4) + i2);
        }
        return bArr2;
    }

    public static String hex2String(byte b) {
        String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
        if (hexString.length() >= 2) {
            return hexString;
        }
        return "0" + hexString;
    }

    public static int string2Short(String str) {
        // Initialize hexMap if it's null or empty
        if (hexMap == null || hexMap.isEmpty()) {
            initializeHexMap();
        }

        // Remove "0x" or "0X" prefix if present
        if (str.startsWith("0x") || str.startsWith("0X")) {
            str = str.substring(2);
        }

        int result = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            Short value = hexMap.get(Character.toString(c).toUpperCase());
            if (value == null) {
                // Handle invalid characters
                throw new IllegalArgumentException("Invalid hexadecimal character: " + c);
            }
            result = (result * 16) + value;
        }
        return result;
    }

    // Method to initialize hexMap
    private static void initializeHexMap() {
        hexMap = new HashMap<>();
        hexMap.put("0", (short) 0);
        hexMap.put("1", (short) 1);
        hexMap.put("2", (short) 2);
        hexMap.put("3", (short) 3);
        hexMap.put("4", (short) 4);
        hexMap.put("5", (short) 5);
        hexMap.put("6", (short) 6);
        hexMap.put("7", (short) 7);
        hexMap.put("8", (short) 8);
        hexMap.put("9", (short) 9);
        hexMap.put("A", (short) 10);
        hexMap.put("B", (short) 11);
        hexMap.put("C", (short) 12);
        hexMap.put("D", (short) 13);
        hexMap.put("E", (short) 14);
        hexMap.put("F", (short) 15);
    }



    public static String Object2Base64String(Serializable obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            String base64String = Base64.encodeToString(data, Base64.DEFAULT);
            return base64String;
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e2) {
                    Loger.safe_inner_exception_catch(e2);
                }
            }
        }
    }




    public static String ary2String(Object[] objArr) {
        String str = "";
        if (objArr == null) {
            return "";
        }
        for (Object obj : objArr) {
            str = obj == null ? str + "null;" : str + obj.toString() ;
        }
        return str;
    }

    private byte[] hexstring2Bytes(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            bArr[i2] = (byte) ((((byte) (Character.digit(str.charAt(i), 16) & 255)) << 4) | ((byte) (Character.digit(str.charAt(i + 1), 16) & 255)));
            i += 2;
        }
        return bArr;
    }
}
